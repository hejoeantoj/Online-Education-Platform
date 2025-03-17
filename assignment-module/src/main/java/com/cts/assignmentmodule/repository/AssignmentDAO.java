package com.cts.assignmentmodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.assignmentmodule.model.Assignment;



public interface AssignmentDAO  extends JpaRepository<Assignment, String>{
	
}