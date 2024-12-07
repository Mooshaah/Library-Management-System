package com.example.librarymanagementsystem.Backend;

import com.example.librarymanagementsystem.Backend.DAOs.BookDAO;
import com.example.librarymanagementsystem.Backend.DAOs.BorrowRecordDAO;
import com.example.librarymanagementsystem.Backend.DAOs.LibrarianDAO;
import com.example.librarymanagementsystem.Backend.Models.Author;
import com.example.librarymanagementsystem.Backend.Models.Book;
import com.example.librarymanagementsystem.Backend.Models.BorrowRecord;
import com.example.librarymanagementsystem.Backend.Models.Member;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
        BookDAO bookDAO = new BookDAO();
        Author author = new Author(1, "Harper", "Lee");
        Author author1 = new Author(1, "Harper", "Lee");
        Author author2 = new Author(2, "George", "Orwell");
        Author author3 = new Author(3, "J.K.", "Rowling");
        Author author4 = new Author(4, "J.R.R.", "Tolkien");
        Author author7 = new Author(7, "F. Scott", "Fitzgerald");

        Book book1 = new Book(1, "2024-12-05", "To Kill a Mockingbird", "Fiction", author1, false);
        Book book2 = new Book(2, "2023-11-15", "1984", "Dystopian", author2, true);
        Book book3 = new Book(3, "2022-07-20", "Animal Farm", "Political Satire", author2, true);
        Book book4 = new Book(4, "2021-03-10", "Harry Potter and the Sorcerer's Stone", "Fantasy", author3, false);
        Book book5 = new Book(5, "2021-05-12", "Harry Potter and the Chamber of Secrets", "Fantasy", author3, true);
        Book book6 = new Book(6, "2020-01-15", "The Hobbit", "Fantasy", author4, false);
        Book book7 = new Book(7, "2023-09-01", "The Lord of the Rings: The Fellowship of the Ring", "Fantasy", author4, true);
        Book book8 = new Book(8, "2023-06-21", "The Great Gatsby", "Tragedy", author7, false);

//        Book book1 = new Book(0, "2024-12-05", "To Kill a Mockingbird", "Fiction", author, false);
//        Book book2 = new Book(23, "2024-12-24", "Pride and Prejudice", "Romance", author, true);
        Member member = new Member(4, "philo", "zaki", "01234567890", "philo", "123", "Student", "Computer Science", 0);
//        ArrayList<Book> books = new ArrayList<>();
//        books.add(book1);
//        books.add(book2);
//        LocalDate testReturnDate = LocalDate.of(2024, 12, 15);

//        bookDAO.addBook(book1, author1.getId());
//        bookDAO.addBook(book2, author2.getId());
//        bookDAO.addBook(book3, author2.getId());
//        bookDAO.addBook(book4, author3.getId());
//        bookDAO.addBook(book5, author3.getId());
//        bookDAO.addBook(book6, author4.getId());
//        bookDAO.addBook(book7, author4.getId());
//        bookDAO.addBook(book8, author7.getId());
    }
}
