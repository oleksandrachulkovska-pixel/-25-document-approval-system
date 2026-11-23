package com.coursework.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/document_approval_db?useSSL=false&serverTimezone=UTC";

    private static final String USER = "root";

    private static final String PASSWORD = "";

    /**
     * Створення підключення до бази даних MySQL.
     */
    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "MySQL JDBC Driver не знайдено!",
                    e
            );
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Перевірка підключення до бази даних.
     */
    public static void main(String[] args) {

        try (Connection connection = getConnection()) {

            System.out.println("Підключення до MySQL успішне!");

            String sql = "SELECT COUNT(*) FROM documents";

            try (Statement statement = connection.createStatement();
                 ResultSet result = statement.executeQuery(sql)) {

                if (result.next()) {

                    int count = result.getInt(1);

                    System.out.println(
                            "Кількість документів: " + count
                    );
                }
            }

        } catch (SQLException e) {

            System.out.println("Помилка роботи з базою даних:");

            e.printStackTrace();
        }
    }
}