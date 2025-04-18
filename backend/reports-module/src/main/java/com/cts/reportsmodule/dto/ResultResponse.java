package com.cts.reportsmodule.dto;

import org.springframework.http.HttpStatus;

public class ResultResponse<T> {
 
	private boolean success;
    private String message;
    private T data;
    private HttpStatus status;
	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}
	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}
	 public HttpStatus getStatus() {
	        return status;
	    }

	    public void setStatus(HttpStatus status) {
	        this.status = status;
	    }
	
	
}