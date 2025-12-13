package org.example.antiplagiarism.dto;

import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.Objects;

@Relation(collectionRelation = "results", itemRelation = "result")
public class PlagiarismCheckResult {
    private final String workId;
    private final int similarityScore;
    private final boolean isFlagged;
    private final LocalDateTime checkedAt;

    public PlagiarismCheckResult(String workId, int similarityScore, boolean isFlagged, LocalDateTime checkedAt) {
        this.workId = workId;
        this.similarityScore = similarityScore;
        this.isFlagged = isFlagged;
        this.checkedAt = checkedAt;
    }

    public String getWorkId() {
        return workId;
    }

    public int getSimilarityScore() {
        return similarityScore;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PlagiarismCheckResult that = (PlagiarismCheckResult) object;
        return similarityScore == that.similarityScore && isFlagged == that.isFlagged && Objects.equals(workId, that.workId) && Objects.equals(checkedAt, that.checkedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workId, similarityScore, isFlagged, checkedAt);
    }
}