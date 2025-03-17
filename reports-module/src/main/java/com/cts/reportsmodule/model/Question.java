package com.cts.reportsmodule.model;


import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name="question")
public class Question {

    @Id
    private String questionId;

    @ManyToOne
    @JoinColumn(name = "quizId", nullable = false)
    private Quiz quiz;

    @Column(nullable=false)
    private String questionText;

    @Column(nullable=false)
    private String optionA;

    @Column(nullable=false)
    private String optionB;

    @Column(nullable=false)
    private String optionC;

    @Column(nullable=false)
    private String correctAnswer;
 
    @PrePersist
    public void generateUuid() {

    	this.questionId = UUID.randomUUID().toString();

    }

}