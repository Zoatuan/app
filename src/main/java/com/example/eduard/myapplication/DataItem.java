package com.example.eduard.myapplication;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataItem implements Serializable {

    /**
     *
     */


    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("favourite")
    private boolean favourite;

    @SerializedName("done")
    private boolean done;

    @SerializedName("expiry")
    private String expire;

    @SerializedName("id")
    private int id;

    @SerializedName("contacts")
    private List<String> contacts;

    @SerializedName("location")
    private Todo.Location location;

    public DataItem(Todo todo) {
        this.id = todo.get_dbID();
        this.name = todo.getName();
        this.description = todo.getDescription();

        String string_date = todo.getExpire();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = sdf.parse(string_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milliseconds = d.getTime();
        String longstring = String.valueOf(milliseconds);

        this.expire = longstring;
        this.favourite =todo.isFavourite();
        this.done=todo.isDone();
        this.contacts =todo.getContactsAsStringList();
        this.location = todo.getLocation();
    }

    public DataItem(String testname1, String testdesc, boolean fav, boolean done, String expire, int i, String contacts) {
        this.id = i;
        this.name = testname1;
        this.description = testdesc;
        this.expire = expire;
        this.favourite =fav;
        this.done=done;

    }


    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public List<Contact> getContacsAsContacts() {

        List<Contact> retliste = new ArrayList<Contact>();
        try {
            for (String contactstring : contacts) {

                JSONObject jsnobject = new JSONObject(contactstring);

                JSONArray jsonArray = jsnobject.getJSONArray("kontakte");

                List<Contact> contacts = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject explrObject = jsonArray.getJSONObject(i);
                    Contact contact = new Contact();

                    contact.setName(explrObject.get("name").toString());
                    contact.setVorname(explrObject.get("vorname").toString());
                    contact.setEmail(explrObject.get("email").toString());
                    contact.setTelenr(explrObject.get("telenr").toString());

                    //ObjectMapper m = new ObjectMapper();
                    //Contact myClass = m.readValue(explrObject.toString(), Contact.class);
                    retliste.add(contact);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return retliste;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    public Todo.Location getLocation() {
        return location;
    }

    public void setLocation(Todo.Location location) {
        this.location = location;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getExpire() {

        long val = Long.valueOf(this.expire);
        Date date=new Date(val);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateText = df2.format(date);
        return dateText;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFavourite(){
        return this.favourite;
    }

    public boolean isDone(){
        return this.done;
    }

    public int get_dbID(){
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean equals(Object other) {
        return this.getId() == ((DataItem) other).getId();
    }

    public DataItem updateFrom(DataItem item) {
        this.setDescription(item.getDescription());
        this.setName(item.getName());

        return this;
    }

}
