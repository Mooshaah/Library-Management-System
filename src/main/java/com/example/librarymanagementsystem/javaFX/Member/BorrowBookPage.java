package com.example.librarymanagementsystem.javaFX.Member;

import com.example.librarymanagementsystem.Backend.DAOs.*;
import com.example.librarymanagementsystem.Backend.Models.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class BorrowBookPage {
    private final Stage stage;
    private final LibrarianDAO librarianDAO;
    private final BorrowRecordDAO borrowRecordDAO;
    private final User user;

    public BorrowBookPage(Stage stage, User user) {
        this.stage = stage;
        this.librarianDAO = new LibrarianDAO();
        this.borrowRecordDAO = new BorrowRecordDAO();
        this.user = user;
    }

    public void show() {
        stage.setTitle("Borrow Books");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        TableView<Book> bookTable = new TableView<>();
        bookTable.setPrefHeight(400);

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));

        TableColumn<Book, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGenre()));

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getAuthor().getFirstName() + " " + data.getValue().getAuthor().getLastName()));

        bookTable.getColumns().addAll(titleColumn, genreColumn, authorColumn);

        List<Book> availableBooks = librarianDAO.getAvailableBooks();
        ObservableList<Book> bookList = FXCollections.observableArrayList(availableBooks);
        bookTable.setItems(bookList);

        Button borrowButton = new Button("Borrow Selected Books");
        borrowButton.setOnAction(event -> {
            ObservableList<Book> selectedBooks = bookTable.getSelectionModel().getSelectedItems();
            if (selectedBooks.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "No Books Selected", "Please select at least one book to borrow.");
                return;
            }
            ArrayList<Book> booksToBorrow = new ArrayList<>(selectedBooks);
            borrowRecordDAO.borrowBook(user.getId(), booksToBorrow);
            bookList.removeAll(selectedBooks);
            bookTable.setItems(FXCollections.observableArrayList(bookList));
            showAlert(Alert.AlertType.INFORMATION, "Success", "Books borrowed successfully!");
        });

        bookTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> new MemberDashboardPage(stage, user).show());

        layout.getChildren().addAll(bookTable, borrowButton, backButton);

        Scene scene = new Scene(layout, 600, 550);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
