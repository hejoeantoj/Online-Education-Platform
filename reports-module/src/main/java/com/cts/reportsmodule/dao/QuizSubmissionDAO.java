package com.cts.reportsmodule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.reportsmodule.model.Course;
import com.cts.reportsmodule.model.QuizSubmission;



public interface QuizSubmissionDAO extends JpaRepository<QuizSubmission,String> {

	List<QuizSubmission> findByUserId(String studentId);
	
	List<QuizSubmission> findByUserIdAndQuizCourse(String userId, Course course);

}
