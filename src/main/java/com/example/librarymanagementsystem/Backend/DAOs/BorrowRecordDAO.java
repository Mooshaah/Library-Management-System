package com.example.librarymanagementsystem.Backend.DAOs;

import com.example.librarymanagementsystem.Backend.DBConnector;
import com.example.librarymanagementsystem.Backend.Models.*;

import java.sql.*;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

public class BorrowRecordDAO {
    DBConnector dbConnector;
    ArrayList<Book> books;
    MemberDAO memberDAO;
    FineDAO fineDAO;

    public BorrowRecordDAO() {
        dbConnector = new DBConnector();
        books = new ArrayList<>();
        memberDAO = new MemberDAO();
        fineDAO = new FineDAO();
    }

    public void borrowBook(int memberID, Book book) {
        String query = "INSERT INTO borrowing_record (BorrowDate, DueDate, MemberID) VALUES (?, ?, ?)";
        String updateBookAvailability = "UPDATE book SET availability = false WHERE BookID = ?";
        String linkBookRecord = "INSERT INTO book_borrowing_record (BookID, RecordID) VALUES (?, ?)";
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(7);

        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement bookAvailabilityStmt = connection.prepareStatement(updateBookAvailability);
             PreparedStatement bookRecordStmt = connection.prepareStatement(linkBookRecord)) {

            // Insert new borrow record
            statement.setDate(1, Date.valueOf(borrowDate));
            statement.setDate(2, Date.valueOf(dueDate));
            statement.setInt(3, memberID);
            statement.executeUpdate();

            // Retrieve the generated RecordID
            int recordID;
            try (ResultSet recordGeneratedKeys = statement.getGeneratedKeys()) {
                if (recordGeneratedKeys.next()) {
                    recordID = recordGeneratedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve the generated RecordID.");
                }
            }

            // Update book availability to false
            bookAvailabilityStmt.setInt(1, book.getId());
            bookAvailabilityStmt.executeUpdate();

            // Link book to borrow record
            bookRecordStmt.setInt(1, book.getId());
            bookRecordStmt.setInt(2, recordID);
            bookRecordStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while borrowing a book", e);
        }
    }

    public void returnBook(Member member, ArrayList<Book> books) {
        String updateBookAvailability = "UPDATE book SET availability = true WHERE BookID = ?";
        String updateBookRecord = """
                UPDATE borrowing_record br
                JOIN book_borrowing_record bbr ON br.RecordID = bbr.RecordID
                SET br.ReturnDate = ?
                WHERE br.MemberID = ? AND bbr.BookID = ?
                """;
        LocalDate returnDate = LocalDate.now();


        try (Connection connection = dbConnector.connect(); PreparedStatement updateBookRecordStatement = connection.prepareStatement(updateBookRecord); PreparedStatement bookAvailabilityStatement = connection.prepareStatement(updateBookAvailability)) {
            for (Book book : books) {
                updateBookRecordStatement.setDate(1, Date.valueOf(returnDate));
                updateBookRecordStatement.setInt(2, member.getId());
                updateBookRecordStatement.setInt(3, book.getId());
                updateBookRecordStatement.executeUpdate();

                bookAvailabilityStatement.setInt(1, book.getId());
                bookAvailabilityStatement.executeUpdate();

                fineDAO.calculateFine(member.getId());

                System.out.println("Book with title {" + book.getTitle() + "} has been returned.");
                System.out.println("Member has to pay $" + member.getPaymentDue() + " as fine.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public ArrayList<Book> getBorrowedBooksByMemberId(int memberId) {
        ArrayList<Book> borrowedBooks = new ArrayList<>();
        String query = "SELECT b.BookID, b.Title, b.Genre, b.PublicationDate, b.Availability, a.AuthorID, a.FirstName, a.LastName " + "FROM book b " + "JOIN book_author ba ON b.BookID = ba.BookID " + "JOIN author a ON ba.AuthorID = a.AuthorID " + "JOIN book_borrowing_record bbr ON b.BookID = bbr.BookID " + "JOIN borrowing_record br ON bbr.RecordID = br.RecordID " + "WHERE br.MemberID = ? AND br.ReturnDate IS NULL";

        try (Connection connection = dbConnector.connect(); PreparedStatement statement = connection.prepareStatement(query)) {
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

    public List<BorrowRecord> getBorrowedRecordsByMemberId(int memberId) {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        String query = """
                SELECT br.RecordID, br.BorrowDate, br.DueDate, b.BookID, b.Title, b.Genre, b.PublicationDate, b.Availability,
                       a.AuthorID, a.FirstName AS AuthorFirstName, a.LastName AS AuthorLastName
                FROM borrowing_record br
                JOIN book_borrowing_record bbr ON br.RecordID = bbr.RecordID
                JOIN book b ON bbr.BookID = b.BookID
                JOIN book_author ba ON b.BookID = ba.BookID
                JOIN author a ON ba.AuthorID = a.AuthorID
                WHERE br.MemberID = ? AND br.ReturnDate IS NULL
                """;

        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, memberId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Fetch borrow record details
                    int recordId = resultSet.getInt("RecordID");
                    Date borrowDate = resultSet.getDate("BorrowDate");
                    Date dueDate = resultSet.getDate("DueDate");

                    // Fetch book details
                    int bookId = resultSet.getInt("BookID");
                    String title = resultSet.getString("Title");
                    String genre = resultSet.getString("Genre");
                    String publicationDate = resultSet.getString("PublicationDate");
                    boolean availability = resultSet.getBoolean("Availability");

                    // Fetch author details
                    int authorId = resultSet.getInt("AuthorID");
                    String authorFirstName = resultSet.getString("AuthorFirstName");
                    String authorLastName = resultSet.getString("AuthorLastName");
                    Author author = new Author(authorId, authorFirstName, authorLastName);

                    // Create book object
                    Book book = new Book(bookId, publicationDate, title, genre, author, availability);

                    // Create BorrowRecord
                    BorrowRecord borrowRecord = new BorrowRecord(recordId, borrowDate, dueDate, book);
                    borrowRecords.add(borrowRecord);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching borrowed records for member ID: " + memberId, e);
        }

        return borrowRecords;
    }
}