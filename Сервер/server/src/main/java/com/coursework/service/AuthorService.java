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

    public void create(Author author) throws SQLException {
        validate(author);
        authorDAO.create(author);
    }

    public Author getById(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Некоректний ID автора");
        }

        return authorDAO.findById(id);
    }

    public List<Author> getAll() throws SQLException {
        return authorDAO.findAll();
    }

    public void update(Author author) throws SQLException {
        validate(author);

        if (author.getId() == null || author.getId() <= 0) {
            throw new IllegalArgumentException("Некоректний ID автора");
        }

        authorDAO.update(author);
    }

    public void delete(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Некоректний ID автора");
        }

        authorDAO.delete(id);
    }

    private void validate(Author author) {
        if (author == null) {
            throw new IllegalArgumentException("Автор не може бути порожнім");
        }

        if (author.getFullName() == null ||
                author.getFullName().isBlank()) {
            throw new IllegalArgumentException("Вкажіть ПІБ автора");
        }

        if (author.getEmail() == null ||
                !author.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Некоректна електронна пошта");
        }
    }
}