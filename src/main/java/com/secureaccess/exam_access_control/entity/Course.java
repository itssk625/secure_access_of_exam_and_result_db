
package com.secureaccess.exam_access_control.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "course_code", nullable = false)
    private String courseCode;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "credits", nullable = false)
    private BigDecimal credits;

    public Course() {
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public BigDecimal getCredits() {
        return credits;
    }
}
