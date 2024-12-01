package com.example.librarymanagementsystem.Backend.Models;

import java.util.Date;

public class BorrowRecord {
    private int id;
    private Date orrowDate;
    private Date dueDate;
    private Date returnDate;

    public BorrowRecord(int id, Date orrowDate, Date dueDate, Date returnDate) {
        this.id = id;
        this.orrowDate = orrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public Date getOrrowDate() {
        return orrowDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public int getId() {
        return id;
    }
}
