import React, { useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import '../css/QuizForm.css'; // Import the CSS file
 
const CreateQuizForm = ({ onQuizCreated, onClose }) => {
    const [quizTitle, setQuizTitle] = useState('');
    const [totalMarks, setTotalMarks] = useState('');
    const [message, setMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const { courseId } = useParams();
    const email = localStorage.getItem('email');
    const instructorId = localStorage.getItem(`${email}_uuid`);
    const token = localStorage.getItem(`${email}_token`);
    const courseTitle = localStorage.getItem('selectedCourseTitle');
 
    const handleCreateQuiz = async () => {
        if (!token) {
            setMessage('No token found. Please log in again.');
            return;
        }
 
        if (!quizTitle || !totalMarks) {
            setMessage('Please fill in all fields.');
            return;
        }
        if(quizTitle.length>20)
        {
            setErrorMessage('Quiz Title Should not be More than 20 chars');
            return;
        }


        const hasAlphabet = /[a-zA-Z]/.test(quizTitle);
        if (!hasAlphabet) {
            setErrorMessage("Quiz Title should contain at least one alphabet.");
            return;
        }
 
        if(totalMarks<=0 || totalMarks>100||totalMarks==NaN)
        {
            setErrorMessage("Total  Marks Should be Positive and between 1 to 100");
            return;
        }
 
        try {
            const response = await axios.post(
                'http://localhost:8099/api/quiz/create',
                {
                    courseId,
                    instructorId,
                    title: quizTitle,
                    totalMarks,
                },
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                }
            );
 
            if (response.data.success) {
                setMessage('Quiz created successfully');
                if (onQuizCreated) {
                    onQuizCreated();
                }
                if (onClose) {
                    onClose();
                }
            } else {
                setMessage('Failed to create quiz: ' + (response.data.message || ''));
            }
        } catch (error) {
            if (error.response && error.response.status === 401) {
                setMessage('Unauthorized access. Please log in again.');
            } else {
                setMessage(`Failed to create quiz: ${error.response?.data?.message || error.message}`);
            }
        }
    };
 
    return (
        <div className="create-quiz-form">
            <h3>Create New Quiz for {courseTitle}</h3>
            {message && <p>{message}</p>}
            {errorMessage && <p className="error-message">{errorMessage}</p>}
            <div>
                <input
                    type="text"
                    placeholder="Quiz Title"
                    value={quizTitle}
                    onChange={(e) => setQuizTitle(e.target.value)}
                />
            </div>
            <div>
                <input
                    type="number"
                    placeholder="Total Marks"
                    value={totalMarks}
                    onChange={(e) => setTotalMarks(e.target.value)}
                />
            </div>
            <div>
                <button onClick={handleCreateQuiz}>Create Quiz</button>
                {onClose && <button className="cancel" onClick={onClose}>Cancel</button>}
            </div>
        </div>
    );
};
 
export default CreateQuizForm;