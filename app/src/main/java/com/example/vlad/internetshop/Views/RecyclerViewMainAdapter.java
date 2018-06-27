package com.example.vlad.internetshop.Views;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vlad.internetshop.Data.ShopData;
import com.example.vlad.internetshop.Enteties.DeviceCard;
import com.example.vlad.internetshop.R;
import com.squareup.picasso.*;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<DeviceCard> deviceCardList;
    private List<DeviceCard> devicePromList;
    private RecyclerViewPromAdapter promAdapter;
    private Context context;

    private final int TYPE_PROM_DEVICES = 0;
    private final int TYPE_MAIN_DEVICES = 1;
    private final int TYPE_TITLE = 2;

    RecyclerViewMainAdapter(List<DeviceCard> deviceCardList, List<DeviceCard> devicePromList, Context context){
        this.deviceCardList= deviceCardList;
        this.context = context;
        this.devicePromList = devicePromList;
        promAdapter = new RecyclerViewPromAdapter(devicePromList, context);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 1)
            return TYPE_PROM_DEVICES;
        else if(position == 0 || position == 2)
            return TYPE_TITLE;
        else
            return TYPE_MAIN_DEVICES;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView;
        switch (viewType){
            case TYPE_PROM_DEVICES:
                groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.prom_recyclerview, parent, false);
                return new PromRecyclerViewViewHolder(groceryProductView);
            case TYPE_TITLE:
                 groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.textview_title_recycler, parent, false);
                 return new TitleRecyclerViewHolder(groceryProductView);
            default://TYPE_MAIN_DEVICES
                groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
                return new DeviceCardViewHolder(groceryProductView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)){
            case TYPE_PROM_DEVICES:
                PromRecyclerViewViewHolder promHolder = (PromRecyclerViewViewHolder)holder;
                LinearLayoutManager horizontalManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                promHolder.promRecyclerView.setLayoutManager(horizontalManager);
                promHolder.promRecyclerView.setAdapter(promAdapter);
                break;
            case TYPE_TITLE:
                TitleRecyclerViewHolder titleViewHolder = (TitleRecyclerViewHolder)holder;
                if(position == 0)
                    titleViewHolder.tvTitle.setText(context.getResources().getString(R.string.promotional_products));
                else
                    titleViewHolder.tvTitle.setText(context.getResources().getString(R.string.products_in_stock));
                break;
            default:// TYPE_MAIN_DEVICES
                DeviceCardViewHolder deviceHolder = (DeviceCardViewHolder)holder;
                Picasso.get().
                        load(deviceCardList.get(position).getImageThumbnailUrl()).
                        into(deviceHolder.imageView);

                deviceHolder.tvDeviceCardPrice.setText(deviceCardList.get(position).getPrice().toString());
                deviceHolder.tvDeviceCardName.setText(deviceCardList.get(position).getName());
                deviceHolder.tvDeviceCardDescription.setText(deviceCardList.get(position).getShortDescription());
                deviceHolder.deviceCard = deviceCardList.get(position);
                deviceHolder.btnBuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShopData.basketDeviceList.add(deviceCardList.get(position));
                        //Load basket activity
                        Intent intent = new Intent(context, BasketActivity.class);
                        context.startActivity(intent);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return deviceCardList.size();
    }

    public class DeviceCardViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvDeviceCardDescription;
        TextView tvDeviceCardName;
        TextView tvDeviceCardPrice;
        DeviceCard deviceCard;
        Button btnBuy;

        public DeviceCardViewHolder(View view) {
            super(view);
            imageView = view.findViewById(com.example.vlad.internetshop.R.id.imgRecyclerMain);
            tvDeviceCardDescription = view.findViewById(com.example.vlad.internetshop.R.id.tv_recycMain_ItemDescription);
            tvDeviceCardName = view.findViewById(com.example.vlad.internetshop.R.id.tv_recycMain_ItemName);
            tvDeviceCardPrice = view.findViewById(com.example.vlad.internetshop.R.id.tv_recycMain_ItemPrice);
            btnBuy = view.findViewById(R.id.btn_recycMainBuy);

            //CardViewOnClick -> Show card with current device
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DeviceCardActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable(DeviceCardActivity.DEVICE_KEY, deviceCard);
                    intent.putExtras(args);
                    context.startActivity(intent);

                }
            });
        }
    }

    public class PromRecyclerViewViewHolder extends RecyclerView.ViewHolder {
        RecyclerView promRecyclerView;

        public PromRecyclerViewViewHolder(View view){
            super(view);
            promRecyclerView = view.findViewById(R.id.recyclerViewPromotional);
        }
    }

    public class TitleRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        public TitleRecyclerViewHolder(View view){
            super(view);
            tvTitle = view.findViewById(R.id.tvTitleRecyclerView);
        }
    }

    public RecyclerViewPromAdapter getPromAdapter() {
        return promAdapter;
    }

    public void setFilter(ArrayList<DeviceCard> newList) {
        deviceCardList = new ArrayList<>();
        deviceCardList.addAll(newList);
        notifyDataSetChanged();
    }

    public void updateMainList(List<DeviceCard> devices){

        //because we have complex recycler, where first 3 elements aren't the device cards,
        //we need to fill this space in the list with empty devices cards
        List<DeviceCard> emptyDevices = new ArrayList<>();
        emptyDevices.add(new DeviceCard());
        emptyDevices.add(new DeviceCard());
        emptyDevices.add(new DeviceCard());

        deviceCardList.clear();
        deviceCardList.addAll(emptyDevices);
        deviceCardList.addAll(devices);

        //update data to the list
        notifyDataSetChanged();
    }

    public void updatePromList(List<DeviceCard> promDevies){
        this.devicePromList.clear();
        this.devicePromList.addAll(promDevies);
        //Update data to the list
        promAdapter.notifyDataSetChanged();
    }
}
