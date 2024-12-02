package com.example.librarymanagementsystem.Backend.DAOs;

import com.example.librarymanagementsystem.Backend.DBConnector;
import com.example.librarymanagementsystem.Backend.Models.Author;
import com.example.librarymanagementsystem.Backend.Models.Book;
import com.example.librarymanagementsystem.Backend.Models.Member;

import java.sql.*;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

public class BorrowRecordDAO {
    DBConnector dbConnector;
    ArrayList<Book> books;
    MemberDAO memberDAO;

    public BorrowRecordDAO() {
        dbConnector = new DBConnector();
        books = new ArrayList<>();
        memberDAO = new MemberDAO();
    }

    public void borrowBook(int memberID, ArrayList<Book> books) {
        String query = "INSERT INTO borrowing_record (BorrowDate, DueDate, MemberID) VALUES (?, ?, ?)";
        String updateBookAvailability = "UPDATE book SET availability = false WHERE BookID = ?";
        String linkBookRecord = "INSERT INTO book_borrowing_record(BookID, RecordID) VALUES (?, ?)";
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(7);

        try (Connection connection = dbConnector.connect(); PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement bookAvailabilityStmt = connection.prepareStatement(updateBookAvailability);
             PreparedStatement bookRecordStmt = connection.prepareStatement(linkBookRecord)) {
            statement.setDate(1, Date.valueOf(borrowDate));
            statement.setDate(2, Date.valueOf(dueDate));
            statement.setInt(3, memberID);
            statement.executeUpdate();

            int recordID;
            try (ResultSet recordGeneratedKeys = statement.getGeneratedKeys()) {
                if (recordGeneratedKeys.next()) {
                    recordID = recordGeneratedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve the generated RecordID.");
                }
            }

            // iterate over books and set availability to false;
            for (Book book : books) {
                bookAvailabilityStmt.setInt(1, book.getId());
                bookAvailabilityStmt.executeUpdate();

                bookRecordStmt.setInt(1, book.getId());
                bookRecordStmt.setInt(2, recordID);
                bookRecordStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    public void returnBook(Member member, ArrayList<Book> books) {
        String updateBookAvailability = "UPDATE book SET availability = true WHERE BookID = ?";
        String updateBookRecord = "UPDATE borrowing_record br " +
                "JOIN book_borrowing_record bbr ON br.RecordID = bbr.RecordID " +
                "SET br.ReturnDate = ? WHERE br.MemberID = ? AND bbr.BookID = ?";
        LocalDate returnDate = LocalDate.now();
        try (Connection connection = dbConnector.connect();
             PreparedStatement updateBookRecordStatement = connection.prepareStatement(updateBookRecord);
             PreparedStatement bookAvailabilityStatement = connection.prepareStatement(updateBookAvailability)) {


            for (Book book : books) {
                updateBookRecordStatement.setDate(1, Date.valueOf(returnDate));
                updateBookRecordStatement.setInt(2, member.getId());
                updateBookRecordStatement.setInt(3, book.getId());
                updateBookRecordStatement.executeUpdate();

                bookAvailabilityStatement.setInt(1, book.getId());
                bookAvailabilityStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public ArrayList<Book> getBorrowedBooksByMemberId(int memberId) {
        ArrayList<Book> borrowedBooks = new ArrayList<>();
        String query = "SELECT b.BookID, b.Title, b.Genre, b.PublicationDate, b.Availability, a.AuthorID, a.FirstName, a.LastName " +
                "FROM book b " +
                "JOIN book_author ba ON b.BookID = ba.BookID " +
                "JOIN author a ON ba.AuthorID = a.AuthorID " +
                "JOIN book_borrowing_record bbr ON b.BookID = bbr.BookID " +
                "JOIN borrowing_record br ON bbr.RecordID = br.RecordID " +
                "WHERE br.MemberID = ? AND br.ReturnDate IS NULL";

        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, memberId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int bookId = resultSet.getInt("BookID");
                String title = resultSet.getString("Title");
                String genre = resultSet.getString("Genre");
                String publicationDate = resultSet.getString("PublicationDate");
                boolean availability = resultSet.getBoolean("Availability");

                int authorId = resultSet.getInt("AuthorID");
                String authorFirstName = resultSet.getString("FirstName");
                String authorLastName = resultSet.getString("LastName");

                Author author = new Author(authorId, authorFirstName, authorLastName);
                borrowedBooks.add(new Book(bookId, publicationDate, title, genre, author, availability));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return borrowedBooks;
    }

    public void calculateFine(int memberID){
        String query = "SELECT ReturnDate,DueDate WHERE MemberID =?";
        try(Connection connection = dbConnector.connect();
        PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, memberID);
        //** Compare the Return date > Due date  and if this condition is true set a fine of 5% on the member
            // w ya philo seeb el function di ana hakhalasha 3la el dohr keda kamil enta f haga tanya w zabat el db schema
            // zawed column lel total price ashan law fi fine ne apply it 3ala el total price **//
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}

