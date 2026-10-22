package com.secureaccess.exam_access_control.dto;

import com.secureaccess.exam_access_control.entity.Exam;
import java.math.BigDecimal;
import java.time.Instant;

public record ExamResponse(
    Long examId,
    Long courseId,
    String examName,
    String examType,
    BigDecimal maxMarks,
    Instant startTime,
    Instant endTime,
    String status
) {
    public static ExamResponse from(Exam exam) {
        return new ExamResponse(
            exam.getExamId(),
            exam.getCourseId(),
            exam.getExamName(),
            exam.getExamType(),
            exam.getMaxMarks(),
            exam.getStartTime(),
            exam.getEndTime(),
            exam.getStatus()
        );
    }
}