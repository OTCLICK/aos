package org.example.antiplagiarism.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.Objects;

@Relation(collectionRelation = "grades", itemRelation = "grade")
public class GradeResponse {
    private final String workId;
    private final int grade;
    private final LocalDateTime gradedAt;

    public GradeResponse(String workId, int grade, LocalDateTime gradedAt) {
        this.workId = workId;
        this.grade = grade;
        this.gradedAt = gradedAt;
    }

    public String getWorkId() {
        return workId;
    }

    public int getGrade() {
        return grade;
    }

    public LocalDateTime getGradedAt() {
        return gradedAt;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        GradeResponse that = (GradeResponse) object;
        return grade == that.grade && Objects.equals(workId, that.workId) && Objects.equals(gradedAt, that.gradedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workId, grade, gradedAt);
    }
}

