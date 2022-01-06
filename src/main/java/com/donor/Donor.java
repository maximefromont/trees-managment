package com.donor;

import com.association.Association;

import java.io.IOException;
import java.io.PrintWriter;

//ATTENTION : Cette classe ne sert à rien et ne doit pas être utilisé, en effet, un donneur est un member avec un type "donor".
//Les membres quand à eux sont de type "memberAssos". Il faudrait que je renomme la table "Member" en "User" dans la base de donnée mais pour l'instant j'ai la flemme.
//J'ai gardé la classe pour le bout de code, je ne sais pas si tu en as besoin, si non, tu peux supprimer la classe et le package.

public class Donor {
    private Association a;
    public Donor(Association assos){
        a=assos;
        //enregistrer sur la bdd doneur
    }
    public void donner(String name,int don){
        a.newrecette(don);
       /* String fileName = name+"_donation.txt";
        String encoding = "UTF-8";
        try{
            PrintWriter writer = new PrintWriter(fileName, encoding);
            writer.println("Faites des dons");
            writer.println("RAPPORT D'ACTIVITE DE L'ENTREPRISE\nrecttes : "+a.getRecette()+
                    "depenses : "+a.getDepense()+"\n solde actuel : "+a.getBudget());
            writer.println("jsp comment on peut faire pour un rapport de l'année précédente");
            writer.close();
        }
        catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        //il va plutot falloir faire ca avec un mail :(
        //Ou en affichage console*/
    }
}
