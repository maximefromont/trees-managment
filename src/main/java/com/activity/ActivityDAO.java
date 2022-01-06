package com.activity;

import java.sql.*;
import java.util.ArrayList;

import com.config.*;

public class ActivityDAO {

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

    public static ArrayList<Activity> getAllActivities() {

        ArrayList<Activity> activities = new ArrayList<Activity>();

        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.activity");
            while (resultSet.next()) {
                activities.add(new Activity(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return activities;
    }
}
