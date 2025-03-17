package com.cts.reportsmodule.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.reportsmodule.model.Assignment;
import com.cts.reportsmodule.model.AssignmentSubmission;
import com.cts.reportsmodule.model.Course;

public interface AssignmentSubmissionDAO extends JpaRepository <AssignmentSubmission,String> {
	
	AssignmentSubmission findByStudentIdAndAssignment(String studentId, Assignment assignment);
	
    AssignmentSubmission deleteByStudentIdAndAssignment(String studentId, Assignment assignment);

	List<AssignmentSubmission> findByStudentId(String studentId);
	
	List<AssignmentSubmission> findByStudentIdAndAssignmentCourse(String StudentId,Course course);
}

