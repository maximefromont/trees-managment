package com.association;

import com.err.BalanceException;
import com.member.Member;

public class Association {
    private int budget,recette,depense;

    public int getBudget() {
        return budget;
    }

    public int getDepense() {
        return depense;
    }

    public int getRecette() {
        return recette;
    }

    public Association(){
        recette=0;
        depense=0;
        budget=recette-depense;
    }

    //Faire une classe erreur budget < 0 pour defraiement
    public void defraiement(int cout,Member... a){}
    public void factures(int fac) throws BalanceException {
        if(budget-(depense+fac)<0){
            throw new BalanceException("Solde insufisant");
        }
        else {
            depense += fac;
            updateBudget();
            updateAssos();
        }
    }


    public void newrecette(int add){
        recette+=add;
        updateBudget();
    }
    public void updateBudget(){
        budget=recette-depense;
        updateAssos();
    }
    public void updateAssos(){
        //update bdd
    }
}
