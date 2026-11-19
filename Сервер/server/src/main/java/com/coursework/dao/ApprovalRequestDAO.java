package com.coursework.dao;

import com.coursework.ApprovalRequest;
import com.coursework.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApprovalRequestDAO {

    // Створення запиту на погодження
    public void create(ApprovalRequest request) throws SQLException {
        String sql = """
                INSERT INTO approval_requests
                (document_id, reviewer_id, status, submitted_at,
                 reviewed_at, f1, f2, f3, integral_index)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, request.getDocumentId());
            statement.setLong(2, request.getReviewerId());
            statement.setString(3, request.getStatus());

            if (request.getSubmittedAt() == null) {
                statement.setNull(4, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(4, Timestamp.valueOf(request.getSubmittedAt()));
            }

            if (request.getReviewedAt() == null) {
                statement.setNull(5, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(5, Timestamp.valueOf(request.getReviewedAt()));
            }

            statement.setDouble(6, request.getF1());
            statement.setDouble(7, request.getF2());
            statement.setDouble(8, request.getF3());
            statement.setDouble(9, request.getIntegralIndex());

            statement.executeUpdate();
        }
    }

    // Пошук запиту за ID
    public ApprovalRequest findById(Long id) throws SQLException {
        String sql = "SELECT * FROM approval_requests WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapApprovalRequest(result);
                }
            }
        }

        return null;
    }

    // Отримання всіх запитів
    public List<ApprovalRequest> findAll() throws SQLException {
        String sql = "SELECT * FROM approval_requests";
        List<ApprovalRequest> requests = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                requests.add(mapApprovalRequest(result));
            }
        }

        return requests;
    }

    // Оновлення запиту
    public void update(ApprovalRequest request) throws SQLException {
        String sql = """
                UPDATE approval_requests
                SET document_id = ?, reviewer_id = ?, status = ?,
                    submitted_at = ?, reviewed_at = ?,
                    f1 = ?, f2 = ?, f3 = ?, integral_index = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, request.getDocumentId());
            statement.setLong(2, request.getReviewerId());
            statement.setString(3, request.getStatus());

            if (request.getSubmittedAt() == null) {
                statement.setNull(4, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(4, Timestamp.valueOf(request.getSubmittedAt()));
            }

            if (request.getReviewedAt() == null) {
                statement.setNull(5, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(5, Timestamp.valueOf(request.getReviewedAt()));
            }

            statement.setDouble(6, request.getF1());
            statement.setDouble(7, request.getF2());
            statement.setDouble(8, request.getF3());
            statement.setDouble(9, request.getIntegralIndex());
            statement.setLong(10, request.getId());

            statement.executeUpdate();
        }
    }

    // Видалення запиту за ID
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM approval_requests WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    // Перетворення результату SQL-запиту на Java-об'єкт
    private ApprovalRequest mapApprovalRequest(ResultSet result) throws SQLException {
        Timestamp submitted = result.getTimestamp("submitted_at");
        Timestamp reviewed = result.getTimestamp("reviewed_at");

        return new ApprovalRequest(
                result.getLong("id"),
                result.getLong("document_id"),
                result.getLong("reviewer_id"),
                result.getString("status"),
                submitted == null ? null : submitted.toLocalDateTime(),
                reviewed == null ? null : reviewed.toLocalDateTime(),
                result.getDouble("f1"),
                result.getDouble("f2"),
                result.getDouble("f3"),
                result.getDouble("integral_index")
        );
    }
}