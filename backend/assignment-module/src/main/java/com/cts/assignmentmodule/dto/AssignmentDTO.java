package com.cts.assignmentmodule.dto;
import java.util.UUID;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
 
public class AssignmentDTO {
    
    @NotNull(message = "Give valid Field Name For Course id")
    private UUID courseId;
    
    @Min(value = 1, message = "Total marks must be at least 1")
    @NotNull(message="Give valid Field Name For total Marks")
    private Integer totalMarks;
    
    @NotEmpty(message = "Question field cannot be Empty")
    private String question;
    
    //@NotNull(message = "Assignment ID cannot be null")
    private UUID assignmentId;
    
    @NotNull(message="Give valid Field Name For InstructorId")
    private UUID instructorId;
    
    public UUID getInstructorId() {
		return instructorId;
	}
 
	public void setInstructorId(UUID instructorId) {
		this.instructorId = instructorId;
	}
 
	public void setTotalMarks(Integer totalMarks) {
		this.totalMarks = totalMarks;
	}
 
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
 