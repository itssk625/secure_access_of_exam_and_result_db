
package com.secureaccess.exam_access_control.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "exams")
public class Exam {

    @Id
    @Column(name = "exam_id")
    private Long examId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "exam_name", nullable = false)
    private String examName;

    @Column(name = "exam_type", nullable = false)
    private String examType;

    @Column(name = "max_marks", nullable = false)
    private BigDecimal maxMarks;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "status", nullable = false)
    private String status;

    public Exam() {
    }

    public Long getExamId() {
        return examId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getExamName() {
        return examName;
    }

    public String getExamType() {
        return examType;
    }

    public BigDecimal getMaxMarks() {
        return maxMarks;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }
}
