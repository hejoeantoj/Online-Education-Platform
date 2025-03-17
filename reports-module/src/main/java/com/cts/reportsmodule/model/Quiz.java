package com.cts.reportsmodule.model;

import jakarta.persistence.*;

import java.util.ArrayList;

import java.util.List;

import java.util.UUID;
 
import com.fasterxml.jackson.annotation.JsonIgnore;

	 
	@Entity
	@Table(name="quiz")
	public class Quiz {

	    @Id
	    @Column(name="quizId")
	    private String quizId;

	    @ManyToOne
	    @JoinColumn(name="courseId", nullable=false)
	    private Course course;

	    @Column(name="title",nullable=false)
	    private String title;
	    
	   
	    @Column(name="totalMarks" ,nullable=false)
	    private int totalMarks;

	    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
	    private List<Question> questions = new ArrayList<>();

	    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
	    private List<QuizSubmission> submissions = new ArrayList<>();

	    @PrePersist
	    public void generateUuid() {
	        this.quizId = UUID.randomUUID().toString();
	    }
	}



	 
	

	 
	
	