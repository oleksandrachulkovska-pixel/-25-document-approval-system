package com.coursework.service;

import com.coursework.ApprovalRequest;
import com.coursework.dao.ApprovalRequestDAO;
import com.coursework.util.AppLogger;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Logger;

public class ApprovalLifecycleService {

    private static final Logger LOGGER = AppLogger.getLogger();

    private final ApprovalRequestDAO approvalRequestDAO =
            new ApprovalRequestDAO();

    // Перехід DRAFT -> SUBMITTED
    public void submit(Long requestId) throws SQLException {
        changeStatus(
                requestId,
                ApprovalStatus.DRAFT,
                ApprovalStatus.SUBMITTED
        );
    }

    // Перехід SUBMITTED -> UNDER_REVIEW
    public void startReview(Long requestId) throws SQLException {
        changeStatus(
                requestId,
                ApprovalStatus.SUBMITTED,
                ApprovalStatus.UNDER_REVIEW
        );
    }

    // Перехід UNDER_REVIEW -> APPROVED
    public void approve(Long requestId) throws SQLException {
        changeStatus(
                requestId,
                ApprovalStatus.UNDER_REVIEW,
                ApprovalStatus.APPROVED
        );
    }

    // Перевірка та виконання переходу між статусами
    private void changeStatus(
            Long requestId,
            ApprovalStatus expectedStatus,
            ApprovalStatus newStatus
    ) throws SQLException {

        if (requestId == null || requestId <= 0) {
            throw new IllegalArgumentException(
                    "Некоректний ідентифікатор запиту."
            );
        }

        ApprovalRequest request =
                approvalRequestDAO.findById(requestId);

        if (request == null) {
            throw new IllegalArgumentException(
                    "Запит на погодження не знайдено."
            );
        }

        ApprovalStatus currentStatus;

        try {
            currentStatus = ApprovalStatus.valueOf(
                    request.getStatus()
            );
        } catch (IllegalArgumentException | NullPointerException e) {
            LOGGER.severe(
                    "Запит містить невідомий статус. ID: " + requestId
            );

            throw new IllegalStateException(
                    "Запит містить невідомий статус."
            );
        }

        if (currentStatus != expectedStatus) {
            LOGGER.warning(
                    "Заборонений перехід статусу для запиту ID: "
                            + requestId
                            + ". Поточний статус: "
                            + currentStatus
                            + ", очікуваний: "
                            + expectedStatus
            );

            throw new IllegalStateException(
                    "Недозволений перехід зі статусу "
                            + currentStatus + " до " + newStatus
            );
        }

        request.setStatus(newStatus.name());

        // Фіксація дати подання
        if (newStatus == ApprovalStatus.SUBMITTED) {
            request.setSubmittedAt(LocalDateTime.now());
        }

        // Фіксація дати схвалення
        if (newStatus == ApprovalStatus.APPROVED) {
            request.setReviewedAt(LocalDateTime.now());
        }

        approvalRequestDAO.update(request);

        LOGGER.info(
                "Успішний перехід статусу запиту ID: "
                        + requestId
                        + " з "
                        + currentStatus
                        + " до "
                        + newStatus
        );
    }
}