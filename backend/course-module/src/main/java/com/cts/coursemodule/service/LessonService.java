
package com.cts.coursemodule.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.coursemodule.dao.CourseDAO;
import com.cts.coursemodule.dao.EnrollmentDAO;
import com.cts.coursemodule.dao.LessonDAO;
import com.cts.coursemodule.dto.LessonDTO;
import com.cts.coursemodule.exception.CourseNotFoundException;
import com.cts.coursemodule.exception.InstructorNotAllowedException;
import com.cts.coursemodule.exception.LessonAlreadyExistsException;
import com.cts.coursemodule.exception.LessonNotFoundException;
import com.cts.coursemodule.model.Course;
import com.cts.coursemodule.model.Enrollment;
import com.cts.coursemodule.model.Lesson;

@Service
public class LessonService {

	@Autowired
	private LessonDAO lessonDAO;

	@Autowired
	private CourseDAO courseDAO;

	@Autowired
	private EnrollmentDAO enrollmentDAO;
     
	/**
     * Adds a new lesson.
     * 
     * @param lessonDTO the lesson data transfer object containing lesson details
     * @return the created lesson
     * 
     */
	public Lesson addLesson(LessonDTO lessonDTO) {
		Course course = courseDAO.findById(lessonDTO.getCourseId().toString())
				.orElseThrow(() -> new CourseNotFoundException("Course not found"));

		// Check if a lesson with the same title already exists for the course

		boolean validInstructor = course.getInstructorId().equals(lessonDTO.getInstructorId().toString());
		if (validInstructor == false) {
			throw new InstructorNotAllowedException("instructor not allowed");
		}
		boolean lessonExists = lessonDAO.existsByCourseAndLessonTitle(course, lessonDTO.getLessonTitle());
		if (lessonExists) {
			throw new LessonAlreadyExistsException("Lesson with the same title already exists for this course"+lessonDTO.getLessonTitle()+"Alreday Exists");
		}

		Lesson newLesson = new Lesson();
		newLesson.setCourse(course);
		newLesson.setLessonTitle(lessonDTO.getLessonTitle());
		newLesson.setContent(lessonDTO.getContent());

		return lessonDAO.save(newLesson);
	}
     
	
	 /**
     * Updates an existing lesson.
     * 
     * @param lessonDTO the lesson data transfer object containing updated lesson details
     * @return the updated lesson
     *
     */
	
	public Lesson updateLesson(LessonDTO lessonDTO) throws Exception {

		Lesson lesson = lessonDAO.findById(lessonDTO.getLessonId().toString())
				.orElseThrow(() -> new LessonNotFoundException("Lesson not found"));

		Course course = courseDAO.findById(lessonDTO.getCourseId().toString())
				.orElseThrow(() -> new CourseNotFoundException("Course not found"));

		// Check if a lesson with the same title already exists for the course

		boolean validInstructor = course.getInstructorId().equals(lessonDTO.getInstructorId().toString());
		if (validInstructor == false) {
			throw new InstructorNotAllowedException("instructor not allowed");
		}

		lesson.setContent(lessonDTO.getContent());
		lesson.setLessonTitle(lessonDTO.getLessonTitle());

		return lessonDAO.save(lesson);

	}
	
	/**
     * Deletes a lesson by its ID and instructor ID.
     * 
     * @param lessonId the ID of the lesson to delete
     * @param instructorId the ID of the instructor
    *
     */

	public void deleteLesson(String lessonId, String instructorId) {
		Lesson lesson = lessonDAO.findById(lessonId).orElseThrow(() -> new LessonNotFoundException("Lesson not found"));

		boolean validInstructor = instructorId.equals(lesson.getCourse().getInstructorId());
		if (validInstructor == false) {
			throw new InstructorNotAllowedException("Instructor Not Allowed");
		}
		lessonDAO.delete(lesson);

	}
	
	/**
     * Retrieves all lessons for a course and user.
     * 
     * @param courseId the ID of the course
     * @param userId the ID of the user
     * @return the list of lessons
     */
	public List<Lesson> getAllLessons(String courseId, String userId) {

		Course course = courseDAO.findById(courseId).orElseThrow(() -> new CourseNotFoundException("Course not found"));

		List<Lesson> allLessons = courseDAO.findLessonsByCourseId(courseId);

		if (allLessons.isEmpty()) {
			throw new LessonNotFoundException("No Lesson Found For This Course");
		}

		List<Enrollment> enrollmentList = enrollmentDAO.findAllByCourseCourseId(courseId);
		List<String> studentList = new ArrayList<>();

		for (Enrollment enrollment : enrollmentList) {
			studentList.add(enrollment.getStudentId());
		}

		if (userId.equals(course.getInstructorId())) {
			return allLessons;

		} else if (studentList.contains(userId)) {
			return allLessons;
		} else {
			throw new RuntimeException("User does not belong to this course");
		}
	}
	
	
	/**
     * Views a lesson by its ID and user ID.
     * 
     * @param lessonId the ID of the lesson
     * @param userId the ID of the user
     * @return the lesson
     */
	

	public Lesson viewLesson(String lessonId, String userId) {
		Lesson lesson = lessonDAO.findById(lessonId).orElseThrow(() -> new LessonNotFoundException("Lesson not found"));

		List<Enrollment> enrollmentList = enrollmentDAO.findAllByCourseCourseId(lesson.getCourse().getCourseId());
		List<String> studentList = new ArrayList<>();

		for (Enrollment enrollment : enrollmentList) {
			studentList.add(enrollment.getStudentId());
		}

		if (userId.equals(lesson.getCourse().getInstructorId())) {
			return lesson;

		} else if (studentList.contains(userId)) {
			return lesson;
		} else {
			throw new RuntimeException("User does not belong to this course");
		}

	}

}
