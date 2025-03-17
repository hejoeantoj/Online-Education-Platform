package com.cts.quizmodule.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cts.quizmodule.exceptions.InstructorNotAllowedException;
import com.cts.quizmodule.client.CourseClient;
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
    
    @Autowired
    private CourseClient courseClient;
    
    
    public boolean courseExists(String courseId) {
		ResponseEntity<?> response=courseClient.getCourseById(courseId);
		if(response.getStatusCode()==HttpStatus.OK)
		{
			return true;
		}
	return false;
    	}


	public boolean verifyInstructor(String instructorId, String courseId) {
		boolean response=courseClient.verifyInstructor(instructorId, courseId);
		if(response)
		{
			return true;
		}else {
	   return false;
	    }
	}

    
    

    public Quiz createQuiz(QuizDTO quizDTO) throws DuplicateQuizException,CourseNotFoundException{
    	
    	boolean status=courseExists(quizDTO.getCourseId().toString());
        if (status == false) {
            throw new CourseNotFoundException("Course does not exist");
        }
        
        boolean validInstructor=verifyInstructor(quizDTO.getInstructorId().toString(),quizDTO.getCourseId().toString());
        System.out.println(validInstructor);
        if(!validInstructor) {
        	throw new InstructorNotAllowedException("Instructor not allowed to create quiz");
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
        
        boolean validInstructor=verifyInstructor(quizDTO.getInstructorId().toString(),quizDTO.getCourseId().toString());
        System.out.println(validInstructor);
        if(!validInstructor) {
        	throw new InstructorNotAllowedException("Instructor not allowed to update quiz");
        }
        
        quiz.setTotalMarks(quizDTO.getTotalMarks());
        return quizDao.save(quiz);
    }
    
    
}