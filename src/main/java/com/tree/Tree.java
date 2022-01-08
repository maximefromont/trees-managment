package com.tree;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Tree {
    private String id, cir, height;
    private String location,name,type, speicies;
    boolean remarquable;
    public Tree(String idcsv,String circsv,String heightcsv,String loccsv,String nom, String ty, String spe, boolean remcsv){
        id=idcsv;
        cir=circsv;
        height=heightcsv;
        location=loccsv;
        name=nom;
        type=ty;
        speicies=spe;
        remarquable=remcsv;
    }



    public String showMe() {
        return "n°" + id + " - " + name + "/genre : " + type +
                "/espece : " + speicies + " - " +
                "circonférence (en cm) : " + cir +

                " - hauteur (en m) : " + height +
                " - Quartier : " + location + " | "
                ;
    }

    public String getLocation() {
        return location;
    }
}
