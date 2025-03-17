package com.cts.reportsmodule.dto;

import java.util.List;

public class StudentCourseReportDTO {
	 private String studentName;
	    private String courseName;
	    private List<AssignmentReportDTO> assignmentReports;
	    //private List<QuizReportDTO> quizReports;
	    private String completedStatus;
	    
	    
	    
		public String getCompletedStatus() {
			return completedStatus;
		}
		public void setCompletedStatus(String completedStatus) {
			this.completedStatus = completedStatus;
		}
		public String getStudentName() {
			return studentName;
		}
		public void setStudentName(String studentName) {
			this.studentName = studentName;
		}
		public String getCourseName() {
			return courseName;
		}
		public void setCourseName(String courseName) {
			this.courseName = courseName;
		}
		public List<AssignmentReportDTO> getAssignmentReports() {
			return assignmentReports;
		}
		public void setAssignmentReports(List<AssignmentReportDTO> assignmentReports) {
			this.assignmentReports = assignmentReports;
		}
		
//		public List<QuizReportDTO> getQuizReports() {
//			return quizReports;
//		}
//		public void setQuizReports(List<QuizReportDTO> quizReports) {
//			this.quizReports = quizReports;
//		}
		
		
	    

}
