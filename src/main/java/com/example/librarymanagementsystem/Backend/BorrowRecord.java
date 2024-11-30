package com.example.librarymanagementsystem.Backend;

import java.util.Date;

public class BorrowRecord {
    private Date orrowDate;
    private Date dueDate;
    private Date returnDate;

    public BorrowRecord(Date orrowDate, Date dueDate, Date returnDate) {
        this.orrowDate = orrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public Date getOrrowDate() {
        return orrowDate;
    }

    public void setOrrowDate(Date orrowDate) {
        this.orrowDate = orrowDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}
