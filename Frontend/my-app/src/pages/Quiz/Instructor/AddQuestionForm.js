import React, { useState } from 'react';
import axios from 'axios';
import '../css/AddQuestionForm.css'; // Import the CSS file
 
const AddQuestionForm = ({ quizId, instructorId, token, onQuestionAdded }) => {
    const [questionText, setQuestionText] = useState('');
    const [optionA, setOptionA] = useState('');
    const [optionB, setOptionB] = useState('');
    const [optionC, setOptionC] = useState('');
    const [correctAnswer, setCorrectAnswer] = useState('');
    const [message, setMessage] = useState('');
     const [errorMessage, setErrorMessage] = useState('');
 
    const handleAddQuestion = async () => {
        if (!['A', 'B', 'C'].includes(correctAnswer.toUpperCase())) {
            setMessage('Correct answer must be A, B, or C (case-sensitive).');
            return;
        }
        if (questionText.trim() === ''||!optionA.trim() || !optionB.trim() || !optionC.trim() || !correctAnswer.trim()) { // Check if questionText is empty or just whitespace
            setErrorMessage('Please Give input to all the fields');
            setMessage(''); 
            return;
        }
        else if (questionText.length > 500) { // Check if question length exceeds 500
            setErrorMessage('Question cannot be more than 500 characters.');
            setMessage('');
            return;
        }
        else {
            setErrorMessage(''); 
        }
 
        const questionPayload = {
            quizId,
            instructorId,
            questionText,
            optionA,
            optionB,
            optionC,
            correctAnswer: correctAnswer.toUpperCase(),
        };
 
        try {
            const response = await axios.post('http://localhost:8099/api/question/create', questionPayload, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });
 
            if (response.data.success) {
                setMessage('Question added successfully');
                setQuestionText('');
                setOptionA('');
                setOptionB('');
                setOptionC('');
                setCorrectAnswer('');
                onQuestionAdded();
            } else {
                setMessage(response.data.message || 'Failed to add question');
            }
        } catch (error) {
            if (error.response && error.response.status === 409) {
                setMessage('Question already exists');
            } else {
                setMessage(`Failed to add question: ${error.response?.data?.message || error.message}`);
            }
        }
    };
 
    return (
        <div className="add-question-form">
            <h3>Add More Questions</h3>
            {message && <p className={message.includes('success') ? 'success-message' : 'error-message'}>{message}</p>}
            {errorMessage && <p className="error-message">{errorMessage}</p>}
           
            <textarea
                placeholder="Question Text"
                value={questionText}
                onChange={(e) => setQuestionText(e.target.value)}
                rows={4}
                cols={50}
            />
            <input
                type="text"
                placeholder="Option A"
                value={optionA}
                onChange={(e) => setOptionA(e.target.value)}
            />
            <input
                type="text"
                placeholder="Option B"
                value={optionB}
                onChange={(e) => setOptionB(e.target.value)}
            />
            <input
                type="text"
                placeholder="Option C"
                value={optionC}
                onChange={(e) => setOptionC(e.target.value)}
            />
            <input
                type="text"
                placeholder="Correct Answer (A, B, or C)"
                value={correctAnswer}
                onChange={(e) => setCorrectAnswer(e.target.value)}
            />
            <button onClick={handleAddQuestion}>Submit Question</button>
        </div>
    );
};
 
export default AddQuestionForm;