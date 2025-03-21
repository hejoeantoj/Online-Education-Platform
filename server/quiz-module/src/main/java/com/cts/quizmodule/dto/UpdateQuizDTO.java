package com.cts.quizmodule.dto;

import java.util.UUID;

public class UpdateQuizDTO {
	
	private UUID quizId;
	
	private UUID instructorId;
	
	private Integer totalMarks;

	public UUID getQuizId() {
		return quizId;
	}

	public void setQuizId(UUID quizId) {
		this.quizId = quizId;
	}

	public UUID getInstructorId() {
		return instructorId;
	}

	public void setInstructorId(UUID instructorId) {
		this.instructorId = instructorId;
	}

	public Integer getTotalMarks() {
		return totalMarks;
	}

	public void setTotalMarks(Integer totalmarks) {
		this.totalMarks = totalmarks;
	}
	
	
	

}
