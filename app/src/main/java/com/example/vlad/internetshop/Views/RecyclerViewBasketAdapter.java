package com.example.vlad.internetshop.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vlad.internetshop.Enteties.DeviceCard;
import com.example.vlad.internetshop.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewBasketAdapter extends RecyclerView.Adapter<RecyclerViewBasketAdapter.BasketViewHolder>{

    List<DeviceCard> deviceCardList;
    Context context;
    TextView basketPrice;

    public RecyclerViewBasketAdapter(Context context, List<DeviceCard> deviceCardList, TextView basketPrice){
        this.context = context;
        this.deviceCardList = deviceCardList;
        this.basketPrice = basketPrice;
    }

    @NonNull
    @Override
    public BasketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item, parent, false);
        return new RecyclerViewBasketAdapter.BasketViewHolder(groceryProductView);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketViewHolder holder, final int position) {
        Picasso.get().
                load(deviceCardList.get(position).getImageThumbnailUrl()).
                into(holder.imgDevice);

        holder.tvDevicePriceBasketItem.setText(deviceCardList.get(position).getPrice().toString() + " $");
        holder.tvDeviceNameBasketItem.setText(deviceCardList.get(position).getName());
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double newPrice = Double.parseDouble(basketPrice.getText().toString().substring(0, basketPrice.getText().toString().length()-2))
                        - deviceCardList.get(position).getPrice();
                newPrice = newPrice < 0 ? 0 : newPrice;
                basketPrice.setText(String.valueOf(newPrice));

                deviceCardList.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceCardList.size();
    }

    public class BasketViewHolder extends RecyclerView.ViewHolder{
        ImageView imgDevice;
        TextView tvDeviceNameBasketItem, tvDevicePriceBasketItem;
        Button btnRemove;

        public BasketViewHolder(View itemView) {
            super(itemView);
            imgDevice = itemView.findViewById(R.id.imgDeviceBasket);
            tvDeviceNameBasketItem = itemView.findViewById(R.id.tvDeviceNameBasketItem);
            tvDevicePriceBasketItem = itemView.findViewById(R.id.tvDevicePriceBasketItem);
            btnRemove = itemView.findViewById(R.id.btnRemoveBasketItem);
        }
    }
}
