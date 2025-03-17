package com.cts.quizmodule.dao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.quizmodule.model.Quiz;
import com.cts.quizmodule.model.QuizSubmission;

@Repository
public interface QuizSubmissionDao extends JpaRepository<QuizSubmission,String> {
     
	QuizSubmission findByQuizAndUserId(Quiz quiz,String userId);
	
	List<QuizSubmission> findByQuizQuizId(String quizId);
}
