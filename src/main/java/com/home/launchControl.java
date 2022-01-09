package com.home;

import com.activity.Activity;
import com.activity.ActivityDAO;
import com.association.Association;
import com.association.AssociationDAO;
import com.association.Finance;
import com.association.FinanceDAO;
import com.cotisation.Cotisation;
import com.cotisation.CotisationDAO;
import com.member.Member;

import com.member.MemberDAO;

import com.opencsv.bean.CsvToBeanBuilder;

import com.tree.Tree;
import com.vote.Vote;
import com.vote.VoteDAO;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.vote.VoteDAO.getAllVoteForMember;

public class launchControl {

    private static Member member;
    private static Association associationMember;

    public static void menu(Member memberRecu) {

        //Initialisation des variable static member et association
        member = memberRecu;
        associationMember = AssociationDAO.getAssociationByMember(member);

        boolean programLife = true;
        while(programLife) {
            if (member.isMember()) {
                System.out.print("\n" + "Menu (membre) : " + "\n" +
                        "1 - Payer ma cotisation" + "\n" +
                        "2 - Extraire mes RGPD" + "\n" +
                        "3 - Voter pour un arbre" + "\n" +
                        "4 - Supprimer mon compte membre" + "\n" +
                        "0 - Quitter le programme" + "\n" +
                        "------ PARTIE TECHNIQUE (pour tester) ------" + "\n" +
                        "10: Exraire le rapport d'activité \n" +
                        "Votre choix : ");
                Scanner s = new Scanner(System.in);
                switch (s.nextInt()) {
                    case 0:
                        programLife = false;
                        break;
                    case 1:
                        payCotisation();
                        break;
                    case 2:
                        displayRGPD();
                        break;
                    case 3:
                        try {
                            vote();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 4:
                        deleteAccount();
                        break;
                    case 10:
                        getActivityReport();
                        break;
                }
            }
            if (!member.isMember()) {
                System.out.print("\n" + "Menu (doneur) : " + "\n" +
                        "1 - Verser un don" + "\n" +
                        "2 - Extraire mes RGPD" + "\n" +
                        "3 - Supprimer mon compte doneur" + "\n" +
                        "0 - Quitter le programme" + "\n" +
                        "Votre choix : ");
                switch (new Scanner(System.in).nextInt()) {
                    case 0:
                        programLife = false;
                        break;
                    case 1:
                        payCotisation();
                        break;
                    case 2:
                        displayRGPD();
                        break;
                    case 3:
                        deleteAccount();
                        break;
                }
            }
        }

        exitProgram();
    }

    private static void exitProgram() {
        System.out.println("\n" + "Merci d'avoir utilisé le programme. Au revcoir !");
        System.exit(0);
    }


    private static void payCotisation() {

        if(member.isMember()) {
            if(CotisationDAO.getCotisationForMemberByDate(member, DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now())) == null) {
                String answer = "";
                while(!answer.equals("Y") && !answer.equals("N")) {
                    System.out.println("Voulez vous payer votre cotisation de 30 euros (Y/N) : ");
                    answer = new Scanner(System.in).next();
                    if(!answer.equals("Y") && !answer.equals("N")) {
                        System.out.println("Veuillez faire attention à répondre par oui (Y) ou par non (N).");
                    }
                }

                if(answer.equals("Y")) {
                    CotisationDAO.createNewCotisation(member, 30);
                    System.out.println("\n" + "Voici le résumé de votre cotisation : " + "\n");
                }
                if(answer.equals("N")) {
                    return;
                }
            } else {
                System.out.println("Vous avez déjà réglè votre cotisation cette année.");
            }

        } else {

            int montant = -1;

            while (montant <= 0) {
                try{
                    System.out.print("\n" + "Veuillez indiquer le montant de votre donation : ");
                    montant = new Scanner(System.in).nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Erreur, vous devez rentrer un nombre entier.");
                }

                if(montant <= 0) {
                    System.out.println("Veuillez faire attention à ce que le montant renseigné soit supérieur à 0.");
                }
            }
            CotisationDAO.createNewCotisation(member, montant);
            System.out.println("\n" + "Voici le résumé de votre cotisation : " + "\n");
        }

        Cotisation cotisation = CotisationDAO.getLastMCotisation();

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


    /**
     * Ecrit dans un fichier le rapport d'activité pour une année
     * @auth Bastien
     * @param year
     */
    private static void activityReportForYear(String year) {

        ArrayList<Activity> activities = ActivityDAO.getAllActivitiesByYear(year);

        String fileName = "activity_report_for_year_" + year + ".txt";
        String encoding = "UTF-8";

        try{
            PrintWriter writer = new PrintWriter(fileName, encoding);
            //Finances
            writer.println("Finances pour l'année " + year);
            Finance finance = FinanceDAO.getFinanceForYear(associationMember, year);
            if (finance == null) {
                writer.println("Pas de finances pour l'année " + year);
            }
            else {
                writer.println("Dépenses : " +finance.getDepense());
                writer.println("Recettes : " +finance.getRecette());
            }
            //Activité
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

    private static void deleteAccount() {

        String answer = "";
        while(!answer.equals("Y") && !answer.equals("N")) {
            System.out.print("\n" + "Êtes vous sur de vouloir supprimer votre compte (Y/N) : ");
            answer = new Scanner(System.in).next();
            if(!answer.equals("Y") && !answer.equals("N")) {
                System.out.println("Veuillez faire attention à répondre par oui (Y) ou par non (N).");
            }
        }

        if(answer.equals("Y")) {
            MemberDAO.deleteMember(member);
            exitProgram();
        }

    }

    /**
     * Fonction menu pour obtenir un rapport d'activité
     */
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


    private static List<Tree> treesCSV() {
        List<Tree>retour = new ArrayList<>();
        try {
            List<Tree> arbre = new CsvToBeanBuilder(new FileReader("src/main/resources/les-arbres.csv"))
                    .withType(Tree.class)
                    .withSeparator(';')
                    .build()
                    .parse();
            retour=arbre;
        } catch(IOException e) {
            System.out.println("Erreur, le fichier 'les-arbres.csv' n'a pas été trouvé.");
            e.printStackTrace();
        }
        return retour;
    }

    public static void printTree() throws FileNotFoundException {
        List<Tree> liste = treesCSV();
        for (int i = 0; i<liste.size();i++){
            System.out.println(liste.get(i).showMe());
        }
    }

    public static void printTree(String lieu) throws FileNotFoundException {
        List<Tree> liste = treesCSV();
        for (int i = 0; i<liste.size();i++){
            if(Objects.equals(lieu, liste.get(i).getLocation())) {
                System.out.println(liste.get(i).showMe());
            }
        }
    }

    public static void vote() throws FileNotFoundException {
        ArrayList<Vote> liste = getAllVoteForMember(member);
        if(liste.size()<5) {
            System.out.println("Voulez vous  avoir la liste des arbres avant de voter (1) ou voter directement (2) ?\n");
            Scanner s = new Scanner(System.in);
            switch (s.nextInt()) {
                case 1:
                    System.out.println("De quel arrondissement/département voulez vous voir les arbres ?\n" +
                            "0 : Tous les arbres" +
                            "1 : Paris, 1er arrondissement" +
                            "2 : Paris, 2eme arrondissement" +
                            "3 : Paris, 3eme arrondissement" +
                            "4 : Paris, 4eme arrondissement" +
                            "5 : Paris, 5eme arrondissement" +
                            "6 : Paris, 6eme arrondissement" +
                            "7 : Paris, 7eme arrondissement" +
                            "8 : Paris, 8eme arrondissement" +
                            "9 : Paris, 9eme arrondissement" +
                            "10 : Paris, 10eme arrondissement" +
                            "11 : Paris, 11eme arrondissement" +
                            "12 : Paris, 12eme arrondissement" +
                            "13 : Paris, 13eme arrondissement" +
                            "14 : Paris, 14eme arrondissement" +
                            "15 : Paris, 15eme arrondissement" +
                            "16 : Paris, 16eme arrondissement" +
                            "17 : Paris, 17eme arrondissement" +
                            "18 : Paris, 18eme arrondissement" +
                            "19 : Paris, 19eme arrondissement" +
                            "20 : Paris, 20eme arrondissement" +
                            "21 : Bois de Boulogne" +
                            "22 : Bois de Vincennes" +
                            "23 : Hauts-de-Seine" +
                            "24 : Seine-Saint-Denis" +
                            "25 : Val-de-Marne");
                    Scanner c = new Scanner(System.in);
                    int num = c.nextInt();
                    if (num == 0) {
                        printTree();
                    } else if (num <= 20) {
                        printTree("PARIS " + num + "E ARRDT");
                    } else {
                        switch (num) {
                            case 21:
                                printTree("BOIS DE BOULOGNE");
                                break;
                            case 22:
                                printTree("BOIS DE VINCENNES");
                                break;
                            case 23:
                                ;
                                printTree("HAUTS-DE-SEINE");
                                break;
                            case 24:
                                ;
                                printTree("SEINE-SAINT-DENIS");
                                break;
                            case 25:
                                ;
                                printTree("VAL-DE-MARNE");
                                break;
                        }
                    }
                case 2:
                    System.out.println("Veuillez rentrer les id des 5 arbres (l'ordre n'a pas d'importance)");
                    Scanner s1 = new Scanner(System.in);
                    VoteDAO.createNewVote(member, s1.nextInt());
                    Scanner s2 = new Scanner(System.in);
                    VoteDAO.createNewVote(member, s2.nextInt());
                    Scanner s3 = new Scanner(System.in);
                    VoteDAO.createNewVote(member, s3.nextInt());
                    Scanner s4 = new Scanner(System.in);
                    VoteDAO.createNewVote(member, s4.nextInt());
                    Scanner s5 = new Scanner(System.in);
                    VoteDAO.createNewVote(member, s5.nextInt());
                    break;
            }
        }
        else{
            System.out.println("Vous avez déjà voté");
        }
    }

}
