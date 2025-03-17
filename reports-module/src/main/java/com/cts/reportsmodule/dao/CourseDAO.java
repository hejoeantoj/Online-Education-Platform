package com.cts.reportsmodule.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.reportsmodule.model.Course;



public interface CourseDAO extends JpaRepository<Course, String>{

}
