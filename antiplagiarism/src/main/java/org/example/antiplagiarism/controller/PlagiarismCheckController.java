package org.example.antiplagiarism.controller;

import org.example.antiplagiarism.entity.Work;
import org.example.antiplagiarism.repository.WorkRepository;
import org.example.antiplagiarism.service.PlagiarismCheckService;
import org.example.analysisservice.CheckPlagiarismResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/works")
public class PlagiarismCheckController {

    private final WorkRepository workRepository;
    private final PlagiarismCheckService plagiarismCheckService;

    public PlagiarismCheckController(WorkRepository workRepository,
                                     PlagiarismCheckService plagiarismCheckService) {
        this.workRepository = workRepository;
        this.plagiarismCheckService = plagiarismCheckService;
    }

    @PutMapping("/{workId}/check-plagiarism")
    public ResponseEntity<String> checkPlagiarism(@PathVariable String workId) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new IllegalArgumentException("Work not found: " + workId));

        CheckPlagiarismResponse response = plagiarismCheckService.checkWork(work);

        return ResponseEntity.ok(
                String.format("Verification complete. Similarity: %d%% (flag: %s)",
                        response.getSimilarityScore(),
                        response.getIsFlagged() ? "Yes" : "No")
        );
    }
}
