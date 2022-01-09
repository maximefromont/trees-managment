package com.activity;

import com.member.MemberDAO;

public class Activity {

    private int id;
    private int id_association;
    private String name;
    private String date;

    public Activity(int id, int id_member, String name, String date) {
        this.id = id;
        this.id_association = id_member;
        this.name = name;
        this.date = date;
    }

    public int getId() {return id;}

    public int getId_member() {return id_association;}

    public String getName() {return name;}

    public String getDate() {return date;}

    @Override
    public String toString() {
        return "Activity : " + "\n" +
                "id=" + getId() + "\n" +
                "id_member=" + getId_member() + '\n' +
                "name=" + getName() + '\n' +
                "date=" + getDate();
    }

    /**
     * Retourne les informations utiles à un utilisateur concernant une activité
     * @auth Bastien
     * @return String
     */
    public String info() {
        return "A la date : " + getDate() + "\n" +
                "Le membre " + MemberDAO.getMemberById(getId_member()).getName() + "\n" +
                getName() + "\n";
    }
}
