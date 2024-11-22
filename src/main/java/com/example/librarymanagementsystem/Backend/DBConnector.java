package com.example.librarymanagementsystem.Backend;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    public void connect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/assignment";
        String user = "root";
        String password = " ";
        DriverManager.getConnection(url,user,password);
    }
}
