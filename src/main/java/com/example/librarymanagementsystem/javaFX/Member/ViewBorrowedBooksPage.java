package com.example.librarymanagementsystem.javaFX.Member;

import com.example.librarymanagementsystem.Backend.DAOs.BorrowRecordDAO;
import com.example.librarymanagementsystem.Backend.Models.Book;
import com.example.librarymanagementsystem.Backend.Models.BorrowRecord;
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

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewBorrowedBooksPage {
    private final Stage stage;
    private final BorrowRecordDAO borrowRecordDAO;
    private final User user;
    private final Map<Book, BorrowRecord> bookRecordMap = new HashMap<>();

    public ViewBorrowedBooksPage(Stage stage, User user) {
        this.stage = stage;
        this.borrowRecordDAO = new BorrowRecordDAO();
        this.user = user;
    }

    public void show() {
        stage.setTitle("View Borrowed Books");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        TableView<Book> bookTable = new TableView<>();
        bookTable.setPrefHeight(400);

        // Columns for Book details
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAuthor().toString()));

        TableColumn<Book, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGenre()));

        // Columns for BorrowRecord details
        TableColumn<Book, String> borrowDateColumn = new TableColumn<>("Borrow Date");
        borrowDateColumn.setCellValueFactory(data -> {
            BorrowRecord record = bookRecordMap.get(data.getValue());
            return new SimpleStringProperty(record != null ?
                    new SimpleDateFormat("yyyy-MM-dd").format(record.getBorrowDate()) : "");
        });

        TableColumn<Book, String> dueDateColumn = new TableColumn<>("Due Date");
        dueDateColumn.setCellValueFactory(data -> {
            BorrowRecord record = bookRecordMap.get(data.getValue());
            return new SimpleStringProperty(record != null ?
                    new SimpleDateFormat("yyyy-MM-dd").format(record.getDueDate()) : "");
        });

        TableColumn<Book, String> overdueColumn = new TableColumn<>("Overdue Days");
        overdueColumn.setCellValueFactory(data -> {
            BorrowRecord record = bookRecordMap.get(data.getValue());
            record.calculateOverdueDays(record.getReturnDate());
            return new SimpleStringProperty(record != null ?
                    String.valueOf(record.getOverdueDays()) : "0");
        });

        // Add all columns to the table
        bookTable.getColumns().addAll(titleColumn, authorColumn, genreColumn, borrowDateColumn, dueDateColumn, overdueColumn);

        // Fetch Borrow Records and populate bookRecordMap
        List<BorrowRecord> borrowedRecords = borrowRecordDAO.getBorrowedRecordsByMemberId(user.getId());
        ObservableList<Book> bookList = FXCollections.observableArrayList();

        for (BorrowRecord record : borrowedRecords) {
            Book book = record.getBook();
            bookRecordMap.put(book, record);
            bookList.add(book);
        }

        bookTable.setItems(bookList);

        // Back Button
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> new MemberDashboardPage(stage, user).show());

        layout.getChildren().addAll(bookTable, backButton);

        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}