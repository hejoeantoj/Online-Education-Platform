package com.cts.reportsmodule.dto;

import java.util.List;

public class StudentCourseReportDTO {
	 private String studentId;
	    private String courseId;
	    private List<AssignmentReportDTO> assignmentReports;
	    private List<QuizReportDTO> quizReports;
	   
	    
	    public List<QuizReportDTO> getQuizReports() {
			return quizReports;
		}
		public void setQuizReports(List<QuizReportDTO> quizReports) {
			this.quizReports = quizReports;
		}
		private String completedStatus;
		public String getStudentId() {
			return studentId;
		}
		public void setStudentId(String studentId) {
			this.studentId = studentId;
		}
		public String getCourseId() {
			return courseId;
		}
		public void setCourseId(String courseId) {
			this.courseId = courseId;
		}
		public List<AssignmentReportDTO> getAssignmentReports() {
			return assignmentReports;
		}
		public void setAssignmentReports(List<AssignmentReportDTO> assignmentReports) {
			this.assignmentReports = assignmentReports;
		}
		public String getCompletedStatus() {
			return completedStatus;
		}
		public void setCompletedStatus(String completedStatus) {
			this.completedStatus = completedStatus;
		}
	    
	    
	  
		
	    

}
