// UpdateLesson.js
import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import "./UpdateLesson.css"; // Import the CSS file

const UpdateLesson = ({ lesson, onLessonUpdated, onCancelUpdate }) => {
    const { courseId } = useParams();
    const navigate = useNavigate();
    const [lessonTitle, setLessonTitle] = useState(lesson?.title || "");
    const [content, setContent] = useState(lesson?.content || "");
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const email = localStorage.getItem("email");
    const token = localStorage.getItem(`${email}_token`);

    useEffect(() => {
        if (lesson) {
            setLessonTitle(lesson.title || "");
            setContent(lesson.content || "");
        }
    }, [lesson]);

    const handleUpdateLesson = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        const lessonData = {
            lessonId: lesson.lessonId,
            courseId: courseId,
            lessonTitle: lessonTitle,
            content: content,
            instructorId: localStorage.getItem(`${email}_uuid`)
        };

        try {
            const response = await axios.put("http://localhost:8082/api/lesson/update", lessonData, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            alert("Lesson updated successfully!");
            onLessonUpdated(response.data); // Notify parent about the update
        } catch (error) {
            console.error("Error updating lesson:", error);
            setError("Failed to update lesson.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="update-lesson-container">
            <h4>Update Lesson</h4>
            <form onSubmit={handleUpdateLesson} className="lesson-form">
                <div className="form-group">
                    <label htmlFor="lessonTitle">Lesson Title:</label>
                    <input
                        type="text"
                        id="lessonTitle"
                        value={lessonTitle}
                        onChange={(e) => setLessonTitle(e.target.value)}
                        required
                        className="form-control"
                    />
                </div>
                <div className="form-group content-group">
                    <label htmlFor="content">Content:</label>
                    <textarea
                        id="content"
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        required
                        className="form-control content-textarea"
                    />
                </div>
                <div className="form-actions">
                    <button type="submit" disabled={loading} className="submit-button">
                        {loading ? "Updating..." : "Update Lesson"}
                    </button>
                    <button type="button" onClick={onCancelUpdate} className="cancel-button">
                        Cancel
                    </button>
                </div>
                {error && <p className="error-message">{error}</p>}
            </form>
        </div>
    );
};

export default UpdateLesson;