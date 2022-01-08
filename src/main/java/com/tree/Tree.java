package com.tree;

import java.io.*;
import java.util.Scanner;

public class Tree {
    int id, cir, height;
    String location,name,type, speicies;
    boolean remarquable;
    public Tree(int idcsv,int circsv,int heightcsv,String loccsv,String nom, String ty, String spe, boolean remcsv){
        id=idcsv;
        cir=circsv;
        height=heightcsv;
        location=loccsv;
        name=nom;
        type=ty;
        speicies=spe;
        remarquable=remcsv;
    }

    public static void treesCSV() throws FileNotFoundException {
        File getCSVFile = new File("java_project/src/main/ressources/les-arbres.csv");
        Scanner sc = new Scanner(getCSVFile);
        sc.useDelimiter(";");
        while (sc.hasNext()){
            int id= Integer.parseInt(sc.next());
            sc.next();
            sc.next();
            String loc=sc.next();
            sc.next();
            sc.next();
            sc.next();
            sc.next();
            String name = sc.next();
            String type = sc.next();
            String spe = sc.next();
            sc.next();
            int cir = Integer.parseInt(sc.next());
            int hei = Integer.parseInt(sc.next());
            sc.next();
            String rem = sc.next();
            boolean remar = true;
            if(rem == "NON"){
                remar=false;
            }
            sc.next();
            Tree variable = new Tree(id,cir,hei,loc,name,type,spe,remar);
        }
    }

    public static void printTree(){}
}
