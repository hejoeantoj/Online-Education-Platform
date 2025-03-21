package com.cts.quizmodule.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.quizmodule.dto.QuizResponse;
import com.cts.quizmodule.exceptions.ExistingQuizSubmissionException;
import com.cts.quizmodule.exceptions.QuizNotFoundException;
import com.cts.quizmodule.exceptions.StudentNotEnrolledException;
import com.cts.quizmodule.exceptions.UserNotFoundException;
import com.cts.quizmodule.model.QuizSubmission;
import com.cts.quizmodule.service.QuizSubmissionService;
import com.cts.quizmodule.utils.ResultResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/squiz")
public class QuizSubmissionController {

    @Autowired
    private QuizSubmissionService submissionService;

    /*
     * Method to Add their Submission by user id and quiz id
     */
    @PutMapping("/submit")
    public ResponseEntity<ResultResponse<QuizSubmission>> submitAnswer(@Valid @RequestBody QuizResponse response) {
        ResultResponse<QuizSubmission> resultResponse = new ResultResponse<>();
            QuizSubmission submission = submissionService.addSubmission(response);
            resultResponse.setSuccess(true);
            resultResponse.setMessage("Submission added successfully");
            resultResponse.setData(submission);
            resultResponse.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }

    /*
     * Method to view submission result of a user for specific quiz id
     */
    
    
    @GetMapping("/result")
    public ResponseEntity<ResultResponse<QuizSubmission>> courseSubmissions(@Valid @RequestBody QuizResponse response) {
        ResultResponse<QuizSubmission> resultResponse = new ResultResponse<>();
            QuizSubmission submission = submissionService.getResultByQuizIdAndUserId(response.getQuizId().toString(), response.getUserId().toString());
            resultResponse.setSuccess(true);
            resultResponse.setMessage("Submission retrieved successfully");
            resultResponse.setData(submission);
            resultResponse.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(resultResponse, HttpStatus.OK);
        
    }
 
    
    
    /*
     * Method to view all submissions irrespective of user id for a quiz
     */
    
    
    @GetMapping("/viewAll")
    public ResponseEntity<ResultResponse<List<QuizSubmission>>> getAllSubmissionsByQuizId( @RequestBody QuizResponse response) {
    	System.out.println("hhhhhhhhhhhhhh");
        ResultResponse<List<QuizSubmission>> resultResponse = new ResultResponse<>();
            List<QuizSubmission> submissions = submissionService.getAllSubmissionsByQuizId(response.getQuizId());
            resultResponse.setSuccess(true);
            resultResponse.setMessage("Submissions retrieved successfully");
            resultResponse.setData(submissions);
            resultResponse.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(resultResponse, HttpStatus.OK);
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //////integartion
    
    
    
    
 
    
    @GetMapping("/QuizSubmissionDetails")
    public Map<String,Double> getQuizByStudentIdAndQuizId(@RequestParam String studentId,@RequestParam String quizId) {
    	return submissionService.getQuizByStudentIdAndQuizId(studentId,quizId);
    }
}