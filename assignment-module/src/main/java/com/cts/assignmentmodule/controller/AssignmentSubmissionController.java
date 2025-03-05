package com.cts.assignmentmodule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.cts.assignmentmodule.model.Assignment;
import com.cts.assignmentmodule.model.Submission;
import com.cts.assignmentmodule.service.AssignmentService;
import com.cts.assignmentmodule.service.SubmissionService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class AssignmentSubmissionController {

    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private SubmissionService submissionService;

    // Retrieves a list of all assignments
    @GetMapping("/assignments")
    public List<Assignment> getAllAssignments() {
        //log.info("Fetching all assignments");
        return assignmentService.getAllAssignments();
    }

    //  creates a new assignment 
    @PostMapping("instructor/assignments")
    public Assignment createAssignment(@RequestBody Assignment assignment) {
        //log.info("Create assignment {}", assignment);
        return assignmentService.createAssignment(assignment);
    }

    // retrieves list of all submissions
    @GetMapping("instructor/submissions")
    public List<Submission> getAllSubmissions() {
        //log.info("Fetch all submissions");
        return submissionService.getAllSubmissions();
    }

    // Users can upload their PDF files
    @PostMapping("/upload")
    public void uploadSubmission(@RequestParam("file") MultipartFile file,
                                 @RequestParam("userId") int userId,
                                 @RequestParam("assignmentId") int assignmentId) throws IOException {
       // log.info("Uploading submission ");
        submissionService.saveSubmission(file, userId, assignmentId);
    }

    // To view the submitted files
    @GetMapping("/{userId}/file/{assignmentId}")
    public void getFile(@PathVariable int userId, @PathVariable int assignmentId, HttpServletResponse response) throws IOException {
        //log.info("Fetching file");
        byte[] file = submissionService.getFileByUserIdAndAssignmentId(userId, assignmentId);
        response.setContentType("application/pdf");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=submission.pdf");
        response.getOutputStream().write(file);
    }

    // Assign marks to students 
    @PutMapping("/user/{userId}/assignment/{assignmentId}/marks/{marks}")
    public void assignMarks(@PathVariable int userId, @PathVariable int assignmentId, @PathVariable int marks) {
        //log.info("Assigning marks ");
        submissionService.assignMarksByUserId(userId, assignmentId, marks);
    }

    // To view percentage marks of student
    @GetMapping("assignment/{assignmentId}/user/{userId}/percentage")
    public ResponseEntity<Double> getSubmissionPercentage(@PathVariable int assignmentId, @PathVariable int userId) {
       // log.info("Fetching percentage" );
        double percentage = submissionService.calculatePercentage(userId, assignmentId);
        return ResponseEntity.ok(percentage);
    }

    // Delete submission method
    @DeleteMapping("/{assignmentId}/users/{userId}/submissions")
    public ResponseEntity<Void> deleteSubmission(@PathVariable int assignmentId, @PathVariable int userId) {
        //log.info("Deleting submission ");
        submissionService.deleteByUserIdandAssignment(userId, assignmentId);
        return ResponseEntity.noContent().build();
    }
    
    //deleting assignment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Integer id) {
        try {
            assignmentService.deleteAssignment(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
}
