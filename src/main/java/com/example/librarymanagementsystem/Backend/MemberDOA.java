package com.example.librarymanagementsystem.Backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDOA {
    private DBConnector dbConnector;
    private Member member;
    private int librarianId;

    public MemberDOA() {
        dbConnector = new DBConnector();
    }

    public void CreateMember(Member member, int librarianId) {
        String query = "INSERT INTO member (FirstName, LastName, PhoneNumber, Email, Type, Department, librarianID) VALUES (?,?,?,?,?,?,?)";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, member.getFname());
            statement.setString(2, member.getLname());
            statement.setString(3, member.getPhoneNumber());
            statement.setString(4, member.getEmail());
            statement.setString(5, member.getType());
            statement.setString(6, member.getDepartment());
            statement.setInt(7, librarianId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMember(Member member, int memberId) {
        String query = "UPDATE member SET FirstName = ?, LastName = ?, PhoneNumber = ?, Email = ?, Type = ?, Department = ? WHERE MemberID = ?";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, member.getFname());
            statement.setString(2, member.getLname());
            statement.setString(3, member.getPhoneNumber());
            statement.setString(4, member.getEmail());
            statement.setString(5, member.getType());
            statement.setString(6, member.getDepartment());
            statement.setInt(7, memberId);
            statement.executeUpdate();
             System.out.println("Member after update: ");
             getMemberById(memberId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getMemberById(int id) {

        String query = "SELECT * FROM member WHERE MemberID= ?";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int memberId = result.getInt("MemberID");
                String memberFname = result.getString("FirstName");
                String memberLname = result.getString("LastName");
                String memberPhoneNumber = result.getString("PhoneNumber");
                String memberEmail = result.getString("Email");
                String memberType = result.getString("Type");
                String memberDepartment = result.getString("Department");
                System.out.println("MemberID: " + memberId + " First Name: " + memberFname + " Last Name: " + memberLname + " PhoneNumber: " + memberPhoneNumber + " Email: " + memberEmail + " Type: " + memberType + " Department: " + memberDepartment);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
