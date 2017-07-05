package com.example.eduard.myapplication;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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

    public DataItem(Todo todo) {
        this.id = todo.get_dbID();
        this.name = todo.getName();
        this.description = todo.getDescription();
        this.expire = todo.getExpire();
        this.favourite =todo.isFavourite();
        this.done=todo.isDone();
    }

    public DataItem(String testname1, String testdesc, boolean fav, boolean done, String s, int i) {
        this.id = i;
        this.name = testname1;
        this.description = testdesc;
        this.expire = s;
        this.favourite =fav;
        this.done=done;
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
