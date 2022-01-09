package com.home;

import com.activity.Activity;
import com.activity.ActivityDAO;
import com.association.Association;
import com.association.AssociationDAO;
import com.association.Finance;
import com.association.FinanceDAO;
import com.cotisation.Cotisation;
import com.cotisation.CotisationDAO;
import com.mail.SendMail;
import com.member.Member;

import com.member.MemberDAO;

import com.opencsv.bean.CsvToBeanBuilder;

import com.tree.Tree;
import com.vote.Vote;
import com.vote.VoteDAO;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.member.MemberDAO.getAllMemberThatAreDonor;
import static com.vote.VoteDAO.getAllVoteForMember;

public class launchControl {

    private static Member currentMember;
    private static Association associationMember;

    public static void menu(Member memberRecu) {

        //Initialisation des variable static member et association
        currentMember = memberRecu;
        associationMember = AssociationDAO.getAssociationByMember(currentMember);

        boolean programLife = true;
        while(programLife) {
            if (currentMember.isMember()) {

                System.out.print("\n" + "Menu (membre) : " + "\n" +
                        "1 - Payer ma cotisation" + "\n" +
                        "2 - Extraire mes RGPD" + "\n" +
                        "3 - Voter pour un arbre" + "\n" +
                        "4 - Supprimer mon compte membre" + "\n" +
                        "0 - Quitter le programme" + "\n" +
                        "------ PARTIE TECHNIQUE (pour tester) ------" + "\n" +
                        "10 - Exraire le rapport d'activité \n" +
                        "11 - Revoquer les membres n'ayant pas payé leur cotisation \n" +
                        "12 - Faire une demande de don" + "\n" +
                        "13 - Faire une demande de subvention" + "\n" +
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
                        vote();
                        break;
                    case 4:
                        deleteAccount();
                        break;
                    case 10:
                        getActivityReport();
                        break;
                    case 11:
                        checkMemberCotisation();
                        break;
                    case 12:
                        askForDonation();
                        break;
                    case 13:
                        askForSubvention();
                        break;
                }
            }
            if (!currentMember.isMember()) {
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
        System.out.println("\n" + "Merci d'avoir utilisé le programme. Au revoir !");
        System.exit(0);
    }

    private static void askForSubvention() {
        System.out.print("Veuillez renseigner l'adresse email du contact auquel demander une subvention : ");
        String mailTo = new Scanner(System.in).next();

        String message = initAskFor();

        SendMail.main(("Demande de subvention pour "+associationMember.getName()), message, mailTo);
    }

    private static void askForDonation() {
        String message = initAskFor();

        List<Member> donors = getAllMemberThatAreDonor();
        for(int i=0; i<donors.size();i++) {
            SendMail.main(("Demande de donnation pour "+associationMember.getName()), message, donors.get(i).getLogin());
        }
    }

    private static String initAskFor() {
        //Génaration des rapports d'activitées des deux dernières années
        activityReportForYear("2022");
        activityReportForYear("2021");

        String message = "";
        try {
            message = "Bonjour madame, monsieur.\n" +
                    "Afin de préserver tous les arbres qui font la beauté de Paris, nous faisons appel à vous dans l'objectif d'obtenir une subvention.\n" +
                    "Voici nos rapports d'activités des deux dernières années contenant chacun une synthèse : \n\n\n"+
                    Files.readString(Path.of("activity_report_for_year_2022.txt"))+"\n________________________________________________________\n"+
                    Files.readString(Path.of("activity_report_for_year_2021.txt"));
        } catch (IOException e) {
            System.out.println("\nErreur, un problème est survenue lors de la génération du mail.");
            e.printStackTrace();
        }

        return message;
    }

    private static void payCotisation() {

        if(currentMember.isMember()) {

            if(CotisationDAO.getCotisationForMemberByDate(currentMember, DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now())) == null) {

                String answer = "";
                while(!answer.equals("Y") && !answer.equals("N")) {
                    System.out.print("Voulez vous payer votre cotisation de 30 euros (Y/N) : ");
                    answer = new Scanner(System.in).next();
                    if(!answer.equals("Y") && !answer.equals("N")) {
                        System.out.println("Veuillez faire attention à répondre par oui (Y) ou par non (N).");
                    }
                }

                if(answer.equals("Y")) {
                    CotisationDAO.createNewCotisation(currentMember, 30);
                    System.out.println("\n" + "Voici le résumé de votre cotisation : " + "\n");
                    System.out.println(CotisationDAO.getLastMCotisation().getInfo());
                }
                if(answer.equals("N")) {
                    return;
                }

            } else { System.out.println("Vous avez déjà réglè votre cotisation cette année."); }

        } else {

            int montant = -1;
            while (montant <= 0) {
                try{
                    System.out.print("\n" + "Veuillez renseigner le montant de votre donation : ");
                    montant = new Scanner(System.in).nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Erreur, vous devez rentrer un nombre entier.");
                }
                if(montant <= 0) {
                    System.out.println("Veuillez faire attention à ce que le montant renseigné soit supérieur à 0.");
                }
            }

            CotisationDAO.createNewCotisation(currentMember, montant);
            System.out.println("\n" + "Voici le résumé de votre donation : " + "\n");
            System.out.println(CotisationDAO.getLastMCotisation().getInfo());
        }

        //Update de la recette de l'association (et de sa variable statique)
        AssociationDAO.updateRecette(associationMember);
        associationMember = AssociationDAO.getAssociationByMember(currentMember);
    }

    /** displayRGPD
     * @auth Martin & Maxime
     * Crée un fichier contenant les informations de la personne
     */
    private static void displayRGPD() {
        String fileName = currentMember.getName()+"_"+ currentMember.getId()+"_data.txt";
        String encoding = "UTF-8";
        try{
            PrintWriter writer = new PrintWriter(fileName, encoding);
            writer.println(currentMember.toString());
            writer.println("--------------------");
            for(Cotisation cotisation : CotisationDAO.getAllCotisationForMember(currentMember)) {
                writer.println(cotisation.toString());
                writer.println("\n");
            }
            writer.close();

            System.out.println("Un fichier de format txt à été imprimé dans java project.");
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Une erreur durant l'impression du fichier RGPD à eu lieu.");
        }
    }


    /**
     * Crée un fichier contenant le rapport d'activité de l'association pour une année
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
            MemberDAO.deleteMember(currentMember);
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

    /** treeCSV
     * @auth Martin
     * @return List comprenants les arbres du CSV
     */
    private static List<Tree> treesCSV() {
        List<Tree>retour = new ArrayList<>();
        try {
            List<Tree> arbre = new CsvToBeanBuilder(new FileReader("src/main/resources/les-arbres.csv"))
                    .withType(Tree.class)
                    .withSeparator(';')
                    .build()
                    .parse();
            retour=arbre;
        } catch(FileNotFoundException e) {
            System.out.println("Erreur, le fichier 'les-arbres.csv' n'a pas été trouvé.");
            e.printStackTrace();
        }
        return retour;
    }

    /** printTree
     * @auth Martin
     * Imprime dans la consoles les différents arbres
     */
    public static void printTree() {
        List<Tree> liste = treesCSV();
        for (int i = 0; i<liste.size();i++){
            System.out.println(liste.get(i).showMe());
        }
    }

    /** printTree
     * @auth Martin
     * @param lieu Arrondissement/département dont on veut les arbres
     * Imprime les arbres dans la console
     */
    public static void printTree(String lieu) {
        List<Tree> liste = treesCSV();
        for (int i = 0; i<liste.size();i++){
            if(Objects.equals(lieu, liste.get(i).getLocation())) {
                System.out.println(liste.get(i).showMe());
            }
        }
    }

    /** Vote
     * @auth Martin
     * Permet à un membre de voter pour 5 arbres
     */
    public static void vote() {
        ArrayList<Vote> liste = getAllVoteForMember(currentMember);
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
                    VoteDAO.createNewVote(currentMember, s1.nextInt());
                    Scanner s2 = new Scanner(System.in);
                    VoteDAO.createNewVote(currentMember, s2.nextInt());
                    Scanner s3 = new Scanner(System.in);
                    VoteDAO.createNewVote(currentMember, s3.nextInt());
                    Scanner s4 = new Scanner(System.in);
                    VoteDAO.createNewVote(currentMember, s4.nextInt());
                    Scanner s5 = new Scanner(System.in);
                    VoteDAO.createNewVote(currentMember, s5.nextInt());
                    break;
            }
        }
        else{
            System.out.println("Vous avez déjà voté");
        }
    }


    /**
     * Vérifie que chaque membre aie payé sa cotisation. Ils sont radiés sinon.
     * @auth Bastien
     */
    public static void  checkMemberCotisation(){
        boolean selfDeleteCheck = false;

        String date = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now());
        ArrayList<Member> members = MemberDAO.getAllMember();

        for(Member member : members) {
            if(CotisationDAO.getCotisationForMemberByDate(member, date) == null && member.isMember()) {
                System.out.println("Le membre " + member.getName() + " est radié pour non reglement de sa cotisation");
                MemberDAO.deleteMember(member);
                if (member.getId() == currentMember.getId()) {
                    selfDeleteCheck = true;
                }
            }
        }

        if (selfDeleteCheck) {
            exitProgram();
        }

    }

}
