package com.cts.reportsmodule.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.reportsmodule.dto.InstructorCourseReportDTO;
import com.cts.reportsmodule.dto.ResultResponse;
import com.cts.reportsmodule.dto.StudentCourseReportDTO;
import com.cts.reportsmodule.service.InstructorReportService;

import com.cts.reportsmodule.service.StudentReportService;


@RestController
@RequestMapping("/api/report")
public class ReportController {
	
	@Autowired
	private StudentReportService studentReportService;
	
	
	@Autowired
	private InstructorReportService instructorReportService;
	
	

	
	@GetMapping("/")
	public String hello() {
		System.out.print("CONTROLLER");
		return "Hi da";
	}
	
	@GetMapping("/student")
    public ResponseEntity<ResultResponse<StudentCourseReportDTO>> generateStudentCourseReport(@RequestParam String studentId, @RequestParam String courseId) {
    	ResultResponse<StudentCourseReportDTO> response=new ResultResponse<>();
        try {
            StudentCourseReportDTO report = studentReportService.generateStudentCourseReport(studentId, courseId);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
        	e.printStackTrace();
        	 response.setSuccess(false);
             response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
             response.setMessage("COULD NOT ABLE TO GENERATE REPORT..TRY AGAIN LATER");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	
	
	
	
	
	
	
	
	@GetMapping("/instructor")
    public ResponseEntity<ResultResponse<List<InstructorCourseReportDTO>>> generateInstructorCourseReport(@RequestParam String instructorId, @RequestParam String courseId) {
    	ResultResponse<List<InstructorCourseReportDTO>> response=new ResultResponse<>();
        try {
            List<InstructorCourseReportDTO> report = instructorReportService.generateInstructorCourseReport(instructorId, courseId);
            response.setSuccess(true);
            response.setStatus(HttpStatus.OK);
            response.setData(report);
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
        	e.printStackTrace();
        	 response.setSuccess(false);
             response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
             response.setMessage("COULD NOT ABLE TO GENERATE REPORT..TRY AGAIN LATER");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	
	
}
	
	
	
	
	




