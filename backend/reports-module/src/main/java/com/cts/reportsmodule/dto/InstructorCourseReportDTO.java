package com.cts.reportsmodule.dto;

import java.util.List;

public class InstructorCourseReportDTO {
	
	private String studentId;
	
	private List<AssignmentReportDTO> assignmentReports;
	 
	private List<QuizReportDTO> quizReports;
	
	private String completedStatus;

	
	
	
	
	/**
	 * @return the completedStatus
	 */
	public String getCompletedStatus() {
		return completedStatus;
	}

	/**
	 * @param completedStatus the completedStatus to set
	 */
	public void setCompletedStatus(String completedStatus) {
		this.completedStatus = completedStatus;
	}

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
