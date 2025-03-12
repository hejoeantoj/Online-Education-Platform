package com.cts.quizmodule.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.quizmodule.model.Quiz;


@Repository
public interface QuizDao extends JpaRepository<Quiz,String>{

}
