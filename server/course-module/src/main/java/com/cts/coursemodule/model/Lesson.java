package com.cts.coursemodule.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name="lesson")
public class Lesson {
	
	@Id
	private String lessonId;
	
	private String lessonTitle;
	
	public String getLessonTitle() {
		return lessonTitle;
	}

	public void setLessonTitle(String lessonTitle) {
		this.lessonTitle = lessonTitle;
	}

	@ManyToOne
    @JoinColumn(name="courseId", nullable = false)
	@JsonIgnore
    private Course course;
	
	@Column(name = "content", columnDefinition = "LONGTEXT")
	private String content;

	
	@PrePersist
	public void generateLessonId() {
		this.lessonId=UUID.randomUUID().toString();
	}

	/**
	 * @return the lessonId
	 */
	public String getLessonId() {
		return lessonId;
	}

	/**
	 * @param lessonId the lessonId to set
	 */
	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}

	/**
	 * @return the course
	 */
	public Course getCourse() {
		return course;
	}

	/**
	 * @param course the course to set
	 */
	public void setCourse(Course course) {
		this.course = course;
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

	/**
	 * @return the resource
	 */
	
	
}
