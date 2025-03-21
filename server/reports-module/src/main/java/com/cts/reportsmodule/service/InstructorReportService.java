package com.cts.reportsmodule.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.reportsmodule.client.AssignmentClient;
import com.cts.reportsmodule.client.CourseClient;
import com.cts.reportsmodule.client.QuizClient;
import com.cts.reportsmodule.dto.AssignmentReportDTO;
import com.cts.reportsmodule.dto.InstructorCourseReportDTO;
import com.cts.reportsmodule.dto.QuizReportDTO;
import com.cts.reportsmodule.exception.InstructorNotAllowedException;

@Service
public class InstructorReportService {
	
	@Autowired
	private CourseClient courseClient;
	
	@Autowired
	private AssignmentClient assignmentClient;
	
	@Autowired
	private QuizClient quizClient;
	
	public boolean verifyInstructor(String instructorId, String courseId) {
		return courseClient.verifyInstructor(instructorId, courseId);
		
      }
	
	
	
	
   public  List<InstructorCourseReportDTO> generateInstructorCourseReport(String instructorId,String courseId) {
	   
	   
	   boolean isvalid=verifyInstructor(instructorId,courseId);
	   if(isvalid==false) {
		   throw new InstructorNotAllowedException("Instructor not allowed");
	   }
	   
	 List<InstructorCourseReportDTO> combinedReport=new ArrayList();  
   
    InstructorCourseReportDTO report=new InstructorCourseReportDTO();
    
    //***************************************************************************************************//
   
    List<AssignmentReportDTO> assignmentResponse=new ArrayList<>();
    
    List<QuizReportDTO> quizResponse=new ArrayList<>();
   
    List<String> enrollmentList=courseClient.studentList(courseId);
   
   for(String enrollment:enrollmentList) {
	   
	   List<String> assignment=assignmentClient.getAllAssignmentsByCourseId(courseId);
		int assignmentNumber=1;
		int notCompleted=0;
           for(String assignmentId:assignment) {
			
//			System.out.println(assignmentId+"hiiiiiiiiiiii");
//			System.out.println("before");
			
			Map<String ,Double> mapData=assignmentClient.getAssignmentsByStudentIdAndAssignmentId(enrollment, assignmentId);
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
		
//*******************************************************************************************88888//
           List<String> quiz=quizClient.getAllQuizByCourseId(courseId);
           int quizNumber=1;
   		
   		   		
   		    for(String quizId:quiz) {			
   			Map<String ,Double> mapData=quizClient.getQuizByStudentIdAndQuizId(enrollment, quizId);
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
   		report.setStudentId(enrollment);
   		
   		combinedReport.add(report);
   				
       }
   
   return combinedReport;
   
         
         

        
	   	   
   }
	
	
	
}
   	

	   
