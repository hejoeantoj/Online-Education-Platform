package com.cts.quizmodule.dto;

import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Validated
@JsonIgnoreProperties(ignoreUnknown=true)
public class QuestionDTO {
	
	
	//private String quizId;
	@NotNull
	private UUID quizId;

	@NotNull(message = "give proper name for column")
	@NotBlank(message="give value should not be empty empty")
	private String questionText;
	
	@NotNull(message = "give proper name for column")
	@NotBlank(message="give value should not be empty empty")
	private String optionA;
	
	
	@NotNull(message = "give proper name for column")
	@NotBlank(message="give value dont put empty")
	private String optionB;
	
	@NotNull(message = "give proper name for column")
	@NotBlank(message="give value dont put empty")
	private String optionC;
	
	@NotNull(message = "give proper name for column")
	@NotBlank(message="give value dont put empty")
	@Size(max=1)
	@Pattern(regexp = "[ABCabc]", message = "optionA should be one of the following characters: A, B, C")
	private String correctAnswer;

	
	
	
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
	public String getQuestionText() {
		return questionText;
	}
	/**
	 * @param questionText the questionText to set
	 */
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	/**
	 * @return the optionA
	 */
	public String getOptionA() {
		return optionA;
	}
	/**
	 * @param optionA the optionA to set
	 */
	public void setOptionA(String optionA) {
		this.optionA = optionA;
	}
	/**
	 * @return the optionB
	 */
	public String getOptionB() {
		return optionB;
	}
	/**
	 * @param optionB the optionB to set
	 */
	public void setOptionB(String optionB) {
		this.optionB = optionB;
	}
	/**
	 * @return the optionC
	 */
	public String getOptionC() {
		return optionC;
	}
	/**
	 * @param optionC the optionC to set
	 */
	public void setOptionC(String optionC) {
		this.optionC = optionC;
	}
	/**
	 * @return the correctAnswer
	 */
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	/**
	 * @param correctAnswer the correctAnswer to set
	 */
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
}
