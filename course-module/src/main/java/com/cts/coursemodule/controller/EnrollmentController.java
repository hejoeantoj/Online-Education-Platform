
package com.cts.coursemodule.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.coursemodule.dto.CourseDetailsDTO;
import com.cts.coursemodule.dto.EnrollmentDTO;
import com.cts.coursemodule.model.Enrollment;
import com.cts.coursemodule.service.EnrollmentService;
import com.cts.coursemodule.exception.AlreadyEnrolledException;
import com.cts.coursemodule.exception.CourseNotFoundException;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;


    @PostMapping("/createenrollment")
    public ResponseEntity<?> createEnrollment(@RequestBody EnrollmentDTO enrollmentDTO) {
        try {
            Enrollment createdEnrollment = enrollmentService.createEnrollment(enrollmentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEnrollment);
        } catch (CourseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AlreadyEnrolledException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    // gives list of enrollment
    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }

  
    @GetMapping("/get/courseList")
    public ResponseEntity<?> getEnrollmentByStudentId(@RequestParam EnrollmentDTO enrollmentDTO) {
        try {
            List<CourseDetailsDTO> courses = enrollmentService.getEnrollmentByStudentId(enrollmentDTO.getStudentId().toString());
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

   
    
    
    @GetMapping("/get/studentsList")
    public ResponseEntity<?> getEnrollmentByCourseId(@RequestBody EnrollmentDTO enrollmentDTO) {
        try {
            List<String> students = enrollmentService.getEnrollmentByCourseId(enrollmentDTO);
            return ResponseEntity.ok(students);
        } catch (CourseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
        
        
     @GetMapping ("/verifyEnrollment")
     public boolean verifyEnrollment(@RequestParam String studentId,@RequestParam String courseId){
    	 boolean status=enrollmentService.verifyEnrollment(studentId,courseId);
    	 return status;
     }
}
