package com.example.librarymanagementsystem.Backend;

import com.example.librarymanagementsystem.Backend.DAOs.BorrowRecordDAO;
import com.example.librarymanagementsystem.Backend.DAOs.LibrarianDAO;
import com.example.librarymanagementsystem.Backend.Models.Author;
import com.example.librarymanagementsystem.Backend.Models.Book;
import com.example.librarymanagementsystem.Backend.Models.BorrowRecord;
import com.example.librarymanagementsystem.Backend.Models.Member;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
//import java.sql.

public class DBConnector {
    public Connection connect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/libraryManagementSystem";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }


    public static void main(String[] args) throws SQLException {
        LibrarianDAO librarian = new LibrarianDAO();
        BorrowRecordDAO borrowRecord = new BorrowRecordDAO();
        Author author = new Author(1, "Harper", "Lee");
        Book book1 = new Book(22, "2024-12-05", "To Kill a Mockingbird", "Fiction", author, true);
        Book book2 = new Book(23, "2024-12-24", "Pride and Prejudice", "Romance", author, true);
        Member member = new Member(3, "philo", "zaki", "01234567890", "philo", "123", "Student", "Computer Science", 0);
        ArrayList<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);


        BorrowRecordDAO recordDAO = new BorrowRecordDAO();
//        recordDAO.borrowBook(member.getId(), books);
//        recordDAO.returnBook(member, books);
//        recordDAO.calculateFine(member.getId());
    }
}
