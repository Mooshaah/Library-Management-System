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
        return DriverManager.getConnection(url, user, password);
    }


    public static void main(String[] args) throws SQLException {

        // second statement to be executed
//        MemberDOA member = new MemberDOA();
//        Member memberDummy = new Member("Mohamed", "Elshaarawy", "01265775635", "anon@philo.com", "asd123", "Student", "CS");
//        Member memberDummy2 = new Member("Mohamed", "edited", "01265775635", "mohamed@hotmail.com", "asd123", "Student", "CS");


        // ------------- The commented out code is for testing purposes --------------- //
//        member.CreateMember(memberDummy);
//        member.CreateMember(memberDummy2);
        // third statement to exec
//        member.getMemberById(1);
//        member.updateMember(memberDummy2,1);
        //fourth statement to exec
//        member.checkMemberEmail("mohamed@hotmail.com");
        //Fifth statement to exec
//        member.checkMemberEmailAndPassword("mohamed@hotmail.com", "asd123");

//        member.DelteMember(2);

        LibrarianDAO librarian = new LibrarianDAO();
        Book book1 = new Book(true, "1970-11-19", "Ex", "Love");
        Book book2 = new Book(true, "1970-11-19", "Ex-part2", "Love");
        Book book3 = new Book(true, "1970-11-19", "Ex-part3", "Test");
        Book book4 = new Book(true, "1970-11-19", "test1", "Love");
        Book book5 = new Book(true, "1970-11-19", "test5", "Love");
        Author author = new Author("William", "Shakespeare");

//        librarian.deleteBook(8);

//        librarian.addBook(book1, author);
        librarian.getBookByAuthor(author);
//         librarian.getBookByGenre("asd");
//         librarian.getLibrarianByID(1);
//        librarian.getBookByTitle("Ex-part2");
    }
}
