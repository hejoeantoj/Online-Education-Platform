package com.cts.quizmodule.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.quizmodule.dto.QuizResponse;
import com.cts.quizmodule.exceptions.QuizNotFoundException;
import com.cts.quizmodule.exceptions.UserNotFoundException;
import com.cts.quizmodule.model.QuizSubmission;
import com.cts.quizmodule.service.QuizSubmissionService;
import com.cts.quizmodule.utils.ResultResponse;

@RestController
@RequestMapping("/api/v1")
public class QuizSubmissionController {

    @Autowired
    private QuizSubmissionService submissionService;

    /*
     * Method to Add their Submission by user id and quiz id
     */
    @PostMapping("/quizzes/responses")
    public ResponseEntity<ResultResponse<QuizSubmission>> submitAnswer(@RequestBody QuizResponse response) {
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
        } catch (Exception e) {
            resultResponse.setSuccess(false);
            resultResponse.setMessage("An unexpected error occurred");
            resultResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(resultResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Method to view submission result of a user for specific quiz id
     */
    @PostMapping("/quizzes/quizId/userId/viewSubmissionsResult")
    public ResponseEntity<ResultResponse<QuizSubmission>> courseSubmissions(@RequestBody QuizResponse response) {
        ResultResponse<QuizSubmission> resultResponse = new ResultResponse<>();
        try {
            QuizSubmission submission = submissionService.getResultByQuizIdAndUserId(response.getQuizId(), response.getUserId());
            if (submission != null) {
                resultResponse.setSuccess(true);
                resultResponse.setMessage("Submission retrieved successfully");
                resultResponse.setData(submission);
                resultResponse.setStatus(HttpStatus.OK);
                return new ResponseEntity<>(resultResponse, HttpStatus.OK);
            } else {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("Submission not found");
                resultResponse.setStatus(HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(resultResponse, HttpStatus.NOT_FOUND);
            }
        } catch (QuizNotFoundException e) {
            resultResponse.setSuccess(false);
            resultResponse.setMessage("quiz not found for given id");
            resultResponse.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(resultResponse, HttpStatus.OK);
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
    @PostMapping("/quizzes/quizId/getAllSubmissionsByQuizId")
    public ResponseEntity<ResultResponse<List<QuizSubmission>>> getAllSubmissionsByQuizId(@RequestBody QuizResponse response) {
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