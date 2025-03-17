package com.cts.assignmentmodule.advice;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cts.assignmentmodule.exceptions.DuplicateAssignmentException;
import com.cts.assignmentmodule.exceptions.UserInputInvalidException;

@RestControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(DuplicateAssignmentException.class)
    public ResponseEntity<String> handleDuplicateAssignmentException(DuplicateAssignmentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(UserInputInvalidException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(UserInputInvalidException ex) {
        String errorMessage = "Invalid input format. Please ensure all fields are correctly formatted.";
        if (ex instanceof UserInputInvalidException) {
            errorMessage = ex.getMessage();
        }
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
    // Handle other exceptions
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleException(Exception ex) {
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}