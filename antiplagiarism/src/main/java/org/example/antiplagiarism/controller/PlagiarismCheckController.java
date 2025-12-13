package org.example.antiplagiarism.controller;

import com.google.protobuf.Timestamp;
import org.example.antiplagiarism.events.PlagiarismCheckCompletedEvent;
import org.example.analysisservice.AnalysisServiceGrpc;
import org.example.analysisservice.CheckPlagiarismRequest;
import org.example.analysisservice.CheckPlagiarismResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;

import java.time.Instant;

@RestController
@RequestMapping("/api/works")
public class PlagiarismCheckController {

    @GrpcClient("analysis-service")
    private AnalysisServiceGrpc.AnalysisServiceBlockingStub analysisStub;

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.fanout.plagiarism.name:plagiarism-fanout}")
    private String fanoutExchangeName;

    private static final Logger log = LoggerFactory.getLogger(PlagiarismCheckController.class);

    public PlagiarismCheckController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/{workId}/check-plagiarism")
    public ResponseEntity<String> checkPlagiarism(@PathVariable String workId) {

        String fileContent = "Заглушка содержимого работы, потом с базы подберу";

        CheckPlagiarismResponse response;
        boolean grpcSuccess = true;

        try {
            CheckPlagiarismRequest request = CheckPlagiarismRequest.newBuilder()
                    .setWorkId(workId)
                    .setFileContent(fileContent)
                    .build();

            response = analysisStub.checkPlagiarism(request);

        } catch (Exception e) {
            log.warn("gRPC plagiarism detector недоступен: {}. Use fallback.", e.toString());

            grpcSuccess = false;

            response = CheckPlagiarismResponse.newBuilder()
                    .setWorkId(workId)
                    .setSimilarityScore(0)
                    .setIsFlagged(false)
                    .setCheckedAt(String.valueOf(Timestamp.newBuilder()
                            .setSeconds(Instant.now().getEpochSecond())
                            .build()))
                    .build();
        }

        if (grpcSuccess) {
            PlagiarismCheckCompletedEvent event = new PlagiarismCheckCompletedEvent(
                    response.getWorkId(),
                    response.getSimilarityScore(),
                    response.getIsFlagged(),
                    Instant.now().toString()
            );

            rabbitTemplate.convertAndSend(fanoutExchangeName, "", event);

            log.info("Event published PlagiarismCheckCompletedEvent: workId={}, score={}, flagged={}",
                    workId, response.getSimilarityScore(), response.getIsFlagged());
        } else {
            log.info("Event not published - plagiarism-analysis-service was unavailable");
        }

        return ResponseEntity.ok(
                String.format("Verification complete. Similarity: %d%% (flag: %s)",
                        response.getSimilarityScore(),
                        response.getIsFlagged() ? "Yes" : "No")
        );
    }
}