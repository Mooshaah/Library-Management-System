package com.example.librarymanagementsystem.javaFX;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LandingPage {
    private final Stage stage;

    public LandingPage(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        Label heading = new Label("Welcome to UofCanada Library");
        heading.setStyle("-fx-font-size: 24px; -fx-padding: 10px;");

        Button librarianButton = new Button("Login as Librarian");
        librarianButton.setOnAction(e -> {
            LoginPage loginPage = new LoginPage(stage, LibraryApp.LIBRARIAN);
            loginPage.show();
        });

        Button memberButton = new Button("Login as Member");
        memberButton.setOnAction(e -> {
            LoginPage loginPage = new LoginPage(stage, LibraryApp.MEMBER);
            loginPage.show();
        });

        VBox layout = new VBox(20, heading, librarianButton, memberButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20px;");

        Scene scene = new Scene(layout, 400, 300);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }
}
