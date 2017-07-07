package com.example.eduard.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class Mapview extends AppCompatActivity implements OnMapReadyCallback {

    Context context;
    private GoogleMap mMap;
    private LatLng location;
    private LatLng oldLocation;
    DBDataSource dbDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dbDataSource = new DBDataSource(this);
        this.context = this.getApplicationContext();
        setContentView(R.layout.activity_mapview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button overview = (Button) findViewById(R.id.overview_button_map);
        final Intent toOverview = new Intent(this, Overview.class);
        overview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(toOverview);
            }
        });

        Button mapview = (Button) findViewById(R.id.mapview_button_map);
        final Intent intentMapview = new Intent(this, Mapview.class);
        mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentMapview);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final List<Todo> todoList = dbDataSource.getAllTodos();
        boolean camBool = true;
        for (Todo todo : todoList) {
            LatLng latlng = new LatLng(todo.getLocation().getLatlng().getLat(), todo.getLocation().getLatlng().getLng());
            mMap.addMarker(new MarkerOptions().position(latlng).title(""+todo.get_dbID()));
            if(camBool) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latlng)
                        .zoom(13)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                camBool = false;
            }
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intentToDetail = new Intent(context, detailitem.class);
                intentToDetail.putExtra("todo_id", Integer.valueOf(marker.getTitle()));
                startActivity(intentToDetail);
                return false;
            }
        });

    }
}
