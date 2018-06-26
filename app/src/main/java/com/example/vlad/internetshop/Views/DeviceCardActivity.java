package com.example.vlad.internetshop.Views;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.vlad.internetshop.Enteties.DeviceCard;
import com.squareup.picasso.Picasso;
import com.example.vlad.internetshop.R;

public class DeviceCardActivity extends AppCompatActivity{

    public static String DEVICE_KEY = "DEVICE_KEY";

    TextView tvDeviceCardName, tvDeviceCardNumberOfPurchases,
            tvDeviceCardDescription, tvDeviceCardPrice;

    Button btnDeviceCardBuy;
    ImageView imgDeviceCardInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_card);

        //Add back arrow into the toolbar
        Toolbar toolbar = findViewById(R.id.navAction);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        //find views

        tvDeviceCardName = (TextView)findViewById(R.id.tvDeviceCardName);
        tvDeviceCardNumberOfPurchases = (TextView)findViewById(R.id.tvDeviceCardNumberOfPurchases);
        tvDeviceCardDescription = (TextView)findViewById(R.id.tvDeviceCardDescription);
        tvDeviceCardPrice = (TextView)findViewById(R.id.tvDeviceCardPrice);

        btnDeviceCardBuy = (Button)findViewById(R.id.btnDeviceCardBuy);
        imgDeviceCardInfo = (ImageView)findViewById(R.id.imgDeviceCardInfo);

        //fill viw with the device data
        initDeviceInfo();

        //button buy click
        btnDeviceCardBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDeviceCardBuy.setText(getResources().getString(R.string.openBasket));
                //TODO: add to the basket
            }
        });


    }

    /**
     * get extras (DeviceCard) and show info about device
     */
    private void initDeviceInfo() {
        Bundle args = getIntent().getExtras();
        DeviceCard device = (DeviceCard)args.getSerializable(DEVICE_KEY);

        Picasso.get().
                load(device.getImageUrl()).
                into(imgDeviceCardInfo);

        tvDeviceCardName.setText(device.getName());
        tvDeviceCardNumberOfPurchases.setText(device.getBought().toString());
        tvDeviceCardDescription.setText(device.getLongDescription());
        tvDeviceCardPrice.setText(String.format("%.2f",  device.getPrice()) + " $");

    }
}
