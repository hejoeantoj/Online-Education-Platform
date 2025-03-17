package com.cts.reportsmodule.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "enrollment")
public class Enrollment {

    @Id
    @Column(name="enrollmentId")
    private String enrollmentId;
    
    @ManyToOne
    @JoinColumn(name = "studentId", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;

    @Column(name = "enrolledAt")
    private LocalDateTime enrolledAt;

    @PrePersist
    public void generateEnrollmentIdandDate() {
        this.enrollmentId = UUID.randomUUID().toString();
        this.enrolledAt = LocalDateTime.now();
    }

	public String getEnrollmentId() {
		return enrollmentId;
	}

	public void setEnrollmentId(String enrollmentId) {
		this.enrollmentId = enrollmentId;
	}

	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public LocalDateTime getEnrolledAt() {
		return enrolledAt;
	}

	public void setEnrolledAt(LocalDateTime enrolledAt) {
		this.enrolledAt = enrolledAt;
	}
    
    
}
