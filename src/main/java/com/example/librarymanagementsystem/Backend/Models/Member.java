package com.example.librarymanagementsystem.Backend.Models;

public class Member implements User {
    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    private String type;
    private String department;

    public Member(int id, String firstName, String lastName, String phoneNumber, String email, String password, String type, String department) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.type = type;
        this.department = department;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDepartment() {
        return department;
    }
}
