package org.example.antiplagiarism.service;

import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.analysisservice.AnalysisServiceGrpc;
import org.example.analysisservice.CheckPlagiarismRequest;
import org.example.analysisservice.CheckPlagiarismResponse;
import org.springframework.stereotype.Service;

@Service
public class PlagiarismAnalysisClient {

    @GrpcClient("analysis-service")
    private AnalysisServiceGrpc.AnalysisServiceBlockingStub analysisStub;

    public CheckPlagiarismResponse checkPlagiarism(String workId, String fileContent) {
        CheckPlagiarismRequest request = CheckPlagiarismRequest.newBuilder()
                .setWorkId(workId)
                .setFileContent(fileContent)
                .build();

        try {
            return analysisStub.checkPlagiarism(request);
        } catch (StatusRuntimeException e) {
            throw new RuntimeException("Plagiarism analysis service is unavailable", e);
        }
    }
}