// LessonActions.js
import React from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import styles from './InstructorCourseDetails.module.css'; // Import the CSS module

const LessonActions = ({ lessonId, onLessonDeleted }) => {
    
    const email = localStorage.getItem("email");
    const token = localStorage.getItem(`${email}_token`);
    const uuid = localStorage.getItem(`${email}_uuid`);

    const handleDeleteLesson = async () => {
        if (window.confirm("Are you sure you want to delete this lesson? This action cannot be undone.")) {
            try {
                 await axios.delete(`http://localhost:8082/api/lesson/delete?lessonId=${lessonId}&instructorId=${uuid}`, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
                alert("Lesson deleted successfully!");
                onLessonDeleted(lessonId); // Notify parent about the deletion
            } catch (error) {
                console.error("Error deleting lesson:", error);
                alert("Failed to delete lesson.");
            }
        }
    };

    return (
        <div>
            <button onClick={handleDeleteLesson} className={`${styles.btn} ${styles.deleteLessonButton}`}>Delete Lesson</button>
        </div>
    );
};

export default LessonActions;