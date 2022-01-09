package com.visite;

import com.member.MemberDAO;

public class Visite {

    private int id;
    private int id_member;
    private int id_arbre;
    private int prix;
    private String compte_rendu;

    public Visite(int id, int id_member, int id_arbre, int prix, String compte_rendu) {
        this.id = id;
        this.id_member = id_member;
        this.id_arbre = id_arbre;
        this.prix = prix;
        this.compte_rendu = compte_rendu;
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

    public int getPrix() {
        return prix;
    }

    public String getCompte_rendu() {
        return compte_rendu;
    }

    @Override
    public String toString() {
        return "Visite : " +
                "id=" + getId() + "\n" +
                "id_member=" + getId_member() + '\n' +
                "id_arbre=" + getId_arbre() + '\n' +
                "prix=" + getPrix() + "\n" +
                "compte_rendu=" + getCompte_rendu();
    }

    public String getInfo() {
        return "Visite n°"+getId()+" : " + "\n" +
                "Nom : " + MemberDAO.getMemberById(getId_member()).getName() + '\n' +
                "Numéro d'arbre : " + getId_arbre() + '\n' +
                "Defraiement remboursé : " + getPrix() + "euros" + "\n" +
                "Compte-rendu : " + getCompte_rendu();
    }
}
