package com.example.eduard.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class AddItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        EditText editName = (EditText) findViewById(R.id.add_name_edit);
        EditText editDesc = (EditText) findViewById(R.id.add_description_edit);
        EditText editDate = (EditText) findViewById(R.id.add_expiredate_edit);
        EditText editTime = (EditText) findViewById(R.id.add_expiretime_edit);
        Switch editFavo = (Switch) findViewById(R.id.add_favourite_edit);

        editName.setText("");
        editDesc.setText("");
        editDate.setText("");
        editTime.setText("");
        editFavo.setChecked(false);

        SetDate setDate = new SetDate(editDate, this);
        SetTime setTime = new SetTime(editTime, this);

        final Intent intentToOverview = new Intent(this, Overview.class);
        final Button addButton = (Button) findViewById(R.id.add_adding);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Save
                startActivity(intentToOverview);
            }
        });
    }

}
