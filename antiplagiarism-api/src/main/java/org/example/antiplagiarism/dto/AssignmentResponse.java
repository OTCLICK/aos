package org.example.antiplagiarism.dto;

import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.Objects;

@Relation(collectionRelation = "assignments", itemRelation = "assignment")
public class AssignmentResponse {
    private final String workId;
    private final String reviewId;
    private final LocalDateTime assignedAt;

    public AssignmentResponse(String workId, String reviewId, LocalDateTime assignedAt) {
        this.workId = workId;
        this.reviewId = reviewId;
        this.assignedAt = assignedAt;
    }

    public String getWorkId() {
        return workId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        AssignmentResponse that = (AssignmentResponse) object;
        return Objects.equals(workId, that.workId) && Objects.equals(reviewId, that.reviewId) && Objects.equals(assignedAt, that.assignedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workId, reviewId, assignedAt);
    }
}
