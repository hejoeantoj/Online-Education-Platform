package com.cts.assignmentmodule.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.assignmentmodule.dao.AssignmentDAO;
import com.cts.assignmentmodule.model.Assignment;


@Service
public class AssignmentService {
    @Autowired
    private AssignmentDAO assignmentDao;

    public List<Assignment> getAllAssignments() {
        return assignmentDao.findAll();
    }
    public Assignment createAssignment(Assignment assignment) {
        if (assignment.getQuestion() == null || assignment.getQuestion().isEmpty()) {
            throw new IllegalArgumentException("Question field cannot be null or empty");
        }
        return assignmentDao.save(assignment);
    }
    public void deleteAssignment(Integer id) {
        if (!assignmentDao.existsById(id)) {
            throw new IllegalArgumentException("Assignment with ID " + id + " does not exist");
        }
        assignmentDao.deleteById(id);
    }
}
    
    
    


}