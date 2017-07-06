package com.example.eduard.myapplication;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
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
    private Location location;
    private List<Contact> contacts = new ArrayList<>();;
    public Todo() {
    }

/*    public Todo(DataItem item) {
        this.name = item.getName();
        this.description = item.getDescription();
        this.favourite = item.isFavourite();
        this.done = item.isDone();
        this.expire = item.getExpire();
        this.dbID = item.get_dbID();
        this.location = item.getLocation();
        this.contacts = item
    }*/

    public List<Contact> getContacts() {
        return contacts;
    }

    public List<String> getContactsAsStringList() {
        List<Contact> resultList = this.getContacts();
        List<String> retliste = new ArrayList<String>();

        for (Contact contact:resultList){
            retliste.add("name: "+ contact.getName() + ",vname: " + contact.getVorname() + ",email:" + contact.getEmail() + ",telenr:" + contact.getTelenr());
        }

        return retliste;
    }


    public String getLocationAsString(){
        System.out.println("location: " + new Gson().toJson(this.getLocation()));
        return new Gson().toJson(this.getLocation());
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
    public Todo(String name, String description, boolean favourite,boolean done, String expire, int dbID, List<Contact> contacts, Location location) {
        this.name = name;
        this.description = description;
        this.done = done;
        this.favourite = favourite;
        this.expire = expire;
        this.dbID = dbID;
        this.contacts = contacts;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public String getLocationAsJSONString(){
        return new Gson().toJson(this.location);
    }

    public void setLocation(Location location) {
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

    public String getContactsAsJSONString(){
        String message;
        JSONObject json = new JSONObject();

        try {

            List<Contact> resultList = this.getContacts();
            JSONArray array = new JSONArray();
            for (Contact contact:resultList){
                JSONObject item = new JSONObject();
                item.put("name",contact.getName());
                item.put("vorname",contact.getVorname());
                item.put("email",contact.getEmail());
                item.put("telenr",contact.getTelenr());
                array.put(item);
            }
            json.put("kontakte", array);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
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

    public static class LatLng implements Serializable {

        private double lat;
        private double lng;

        public LatLng() {

        }

        public LatLng(long lat,long lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

    }

    public static class Location implements Serializable {

        private String name;

        private LatLng latlng;

        public Location() {

        }

        public Location(String name,LatLng latlng) {
            this.name = name;
            this.latlng = latlng;
        }

        public LatLng getLatlng() {
            return latlng;
        }

        public void setLatlng(LatLng latlng) {
            this.latlng = latlng;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


    }
}
