package com.cts.coursemodule.advice;
 
import java.util.HashMap;

import java.util.Map;
 
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cts.coursemodule.dto.ResultResponse;
import com.cts.coursemodule.exception.AlreadyEnrolledException;

import com.cts.coursemodule.exception.CourseAlreadyExistsException;

import com.cts.coursemodule.exception.CourseNotFoundException;

import com.cts.coursemodule.exception.InstructorNotAllowedException;

import com.cts.coursemodule.exception.InstructorNotFoundException;

import com.cts.coursemodule.exception.LessonAlreadyExistsException;

import com.cts.coursemodule.exception.LessonNotFoundException;
import com.cts.coursemodule.exception.StudentNotFoundException;

import org.springframework.http.converter.HttpMessageNotReadableException;
 
@RestControllerAdvice
 
public class GlobalExceptionHandler {
 
	@ExceptionHandler(MethodArgumentNotValidException.class)

	public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {

		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors()

				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

		return ResponseEntity.badRequest().body(errors);

	}
 
	

	@ExceptionHandler(AlreadyEnrolledException.class)

    public ResponseEntity<ResultResponse<Void>> handleGeneralException(AlreadyEnrolledException ex) {

        ResultResponse<Void> response = new ResultResponse<>();

        response.setSuccess(false);

        response.setMessage("User already enrolled in this course");

        response.setStatus(HttpStatus.CONFLICT);

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);

    }

	@ExceptionHandler(CourseAlreadyExistsException.class)

    public ResponseEntity<ResultResponse<Void>> handleGeneralException(CourseAlreadyExistsException ex) {

        ResultResponse<Void> response = new ResultResponse<>();

        response.setSuccess(false);

        response.setMessage("Course already exists");

        response.setStatus(HttpStatus.CONFLICT);

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);

    }

	@ExceptionHandler(CourseNotFoundException.class)

    public ResponseEntity<ResultResponse<Void>> handleGeneralException(CourseNotFoundException ex) {

        ResultResponse<Void> response = new ResultResponse<>();

        response.setSuccess(false);

        response.setMessage("Course Not found");

        response.setStatus(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

    }

	@ExceptionHandler(InstructorNotAllowedException.class)

    public ResponseEntity<ResultResponse<Void>> handleGeneralException(InstructorNotAllowedException ex) {

        ResultResponse<Void> response = new ResultResponse<>();

        response.setSuccess(false);

        response.setMessage("Instructor Not allowed");

        response.setStatus(HttpStatus.FORBIDDEN);

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);

    }

	@ExceptionHandler(InstructorNotFoundException.class)

    public ResponseEntity<ResultResponse<Void>> handleGeneralException(InstructorNotFoundException ex) {

        ResultResponse<Void> response = new ResultResponse<>();

        response.setSuccess(false);

        response.setMessage("Instructor Not Found");

        response.setStatus(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

    }

	@ExceptionHandler(LessonAlreadyExistsException.class)

    public ResponseEntity<ResultResponse<Void>> handleGeneralException(LessonAlreadyExistsException ex) {

        ResultResponse<Void> response = new ResultResponse<>();

        response.setSuccess(false);

        response.setMessage("Lesson Already Exists");

        response.setStatus(HttpStatus.CONFLICT);

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);

    }

	@ExceptionHandler(LessonNotFoundException.class)

    public ResponseEntity<ResultResponse<Void>> handleGeneralException(LessonNotFoundException ex) {

        ResultResponse<Void> response = new ResultResponse<>();

        response.setSuccess(false);

        response.setMessage("Lesson not found for this course");

        response.setStatus(HttpStatus.OK);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

	@ExceptionHandler(StudentNotFoundException.class)

    public ResponseEntity<ResultResponse<Void>> handleGeneralException(StudentNotFoundException ex) {

        ResultResponse<Void> response = new ResultResponse<>();

        response.setSuccess(false);

        response.setMessage("No Student Found");

        response.setStatus(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

    }

	@ExceptionHandler(HttpMessageNotReadableException.class)

    public ResponseEntity<ResultResponse<Void>> HttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        ResultResponse<Void> response = new ResultResponse<>();

        response.setSuccess(false);

        response.setMessage("UUID INVALID ");

        response.setStatus(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

}

 