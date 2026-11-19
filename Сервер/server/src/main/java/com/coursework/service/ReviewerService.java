package com.coursework.service;

import com.coursework.Reviewer;
import com.coursework.dao.ReviewerDAO;

import java.sql.SQLException;
import java.util.List;

public class ReviewerService {

    private final ReviewerDAO reviewerDAO;

    public ReviewerService() {
        this.reviewerDAO = new ReviewerDAO();
    }

    // Створення рецензента
    public void createReviewer(Reviewer reviewer) throws SQLException {
        validateReviewer(reviewer);
        reviewerDAO.create(reviewer);
    }

    // Пошук рецензента за ID
    public Reviewer getReviewerById(Long id) throws SQLException {
        validateId(id);
        return reviewerDAO.findById(id);
    }

    // Отримання всіх рецензентів
    public List<Reviewer> getAllReviewers() throws SQLException {
        return reviewerDAO.findAll();
    }

    // Оновлення рецензента
    public void updateReviewer(Reviewer reviewer) throws SQLException {
        if (reviewer == null) {
            throw new IllegalArgumentException("Рецензент не може бути порожнім");
        }

        validateId(reviewer.getId());
        validateReviewer(reviewer);

        reviewerDAO.update(reviewer);
    }

    // Видалення рецензента
    public void deleteReviewer(Long id) throws SQLException {
        validateId(id);
        reviewerDAO.delete(id);
    }

    // Перевірка ID
    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Некоректний ID рецензента");
        }
    }

    // Перевірка даних рецензента
    private void validateReviewer(Reviewer reviewer) {
        if (reviewer == null) {
            throw new IllegalArgumentException("Рецензент не може бути порожнім");
        }

        if (reviewer.getFullName() == null ||
                reviewer.getFullName().isBlank()) {
            throw new IllegalArgumentException("Вкажіть ПІБ рецензента");
        }

        if (reviewer.getEmail() == null ||
                !reviewer.getEmail().matches(
                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Некоректна електронна пошта");
        }

        if (reviewer.getDepartment() == null ||
                reviewer.getDepartment().isBlank()) {
            throw new IllegalArgumentException("Вкажіть підрозділ рецензента");
        }

        if (reviewer.getPosition() == null ||
                reviewer.getPosition().isBlank()) {
            throw new IllegalArgumentException("Вкажіть посаду рецензента");
        }
    }
}