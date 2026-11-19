package com.coursework.service;

import com.coursework.ApprovalRequest;
import com.coursework.dao.ApprovalRequestDAO;
import com.coursework.dao.DocumentDAO;
import com.coursework.dao.ReviewerDAO;
import com.coursework.util.AppLogger;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ApprovalRequestService {

    private static final Logger LOGGER = AppLogger.getLogger();

    private final ApprovalRequestDAO approvalRequestDAO =
            new ApprovalRequestDAO();

    private final DocumentDAO documentDAO =
            new DocumentDAO();

    private final ReviewerDAO reviewerDAO =
            new ReviewerDAO();

    private final PriorityCalculationService calculationService =
            new PriorityCalculationService();

    // Створення запиту на погодження
    public void createRequest(ApprovalRequest request)
            throws SQLException {

        validateRequest(request);

        if (!documentDAO.existsById(request.getDocumentId())) {
            throw new IllegalArgumentException(
                    "Документ із таким ID не існує."
            );
        }

        if (!reviewerDAO.existsById(request.getReviewerId())) {
            throw new IllegalArgumentException(
                    "Рецензент із таким ID не існує."
            );
        }

        if (!ApprovalStatus.DRAFT.name().equals(request.getStatus())) {
            throw new IllegalArgumentException(
                    "Новий запит повинен мати статус DRAFT."
            );
        }

        double integralIndex = calculationService.calculate(
                request.getF1(),
                request.getF2(),
                request.getF3()
        );

        request.setIntegralIndex(integralIndex);

        approvalRequestDAO.create(request);

        LOGGER.info(
                "Створено запит на погодження. ID документа: "
                        + request.getDocumentId()
        );
    }

    // Отримання запиту за ID
    public ApprovalRequest getRequestById(Long id)
            throws SQLException {

        validateId(id);

        ApprovalRequest request = approvalRequestDAO.findById(id);

        if (request == null) {
            LOGGER.warning("Запит не знайдено. ID: " + id);
        }

        return request;
    }

    // Отримання всіх запитів
    public List<ApprovalRequest> getAllRequests()
            throws SQLException {

        List<ApprovalRequest> requests =
                approvalRequestDAO.findAll();

        LOGGER.info(
                "Отримано список запитів. Кількість: "
                        + requests.size()
        );

        return requests;
    }

    // Оновлення запиту
    public void updateRequest(ApprovalRequest request)
            throws SQLException {

        validateRequest(request);
        validateId(request.getId());

        if (!documentDAO.existsById(request.getDocumentId())) {
            throw new IllegalArgumentException(
                    "Документ із таким ID не існує."
            );
        }

        if (!reviewerDAO.existsById(request.getReviewerId())) {
            throw new IllegalArgumentException(
                    "Рецензент із таким ID не існує."
            );
        }

        ApprovalRequest existing =
                approvalRequestDAO.findById(request.getId());

        if (existing == null) {
            throw new IllegalArgumentException(
                    "Запит на погодження не знайдено."
            );
        }

        if (!existing.getStatus().equals(request.getStatus())) {
            throw new IllegalArgumentException(
                    "Змінювати статус можна лише через ApprovalLifecycleService."
            );
        }

        double integralIndex = calculationService.calculate(
                request.getF1(),
                request.getF2(),
                request.getF3()
        );

        request.setIntegralIndex(integralIndex);

        approvalRequestDAO.update(request);

        LOGGER.info(
                "Оновлено запит на погодження. ID: "
                        + request.getId()
        );
    }

    // Видалення запиту
    public void deleteRequest(Long id) throws SQLException {

        validateId(id);

        approvalRequestDAO.delete(id);

        LOGGER.info(
                "Видалено запит на погодження. ID: " + id
        );
    }

    // Перевірка даних запиту
    private void validateRequest(ApprovalRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Запит не може бути порожнім."
            );
        }

        if (request.getDocumentId() == null ||
                request.getDocumentId() <= 0) {

            throw new IllegalArgumentException(
                    "Некоректний ID документа."
            );
        }

        if (request.getReviewerId() == null ||
                request.getReviewerId() <= 0) {

            throw new IllegalArgumentException(
                    "Некоректний ID рецензента."
            );
        }

        if (request.getStatus() == null) {
            throw new IllegalArgumentException(
                    "Статус запиту обов'язковий."
            );
        }

        try {
            ApprovalStatus.valueOf(request.getStatus());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Недопустимий статус запиту."
            );
        }

        validateFactor(request.getF1(), "F1");
        validateFactor(request.getF2(), "F2");
        validateFactor(request.getF3(), "F3");
    }

    // Перевірка факторів інтегрального показника
    private void validateFactor(double value, String name) {

        if (!Double.isFinite(value) ||
                value < 0 ||
                value > 100) {

            throw new IllegalArgumentException(
                    name + " повинен бути в межах від 0 до 100."
            );
        }
    }

    // Перевірка ID
    private void validateId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Некоректний ID запиту."
            );
        }
    }
}