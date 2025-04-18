package com.cts.assignmentmodule.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "COURSE-MODULE")
public interface CourseClient {
	
	    @GetMapping("/api/course/verifyCourse")
	    public boolean verifyCourse(@RequestParam String courseId);
	 
	 
	 @GetMapping ("/api/enrollment/verifyEnrollment")
     public boolean verifyEnrollment(@RequestParam String studentId,@RequestParam String courseId);
	 
	 @GetMapping("/api/course/verifyInstructor")
	    public boolean verifyInstructor(@RequestParam String instructorId,@RequestParam String courseId) ;
}