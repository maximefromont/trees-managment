package com.activity;

public class Activity {

    private int id;
    private int id_member;
    private String name;
    private String date;

    public Activity(int id, int id_member, String name, String date) {
        this.id = id;
        this.id_member = id_member;
        this.name = name;
        this.date = date;
    }

    public int getId() {return id;}

    public int getId_member() {return id_member;}

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
}
