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
        String query = "INSERT INTO member (FirstName, LastName, PhoneNumber, Email, Password, Type, Department, librarianID) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, member.getFname());
            statement.setString(2, member.getLname());
            statement.setString(3, member.getPhoneNumber());
            statement.setString(4, member.getEmail());
            statement.setString(5, member.getPassword());
            statement.setString(6, member.getType());
            statement.setString(7, member.getDepartment());
            statement.setInt(8, librarianId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMember(Member member, int memberId) {
        String query = "UPDATE member SET FirstName = ?, LastName = ?, PhoneNumber = ?, Email = ?, Password=?, Type = ?, Department = ? WHERE MemberID = ?";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, member.getFname());
            statement.setString(2, member.getLname());
            statement.setString(3, member.getPhoneNumber());
            statement.setString(4, member.getEmail());
            statement.setString(5, member.getPassword());
            statement.setString(6, member.getType());
            statement.setString(7, member.getDepartment());
            statement.setInt(8, memberId);
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
                String memberPassword = result.getString("Password");
                String memberType = result.getString("Type");
                String memberDepartment = result.getString("Department");
                System.out.println("MemberID: " + memberId + " First Name: " + memberFname + " Last Name: " + memberLname + " PhoneNumber: " + memberPhoneNumber + " Email: " + memberEmail + " Password: " + memberPassword + " Type: " + memberType + " Department: " + memberDepartment);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean checkMemberEmail(String email) {
        String query = "SELECT Count(*) AS count FROM member WHERE Email = ?";
        try (Connection connection = dbConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");

                if (count > 0) {
                    System.out.println("Email already exists !");
                    return true;

                } else {
                    System.out.println("Email does not exist !");
                    return false;
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean checkMemberEmailAndPassword(String email, String password) {
        String emailQuery = "SELECT Count(*) AS Ecount FROM member WHERE Email = ?";
        String passwordQuery = "SELECT Count(*) AS Pcount FROM member WHERE Password = ?";

        try (Connection connection = dbConnector.connect();
             PreparedStatement emailStatement = connection.prepareStatement(emailQuery);
             PreparedStatement passStatement = connection.prepareStatement(passwordQuery)) {
            emailStatement.setString(1, email);
            passStatement.setString(1, password);
            ResultSet emailResult = emailStatement.executeQuery();
            ResultSet passResult = passStatement.executeQuery();
            while(emailResult.next() && passResult.next()){
                int eCount = emailResult.getInt("Ecount");
                int pCount = passResult.getInt("Pcount");
                if (eCount > 0 && pCount > 0){
                    System.out.println("Email and password matches !");
                    return true;
                }else{
                    System.out.println("Email or password does not match !");
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

