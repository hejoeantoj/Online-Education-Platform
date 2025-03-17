package com.cts.assignmentmodule.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
public class Assignment {

    @Id
    private String assignmentId;

    @Column(name = "courseId", nullable = false)
    private String courseId;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private int totalMarks;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
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

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
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
