package com.BTA.SmartPrep.domain.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "Submissions")
public class Submission {
    public Submission() {
    }

    public Submission(String submissionId, SolutionRating solutionRating, LocalDateTime submittedAt, String answer, User user, Problem problem) {
        this.submissionId = submissionId;
        this.solutionRating = solutionRating;
        this.submittedAt = submittedAt;
        this.answer = answer;
        this.user = user;
        this.problem = problem;
    }

    @Id //This Annotation Says this is an ID
    @GeneratedValue(strategy = GenerationType.UUID) //Creates ID for us, using UUID
    @Column(name = "submission_ID",updatable = false,nullable = false) //col name from DB, not Updatable, cannot be null
    private String submissionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rating")
    private SolutionRating solutionRating;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    @Column(name = "answer", columnDefinition = "TEXT")
    private String answer;

    @ManyToOne
    @JoinColumn(name = "user_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "problem_ID")
    private Problem problem;

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public SolutionRating getSolutionRating() {
        return solutionRating;
    }

    public void setSolutionRating(SolutionRating solutionRating) {
        this.solutionRating = solutionRating;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Submission that = (Submission) o;
        return Objects.equals(submissionId, that.submissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(submissionId);
    }

    @Override
    public String toString() {
        return "Submission{" +
                "submissionId=" + submissionId +
                ", solutionRating=" + solutionRating +
                ", submittedAt=" + submittedAt +
                ", answer='" + answer + '\'' +
                ", user=" + user +
                ", problem=" + problem +
                '}';
    }
}
