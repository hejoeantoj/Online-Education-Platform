package com.cts.quizmodule.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.quizmodule.dto.QuizDTO;
import com.cts.quizmodule.dto.UpdateQuizDTO;
import com.cts.quizmodule.exceptions.DuplicateQuestionException;
import com.cts.quizmodule.exceptions.DuplicateQuizException;
import com.cts.quizmodule.exceptions.InstructorNotAllowedException;
import com.cts.quizmodule.exceptions.QuizNotFoundException;
import com.cts.quizmodule.model.Quiz;
import com.cts.quizmodule.service.QuizService;
import com.cts.quizmodule.utils.ResultResponse;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;
    
    
    

    /*
     * Method for creating new quiz 
     */
    
     @PostMapping("/create")
     public ResponseEntity<ResultResponse<Quiz>> createQuiz(@Valid @RequestBody QuizDTO quizdto) {
         ResultResponse<Quiz> response = new ResultResponse<>();
         
             Quiz createdQuiz = quizService.createQuiz(quizdto);
             response.setSuccess(true);
             response.setMessage("Quiz created successfully");
             response.setData(createdQuiz);
             response.setStatus(HttpStatus.OK);
             return new ResponseEntity<>(response, HttpStatus.OK);
  
         
       }

    /*
     * Method For Viewing all the quizzes
     */
   @GetMapping("/viewAll")
   public ResponseEntity<ResultResponse<List<Quiz>>> getAllByQuiz(@RequestParam String courseId) {
 
        ResultResponse<List<Quiz>> response = new ResultResponse<>();
            List<Quiz> quiz = quizService.getAllQuiz(courseId);
            response.setSuccess(true);
            response.setMessage("Quizzes retrieved successfully");
            response.setData(quiz);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
       
    }
 
     
     
     

    /*
     * Method to Delete Any quiz by Id
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ResultResponse<String>> deleteQuiz( @RequestParam UUID quizId) {
        ResultResponse<String> response = new ResultResponse<>();
            quizService.deleteQuiz(quizId);
            response.setSuccess(true);
            response.setMessage("Quiz Deleted");
            response.setData("Quiz Deleted");
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
       
       }
 

    /* 
     * Updating an existing quiz by using quiz Id
     */
    
    
    @PutMapping("/update")
    public ResponseEntity<ResultResponse<Quiz>> updateTotalMarks(@RequestBody UpdateQuizDTO quizDTO) {
        ResultResponse<Quiz> response = new ResultResponse<>();
            Quiz updatedQuiz = quizService.updateTotalMarks(quizDTO);
            response.setSuccess(true);
            response.setMessage("Quiz updated successfully");
            response.setData(updatedQuiz);
            response.setStatus(HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
      
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
  //for integration
    @GetMapping("/QuizDetails")
    public List<String> getAllQuizByCourseId(@RequestParam String courseId) {
    
    	return quizService.getAllQuizByCourseId(courseId);
    }
}