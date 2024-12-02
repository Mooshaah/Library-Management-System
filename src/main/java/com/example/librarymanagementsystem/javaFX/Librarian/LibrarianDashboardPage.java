package com.example.librarymanagementsystem.javaFX.Librarian;

import com.example.librarymanagementsystem.Backend.Models.User;
import com.example.librarymanagementsystem.javaFX.ViewBooksPage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LibrarianDashboardPage {
    private final Stage stage;
    private final User user;

    public LibrarianDashboardPage(Stage stage, User user) {
        this.stage = stage;
        this.user = user;
    }

    public void show() {
        Label greetingLabel = new Label("Hello, " + user.getFirstName() + "!");
        greetingLabel.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");

        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(20);
        buttonGrid.setVgap(20);
        buttonGrid.setAlignment(Pos.CENTER);
        buttonGrid.setPadding(new Insets(20));

        Button addBookButton = new Button("Add Book");
        Button removeBookButton = new Button("Remove Book");
        Button viewBooksButton = new Button("View Books");
        Button removeMemberButton = new Button("Remove Member");
        Button viewReportsButton = new Button("View Reports");

        addBookButton.setPrefSize(120, 40);
        removeBookButton.setPrefSize(120, 40);
        viewBooksButton.setPrefSize(120, 40);
        removeMemberButton.setPrefSize(120, 40);
        viewReportsButton.setPrefSize(120, 40);

        addBookButton.setOnAction(e -> new AddBookPage(stage, user).show());
        removeBookButton.setOnAction(e -> new RemoveBookPage(stage, user).show());
        viewBooksButton.setOnAction(e -> new ViewBooksPage(stage, user, "Librarian").show());
        removeMemberButton.setOnAction(e -> System.out.println("Remove Member button clicked."));
        viewReportsButton.setOnAction(e -> System.out.println("View Reports button clicked."));

        buttonGrid.add(addBookButton, 0, 0);
        buttonGrid.add(removeBookButton, 1, 0);
        buttonGrid.add(viewBooksButton, 0, 1);
        buttonGrid.add(removeMemberButton, 1, 1);
        buttonGrid.add(viewReportsButton, 0, 2);

        BorderPane layout = new BorderPane();
        layout.setTop(greetingLabel);
        layout.setCenter(buttonGrid);
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Librarian Dashboard");
        stage.show();
    }
}
