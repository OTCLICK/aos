package org.example.antiplagiarism.service;

import org.example.antiplagiarism.config.RabbitMQConfig;
import org.example.antiplagiarism.dto.GradeRequest;
import org.example.antiplagiarism.dto.GradeResponse;
import org.example.antiplagiarism.events.GradeAssignedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class GradingService {

    private final Map<String, GradeResponse> grades = new ConcurrentHashMap<>();
    private final SubmissionService submissionService;
    private final RabbitTemplate rabbitTemplate;

    public GradingService(SubmissionService submissionService, RabbitTemplate rabbitTemplate) {
        this.submissionService = submissionService;
        this.rabbitTemplate = rabbitTemplate;
    }

    public GradeResponse gradeWork(String workId, GradeRequest request) {
        submissionService.getWorkById(workId);

        GradeResponse grade = new GradeResponse(
                workId,
                request.grade(),
                LocalDateTime.now()
        );
        grades.put(workId, grade);

        sendGradeAssignedEvent(grade, request.reviewerId());

        return grade;
    }

    public GradeResponse getGradeByWorkId(String workId) {
        submissionService.getWorkById(workId);

        GradeResponse grade = grades.get(workId);
        if (grade == null) {
            throw new RuntimeException("Оценка для работы с ID " + workId + " не найдена");
        }
        return grade;
    }

    public List<GradeResponse> getAllGrades() {
        return new ArrayList<>(grades.values());
    }

    private void sendGradeAssignedEvent(GradeResponse grade, String reviewerId) {
        GradeAssignedEvent event = new GradeAssignedEvent(
                grade.getWorkId(),
                grade.getGrade(),
                reviewerId,
                grade.getGradedAt()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_GRADE_ASSIGNED, event);
    }

}
