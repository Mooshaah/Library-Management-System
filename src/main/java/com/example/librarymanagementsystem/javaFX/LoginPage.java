package com.example.librarymanagementsystem.javaFX;

import com.example.librarymanagementsystem.Backend.LibrarianDAO;
import com.example.librarymanagementsystem.Backend.MemberDAO;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static com.example.librarymanagementsystem.javaFX.UIComponents.*;

public class LoginPage {
    private final Stage stage;
    private final String userType;
    private final MemberDAO memberDAO = new MemberDAO();
    private final LibrarianDAO librarianDAO = new LibrarianDAO();

    public LoginPage(Stage stage, String userType) {
        this.stage = stage;
        this.userType = userType;
    }

    public void show() {
        Label heading = createLabel("Login as " + userType, 20);
        TextField emailField = createTextField("Email", 250);
        PasswordField passwordField = createPasswordField("Password", 250);
        Label statusLabel = createLabel("", 14);

        Button loginButton = createButton("Login", e -> handleLogin(emailField.getText(), passwordField.getText(), statusLabel));
        Button backButton = createButton("Back", e -> new LandingPage(stage).show());

        VBox layout = createVBox(10, heading, emailField, passwordField, loginButton, statusLabel);

        if (LibraryApp.MEMBER.equals(userType)) {
            Label signupPrompt = createLabel("Don't have an account?", 12);
            Button signupButton = createTextButton("Sign up now!", e -> new SignUpPage(stage).show());
            layout.getChildren().addAll(signupPrompt, signupButton);
        }

        layout.getChildren().add(backButton);
        setScene(stage, layout, userType + " Login");
    }

    private void handleLogin(String email, String password, Label statusLabel) {
        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Please enter both email and password.");
            return;
        }

        boolean isAuthenticated;
        if (LibraryApp.MEMBER.equals(userType)) {
            isAuthenticated = memberDAO.checkMemberEmailAndPassword(email, password);
        } else {
            isAuthenticated = librarianDAO.checkLibrarianEmailAndPassword(email, password);
        }

        if (!isAuthenticated) {
            statusLabel.setTextFill(Color.RED);
            statusLabel.setText("Invalid email or password.");
            return;
        }

        statusLabel.setTextFill(Color.GREEN);
        statusLabel.setText("Login successful!");

        if (LibraryApp.MEMBER.equals(userType)) {
            new DashboardPage(stage, LibraryApp.MEMBER).show();
        } else {
            new DashboardPage(stage, LibraryApp.LIBRARIAN).show();
        }
    }
}
