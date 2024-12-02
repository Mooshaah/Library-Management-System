package com.example.librarymanagementsystem.javaFX;

import com.example.librarymanagementsystem.Backend.Models.User;
import com.example.librarymanagementsystem.javaFX.Librarian.AddBookPage;
import com.example.librarymanagementsystem.javaFX.Librarian.RemoveBookPage;
import com.example.librarymanagementsystem.javaFX.Member.BorrowBookPage;
import com.example.librarymanagementsystem.javaFX.Member.ReturnBookPage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class DashboardPage {
    private final Stage stage;
    private final String userType;
    private final User user;

    public DashboardPage(Stage stage, String userType, User user) {
        this.stage = stage;
        this.userType = userType;
        this.user = user;
    }

    public void show() {
        // Create a label for the greeting
        Label greetingLabel = new Label("Hello, " + user.getFirstName() + "!");
        greetingLabel.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");

        // Create a grid layout for buttons
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(20); // Horizontal gap between buttons
        buttonGrid.setVgap(20); // Vertical gap between buttons
        buttonGrid.setAlignment(Pos.CENTER);
        buttonGrid.setPadding(new Insets(20)); // Padding around the grid

        if ("Member".equals(userType)) {
            // Member-specific buttons
            Button viewBooksButton = new Button("View Books");
            Button borrowBooksButton = new Button("Borrow Books");
            Button payFineButton = new Button("Pay Fine");
            Button returnBookButton = new Button("Return Book");

            viewBooksButton.setPrefSize(120, 40);
            borrowBooksButton.setPrefSize(120, 40);
            payFineButton.setPrefSize(120, 40);
            returnBookButton.setPrefSize(120, 40);

            viewBooksButton.setOnAction(e -> viewBooks());
            borrowBooksButton.setOnAction(e -> borrowBooks());
            payFineButton.setOnAction(e -> payFine());
            returnBookButton.setOnAction(e -> returnBook());

            // Arrange buttons in the grid
            buttonGrid.add(viewBooksButton, 0, 0);
            buttonGrid.add(borrowBooksButton, 1, 0);
            buttonGrid.add(payFineButton, 0, 1);
            buttonGrid.add(returnBookButton, 1, 1);
        } else if ("Librarian".equals(userType)) {
            // Librarian-specific buttons
            Button addBookButton = new Button("Add Book");
            Button removeBookButton = new Button("Remove Book");
            Button viewBooksButton = new Button("View Books"); // Added View Books button
            Button removeMemberButton = new Button("Remove Member");
            Button viewReportsButton = new Button("View Reports");

            addBookButton.setPrefSize(120, 40);
            removeBookButton.setPrefSize(120, 40);
            viewBooksButton.setPrefSize(120, 40);
            removeMemberButton.setPrefSize(120, 40);
            viewReportsButton.setPrefSize(120, 40);

            addBookButton.setOnAction(e -> addBook());
            removeBookButton.setOnAction(e -> removeBook());
            viewBooksButton.setOnAction(e -> viewBooks()); // Set action for View Books button
            removeMemberButton.setOnAction(e -> removeMember());
            viewReportsButton.setOnAction(e -> viewReports());

            // Arrange buttons in the grid
            buttonGrid.add(addBookButton, 0, 0);
            buttonGrid.add(removeBookButton, 1, 0);
            buttonGrid.add(viewBooksButton, 0, 1); // Position the View Books button
            buttonGrid.add(removeMemberButton, 1, 1);
            buttonGrid.add(viewReportsButton, 0, 2);
        }

        // Create a BorderPane layout
        BorderPane layout = new BorderPane();
        layout.setTop(greetingLabel);
        layout.setCenter(buttonGrid);
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);

        // Create and set the scene
        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.setTitle(userType + " Dashboard");
        stage.show();
    }

    // Placeholder methods for button actions
    private void viewBooks() {
        new ViewBooksPage(stage, user, userType).show();
    }

    private void borrowBooks() {
        new BorrowBookPage(stage, user).show();
    }

    private void payFine() {
        System.out.println("Pay Fine button clicked.");
    }

    private void returnBook() {
        new ReturnBookPage(stage, user).show();
    }

    private void addBook() {
        new AddBookPage(stage, user).show();
    }

    private void removeBook() {
        new RemoveBookPage(stage, user).show();
    }

    private void removeMember() {
        System.out.println("Remove Member button clicked.");
    }

    private void viewReports() {
        System.out.println("View Reports button clicked.");
    }
}
