package com.home;

import com.config.*;
import com.member.*;

public class main {
//test
    public static void main(String[] args) {

        //Exemple de récupération d'un lien depuis le fichier de config
        System.out.println(ReadProperties.getConfig("linkBDD"));
        System.out.println(ReadProperties.getConfig("loginBDD"));
        System.out.println(ReadProperties.getConfig("mdpBDD"));

        System.out.println("\n");

        for (Member member : MemberDAO.getAllMember()) {
            System.out.println("id = "+member.getId());
            System.out.println("name = "+member.getName());
            System.out.println("birth = "+member.getBirth());
            System.out.println("adress = "+member.getAdress());
            System.out.println("registrationDate = "+member.getRegistrationDate());
        }

    }

}
