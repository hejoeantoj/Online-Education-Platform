package com.cts.assignmentmodule.dto;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Validated
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssignmentSubmissionDTO {

    @NotNull(message = "Student ID cannot be blank")
    private UUID studentId;

    @NotNull(message = "Assignment ID cannot be blank")
    private UUID assignmentId;

    //(message = "File cannot be null")
    private MultipartFile file;

    @PositiveOrZero(message = "Obtained marks must be positive or zero")
    private Double obtainedMarks;

    @Size(max = 500, message = "Feedback cannot exceed 500 characters")
    private String feedback;
    
    private String instructorId;

    public String getInstructorId() {
		return instructorId;
	}

	public void setInstructorId(String instructorId) {
		this.instructorId = instructorId;
	}

	// Getters and Setters
    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public UUID getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(UUID assignmentId) {
        this.assignmentId = assignmentId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Double getObtainedMarks() {
        return obtainedMarks;
    }

    public void setObtainedMarks(Double obtainedMarks) {
        this.obtainedMarks = obtainedMarks;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}