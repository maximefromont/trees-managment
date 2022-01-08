package com.home;

import com.activity.Activity;
import com.activity.ActivityDAO;
import com.association.Association;
import com.association.AssociationDAO;
import com.config.*;
import com.cotisation.Cotisation;
import com.cotisation.CotisationDAO;
import com.member.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class main {

    public static void main(String[] args) throws FileNotFoundException {

        /*
        //Exemple de récupération d'un lien depuis le fichier de config
        System.out.println(ReadProperties.getConfig("linkBDD"));
        System.out.println(ReadProperties.getConfig("loginBDD"));
        System.out.println(ReadProperties.getConfig("mdpBDD"));
        System.out.println("\n");
         */

        /*
        //Exemple de récupération de tout les membres
        for (Member member : MemberDAO.getAllMember()) {
           System.out.println(member.toString());
            System.out.println("------------------");
        }
        System.out.println("\n");

        //Exemple de récupération de toute les associations
        for (Association association : AssociationDAO.getAllAssociation()) {
            System.out.println(association.toString());
            System.out.println("------------------");
        }
        System.out.println("\n");

        //Exemple de récupération de toute les activités
        for (Activity activity : ActivityDAO.getAllActivities()) {
            System.out.println(activity.toString());
            System.out.println("------------------");
        }
        System.out.println("\n");

        //Exemple de récupération de toute les cotisations
        for (Cotisation cotisation : CotisationDAO.getAllCotisation()) {
            System.out.println(cotisation.toString());
            System.out.println("------------------");
        }
        System.out.println("\n");
         */

        System.out.print("Bienvenue, veuillez indiquer votre login : ");
        Member member = MemberDAO.getMemberByLogin(new Scanner(System.in).nextLine());
        System.out.print("Et votre mot de passe : ");
        String password = new Scanner(System.in).nextLine();

        if(password.equals(member.getMdp())) {
            System.out.println("Bienvenue "+member.getName()+" ! Lancement du programme.");
            launchControl.menu(member);
        } else {
            System.out.println("L'identifiant ou le mot de passe est incorrect.");
        }

        //Changer le numéro pour changer de membre
        //Dans les données de bases de la base, 1 = Maxime, 2 = Martin et 3 = Bastien
        //Member memberTest = MemberDAO.getMemberById(2);

    }

}
