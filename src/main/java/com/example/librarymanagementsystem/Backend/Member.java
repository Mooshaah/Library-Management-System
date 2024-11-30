package com.example.librarymanagementsystem.Backend;

public class Member {
    private String firstName, lastName;
    private String phoneNumber;
    private String email;
    private String type;
    private String department;
    private String password;

    public Member(String Fname, String lastName, String phoneNumber, String email, String password, String type, String department) {
        this.firstName = Fname;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.type = type;
        this.department = department;
    }

    public void setFirstName(String fname) {
        firstName = fname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPassword() {
        return password;
    }

}
