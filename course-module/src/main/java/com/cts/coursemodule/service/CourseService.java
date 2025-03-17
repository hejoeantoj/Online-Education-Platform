
package com.cts.coursemodule.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.coursemodule.dao.CourseDAO;
import com.cts.coursemodule.dto.CourseDTO;
import com.cts.coursemodule.model.Course;
import com.cts.coursemodule.exception.CourseAlreadyExistsException;
import com.cts.coursemodule.exception.CourseNotFoundException;
import com.cts.coursemodule.exception.InstructorNotFoundException;

@Service
public class CourseService {

    @Autowired
    private CourseDAO courseDAO;

    
    public Course createCourse(CourseDTO courseDTO) {
    	
        // Check if a course with the same title and description already exists for the same instructor
    	
        List<Course> existingCourses = courseDAO.findByInstructorId(courseDTO.getInstructorId().toString());
        for (Course course : existingCourses) {
            if (course.getCourseTitle().equals(courseDTO.getCourseTitle()) &&
                course.getDescription().equals(courseDTO.getDescription())) {
                throw new CourseAlreadyExistsException("Course already exists with the same title and description for this instructor");
            }

            // Check if all contents are the same for the same instructor
            
            if (course.getCourseTitle().equals(courseDTO.getCourseTitle()) &&
                course.getDescription().equals(courseDTO.getDescription()) &&
                course.getInstructorId().equals(courseDTO.getInstructorId().toString()) &&
                course.getCategory().equals(courseDTO.getCategory()) &&
                course.getDuration().equals(courseDTO.getDuration())) {
                throw new CourseAlreadyExistsException("Course already exists with the same contents for this instructor");
            }
        }

        Course course = new Course();
        course.setCourseTitle(courseDTO.getCourseTitle());
        course.setDescription(courseDTO.getDescription());
        course.setInstructorId(courseDTO.getInstructorId().toString());
        course.setCategory(courseDTO.getCategory());
        course.setDuration(courseDTO.getDuration());
        return courseDAO.save(course);
    }

    // update course details by courseId
    
    public Course updateCourse(CourseDTO courseDTO) {
        Course existingCourse = courseDAO.findById(courseDTO.getCourseId().toString())
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id " + courseDTO.getCourseId()));
        existingCourse.setCourseTitle(courseDTO.getCourseTitle());
        existingCourse.setDescription(courseDTO.getDescription());
        existingCourse.setCategory(courseDTO.getCategory());
        existingCourse.setDuration(courseDTO.getDuration());
        return courseDAO.save(existingCourse);
    }
    
    // getting all courses available
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    // get course details by courseId
    public Optional<Course> getCourseById(String courseId) {
        Optional<Course> course = courseDAO.findById(courseId);
        if (!course.isPresent()) {
            throw new CourseNotFoundException("Course not found with id " + courseId);
        }
        return course;
    }
    
    
    
   /**
    * 
    * 
    * 
    *integreate with user table
    * 
    */
     
    
    
    // get courses by instructorId
    public List<Course> getCoursesByInstructorId(String instructorId) {
        List<Course> courses = courseDAO.findByInstructorId(instructorId);
        if (courses.isEmpty()) {
            throw new InstructorNotFoundException("No courses found for instructor ID " + instructorId);
        }
        return courses;
    }

    // delete existing course by courseId
    public void deleteCourse(CourseDTO courseDTO) {
        if (!courseDAO.existsById(courseDTO.getCourseId().toString())) {
            throw new CourseNotFoundException("Course not found with id " + courseDTO.getCourseId());
        }
        courseDAO.deleteById(courseDTO.getCourseId().toString());
    }

    public boolean verifyInstructor(String instructorId, String courseId) {
        Course course = courseDAO.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        if (instructorId.equals(course.getInstructorId())) {
            System.out.println(true);
            return true;
        }
        return false;
    }
}
