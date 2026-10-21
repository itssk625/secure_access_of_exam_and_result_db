
package com.secureaccess.exam_access_control.repository;

import com.secureaccess.exam_access_control.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(value = """
        SELECT c.*
        FROM courses c
        JOIN faculty_courses fc ON fc.course_id = c.course_id
        WHERE fc.faculty_id = :facultyId
        ORDER BY c.course_code
        """, nativeQuery = true)
    List<Course> findAssignedCourses(
            @Param("facultyId") Long facultyId);
}
