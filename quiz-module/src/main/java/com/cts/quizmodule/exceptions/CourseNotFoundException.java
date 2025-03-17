package com.cts.quizmodule.exceptions;

public class CourseNotFoundException extends RuntimeException {
	
	public CourseNotFoundException(String message){
		super(message);
	}

}
