import React, { useState, useEffect, useCallback } from "react";
import axios from "axios";
import Header from "../../components/Header";
import Footer from "../../components/Footer";
// Import React-Bootstrap components
import { Container, Row, Col, Card, Button, Form, Spinner, Alert, Accordion, Badge, Stack } from 'react-bootstrap';
// Import Icons
import { TagsFill, ClockHistory, ListUl, CheckCircleFill, InfoCircleFill, Search } from 'react-bootstrap-icons';
// Keep your CSS module
import styles from "./StudentDashboard.module.css";
 
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
 
// Function to format category names displayed in course card.
const formatCategory = (category) => {
    if (!category || typeof category !== 'string') return 'N/A';
    return category.charAt(0).toUpperCase() + category.slice(1).toLowerCase();
};
 
function StudentDashboard() {
    
    const [allAvailableCourses, setAllAvailableCourses] = useState([]);
    const [enrolledCourses, setEnrolledCourses] = useState([]);
    const [filteredCourses, setFilteredCourses] = useState([]);
    const [categoryFilter, setCategoryFilter] = useState("All");
    const [durationFilter, setDurationFilter] = useState("All");
    const [courseTypeFilter, setCourseTypeFilter] = useState("All");
    const [enrolledCourseIds, setEnrolledCourseIds] = useState(new Set());
    const [loadingAll, setLoadingAll] = useState(false);
    const [loadingEnrolled, setLoadingEnrolled] = useState(false);
    const [error, setError] = useState(null); // Main error state for API issues
    const [searchQuery, setSearchQuery] = useState('');
    const [allCoursesMap, setAllCoursesMap] = useState(new Map());
 
    
    const getUserInfo = useCallback(() => {
        const email = localStorage.getItem("email");
        const token = email ? localStorage.getItem(`${email}_token`) : null;
        const studentId = email ? localStorage.getItem(`${email}_uuid`) : null;
        if (!email || !studentId) console.warn("Email or Student ID missing from localStorage.");
        return { email, token, studentId };
    }, []);
 
    // --- Data Fetching ---
    // Fetch All Courses AND Populate Map
    const fetchAllAvailableCourses = useCallback(async () => {
        setLoadingAll(true);
        setError(null); // Clear previous errors when starting fetch
        const apiUrl = `http://localhost:8082/api/course/get`; // Your working URL
        console.log("Attempting to fetch all available courses...");
        try {
            const { token } = getUserInfo();
            const headers = token ? { Authorization: `Bearer ${token}` } : {};
            const response = await axios.get(apiUrl, { headers });
            const coursesData = response?.data?.data || response?.data || [];
            console.log("Fetched All Courses Raw Data:", coursesData);
 
            if (Array.isArray(coursesData)) {
                setAllAvailableCourses(coursesData);
                // *** Create and store the map ***
                const courseMap = new Map();
                coursesData.forEach(course => {
                    if (course && course.courseId) {
                        courseMap.set(course.courseId, course); // Map ID to full course object
                    }
                });
                setAllCoursesMap(courseMap); // Update map state
                console.log("Set All Available Courses State and Populated Map:", coursesData.length);
            } else {
                console.error("Fetched available courses data is not an array:", coursesData);
                setAllAvailableCourses([]);
                setAllCoursesMap(new Map()); // Clear map
                setError(prevError => prevError || "Received invalid data format for available courses.");
            }
        } catch (err) {
            console.error("Error fetching all available courses:", err.response || err.request || err.message);
            setError(prevError => prevError || (err.response?.data?.message || "Failed to fetch available courses.")); // Set error on failure
            setAllAvailableCourses([]);
            setAllCoursesMap(new Map()); 
        } finally {
            setLoadingAll(false); 
        }
    }, [getUserInfo]); 
 
    // Function to fetch the enrolled courses.
    const fetchEnrolledCourses = useCallback(async () => {
        const { studentId, token } = getUserInfo();

        if (!studentId) {
            console.log("Student ID missing, cannot fetch enrolled courses.");
            setEnrolledCourses([]); setEnrolledCourseIds(new Set()); return;
        }
        
        if (allCoursesMap.size === 0) {
            console.log("No Enrolled courses are there");
            return; 
        }
 
        setLoadingEnrolled(true);
        const apiUrl = `http://localhost:8082/api/enrollment/student?studentId=${studentId}`; // Your working URL
        console.log("Attempting to fetch enrolled courses structure...");
 
        try {
            const response = await axios.get(apiUrl, { headers: token ? { Authorization: `Bearer ${token}` } : {}, });
            const enrollmentData = response?.data?.data || response?.data || [];
            console.log("Fetched Enrolled Courses Raw Data Structure:", response?.data);
 
            let enrolledCourse = [];
            if (Array.isArray(enrollmentData)) {
                enrolledCourse = enrollmentData.map((item) => {
                    const basicCourseInfo = item?.course || item;
                    if (basicCourseInfo && basicCourseInfo.courseId) {
                        
                        const fullCourseDetails = allCoursesMap.get(basicCourseInfo.courseId);
                        if (fullCourseDetails) {
                            return fullCourseDetails; // Use full details if found
                        } else {
                            console.warn(`Full details not found for enrolled courseId: ${basicCourseInfo.courseId}. Using basic info.`);
                            return basicCourseInfo; // Fallback (will miss category/duration)
                        }
                    }
                    return null;
                }).filter(Boolean);
            } else { console.error("Fetched enrolled courses data structure is not an array:", enrollmentData); }
 
            console.log("Processing Enriched Enrolled Courses:", enrolledCourse);
            setEnrolledCourses(enrolledCourse);
            setEnrolledCourseIds(new Set(enrolledCourse.map((course) => course?.courseId).filter(Boolean)));
 
        } catch (err) {
            console.error("Error fetching enrolled courses structure:", err.response || err.request || err.message);
            setError(prevError => prevError || (err.response?.data?.message || "Failed to fetch enrolled courses."));
            setEnrolledCourses([]); setEnrolledCourseIds(new Set());
        } finally {
            setLoadingEnrolled(false); 
        }
       
    }, [getUserInfo, allCoursesMap]); // Removed 'error'
 
    // --- Enrollment ---
    const enrollCourse = useCallback(async (courseId) => {
        const { studentId, token } = getUserInfo();
        if (!studentId) { alert("Could not enroll. User information missing."); return; }
        if (enrolledCourseIds.has(courseId)) { alert("You are already enrolled in this course."); return; }
        if (!courseId) { alert("Invalid course selected for enrollment."); return; }
        console.log(`Enrolling student ${studentId} in course ${courseId}`);
        try {
            await axios.post(
                "http://localhost:8082/api/enrollment/create", // Your URL
                { courseId, studentId },
                { headers: token ? { Authorization: `Bearer ${token}` } : {} }
            );
            alert("Enrolled successfully!");
            // Re-fetch BOTH after enrollment
            fetchAllAvailableCourses(); // Re-populates map too
            // fetchEnrolledCourses will be triggered automatically when map updates if needed
            // Or call explicitly if immediate update is desired:
            // fetchEnrolledCourses(); // Call if needed, but dependency might handle it
        } catch (enrollError) {
            console.error("Error enrolling in the course:", enrollError.response || enrollError.request || enrollError.message);
            alert(`Enrollment failed: ${enrollError.response?.data?.message || enrollError.message}`);
        }
    // Ensure all functions called inside are stable or listed as dependencies
    }, [getUserInfo, enrolledCourseIds, fetchAllAvailableCourses /*, fetchEnrolledCourses */]); // fetchEnrolledCourses removed if triggered by map update
 
    // --- Search Handler ---
    const handleSearch = useCallback((searchTerm) => {
        console.log("Search term received:", searchTerm);
        setSearchQuery(searchTerm);
    }, []);
 
    // --- Effects ---
    // Initial Fetch for All Courses (populates map)
    useEffect(() => {
        console.log("--- Initial Fetch Effect Running ---");
        fetchAllAvailableCourses();
    }, [fetchAllAvailableCourses]); // Trigger only when function definition changes (effectively once)
 
    // Fetch Enrolled Courses *after* the map is populated
    useEffect(() => {
        // Only run if the map has been populated (size > 0)
        if (allCoursesMap.size > 0) {
            console.log("--- Map Ready Effect: Triggering fetchEnrolledCourses ---");
            fetchEnrolledCourses();
        }
        // This effect depends on fetchEnrolledCourses callback AND the map itself
    }, [allCoursesMap, fetchEnrolledCourses]);
 
    // Filtering logic
    useEffect(() => {
        console.log("--- Running Filter Effect ---");
        let currentCourses = [];
        if (courseTypeFilter === "Enrolled") {
            currentCourses = Array.isArray(enrolledCourses) ? [...enrolledCourses] : [];
        } else {
            currentCourses = Array.isArray(allAvailableCourses) ? [...allAvailableCourses] : [];
            // Apply category/duration filters only when viewing "All"
            if (categoryFilter !== "All") {
                currentCourses = currentCourses.filter( (course) => course && course.category === categoryFilter );
            }
            if (durationFilter !== "All") {
                const maxDuration = parseInt(durationFilter, 10);
                if (!isNaN(maxDuration)) { // Check if duration is a valid number
                    currentCourses = currentCourses.filter((course) => {
                        if (course && course.duration !== undefined && course.duration !== null) {
                            const duration = parseInt(course.duration, 10);
                            return !isNaN(duration) && duration >= 0 && duration <= maxDuration;
                        } return false;
                    });
                } else {
                    console.warn("Invalid duration filter value:", durationFilter); // Warn but don't set global error
                }
            }
        }
        // Apply search filter
        if (searchQuery) {
            const lowerCaseQuery = searchQuery.toLowerCase();
            currentCourses = currentCourses.filter(course =>
                course && course.courseTitle && typeof course.courseTitle === 'string' && course.courseTitle.toLowerCase().includes(lowerCaseQuery)
            );
        }
        console.log("--- Final Filtered Courses to be Set:", currentCourses);
        setFilteredCourses(currentCourses);
 
    // Removed 'error' from dependencies
    }, [ courseTypeFilter, categoryFilter, durationFilter, allAvailableCourses, enrolledCourses, searchQuery ]);
 
 
    const isLoading = loadingAll || loadingEnrolled;
 
    console.log("--- Rendering ---", { isLoading, error, filteredCoursesLength: filteredCourses?.length });
 
    // --- JSX Structure ---
    return (
        <div className={styles.containerWithBackground}>
            <Header onSearch={handleSearch} />
            <Container fluid className={`py-4 py-md-5 ${styles.contentOverlay}`}>
                <Row>
                    {/* Sidebar Col */}
                    <Col md={4} lg={3} xl={2}>
 
                        <div className={`${styles.stickySidebar} p-3`}>
                            <h4 className="text-white mb-3"> <ListUl className="me-2" /> Filters </h4>
                            <Accordion defaultActiveKey={['0', '1', '2']} alwaysOpen>
                                {/* Accordion Item: View */}
                                <Accordion.Item eventKey="0" className={styles.filterAccordionItem}>
                                    <Accordion.Header>View Courses</Accordion.Header>
                                    <Accordion.Body> <Form.Select bsPrefix={styles.customSelect} value={courseTypeFilter} onChange={(e) => setCourseTypeFilter(e.target.value)} disabled={isLoading}> <option value="All">All Available</option> <option value="Enrolled">My Enrolled</option> </Form.Select> </Accordion.Body>
                                </Accordion.Item>
                                {/* Accordion Item: Category (Conditional) */}
                                {courseTypeFilter === "All" && (
                                    <Accordion.Item eventKey="1" className={styles.filterAccordionItem}>
                                        <Accordion.Header><TagsFill className="me-2 text-primary" /> Category</Accordion.Header>
                                        <Accordion.Body> <Form.Select bsPrefix={styles.customSelect} value={categoryFilter} onChange={(e) => setCategoryFilter(e.target.value)} disabled={isLoading}>
                                            <option value="All">All Categories</option>
                                            <option value="TECHNOLOGY">Technology</option>
                                            <option value="BUSINESS">Business</option>
                                            <option value="ARTS">Arts</option>
                                            <option value="LANGUAGE">Language</option>
                                            <option value="SCIENCE">Science</option>
                                            <option value="HEALTH">Health</option>
                                            <option value="LAW">Law</option>
                                            <option value="MATH">Math</option>
                                            <option value="FINANCEANDACCOUNTING">Finance and Accounting</option>
                                            <option value="MUSIC">Music</option>
                                            <option value="PHOTOGRAPHY">Photography</option>
 
                                        </Form.Select> </Accordion.Body>
                                    </Accordion.Item>
                                )}
                                {/* Accordion Item: Duration (Conditional) */}
                                {courseTypeFilter === "All" && (
                                    <Accordion.Item eventKey="2" className={styles.filterAccordionItem}>
                                        <Accordion.Header><ClockHistory className="me-2 text-primary" /> Max Duration</Accordion.Header>
                                        <Accordion.Body> <Form.Select bsPrefix={styles.customSelect} value={durationFilter} onChange={(e) => setDurationFilter(e.target.value)} disabled={isLoading}>
                                            <option value="All">Any Duration</option>
                                            <option value="1">Up to 1 Week</option>
                                            <option value="2">Up to 2 Weeks</option>
                                            <option value="3">Up to 3 Weeks</option>
                                            <option value="4">Up to 4 Weeks</option>
                                            <option value="5">Up to 5 Weeks</option>
                                            <option value="6">Up to 6 Weeks</option>
                                            <option value="7">Up to 7 Weeks</option>
                                            <option value="8">Up to 8 Weeks</option>
                                            <option value="9">Up to 9 Weeks</option>
                                             
                                        </Form.Select> </Accordion.Body>
                                    </Accordion.Item>
                                )}
                            </Accordion>
                        </div>
                    </Col>
                  
                    {/* Main Content Column */}

               
                    <Col md={8} lg={9} xl={10}>
                        
                        {/* Error display */}
                        {error && <Alert variant="danger" dismissible onClose={() => setError(null)}> <InfoCircleFill className="me-2"/> Error: {error}</Alert>}
                        {/* Loading display */}
                        {isLoading && ( <div className="text-center py-5"> <Spinner animation="grow" variant="light" /> <p className="text-light mt-3 lead">Loading courses...</p> </div> )}
                         
                        {/* Course List Grid */}
                        {!isLoading && (
                            <div className={`${styles.courseListGrid} course-list-parent`}>
                                {Array.isArray(filteredCourses) && filteredCourses.map((course) => (
                                    course && course.courseId ? (
                                        // Use card structure with visual tweaks
                                        <Card key={course.courseId} className={`${styles.courseCardEnhanced} course-card-item`}>
                                            <Card.Img
                                                variant="top"
                                                alt={course.courseTitle || "Course thumbnail"}
                                                src={categoryImageMap[course.category] || null} // Get image based on category
                                                className={styles.courseCardImage}
                                            />
                                            <Badge bg="primary" className={styles.categoryBadge}>{formatCategory(course.category)}</Badge>
                                            <Card.Body className="d-flex flex-column p-1">
                                                <Card.Title className={`${styles.courseCardTitle} mb-1`}>
                                                    {course.courseTitle || "Untitled Course"}
                                                </Card.Title>
                                                <div className="d-flex justify-content-center mb-1">
                                                    <Stack direction="horizontal" gap={2} className="text-muted" style={{ fontSize: '0.7rem' }}>
                                                        {course.duration !== undefined && course.duration !== null && (
                                                            <div className="d-flex align-items-center" title={`${course.duration} week(s) duration`}>
                                                                <ClockHistory className="me-1" />
                                                                <span>{`${course.duration} week${parseInt(course.duration, 10) !== 1 ? 's' : ''}`}</span>
                                                            </div>
                                                        )}
                                                    </Stack>
                                                </div>
                                            </Card.Body>
                                            <Card.Footer className={styles.courseCardFooter}>
                                                <Stack direction="horizontal" gap={2}>
                                                    <Button variant="outline-primary" size="sm" className="flex-grow-1" onClick={() => (window.location.href = `/student/course/${course.courseId}`)}> View Details </Button>
                                                    {enrolledCourseIds.has(course.courseId) ? (
                                                        <Button variant="outline-success" size="sm" disabled className="flex-grow-1 d-flex align-items-center justify-content-center">
                                                            <CheckCircleFill className="me-1" /> Enrolled
                                                        </Button>
                                                    ) : (
                                                        <Button variant="primary" size="sm" className="flex-grow-1" onClick={() => enrollCourse(course.courseId)}> Enroll Now </Button>
                                                    )}
                                                </Stack>
                                            </Card.Footer>
                                        </Card>
                                    ) : null
                                ))}
                            </div>
                        )}
                        {/* No Courses Alert */}
                        {!isLoading && (!Array.isArray(filteredCourses) || filteredCourses.length === 0) && !error && (
                            <Alert variant="light" className="text-center bg-transparent border-0 text-white-50">
                                {searchQuery ? <><Search className="me-2"/> No courses found matching your search.</> :
                                    (courseTypeFilter === "Enrolled" ? "You are not enrolled in any courses." : "No courses match your current filters.")
                                }
                            </Alert>
                        )}
                        <br></br>
                        <p className = {styles.noCourse}> No more courses available</p>
                    </Col>
                </Row>
            </Container>
            <br></br>
            <Footer />
        </div>
    );
}
 
export default StudentDashboard;
 
 