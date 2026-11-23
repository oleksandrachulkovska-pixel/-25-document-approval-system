package com.coursework.service;

import com.coursework.ApprovalRequest;

public class ApprovalRequestValidator {

    public static void validate(ApprovalRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("Запит не може бути порожнім.");
        }

        if (request.getDocumentId() == null || request.getDocumentId() <= 0) {
            throw new IllegalArgumentException("Некоректний ID документа.");
        }

        if (request.getReviewerId() == null || request.getReviewerId() <= 0) {
            throw new IllegalArgumentException("Некоректний ID рецензента.");
        }

        if (request.getStatus() == null) {
            throw new IllegalArgumentException("Статус запиту обов'язковий.");
        }

        try {
            ApprovalStatus.valueOf(request.getStatus());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Недопустимий статус запиту.");
        }

        validateFactor(request.getF1(), "F1");
        validateFactor(request.getF2(), "F2");
        validateFactor(request.getF3(), "F3");
    }

    private static void validateFactor(double value, String name) {

        if (!Double.isFinite(value) || value < 0 || value > 100) {
            throw new IllegalArgumentException(
                    name + " повинен бути в межах від 0 до 100."
            );
        }
    }
}