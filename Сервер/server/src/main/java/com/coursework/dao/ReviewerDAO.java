package com.coursework.dao;

import com.coursework.Reviewer;
import com.coursework.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewerDAO {

    // Створення рецензента
    public void create(Reviewer reviewer) throws SQLException {
        String sql = """
                INSERT INTO reviewers
                (full_name, email, department, position)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, reviewer.getFullName());
            statement.setString(2, reviewer.getEmail());
            statement.setString(3, reviewer.getDepartment());
            statement.setString(4, reviewer.getPosition());

            statement.executeUpdate();
        }
    }

    // Отримання рецензента за ID
    public Reviewer findById(Long id) throws SQLException {
        String sql = "SELECT * FROM reviewers WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapReviewer(result);
                }
            }
        }

        return null;
    }

    // Отримання всіх рецензентів
    public List<Reviewer> findAll() throws SQLException {
        List<Reviewer> reviewers = new ArrayList<>();

        String sql = "SELECT * FROM reviewers";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                reviewers.add(mapReviewer(result));
            }
        }

        return reviewers;
    }

    // Оновлення рецензента
    public void update(Reviewer reviewer) throws SQLException {
        String sql = """
                UPDATE reviewers
                SET full_name = ?, email = ?, department = ?, position = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, reviewer.getFullName());
            statement.setString(2, reviewer.getEmail());
            statement.setString(3, reviewer.getDepartment());
            statement.setString(4, reviewer.getPosition());
            statement.setLong(5, reviewer.getId());

            statement.executeUpdate();
        }
    }

    // Видалення рецензента
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM reviewers WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    // Перевірка існування рецензента за ID
    public boolean existsById(Long id) throws SQLException {
        String sql = "SELECT 1 FROM reviewers WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    // Перетворення результату SQL-запиту на об'єкт Reviewer
    private Reviewer mapReviewer(ResultSet result) throws SQLException {
        return new Reviewer(
                result.getLong("id"),
                result.getString("full_name"),
                result.getString("email"),
                result.getString("department"),
                result.getString("position")
        );
    }
}