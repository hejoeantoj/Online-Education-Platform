import React from 'react';

const AssignmentResult = ({
    assignmentReport,
    hasUploadedBefore,
    isFetchingResult,
    resultMessage,
    onViewResultClick, // Callback to trigger fetch in parent
}) => {
    const handleViewClick = () => {
        if (!isFetchingResult) {
            onViewResultClick();
        }
    };

    return (
        <div className="assignment-result-section">
            <button
                onClick={handleViewClick}
                disabled={!hasUploadedBefore || isFetchingResult}
                className="view-result-button"
            >
                {/* <i className="fas fa-eye"></i>  */}
                {isFetchingResult ? 'Fetching Result...' : 'View Result'}
            </button>

            <div className="result-display">
                 {/* Show specific message if trying to view before upload */}
                 {!hasUploadedBefore && resultMessage && (
                    <p className="info-message">{resultMessage}</p>
                 )}

                 {/* Show results if available */}
                 {hasUploadedBefore && assignmentReport ? (
                    <div className="result-details">
                        <h3>Assignment Result</h3>
                        <p>
                            <strong>Percentage:</strong> {assignmentReport.percentage}%
                        </p>
                        <p>
                            <strong>Feedback:</strong> {assignmentReport.feedback || 'N/A'}
                        </p>
                        <p>
                            <strong>Reviewed At:</strong> {assignmentReport.reviewedAt ? new Date(assignmentReport.reviewedAt).toLocaleString() : 'Not yet reviewed'}
                        </p>
                    </div>
                 ) : hasUploadedBefore && resultMessage ? (
                     // Show messages like "No result available yet" or fetch errors
                     <p className="info-message">{resultMessage}</p>
                 ) : hasUploadedBefore && !isFetchingResult ? (
                     // Default state after upload but before clicking view result or if fetch yielded nothing initially
                     <p className="info-message">Click 'View Result' to check for feedback.</p>
                 ) : null /* Covers the case before upload */
                 }
            </div>
        </div>
    );
};

export default AssignmentResult;