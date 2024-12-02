package com.example.librarymanagementsystem.javaFX.Member;

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

public class MemberDashboardPage {
    private final Stage stage;
    private final User user;

    public MemberDashboardPage(Stage stage, User user) {
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

        Button viewBooksButton = new Button("View Books");
        Button borrowBooksButton = new Button("Borrow Books");
        Button payFineButton = new Button("Pay Fine");
        Button returnBookButton = new Button("Return Book");

        viewBooksButton.setPrefSize(120, 40);
        borrowBooksButton.setPrefSize(120, 40);
        payFineButton.setPrefSize(120, 40);
        returnBookButton.setPrefSize(120, 40);

        viewBooksButton.setOnAction(e -> new ViewBooksPage(stage, user, "Member").show());
        borrowBooksButton.setOnAction(e -> new BorrowBookPage(stage, user).show());
        payFineButton.setOnAction(e -> System.out.println("Pay Fine button clicked."));
        returnBookButton.setOnAction(e -> new ReturnBookPage(stage, user).show());

        buttonGrid.add(viewBooksButton, 0, 0);
        buttonGrid.add(borrowBooksButton, 1, 0);
        buttonGrid.add(payFineButton, 0, 1);
        buttonGrid.add(returnBookButton, 1, 1);

        BorderPane layout = new BorderPane();
        layout.setTop(greetingLabel);
        layout.setCenter(buttonGrid);
        BorderPane.setAlignment(greetingLabel, Pos.TOP_LEFT);

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Member Dashboard");
        stage.show();
    }
}
