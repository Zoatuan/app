package com.example.eduard.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddItem extends AppCompatActivity implements OnMapReadyCallback {

    Context context;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    Todo todo;
    List<Contact> displayedContacts;
    DBDataSource dbDataSource;
    private GoogleMap mMap;
    private LatLng location;
    private LatLng oldLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.context = this.getApplicationContext();
        super.onCreate(savedInstanceState);
        dbDataSource = new DBDataSource(this);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final EditText editName = (EditText) findViewById(R.id.add_name_edit);
        final EditText editDesc = (EditText) findViewById(R.id.add_description_edit);
        final EditText editDate = (EditText) findViewById(R.id.add_expiredate_edit);
        final EditText editTime = (EditText) findViewById(R.id.add_expiretime_edit);
        final Switch editFavo = (Switch) findViewById(R.id.add_favourite_edit);

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
                Todo newTodo = new Todo();
                newTodo.setName(editName.getText().toString());
                newTodo.setDescription(editDesc.getText().toString());
                SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
                SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
                SimpleDateFormat sdfExpire = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                Date time = new Date();
                Date expire = new Date();
                String expireString = "";
                try {
                    date = sdfDate.parse(editDate.getText().toString());
                    time = sdfTime.parse(editTime.getText().toString());
                    expire.setTime(date.getTime() + time.getTime());
                    expireString = sdfExpire.format(expire);
                } catch (ParseException e) {
                    expireString = "";
                }
                newTodo.setExpire(expireString);
                newTodo.setDone(false);
                newTodo.setFavourite(editFavo.isChecked());
                if(displayedContacts.size()>0) {
                    for(Contact contact : displayedContacts) {
                        newTodo.addContacts(contact);
                    }
                }
                EditText locationName = (EditText) findViewById(R.id.add_locationName_edit);
                if(locationName != null && locationName.getText() != null && !locationName.getText().toString().equals("") && location != null)
                newTodo.setLocation(new Todo.Location(locationName.getText().toString(),new Todo.LatLng(location)));
                dbDataSource.newTodo(newTodo);
                startActivity(intentToOverview);
            }
        });
        showContacts();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }



    private void showContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            final List<Contact> contactList = getContacts();
            displayedContacts = new ArrayList<>();

            final boolean[] isSelectedArray = new boolean[contactList.size()];
            final String[] contactArray = new String[contactList.size()];

            String textViewString = "";
            for (Contact contact : displayedContacts) {
                textViewString += contact.getVorname() + " " + contact.getName() + "\n";
                int index = contactList.indexOf(contact);
                isSelectedArray[contactList.indexOf(contact)] = true;
            }

            int counter = 0;
            for (Contact contact : contactList) {
                contactArray[counter] = contact.getVorname() + " " + contact.getName();
                counter++;
            }


            final TextView contactText = (TextView) findViewById(R.id.add_contacts_edit);
            TextView contactName = (TextView) findViewById(R.id.add_contacts);
            if(textViewString.equals("")) {
                contactText.setText("none");
            } else {
                contactText.setText(textViewString);
            }

            contactName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final boolean[] checkArray = new boolean[isSelectedArray.length];
                    for(int i = 0; i < isSelectedArray.length; i++) {
                        checkArray[i] = isSelectedArray[i];
                    }
                    new AlertDialog.Builder(AddItem.this)
                            .setTitle("All contacts")
                            .setMultiChoiceItems(contactArray, isSelectedArray, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    if(isChecked) {
                                        if(!displayedContacts.contains(contactList.get(which))) {
                                            displayedContacts.add(contactList.get(which));
                                            isSelectedArray[which] = true;
                                        }
                                    } else {
                                        displayedContacts.remove(contactList.get(which));
                                        isSelectedArray[which] = false;
                                    }
                                }
                            })
                            .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String contactTextString = "";
                                    for (Contact contact : displayedContacts) {
                                        contactTextString += contact.getVorname() + " " + contact.getName() + "\n";
                                    }
                                    if(contactTextString.equals("")) {
                                        contactTextString = "none";
                                    }
                                    contactText.setText(contactTextString);
                                }
                            })
                            .setNegativeButton("Abort", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for (int i = 0; i < checkArray.length; i++) {
                                        if(!checkArray[i]) {
                                            displayedContacts.remove(contactList.get(i));
                                            isSelectedArray[i] = false;
                                        }
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showContacts();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private List<Contact> getContacts() {
        List<Contact> contactList = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if(cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String phone = "";
                String email = "";
                if("1".equals(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
                    if(pCur.moveToFirst()) {
                        do {
                            phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            break;
                        } while (pCur.moveToNext());
                        pCur.close();
                    }
                }
                Cursor eCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID +" = ?",new String[]{ id }, null);
                if(eCur.moveToFirst()) {
                    do {
                        email = eCur.getString(eCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                    } while(eCur.moveToNext());
                    eCur.close();
                }
                contactList.add(new Contact(name.split(" ")[1], name.split(" ")[0], phone, email));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng braunschweig = new LatLng(52.26594, 10.52673);
        //mMap.addMarker(new MarkerOptions().position(braunschweig).title("Marker in Sydney"));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(braunschweig)
                .zoom(15)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                location = latLng;
                new AlertDialog.Builder(AddItem.this)
                        .setTitle("Sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mMap.clear();
                                oldLocation = location;
                                mMap.addMarker(new MarkerOptions().position(location));

                                EditText locationName = (EditText) findViewById(R.id.add_locationName_edit);
                                Geocoder geocoder = new Geocoder(context);
                                List<Address> adressList = new ArrayList<Address>();
                                try {
                                    adressList = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                locationName.setText(adressList.get(0).getThoroughfare() + " " + adressList.get(0).getSubThoroughfare());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mMap.clear();
                                if(oldLocation != null) {
                                    mMap.addMarker(new MarkerOptions().position(oldLocation));
                                    location = oldLocation;
                                }
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });
    }
}
