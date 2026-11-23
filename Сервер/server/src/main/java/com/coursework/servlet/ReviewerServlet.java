package com.coursework.servlet;

import com.coursework.Reviewer;
import com.coursework.service.ReviewerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ReviewerServlet extends HttpServlet {

    private final ReviewerService reviewerService = new ReviewerService();

    private final ObjectMapper objectMapper = new ObjectMapper();

    // GET — отримання всіх рецензентів або одного за ID
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String idParameter = request.getParameter("id");

            if (idParameter != null) {
                Long id = Long.parseLong(idParameter);

                Reviewer reviewer = reviewerService.getReviewerById(id);

                if (reviewer == null) {
                    sendError(response, 404, "Рецензента не знайдено.");
                    return;
                }

                objectMapper.writeValue(response.getWriter(), reviewer);

            } else {
                objectMapper.writeValue(
                        response.getWriter(),
                        reviewerService.getAllReviewers()
                );
            }

        } catch (NumberFormatException e) {
            sendError(response, 400, "Некоректний ID рецензента.");

        } catch (IllegalArgumentException e) {
            sendError(response, 400, e.getMessage());

        } catch (SQLException e) {
            sendError(response, 500, "Помилка роботи з базою даних.");
        }
    }

    // POST — створення рецензента
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            Reviewer reviewer = objectMapper.readValue(
                    request.getInputStream(),
                    Reviewer.class
            );

            reviewerService.createReviewer(reviewer);

            response.setStatus(HttpServletResponse.SC_CREATED);

            Map<String, String> result = new HashMap<>();
            result.put("message", "Рецензента успішно створено.");

            objectMapper.writeValue(response.getWriter(), result);

        } catch (IllegalArgumentException e) {
            sendError(response, 400, e.getMessage());

        } catch (SQLException e) {
            sendError(response, 500, "Помилка створення рецензента.");
        }
    }

    // PUT — редагування рецензента
    @Override
    protected void doPut(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String idParameter = request.getParameter("id");

            if (idParameter == null) {
                sendError(response, 400, "Необхідно вказати ID рецензента.");
                return;
            }

            Long id = Long.parseLong(idParameter);

            Reviewer reviewer = objectMapper.readValue(
                    request.getInputStream(),
                    Reviewer.class
            );

            reviewer.setId(id);
            reviewerService.updateReviewer(reviewer);

            Map<String, String> result = new HashMap<>();
            result.put("message", "Рецензента успішно оновлено.");

            objectMapper.writeValue(response.getWriter(), result);

        } catch (NumberFormatException e) {
            sendError(response, 400, "Некоректний ID рецензента.");

        } catch (IllegalArgumentException e) {
            sendError(response, 400, e.getMessage());

        } catch (SQLException e) {
            sendError(response, 500, "Помилка оновлення рецензента.");
        }
    }

    // DELETE — видалення рецензента
    @Override
    protected void doDelete(HttpServletRequest request,
                            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String idParameter = request.getParameter("id");

            if (idParameter == null) {
                sendError(response, 400, "Необхідно вказати ID рецензента.");
                return;
            }

            Long id = Long.parseLong(idParameter);

            reviewerService.deleteReviewer(id);

            Map<String, String> result = new HashMap<>();
            result.put("message", "Рецензента успішно видалено.");

            objectMapper.writeValue(response.getWriter(), result);

        } catch (NumberFormatException e) {
            sendError(response, 400, "Некоректний ID рецензента.");

        } catch (IllegalArgumentException e) {
            sendError(response, 400, e.getMessage());

        } catch (SQLException e) {
            sendError(response, 500, "Помилка видалення рецензента.");
        }
    }

    // Формування JSON-повідомлення про помилку
    private void sendError(HttpServletResponse response,
                           int status,
                           String message) throws IOException {

        response.setStatus(status);

        Map<String, String> error = new HashMap<>();
        error.put("error", message);

        objectMapper.writeValue(response.getWriter(), error);
    }
}