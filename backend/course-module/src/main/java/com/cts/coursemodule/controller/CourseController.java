package com.cts.coursemodule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.cts.coursemodule.client.CommunicationClient;
import com.cts.coursemodule.dto.CourseDTO;
import com.cts.coursemodule.dto.ResultResponse;
import com.cts.coursemodule.exception.CourseNotFoundException;
import com.cts.coursemodule.model.Course;
import com.cts.coursemodule.service.CourseService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;
    
    
    /**
     * Creates a new course.
     * 
     * @param courseDTO the course data transfer object containing course details
     * @return ResponseEntity containing the created course and HTTP status
     */

   
    @PostMapping("/create")
    public ResponseEntity<ResultResponse<Course>> createCourse(@Valid @RequestBody CourseDTO courseDTO) {
        ResultResponse<Course> response = new ResultResponse<>();
        
            Course createdCourse = courseService.createCourse(courseDTO);
            response.setSuccess(true);
            response.setMessage("Course created successfully");
            response.setData(createdCourse);
            response.setStatus(HttpStatus.CREATED);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    
    
    /**
     * Retrieves all courses.(For all the users)
     * 
     * @return ResponseEntity containing the list of all courses and HTTP status
     */
    
    
    
    @GetMapping("/get")
    public ResponseEntity<ResultResponse<List<Course>>> getAllCourses() {
        ResultResponse<List<Course>> response = new ResultResponse<>();
        
            List<Course> courses = courseService.getAllCourses();
            response.setSuccess(true);
            response.setMessage("Courses retrieved successfully");
            response.setData(courses);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        
    }
    
    
    
    /**
     * Retrieves a course by its ID.
     * 
     * @param courseId the ID of the course to retrieve
     * @return ResponseEntity containing the course and HTTP status
     */
    
 
    @GetMapping("/courseDetails")
    public ResponseEntity<ResultResponse<Course>> getCourseById(@RequestParam String courseId) {
        ResultResponse<Course> response = new ResultResponse<>();
            Optional<Course> course = courseService.getCourseById(courseId);
            response.setSuccess(true);
            response.setMessage("Course retrieved successfully");
            response.setData(course.orElse(null));
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
       
    }
    
    /**
     * Retrieves courses by instructor ID.
     * 
     * @param instructorId the ID of the instructor
     * @return ResponseEntity containing the list of courses and HTTP status
     */
    
    

    @GetMapping("/instructorId")
    public ResponseEntity<ResultResponse<List<Course>>> getCoursesByInstructorId(@RequestParam String instructorId) {
        ResultResponse<List<Course>> response = new ResultResponse<>();
        
            List<Course> courses = courseService.getCoursesByInstructorId(instructorId);
            response.setSuccess(true);
            response.setMessage("Courses retrieved successfully");
            response.setData(courses);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        
    }
    
    

    @PutMapping("/update")
    public ResponseEntity<ResultResponse<Course>> updateCourse(@Valid @RequestBody CourseDTO courseDTO) {
        ResultResponse<Course> response = new ResultResponse<>();
            Course updatedCourse = courseService.updateCourse(courseDTO);
            response.setSuccess(true);
            response.setMessage("Course updated successfully");
            response.setData(updatedCourse);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        
    }
 
    
    /**
     * Updates an existing course.
     * 
     * @param courseDTO the course data transfer object containing  course details which needs to be updated.
     * @return ResponseEntity containing the updated course and HTTP status
     */

    @DeleteMapping("/delete")
    public ResponseEntity<ResultResponse<Void>> deleteCourse(@Valid @RequestParam String courseId,@RequestParam String instructorId) {
        ResultResponse<Void> response = new ResultResponse<>();
            courseService.deleteCourse(courseId,instructorId);
            response.setSuccess(true);
            response.setMessage("Course deleted successfully");
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
       
    }
 
    
    
    
    
    /*
     * Needed for other services
     * 
     * Integration
     * 
     */
   
 
    @GetMapping("/verifyInstructor")
    public boolean verifyInstructor(@RequestParam String instructorId,@RequestParam String courseId) {
    	//System.out.println("hi");
    	System.out.println(instructorId);
    	return courseService.verifyInstructor(instructorId,courseId);
    }
    
    @GetMapping("/verifyCourse")
    public boolean verifyCourse(@RequestParam String courseId) {
    	//System.out.println("hi");
    	return courseService.verifyCourse(courseId);
    }
    
    @GetMapping("/getInstructorId")
    public String getInstructorId(@RequestParam String courseId) {
    	return courseService.getInstructorId(courseId);
    }
    
   

    
}
