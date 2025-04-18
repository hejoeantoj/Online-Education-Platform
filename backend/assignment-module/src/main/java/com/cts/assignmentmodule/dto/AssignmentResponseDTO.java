package com.cts.assignmentmodule.dto;

import java.time.LocalDateTime;

public class AssignmentResponseDTO {
	
	private String assignmentId;
	private String question;
	//private LocalDateTime createdAt;
	private String courseId;
	private Integer totalMarks;
	
	public Integer getTotalMarks() {
		return totalMarks;
	}
	public void setTotalMarks(Integer totalMarks) {
		this.totalMarks = totalMarks;
	}
	public String getAssignmentId() {
		return assignmentId;
	}
	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}


	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	
	
	

}
