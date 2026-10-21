
package com.secureaccess.exam_access_control.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "faculty")
public class Faculty {

    @Id
    @Column(name = "faculty_id")
    private Long facultyId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "designation")
    private String designation;

    public Faculty() {
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public String getDesignation() {
        return designation;
    }
}
