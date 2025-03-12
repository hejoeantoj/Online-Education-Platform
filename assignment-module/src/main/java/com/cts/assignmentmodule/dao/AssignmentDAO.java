package com.cts.assignmentmodule.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.assignmentmodule.model.Assignment;

public interface AssignmentDAO  extends JpaRepository<Assignment, Integer>{

}
