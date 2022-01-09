package com.association;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.config.*;
import com.cotisation.Cotisation;
import com.cotisation.CotisationDAO;
import com.member.Member;

public class AssociationDAO {

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
     * @return Toutes les associations
     */
    public static ArrayList<Association> getAllAssociation() {

        ArrayList<Association> associations = new ArrayList<Association>();

        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.association");
            while (resultSet.next()) {
                associations.add(new Association(resultSet.getInt(1), resultSet.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return associations;
    }

    /**
     * @auth Maxime
     * @param member
     * @return association du membre
     */
    public  static  Association getAssociationByMember(Member member) {

        Connection connection = getConnection();
        Association association = null;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.association WHERE id="+member.getId_association());
            if(resultSet.next())
                association = new Association(resultSet.getInt(1), resultSet.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return association;

    }

    /**
     * @auth Maxime
     * @param id
     * @return ASssotiation aen fonction de son id
     */
    public  static  Association getAssociationById(int id) {

        Connection connection = getConnection();
        Association association = null;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.association WHERE id="+id);
            if(resultSet.next())
                association = new Association(resultSet.getInt(1), resultSet.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return association;

    }

    /**
     * @auth Maxime
     * @param association met à jour la recette de l'association
     */
    public static void updateRecette(Association association) {

        Connection connection = getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE public.finance SET recette=? WHERE id_association=? and date='"+ DateTimeFormatter.ofPattern("yyyy").format(LocalDateTime.now())+"';");

            int sommeCotisations = 0;
            for(Cotisation cotisation : CotisationDAO.getAllCotisationForAssociation(association)) {
                sommeCotisations += cotisation.getPrix();
            }

            preparedStatement.setInt(1, sommeCotisations);
            preparedStatement.setInt(2, association.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * @auth
     * @param association
     * @param montant met à jour les dépenses de l'association
     */
    public static void updateDepense(Association association, int montant) {

        Connection connection = getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE public.finance SET depense=depense+? WHERE id_association=? and date='"+ DateTimeFormatter.ofPattern("yyyy").format(LocalDateTime.now())+"';");

            preparedStatement.setInt(1, montant);
            preparedStatement.setInt(2, association.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
