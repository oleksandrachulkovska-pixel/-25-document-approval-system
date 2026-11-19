package com.coursework.service;

import com.coursework.Document;
import com.coursework.dao.AuthorDAO;
import com.coursework.dao.DocumentDAO;

import java.sql.SQLException;
import java.util.List;

public class DocumentService {

    private final DocumentDAO documentDAO = new DocumentDAO();
    private final AuthorDAO authorDAO = new AuthorDAO();

    // Створення документа
    public void createDocument(Document document) throws SQLException {
        validateDocument(document);

        if (!authorDAO.existsById(document.getAuthorId())) {
            throw new IllegalArgumentException(
                    "Автор із таким ID не існує."
            );
        }

        documentDAO.create(document);
    }

    // Отримання документа за ID
    public Document getDocumentById(Long id) throws SQLException {
        validateId(id);
        return documentDAO.findById(id);
    }

    // Отримання всіх документів
    public List<Document> getAllDocuments() throws SQLException {
        return documentDAO.findAll();
    }

    // Оновлення документа
    public void updateDocument(Document document) throws SQLException {
        validateDocument(document);
        validateId(document.getId());

        if (!authorDAO.existsById(document.getAuthorId())) {
            throw new IllegalArgumentException(
                    "Автор із таким ID не існує."
            );
        }

        documentDAO.update(document);
    }

    // Видалення документа
    public void deleteDocument(Long id) throws SQLException {
        validateId(id);
        documentDAO.delete(id);
    }

    // Перевірка коректності даних документа
    private void validateDocument(Document document) {

        if (document == null) {
            throw new IllegalArgumentException(
                    "Документ не може бути порожнім."
            );
        }

        if (document.getTitle() == null ||
                document.getTitle().isBlank()) {
            throw new IllegalArgumentException(
                    "Назва документа обов'язкова."
            );
        }

        if (document.getDocumentType() == null ||
                document.getDocumentType().isBlank()) {
            throw new IllegalArgumentException(
                    "Тип документа обов'язковий."
            );
        }

        if (document.getFileName() == null ||
                document.getFileName().isBlank()) {
            throw new IllegalArgumentException(
                    "Назва файлу обов'язкова."
            );
        }

        if (document.getAuthorId() == null ||
                document.getAuthorId() <= 0) {
            throw new IllegalArgumentException(
                    "Некоректний ID автора."
            );
        }
    }

    // Перевірка ID
    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Некоректний ID документа."
            );
        }
    }
}