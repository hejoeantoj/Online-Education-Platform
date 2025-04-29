
package com.cts.coursemodule.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.coursemodule.client.UserClient;
import com.cts.coursemodule.dao.CourseDAO;
import com.cts.coursemodule.dao.EnrollmentDAO;
import com.cts.coursemodule.dto.CourseDetailsDTO;
import com.cts.coursemodule.dto.EnrollmentDTO;
import com.cts.coursemodule.model.Course;
import com.cts.coursemodule.model.Enrollment;
import com.cts.coursemodule.exception.AlreadyEnrolledException;
import com.cts.coursemodule.exception.CourseNotFoundException;
import com.cts.coursemodule.exception.InstructorNotAllowedException;
import com.cts.coursemodule.exception.StudentNotFoundException;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentDAO enrollmentDao;

    @Autowired
    private CourseDAO courseDao;
    
    
    @Autowired
    private UserClient userClient;
    
    /**
     * Checks if the student exists.
     * 
     * @param studentId the ID of the student
     * @return true if the student exists, false otherwise
     */
    
    public boolean checkStudent(String studentId) {
    	return userClient.checkStudent(studentId);
    }
    
    /**
     * Creates a new enrollment.
     * 
     * @param enrollmentDTO the enrollment data transfer object containing enrollment details
     * @return the created enrollment
     */
    public Enrollment createEnrollment(EnrollmentDTO enrollmentDTO) {
        
    	boolean verified=checkStudent(enrollmentDTO.getStudentId().toString());
    	if(verified==false) {
    		throw new StudentNotFoundException("Student Not Allowed");
    	}
    	
    	
    	// Retrieve course by ID
        Course course = courseDao.findById(enrollmentDTO.getCourseId().toString())
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        // Check if the student is already enrolled in the course
        boolean isAlreadyEnrolled = enrollmentDao.existsByStudentIdAndCourse(enrollmentDTO.getStudentId().toString(), course);
        if (isAlreadyEnrolled) {
            throw new AlreadyEnrolledException("You're already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(enrollmentDTO.getStudentId().toString());
        enrollment.setCourse(course);

        return enrollmentDao.save(enrollment);
    }

    /**
     * Retrieves all enrollments.
     * 
     * @return the list of all enrollments
     */
    public List<Enrollment> getAllEnrollments() {
        return enrollmentDao.findAll();
    }

    /**
     * Retrieves enrollments by student ID.
     * 
     * @param studentId the ID of the student
     * @return the list of course details
     */
    public List<CourseDetailsDTO> getEnrollmentByStudentId(String studentId) {
        List<CourseDetailsDTO> courseDetailsList = new ArrayList<>();
        List<Enrollment> enrollments = enrollmentDao.findByStudentId(studentId);

        for (Enrollment enrollment : enrollments) {
            Course course = enrollment.getCourse();
            CourseDetailsDTO courseDetails = new CourseDetailsDTO();
            courseDetails.setCourseId(course.getCourseId());
            courseDetails.setCourseTitle(course.getCourseTitle());
            
            courseDetailsList.add(courseDetails);
        }
        return courseDetailsList;
    }

    /**
     * Retrieves enrollments by course ID.
     * 
     * @param courseId the ID of the course
     * @return the list of student IDs
     * 
     */
    
    public List<String> getEnrollmentByCourseId(String courseId) {
        List<Enrollment> enrollments = enrollmentDao.findAllByCourseCourseId(courseId);
        if (enrollments.isEmpty()) {
            throw new CourseNotFoundException("Course not found ");
        }

        List<String> studentList = new ArrayList<>();
        for (Enrollment ee : enrollments) {
            studentList.add(ee.getStudentId());
        }
        return studentList;
    }
    
	 public LocalDate getEnrollmentDate(String studentId, String courseId) {
	        Optional<Enrollment> enrollmentOptional = enrollmentDao.findByStudentIdAndCourse_CourseId(studentId, courseId);
	        if (enrollmentOptional.isPresent()) {
	            return enrollmentOptional.get().getDateOfEnrollment();
	        }
	        return null;
	    }
    
    
    
    /*
     * 
     * For integration with other services..
     * 
     * 
     */
    
    public boolean verifyEnrollment(String studentId,String courseId) {
    	Course course = courseDao.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

    	boolean status=enrollmentDao.existsByStudentIdAndCourse(studentId, course);
    	return status;
    }


	public List<String> studentList(String courseId) {
		System.out.println(courseId);
		Course course = courseDao.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
		
		List<Enrollment> enrollmentList = enrollmentDao.findAllByCourseCourseId(courseId);
		List<String> studentList=new ArrayList<>();
		
		for(Enrollment enrollment:enrollmentList) {
			studentList.add(enrollment.getStudentId());
		}
		return studentList;
	}
}
