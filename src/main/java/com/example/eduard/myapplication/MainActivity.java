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
}

