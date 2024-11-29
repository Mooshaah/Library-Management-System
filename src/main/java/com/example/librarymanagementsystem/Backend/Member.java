package com.example.librarymanagementsystem.Backend;

public class Member {
    private String Fname, Lname;
    private String phoneNumber;
    private String email;
    private String type;
    private String department;


    public Member(String Fname, String Lname, String phoneNumber, String email, String type, String department) {
        this.Fname = Fname;
        this.Lname = Lname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.type = type;
        this.department = department;
    }

    public void setFname(String fname) {
        Fname = fname;
    }
    public String getFname() {
        return Fname;
    }
    public void setLname(String lname) {
        Lname = lname;
    }
    public String getLname() {
        return Lname;
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

    
}
