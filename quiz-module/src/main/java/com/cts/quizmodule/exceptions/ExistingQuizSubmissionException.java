package com.cts.quizmodule.exceptions;

public class ExistingQuizSubmissionException extends RuntimeException{
	
	public ExistingQuizSubmissionException(String message)
	{
		super(message);
	}

}
