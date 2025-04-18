package com.cts.quizmodule.dto;

import java.util.List;
import java.util.Map;


import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotNull;


public class QuizResponse {
	
	@NotNull(message="give proper uuid")
	private UUID quizId;
	
	
	@NotNull(message="userId must be UUID")
    private UUID userId;
	//@Pattern(regexp = "[ABC]", message = "optionA should be one of the following characters: A, B, C")
	
	private List<QuizAnswerDTO> response;
	
	

	

	public List<QuizAnswerDTO> getResponse() {
		return response;
	}

	public void setResponse(List<QuizAnswerDTO> response) {
		this.response = response;
	}

	/**
	 * @return the quizId
	 */
	public UUID getQuizId() {
		return quizId;
	}

	/**
	 * @param quizId the quizId to set
	 */
	public void setQuizId(UUID quizId) {
		this.quizId = quizId;
	}

	
	/**
	 * @return the userId
	 */
	public UUID getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	/**
	 * @return the selectedOptions
	 */
	

//	/**
//	 * @return the selectedOptions
//	 */
//	public Map<String, String> getSelectedOptions() {
//		return selectedOptions;
//	}
//
//	/**
//	 * @param selectedOptions the selectedOptions to set
//	 */
//	public void setSelectedOptions(Map<String, String> selectedOptions) {
//		this.selectedOptions = selectedOptions;
//	}
	
	
	
	
	
	
}

