package com.cts.coursemodule.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.coursemodule.model.Course;
import com.cts.coursemodule.model.Enrollment;


@Repository
public interface EnrollmentDAO extends JpaRepository<Enrollment, String> {
	
	 List<Enrollment> findByStudentId(String studentId);
	 		    
	 
	 boolean existsByStudentIdAndCourse(String StudentId,Course course);

	List<Enrollment> findAllByCourseCourseId(String courseId);

}