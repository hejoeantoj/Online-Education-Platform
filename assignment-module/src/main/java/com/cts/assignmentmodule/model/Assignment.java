package com.cts.assignmentmodule.model;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;

@Entity
public class Assignment {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private int assignmentId;

    @Column(nullable = false)
    private int courseId;

    @OneToMany(mappedBy = "assignment",fetch = FetchType.LAZY)
    private Set<Submission> submissions = new HashSet<>();

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private int totalMarks;

    private LocalDateTime createdAt;
    
    /**
	 * @return the createdAt
	 */
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@PrePersist
    public void generateCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

	/**
	 * @return the assignmentId
	 */
	public int getAssignmentId() {
		return assignmentId;
	}

	/**
	 * @param assignmentId the assignmentId to set
	 */
	public void setAssignmentId(int assignmentId) {
		this.assignmentId = assignmentId;
	}

	/**
	 * @return the courseId
	 */
	public int getCourseId() {
		return courseId;
	}

	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	/**
	 * @return the submissions
	 */
	public Set<Submission> getSubmissions() {
		return submissions;
	}

	/**
	 * @param submissions the submissions to set
	 */
	public void setSubmissions(Set<Submission> submissions) {
		this.submissions = submissions;
	}

	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * @return the totalMarks
	 */
	public int getTotalMarks() {
		return totalMarks;
	}

	/**
	 * @param totalMarks the totalMarks to set
	 */
	public void setTotalMarks(int totalMarks) {
		this.totalMarks = totalMarks;
	}
	public Assignment(int assignmentId, int courseId, Set<Submission> submissions, String question, int totalMarks,
			LocalDateTime createdAt) {
		super();
		this.assignmentId = assignmentId;
		this.courseId = courseId;
		this.submissions = submissions;
		this.question = question;
		this.totalMarks = totalMarks;
		this.createdAt = createdAt;
	}
    public Assignment() {}
    
}
