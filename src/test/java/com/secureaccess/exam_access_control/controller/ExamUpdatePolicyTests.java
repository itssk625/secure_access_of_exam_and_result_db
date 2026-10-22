package com.secureaccess.exam_access_control.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.secureaccess.exam_access_control.dto.ExamRequest;
import com.secureaccess.exam_access_control.dto.ExamResponse;
import com.secureaccess.exam_access_control.entity.Exam;
import com.secureaccess.exam_access_control.repository.CourseRepository;
import com.secureaccess.exam_access_control.repository.ExamRepository;
import com.secureaccess.exam_access_control.repository.FacultyRepository;
import com.secureaccess.exam_access_control.repository.MarksRepository;
import com.secureaccess.exam_access_control.repository.ResultRepository;
import com.secureaccess.exam_access_control.repository.StudentRepository;
import com.secureaccess.exam_access_control.repository.UserRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.mock;

class ExamUpdatePolicyTests {
    private CourseRepository courseRepository;
    private ExamRepository examRepository;
    private AuthTestController controller;

    @BeforeEach
    void setUp() {
        UserRepository userRepository = mock(UserRepository.class);
        StudentRepository studentRepository = mock(StudentRepository.class);
        ResultRepository resultRepository = mock(ResultRepository.class);
        FacultyRepository facultyRepository = mock(FacultyRepository.class);
        courseRepository = mock(CourseRepository.class);
        MarksRepository marksRepository = mock(MarksRepository.class);
        examRepository = mock(ExamRepository.class);
        controller = new AuthTestController(userRepository, studentRepository,
                resultRepository, facultyRepository, courseRepository,
                marksRepository, examRepository);
    }

    @Test
    void plannedExamCanBeReassignedToAnotherExistingCourse() {
        Exam exam = plannedExam(1L);
        when(examRepository.findById(10L)).thenReturn(Optional.of(exam));
        when(courseRepository.existsById(2L)).thenReturn(true);
        when(examRepository.save(any(Exam.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ExamResponse response = controller.updateExam(10L, requestForCourse(2L));

        assertEquals(2L, response.courseId());
        assertEquals("PLANNED", response.status());
        verify(courseRepository).existsById(2L);
        verify(examRepository).save(exam);
    }

    @Test
    void rejectsReassignmentToNonexistentCourse() {
        Exam exam = plannedExam(1L);
        when(examRepository.findById(10L)).thenReturn(Optional.of(exam));
        when(courseRepository.existsById(999L)).thenReturn(false);

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> controller.updateExam(10L, requestForCourse(999L)));

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        assertEquals(1L, exam.getCourseId());
        verify(examRepository, never()).save(any(Exam.class));
    }

    @Test
    void doesNotAllowReassignmentWhenExamIsNotPlanned() {
        Exam exam = plannedExam(1L);
        exam.setStatus("SUBMITTED");
        when(examRepository.findById(10L)).thenReturn(Optional.of(exam));

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> controller.updateExam(10L, requestForCourse(2L)));

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(courseRepository, never()).existsById(2L);
        verify(examRepository, never()).save(any(Exam.class));
    }

    private static Exam plannedExam(Long courseId) {
        Exam exam = new Exam();
        exam.setCourseId(courseId);
        exam.setExamName("Midsem");
        exam.setExamType("MIDSEM");
        exam.setMaxMarks(new BigDecimal("30.00"));
        exam.setStartTime(Instant.parse("2026-10-23T09:00:00Z"));
        exam.setEndTime(Instant.parse("2026-10-23T10:00:00Z"));
        exam.setStatus("PLANNED");
        return exam;
    }

    private static ExamRequest requestForCourse(Long courseId) {
        return new ExamRequest(courseId, "Midsem Updated", "MIDSEM",
                new BigDecimal("40.00"),
                Instant.parse("2026-10-23T09:00:00Z"),
                Instant.parse("2026-10-23T10:00:00Z"));
    }
}
