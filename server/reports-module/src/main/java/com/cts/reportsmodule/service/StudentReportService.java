package com.cts.reportsmodule.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cts.reportsmodule.client.AssignmentClient;
import com.cts.reportsmodule.client.CourseClient;
import com.cts.reportsmodule.client.QuizClient;
import com.cts.reportsmodule.dto.AssignmentReportDTO;
import com.cts.reportsmodule.dto.QuizReportDTO;
import com.cts.reportsmodule.dto.ResultResponse;
import com.cts.reportsmodule.dto.StudentCourseReportDTO;
import com.cts.reportsmodule.exception.StudentNotEnrolledException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StudentReportService {
	
	@Autowired
	private CourseClient courseClient;
	
	@Autowired
	private AssignmentClient assignmentClient;
	
	@Autowired
	private QuizClient quizClient;
	
	public boolean verifyStudent(String studentId, String courseId) {
		boolean response=courseClient.verifyEnrollment(studentId, courseId);
		return response;
   }
	
    

	public StudentCourseReportDTO generateStudentCourseReport(String studentId, String courseId) {
		
		StudentCourseReportDTO report=new StudentCourseReportDTO();
		report.setStudentId(studentId);
		report.setCourseId(courseId);
		int notCompleted=0;
		
		
		List<String> assignment=assignmentClient.getAllAssignmentsByCourseId(courseId);
		int assignmentNumber=1;
		
		List<AssignmentReportDTO> assignmentResponse=new ArrayList<>();
		//System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
		
		for(String assignmentId:assignment) {
			
//			System.out.println(assignmentId+"hiiiiiiiiiiii");
//			System.out.println("before");
			
			Map<String ,Double> mapData=assignmentClient.getAssignmentsByStudentIdAndAssignmentId(studentId, assignmentId);
			System.out.println("after");
			AssignmentReportDTO assignmentDTO=new AssignmentReportDTO();
			assignmentDTO.setAssignmentNumber(assignmentNumber++);
			assignmentDTO.setMarks(mapData.get(assignmentId));
			if(mapData.get(assignmentId)==null) {
				notCompleted++;
				System.out.println(notCompleted);
			}
			assignmentResponse.add(assignmentDTO);
		}		
		
		
		report.setAssignmentReports(assignmentResponse);
		
		
     //******************************************************************************************//
		
		List<String> quiz=quizClient.getAllQuizByCourseId(courseId);
        int quizNumber=1;
		
		
		
		
		List<QuizReportDTO> quizResponse=new ArrayList<>();
		
		for(String quizId:quiz) {			
			Map<String ,Double> mapData=quizClient.getQuizByStudentIdAndQuizId(studentId, quizId);
			System.out.println("after");
			QuizReportDTO quizDTO=new QuizReportDTO();
			quizDTO.setQuizNumber(quizNumber++);
			quizDTO.setMarks(mapData.get(quizId));
			if(mapData.get(quizId)==null) {
				notCompleted++;
				System.out.println(notCompleted);
			}
			quizResponse.add(quizDTO);
		}
		
		
		report.setQuizReports(quizResponse);
		
		

		
		
		if(notCompleted>0) {
			report.setCompletedStatus("Not Completed");
		}else {
			report.setCompletedStatus("Completed");
		}
		return report;
	}
	
	


}
