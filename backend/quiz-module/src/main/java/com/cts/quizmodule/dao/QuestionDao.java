package com.cts.quizmodule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.quizmodule.model.Question;
import com.cts.quizmodule.model.Quiz;
import com.cts.quizmodule.model.QuizSubmission;

@Repository
public interface QuestionDao extends JpaRepository<Question,String> {
   List<Question> findByQuizQuizId(String quizId);
}
