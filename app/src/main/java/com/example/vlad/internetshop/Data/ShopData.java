package com.example.vlad.internetshop.Data;

import android.util.Log;

import com.example.vlad.internetshop.Enteties.DeviceCard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShopData implements IShopData, Serializable{

    private final String BASE_URL = "http://multiflexersshop.azurewebsites.net";
    private Gson gson = new GsonBuilder().create();
    private Retrofit retrofit = new Retrofit.Builder().
            addConverterFactory(GsonConverterFactory.create(gson)).
            baseUrl(BASE_URL).
            build();

    private ShopAPI shopAPI = retrofit.create(ShopAPI.class);

    public static List<DeviceCard> basketDeviceList = new ArrayList<>();

    /**
     * Get all devicesCards
     * @return List of the devicesCards from the server
     */
    @Override
    public List<DeviceCard> getAllDevices(){
        List<DeviceCard> list = null;

        Call<List<DeviceCard>> call = shopAPI.getAllDevices();
        try{
            Response<List<DeviceCard>> response = call.execute();
            list = response.body();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Get information for concrete device with current id
     * @param id - id of the device
     * @return deviceCard - info about device.
     */
    @Override
    public DeviceCard getDevice(long id) {
        DeviceCard deviceCard = null;

        Call<DeviceCard> call = shopAPI.gttDevice(id);
        try {
            Response<DeviceCard> response = call.execute();
            deviceCard = response.body();
        }catch (IOException e){
            e.printStackTrace();
        }

        return deviceCard;
    }

    /**
     * Get all devicesCards
     * @return List of the promotional devices cards from the server
     */
    @Override
    public List<DeviceCard> getAllPromotionalDevices(){
        List<DeviceCard> list = null;

        Call<List<DeviceCard>> call = shopAPI.getAllPromotionalDevices();
        try{
            list = call.execute().body();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        ArrayList<DeviceCard> justPromotional = new ArrayList<>();

        for(DeviceCard deviceCard : list){
            if(deviceCard.getPromotional()>0){
                justPromotional.add(deviceCard);
            }
        }

        return justPromotional;
    }

}
