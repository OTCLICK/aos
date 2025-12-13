package org.example.antiplagiarism.dto;

import java.time.LocalDateTime;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "works", itemRelation = "work")
public class WorkSubmissionResponse extends RepresentationModel<WorkSubmissionResponse> {

    private final String workId;
    private final String title;
    private final String studentId;
    private final LocalDateTime workedAt;

    public WorkSubmissionResponse(String workId, String title, String studentId, LocalDateTime workedAt) {
        this.workId = workId;
        this.title = title;
        this.studentId = studentId;
        this.workedAt = workedAt;
    }

    public String getWorkId() { return workId; }
    public String getTitle() { return title; }
    public String getStudentId() { return studentId; }
    public LocalDateTime getWorkedAt() { return workedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkSubmissionResponse that = (WorkSubmissionResponse) o;
        return java.util.Objects.equals(workId, that.workId) &&
                java.util.Objects.equals(title, that.title) &&
                java.util.Objects.equals(studentId, that.studentId) &&
                java.util.Objects.equals(workedAt, that.workedAt);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(workId, title, studentId, workedAt);
    }
}