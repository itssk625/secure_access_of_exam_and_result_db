package com.secureaccess.exam_access_control.dto;

import com.secureaccess.exam_access_control.entity.Course;
import java.math.BigDecimal;

public record CourseResponse(
        Long courseId,
        String courseCode,
        String courseName,
        Long departmentId,
        BigDecimal credits
) {
    public static CourseResponse from(Course course) {
        return new CourseResponse(
                course.getCourseId(),
                course.getCourseCode(),
                course.getCourseName(),
                course.getDepartmentId(),
                course.getCredits()
        );
    }
}