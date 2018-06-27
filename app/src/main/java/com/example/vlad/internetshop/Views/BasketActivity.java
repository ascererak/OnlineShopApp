package com.example.vlad.internetshop.Views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vlad.internetshop.Data.ShopData;
import com.example.vlad.internetshop.Enteties.DeviceCard;
import com.example.vlad.internetshop.R;

public class BasketActivity extends AppCompatActivity {

    RecyclerView recyclerViewDevicesBasket;
    RecyclerViewBasketAdapter deviceCardAdapter;
    Button btnConfirmPurchases;
    TextView tvBasketPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baskset);

        Toolbar toolbar = findViewById(R.id.basketToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                //TODO: start activity with ordering
            }
        });

        initRecyclerView();
    }

    private double getListSum(){
        double sum = 0;

        for(DeviceCard deviceCard: ShopData.basketDeviceList)
            sum += deviceCard.getPrice();

        return sum;
    }

    private void initRecyclerView() {
        recyclerViewDevicesBasket = (RecyclerView) findViewById(R.id.recyclerViewBasket);
        deviceCardAdapter = new RecyclerViewBasketAdapter(getApplicationContext(), ShopData.basketDeviceList, tvBasketPrice);
        LinearLayoutManager layoutManager = new LinearLayoutManager(BasketActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewDevicesBasket.setLayoutManager(layoutManager);
        recyclerViewDevicesBasket.setAdapter(deviceCardAdapter);
    }
}
