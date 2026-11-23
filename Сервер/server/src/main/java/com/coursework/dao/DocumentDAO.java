package com.coursework.dao;

import com.coursework.Document;
import com.coursework.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DocumentDAO {

    // Створення документа
    public void create(Document document) throws SQLException {
        String sql = """
                INSERT INTO documents
                (title, description, document_type, file_name, author_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, document.getTitle());
            statement.setString(2, document.getDescription());
            statement.setString(3, document.getDocumentType());
            statement.setString(4, document.getFileName());
            statement.setLong(5, document.getAuthorId());

            statement.executeUpdate();
        }
    }

    // Отримання документа за ID
    public Document findById(Long id) throws SQLException {
        String sql = "SELECT * FROM documents WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapDocument(result);
                }
            }
        }

        return null;
    }

    // Отримання всіх документів
    public List<Document> findAll() throws SQLException {
        List<Document> documents = new ArrayList<>();

        String sql = "SELECT * FROM documents";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                documents.add(mapDocument(result));

                System.out.println(
                        "DAO: отримано документ ID = "
                                + documents.get(documents.size() - 1).getId()
                );
            }

            System.out.println(
                    "DAO: загальна кількість документів = "
                            + documents.size()
            );
        }

        return documents;
    }

    // Оновлення документа
    public void update(Document document) throws SQLException {
        String sql = """
                UPDATE documents
                SET title = ?, description = ?, document_type = ?,
                    file_name = ?, author_id = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, document.getTitle());
            statement.setString(2, document.getDescription());
            statement.setString(3, document.getDocumentType());
            statement.setString(4, document.getFileName());
            statement.setLong(5, document.getAuthorId());
            statement.setLong(6, document.getId());

            statement.executeUpdate();
        }
    }

    // Видалення документа
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM documents WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            statement.executeUpdate();
        }
    }

    // Перевірка існування документа за ID
    public boolean existsById(Long id) throws SQLException {
        String sql = "SELECT 1 FROM documents WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    // Перетворення результату SQL-запиту на об'єкт Document
    private Document mapDocument(ResultSet result) throws SQLException {
        Timestamp timestamp = result.getTimestamp("created_at");

        return new Document(
                result.getLong("id"),
                result.getString("title"),
                result.getString("description"),
                result.getString("document_type"),
                result.getString("file_name"),
                timestamp == null ? null : timestamp.toLocalDateTime(),
                result.getLong("author_id")
        );
    }
}