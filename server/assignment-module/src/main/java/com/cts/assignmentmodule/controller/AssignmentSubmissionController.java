package com.cts.assignmentmodule.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.assignmentmodule.dto.AssignMarksDTO;
import com.cts.assignmentmodule.dto.AssignmentSubmissionDTO;
import com.cts.assignmentmodule.exceptions.AlreadyMarksAssignedException;
import com.cts.assignmentmodule.exceptions.AssignmentNotFoundException;
import com.cts.assignmentmodule.exceptions.AssignmentSubmissionNotFoundException;
import com.cts.assignmentmodule.exceptions.DuplicateAssignmentSubmissionException;
import com.cts.assignmentmodule.exceptions.InstructorNotAllowedException;
import com.cts.assignmentmodule.exceptions.StudentNotEnrolledException;
import com.cts.assignmentmodule.model.Assignment;
import com.cts.assignmentmodule.model.AssignmentSubmission;
import com.cts.assignmentmodule.service.AssignmentSubmissionService;
import com.cts.assignmentmodule.utils.ResultResponse;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @Created By: ShaikAsma
 * @Since: 2025
 * 
 * Controller for handling assignment submissions.
 */

@Validated
@RestController
@RequestMapping("/api/asubmission")
public class AssignmentSubmissionController {

    private static final Logger log = LoggerFactory.getLogger(AssignmentSubmissionController.class);

    @Autowired
    private AssignmentSubmissionService assignmentSubmissionService;

    
    /*
     * Method to retrieve all assignment submissions.
     * 
     */
    
    
    @GetMapping("/viewAll")
    public ResponseEntity<ResultResponse<?>> getAllSubmissions(@Valid @RequestParam String courseId) {
        ResultResponse<List<AssignmentSubmission>> response = new ResultResponse<>();
            List<AssignmentSubmission> submissions = assignmentSubmissionService.getAllSubmissions(courseId);
            
            response.setSuccess(true);
            response.setMessage("Submissions retrieved successfully");
            response.setData(submissions);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    
    
    /*
     * Method to upload a new assignment submission.
     *
     */
    
    
    
    @PostMapping(value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResultResponse<?>> uploadSubmission( @ModelAttribute AssignmentSubmissionDTO assignmentSubmissionDTO) {
        ResultResponse<String> response = new ResultResponse<>();
        try {
            assignmentSubmissionService.saveSubmission(assignmentSubmissionDTO);
            response.setSuccess(true);
            response.setMessage("Submission uploaded successfully");
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (IOException e) {
            log.error("Error uploading submission: {}", e.getMessage());
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.CONFLICT);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        
     }
 

    /*
     * Method to retrieve a file for a specific user and assignment.
     * 
     *
     */
    
    
    @GetMapping("/viewFiles")
    public ResponseEntity<ResultResponse<byte[]>> getFile(@RequestParam String studentId,@RequestParam String assignmentId,@RequestParam String instructorId, HttpServletResponse response) {
        log.info("Entered into getFile--->Retrieving file for userId and assignmentId");
        ResultResponse<byte[]> resultResponse = new ResultResponse<>();
        
        try {
            byte[] file = assignmentSubmissionService.getFileByUserIdAndAssignmentId(studentId,assignmentId,instructorId);
            response.setContentType("application/pdf");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=submission.pdf");
            response.getOutputStream().write(file);
            
            resultResponse.setSuccess(true);
            resultResponse.setMessage("File retrieved successfully");
            resultResponse.setData(file);
            resultResponse.setStatus(HttpStatus.OK);
            
            return new ResponseEntity<>(resultResponse, HttpStatus.OK);
        }
       
       catch (IOException e) {
           log.error("Error retrieving file: {}", e.getMessage());
          resultResponse.setSuccess(false);
          resultResponse.setMessage(e.getMessage());
          resultResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
          return new ResponseEntity<>(resultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
    
    
    /*
     * Method to assign marks for a specific user and assignment.
     *
     */
   

    @PutMapping("/assignMarks")
    public ResponseEntity<ResultResponse<?>> assignMarks(@Valid @RequestBody AssignMarksDTO assignMarks) {
        ResultResponse<Void> response = new ResultResponse<>();
            assignmentSubmissionService.assignMarksByUserId(assignMarks);
            response.setSuccess(true);
            response.setMessage("Marks assigned successfully");
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        
        
    }
    
    
    /*
     * Method to delete a submission for a specific user and assignment.
     *
     */
    
    
    @DeleteMapping("/delete")
    public ResponseEntity<ResultResponse<?>> deleteSubmission( @RequestParam String studentId, @RequestParam String assignmentId) {
        ResultResponse<Void> response = new ResultResponse<>();
            assignmentSubmissionService.deleteByUserIdandAssignment(studentId,assignmentId);
            response.setSuccess(true);
            response.setMessage("Submission deleted successfully");
            response.setStatus(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        
    }
    
    
    
    /*
     * 
     * For report module............
     * 
     * 
     * 
     * 
     */
    
    @GetMapping("/AssignmentSubmissionDetails")
    public Map<String,Double> getAssignmentsByStudentIdAndAssignmentId(@RequestParam String studentId,@RequestParam String assignmentId) {
    	return assignmentSubmissionService.getAssignmentsByStudentIdAndAssignmentId(studentId,assignmentId);
    }
}
