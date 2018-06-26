package com.example.vlad.internetshop.Views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vlad.internetshop.Enteties.DeviceCard;
import com.example.vlad.internetshop.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewPromAdapter extends RecyclerView.Adapter<RecyclerViewPromAdapter.DeviceCardViewHolder>{
    private List<DeviceCard> deviceCardList;
    private Context context;

    public RecyclerViewPromAdapter(List<DeviceCard> deviceCardList, Context context){
        this.deviceCardList= deviceCardList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewPromAdapter.DeviceCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_prom, parent, false);
        RecyclerViewPromAdapter.DeviceCardViewHolder viewHolder = new RecyclerViewPromAdapter.DeviceCardViewHolder(groceryProductView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewPromAdapter.DeviceCardViewHolder holder, final int position) {
        Picasso.get().
                load(deviceCardList.get(position).getImageThumbnailUrl()).
                into(holder.imageView);

        holder.tvDeviceCardPrice.setText(deviceCardList.get(position).getPrice().toString());
        holder.tvDeviceCardName.setText(deviceCardList.get(position).getName());
        holder.tvDeviceCardDescription.setText(deviceCardList.get(position).getShortDescription());


        holder.tvSale.setText("-25%");//TODO: Correct here with te actual params
        holder.deviceCard = deviceCardList.get(position);
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
        TextView tvSale;
        DeviceCard deviceCard;

        public DeviceCardViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imgRecycleProm);
            tvDeviceCardDescription = view.findViewById(com.example.vlad.internetshop.R.id.tv_recycProm_ItemDescription);
            tvDeviceCardName = view.findViewById(com.example.vlad.internetshop.R.id.tv_recycProm_ItemName);
            tvDeviceCardPrice = view.findViewById(com.example.vlad.internetshop.R.id.tv_recycProm_ItemPrice);
            tvSale = view.findViewById(R.id.tv_sale);

            //CardViewOnClick -> Show card with current device
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "device id: "+ deviceCard.getDeviceId(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context, DeviceCardActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable(DeviceCardActivity.DEVICE_KEY, deviceCard);
                    intent.putExtras(args);
                    context.startActivity(intent);
                }
            });
        }
    }
}
