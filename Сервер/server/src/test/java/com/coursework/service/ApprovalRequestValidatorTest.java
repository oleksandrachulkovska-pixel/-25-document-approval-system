package com.coursework.service;

import com.coursework.ApprovalRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApprovalRequestValidatorTest {

    private ApprovalRequest createValidRequest() {
        ApprovalRequest request = new ApprovalRequest();
        request.setDocumentId(1L);
        request.setReviewerId(1L);
        request.setStatus("DRAFT");
        request.setF1(80);
        request.setF2(60);
        request.setF3(40);
        return request;
    }

    @Test
    void shouldAcceptValidRequest() {
        assertDoesNotThrow(() ->
                ApprovalRequestValidator.validate(createValidRequest()));
    }

    @Test
    void shouldRejectNullRequest() {
        assertThrows(IllegalArgumentException.class, () ->
                ApprovalRequestValidator.validate(null));
    }

    @Test
    void shouldRejectInvalidDocumentId() {
        ApprovalRequest request = createValidRequest();
        request.setDocumentId(0L);

        assertThrows(IllegalArgumentException.class, () ->
                ApprovalRequestValidator.validate(request));
    }

    @Test
    void shouldRejectInvalidReviewerId() {
        ApprovalRequest request = createValidRequest();
        request.setReviewerId(-1L);

        assertThrows(IllegalArgumentException.class, () ->
                ApprovalRequestValidator.validate(request));
    }

    @Test
    void shouldRejectNullStatus() {
        ApprovalRequest request = createValidRequest();
        request.setStatus(null);

        assertThrows(IllegalArgumentException.class, () ->
                ApprovalRequestValidator.validate(request));
    }

    @Test
    void shouldRejectUnknownStatus() {
        ApprovalRequest request = createValidRequest();
        request.setStatus("UNKNOWN");

        assertThrows(IllegalArgumentException.class, () ->
                ApprovalRequestValidator.validate(request));
    }

    @Test
    void shouldRejectFactorBelowZero() {
        ApprovalRequest request = createValidRequest();
        request.setF1(-1);

        assertThrows(IllegalArgumentException.class, () ->
                ApprovalRequestValidator.validate(request));
    }

    @Test
    void shouldRejectFactorAbove100() {
        ApprovalRequest request = createValidRequest();
        request.setF2(101);

        assertThrows(IllegalArgumentException.class, () ->
                ApprovalRequestValidator.validate(request));
    }

    @Test
    void shouldRejectNaNFactor() {
        ApprovalRequest request = createValidRequest();
        request.setF3(Double.NaN);

        assertThrows(IllegalArgumentException.class, () ->
                ApprovalRequestValidator.validate(request));
    }

    @Test
    void shouldRejectInfiniteFactor() {
        ApprovalRequest request = createValidRequest();
        request.setF1(Double.POSITIVE_INFINITY);

        assertThrows(IllegalArgumentException.class, () ->
                ApprovalRequestValidator.validate(request));
    }
}