
package com.cts.coursemodule.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.coursemodule.dao.CourseDAO;
import com.cts.coursemodule.dao.LessonDAO;
import com.cts.coursemodule.dto.LessonDTO;
import com.cts.coursemodule.model.Course;
import com.cts.coursemodule.model.Lesson;
import com.cts.coursemodule.exception.CourseNotFoundException;
import com.cts.coursemodule.exception.InstructorNotAllowedException;
import com.cts.coursemodule.exception.LessonAlreadyExistsException;
import com.cts.coursemodule.exception.LessonNotFoundException;

@Service
public class LessonService {
    
    @Autowired
    private LessonDAO lessonDAO;
    
    @Autowired
    private CourseDAO courseDAO;

    public Lesson addLesson(LessonDTO lessonDTO) {
        Course course = courseDAO.findById(lessonDTO.getCourseId().toString())
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        // Check if a lesson with the same title already exists for the course
        
        boolean validInstructor=course.getInstructorId().equals(lessonDTO.getInstructorId());
        if(validInstructor==false) {
        	throw new InstructorNotAllowedException("instructor not allowed");
        }
        boolean lessonExists = lessonDAO.existsByCourseAndLessonTitle(course, lessonDTO.getLessonTitle());
        if (lessonExists) {
            throw new LessonAlreadyExistsException("Lesson with the same title already exists for this course");
        }

        Lesson newLesson = new Lesson();
        newLesson.setCourse(course);
        newLesson.setLessonTitle(lessonDTO.getLessonTitle());
        newLesson.setContent(lessonDTO.getContent());

        return lessonDAO.save(newLesson);
    }

    public Lesson updateLesson(LessonDTO lessonDTO) throws Exception {
    	
    	Course course = courseDAO.findById(lessonDTO.getCourseId().toString())
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        // Check if a lesson with the same title already exists for the course
        
        boolean validInstructor=course.getInstructorId().equals(lessonDTO.getInstructorId());
        if(validInstructor==false) {
        	throw new InstructorNotAllowedException("instructor not allowed");
        }
        
        Lesson lesson = lessonDAO.findById(lessonDTO.getLessonId().toString())
        		.orElseThrow(() -> new LessonNotFoundException("Lesson not found"));

            lesson.setContent(lessonDTO.getContent());
            lesson.setLessonTitle(lessonDTO.getLessonTitle());
          
            return lessonDAO.save(lesson);
        
    }

    public void deleteLesson(String lessonId)  {
    	Lesson lesson = lessonDAO.findById(lessonId)
        		.orElseThrow(() -> new LessonNotFoundException("Lesson not found"));
            lessonDAO.delete(lesson);
        
    }

    public List<Lesson> getAllLessons(String courseId) {
    	
    	List<Lesson> allLessons = courseDAO.findLessonsByCourseId(courseId)  ;   
    	if(allLessons.isEmpty()) {
    		throw new LessonNotFoundException("No Lesson Found For This Course");
    	}
    	return allLessons;
    }

   
}