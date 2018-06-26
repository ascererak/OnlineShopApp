package com.example.vlad.internetshop.Data;

import com.example.vlad.internetshop.Enteties.DeviceCard;
import com.example.vlad.internetshop.Enteties.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ShopAPI {

    @GET("/api/DeviceData")
    Call<List<DeviceCard>> getAllDevices();

    @GET("/api/DeviceData/{id}")
    Call<DeviceCard> gttDevice(@Path("id") long id);

    @GET("/api/DeviceData")//TODO: Correct here. New API request
    Call<List<DeviceCard>> getAllPromotionalDevices();


    @POST("api/AccountData/Register")
    Call<Boolean> registerUser(@Body User user);

}
