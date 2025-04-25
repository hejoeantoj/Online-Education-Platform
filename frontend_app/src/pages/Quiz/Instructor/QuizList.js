// QuizList.js
import React from 'react';
import '../css/QuizList.css';

const QuizList = ({ quizzes, onQuizSelect }) => {
    return (
        <div className="quiz-container">
            <ul className="quiz-list">
                {quizzes && quizzes.length > 0 ? (
                    quizzes.map((quiz) => (
                        <li className="quiz-card" key={quiz.quizId}>
                            <div className="quiz-header">
                                <strong>{quiz.title}</strong>
                            </div>
                            <div className="quiz-details">
                                <p>Total Marks: {quiz.totalMarks}</p>
                                <button onClick={() => onQuizSelect(quiz.quizId, quiz.totalMarks)}>
                                    Questions
                                </button>
                            </div>
                        </li>
                    ))
                ) : (
                    <p className="no-quizzes">No quizzes available for this course yet.</p>
                )}
            </ul>
        </div>
    );
};

export default QuizList;
