package com.cts.assignmentmodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.assignmentmodule.model.Assignment;


@Repository
public interface AssignmentDAO  extends JpaRepository<Assignment, String>{
	
}