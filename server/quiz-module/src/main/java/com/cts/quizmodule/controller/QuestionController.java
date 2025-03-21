package com.cts.quizmodule.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.quizmodule.dto.QuestionDTO;
import com.cts.quizmodule.exceptions.DuplicateQuestionException;
import com.cts.quizmodule.exceptions.InstructorNotAllowedException;
import com.cts.quizmodule.exceptions.QuizNotFoundException;
import com.cts.quizmodule.model.Question;
import com.cts.quizmodule.service.QuestionService;
import com.cts.quizmodule.utils.ResultResponse;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /*
     * Method for Creating Questions for that particular Quiz Id
     * In questionDto itself we're having quiz id
     */
    @PostMapping("/create")
    public ResponseEntity<ResultResponse<Question>> createQuestion(@Valid @RequestBody QuestionDTO question) {
        ResultResponse<Question> response = new ResultResponse<>();
            Question createdQuestion = questionService.createQuestion(question);
            response.setSuccess(true);
            response.setMessage("Question created successfully");
            response.setData(createdQuestion);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }
 
 

    /*
     * Method for Viewing all Questions for that Particular Quiz Id
     */

 

    /*
     * "API" to view all questions by quiz id
     */
    @GetMapping("/view")
    public ResponseEntity<ResultResponse<List<Question>>> getAllQuestionsById( @RequestParam String quizId) {
        ResultResponse<List<Question>> response = new ResultResponse<>();
            List<Question> questions = questionService.getAllQuestionsById(quizId);
            response.setSuccess(true);
            response.setMessage("Questions retrieved successfully");
            response.setData(questions);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        
    }
 
}