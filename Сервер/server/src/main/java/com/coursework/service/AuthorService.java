package com.coursework.service;

import com.coursework.Author;
import com.coursework.dao.AuthorDAO;

import java.sql.SQLException;
import java.util.List;

public class AuthorService {

    private final AuthorDAO authorDAO;

    public AuthorService() {
        this.authorDAO = new AuthorDAO();
    }

    // Створення автора
    public void createAuthor(Author author) throws SQLException {
        validateAuthor(author);
        authorDAO.create(author);
    }

    // Отримання автора за ID
    public Author getAuthorById(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Некоректний ID автора");
        }

        return authorDAO.findById(id);
    }

    // Отримання всіх авторів
    public List<Author> getAllAuthors() throws SQLException {
        return authorDAO.findAll();
    }

    // Оновлення автора
    public void updateAuthor(Author author) throws SQLException {
        if (author == null || author.getId() == null || author.getId() <= 0) {
            throw new IllegalArgumentException("Некоректний автор або його ID");
        }

        validateAuthor(author);
        authorDAO.update(author);
    }

    // Видалення автора
    public void deleteAuthor(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Некоректний ID автора");
        }

        authorDAO.delete(id);
    }

    // Перевірка даних автора
    private void validateAuthor(Author author) {
        if (author == null) {
            throw new IllegalArgumentException("Автор не може бути порожнім");
        }

        if (author.getFullName() == null || author.getFullName().isBlank()) {
            throw new IllegalArgumentException("Вкажіть ПІБ автора");
        }

        if (author.getEmail() == null || !author.getEmail().matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Некоректна електронна пошта");
        }

        if (author.getDepartment() == null || author.getDepartment().isBlank()) {
            throw new IllegalArgumentException("Вкажіть підрозділ автора");
        }

        if (author.getPosition() == null || author.getPosition().isBlank()) {
            throw new IllegalArgumentException("Вкажіть посаду автора");
        }
    }
}