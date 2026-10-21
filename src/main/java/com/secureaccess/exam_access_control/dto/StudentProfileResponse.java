
package com.secureaccess.exam_access_control.dto;

import com.secureaccess.exam_access_control.entity.Student;

public record StudentProfileResponse(
        Long studentId,
        String rollNo,
        String name,
        Long departmentId,
        Short year,
        String section
) {
    public static StudentProfileResponse from(Student student) {
        return new StudentProfileResponse(
                student.getStudentId(),
                student.getRollNo(),
                student.getName(),
                student.getDepartmentId(),
                student.getYear(),
                student.getSection()
        );
    }
}
