package com.cts.coursemodule.test;
 
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.cts.coursemodule.dao.CourseDAO;
import com.cts.coursemodule.dao.EnrollmentDAO;
import com.cts.coursemodule.dao.LessonDAO;
import com.cts.coursemodule.dto.LessonDTO;
import com.cts.coursemodule.enums.Category;
import com.cts.coursemodule.exception.CourseNotFoundException;
import com.cts.coursemodule.exception.InstructorNotAllowedException;
import com.cts.coursemodule.exception.LessonAlreadyExistsException;
import com.cts.coursemodule.exception.LessonNotFoundException;
import com.cts.coursemodule.model.Course;
import com.cts.coursemodule.model.Lesson;
import com.cts.coursemodule.service.LessonService;
 
@SpringBootTest
class LessonTest {
 
	@InjectMocks
	private LessonService lessonService;
 
	@Mock
	private LessonDAO lessonDAO;
 
	@Mock
	private CourseDAO courseDAO;
 
	@Mock
	private EnrollmentDAO enrollmentDAO;
 
	private LessonDTO lessonDTO;
	private Course course;
	private Lesson lesson;
	private final String courseId = UUID.randomUUID().toString();
	private final String instructorId = UUID.randomUUID().toString();
	private final String lessonId = UUID.randomUUID().toString();
	private final String studentId = UUID.randomUUID().toString();
 
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
 
		lessonDTO = new LessonDTO();
		lessonDTO.setCourseId(UUID.fromString(courseId));
		lessonDTO.setInstructorId(UUID.fromString(instructorId));
		lessonDTO.setLessonTitle("Introduction");
		lessonDTO.setContent("Basic concepts");
 
		course = new Course();
		course.setCourseId(courseId);
		course.setCourseTitle("Math 101");
		course.setDescription("Basic Mathematics");
		course.setInstructorId(instructorId);
		course.setCategory(Category.TECHNOLOGY);
		course.setDuration(12);
 
		lesson = new Lesson();
		lesson.setLessonId(lessonId);
		lesson.setCourse(course);
		lesson.setLessonTitle("Introduction");
		lesson.setContent("Basic concepts");
	}
 
	@Test
	void testAddLesson_success() {
		when(courseDAO.findById(courseId)).thenReturn(Optional.of(course));
		when(lessonDAO.existsByCourseAndLessonTitle(course, "Introduction")).thenReturn(false);
		when(lessonDAO.save(any(Lesson.class))).thenReturn(lesson);
		Lesson createdLesson = lessonService.addLesson(lessonDTO);
		assertNotNull(createdLesson);
		assertEquals("Introduction", createdLesson.getLessonTitle());
		assertEquals(course, createdLesson.getCourse());
		verify(courseDAO, times(1)).findById(courseId);
		verify(lessonDAO, times(1)).save(any(Lesson.class));
	}
 
	@Test
	void testAddLesson_courseNotFound() {
		when(courseDAO.findById(courseId)).thenReturn(Optional.empty());
		assertThrows(CourseNotFoundException.class, () -> lessonService.addLesson(lessonDTO));
		verify(courseDAO, times(1)).findById(courseId);
		verify(lessonDAO, never()).save(any(Lesson.class));
	}
 
	@Test
	void testAddLesson_instructorNotAllowed() {
		Course differentCourse = new Course();
		differentCourse.setCourseId(courseId);
		differentCourse.setInstructorId(UUID.randomUUID().toString());
		when(courseDAO.findById(courseId)).thenReturn(Optional.of(differentCourse));
		assertThrows(InstructorNotAllowedException.class, () -> lessonService.addLesson(lessonDTO));
		verify(courseDAO, times(1)).findById(courseId);
		verify(lessonDAO, never()).save(any(Lesson.class));
	}
 
	@Test
	void testAddLesson_lessonAlreadyExists() {
		when(courseDAO.findById(courseId)).thenReturn(Optional.of(course));
		when(lessonDAO.existsByCourseAndLessonTitle(course, "Introduction")).thenReturn(true);
		assertThrows(LessonAlreadyExistsException.class, () -> lessonService.addLesson(lessonDTO));
		verify(courseDAO, times(1)).findById(courseId);
		verify(lessonDAO, never()).save(any(Lesson.class));
	}
 
	@Test
	void testUpdateLesson_success() throws Exception {
		LessonDTO updateDTO = new LessonDTO();
		updateDTO.setLessonId(UUID.fromString(lessonId));
		updateDTO.setCourseId(UUID.fromString(courseId));
		updateDTO.setInstructorId(UUID.fromString(instructorId));
		updateDTO.setLessonTitle("Updated Introduction");
		when(lessonDAO.findById(lessonId)).thenReturn(Optional.of(lesson));
		when(courseDAO.findById(courseId)).thenReturn(Optional.of(course));
		when(lessonDAO.save(any(Lesson.class))).thenReturn(lesson);
		Lesson updatedLesson = lessonService.updateLesson(updateDTO);
		assertNotNull(updatedLesson);
		assertEquals("Updated Introduction", updatedLesson.getLessonTitle());
		verify(lessonDAO, times(1)).findById(lessonId);
		verify(lessonDAO, times(1)).save(any(Lesson.class));
	}
 
	@Test
	void testUpdateLesson_lessonNotFound() {
		LessonDTO updateDTO = new LessonDTO();
		updateDTO.setLessonId(UUID.randomUUID());
		when(lessonDAO.findById(updateDTO.getLessonId().toString())).thenReturn(Optional.empty());
		assertThrows(LessonNotFoundException.class, () -> lessonService.updateLesson(updateDTO));
		verify(lessonDAO, times(1)).findById(updateDTO.getLessonId().toString());
		verify(lessonDAO, never()).save(any(Lesson.class));
	}
 
	@Test
	void testUpdateLesson_instructorNotAllowed() {
		LessonDTO updateDTO = new LessonDTO();
		updateDTO.setLessonId(UUID.fromString(lessonId));
		updateDTO.setCourseId(UUID.fromString(courseId));
		updateDTO.setInstructorId(UUID.randomUUID());
		when(lessonDAO.findById(lessonId)).thenReturn(Optional.of(lesson));
		when(courseDAO.findById(courseId)).thenReturn(Optional.of(course));
		assertThrows(InstructorNotAllowedException.class, () -> lessonService.updateLesson(updateDTO));
		verify(lessonDAO, times(1)).findById(lessonId);
		verify(lessonDAO, never()).save(any(Lesson.class));
	}
 
	@Test
	void testDeleteLesson_success() {
		when(lessonDAO.findById(lessonId)).thenReturn(Optional.of(lesson));
		doNothing().when(lessonDAO).delete(lesson);
		assertDoesNotThrow(() -> lessonService.deleteLesson(lessonId, instructorId));
		verify(lessonDAO, times(1)).findById(lessonId);
		verify(lessonDAO, times(1)).delete(lesson);
	}
 
	@Test
	void testDeleteLesson_lessonNotFound() {
		when(lessonDAO.findById(lessonId)).thenReturn(Optional.empty());
		assertThrows(LessonNotFoundException.class, () -> lessonService.deleteLesson(lessonId, instructorId));
		verify(lessonDAO, times(1)).findById(lessonId);
		verify(lessonDAO, never()).delete(any());
	}
 
	@Test
	void testDeleteLesson_instructorNotAllowed() {
		Course differentCourse = new Course();
		differentCourse.setInstructorId(UUID.randomUUID().toString());
		Lesson lessonWithDifferentInstructor = new Lesson();
		lessonWithDifferentInstructor.setCourse(differentCourse);
		when(lessonDAO.findById(lessonId)).thenReturn(Optional.of(lessonWithDifferentInstructor));
		assertThrows(InstructorNotAllowedException.class, () -> lessonService.deleteLesson(lessonId, instructorId));
		verify(lessonDAO, times(1)).findById(lessonId);
		verify(lessonDAO, never()).delete(any());
	}
 
	@Test
	void testGetAllLessons_instructorAccess_success() {
		when(courseDAO.findById(courseId)).thenReturn(Optional.of(course));
		when(courseDAO.findLessonsByCourseId(courseId)).thenReturn(Arrays.asList(lesson));
		List<Lesson> result = lessonService.getAllLessons(courseId, instructorId);
		assertNotNull(result);
		assertEquals(1, result.size());
		verify(courseDAO, times(1)).findById(courseId);
		verify(courseDAO, times(1)).findLessonsByCourseId(courseId);
	}
 
	@Test
	void testGetAllLessons_courseNotFound() {
		when(courseDAO.findById(courseId)).thenReturn(Optional.empty());
		assertThrows(CourseNotFoundException.class, () -> lessonService.getAllLessons(courseId, instructorId));
		verify(courseDAO, times(1)).findById(courseId);
		verify(courseDAO, never()).findLessonsByCourseId(anyString());
		verify(enrollmentDAO, never()).findAllByCourseCourseId(anyString());
	}
 
	@Test
	void testViewLesson_instructorAccess_success() {
		when(lessonDAO.findById(lessonId)).thenReturn(Optional.of(lesson));
		Lesson result = lessonService.viewLesson(lessonId, instructorId);
		assertNotNull(result);
		assertEquals(lesson, result);
		verify(lessonDAO, times(1)).findById(lessonId);
	}
 
	@Test
	void testViewLesson_lessonNotFound() {
		when(lessonDAO.findById(lessonId)).thenReturn(Optional.empty());
		assertThrows(LessonNotFoundException.class, () -> lessonService.viewLesson(lessonId, instructorId));
		verify(lessonDAO, times(1)).findById(lessonId);
	}
}
 