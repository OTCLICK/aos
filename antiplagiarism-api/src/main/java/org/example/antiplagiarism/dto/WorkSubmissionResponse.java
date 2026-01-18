package org.example.antiplagiarism.dto;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "works", itemRelation = "work")
public class WorkSubmissionResponse extends RepresentationModel<WorkSubmissionResponse> {

    private final String workId;
    private final String title;
    private final String studentId;
    private final LocalDateTime submittedAt;
    private final String status;
    private final Double similarityScore;
    private final String reviewerId;

    public WorkSubmissionResponse(String workId, String title, String studentId, LocalDateTime submittedAt, String status, Double similarityScore, String reviewerId) {
        this.workId = workId;
        this.title = title;
        this.studentId = studentId;
        this.submittedAt = submittedAt;
        this.status = status;
        this.similarityScore = similarityScore;
        this.reviewerId = reviewerId;
    }

    public String getWorkId() {
        return workId;
    }

    public String getTitle() {
        return title;
    }

    public String getStudentId() {
        return studentId;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public String getStatus() {
        return status;
    }

    public Double getSimilarityScore() {
        return similarityScore;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WorkSubmissionResponse response = (WorkSubmissionResponse) o;
        return Objects.equals(workId, response.workId) && Objects.equals(title, response.title) && Objects.equals(studentId, response.studentId) && Objects.equals(submittedAt, response.submittedAt) && Objects.equals(status, response.status) && Objects.equals(similarityScore, response.similarityScore) && Objects.equals(reviewerId, response.reviewerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), workId, title, studentId, submittedAt, status, similarityScore, reviewerId);
    }
}