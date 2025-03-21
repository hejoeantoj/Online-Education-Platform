package com.cts.coursemodule.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.coursemodule.model.Course;
import com.cts.coursemodule.model.Lesson;

public interface LessonDAO extends JpaRepository<Lesson,String> {

	boolean existsByCourseAndLessonTitle(Course course, String lessonTitle);

}
