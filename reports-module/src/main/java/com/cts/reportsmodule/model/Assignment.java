package com.cts.reportsmodule.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
public class Assignment {

    @Id
    private String assignmentId;

    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private int totalMarks;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL)
    private List<AssignmentSubmission> submissions ;

    @PrePersist
    public void generateAssignmentId() {
        this.assignmentId = UUID.randomUUID().toString();
    }

	public String getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getTotalMarks() {
		return totalMarks;
	}

	public void setTotalMarks(int totalMarks) {
		this.totalMarks = totalMarks;
	}

	public List<AssignmentSubmission> getSubmissions() {
		return submissions;
	}

	public void setSubmissions(List<AssignmentSubmission> submissions) {
		this.submissions = submissions;
	}
    
    
   
}
