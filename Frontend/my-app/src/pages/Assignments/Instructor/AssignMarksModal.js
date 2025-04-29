// AssignMarksModal.js
import React, { useState, useEffect } from 'react';
import Modal from 'react-modal';
import styles from '../css/ModalStyles.module.css'; 

const AssignMarksModal = ({
    isOpen,
    onRequestClose,
    selectedSubmission,
    selectedAssignment,
    token,
    instructorUuid,
    studentUsernames,
    onMarksAssigned,
}) => {
    const [obtainedMarks, setObtainedMarks] = useState('');
    const [feedback, setFeedback] = useState('');
    const [marksError, setMarksError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const studentUsername = selectedSubmission ? studentUsernames[selectedSubmission.studentId] : null;
    const studentIdDisplay = selectedSubmission ? `ID: ${selectedSubmission.studentId}` : '';
    const totalMarks = selectedAssignment ? selectedAssignment.totalMarks : 0;

     useEffect(() => {
        if (isOpen && selectedSubmission) {
            setObtainedMarks(selectedSubmission.obtainedMarks?.toString() ?? ''); 
            setFeedback(selectedSubmission.feedback || '');
            setMarksError(''); 
             setIsLoading(false);
        }
     
         if (!isOpen) {
            setObtainedMarks('');
            setFeedback('');
            setMarksError('');
            setIsLoading(false);
        }
    }, [isOpen, selectedSubmission]);

    const handleObtainedMarksChange = (e) => {
        const value = e.target.value;
        setObtainedMarks(value);
        validateMarks(value); 
    };

     const validateMarks = (value) => {
        if (selectedAssignment && value !== '') {
            const obtained = parseFloat(value);
            const total = parseFloat(selectedAssignment.totalMarks);
            if (isNaN(obtained) || obtained < 0) {
                setMarksError('Obtained marks must be a non-negative number.');
                return false;
            } else if (obtained > total) {
                setMarksError(`Marks cannot exceed total (${total}).`);
                 return false;
            } else {
                setMarksError(''); 
                return true;
            }
        } else if (value === '') {
             setMarksError(''); 
             return false; 
         }
         return true; 
    };

    const handleAssignMarks = async () => {
        if (!selectedAssignment || !selectedSubmission || !instructorUuid) {
            console.error("Cannot assign marks: Missing critical data.", {selectedAssignment, selectedSubmission, instructorUuid});
            setMarksError("An error occurred. Please close the modal and try again.");
            return;
        }

        // Final validation before submitting
         if (obtainedMarks === '') {
             setMarksError('Obtained marks are required.');
             return;
         }
         if (!validateMarks(obtainedMarks)) {
            // If validation function set an error, just return
             return;
         }
        // If validateMarks cleared the error but didn't return false (e.g., complex case), double-check
        if (marksError) {
             return;
         }

        setIsLoading(true); // Start loading

        try {
            const obtained = parseFloat(obtainedMarks);
            const response = await fetch('http://localhost:8083/api/asubmission/assignMarks', {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    studentId: selectedSubmission.studentId,
                    instructorId: instructorUuid, 
                    assignmentId: selectedAssignment.assignmentId,
                    obtainedMarks: obtained,
                    feedback: feedback.trim() || null 
                })
            });

            const data = await response.json();

            if (response.ok && data.success) {
                console.log('Marks assigned successfully', data);
                onMarksAssigned(selectedAssignment.assignmentId); 
                 onRequestClose();
            } else {
                console.error('Error assigning marks (API Response):', data);
                setMarksError(data.message || `Failed to assign marks (${response.status}).`);
            }
        } catch (error) {
            console.error('Error assigning marks (Network/Fetch):', error);
            setMarksError('Network error or failed to parse response. Please try again.');
        } finally {
            setIsLoading(false); 
        }
    };

    return (
        <Modal
            isOpen={isOpen}
            onRequestClose={onRequestClose}
            contentLabel="Assign Marks"
             className={styles.modalContent} 
             overlayClassName={styles.modalOverlay} 
        >
             <h3 className={styles.modalHeader}>
                Assign Marks for: {studentUsername || studentIdDisplay}
             </h3>

            {selectedSubmission && selectedAssignment ? (
                <div className={styles.form}>
                     <label htmlFor="obtainedMarks" className={styles.label}>
                        Obtained Marks (Max: {totalMarks})
                    </label>
                    <input
                        id="obtainedMarks"
                         type="number"
                         placeholder={`Enter marks (0 - ${totalMarks})`}
                         value={obtainedMarks}
                         onChange={handleObtainedMarksChange}
                         className={`${styles.input} ${marksError ? styles.inputError : ''}`} 
                         max={totalMarks} 
                        min="0" 
                        step="any" 
                        required 
                        disabled={isLoading}
                    />
                    {marksError && <p className={styles.errorMessage}>{marksError}</p>}

                    <label htmlFor="feedback" className={styles.label}>Feedback (Optional)</label>
                    <textarea
                        id="feedback"
                        placeholder="Provide constructive feedback to the student..."
                        value={feedback}
                        onChange={(e) => setFeedback(e.target.value)}
                        className={styles.textarea}
                        rows={4}
                        disabled={isLoading}
                    />

                    <div className={styles.modalActions}>
                         <button
                             onClick={onRequestClose}
                             className={`${styles.button} ${styles.buttonCancel}`}
                             disabled={isLoading}
                         >
                             Cancel
                        </button>
                        <button
                            onClick={handleAssignMarks}
                             disabled={isLoading || !!marksError || obtainedMarks === ''}
                            className={`${styles.button} ${styles.buttonPrimary}`}
                        >
                            {isLoading ? 'Submitting...' : 'Submit Marks'}
                        </button>
                    </div>
                </div>
            ) : (
             
                 <p>Loading submission details...</p>
            )}
        </Modal>
    );
};

export default AssignMarksModal;