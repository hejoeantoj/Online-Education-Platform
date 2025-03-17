package com.cts.assignmentmodule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.assignmentmodule.dto.AssignmentSubmissionDTO;
import com.cts.assignmentmodule.exceptions.AssignmentNotFoundException;
import com.cts.assignmentmodule.exceptions.AssignmentSubmissionNotFoundException;
import com.cts.assignmentmodule.exceptions.DuplicateAssignmentSubmissionException;
import com.cts.assignmentmodule.model.Assignment;
import com.cts.assignmentmodule.model.AssignmentSubmission;
import com.cts.assignmentmodule.repository.AssignmentDAO;
import com.cts.assignmentmodule.repository.AssignmentSubmissionDAO;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AssignmentSubmissionService {

    private static final Logger log = LoggerFactory.getLogger(AssignmentSubmissionService.class);

    @Autowired
    private AssignmentSubmissionDAO assignmentSubmissionDao;

    @Autowired
    private AssignmentDAO assignmentDao;

    public List<AssignmentSubmission> getAllSubmissions() {
        return assignmentSubmissionDao.findAll();
    }

    public void saveSubmission(AssignmentSubmissionDTO assignmentSubmissionDTO) throws AssignmentNotFoundException,IOException, DuplicateAssignmentSubmissionException,IllegalArgumentException {
        Assignment assignment = assignmentDao.findById(assignmentSubmissionDTO.getAssignmentId().toString())
                .orElseThrow(() -> {
                    log.error("Assignment not found with ID: {}", assignmentSubmissionDTO.getAssignmentId());
                    return new AssignmentNotFoundException("Assignment not found");
                });

        AssignmentSubmission existingSubmission = assignmentSubmissionDao.findByStudentIdAndAssignment(assignmentSubmissionDTO.getStudentId().toString(), assignment);
        if (existingSubmission != null) {
            log.error("Submission already exists for studentId: {} and assignmentId: {}", assignmentSubmissionDTO.getStudentId(), assignmentSubmissionDTO.getAssignmentId());
            throw new DuplicateAssignmentSubmissionException("Submission already exists for this assignment and student");
        }

        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setStudentId(assignmentSubmissionDTO.getStudentId().toString());
        submission.setAnswerUpload(assignmentSubmissionDTO.getFile().getBytes());
        submission.setAssignment(assignment);
        submission.setObtainedMarks(null);
        submission.setPercentage(null);
        submission.setReviewedAt(null);
        assignmentSubmissionDao.save(submission);
    }

    public byte[] getFileByUserIdAndAssignmentId(AssignmentSubmissionDTO assignmentSubmissionDTO) throws AssignmentNotFoundException,AssignmentSubmissionNotFoundException {
        Assignment assignment = assignmentDao.findById(assignmentSubmissionDTO.getAssignmentId().toString())
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));

        AssignmentSubmission submission = assignmentSubmissionDao.findByStudentIdAndAssignment(assignmentSubmissionDTO.getStudentId().toString(), assignment);
        if (submission == null) {
            log.error("Submission not found for studentId: {} and assignmentId: {}", assignmentSubmissionDTO.getStudentId(), assignmentSubmissionDTO.getAssignmentId());
            throw new AssignmentSubmissionNotFoundException("Submission not found");
        }
        return submission.getAnswerUpload();
    }

    public void assignMarksByUserId(AssignmentSubmissionDTO assignmentSubmissionDTO) throws AssignmentNotFoundException,AssignmentSubmissionNotFoundException{
        Assignment assignment = assignmentDao.findById(assignmentSubmissionDTO.getAssignmentId().toString())
                .orElseThrow(() -> {
                    log.error("Assignment not found with ID: {}", assignmentSubmissionDTO.getAssignmentId());
                    return new AssignmentNotFoundException("Assignment not found");
                });

        AssignmentSubmission submission = assignmentSubmissionDao.findByStudentIdAndAssignment(assignmentSubmissionDTO.getStudentId().toString(), assignment);
        if (submission == null) {
            log.error("Submission not found for given studentId and assignmentId");
            throw new AssignmentSubmissionNotFoundException("Submission not found");
        }

        submission.setObtainedMarks(assignmentSubmissionDTO.getObtainedMarks());
        int totalMarks = assignment.getTotalMarks();
        double percentage = (assignmentSubmissionDTO.getObtainedMarks() / totalMarks) * 100;
        submission.setPercentage(percentage);
        submission.setFeedback(assignmentSubmissionDTO.getFeedback());
        submission.setReviewedAt(LocalDateTime.now());
        assignmentSubmissionDao.save(submission);

        log.info("Exiting from assignMarksByUserId--> Marks assigned successfully for given studentId and assignmentId");
    }

    public void deleteByUserIdandAssignment(AssignmentSubmissionDTO assignmentSubmissionDTO) {
        Assignment assignment = assignmentDao.findById(assignmentSubmissionDTO.getAssignmentId().toString())
                .orElseThrow(() -> {
                    log.error("Assignment not found with given ID");
                    return new AssignmentNotFoundException("Assignment not found");
                });

        AssignmentSubmission submission = assignmentSubmissionDao.findByStudentIdAndAssignment(assignmentSubmissionDTO.getStudentId().toString(), assignment);
        if (submission == null) {
            log.error("Submission not found for given studentId and assignmentId");
            throw new AssignmentSubmissionNotFoundException("Submission not found");
        }
        assignmentSubmissionDao.delete(submission);
    }
}