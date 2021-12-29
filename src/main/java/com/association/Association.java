package com.association;

import com.member.Member;

public class Association {
    private int budget;

    public Association(){
        budget=0;
    }

    public void defraiement(int cout,Member... a){}
    public void factures(int fac){
        budget-=fac;
        updateAssos();
    }
    //Faire une classe erreur budget <= 0 pour defraiement et factures
    public void updateBudget(int add){
        budget+=add;
        updateAssos();
    }
    public void updateAssos(){
        //update bdd
    }
}
