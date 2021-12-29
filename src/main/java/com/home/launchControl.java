package com.home;

import com.member.Member;

import java.util.Scanner;

public class launchControl {

    public static void main() {
        Member memberTest = new Member(4,"Jean","Sah","Quelque part","A noel bg");
        System.out.println("Menu\n" +
                "1: Payer sa cotisation\n" +
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
                memberTest.cotisation(c);
                System.out.println(memberTest.getCotisation()+"\n"+ memberTest.getAnneeCotisation());
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
}
