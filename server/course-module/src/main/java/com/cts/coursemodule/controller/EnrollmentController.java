
package com.cts.coursemodule.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.coursemodule.dto.CourseDetailsDTO;
import com.cts.coursemodule.dto.EnrollmentDTO;
import com.cts.coursemodule.dto.ResultResponse;
import com.cts.coursemodule.model.Enrollment;
import com.cts.coursemodule.service.EnrollmentService;

import jakarta.validation.Valid;

import com.cts.coursemodule.exception.AlreadyEnrolledException;
import com.cts.coursemodule.exception.CourseNotFoundException;

@RestController
@Validated
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;


    @PostMapping("/create")
    public ResponseEntity<ResultResponse<Enrollment>> createEnrollment(@Valid @RequestBody EnrollmentDTO enrollmentDTO) {
        ResultResponse<Enrollment> response = new ResultResponse<>();
            Enrollment createdEnrollment = enrollmentService.createEnrollment(enrollmentDTO);
            response.setSuccess(true);
            response.setMessage("Enrollment created successfully");
            response.setData(createdEnrollment);
            response.setStatus(HttpStatus.CREATED);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
       
    }

    // gives list of enrollment
    @GetMapping("getAll")//ADMIN.......
    public ResponseEntity<ResultResponse<List<Enrollment>>> getAllEnrollments() {
        ResultResponse<List<Enrollment>> response = new ResultResponse<>();
            List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
            response.setSuccess(true);
            response.setMessage("Enrollments retrieved successfully");
            response.setData(enrollments);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
       
    }

  
    @GetMapping("/student")
    public ResponseEntity<ResultResponse<List<CourseDetailsDTO>>> getEnrollmentByStudentId(@RequestParam String studentId) {
        ResultResponse<List<CourseDetailsDTO>> response = new ResultResponse<>();
            List<CourseDetailsDTO> courses = enrollmentService.getEnrollmentByStudentId(studentId);
            response.setSuccess(true);
            response.setMessage("Courses retrieved successfully");
            response.setData(courses);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        
    }

   
    
    
    @GetMapping("/course")
    public ResponseEntity<ResultResponse<List<String>>> getEnrollmentByCourseId(@Valid @RequestParam String courseId) {
        ResultResponse<List<String>> response = new ResultResponse<>();
            List<String> students = enrollmentService.getEnrollmentByCourseId(courseId);
            response.setSuccess(true);
            response.setMessage("Students retrieved successfully");
            response.setData(students);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        
    }
 
        
       /*
        * 
        * for integration
        * 
        * 
        * 
        */
     @GetMapping ("/verifyEnrollment")
     public boolean verifyEnrollment(@RequestParam String studentId,@RequestParam String courseId){
    	 boolean status=enrollmentService.verifyEnrollment(studentId,courseId);
    	 return status;
     }
     
     
     @GetMapping ("/studentList")
     public List<String> studentList(@RequestParam  String courseId){
    	 System.out.println("ffffffffffff");
    	 return enrollmentService.studentList(courseId);
    	
     }
     
     
}
