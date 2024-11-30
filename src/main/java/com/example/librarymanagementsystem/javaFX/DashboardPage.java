package com.example.librarymanagementsystem.javaFX;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.example.librarymanagementsystem.javaFX.UIComponents.*;

public class DashboardPage {
    private final Stage stage;
    private final String userType;

    public DashboardPage(Stage stage, String userType) {
        this.stage = stage;
        this.userType = userType;
    }

    public void show() {
        Label heading = createLabel("Welcome to the " + userType + " Dashboard!", 20);
        Button logoutButton = createButton("Logout", e -> {
            LandingPage landingPage = new LandingPage(stage);
            landingPage.show();
        });

        VBox layout = createVBox(20, heading, logoutButton);
        setScene(stage, layout, userType + " Dashboard");
    }
}
