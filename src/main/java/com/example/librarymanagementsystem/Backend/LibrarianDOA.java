package com.example.librarymanagementsystem.Backend;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibrarianDOA {
    private DBConnector dbConnector;
    private Librarian librarian;


    public LibrarianDOA() {
        dbConnector = new DBConnector();
    }

    public void createLibrarian(Librarian librarian) {
        String query = "INSERT INTO librarian (FirstName,LastName,PhoneNumber,Email) VALUES (?,?,?,?)"; // don't forget to add password col after after taking changes in the table
        try(Connection connection = dbConnector.connect();
            PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1,librarian.getFirstName());
            statement.setString(2, librarian.getLastName());
            statement.setInt(3, Integer.parseInt(librarian.getPhoneNumber()));
            statement.setString(4, librarian.getEmail());
            statement.setString(5, librarian.getPhoneNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateLibrarian(Librarian librarian, int librarianID){
        String query = "UPDATE librarian SET FirstName = ?, LastName = ?, PhoneNumber = ?, Email = ?, Password=? WHERE LibrarianID = ?";
        try(Connection connection = dbConnector.connect();
        PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, librarian.getFirstName());
            statement.setString(2, librarian.getLastName());
            statement.setString(3, librarian.getPhoneNumber());
            statement.setString(4, librarian.getEmail());
            statement.setString(5, librarian.getPassword());
            statement.setInt(6, librarianID);
            statement.executeUpdate();
            System.out.println("Librarian after update: ");
            getLibrarianByID(librarianID);

        }catch(SQLException e ){
            e.printStackTrace();
        }
    }

    public void getLibrarianByID(int id){
        String query = "SELECT * FROM librarian WHERE LibrarianID= ?";
        try(Connection connection = dbConnector.connect();
        PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1,id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                int librarianID = rs.getInt("LibrarianID");
                String librarianFName = rs.getString("FirstName");
                String librarianLName = rs.getString("LastName");
                String librarianPhoneNumber = rs.getString("PhoneNumber");
                String  librarianEmail= rs.getString("Email");
                System.out.println("Librarian ID "+ librarianID+ " Frist Name: "+ librarianFName+" Last Name: "+librarianLName+" phone Number: "+ librarianPhoneNumber+" Email: "+librarianEmail);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void addBook(Book book){

        String query = "INSERET INTO book(BookID,Title, Genre, PublicationDate, Availability) VALUES (?, ?, ?, ?, ?)";
        try(Connection connection = dbConnector.connect();
            PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1,book.getBookId());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getPublicationDate());
            statement.setString(4, book.getTitle());
            statement.setBoolean(5, book.getAvailability(book.availability));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void DeleteBook(int bookId) {
        String query = "DELETE FROM book WHERE BookID = ?";
        try(Connection connection = dbConnector.connect();
            PreparedStatement statement= connection.prepareStatement(query)){
            statement.setInt(1, bookId);
            int ra = statement.executeUpdate();
            if(ra > 0){
                System.out.println("Member with ID "+bookId+" has been deleted");
            } else {
                System.out.println("No member with such ID !");
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void getBookByGenre(String genre){
        String query = "SELECT * FROM book WHERE Genre= ?";
        try(Connection connection = dbConnector.connect();
        PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, genre);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                int bookID = rs.getInt("BookID");
                String bookTitle = rs.getString("Title");
                String Genre = rs.getString("Genre");
                String pubDate = rs.getString("PublicationDate");
                Boolean availability = rs.getBoolean("availability");
                System.out.println("Book ID: "+ bookID+" Genre: "+Genre+" Publication Date: "+pubDate+" Availability "+ availability);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void getBookByTitle(String title) {
        String query = "SELECT * FROM book WHERE Title = ?";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int bookID = rs.getInt("BookID");
                String bookTitle = rs.getString("Title");
                String genre = rs.getString("Genre");
                String pubDate = rs.getString("PublicationDate");
                boolean availability = rs.getBoolean("Availability");

                System.out.println("Book Details:");
                System.out.println("ID: " + bookID);
                System.out.println("Title: " + bookTitle);
                System.out.println("Genre: " + genre);
                System.out.println("Publication Date: " + pubDate);
                System.out.println("Available: " + availability);
            } else {
                System.out.println("The book titled: '" + title + "' is not available in the library.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
