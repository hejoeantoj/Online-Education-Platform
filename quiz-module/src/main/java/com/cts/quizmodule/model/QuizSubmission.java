package com.cts.quizmodule.model;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name="submission")
public class QuizSubmission {
    
	@Id
    private String submissionId;


	@Column(nullable=false)
    private String userId; 
	
	
	@ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;
	
    
    @Column(nullable=true)
    private double obtainedMarks;
    
    
    
    /**
	 * @return the percentage
	 */
	public double getPercentage() {
		return percentage;
	}




	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	@Column(nullable=false)
    private double percentage;
    
    @PrePersist
    public void generateUuid() {
    	this.submissionId = UUID.randomUUID().toString();
    }
	
    

	
    /**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public double getObtainedMarks() {
		return obtainedMarks;
	}

	public void setObtainedMarks(double obtainedMarks) {
		this.obtainedMarks = obtainedMarks;
	}

	/**
	 * @return the submissionId
	 */
	public String getSubmissionId() {
		return submissionId;
	}

	/**
	 * @param submissionId the submissionId to set
	 */
	public void setSubmissionId(String submissionId) {
		this.submissionId = submissionId;
	}

	public QuizSubmission() {
		
	}

	public QuizSubmission(String submissionId, String userId, Quiz quiz, int obtainedMarks) {
		super();
		this.submissionId = submissionId;
		this.userId = userId;
		this.quiz = quiz;
		this.obtainedMarks = obtainedMarks;
	}




	 
	
  
}
