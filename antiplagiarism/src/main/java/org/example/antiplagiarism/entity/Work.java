package org.example.antiplagiarism.entity;

import jakarta.persistence.*;
import org.example.antiplagiarism.entity.enums.WorkStatus;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "works")
public class Work extends BaseEntity {

    private String id;

    private String title;

    private String content;

    private LocalDateTime submittedAt;

    private User student;

    private Double similarityScore;

    private User reviewer;

    private WorkStatus status;

    protected Work() {}

    public Work(String title, String content, User student) {
        this.title = title;
        this.content = content;
        this.submittedAt = LocalDateTime.now();
        this.student = student;
        this.status = WorkStatus.SUBMITTED;
    }

    @Column(name = "title", nullable = false)
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    @Column(name = "submitted_at", nullable = false)
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }

    @Column(name = "originality")
    public Double getSimilarityScore() { return similarityScore; }
    public void setSimilarityScore(Double originality) { this.similarityScore = originality; }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    public User getReviewer() { return reviewer; }
    public void setReviewer(User reviewer) { this.reviewer = reviewer; }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public WorkStatus getStatus() { return status; }
    public void setStatus(WorkStatus status) { this.status = status; }
}
