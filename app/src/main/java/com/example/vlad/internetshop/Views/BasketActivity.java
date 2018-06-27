package com.example.vlad.internetshop.Views;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vlad.internetshop.Data.ShopData;
import com.example.vlad.internetshop.Enteties.DeviceCard;
import com.example.vlad.internetshop.R;

public class BasketActivity extends AppCompatActivity {

    RecyclerView recyclerViewDevicesBasket;
    RecyclerViewBasketAdapter deviceCardAdapter;
    Button btnConfirmPurchases;
    TextView tvBasketPrice;
    private double amount;

    public static final String KEY_AMOUNT = "KEY_AMOUNT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baskset);

        //Add back arrow into the toolbar
        Toolbar toolbar = findViewById(R.id.simpleTextToolbar);
        TextView tvToolbarText = findViewById(R.id.toolbarText);
        tvToolbarText.setText(getResources().getString(R.string.Basket));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvBasketPrice = findViewById(R.id.tvBasketPrice);
        tvBasketPrice.setText(String.valueOf(getListSum()) + " $");
        btnConfirmPurchases = findViewById(R.id.btnConfirmPurchase);
        btnConfirmPurchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ShopData.basketDeviceList.size() == 0)
                    Toast.makeText(getApplicationContext(), "Basket is empty", Toast.LENGTH_SHORT).show();
                else{
                    Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
                    intent.putExtra(KEY_AMOUNT, amount);
                    startActivity(intent);
                    ShopData.basketDeviceList.clear();
                    finish();
                }
            }
        });

        initRecyclerView();
    }

    private double getListSum(){
        amount = 0;

        for(DeviceCard deviceCard: ShopData.basketDeviceList)
            amount += deviceCard.getPrice();

        return amount;
    }

    private void initRecyclerView() {
        recyclerViewDevicesBasket = (RecyclerView) findViewById(R.id.recyclerViewBasket);
        deviceCardAdapter = new RecyclerViewBasketAdapter(getApplicationContext(), ShopData.basketDeviceList, tvBasketPrice, amount);
        LinearLayoutManager layoutManager = new LinearLayoutManager(BasketActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewDevicesBasket.setLayoutManager(layoutManager);
        recyclerViewDevicesBasket.setAdapter(deviceCardAdapter);
    }
}
