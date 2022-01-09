package com.activity;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.association.Association;
import com.config.*;
import com.member.Member;

public class ActivityDAO {

    /**
     * @auth Maxime
     * @return Connection à la base de donnée
     */
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

    /**
     * @auth Maxime
     * @return outes les activitées de l'année de l'association
     */
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

    /**
     * Renvoie la liste des activités pour une année
     * @auth Bastien
     * @param year, l'année en question
     * @return ArrayList<Activity> activities, une liste d'Activity
     */
    public static ArrayList<Activity> getAllActivitiesByYear(String year) {

        ArrayList<Activity> activities = new ArrayList<Activity>();

        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.activity where date LIKE '%" + year + "%'");
            while (resultSet.next()) {
                activities.add(new Activity(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3), resultSet.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return activities;
    }

    /**
     * @auth Maxime
     * @param association
     * @param name nom de l'activité
     * Insère dans une base de donnée une activité lié à une association
     */
    public static void createNewActivity(Association association, String name) {

        Connection connection = getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO public.activity VALUES (DEFAULT, ?, ?, ?);");
            preparedStatement.setInt(1, association.getId());
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now())); //Date à l'instant
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
