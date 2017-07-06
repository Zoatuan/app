package com.example.eduard.myapplication;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private DBDataSource dbDataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Todo todo = new Todo("Testname1","Testdesc",false,new Date(System.currentTimeMillis()));

        dbDataSource = new DBDataSource(this);
        //dbDataSource.deleteToDoByID(1);
        //dbDataSource.deleteAllToDos();

        Todo newToDo = new Todo("NeuesTodo","NeueDesc3",false,false,"2017-06-17 20:40:40");

        Contact contact1 = new Contact("Testnachname","Testvorname","bla@test.de","12345678");
        Contact contact2 = new Contact("Testnachname2","Testvorname2","bla2@test.de","123456789");
        newToDo.addContacts(contact1);
        newToDo.addContacts(contact2);

        dbDataSource.newTodo(newToDo);

        System.out.println(newToDo.getContactsAsJSONString());
/*
        TextView textView = (TextView) findViewById(R.id.FirstTextField);
       String sammlung = "";
        List<Todo> resultList = new ArrayList<>();
        resultList = dbDataSource.getAllTodos();

        for (Todo todo:resultList){
            sammlung+=todo.getName()+ " " +todo.getDescription()+ " " + " " +todo.isDone()+ " "+ todo.get_dbID()+"\n";
        }
        textView.setText(sammlung);
        // Todo todo = dbDataSource.getToDoByID(1);
        // sammlung += "per ID: "+todo.getName()+ " " +todo.getDescription()+ " " + todo.get_dbID()+"\n";

        //int size = dbDataSource.getAllTodos().size();
/*
        IDataItemCRUDOperations webtest;

        webtest = new RemoteDataItemCRUDOperationsImpl();

        String string_date = "12-December-2012";

        SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
        long milliseconds = 0;
        try {
            Date d = f.parse(string_date);
            milliseconds = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //DataItem test = new DataItem("Testname1","Testdesc",false,false,Long.toString(milliseconds),0);

        List<DataItem> resultList = new ArrayList<>();
        resultList = webtest.readAllDataItems();
        String sammlung = "";
        for (DataItem item:resultList){
            sammlung+=item.getName()+ " " +item.getDescription()+ " " + " " +item.isDone()+ " "+ item.get_dbID()+"\n";
        }
        textView.setText(sammlung);

        //webtest.createDataItem(tes t);

        //webtest.deleteDataItem(2);

        //DataItem itest = webtest.readDataItem(1);

         // System.out.println("lolololololol:" + itest.getName());


        //textView.setText(sammlung);

        //int size2 = dbDataSource.getAllTodos().size();
        //textView.setText("size1: "+size+", size2: "+size2+", CurrentTime: "+newToDo.getExpire());


/*         dbDataSource.getAllTodos();
         textView.setText(dbDataSource.getAllTodos().get(0).toString());
         Intent intentToLogIn = new Intent(this, LoginScreen.class);
         startActivity(intentToLogIn);*/


    }

}
