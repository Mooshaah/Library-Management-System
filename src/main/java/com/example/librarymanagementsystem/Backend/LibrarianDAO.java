package com.example.librarymanagementsystem.Backend;

import java.sql.*;

public class LibrarianDAO {
    private final DBConnector dbConnector;

    public LibrarianDAO() {
        dbConnector = new DBConnector();
    }

    public void createLibrarian(Librarian librarian) {
        String query = "INSERT INTO librarian (FirstName,LastName,PhoneNumber,Email,Password) VALUES (?,?,?,?,?)";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, librarian.getFirstName());
            statement.setString(2, librarian.getLastName());
            statement.setString(3, librarian.getPhoneNumber());
            statement.setString(4, librarian.getEmail());
            statement.setString(5, librarian.getPhoneNumber());
            statement.setString(6, librarian.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateLibrarian(Librarian librarian, int BookID) {
        String query = "UPDATE librarian SET FirstName = ?, LastName = ?, PhoneNumber = ?, Email = ?, Password=? WHERE LibrarianID = ?";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, librarian.getFirstName());
            statement.setString(2, librarian.getLastName());
            statement.setString(3, librarian.getPhoneNumber());
            statement.setString(4, librarian.getEmail());
            statement.setString(5, librarian.getPassword());
            statement.setInt(6, BookID);
            statement.executeUpdate();
            System.out.println("Librarian after update: ");
            getLibrarianByID(BookID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Boolean checkLibrarianEmailAndPassword(String email, String password) {
        String emailQuery = "SELECT Count(*) AS Ecount FROM librarian WHERE BINARY Email = ?";
        String passwordQuery = "SELECT Count(*) AS Pcount FROM librarian WHERE BINARY Password = ?";

        try (Connection connection = dbConnector.connect();
             PreparedStatement emailStatement = connection.prepareStatement(emailQuery);
             PreparedStatement passStatement = connection.prepareStatement(passwordQuery)) {
            emailStatement.setString(1, email);
            passStatement.setString(1, password);
            ResultSet emailResult = emailStatement.executeQuery();
            ResultSet passResult = passStatement.executeQuery();
            while (emailResult.next() && passResult.next()) {
                int eCount = emailResult.getInt("Ecount");
                int pCount = passResult.getInt("Pcount");
                if (eCount > 0 && pCount > 0) return true;
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void getLibrarianByID(int id) {
        String query = "SELECT * FROM librarian WHERE LibrarianID= ?";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int BookID = rs.getInt("LibrarianID");
                String librarianFName = rs.getString("FirstName");
                String librarianLName = rs.getString("LastName");
                String librarianPhoneNumber = rs.getString("PhoneNumber");
                String librarianEmail = rs.getString("Email");
                System.out.println("Librarian ID: " + BookID + ", First Name: " + librarianFName + ", Last Name: " + librarianLName + ", Phone Number: " + librarianPhoneNumber + ", Email: " + librarianEmail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBook(Book book, Author author) {
        String bookQuery = "INSERT INTO book(Title, Genre, PublicationDate, Availability) VALUES (?, ?, ?, ?)";
        String findAuthorQuery = "SELECT AuthorID FROM author WHERE FirstName = ? AND LastName = ?";
        String authorQuery = "INSERT INTO author(FirstName, LastName) VALUES (?, ?)";
        String bookAuthorQuery = "INSERT INTO book_author(BookID, AuthorID) VALUES (?, ?)";

        try (Connection connection = dbConnector.connect();
             PreparedStatement bookStatement = connection.prepareStatement(bookQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement findAuthorStatement = connection.prepareStatement(findAuthorQuery);
             PreparedStatement authorStatement = connection.prepareStatement(authorQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement bookAuthorStatement = connection.prepareStatement(bookAuthorQuery)) {

            // Insert book into the book table
            int bookId = insertBook(bookStatement, book);

            // Check if the author already exists or insert a new one
            int authorId = findOrInsertAuthor(findAuthorStatement, authorStatement, author);

            // Link book and author
            bookAuthorStatement.setInt(1, bookId);
            bookAuthorStatement.setInt(2, authorId);
            bookAuthorStatement.executeUpdate();

            System.out.println("Book added successfully with BookID: " + bookId + " and linked to AuthorID: " + authorId);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(int bookId) {
        String query = "DELETE FROM book WHERE BookID = ?";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            int ra = statement.executeUpdate();
            if (ra > 0) {
                System.out.println("Book with ID " + bookId + " has been deleted");
            } else {
                System.out.println("No book with such ID!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getBookByGenre(String genre) {
        String query = "SELECT * FROM book WHERE Genre= ?";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, genre);
            ResultSet rs = statement.executeQuery();
            boolean isExist = false;

            while (rs.next()) {
                isExist = true;
                String genreName = rs.getString("Genre");
                if (genreName.equals(genre)) {
                    int bookID = rs.getInt("BookID");
                    String bookTitle = rs.getString("Title");
                    String Genre = rs.getString("Genre");
                    String pubDate = rs.getString("PublicationDate");
                    int availability = rs.getInt("availability");
                    System.out.println("Book Details: ID: " + bookID + ", Title: " + bookTitle + ", Genre: " + Genre + ", Publication Date: " + pubDate + ", Available copies: " + availability);
                }
            }

            if (!isExist) {
                System.out.println("The book genre: '" + genre + "' is not available in the library.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

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


    private int findOrInsertAuthor(PreparedStatement findAuthorStatement, PreparedStatement authorStatement, Author author) throws SQLException {
        // Check if the author already exists
        findAuthorStatement.setString(1, author.getFirstName());
        findAuthorStatement.setString(2, author.getLastName());
        try (ResultSet resultSet = findAuthorStatement.executeQuery()) {
            if (resultSet.next()) {
                // Author exists, return AuthorID
                return resultSet.getInt("AuthorID");
            }
        }

        // Author does not exist, insert and retrieve generated AuthorID
        authorStatement.setString(1, author.getFirstName());
        authorStatement.setString(2, author.getLastName());
        authorStatement.executeUpdate();

        try (ResultSet authorGeneratedKeys = authorStatement.getGeneratedKeys()) {
            if (authorGeneratedKeys.next()) {
                return authorGeneratedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve the generated AuthorID.");
            }
        }
    }

    private int insertBook(PreparedStatement bookStatement, Book book) throws SQLException {
        bookStatement.setString(1, book.getTitle());
        bookStatement.setString(2, book.getGenre());
        bookStatement.setDate(3, Date.valueOf(book.getPublicationDate()));
        bookStatement.setBoolean(4, book.getAvailability());
        bookStatement.executeUpdate();

        try (ResultSet bookGeneratedKeys = bookStatement.getGeneratedKeys()) {
            if (bookGeneratedKeys.next()) {
                return bookGeneratedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve the generated BookID.");
            }
        }
    }

    public int getLibrarianIDByName(String FirstName, String LastName) {
        String query = "SELECT LibrarianID FROM librarian WHERE FirstName = ? AND LastName = ?";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, FirstName);
            statement.setString(2, LastName);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int BookID = rs.getInt("LibrarianID");
                System.out.println("Librarian ID: " + BookID);
                return BookID;
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return -1; // a return that means no librarian found

    }

    public int getBookIDByTitle(String bookTitle, String authorFirstName, String authorLastName) {
        String query = "SELECT b.BookID FROM book b JOIN book_author ba ON b.BookID = ba.BookID " +
                "JOIN author a ON ba.AuthorID = a.AuthorID WHERE b.Title = ? AND a.FirstName = ? AND a.LastName = ?";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, bookTitle);
            statement.setString(2, authorFirstName);
            statement.setString(3, authorLastName);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int BookID = rs.getInt("BookID");
                System.out.println("Book ID: " + BookID);
                return BookID;
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return -1; // a return that means no librarian found

    }


    public String[] getAuthorById(int id) {
        String query = "SELECT AuthorID, FirstName, LastName FROM author WHERE AuthorID= ?";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int AuthorID = result.getInt("AuthorID");
                String authorFname = result.getString("FirstName");
                String authorLname = result.getString("LastName");
                return new String[] {authorFname, authorLname};
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}