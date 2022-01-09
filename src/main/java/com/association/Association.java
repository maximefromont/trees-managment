package com.association;

import com.exceptions.BalanceException;
import com.member.Member;

public class Association {

    private int id;
    private String name;

    public Association(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {return id;}

    public String getName() {return name;}

    @Override
    public String toString() {
        return "Association : " + "\n" +
                "id=" + getId() + "\n" +
                "name=" + getName();
    }

}
