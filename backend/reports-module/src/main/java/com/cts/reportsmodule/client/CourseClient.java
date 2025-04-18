package com.cts.reportsmodule.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.cts.reportsmodule.dto.ResultResponse;



//import com.cts.coursemodule.model.Course;

@FeignClient(name = "COURSE-MODULE")
public interface CourseClient {
    
	
	  
	 @GetMapping ("/api/enrollment/verifyEnrollment")
     public boolean verifyEnrollment(@RequestParam String studentId,@RequestParam String courseId);
	 
	 @GetMapping("/api/course/verifyInstructor")
	    public boolean verifyInstructor(@RequestParam String instructorId,@RequestParam String courseId) ;
	 
	 @GetMapping ("/api/enrollment/studentList")
     public List<String> studentList(@RequestParam  String courseId);
	 
//	 @GetMapping("api/courses/courseDetails")
//	  public ResponseEntity<ResultResponse<?>> getCourseObject(@RequestParam String courseId) ;
}