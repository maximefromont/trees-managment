package com.home;

import com.association.Association;
import com.member.Member;

import java.util.Scanner;

public class launchControl {

    public static void main() {
        Member memberTest = new Member(4,"Jean","Sah","Quelque part","A noel bg");
        Association assosTest = new Association();
        System.out.println("Menu\n" +
                "1: Payer sa cotisation\n" +
                "2: RGPD\n" +
                "Quelle fonction voulez-vous effectuer ? : ");
        int n;
        Scanner S = new Scanner(System.in);
        n=S.nextInt();
        switch (n){
            case 1:
                int c=0;
                while(c<=0) {
                    System.out.println(memberTest.getCotisation() +
                            "\nCombien voulez-vous payer ?");
                    Scanner S1 = new Scanner(System.in);
                    c = S1.nextInt();
                }
                memberTest.cotisation(c,assosTest);
                System.out.println(memberTest.getCotisation()+"\n"+ memberTest.getAnneeCotisation());
                break;
            case 2: memberTest.RGPD();
            System.out.println("Fichier dans java project en format txt");
                break;
            case 3:
                break;
        }
    }
}
