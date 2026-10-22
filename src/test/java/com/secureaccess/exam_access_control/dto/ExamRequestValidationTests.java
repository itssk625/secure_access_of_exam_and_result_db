package com.secureaccess.exam_access_control.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;

class ExamRequestValidationTests {
    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void closeValidatorFactory() {
        factory.close();
    }

    @Test
    void acceptsValidExamRequest() {
        ExamRequest request = new ExamRequest(
                1L, "Midsem", "MIDSEM", new BigDecimal("30.00"), null, null);
        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void rejectsMissingOrInvalidRequiredFields() {
        ExamRequest request = new ExamRequest(
                0L, "  ", "", BigDecimal.ZERO, null, null);
        Set<ConstraintViolation<ExamRequest>> violations = validator.validate(request);
        assertEquals(4, violations.size());
    }

    @Test
    void rejectsNegativeCourseIdAndNonPositiveMaximumMarks() {
        ExamRequest request = new ExamRequest(
                -1L, "Final", "ENDSEM", new BigDecimal("-1"), null, null);
        Set<ConstraintViolation<ExamRequest>> violations = validator.validate(request);
        assertEquals(2, violations.size());
    }

    @Test
    void rejectsMissingRequiredValues() {
        ExamRequest request = new ExamRequest(
                null, null, null, null, null, null);
        Set<ConstraintViolation<ExamRequest>> violations = validator.validate(request);
        assertEquals(4, violations.size());
    }

    @Test
    void rejectsMaximumMarksBelowOneHundredth() {
        ExamRequest request = new ExamRequest(
                1L, "Quiz", "QUIZ", new BigDecimal("0.009"), null, null);
        assertTrue(validator.validate(request).stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("maxMarks")));
    }
}
