import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';
import CreateQuizForm from './CreateQuizForm';
import QuizList from './QuizList';
import QuestionList from './QuestionList';
import AddQuestionForm from './AddQuestionForm';
import '../css/ViewQuizzes.css';

const ViewQuizzes = () => {
    const navigate = useNavigate();
    const { courseId } = useParams();
    const courseTitle = localStorage.getItem('selectedCourseTitle');
    const email = localStorage.getItem('email');
    const token = localStorage.getItem(`${email}_token`);
    const instructorId = localStorage.getItem(`${email}_uuid`);
    const [quizzes, setQuizzes] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [message, setMessage] = useState('');
    const [selectedQuizId, setSelectedQuizId] = useState(null);
    const [questions, setQuestions] = useState([]);
    const [viewingQuestions, setViewingQuestions] = useState(false);
    const [previousPage, setPreviousPage] = useState(null);
    const [currentQuizTotalMarks, setCurrentQuizTotalMarks] = useState(0);
    const [showCreateQuizForm, setShowCreateQuizForm] = useState(false);

    const fetchQuizzes = async () => {
      
        setLoading(true);
        // setError('');

        try {
            const response = await axios.get(
                `http://localhost:8099/api/quiz/viewAll?courseId=${courseId}`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            if (response.data && response.data.success && response.data.data) {
                setQuizzes(response.data.data);
            } else {
                setError(response.data?.message || 'Failed to fetch quizzes (invalid data format).');
            }
        } catch (error) {
            setError('Failed to fetch quizzes. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchQuizzes();
        setPreviousPage(document.referrer);
    }, [courseId, token]);

    const handleQuizSelect = async (quizId, totalMarks) => {
        setSelectedQuizId(quizId);
        setMessage('');
        setViewingQuestions(true);
        setCurrentQuizTotalMarks(totalMarks);
        await fetchQuestions(quizId);
    };

    const fetchQuestions = async (quizId) => {
        try {
            const response = await axios.get(`http://localhost:8099/api/question/view?quizId=${quizId}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (response.data && response.data.success && response.data.data) {
                setQuestions(response.data.data);
            } else {
                setQuestions([]);
                setMessage(response.data?.message || 'Failed to fetch questions (invalid data format).');
            }
        } catch (error) {
            setQuestions([]);
            setMessage('Failed to fetch questions. Please try again.');
        }
    };

    const handleQuestionAdded = async () => {
        await fetchQuestions(selectedQuizId);
        setMessage('');
    };

    const handleBack = () => {
        if (viewingQuestions) {
            setViewingQuestions(false);
            setSelectedQuizId(null);
            setQuestions([]);
        } else if (previousPage) {
            navigate(`/instructor/course/${courseId}`);
        } else {
            navigate(-1);
        }
    };

    const handleCreateQuizButtonClick = () => {
        setShowCreateQuizForm(true);
    };

    const handleQuizCreated = () => {
        fetchQuizzes();
        setShowCreateQuizForm(false);
    };

    const handleCloseCreateQuizForm = () => {
        setShowCreateQuizForm(false);
    };

    if (loading) {
        return <div>Loading quizzes...</div>;
    }

    if (error) {
        return <div style={{ color: 'red' }}>Error: {error}</div>;
    }

    return (
        <div className="view-quizzes-container">
            <h2>Quizzes :{courseTitle}</h2>
            {message && <p className="message">{message}</p>}
    
            {!showCreateQuizForm && (
                <button className="create-quiz-button" onClick={handleCreateQuizButtonClick}>
                    Create New Quiz
                </button>
            )}
    
            {showCreateQuizForm && (
                <div style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', backgroundColor: 'white', padding: '20px', border: '1px solid #ccc', zIndex: 1000 }}>
                    <CreateQuizForm onQuizCreated={handleQuizCreated} onClose={handleCloseCreateQuizForm} courseId={courseId} token={token} />
                </div>
            )}
    
            {!viewingQuestions ? (
                <div className="quiz-list">
                    <QuizList quizzes={quizzes} onQuizSelect={handleQuizSelect} />
                </div>
            ) : (
                <div>
                    <h3>Questions for Quiz</h3>
                    {questions.length > 0 ? ( <QuestionList questions={questions} /> ) : ( <p>No questions added yet. Add one below.</p> )}
                    {selectedQuizId && (
                       
                       <AddQuestionForm
                            quizId={selectedQuizId}
                            instructorId={instructorId}
                            token={token}
                            onQuestionAdded={handleQuestionAdded}
                        />
                    )}
                </div>
            )}
            <button className="back-button" onClick={handleBack}>
                Back
            </button>
        </div>
    );
};

export default ViewQuizzes;
