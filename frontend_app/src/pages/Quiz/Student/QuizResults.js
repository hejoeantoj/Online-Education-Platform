// QuizResults.js
import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/QuizResults.css';

const QuizResults = ({ score, percentage }) => {
    const navigate = useNavigate();
    return (
        <div className="quiz-result-card">
            <div className="card">
                <div className="card-body">
                    <h2 className="card-title">Quiz Results</h2>
                    <p className="card-text">Your Score: {score}</p>
                    <p className="card-text">Your Percentage: {percentage}%</p>
                    <button onClick={() => navigate(-1)}>Previous Page</button>
                </div>
            </div>
        </div>
    );
};

export default QuizResults;