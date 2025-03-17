package com.cts.assignmentmodule.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.assignmentmodule.dto.AssignmentDTO;
import com.cts.assignmentmodule.exceptions.AssignmentNotFoundException;
import com.cts.assignmentmodule.exceptions.DuplicateAssignmentException;
import com.cts.assignmentmodule.exceptions.UserInputInvalidException;
import com.cts.assignmentmodule.model.Assignment;
import com.cts.assignmentmodule.repository.AssignmentDAO;

@Service
public class AssignmentService {
    private static final Logger log = LoggerFactory.getLogger(AssignmentService.class);

    @Autowired
    private AssignmentDAO assignmentDao;

    /*
     * Method to Retrieve list of all assignments
     * returns---->List of all assignments submitted
     */
    public List<Assignment> getAllAssignments() {
        return assignmentDao.findAll();
    }

    /*
     * Method to Create a new assignment---------->returns The created assignment
     */
    public Assignment createAssignment(AssignmentDTO assignmentdto) throws DuplicateAssignmentException, IllegalArgumentException, UserInputInvalidException {
        if (assignmentdto.getCourseId() == null || assignmentdto.getCourseId().toString().length() != 36) {
            throw new UserInputInvalidException("Invalid UUID format. UUID must be a 36-character string.");
        }
        if (assignmentdto.getQuestion() == null || assignmentdto.getQuestion().isEmpty()) {
            log.error("Question field cannot be null or empty");
            throw new IllegalArgumentException("Question field cannot be null or empty");
        }

        // Checking for duplicate assignment
        List<Assignment> existingAssignments = assignmentDao.findAll();
        for (Assignment assignment : existingAssignments) {
            if (assignment.getQuestion().equals(assignmentdto.getQuestion())) {
                log.error("Assignment with the same question already exists");
                throw new DuplicateAssignmentException("Assignment with the same question already exists");
            }
        }

        Assignment assignment = new Assignment();
        assignment.setCourseId(assignmentdto.getCourseId().toString());
        assignment.setQuestion(assignmentdto.getQuestion());
        assignment.setTotalMarks(assignmentdto.getTotalMarks());
        return assignmentDao.save(assignment);
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
}