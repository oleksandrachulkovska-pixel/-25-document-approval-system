package com.coursework.service;

public class ApprovalStatusTransition {

    public static boolean isAllowed(
            ApprovalStatus current,
            ApprovalStatus next) {

        if (current == null || next == null) {
            return false;
        }

        return switch (current) {
            case DRAFT -> next == ApprovalStatus.SUBMITTED;
            case SUBMITTED -> next == ApprovalStatus.UNDER_REVIEW;
            case UNDER_REVIEW -> next == ApprovalStatus.APPROVED;
            case APPROVED -> false;
        };
    }

    public static void validate(
            ApprovalStatus current,
            ApprovalStatus next) {

        if (!isAllowed(current, next)) {
            throw new IllegalStateException(
                    "Недозволений перехід зі статусу "
                            + current + " до " + next
            );
        }
    }
}