package com.example.librarymanagementsystem.Backend;

public class Book {
    boolean availability;
    String publicationDate;
    String title;
    String genre;

    public Book(boolean availability, String publicationDate, String title, String genre) {
        this.availability = availability;
        this.publicationDate = publicationDate;
        this.title = title;
        this.genre = genre;
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

    public boolean getAvailability() {
        return availability;
    }
}
