
package com.cts.coursemodule.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.coursemodule.dao.CourseDAO;
import com.cts.coursemodule.dao.EnrollmentDAO;
import com.cts.coursemodule.dto.CourseDetailsDTO;
import com.cts.coursemodule.dto.EnrollmentDTO;
import com.cts.coursemodule.model.Course;
import com.cts.coursemodule.model.Enrollment;
import com.cts.coursemodule.exception.AlreadyEnrolledException;
import com.cts.coursemodule.exception.CourseNotFoundException;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentDAO enrollmentDao;

    @Autowired
    private CourseDAO courseDao;

    public Enrollment createEnrollment(EnrollmentDTO enrollmentdto) {
        // Retrieve course by ID
        Course course = courseDao.findById(enrollmentdto.getCourseId().toString())
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        // Check if the student is already enrolled in the course
        boolean isAlreadyEnrolled = enrollmentDao.existsByStudentIdAndCourse(enrollmentdto.getStudentId().toString(), course);
        if (isAlreadyEnrolled) {
            throw new AlreadyEnrolledException("You're already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(enrollmentdto.getStudentId().toString());
        enrollment.setCourse(course);

        return enrollmentDao.save(enrollment);
    }

  
    public List<Enrollment> getAllEnrollments() {
        return enrollmentDao.findAll();
    }

 
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

    /*
     * 
     * integrate with this user table to get the names of the students.
     * 
     * 
     */
    public List<String> getEnrollmentByCourseId(EnrollmentDTO enrollmentDTO) {
        List<Enrollment> enrollments = enrollmentDao.findAllByCourseCourseId(enrollmentDTO.getCourseId().toString());
        if (enrollments.isEmpty()) {
            throw new CourseNotFoundException("Course not found with id " + enrollmentDTO.getCourseId().toString());
        }

        List<String> studentList = new ArrayList<>();
        for (Enrollment ee : enrollments) {
            studentList.add(ee.getStudentId());
        }
        return studentList;
    }
    /*
     * 
     * For integration purpose-----need to verify it is correct studentid
     * 
     * 
     */
    public boolean verifyEnrollment(String studentId,String courseId) {
    	Course course = courseDao.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

    	boolean status=enrollmentDao.existsByStudentIdAndCourse(studentId, course);
    	return status;
    }
}
