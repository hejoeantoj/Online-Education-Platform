package com.cts.quizmodule.dto;

import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
@JsonIgnoreProperties(ignoreUnknown=true)
public class QuizDTO {
	
	
	private UUID quizId;
	
	@NotNull
    private UUID courseId;
	
	@NotNull(message = "give proper name for column")
	@NotBlank(message="give value")
	private String title;
	
	@NotNull
	private Integer totalMarks;
	
	
	
	/**
	 * @return the courseId
	 */
	public UUID getCourseId() {
		return courseId;
	}
	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(UUID courseId) {
		this.courseId = courseId;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the totalMarks
	 */
	public Integer getTotalMarks() {
		return totalMarks;
	}
	/**
	 * @param totalMarks the totalMarks to set
	 */
	public void setTotalMarks(Integer totalMarks) {
		this.totalMarks = totalMarks;
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
	
}
