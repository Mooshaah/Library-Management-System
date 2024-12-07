package com.example.librarymanagementsystem.Backend.Models;

import java.time.Instant;
import java.util.Date;

public class Fine {
    private int id;
    private double amount;
    private boolean isPaid;
    private Date issueDate;

    public Fine(int id, double amount, boolean isPaid) {
        this.id = id;
        this.amount = amount;
        this.isPaid = isPaid;
        this.issueDate = Date.from(Instant.now());
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public Date getIssueDate() {
        return this.issueDate;
    }
}
