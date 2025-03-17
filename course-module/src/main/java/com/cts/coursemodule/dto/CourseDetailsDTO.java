package com.cts.coursemodule.dto;

import java.util.UUID;

public class CourseDetailsDTO {
    
	private String courseId;
    private String courseTitle;

//    public CourseDetailsDTO(UUID courseId, String courseTitle) {
//        this.courseId = courseId;
//        this.courseTitle = courseTitle;
//    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }
}