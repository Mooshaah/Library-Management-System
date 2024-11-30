package com.example.librarymanagementsystem.javaFX;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.example.librarymanagementsystem.javaFX.UIComponents.*;

public class SignUpPage {
    private final Stage stage;

    public SignUpPage(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        Label heading = createLabel("Sign Up Page - Under Construction", 18);
        Button backButton = createButton("Back", e -> {
            LoginPage loginPage = new LoginPage(stage, LibraryApp.MEMBER);
            loginPage.show();
        });

        VBox layout = createVBox(10, heading, backButton);
        setScene(stage, layout, "Sign Up");
    }
}
