// QuestionList.js
import React from 'react';
import '../css/QuestionList.css';
const QuestionList = ({ questions }) => {

    return (
        <div className="question-cards">
            <p>Total Questions: {questions.length}</p>
            {questions.map((question, index) => (
                <div key={question.questionId} className="question-card">
                    <strong>{index + 1}. {question.questionText}</strong>
                    <ul>
                        <li>A: {question.optionA}</li>
                        <li>B: {question.optionB}</li>
                        <li>C: {question.optionC}</li>
                        <li className="correct-answer">Correct Answer: {question.correctAnswer}</li>
                    </ul>
                </div>
            ))}
        </div>
    );
};

export default QuestionList;