package com.cts.quizmodule.service;


import java.util.List;
import java.util.stream.Collectors;

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
    	
    	
        Question question = new Question();
        Quiz quiz = quizDao.findById(questionDTO.getQuizId())
                .orElseThrow(() -> new QuizNotFoundException("quiz not found"));
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
    
    //viewing questions by quizId

    public List<Question> getAllQuestionsById(String quizId) {
        return questionDao.findByQuizQuizId(quizId);
    }
    
    
//    public List<Question> addOrUpdateQuestionsToQuiz(String quizId, List<QuestionDTO> questionDTOs) throws QuizNotFoundException {
//        Quiz quiz = quizDao.findById(quizId)
//                .orElseThrow(() -> new QuizNotFoundException("Quiz not found"));
//        
//        List<Question> questions = questionDTOs.stream()
//                .map(dto -> mapToQuestion(dto, quiz))
//                .collect(Collectors.toList());
//        
//        questionDao.saveAll(questions);
//        
//        // Update total marks based on the number of questions
//        int totalMarks = questionDao.findByQuizQuizId(quizId).size();
//        quiz.setTotalMarks(totalMarks);
//        quizDao.save(quiz);
//        
//        return questionDao.findByQuizQuizId(quizId);
//    }
//
//    private Question mapToQuestion(QuestionDTO questionDTO, Quiz quiz) {
//        Question question = new Question();
//        question.setOptionA(questionDTO.getOptionA());
//        question.setOptionB(questionDTO.getOptionB());
//        question.setOptionC(questionDTO.getOptionC());
//        question.setQuestionText(questionDTO.getQuestionText());
//        question.setCorrectAnswer(questionDTO.getCorrectAnswer());
//        question.setQuiz(quiz);
//        return question;
//    }
//    
    
}
