package com.cts.coursemodule.dto;
 
import java.util.UUID;
 
import com.cts.coursemodule.enums.Category;
 
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
 
public class CourseDTO {
 
	private UUID courseId;
 
	@NotEmpty(message = "Course Title cannot be Empty")
	private String courseTitle;
 
	@NotEmpty(message = "Description cannot be Empty")
	private String description;
 
	@NotNull(message = "Instructor ID cannot be null")
	private UUID instructorId;
 
	@NotNull(message = "Category cannot be null")
	private Category category;
 
	@NotNull(message = "Duration is Mandatory")
	@Min(value = 1, message = "Duration should be Greater than 1 week")
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
 