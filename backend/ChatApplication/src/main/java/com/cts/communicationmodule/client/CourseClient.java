package com.cts.communicationmodule.client;

import java.util.List;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "COURSE-MODULE")
public interface CourseClient {
	
//	@GetMapping("api/enrollment/verifyEnrollment")
//    public boolean verifyEnrollment(@RequestParam String studentId,@RequestParam String courseId);
	 
	 @GetMapping("api/course/verifyInstructor")
	  public boolean verifyInstructor(@RequestParam String instructorId,@RequestParam String courseId) ;
	 
	 @GetMapping("api/course/verifyCourse")
	    public boolean verifyCourse(@RequestParam String courseId);
	 

     @GetMapping ("api/enrollment/studentList")
     public List<String> studentList(@RequestParam String courseId);
     
     
     @GetMapping("api/course/getInstructorId")
     public String getInstructorId(@RequestParam String courseId);
}


