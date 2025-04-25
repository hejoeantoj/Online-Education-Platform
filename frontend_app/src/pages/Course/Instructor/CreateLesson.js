import React, {useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import "./CreateLesson.css"; // Import the CSS file

const CreateLesson = ({ onLessonCreated }) => {
    const { courseId } = useParams();
    const navigate = useNavigate();
    const [lessonTitle, setLessonTitle] = useState("");
    const [content, setContent] = useState("");
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const email = localStorage.getItem("email");
    const token = localStorage.getItem(`${email}_token`);
    const uuid = localStorage.getItem(`${email}_uuid`);


    const handleCreateLesson = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        const lessonData = {
            courseId,
            instructorId: uuid,
            lessonTitle,
            content
        };
        console.log(lessonData);
        try {
            await axios.post("http://localhost:8082/api/lesson/add", lessonData, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            alert("Lesson created successfully!");
            setContent('');
            setLessonTitle('');
            navigate(`/instructor/course/${courseId}`);
        } catch (error) {
            console.error("Error creating lesson:", error);
            setError("Failed to create lesson.");
        } finally {
            setLoading(false);
        }

        onLessonCreated();
    };

    return (
        <div className="create-lesson-container">
            <h2>Create New Lesson</h2>
            <form onSubmit={handleCreateLesson} className="lesson-form">
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
                    <label htmlFor="content">Lesson Content:</label>
                    <textarea
                        id="content"
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        required
                        className="form-control content-textarea"
                    />
                </div>
                <button type="submit" disabled={loading} className="submit-button">
                    {loading ? "Creating..." : "Create Lesson"}
                </button>
                {error && <p className="error-message">{error}</p>}
            </form>
        </div>
    );
};

export default CreateLesson;