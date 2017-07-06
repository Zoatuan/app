package com.example.eduard.myapplication;

import android.os.StrictMode;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.PUT;

/**
 * Created by Eduard on 06.07.2017.
 */

public class RemoteUserCRUDOperations implements IUserOperations{
    public interface IUserOperations{

        /*
         * the operations
         */
        @PUT("/api/users/auth")
        public Call<Boolean> checkLogin(@Body User user);

    }

    private RemoteUserCRUDOperations.IUserOperations webAPI;
    // 10.0.2.2 wenn nicht genymotion
    public RemoteUserCRUDOperations(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.3.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.webAPI = retrofit.create(RemoteUserCRUDOperations.IUserOperations.class);
    }
    @Override
    public boolean checkLogin(User user) {
        try {
            return this.webAPI.checkLogin(user).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
