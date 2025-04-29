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
 
    public List<InstructorCourseReportDTO> generateInstructorCourseReport(String instructorId, String courseId) {

        boolean isValid = verifyInstructor(instructorId, courseId);

        if (!isValid) {

            throw new InstructorNotAllowedException("Instructor not allowed");

        }
 
        List<InstructorCourseReportDTO> combinedReport = new ArrayList<>();
 
        List<String> enrollmentList = courseClient.studentList(courseId);
 
        for (String enrollment : enrollmentList) {

            InstructorCourseReportDTO report = new InstructorCourseReportDTO();

            List<AssignmentReportDTO> assignmentResponse = new ArrayList<>();

            List<QuizReportDTO> quizResponse = new ArrayList<>();

            int assignmentNumber = 1;

            int quizNumber = 1;

            int notCompleted = 0;
 
            // Fetch and prepare assignment reports

            List<String> assignments = assignmentClient.getAllAssignmentsByCourseId(courseId);

            for (String assignmentId : assignments) {

                Map<String, Double> mapData = assignmentClient.getAssignmentsByStudentIdAndAssignmentId(enrollment, assignmentId);

                AssignmentReportDTO assignmentDTO = new AssignmentReportDTO();

                assignmentDTO.setAssignmentNumber(assignmentNumber++);

                assignmentDTO.setMarks(mapData.get(assignmentId));

                if (mapData.get(assignmentId) == null) {

                    notCompleted++;

                }

                assignmentResponse.add(assignmentDTO);

            }
 
            // Fetch and prepare quiz reports

            List<String> quizzes = quizClient.getAllQuizByCourseId(courseId);

            for (String quizId : quizzes) {

                Map<String, Double> mapData = quizClient.getQuizByStudentIdAndQuizId(enrollment, quizId);

                QuizReportDTO quizDTO = new QuizReportDTO();

                quizDTO.setQuizNumber(quizNumber++);

                quizDTO.setMarks(mapData.get(quizId));

                if (mapData.get(quizId) == null || mapData.get(quizId) < 50.0) {

                    notCompleted++;

                }

                quizResponse.add(quizDTO);

            }
 
            // Set data into report

            report.setStudentId(enrollment);

            report.setAssignmentReports(assignmentResponse);

            report.setQuizReports(quizResponse);
            
            if(notCompleted>0)
                report.setCompletedStatus("Not Completed");
                else {
                	report.setCompletedStatus("Completed");
                }
     
 
            // Add report to the final list

            combinedReport.add(report);

        }
 
        return combinedReport;

    }

}

 