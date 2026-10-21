
package com.secureaccess.exam_access_control.dto;

import com.secureaccess.exam_access_control.entity.Marks;
import java.math.BigDecimal;

public record MarkResponse(
        Long markId,
        Long studentId,
        Long examId,
        BigDecimal marksObtained,
        String markStatus
) {
    public static MarkResponse from(Marks marks) {
        return new MarkResponse(
                marks.getMarkId(),
                marks.getStudentId(),
                marks.getExamId(),
                marks.getMarksObtained(),
                marks.getMarkStatus()
        );
    }
}
