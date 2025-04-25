import React, { useEffect, useState, useCallback } from 'react';
import axios from 'axios';

// Child components of Assignments
import AssignmentDetails from '../student/AssignmentDetails';
import AssignmentUploadForm from '../student/AssignmentUploadForm';
import AssignmentResult from '../student/AssignmentResult';

import '../css/AssignmentPopup.css';

const AssignmentPopup = ({ assignment, courseId, studentId, fetchEnrollmentDate, onClose }) => {
    const [dueDate, setDueDate] = useState('');
    const [uploadSuccess, setUploadSuccess] = useState(false); // Tracks if an upload has *ever* occurred
    const [resultMessage, setResultMessage] = useState('');
    const [hasUploadedBefore, setHasUploadedBefore] = useState(false); // From initial check
    const [isFetchingResult, setIsFetchingResult] = useState(false);
    const [assignmentReport, setAssignmentReport] = useState(null);
    const [isLoadingStatus, setIsLoadingStatus] = useState(true); // For initial status check


    useEffect(() => {
        const fetchDate = async () => {
            try {
                const enrollmentDate = await fetchEnrollmentDate(courseId, studentId);
                if (enrollmentDate) {
                    const date = new Date(enrollmentDate);
                    date.setDate(date.getDate() + 30); 
                    setDueDate(date.toISOString().split('T')[0]);
                } else {
                    console.warn("Could not fetch enrollment date.");
                    setDueDate('N/A');
                }
            } catch (error) {
                console.error("Error fetching enrollment date:", error);
                setDueDate('Error');
            }
        };
        fetchDate();
    }, [courseId, studentId, fetchEnrollmentDate]);

    // Check Initial Submission Status
    useEffect(() => {
        const checkSubmissionStatus = async () => {
            setIsLoadingStatus(true);
            const email = localStorage.getItem('email');
            const token = localStorage.getItem(`${email}_token`);
            if (!token || !studentId || !assignment?.assignmentId) {
                console.warn("Missing data for submission status check.");
                setIsLoadingStatus(false);
                return; // Don't proceed if essential data is missing
            }
            const verifySubmissionUrl = `http://localhost:8083/api/asubmission/verifySubmission?studentId=${studentId}&assignmentId=${assignment.assignmentId}`;

            try {
                const response = await axios.get(verifySubmissionUrl, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                console.log('Initial Submission status:', response.data);
                const alreadySubmitted = response.data?.success && response.data?.data === true;
                setHasUploadedBefore(alreadySubmitted);
                setUploadSuccess(alreadySubmitted); // Sync parent state
            } catch (error) {
                console.error('Error checking submission status:', error);
            
            } finally {
                setIsLoadingStatus(false);
            }
        };

        checkSubmissionStatus();
    }, [studentId]); // Add assignment?.assignmentId for safety


    // --- Callback Handlers ---

    // Called by AssignmentUploadForm on successful upload
    const handleUploadSuccess = useCallback(() => {
        setUploadSuccess(true);
        setHasUploadedBefore(true); // Mark as uploaded for this session too
        setAssignmentReport(null); // Clear previous report if re-uploading
        setResultMessage('');      // Clear any previous messages
    }, []);

    // Called by AssignmentResult when 'View Result' is clicked
    const handleFetchResult = useCallback(async () => {
        if (!hasUploadedBefore) {
            setResultMessage('Please upload your assignment first to view results.');
            setAssignmentReport(null);
            return;
        }

        setIsFetchingResult(true);
        setResultMessage(''); // Clear previous messages
        setAssignmentReport(null); // Clear previous report
        const email = localStorage.getItem('email');
        const token = localStorage.getItem(`${email}_token`);
        if (!token) {
             setResultMessage('Authentication error. Please log in again.');
             setIsFetchingResult(false);
             return;
        }
        const viewResultUrl = `http://localhost:8083/api/asubmission/assignmentReport?studentId=${studentId}&assignmentId=${assignment.assignmentId}`;

        try {
            const response = await axios.get(viewResultUrl, {
                headers: { Authorization: `Bearer ${token}` },
            });
            console.log('Assignment Report Response:', response.data);
            if (response.data?.success && response.data?.data) {
                 // Check if data has meaningful content (not just empty object/defaults)
                 if (response.data.data.percentage !== null || response.data.data.feedback) {
                     setAssignmentReport(response.data.data);
                 } else {
                     setResultMessage('Result is pending review.');
                     setAssignmentReport(null); // Ensure no stale report is shown
                 }
            } else if (response.data?.message) {
                 setResultMessage(response.data.message || 'No result available yet.');
                 setAssignmentReport(null);
            }
            else {
                setResultMessage('No result available yet.');
                setAssignmentReport(null);
            }
        } catch (error) {
            console.error('Error fetching assignment report:', error);
             const errorMsg = error.response?.data?.message || 'Failed to fetch result. Please try again.';
            setResultMessage(errorMsg);
            setAssignmentReport(null);
        } finally {
            setIsFetchingResult(false);
        }
    }, [hasUploadedBefore, studentId, assignment?.assignmentId]); // Add dependencies

    // Stop propagation on the main div click if it's acting as an overlay background
    const handlePopupClick = (e) => {
         e.stopPropagation();
    }

    // Render loading state while checking status
    if (isLoadingStatus) {
        return (
            <div className="assignment-popup-overlay" onClick={onClose}> {/* Optional Overlay */}
                <div className="assignment-popup-content loading" onClick={handlePopupClick}>
                    Loading assignment status...
                </div>
            </div>
        );
    }

    // Main Render
    return (
        // Optional: Add an overlay div that closes the popup when clicked
        <div className="assignment-popup-overlay" onClick={onClose}>
            {/* Stop propagation on the content area so clicks inside don't close it */}
            <div className="assignment-popup-content" onClick={handlePopupClick}>
                {/* Close Button */}
                <button onClick={onClose} className="close-button" aria-label="Close">
                    &times; {/* Simple X icon */}
                </button>

                {/* Assignment Details */}
                <AssignmentDetails
                    question={assignment?.question || 'Assignment Question'}
                    totalMarks={assignment?.totalMarks || 'N/A'}
                    dueDate={dueDate}
                />

                <hr className="divider" />

                {/* Upload Form */}
                <AssignmentUploadForm
                    assignmentId={assignment?.assignmentId}
                    courseId={courseId}
                    studentId={studentId}
                    hasUploadedBefore={hasUploadedBefore} // Pass initial status
                    onUploadSuccess={handleUploadSuccess} // Pass callback
                />

                <hr className="divider" />

                {/* Result Section */}
                <AssignmentResult
                    assignmentReport={assignmentReport}
                    hasUploadedBefore={hasUploadedBefore || uploadSuccess} // Consider both initial and session upload status
                    isFetchingResult={isFetchingResult}
                    resultMessage={resultMessage}
                    onViewResultClick={handleFetchResult} // Pass callback
                />

            </div>
        </div>
    );
};

export default AssignmentPopup;