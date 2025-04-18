package com.cts.assignmentmodule.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.cts.assignmentmodule.dto.AssignmentDTO;
import com.cts.assignmentmodule.dto.AssignmentResponseDTO;
import com.cts.assignmentmodule.exceptions.AssignmentNotFoundException;
import com.cts.assignmentmodule.exceptions.DuplicateAssignmentException;
import com.cts.assignmentmodule.model.Assignment;
import com.cts.assignmentmodule.service.AssignmentService;
import com.cts.assignmentmodule.utils.ResultResponse;

import jakarta.validation.Valid;

import java.util.List;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@RestController
@Validated
@RequestMapping("/api/assignment")
public class AssignmentController {

    private static final Logger log = LoggerFactory.getLogger(AssignmentController.class);

    @Autowired
    private AssignmentService assignmentService;

    /*
     * Method to Retrieve list of all assignments
     */
    
    
    @PostMapping("/create")
    public ResponseEntity<ResultResponse<Assignment>> createAssignment(@Valid @RequestBody AssignmentDTO assignment) {
        ResultResponse<Assignment> response = new ResultResponse<>();
            Assignment createdAssignment = assignmentService.createAssignment(assignment);
            response.setSuccess(true);
            response.setMessage("Assignment created successfully");
            response.setData(createdAssignment);
            response.setStatus(HttpStatus.CREATED);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        
    }
    
    
    
    
    
    
    @GetMapping("/view")
    public ResponseEntity<ResultResponse<List<AssignmentResponseDTO>>> getAllAssignments(@RequestParam String courseId) {
        ResultResponse<List<AssignmentResponseDTO>> response = new ResultResponse<>();
        
            List<AssignmentResponseDTO> assignments = assignmentService.getAllAssignments(courseId);
            response.setSuccess(true);
            response.setMessage("Assignments retrieved successfully");
            response.setData(assignments);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);     
    }
    
    
    @GetMapping("/viewById")
    public ResponseEntity<ResultResponse<AssignmentResponseDTO>> viewById(@RequestBody AssignmentDTO assignmentId) {
        ResultResponse<AssignmentResponseDTO> response = new ResultResponse<>();
            AssignmentResponseDTO assignmentResponse=new AssignmentResponseDTO();
            Assignment assignment = assignmentService.viewById(assignmentId);
            
            assignmentResponse.setAssignmentId(assignment.getAssignmentId());
            assignmentResponse.setCourseId(assignment.getCourseId());
            assignmentResponse.setQuestion(assignment.getAssignmentId());
            //.setCreatedAt(assignment.get)
            response.setSuccess(true);
            response.setMessage("Assignments retrieved successfully");
            response.setData(assignmentResponse);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);     
    }


    
    @DeleteMapping("/delete")
    public ResponseEntity<ResultResponse<Void>> deleteAssignment( @RequestBody AssignmentDTO assignmentDTO) {
        ResultResponse<Void> response = new ResultResponse<>();
 
            assignmentService.deleteAssignment(assignmentDTO);
            response.setSuccess(true);
            response.setMessage("Assignment deleted successfully");
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //for integration
    @GetMapping("/AssignmentDetails")
    public List<String> getAllAssignmentsByCourseId(@RequestParam String courseId) {
    
    	return assignmentService.getAllAssignmentsByCourseId(courseId);
    }
    	
}