// InstructorCourseContent.js
import React, { useState, useEffect } from 'react';
import styles from './InstructorCourseDetails.module.css';
import CreateLesson from "./CreateLesson";
import AssignmentForInstructor from "../../Assignments/Instructor/AssignmentForInstructor";
import ViewQuizzes from "../../Quiz/Instructor/ViewQuizzes";
import ChatBox from '../../Chat/ChatBox';
import InstructorReport from "../../Report/InstructorReport";
import UpdateLesson from "./UpdateLesson";
import LessonActions from "./LessonActions";
 
const InstructorCourseContent = ({
    course,
    lessons,
    selectedContent,
    courseId,
    handleLessonCreated,
    handleReturnToCourseView,
    onLessonUpdatedFromContent, //  prop to handle lesson updates in parent
    onLessonDeletedFromContent, //  prop to handle lesson deletions in parent
}) => {
    const [isEditingLesson, setIsEditingLesson] = useState(false);
    const [fadeClass, setFadeClass] = useState('');
 
    useEffect(() => {
        setFadeClass('fade-out');
        const timeoutId = setTimeout(() => {
            setFadeClass('fade-in');
        }, 300); // Adjust the timeout to match your CSS transition duration
        return () => clearTimeout(timeoutId);
    }, [selectedContent]);
 
    const handleUpdateClick = () => {
        setIsEditingLesson(true);
    };
 
    const handleLessonUpdated = (updatedLessonData) => {
        setIsEditingLesson(false);
        onLessonUpdatedFromContent(updatedLessonData); // Notify parent
    };
 
    const handleCancelUpdate = () => {
        setIsEditingLesson(false);
    };
 
    const handleLessonDeleted = (deletedLessonId) => {
        onLessonDeletedFromContent(deletedLessonId); // Notify parent
    };
 
    return (
        <main className={`col-md-9 ${styles.mainContent} ${fadeClass}`}>
            {/* Default View */}
            {!selectedContent && course && (
                <div style={{textAlign: 'center', padding: '20px', fontSize: '23px'}}>
                    <h2>{course.courseTitle}</h2>
                    <p><strong>Description:</strong> {course.description}</p>
                    <p><strong>Category:</strong> {course.category}</p>
                    <p><strong>Duration:</strong> {course.duration} weeks</p>
                    {/* <p>Lesson : {lessons.length}</p>
                    <p>Assignments:{AssignmentList.length}</p>
                    <p>Quiz:{}</p> */}
                </div>
            )}
 
            {/* Create Lesson */}
            {selectedContent === 'createLesson' && (
                <CreateLesson courseId={courseId} onLessonCreated={handleLessonCreated} />
            )}
 
            {/* View Lesson */}
            {selectedContent?.lessonId && !isEditingLesson && (
                <div className={styles.lessonView}>
                    <h3 className={styles.lessonTitle}>
                        {selectedContent.title || `Lesson ${lessons.findIndex(l => l.lessonId === selectedContent.lessonId) + 1}`}
                    </h3>
                    <div className={styles.lessonContent} dangerouslySetInnerHTML={{ __html: selectedContent.content }} />
                    <div className={styles.lessonActions}>
                        <button onClick={handleUpdateClick} className={`${styles.btn} ${styles.btnUpdate}`}>Update Lesson</button>
                        <LessonActions lessonId={selectedContent.lessonId} onLessonDeleted={handleLessonDeleted} />
                    </div>
                </div>
            )}
 
            {/* Edit Lesson Form */}
            {selectedContent?.lessonId && isEditingLesson && (
                <UpdateLesson
                    lesson={{
                        lessonId: selectedContent.lessonId,
                        title: selectedContent.title,
                        content: selectedContent.content
                    }}
                    onLessonUpdated={handleLessonUpdated}
                    onCancelUpdate={handleCancelUpdate}
                />
            )}
 
            {/* Lessons Overview */}
            {selectedContent === 'lessonsOverview' && course && (
                <div>
                    <h3>Lessons for {course.courseTitle}</h3>
                    <p>Select a Lesson to Display.</p>
                </div>
            )}
 
            {/* Render Assignment Component */}
            {selectedContent === 'assignments' && (
                <AssignmentForInstructor />
            )}
 
            {/* Render Quiz Component */}
            {selectedContent === 'quizzes' && (
                <ViewQuizzes
                    onBackToCourseView={handleReturnToCourseView}
                />
            )}
 
            {/* Render Q&A (ChatBox) Component */}
            {selectedContent === 'qna' && (
                <ChatBox />
            )}
 
            {/* Render Reports Component */}
            {selectedContent === 'reports' && courseId && (
                <InstructorReport courseId={courseId} />
            )}
        </main>
    );
};
 
export default InstructorCourseContent;
 