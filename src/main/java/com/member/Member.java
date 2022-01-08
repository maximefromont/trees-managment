package com.member;
import com.association.Association;

import java.util.Date;
import java.io.*;
public class Member {

    private int id;
    private int id_association;
    private boolean type;
    private String login;
    private String mdp;
    private String name;
    private String birth;
    private String adress;
    private String registrationDate;
    private int resteVisite;

    public Member(int id, int id_association, boolean type, String login, String mdp, String name, String birth, String adress, String registrationDate) {
        this.id = id;
        this.id_association = id_association;
        this.type = type;
        this.login = login;
        this.mdp = mdp;
        this.name = name;
        this.birth = birth;
        this.adress = adress;
        this.registrationDate = registrationDate;
        resteVisite = 5;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {return login;}

    public String getMdp() {return mdp;}

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

    public int getId_association() {return id_association;}

    public boolean isMember() {return type;}
    public boolean isDonor() {return !type;}

    //A reecrire en dehors de membre
    public void visite (){
        resteVisite-=1;
    } // /\nbr de visite limité/\

    @Override
    public String toString() {
        return "Member : " + "\n" +
                "id=" + getId() + "\n" +
                "id_association=" + getId_association() + '\n' +
                "type=" + isMember() + '\n' +
                "login=" + getLogin() + "\n" +
                "mdp=" + getMdp() + "\n" +
                "name=" + getName() + '\n' +
                "birth=" + getBirth() + '\n' +
                "adress=" + getAdress() + '\n' +
                "registrationDate=" + getRegistrationDate();
    }

    //Ca sert à quoi ?
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
}
