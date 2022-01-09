package com.cotisation;

public class Cotisation {

    private int id;
    private int id_member;
    private int prix;
    private String date;

    public Cotisation(int id, int id_member, int prix, String date) {
        this.id = id;
        this.id_member = id_member;
        this.prix = prix;
        this.date = date;
    }

    public int getId() {return id;}

    public int getId_member() {return id_member;}

    public int getPrix() {return prix;}

    public String getDate() {return date;}

    @Override
    public String toString() {
        return "Cotisation : " + "\n" +
                "id=" + getId() + "\n" +
                "id_member=" + getId_member() + '\n' +
                "prix=" + getPrix() + '\n' +
                "date=" + getDate();
    }
}
