package com.cts.communicationmodule.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;


@Entity
@Table(name="notification")
public class Notification {
	
	@Id
	private String notificationId;
	private String message;
	private LocalDateTime createdAt = LocalDateTime.now();
	
	
	
	
	public Notification() {}
	
	public Notification(String message) {
		this.message = message;
	}

	@PrePersist
	public void generateUUID() {
		if(this.notificationId==null) {
			this.notificationId=UUID.randomUUID().toString();
		}
	}

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	
//	private String courseId;
//
//
//
//
//	public String getCourseId() {
//		return courseId;
//	}
//
//	public void setCourseId(String courseId) {
//		this.courseId = courseId;
//	}
	
//	public Notification(String message,String courseId) {
//		this.message = message;
//		this.courseId=courseId;
//	}
	
}
