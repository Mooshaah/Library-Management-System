package com.example.librarymanagementsystem.Backend;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//import java.sql.

public class DBConnector {
    public Connection connect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/libraryManagementSystem";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url,user,password);
    }


    public static void main(String[] args) throws SQLException {



    // second statement to be executed
        MemberDOA member = new MemberDOA();
        Member memberDummy = new Member("Mohamed","Elshaarawy","01265775635","anon@philo.com","Student","CS");
        Member memberDummy2 = new Member("Mohamed","edited","01265775635","mohamed@hotmail.com","Student","CS");
//        member.CreateMember(memberDummy,1);

    // third statement to exec
        member.getMemberById(1);
        member.updateMember(memberDummy2,1);

    }
}
