package com.coursework.service;

/**
 * Статуси життєвого циклу запиту на погодження документа.
 */
public enum ApprovalStatus {

    DRAFT,         // Чернетка
    SUBMITTED,     // Подано
    UNDER_REVIEW,  // На розгляді
    APPROVED       // Схвалено
}