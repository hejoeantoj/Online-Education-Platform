package com.cts.coursemodule.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.cts.coursemodule.dto.LessonDTO;
import com.cts.coursemodule.dto.LessonDetails;
import com.cts.coursemodule.dto.ResultResponse;
import com.cts.coursemodule.exception.LessonNotFoundException;
import com.cts.coursemodule.model.Lesson;
import com.cts.coursemodule.service.LessonService;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/lesson")
public class LessonController {

    @Autowired
    private LessonService lessonService;
    
    
    /* Adds a new lesson.
    * 
    * @param lessonDTO the lesson data transfer object containing lesson details
    * @return ResponseEntity containing the lesson details and HTTP status
    */
    
    @PostMapping("/add")
    public ResponseEntity<LessonDetails> addLesson(@Validated(LessonDTO.Create.class) @RequestBody LessonDTO lessonDTO) {
        LessonDetails lessonDetails = new LessonDetails();
        Lesson lesson = lessonService.addLesson(lessonDTO);
        lessonDetails.setLessonId(lesson.getLessonId());
        lessonDetails.setCourseTitle(lesson.getCourse().getCourseTitle());
        lessonDetails.setLessonTitle(lesson.getLessonTitle());
        lessonDetails.setContent(lesson.getContent());
        return new ResponseEntity<>(lessonDetails, HttpStatus.CREATED);
    }
    
    
    /**
     * Updates an existing lesson.
     * 
     * @param lessonDTO the lesson data transfer object containing updated lesson details
     * @return ResponseEntity containing the updated lesson details and HTTP status
     * 
     */

    @PutMapping("/update")
    public ResponseEntity<LessonDetails> updateLesson(@Validated(LessonDTO.Update.class) @RequestBody LessonDTO lessonDTO) throws Exception {
        LessonDetails lessonDetails = new LessonDetails();
        Lesson updatedLesson = lessonService.updateLesson(lessonDTO);
        lessonDetails.setLessonId(updatedLesson.getLessonId());
        lessonDetails.setCourseTitle(updatedLesson.getCourse().getCourseTitle());
        lessonDetails.setLessonTitle(updatedLesson.getLessonTitle());
        lessonDetails.setContent(updatedLesson.getContent());
        return new ResponseEntity<>(lessonDetails, HttpStatus.OK);
    }
     
    /**
     * Deletes a lesson by its ID and instructor ID.
     * 
     * @param lessonId the ID of the lesson to delete
     * @param instructorId the ID of the instructor
     * @return ResponseEntity containing HTTP status
     */

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteLesson(@RequestParam String lessonId,String instructorId ) {
        try {
            lessonService.deleteLesson(lessonId,instructorId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    
    /**
     * Retrieves all lessons for a course and user.
     * 
     * @param courseId the ID of the course
     * @param userId the ID of the user
     * @return ResponseEntity containing the list of lessons and HTTP status
     */
    

    @GetMapping("/getAll")
    public ResponseEntity<List<Lesson>> getAllLessons(@RequestParam String courseId,@RequestParam String userId) {
        List<Lesson> lessons = lessonService.getAllLessons(courseId,userId);
        return new ResponseEntity<>(lessons, HttpStatus.OK);
    }
    
    /**
     * Views a lesson by its ID and user ID.
     * 
     * @param lessonId the ID of the lesson
     * @param userId the ID of the user
     * @return ResponseEntity containing the lesson details and HTTP status
     */
    
    @GetMapping("/view")
    public ResponseEntity<ResultResponse<Lesson>> viewLesson(@RequestParam String lessonId,@RequestParam String userId) {
    	ResultResponse<Lesson> response=new ResultResponse<>();
        Lesson lesson = lessonService.viewLesson(lessonId,userId);
        response.setSuccess(true);
        response.setData(lesson);
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


   
}