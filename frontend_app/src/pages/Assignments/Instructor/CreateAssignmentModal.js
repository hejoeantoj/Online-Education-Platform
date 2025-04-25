import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import axios from 'axios';
import styles from '../css/ModalStyles.module.css'; // Use a shared modal style module

const CreateAssignmentModal = ({ isOpen, onRequestClose, courseId, token, instructorUuid, onCreateSuccess }) => {
    const [assignmentQuestion, setAssignmentQuestion] = useState('');
    const [assignmentMarks, setAssignmentMarks] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [isLoading, setIsLoading] = useState(false); 

  
    useEffect(() => {
        if (!isOpen) {
            setAssignmentQuestion('');
            setAssignmentMarks('');
            setErrorMessage('');
            setIsLoading(false);
        }
    }, [isOpen]);

    const handleCreateAssignment = async () => {
        setErrorMessage(''); // Clear previous errors
        if (!assignmentQuestion.trim() || !assignmentMarks.trim()) {
            setErrorMessage("Please provide both an assignment question and total marks.");
            return;
        }

        // Validation for assignment title: at least one alphabet
        const hasAlphabet = /[a-zA-Z]/.test(assignmentQuestion);
        if (!hasAlphabet) {
            setErrorMessage("Assignment question should contain at least one alphabet.");
            return;
        }

        const marks = parseFloat(assignmentMarks);
        if (isNaN(marks) || marks <= 0 || marks > 100) {
            setErrorMessage("Total marks can be between 1 and 100.");
            return;
        }
        if (!instructorUuid) {
            console.error("Instructor UUID missing, cannot create assignment.");
            setErrorMessage("Error: Instructor ID not found. Please log in again.");
            return;
        }

        setIsLoading(true); // Start loading indicator

        try {
            const response = await axios.post(
                'http://localhost:8083/api/assignment/create',
                {
                    courseId,
                    question: assignmentQuestion,
                    totalMarks: marks,
                    instructorId: instructorUuid // Ensure this matches backend expectation
                },
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                }
            );

            // Assuming backend sends { success: true, ... } on success
            if (response.data && response.data.success) {
                console.log('Assignment created successfully', response.data);
                onCreateSuccess(); // Notify parent component
                onRequestClose(); // Close modal
            } else {
                // Handle cases where backend indicates failure but doesn't throw HTTP error
                console.error('Failed to create assignment (API indicated failure):', response.data);
                setErrorMessage(response.data?.message || 'An unknown error occurred during creation.');
            }
        } catch (error) {
            console.error('Error creating the assignment:', error);
            if (error.response) {
               
                setErrorMessage(error.response.data?.message || `Server error: ${error.response.status}`);
            } else if (error.request) {
               
                setErrorMessage('Network error. Could not reach the server.');
            } else {
               
                setErrorMessage('An unexpected error occurred. Please try again.');
            }
        } finally {
            setIsLoading(false); 
        }
    };

    return (
        <Modal
            isOpen={isOpen}
            onRequestClose={onRequestClose}
            contentLabel="Create Assignment"
            className={styles.modalContent} // Use class from CSS Module
            overlayClassName={styles.modalOverlay} // Use class from CSS Module
        >
            <h3 className={styles.modalHeader}>Create New Assignment</h3>
            <div className={styles.form}>
                <label htmlFor="assignmentQuestion" className={styles.label}>Assignment Question</label>
                <textarea
                    id="assignmentQuestion"
                    placeholder="Enter the assignment question details here..."
                    value={assignmentQuestion}
                    onChange={(e) => setAssignmentQuestion(e.target.value)}
                    className={styles.textarea}
                    rows={5}
                    disabled={isLoading}
                />

                <label htmlFor="assignmentMarks" className={styles.label}>Total Marks</label>
                <input
                    id="assignmentMarks"
                    type="number"
                    placeholder="e.g., 100"
                    value={assignmentMarks}
                    onChange={(e) => setAssignmentMarks(e.target.value)}
                    className={styles.input}
                    min="1" // HTML5 validation
                    max="100"
                    disabled={isLoading}
                />

                {errorMessage && <p className={styles.errorMessage}>{errorMessage}</p>}

                <div className={styles.modalActions}>
                    <button
                        onClick={onRequestClose}
                        className={`${styles.button} ${styles.buttonCancel}`}
                        disabled={isLoading} // Disable while loading
                    >
                        Cancel
                    </button>
                    <button
                        onClick={handleCreateAssignment}
                        className={`${styles.button} ${styles.buttonPrimary}`}
                        disabled={isLoading || !assignmentQuestion.trim() || !assignmentMarks.trim() || !/[a-zA-Z]/.test(assignmentQuestion)} // Disable if loading or fields empty or no alphabet in question
                    >
                        {isLoading ? 'Creating...' : 'Create Assignment'}
                    </button>
                </div>
            </div>
        </Modal>
    );
};

export default CreateAssignmentModal;