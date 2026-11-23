package com.coursework.client.controller;

import com.coursework.client.api.ApiClient;
import com.coursework.client.model.ApprovalRequest;
import com.fasterxml.jackson.core.type.TypeReference;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ApprovalRequestController {

    private final ApiClient apiClient = new ApiClient();

    private final TableView<ApprovalRequest> table = new TableView<>();

    private final ObservableList<ApprovalRequest> requests =
            FXCollections.observableArrayList();

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public VBox createView() {

        TableColumn<ApprovalRequest, Long> idColumn =
                new TableColumn<>("ID");

        idColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(
                        data.getValue().getId()
                )
        );

        TableColumn<ApprovalRequest, Long> documentColumn =
                new TableColumn<>("ID документа");

        documentColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(
                        data.getValue().getDocumentId()
                )
        );

        TableColumn<ApprovalRequest, Long> reviewerColumn =
                new TableColumn<>("ID рецензента");

        reviewerColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(
                        data.getValue().getReviewerId()
                )
        );

        TableColumn<ApprovalRequest, String> statusColumn =
                new TableColumn<>("Статус");

        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getStatus()
                )
        );

        TableColumn<ApprovalRequest, Double> f1Column =
                new TableColumn<>("F1");

        f1Column.setCellValueFactory(data ->
                new SimpleObjectProperty<>(
                        data.getValue().getF1()
                )
        );

        TableColumn<ApprovalRequest, Double> f2Column =
                new TableColumn<>("F2");

        f2Column.setCellValueFactory(data ->
                new SimpleObjectProperty<>(
                        data.getValue().getF2()
                )
        );

        TableColumn<ApprovalRequest, Double> f3Column =
                new TableColumn<>("F3");

        f3Column.setCellValueFactory(data ->
                new SimpleObjectProperty<>(
                        data.getValue().getF3()
                )
        );

        TableColumn<ApprovalRequest, Double> indexColumn =
                new TableColumn<>("Індекс");

        indexColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(
                        data.getValue().getIntegralIndex()
                )
        );

        TableColumn<ApprovalRequest, String> submittedColumn =
                new TableColumn<>("Дата подання");

        submittedColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getSubmittedAt() == null
                                ? "-"
                                : data.getValue()
                                .getSubmittedAt()
                                .format(formatter)
                )
        );

        TableColumn<ApprovalRequest, String> reviewedColumn =
                new TableColumn<>("Дата погодження");

        reviewedColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getReviewedAt() == null
                                ? "-"
                                : data.getValue()
                                .getReviewedAt()
                                .format(formatter)
                )
        );

        table.getColumns().addAll(
                idColumn,
                documentColumn,
                reviewerColumn,
                statusColumn,
                f1Column,
                f2Column,
                f3Column,
                indexColumn,
                submittedColumn,
                reviewedColumn
        );

        table.setItems(requests);

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        Button refreshButton = new Button("Оновити");
        Button submitButton = new Button("Подати");
        Button reviewButton = new Button("Почати рецензування");
        Button approveButton = new Button("Погодити");

        refreshButton.setOnAction(event -> loadRequests());

        submitButton.setOnAction(event ->
                changeStatus("submit")
        );

        reviewButton.setOnAction(event ->
                changeStatus("startReview")
        );

        approveButton.setOnAction(event ->
                changeStatus("approve")
        );

        HBox buttons = new HBox(
                10,
                refreshButton,
                submitButton,
                reviewButton,
                approveButton
        );

        VBox root = new VBox(15, table, buttons);

        root.setPadding(new Insets(15));

        loadRequests();

        return root;
    }

    private void loadRequests() {

        try {

            String response =
                    apiClient.get("/approval-requests");

            List<ApprovalRequest> result =
                    apiClient.getObjectMapper().readValue(
                            response,
                            new TypeReference<List<ApprovalRequest>>() {}
                    );

            requests.setAll(result);

        } catch (Exception e) {

            showError(
                    "Помилка завантаження запитів",
                    e.getMessage()
            );
        }
    }

    private void changeStatus(String action) {

        ApprovalRequest selected =
                table.getSelectionModel().getSelectedItem();

        if (selected == null) {

            showError(
                    "Не вибрано запит",
                    "Спочатку виберіть запит у таблиці."
            );

            return;
        }

        String endpoint =
                "/approval-requests?action="
                        + action
                        + "&id="
                        + selected.getId();

        try {

            apiClient.post(endpoint, null);

            loadRequests();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Успішно");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Запит успішно оброблено."
            );

            alert.showAndWait();

        } catch (Exception e) {

            showError(
                    "Помилка зміни статусу",
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