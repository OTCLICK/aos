package org.example.antiplagiarism.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.example.antiplagiarism.dto.GradeRequest;
import org.example.antiplagiarism.dto.GradeResponse;
import org.example.antiplagiarism.dto.WorkSubmissionResponse;
import org.example.antiplagiarism.service.GradingService;

import java.util.List;
import java.util.Map;

@DgsComponent
public class GradeDataFetcher {

    private final GradingService gradingService;

    public GradeDataFetcher(GradingService gradingService) {
        this.gradingService = gradingService;
    }

    @DgsQuery
    public GradeResponse gradeByWorkId(@InputArgument String workId) {
        return gradingService.getGradeByWorkId(workId);
    }

    @DgsMutation
    public GradeResponse gradeWork(@InputArgument String workId, @InputArgument("input") Map<String, String> input) {
        GradeRequest req = new GradeRequest(
                Integer.parseInt(input.get("grade")),
                input.get("reviewerId")
        );
        return gradingService.gradeWork(workId, req);
    }

    @DgsQuery
    public List<GradeResponse> allGrades() {
        return gradingService.getAllGrades();
    }
}
