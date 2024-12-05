package com.example.librarymanagementsystem.javaFX.Member;

import com.example.librarymanagementsystem.Backend.DAOs.BorrowRecordDAO;
import com.example.librarymanagementsystem.Backend.Models.Book;
import com.example.librarymanagementsystem.Backend.Models.Member;
import com.example.librarymanagementsystem.Backend.Models.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReturnBookPage {
    private final Stage stage;
    private final BorrowRecordDAO borrowRecordDAO;
    private final User user;

    public ReturnBookPage(Stage stage, User user) {
        this.stage = stage;
        this.borrowRecordDAO = new BorrowRecordDAO();
        this.user = user;
    }

    public void show() {
        stage.setTitle("Return Books");

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

        // Fetch borrowed books from the database
        List<Book> borrowedBooks = borrowRecordDAO.getBorrowedBooksByMemberId(user.getId());
        ObservableList<Book> bookList = FXCollections.observableArrayList(borrowedBooks);
        bookTable.setItems(bookList);

        // Allow multiple selection in the TableView
        bookTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Button returnButton = new Button("Return Selected Books");
        returnButton.setOnAction(event -> {
            ObservableList<Book> selectedBooks = bookTable.getSelectionModel().getSelectedItems();
            if (selectedBooks.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "No Books Selected", "Please select at least one book to return.");
                return;
            }

            ArrayList<Book> booksToReturn = new ArrayList<>(selectedBooks);
            LocalDate testReturnDate = LocalDate.of(2024, 12, 14);
            borrowRecordDAO.returnBook((Member) user, booksToReturn);

            // Refresh the table view
            bookList.removeAll(selectedBooks);
            bookTable.setItems(FXCollections.observableArrayList(bookList));

            showAlert(Alert.AlertType.INFORMATION, "Success", "Books returned successfully!");
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> new MemberDashboardPage(stage, user).show());

        layout.getChildren().addAll(bookTable, returnButton, backButton);

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