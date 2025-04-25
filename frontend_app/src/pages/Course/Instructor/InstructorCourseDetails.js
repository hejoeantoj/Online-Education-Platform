// InstructorCourseDetails.js


import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import styles from "./InstructorCourseDetails.module.css";
import "bootstrap/dist/css/bootstrap.min.css";
import CourseHeader from "../CourseHeader";
import InstructorCourseSidebar from "./InstructorCourseSidebar";
import InstructorCourseContent from "./InstructorCourseContent";
 
const InstructorCourseDetails = () => {
    const { courseId } = useParams();
    const [course, setCourse] = useState(null);
    const [lessons, setLessons] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const [selectedContent, setSelectedContent] = useState(null);
    const [showLessons, setShowLessons] = useState(false);
 
    const email = localStorage.getItem("email");
    const token = localStorage.getItem(`${email}_token`);
    const userId = localStorage.getItem(`${email}_uuid`);
 


    //Function to fetch all the lesson in that particular course...

    const fetchLessons = async () => {
        try {
            const lessonsResponse = await axios.get(`http://localhost:8082/api/lesson/getAll?courseId=${courseId}&userId=${userId}`);
            setLessons(lessonsResponse.data || []);
        } catch (error) {
            console.error("Error fetching lessons:", error);
            setLessons([]);
        }
    };
     
    //Used to display the course details...
    useEffect(() => {
        const fetchCourseDetails = async () => {
            setLoading(true);
            setError(null);
            try {
                const response = await axios.get(`http://localhost:8082/api/course/courseDetails?courseId=${courseId}`);
                const courseData = response.data.data || null;
                setCourse(courseData);
                if (courseData) {
                    localStorage.setItem('selectedCourseTitle', courseData.courseTitle);
                } else {
                    setError("Course details not found.");
                    localStorage.removeItem('selectedCourseTitle');
                }
                if (courseData) {
                    await fetchLessons();
                }
            } catch (error) {
                console.error("Error fetching course details:", error);
                setError(`Failed to load course details. ${error.response?.data?.message || error.message}`);
                setCourse(null);
                localStorage.removeItem('selectedCourseTitle');
            } finally {
                setLoading(false);
            }
        };
        fetchCourseDetails();
    }, [courseId, userId]);

 
  //Toggle functionality used in the Side bar...
    const toggleSection = (section) => {
        
        if (section === "lessons") {
            const opening = !showLessons;
            setShowLessons(opening);
            if (opening && !selectedContent?.lessonId && selectedContent !== 'createLesson') {
                setSelectedContent('lessonsOverview');
            } else if (!opening && selectedContent === 'lessonsOverview') {
                setSelectedContent(null);
            }
        } else if (section === "assignments") {
            setSelectedContent('assignments');
            setShowLessons(false);
        } else if (section === "quizzes") {
            setSelectedContent('quizzes');
            setShowLessons(false);
        } else if (section === 'qna') {
            setSelectedContent('qna');
            setShowLessons(false);
        } else if (section === 'reports') {
            setSelectedContent('reports'); 
            setShowLessons(false);
        }
    };
 

    const handleCreateLessonClick = () => {
        setSelectedContent('createLesson');
        setShowLessons(true);
    };
 
    //Function to delete the course.
    const handleDeleteCourse = async () => {
        if (window.confirm("Are you sure you want to delete this course? This action cannot be undone.")) {
            try {
                await axios.delete(`http://localhost:8082/api/course/delete?courseId=${courseId}&instructorId=${userId}`, {
                    headers: { Authorization: `Bearer ${token}` }
                });
                alert("Course deleted successfully!");
                localStorage.removeItem('selectedCourseTitle');
                navigate("/instructor");
            } catch (error) {
                console.error("Error deleting course!", error);
                alert(`Failed to delete the course. ${error.response?.data?.message || error.message}`);
            }
        }
    };
 

    //Used to display the particular lesson in main content area..
    const handleLessonClick = (lesson) => {
        setSelectedContent(lesson);
        setShowLessons(true);
    };
 
    
    //Method is called once after the lesson is created
    const handleLessonCreated = async () => {
        setSelectedContent('lessonsOverview');
        await fetchLessons();
        setShowLessons(true);
    };
 
    const handleReturnToCourseView = () => {
        setSelectedContent(null);
        setShowLessons(false);
    };


    const handleLessonUpdatedFromContent = (updatedLesson) => {
        setLessons(prevLessons =>
            prevLessons.map(lesson =>
                lesson.lessonId === updatedLesson.lessonId ?
                    { ...lesson, title: updatedLesson.lessonTitle, content: updatedLesson.content } :
                    lesson
            )
        );
        setSelectedContent(updatedLesson); 
    };
 

    const handleLessonDeletedFromContent = (deletedLessonId) => {
        setLessons(prevLessons => prevLessons.filter(lesson => lesson.lessonId !== deletedLessonId));
        setSelectedContent('lessonsOverview'); 
    };
 
    const handleHomeClick = () => {
        navigate('/instructor');
      };
 
 
    if (loading) return <p>Loading course details...</p>;
    if (error && !course) return <p>{error}</p>;
    if (!course) return <p>Course not found or failed to load.</p>;
 
    return (
        <div>
            <CourseHeader
                title={course.courseTitle}
                onBack={() => navigate(-1)}
                onHome={() => navigate("/instructor")}
            />
 
            <div className={`container-fluid ${styles.courseDetailsContainer}`}>
                <InstructorCourseSidebar
                   // course={course}
                    lessons={lessons}
                    showLessons={showLessons}
                    selectedContent={selectedContent}
                    toggleSection={toggleSection}
                    handleLessonClick={handleLessonClick}
                    handleCreateLessonClick={handleCreateLessonClick}
                    handleDeleteCourse={handleDeleteCourse}
                    handleHomeClick={handleHomeClick}
                />
                <InstructorCourseContent
                    course={course}
                    lessons={lessons}
                    selectedContent={selectedContent}
                    courseId={courseId}
       
                    handleLessonCreated={handleLessonCreated}
                    handleReturnToCourseView={handleReturnToCourseView}
                    onLessonUpdatedFromContent={handleLessonUpdatedFromContent}
                    onLessonDeletedFromContent={handleLessonDeletedFromContent}
                />
            </div>
        </div>
    );
};
 
export default InstructorCourseDetails;
 