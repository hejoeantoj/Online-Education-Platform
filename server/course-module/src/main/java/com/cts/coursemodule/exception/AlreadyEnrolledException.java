package com.cts.coursemodule.exception;

public class AlreadyEnrolledException extends RuntimeException {
    private static final long serialVersionUID = 1L;

	public AlreadyEnrolledException(String message) {
        super(message);
    }
}