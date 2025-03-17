package com.cts.reportsmodule.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.reportsmodule.model.Course;
import com.cts.reportsmodule.model.Report;
import com.cts.reportsmodule.model.User;



public interface ReportDAO extends JpaRepository<Report,String> {

	//Report findByStudentIdAndCourseId(User student, Course course);

	Report findByStudentAndCourse(User student, Course course);

}
