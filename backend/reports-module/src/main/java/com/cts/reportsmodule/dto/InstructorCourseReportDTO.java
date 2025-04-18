package com.cts.reportsmodule.dto;

import java.util.List;

public class InstructorCourseReportDTO {
	
	private String studentId;
	
	private List<AssignmentReportDTO> assignmentReports;
	 
	private List<QuizReportDTO> quizReports;

	
	
	
	
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public List<AssignmentReportDTO> getAssignmentReports() {
		return assignmentReports;
	}

	public void setAssignmentReports(List<AssignmentReportDTO> assignmentReports) {
		this.assignmentReports = assignmentReports;
	}

	public List<QuizReportDTO> getQuizReports() {
		return quizReports;
	}

	public void setQuizReports(List<QuizReportDTO> quizReports) {
		this.quizReports = quizReports;
	}
	   
	

}
