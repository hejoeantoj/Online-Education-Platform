package com.cts.coursemodule.exception;

public class CourseAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public CourseAlreadyExistsException(String message) {
        super(message);
    }
}
