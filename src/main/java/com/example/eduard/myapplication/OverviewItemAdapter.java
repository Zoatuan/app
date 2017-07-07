package com.example.eduard.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 02.07.2017.
 */

public class OverviewItemAdapter extends ArrayAdapter<Todo> {

    private Context context;
    private DBDataSource dbDataSource;
    Todo todo;
    List<Todo> todoArrayList;

    public OverviewItemAdapter(Context context, List<Todo> todoArrayList) {
        super(context, 0, todoArrayList);
        this.context = context;
        this.todoArrayList = todoArrayList;
        dbDataSource = new DBDataSource(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        todo = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_item, parent, false);
        }

        //Declare textview and switch-element
        TextView textView = (TextView) convertView.findViewById(R.id.item_textview);
        Switch item_switch_done = (Switch) convertView.findViewById(R.id.item_switch_done);
        Switch item_switch_favourite = (Switch) convertView.findViewById(R.id.item_switch_favourite);

        //Filling textview element with name, expire and favourite from todoObject
        String germanDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd.MM.yyyy");

        try {
            Date tempDate = sdf.parse(todo.getExpire());
            germanDate = sdf2.format(tempDate);
            if((new Date(System.currentTimeMillis())).after(sdf.parse(todo.getExpire()))) {
                textView.setTextColor(context.getResources().getColor(R.color.colorAccent));
            }
        } catch (ParseException e) {
            germanDate = todo.getExpire();
        }


        textView.setText(todo.getName() + "\n" + germanDate);

        //Filling switch with done-attribute from todoObject
        item_switch_done.setChecked(todo.isDone());
        item_switch_favourite.setChecked(todo.isFavourite());

        //Adding onClickListener
        final Intent intentToDetail = new Intent(context, detailitem.class);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Todo finalTodo = new Todo();
                for (Todo work : todoArrayList) {
                    if(work.getName().equals(((TextView) v).getText().toString().split("\n")[0])) {
                        finalTodo = work;
                    }
                }
                if(finalTodo != null && !"".equals(finalTodo.getName())) {
                    intentToDetail.putExtra("todo_id", finalTodo.get_dbID());
                }
                context.startActivity(intentToDetail);
            }
        });
        item_switch_done.setCameraDistance(todo.get_dbID());
        item_switch_done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int db = (int)((Switch) buttonView).getCameraDistance();
                    Todo finalTodo = dbDataSource.getToDoByID(db);
                    finalTodo.setDone(isChecked);
                    dbDataSource.editTodo(finalTodo);
            }
        });
        item_switch_done.setCameraDistance(todo.get_dbID());
        item_switch_favourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int db = (int)((Switch) buttonView).getCameraDistance();
                    Todo finalTodo = dbDataSource.getToDoByID(db);
                    finalTodo.setFavourite(isChecked);
                    dbDataSource.editTodo(finalTodo);
            }
        });
        return convertView;
    }
}
