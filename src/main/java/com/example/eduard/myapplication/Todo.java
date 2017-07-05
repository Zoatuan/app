package com.example.eduard.myapplication;

import java.util.Date;
import java.util.List;

/**
 * Created by Eduard on 12.05.2017.
 */

public class Todo {
    private String name;
    private String description;
    private boolean favourite;
    private boolean done;
    private String expire;
    private int dbID;
    private String location;
    private List<Contact> contacts;
    public Todo() {
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void addContacts(Contact contact) {
        this.contacts.add(contact);
    }

    public void removeContacts() {
        this.contacts.clear();
    }

    public Todo(String name, String description, boolean favourite,boolean done, String expire, int dbID) {
        this.name = name;
        this.description = description;
        this.done = done;
        this.favourite = favourite;
        this.expire = expire;
        this.dbID = dbID;
    }
    public Todo(String name, String description, boolean favourite,boolean done, String expire) {
        this.name = name;
        this.description = description;
        this.done = done;
        this.favourite = favourite;
        this.expire = expire;
        this.dbID = 0;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public void setDone(boolean favourite) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public void set_dbID(int dbID) {
        this.dbID = dbID;
    }
    public int get_dbID() {
        return dbID;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", favourite=" + favourite +
                ", done=" + done +
                ", expire=" + expire +
                ", Contacts=" + contacts.toString() +
                ", Location=" + location+
                '}';
    }

}
