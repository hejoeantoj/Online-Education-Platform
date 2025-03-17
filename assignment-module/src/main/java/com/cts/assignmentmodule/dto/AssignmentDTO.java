package com.cts.assignmentmodule.dto;

import java.util.UUID;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Validated
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssignmentDTO {
    
    @NotNull(message = "Courseobj can't be blank")
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format")
    @Size(min = 36, max = 36, message = "courseId must be exactly 36 characters long")
    private UUID courseId;
    
    @Min(value = 1, message = "Total marks must be at least 1")
    @NotNull(message="give correct column name")
    private Integer totalMarks;
    
    @NotNull(message = "Question field cannot be blank")
    @NotBlank(message="give value")
    private String question;
    
    @NotNull(message = "Assignment ID cannot be blank")
    private UUID assignmentId;

    // Getters and Setters
    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public UUID getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(UUID assignmentId) {
        this.assignmentId = assignmentId;
    }
}