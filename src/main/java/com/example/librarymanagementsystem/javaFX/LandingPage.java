package com.example.librarymanagementsystem.javaFX;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.example.librarymanagementsystem.javaFX.UIComponents.*;

public class LandingPage {
    private final Stage stage;

    public LandingPage(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        Label heading = createLabel("Welcome to UofCanada Library", 24);
        Button librarianButton = createButton("Login as Librarian", e -> {
            LoginPage loginPage = new LoginPage(stage, LibraryApp.LIBRARIAN);
            loginPage.show();
        });
        Button memberButton = createButton("Login as Member", e -> {
            LoginPage loginPage = new LoginPage(stage, LibraryApp.MEMBER);
            loginPage.show();
        });

        VBox layout = createVBox(20, heading, librarianButton, memberButton);
        setScene(stage, layout, "Library Management System");
    }
}
