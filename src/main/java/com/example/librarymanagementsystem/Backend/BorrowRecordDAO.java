package com.example.librarymanagementsystem.Backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class BorrowRecordDAO {
    DBConnector dbConnector;
    ArrayList<Book> books;
    MemberDAO memberDAO;

    public BorrowRecordDAO() {
        dbConnector = new DBConnector();
        books = new ArrayList<>();
        memberDAO = new MemberDAO();
    }

    public void borrowBook(Member member, ArrayList<Book> books) {
        String updateBookQuery = "UPDATE book SET availability = false WHERE BookID = ?";
        String insertBookQuery = "INSERT INTO borrow_record (BorrowDate, ReturnDate, DueDate, LibrarianID, MemberID) VALUES (?, ?, ?, ?, ?)";
        String bookQuery = "SELECT Title FROM book WHERE Title= ?";
        String memberQuery = "SELECT FirstName, LastName FROM member WHERE  FirstName = ?, LastName= ?";
        try (Connection connection = dbConnector.connect();
             PreparedStatement updateBookStatement = connection.prepareStatement(updateBookQuery);
             PreparedStatement memberStatement = connection.prepareStatement(memberQuery)) {
            // Rest of code goes here

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
