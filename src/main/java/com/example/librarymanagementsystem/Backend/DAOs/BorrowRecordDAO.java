package com.example.librarymanagementsystem.Backend.DAOs;

import com.example.librarymanagementsystem.Backend.DBConnector;
import com.example.librarymanagementsystem.Backend.Models.Author;
import com.example.librarymanagementsystem.Backend.Models.Book;
import com.example.librarymanagementsystem.Backend.Models.BorrowRecord;
import com.example.librarymanagementsystem.Backend.Models.Member;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowRecordDAO {
    DBConnector dbConnector;
    FineDAO fineDAO;

    public BorrowRecordDAO() {
        dbConnector = new DBConnector();
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

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public ArrayList<Book> getBorrowedBooksByMemberId(int memberId) {
        ArrayList<Book> borrowedBooks = new ArrayList<>();
        String query = """
                SELECT b.BookID, b.Title, b.Genre, b.PublicationDate, b.Availability, a.AuthorID, a.FirstName, a.LastName
                FROM book b
                JOIN book_author ba ON b.BookID = ba.BookID
                JOIN author a ON ba.AuthorID = a.AuthorID
                JOIN book_borrowing_record bbr ON b.BookID = bbr.BookID
                JOIN borrowing_record br ON bbr.RecordID = br.RecordID
                WHERE br.MemberID = ? AND br.ReturnDate IS NULL
                """;

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
                SELECT br.RecordID, br.BorrowDate, br.DueDate, br.ReturnDate, b.BookID, b.Title, b.Genre, b.PublicationDate, b.Availability,
                       a.AuthorID, a.FirstName AS AuthorFirstName, a.LastName AS AuthorLastName
                FROM borrowing_record br
                JOIN book_borrowing_record bbr ON br.RecordID = bbr.RecordID
                JOIN book b ON bbr.BookID = b.BookID
                JOIN book_author ba ON b.BookID = ba.BookID
                JOIN author a ON ba.AuthorID = a.AuthorID
                WHERE br.MemberID = ?
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
                    Date returnDate = resultSet.getDate("ReturnDate");

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
                    BorrowRecord borrowRecord = new BorrowRecord(recordId, borrowDate, dueDate, book, null);
                    borrowRecord.setReturnDate(returnDate);
                    borrowRecords.add(borrowRecord);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching borrowed records for member ID: " + memberId, e);
        }

        return borrowRecords;
    }

    public List<BorrowRecord> getAllBorrowRecords() {
        List<BorrowRecord> borrowRecords = new ArrayList<>();
        String query = """
                SELECT br.RecordID, br.BorrowDate, br.DueDate, br.ReturnDate, 
                       b.BookID, b.Title, b.Genre, b.PublicationDate, b.Availability,
                       a.AuthorID, a.FirstName AS AuthorFirstName, a.LastName AS AuthorLastName,
                       m.MemberID, m.FirstName AS MemberFirstName, m.LastName AS MemberLastName, 
                       m.PhoneNumber, m.Email, m.Type, m.Department, m.PaymentDue
                FROM borrowing_record br
                JOIN book_borrowing_record bbr ON br.RecordID = bbr.RecordID
                JOIN book b ON bbr.BookID = b.BookID
                JOIN book_author ba ON b.BookID = ba.BookID
                JOIN author a ON ba.AuthorID = a.AuthorID
                JOIN member m ON br.MemberID = m.MemberID
                """;

        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int recordId = resultSet.getInt("RecordID");
                Date borrowDate = resultSet.getDate("BorrowDate");
                Date dueDate = resultSet.getDate("DueDate");
                Date returnDate = resultSet.getDate("ReturnDate");

                int bookId = resultSet.getInt("BookID");
                String title = resultSet.getString("Title");
                String genre = resultSet.getString("Genre");
                String publicationDate = resultSet.getString("PublicationDate");
                boolean availability = resultSet.getBoolean("Availability");

                int authorId = resultSet.getInt("AuthorID");
                String authorFirstName = resultSet.getString("AuthorFirstName");
                String authorLastName = resultSet.getString("AuthorLastName");
                Author author = new Author(authorId, authorFirstName, authorLastName);

                Book book = new Book(bookId, publicationDate, title, genre, author, availability);

                int memberId = resultSet.getInt("MemberID");
                String memberFirstName = resultSet.getString("MemberFirstName");
                String memberLastName = resultSet.getString("MemberLastName");
                String phoneNumber = resultSet.getString("PhoneNumber");
                String email = resultSet.getString("Email");
                String type = resultSet.getString("Type");
                String department = resultSet.getString("Department");
                double paymentDue = resultSet.getDouble("PaymentDue");

                Member member = new Member(memberId, memberFirstName, memberLastName, phoneNumber, email, null, type, department, paymentDue);

                BorrowRecord borrowRecord = new BorrowRecord(recordId, borrowDate, dueDate, book, member);
                borrowRecord.setReturnDate(returnDate);
                borrowRecords.add(borrowRecord);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching all borrow records", e);
        }

        return borrowRecords;
    }
}