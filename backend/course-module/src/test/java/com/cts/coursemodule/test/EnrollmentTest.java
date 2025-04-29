package com.cts.coursemodule.test;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
 
import com.cts.coursemodule.client.UserClient;
import com.cts.coursemodule.dao.CourseDAO;
import com.cts.coursemodule.dao.EnrollmentDAO;
import com.cts.coursemodule.dto.CourseDetailsDTO;
import com.cts.coursemodule.dto.EnrollmentDTO;
import com.cts.coursemodule.enums.Category;
import com.cts.coursemodule.exception.AlreadyEnrolledException;
import com.cts.coursemodule.exception.CourseNotFoundException;
import com.cts.coursemodule.exception.StudentNotFoundException;
import com.cts.coursemodule.model.Course;
import com.cts.coursemodule.model.Enrollment;
import com.cts.coursemodule.service.EnrollmentService;
 
@SpringBootTest
class EnrollmentTest {
 
    @InjectMocks
    private EnrollmentService enrollmentService;
 
    @Mock
    private EnrollmentDAO enrollmentDAO;
 
    @Mock
    private CourseDAO courseDAO;
 
    @Mock
    private UserClient userClient;
 
    private EnrollmentDTO enrollmentDTO;
    private Course course;
    private Enrollment enrollment;
    private final String studentId = UUID.randomUUID().toString();
    private final String courseId = UUID.randomUUID().toString();
 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
 
        enrollmentDTO = new EnrollmentDTO();
        enrollmentDTO.setStudentId(UUID.fromString(studentId));
        enrollmentDTO.setCourseId(UUID.fromString(courseId));
 
        course = new Course();
        course.setCourseId(courseId);
        course.setCourseTitle("Java Basics");
        course.setDescription("Introduction to Java");
        course.setInstructorId(UUID.randomUUID().toString());
        course.setCategory(Category.TECHNOLOGY);
        course.setDuration(8);
 
        enrollment = new Enrollment();
        enrollment.setEnrollmentId(UUID.randomUUID().toString());
        enrollment.setStudentId(studentId);
        enrollment.setCourse(course);
        enrollment.setDateOfEnrollment(LocalDate.now());
    }
 
    
    @Test
    void testCheckStudent_studentExists() {
        when(userClient.checkStudent(studentId)).thenReturn(true);
        assertTrue(enrollmentService.checkStudent(studentId));
        verify(userClient, times(1)).checkStudent(studentId);
    }
 
    @Test
    void testCreateEnrollment_success() {
        when(userClient.checkStudent(studentId)).thenReturn(true);
        when(courseDAO.findById(courseId)).thenReturn(Optional.of(course));
        when(enrollmentDAO.existsByStudentIdAndCourse(studentId, course)).thenReturn(false);
        when(enrollmentDAO.save(any(Enrollment.class))).thenReturn(enrollment);
 
        Enrollment createdEnrollment = enrollmentService.createEnrollment(enrollmentDTO);
 
        assertNotNull(createdEnrollment);
        assertEquals(studentId, createdEnrollment.getStudentId());
        assertEquals(course, createdEnrollment.getCourse());
        assertNotNull(createdEnrollment.getDateOfEnrollment());
        verify(userClient, times(1)).checkStudent(studentId);
        verify(courseDAO, times(1)).findById(courseId);
        verify(enrollmentDAO, times(1)).existsByStudentIdAndCourse(studentId, course);
        verify(enrollmentDAO, times(1)).save(any(Enrollment.class));
    }
 
    @Test
    void testCreateEnrollment_studentNotFound() {
        when(userClient.checkStudent(studentId)).thenReturn(false);
        assertThrows(StudentNotFoundException.class, () -> enrollmentService.createEnrollment(enrollmentDTO));
        verify(userClient, times(1)).checkStudent(studentId);
        verify(courseDAO, never()).findById(anyString());
        verify(enrollmentDAO, never()).existsByStudentIdAndCourse(anyString(), any());
        verify(enrollmentDAO, never()).save(any(Enrollment.class));
    }
 
    @Test
    void testCreateEnrollment_courseNotFound() {
        when(userClient.checkStudent(studentId)).thenReturn(true);
        when(courseDAO.findById(courseId)).thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class, () -> enrollmentService.createEnrollment(enrollmentDTO));
        verify(userClient, times(1)).checkStudent(studentId);
        verify(courseDAO, times(1)).findById(courseId);
        verify(enrollmentDAO, never()).existsByStudentIdAndCourse(anyString(), any());
        verify(enrollmentDAO, never()).save(any(Enrollment.class));
    }
 
    @Test
    void testCreateEnrollment_alreadyEnrolled() {
        when(userClient.checkStudent(studentId)).thenReturn(true);
        when(courseDAO.findById(courseId)).thenReturn(Optional.of(course));
        when(enrollmentDAO.existsByStudentIdAndCourse(studentId, course)).thenReturn(true);
        assertThrows(AlreadyEnrolledException.class, () -> enrollmentService.createEnrollment(enrollmentDTO));
        verify(userClient, times(1)).checkStudent(studentId);
        verify(courseDAO, times(1)).findById(courseId);
        verify(enrollmentDAO, times(1)).existsByStudentIdAndCourse(studentId, course);
        verify(enrollmentDAO, never()).save(any(Enrollment.class));
    }
 
    @Test
    void testGetAllEnrollments_success() {
        Enrollment enrollment2 = new Enrollment(UUID.randomUUID().toString(), UUID.randomUUID().toString(), new Course(), LocalDate.now().minusDays(2));
        List<Enrollment> enrollments = Arrays.asList(enrollment, enrollment2);
        when(enrollmentDAO.findAll()).thenReturn(enrollments);
        List<Enrollment> result = enrollmentService.getAllEnrollments();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(enrollmentDAO, times(1)).findAll();
    }
 
    @Test
    void testGetEnrollmentByStudentId_success() {
        Course course2 = new Course();
        course2.setCourseId(UUID.randomUUID().toString());
        course2.setCourseTitle("Spring Boot");
        List<Enrollment> enrollments = Arrays.asList(enrollment, new Enrollment(UUID.randomUUID().toString(), studentId, course2, LocalDate.now()));
        when(enrollmentDAO.findByStudentId(studentId)).thenReturn(enrollments);
        List<CourseDetailsDTO> result = enrollmentService.getEnrollmentByStudentId(studentId);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(course.getCourseId(), result.get(0).getCourseId());
        assertEquals(course.getCourseTitle(), result.get(0).getCourseTitle());
        assertEquals(course2.getCourseId(), result.get(1).getCourseId());
        assertEquals(course2.getCourseTitle(), result.get(1).getCourseTitle());
        verify(enrollmentDAO, times(1)).findByStudentId(studentId);
    }
 
    @Test
    void testGetEnrollmentByStudentId_noEnrollmentsFound() {
        when(enrollmentDAO.findByStudentId(studentId)).thenReturn(List.of());
        List<CourseDetailsDTO> result = enrollmentService.getEnrollmentByStudentId(studentId);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(enrollmentDAO, times(1)).findByStudentId(studentId);
    }
 
    @Test
    void testGetEnrollmentByCourseId_success() {
        String studentId2 = UUID.randomUUID().toString();
        List<Enrollment> enrollments = Arrays.asList(enrollment, new Enrollment(UUID.randomUUID().toString(), studentId2, course, LocalDate.now()));
        when(enrollmentDAO.findAllByCourseCourseId(courseId)).thenReturn(enrollments);
        List<String> result = enrollmentService.getEnrollmentByCourseId(courseId);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(studentId));
        assertTrue(result.contains(studentId2));
        verify(enrollmentDAO, times(1)).findAllByCourseCourseId(courseId);
    }
 
    @Test
    void testGetEnrollmentByCourseId_courseNotFound() {
        when(enrollmentDAO.findAllByCourseCourseId(courseId)).thenReturn(List.of());
        assertThrows(CourseNotFoundException.class, () -> enrollmentService.getEnrollmentByCourseId(courseId));
        verify(enrollmentDAO, times(1)).findAllByCourseCourseId(courseId);
    }
 
    @Test
    void testGetEnrollmentDate_enrollmentFound() {
        LocalDate now = LocalDate.now();
        enrollment.setDateOfEnrollment(now);
        when(enrollmentDAO.findByStudentIdAndCourse_CourseId(studentId, courseId)).thenReturn(Optional.of(enrollment));
        LocalDate result = enrollmentService.getEnrollmentDate(studentId, courseId);
        assertNotNull(result);
        assertEquals(now, result);
        verify(enrollmentDAO, times(1)).findByStudentIdAndCourse_CourseId(studentId, courseId);
    }
 
    @Test
    void testGetEnrollmentDate_enrollmentNotFound() {
        when(enrollmentDAO.findByStudentIdAndCourse_CourseId(studentId, courseId)).thenReturn(Optional.empty());
        LocalDate result = enrollmentService.getEnrollmentDate(studentId, courseId);
        assertNull(result);
        verify(enrollmentDAO, times(1)).findByStudentIdAndCourse_CourseId(studentId, courseId);
    }
 
    @Test
    void testVerifyEnrollment_enrollmentExists() {
        when(courseDAO.findById(courseId)).thenReturn(Optional.of(course));
        when(enrollmentDAO.existsByStudentIdAndCourse(studentId, course)).thenReturn(true);
        assertTrue(enrollmentService.verifyEnrollment(studentId, courseId));
        verify(courseDAO, times(1)).findById(courseId);
        verify(enrollmentDAO, times(1)).existsByStudentIdAndCourse(studentId, course);
    }
 
    @Test
    void testVerifyEnrollment_courseNotFound() {
        when(courseDAO.findById(courseId)).thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class, () -> enrollmentService.verifyEnrollment(studentId, courseId));
        verify(courseDAO, times(1)).findById(courseId);
        verify(enrollmentDAO, never()).existsByStudentIdAndCourse(anyString(), any());
    }
 
    @Test
    void testStudentList_success() {
        String studentId2 = UUID.randomUUID().toString();
        List<Enrollment> enrollments = Arrays.asList(enrollment, new Enrollment(UUID.randomUUID().toString(), studentId2, course, LocalDate.now()));
        when(courseDAO.findById(courseId)).thenReturn(Optional.of(course));
        when(enrollmentDAO.findAllByCourseCourseId(courseId)).thenReturn(enrollments);
        List<String> result = enrollmentService.studentList(courseId);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(studentId));
        assertTrue(result.contains(studentId2));
        verify(courseDAO, times(1)).findById(courseId);
        verify(enrollmentDAO, times(1)).findAllByCourseCourseId(courseId);
    }
}
 