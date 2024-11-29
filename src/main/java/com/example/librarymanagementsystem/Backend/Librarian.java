package com.example.librarymanagementsystem.Backend;

public class Librarian {

    private String FirstName;
    private String LastName;
    private String phoneNumber;
    private String email;

    public Librarian(String FirstName, String LastName, String phoneNumber, String email) {
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
}
