package com.secureaccess.exam_access_control.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Instant;

public record ExamRequest(
    @NotNull @Positive Long courseId,
    @NotBlank String examName,
    @NotBlank String examType,
    @NotNull @DecimalMin("0.01") BigDecimal maxMarks,
    Instant startTime,
    Instant endTime
) {}