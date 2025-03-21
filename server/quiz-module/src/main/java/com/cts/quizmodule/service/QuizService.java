package com.cts.quizmodule.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cts.quizmodule.exceptions.InstructorNotAllowedException;
import com.cts.quizmodule.client.CourseClient;
import com.cts.quizmodule.dao.QuizDao;
import com.cts.quizmodule.dto.QuizDTO;
import com.cts.quizmodule.dto.UpdateQuizDTO;
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
    	 boolean response=courseClient. verifyCourse( courseId);
    	 return response;
    }


	public boolean verifyInstructor(String instructorId, String courseId) {
		boolean response=courseClient.verifyInstructor(instructorId, courseId);
		return response;
	}

    
    

    public Quiz createQuiz(QuizDTO quizDTO) throws DuplicateQuizException,CourseNotFoundException,InstructorNotAllowedException{
    	
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
            throw new DuplicateQuizException();
        }
    	
    	
    	
        Quiz quiz = new Quiz();
        quiz.setCourseId(quizDTO.getCourseId().toString());
        quiz.setTitle(quizDTO.getTitle());
        quiz.setTotalMarks(quizDTO.getTotalMarks());
        return quizDao.save(quiz);    
       
    }
    
    public List<Quiz> getAllQuiz(String courseId) {
    	
		boolean status=courseExists(courseId);
		if (status == false) {
            throw new IllegalArgumentException("Course does not exist");
        }
		List<Quiz> quizList=quizDao.findByCourseId(courseId);
		
		
		return quizList;
	}
    
    
    
    
    

    public void deleteQuiz(UUID quizId) {
        Quiz quiz = quizDao.findById(quizId.toString())
                .orElseThrow(() -> new QuizNotFoundException("quiz not found"));
        quizDao.delete(quiz);
    }

    
    

    
    
    
    
    public Quiz updateTotalMarks(UpdateQuizDTO quizDTO) throws QuizNotFoundException {
    	
        Quiz quiz = quizDao.findById(quizDTO.getQuizId().toString())
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found"));
        
        boolean validInstructor=verifyInstructor(quizDTO.getInstructorId().toString(),quiz.getCourseId().toString());
        System.out.println(validInstructor);
       
        if(!validInstructor) {
        	throw new InstructorNotAllowedException("Instructor not allowed to update quiz");
        }
        
        quiz.setTotalMarks(quizDTO.getTotalMarks());
        System.out.println("shhhhhhhhhhhhhhhhhhhhhhhssssssssssssssssss");
        return quizDao.save(quiz);
    }

    
    
    
    ///////integration;;;;;;;;;;;;;

	public List<String> getAllQuizByCourseId(String courseId) {
	
		boolean status=courseExists(courseId);
		if (status == false) {
            throw new IllegalArgumentException("Course does not exist");
        }
		List<String> quizId=new ArrayList<>();
		List<Quiz> quizList=quizDao.findByCourseId(courseId);
		
		for(Quiz quiz:quizList) {
			quizId.add(quiz.getQuizId());
		}
		return quizId;
	}
	
	
	
	
    
}