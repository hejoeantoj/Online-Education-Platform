// ViewSubmissionsModal.js
import React from 'react';
import Modal from 'react-modal';
import styles from '../css/ModalStyles.module.css'; 
import listStyles from '../css/AssignmentList.module.css'; 
 
const ViewSubmissionsModal = ({
    isOpen,
    onRequestClose,
    selectedAssignment,
    submissions,
    studentUsernames,
    onDownloadFile,
    onAssignMarks, 
}) => {
    return (
        <Modal
            isOpen={isOpen}
            onRequestClose={onRequestClose}
            contentLabel="View Submissions"
            className={styles.modalContent} 
            overlayClassName={styles.modalOverlay} 
        >
            <h3 className={styles.modalHeader}>
                Submissions for: {selectedAssignment?.question || 'Assignment'}
                <span className={styles.totalMarksHeader}>(Total: {selectedAssignment?.totalMarks} Marks)</span>
            </h3>
 
            {submissions.length > 0 ? (
                <ul className={listStyles.list}> 
                     {submissions.map(submission => (

                        <li key={submission.submissionId || submission.studentId} className={`${listStyles.listItem} ${styles.submissionItem}`}> 
                            <span className={styles.studentIdentifier}>
                                Student: {studentUsernames[submission.studentId] || `ID: ${submission.studentId}`}
                            </span>
                             
                             <div className={styles.buttonGroup}>
                                <button
                                    onClick={() => onDownloadFile(submission)}
    
                                    className={`${styles.button} ${styles.buttonSecondary} ${styles.actionButton}`}
                                 >
                                    Download Answer
                                </button>
                                <button
                                    onClick={() => onAssignMarks(submission)} 
                                    className={`${styles.button} ${styles.buttonPrimary} ${styles.actionButton}`}
                                >
                                     Assign Marks
                                </button>
                            </div>
                        </li>
                    ))}
                </ul>
            ) : (
                <p className={styles.noSubmissionsMessage}>No unreviewed submissions found for this assignment.</p>
            )}
 
            <div className={styles.modalActions}>
                <button
                    onClick={onRequestClose}
                    className={`${styles.button} ${styles.buttonCancel}`}
                >
                    Close
                </button>
            </div>
        </Modal>
    );
};
 
export default ViewSubmissionsModal;
 