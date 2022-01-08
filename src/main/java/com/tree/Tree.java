package com.tree;

import com.opencsv.bean.CsvBindByPosition;

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
        return "n°" + id + " - " + name + "/genre : " + type +
                "/espece : " + speicies + " - " +
                "circonférence (en cm) : " + cir +

                " - hauteur (en m) : " + height +
                " - Quartier : " + location + " | "
                ;
    }

    @Override
    public String toString() {
        return "Tree{" +
                "id='" + id + '\'' +
                ", cir='" + cir + '\'' +
                ", height='" + height + '\'' +
                ", location='" + location + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", speicies='" + speicies + '\'' +
                ", remarquable='" + remarquable + '\'' +
                '}';
    }

    public String getLocation() {
        return location;
    }
}
