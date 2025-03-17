package com.cts.quizmodule.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cts.quizmodule.dao.QuizDao;
import com.cts.quizmodule.dto.QuizDTO;
import com.cts.quizmodule.exceptions.CourseNotFoundException;
import com.cts.quizmodule.exceptions.DuplicateQuizException;
import com.cts.quizmodule.exceptions.QuizNotFoundException;
import com.cts.quizmodule.model.Quiz;

@Service
public class QuizService {

    @Autowired
    private QuizDao quizDao;
    
    

    public Quiz createQuiz(QuizDTO quizDTO) throws DuplicateQuizException{
    	if (!quizDao.existsByCourseId(quizDTO.getCourseId().toString())) {
            throw new CourseNotFoundException("Course not found");
        }
    	
    	if (quizDao.existsByTitle(quizDTO.getTitle())) {
            throw new DuplicateQuizException("Duplicate quiz found");
        }
        Quiz quiz = new Quiz();
        quiz.setCourseId(quizDTO.getCourseId().toString());
        quiz.setTitle(quizDTO.getTitle());
        quiz.setTotalMarks(quizDTO.getTotalMarks());
        return quizDao.save(quiz);    
       
    }

    public void deleteQuiz(UUID quizId) {
        Quiz quiz = quizDao.findById(quizId.toString())
                .orElseThrow(() -> new QuizNotFoundException("quiz not found"));
        quizDao.delete(quiz);
    }

    public List<Quiz> getAllQuiz() {
        return quizDao.findAll();
    }
    
    
    public Quiz updateTotalMarks(QuizDTO quizDTO) throws QuizNotFoundException {
        Quiz quiz = quizDao.findById(quizDTO.getQuizId().toString())
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found"));
        
        quiz.setTotalMarks(quizDTO.getTotalMarks());
        return quizDao.save(quiz);
    }
    
    
}