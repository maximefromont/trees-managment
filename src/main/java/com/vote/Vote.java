package com.vote;

public class Vote {

    private int id;
    private int id_member;
    private int id_arbre;

    public Vote(int id, int id_member, int id_arbre) {
        this.id = id;
        this.id_member = id_member;
        this.id_arbre = id_arbre;
    }

    public int getId() {
        return id;
    }

    public int getId_member() {
        return id_member;
    }

    public int getId_arbre() {
        return id_arbre;
    }

    @Override
    public String toString() {
        return "Vote : " + "\n" +
                "id=" + getId() + "\n" +
                "id_member=" + getId_member() + '\n' +
                "id_arbre=" + getId_arbre();
    }
}
