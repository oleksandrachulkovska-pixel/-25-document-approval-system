package com.coursework;

import com.coursework.dao.DocumentDAO;

import java.sql.SQLException;
import java.util.List;

public class DatabaseTest {

    public static void main(String[] args) {

        DocumentDAO documentDAO = new DocumentDAO();

        try {
            System.out.println("СПИСОК ДОКУМЕНТІВ:");

            List<Document> documents = documentDAO.findAll();

            for (Document document : documents) {

                System.out.println("-------------------------");
                System.out.println("ID: " + document.getId());
                System.out.println("Назва: " + document.getTitle());
                System.out.println("Опис: " + document.getDescription());
                System.out.println("Тип: " + document.getDocumentType());
                System.out.println("Файл: " + document.getFileName());
                System.out.println("ID автора: " + document.getAuthorId());
            }

            System.out.println("-------------------------");
            System.out.println("Кількість документів: " + documents.size());

        } catch (SQLException e) {

            System.out.println("Помилка роботи з базою даних:");
            e.printStackTrace();
        }
    }
}