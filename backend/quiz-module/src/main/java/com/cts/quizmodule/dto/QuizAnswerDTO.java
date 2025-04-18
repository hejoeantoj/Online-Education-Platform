package com.cts.quizmodule.dto;

import java.util.List;
import java.util.UUID;

public class QuizAnswerDTO {
	
	private UUID questionId;
	
	public UUID getQuestionId() {
		return questionId;
	}

	public void setQuestionId(UUID questionId) {
		this.questionId = questionId;
	}

	private String selectedOption;

	

	public String getSelectedOption() {
		return selectedOption;
	}

	public void setSelectedOption(String selectedOption) {
		this.selectedOption = selectedOption;
	}

}
