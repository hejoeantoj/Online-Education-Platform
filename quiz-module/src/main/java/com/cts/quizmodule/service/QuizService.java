package com.cts.quizmodule.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.quizmodule.dao.QuestionDao;
import com.cts.quizmodule.dao.QuizDao;
import com.cts.quizmodule.dto.QuestionDTO;
import com.cts.quizmodule.dto.QuizDTO;
import com.cts.quizmodule.exceptions.QuizNotFoundException;
import com.cts.quizmodule.model.Question;
import com.cts.quizmodule.model.Quiz;

@Service
public class QuizService {

    @Autowired
    private QuizDao quizDao;
    
    

    public Quiz createQuiz(QuizDTO quizDTO) {
        Quiz quiz = new Quiz();
        quiz.setCourseId(quizDTO.getCourseId());
        quiz.setTitle(quizDTO.getTitle());
        quiz.setTotalMarks(quizDTO.getTotalMarks());
        return quizDao.save(quiz);
    }

    public void deleteQuiz(String quizId) {
        Quiz quiz = quizDao.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException("quiz not found"));
        quizDao.delete(quiz);
    }

    public List<Quiz> getAllQuiz() {
        return quizDao.findAll();
    }
    
    
    public Quiz updateTotalMarks(QuizDTO quizDTO) throws QuizNotFoundException {
        Quiz quiz = quizDao.findById(quizDTO.getQuizId())
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found"));
        
        quiz.setTotalMarks(quizDTO.getTotalMarks());
        return quizDao.save(quiz);
    }

    
}
