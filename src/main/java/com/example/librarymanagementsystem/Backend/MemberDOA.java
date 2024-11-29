package com.example.librarymanagementsystem.Backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MemberDOA

{
    private DBConnector dbConnector;
    private Member member;
    public MemberDOA(){
        dbConnector = new DBConnector();
    }
        public void CreateMember(Member member) {
        String query = "INSERT INTO member (FirstName, LastName, PhoneNumber, Email, Type, Department) VALUES (?,?,?,?,?,?)";
        try(Connection connection = dbConnector.connect();
            PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1,member.getFname());
            statement.setString(2,member.getLname());
            statement.setString(3,member.getPhoneNumber());
            statement.setString(4,member.getEmail());
            statement.setString(5, member.getType());
            statement.setString(6,member.getDepartment());

            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
