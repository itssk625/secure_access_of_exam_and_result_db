package com.secureaccess.exam_access_control.config;

import com.secureaccess.exam_access_control.controller.AuthTestController;
import com.secureaccess.exam_access_control.repository.CourseRepository;
import com.secureaccess.exam_access_control.repository.ExamRepository;
import com.secureaccess.exam_access_control.repository.FacultyRepository;
import com.secureaccess.exam_access_control.repository.MarksRepository;
import com.secureaccess.exam_access_control.repository.ResultRepository;
import com.secureaccess.exam_access_control.repository.StudentRepository;
import com.secureaccess.exam_access_control.repository.UserRepository;
import com.secureaccess.exam_access_control.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthTestController.class)
@Import(SecurityConfig.class)
class SecurityConfigTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private UserRepository userRepository;
    @MockitoBean private StudentRepository studentRepository;
    @MockitoBean private ResultRepository resultRepository;
    @MockitoBean private FacultyRepository facultyRepository;
    @MockitoBean private CourseRepository courseRepository;
    @MockitoBean private MarksRepository marksRepository;
    @MockitoBean private ExamRepository examRepository;
    @MockitoBean private CustomUserDetailsService customUserDetailsService;

    @Test
    void currentUserEndpointRejectsUnauthenticatedRequest() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testEndpointNamespaceIsNotPublic() throws Exception {
        mockMvc.perform(get("/api/test/me"))
                .andExpect(status().isUnauthorized());
    }
}
