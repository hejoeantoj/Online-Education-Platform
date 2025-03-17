package com.cts.coursemodule.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cts.coursemodule.model.Course;
import com.cts.coursemodule.model.Lesson;

@Repository
public interface CourseDAO extends JpaRepository<Course, String>{

	List<Course> findByInstructorId(String instructorId);

	@Query("SELECT l FROM Lesson l WHERE l.course.courseId = :courseId")
    List<Lesson> findLessonsByCourseId(@Param("courseId") String courseId);
}