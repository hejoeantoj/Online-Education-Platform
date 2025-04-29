package com.cts.usermodule.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cts.usermodule.dto.Response;
import com.cts.usermodule.exception.EmailAlreadyExistsException;
import com.cts.usermodule.exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Response<?>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        Response<?> response = new Response<>();
        response.setSuccess(false);
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setErrorMessage("Email Already Exists");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Response<?>> handleUserNotFoundException(UserNotFoundException ex) {
        Response<?> response = new Response<>();
        response.setSuccess(false);
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setErrorMessage("User not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<?>> handleGenericException(Exception ex) {
        Response<?> response = new Response<>();
        response.setSuccess(false);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setErrorMessage("An unexpected error occurred");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}