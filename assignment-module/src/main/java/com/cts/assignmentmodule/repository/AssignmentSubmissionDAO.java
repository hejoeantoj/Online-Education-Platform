package com.cts.assignmentmodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.assignmentmodule.model.Assignment;
import com.cts.assignmentmodule.model.AssignmentSubmission;


@Repository
public interface AssignmentSubmissionDAO extends JpaRepository <AssignmentSubmission,String> {
	
	AssignmentSubmission findByStudentIdAndAssignment(String studentId, Assignment assignment);
	
    AssignmentSubmission deleteByStudentIdAndAssignment(String studentId, Assignment assignment);
}

