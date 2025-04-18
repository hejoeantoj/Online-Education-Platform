
package com.cts.coursemodule.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.coursemodule.client.CommunicationClient;
import com.cts.coursemodule.client.UserClient;
import com.cts.coursemodule.dao.CourseDAO;
import com.cts.coursemodule.dto.CourseDTO;
import com.cts.coursemodule.model.Course;
import com.cts.coursemodule.exception.CourseAlreadyExistsException;
import com.cts.coursemodule.exception.CourseNotFoundException;
import com.cts.coursemodule.exception.InstructorNotAllowedException;
import com.cts.coursemodule.exception.InstructorNotFoundException;

@Service
public class CourseService {

    @Autowired
    private CourseDAO courseDAO;
    
    @Autowired
    private CommunicationClient communicationClient;
    
    @Autowired
    private UserClient userClient;
    
    
    
    
    /**
     * Checks if the instructor exists.
     * 
     * @param instructorId the ID of the instructor
     * @return true if the instructor exists, false otherwise
     */
    
    
    boolean checkInstructor(String instructorId) {
    	return userClient.checkInstructor(instructorId);
    }
    
    /**
     * Creates a new course.
     * 
     * @param courseDTO the course data transfer object containing course details
     * @return the created course
     * @throws InstructorNotAllowedException if the instructor is not allowed
     * @throws CourseAlreadyExistsException if a course with the same details already exists
     */
    
    
    public Course createCourse(CourseDTO courseDTO) {
    	
        
    	boolean verified=checkInstructor(courseDTO.getInstructorId().toString());
    	if(verified==false) {
    		throw new InstructorNotAllowedException("Instructor Not Allowed");
    	}
    		
    	
    	// Check if a course with the same title and description already exists for the same instructor
        List<Course> existingCourses = courseDAO.findByInstructorId(courseDTO.getInstructorId().toString());
        for (Course course : existingCourses) {
            if (course.getCourseTitle().equals(courseDTO.getCourseTitle()) &&
            	course.getCategory().equals(courseDTO.getCategory())&&
                course.getDescription().equals(courseDTO.getDescription())){
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
        communicationClient.createNewNotification("Hi Students.....A New Course "+courseDTO.getCourseTitle()+" Is Available....Ready To Learn?");
        return courseDAO.save(course);
    }

    /**
     * Updates an existing course.
     * 
     * @param courseDTO the course data transfer object containing updated course details
     * @return the updated course
     *
     */
    
    
    
    
    
    
    public Course updateCourse(CourseDTO courseDTO) {
    	
    	
        Course existingCourse = courseDAO.findById(courseDTO.getCourseId().toString())
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id " + courseDTO.getCourseId()));
       
        if(existingCourse.getInstructorId().equals(courseDTO.getInstructorId().toString())) { 
        existingCourse.setCourseTitle(courseDTO.getCourseTitle());
        existingCourse.setDescription(courseDTO.getDescription());
        existingCourse.setCategory(courseDTO.getCategory());
        existingCourse.setDuration(courseDTO.getDuration());
        return courseDAO.save(existingCourse);
        }else {
        	throw new InstructorNotAllowedException("Instructor Not Allowed");
        }
    }
    
    
    
    
    /**
     * Retrieves all courses.
     * 
     * @return the list of all courses
     */
    
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }
    
    
    /**
     * Retrieves courses by instructor ID.
     * 
     * @param instructorId the ID of the instructor
     * @return the list of courses
     * 
     */
    
    
    
    public List<Course> getCoursesByInstructorId(String instructorId) {
        List<Course> courses = courseDAO.findByInstructorId(instructorId);
//        if (courses.isEmpty()) {
//            throw new CourseNotFoundException("No courses found for instructor ID " + instructorId);
//        }
        return courses;
    }
    
    
    /**
     * Deletes a course by its ID and instructor ID.
     * 
     * @param courseId the ID of the course to delete
     * @param instructorId the ID of the instructor
     * @throws CourseNotFoundException if the course is not found
     * 
     */
    
    
    public void deleteCourse(String courseId,String instructorId) {
        Course existingCourse = courseDAO.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
       
        if(instructorId.equals(existingCourse.getInstructorId().toString())) {
                
        courseDAO.deleteById(courseId);
    }else {
    	throw new InstructorNotAllowedException("Instructor Not Allowed");
    }
 }

    
    
    
  
    
    
    
    
   /**
    * 
    * Integration with other modules;
    * 
   */
    

    public boolean verifyInstructor(String instructorId, String courseId) {
        Course course = courseDAO.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        if (instructorId.equals(course.getInstructorId())) {
        	System.out.println("hi");
            System.out.println(true);
            return true;
        }
        return false;
    }
    
    
    
    
    public Optional<Course> getCourseById(String courseId) {
        Optional<Course> course = courseDAO.findById(courseId);
        if (!course.isPresent()) {
            throw new CourseNotFoundException("Course not found with id " + courseId);
        }
        return course;
    }
    
    

	public boolean verifyCourse(String courseId) {
		 Course course = courseDAO.findById(courseId)
	                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
		 if(course==null) {
			 return false;	 
		 }
		
		return true;
	}

	public String getInstructorId(String courseId) {
		Course course = courseDAO.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
		return course.getInstructorId();
	}
    
    

    
    
    
    
    
}
