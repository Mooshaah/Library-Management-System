package com.example.librarymanagementsystem.Backend.Models;

public class Member {
    private int id;
    private String firstName, lastName;
    private String phoneNumber;
    private String email;
    private String type;
    private String department;
    private String password;

    public Member(int id, String Fname, String lastName, String phoneNumber, String email, String password, String type, String department) {
        this.id = id;
        this.firstName = Fname;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.type = type;
        this.department = department;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public String getDepartment() {
        return department;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

}
