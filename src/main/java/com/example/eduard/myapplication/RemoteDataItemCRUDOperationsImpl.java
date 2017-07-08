package com.example.eduard.myapplication;

import android.os.StrictMode;

import java.io.IOException;
import java.util.List;


import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;


/**
 * Created by Eduard on 05.07.2017.
 */

public class RemoteDataItemCRUDOperationsImpl implements IDataItemCRUDOperations {

    public interface IDataItemCRUDWebAPI{

        /*
         * the operations
         */
        @POST("/api/todos")
        public Call<DataItem> createDataItem(@Body DataItem item);

        @GET("/api/todos")
        public Call<List<DataItem> >readAllDataItems();

        @GET("/api/todos/{id}")
        public Call<DataItem> readDataItem(@Path("id") int dateItemId);

        @PUT("/api/todos/{id}")
        public Call<DataItem> updateDataItem(@Path("id")int dateItemId, @Body DataItem item);

        @DELETE("/api/todos/{id}")
        public Call<Boolean> deleteDataItem (@Path("id")int dataItemId);

    }

    private IDataItemCRUDWebAPI webAPI;
    // 10.0.2.2 wenn nicht genymotion
    public RemoteDataItemCRUDOperationsImpl(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.3.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.webAPI = retrofit.create(IDataItemCRUDWebAPI.class);
    }


    @Override
    public DataItem createDataItem(DataItem item) {
        try {
            return this.webAPI.createDataItem(item).execute().body();

        } catch (Exception e) {
            e.printStackTrace();
        };
        return null;
    }


    @Override
    public List<DataItem> readAllDataItems() throws IOException{
        try {
            return this.webAPI.readAllDataItems().execute().body();
        } catch (IOException e) {
            throw new IOException(e);
        }

    }

    @Override
    public DataItem readDataItem( int dateItemId) {
        try {
            return this.webAPI.readDataItem(dateItemId).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public DataItem updateDataItem(int id, DataItem item) {
        try {
            return this.webAPI.updateDataItem(id,item).execute().body();

        } catch (Exception e) {
            e.printStackTrace();
        };
        return null;
    }

    @Override
    public boolean deleteDataItem(int dataItemId) {
        try {
            return this.webAPI.deleteDataItem(dataItemId).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
