package com.cts.reportsmodule.client;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cts.reportsmodule.dto.ResultResponse;


@FeignClient(name = "ASSIGNMENT-MODULE")
public interface AssignmentClient {

	 	@GetMapping("/api/assignment/AssignmentDetails")
	    public List<String> getAllAssignmentsByCourseId(@RequestParam String courseId);
	    
	    @GetMapping("/api/asubmission/AssignmentSubmissionDetails")
	    public Map<String,Double> getAssignmentsByStudentIdAndAssignmentId(@RequestParam String studentId, @RequestParam String assignmentId);
	
}
