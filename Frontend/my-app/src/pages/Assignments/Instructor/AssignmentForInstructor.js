// AssignmentForInstructor.js
import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import Modal from 'react-modal';
import CreateAssignmentModal from './CreateAssignmentModal';
import ViewSubmissionsModal from './ViewSubmissionsModal';
import AssignMarksModal from './AssignMarksModal';
import AssignmentList from './AssignmentList';
import styles from '../css/AssignmentForInstructor.module.css'; 
 
Modal.setAppElement('#root'); // For accessibility
 
const AssignmentForInstructor = () => {
    const { courseId } = useParams();
    const [course, setCourse] = useState(null);
    const [assignments, setAssignments] = useState([]);
    const [submissions, setSubmissions] = useState([]);
    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
    const [isSubmissionModalOpen, setIsSubmissionModalOpen] = useState(false);
    const [isAssignMarksModalOpen, setIsAssignMarksModalOpen] = useState(false);
    const [successMessage, setSuccessMessage] = useState('');
    const [selectedAssignment, setSelectedAssignment] = useState(null);
    const [selectedSubmission, setSelectedSubmission] = useState(null);
    const [studentUsernames, setStudentUsernames] = useState({});

 
    const email = localStorage.getItem('email');
    const token = localStorage.getItem(`${email}_token`);
    const instructorUuid = localStorage.getItem(`${email}_uuid`);
 
    useEffect(() => {
        if (token && courseId) {
            fetchAssignments();
        } else {
            console.error("Token or Course ID missing. Cannot fetch assignments.");
        }
    }, [courseId, token]);
 
    useEffect(() => {
        const fetchCourseDetails = async () => {
            try {
                const response = await axios.get(`http://localhost:8082/api/course/courseDetails?courseId=${courseId}`,{
   
                });
                setCourse(response.data.data || null);
            } catch (error) {
                console.error("Error fetching course details:", error);
            }
        };
        fetchCourseDetails();
    }, [courseId]);
 
    const fetchAssignments = async () => {
        try {
            const response = await axios.get(`http://localhost:8083/api/assignment/view?courseId=${courseId}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                }
            });
            if (response.data && response.data.success && Array.isArray(response.data.data)) {
                setAssignments(response.data.data);
            } else {
                setAssignments([]);
                console.error('Unexpected response structure:', response.data);
            }
        } catch (error) {
            console.error('There was an error fetching the assignments!', error);
            setAssignments([]);
        }
    };
 
    const fetchUsernames = async (studentIds) => {
        if (studentIds.length === 0 || !token) return;
 
        try {
            const response = await axios.post('http://localhost:8070/api/user/fetchUsernamesByIds', studentIds, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
            });
            if (response.data && response.data.success && response.data.data) {
                setStudentUsernames(prev => ({ ...prev, ...response.data.data }));
            } else {
                console.error('Failed to fetch usernames or unexpected data structure:', response.data);
            }
        } catch (error) {
            console.error('Error fetching usernames:', error);
        }
    };
 
    const handleViewSubmissions = async (assignmentId) => {
        const foundAssignment = assignments.find(assignment => assignment.assignmentId === assignmentId);
        if (!foundAssignment) {
            console.error("Could not find the selected assignment details.");
            return;
        }
        setSelectedAssignment(foundAssignment);
 
        try {
            const response = await axios.get(`http://localhost:8083/api/asubmission/viewByCourseIdandAssignmentId?courseId=${courseId}&assignmentId=${assignmentId}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
 
            if (response.data && response.data.success && Array.isArray(response.data.data)) {
                const allSubmissions = response.data.data;
                // Show only unreviewed assignments..
                const unreviewedSubmissions = allSubmissions.filter(submission => submission.obtainedMarks === null || submission.obtainedMarks === undefined);
                setSubmissions(unreviewedSubmissions);
                setIsSubmissionModalOpen(true);
 
                const studentIdsToFetch = unreviewedSubmissions
                    .map(sub => sub.studentId)
                    .filter(id => !studentUsernames[id]); // Only fetch if not already fetched
 
                if (studentIdsToFetch.length > 0) {
                    await fetchUsernames(studentIdsToFetch);
                }
            } else {
                setSubmissions([]); // Ensure submissions is an empty array on no data
                setIsSubmissionModalOpen(true); // Still open modal to show "no submissions" message
            }
 
        } catch (error) {
            console.error('There was an error fetching the submissions!', error);
            setSubmissions([]);
        }
    };
 
     const handleDownloadFile = async (submission) => {
        if (!selectedAssignment || !submission || !instructorUuid) {
            console.error("Missing IDs for downloading file.");
            alert("Cannot download file: Missing required information.");
            return;
        }
        const assignmentId = selectedAssignment.assignmentId;
        const studentId = submission.studentId;
 
        try {
            const response = await axios.get(`http://localhost:8083/api/asubmission/viewFiles`, {
                params: {
                    studentId: studentId,
                    assignmentId: assignmentId,
                    instructorId: instructorUuid 
                },
                headers: {
                    Authorization: `Bearer ${token}`
                },
                responseType: 'blob' 
            });
 
            // Extract filename from Content-Disposition header if available
             const contentDisposition = response.headers['content-disposition'];
            let filename = `submission_student_${studentUsernames[studentId]}_assignment_${assignmentId}.pdf`; // Default filename
            if (contentDisposition) {
                const filenameMatch = contentDisposition.match(/filename="?(.+)"?/i);
                if (filenameMatch && filenameMatch.length > 1) {
                    filename = filenameMatch[1];
                }
            }
 
            // Create a URL for the blob
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', filename); // Use extracted or default filename
            document.body.appendChild(link);
            link.click();
 
            // Clean up
            document.body.removeChild(link);
            window.URL.revokeObjectURL(url);
 
        } catch (error) {    //Error handling related to downloading...
            console.error('Error downloading the file:', error);
            if (error.response && error.response.status === 404) {
                 alert("Error downloading file: No file found for this submission.");
            } else if (error.response) {
                try {
                    const errorText = await error.response.data.text();
                    const errorJson = JSON.parse(errorText);
                    alert(`Error downloading file: ${errorJson.message || 'Server error'}`);
                } catch (parseError) {
                    alert("Error downloading file. Please check server logs or try again.");
                }
            }
             else {
                 alert("Error downloading file. Please try again.");
             }
        }
    };
 
    const handleAssignMarks = (submission) => {
        setSelectedSubmission(submission);
        setIsAssignMarksModalOpen(true); 
        setIsSubmissionModalOpen(false);
    };
 
     // Called when marks are successfully assigned in the modal
     const handleMarksAssigned = (assignmentId) => {
        fetchAssignments(); // Re-fetch assignments 
    
        const currentAssignmentId = selectedAssignment ? selectedAssignment.assignmentId : assignmentId;
        if (currentAssignmentId) {
            handleViewSubmissions(currentAssignmentId); // Refresh the view submission modal's data
        }
        
    };
 
    // Called when a new assignment is successfully created
    const handleCreateSuccess = () => {
        fetchAssignments(); // Refresh assignments list
        setSuccessMessage('Assignment created successfully!');
        setTimeout(() => setSuccessMessage(''), 3000); // Clear message after 3 seconds
    };
 
    return (
        <div className={styles.container}>
            <h2 className={styles.header}>
                Assignments : {course?.courseTitle || 'Loading...'}
            </h2>
            <button onClick={() => setIsCreateModalOpen(true)} className={`${styles.button} ${styles.buttonPrimary}`}>
                Create New Assignment
            </button>
            {successMessage && <p className={styles.successMessage}>{successMessage}</p>}
 
            <AssignmentList assignments={assignments} onViewSubmissions={handleViewSubmissions} />
 
            {/* Modals */}
            <CreateAssignmentModal
                isOpen={isCreateModalOpen}
                onRequestClose={() => setIsCreateModalOpen(false)}
                courseId={courseId}
                token={token}
                instructorUuid={instructorUuid}
                onCreateSuccess={handleCreateSuccess}
            />
 
            <ViewSubmissionsModal
                isOpen={isSubmissionModalOpen}
                 onRequestClose={() => {
                    setIsSubmissionModalOpen(false);
                    setSelectedAssignment(null); // Clear selection when closing
                    setSubmissions([]); // Clear submissions
                }}
                selectedAssignment={selectedAssignment}
                submissions={submissions}
                studentUsernames={studentUsernames}
                onDownloadFile={handleDownloadFile}
                onAssignMarks={handleAssignMarks} 
            />
 
             <AssignMarksModal
                isOpen={isAssignMarksModalOpen}
                onRequestClose={() => {
                    setIsAssignMarksModalOpen(false);
                    setSelectedSubmission(null); // Clear selected submission
                 
                 }}
                selectedSubmission={selectedSubmission}
                selectedAssignment={selectedAssignment} 
                token={token}
                instructorUuid={instructorUuid}
                studentUsernames={studentUsernames} // Pass usernames for display
                onMarksAssigned={handleMarksAssigned} // Callback after marks assigned
            />
        </div>
    );
};
 
export default AssignmentForInstructor;
 