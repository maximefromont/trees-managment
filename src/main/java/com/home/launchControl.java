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
import static com.vote.VoteDAO.getAllVote;
import static com.vote.VoteDAO.getAllVoteForMember;
import static java.lang.Integer.parseInt;
import static java.util.Collections.frequency;

public class launchControl {

    private static Member currentMember;
    private static Association associationMember;
    private static String treeFileName;

    public static void menu(Member memberRecu, String treeFileNameRecu) {

        //Initialisation des variable static member et association
        currentMember = memberRecu;
        associationMember = AssociationDAO.getAssociationByMember(currentMember);
        treeFileName = treeFileNameRecu;

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
                        "14 - Resultats des votes\n" +
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
                    case 14:
                        resultVote();
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

    public static void exitProgram() {
        System.out.println("\n" + "Merci d'avoir utilisé le programme. Au revoir !");
        System.exit(0);
    }

    /**
     * @auth Martin
     * Envoie un mail de demande de subvention
     */
    private static void askForSubvention() {
        System.out.print("Veuillez renseigner l'adresse email du contact auquel demander une subvention : ");
        String mailTo = new Scanner(System.in).next();

        String message = initAskFor("subvention");

        SendMail.main(("Demande de subvention pour "+associationMember.getName()), message, mailTo);
    }

    /**
     * @auth Martin
     * Envoie un mail de demande de donnation à tous les donnateurs
     */
    private static void askForDonation() {
        String message = initAskFor("donation");

        List<Member> donors = getAllMemberThatAreDonor();
        for(int i=0; i<donors.size();i++) {
            SendMail.main(("Demande de donnation pour "+associationMember.getName()), message, donors.get(i).getLogin());
        }
    }

    /**
     * @auth Martin
     * @param type type de demande
     * @return message du meil
     */
    private static String initAskFor(String type) {
        //Génaration des rapports d'activitées des deux dernières années
        String year = DateTimeFormatter.ofPattern("yyyy").format(LocalDateTime.now());
        String lastYear = Integer.toString(parseInt(year)-1);
        activityReportForYear(year);
        activityReportForYear(lastYear);

        String message = "";
        try {
            message = "Bonjour madame, monsieur.\n" +
                    "Afin de préserver tous les arbres qui font la beauté de Paris, nous faisons appel à vous dans l'objectif d'obtenir une "+type+".\n" +
                    "Voici nos rapports d'activités des deux dernières années contenant chacun une synthèse : \n\n\n"+
                    Files.readString(Path.of(associationMember.getName()+"_activity_report_for_year_" + year + ".txt"))+"\n________________________________________________________\n"+
                    Files.readString(Path.of(associationMember.getName()+"_activity_report_for_year_" + lastYear + ".txt"));
        } catch (IOException e) {
            System.out.println("Erreur, un problème est survenue lors de la génération du mail.");
            e.printStackTrace();
        }

        return message;
    }

    /**
     * @auth Martin & Maxime
     * Demande à l'utlisateur de payer une cotisation ou une donation en fonction de son type
     */
    private static void payCotisation() {

        if(currentMember.isMember()) {

            if(CotisationDAO.getCotisationForMemberByDate(currentMember, DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now())) == null) {

                int montant = 30; //Montant en dur
                String answer = "";
                while(!answer.equals("Y") && !answer.equals("N")) {
                    System.out.print("Voulez vous payer votre cotisation de "+montant+" euros (Y/N) : ");
                    answer = new Scanner(System.in).next();
                    if(!answer.equals("Y") && !answer.equals("N")) {
                        System.out.println("Veuillez faire attention à répondre par oui (Y) ou par non (N).");
                    }
                }

                if(answer.equals("Y")) {
                    CotisationDAO.createNewCotisation(currentMember, 30);
                    ActivityDAO.createNewActivity(associationMember, associationMember.getName()+" à reçu une cotisation de "+montant+" euros de la part de "+currentMember.getName()+".");
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
            ActivityDAO.createNewActivity(associationMember, associationMember.getName()+" à reçu une donation de "+montant+" de la part de "+currentMember.getName()+".");
            System.out.println("\n" + "Voici le résumé de votre donation : " + "\n");
            System.out.println(CotisationDAO.getLastMCotisation().getInfo());
        }

        //Update de la recette de l'association (et de sa variable statique)
        AssociationDAO.updateRecette(associationMember);
        associationMember = AssociationDAO.getAssociationByMember(currentMember);
    }

    /**
     * @auth Martin & Maxime
     * Crée un fichier contenant les informations de l'utilisateur
     */
    private static void displayRGPD() {
        String fileName = currentMember.getName()+"_"+ currentMember.getId()+"_data.txt";
        String encoding = "UTF-8";

        try{
            PrintWriter writer = new PrintWriter(fileName, encoding);
            writer.println(currentMember.getInfo());
            writer.println("--------------------");
            for(Cotisation cotisation : CotisationDAO.getAllCotisationForMember(currentMember)) {
                writer.println(cotisation.getInfo());
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
     * @auth Bastien
     * Demande à l'utilisateur de saisir une année et débute l'impression d'un rapport d'activité de l'association
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

    /**
     * Crée un fichier contenant le rapport d'activité de l'association pour une année
     * @auth Bastien
     * @param year
     */
    private static void activityReportForYear(String year) {
        ArrayList<Activity> activities = ActivityDAO.getAllActivitiesByYear(year);

        String fileName = associationMember.getName()+"_activity_report_for_year_" + year + ".txt";
        String encoding = "UTF-8";

        try{
            PrintWriter writer = new PrintWriter(fileName, encoding);

            //Finances
            writer.println("Etats des finances pour l'année "+year+" : ");
            Finance finance = FinanceDAO.getFinanceForYear(associationMember, year);
            if (finance == null) {
                writer.println("Aucune informations sur cette année.");
            } else {
                writer.println("Dépense : " +finance.getDepense());
                writer.println("Recette : " +finance.getRecette());
            }

            //Activité
            writer.println("Synthèse des activitées pour l'année "+year+" : ");
            if (activities.size() == 0)
            {
                writer.println("Aucune activitées concernant cette année.");
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
            e.printStackTrace();
            System.out.println("Une erreur durant l'impression du rapport d'activité à eu lieu.");
        }

    }

    /**
     * @auth Maxime
     * Demande à l'utilisateur si il souhaite supprimer son compte, et si oui, le supprime et quitte le programme
     */
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
        if (answer.equals("N")) {
            return;
        }
    }

    /**
     * @auth Martin
     * @return Une liste comprenant les arbres du fichier CSV
     */
    private static List<Tree> treesCSV() {
        List<Tree> arbres = new ArrayList<>();
        try {
            arbres = new CsvToBeanBuilder(new FileReader(treeFileName))
                    .withType(Tree.class)
                    .withSeparator(';')
                    .build()
                    .parse();
        } catch(FileNotFoundException e) {
            System.out.println("Erreur, le fichier 'les-arbres.csv' n'a pas été trouvé.");
            e.printStackTrace();
        }
        return arbres;
    }

    /**
     * @auth Martin
     * Imprime la liste de tous les abres dans la console (fonction lente)
     */
    public static void printTree() {
        List<Tree> liste = treesCSV();
        for (int i = 0; i<liste.size();i++){
            System.out.println(liste.get(i).showMe());
        }
    }

    /**
     * @auth Martin
     * @param lieu Arrondissement/département
     * Imprime les arbres d'un certain arrondissement dans la console (fonction lente)
     */
    public static void printTree(String lieu) {
        List<Tree> liste = treesCSV();
        for (int i = 0; i<liste.size();i++){
            if(Objects.equals(lieu, liste.get(i).getLocation())) {
                System.out.println(liste.get(i).showMe());
            }
        }
    }

    /**
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

    /** resultVote
     * @auth Martin
     * Renvoie le résultat du vote des arbres à faire passen en arbre remarquable
      */
    private static void resultVote(){
        List<Vote> resultbeta = getAllVote();
        List<String> result = new ArrayList<String>();
        for(int i=0;i<resultbeta.size();i++){
            result.add(String.valueOf(resultbeta.get(i).getId_arbre()));
        }
        List<Tree> arbre = treesCSV();
        Tree first,second = new Tree("-1","-1","-1","-1","-1","-1","-1","-1"),third = new Tree("-1","-1","-1","-1","-1","-1","-1","-1"),fourth = new Tree("-1","-1","-1","-1","-1","-1","-1","-1"),fifth = new Tree("-1","-1","-1","-1","-1","-1","-1","-1");
        first=arbre.get(0);
        for (int i=1;i<arbre.size();i++){
            if(frequency(result, arbre.get(i).getId())>frequency(result, first.getId()) ||
                    frequency(result, arbre.get(i).getId())==frequency(result, first.getId()) && parseInt(arbre.get(i).getCir())>parseInt(first.getCir()) ||
                    frequency(result, arbre.get(i).getId())==frequency(result, first.getId()) && parseInt(arbre.get(i).getCir())==parseInt(first.getCir()) && parseInt(arbre.get(i).getHeight())>parseInt(first.getHeight())){
                fifth=fourth;
                fourth=third;
                third=second;
                second=first;
                first=arbre.get(i);
            }
            else if(frequency(result, arbre.get(i).getId())>frequency(result, second.getId()) ||
                    frequency(result, arbre.get(i).getId())==frequency(result, second.getId()) && parseInt(arbre.get(i).getCir())>parseInt(second.getCir()) ||
                    frequency(result, arbre.get(i).getId())==frequency(result, second.getId()) && parseInt(arbre.get(i).getCir())==parseInt(second.getCir()) && parseInt(arbre.get(i).getHeight())>parseInt(second.getHeight())) {
                fifth = fourth;
                fourth = third;
                third = second;
                second = arbre.get(i);
            }
            else if(frequency(result, arbre.get(i).getId())>frequency(result, third.getId()) ||
                    frequency(result, arbre.get(i).getId())==frequency(result, third.getId()) && parseInt(arbre.get(i).getCir())>parseInt(third.getCir()) ||
                    frequency(result, arbre.get(i).getId())==frequency(result, third.getId()) && parseInt(arbre.get(i).getCir())==parseInt(third.getCir()) && parseInt(arbre.get(i).getHeight())>parseInt(third.getHeight())) {
                fifth = fourth;
                fourth = third;
                third = arbre.get(i);
            }
            else if(frequency(result, arbre.get(i).getId())>frequency(result, fourth.getId()) ||
                    frequency(result, arbre.get(i).getId())==frequency(result, fourth.getId()) && parseInt(arbre.get(i).getCir())>parseInt(fourth.getCir()) ||
                    frequency(result, arbre.get(i).getId())==frequency(result, fourth.getId()) && parseInt(arbre.get(i).getCir())==parseInt(fourth.getCir()) && parseInt(arbre.get(i).getHeight())>parseInt(fourth.getHeight())) {
                fifth = fourth;
                fourth = arbre.get(i);
            }
            else if(frequency(result, arbre.get(i).getId())>frequency(result, fifth.getId()) ||
                    frequency(result, arbre.get(i).getId())==frequency(result, fifth.getId()) && parseInt(arbre.get(i).getCir())>parseInt(fifth.getCir()) ||
                    frequency(result, arbre.get(i).getId())==frequency(result, fifth.getId()) && parseInt(arbre.get(i).getCir())==parseInt(fifth.getCir()) && parseInt(arbre.get(i).getHeight())>parseInt(fifth.getHeight())) {
                fifth = arbre.get(i);
            }
        }
        System.out.println("Resultats :\n" +
                "1er - "+first.showMe()+", "+frequency(result, first.getId())+" votes\n" +
                " 2eme - "+second.showMe()+", "+frequency(result, second.getId())+" votes\n" +
                " 3eme - "+third.showMe()+", "+frequency(result, third.getId())+" votes\n" +
                " 4eme - "+fourth.showMe()+", "+frequency(result, fourth.getId())+" votes\n" +
                " 5eme - "+fifth.showMe()+", "+frequency(result, fifth.getId())+" votes\n");
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
