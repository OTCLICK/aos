package org.example.plagiarismanalysisservice;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.analysisservice.AnalysisServiceGrpc;
import org.example.analysisservice.CheckPlagiarismRequest;
import org.example.analysisservice.CheckPlagiarismResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@GrpcService
public class AnalysisServiceImpl extends AnalysisServiceGrpc.AnalysisServiceImplBase {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final Random random = new Random();

    @Override
    public void checkPlagiarism(CheckPlagiarismRequest request, StreamObserver<CheckPlagiarismResponse> responseObserver) {
        try {
            int similarityScore = random.nextInt(101);
            boolean isFlagged = similarityScore > 70;

            String checkedAt = LocalDateTime.now().format(FORMATTER);

            CheckPlagiarismResponse response = CheckPlagiarismResponse.newBuilder()
                    .setWorkId(request.getWorkId())
                    .setSimilarityScore(similarityScore)
                    .setIsFlagged(isFlagged)
                    .setCheckedAt(checkedAt)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Error in work analysis")
                            .withCause(e)
                            .asRuntimeException()
            );
        }
    }
}

