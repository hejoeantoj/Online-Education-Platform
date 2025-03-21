package com.cts.coursemodule.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public class LessonDTO {

    @Null(groups = Create.class)
    @NotNull(groups = Update.class)
    private UUID lessonId;

    @NotEmpty(message = "Lesson Title cannot be Empty")
    private String lessonTitle;

    @NotNull(message = "Course ID cannot be null")
    private UUID courseId;

    @NotNull(message = "Instructor ID cannot be null")
    private UUID instructorId;

    @NotEmpty(message = "Content cannot be Empty")
    private String content;

    // Getters and setters...

    public UUID getLessonId() {
        return lessonId;
    }

    public void setLessonId(UUID lessonId) {
        this.lessonId = lessonId;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    public UUID getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(UUID instructorId) {
        this.instructorId = instructorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public interface Create {}
    public interface Update {}
}