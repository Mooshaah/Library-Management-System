package com.example.librarymanagementsystem.Backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LibrarianDOA {
    private DBConnector dbConnector;
    private Librarian librarian;

    public LibrarianDOA() {
        dbConnector = new DBConnector();
    }

    public void createLibrarian(Librarian librarian) {
        String query = "INSERT INTO librarian (FirstName,LastName,PhoneNumber,Email) VALUES (?,?,?,?)";
        try(Connection connection = dbConnector.connect();
            PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1,librarian.getFirstName());
            statement.setString(2, librarian.getLastName());
            statement.setInt(3, Integer.parseInt(librarian.getPhoneNumber()));
            statement.setString(4, librarian.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
