package com.cts.coursemodule.dto;
 
import java.util.UUID;
 
import org.springframework.validation.annotation.Validated;
 
import jakarta.validation.constraints.NotNull;
 
@Validated
 
public class EnrollmentDTO {
	
	@NotNull(message = "Course ID cannot be null")
	private UUID courseId;
	
	@NotNull(message = "Student ID cannot be null")
	private UUID studentId;
	
	public UUID getCourseId() {
		return courseId;
	}
	public void setCourseId(UUID courseId) {
		this.courseId = courseId;
	}
	public UUID getStudentId() {
		return studentId;
	}
	public void setStudentId(UUID studentId) {
		this.studentId = studentId;
	}
	
	
 
}
 