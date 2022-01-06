package com.home;

import com.association.Association;
import com.association.AssociationDAO;
import com.cotisation.Cotisation;
import com.cotisation.CotisationDAO;
import com.member.Member;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class launchControl {

    private static Member member;
    private static Association associationMember;

    public static void menu(Member memberRecu) {

        //Initialisation des variable static member et association
        member = memberRecu;
        associationMember = AssociationDAO.getAssociationByMember(member);

        //Affichage membre
        System.out.println("\n" + member.toString());

        //Affichage association
        System.out.println("\n" + associationMember.toString());

        System.out.println("\n" + "Menu : " + "\n" +
                "1: Payer sa cotisation" + "\n" +
                "2: RGPD" + "\n" +
                "Quelle fonction voulez-vous effectuer ? : ");
        Scanner s = new Scanner(System.in);
        switch (s.nextInt()){
            case 1:
                payCotisation();
                break;
            case 2:
                displayRGPD();
                break;
            case 3:
                break;
        }
    }

    private static void payCotisation() {

        System.out.print("\n" + "Veuillez indiquer le montant de votre cotisation : ");
        Scanner s = new Scanner(System.in);
        CotisationDAO.createNewCotisation(member, s.nextInt(), true);

        System.out.println("\n" + "Voici le résumé de votre cotisation : " + "\n");
        ArrayList<Cotisation> cotisations = CotisationDAO.getAllCotisationForMember(member);
        System.out.println(cotisations.get(cotisations.size() - 1).toString()); //Dernière cotisation pour ce membre

        AssociationDAO.updateRecette(associationMember); //Update de la recette de l'association du membre
        associationMember = AssociationDAO.getAssociationByMember(member); //Update de la variable static associationMember

        System.out.println("\n" + associationMember.toString());
    }

    private static void displayRGPD() {
        String fileName = member.getName()+"_"+member.getId()+"_data.txt";
        String encoding = "UTF-8";
        try{
            PrintWriter writer = new PrintWriter(fileName, encoding);
            writer.println(member.toString());
            writer.println("--------------------");
            for(Cotisation cotisation : CotisationDAO.getAllCotisationForMember(member)) {
                writer.println(cotisation.toString());
                writer.println("\n");
            }
            writer.close();

            System.out.println("Un fichier de format txt à été imprimé dans java project.");
        }
        catch (IOException e){
            System.out.println("Une erreur durant l'impression du fichier RGPD à eu lieu.");
            e.printStackTrace();
        }
    }
}
