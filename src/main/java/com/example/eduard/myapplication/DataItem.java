package com.example.eduard.myapplication;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
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
    private List<String> contacs;

    @SerializedName("location")
    private Todo.Location location;

    public DataItem(Todo todo) {
        this.id = todo.get_dbID();
        this.name = todo.getName();
        this.description = todo.getDescription();
        this.expire = todo.getExpire();
        this.favourite =todo.isFavourite();
        this.done=todo.isDone();
        this.contacs =todo.getContactsAsStringList();
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

    public List<String> getContacs() {
        return contacs;
    }

    public void setContacs(List<String> contacs) {
        this.contacs = contacs;
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
        return this.expire;
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
