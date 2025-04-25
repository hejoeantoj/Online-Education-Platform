// ViewSubmissionsModal.js
import React from 'react';
import Modal from 'react-modal';
import styles from '../css/ModalStyles.module.css'; // Use shared modal styles
import listStyles from '../css/AssignmentList.module.css'; // Reuse some list item styles if applicable
 
const ViewSubmissionsModal = ({
    isOpen,
    onRequestClose,
    selectedAssignment,
    submissions,
    studentUsernames,
    onDownloadFile,
    onAssignMarks, // Renamed from handleAssignMarksClick for clarity
}) => {
    return (
        <Modal
            isOpen={isOpen}
            onRequestClose={onRequestClose}
            contentLabel="View Submissions"
            className={styles.modalContent} // Use shared class
            overlayClassName={styles.modalOverlay} // Use shared class
        >
            <h3 className={styles.modalHeader}>
                Submissions for: {selectedAssignment?.question || 'Assignment'}
                <span className={styles.totalMarksHeader}>(Total: {selectedAssignment?.totalMarks} Marks)</span>
            </h3>
 
            {submissions.length > 0 ? (
                <ul className={listStyles.list}> {/* Reuse list style */}
                     {submissions.map(submission => (
                        <li key={submission.submissionId || submission.studentId} className={`${listStyles.listItem} ${styles.submissionItem}`}> {/* Reuse list item style */}
                            <span className={styles.studentIdentifier}>
                                Student: {studentUsernames[submission.studentId] || `ID: ${submission.studentId}`}
                            </span>
                             {/* Button Group */}
                             <div className={styles.buttonGroup}>
                                <button
                                    onClick={() => onDownloadFile(submission)}
                                     // Apply specific style or reuse secondary
                                    className={`${styles.button} ${styles.buttonSecondary} ${styles.actionButton}`}
                                 >
                                    Download Answer
                                </button>
                                <button
                                    onClick={() => onAssignMarks(submission)} // Use the passed handler
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
 