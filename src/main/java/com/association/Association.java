package com.association;

import com.exceptions.BalanceException;
import com.member.Member;

public class Association {

    private int id;
    private String name;
    private int recette;
    private int depense;

    public Association(int id, String name, int recette, int depense){
        this.id = id;
        this.name = name;
        this.recette = recette;
        this.depense = depense;
    }

    public int getId() {return id;}

    public String getName() {return name;}

    public int getDepense() {
        return depense;
    }

    public int getRecette() {
        return recette;
    }

    public int getBudget() {
        return recette-depense;
    }

    @Override
    public String toString() {
        return "Association : " + "\n" +
                "id=" + getId() + "\n" +
                "name=" + getName() + '\n' +
                "recette=" + getRecette() + '\n' +
                "depense=" + getDepense() + '\n' +
                "budget=" + getBudget();
    }

    //Faire une classe erreur budget < 0 pour defraiement
    public void defraiement(int cout,Member... a){}
    public void factures(int fac) throws BalanceException {
        if(getBudget()-(depense+fac)<0){
            throw new BalanceException("Solde insufisant");
        }
        else {
            depense += fac;
            updateAssos();
        }
    }


    public void newrecette(int add){
        recette+=add;
    }

    public void updateAssos(){
        //update bdd
    }
}
