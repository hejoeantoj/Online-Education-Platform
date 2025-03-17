package com.cts.reportsmodule.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.reportsmodule.dao.AssignmentDAO;
import com.cts.reportsmodule.dao.AssignmentSubmissionDAO;
import com.cts.reportsmodule.dao.CourseDAO;
import com.cts.reportsmodule.dao.QuizDAO;
import com.cts.reportsmodule.dao.QuizSubmissionDAO;
import com.cts.reportsmodule.dao.ReportDAO;
import com.cts.reportsmodule.dao.UserDAO;
import com.cts.reportsmodule.dto.AssignmentReportDTO;
import com.cts.reportsmodule.dto.StudentCourseReportDTO;
import com.cts.reportsmodule.model.Assignment;
import com.cts.reportsmodule.model.AssignmentSubmission;
import com.cts.reportsmodule.model.Course;
import com.cts.reportsmodule.model.Quiz;
import com.cts.reportsmodule.model.QuizSubmission;
import com.cts.reportsmodule.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentReportService {

	@Autowired
    private ReportDAO reportDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private CourseDAO courseDAO;

    @Autowired
    private AssignmentDAO assignmentDAO;

    @Autowired
    private AssignmentSubmissionDAO assignmentSubmissionDAO;
   
    @Autowired
    private QuizDAO quizDAO;

    @Autowired
    private QuizSubmissionDAO quizSubmissionDAO;

    public StudentCourseReportDTO generateStudentCourseReport(String studentId, String courseId) {
        User student = userDAO.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseDAO.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        List<Assignment> assignments = assignmentDAO.findByCourse(course);
        List<AssignmentSubmission> assignmentSubmissions = assignmentSubmissionDAO.findByStudentId(studentId);
        List<Quiz> quizzes = quizDAO.findByCourse(course);
        List<QuizSubmission> quizSubmissions = quizSubmissionDAO.findByUserId(studentId);

        List<AssignmentReportDTO> assignmentReports = new ArrayList<>();
        int serialNumber=1;
        int notCompleted=0;
        for (Assignment assignment : assignments) {
            AssignmentReportDTO assignmentReport = new AssignmentReportDTO();
            //assignmentReport.setAssignmentId(assignment.getAssignmentId());
            assignmentReport.setAssignmentNumber(serialNumber++); //serial no

            AssignmentSubmission matchingSubmission = null;
            for (AssignmentSubmission submission : assignmentSubmissions) {
                if (submission.getAssignment().getAssignmentId().equals(assignment.getAssignmentId())) {
                    matchingSubmission = submission;
                    break;
                }
            }

            if (matchingSubmission != null && matchingSubmission.getReviewedAt() != null) {
                assignmentReport.setMarks(matchingSubmission.getObtainedMarks());
                assignmentReport.setStatus("Completed");
            } else {
                assignmentReport.setMarks(null);
                assignmentReport.setStatus("Not Completed");
                notCompleted++;
            }

            assignmentReports.add(assignmentReport);
        }

//        List<QuizReportDTO> quizReports = new ArrayList<>();
//        for (Quiz quiz : quizzes) {
//            QuizReportDTO quizReport = new QuizReportDTO();
//            quizReport.setQuizId(quiz.getQuizId());
//            quizReport.setQuizNumber("1234"); // Using a placeholder number for simplicity
//
//            QuizSubmission matchingSubmission = null;
//            for (QuizSubmission submission : quizSubmissions) {
//                if (submission.getQuiz().getQuizId().equals(quiz.getQuizId())) {
//                    matchingSubmission = submission;
//                    break;
//                }
//            }
//
//            if (matchingSubmission != null) {
//                quizReport.setMarks(matchingSubmission.getObtainedMarks());
//                quizReport.setStatus("Completed");
//            } else {
//                quizReport.setMarks(null);
//                quizReport.setStatus("Not Completed");
//            }
//
//            quizReports.add(quizReport);
//       }

        StudentCourseReportDTO report = new StudentCourseReportDTO();
        report.setStudentName(student.getUserName());
        report.setCourseName(course.getTitle());
        report.setAssignmentReports(assignmentReports);
        //report.setQuizReports(quizReports);
        if(notCompleted==0) {
        	report.setCompletedStatus("Completed");
        }else {
        	report.setCompletedStatus("Not Completed");
        }
        
        return report;
    }
}