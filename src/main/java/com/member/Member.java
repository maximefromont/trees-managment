package com.member;
import com.association.Association;

import java.util.Date;
import java.io.*;
public class Member {

    private int id;
    private String name;
    private String birth;
    private String adress;
    private String registrationDate;
    private int cotisation;
    private int anneeCotisation;
    private int resteVisite;

    public Member(int id, String name, String birth, String adress, String registrationDate) {
        this.id = id;
        this.name = name;
        this.birth = birth;
        this.adress = adress;
        this.registrationDate = registrationDate;
        cotisation = 0;
        resteVisite = 5;
        //il est possible de l'enregistrer dans la BDD ici ?
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBirth() {
        return birth;
    }

    public String getAdress() {
        return adress;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public int getCotisation() {
        return cotisation;
    }

    public int getAnneeCotisation() {
        return anneeCotisation;
    }

    public void cotisation (int c, Association a){
        cotisation+=c;
        Date dt=new Date();
        int year=dt.getYear();
        anneeCotisation=year+1900;
        updateMember();
        a.newrecette(c);
    }

    public void vote (/* les arbres de son choix */){}
    public void visite (){
        resteVisite-=1;
    } // /\nbr de visite limité/\

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\n' +
                ", birth='" + birth + '\n' +
                ", adress='" + adress + '\n' +
                ", registrationDate='" + registrationDate + '\n' +
                ", cotisation=" + cotisation +
                '}';
    }

    public void RGPD (){ //Pour le moment le fichier est dans le projet, trouver le moyen de faire une adresse générique ou un mail
        String fileName = name+"_"+id+"_data.txt";
        String encoding = "UTF-8";
        try{
            PrintWriter writer = new PrintWriter(fileName, encoding);
            writer.println(this.toString());
            writer.close();
        }
        catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void  auRevoir(){
        Date dt=new Date();
        int year=dt.getYear();
        int current_year=year+1900;
        /*
        for(Touts les adhérents){
            if(member.anneeCotisation<current_year){
                //Supression BDD
            }
        } */
    }

    public void updateMember(){
        //update bdd
    }
}
