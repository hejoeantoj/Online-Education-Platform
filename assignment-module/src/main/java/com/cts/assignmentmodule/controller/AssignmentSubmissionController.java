package com.cts.assignmentmodule.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.assignmentmodule.dto.AssignmentSubmissionDTO;
import com.cts.assignmentmodule.exceptions.AlreadyMarksAssignedException;
import com.cts.assignmentmodule.exceptions.AssignmentNotFoundException;
import com.cts.assignmentmodule.exceptions.AssignmentSubmissionNotFoundException;
import com.cts.assignmentmodule.exceptions.DuplicateAssignmentSubmissionException;
import com.cts.assignmentmodule.exceptions.InstructorNotAllowedException;
import com.cts.assignmentmodule.exceptions.StudentNotEnrolledException;
import com.cts.assignmentmodule.model.AssignmentSubmission;
import com.cts.assignmentmodule.service.AssignmentSubmissionService;
import com.cts.assignmentmodule.utils.ResultResponse;

import jakarta.servlet.http.HttpServletResponse;

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
public class AssignmentSubmissionController {

    private static final Logger log = LoggerFactory.getLogger(AssignmentSubmissionController.class);

    @Autowired
    private AssignmentSubmissionService assignmentSubmissionService;

    
    /*
     * Method to retrieve all assignment submissions.
     * 
     */
    
    
    @GetMapping("instructor/viewSubmissions")
    public ResponseEntity<ResultResponse<?>> getAllSubmissions() {
        ResultResponse<List<AssignmentSubmission>> response = new ResultResponse<>();
        
        try {
            List<AssignmentSubmission> submissions = assignmentSubmissionService.getAllSubmissions();
            
            response.setSuccess(true);
            response.setMessage("Submissions retrieved successfully");
            response.setData(submissions);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } 
        catch (Exception e) {
            log.error("Error retrieving submissions");
            response.setSuccess(false);
            response.setMessage("An error occurred while retrieving submissions check ur uri");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    
    /*
     * Method to upload a new assignment submission.
     *
     */
    
    
    
    @PostMapping(value="/uploadAssignment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultResponse<?>> uploadSubmission(@ModelAttribute AssignmentSubmissionDTO assignmentSubmissionDTO) {
        ResultResponse<String> response = new ResultResponse<>();
        try {
            assignmentSubmissionService.saveSubmission(assignmentSubmissionDTO);
            response.setSuccess(true);
            response.setMessage("Submission uploaded successfully");
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } 
        catch (DuplicateAssignmentSubmissionException e) {
            log.error("Error uploading submission: {}", e.getMessage());
            response.setSuccess(false);
            response.setMessage("You cant upload assignment ...submission exists");
            response.setStatus(HttpStatus.CONFLICT);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        catch (AssignmentNotFoundException e) {
            log.error("Assignment not found for given AssignmentId");
            response.setSuccess(false);
            response.setMessage("Assignment not found for given AssignmentId");
            response.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        catch (StudentNotEnrolledException e) {
            log.error("Student not enrolled in the course");
            response.setSuccess(false);
            response.setMessage("Student Not Enrolled in the course");
            response.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }catch (IOException e) {
            log.error("Error uploading submission: {}", e.getMessage());
            response.setSuccess(false);
            response.setMessage("You cant upload assignment ...submission exists");
            response.setStatus(HttpStatus.CONFLICT);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        
        
    }

    /*
     * Method to retrieve a file for a specific user and assignment.
     * 
     *
     */
    
    
    @PostMapping("instructor/viewFiles")
    public ResponseEntity<ResultResponse<byte[]>> getFile(@Valid @RequestBody AssignmentSubmissionDTO assignmentSubmissionDTO, HttpServletResponse response) {
        log.info("Entered into getFile--->Retrieving file for userId and assignmentId");
        ResultResponse<byte[]> resultResponse = new ResultResponse<>();
        
        try {
            byte[] file = assignmentSubmissionService.getFileByUserIdAndAssignmentId(assignmentSubmissionDTO);
            response.setContentType("application/pdf");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=submission.pdf");
            response.getOutputStream().write(file);
            
            resultResponse.setSuccess(true);
            resultResponse.setMessage("File retrieved successfully");
            resultResponse.setData(file);
            resultResponse.setStatus(HttpStatus.OK);
            
            return new ResponseEntity<>(resultResponse, HttpStatus.OK);
        } 
        catch (AssignmentNotFoundException e) {
            log.error("Error uploading submission: {}", e.getMessage());
            resultResponse.setSuccess(false);
            resultResponse.setMessage("Assignment Not found for the given Id");
            resultResponse.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(resultResponse, HttpStatus.NOT_FOUND);
        } 
        catch (AssignmentSubmissionNotFoundException e) {
            log.error("Error uploading submission: {}", e.getMessage());
            resultResponse.setSuccess(false);
            resultResponse.setMessage("SubmissionNot found Exception");
            resultResponse.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(resultResponse, HttpStatus.NOT_FOUND);
        }
        catch (IOException e) {
            log.error("Error retrieving file: {}", e.getMessage());
            
            resultResponse.setSuccess(false);
            resultResponse.setMessage("File is Not Readable");
            resultResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            
            return new ResponseEntity<>(resultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } 
    }
    
    
    /*
     * Method to assign marks for a specific user and assignment.
     *
     */
   

    @PutMapping("instructor/assignMarks")
    public ResponseEntity<ResultResponse<?>> assignMarks(@Valid @RequestBody AssignmentSubmissionDTO assignmentSubmissionDTO) {
        ResultResponse<Void> response = new ResultResponse<>();
        
        try {
            assignmentSubmissionService.assignMarksByUserId(assignmentSubmissionDTO);
            response.setSuccess(true);
            response.setMessage("Marks assigned successfully");
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AssignmentNotFoundException e) {
            log.error("Error assigning marks: {}", e.getMessage());
            response.setSuccess(false);
            response.setMessage("Assignment Not Found ");
            response.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } 
        catch (InstructorNotAllowedException e) {
            log.error("Error uploading submission: {}", e.getMessage());
            response.setSuccess(false);
            response.setMessage("instructor not allowed");
            response.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        catch (AlreadyMarksAssignedException e) {
            log.error("Error uploading submission: {}", e.getMessage());
            response.setSuccess(false);
            response.setMessage("marks assigned already");
            response.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        
    }
    
    
    /*
     * Method to delete a submission for a specific user and assignment.
     *
     */
    
    
    @DeleteMapping("student/deleteSubmission")
    public ResponseEntity<ResultResponse<?>> deleteSubmission(@Valid @RequestBody AssignmentSubmissionDTO assignmentSubmissionDTO) {
        ResultResponse<Void> response = new ResultResponse<>();
        
        try {
            assignmentSubmissionService.deleteByUserIdandAssignment(assignmentSubmissionDTO);
            response.setSuccess(true);
            response.setMessage("Submission deleted successfully");
            response.setStatus(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (AssignmentNotFoundException e) {
            log.error("Error deleting submission: {}", e.getMessage());
            response.setSuccess(false);
            response.setMessage("Assignment Not found ");
            response.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } 
        catch (AssignmentSubmissionNotFoundException e) {
            log.error("Error uploading submission: {}", e.getMessage());
            response.setSuccess(false);
            response.setMessage("Submission not found ");
            response.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
                
    }
}
