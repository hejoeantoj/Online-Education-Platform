package com.cts.coursemodule.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cts.coursemodule.dto.LessonDTO;
import com.cts.coursemodule.dto.LessonDetails;
import com.cts.coursemodule.exception.LessonNotFoundException;
import com.cts.coursemodule.model.Lesson;
import com.cts.coursemodule.service.LessonService;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @PostMapping("/addlesson")
    public ResponseEntity<LessonDetails> addLesson(@RequestBody LessonDTO lessonDTO) {
    	LessonDetails lessonDetails=new LessonDetails();
        Lesson lesson = lessonService.addLesson(lessonDTO);
        lessonDetails.setLessonId(lesson.getLessonId());
        lessonDetails.setCourseTitle(lesson.getCourse().getCourseTitle());
        lessonDetails.setLessonTitle(lesson.getLessonTitle());
        lessonDetails.setContent(lesson.getContent());
        return new ResponseEntity<>(lessonDetails, HttpStatus.CREATED);
    }

    @PutMapping("/updatelesson")
    public ResponseEntity<LessonDetails> updateLesson(@RequestBody LessonDTO lesson) {
    	LessonDetails lessonDetails=new LessonDetails();
        try {
            Lesson updatedLesson = lessonService.updateLesson(lesson);
            lessonDetails.setLessonId(updatedLesson.getLessonId());
            lessonDetails.setCourseTitle(updatedLesson.getCourse().getCourseTitle());
            lessonDetails.setLessonTitle(updatedLesson.getLessonTitle());
            lessonDetails.setContent(updatedLesson.getContent());
            return new ResponseEntity<>(lessonDetails, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deletelesson")
    public ResponseEntity<Void> deleteLesson(@RequestParam String lessonId ) {
        try {
            lessonService.deleteLesson(lessonId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getlessons")
    public ResponseEntity<List<Lesson>> getAllLessons(@RequestParam String courseId) {
        List<Lesson> lessons = lessonService.getAllLessons(courseId);
        return new ResponseEntity<>(lessons, HttpStatus.OK);
    }

   
}