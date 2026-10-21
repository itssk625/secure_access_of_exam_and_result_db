
package com.secureaccess.exam_access_control.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "marks")
public class Marks {

    @Id
    @Column(name = "mark_id")
    private Long markId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "exam_id", nullable = false)
    private Long examId;

    @Column(name = "marks_obtained", nullable = false)
    private BigDecimal marksObtained;

    @Column(name = "entered_by", nullable = false)
    private Long enteredBy;

    @Column(name = "entered_at", nullable = false)
    private Instant enteredAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "mark_status", nullable = false)
    private String markStatus;

    public Marks() {
    }

    public Long getMarkId() { return markId; }
    public Long getStudentId() { return studentId; }
    public Long getExamId() { return examId; }
    public BigDecimal getMarksObtained() { return marksObtained; }
    public Long getEnteredBy() { return enteredBy; }
    public Instant getEnteredAt() { return enteredAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public String getMarkStatus() { return markStatus; }
}
