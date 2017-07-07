package com.example.eduard.myapplication;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.Serializable;
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

        dbDataSource = new DBDataSource(this);

        if(DBDataSource.webIsReachable){
            Intent intentToLogIn = new Intent(this, LoginScreen.class);
            startActivity(intentToLogIn);

        }else{
            Intent overview = new Intent(this, Overview.class);
            startActivity(overview);
    }


    }
/*
        Todo newToDo = new Todo("Caffee","Etwas Chillen",false,true,"1355288400000");
        Contact contact1 = new Contact("Bruhl","Eduard","test@web.web","123456");
        newToDo.addContacts(contact1);
        //dbDataSource.newTodo(newToDo);

        TextView textView = (TextView) findViewById(R.id.FirstTextField);
        String sammlung = "";
        List<Todo> resultList = dbDataSource.getAllTodos();
        for (Todo todo:resultList){
            sammlung+=todo.getName()+ " " +todo.getDescription()+ " " + " " +todo.isDone()+ " "+ todo.get_dbID()+ todo.getContactsAsJSONString() +"\n";
        }
        Todo todo = dbDataSource.getToDoByID(1);
        sammlung+="\n Todo by ID:"+ todo.getName()+ " " +todo.getDescription()+ " " + " " +todo.isDone()+ " "+ todo.get_dbID()+ todo.getContactsAsJSONString() +"\n";
        textView.setText(sammlung);
 *//*
    // Todo todo = new Todo("Testname1","Testdesc",false,new Date(System.currentTimeMillis()));
{"email":"s@bht.de","pwd":"000000"}
    dbDataSource = new DBDataSource(this);
    //dbDataSource.deleteToDoByID(1);
    //dbDataSource.deleteAllToDos();

    //dbDataSource.newTodo(newToDo);
        // Contact contact2 = new Contact("Seelmann","Eduard","test@web.web","123456");


        //Contact contact2 = new Contact("Testnachname2","Testvorname2","bla2@test.de","123456789");
    //System.out.println(newToDo.getContactsAsJSONString());
// {"name":"asdasd","description":"asdasd","contacts":["asdasd","testest"],"location":{"latlng":{"lat":12.0,"lng":12.0},"name":"test"}}


        Todo todo = dbDataSource.getToDoByID(1);
        String sammlung = "per ID: "+todo.getName()+ " " +todo.getDescription()+ " " + todo.get_dbID()+ todo.getContactsAsJSONString() + todo.getLocation() +"\n";
        System.out.println(sammlung);
        //int size = dbDataSource.getAllTodos().size();

/*       // newToDo.addContacts(contact2);

        newToDo.setLocation(new Todo.Location("PartyBus",new Todo.LatLng(12,12)));

        IDataItemCRUDOperations webtest = new RemoteDataItemCRUDOperationsImpl();


       // DataItem test = new DataItem(newToDo);
        // webtest.createDataItem(test);

        DataItem bla = webtest.readDataItem(7);

        System.out.println(bla.getName());

        List<String> testliste = new ArrayList<String>();

        testliste.add("asdasd");
        String bla = new Gson().toJson(testliste);

        System.out.println(bla);

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
         // System.out.println("lolololololol:" + itest.getName()
        //textView.setText(sammlung);
        //int size2 = dbDataSource.getAllTodos().size();
        //textView.setText("size1: "+size+", size2: "+size2+", CurrentTime: "+newToDo.getExpire());
       dbDataSource.getAllTodos();
         textView.setText(dbDataSource.getAllTodos().get(0).toString());
         Intent intentToLogIn = new Intent(this, LoginScreen.class);
         startActivity(intentToLogIn);

        TextView textView = (TextView) findViewById(R.id.FirstTextField);

         dbDataSource = new DBDataSource(this);
        //dbDataSource.deleteAllToDos();
        Todo newtodo = new Todo("Offtodo","testdesc",true,false,"123456789");
        newtodo.addContacts(new Contact("Eduard","Bruhl",null,null));
        newtodo.setLocation(new Todo.Location("Timbktu",new Todo.LatLng(12,12)));

        dbDataSource.newTodo(newtodo);

        String sammlung = "";
        List<Todo> resultList = dbDataSource.getAllTodos();
        for (Todo todo:resultList){
            sammlung+=todo.getName()+ " " +todo.getDescription()+ " " + " " +todo.isDone()+ " "+ todo.get_dbID()+ todo.getContactsAsJSONString() +"\n";
        }

        textView.setText(sammlung);
        User user = new User("s@bht.de","000000");
        System.out.println("user: " + new Gson().toJson(user));

        IUserOperations webtestuser = new RemoteUserCRUDOperations();
        boolean logtrue = webtestuser.checkLogin(user);

        System.out.println("logtrue: " + logtrue);


         */

}

