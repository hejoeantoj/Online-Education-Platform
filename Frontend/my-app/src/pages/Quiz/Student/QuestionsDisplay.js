import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import Timer from './Timer';
import QuizInfoPopup from './QuizInfoPopup';
import QuizQuestion from './QuizQuestion';
import QuizResults from './QuizResults';
import '../css/QuestionsDisplay.css'; // Importing the CSS file



const QuestionsDisplay = () => {
  const { quizId } = useParams();
  const [questions, setQuestions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedAnswers, setSelectedAnswers] = useState({});
  const [score, setScore] = useState(null);
  const [percentage, setPercentage] = useState(null);
  const [quizDetails, setQuizDetails] = useState(null);
  const [isTimerRunning, setIsTimerRunning] = useState(false);
  const [showQuizInfoPopup, setShowQuizInfoPopup] = useState(true);
  const [totalQuestionsCount, setTotalQuestionsCount] = useState(null);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);

  useEffect(() => {
    const fetchInitialData = async () => {
      setLoading(true);
      setError(null);
      const email = localStorage.getItem('email');
      const token = localStorage.getItem(`${email}_token`);

      try {
        const quizDetailsResponse = await axios.get(`http://localhost:8099/api/quiz/view?quizId=${quizId}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setQuizDetails(quizDetailsResponse.data.data);

        const questionsResponse = await axios.get(`http://localhost:8099/api/question/view?quizId=${quizId}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setQuestions(questionsResponse.data.data);
        setTotalQuestionsCount(questionsResponse.data.data.length);
        setLoading(false);
      } catch (err) {
        setError(err.message || 'Failed to fetch quiz details and questions.');
        setLoading(false);
      }
    };

    fetchInitialData();
  }, [quizId]);

  const calculateQuizDuration = () => (totalQuestionsCount !== null ? totalQuestionsCount * 60 : null);

  const handleOptionChange = (questionId, option) => {
    setSelectedAnswers((prevAnswers) => ({ ...prevAnswers, [questionId]: option }));
  };

  const handleSubmit = async () => {
    setIsTimerRunning(false);
    try {
      const email = localStorage.getItem('email');
      const token = localStorage.getItem(`${email}_token`);
      const userId = localStorage.getItem(`${email}_uuid`);

      const payload = {
        quizId,
        userId,
        response: Object.keys(selectedAnswers).map((questionId) => ({
          questionId,
          selectedOption: selectedAnswers[questionId],
        })),
      };

      const response = await axios.put(`http://localhost:8099/api/squiz/submit`, payload, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setScore(response.data.data.obtainedMarks);
      setPercentage(response.data.data.percentage);
    } catch (error) {
      setError(error.message || 'Failed to submit quiz.');
    }
  };

  const handleTimeUp = () => {
    setIsTimerRunning(false);
    handleSubmit();
  };

  const handleStartQuiz = () => {
    setShowQuizInfoPopup(false);
    setIsTimerRunning(true);
  };

  const goToNextQuestion = () => {
    setCurrentQuestionIndex((prevIndex) => Math.min(prevIndex + 1, questions.length - 1));
  };

  const goToPreviousQuestion = () => {
    setCurrentQuestionIndex((prevIndex) => Math.max(prevIndex - 1, 0));
  };

  if (loading) {
    return <div className="loading-container">Loading quiz details...</div>;
  }

  if (error) {
    return <div className="error-container">Error: {error}</div>;
  }

  if (showQuizInfoPopup && quizDetails && totalQuestionsCount !== null) {
    return (
      <QuizInfoPopup
        quizDetails={quizDetails}
        totalQuestionsCount={totalQuestionsCount}
        calculateQuizDuration={calculateQuizDuration}
        onStartQuiz={handleStartQuiz}
      />
    );
  }

  const quizDuration = calculateQuizDuration();
  const currentQuestion = questions[currentQuestionIndex];

  return (
    <div className="questions-display-container">
      <div className="quiz-header">
        <h1>{quizDetails?.title || 'Quiz'}</h1>
        {isTimerRunning && (
          <div className="timer-container">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="#007bff" viewBox="0 0 16 16">
              <path d="M8 3a6 6 0 1 0 0 12A6 6 0 0 0 8 3zM8 1a7 7 0 1 1 0 14A7 7 0 0 1 8 1z" />
              <path d="M10.93 6.588L8.5 8.293V4.5a.5.5 0 0 0-1 0v4l2.93-2.088a.5.5 0 1 0-.5-.824z" />
            </svg>
            <Timer duration={quizDuration} onTimeUp={handleTimeUp} isRunning={isTimerRunning} />
          </div>
        )}
      </div>

      {questions.length > 0 && !score ? (
        <div className="quiz-content">
          <div className="question-navigation">
            <p>Question {currentQuestionIndex + 1} of {questions.length}</p>
            <div className="navigation-buttons">
              <button
                onClick={goToPreviousQuestion}
                disabled={currentQuestionIndex === 0}
                className="nav-button"
              >
                Previous
              </button>
              <button
                onClick={goToNextQuestion}
                disabled={currentQuestionIndex === questions.length - 1}
                className="nav-button"
              >
                Next
              </button>
            </div>
          </div>

          {currentQuestion && (
            <QuizQuestion
              key={currentQuestion.questionId}
              question={currentQuestion}
              index={currentQuestionIndex}
              selectedAnswers={selectedAnswers}
              onOptionChange={handleOptionChange}
            />
          )}

          <div className="quiz-actions">
            <button
              className="submit-button"
              onClick={handleSubmit}
              disabled={Object.keys(selectedAnswers).length !== questions.length}
            >
              Submit Quiz
            </button>
          </div>
        </div>
      ) : questions.length === 0 && !loading && !error ? (
        <div className="no-questions">No questions available for this quiz.</div>
      ) : null}

      {score !== null && <QuizResults score={score} percentage={percentage} />}
    </div>
  );
};

export default QuestionsDisplay;