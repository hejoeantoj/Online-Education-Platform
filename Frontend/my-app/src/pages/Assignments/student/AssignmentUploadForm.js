import React, { useState } from 'react';
import axios from 'axios';

const AssignmentUploadForm = ({
    assignmentId,
    courseId,
    studentId,
    hasUploadedBefore,
    onUploadSuccess, // Callback for successful upload
}) => {
    const [file, setFile] = useState(null);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [uploadError, setUploadError] = useState('');
    const [localUploadSuccess, setLocalUploadSuccess] = useState(hasUploadedBefore);

    const handleFileChange = (e) => {
        e.stopPropagation();
        setFile(e.target.files[0]);
        setUploadError(''); // Clear error when a new file is selected
        setLocalUploadSuccess(false); // Reset success state if re-selecting
    };

    const handleUpload = async (e) => {
        e.preventDefault();
        if (!file) {
            setUploadError('Please select a file to upload.');
            return;
        }

        setIsSubmitting(true);
        setUploadError('');
        const email = localStorage.getItem('email');
        const token = localStorage.getItem(`${email}_token`);

        const formData = new FormData();
        formData.append('file', file);
        formData.append('assignmentId', assignmentId);
        formData.append('studentId', studentId);
        formData.append('courseId', courseId);

        try {
            const response = await axios.post('http://localhost:8083/api/asubmission/upload', formData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'multipart/form-data',
                },
            });
            console.log('Upload successful:', response.data);
            setLocalUploadSuccess(true);
            onUploadSuccess(); // Notify parent component
            alert('Assignment uploaded successfully!');
        } catch (error) {
            console.error('Error uploading assignment:', error);
            setLocalUploadSuccess(false);
            setUploadError(error.response?.data?.message || 'Failed to upload assignment. Please try again.');
        } finally {
            setIsSubmitting(false);
        }
    };

    // Determine button text and state
    let buttonText = 'Upload';
    if (isSubmitting) {
        buttonText = 'Uploading...';
    } else if (localUploadSuccess || hasUploadedBefore) {
        buttonText = 'Uploaded!';
    }

    const isUploadDisabled = !file || isSubmitting || localUploadSuccess || hasUploadedBefore;

    return (
        <div className="assignment-upload-form">
            <h3>Upload Assignment</h3>
            <form onSubmit={handleUpload}>
                <div className="file-input-container">
                    <input
                        type="file"
                        onClick={(e) => e.stopPropagation()}
                        onChange={handleFileChange}
                        accept="application/pdf, application/msword, application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                        id={`inputGroupFile_${assignmentId}`}
                        aria-describedby={`inputGroupFileAddon_${assignmentId}`}
                        // Button functionalities..
                        disabled={isSubmitting || localUploadSuccess || hasUploadedBefore}
                        // Required only if not already uploaded
                        required={!(localUploadSuccess || hasUploadedBefore)}
                        className="file-input"
                    />
                     {/* Display file name if selected */}
                     {file && !isSubmitting && !(localUploadSuccess || hasUploadedBefore) && (
                        <span className="file-name">{file.name}</span>
                    )}
                </div>

                <button
                    type="submit"
                    disabled={isUploadDisabled}
                    className="upload-button"
                >
                   
                    {buttonText}
                </button>
                {localUploadSuccess && <span className="upload-success-indicator">✔</span>}
                {uploadError && <p className="error-message">{uploadError}</p>}
            </form>
        </div>
    );
};

export default AssignmentUploadForm;