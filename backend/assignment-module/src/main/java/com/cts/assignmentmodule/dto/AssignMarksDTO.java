package com.cts.assignmentmodule.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AssignMarksDTO {
	
	@NotNull
	private UUID assignmentId;
	
	@NotNull
	private UUID studentId;
	
	@NotNull
    private UUID instructorId;
	
	@NotNull
	private Double obtainedMarks;
	
	@NotEmpty
	private String feedback;
	
	
	public UUID getStudentId() {
		return studentId;
	}

	public void setStudentId(UUID studentId) {
		this.studentId = studentId;
	}

	
	public Double getObtainedMarks() {
		return obtainedMarks;
	}

	public void setObtainedMarks(Double obtainedMarks) {
		this.obtainedMarks = obtainedMarks;
	}

	

	public UUID getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(UUID assignmentId) {
		this.assignmentId = assignmentId;
	}

	public UUID getInstructorId() {
		return instructorId;
	}

	public void setInstructorId(UUID instructorId) {
		this.instructorId = instructorId;
	}


	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

}
