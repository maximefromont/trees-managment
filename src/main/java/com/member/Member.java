package com.member;

public class Member {

    private int id;
    private String name;
    private String birth;
    private String adress;
    private String registrationDate;

    public Member(int id, String name, String birth, String adress, String registrationDate) {
        this.id = id;
        this.name = name;
        this.birth = birth;
        this.adress = adress;
        this.registrationDate = registrationDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBirth() {
        return birth;
    }

    public String getAdress() {
        return adress;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }
}
