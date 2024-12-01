package com.example.librarymanagementsystem.Backend.DAOs;

import com.example.librarymanagementsystem.Backend.DBConnector;
import com.example.librarymanagementsystem.Backend.Models.Book;

import java.sql.*;
import java.util.ArrayList;
import java.time.LocalDate;

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

        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement bookAvailabilityStmt = connection.prepareStatement(updateBookAvailability);
             PreparedStatement bookRecordStmt = connection.prepareStatement(linkBookRecord)
        ) {
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

    public void ReturnBook(int memberID, ArrayList<Book> books) {
        String updateBookAvailablility = "UPDATE book SET availability = true WHERE BookID = ?";
        String updateBookRecord = "UPDATE borrowing_record SET (ReturnDate) WHERE MemberID = ? VALUES (?,?)";
        LocalDate borrowDate = LocalDate.now();
        try(Connection connection = dbConnector.connect();
        PreparedStatement bookAvailabilitystatement = connection.prepareStatement(updateBookAvailablility);
        PreparedStatement updateBookRecordstatement = connection.prepareStatement(updateBookRecord);) {

        } catch (SQLException e ){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

//    public void borrowBook2(Member member, Librarian librarian, ArrayList<Book> books, Date returnDate, Date dueDate) {
//        String updateBookQuery = "UPDATE book SET availability = false WHERE BookID = ?";
//        String bookQuery = "SELECT Title FROM book WHERE Title= ?";
//        String insertRecordQuery = "INSERT INTO borrow_record (BorrowDate, ReturnDate, DueDate, LibrarianID, MemberID) VALUES (?, ?, ?, ?, ?)";
//        String memberQuery = "SELECT FirstName, LastName FROM member WHERE  FirstName = ?, LastName= ?";
//        String bookBorrowingRecord = "INSERT INTO book_borrowing_record (BookID, RecordID) VALUES (?, ?)";
//        String BookIDQuery = "SELECT BookID FROM book_borrowing_record WHERE RecordID = ?";
//        try (Connection connection = dbConnector.connect();
//             PreparedStatement updateBookStatement = connection.prepareStatement(updateBookQuery);
//             PreparedStatement memberStatement = connection.prepareStatement(memberQuery);
//             PreparedStatement insertRecordStatement = connection.prepareStatement(insertRecordQuery, Statement.RETURN_GENERATED_KEYS);
//             PreparedStatement insertBookBorrowingRecordStatement = connection.prepareStatement(insertRecordQuery);
//             PreparedStatement bookStatement = connection.prepareStatement(bookQuery);
//             PreparedStatement bookIDStatement = connection.prepareStatement(bookQuery)) {
//
//
//            insertRecordStatement.setDate(1, Date.valueOf(currnetDate));
//            insertRecordStatement.setDate(2, returnDate);
//            insertRecordStatement.setDate(3, dueDate);
//
//            int librarianID = librarianDAO.getLibrarianIDByName(librarian.getFirstName(), librarian.getLastName());
//            int memberID = memberDAO.getMemberIDByName(member.getFirstName(), member.getLastName());
//            if (librarianID != -1) {
//                insertRecordStatement.setInt(4, librarianDAO.getLibrarianIDByName(librarian.getFirstName(), librarian.getLastName()));
//            }
//
//            if (memberID != -1) {
//                insertRecordStatement.setInt(5, memberDAO.getMemberIDByName(member.getFirstName(), member.getLastName()));
//            }
//
//            int recordID;
//            try (ResultSet recordGeneratedKeys = insertRecordStatement.getGeneratedKeys()) {
//                if (recordGeneratedKeys.next()) {
//                    recordID = recordGeneratedKeys.getInt(1);
//                } else {
//                    throw new SQLException("Failed to retrieve the generated BookID.");
//                }
//            }
//
//            ResultSet rs = bookIDStatement.executeQuery();
//            int bookID = -1;
//            boolean flag = false;
//            while (rs.next()) {
//                flag = true;
//                bookID = rs.getInt("BookID");
//            }
//
//            for (Book book : books) {
//                insertBookBorrowingRecordStatement.setInt(1, bookID);
//                insertBookBorrowingRecordStatement.setInt(2, recordID);
//            }
//
//            insertRecordStatement.executeUpdate();
//            ResultSet updateBookRs = bookStatement.executeQuery();
//            ResultSet memberRs = memberStatement.executeQuery();
//            ResultSet insertBookRs = insertRecordStatement.executeQuery();
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new RuntimeException();
//        }
//    }
}

