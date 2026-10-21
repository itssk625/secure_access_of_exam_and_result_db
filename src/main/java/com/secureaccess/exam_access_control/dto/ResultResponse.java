
package com.secureaccess.exam_access_control.dto;

import com.secureaccess.exam_access_control.entity.Result;

import java.math.BigDecimal;

public record ResultResponse(
        Long resultId,
        Long examId,
        BigDecimal totalMarks,
        String grade,
        String status
) {
    public static ResultResponse from(Result result) {
        return new ResultResponse(
                result.getResultId(),
                result.getExamId(),
                result.getTotalMarks(),
                result.getGrade(),
                result.getStatus()
        );
    }
}
