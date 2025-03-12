package com.cts.assignmentmodule.dto;



import org.springframework.web.multipart.MultipartFile;

public class SubmissionDTO {
 
    private int userId;
    private int assignmentId;
    private MultipartFile file;
	/**
	 * @return the files
	 */
	public MultipartFile getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	/**
	 * @return the assignmentId
	 */
	public int getAssignmentId() {
		return assignmentId;
	}
	/**
	 * @param assignmentId the assignmentId to set
	 */
	public void setAssignmentId(int assignmentId) {
		this.assignmentId = assignmentId;
	}
    
}