package com.cotisation;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.association.Association;
import com.config.*;
import com.member.Member;

public class CotisationDAO {

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

    public static ArrayList<Cotisation> getAllCotisation() {

        ArrayList<Cotisation> cotisations = new ArrayList<Cotisation>();

        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.cotisation");
            while (resultSet.next()) {
                cotisations.add(new Cotisation(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getString(4), resultSet.getBoolean(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cotisations;
    }

    public static ArrayList<Cotisation> getAllCotisationForAssociation(Association association) {

        ArrayList<Cotisation> cotisations = new ArrayList<Cotisation>();

        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.cotisation as co INNER JOIN MEMBER as me ON co.id_member=me.id WHERE me.id_association="+association.getId());
            while (resultSet.next()) {
                cotisations.add(new Cotisation(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getString(4), resultSet.getBoolean(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cotisations;
    }

    public static Cotisation getCotisationById(int id) {

        Cotisation cotisation = null;

        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.cotisation WHERE id="+id);
            resultSet.next();
            cotisation = new Cotisation(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getString(4), resultSet.getBoolean(5));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cotisation;
    }

    public static ArrayList<Cotisation> getAllCotisationForMember(Member member) {

        ArrayList<Cotisation> cotisations = new ArrayList<Cotisation>();

        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.cotisation WHERE id_member="+member.getId());
            while (resultSet.next()) {
                cotisations.add(new Cotisation(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getString(4), resultSet.getBoolean(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cotisations;
    }

    public static void createNewCotisation(Member member, int prix, boolean paid) {

        Connection connection = getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO public.cotisation VALUES (DEFAULT, ?, ?, ?, ?);");
            preparedStatement.setInt(1, member.getId());
            preparedStatement.setInt(2, prix);
            preparedStatement.setString(3, DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now())); //Date à l'instant
            preparedStatement.setBoolean(4, paid);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
