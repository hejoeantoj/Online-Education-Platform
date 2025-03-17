package com.cts.reportsmodule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.reportsmodule.model.Course;
import com.cts.reportsmodule.model.Enrollment;



public interface EnrollmentDAO extends JpaRepository<Enrollment,String>{

	List<Enrollment> findByCourse(Course course);

}
