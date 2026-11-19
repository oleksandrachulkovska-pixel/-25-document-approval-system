package com.coursework.dao;

import com.coursework.Author;
import com.coursework.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {

    // Створення автора
    public void create(Author author) throws SQLException {
        String sql = """
                INSERT INTO authors
                (full_name, email, department, position)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, author.getFullName());
            statement.setString(2, author.getEmail());
            statement.setString(3, author.getDepartment());
            statement.setString(4, author.getPosition());

            statement.executeUpdate();
        }
    }

    // Отримання автора за ID
    public Author findById(Long id) throws SQLException {
        String sql = "SELECT * FROM authors WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapAuthor(result);
                }
            }
        }

        return null;
    }

    // Отримання всіх авторів
    public List<Author> findAll() throws SQLException {
        List<Author> authors = new ArrayList<>();

        String sql = "SELECT * FROM authors";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                authors.add(mapAuthor(result));
            }
        }

        return authors;
    }

    // Редагування автора
    public void update(Author author) throws SQLException {
        String sql = """
                UPDATE authors
                SET full_name = ?, email = ?, department = ?, position = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, author.getFullName());
            statement.setString(2, author.getEmail());
            statement.setString(3, author.getDepartment());
            statement.setString(4, author.getPosition());
            statement.setLong(5, author.getId());

            statement.executeUpdate();
        }
    }

    // Видалення автора
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM authors WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    // Перевірка існування автора за ID
    public boolean existsById(Long id) throws SQLException {
        String sql = "SELECT 1 FROM authors WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    // Перетворення результату SQL-запиту на об'єкт Author
    private Author mapAuthor(ResultSet result) throws SQLException {
        return new Author(
                result.getLong("id"),
                result.getString("full_name"),
                result.getString("email"),
                result.getString("department"),
                result.getString("position")
        );
    }
}