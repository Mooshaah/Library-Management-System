package com.example.librarymanagementsystem.Backend;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//import java.sql.

public class DBConnector {
    public Connection connect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/libraryManagementSystem";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url,user,password);
    }

//    public Connection closeConnection() throws SQLException {
//        return
//    }
    public static void main(String[] args) throws SQLException {

        // first statement to be executed on the server
//        Librarian librarian1 = new Librarian("Philo","falafel","02215334","philo@uofcanada.edu.eg");
//        LibrarianDOA librarian = new LibrarianDOA();
//        librarian.createLibrarian(librarian1);

    // second statement to be executed
        MemberDOA member = new MemberDOA();
        Member memberDummy = new Member("Mohamed","Elshaarawy","01265775635","anon@philo.com","Student","CS");
        member.CreateMember(memberDummy);

    }
}
