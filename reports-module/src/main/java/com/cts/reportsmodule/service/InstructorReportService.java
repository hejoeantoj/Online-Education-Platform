package com.cts.reportsmodule.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.reportsmodule.dao.AssignmentDAO;
import com.cts.reportsmodule.dao.AssignmentSubmissionDAO;
import com.cts.reportsmodule.dao.CourseDAO;
import com.cts.reportsmodule.dao.EnrollmentDAO;
import com.cts.reportsmodule.dao.QuizDAO;
import com.cts.reportsmodule.dao.QuizSubmissionDAO;
import com.cts.reportsmodule.dao.ReportDAO;
import com.cts.reportsmodule.dao.UserDAO;
import com.cts.reportsmodule.dto.AssignmentReportDTO;
import com.cts.reportsmodule.dto.InstructorCourseReport;
import com.cts.reportsmodule.model.Assignment;
import com.cts.reportsmodule.model.AssignmentSubmission;
import com.cts.reportsmodule.model.Course;
import com.cts.reportsmodule.model.Enrollment;
import com.cts.reportsmodule.model.Quiz;
import com.cts.reportsmodule.model.QuizSubmission;
import com.cts.reportsmodule.model.User;

@Service
public class InstructorReportService {

	@Autowired
    private ReportDAO reportDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private CourseDAO courseDAO;
    
    @Autowired
    private EnrollmentDAO enrollmentDAO;

    @Autowired
    private AssignmentDAO assignmentDAO;

    @Autowired
    private AssignmentSubmissionDAO assignmentSubmissionDAO;
   
    @Autowired
    private QuizDAO quizDAO;

    @Autowired
    private QuizSubmissionDAO quizSubmissionDAO;


    
    public List<InstructorCourseReport> generateStudentProgressReport(String instructorId, String courseId) {
        Course course = courseDAO.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getInstructorId().getUserId().equals(instructorId)) {
            throw new RuntimeException("Instructor not authorized for this course");
        }

        List<Enrollment> enrollments = enrollmentDAO.findByCourse(course);
        
        List<Assignment> assignments = assignmentDAO.findByCourse(course);
        System.out.println(assignments.toString() + enrollments.toString());
        //List<Quiz> quizzes = quizDAO.findByCourse(course);
       
        List<InstructorCourseReport> report = new ArrayList<>();
        //String learningResourse="Completed";
        
        int serialNumber=1;
        int notCompleted=0;
        
        for (Enrollment enrollment : enrollments) {
            User student = enrollment.getStudent();
            String studentId = student.getUserId();
            String studentName = student.getUserName();
            List<AssignmentSubmission> assignmentSubmissions = assignmentSubmissionDAO.findByStudentId(studentId);
            List<QuizSubmission> quizSubmissions = quizSubmissionDAO.findByUserId(studentId);
            
            List<AssignmentReportDTO> assignmentReports = new ArrayList<>();
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
            
            

            InstructorCourseReport instructorCourseReport = new InstructorCourseReport();
            
            instructorCourseReport.setStudentId(studentId);
            instructorCourseReport.setStudentName(studentName);
            if(notCompleted>0) {
            instructorCourseReport.setCompletionStatus("Not Completed");
            }else {
            	instructorCourseReport.setCompletionStatus("Completed");
            }
            instructorCourseReport.setAssignmentReports(assignmentReports);
            
            
            report.add(instructorCourseReport);
        }

        return report;
    }
    
    
    
}