package com.cts.assignmentmodule.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import com.cts.assignmentmodule.dto.AssignmentDTO;
import com.cts.assignmentmodule.exceptions.AssignmentNotFoundException;
import com.cts.assignmentmodule.exceptions.DuplicateAssignmentException;
import com.cts.assignmentmodule.model.Assignment;
import com.cts.assignmentmodule.service.AssignmentService;
import com.cts.assignmentmodule.utils.ResultResponse;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*
 * @Created By: ShaikAsma
 * @Since: 2025
 * 
 * Controller for handling assignment submissions.
 */


@RestController
@RequestMapping("/api/v1")
public class AssignmentController {

    private static final Logger log = LoggerFactory.getLogger(AssignmentController.class);

    @Autowired
    private AssignmentService assignmentService;

    /*
     * Method to Retrieve list of all assignments
     */
    @GetMapping("/viewAssignments")
    public ResponseEntity<ResultResponse<List<Assignment>>> getAllAssignments() {
        ResultResponse<List<Assignment>> response = new ResultResponse<>();

        try {
            List<Assignment> assignments = assignmentService.getAllAssignments();
            response.setSuccess(true);
            response.setMessage("Assignments retrieved successfully");
            response.setData(assignments);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error retrieving assignments: {}", e.getMessage());
            response.setSuccess(false);
            response.setMessage("An error occurred while retrieving assignments check your URI again ");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Method to create a new assignment
     */
    
    
    @PostMapping("instructor/createAssignments")
    public ResponseEntity<ResultResponse<Assignment>> createAssignment(@Valid @RequestBody AssignmentDTO assignment) {
        ResultResponse<Assignment> response = new ResultResponse<>();

        try {
            Assignment createdAssignment = assignmentService.createAssignment(assignment);
            response.setSuccess(true);
            response.setMessage("Assignment created successfully");
            response.setData(createdAssignment);
            response.setStatus(HttpStatus.CREATED);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } 
        catch (DuplicateAssignmentException e) {
            log.error("Error creating assignment: {}", e.getMessage());
            response.setSuccess(false);
            response.setMessage("Cant create Assignment as it is exists already");
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * Method to delete an assignment by its ID
     */
    
    
    @DeleteMapping("instructor/deletebyId")
    public ResponseEntity<ResultResponse<Void>> deleteAssignment(@Valid @RequestBody AssignmentDTO assignmentDTO) {
        ResultResponse<Void> response = new ResultResponse<>();

        try {
            assignmentService.deleteAssignment(assignmentDTO);
            response.setSuccess(true);
            response.setMessage("Assignment deleted successfully");
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AssignmentNotFoundException e) {
            log.error("Error deleting submission: {}", e.getMessage());
            response.setSuccess(false);
            response.setMessage("The Assignment with given id is not available to delete");
            response.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }  
        //Throws an exception if any input is wrong 
        catch (IllegalArgumentException e) {
            log.error("Error creating assignment: {}", e.getMessage());
            response.setSuccess(false);
            response.setMessage("Check Given Input once again...");
            response.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}