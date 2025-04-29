import React from 'react';

const AssignmentDetails = ({ question, totalMarks, dueDate }) => {
    return (
        <div className="assignment-details">
            <h2>{question}</h2>
            <p>
                <strong>Total Marks:</strong> {totalMarks}
            </p>
            <p>
                <strong>Due Date:</strong> {dueDate || 'Loading...'}
            </p>
        </div>
    );
};

export default AssignmentDetails;