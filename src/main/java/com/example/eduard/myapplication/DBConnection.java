package com.example.eduard.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eduard on 12.05.2017.
 *
 CREATE TABLE todo (
 Name nvarchar(255) NOT NULL,
 Description nvarchar(255),
 Favourite bool NOT NULL ,
 Done bool NOT NULL,
 Expire time NOT NULL,
 Contacts nvarchar(1023),
 Location nvarchar(1023));
 */

public class DBConnection extends SQLiteOpenHelper {
    private static String DB_NAME = "mobappdevfinal.db";
    private static String DB_PATH = "";
    private final Context mContext;
    private SQLiteDatabase mDataBase;

    private static String createDB = "CREATE DATABASE test ( id integer, name nvarchar(255))";

    public DBConnection(Context context) {
        super(context, DB_NAME, null, 1);

        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;

        try {
            this.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.openDataBase();

    }

    public void createDataBase() throws IOException {
        //If the database does not exist, copy it from the assets.

        boolean mDataBaseExist = checkDataBase();
        if (!mDataBaseExist) {
            this.getReadableDatabase();
            this.close();
            try {
                //Copy the database from assests
                copyDataBase();
                //  Log.e(TAG, "createDatabase database created");
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    //Copy the database from assets
    private void copyDataBase() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Check that the database exists here: /data/data/your package/databases/Da Name
    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    //Open the database, so we can query it
    public boolean openDataBase() throws SQLException {
        String mPath = DB_PATH + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    public List<Todo> getAllToDos() throws Exception {

        Cursor cursor = mDataBase.rawQuery("SELECT *,rowid FROM todo",null );
        List<Todo> resultList = new ArrayList<>();
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("Name"));
            String description= cursor.getString(cursor.getColumnIndex("Description"));
            boolean favourite = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("Favourite")));
            boolean done = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("Done")));
            String expire = cursor.getString(cursor.getColumnIndex("Expire"));

            String contactsstring = cursor.getString(cursor.getColumnIndex("Contacts"));

            JSONObject jsnobject = new JSONObject(contactsstring);

            JSONArray jsonArray = jsnobject.getJSONArray("kontakte");

            List<Contact> contacts = new ArrayList<>();;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject explrObject = jsonArray.getJSONObject(i);
                Contact contact = new Contact();

                contact.setName(explrObject.get("name").toString());
                contact.setVorname(explrObject.get("vorname").toString());
                contact.setEmail(explrObject.get("email").toString());
                contact.setTelenr(explrObject.get("telenr").toString());

                //ObjectMapper m = new ObjectMapper();
                //Contact myClass = m.readValue(explrObject.toString(), Contact.class);
                contacts.add(contact);
            }



            int toDoId = cursor.getInt(cursor.getColumnIndex("rowid"));

            String location= cursor.getString(cursor.getColumnIndex("Location"));
            Gson gson = new GsonBuilder().create();

            Todo.Location p = gson.fromJson(location, Todo.Location.class);

            Todo todo = new Todo(name,description,favourite,done,expire,toDoId,contacts,p);
            resultList.add(todo);

        }


        return resultList;
    }

    public boolean newToDo(Todo newToDo) {
        boolean ret = true;
        try {
            mDataBase.execSQL("INSERT INTO todo VALUES ('" + newToDo.getName() + "','" + newToDo.getDescription() + "','" + newToDo.isFavourite() + "','" + newToDo.isDone() + "','" + newToDo.getExpire()+ "','" + newToDo.getContactsAsJSONString()+ "','" + newToDo.getLocationAsJSONString() + "')");
        } catch (SQLException e) {
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }

    public boolean editToDo(Todo editTodo) {
        boolean ret = true;
        try {
            mDataBase.execSQL("DELETE FROM todo WHERE rowid = " + editTodo.get_dbID());
            newToDo(editTodo);
        } catch (SQLException e) {
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }

    public boolean deleteToDoByID(int toDoId) {
        boolean ret = true;
        try {
            mDataBase.execSQL("DELETE FROM todo WHERE rowid = " + toDoId );
        } catch (SQLException e) {
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }

    public boolean deleteToDo(Todo deleteTodo) {
        boolean ret = true;
        try {
            mDataBase.execSQL("DELETE FROM todo WHERE rowid = " + deleteTodo.get_dbID());
        } catch (SQLException e) {
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }

    public Todo getToDoByID(int toDoId) {
        Todo todo;
        todo = null;
        try {
            Cursor cursor = mDataBase.rawQuery("SELECT *,rowid FROM todo WHERE rowid = ? LIMIT 1", new String[] {Integer.toString(toDoId)});
            if ( cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndex("Name"));
                String description= cursor.getString(cursor.getColumnIndex("Description"));
                boolean favourite = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("Favourite")));
                boolean done = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("Done")));
                String expire = cursor.getString(cursor.getColumnIndex("Expire"));

                String contactsstring = cursor.getString(cursor.getColumnIndex("Contacts"));

                JSONObject jsnobject = new JSONObject(contactsstring);

                JSONArray jsonArray = jsnobject.getJSONArray("kontakte");

                List<Contact> contacts = new ArrayList<>();;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject explrObject = jsonArray.getJSONObject(i);
                    Contact contact = new Contact();

                    contact.setName(explrObject.get("name").toString());
                    contact.setVorname(explrObject.get("vorname").toString());
                    contact.setEmail(explrObject.get("email").toString());
                    contact.setTelenr(explrObject.get("telenr").toString());
                    
                    //ObjectMapper m = new ObjectMapper();
                    //Contact myClass = m.readValue(explrObject.toString(), Contact.class);
                    contacts.add(contact);
                }


                String location= cursor.getString(cursor.getColumnIndex("Location"));
                Gson gson = new GsonBuilder().create();
                Todo.Location p = gson.fromJson(location, Todo.Location.class);

                todo = new Todo(name,description,favourite,done,expire,toDoId,contacts,p);
                return todo;
            }else{
                System.out.println("ToDo mit der ID nicht vorhanden");
                todo = new Todo("null","null",false,false,"0000-00-00 00:00:00",0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return todo;
    }

    public boolean deleteAllToDos() {
        boolean ret = true;
        try {
            mDataBase.execSQL("DELETE FROM todo");
        } catch (SQLException e) {
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }

}
