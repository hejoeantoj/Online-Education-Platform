// AssignmentList.js
import React from 'react';
import styles from '../css/AssignmentList.module.css'; // Import CSS Module

const AssignmentList = ({ assignments, onViewSubmissions }) => {
    return (
        <div className={styles.listContainer}>
            <h3 className={styles.subHeader}>Existing Assignments</h3>
            {assignments.length > 0 ? (
                <ul className={styles.list}>
                    {assignments.map(assignment => (

                        <li key={assignment.assignmentId} className={styles.listItem}>
                            <div className={styles.assignmentDetails}>
                                <span className={styles.assignmentQuestion}>{assignment.question}</span>
                                <span className={styles.assignmentMarks}>(Total Marks: {assignment.totalMarks})</span>
                            </div>
                            <button
                                onClick={() => onViewSubmissions(assignment.assignmentId)}
                                className={`${styles.button} ${styles.buttonSecondary}`} 
                            >
                                View Submissions
                            </button>
                        </li>
                    ))}
                </ul>
            ) : (
                <p className={styles.noAssignments}>No assignments created yet for this course.</p>
            )}
        </div>
    );
};

export default AssignmentList;