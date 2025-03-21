package com.cts.assignmentmodule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.cts.assignmentmodule.client.CourseClient;
import com.cts.assignmentmodule.dto.AssignMarksDTO;
import com.cts.assignmentmodule.dto.AssignmentSubmissionDTO;
import com.cts.assignmentmodule.exceptions.AlreadyMarksAssignedException;
import com.cts.assignmentmodule.exceptions.AssignmentNotFoundException;
import com.cts.assignmentmodule.exceptions.AssignmentSubmissionNotFoundException;
import com.cts.assignmentmodule.exceptions.DuplicateAssignmentSubmissionException;
import com.cts.assignmentmodule.exceptions.InstructorNotAllowedException;
import com.cts.assignmentmodule.exceptions.InvalidMarksException;
import com.cts.assignmentmodule.exceptions.StudentNotEnrolledException;
import com.cts.assignmentmodule.model.Assignment;
import com.cts.assignmentmodule.model.AssignmentSubmission;
import com.cts.assignmentmodule.repository.AssignmentDAO;
import com.cts.assignmentmodule.repository.AssignmentSubmissionDAO;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AssignmentSubmissionService {

    private static final Logger log = LoggerFactory.getLogger(AssignmentSubmissionService.class);

    @Autowired
    private AssignmentSubmissionDAO assignmentSubmissionDao;

    @Autowired
    private AssignmentDAO assignmentDao;
    
    @Autowired
    private CourseClient courseClient;
    
   
    public boolean verifyStudent(String studentId, String courseId) {
		boolean response=courseClient.verifyEnrollment(studentId, courseId);
		return response;
   }
    
    public boolean verifyInstructor(String instructorId, String courseId) {
		boolean response=courseClient.verifyInstructor(instructorId, courseId);
		return response;
   }
    

    public List<AssignmentSubmission> getAllSubmissions(String courseId) {
        return assignmentSubmissionDao.findByAssignmentCourseId(courseId);
    }
    
    

    public void saveSubmission(AssignmentSubmissionDTO assignmentSubmissionDTO) throws AssignmentNotFoundException, DuplicateAssignmentSubmissionException,StudentNotEnrolledException, IOException {
    	Assignment assignment = assignmentDao.findById(assignmentSubmissionDTO.getAssignmentId().toString())
                .orElseThrow(() -> {
                    log.error("Assignment not found with ID: {}", assignmentSubmissionDTO.getAssignmentId());
                    return new AssignmentNotFoundException("Assignment not found");
                });
    	
    	
    	boolean status=verifyStudent(assignmentSubmissionDTO.getStudentId().toString(),assignment.getCourseId());
    	//System.out.println(status+"inside function");
    	if(status==true) {

        	AssignmentSubmission existingSubmission = assignmentSubmissionDao.findByStudentIdAndAssignmentAssignmentId(assignmentSubmissionDTO.getStudentId().toString(), assignment.getAssignmentId());
            if (existingSubmission != null) {
                log.error("Submission already exists for studentId: {} and assignmentId: {}", assignmentSubmissionDTO.getStudentId(), assignmentSubmissionDTO.getAssignmentId());
                throw new DuplicateAssignmentSubmissionException("Submission already exists for this assignment and student");
                }
    		
      }else {
    		 throw new StudentNotEnrolledException("Student is not enrolled in the course");
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

    
    
    
    
    
    public byte[] getFileByUserIdAndAssignmentId( String studentId, String assignmentId, String instructorId) throws AssignmentNotFoundException,AssignmentSubmissionNotFoundException {
       System.out.println("++++++++++++");
    	Assignment assignment = assignmentDao.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found" + assignmentId));

    	boolean verifyInstructor=courseClient.verifyInstructor(instructorId, assignment.getCourseId());
    	if(verifyInstructor==false) {
    		throw new InstructorNotAllowedException("");
    	}
    	
        AssignmentSubmission submission = assignmentSubmissionDao.findByStudentIdAndAssignment(studentId, assignment);
        if (submission == null) {
            
            throw new AssignmentSubmissionNotFoundException("Submission not found");
        }
        
        
        return submission.getAnswerUpload();
    }
    
    
    
    
    

    public void assignMarksByUserId(AssignMarksDTO assignMarks) throws AssignmentNotFoundException,AssignmentSubmissionNotFoundException,InstructorNotAllowedException,AlreadyMarksAssignedException{
        Assignment assignment = assignmentDao.findById(assignMarks.getAssignmentId().toString())
                .orElseThrow(() -> {
                    log.error("Assignment not found with ID: {}", assignMarks.getAssignmentId());
                    return new AssignmentNotFoundException("Assignment not found");
                });

        AssignmentSubmission submission = assignmentSubmissionDao.findByStudentIdAndAssignment(assignMarks.getStudentId().toString(), assignment);
        if (submission == null) {
            log.error("Submission not found for given studentId and assignmentId");
            throw new AssignmentSubmissionNotFoundException("Submission not found*************8");
        }
        
        boolean validInstructor=verifyInstructor(assignMarks.getInstructorId().toString(),assignment.getCourseId());
        System.out.println(validInstructor);
        if(!validInstructor) {
        	throw new InstructorNotAllowedException("Instructor not allowed to assign marks");
        }
        
        if(submission.getObtainedMarks()==null) {
        submission.setObtainedMarks(assignMarks.getObtainedMarks());
        int totalMarks = assignment.getTotalMarks();
        if(assignMarks.getObtainedMarks()>totalMarks)
        {
        	throw new InvalidMarksException("");
        }
        
        double percentage = (assignMarks.getObtainedMarks() / totalMarks) * 100;
        submission.setPercentage(percentage);
        submission.setFeedback(assignMarks.getFeedback());
        submission.setReviewedAt(LocalDateTime.now());
        assignmentSubmissionDao.save(submission);

        log.info("Exiting from assignMarksByUserId--> Marks assigned successfully for given studentId and assignmentId");
        }else {
    	  throw new AlreadyMarksAssignedException("marks already assigned");
        }
    }
    
    
    
    
    
    public void deleteByUserIdandAssignment(String studentId,String assignmentId) {
        Assignment assignment = assignmentDao.findById(assignmentId)
                .orElseThrow(() -> {
                    log.error("Assignment not found with given ID");
                    return new AssignmentNotFoundException("Assignment not found");
                });

        AssignmentSubmission submission = assignmentSubmissionDao.findByStudentIdAndAssignment(studentId, assignment);
        if (submission == null) {
            log.error("Submission not found for given studentId and assignmentId");
            throw new AssignmentSubmissionNotFoundException("Submission not found");
        }
        assignmentSubmissionDao.delete(submission);
    }
    
    
    /*
     * for integration purpose.....
     * 
     * reposrt module......
     * 
     */
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    public Map<String,Double> getAssignmentsByStudentIdAndAssignmentId(String studentId,String assignmentId){
    	
    	Assignment assignment = assignmentDao.findById(assignmentId)
    			.orElseThrow(() -> new AssignmentNotFoundException("Course not found"));
    	
    	 AssignmentSubmission assignmentSubmission=assignmentSubmissionDao.findByStudentIdAndAssignment(studentId, assignment);
    	 Map<String,Double> mapData=new HashMap<>();
    	 
    	 if(assignmentSubmission==null) {
    		  mapData.put(assignmentId, null);
    		  return mapData;
    	 }
    	 
//    	 if(assignmentSubmission.getReviewedAt()!=null)
//    	 {
    	 System.out.println(assignmentSubmission.getSubmittedAt());
    	 mapData.put(assignmentSubmission.getAssignment().getAssignmentId(), assignmentSubmission.getPercentage());
//    	 }else {
//    		 mapData.put(assignmentSubmission.getAssignment().getAssignmentId(), null);;
//    	 }
    	 
    	 System.out.println("this is the method inside assisgnment");
    	return mapData;
    }
}