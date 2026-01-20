package org.example.antiplagiarism.service;

import org.example.antiplagiarism.entity.Work;
import org.example.antiplagiarism.entity.enums.WorkStatus;
import org.example.antiplagiarism.events.PlagiarismCheckCompletedEvent;
import org.example.antiplagiarism.repository.WorkRepository;
import org.example.analysisservice.CheckPlagiarismResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PlagiarismCheckService {

    private static final Logger log = LoggerFactory.getLogger(PlagiarismCheckService.class);

    private final WorkRepository workRepository;
    private final PlagiarismAnalysisClient plagiarismAnalysisClient;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.fanout.plagiarism.name:plagiarism-fanout}")
    private String fanoutExchangeName;

    public PlagiarismCheckService(WorkRepository workRepository,
                                  PlagiarismAnalysisClient plagiarismAnalysisClient,
                                  RabbitTemplate rabbitTemplate) {
        this.workRepository = workRepository;
        this.plagiarismAnalysisClient = plagiarismAnalysisClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    public CheckPlagiarismResponse checkWork(Work work) {

        if (work.getStatus() != WorkStatus.SUBMITTED) {
            throw new IllegalStateException("Work status must be SUBMITTED for plagiarism check");
        }

        CheckPlagiarismResponse response;
        boolean grpcSuccess = true;

        try {
            response = plagiarismAnalysisClient.checkPlagiarism(work.getId(), work.getContent());
        } catch (RuntimeException e) {
            grpcSuccess = false;
            response = CheckPlagiarismResponse.newBuilder()
                    .setWorkId(work.getId())
                    .setSimilarityScore(0)
                    .setIsFlagged(false)
                    .setCheckedAt(Instant.now().toString())
                    .build();
            log.warn("Plagiarism service unavailable: {}. Using fallback.", e.getMessage());
        }

        work.setSimilarityScore((double) response.getSimilarityScore());
        work.setStatus(response.getIsFlagged() ? WorkStatus.PLAGIARISM_FAILED : WorkStatus.ASSIGNED);
        workRepository.save(work);

        if (grpcSuccess) {
            PlagiarismCheckCompletedEvent event = new PlagiarismCheckCompletedEvent(
                    work.getId(),
                    response.getSimilarityScore(),
                    response.getIsFlagged(),
                    Instant.now().toString()
            );
            rabbitTemplate.convertAndSend(fanoutExchangeName, "", event);
            log.info("PlagiarismCheckCompletedEvent published: workId={}, score={}, flagged={}",
                    work.getId(), response.getSimilarityScore(), response.getIsFlagged());
        }

        return response;
    }
}
