import React from 'react';
import { Link } from 'react-router-dom';
import styles from './StudentCourseDetails.module.css'; // Assuming styles are in this file

const CourseSidebar = ({
  course,
  enrolled,
  lessons,
  assignments,
  quizzes,
  isLessonsOpen,
  isAssignmentsOpen,
  isQuizzesOpen,
  currentLesson,
  currentAssignment,
  currentQuiz,
  handleLessonsClick,
  handleLessonItemClick,
  toggleAssignmentsDropdown,
  handleAssignmentClick,
  handleQuizzesClick,
  handleQuizItemClick,
  handleQAClick,
  handleReportsClick,
  handleHomeClick,
  handleEnrollClick,
}) => {
  return (
    <aside className={`${styles.sidebar} course-sidebar`}>
      <h3 className="course-title">{course?.courseTitle}</h3>
      <nav className="course-nav">
        <ul className={styles.navList}>
          <li className={styles.navItem}>
            <div
              className={`${styles.navButton} ${isLessonsOpen || currentLesson ? styles.activeButton : styles.inactiveButton} lessons-button`}
              onClick={handleLessonsClick}
            >
              Lessons
            </div>
            {isLessonsOpen && enrolled && (
              <ul className={styles.dropdownList}>
                {lessons.length > 0 ? (
                  lessons.map((lesson, index) => (
                    <li
                      key={lesson.lessonId}
                      className={`${styles.dropdownItem} lesson-item ${currentLesson?.lessonId === lesson.lessonId ? styles.active : ''}`}
                      onClick={() => handleLessonItemClick(lesson)}
                    >
                      <div className={styles.dropdownLink}>
                        Lesson {index + 1}: {lesson.lessonTitle || `Lesson ${index + 1}`}
                      </div>
                    </li>
                  ))
                ) : (
                  <li className={styles.dropdownItem}>No lessons available.</li>
                )}
              </ul>
            )}
          </li>

          <li className={styles.navItem}>
            <div
              className={`${styles.navButton} ${isAssignmentsOpen || currentAssignment ? styles.activeButton : styles.inactiveButton} assignments-button`}
              onClick={toggleAssignmentsDropdown}
            >
              Assignments
            </div>
            {isAssignmentsOpen && enrolled && (
              <ul className={styles.dropdownList}>
                {assignments.length > 0 ? (
                  assignments.map((assignment, index) => (
                    <li
                      key={assignment.assignmentId}
                      className={`${styles.dropdownItem} assignment-item ${currentAssignment?.assignmentId === assignment.assignmentId ? styles.active : ''}`}
                      onClick={() => handleAssignmentClick(assignment.assignmentId)}
                    >
                      <div className={styles.dropdownLink}>
                        Assignment {index + 1}: {assignment.title || assignment.question}
                        {assignment.submitted && <span className={styles.submittedAssignment}>Submitted</span>}
                      </div>
                    </li>
                  ))
                ) : (
                  <li className={styles.dropdownItem}>No assignments available.</li>
                )}
              </ul>
            )}
          </li>

          <li className={styles.navItem}>
            <div
              className={`${styles.navButton} ${isQuizzesOpen || currentQuiz ? styles.activeButton : styles.inactiveButton} quizzes-button`}
              onClick={handleQuizzesClick}
            >
              Quizzes
            </div>
            {isQuizzesOpen && enrolled && (
              <ul className={styles.dropdownList}>
                {quizzes.length > 0 ? (
                  quizzes.map((quiz, index) => (
                    <li
                      key={quiz.quizId}
                      className={`${styles.dropdownItem} quiz-item ${currentQuiz?.quizId === quiz.quizId ? styles.active : ''}`}
                      onClick={() => handleQuizItemClick(quiz)}
                    >
                      <div className={styles.dropdownLink}>
                        Quiz {index + 1}: {quiz.title || `Quiz ${index + 1}`}
                      </div>
                    </li>
                  ))
                ) : (
                  <li className={styles.dropdownItem}>No quizzes available.</li>
                )}
              </ul>
            )}
          </li>

          <li className={styles.navItem}>
            <button onClick={handleQAClick} className={`${styles.navButton} ${styles.inactiveButton} qa-button`}>Q&A</button>
          </li>

          <li className={styles.navItem}>
            <button onClick={handleReportsClick} className={`${styles.navButton} ${styles.inactiveButton} reports-button`}>Report</button>
          </li>
        </ul>
      </nav>
      {!enrolled && course && (
        <div className={styles.enrollmentInfo}>
          <p>You are not enrolled in this course.</p>
          <button className="enroll-button" onClick={handleEnrollClick}>Enroll Now</button>
        </div>
      )}
      <button onClick={handleHomeClick} className="back-to-dashboard-button">Back to Dashboard</button>
    </aside>
  );
};

export default CourseSidebar;