package org.example.antiplagiarism.service;

import jakarta.validation.ValidationException;
import org.example.antiplagiarism.config.RabbitMQConfig;
import org.example.antiplagiarism.dto.WorkSubmissionRequest;
import org.example.antiplagiarism.dto.WorkSubmissionResponse;
import org.example.antiplagiarism.entity.User;
import org.example.antiplagiarism.entity.enums.UserRole;
import org.example.antiplagiarism.entity.Work;
import org.example.antiplagiarism.events.WorkDeletedEvent;
import org.example.antiplagiarism.events.WorkSubmittedEvent;
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
public class SubmissionService {

    private final WorkRepository workRepository;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private final GradeRepository gradeRepository;

    public SubmissionService(WorkRepository workRepository,
                             UserRepository userRepository,
                             RabbitTemplate rabbitTemplate, GradeRepository gradeRepository) {
        this.workRepository = workRepository;
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.gradeRepository = gradeRepository;
    }

    public WorkSubmissionResponse submitWork(WorkSubmissionRequest request) {
        User student = userRepository.findById(request.studentId())
                .orElseThrow(() -> new ValidationException("Студент не найден: " + request.studentId()));

        if (student.getRole() != UserRole.STUDENT) {
            throw new ValidationException("Пользователь не является студентом");
        }

        if (workRepository.existsByStudentIdAndTitle(student.getId(), request.title())) {
            throw new ValidationException("Работа с таким названием уже была загружена этим студентом");
        }

        Work work = new Work(request.title(), request.fileContent(), student);
        Work saved = workRepository.save(work);

        sendWorkSubmittedEvent(saved);

        return new WorkSubmissionResponse(
                saved.getId(),
                saved.getTitle(),
                student.getEmail(),
                saved.getSubmittedAt(),
                saved.getStatus().name(),
                saved.getSimilarityScore(),
                null
        );
    }

    public void deleteWork(String workId) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new ValidationException("Такой работы нет"));

        sendWorkDeletedEvent(work);
        gradeRepository.deleteByWorkId(workId);
        workRepository.deleteById(workId);    }

    public WorkSubmissionResponse getWorkById(String workId) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new ResourceNotFoundException("Работа", workId));

        String reviewerEmail = (work.getReviewer() != null)
                ? work.getReviewer().getEmail()
                : null;

        return new WorkSubmissionResponse(
                work.getId(),
                work.getTitle(),
                work.getStudent().getEmail(),
                work.getSubmittedAt(),
                work.getStatus().name(),
                work.getSimilarityScore(),
                reviewerEmail
        );
    }

    public List<WorkSubmissionResponse> getAllWorks() {
        return workRepository.findAll().stream()
                .map(work -> {
                    String reviewerEmail = (work.getReviewer() != null)
                            ? work.getReviewer().getEmail()
                            : null;
                    return new WorkSubmissionResponse(
                            work.getId(),
                            work.getTitle(),
                            work.getStudent().getEmail(),
                            work.getSubmittedAt(),
                            work.getStatus().name(),
                            work.getSimilarityScore(),
                            reviewerEmail
                    );
                })
                .collect(Collectors.toList());
    }

    private void sendWorkSubmittedEvent(Work work) {
        WorkSubmittedEvent event = new WorkSubmittedEvent(
                work.getId(),
                work.getTitle(),
                work.getStudent().getId(),
                work.getSubmittedAt()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_WORK_SUBMITTED, event);
    }

    private void sendWorkDeletedEvent(Work work) {
        WorkDeletedEvent event = new WorkDeletedEvent(
                work.getId(),
                work.getTitle(),
                work.getStudent().getEmail(),
                work.getSubmittedAt()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_WORK_DELETED, event);
    }
}