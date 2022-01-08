package com.home;

import com.activity.Activity;
import com.activity.ActivityDAO;
import com.association.Association;
import com.association.AssociationDAO;
import com.cotisation.Cotisation;
import com.cotisation.CotisationDAO;
import com.member.Member;
import com.tree.Tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class launchControl {

    private static Member member;
    private static Association associationMember;

    public static void menu(Member memberRecu) throws FileNotFoundException {

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
                "3: Rapport d'activité \n" +
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
                getActivityReport();
                break;
            case 4:
                printTree();
                break;
        }
    }

    private static void payCotisation() {

        int montant = -1;

        while (montant <= 0) {
            try{
                System.out.print("\n" + "Veuillez indiquer le montant de votre cotisation : ");
                montant = new Scanner(System.in).nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Erreur, vous devez rentrer un nombre entier.");
            }

            if(montant <= 0) {
                System.out.println("Veuillez faire attention à ce que le montant renseigné soit supérieur à 0.");
            }
        }

        CotisationDAO.createNewCotisation(member, montant, true);

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

    private static void activityReportForYear(String year) {

        ArrayList<Activity> activities = ActivityDAO.getAllActivitiesByYear(year);

        String fileName = "activity_report_for_year_" + year + ".txt";
        String encoding = "UTF-8";

        try{
            PrintWriter writer = new PrintWriter(fileName, encoding);
            writer.println("Rapport d'activité pour l'année " + year);
            writer.println("--------------------");
            if (activities.size() == 0)
            {
                writer.println("Pas d'activité cette année");
                writer.println("--------------------");
            }
            else {
                for (Activity activity : activities) {
                    writer.println(activity.info());
                    writer.println("--------------------");
                }
            }
            writer.close();

            System.out.println("Un fichier de format txt à été imprimé dans java project.");
        }
        catch (IOException e){
            System.out.println("Une erreur durant l'impression du rapport d'activité à eu lieu.");
            e.printStackTrace();
        }

    }

    private static void getActivityReport() {
        int year = 0;
        boolean condition = false;

        while (!condition) {
            try{
                System.out.print("\n" + "Veuillez indiquer l'année du rapport : ");
                year = new Scanner(System.in).nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Erreur, vous devez rentrer une année valide.");
            }

            condition = true;
            if(year < 1900 || year > 2100) {
                System.out.println("Erreur, vous devez rentrer une année valide.");
                condition = false;
            }
        }

        activityReportForYear(String.valueOf(year));
    }

    private static ArrayList<Tree> treesCSV() throws FileNotFoundException {
        File getCSVFile = new File("src/main/resources/les-arbres.csv");
        Scanner sc = new Scanner(getCSVFile);
        sc.useDelimiter(";|\\n");
        ArrayList<Tree> Retour = new ArrayList<Tree>();
        sc.next();sc.next();sc.next();sc.next();sc.next();sc.next();sc.next();sc.next();sc.next();sc.next();sc.next();sc.next();sc.next();sc.next();sc.next();sc.next();
        while (sc.hasNext()){
            String id= sc.next();
            sc.next();
            sc.next();
            String loc=sc.next();
            sc.next();
            sc.next();
            sc.next();
            sc.next();
            String name = sc.next();
            String type = sc.next();
            String spe = sc.next();
            sc.next();
            String cir = sc.next();
            String hei = sc.next();
            sc.next();
            String rem = sc.next();
            boolean remar = !Objects.equals(rem, "NON");
            sc.next();
            Retour.add(new Tree(id,cir,hei,loc,name,type,spe,remar));
           // System.out.println(Retour.size());
        }
       // Retour.remove(0);
        return Retour;
    }
    public static void printTree() throws FileNotFoundException {
        ArrayList<Tree> liste = treesCSV();
        for (int i = 0; i<liste.size();i++){
            System.out.println(liste.get(i).showMe());
        }
    }

    public static void orintTree(String lieu) throws FileNotFoundException {
        ArrayList<Tree> liste = treesCSV();
        for (int i = 0; i<liste.size();i++){
            if(lieu==liste.get(i).getLocation()) {
                System.out.println(liste.get(i).showMe());
            }
        }
    }
}
