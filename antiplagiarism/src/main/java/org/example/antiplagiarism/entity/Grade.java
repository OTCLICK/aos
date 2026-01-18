package org.example.antiplagiarism.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "grades")
public class Grade extends BaseEntity {

    private Work work;

    private User reviewer;

    private Integer score;

    private String comment;

    private LocalDateTime gradedAt;

    protected Grade() {}

    public Grade(Work work, User reviewer, Integer score, String comment) {
        this.work = work;
        this.reviewer = reviewer;
        this.score = score;
        this.comment = comment;
        this.gradedAt = LocalDateTime.now();
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_id", nullable = false, unique = true)
    public Work getWork() { return work; }
    public void setWork(Work work) { this.work = work; }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    public User getReviewer() { return reviewer; }
    public void setReviewer(User reviewer) { this.reviewer = reviewer; }

    @Column(name = "score", nullable = false)
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    @Column(name = "comment", columnDefinition = "TEXT")
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    @Column(name = "graded_at", nullable = false)
    public LocalDateTime getGradedAt() { return gradedAt; }
    public void setGradedAt(LocalDateTime gradedAt) { this.gradedAt = gradedAt; }
}
