package com.example.eduard.myapplication;

import java.util.List;

/**
 * Created by Eduard on 05.07.2017.
 */

public class Contact {
    private String name;
    private String vorname;
    private String email;
    private String telenr;
    private int id;

    public Contact(String name, String vorname, String email, String telenr){
        this.name = name;
        this.vorname = vorname;
        this.email= email;
        this.telenr=telenr;

    }

    public Contact(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelenr() {
        return telenr;
    }

    public void setTelenr(String telenr) {
        this.telenr = telenr;
    }


}
