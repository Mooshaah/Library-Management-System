package com.example.librarymanagementsystem;

import com.example.librarymanagementsystem.Backend.Member;
import com.example.librarymanagementsystem.Backend.MemberDOA;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SignUpFormApp extends Application {
    private MemberDOA member = new MemberDOA();
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sign Up Form");

        // Create a grid pane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        // Add labels and text fields
        Label firstNameLabel = new Label("First Name:");
        grid.add(firstNameLabel, 0, 0);

        TextField firstNameField = new TextField();
        grid.add(firstNameField, 1, 0);

        Label lastNameLabel = new Label("Last Name:");
        grid.add(lastNameLabel, 0, 1);

        TextField lastNameField = new TextField();
        grid.add(lastNameField, 1, 1);

        Label phoneLabel = new Label("Phone Number:");
        grid.add(phoneLabel, 0, 2);

        TextField phoneField = new TextField();
        grid.add(phoneField, 1, 2);

        Label emailLabel = new Label("Email:");
        grid.add(emailLabel, 0, 3);

        TextField emailField = new TextField();
        grid.add(emailField, 1, 3);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 4);

        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 4);

        Label typeLabel = new Label("Type:");
        grid.add(typeLabel, 0, 5);

        TextField typeField = new TextField();
        grid.add(typeField, 1, 5);

        Label departmentLabel = new Label("Department:");
        grid.add(departmentLabel, 0, 6);

        TextField departmentField = new TextField();
        grid.add(departmentField, 1, 6);

        // Add a submit button
        Button submitButton = new Button("Submit");
        grid.add(submitButton, 1, 7);

        // Add action to the submit button
        submitButton.setOnAction(event -> {
            System.out.println("Form Submitted");
            Member member1 = new Member(firstNameField.getText(), lastNameField.getText(),phoneField.getText(),emailField.getText(),passwordField.getText(),typeField.getText(),departmentField.getText());
            member.CreateMember(member1);
            //Testing purposes
//            System.out.println("First Name: " + firstNameField.getText());
//            System.out.println("Last Name: " + lastNameField.getText());
//            System.out.println("Phone Number: " + phoneField.getText());
//            System.out.println("Email: " + emailField.getText());
//            System.out.println("Password: " + passwordField.getText());
//            System.out.println("Type: " + typeField.getText());
//            System.out.println("Department: " + departmentField.getText());

        });

        // Create the scene
        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setScene(scene);

        // Display the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}