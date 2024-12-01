package com.example.librarymanagementsystem.javaFX.Member;

import com.example.librarymanagementsystem.Backend.Models.Book;
import com.example.librarymanagementsystem.Backend.DAOs.LibrarianDAO;
import com.example.librarymanagementsystem.javaFX.DashboardPage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

public class ViewBooksPage {
    private final Stage stage;
    private final LibrarianDAO librarianDAO;
    private final String memberName;

    public ViewBooksPage(Stage stage, String memberName) {
        this.stage = stage;
        this.librarianDAO = new LibrarianDAO();
        this.memberName = memberName;
    }

    public void show() {
        stage.setTitle("View Books");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        // TableView for displaying books
        TableView<Book> bookTable = new TableView<>();
        bookTable.setPrefHeight(400);

        // Define columns for TableView
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));

        TableColumn<Book, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGenre()));

        TableColumn<Book, String> availabilityColumn = new TableColumn<>("Availability");
        availabilityColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getAvailability() ? "Available" : "Not Available"));

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getAuthor().getFirstName() + " " + data.getValue().getAuthor().getLastName()));

        // Add columns to the table
        bookTable.getColumns().addAll(titleColumn, genreColumn, availabilityColumn, authorColumn);

        // Fetch books from the database
        ArrayList<Book> books = librarianDAO.getAllBooks();
        ObservableList<Book> bookList = FXCollections.observableArrayList(books);
        bookTable.setItems(bookList);

        // Search bar for filtering books by title
        TextField searchField = new TextField();
        searchField.setPromptText("Search by Title");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Create a filtered list manually
            ObservableList<Book> filteredBooks = FXCollections.observableArrayList();
            for (Book book : books) {
                if (book.getTitle().toLowerCase().contains(newValue.toLowerCase())) {
                    filteredBooks.add(book);
                }
            }
            // Update the TableView with the filtered list
            bookTable.setItems(filteredBooks);
        });

        // Back button to return to the previous page
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> new DashboardPage(stage, "Member", memberName).show());

        layout.getChildren().addAll(searchField, bookTable, backButton);

        Scene scene = new Scene(layout, 600, 550);
        stage.setScene(scene);
        stage.show();
    }
}
