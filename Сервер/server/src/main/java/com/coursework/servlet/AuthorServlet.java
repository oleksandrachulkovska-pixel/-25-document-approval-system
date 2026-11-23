package com.coursework.servlet;

import com.coursework.Author;
import com.coursework.service.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class AuthorServlet extends HttpServlet {

    private AuthorService authorService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        authorService = new AuthorService();
        objectMapper = new ObjectMapper();
    }

    // Налаштування JSON-відповіді
    private void configureResponse(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    // GET: отримання всіх авторів або автора за ID
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        configureResponse(response);

        try {
            String idParameter = request.getParameter("id");

            if (idParameter != null && !idParameter.isBlank()) {

                Long id = Long.parseLong(idParameter);
                Author author = authorService.getById(id);

                if (author == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);

                    objectMapper.writeValue(
                            response.getWriter(),
                            "Автора не знайдено"
                    );
                    return;
                }

                objectMapper.writeValue(
                        response.getWriter(),
                        author
                );

            } else {

                List<Author> authors = authorService.getAll();

                objectMapper.writeValue(
                        response.getWriter(),
                        authors
                );
            }

        } catch (NumberFormatException e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            objectMapper.writeValue(
                    response.getWriter(),
                    "Некоректний ID автора"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            objectMapper.writeValue(
                    response.getWriter(),
                    "Помилка отримання авторів: " + e.getMessage()
            );
        }
    }

    // POST: створення нового автора
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        configureResponse(response);

        try {
            Author author = objectMapper.readValue(
                    request.getInputStream(),
                    Author.class
            );

            authorService.create(author);

            response.setStatus(HttpServletResponse.SC_CREATED);

            objectMapper.writeValue(
                    response.getWriter(),
                    "Автора успішно створено"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            objectMapper.writeValue(
                    response.getWriter(),
                    "Помилка створення автора: " + e.getMessage()
            );
        }
    }

    // PUT: оновлення даних автора
    @Override
    protected void doPut(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        configureResponse(response);

        try {
            String idParameter = request.getParameter("id");

            if (idParameter == null || idParameter.isBlank()) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                objectMapper.writeValue(
                        response.getWriter(),
                        "Не вказано ID автора"
                );

                return;
            }

            Long id = Long.parseLong(idParameter);

            Author author = objectMapper.readValue(
                    request.getInputStream(),
                    Author.class
            );

            author.setId(id);

            authorService.update(author);

            response.setStatus(HttpServletResponse.SC_OK);

            objectMapper.writeValue(
                    response.getWriter(),
                    "Дані автора успішно оновлено"
            );

        } catch (NumberFormatException e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            objectMapper.writeValue(
                    response.getWriter(),
                    "Некоректний ID автора"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            objectMapper.writeValue(
                    response.getWriter(),
                    "Помилка оновлення автора: " + e.getMessage()
            );
        }
    }

    // DELETE: видалення автора
    @Override
    protected void doDelete(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        configureResponse(response);

        try {
            String idParameter = request.getParameter("id");

            if (idParameter == null || idParameter.isBlank()) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                objectMapper.writeValue(
                        response.getWriter(),
                        "Не вказано ID автора"
                );

                return;
            }

            Long id = Long.parseLong(idParameter);

            authorService.delete(id);

            response.setStatus(HttpServletResponse.SC_OK);

            objectMapper.writeValue(
                    response.getWriter(),
                    "Автора успішно видалено"
            );

        } catch (NumberFormatException e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            objectMapper.writeValue(
                    response.getWriter(),
                    "Некоректний ID автора"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            objectMapper.writeValue(
                    response.getWriter(),
                    "Помилка видалення автора: " + e.getMessage()
            );
        }
    }
}