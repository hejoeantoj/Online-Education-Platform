package com.cts.reportsmodule.dto;

public class AssignmentReportDTO {
	//private String assignmentId;
    private int assignmentNumber;
    private Double marks;
    private String status;
    
//	public String getAssignmentId() {
//		return assignmentId;
//	}
//	public void setAssignmentId(String assignmentId) {
//		this.assignmentId = assignmentId;
//	}
	public int getAssignmentNumber() {
		return assignmentNumber;
	}
	public void setAssignmentNumber(int assignmentNumber) {
		this.assignmentNumber = assignmentNumber;
	}
	public Double getMarks() {
		return marks;
	}
	public void setMarks(Double marks) {
		this.marks = marks;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
     
}
