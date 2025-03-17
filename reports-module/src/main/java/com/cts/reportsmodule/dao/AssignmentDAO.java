package com.cts.reportsmodule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cts.reportsmodule.model.Assignment;
import com.cts.reportsmodule.model.Course;



public interface AssignmentDAO  extends JpaRepository<Assignment, String>{

	List<Assignment> findByCourse(Course course);
	
}