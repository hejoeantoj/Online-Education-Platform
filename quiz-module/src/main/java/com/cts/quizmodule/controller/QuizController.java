package com.cts.quizmodule.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.quizmodule.dto.QuizDTO;
import com.cts.quizmodule.exceptions.DuplicateQuestionException;
import com.cts.quizmodule.exceptions.QuizNotFoundException;
import com.cts.quizmodule.model.Quiz;
import com.cts.quizmodule.service.QuizService;
import com.cts.quizmodule.utils.ResultResponse;

@RestController
@RequestMapping("/api/v1")
public class QuizController {

    @Autowired
    private QuizService quizService;

    /*
     * Method for creating new quiz 
     */
    
     @PostMapping("quizzes/createQuiz")
    public ResponseEntity<ResultResponse<Quiz>> createQuiz(@RequestBody QuizDTO quiz) {
        ResultResponse<Quiz> response = new ResultResponse<>();
        try {
            Quiz createdQuiz = quizService.createQuiz(quiz);
            response.setSuccess(true);
            response.setMessage("Quiz created successfully");
            response.setData(createdQuiz);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch ( DuplicateQuestionException e) {
            response.setSuccess(false);
            response.setMessage("cant create quiz due to duplicate question ...");
            response.setStatus(HttpStatus.CONFLICT);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } 
    }

    /*
     * Method For Viewing all the quizzes
     */
    @GetMapping("quizzes/viewAll")
    public ResponseEntity<ResultResponse<List<Quiz>>> getAllQuizzes() {
        ResultResponse<List<Quiz>> response = new ResultResponse<>();
        try {
            List<Quiz> quizzes = quizService.getAllQuiz();
            response.setSuccess(true);
            response.setMessage("Quizzes retrieved successfully");
            response.setData(quizzes);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("An unexpected error occurred");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Method to Delete Any quiz by Id
     */
    @DeleteMapping("quizzes/deleteByquizid")
    public ResponseEntity<ResultResponse<String>> deleteQuiz(@RequestParam String quizId) {
        ResultResponse<String> response = new ResultResponse<>();
        try {
            quizService.deleteQuiz(quizId);
            response.setSuccess(true);
            response.setMessage("Quiz Deleted");
            response.setData("Quiz Deleted");
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (QuizNotFoundException e) {
            response.setSuccess(false);
            response.setMessage("Quiz Not found deletion error");
            response.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("An unexpected error occurred");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* 
     * Updating an existing quiz by using quiz Id
     */
    @PutMapping("quizzes/quizId/updateQuizByTotalMarks")
    public ResponseEntity<ResultResponse<Quiz>> updateTotalMarks(@RequestBody QuizDTO quizDTO) {
        ResultResponse<Quiz> response = new ResultResponse<>();
        try {
            Quiz updatedQuiz = quizService.updateTotalMarks(quizDTO);
            response.setSuccess(true);
            response.setMessage("Quiz updated successfully");
            response.setData(updatedQuiz);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("An unexpected error occurred");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}