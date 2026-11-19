package com.coursework;

import com.coursework.dao.ReviewerDAO;
import java.sql.SQLException;

public class DatabaseTest {

    public static void main(String[] args) {

        ReviewerDAO reviewerDAO = new ReviewerDAO();

        try {
            System.out.println("СПИСОК РЕЦЕНЗЕНТІВ:");

            var reviewers = reviewerDAO.findAll();

            for (Reviewer reviewer : reviewers) {
                System.out.println("-------------------------");
                System.out.println("ID: " + reviewer.getId());
                System.out.println("ПІБ: " + reviewer.getFullName());
                System.out.println("Email: " + reviewer.getEmail());
                System.out.println("Підрозділ: " + reviewer.getDepartment());
                System.out.println("Посада: " + reviewer.getPosition());
            }

            System.out.println("-------------------------");
            System.out.println("Кількість рецензентів: " + reviewers.size());

        } catch (SQLException e) {
            System.out.println("Помилка роботи з базою даних:");
            e.printStackTrace();
        }
    }
}