package com.example.librarymanagementsystem.Backend;

public class Book {
    int bookId;
    boolean availability;

    String publicationDate;
    String title;
    String genre;

    public Book(int bookId, boolean availability, String publicationDate, String title, String genre) {
        this.bookId = bookId;
        this.availability = availability;
        this.publicationDate = publicationDate;
        this.title = title;
        this.genre = genre;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean getAvailability(boolean availability){
        return availability;
    }
}
