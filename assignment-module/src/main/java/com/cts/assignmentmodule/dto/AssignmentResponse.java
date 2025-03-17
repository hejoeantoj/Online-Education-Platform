//package com.cts.assignmentmodule.dto;
//
//import java.util.UUID;
//
//import javax.validation.constraints.Min;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;
//
//import org.springframework.validation.annotation.Validated;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//
//
//@Validated
//@JsonIgnoreProperties(ignoreUnknown=true)
//public class AssignmentResponse {
//    
//    @NotNull(message = "Assignment ID cannot be blank")
//    private UUID assignmentId;
//    
//    @NotBlank(message = "Question cannot be blank")
//    @Size(max = 500, message = "Question cannot exceed 500 characters")
//    private String question;
//    
//    @Min(value = 1, message = "Total marks must be at least 1")
//    private int totalMarks;
//
//    // Getters and Setters
//    public UUID getAssignmentId() {
//        return assignmentId;
//    }
//
//    public void setAssignmentId(UUID assignmentId) {
//        this.assignmentId = assignmentId;
//    }
//
//    public String getQuestion() {
//        return question;
//    }
//
//    public void setQuestion(String question) {
//        this.question = question;
//    }
//
//    public int getTotalMarks() {
//        return totalMarks;
//    }
//
//    public void setTotalMarks(int totalMarks) {
//        this.totalMarks = totalMarks;
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
////  @NotBlank(message = "Course title cannot be blank")
////private String courseTitle;
//
////public String getCourseTitle() {
////  return courseTitle;
////}
////
////public void setCourseTitle(String courseTitle) {
////  this.courseTitle = courseTitle;
////}