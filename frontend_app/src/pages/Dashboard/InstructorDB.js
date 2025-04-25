import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
import styles from "./InstructorDashboard.module.css";
import { Card, Button, Alert, Spinner, Badge } from 'react-bootstrap';
import { ClockHistory } from 'react-bootstrap-icons';
 
import technologyImage from "../../assests/technology.jpg";
import languageImage from "../../assests/language.jfif";
import healthImage from "../../assests/health.jpg";
import scienceImage from "../../assests/science.jfif";
import businessImage from "../../assests/business.jpg";
import artsImage from "../../assests/arts.jfif";
import financeImage from "../../assests/finance.jpg";
import lawImage from "../../assests/law.jfif";
import musicImage from "../../assests/music.jfif";
import photographyImage from "../../assests/photography.jfif";
import mathsImage from "../../assests/maths.jfif";
 
const categoryImageMap = {
    TECHNOLOGY: technologyImage,
    LANGUAGE: languageImage,
    HEALTH: healthImage,
    SCIENCE: scienceImage,
    BUSINESS: businessImage,
    ARTS: artsImage,
    FINANCEANDACCOUNTING: financeImage,
    LAW: lawImage,
    MUSIC: musicImage,
    PHOTOGRAPHY: photographyImage,
    MATH: mathsImage
};
 
// Helper function to format category names
const formatCategory = (category) => {
    if (!category || typeof category !== 'string') return 'N/A';
    return category.charAt(0).toUpperCase() + category.slice(1).toLowerCase();
};
 
const InstructorDB = () => {
    const [courses, setCourses] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [filteredCourses, setFilteredCourses] = useState([]);
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
 
    useEffect(() => {
        const fetchInstructorCourses = async () => {
 
            const email = localStorage.getItem('email');
            const token = localStorage.getItem(`${email}_token`);
            const instructorId = localStorage.getItem(`${email}_uuid`);
            try {
                const response = await axios.get(
                    `http://localhost:8082/api/course/instructorId?instructorId=${instructorId}`,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`
                        }
                    }
                );
                setCourses(response.data.data || []);
                setLoading(false);
            } catch (err) {
                console.error('There was an error fetching the courses!', err);
                setError('Failed to load courses created by you.');
                setLoading(false);
                if (err.response && err.response.status === 401) {
                    localStorage.clear(); // Clear potentially invalid tokens
                    navigate('/login');
                }
            }
        };
 
        fetchInstructorCourses();
    }, [navigate]);
 
    const handleSearch = (query) => {
        setSearchQuery(query);
    };
 
    useEffect(() => {
        const filtered = courses.filter(course =>
            course.courseTitle?.toLowerCase().includes(searchQuery.toLowerCase())
        );
        setFilteredCourses(filtered);
    }, [searchQuery, courses]);
 
    if (loading) return <Spinner animation="grow" variant="light" />;
    if (error) return <Alert variant="danger">{error}</Alert>;
 
    return (
        <div className={styles.containerWithBackground}>
            <Header onSearch={handleSearch} />
            <div className={`${styles.contentOverlay} d-flex flex-column align-items-center`} style={{backgroundColor:'transparent'}}>
                <div className="d-flex justify-content-center mb-4" style={{ width: '100%', maxWidth: '900px' }}>
                    <Button onClick={() => navigate('/createcourse')} className={styles.createCourseButton} >
                        Create New Course
                    </Button>
                </div>
                <div className={`${styles.courseListGrid} course-list-parent`} style={{ width: '100%', maxWidth: '1200px',marginLeft:'50px',marginRight:'50px' }}>
                    {filteredCourses.map((course) => (
                        <Card key={course.courseId} className={`${styles.courseCardEnhanced} course-card-item`}>
                            <Card.Img
                                variant="top"
                                alt={course.courseTitle || "Course thumbnail"}
                                src={categoryImageMap[course.category] || null} // Get image based on category
                                className={styles.courseCardImage}
                            />
                            {course.category && <Badge bg="primary" className={styles.categoryBadge}>{formatCategory(course.category)}</Badge>}
                            <Card.Body className="d-flex flex-column align-items-center text-center p-2">
                                <Card.Title className={`${styles.courseCardTitle} mb-1`}>
                                    {course.courseTitle || "Untitled Course"}
                                </Card.Title>
                                {course.duration !== undefined && course.duration !== null && (
                                    <div className="d-flex align-items-center text-muted mb-1" style={{ fontSize: '0.7rem' }}>
                                        <ClockHistory className="me-1" />
                                        <span>{`${course.duration} week${parseInt(course.duration, 10) !== 1 ? 's' : ''}`}</span>
                                    </div>
                                )}
                            </Card.Body>
                            <Card.Footer className={`d-flex justify-content-center ${styles.courseCardFooter}`}>
                                <Button
                                    variant="outline-primary"
                                    size="sm"
                                    className="w-auto"
                                    onClick={() => (window.location.href = `/instructor/course/${course.courseId}`)}
                                >
                                    View Details
                                </Button>
                            </Card.Footer>
                        </Card>
                    ))}
                </div>
                {filteredCourses.length === 0 && !loading && (
                    <Alert variant="info" className="text-center mt-3" style={{ maxWidth: '1200px', width: '100%' }}>
                        {searchQuery ? 'No courses found matching your search.' : "You haven't created any courses yet."}
                    </Alert>
                )}
                <br />
                <p className = {styles.noCourse}> No more courses available</p>
            </div>
            <Footer />
        </div>
    );
};
 
export default InstructorDB;
 