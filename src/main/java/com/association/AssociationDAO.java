package com.association;

import java.sql.*;
import java.util.ArrayList;

import com.config.*;
import com.cotisation.Cotisation;
import com.cotisation.CotisationDAO;
import com.member.Member;

public class AssociationDAO {

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

    public static ArrayList<Association> getAllAssociation() {

        ArrayList<Association> associations = new ArrayList<Association>();

        Connection connection = getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.association");
            while (resultSet.next()) {
                associations.add(new Association(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getInt(4)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return associations;
    }

    public  static  Association getAssociationByMember(Member member) {

        Connection connection = getConnection();
        Association association = null;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.association WHERE id="+member.getId_association());
            resultSet.next();
            association = new Association(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getInt(4));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return association;

    }

    public static void updateRecette(Association association) {

        Connection connection = getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE public.association SET recette=? WHERE id=?;");

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
}
