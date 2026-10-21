package com.secureaccess.exam_access_control.repository;
import com.secureaccess.exam_access_control.entity.Course;
import com.secureaccess.exam_access_control.entity.Exam;
import com.secureaccess.exam_access_control.entity.Course;
import com.secureaccess.exam_access_control.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findByStudentId(Long studentId);
    @Query("""
    SELECT DISTINCT c
    FROM Result r, Exam e, Course c
    WHERE r.studentId = :studentId
      AND r.examId = e.examId
      AND e.courseId = c.courseId
    """)
List<Course> findCoursesByStudentId(
        @Param("studentId") Long studentId);

    @Query("""
        SELECT DISTINCT e.courseId
        FROM Result r, Exam e
        WHERE r.studentId = :studentId
          AND r.examId = e.examId
        """)
    List<Long> findCourseIdsByStudentId(
            @Param("studentId") Long studentId);
}