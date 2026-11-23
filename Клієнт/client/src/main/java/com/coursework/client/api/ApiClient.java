package com.coursework.client.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ApiClient {

    // Адреса серверного REST API
    private static final String BASE_URL =
            "http://127.0.0.1:8080/server/api";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ApiClient() {

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    // GET-запит
    public String get(String endpoint)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .timeout(Duration.ofSeconds(30))
                .GET()
                .header("Accept", "application/json")
                .build();

        return sendRequest(request);
    }

    // POST-запит
    public String post(String endpoint, Object data)
            throws IOException, InterruptedException {

        String json = objectMapper.writeValueAsString(data);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return sendRequest(request);
    }

    // PUT-запит
    public String put(String endpoint, Object data)
            throws IOException, InterruptedException {

        String json = objectMapper.writeValueAsString(data);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        return sendRequest(request);
    }

    // DELETE-запит
    public String delete(String endpoint)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .timeout(Duration.ofSeconds(30))
                .header("Accept", "application/json")
                .DELETE()
                .build();

        return sendRequest(request);
    }

    // Надсилання HTTP-запиту
    private String sendRequest(HttpRequest request)
            throws IOException, InterruptedException {

        System.out.println("HTTP-запит: " + request.method());
        System.out.println("Адреса: " + request.uri());

        HttpResponse<String> response;

        try {

            response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

        } catch (ConnectException e) {

            throw new IOException(
                    "Не вдалося підключитися до сервера Tomcat. "
                            + "Перевірте, чи запущений сервер на порту 8080.",
                    e
            );
        }

        System.out.println(
                "HTTP-статус: " + response.statusCode()
        );

        if (response.statusCode() < 200 ||
                response.statusCode() >= 300) {

            throw new IOException(
                    "HTTP помилка " + response.statusCode()
                            + ": " + response.body()
            );
        }

        return response.body();
    }

    // Отримання ObjectMapper
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}