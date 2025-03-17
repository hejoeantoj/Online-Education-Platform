package com.cts.reportsmodule.model;

import java.util.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name="report")
public class Report {
   
	@Id
	 private String reportId;
	 
	 @ManyToOne
	 @JoinColumn(name="studentId", nullable=false)
	 private User student;
	 
	 @ManyToOne
	 @JoinColumn(name="courseId", nullable=false)
	 private Course course;
	 
	 @Column(nullable = false)
	 private int averageMarks;
	 
	 @Column(nullable = false)
	 private boolean isCompleted;
	 
	 @PrePersist
	 public void generateUuid() {
	     this.reportId = UUID.randomUUID().toString();
	 }

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
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

	public int getAverageMarks() {
		return averageMarks;
	}

	public void setAverageMarks(int averageMarks) {
		this.averageMarks = averageMarks;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
	
	 
	
	
	
	
	
	
	
	
}
