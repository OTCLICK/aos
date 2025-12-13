package org.example.antiplagiarism.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.example.antiplagiarism.dto.WorkSubmissionRequest;
import org.example.antiplagiarism.dto.WorkSubmissionResponse;
import org.example.antiplagiarism.service.SubmissionService;

import java.util.List;
import java.util.Map;

@DgsComponent
public class WorkDataFetcher {

    private final SubmissionService submissionService;

    public WorkDataFetcher(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @DgsQuery
    public WorkSubmissionResponse workById(@InputArgument String workId) {
        return submissionService.getWorkById(workId);
    }

    @DgsQuery
    public List<WorkSubmissionResponse> allWorks() {
        return submissionService.getAllWorks();
    }

    @DgsMutation
    public WorkSubmissionResponse submitWork(@InputArgument("input") Map<String, String> input) {
        WorkSubmissionRequest req = new WorkSubmissionRequest(
                input.get("title"),
                input.get("studentId"),
                input.get("fileContent")
        );
        return submissionService.submitWork(req);
    }
}