package com.cts.assignmentmodule.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.assignmentmodule.model.Assignment;
import com.cts.assignmentmodule.model.Submission;

import java.util.Optional;
import java.util.UUID;

public interface SubmissionDAO extends JpaRepository<Submission, UUID> {
	
	Submission findByUserIdAndAssignment(int userId, Assignment assignment);
    Submission deleteByUserIdAndAssignment(int userId, Assignment assignment);
}
