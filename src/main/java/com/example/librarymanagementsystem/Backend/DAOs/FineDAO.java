package com.example.librarymanagementsystem.Backend.DAOs;

import com.example.librarymanagementsystem.Backend.DBConnector;
import com.example.librarymanagementsystem.Backend.Models.BorrowRecord;
import com.example.librarymanagementsystem.Backend.Models.Fine;
import com.example.librarymanagementsystem.Backend.Models.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FineDAO {
    private final DBConnector dbConnector;
    private final MemberDAO memberDAO;

    public FineDAO() {
        this.dbConnector = new DBConnector();
        this.memberDAO = new MemberDAO();
    }

    public void calculateFine(int memberID) {
        Member member = memberDAO.getMemberById(memberID);
        String selectRecordQuery = "SELECT RecordID,BorrowDate,ReturnDate,DueDate FROM borrowing_record WHERE MemberID =?";
        String selectFineQuery = "SELECT * FROM fine WHERE MemberID = ?";
        String updateFineQuery = "UPDATE fine SET amount = ? WHERE MemberID = ?";
        String insertFineQuery = "INSERT INTO fine (Amount, isPaid, MemberID) VALUES (?,?,?)";
        String updateMemberQuery = "UPDATE member SET PaymentDue = ? WHERE MemberID = ?";

        try (Connection connection = dbConnector.connect();
             PreparedStatement selectRecord = connection.prepareStatement(selectRecordQuery);
             PreparedStatement selectFine = connection.prepareStatement(selectFineQuery);
             PreparedStatement insertFine = connection.prepareStatement(insertFineQuery);
             PreparedStatement updateMember = connection.prepareStatement(updateMemberQuery);
             PreparedStatement updateFine = connection.prepareStatement(updateFineQuery)) {

            selectRecord.setInt(1, memberID);
            ResultSet resultSet = selectRecord.executeQuery();

            while (resultSet.next()) {
                int recordID = resultSet.getInt("RecordID");
                Date borrowDate = resultSet.getDate("BorrowDate");
                Date returnDate = resultSet.getDate("ReturnDate");
                Date dueDate = resultSet.getDate("DueDate");
                BorrowRecord borrowRecord = new BorrowRecord(recordID, borrowDate, dueDate, null);
                int overdueDays = borrowRecord.calculateOverdueDays(returnDate);

                if (overdueDays != 0) {
                    double amount = getAmount(overdueDays);

                    selectFine.setInt(1, memberID);
                    ResultSet fineResultSet = selectFine.executeQuery();

                    if (fineResultSet.next()) {
                        double currentAmount = fineResultSet.getDouble("Amount");
                        double updatedAmount = currentAmount + amount;
                        updateFine.setDouble(1, updatedAmount);
                        updateFine.setInt(2, memberID);
                        updateFine.executeUpdate();
                        System.out.println("Updated fine for MemberID: " + memberID + " to $" + updatedAmount);
                    } else {
                        insertFine.setDouble(1, amount);
                        insertFine.setBoolean(2, false);
                        insertFine.setInt(3, memberID);
                        insertFine.executeUpdate();
                        System.out.println("Inserted fine for MemberID: " + memberID + " with amount $" + amount);
                    }

                    member.addFine(amount);
                    updateMember.setDouble(1, member.getPaymentDue());
                    updateMember.setInt(2, memberID);
                    updateMember.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public List<Fine> getFinesByMemberId(int memberId) {
        List<Fine> fines = new ArrayList<>();
        String query = "SELECT FineID, Amount, IsPaid FROM fine WHERE MemberID = ?";

        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, memberId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int fineId = resultSet.getInt("FineID");
                    double amount = resultSet.getDouble("Amount");
                    boolean isPaid = resultSet.getBoolean("IsPaid");
                    fines.add(new Fine(fineId, amount, isPaid));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching fines for member ID: " + memberId, e);
        }

        return fines;
    }

    public void payFine(int fineId) {
        String query = "UPDATE fine SET IsPaid = true WHERE FineID = ?";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, fineId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error paying fine ID: " + fineId, e);
        }
    }

    private static double getAmount(int overdueDays) {
        double baseAmount = 10;
        if (overdueDays == 1) {
            return baseAmount;
        } else {
            return baseAmount + (baseAmount * 0.1 * (overdueDays - 1));
        }
    }

}
