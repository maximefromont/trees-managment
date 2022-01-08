package com.association;

public class Finance {

    private int id;
    private int id_association;
    private int recette;
    private int depense;
    private String date;

    public Finance(int id, int id_association, int recette, int depense, String date) {
        this.id = id;
        this.id_association = id_association;
        this.recette = recette;
        this.depense = depense;
        this.date = date;
    }

    public int getId() {return id;}

    public int getId_association() {return id_association;}

    public int getRecette() {return recette;}

    public int getDepense() {return depense;}

    public int getBudget() {
        return recette-depense;
    }

    public String getDate() {return date;}
}
