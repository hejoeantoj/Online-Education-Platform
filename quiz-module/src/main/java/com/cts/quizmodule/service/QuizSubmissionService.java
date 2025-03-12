package com.cts.quizmodule.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.quizmodule.dao.QuestionDao;
import com.cts.quizmodule.dao.QuizDao;
import com.cts.quizmodule.dao.QuizSubmissionDao;
import com.cts.quizmodule.dto.QuizResponse;
import com.cts.quizmodule.exceptions.QuizNotFoundException;
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

    public QuizSubmission addSubmission(QuizResponse response) {
        double score = calculateMark(response.getSelectedOptions());

        QuizSubmission submission = new QuizSubmission();
        submission.setUserId(response.getUserId());

        Optional<Quiz> quiz = quizDao.findById(response.getQuizId());
        Quiz actualQuiz=null;
        if (quiz.isPresent()) {
             actualQuiz = quiz.get();
            submission.setQuiz(actualQuiz);
        }
        
        submission.setObtainedMarks(score);
        int totalMarks = actualQuiz.getTotalMarks();
        double percentage=(score/totalMarks)*100;
        submission.setPercentage(percentage);
        
        return submissionDao.save(submission);
    }

    public int calculateMark(Map<String, String> selectedAns) {
        int score = 0;

        for (String ele : selectedAns.keySet()) {
            Optional<Question> question = questionDao.findById(ele);
            if (question.isPresent()) {
                String crtAns = question.get().getCorrectAnswer();
                if (crtAns.equals(selectedAns.get(ele))) {
                    score++;
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
            throw new UserNotFoundException("User not found for the given quiz ID");
        }
        
        return submission;
    }

    public List<QuizSubmission> getAllSubmissionsByQuizId(String quizId) {        
        return submissionDao.findByQuizQuizId(quizId);
    }
    
    
    
}
