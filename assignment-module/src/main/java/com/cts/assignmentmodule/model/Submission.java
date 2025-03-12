package com.cts.assignmentmodule.model;
import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
@Entity
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID submissionId;
    @Column(nullable = false)
    private int userId;

    @ManyToOne
    @JoinColumn(name = "assignmentId", nullable = false)
    @JsonIgnore
    private Assignment assignment;

    @Lob
    private byte[] answerUpload;

    private Integer obtainedMarks;

    @Column(nullable = false)
    private LocalDateTime submittedAt;

	/**
	 * @return the submissionId
	 */
	public UUID getSubmissionId() {
		return submissionId;
	}

	/**
	 * @param submissionId the submissionId to set
	 */
	public void setSubmissionId(UUID submissionId) {
		this.submissionId = submissionId;
	}

	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return the assignment
	 */
	public Assignment getAssignment() {
		return assignment;
	}

	/**
	 * @param assignment the assignment to set
	 */
	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	/**
	 * @return the answerUpload
	 */
	public byte[] getAnswerUpload() {
		return answerUpload;
	}

	/**
	 * @param answerUpload the answerUpload to set
	 */
	public void setAnswerUpload(byte[] answerUpload) {
		this.answerUpload = answerUpload;
	}

	/**
	 * @return the obtainedMarks
	 */
	public int getObtainedMarks() {
		return obtainedMarks;
	}

	/**
	 * @param obtainedMarks the obtainedMarks to set
	 */
	public void setObtainedMarks(Integer obtainedMarks) {
		this.obtainedMarks = obtainedMarks;
	}

	/**
	 * @return the submittedAt
	 */
	public LocalDateTime getSubmittedAt() {
		return submittedAt;
	}

	/**
	 * @param submittedAt the submittedAt to set
	 */
	public void setSubmittedAt(LocalDateTime submittedAt) {
		this.submittedAt = submittedAt;
	}

	public Submission(UUID submissionId, int userId, Assignment assignment, byte[] answerUpload, Integer obtainedMarks,
			LocalDateTime submittedAt) {
		super();
		this.submissionId = submissionId;
		this.userId = userId;
		this.assignment = assignment;
		this.answerUpload = answerUpload;
		this.obtainedMarks = obtainedMarks;
		this.submittedAt = submittedAt;
	}
    public Submission() {}
	
    

	
}