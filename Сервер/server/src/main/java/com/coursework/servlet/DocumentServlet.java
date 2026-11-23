package com.coursework.servlet;

import com.coursework.Document;
import com.coursework.service.DocumentService;
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

public class DocumentServlet extends HttpServlet {

    private final DocumentService documentService = new DocumentService();

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    // GET — отримання всіх документів або документа за ID
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

                Document document = documentService.getDocumentById(id);

                if (document == null) {
                    sendError(response, 404, "Документ не знайдено.");
                    return;
                }

                objectMapper.writeValue(response.getWriter(), document);

            } else {
                objectMapper.writeValue(
                        response.getWriter(),
                        documentService.getAllDocuments()
                );
            }

        } catch (NumberFormatException e) {
            sendError(response, 400, "Некоректний ID документа.");

        } catch (IllegalArgumentException e) {
            sendError(response, 400, e.getMessage());

        } catch (SQLException e) {
            sendError(response, 500, "Помилка роботи з базою даних.");
        }
    }

    // POST — створення документа
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            Document document = objectMapper.readValue(
                    request.getInputStream(),
                    Document.class
            );

            documentService.createDocument(document);

            response.setStatus(HttpServletResponse.SC_CREATED);

            Map<String, String> result = new HashMap<>();
            result.put("message", "Документ успішно створено.");

            objectMapper.writeValue(response.getWriter(), result);

        } catch (IllegalArgumentException e) {
            sendError(response, 400, e.getMessage());

        } catch (SQLException e) {
            sendError(response, 500, "Помилка створення документа.");
        }
    }

    // PUT — редагування документа
    @Override
    protected void doPut(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String idParameter = request.getParameter("id");

            if (idParameter == null) {
                sendError(response, 400, "Необхідно вказати ID документа.");
                return;
            }

            Long id = Long.parseLong(idParameter);

            Document document = objectMapper.readValue(
                    request.getInputStream(),
                    Document.class
            );

            document.setId(id);
            documentService.updateDocument(document);

            Map<String, String> result = new HashMap<>();
            result.put("message", "Документ успішно оновлено.");

            objectMapper.writeValue(response.getWriter(), result);

        } catch (NumberFormatException e) {
            sendError(response, 400, "Некоректний ID документа.");

        } catch (IllegalArgumentException e) {
            sendError(response, 400, e.getMessage());

        } catch (SQLException e) {
            sendError(response, 500, "Помилка оновлення документа.");
        }
    }

    // DELETE — видалення документа
    @Override
    protected void doDelete(HttpServletRequest request,
                            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String idParameter = request.getParameter("id");

            if (idParameter == null) {
                sendError(response, 400, "Необхідно вказати ID документа.");
                return;
            }

            Long id = Long.parseLong(idParameter);

            documentService.deleteDocument(id);

            Map<String, String> result = new HashMap<>();
            result.put("message", "Документ успішно видалено.");

            objectMapper.writeValue(response.getWriter(), result);

        } catch (NumberFormatException e) {
            sendError(response, 400, "Некоректний ID документа.");

        } catch (IllegalArgumentException e) {
            sendError(response, 400, e.getMessage());

        } catch (SQLException e) {
            sendError(response, 500, "Помилка видалення документа.");
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