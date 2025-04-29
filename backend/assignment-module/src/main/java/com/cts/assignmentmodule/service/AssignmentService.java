package com.cts.assignmentmodule.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cts.assignmentmodule.client.CourseClient;

import com.cts.assignmentmodule.dto.AssignmentDTO;
import com.cts.assignmentmodule.dto.AssignmentResponseDTO;
import com.cts.assignmentmodule.exceptions.AssignmentNotFoundException;
import com.cts.assignmentmodule.exceptions.CourseNotFoundException;
import com.cts.assignmentmodule.exceptions.DuplicateAssignmentException;
import com.cts.assignmentmodule.exceptions.DuplicateAssignmentSubmissionException;
import com.cts.assignmentmodule.exceptions.InstructorNotAllowedException;
import com.cts.assignmentmodule.exceptions.UserInputInvalidException;
import com.cts.assignmentmodule.model.Assignment;
import com.cts.assignmentmodule.repository.AssignmentDAO;
import com.cts.assignmentmodule.utils.ResultResponse;


@Service
public class AssignmentService {
    private static final Logger log = LoggerFactory.getLogger(AssignmentService.class);

    @Autowired
    private AssignmentDAO assignmentDao;
    
    @Autowired
    private CourseClient courseClient;
    
    
    public boolean courseExists(String courseId) {
    		boolean response=courseClient.verifyCourse(courseId);
    		return response;
    }
    
    
    public boolean verifyInstructor(String instructorId, String courseId) {
		boolean response=courseClient.verifyInstructor(instructorId, courseId);
		return response;
   }

    /*
     * Method to Retrieve list of all assignments
     * returns----List of all assignments submitted
     */
    public List<AssignmentResponseDTO> getAllAssignments(String courseId) {
    	if(!courseExists(courseId)) {
    		throw new CourseNotFoundException("Course Not Found");
    	}
    	
    	List<AssignmentResponseDTO> response=new ArrayList<>();
        List<Assignment> assignmentList= assignmentDao.findByCourseId(courseId);
        for(Assignment assignment:assignmentList) {
        	AssignmentResponseDTO assignmentObj=new AssignmentResponseDTO();
        	
        	assignmentObj.setAssignmentId(assignment.getAssignmentId());
        	assignmentObj.setCourseId(assignment.getCourseId());
        	assignmentObj.setQuestion(assignment.getQuestion());
        	assignmentObj.setTotalMarks(assignment.getTotalMarks());
        	
        	response.add(assignmentObj);
        }
        	return response;
        	
        }
    

    /*
     * Method to Create a new assignment---------->returns The created assignment
     */
    public Assignment createAssignment(AssignmentDTO assignmentDTO) throws DuplicateAssignmentSubmissionException, UserInputInvalidException {
       

        
        boolean validInstructor=verifyInstructor(assignmentDTO.getInstructorId().toString(),assignmentDTO.getCourseId().toString());
        System.out.println(validInstructor);
        if(!validInstructor) {
        	throw new InstructorNotAllowedException("Instructor not Allowed");
        }
        
        
        boolean status=courseExists(assignmentDTO.getCourseId().toString());
        if (status == false) {
            throw new IllegalArgumentException();
        }
        	
        
        // Checking for duplicate assignment
        List<Assignment> existingAssignments = assignmentDao.findByCourseId(assignmentDTO.getCourseId().toString());
        for (Assignment assignment : existingAssignments) {
            if (assignment.getQuestion().equals(assignmentDTO.getQuestion())) {
                log.error("Assignment with the same question already exists");
                throw new DuplicateAssignmentException("");
            }
        }
        
       
     	
        	
        Assignment assignment = new Assignment();
        assignment.setCourseId(assignmentDTO.getCourseId().toString());
        assignment.setQuestion(assignmentDTO.getQuestion());
        assignment.setTotalMarks(assignmentDTO.getTotalMarks());
        return assignmentDao.save(assignment);
    }
    
    
 
    
      
    
    public Assignment viewById(AssignmentDTO assignmentId) {
    	System.out.println(assignmentId.toString());
        Assignment assignment = assignmentDao.findById(assignmentId.getAssignmentId().toString())
        		
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));
        return assignment;
    }
    /*
     * Method to Delete an assignment by its ID 
     * if not exists id throws an Assignment not found exception
     */
    
    public void deleteAssignment(AssignmentDTO assignmentDTO) throws AssignmentNotFoundException {
        if (!assignmentDao.existsById(assignmentDTO.getAssignmentId().toString())) {
            log.error("Assignment with ID: {} does not exist", assignmentDTO.getAssignmentId());
            throw new AssignmentNotFoundException("Assignment with ID does not exist");
        }

        assignmentDao.deleteById(assignmentDTO.getAssignmentId().toString());
    }
    
    
    ///for integrating ---report
    /*
     * for reports.
     * 
     * 
     */
    
      
    
    public List<String> getAllAssignmentsByCourseId(String courseId){
    	boolean status=courseExists(courseId);
        if (status == false) {
            throw new IllegalArgumentException("Course does not exist");
        }
        List<String> assignmentId = new ArrayList<>();
    	List<Assignment> assignmentList=assignmentDao.findByCourseId(courseId);
    	
    	for(Assignment assignment:assignmentList) {
    		assignmentId.add(assignment.getAssignmentId());
    	}
    	return assignmentId;
    }


	
}
