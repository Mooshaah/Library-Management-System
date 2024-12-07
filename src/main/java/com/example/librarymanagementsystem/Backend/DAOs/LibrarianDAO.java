package com.example.librarymanagementsystem.Backend.DAOs;

import com.example.librarymanagementsystem.Backend.DBConnector;
import com.example.librarymanagementsystem.Backend.Models.Author;
import com.example.librarymanagementsystem.Backend.Models.Librarian;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public String getLibrarianFirstNameByEmail(String email) {
        String query = "SELECT FirstName FROM librarian WHERE Email = ?";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("FirstName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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


    public void addAuthor(Author author) {
        String query = "INSERT INTO author (FirstName, LastName) VALUES (?, ?)";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set parameters for the query
            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getLastName());

            // Execute the insert query
            statement.executeUpdate();
            System.out.println("Author added successfully: " + author.getFirstName() + " " + author.getLastName());

        } catch (SQLException e) {
            System.err.println("Failed to add author: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public ArrayList<Author> getAllAuthors() {
        ArrayList<Author> authors = new ArrayList<>();
        String query = "SELECT AuthorID, FirstName, LastName FROM author";

        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int authorID = resultSet.getInt("AuthorID");
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                authors.add(new Author(authorID, firstName, lastName));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authors;
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
        return -1;

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
        return -1;

    }

    public Author getAuthorById(int id) {
        String query = "SELECT AuthorID, FirstName, LastName FROM author WHERE AuthorID= ?";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int authorID = result.getInt("AuthorID");
                String authorFname = result.getString("FirstName");
                String authorLname = result.getString("LastName");

                return new Author(authorID, authorFname, authorLname);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Librarian getLibrarianByEmail(String email) {
        String query = "SELECT * FROM librarian WHERE email = ?";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Librarian(
                        resultSet.getInt("LibrarianID"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}