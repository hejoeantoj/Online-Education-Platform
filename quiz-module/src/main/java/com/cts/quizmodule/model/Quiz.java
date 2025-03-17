package com.cts.quizmodule.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="quiz")
public class Quiz {
    
	@Id
	private String quizId;
    
    @Column(nullable=false)
    
    private String courseId; 
    
    @Column(nullable=false)
    private String title;
    
    @Column(nullable=false)
    private int totalMarks;
    
    
    
    @JsonIgnore
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>();
    
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<QuizSubmission> submission = new ArrayList<>();

    // Getters and setters

    public String getQuizId() {
        return quizId;
   }

   public void setQuizId(String quizId) {
        this.quizId = quizId;
        }

    public String getCourseId() {
        return courseId;
    }

  public void setCourseId(String courseId) {
       this.courseId = courseId;
    }

   public String getTitle() {
        return title;
   }

   public void setTitle(String title) {
      this.title = title;
    }

    public int getTotalMarks() {
      return totalMarks;
  }

   public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }
   public List<Question> getQuestions() {
        return questions;
    }

   public void setQuestions(List<Question> questions) {
       this.questions = questions;
   }

   public Quiz(String quizId, String courseId, String title, int totalMarks, List<Question> questions) {
        this.quizId = quizId;
       this.courseId = courseId;
          this.title = title;
          this.totalMarks = totalMarks;
          this.questions = questions;
   }
    
    public Quiz() {
    }
    @PrePersist
    public void generateUuid() {
    	this.quizId = UUID.randomUUID().toString();
    }
	
}