package com.cts.coursemodule.dto;

import java.util.UUID;

import org.springframework.validation.annotation.Validated;

import com.cts.coursemodule.enums.Category;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



public class CourseDTO {
	
	private UUID courseId;
	
	@NotNull(message="Course Title is Mandatory")
	@NotBlank(message="Course Title should not be null")
	private String courseTitle;
	
	@NotNull(message="Course Title is Mandatory")
	@NotBlank(message="Description should not be null")
	private String description;
	
	
	private UUID instructorId;
	
	
	private Category category;	
	
	@NotNull(message="Duration is Mandatory")
	@Min(value = 1,message=" Greater than 1 week")
	private Integer duration;
	
	
	
	public UUID getCourseId() {
		return courseId;
	}
	public void setCourseId(UUID courseId) {
		this.courseId = courseId;
	}
	
	public String getCourseTitle() {
		return courseTitle;
	}
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public UUID getInstructorId() {
		return instructorId;
	}
	public void setInstructorId(UUID instructorId) {
		this.instructorId = instructorId;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	
	
}