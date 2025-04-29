import React from 'react';
import '../css/QuizQuestions.css';

const QuizQuestion = ({ question, index, selectedAnswers, onOptionChange }) => {

    return (
        <div className="question-card">
            <h3 className="question-title">{index + 1}. {question.questionText}</h3>
            <ul className="options">
                <li>
                    <label>
                        <input
                            type="radio"
                            name={question.questionId}
                            value="A"
                            checked={selectedAnswers[question.questionId] === 'A'}
                            onChange={() => onOptionChange(question.questionId, 'A')}
                        />
                        A. {question.optionA}
                    </label>
                </li>
                <li>
                    <label>
                        <input
                            type="radio"
                            name={question.questionId}
                            value="B"
                            checked={selectedAnswers[question.questionId] === 'B'}
                            onChange={() => onOptionChange(question.questionId, 'B')}
                        />
                        B. {question.optionB}
                    </label>
                </li>
                <li>
                    <label>
                        <input
                            type="radio"
                            name={question.questionId}
                            value="C"
                            checked={selectedAnswers[question.questionId] === 'C'}
                            onChange={() => onOptionChange(question.questionId, 'C')}
                        />
                        C. {question.optionC}
                    </label>
                </li>
               
            </ul>
        </div>
    );
};

export default QuizQuestion;
