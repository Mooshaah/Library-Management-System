package com.example.librarymanagementsystem.Backend.DAOs;

import com.example.librarymanagementsystem.Backend.DBConnector;
import com.example.librarymanagementsystem.Backend.Models.*;

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

        try (Connection connection = dbConnector.connect(); PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); PreparedStatement bookAvailabilityStmt = connection.prepareStatement(updateBookAvailability); PreparedStatement bookRecordStmt = connection.prepareStatement(linkBookRecord)) {
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
        String updateBookRecord = "UPDATE borrowing_record br " + "JOIN book_borrowing_record bbr ON br.RecordID = bbr.RecordID " + "SET br.ReturnDate = ? WHERE br.MemberID = ? AND bbr.BookID = ?";
        LocalDate returnDate = LocalDate.now();


        try (Connection connection = dbConnector.connect(); PreparedStatement updateBookRecordStatement = connection.prepareStatement(updateBookRecord); PreparedStatement bookAvailabilityStatement = connection.prepareStatement(updateBookAvailability)) {


            for (Book book : books) {
                updateBookRecordStatement.setDate(1, Date.valueOf(returnDate));
                updateBookRecordStatement.setInt(2, member.getId());
                updateBookRecordStatement.setInt(3, book.getId());
                updateBookRecordStatement.executeUpdate();

                bookAvailabilityStatement.setInt(1, book.getId());
                bookAvailabilityStatement.executeUpdate();


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


    public void calculateFine(int memberID) {
        Member member = memberDAO.getMemberById(memberID);
        String selectRecordQuery = "SELECT RecordID,BorrowDate,ReturnDate,DueDate FROM borrowing_record WHERE MemberID =?";
        String selectFineQuery = "SELECT * FROM fine WHERE MemberID = ?";
        String updateFineQuery = "UPDATE fine SET amount = ? WHERE MemberID = ?";
        String insertFineQuery = "INSERT INTO fine (Amount, isPaid, MemberID) VALUES (?,?,?)";
        String updateMemberQuery = "UPDATE member SET PaymentDue = ? WHERE MemberID = ?";

        try (Connection connection = dbConnector.connect(); PreparedStatement selectRecord = connection.prepareStatement(selectRecordQuery); PreparedStatement selectFine = connection.prepareStatement(selectFineQuery); PreparedStatement insertFine = connection.prepareStatement(insertFineQuery); PreparedStatement updateMember = connection.prepareStatement(updateMemberQuery); PreparedStatement updateFine = connection.prepareStatement(updateFineQuery)) {

            selectRecord.setInt(1, memberID);
            ResultSet resultSet = selectRecord.executeQuery();

            while (resultSet.next()) {
                int recordID = resultSet.getInt("RecordID");
                Date borrowDate = resultSet.getDate("BorrowDate");
                Date returnDate = resultSet.getDate("ReturnDate");
                Date dueDate = resultSet.getDate("DueDate");
                BorrowRecord borrowRecord = new BorrowRecord(recordID, borrowDate, dueDate);
                int overdueDays = borrowRecord.calculateOverdueDays(returnDate);

                if (overdueDays != 0) {
                    double amount = getAmount(overdueDays);

                    selectFine.setInt(1, memberID);
                    ResultSet fineResultSet = selectFine.executeQuery();

                    if (fineResultSet.next()) {
                        double currentAmount = fineResultSet.getDouble("Amount");
                        double updatedAmount = currentAmount + amount;
                        updateFine.setDouble(1, updatedAmount);
                        updateFine.setInt(2, memberID);
                        updateFine.executeUpdate();
                        System.out.println("Updated fine for MemberID: " + memberID + " to $" + updatedAmount);
                    } else {
                        insertFine.setDouble(1, amount);
                        insertFine.setBoolean(2, false);
                        insertFine.setInt(3, memberID);
                        insertFine.executeUpdate();
                        System.out.println("Inserted fine for MemberID: " + memberID + " with amount $" + amount);
                    }

                    member.addFine(amount);
                    updateMember.setDouble(1, member.getPaymentDue());
                    updateMember.setInt(2, memberID);
                    updateMember.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static double getAmount(int overdueDays) {
        double baseAmount = 10;
        if (overdueDays == 1) {
            return baseAmount;
        } else {
            return baseAmount + (baseAmount * 0.1 * (overdueDays - 1));
        }
    }
}