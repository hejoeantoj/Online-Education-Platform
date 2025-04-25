// InstructorCourseSidebar.js
import React, { useState } from 'react';
import styles from './InstructorCourseDetails.module.css';
import { Link } from 'react-router-dom';
 
const InstructorCourseSidebar = ({
    lessons,
    showLessons,
    selectedContent,
    toggleSection,
    handleLessonClick,
    handleCreateLessonClick,
    handleDeleteCourse,
    handleHomeClick
}) => {
    return (
        <aside className={`col-md-3 ${styles.sidebar}`}>
            <h4>Course Contents</h4>
            <nav>
                <ul className={styles.navList}>
                    {/* Lessons */}
                    <li className={styles.navItem}>
                        <div
                            className={showLessons || selectedContent?.lessonId || selectedContent === 'createLesson' || selectedContent === 'lessonsOverview' ? styles.activeButton : styles.inactiveButton}
                            onClick={() => toggleSection('lessons')}
                        >
                            Lessons
                        </div>
                        {showLessons && ( /* Dropdown logic */
                            <ul className={styles.dropdownList}>
                                {lessons.length > 0 ? (
                                    lessons.map((lesson) => (
                                        <li key={lesson.lessonId}
                                            className={`${styles.dropdownItem} ${selectedContent?.lessonId === lesson.lessonId ? styles.activeDropdownItem : ''}`}
                                            onClick={() => handleLessonClick(lesson)}>
                                            <div className={styles.dropdownLink}>
                                                {lesson.title || `Lesson ${lessons.indexOf(lesson) + 1}: ${lesson.lessonTitle}`}
                                            </div>
                                        </li>
                                    ))
                                ) : ( <li className={styles.dropdownItem}>No lessons present.</li> )}
                                <li className={styles.dropdownItem}>
                                    <button onClick={handleCreateLessonClick} className={`${styles.addActionButton} ${selectedContent === 'createLesson' ? styles.activeButton : ''}`}>Add Lesson</button>
                                </li>
                            </ul>
                        )}
                    </li>
 
                    {/* Assignments */}
                    <li className={styles.navItem}>
                        <div
                            className={selectedContent === 'assignments' ? styles.activeButton : styles.inactiveButton}
                            onClick={() => toggleSection('assignments')}
                        >
                            Assignments
                        </div>
                    </li>
 
                    {/* Quiz */}
                    <li className={styles.navItem}>
                        <div
                            className={selectedContent === 'quizzes' ? styles.activeButton : styles.inactiveButton}
                            onClick={() => toggleSection('quizzes')}
                        >
                            Quiz
                        </div>
                    </li>
 
                    {/* Q&A */}
                    <li className={styles.navItem}>
                        <div
                            className={selectedContent === 'qna' ? styles.activeButton : styles.inactiveButton}
                            onClick={() => toggleSection('qna')}
                        >
                            Q&A
                        </div>
                    </li>
 
                    {/* Reports */}
                    <li className={styles.navItem}>
                        <div onClick={() => toggleSection('reports')} className={selectedContent === 'reports' ? styles.activeButton : styles.inactiveButton}>Reports</div>
                    </li>
                </ul>
                <button onClick={handleHomeClick} className="back-to-dashboard-button">Back to Dashboard</button>
                <button onClick={handleDeleteCourse} className={`btn btn-danger ${styles.deleteButton}`}>Delete Course</button>
            </nav>
        </aside>
    );
};
 
export default InstructorCourseSidebar;
 