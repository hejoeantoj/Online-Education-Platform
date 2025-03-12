package com.cts.quizmodule.dto;

import java.util.Map;
import java.util.UUID;

public class QuizResponse {
	private String quizId;
	
    private String userId;
	
	private Map<String, String> selectedOptions;

	/**
	 * @return the quizId
	 */
	public String getQuizId() {
		return quizId;
	}

	/**
	 * @param quizId the quizId to set
	 */
	public void setQuizId(String quizId) {
		this.quizId = quizId;
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

	/**
	 * @return the selectedOptions
	 */
	public Map<String, String> getSelectedOptions() {
		return selectedOptions;
	}

	/**
	 * @param selectedOptions the selectedOptions to set
	 */
	public void setSelectedOptions(Map<String, String> selectedOptions) {
		this.selectedOptions = selectedOptions;
	}
	
	
	
}

