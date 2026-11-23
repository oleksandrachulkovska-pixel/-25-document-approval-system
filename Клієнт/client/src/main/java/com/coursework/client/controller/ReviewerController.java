package com.coursework.client.controller;

import com.coursework.client.api.ApiClient;
import com.coursework.client.model.Reviewer;
import com.fasterxml.jackson.core.type.TypeReference;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class ReviewerController {

    private final ApiClient apiClient = new ApiClient();

    private final TableView<Reviewer> table = new TableView<>();

    private final ObservableList<Reviewer> reviewers =
            FXCollections.observableArrayList();

    public VBox createView() {

        TableColumn<Reviewer, Long> idColumn =
                new TableColumn<>("ID");

        idColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        data.getValue().getId()
                )
        );

        TableColumn<Reviewer, String> nameColumn =
                new TableColumn<>("ПІБ");

        nameColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getFullName()
                )
        );

        TableColumn<Reviewer, String> emailColumn =
                new TableColumn<>("Email");

        emailColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getEmail()
                )
        );

        TableColumn<Reviewer, String> departmentColumn =
                new TableColumn<>("Відділ");

        departmentColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getDepartment()
                )
        );

        TableColumn<Reviewer, String> positionColumn =
                new TableColumn<>("Посада");

        positionColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
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

        table.setItems(reviewers);

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        Button refreshButton = new Button("Оновити");

        refreshButton.setOnAction(
                event -> loadReviewers()
        );

        HBox buttons = new HBox(10, refreshButton);

        VBox root = new VBox(15, table, buttons);
        root.setPadding(new Insets(15));

        loadReviewers();

        return root;
    }

    private void loadReviewers() {

        try {

            String response = apiClient.get("/reviewers");

            List<Reviewer> result =
                    apiClient.getObjectMapper().readValue(
                            response,
                            new TypeReference<List<Reviewer>>() {}
                    );

            reviewers.setAll(result);

        } catch (Exception e) {

            showError(
                    "Помилка завантаження рецензентів",
                    e.getMessage()
            );
        }
    }

    private void showError(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}