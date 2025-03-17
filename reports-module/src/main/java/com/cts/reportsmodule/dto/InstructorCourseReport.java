package com.cts.reportsmodule.dto;

import java.util.List;

public class InstructorCourseReport {
	   
	private String studentId;
    private String studentName;
    private List<AssignmentReportDTO> assignmentReports;
  
    private String CompletionStatus;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public List<AssignmentReportDTO> getAssignmentReports() {
		return assignmentReports;
	}

	public void setAssignmentReports(List<AssignmentReportDTO> assignmentReports) {
		this.assignmentReports = assignmentReports;
	}

	public String getCompletionStatus() {
		return CompletionStatus;
	}

	public void setCompletionStatus(String completionStatus) {
		CompletionStatus = completionStatus;
	}
    
    
    

	
    
	    

}
