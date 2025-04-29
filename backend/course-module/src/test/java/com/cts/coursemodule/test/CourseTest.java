package com.cts.coursemodule.test;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
 
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
 
import com.cts.coursemodule.client.CommunicationClient;
import com.cts.coursemodule.client.UserClient;
import com.cts.coursemodule.dao.CourseDAO;
import com.cts.coursemodule.dto.CourseDTO;
import com.cts.coursemodule.enums.Category;
import com.cts.coursemodule.exception.CourseAlreadyExistsException;
import com.cts.coursemodule.exception.CourseNotFoundException;
import com.cts.coursemodule.exception.InstructorNotAllowedException;
import com.cts.coursemodule.model.Course;
import com.cts.coursemodule.service.CourseService;
 
@SpringBootTest
class CourseTest {
 
    @InjectMocks
    private CourseService courseService;
 
    @Mock
    private CourseDAO courseDAO;
 
    @Mock
    private CommunicationClient communicationClient;
 
    @Mock
    private UserClient userClient;
 
    private CourseDTO courseDTO;
    private Course course;
    private final String instructorId = UUID.randomUUID().toString();
    private final String courseId = UUID.randomUUID().toString();
 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        courseDTO = new CourseDTO();
        courseDTO.setCourseTitle("Java Basics");
        courseDTO.setDescription("Introduction to Java programming");
        courseDTO.setInstructorId(UUID.fromString(instructorId));
        courseDTO.setCategory(Category.TECHNOLOGY);
        courseDTO.setDuration(8);
 
        course = new Course();
        course.setCourseId(courseId);
        course.setCourseTitle("Java Basics");
        course.setDescription("Introduction to Java programming");
        course.setInstructorId(instructorId);
        course.setCategory(Category.TECHNOLOGY);
        course.setDuration(8);
    }
 
    @Test
    void testCheckInstructor_instructorExists() {
        when(userClient.checkInstructor(instructorId)).thenReturn(true);
        assertTrue(courseService.checkInstructor(instructorId));
        verify(userClient, times(1)).checkInstructor(instructorId);
    }
 
    @Test
    void testCreateCourse_success() {
        when(userClient.checkInstructor(instructorId)).thenReturn(true);
        when(courseDAO.findByInstructorId(instructorId)).thenReturn(List.of());
        when(courseDAO.save(any(Course.class))).thenReturn(course);
        Course createdCourse = courseService.createCourse(courseDTO);
        assertNotNull(createdCourse);
        assertEquals(course.getCourseTitle(), createdCourse.getCourseTitle());
        verify(courseDAO, times(1)).save(any(Course.class));
        verify(communicationClient, times(1)).createNewNotification(anyString());
    }
 
    @Test
    void testCreateCourse_instructorNotAllowed() {
        when(userClient.checkInstructor(instructorId)).thenReturn(false);
        assertThrows(InstructorNotAllowedException.class, () -> courseService.createCourse(courseDTO));
        verify(courseDAO, never()).save(any(Course.class));
        verify(communicationClient, never()).createNewNotification(anyString());
    }
 
    @Test
    void testCreateCourse_alreadyExistsWithSameContents() {
        when(userClient.checkInstructor(instructorId)).thenReturn(true);
        when(courseDAO.findByInstructorId(instructorId)).thenReturn(List.of(course));
        assertThrows(CourseAlreadyExistsException.class, () -> courseService.createCourse(courseDTO));
        verify(courseDAO, never()).save(any(Course.class));
        verify(communicationClient, never()).createNewNotification(anyString());
    }
 
    @Test
    void testUpdateCourse_courseNotFound() {
        CourseDTO updateDTO = new CourseDTO();
        updateDTO.setCourseId(UUID.randomUUID());
        when(courseDAO.findById(updateDTO.getCourseId().toString())).thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class, () -> courseService.updateCourse(updateDTO));
        verify(courseDAO, never()).save(any(Course.class));
    }
 
    @Test
    void testUpdateCourse_instructorNotAllowed() {
        CourseDTO updateDTO = new CourseDTO();
        updateDTO.setCourseId(UUID.fromString(courseId));
        updateDTO.setInstructorId(UUID.randomUUID());
        when(courseDAO.findById(courseId)).thenReturn(Optional.of(course));
        assertThrows(InstructorNotAllowedException.class, () -> courseService.updateCourse(updateDTO));
        verify(courseDAO, never()).save(any(Course.class));
    }
 
    @Test
    void testGetAllCourses_success() {
        List<Course> courses = Arrays.asList(course, new Course());
        when(courseDAO.findAll()).thenReturn(courses);
        List<Course> result = courseService.getAllCourses();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(courseDAO, times(1)).findAll();
    }
 
    @Test
    void testGetCoursesByInstructorId_success() {
        List<Course> courses = Arrays.asList(course, new Course());
        when(courseDAO.findByInstructorId(instructorId)).thenReturn(courses);
        List<Course> result = courseService.getCoursesByInstructorId(instructorId);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(instructorId, result.get(0).getInstructorId());
        verify(courseDAO, times(1)).findByInstructorId(instructorId);
    }
 
    @Test
    void testDeleteCourse_success() {
        when(courseDAO.findById(courseId)).thenReturn(Optional.of(course));
        doNothing().when(courseDAO).deleteById(courseId);
        assertDoesNotThrow(() -> courseService.deleteCourse(courseId, instructorId));
        verify(courseDAO, times(1)).deleteById(courseId);
    }
 
    @Test
    void testDeleteCourse_courseNotFound() {
        when(courseDAO.findById("nonExistentId")).thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class, () -> courseService.deleteCourse("nonExistentId", instructorId));
        verify(courseDAO, never()).deleteById(anyString());
    }
 
    @Test
    void testDeleteCourse_instructorNotAllowed() {
        Course differentCourse = new Course();
        differentCourse.setInstructorId("differentInstructorId");
        when(courseDAO.findById(courseId)).thenReturn(Optional.of(differentCourse));
        assertThrows(InstructorNotAllowedException.class, () -> courseService.deleteCourse(courseId, instructorId));
        verify(courseDAO, never()).deleteById(anyString());
    }
 
    @Test
    void testVerifyInstructor_courseFoundAndInstructorMatches() {
        when(courseDAO.findById(courseId)).thenReturn(Optional.of(course));
        assertTrue(courseService.verifyInstructor(instructorId, courseId));
        verify(courseDAO, times(1)).findById(courseId);
    }
 
    @Test
    void testGetCourseById_success() {
        when(courseDAO.findById(courseId)).thenReturn(Optional.of(course));
        Optional<Course> result = courseService.getCourseById(courseId);
        assertTrue(result.isPresent());
        assertEquals(course.getCourseTitle(), result.get().getCourseTitle());
        verify(courseDAO, times(1)).findById(courseId);
    }
 
    @Test
    void testVerifyCourse_courseExists() {
        when(courseDAO.findById(courseId)).thenReturn(Optional.of(course));
        assertTrue(courseService.verifyCourse(courseId));
        verify(courseDAO, times(1)).findById(courseId);
    }
}
 