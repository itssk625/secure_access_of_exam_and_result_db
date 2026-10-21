
package com.secureaccess.exam_access_control.repository;

import com.secureaccess.exam_access_control.entity.Marks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MarksRepository extends JpaRepository<Marks, Long> {

    @Query(value = """
        SELECT m.*
        FROM marks m
        JOIN exams e ON e.exam_id = m.exam_id
        WHERE e.course_id = :courseId
        ORDER BY m.mark_id
        """, nativeQuery = true)
    List<Marks> findByCourseId(
            @Param("courseId") Long courseId);
}
