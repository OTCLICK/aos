package org.example.antiplagiarism.service;

import org.example.antiplagiarism.config.RabbitMQConfig;
import org.example.antiplagiarism.dto.GradeRequest;
import org.example.antiplagiarism.dto.GradeResponse;
import org.example.antiplagiarism.entity.Grade;
import org.example.antiplagiarism.entity.User;
import org.example.antiplagiarism.entity.enums.UserRole;
import org.example.antiplagiarism.entity.Work;
import org.example.antiplagiarism.entity.enums.WorkStatus;
import org.example.antiplagiarism.events.GradeAssignedEvent;
import org.example.antiplagiarism.exception.ResourceNotFoundException;
import org.example.antiplagiarism.repository.GradeRepository;
import org.example.antiplagiarism.repository.UserRepository;
import org.example.antiplagiarism.repository.WorkRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GradingService {

    private final GradeRepository gradeRepository;
    private final WorkRepository workRepository;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    public GradingService(GradeRepository gradeRepository,
                          WorkRepository workRepository,
                          UserRepository userRepository,
                          RabbitTemplate rabbitTemplate) {
        this.gradeRepository = gradeRepository;
        this.workRepository = workRepository;
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public GradeResponse gradeWork(String workId, GradeRequest request) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new ResourceNotFoundException("Работа", workId));

        User reviewer = userRepository.findById(request.reviewerId())
                .orElseThrow(() -> new IllegalArgumentException("Проверяющий не найден: " + request.reviewerId()));

        if (reviewer.getRole() != UserRole.REVIEWER) {
            throw new IllegalArgumentException("Пользователь не является проверяющим");
        }

        if (work.getStatus() != WorkStatus.ASSIGNED) {
            throw new IllegalStateException("Нельзя оценить работу в статусе: " + work.getStatus());
        }

        Grade grade = new Grade(
                work, reviewer, request.grade(), null
        );
        Grade savedGrade = gradeRepository.save(grade);

        work.setStatus(WorkStatus.GRADED);
        work.setReviewer(reviewer);
        workRepository.save(work);

        sendGradeAssignedEvent(savedGrade, reviewer.getEmail());

        return new GradeResponse(
                savedGrade.getWork().getId(),
                savedGrade.getScore(),
                savedGrade.getGradedAt()
        );
    }

    public GradeResponse getGradeByWorkId(String workId) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new ResourceNotFoundException("Работа", workId));

        return gradeRepository.findByWorkId(workId)
                .map(g -> new GradeResponse(g.getWork().getId(), g.getScore(), g.getGradedAt()))
                .orElseThrow(() -> new RuntimeException("Оценка для работы с ID " + workId + " не найдена"));
    }

    public List<GradeResponse> getAllGrades() {
        return gradeRepository.findAll().stream()
                .map(g -> new GradeResponse(g.getWork().getId(), g.getScore(), g.getGradedAt()))
                .collect(Collectors.toList());
    }

    private void sendGradeAssignedEvent(org.example.antiplagiarism.entity.Grade grade, String reviewerEmail) {
        GradeAssignedEvent event = new GradeAssignedEvent(
                grade.getWork().getId(),
                grade.getScore(),
                reviewerEmail,
                grade.getGradedAt()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_GRADE_ASSIGNED, event);
    }
}