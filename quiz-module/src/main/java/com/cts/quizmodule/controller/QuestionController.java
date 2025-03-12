package com.cts.quizmodule.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.quizmodule.dto.QuestionDTO;
import com.cts.quizmodule.exceptions.DuplicateQuestionException;
import com.cts.quizmodule.exceptions.QuizNotFoundException;
import com.cts.quizmodule.model.Question;
import com.cts.quizmodule.service.QuestionService;
import com.cts.quizmodule.utils.ResultResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /*
     * Method for Creating Questions for that particular Quiz Id
     * In questionDto itself we're having quiz id
     */
    @PostMapping("/quizzes/quizId/createQuestion")
    public ResponseEntity<ResultResponse<Question>> createQuestion(@RequestBody QuestionDTO question) {
        ResultResponse<Question> response = new ResultResponse<>();
        try {
            Question createdQuestion = questionService.createQuestion(question);
            response.setSuccess(true);
            response.setMessage("Question created successfully");
            response.setData(createdQuestion);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DuplicateQuestionException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.CONFLICT);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } catch (QuizNotFoundException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("An unexpected error occurred");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Method for Viewing all Questions for that Particular Quiz Id
     */
    @GetMapping("quizzes/getAllQuestions")
    public ResponseEntity<ResultResponse<List<Question>>> getAllQuestions() {
        ResultResponse<List<Question>> response = new ResultResponse<>();
        try {
            List<Question> questions = questionService.getAllQuestions();
            response.setSuccess(true);
            response.setMessage("Questions retrieved successfully");
            response.setData(questions);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error Retreving questions");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * "API" to view all questions by quiz id
     */
    @PostMapping("quizzes/quizId/viewQuestionsByQuizId")
    public ResponseEntity<ResultResponse<List<Question>>> getAllQuestionsById(@RequestBody QuestionDTO questionDTO) {
        ResultResponse<List<Question>> response = new ResultResponse<>();
        try {
            List<Question> questions = questionService.getAllQuestionsById(questionDTO.getQuizId());
            response.setSuccess(true);
            response.setMessage("Questions retrieved successfully");
            response.setData(questions);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (QuizNotFoundException e) {
            response.setSuccess(false);
            response.setMessage("quiz not found for given quiz id");
            response.setStatus(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("error occurred while retrieving values");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}