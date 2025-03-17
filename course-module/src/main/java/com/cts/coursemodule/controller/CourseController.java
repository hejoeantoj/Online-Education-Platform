package com.cts.coursemodule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.coursemodule.dto.CourseDTO;
import com.cts.coursemodule.model.Course;
import com.cts.coursemodule.service.CourseService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/createcourse")
    public ResponseEntity<Course> createCourse(@Valid @RequestBody CourseDTO coursedto) {
        Course createdCourse = courseService.createCourse(coursedto);
        return ResponseEntity.ok(createdCourse);
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/courseDetails")
    public ResponseEntity<?> getCourseById(@RequestParam String courseId) {
        Optional<Course> course = courseService.getCourseById(courseId);
        return ResponseEntity.ok(course.get());
    }

    @GetMapping("/coursesByInstructor")
    public ResponseEntity<List<Course>> getCoursesByInstructorId(@RequestBody CourseDTO courseDTO) {
        List<Course> courses = courseService.getCoursesByInstructorId(courseDTO.getInstructorId().toString());
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/updatecourse")
    public ResponseEntity<Course> updateCourse(@RequestBody CourseDTO courseDTO) {
        Course updatedCourse = courseService.updateCourse(courseDTO);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/deleteCourse")
    public ResponseEntity<Void> deleteCourse(@RequestBody CourseDTO courseDTO) {
        courseService.deleteCourse(courseDTO);
        return ResponseEntity.ok().build();
    }
    
    
    @GetMapping("/verifyInstructor")
    public boolean verifyInstructor(@RequestParam String instructorId,@RequestParam String courseId) {
    	//System.out.println("hi");
    	return courseService.verifyInstructor(instructorId,courseId);
    }
    
}
