package com.cts.coursemodule.model;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    private String enrollmentId;
    
    @Column( name="studentId", nullable = false)
    private String studentId;
    
    @ManyToOne
    @JoinColumn(name="courseId", nullable = false)
    private Course course;
    
    @Column(name = "dateOfEnrollment", nullable = false)
    private LocalDate dateOfEnrollment;
    
    
    
    @PrePersist
	public void generateUUIDandDateOfEnrollment() {
            this.enrollmentId = UUID.randomUUID().toString();
            this.dateOfEnrollment = LocalDate.now();
    }
    
	public String getEnrollmentId() {
		return enrollmentId;
	}

	public void setEnrollmentId(String enrollmentId) {
		this.enrollmentId = enrollmentId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public LocalDate getDateOfEnrollment() {
		return dateOfEnrollment;
	}

	public void setDateOfEnrollment(LocalDate dateOfEnrollment) {
		this.dateOfEnrollment = dateOfEnrollment;
	}	
	
}
