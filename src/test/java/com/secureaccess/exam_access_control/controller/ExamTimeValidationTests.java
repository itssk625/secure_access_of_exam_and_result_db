package com.secureaccess.exam_access_control.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

class ExamTimeValidationTests {
    @Test
    void allowsMissingOptionalTimes() {
        assertDoesNotThrow(() -> AuthTestController.validateExamTimes(null, null));
        assertDoesNotThrow(() -> AuthTestController.validateExamTimes(
                Instant.parse("2026-10-22T09:00:00Z"), null));
        assertDoesNotThrow(() -> AuthTestController.validateExamTimes(
                null, Instant.parse("2026-10-22T10:00:00Z")));
    }

    @Test
    void acceptsEndAfterStart() {
        assertDoesNotThrow(() -> AuthTestController.validateExamTimes(
                Instant.parse("2026-10-22T09:00:00Z"),
                Instant.parse("2026-10-22T10:00:00Z")));
    }

    @Test
    void rejectsEqualOrEarlierEndTime() {
        ResponseStatusException equal = assertThrows(ResponseStatusException.class,
                () -> AuthTestController.validateExamTimes(
                        Instant.parse("2026-10-22T09:00:00Z"),
                        Instant.parse("2026-10-22T09:00:00Z")));
        assertEquals(400, equal.getStatusCode().value());

        ResponseStatusException earlier = assertThrows(ResponseStatusException.class,
                () -> AuthTestController.validateExamTimes(
                        Instant.parse("2026-10-22T10:00:00Z"),
                        Instant.parse("2026-10-22T09:00:00Z")));
        assertEquals(400, earlier.getStatusCode().value());
    }
}
