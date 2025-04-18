package com.cts.assignmentmodule.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class AssignmentSubmission {

    @Id
    private String submissionId;

    
    @Column(name = "studentId", nullable = false)
    private String studentId;

    @ManyToOne
    @JoinColumn(name = "assignmentId", nullable = false)
    @JsonBackReference
    private Assignment assignment;

    @Lob
    private byte[] answerUpload;
    
    @Column (nullable=true)
    private Double  obtainedMarks;
    
   @Column (nullable=true)
    private Double percentage;

	@Column(nullable = false)
    private LocalDateTime submittedAt;
    
    @Column(nullable=true)
    private LocalDateTime reviewedAt;
    
    @Column(nullable=true)
    private String feedback;

    

	@PrePersist
    public void generateSubmissionId() {
        this.submissionId = UUID.randomUUID().toString();
        this.submittedAt=LocalDateTime.now();
       
    }

	public String getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(String submissionId) {
		this.submissionId = submissionId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}
	

	public byte[] getAnswerUpload() {
		return answerUpload;
	}

	public void setAnswerUpload(byte[] answerUpload) {
		this.answerUpload = answerUpload;
	}

	public Double getObtainedMarks() {
		return obtainedMarks;
	}

	public void setObtainedMarks(Double obtainedMarks) {
		this.obtainedMarks = obtainedMarks;
	}

	/**
	 * @return the percentage
	 */
	public Double getPercentage() {
		return percentage;
	}

	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
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

	/**
	 * @return the reviewedAt
	 */
	public LocalDateTime getReviewedAt() {
		return reviewedAt;
	}

	/**
	 * @param reviewedAt the reviewedAt to set
	 */
	public void setReviewedAt(LocalDateTime reviewedAt) {
		this.reviewedAt = reviewedAt;
	}

	/**
	 * @return the feedback
	 */
	public String getFeedback() {
		return feedback;
	}

	/**
	 * @param feedback the feedback to set
	 */
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	
	



    
}
