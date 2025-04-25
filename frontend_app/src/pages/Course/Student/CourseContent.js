// In CourseContent.js
import React from 'react';
import styles from './StudentCourseDetails.module.css';
import AssignmentPopup from '../../Assignments/student/AssignmentPopup';
import ChatBox from '../../Chat/ChatBox';
import StudentReport from '../../Report/StudentReport';
import { useNavigate } from 'react-router-dom';

const CourseContent = ({
 course,
 enrolled,
 selectedContent,
 currentLesson,
 currentAssignment,
 currentQuiz,
 courseId,
 studentId,
 fetchEnrollmentDate,
 closeAssignmentPopup,
 handleAssignmentSubmitted,
 isChatOpen,
 isReportOpen,
 isAssignmentsOpen,
}) => {
 const navigate = useNavigate();

 const handleAttendQuiz = () => {
   if (currentQuiz && courseId) {
     navigate(`/course/${courseId}/quiz/${currentQuiz.quizId}`); // Adjust the route as needed
   }
 };

 return (
   <main className={`col-md-9 ${styles.mainContent}`}>
     {!enrolled && selectedContent !== 'not-enrolled' && course && <h2>Welcome to {course?.courseTitle}. Enroll to access the content.</h2>}
     {/* <h2>{course?.courseTitle}</h2> */}

     {selectedContent === 'view-lesson' && currentLesson && enrolled && (
       <div className={styles.lessonContent}>
         <h3>{currentLesson.lessonTitle || 'Lesson Content'}</h3>
         {/* You might use the LessonView component here if you have one */}
         <p>{currentLesson.content || 'Lesson content will be displayed here.'}</p>
         {/* Render any additional lesson-specific content */}
       </div>
     )}

     {selectedContent === 'view-assignment' && currentAssignment && enrolled && (
       <div className={styles.assignmentView}>
         <h3>{currentAssignment.title || 'Assignment'}</h3>
         <p>{currentAssignment.question || 'Assignment details will be displayed here.'}</p>
         <button className="btn btn-primary btn-sm" onClick={() => closeAssignmentPopup()}>View/Submit Assignment</button>
         {/* The AssignmentPopup is rendered conditionally below */}
       </div>
     )}

     {selectedContent === 'view-quiz' && currentQuiz && enrolled && (
       <div className={styles.quizView}>
         <h3>{currentQuiz.title || 'Quiz'}</h3>
         {/* <p>{currentQuiz.description || 'Quiz details will be displayed here.'}</p> */}
         <button className="btn btn-primary" onClick={handleAttendQuiz}>
           Attend / Start Quiz
         </button>
         {/* You might use the QuizView component here to display more quiz info */}
       </div>
     )}

     {selectedContent === 'chat' && enrolled && (
       <div className={styles.chatBoxContainerWrapper}>
         <ChatBox courseId={courseId} />
         {/* Optional close button */}
         {/* {onCloseChat && <button onClick={onCloseChat}>Close Chat</button>} */}
       </div>
     )}

     {selectedContent === 'report' && enrolled && (
       <div className={styles.reportContainerWrapper}>
         <StudentReport courseId={courseId} />
         {/* Optional close button */}
         {/* {onCloseReport && <button onClick={onCloseReport}>Close Report</button>} */}
       </div>
     )}

     {isAssignmentsOpen && currentAssignment && (
       <AssignmentPopup
       assignment={currentAssignment}
       onClose={closeAssignmentPopup}
       onSubmit={handleAssignmentSubmitted}
       fetchEnrollmentDate={fetchEnrollmentDate} // Pass the function as a prop
       courseId={courseId} // Make sure courseId and studentId are also passed if needed by AssignmentPopup
       studentId={studentId}
       />
     )}

     {!selectedContent && enrolled && course && (
       <div>
         <p><strong>Description:</strong> {course.description}</p>
         <p><strong>Category:</strong> {course.category}</p>
         <p><strong>Duration:</strong> {course.duration} weeks</p>
       </div>
     )}

     {!selectedContent && !enrolled && course && (
       <div>
         <p><strong>Description:</strong> {course.description}</p>
         <p><strong>Category:</strong> {course.category}</p>
         <p><strong>Duration:</strong> {course.duration} weeks</p>
       </div>
     )}
   </main>
 );
};

export default CourseContent;