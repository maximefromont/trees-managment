package com.tree;

import com.opencsv.bean.CsvBindByPosition;

/**
 * @author Martin
 */
public class Tree {

    @CsvBindByPosition(position = 0)
    private String id;

    @CsvBindByPosition(position = 12)
    private String cir;

    @CsvBindByPosition(position = 13)
    private String height;

    @CsvBindByPosition(position = 3)
    private String location;

    @CsvBindByPosition(position = 8)
    private String name;

    @CsvBindByPosition(position = 9)
    private String type;

    @CsvBindByPosition(position = 10)
    private String speicies;

    @CsvBindByPosition(position = 15)
    private String remarquable;

    public Tree(){}
    public Tree(String idcsv,String circsv,String heightcsv,String loccsv,String nom, String ty, String spe, String remcsv){
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
        return "n°" + id + " - " + name + " / Genre : " + type +
                " / Espece : " + speicies + " - " +
                "Circonférence : " + cir +

                "cm - Hauteur : " + height +
                "m - Quartier : " + location + " | "
                ;
    }

    public String getLocation() {
        return location;
    }
}
