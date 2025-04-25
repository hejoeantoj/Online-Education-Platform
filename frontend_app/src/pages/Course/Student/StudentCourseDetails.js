import React, { useEffect, useState, useCallback } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import styles from './StudentCourseDetails.module.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import CourseSidebar from './CourseSidebar';
import CourseContent from './CourseContent';
import CourseHeader from '../CourseHeader';

const StudentCourseDetails = () => {
  const { courseId } = useParams();
  const [course, setCourse] = useState(null);
  const [lessons, setLessons] = useState([]);
  const [assignments, setAssignments] = useState([]);
  const [quizzes, setQuizzes] = useState([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const email = localStorage.getItem('email');
  const token = localStorage.getItem(`${email}_token`);
  const studentId = localStorage.getItem(`${email}_uuid`);

  const [enrolled, setEnrolled] = useState(false);
  const [selectedContent, setSelectedContent] = useState(null);

  const [currentAssignment, setCurrentAssignment] = useState(null);
  const [currentLesson, setCurrentLesson] = useState(null);
  const [currentQuiz, setCurrentQuiz] = useState(null);

  const [isAssignmentsOpen, setIsAssignmentsOpen] = useState(false);
  const [isLessonsOpen, setIsLessonsOpen] = useState(false);
  const [isQuizzesOpen, setIsQuizzesOpen] = useState(false);
  

  const navigate = useNavigate();

  useEffect(() => {
    const checkEnrollment = async () => {
      try {
        const response = await axios.get(`http://localhost:8082/api/enrollment/student?studentId=${studentId}`, {
          headers: { Authorization: `Bearer ${token}` }
        });
        const enrolledCourses = response.data.data || [];
        const isEnrolled = enrolledCourses.some(c => c.courseId === courseId);
        setEnrolled(isEnrolled);
      } catch (error) {
        console.error("Error checking enrollment status:", error);
        setError("Failed to check enrollment status.");
      }
    };
    checkEnrollment();
  }, [courseId, studentId, token]);


  const fetchCourseDetails = useCallback(async () => {
    try {
      const response = await axios.get(`http://localhost:8082/api/course/courseDetails?courseId=${courseId}`);
      setCourse(response.data.data || null);
    } catch (error) {
      console.error("Error fetching course details:", error);
      setError("Failed to load course details.");
    }
  }, [courseId]);

  const fetchCourseContent = useCallback(async () => {
    if (enrolled && courseId && studentId && token) {
      setError(null);
      setLoading(true);
      try {
        const [lessonsResponse, assignmentsResponse, quizzesResponse] = await Promise.all([
          axios.get(`http://localhost:8082/api/lesson/getAll?courseId=${courseId}&userId=${studentId}`, {
            headers: { Authorization: `Bearer ${token}` },
          }),
          axios.get(`http://localhost:8083/api/assignment/view?courseId=${courseId}`, {
            headers: { Authorization: `Bearer ${token}` },
          }),
          axios.get(`http://localhost:8099/api/quiz/viewAll?courseId=${courseId}`, {
            headers: { Authorization: `Bearer ${token}` },
          }),
        ]);

        setLessons(lessonsResponse.data || []);
        setAssignments(assignmentsResponse.data.data || []);
        setQuizzes(quizzesResponse.data.data || []);
      } catch (err) {
        console.error('Error during course content fetch:', err);
        setError('Failed to fetch course content.');
      } finally {
        setLoading(false);
      }
    } else if (courseId) {
      setLessons([]);
      setAssignments([]);
      setQuizzes([]);
      setLoading(false);
    } else {
      setLoading(false);
    }
  }, [enrolled, courseId, studentId, token]);

  useEffect(() => {
    fetchCourseDetails();
  }, [fetchCourseDetails]);

  useEffect(() => {
    if (enrolled || courseId) {
      fetchCourseContent();
    }
  }, [enrolled, courseId, studentId, token, fetchCourseContent]);


  const fetchEnrollmentDate = useCallback(async (courseId, studentId) => {
    try {
      const response = await axios.get(`http://localhost:8082/api/enrollment/enrollment-date?courseId=${courseId}&studentId=${studentId}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching enrollment date:', error);
      return null;
    }
  }, [token]);

  const handleAssignmentClick = (assignmentId) => {
    if (!enrolled) {
      setSelectedContent('not-enrolled');
      return;
    }
    setCurrentAssignment(assignments.find(a => a.assignmentId === assignmentId));
    setSelectedContent('view-assignment');
    setIsAssignmentsOpen(true);
    setIsLessonsOpen(false);
    setIsQuizzesOpen(false);
  };

  const toggleAssignmentsDropdown = () => {
    if (!enrolled) {
      setSelectedContent('not-enrolled');
      return;
    }
    setIsAssignmentsOpen(!isAssignmentsOpen);
    setSelectedContent(isAssignmentsOpen ? null : 'assignments');
    setIsLessonsOpen(false);
    setIsQuizzesOpen(false);
    setCurrentLesson(null);
    setCurrentQuiz(null);
  };

  const handleAssignmentSubmitted = (submittedAssignmentId) => {
    setAssignments(prev =>
      prev.map(a =>
        a.assignmentId === submittedAssignmentId ? { ...a, submitted: true } : a
      )
    );
    setCurrentAssignment(null);
    setSelectedContent('assignments');
    setIsAssignmentsOpen(true);
  };

  const closeAssignmentPopup = () => {
    setCurrentAssignment(null);
    setSelectedContent('assignments');
    setIsAssignmentsOpen(true);
  };

  const handleLessonsClick = () => {
    if (!enrolled) {
      setSelectedContent('not-enrolled');
      return;
    }
    setIsLessonsOpen(!isLessonsOpen);
    setSelectedContent(isLessonsOpen ? null : 'lessons');
    setIsAssignmentsOpen(false);
    setIsQuizzesOpen(false);
    setCurrentLesson(null);
    setCurrentQuiz(null);
  };

  const handleLessonItemClick = (lesson) => {
    if (!enrolled) {
      setSelectedContent('not-enrolled');
      return;
    }
    setCurrentLesson(lesson);
    setSelectedContent('view-lesson');
    setIsLessonsOpen(true);
    setIsAssignmentsOpen(false);
    setIsQuizzesOpen(false);
  };

  const handleQuizzesClick = () => {
    if (!enrolled) {
      setSelectedContent('not-enrolled');
      return;
    }
    setIsQuizzesOpen(!isQuizzesOpen);
    setSelectedContent(isQuizzesOpen ? null : 'quizzes');
    setIsAssignmentsOpen(false);
    setIsLessonsOpen(false);
    setCurrentQuiz(null);
    setCurrentLesson(null);
  };

  const handleQuizItemClick = (quiz) => {
    if (!enrolled) {
      setSelectedContent('not-enrolled');
      return;
    }
    setCurrentQuiz(quiz);
    setSelectedContent('view-quiz');
    setIsQuizzesOpen(true);
    setIsAssignmentsOpen(false);
    setIsLessonsOpen(false);
  };

  const handleQAClick = () => {
    if (!enrolled) {
      setSelectedContent('not-enrolled');
      return;
    }
    setSelectedContent('chat');
    setIsAssignmentsOpen(false);
    setIsLessonsOpen(false);
    setIsQuizzesOpen(false);
    setCurrentLesson(null);
    setCurrentQuiz(null);
  };

  const handleReportsClick = () => {
    if (!enrolled) {
      setSelectedContent('not-enrolled');
      return;
    }
    setSelectedContent('report');
    setIsAssignmentsOpen(false);
    setIsLessonsOpen(false);
    setIsQuizzesOpen(false);
    setCurrentLesson(null);
    setCurrentQuiz(null);
  };

  const handleHomeClick = () => {
    navigate('/student');
  };

  const handleEnrollClick = (async () => {
    if (!studentId || !token) {
      alert("Could not enroll. User information missing.");
      return;
    }

    try {
      await axios.post(
        "http://localhost:8082/api/enrollment/create",
        { courseId, studentId },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      alert("Enrolled successfully!");
      setEnrolled(true);
      
      fetchCourseContent();
      
    } catch (error) {
      console.error("Error enrolling in the course:", error);
      alert(`Enrollment failed: ${error.response?.data?.message || error.message}`);
    }
  }, [courseId, studentId, token, fetchCourseContent]);


  useEffect(() => {
    setLoading(true);
    Promise.all([
      fetchCourseDetails(),
      (enrolled || courseId) ? fetchCourseContent() : Promise.resolve(), // Conditionally fetch content
    ])
      .finally(() => setLoading(false));
  }, [fetchCourseDetails, fetchCourseContent, enrolled, courseId]);

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center vh-100">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
      </div>
    );
  }

  if (error) {
    return <div className="alert alert-danger" role="alert">{error}</div>;
  }


  return (
    <div>
      <CourseHeader
        title={course?.courseTitle || 'Course Details'}
        onBack={() => navigate(-1)}
        onHome={handleHomeClick}
      />
      <div className={`container-fluid ${styles.courseDetailsContainer}`}>
        <CourseSidebar
          course={course}
          enrolled={enrolled}
          lessons={lessons}
          assignments={assignments}
          quizzes={quizzes}
          isLessonsOpen={isLessonsOpen}
          isAssignmentsOpen={isAssignmentsOpen}
          isQuizzesOpen={isQuizzesOpen}
          currentLesson={currentLesson}
          currentAssignment={currentAssignment}
          currentQuiz={currentQuiz}
          handleLessonsClick={handleLessonsClick}
          handleLessonItemClick={handleLessonItemClick}
          toggleAssignmentsDropdown={toggleAssignmentsDropdown}
          handleAssignmentClick={handleAssignmentClick}
          handleQuizzesClick={handleQuizzesClick}
          handleQuizItemClick={handleQuizItemClick}
          handleQAClick={handleQAClick}
          handleReportsClick={handleReportsClick}
          handleHomeClick={handleHomeClick}
          handleEnrollClick={handleEnrollClick}
        />
        <CourseContent
          course={course}
          enrolled={enrolled}
          selectedContent={selectedContent}
          currentLesson={currentLesson}
          currentAssignment={currentAssignment}
          currentQuiz={currentQuiz}
          courseId={courseId}
          studentId={studentId}
          fetchEnrollmentDate={fetchEnrollmentDate}
          closeAssignmentPopup={closeAssignmentPopup}
          handleAssignmentSubmitted={handleAssignmentSubmitted}
          isAssignmentsOpen={isAssignmentsOpen}
        />
      </div>
    </div>
  );
};

export default StudentCourseDetails;