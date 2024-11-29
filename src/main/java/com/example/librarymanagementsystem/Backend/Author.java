package com.example.librarymanagementsystem.Backend;

public class Author {
    private String authorName;
    private String lastName;

    public Author(String authorName, String lastName) {
        this.authorName = authorName;
        this.lastName = lastName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
