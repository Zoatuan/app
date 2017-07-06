package com.example.eduard.myapplication;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@SuppressWarnings("Since15")
public class Overview extends AppCompatActivity {

    DBDataSource dbDataSource;
    List<Todo> todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbDataSource = new DBDataSource(this);

        setContentView(R.layout.activity_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intent intentToAdd = new Intent(this, AddItem.class);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentToAdd);
            }
        });
        doTheRest(true);
    }

    private List<Todo> sortByFavouriteAndDate(List<Todo> ausgangsListe) {
        ausgangsListe.sort(new Comparator<Todo>() {
            @Override
            public int compare(Todo o1, Todo o2) {
                int result = 0;

                if(o1.isDone() && !o2.isDone()) {
                    result = 1;
                } else if(!o1.isDone() && o2.isDone()) {
                    result = -1;
                } else {
                    if(o1.isFavourite() && !o2.isFavourite()) {
                        result = 1;
                    } else if(!o1.isFavourite() && o2.isFavourite()){
                        result = -1;
                    } else {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date o1Date = new Date();
                        Date o2Date = new Date();
                        try {
                            o1Date = sdf.parse(o1.getExpire());
                            o2Date = sdf.parse(o2.getExpire());
                        } catch (ParseException e) { }
                        if(o1Date.after(o2Date)) {
                            result = 1;
                        } else if(o1Date.before(o2Date)) {
                            result = -1;
                        }
                    }
                }

                return result;
            }
        });
        return ausgangsListe;
    }

    private List<Todo> sortByDateAndFavourite(List<Todo> ausgangsListe) {
        ausgangsListe.sort(new Comparator<Todo>() {
            @Override
            public int compare(Todo o1, Todo o2) {
                int result = 0;

                if(o1.isDone() && !o2.isDone()) {
                    result = 1;
                } else if(!o1.isDone() && o2.isDone()) {
                    result = -1;
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date o1Date = new Date();
                    Date o2Date = new Date();
                    try {
                        o1Date = sdf.parse(o1.getExpire());
                        o2Date = sdf.parse(o2.getExpire());
                    } catch (ParseException e) { }
                    if(o1Date.after(o2Date)) {
                        result = 1;
                    } else if(o1Date.before(o2Date)) {
                        result = -1;
                    } else {
                        if(o1.isFavourite() && !o2.isFavourite()) {
                            result = 1;
                        } else if(!o1.isFavourite() && o2.isFavourite()) {
                            result = -1;
                        }
                    }
                }

                return result;
            }
        });
        return ausgangsListe;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.dateFavSort:
                    doTheRest(false);
                    return true;
                case R.id.FavDateSort:
                    doTheRest(true);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
    }

    private void doTheRest(boolean dateOrFav) {

        todoList = dbDataSource.getAllTodos();
        if (dateOrFav) {
            todoList = sortByFavouriteAndDate(todoList);
        } else {
            todoList = sortByDateAndFavourite(todoList);
        }

        OverviewItemAdapter overviewItemAdapter = new OverviewItemAdapter(this, todoList);

        ListView overviewListview = (ListView) findViewById(R.id.overview_listview);
        overviewListview.setAdapter(overviewItemAdapter);

        final Intent intentToDetail = new Intent(this, detailitem.class);
        overviewListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intentToDetail.putExtra("todo_id", todoList.get(position).get_dbID());
                startActivity(intentToDetail);
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
