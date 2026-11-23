package com.coursework.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApprovalStatusTransitionTest {

    @Test
    void shouldAllowDraftToSubmitted() {
        assertTrue(ApprovalStatusTransition.isAllowed(
                ApprovalStatus.DRAFT,
                ApprovalStatus.SUBMITTED));
    }

    @Test
    void shouldAllowSubmittedToUnderReview() {
        assertTrue(ApprovalStatusTransition.isAllowed(
                ApprovalStatus.SUBMITTED,
                ApprovalStatus.UNDER_REVIEW));
    }

    @Test
    void shouldAllowUnderReviewToApproved() {
        assertTrue(ApprovalStatusTransition.isAllowed(
                ApprovalStatus.UNDER_REVIEW,
                ApprovalStatus.APPROVED));
    }

    @Test
    void shouldRejectDraftToApproved() {
        assertFalse(ApprovalStatusTransition.isAllowed(
                ApprovalStatus.DRAFT,
                ApprovalStatus.APPROVED));
    }

    @Test
    void shouldRejectApprovedToDraft() {
        assertFalse(ApprovalStatusTransition.isAllowed(
                ApprovalStatus.APPROVED,
                ApprovalStatus.DRAFT));
    }

    @Test
    void shouldRejectNullStatuses() {
        assertFalse(ApprovalStatusTransition.isAllowed(null, ApprovalStatus.SUBMITTED));
        assertFalse(ApprovalStatusTransition.isAllowed(ApprovalStatus.DRAFT, null));
    }

    @Test
    void shouldThrowExceptionForInvalidTransition() {
        assertThrows(IllegalStateException.class, () ->
                ApprovalStatusTransition.validate(
                        ApprovalStatus.DRAFT,
                        ApprovalStatus.APPROVED));
    }

    @Test
    void shouldNotAllowSameStatus() {
        assertFalse(ApprovalStatusTransition.isAllowed(
                ApprovalStatus.SUBMITTED,
                ApprovalStatus.SUBMITTED));
    }
}