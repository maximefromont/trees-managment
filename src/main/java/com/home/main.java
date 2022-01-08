package com.home;

import com.activity.ActivityDAO;
import com.association.Association;
import com.association.AssociationDAO;
import com.member.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class main {

    public static void main(String[] args) throws IOException {

        int answer = -1;
        while (answer < 0) {

            System.out.println("1 - Créer un compte");
            System.out.println("2 - Se connecter");
            System.out.println("0 - Quitter le programme");

            try{
                System.out.print("\n" + "Votre choix : ");
                answer = new Scanner(System.in).nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Erreur, vous devez rentrer un nombre entier.");
            }

            if(answer < 0) {
                System.out.println("Veuillez faire attention à ce que votre choix soit supérieur ou égal à 0.");
            }
        }

        switch (answer) {
            case 0:
                break;
            case 1:
                register();
                break;
            case 2:
                login();
                break;
        }

    }

    private static void register() throws IOException {

        Association association = AssociationDAO.getAssociationById(1); //Association 1 en dur
        boolean type = false;
        String login = "";
        String mdp = "";
        String name = "";
        String birth = "";
        String adress = "";

        boolean verifLogin = false;
        while(!verifLogin) {
            System.out.print("\n" + "Veuillez renseigner un login sous forme d'adresse email : ");
            String tempLogin = new Scanner(System.in).next();
            if(MemberDAO.getMemberByLogin(tempLogin) == null) {
                verifLogin = true;
                login = tempLogin;
            } else {
                System.out.println("Désolé mais ce login existe déjà.");
            }
        }

        boolean verifPassword = false;
        while(!verifPassword) {
            System.out.print("\n" + "Veuillez renseigner un mot de passe : ");
            String tempMdp = new Scanner(System.in).next();
            System.out.print("Veuillez confirmer votre mot de passe : ");
            String tempMdp2 = new Scanner(System.in).next();
            if(tempMdp.equals(tempMdp2)) {
                verifPassword = true;
                mdp = tempMdp;
            } else {
                System.out.println("Désolé, les deux mots de passe que vous avez renseigné ne sont pas identique.");
            }
        }

        System.out.print("\n" + "Veuillez indiquer votre nom : ");
        name = new Scanner(System.in).nextLine();

        //Pas de vérification attention
        System.out.print("\n" + "Veuillez indiquer votre date d'anniversaire en respectant le format JJ/MM/AAAA : ");
        birth = new Scanner(System.in).next();

        System.out.print("\n" + "Veuillez indiquer votre adresse postale : ");
        adress = new Scanner(System.in).nextLine();

        System.out.println("\n" + "Veuillez renseigner le type du compte : ");
        System.out.println("1 - Membre d'une association");
        System.out.println("2 - Donneur pour une association");
        int answer = -1;
        while(answer <= 0) {
            try{
                System.out.print("Votre choix : ");
                answer = new Scanner(System.in).nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Erreur, vous devez rentrer un nombre entier.");
            }
            if(answer <= 0) {
                System.out.println("Veuillez faire attention à ce que votre choix soit supérieur à 0.");
            }
        }
        if (answer == 1) {type = true;}
        if (answer == 2) {type = false;}

        MemberDAO.createNewMember(association, type, login, mdp, name, birth, adress);

        Member member = MemberDAO.getLastMember();

        ActivityDAO.createNewActivity(association, "Création d'un compte de type : " + member.getType() + " et de login : "+member.getLogin() +" (id = "+member.getId()+").");
        System.out.println("\n" + "Bienvenue "+member.getName()+"! Lancement du programme.");

        launchControl.menu(member);

    }

    private static void login() {

        System.out.print("\n" + "Bienvenue, veuillez indiquer votre login : ");
        Member member = MemberDAO.getMemberByLogin(new Scanner(System.in).next());
        if(member != null) {
            System.out.print("Et votre mot de passe : ");
            String password = new Scanner(System.in).next();

            if(password.equals(member.getMdp())) {
                System.out.println("\n" + "Bienvenue "+member.getName()+" ! Lancement du programme.");
                launchControl.menu(member);
            } else {
                System.out.println("Le mot de passe est incorrecte.");
            }
        } else {
            System.out.println("L'identifiant est incorrecte.");
        }
    }
}
