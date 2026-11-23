package com.coursework.servlet;

import com.coursework.ApprovalRequest;
import com.coursework.service.ApprovalRequestService;
import com.coursework.service.ApprovalLifecycleService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ApprovalRequestServlet extends HttpServlet {

    private final ApprovalRequestService approvalRequestService =
            new ApprovalRequestService();

    private final ApprovalLifecycleService lifecycleService =
            new ApprovalLifecycleService();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // GET — отримання всіх запитів або одного за ID
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        setJsonResponse(response);

        try {
            String idParameter = request.getParameter("id");

            if (idParameter != null) {

                Long id = Long.parseLong(idParameter);

                ApprovalRequest approvalRequest =
                        approvalRequestService.getRequestById(id);

                if (approvalRequest == null) {
                    sendError(response, 404,
                            "Запит на погодження не знайдено.");
                    return;
                }

                objectMapper.writeValue(
                        response.getWriter(),
                        approvalRequest
                );

            } else {

                objectMapper.writeValue(
                        response.getWriter(),
                        approvalRequestService.getAllRequests()
                );
            }

        } catch (NumberFormatException e) {
            sendError(response, 400, "Некоректний ID запиту.");

        } catch (IllegalArgumentException e) {
            sendError(response, 400, e.getMessage());

        } catch (SQLException e) {
            sendError(response, 500,
                    "Помилка отримання запитів із бази даних.");
        }
    }

    // POST — створення запиту або зміна його статусу
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        setJsonResponse(response);

        try {
            String action = request.getParameter("action");

            // Виконання переходу життєвого циклу
            if (action != null) {

                String idParameter = request.getParameter("id");

                if (idParameter == null) {
                    sendError(response, 400,
                            "Необхідно вказати ID запиту.");
                    return;
                }

                Long id = Long.parseLong(idParameter);

                switch (action) {
                    case "submit":
                        lifecycleService.submit(id);
                        break;

                    case "startReview":
                        lifecycleService.startReview(id);
                        break;

                    case "approve":
                        lifecycleService.approve(id);
                        break;

                    default:
                        sendError(response, 400,
                                "Невідома операція зміни статусу.");
                        return;
                }

                sendSuccess(response, "Статус запиту успішно змінено.");
                return;
            }

            // Створення нового запиту
            ApprovalRequest approvalRequest =
                    objectMapper.readValue(
                            request.getInputStream(),
                            ApprovalRequest.class
                    );

            approvalRequestService.createRequest(approvalRequest);

            response.setStatus(HttpServletResponse.SC_CREATED);

            sendSuccess(response, "Запит на погодження створено.");

        } catch (NumberFormatException e) {
            sendError(response, 400, "Некоректний ID запиту.");

        } catch (IllegalArgumentException e) {
            sendError(response, 400, e.getMessage());

        } catch (IllegalStateException e) {
            sendError(response, 409, e.getMessage());

        } catch (SQLException e) {
            sendError(response, 500,
                    "Помилка створення або зміни статусу запиту.");
        }
    }

    // PUT — оновлення запиту
    @Override
    protected void doPut(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        setJsonResponse(response);

        try {
            String idParameter = request.getParameter("id");

            if (idParameter == null) {
                sendError(response, 400,
                        "Необхідно вказати ID запиту.");
                return;
            }

            Long id = Long.parseLong(idParameter);

            ApprovalRequest approvalRequest =
                    objectMapper.readValue(
                            request.getInputStream(),
                            ApprovalRequest.class
                    );

            approvalRequest.setId(id);

            approvalRequestService.updateRequest(approvalRequest);

            sendSuccess(response, "Запит успішно оновлено.");

        } catch (NumberFormatException e) {
            sendError(response, 400, "Некоректний ID запиту.");

        } catch (IllegalArgumentException e) {
            sendError(response, 400, e.getMessage());

        } catch (SQLException e) {
            sendError(response, 500,
                    "Помилка оновлення запиту.");
        }
    }

    // DELETE — видалення запиту
    @Override
    protected void doDelete(HttpServletRequest request,
                            HttpServletResponse response)
            throws ServletException, IOException {

        setJsonResponse(response);

        try {
            String idParameter = request.getParameter("id");

            if (idParameter == null) {
                sendError(response, 400,
                        "Необхідно вказати ID запиту.");
                return;
            }

            Long id = Long.parseLong(idParameter);

            approvalRequestService.deleteRequest(id);

            sendSuccess(response, "Запит успішно видалено.");

        } catch (NumberFormatException e) {
            sendError(response, 400, "Некоректний ID запиту.");

        } catch (IllegalArgumentException e) {
            sendError(response, 400, e.getMessage());

        } catch (SQLException e) {
            sendError(response, 500,
                    "Помилка видалення запиту.");
        }
    }

    // Налаштування JSON-відповіді
    private void setJsonResponse(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    // Успішна відповідь
    private void sendSuccess(HttpServletResponse response,
                             String message) throws IOException {

        Map<String, String> result = new HashMap<>();
        result.put("message", message);

        objectMapper.writeValue(response.getWriter(), result);
    }

    // Відповідь про помилку
    private void sendError(HttpServletResponse response,
                           int status,
                           String message) throws IOException {

        response.setStatus(status);

        Map<String, String> error = new HashMap<>();
        error.put("error", message);

        objectMapper.writeValue(response.getWriter(), error);
    }
}