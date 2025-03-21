package com.cts.assignmentmodule.exceptions;

public class CourseNotFoundException extends RuntimeException {
       public CourseNotFoundException(String message) {
    	   super(message);
       }
}
