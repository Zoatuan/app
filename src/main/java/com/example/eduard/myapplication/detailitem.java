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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

public class detailitem extends AppCompatActivity implements OnMapReadyCallback {

    DBDataSource dbDataSource;
    Context context;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    Todo todo;
    EditText editName;
    EditText editDescription;
    EditText editDate;
    EditText editTime;
    Switch editDone;
    List<Contact> displayedContacts;
    private GoogleMap mMap;
    private LatLng location;
    private LatLng oldLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.context = this.getApplicationContext();
        super.onCreate(savedInstanceState);
        dbDataSource = new DBDataSource(this);
        setContentView(R.layout.activity_detailitem);

        final Intent intent = getIntent();

        todo = dbDataSource.getToDoByID(intent.getIntExtra("todo_id", -1));

        editName = (EditText) findViewById(R.id.detail_name_edit);
        editDescription = (EditText) findViewById(R.id.detail_description_edit);
        editDate = (EditText) findViewById(R.id.detail_expiredate_edit);
        editTime = (EditText) findViewById(R.id.detail_expiretime_edit);
        editDone = (Switch) findViewById(R.id.detail_done_edit);
        TextView showFavourite = (TextView) findViewById(R.id.detail_favourite_edit);

        String todoDate = "";
        String todoTime = "";
        SimpleDateFormat expireStringToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat expireDate = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat expireTime = new SimpleDateFormat("HH:mm");

        try {
            Date tempDate = expireStringToDate.parse(todo.getExpire());
            todoDate = expireDate.format(tempDate);
            todoTime = expireTime.format(tempDate);
        } catch (ParseException e) { }

        editName.setText(todo.getName());
        editDescription.setText(todo.getDescription());
        editDate.setText(todoDate.toString());
        editTime.setText(todoTime.toString());
        editDone.setChecked(todo.isDone());
        if(todo.isFavourite()) {
            showFavourite.setText("Favourite");
        } else {
            showFavourite.setText("No");
        }

        SetDate fromDate = new SetDate(editDate, this);
        SetTime fromTime = new SetTime(editTime, this);

        Button delete = (Button) findViewById(R.id.detail_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(detailitem.this).setTitle("Delete action!")
                        .setMessage("Do you really wanna delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO delete
                                dbDataSource.deleteToDoByID(todo.get_dbID());
                                startActivity(new Intent(context, Overview.class));
                            }
                         })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detail_map);
        mapFragment.getMapAsync(this);
        showContacts();
    }

    private void showContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            List<Contact> todoContacts = new ArrayList<>();
            todoContacts.addAll(todo.getContacts());
            final List<Contact> contactList = getContacts();
            displayedContacts = new ArrayList<>();

            for(Contact contact : todoContacts) {

                boolean isNotInContactList = true;
                for(Contact contact2 : contactList) {
                    if(contact.getName().equals(contact2.getName()) && contact.getVorname().equals(contact2.getVorname())) {
                        isNotInContactList = false;
                        displayedContacts.add(contact2);
                    }
                }

                if(isNotInContactList) {
                    contactList.add(contact);
                    displayedContacts.add(contact);
                }
            }

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


            final TextView contactText = (TextView) findViewById(R.id.detail_contacts_edit);
            TextView contactName = (TextView) findViewById(R.id.detail_contacts);
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
                    new AlertDialog.Builder(detailitem.this)
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

            contactText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] items = new String[displayedContacts.size()];
                    for (Contact contact : displayedContacts) {
                        items[displayedContacts.indexOf(contact)] = contact.getVorname() + " " + contact.getName();
                    }
                    new AlertDialog.Builder(detailitem.this)
                            .setTitle("Take contact")
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Contact checkContact = displayedContacts.get(which);
                                    final int adressContact = which;
                                    boolean hasTel = false;
                                    boolean hasEmail = false;
                                    final String[] array;
                                    if(null != checkContact.getTelenr() && !"".equals(checkContact.getTelenr())) {
                                        hasTel = true;
                                    }
                                    if(null != checkContact.getEmail() && !"".equals(checkContact.getEmail())) {
                                        hasEmail = true;
                                    }
                                    if(hasEmail && hasTel) {
                                        array = new String[2];
                                        array[0] = "Email";
                                        array[1] = "SMS";
                                    } else if(!hasEmail && hasTel) {
                                        array = new String[1];
                                        array[0] = "SMS";
                                    } else if(hasEmail && !hasTel) {
                                        array = new String[1];
                                        array[0] = "Email";
                                    } else {
                                        array = new String[0];
                                    }


                                    new AlertDialog.Builder(detailitem.this)
                                            .setTitle("Choose action")
                                            .setItems(array, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Contact finalContact = displayedContacts.get(adressContact);
                                                    if(array[which] == "Email") {
                                                        //EMAIL
                                                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                                        sendIntent.setData(Uri.parse("sms:"));
                                                        sendIntent.putExtra("address", finalContact.getEmail());
                                                        sendIntent.putExtra("sms_body", "Email: " + finalContact.getEmail() + "\n" +
                                                                "Title: " + todo.getName() + "\n" +
                                                                "Description: " + todo.getDescription());
                                                        startActivity(sendIntent);
                                                    } else {
                                                        //SMS

                                                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                                        sendIntent.setData(Uri.parse("sms:"));
                                                        String phoneNumber = finalContact.getTelenr();
                                                        if(!phoneNumber.startsWith("+")) {
                                                            phoneNumber = "+49 "+phoneNumber;
                                                        }
                                                        sendIntent.putExtra("address", finalContact.getTelenr());
                                                        sendIntent.putExtra("sms_body", "Tel-Nr: " + finalContact.getTelenr() + "\n" +
                                                                                        "Title: " + todo.getName() + "\n" +
                                                                                        "Description: " + todo.getDescription());
                                                        startActivity(sendIntent);
                                                    }
                                                }
                                            })
                                            .show();
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
                try{
                    contactList.add(new Contact(name.split(" ")[1], name.split(" ")[0], email, phone));
                }catch(Exception e){
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }

    @Override
    public void onBackPressed() {
        todo.setName(editName.getText().toString());
        todo.setDescription(editDescription.getText().toString());
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
        todo.setExpire(expireString);
        todo.setDone(editDone.isChecked());
        todo.removeContacts();
        for (Contact contact : displayedContacts) {
            todo.addContacts(contact);
        }
        EditText locationName = (EditText) findViewById(R.id.detail_locationName_edit);
        if(locationName != null && locationName.getText() != null && !locationName.getText().toString().equals("") && location != null)
        todo.setLocation(new Todo.Location(locationName.getText().toString(),new Todo.LatLng(location)));
        dbDataSource.editTodo(todo);
        Intent intent = new Intent(this, Overview.class);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng braunschweig = new LatLng(52.26594, 10.52673);
        if(todo.getLocation()!= null) {
            LatLng current = new LatLng(todo.getLocation().getLatlng().getLat(), todo.getLocation().getLatlng().getLng());
            mMap.addMarker(new MarkerOptions().position(current).title(todo.getName()));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(current)
                    .zoom(15)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            EditText locName = (EditText) findViewById(R.id.detail_locationName_edit);
            locName.setText(todo.getLocation().getName());
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                location = latLng;
                new AlertDialog.Builder(detailitem.this)
                        .setTitle("Sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mMap.clear();
                                oldLocation = location;
                                mMap.addMarker(new MarkerOptions().position(location));

                                EditText locationName = (EditText) findViewById(R.id.detail_locationName_edit);
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
