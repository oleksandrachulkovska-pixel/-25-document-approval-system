package com.coursework.client.controller;

import com.coursework.client.api.ApiClient;
import com.coursework.client.model.Author;
import com.fasterxml.jackson.core.type.TypeReference;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

public class AuthorController {

    private final ApiClient apiClient = new ApiClient();

    private final TableView<Author> table = new TableView<>();

    private final ObservableList<Author> authors =
            FXCollections.observableArrayList();

    public VBox createView() {

        TableColumn<Author, Long> idColumn =
                new TableColumn<>("ID");

        idColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(
                        data.getValue().getId()
                )
        );

        TableColumn<Author, String> nameColumn =
                new TableColumn<>("ПІБ");

        nameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getFullName()
                )
        );

        TableColumn<Author, String> emailColumn =
                new TableColumn<>("Email");

        emailColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getEmail()
                )
        );

        TableColumn<Author, String> departmentColumn =
                new TableColumn<>("Відділ");

        departmentColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getDepartment()
                )
        );

        TableColumn<Author, String> positionColumn =
                new TableColumn<>("Посада");

        positionColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getPosition()
                )
        );

        table.getColumns().addAll(
                idColumn,
                nameColumn,
                emailColumn,
                departmentColumn,
                positionColumn
        );

        table.setItems(authors);

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        // Кнопки керування
        Button addButton = new Button("Додати");
        Button editButton = new Button("Редагувати");
        Button deleteButton = new Button("Видалити");
        Button refreshButton = new Button("Оновити");

        addButton.setOnAction(event -> addAuthor());
        editButton.setOnAction(event -> editAuthor());
        deleteButton.setOnAction(event -> deleteAuthor());
        refreshButton.setOnAction(event -> loadAuthors());

        HBox buttons = new HBox(
                10,
                addButton,
                editButton,
                deleteButton,
                refreshButton
        );

        VBox root = new VBox(15, table, buttons);
        root.setPadding(new Insets(15));

        loadAuthors();

        return root;
    }

    // Завантаження авторів
    private void loadAuthors() {

        try {

            System.out.println("Завантаження авторів із сервера...");

            String response = apiClient.get("/authors");

            System.out.println("Відповідь сервера:");
            System.out.println(response);

            List<Author> result =
                    apiClient.getObjectMapper().readValue(
                            response,
                            new TypeReference<List<Author>>() {}
                    );

            authors.setAll(result);

            System.out.println(
                    "Завантажено авторів: " + result.size()
            );

        } catch (Exception e) {

            System.err.println(
                    "ПОМИЛКА ЗАВАНТАЖЕННЯ АВТОРІВ:"
            );

            e.printStackTrace();

            String errorMessage = e.getMessage() != null
                    ? e.getMessage()
                    : e.toString();

            showError(
                    "Помилка завантаження авторів",
                    errorMessage
            );
        }
    }

    // Додавання автора
    private void addAuthor() {

        Author author = showAuthorDialog(null);

        if (author == null) {
            return;
        }

        try {

            apiClient.post("/authors", author);

            showInformation(
                    "Успіх",
                    "Автора успішно додано."
            );

            loadAuthors();

        } catch (Exception e) {

            showException(
                    "Помилка додавання автора",
                    e
            );
        }
    }

    // Редагування автора
    private void editAuthor() {

        Author selected = table.getSelectionModel()
                .getSelectedItem();

        if (selected == null) {

            showWarning(
                    "Виберіть автора",
                    "Спочатку виберіть автора в таблиці."
            );

            return;
        }

        Author updated = showAuthorDialog(selected);

        if (updated == null) {
            return;
        }

        try {

            apiClient.put(
                    "/authors?id=" + selected.getId(),
                    updated
            );

            showInformation(
                    "Успіх",
                    "Дані автора успішно оновлено."
            );

            loadAuthors();

        } catch (Exception e) {

            showException(
                    "Помилка редагування автора",
                    e
            );
        }
    }

    // Видалення автора
    private void deleteAuthor() {

        Author selected = table.getSelectionModel()
                .getSelectedItem();

        if (selected == null) {

            showWarning(
                    "Виберіть автора",
                    "Спочатку виберіть автора в таблиці."
            );

            return;
        }

        Alert confirmation =
                new Alert(Alert.AlertType.CONFIRMATION);

        confirmation.setTitle("Підтвердження видалення");
        confirmation.setHeaderText(
                "Видалити автора " + selected.getFullName() + "?"
        );

        confirmation.setContentText(
                "Цю дію потрібно підтвердити."
        );

        Optional<ButtonType> result =
                confirmation.showAndWait();

        if (result.isEmpty()
                || result.get() != ButtonType.OK) {
            return;
        }

        try {

            apiClient.delete(
                    "/authors?id=" + selected.getId()
            );

            showInformation(
                    "Успіх",
                    "Автора успішно видалено."
            );

            loadAuthors();

        } catch (Exception e) {

            showException(
                    "Помилка видалення автора",
                    e
            );
        }
    }

    // Діалог додавання та редагування
    private Author showAuthorDialog(Author existing) {

        Dialog<Author> dialog = new Dialog<>();

        dialog.setTitle(
                existing == null
                        ? "Додавання автора"
                        : "Редагування автора"
        );

        dialog.setHeaderText(
                existing == null
                        ? "Введіть дані нового автора"
                        : "Змініть дані автора"
        );

        ButtonType saveButton =
                new ButtonType("Зберегти",
                        ButtonType.OK.getButtonData());

        dialog.getDialogPane().getButtonTypes().addAll(
                saveButton,
                ButtonType.CANCEL
        );

        TextField nameField = new TextField();
        TextField emailField = new TextField();
        TextField departmentField = new TextField();
        TextField positionField = new TextField();

        nameField.setPromptText("Прізвище, ім'я та по батькові");
        emailField.setPromptText("Електронна пошта");
        departmentField.setPromptText("Відділ");
        positionField.setPromptText("Посада");

        if (existing != null) {

            nameField.setText(existing.getFullName());
            emailField.setText(existing.getEmail());
            departmentField.setText(existing.getDepartment());
            positionField.setText(existing.getPosition());
        }

        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("ПІБ:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);

        grid.add(new Label("Відділ:"), 0, 2);
        grid.add(departmentField, 1, 2);

        grid.add(new Label("Посада:"), 0, 3);
        grid.add(positionField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {

            if (button == saveButton) {

                String name = nameField.getText().trim();
                String email = emailField.getText().trim();

                if (name.isEmpty() || email.isEmpty()) {

                    showWarning(
                            "Перевірка даних",
                            "ПІБ та Email обов'язкові."
                    );

                    return null;
                }

                if (!email.matches(
                        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
                )) {

                    showWarning(
                            "Перевірка Email",
                            "Введіть коректну електронну адресу."
                    );

                    return null;
                }

                Long id = existing == null
                        ? null
                        : existing.getId();

                return new Author(
                        id,
                        name,
                        email,
                        departmentField.getText().trim(),
                        positionField.getText().trim()
                );
            }

            return null;
        });

        Optional<Author> result = dialog.showAndWait();

        return result.orElse(null);
    }

    // Відображення помилок
    private void showException(String title, Exception e) {

        e.printStackTrace();

        String message = e.getMessage() != null
                ? e.getMessage()
                : e.toString();

        showError(title, message);
    }

    private void showError(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle(title);
        alert.setHeaderText("Не вдалося виконати операцію");
        alert.setContentText(message);

        alert.showAndWait();
    }

    private void showWarning(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    private void showInformation(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}