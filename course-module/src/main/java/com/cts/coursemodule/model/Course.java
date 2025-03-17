package com.cts.coursemodule.model;

import java.util.List;
import java.util.UUID;

import com.cts.coursemodule.enums.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    private String courseId;
    
    @Column(name = "courseTitle", nullable = false)
    private String courseTitle;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "instructorId", nullable = false)
    private String instructorId;
    

    @Enumerated(EnumType.STRING)
    private Category category;
    
	
	@Column(name = "duration" ,nullable =false)
	private Integer duration;
	
	@PrePersist
	public void generateUUID() {
    	if (this.courseId == null) {
            this.courseId = UUID.randomUUID().toString();
        }
	}
    

	@OneToMany(mappedBy = "course", cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    @JsonIgnore
    private List<Enrollment> enrollments;
	
	@OneToMany(mappedBy = "course", cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Lesson> lessons;

	/**
	 * @return the lessons
	 */
	public List<Lesson> getLessons() {
		return lessons;
	}

	/**
	 * @param lessons the lessons to set
	 */
	public void setLessons(List<Lesson> lessons) {
		this.lessons = lessons;
	}



    
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInstructorId() {
		return instructorId;
	}

	public void setInstructorId(String instructorId) {
		this.instructorId = instructorId;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Enrollment> getEnrollments() {
		return enrollments;
	}

	public void setEnrollments(List<Enrollment> enrollments) {
		this.enrollments = enrollments;
	}
    
	public Integer getDuration() {
		return duration;
	}
	
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	
}
