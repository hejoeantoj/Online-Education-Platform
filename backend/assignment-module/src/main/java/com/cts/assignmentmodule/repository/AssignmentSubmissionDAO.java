package com.cts.assignmentmodule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.assignmentmodule.model.Assignment;
import com.cts.assignmentmodule.model.AssignmentSubmission;


@Repository
public interface AssignmentSubmissionDAO extends JpaRepository <AssignmentSubmission,String> {
	
	AssignmentSubmission findByStudentIdAndAssignment(String studentId, Assignment assignment);
	
	AssignmentSubmission findByStudentIdAndAssignmentAssignmentId(String studentId, String assignmentId);
	
    AssignmentSubmission deleteByStudentIdAndAssignment(String studentId, Assignment assignment);

	List<AssignmentSubmission> findByAssignmentCourseId(String courseId);

	@Query("SELECT s FROM AssignmentSubmission s WHERE s.assignment.courseId = :courseId AND s.assignment.assignmentId = :assignmentId")
    List<AssignmentSubmission> findByCourseIdAndAssignmentId(@Param("courseId") String courseId, @Param("assignmentId") String assignmentId);
	
	
	
}

