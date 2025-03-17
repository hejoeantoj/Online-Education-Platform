package com.cts.reportsmodule.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.reportsmodule.dto.AssignmentReportDTO;
import com.cts.reportsmodule.dto.InstructorCourseReport;
import com.cts.reportsmodule.dto.Response;
import com.cts.reportsmodule.dto.StudentCourseReportDTO;
import com.cts.reportsmodule.service.InstructorReportService;
import com.cts.reportsmodule.service.StudentReportService;
import com.opencsv.CSVWriter;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    
    @Autowired
    private StudentReportService studentReportService;
    
    @Autowired
    private InstructorReportService instructorReportService;

    
    @GetMapping("/generate/student/report")
    public ResponseEntity<Response<StudentCourseReportDTO>> generateStudentCourseReport(@RequestParam String studentId, @RequestParam String courseId) {
    	Response<StudentCourseReportDTO> response=new Response<>();
        try {
            StudentCourseReportDTO report = studentReportService.generateStudentCourseReport(studentId, courseId);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
        	 response.setSuccess(false);
             response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
             response.setErrorMessage("COULD NOT ABLE TO GENERATE REPORT..TRY AGAIN LATER");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    

  
//    @GetMapping("/generate/instructor/report")
//    public ResponseEntity<InputStreamResource> generateReport(@RequestParam String instructorId, @RequestParam String courseId) {
//        try {
//            List<InstructorCourseReport> report = instructorReportService.generateStudentProgressReport(instructorId, courseId);
//
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            CSVWriter writer = new CSVWriter(new OutputStreamWriter(out));
//            writer.writeNext(new String[]{"Student ID", "Student Name", "Assignment ID", "Assignment Number", "Marks", "Status", "Completion Status"});
//
//            for (InstructorCourseReport studentReport : report) {
//                for (AssignmentReportDTO assignmentReport : studentReport.getAssignmentReports()) {
//                    writer.writeNext(new String[]{
//                            studentReport.getStudentId(),
//                            studentReport.getStudentName(),
//                            //assignmentReport.getAssignmentId(),
//                            String.valueOf(assignmentReport.getAssignmentNumber()),
//                            String.valueOf(assignmentReport.getMarks()),
//                            assignmentReport.getStatus(),
//                            studentReport.getCompletionStatus()
//                    });
//                }
//            }
//
//            writer.close();
//
//            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Content-Disposition", "attachment; filename=report.csv");
//
//            return ResponseEntity
//                    .ok()
//                    .headers(headers)
//                    .contentType(MediaType.parseMediaType("application/csv"))
//                    .body(new InputStreamResource(in));
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
    
    
    @GetMapping("/generate/instructor/report")
    public ResponseEntity<?> generateReport(@RequestParam String instructorId, @RequestParam String courseId) {
        Response<InputStreamResource> response = new Response<>();
        try {
            List<InstructorCourseReport> report = instructorReportService.generateStudentProgressReport(instructorId, courseId);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CSVWriter writer = new CSVWriter(new OutputStreamWriter(out));
            writer.writeNext(new String[]{"Student ID", "Student Name", "Assignment ID", "Assignment Number", "Marks", "Status", "Completion Status"});

            for (InstructorCourseReport studentReport : report) {
                for (AssignmentReportDTO assignmentReport : studentReport.getAssignmentReports()) {
                    writer.writeNext(new String[]{
                            studentReport.getStudentId(),
                            studentReport.getStudentName(),
                            //assignmentReport.getAssignmentId(),
                            String.valueOf(assignmentReport.getAssignmentNumber()),
                            String.valueOf(assignmentReport.getMarks()),
                            assignmentReport.getStatus(),
                            studentReport.getCompletionStatus()
                    });
                }
            }

            writer.close();

            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=report.csv");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/csv"))
                    .body(new InputStreamResource(in));
        } catch (IOException e) {
            e.printStackTrace();
            response.setSuccess(false);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setErrorMessage("An error occurred while generating the report.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
