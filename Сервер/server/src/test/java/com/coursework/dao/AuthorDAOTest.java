package com.coursework.dao;

import com.coursework.Author;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthorDAOTest {

    @Test
    void testCreateAuthor() throws Exception {
        AuthorDAO authorDAO = new AuthorDAO();

        String email = "test_" + UUID.randomUUID() + "@example.com";

        Author author = new Author(
                null,
                "Тестовий Автор",
                email,
                "Тестовий відділ",
                "Тестова посада"
        );

        authorDAO.create(author);

        Author createdAuthor = authorDAO.findAll()
                .stream()
                .filter(a -> email.equals(a.getEmail()))
                .findFirst()
                .orElse(null);

        assertNotNull(createdAuthor, "Автор повинен бути створений у базі даних");

        try {
            authorDAO.delete(createdAuthor.getId());
        } finally {
            System.out.println("Перевірку створення автора завершено.");
        }
    }
}