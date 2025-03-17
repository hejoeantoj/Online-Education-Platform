package com.cts.reportsmodule.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @Column(name="courseId")
    private String courseId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description",nullable = false)
    private String description;

    @Column(name = "category",nullable = false)
    private String category;

    @Column(name = "duration",nullable = false)
    private Long duration;

    @Column(name = "content",nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "instructorId",referencedColumnName="userId" ,nullable = false)
    private User instructorId;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Enrollment> enrollments;

    @PrePersist
    public void generateCourseId() {
    	this.courseId=UUID.randomUUID().toString();
    }

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getInstructorId() {
		return instructorId;
	}

	public void setInstructorId(User instructorId) {
		this.instructorId = instructorId;
	}

	public List<Enrollment> getEnrollments() {
		return enrollments;
	}

	public void setEnrollments(List<Enrollment> enrollments) {
		this.enrollments = enrollments;
	}
     
    
   
}
