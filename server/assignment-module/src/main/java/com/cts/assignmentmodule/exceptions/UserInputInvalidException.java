package com.cts.assignmentmodule.exceptions;

import org.springframework.http.converter.HttpMessageNotReadableException;

public class UserInputInvalidException extends HttpMessageNotReadableException{
	public UserInputInvalidException(String message) {
		super(message);
	}

}
