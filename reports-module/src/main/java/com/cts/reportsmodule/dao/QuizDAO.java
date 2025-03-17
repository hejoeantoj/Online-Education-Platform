package com.cts.reportsmodule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.reportsmodule.model.Course;
import com.cts.reportsmodule.model.Quiz;



public interface QuizDAO extends JpaRepository<Quiz,String> {

	List<Quiz> findByCourse(Course course);

}
