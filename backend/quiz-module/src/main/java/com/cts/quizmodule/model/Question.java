package com.cts.quizmodule.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name="question")
public class Question {
    @Id
    private String questionId;
   
    
    @ManyToOne
    @JsonIgnore
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

    // Getters and setters

   

    public Quiz getQuiz() {
        return quiz;
    }

    /**
	 * @return the questionId
	 */
	public String getQuestionId() {
		return questionId;
	}

	/**
	 * @param questionId the questionId to set
	 */
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Question(String questionId, Quiz quiz, String questionText, String optionA, String optionB, String optionC, String correctAnswer) {
        this.questionId = questionId;
        this.quiz = quiz;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.correctAnswer = correctAnswer;
    }

    public Question() {
    }
    @PrePersist
    public void generateUuid() {
    	this.questionId = UUID.randomUUID().toString();
    }
}