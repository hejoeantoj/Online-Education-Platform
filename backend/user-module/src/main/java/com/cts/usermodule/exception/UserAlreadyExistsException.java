package com.cts.usermodule.exception;

public class UserAlreadyExistsException extends RuntimeException {
	
	public UserAlreadyExistsException(String message) {
		super(message);
	}
}


