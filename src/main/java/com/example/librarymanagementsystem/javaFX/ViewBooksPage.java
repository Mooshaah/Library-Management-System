package com.example.librarymanagementsystem.javaFX;

import com.example.librarymanagementsystem.Backend.Models.Book;
import com.example.librarymanagementsystem.Backend.DAOs.LibrarianDAO;
import com.example.librarymanagementsystem.Backend.Models.Librarian;
import com.example.librarymanagementsystem.Backend.Models.User;
import com.example.librarymanagementsystem.javaFX.Librarian.LibrarianDashboardPage;
import com.example.librarymanagementsystem.javaFX.Member.MemberDashboardPage;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ViewBooksPage {
    private final Stage stage;
    private final LibrarianDAO librarianDAO;
    private final User user;

    public ViewBooksPage(Stage stage, User user) {
        this.stage = stage;
        this.librarianDAO = new LibrarianDAO();
        this.user = user;
    }

    public void show() {
        stage.setTitle("View Books");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        TableView<Book> bookTable = new TableView<>();
        bookTable.setPrefHeight(400);

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

        bookTable.getColumns().addAll(titleColumn, genreColumn, availabilityColumn, authorColumn);

        List<Book> books = librarianDAO.getAllBooks();
        ObservableList<Book> bookList = FXCollections.observableArrayList(books);
        bookTable.setItems(bookList);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by Title");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Book> filteredBooks = FXCollections.observableArrayList();
            for (Book book : books) {
                if (book.getTitle().toLowerCase().contains(newValue.toLowerCase())) {
                    filteredBooks.add(book);
                }
            }
            bookTable.setItems(filteredBooks);
        });

        Button backButton = new Button("Back");
        if (user instanceof Librarian) {
            backButton.setOnAction(event -> new LibrarianDashboardPage(stage, user).show());
        }else {
            backButton.setOnAction(event -> new MemberDashboardPage(stage, user).show());
        }

        layout.getChildren().addAll(searchField, bookTable, backButton);

        Scene scene = new Scene(layout, 600, 550);
        stage.setScene(scene);
        stage.show();
    }
}
