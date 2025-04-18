package com.cts.assignmentmodule.dto;

import java.time.LocalDateTime;

public class AssignmentReportDTO {

	private Double percentage;
	
	private String feedback;
	
	private LocalDateTime reviewedAt;

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public LocalDateTime getReviewedAt() {
		return reviewedAt;
	}

	public void setReviewedAt(LocalDateTime reviewedAt) {
		this.reviewedAt = reviewedAt;
	}
	
	
	
	
	
	
}
