package com.cts.assignmentmodule.dto;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
 
public class AssignmentSubmissionDTO {
 
    @NotNull(message = "Give valid Field Name For Student ID")
    private UUID studentId;
 
    @NotNull(message = "Give valid Field Name For AssignmentId")
    private UUID assignmentId;
 
    //(message = "File cannot be null")
    private MultipartFile file;
    
    
 
    @Null(message = "Obtained marks must be positive or zero")
    private Double obtainedMarks;
 
                    //@Size(max = 500, message = "Feedback cannot exceed 500 characters")
    @Null(message="Give valid Field name For Feedback")
    private String feedback;
    
    
    
 
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
 