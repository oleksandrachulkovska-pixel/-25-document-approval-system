package com.coursework.client.controller;

import com.coursework.client.api.ApiClient;
import com.coursework.client.model.Document;
import com.fasterxml.jackson.core.type.TypeReference;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentController {

    private static final Logger LOGGER =
            Logger.getLogger(DocumentController.class.getName());

    private static final String EXPECTED_HEADER =
            "title,description,document_type,file_name,author_id";

    private final ApiClient apiClient = new ApiClient();

    private final TableView<Document> table = new TableView<>();

    private final ObservableList<Document> documents =
            FXCollections.observableArrayList();

    private final Button refreshButton =
            new Button("Оновити");

    private final Button importButton =
            new Button("Імпортувати CSV");

    private final ProgressBar progressBar =
            new ProgressBar(0);

    private final Label statusLabel =
            new Label("Очікування імпорту");

    public VBox createView() {

        TableColumn<Document, Long> idColumn =
                new TableColumn<>("ID");

        idColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        data.getValue().getId()
                )
        );

        TableColumn<Document, String> titleColumn =
                new TableColumn<>("Назва документа");

        titleColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getTitle()
                )
        );

        TableColumn<Document, String> typeColumn =
                new TableColumn<>("Тип документа");

        typeColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getDocumentType()
                )
        );

        TableColumn<Document, String> fileColumn =
                new TableColumn<>("Файл");

        fileColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getFileName()
                )
        );

        TableColumn<Document, Long> authorColumn =
                new TableColumn<>("ID автора");

        authorColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        data.getValue().getAuthorId()
                )
        );

        table.getColumns().addAll(
                idColumn,
                titleColumn,
                typeColumn,
                fileColumn,
                authorColumn
        );

        table.setItems(documents);

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        refreshButton.setOnAction(event -> loadDocuments());

        importButton.setOnAction(event -> chooseCsvFile());

        progressBar.setMaxWidth(Double.MAX_VALUE);

        HBox buttons = new HBox(
                10,
                refreshButton,
                importButton
        );

        VBox root = new VBox(
                12,
                table,
                buttons,
                progressBar,
                statusLabel
        );

        root.setPadding(new Insets(15));

        loadDocuments();

        return root;
    }

    private void chooseCsvFile() {

        FileChooser chooser = new FileChooser();

        chooser.setTitle("Виберіть CSV-файл");

        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "CSV-файли",
                        "*.csv"
                )
        );

        Window window = importButton.getScene() == null
                ? null
                : importButton.getScene().getWindow();

        File selectedFile = chooser.showOpenDialog(window);

        if (selectedFile == null) {
            return;
        }

        startImport(selectedFile.toPath());
    }

    private void startImport(Path csvPath) {

        if (importButton.isDisabled()) {
            return;
        }

        importButton.setDisable(true);

        progressBar.setProgress(-1);

        statusLabel.setText("Підготовка імпорту...");

        Task<ImportResult> task = new Task<ImportResult>() {

            @Override
            protected ImportResult call() throws Exception {

                return importCsv(csvPath);
            }

            private ImportResult importCsv(Path path)
                    throws Exception {

                LOGGER.info(
                        "Початок імпорту CSV: "
                                + path.getFileName()
                );

                if (!Files.exists(path)
                        || !Files.isRegularFile(path)) {

                    throw new IOException(
                            "CSV-файл не знайдено."
                    );
                }

                ImportResult result = new ImportResult();

                List<String> errorLines = new ArrayList<>();

                try (BufferedReader reader =
                             Files.newBufferedReader(
                                     path,
                                     StandardCharsets.UTF_8
                             )) {

                    String header = reader.readLine();

                    if (header == null) {
                        throw new IOException(
                                "CSV-файл порожній."
                        );
                    }

                    if (!header.isEmpty()
                            && header.charAt(0) == '\uFEFF') {

                        header = header.substring(1);
                    }

                    if (!EXPECTED_HEADER.equals(header.trim())) {

                        throw new IOException(
                                "Неправильний заголовок CSV.\n"
                                        + "Очікується:\n"
                                        + EXPECTED_HEADER
                        );
                    }

                    List<String> rows = new ArrayList<>();

                    String line;

                    while ((line = reader.readLine()) != null) {

                        if (!line.isBlank()) {
                            rows.add(line);
                        }
                    }

                    int total = rows.size();

                    if (total == 0) {
                        throw new IOException(
                                "CSV-файл не містить записів."
                        );
                    }

                    for (int i = 0; i < total; i++) {

                        int lineNumber = i + 2;

                        try {

                            List<String> fields =
                                    parseCsvLine(rows.get(i));

                            if (fields.size() != 5) {

                                throw new IllegalArgumentException(
                                        "Очікується 5 полів, отримано "
                                                + fields.size()
                                );
                            }

                            String title =
                                    fields.get(0).trim();

                            String description =
                                    fields.get(1).trim();

                            String documentType =
                                    fields.get(2).trim();

                            String fileName =
                                    fields.get(3).trim();

                            String authorText =
                                    fields.get(4).trim();

                            if (title.isBlank()) {
                                throw new IllegalArgumentException(
                                        "Не вказана назва документа."
                                );
                            }

                            if (documentType.isBlank()) {
                                throw new IllegalArgumentException(
                                        "Не вказаний тип документа."
                                );
                            }

                            if (fileName.isBlank()) {
                                throw new IllegalArgumentException(
                                        "Не вказана назва файлу."
                                );
                            }

                            long authorId;

                            try {

                                authorId = Long.parseLong(authorText);

                            } catch (NumberFormatException e) {

                                throw new IllegalArgumentException(
                                        "ID автора повинен бути числом."
                                );
                            }

                            if (authorId <= 0) {

                                throw new IllegalArgumentException(
                                        "ID автора повинен бути більшим за нуль."
                                );
                            }

                            Document document = new Document(
                                    null,
                                    title,
                                    description,
                                    documentType,
                                    fileName,
                                    null,
                                    authorId
                            );

                            apiClient.post(
                                    "/documents",
                                    document
                            );

                            result.successful++;

                            LOGGER.info(
                                    "Імпортовано рядок "
                                            + lineNumber
                            );

                        } catch (Exception e) {

                            String error =
                                    "Рядок "
                                            + lineNumber
                                            + ": "
                                            + e.getMessage();

                            result.errors.add(error);

                            errorLines.add(error);

                            LOGGER.log(
                                    Level.WARNING,
                                    error,
                                    e
                            );
                        }

                        updateProgress(i + 1, total);

                        updateMessage(
                                "Оброблено "
                                        + (i + 1)
                                        + " із "
                                        + total
                                        + ". Успішно: "
                                        + result.successful
                        );
                    }
                }

                if (!errorLines.isEmpty()) {

                    result.errorFile = writeErrorReport(
                            path,
                            errorLines
                    );
                }

                LOGGER.info(
                        "Імпорт завершено. Успішно: "
                                + result.successful
                                + ", помилок: "
                                + result.errors.size()
                );

                return result;
            }
        };

        task.messageProperty().addListener(
                (observable, oldValue, newValue) ->
                        statusLabel.setText(newValue)
        );

        task.progressProperty().addListener(
                (observable, oldValue, newValue) ->
                        progressBar.setProgress(
                                newValue.doubleValue()
                        )
        );

        task.setOnSucceeded(event -> {

            ImportResult result = task.getValue();

            importButton.setDisable(false);

            progressBar.setProgress(1);

            statusLabel.setText(
                    "Імпорт завершено. Успішно: "
                            + result.successful
                            + ", помилок: "
                            + result.errors.size()
            );

            loadDocuments();

            showImportResult(result);
        });

        task.setOnFailed(event -> {

            importButton.setDisable(false);

            progressBar.setProgress(0);

            Throwable error = task.getException();

            String message = error == null
                    ? "Невідома помилка."
                    : error.getMessage();

            statusLabel.setText("Помилка імпорту");

            LOGGER.log(
                    Level.SEVERE,
                    "Помилка імпорту CSV",
                    error
            );

            showError(
                    "Помилка імпорту",
                    message
            );
        });

        Thread thread = new Thread(
                task,
                "csv-import-thread"
        );

        thread.setDaemon(true);

        thread.start();
    }

    private List<String> parseCsvLine(String line) {

        List<String> fields = new ArrayList<>();

        StringBuilder field = new StringBuilder();

        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char current = line.charAt(i);

            if (current == '"') {

                if (insideQuotes
                        && i + 1 < line.length()
                        && line.charAt(i + 1) == '"') {

                    field.append('"');
                    i++;

                } else {

                    insideQuotes = !insideQuotes;
                }

            } else if (current == ',' && !insideQuotes) {

                fields.add(field.toString());

                field.setLength(0);

            } else {

                field.append(current);
            }
        }

        if (insideQuotes) {

            throw new IllegalArgumentException(
                    "Незакриті лапки у CSV."
            );
        }

        fields.add(field.toString());

        return fields;
    }

    private Path writeErrorReport(
            Path csvPath,
            List<String> errors
    ) throws IOException {

        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern(
                        "yyyyMMdd-HHmmss"
                )
        );

        Path report = csvPath.resolveSibling(
                "import-errors-" + timestamp + ".txt"
        );

        try (BufferedWriter writer =
                     Files.newBufferedWriter(
                             report,
                             StandardCharsets.UTF_8
                     )) {

            writer.write("Звіт про помилки імпорту CSV");

            writer.newLine();

            writer.write("Дата: " + LocalDateTime.now());

            writer.newLine();

            writer.newLine();

            for (String error : errors) {

                writer.write(error);

                writer.newLine();
            }
        }

        return report;
    }

    private void showImportResult(ImportResult result) {

        String message =
                "Успішно імпортовано: "
                        + result.successful
                        + "\nПомилок: "
                        + result.errors.size();

        if (result.errorFile != null) {

            message +=
                    "\n\nЗвіт про помилки:\n"
                            + result.errorFile;
        }

        Alert alert = new Alert(
                result.errors.isEmpty()
                        ? Alert.AlertType.INFORMATION
                        : Alert.AlertType.WARNING
        );

        alert.setTitle("Результат імпорту");

        alert.setHeaderText("Імпорт CSV завершено");

        alert.setContentText(message);

        alert.showAndWait();
    }

    private void loadDocuments() {

        try {

            String response = apiClient.get("/documents");

            List<Document> result =
                    apiClient.getObjectMapper().readValue(
                            response,
                            new TypeReference<List<Document>>() {
                            }
                    );

            documents.setAll(result);

        } catch (Exception e) {

            LOGGER.log(
                    Level.SEVERE,
                    "Помилка завантаження документів",
                    e
            );

            showError(
                    "Помилка завантаження документів",
                    e.getMessage()
            );
        }
    }

    private void showError(
            String title,
            String message
    ) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(
                message == null
                        ? "Невідома помилка."
                        : message
        );

        alert.showAndWait();
    }

    private static class ImportResult {

        private int successful = 0;

        private final List<String> errors =
                new ArrayList<>();

        private Path errorFile;
    }
}