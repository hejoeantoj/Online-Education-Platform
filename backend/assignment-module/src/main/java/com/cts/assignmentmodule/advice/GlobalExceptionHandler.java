package com.cts.assignmentmodule.advice;
 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
 
import com.cts.assignmentmodule.exceptions.AlreadyMarksAssignedException;
import com.cts.assignmentmodule.exceptions.AssignmentNotFoundException;
import com.cts.assignmentmodule.exceptions.AssignmentSubmissionNotFoundException;
import com.cts.assignmentmodule.exceptions.CourseNotFoundException;
import com.cts.assignmentmodule.exceptions.DuplicateAssignmentException;
import com.cts.assignmentmodule.exceptions.DuplicateAssignmentSubmissionException;
import com.cts.assignmentmodule.exceptions.InstructorNotAllowedException;
import com.cts.assignmentmodule.exceptions.InvalidMarksException;
import com.cts.assignmentmodule.exceptions.StudentNotEnrolledException;
 
import com.cts.assignmentmodule.utils.ResultResponse;
 
import java.util.HashMap;
import java.util.Map;
 

@RestControllerAdvice
public class GlobalExceptionHandler {
 
    // Handles All Controller's Validation Errors
 
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }
 
    // Handles Invalid UUID's (more or less chars in UUID)
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResultResponse<Void>> HttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ResultResponse<Void> response = new ResultResponse<>();
        response.setSuccess(false);
        response.setMessage("UUID INVALID ");
        response.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
 
    // Handles Null pointer Exception When we don't use valid in controller it checks field names
  
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ResultResponse<Void>> NullPointerException(NullPointerException ex) {
        ResultResponse<Void> response = new ResultResponse<>();
        response.setSuccess(false);
        response.setMessage("give valid field names");
        response.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
 
    // Handles AlreadyMarksAssignedException--If Instructor has Already Assigned the Marks to the student
    
    
    @ExceptionHandler(AlreadyMarksAssignedException.class)
    public ResponseEntity<ResultResponse<Void>> handleGeneralException(AlreadyMarksAssignedException ex) {
        ResultResponse<Void> response = new ResultResponse<>();
        response.setSuccess(false);
        response.setMessage("Marks Already Assigned");
        response.setStatus(HttpStatus.CONFLICT);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
 
    // Handles AssignmentNotFoundException
    
    
    @ExceptionHandler(AssignmentNotFoundException.class)
    public ResponseEntity<ResultResponse<Void>> handleGeneralException(AssignmentNotFoundException ex) {
    	ex.printStackTrace();
        ResultResponse<Void> response = new ResultResponse<>();
        response.setSuccess(false);
        response.setMessage("Assignment not found");
        response.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
 
    // Handles AssignmentSubmissionNotFoundException--If user Enrolled Course But hasn't taken the Assignment
    
    
    @ExceptionHandler(AssignmentSubmissionNotFoundException.class)
    public ResponseEntity<ResultResponse<Void>> handleGeneralException(AssignmentSubmissionNotFoundException ex) {
        ResultResponse<Void> response = new ResultResponse<>();
        response.setSuccess(false);
        response.setMessage("Submission not found");
        response.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
 
    // Handles CourseNotFoundException
    
    
    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<ResultResponse<Void>> handleGeneralException(CourseNotFoundException ex) {
        ResultResponse<Void> response = new ResultResponse<>();
        response.setSuccess(false);
        response.setMessage("Course not found");
        response.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
 
    // Handles DuplicateAssignmentSubmissionException---If User Already Submitted the Assignment
    
    
    @ExceptionHandler(DuplicateAssignmentSubmissionException.class)
    public ResponseEntity<ResultResponse<Void>> handleGeneralException(DuplicateAssignmentSubmissionException ex) {
        ResultResponse<Void> response = new ResultResponse<>();
        response.setSuccess(false);
        response.setMessage("Duplicate Assignment Submission found");
        response.setStatus(HttpStatus.CONFLICT);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
 
    // Handles InstructorNotAllowedException--If Instructor Hasn't Created The course He Can't Deal with Assignments
    
    @ExceptionHandler(InstructorNotAllowedException.class)
    public ResponseEntity<ResultResponse<Void>> handleGeneralException(InstructorNotAllowedException ex) {
        ResultResponse<Void> response = new ResultResponse<>();
        response.setSuccess(false);
        response.setMessage("Instructor not allowed ");
        response.setStatus(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
 
    // Handles InvalidMarksException----If Instructor Allots marks more Than total Marks
    
    
    @ExceptionHandler(InvalidMarksException.class)
    public ResponseEntity<ResultResponse<Void>> handleGeneralException(InvalidMarksException ex) {
        ResultResponse<Void> response = new ResultResponse<>();
        response.setSuccess(false);
        response.setMessage("Assigning marks is greater than total marks");
        response.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
 
    // Handles StudentNotEnrolledException--- If student hasn't enrolled in the course
    
    
    @ExceptionHandler(StudentNotEnrolledException.class)
    public ResponseEntity<ResultResponse<Void>> handleGeneralException(StudentNotEnrolledException ex) {
        ResultResponse<Void> response = new ResultResponse<>();
        response.setSuccess(false);
        response.setMessage("Student not enrolled in course");
        response.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    
    @ExceptionHandler(DuplicateAssignmentException.class)
    public ResponseEntity<ResultResponse<Void>> handleGeneralException(DuplicateAssignmentException ex) {
        ResultResponse<Void> response = new ResultResponse<>();
        response.setSuccess(false);
        response.setMessage("Assignment Question Already Exists");
        response.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
 
 