package com.example.librarymanagementsystem.javaFX;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class UIComponents {
    public static Label createLabel(String text, int fontSize) {
        Label label = new Label(text);
        label.setFont(new Font(fontSize));
        return label;
    }

    public static TextField createTextField(String promptText, double maxWidth) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setMaxWidth(maxWidth);
        return textField;
    }

    public static PasswordField createPasswordField(String promptText, double maxWidth) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(promptText);
        passwordField.setMaxWidth(maxWidth);
        return passwordField;
    }

    public static Button createButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.setOnAction(handler);
        return button;
    }

    public static Button createTextButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = createButton(text, handler);
        button.setTextFill(Color.BLUE);
        button.setUnderline(true);
        button.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        return button;
    }

    public static VBox createVBox(double spacing, javafx.scene.Node... children) {
        VBox vbox = new VBox(spacing, children);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);
        return vbox;
    }

    public static void setScene(Stage stage, VBox layout, String title) {
        Scene scene = new Scene(layout, 400, 300);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
