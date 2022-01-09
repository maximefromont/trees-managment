package com.cotisation;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.association.Association;
import com.config.*;
import com.member.Member;

public class CotisationDAO {

    /**
     * @auth Maxime
     * @return Connecte à la base de donnée
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
     * @return Toutes les cotisations
     */
    public static ArrayList<Cotisation> getAllCotisation() {

        ArrayList<Cotisation> cotisations = new ArrayList<Cotisation>();

        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.cotisation");
            while (resultSet.next()) {
                cotisations.add(new Cotisation(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cotisations;
    }

    /**
     * @auth Maxime
     * @param association
     * @return Recupere toutes les cotisations d'una ssociation
     */
    public static ArrayList<Cotisation> getAllCotisationForAssociation(Association association) {

        ArrayList<Cotisation> cotisations = new ArrayList<Cotisation>();

        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.cotisation as co INNER JOIN MEMBER as me ON co.id_member=me.id WHERE me.id_association="+association.getId());
            while (resultSet.next()) {
                cotisations.add(new Cotisation(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cotisations;
    }

    /**
     * @auth Maxime
     * @param id
     * @return Recupère une cotisation en fonction de son is
     */
    public static Cotisation getCotisationById(int id) {

        Cotisation cotisation = null;

        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.cotisation WHERE id="+id);
            if(resultSet.next())
                cotisation = new Cotisation(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getString(4));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cotisation;
    }

    /**
     * @auth Maxime
     * @param member
     * @return Recupèrer toutes les d'un membre
     */
    public static ArrayList<Cotisation> getAllCotisationForMember(Member member) {

        ArrayList<Cotisation> cotisations = new ArrayList<Cotisation>();

        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.cotisation WHERE id_member="+member.getId());
            while (resultSet.next()) {
                cotisations.add(new Cotisation(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getString(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cotisations;
    }
    
    /**
     * @auth Maxime
     * @return recupère la dernière cotisation
     */
    public static Cotisation getLastCotisation() {

        Cotisation cotisation = null;

        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.cotisation ORDER BY id DESC LIMIT 1");
            if(resultSet.next())
                cotisation = new Cotisation(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getString(4));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cotisation;
    }

    /**
     * @auth Maxime
     * @param member
     * @param date
     * @return Recupère toutes les cotisation d'un membre en fonction d'une date
     */
    public static Cotisation getCotisationForMemberByDate(Member member, String date) {

        Cotisation cotisation = null;

        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.cotisation WHERE id_member="+member.getId()+" and (DATE_PART('year', date::date) - DATE_PART('year', '"+date+"'::date))<0;");
            if(resultSet.next())
                cotisation = new Cotisation(resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getString(4));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cotisation;
    }

    /**
     * @auth Maxime
     * @param member
     * @param prix
     * Créé une nouvelle cotisation dans la base de donnnée
     */
    public static void createNewCotisation(Member member, int prix) {

        Connection connection = getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO public.cotisation VALUES (DEFAULT, ?, ?, ?);");
            preparedStatement.setInt(1, member.getId());
            preparedStatement.setInt(2, prix);
            preparedStatement.setString(3, DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now())); //Date à l'instant
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
