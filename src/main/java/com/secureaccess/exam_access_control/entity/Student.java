
package com.secureaccess.exam_access_control.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "roll_no", nullable = false)
    private String rollNo;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "year", nullable = false)
    private Short year;

    @Column(name = "section")
    private String section;

    public Student() {
    }

    public Long getStudentId() {
        return studentId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getRollNo() {
        return rollNo;
    }

    public String getName() {
        return name;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public Short getYear() {
        return year;
    }

    public String getSection() {
        return section;
    }
}
