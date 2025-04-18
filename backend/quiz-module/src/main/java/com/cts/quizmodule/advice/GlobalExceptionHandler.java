package com.cts.quizmodule.advice;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
 
import com.cts.quizmodule.exceptions.CourseNotFoundException;

import com.cts.quizmodule.exceptions.DuplicateQuestionException;

import com.cts.quizmodule.exceptions.DuplicateQuizException;

import com.cts.quizmodule.exceptions.ExistingQuizSubmissionException;

import com.cts.quizmodule.exceptions.InstructorNotAllowedException;

import com.cts.quizmodule.exceptions.QuizNotFoundException;

import com.cts.quizmodule.exceptions.StudentNotEnrolledException;

import com.cts.quizmodule.exceptions.SubmissionNotFoundException;

import com.cts.quizmodule.utils.ResultResponse;
 
import org.springframework.validation.FieldError;
 
import java.util.HashMap;

import java.util.Map;
 

 
@RestControllerAdvice

public class GlobalExceptionHandler {
 
	

	//Handles All Controller's Validation Errors
 
    @ExceptionHandler(MethodArgumentNotValidException.class)

    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {

            String fieldName = ((FieldError) error).getField();

            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);

        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

    }


    //Handles Invalid UUID's ----->more or less chars in UUID


    @ExceptionHandler(HttpMessageNotReadableException.class)

    public ResponseEntity<ResultResponse<Void>> HttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        ResultResponse<Void> response = new ResultResponse<>();

        response.setSuccess(false);

        response.setMessage("Check All fields are correct");

        response.setStatus(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

    //Handles Null pointer Exception When we don't use valid in controller it checks field names

    @ExceptionHandler(NullPointerException.class)

    public ResponseEntity<ResultResponse<Void>> handleIllegalArgumentException(NullPointerException ex) {

        ResultResponse<Void> response = new ResultResponse<>();

        response.setSuccess(false);

        response.setMessage("Invalid field name");

        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    //Handles Quiz Not Found Exception 

    @ExceptionHandler(QuizNotFoundException.class)

	public ResponseEntity<?> handleGeneralException(QuizNotFoundException ex) {

    	ResultResponse<Void> response = new ResultResponse<>();

      response.setSuccess(false);

      response.setMessage("Quiz Not Found");

      response.setStatus(HttpStatus.NOT_FOUND);

      return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

	}


    //Handles CourseNotFoundException

    @ExceptionHandler(CourseNotFoundException.class)

	public ResponseEntity<?> handleGeneralException(CourseNotFoundException ex) {

    	ResultResponse<Void> response = new ResultResponse<>();

      response.setSuccess(false);

      response.setMessage("Course Not Found");

      response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

	}

    //Handles DuplicateQuestionException--if SameQuestion Already Exists

    @ExceptionHandler(DuplicateQuestionException.class)

	public ResponseEntity<?> handleGeneralException(DuplicateQuestionException ex) {

    	ResultResponse<Void> response = new ResultResponse<>();

      response.setSuccess(false);

      response.setMessage("Question Already Exists");

      response.setStatus(HttpStatus.CONFLICT);

      return new ResponseEntity<>(response, HttpStatus.CONFLICT);

	}

    //Handles DuplicateQuizException--->if Quiz Of Title Already Exists

    @ExceptionHandler(DuplicateQuizException.class)

	public ResponseEntity<?> handleGeneralException(DuplicateQuizException ex) {

    	ResultResponse<Void> response = new ResultResponse<>();

      response.setSuccess(false);

      response.setMessage("Quiz Already Exists");

      response.setStatus(HttpStatus.CONFLICT);

      return new ResponseEntity<>(response, HttpStatus.CONFLICT);

	}

    //HandlesExistingQuizSubmission-->If User Already taken the Quiz

    @ExceptionHandler(ExistingQuizSubmissionException.class)

	public ResponseEntity<?> handleGeneralException(ExistingQuizSubmissionException ex) {

    	ResultResponse<Void> response = new ResultResponse<>();

      response.setSuccess(false);

      response.setMessage("submission Already Exists");

      response.setStatus(HttpStatus.BAD_REQUEST);

      return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

    //Handles InstructorNotAllowedException--->if Instructor not created the course

    @ExceptionHandler(InstructorNotAllowedException.class)

	public ResponseEntity<?> handleGeneralException(InstructorNotAllowedException ex) {

    	ResultResponse<Void> response = new ResultResponse<>();

      response.setSuccess(false);

      response.setMessage("Instructor Not allowed");

      response.setStatus(HttpStatus.FORBIDDEN);

      return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);

	}

    //Handles StudentNotEnrolledException--->If Student hasn't Enrolled in Course then he can't take the Quiz

    @ExceptionHandler(StudentNotEnrolledException.class)

   	public ResponseEntity<?> handleGeneralException(StudentNotEnrolledException ex) {

       	ResultResponse<Void> response = new ResultResponse<>();

         response.setSuccess(false);

         response.setMessage("Student Not enrolled in course");

         response.setStatus(HttpStatus.NOT_FOUND);

         return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

   	}


    //Handles SubmisssionNotfoundException---If user Enrolled in Course but hasn't take the Quiz

    @ExceptionHandler(SubmissionNotFoundException.class)

   	public ResponseEntity<?> handleGeneralException(SubmissionNotFoundException ex) {

       	ResultResponse<Void> response = new ResultResponse<>();

         response.setSuccess(false);

         response.setMessage("student submission not found");

         response.setStatus(HttpStatus.NOT_FOUND);

         return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

   	}


    //Handles IllegalArgumentException ---Since Question Field can't be Empty

    @ExceptionHandler(IllegalArgumentException.class)

  public ResponseEntity<ResultResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {

    ResultResponse<Void> response = new ResultResponse<>();

     response.setSuccess(false);

     response.setMessage("question cant be empty");

     response.setStatus(HttpStatus.BAD_REQUEST);

     return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

  }


}
 