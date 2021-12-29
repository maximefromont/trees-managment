package com.donor;

import com.association.Association;

public class Donor {
    private Association a;
    public Donor(Association assos){
        a=assos;
    }
    public void donner(int don){
        a.updateBudget(don);
    }
}
