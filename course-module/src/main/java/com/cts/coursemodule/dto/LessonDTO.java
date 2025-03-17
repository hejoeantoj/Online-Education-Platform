package com.cts.coursemodule.dto;

import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Validated
@JsonIgnoreProperties(ignoreUnknown=true)
public class LessonDTO {
	
    private UUID lessonId;
    
    private String lessonTitle;

	
	private UUID courseId;
	
	private UUID instructorId;
	
	
	public UUID getInstructorId() {
		return instructorId;
	}

	public void setInstructorId(UUID instructorId) {
		this.instructorId = instructorId;
	}

	private String content;
	
	
	
	/**
	 * @return the courseId
	 */
	public UUID getCourseId() {
		return courseId;
	}
	
	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(UUID courseId) {
		this.courseId = courseId;
	}
	
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	public UUID getLessonId() {
		return lessonId;
	}

	public void setLessonId(UUID lessonId) {
		this.lessonId = lessonId;
	}
	
	public String getLessonTitle() {
		return lessonTitle;
	}

	public void setLessonTitle(String lessonTitle) {
		this.lessonTitle = lessonTitle;
	}


	
	
}
