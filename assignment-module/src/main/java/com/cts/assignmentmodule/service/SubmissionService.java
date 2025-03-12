package com.cts.assignmentmodule.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cts.assignmentmodule.dao.AssignmentDAO;
import com.cts.assignmentmodule.dao.SubmissionDAO;
import com.cts.assignmentmodule.exceptions.AssignmentNotFoundException;
import com.cts.assignmentmodule.model.Assignment;
import com.cts.assignmentmodule.model.Submission;



@Service
public class SubmissionService {
	
    @Autowired
    private SubmissionDAO submissionDao;
    
    @Autowired
    private AssignmentDAO assignmentDao;
    
    public List<Submission> getAllSubmissions() {
        return submissionDao.findAll();
    }

    
    public void saveSubmission(MultipartFile file, int userId, int assignmentId) throws IOException {
        Assignment assignment = assignmentDao.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));
        Submission submission = new Submission();
        submission.setUserId(userId);
        submission.setAnswerUpload(file.getBytes());
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setAssignment(assignment);
        submissionDao.save(submission);
    }

    public byte[] getFileByUserIdAndAssignmentId(int userId, int assignmentId)  {
    	Assignment assignment = assignmentDao.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));
        Submission submission = submissionDao.findByUserIdAndAssignment(userId, assignment);
               
        return submission.getAnswerUpload();
    }

    public void assignMarksByUserId(int userId, int assignmentId, int marks) {
    	Assignment assignment = assignmentDao.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));
        Submission submission = submissionDao.findByUserIdAndAssignment(userId, assignment);
        submission.setObtainedMarks(marks);
        submissionDao.save(submission);
    }
    
    public double calculatePercentage(int userId, int assignmentId) {
    	Assignment assignment = assignmentDao.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));
        Submission submission = submissionDao.findByUserIdAndAssignment(userId, assignment);
        if (submission == null) {
            throw new AssignmentNotFoundException("Submission not found");
        }
        int obtainedMarks = submission.getObtainedMarks();
        int totalMarks = submission.getAssignment().getTotalMarks();
        if (totalMarks == 0) {
            throw new IllegalArgumentException("Total marks cannot be zero");
        }
        return ((double) obtainedMarks / totalMarks) * 100;
    }
    public void deleteByUserIdandAssignment(int userId, int assignmentId)
    {
    	Assignment assignment = assignmentDao.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));
        Submission submission = submissionDao.findByUserIdAndAssignment(userId, assignment);
        if (submission == null) {
            throw new AssignmentNotFoundException("Submission not found");
        }
        submissionDao.delete(submission);
    }
    

}
