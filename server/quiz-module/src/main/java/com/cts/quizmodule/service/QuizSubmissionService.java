package com.cts.quizmodule.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.cts.quizmodule.client.CourseClient;
import com.cts.quizmodule.dao.QuestionDao;
import com.cts.quizmodule.dao.QuizDao;
import com.cts.quizmodule.dao.QuizSubmissionDao;
import com.cts.quizmodule.dto.QuizAnswerDTO;
import com.cts.quizmodule.dto.QuizResponse;
import com.cts.quizmodule.exceptions.ExistingQuizSubmissionException;
import com.cts.quizmodule.exceptions.QuizNotFoundException;
import com.cts.quizmodule.exceptions.StudentNotEnrolledException;
import com.cts.quizmodule.exceptions.UserNotFoundException;
import com.cts.quizmodule.model.Question;
import com.cts.quizmodule.model.Quiz;
import com.cts.quizmodule.model.QuizSubmission;

@Service
public class QuizSubmissionService {

    @Autowired
    private QuizSubmissionDao submissionDao;

    @Autowired
    private QuizDao quizDao;

    @Autowired
    private QuestionDao questionDao;
    
    @Autowired
    private CourseClient courseClient;
    
   
    public boolean verifyStudent(String studentId, String courseId) {
		boolean response=courseClient.verifyEnrollment(studentId, courseId);
		return response;
   }
    

    public QuizSubmission addSubmission(QuizResponse response) throws QuizNotFoundException, StudentNotEnrolledException, ExistingQuizSubmissionException {
        Quiz quiz = quizDao.findById(response.getQuizId().toString())
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found"));

        boolean status = verifyStudent(response.getUserId().toString(), quiz.getCourseId().toString());
        if (!status) {
            throw new StudentNotEnrolledException("Student not registered");
        }

        QuizSubmission object = null;

        QuizSubmission existingSubmission = submissionDao.findByQuizAndUserId(quiz, response.getUserId().toString());
//        System.out.print(existingSubmission);
        if (existingSubmission == null) {
            QuizSubmission submission = new QuizSubmission();
            submission.setQuiz(quiz);
            submission.setUserId(response.getUserId().toString());

            int quizSize = response.getResponse().size();
            System.out.println(quizSize);
            double score = calculateMark(response.getResponse());
            System.out.print(score / quizSize);
            double obtainedMarks = (score / quizSize) * quiz.getTotalMarks();

            submission.setObtainedMarks(obtainedMarks);
            double percentage = (score / quizSize) * 100;
            submission.setPercentage(percentage);

            object = submissionDao.save(submission);
        } else {
            int quizSize = response.getResponse().size();
            System.out.print("??????????" + response.getResponse().get(0).getSelectedOption());	
            double score = calculateMark(response.getResponse());

            System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhiiiiiiiii" + score);
            double obtainedMarks = (score / quizSize) * quiz.getTotalMarks();

            System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhiiiiiiiii" + obtainedMarks);
            existingSubmission.setObtainedMarks(obtainedMarks);
            double percentage = (score / quizSize) * 100;
            existingSubmission.setPercentage(percentage);

            object = submissionDao.save(existingSubmission);
        }
        return object;
    }

    
    public int calculateMark(List<QuizAnswerDTO> answers) {
        int score = 0;
        
        for (QuizAnswerDTO answer : answers) {
            Question question = questionDao.findById(answer.getQuestionId().toString())
                    .orElseThrow(() -> new QuizNotFoundException("Question not found"));
            if (question != null) {
                String correctAnswers = question.getCorrectAnswer();
                System.out.println(correctAnswers);

                System.out.print(answer.getSelectedOption());
                if (correctAnswers.equals(answer.getSelectedOption())) {
                    score++;
                    System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
                }
            }
        }

        return score;
    }
    
    
    
    
      
    public QuizSubmission getResultByQuizIdAndUserId(String quizId, String userId) throws QuizNotFoundException,UserNotFoundException {
        Quiz quiz = quizDao.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException("quiz not found"));
         
        QuizSubmission submission = submissionDao.findByQuizAndUserId(quiz, userId);
        if (submission == null) {
            throw new UserNotFoundException("submisssion not found for given user and quiz id");
        }
        
        return submission;
    }

    
    
    
    
    
    public List<QuizSubmission> getAllSubmissionsByQuizId(UUID quizId) {
    	Quiz quiz = quizDao.findById(quizId.toString())
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found to view all submissions"));
        return submissionDao.findByQuizQuizId(quizId.toString());
    }

    

    	
/*
 * 
 * 
 * integration purpose;;;;;;;
 * 
 */
    
	public Map<String, Double> getQuizByStudentIdAndQuizId(String studentId, String quizId) {
	
		Quiz quiz=quizDao.findById(quizId)
				.orElseThrow(() -> new QuizNotFoundException("Quiz Not Found"));
		
		 QuizSubmission quizSubmission=submissionDao.findByUserIdAndQuiz(studentId, quiz);
    	 Map<String,Double> mapData=new HashMap<>();
    	 
    	 if(quizSubmission==null) {
   		  mapData.put(quizId, null);
   		  return mapData;
   		  
    	 }
   		 mapData.put(quizSubmission.getQuiz().getQuizId(), quizSubmission.getPercentage());
   	 
    	 
		return mapData;
	}
    
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    
    
}
