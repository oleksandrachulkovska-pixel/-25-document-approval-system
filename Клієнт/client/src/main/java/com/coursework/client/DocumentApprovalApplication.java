package com.coursework.client;

import com.coursework.client.controller.AuthorController;
import com.coursework.client.controller.DocumentController;
import com.coursework.client.controller.ReviewerController;
import com.coursework.client.controller.ApprovalRequestController;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DocumentApprovalApplication extends Application {

    @Override
    public void start(Stage primaryStage) {

        Label title = new Label(
                "ІНФОРМАЦІЙНА СИСТЕМА ПОГОДЖЕННЯ ВНУТРІШНІХ ДОКУМЕНТІВ"
        );

        title.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1f3a5f;"
        );

        VBox header = new VBox(title);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #eaf0f6;");

        TabPane tabPane = new TabPane();

        Tab authorsTab = new Tab("Автори");
        Tab documentsTab = new Tab("Документи");
        Tab reviewersTab = new Tab("Рецензенти");
        Tab requestsTab = new Tab("Запити на погодження");

        authorsTab.setClosable(false);
        documentsTab.setClosable(false);
        reviewersTab.setClosable(false);
        requestsTab.setClosable(false);

        authorsTab.setContent(
                new AuthorController().createView()
        );

        documentsTab.setContent(
                new DocumentController().createView()
        );

        reviewersTab.setContent(
                new ReviewerController().createView()
        );

        requestsTab.setContent(
                new ApprovalRequestController().createView()
        );

        tabPane.getTabs().addAll(
                authorsTab,
                documentsTab,
                reviewersTab,
                requestsTab
        );

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 1000, 650);

        primaryStage.setTitle(
                "Система погодження внутрішніх документів"
        );

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}