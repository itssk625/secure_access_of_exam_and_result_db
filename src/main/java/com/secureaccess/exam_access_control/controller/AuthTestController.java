package com.secureaccess.exam_access_control.controller;

import com.secureaccess.exam_access_control.dto.CourseResponse;
import com.secureaccess.exam_access_control.dto.ExamRequest;
import com.secureaccess.exam_access_control.dto.ExamResponse;
import com.secureaccess.exam_access_control.dto.MarkResponse;
import com.secureaccess.exam_access_control.dto.ResultResponse;
import com.secureaccess.exam_access_control.dto.StudentProfileResponse;

import com.secureaccess.exam_access_control.entity.Course;
import com.secureaccess.exam_access_control.entity.Exam;
import com.secureaccess.exam_access_control.entity.Faculty;
import com.secureaccess.exam_access_control.entity.Marks;
import com.secureaccess.exam_access_control.entity.Result;
import com.secureaccess.exam_access_control.entity.Student;
import com.secureaccess.exam_access_control.entity.User;

import com.secureaccess.exam_access_control.repository.CourseRepository;
import com.secureaccess.exam_access_control.repository.ExamRepository;
import com.secureaccess.exam_access_control.repository.FacultyRepository;
import com.secureaccess.exam_access_control.repository.MarksRepository;
import com.secureaccess.exam_access_control.repository.ResultRepository;
import com.secureaccess.exam_access_control.repository.StudentRepository;
import com.secureaccess.exam_access_control.repository.UserRepository;

import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthTestController {
    private final FacultyRepository facultyRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ResultRepository resultRepository;
    private final MarksRepository marksRepository;
    private final ExamRepository examRepository;
    public AuthTestController(
            UserRepository userRepository,
            StudentRepository studentRepository,
            ResultRepository resultRepository,
            FacultyRepository facultyRepository,
            CourseRepository courseRepository,
            MarksRepository marksRepository,
            ExamRepository examRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.resultRepository = resultRepository;
        this.facultyRepository = facultyRepository;
        this.courseRepository = courseRepository;
        this.marksRepository = marksRepository;
        this.examRepository = examRepository;
    }
    
@GetMapping("/api/faculty/{id}/courses")
@PreAuthorize("hasAuthority('VIEW_COURSE_MARKS')")
public List<CourseResponse> getFacultyCourses(
        @PathVariable Long id,
        Authentication authentication) {

    User user = userRepository.findByUsername(authentication.getName())
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND));

    Faculty faculty = facultyRepository.findByUserId(user.getUserId())
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND));

    if (!faculty.getFacultyId().equals(id)) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    return courseRepository.findAssignedCourses(id)
            .stream()
            .map(CourseResponse::from)
            .toList();
}

    @GetMapping("/api/auth/me")
    public Map<String, Object> currentUser(Authentication authentication) {
        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .toList();

        return Map.of(
                "username", authentication.getName(),
                "authorities", authorities
        );
    }

    @GetMapping("/api/results/{resultId}")
    @PreAuthorize("hasAuthority('VIEW_OWN_RESULT')")
    public ResultResponse getOwnResult(
            @PathVariable Long resultId,
            Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));

        Student student = studentRepository.findByUserId(user.getUserId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));

        Result result = resultRepository.findById(resultId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Return the same response for a missing result and a result owned by
        // another student, so result IDs cannot be enumerated through status codes.
        if (!result.getStudentId().equals(student.getStudentId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (!"PUBLISHED".equalsIgnoreCase(result.getStatus())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return ResultResponse.from(result);
    }
    @GetMapping("/api/students/{id}/results")
@PreAuthorize("hasAuthority('VIEW_OWN_RESULT')")
public List<ResultResponse> getStudentResults(
        @PathVariable Long id,
        Authentication authentication) {

    User user = userRepository.findByUsername(authentication.getName())
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND));

    Student student = studentRepository.findByUserId(user.getUserId())
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND));

    if (!student.getStudentId().equals(id)) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    return resultRepository.findByStudentIdAndStatusIgnoreCase(id, "PUBLISHED")
        .stream()
        .map(ResultResponse::from)
        .toList();
}
@GetMapping("/api/students/{id}")
@PreAuthorize("hasAuthority('VIEW_OWN_PROFILE')")
public StudentProfileResponse getStudentProfile(
        @PathVariable Long id,
        Authentication authentication) {

    User user = userRepository.findByUsername(authentication.getName())
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND));

    Student student = studentRepository.findByUserId(user.getUserId())
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND));

    if (!student.getStudentId().equals(id)) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    return StudentProfileResponse.from(student);
}
@GetMapping("/api/students/{id}/courses")
@PreAuthorize("hasAuthority('VIEW_OWN_COURSES')")
public List<CourseResponse> getStudentCourses(
        @PathVariable Long id,
        Authentication authentication) {

    User user = userRepository.findByUsername(authentication.getName())
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND));

    Student student = studentRepository.findByUserId(user.getUserId())
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND));

    if (!student.getStudentId().equals(id)) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    return resultRepository.findCoursesByStudentId(id)
            .stream()
            .map(CourseResponse::from)
            .toList();
}

@GetMapping("/api/courses/{id}/marks")
@PreAuthorize("hasAuthority('VIEW_COURSE_MARKS')")
public List<MarkResponse> getCourseMarks(
        @PathVariable Long id,
        Authentication authentication) {

    User user = userRepository.findByUsername(authentication.getName())
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND));

    Faculty faculty = facultyRepository.findByUserId(user.getUserId())
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.FORBIDDEN));

    List<Course> assignedCourses =
            courseRepository.findAssignedCourses(faculty.getFacultyId());

    boolean assigned = assignedCourses.stream()
            .anyMatch(course -> course.getCourseId().equals(id));

    if (!assigned) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    return marksRepository.findByCourseId(id)
            .stream()
            .map(MarkResponse::from)
            .toList();
}
@PostMapping("/api/exams")
@PreAuthorize("hasAuthority('CREATE_EXAM')")
public ResponseEntity<ExamResponse> createExam(
        @Valid @RequestBody ExamRequest request) {

    validateExamTimes(request.startTime(), request.endTime());
    ensureCourseExists(request.courseId());

    Exam exam = new Exam();
    exam.setCourseId(request.courseId());
    exam.setExamName(request.examName().trim());
    exam.setExamType(request.examType().trim());
    exam.setMaxMarks(request.maxMarks());
    exam.setStartTime(request.startTime());
    exam.setEndTime(request.endTime());
    exam.setStatus("PLANNED");

    Exam saved = examRepository.save(exam);

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(ExamResponse.from(saved));
}
@PutMapping("/api/exams/{id}")
@PreAuthorize("hasAuthority('MANAGE_EXAM')")
public ExamResponse updateExam(
        @PathVariable Long id,
        @Valid @RequestBody ExamRequest request) {

    validateExamTimes(request.startTime(), request.endTime());

    Exam exam = examRepository.findById(id)
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND));

    if (!"PLANNED".equals(exam.getStatus())) {
    throw new ResponseStatusException(
            HttpStatus.CONFLICT,
            "Only planned exams can be edited"
    );
}

ensureCourseExists(request.courseId());

    exam.setCourseId(request.courseId());
    exam.setExamName(request.examName().trim());
    exam.setExamType(request.examType().trim());
    exam.setMaxMarks(request.maxMarks());
    exam.setStartTime(request.startTime());
    exam.setEndTime(request.endTime());

    return ExamResponse.from(examRepository.save(exam));
}
// Interim read policy: any authenticated user may view exam metadata.
// The approved permission catalogue does not define a VIEW_EXAM permission,
// so do not invent one here. Revisit if the team approves a narrower policy.
@GetMapping("/api/exams/{id}")
@PreAuthorize("isAuthenticated()")
public ExamResponse getExam(@PathVariable Long id) {
    Exam exam = examRepository.findById(id)
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND));

    return ExamResponse.from(exam);
}
private void ensureCourseExists(Long courseId) {
    if (!courseRepository.existsById(courseId)) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Course does not exist"
        );
    }
}

static void validateExamTimes(Instant start, Instant end) {
    if (start != null && end != null && !end.isAfter(start)) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Exam end time must be after start time"
        );
    }
}
}
