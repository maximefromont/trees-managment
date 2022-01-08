package com.association;

import com.activity.Activity;
import com.config.ReadProperties;

import java.sql.*;
import java.util.ArrayList;

public class FinanceDAO {

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

    public static ArrayList<Finance> getFinanceForYear(Association association, String year) {

        ArrayList<Finance> finances = new ArrayList<Finance>();

        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.finance WHERE id_association="+association.getId()+" AND date="+year);
            while (resultSet.next()) {
                finances.add(new Finance(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getString(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return finances;
    }

}
