
package com.secureaccess.exam_access_control.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
    name = "results",
    uniqueConstraints = @UniqueConstraint(
        name = "results_student_id_exam_id_key",
        columnNames = {"student_id", "exam_id"}
    )
)
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long resultId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "exam_id", nullable = false)
    private Long examId;

    @Column(name = "total_marks", nullable = false)
    private BigDecimal totalMarks;

    @Column(name = "grade")
    private String grade;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approved_at")
    private Instant approvedAt;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "integrity_value")
    private String integrityValue;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    public Result() {
    }

    public Long getResultId() {
        return resultId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public Long getExamId() {
        return examId;
    }

    public BigDecimal getTotalMarks() {
        return totalMarks;
    }

    public String getGrade() {
        return grade;
    }

    public String getStatus() {
        return status;
    }

    public Long getApprovedBy() {
        return approvedBy;
    }

    public Instant getApprovedAt() {
        return approvedAt;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public String getIntegrityValue() {
        return integrityValue;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }
}
