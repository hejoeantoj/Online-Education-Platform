import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import ProtectedRoute from './components/ProtectedRoute';
import StudnetCourseDetails from './pages/Course/Student/StudentCourseDetails';
import InstructorCourseDetails from './pages/Course/Instructor/InstructorCourseDetails';
import StudentDB from './pages/Dashboard/StudentDB';
import InstructorDB from './pages/Dashboard/InstructorDB';
import CreateCourse from './pages/Dashboard/CreateCourse';
import AssignmentForInstructor from './pages/Assignments/Instructor/AssignmentForInstructor';
import ViewQuizzes from './pages/Quiz/Instructor/ViewQuizzes';
import QuestionsDisplay from './pages/Quiz/Student/QuestionsDisplay';
import CreateLesson from './pages/Course/Instructor/CreateLesson';
import ChatBox from './pages/Chat/ChatBox';
import StudentReport from './pages/Report/StudentReport';
import InstructorReport from './pages/Report/InstructorReport';
import LandingPage from './pages/LandingPage';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LandingPage />} /> 
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route
          path="/student"
          element={<ProtectedRoute element={<StudentDB />} requiredRole="STUDENT" />}
        />
        <Route
          path="/instructor"
          element={<ProtectedRoute element={<InstructorDB />} requiredRole="INSTRUCTOR" />}
        />
        <Route path="/student/course/:courseId"
          element={<ProtectedRoute element={<StudnetCourseDetails />} requiredRole="STUDENT" />}
        />
        <Route path="/instructor/course/:courseId"
          element={<ProtectedRoute element={<InstructorCourseDetails />} requiredRole="INSTRUCTOR" />}
        />
        <Route path="/createcourse"
          element={<ProtectedRoute element={<CreateCourse />} requiredRole="INSTRUCTOR" />}
        />
        <Route path="/create-lesson/:courseId"
          element={<ProtectedRoute element={<CreateLesson />} requiredRole="INSTRUCTOR" />}
        />
        <Route path="/instructor/course/:courseId/quiz"
          element={<ProtectedRoute element={<ViewQuizzes />} requiredRole="INSTRUCTOR" />}
        />

        {/* <Route path="/instructor/course/:courseId/lesson/:lessonId"
          element={<ProtectedRoute element={<ViewLesson />} />}
        /> */}

        <Route path="/course/:courseId/quiz/:quizId"
          element={<ProtectedRoute element={<QuestionsDisplay />} requiredRole="STUDENT" />}
        />
        <Route path="instructor/course/:courseId/quiz/:quizId"
          element={<ProtectedRoute element={<ViewQuizzes />} requiredRole="STUDENT" />}
        />
        <Route path="/course/:courseId/report"
          element={<ProtectedRoute element={<StudentReport />} requiredRole="STUDENT" />}
        />
        <Route path="instructor/course/:courseId/report"
          element={<ProtectedRoute element={<InstructorReport />} requiredRole="INSTRUCTOR" />}
        />
        <Route path="/course/:courseId/qna"
          element={<ProtectedRoute element={<ChatBox />} requiredRoles={["INSTRUCTOR", "STUDENT"]} />}
        />

        {/* <Route path="/course/:courseId/lesson/:lessonId"
          element={<ProtectedRoute element={<ViewLesson />} requiredRole="STUDENT" />}
        /> */}
        
        <Route path="/instructor-homepage" element={<InstructorDB />} />
        <Route path="/create-course" element={<CreateCourse />} />
        <Route path="instructor/course/:courseId/assignment" element={<AssignmentForInstructor />} />
        <Route path="/course-details-instructor/:courseTitle" element={<AssignmentForInstructor />} />
        {/* <Route path="*" element={<Navigate to="/login" />} /> */}
      </Routes>
    </Router>
  );
}

export default App;
