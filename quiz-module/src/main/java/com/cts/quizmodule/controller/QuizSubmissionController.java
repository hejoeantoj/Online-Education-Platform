package com.cts.quizmodule.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/api/v1")
public class QuizSubmissionController {

    @Autowired
    private QuizSubmissionService submissionService;

    /*
     * Method to Add their Submission by user id and quiz id
     */
    @PutMapping("quizzes/submitQuiz")
    public ResponseEntity<ResultResponse<QuizSubmission>> submitAnswer(@Valid @RequestBody QuizResponse response) {
        ResultResponse<QuizSubmission> resultResponse = new ResultResponse<>();
        try {
            QuizSubmission submission = submissionService.addSubmission(response);
            resultResponse.setSuccess(true);
            resultResponse.setMessage("Submission added successfully");
            resultResponse.setData(submission);
            resultResponse.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(resultResponse, HttpStatus.OK);
        } catch (QuizNotFoundException e) {
            resultResponse.setSuccess(false);
            resultResponse.setMessage(e.getMessage());
            resultResponse.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(resultResponse, HttpStatus.OK);
        } 
        catch (ExistingQuizSubmissionException e) {
            resultResponse.setSuccess(false);
            resultResponse.setMessage("you're already attempted ");
            resultResponse.setStatus(HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(resultResponse, HttpStatus.FORBIDDEN);
        }
        catch (StudentNotEnrolledException e) {
            resultResponse.setSuccess(false);
            resultResponse.setMessage(e.getMessage());
            resultResponse.setStatus(HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(resultResponse, HttpStatus.FORBIDDEN);
        }
    }

    /*
     * Method to view submission result of a user for specific quiz id
     */
    
    
    @GetMapping("/quizzes/viewSubmissionsResult")
    public ResponseEntity<ResultResponse<QuizSubmission>> courseSubmissions(@Valid @RequestBody QuizResponse response) {
        ResultResponse<QuizSubmission> resultResponse = new ResultResponse<>();
        try {
            QuizSubmission submission = submissionService.getResultByQuizIdAndUserId(response.getQuizId().toString(), response.getUserId().toString());
            resultResponse.setSuccess(true);
            resultResponse.setMessage("Submission retrieved successfully");
            resultResponse.setData(submission);
            resultResponse.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(resultResponse, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            resultResponse.setSuccess(false);
            resultResponse.setMessage("Submission not found");
            resultResponse.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(resultResponse, HttpStatus.NOT_FOUND);
        } catch (QuizNotFoundException e) {
            resultResponse.setSuccess(false);
            resultResponse.setMessage("Quiz not found for given ID");
            resultResponse.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(resultResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            resultResponse.setSuccess(false);
            resultResponse.setMessage("An unexpected error occurred");
            resultResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(resultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    /*
     * Method to view all submissions irrespective of user id for a quiz
     */
    
    
    @GetMapping("/quizzes/getAllSubmissionsByQuizId")
    public ResponseEntity<ResultResponse<List<QuizSubmission>>> getAllSubmissionsByQuizId( @RequestBody QuizResponse response) {
        ResultResponse<List<QuizSubmission>> resultResponse = new ResultResponse<>();
        try {
            List<QuizSubmission> submissions = submissionService.getAllSubmissionsByQuizId(response.getQuizId());
            resultResponse.setSuccess(true);
            resultResponse.setMessage("Submissions retrieved successfully");
            resultResponse.setData(submissions);
            resultResponse.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(resultResponse, HttpStatus.OK);
        } catch (QuizNotFoundException e) {
            resultResponse.setSuccess(false);
            resultResponse.setMessage("quiz not found for given quiz id");
            resultResponse.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(resultResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            resultResponse.setSuccess(false);
            resultResponse.setMessage("An unexpected error occurred");
            resultResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(resultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}