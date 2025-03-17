package com.cts.quizmodule.service;


import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.quizmodule.dao.QuestionDao;
import com.cts.quizmodule.dao.QuizDao;
import com.cts.quizmodule.dto.QuestionDTO;
import com.cts.quizmodule.exceptions.DuplicateQuestionException;
import com.cts.quizmodule.exceptions.QuizNotFoundException;
import com.cts.quizmodule.model.Question;
import com.cts.quizmodule.model.Quiz;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private QuizDao quizDao;

    public Question createQuestion(QuestionDTO questionDTO)throws QuizNotFoundException,DuplicateQuestionException
    {
    	 if (questionDTO.getQuestionText() == null || questionDTO.getQuestionText().trim().isEmpty()) {
    	        throw new IllegalArgumentException("Question text cannot be null / empty");
    	 }
    	
        Question question = new Question();
        Quiz quiz = quizDao.findById(questionDTO.getQuizId().toString())
                .orElseThrow(() -> new QuizNotFoundException("quiz not found to create new question"));
        boolean exists = questionDao.findByQuizQuizId(quiz.getQuizId()).stream()
                .anyMatch(q -> q.getQuestionText().equalsIgnoreCase(questionDTO.getQuestionText()));
        
        if (exists) {
            throw new DuplicateQuestionException("Duplicate question found");
        }
        
        question.setOptionC(questionDTO.getOptionC());
        question.setOptionB(questionDTO.getOptionB());
        question.setOptionA(questionDTO.getOptionA());
        question.setQuestionText(questionDTO.getQuestionText());
        question.setCorrectAnswer(questionDTO.getCorrectAnswer());
        question.setQuiz(quiz);
        
        return questionDao.save(question);
        
     }

    public List<Question> getAllQuestions() {
        return questionDao.findAll();
    }
  
    public List<Question> getAllQuestionsById(String quizId) throws QuizNotFoundException {
    	
    	Quiz quiz = quizDao.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found to create new question"));
        return questionDao.findByQuizQuizId(quizId);
    }
  }
