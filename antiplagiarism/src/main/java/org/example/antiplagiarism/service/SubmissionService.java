package org.example.antiplagiarism.service;

import jakarta.validation.ValidationException;
import org.example.analysisservice.CheckPlagiarismRequest;
import org.example.analysisservice.CheckPlagiarismResponse;
import org.example.antiplagiarism.config.RabbitMQConfig;
import org.example.antiplagiarism.dto.WorkSubmissionRequest;
import org.example.antiplagiarism.dto.WorkSubmissionResponse;
import org.example.antiplagiarism.events.PlagiarismCheckCompletedEvent;
import org.example.antiplagiarism.events.WorkDeletedEvent;
import org.example.antiplagiarism.exception.ResourceNotFoundException;
import org.example.antiplagiarism.events.WorkSubmittedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SubmissionService {

    private final Map<String, WorkSubmissionResponse> submissions = new ConcurrentHashMap<>();
    private final Set<String> submissionKeys = ConcurrentHashMap.newKeySet();
    private final RabbitTemplate rabbitTemplate;

    public SubmissionService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public WorkSubmissionResponse submitWork(WorkSubmissionRequest request) {
        String key = request.studentId() + "_" + request.title();
        if (submissionKeys.contains(key)) {
            throw new ValidationException("Работа с таким названием уже была загружена этим студентом");
        }

        String workId = UUID.randomUUID().toString();
        WorkSubmissionResponse response = new WorkSubmissionResponse(
                workId,
                request.title(),
                request.studentId(),
                LocalDateTime.now()
        );

        submissions.put(workId, response);
        submissionKeys.add(key);

        sendWorkSubmittedEvent(response);

        return response;
    }

    public void deleteWork(String workId) {
        WorkSubmissionResponse response = submissions.get(workId);
        if (response == null) {
            throw new ValidationException("Такой работы нет");
        }

        String key = response.getStudentId() + "_" + response.getTitle();
        submissions.remove(response.getWorkId());
        submissionKeys.remove(key);

        sendWorkDeletedEvent(response);
    }

    public WorkSubmissionResponse getWorkById(String workId) {
        WorkSubmissionResponse work = submissions.get(workId);
        if (work == null) {
            throw new ResourceNotFoundException("Работа", workId);
        }
        return work;
    }

    public List<WorkSubmissionResponse> getAllWorks() {
        return new ArrayList<>(submissions.values());
    }

    private void sendWorkSubmittedEvent(WorkSubmissionResponse response) {
        WorkSubmittedEvent event = new WorkSubmittedEvent(
                response.getWorkId(),
                response.getTitle(),
                response.getStudentId(),
                response.getWorkedAt()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_WORK_SUBMITTED, event);
    }

    private void sendWorkDeletedEvent(WorkSubmissionResponse response) {
        WorkDeletedEvent event = new WorkDeletedEvent(
                response.getWorkId(),
                response.getTitle(),
                response.getStudentId(),
                response.getWorkedAt());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_WORK_DELETED, event);
    }

}
