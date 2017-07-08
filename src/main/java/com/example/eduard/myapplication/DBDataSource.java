package com.example.eduard.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;

/**
 * Created by Eduard on 12.05.2017.
 */

public class DBDataSource {
    private SQLiteDatabase database;
    private DBConnection dbHelper;

    public static boolean webIsReachable = true;
    private static boolean checkedOnce = false;

    IDataItemCRUDOperations webapi = new RemoteDataItemCRUDOperationsImpl();

    public DBDataSource(Context context) {
        dbHelper = new DBConnection(context);
        if(!checkedOnce){
            this.isWebIsReachable();
            checkedOnce = true;
        }
    }

    public void isWebIsReachable(){

        try {
            List<DataItem> resultListweb = new ArrayList<>();
            resultListweb = webapi.readAllDataItems();

            List<Todo> resultList= new ArrayList<>();
            resultList = dbHelper.getAllToDos();

            if(resultList.size() <= 0 && resultListweb.size() > 0){
                for (DataItem item:resultListweb){
                    dbHelper.newToDo(new Todo(item));
                }
            }else{
                this.deleteAllToDosWeb();
                for (Todo todo:resultList){
                    webapi.createDataItem(new DataItem (todo));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            webIsReachable = false;
        }
        System.out.println("webIsReachable: " + webIsReachable);
    }

    public List<Todo> getAllTodos(){

        List<Todo> resultList = new ArrayList<>();
        try {
            resultList = dbHelper.getAllToDos();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return resultList;
        }
    }

    public void newTodo(Todo newToDo) {
        dbHelper.newToDo(newToDo);
        if(webIsReachable){
            webapi.createDataItem(new DataItem (newToDo));
        }
    }

    public void deleteAllToDos(){

        dbHelper.deleteAllToDos();
        if(webIsReachable){
            this.deleteAllToDosWeb();
        }
    }

    private void deleteAllToDosWeb(){

        for (int i = 0; i<= 100;i++){
            webapi.deleteDataItem(i);
        }
    }

    public void deleteToDoByID(int toDoId){

        dbHelper.deleteToDoByID(toDoId);
        if(webIsReachable){
            webapi.deleteDataItem(toDoId);
        }
    }

    public Todo getToDoByID(int toDoID) {
        Todo todo = dbHelper.getToDoByID(toDoID);
        return todo;
    }

    public void editTodo(Todo todo) {

        dbHelper.editToDo(todo);
        if(webIsReachable){
            webapi.updateDataItem(todo.get_dbID(),new DataItem(todo));
        }
    }

}
