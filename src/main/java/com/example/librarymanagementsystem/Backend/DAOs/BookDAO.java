package com.example.librarymanagementsystem.Backend.DAOs;

import com.example.librarymanagementsystem.Backend.DBConnector;
import com.example.librarymanagementsystem.Backend.Models.Author;
import com.example.librarymanagementsystem.Backend.Models.Book;

import java.sql.*;
import java.util.ArrayList;

public class BookDAO {
    private final DBConnector dbConnector;

    public BookDAO() {
        this.dbConnector = new DBConnector();
    }

    public void addBook(Book book, int authorId) {
        String bookQuery = "INSERT INTO book(Title, Genre, PublicationDate, Availability) VALUES (?, ?, ?, ?)";
        String bookAuthorQuery = "INSERT INTO book_author(BookID, AuthorID) VALUES (?, ?)";

        try (Connection connection = dbConnector.connect();
             PreparedStatement bookStatement = connection.prepareStatement(bookQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement bookAuthorStatement = connection.prepareStatement(bookAuthorQuery)) {

            // Insert book into the book table
            bookStatement.setString(1, book.getTitle());
            bookStatement.setString(2, book.getGenre());
            bookStatement.setDate(3, Date.valueOf(book.getPublicationDate()));
            bookStatement.setBoolean(4, book.getAvailability());
            bookStatement.executeUpdate();

            // Retrieve the generated book ID
            int bookId;
            try (ResultSet bookGeneratedKeys = bookStatement.getGeneratedKeys()) {
                if (bookGeneratedKeys.next()) {
                    bookId = bookGeneratedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve the generated BookID.");
                }
            }

            // Link book and author
            bookAuthorStatement.setInt(1, bookId);
            bookAuthorStatement.setInt(2, authorId);
            bookAuthorStatement.executeUpdate();

            System.out.println("Book added successfully with BookID: " + bookId + " linked to AuthorID: " + authorId);

        } catch (SQLException e) {
            System.err.println("Failed to add book: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ArrayList<Book> getBooksByGenre(String genre) {
        String query = """
                SELECT b.BookID, b.Title, b.Genre, b.PublicationDate, b.Availability, 
                       a.AuthorID, a.FirstName, a.LastName 
                FROM book b 
                JOIN book_author ba ON b.BookID = ba.BookID 
                JOIN author a ON ba.AuthorID = a.AuthorID 
                WHERE b.Genre = ?
                """;
        ArrayList<Book> books = new ArrayList<>();

        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, genre);
            ResultSet rs = statement.executeQuery();
            boolean isExist = false;

            while (rs.next()) {
                isExist = true;

                int bookID = rs.getInt("BookID");
                String bookTitle = rs.getString("Title");
                String pubDate = rs.getString("PublicationDate");
                boolean availability = rs.getBoolean("Availability");

                int authorID = rs.getInt("AuthorID");
                String authorFirstName = rs.getString("FirstName");
                String authorLastName = rs.getString("LastName");
                Author author = new Author(authorID, authorFirstName, authorLastName);

                Book book = new Book(bookID, pubDate, bookTitle, genre, author, availability);
                books.add(book);
            }

            if (!isExist) {
                System.out.println("The book genre: '" + genre + "' is not available in the library.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public void getBookByTitle(String title) {
        String query = "SELECT * FROM book WHERE Title = ?";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            boolean isExist = false;

            while (rs.next()) {
                isExist = true;
                String bookTitle = rs.getString("Title");
                if (bookTitle.equals(title)) {
                    int bookID = rs.getInt("BookID");
                    String genre = rs.getString("Genre");
                    String pubDate = rs.getString("PublicationDate");
                    boolean availability = rs.getBoolean("Availability");

                    System.out.println("Book Details: ID: " + bookID + ", Title: " + bookTitle + ", Genre: " + genre + ", Publication Date: " + pubDate + ", Available: " + availability);
                } else {
                    System.out.println("The book titled: '" + title + "' is not available in the library.");
                }
            }

            if (!isExist) {
                System.out.println("The book genre: '" + title + "' is not available in the library.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getBookByAuthor(Author author) {
        String authorQuery = "SELECT AuthorID FROM author WHERE firstName = ? AND lastName = ?";
        String bookQuery = "SELECT BookID FROM book_author WHERE authorID = ?";
        try (Connection connection = dbConnector.connect();
             PreparedStatement authorStatement = connection.prepareStatement(authorQuery);
             PreparedStatement bookStatement = connection.prepareStatement(bookQuery)) {
            authorStatement.setString(1, author.getFirstName());
            authorStatement.setString(2, author.getLastName());
            ResultSet rs = authorStatement.executeQuery();

            int rsAuthorID = -1;
            if (rs.next()) {
                rsAuthorID = rs.getInt("authorID");
                System.out.println(rsAuthorID);
            }

            bookStatement.setInt(1, rsAuthorID);
            ResultSet rs2 = bookStatement.executeQuery();

            while (rs2.next()) {
                int rs2BookID = rs2.getInt("bookID");
                getBookByID(rs2BookID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getBookByID(int id) {
        String query = "SELECT * FROM book WHERE bookID = ?";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int bookID = rs.getInt("BookID");
                String bookTitle = rs.getString("Title");
                String genre = rs.getString("Genre");
                String pubDate = rs.getString("PublicationDate");
                boolean availability = rs.getBoolean("Availability");

                System.out.println("Book Details: ID: " + bookID + ", Title: " + bookTitle + ", Genre: " + genre + ", Publication Date: " + pubDate + ", Available: " + availability);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> books = new ArrayList<>();
        String query = "SELECT b.BookID, b.Title, b.Genre, b.PublicationDate, b.Availability, a.AuthorID, a.FirstName, a.LastName " +
                "FROM book b " +
                "JOIN book_author ba ON b.BookID = ba.BookID " +
                "JOIN author a ON ba.AuthorID = a.AuthorID";

        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("BookID");
                String title = resultSet.getString("Title");
                String genre = resultSet.getString("Genre");
                String pubDate = resultSet.getString("PublicationDate");
                boolean availability = resultSet.getBoolean("Availability");

                int authorID = resultSet.getInt("AuthorID");
                String authorFirstName = resultSet.getString("FirstName");
                String authorLastName = resultSet.getString("LastName");
                Author author = new Author(authorID, authorFirstName, authorLastName);

                books.add(new Book(id, pubDate, title, genre, author, availability));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public ArrayList<Book> getAvailableBooks() {
        ArrayList<Book> books = new ArrayList<>();
        String query = "SELECT b.BookID, b.Title, b.Genre, b.PublicationDate, b.Availability, a.AuthorID, a.FirstName, a.LastName " +
                "FROM book b " +
                "JOIN book_author ba ON b.BookID = ba.BookID " +
                "JOIN author a ON ba.AuthorID = a.AuthorID " +
                "WHERE b.Availability = true";

        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("BookID");
                String title = resultSet.getString("Title");
                String genre = resultSet.getString("Genre");
                String pubDate = resultSet.getString("PublicationDate");
                boolean availability = resultSet.getBoolean("Availability");

                int authorID = resultSet.getInt("AuthorID");
                String authorFirstName = resultSet.getString("FirstName");
                String authorLastName = resultSet.getString("LastName");
                Author author = new Author(authorID, authorFirstName, authorLastName);

                books.add(new Book(id, pubDate, title, genre, author, availability));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public void deleteBook(int bookId) {
        String deleteBorrowingRecordQuery = "DELETE FROM book_borrowing_record WHERE BookID = ?";
        String deleteAuthorRelationQuery = "DELETE FROM book_author WHERE BookID = ?";
        String deleteBookQuery = "DELETE FROM book WHERE BookID = ?";

        try (Connection connection = dbConnector.connect()) {
            // Delete references in book_borrowing_record table
            try (PreparedStatement deleteBorrowingRecordStmt = connection.prepareStatement(deleteBorrowingRecordQuery)) {
                deleteBorrowingRecordStmt.setInt(1, bookId);
                deleteBorrowingRecordStmt.executeUpdate();
            }

            // Delete references in book_author table
            try (PreparedStatement deleteAuthorRelationStmt = connection.prepareStatement(deleteAuthorRelationQuery)) {
                deleteAuthorRelationStmt.setInt(1, bookId);
                deleteAuthorRelationStmt.executeUpdate();
            }

            // Delete the book from the book table
            try (PreparedStatement deleteBookStmt = connection.prepareStatement(deleteBookQuery)) {
                deleteBookStmt.setInt(1, bookId);
                int rowsAffected = deleteBookStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Book with ID " + bookId + " has been deleted.");
                } else {
                    System.out.println("No book with such ID exists.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
