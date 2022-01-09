package com.visite;

import com.activity.Activity;
import com.association.Association;
import com.config.ReadProperties;
import com.cotisation.Cotisation;
import com.member.Member;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class VisiteDAO {

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

    public static Visite getLastVisite() {

        Visite visite = null;

        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.visite ORDER BY id DESC LIMIT 1");
            if(resultSet.next())
                visite = new Visite(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getString(5));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return visite;
    }

    public static ArrayList<Visite> getAllVisites() {

        ArrayList<Visite> visites = new ArrayList<Visite>();

        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.visite");
            while (resultSet.next()) {
                visites.add(new Visite(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getString(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return visites;
    }

    public static ArrayList<Visite> getAllVisitesForMember(Member member) {

        ArrayList<Visite> visites = new ArrayList<Visite>();

        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.visite WHERE id_member="+member.getId());
            while (resultSet.next()) {
                visites.add(new Visite(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getString(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return visites;
    }

    public static void createNewVisite(Member member, int id_arbre, int prix, String compte_rendu) {

        Connection connection = getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO public.visite VALUES (DEFAULT, ?, ?, ?, ?);");
            preparedStatement.setInt(1, member.getId());
            preparedStatement.setInt(2, id_arbre);
            preparedStatement.setInt(3, prix);
            preparedStatement.setString(4, compte_rendu);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
