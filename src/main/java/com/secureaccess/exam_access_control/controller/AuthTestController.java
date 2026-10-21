
package com.secureaccess.exam_access_control.controller;
import com.secureaccess.exam_access_control.dto.CourseResponse;
import com.secureaccess.exam_access_control.dto.ResultResponse;
import com.secureaccess.exam_access_control.entity.Result;
import com.secureaccess.exam_access_control.entity.Student;
import com.secureaccess.exam_access_control.entity.User;
import com.secureaccess.exam_access_control.repository.ResultRepository;
import com.secureaccess.exam_access_control.repository.StudentRepository;
import com.secureaccess.exam_access_control.repository.UserRepository;
import com.secureaccess.exam_access_control.dto.StudentProfileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.secureaccess.exam_access_control.dto.CourseResponse;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;
import java.util.List;
import com.secureaccess.exam_access_control.entity.Course;
import com.secureaccess.exam_access_control.entity.Faculty;
import com.secureaccess.exam_access_control.dto.MarkResponse;
import com.secureaccess.exam_access_control.entity.Marks;
import com.secureaccess.exam_access_control.repository.MarksRepository;
import com.secureaccess.exam_access_control.dto.CourseResponse;
import com.secureaccess.exam_access_control.entity.Course;
import com.secureaccess.exam_access_control.entity.Faculty;
import com.secureaccess.exam_access_control.repository.CourseRepository;
import com.secureaccess.exam_access_control.repository.FacultyRepository;
@RestController
public class AuthTestController {
    private final FacultyRepository facultyRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ResultRepository resultRepository;
    private final MarksRepository marksRepository;
    public AuthTestController(
            UserRepository userRepository,
            StudentRepository studentRepository,
            ResultRepository resultRepository,
            FacultyRepository facultyRepository,
            CourseRepository courseRepository,
            MarksRepository marksRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.resultRepository = resultRepository;
        this.facultyRepository = facultyRepository;
        this.courseRepository = courseRepository;
        this.marksRepository = marksRepository;
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

    @GetMapping({"/api/auth/me", "/api/test/me"})
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

    @GetMapping("/api/test/enter-marks")
    @PreAuthorize("hasAuthority('ENTER_MARKS')")
    public Map<String, String> testEnterMarksPermission() {
        return Map.of(
                "message", "Access granted",
                "requiredPermission", "ENTER_MARKS"
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

        if (!result.getStudentId().equals(student.getStudentId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
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

    return resultRepository.findByStudentId(id)
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

}
