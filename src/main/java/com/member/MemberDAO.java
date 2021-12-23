package com.member;

import java.sql.*;
import java.util.ArrayList;

import com.config.*;

public class MemberDAO {

    private static Connection getConnection() {

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(ReadProperties.getConfig("linkBDD"), ReadProperties.getConfig("loginBDD"), ReadProperties.getConfig("mdpBDD"));
        } catch (Exception e) {
            e.printStackTrace();
            if(e instanceof ClassNotFoundException)
                System.out.println("PostgreSQL JDBC driver not found.");
            if(e instanceof SQLException)
                System.out.println("Connection failure.");
        }

        return connection;
    }

    public static ArrayList<Member> getAllMember() {

        ArrayList<Member> members = new ArrayList<Member>();

        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.member");
            while (resultSet.next()) {
                members.add(new Member(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }
}
