package com.example.vlad.internetshop.Views;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vlad.internetshop.R;

import java.sql.Date;
import java.util.Calendar;

public class OrderActivity extends AppCompatActivity {

    private double amount;

    TextView tvPrice;

    EditText etName, etLastName, etCardNumber,
    etMonth, etYear, etCV;

    Button btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initViews();

        //Init amount
        amount = getIntent().getDoubleExtra(BasketActivity.KEY_AMOUNT, 0);
        tvPrice.setText(String.valueOf(amount)+ " $");
    }

    private void initViews() {
        //Add back arrow into the toolbar
        Toolbar toolbar = findViewById(R.id.simpleTextToolbar);;
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

        //init views
        etName = findViewById(R.id.order_name);
        etLastName = findViewById(R.id.order_lastName);
        etCardNumber = findViewById(R.id.order_cardNumber);
        etMonth = findViewById(R.id.order_cardMonth);
        etYear = findViewById(R.id.order_cardYear);
        etCV = findViewById(R.id.order_cardCV);
        tvPrice = findViewById(R.id.order_TotalPrice);
    }

    public void btnOrderClick(View v){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date());
        int year = calendar.get(Calendar.YEAR);

        if(etName.getText().length() <2){
            Toast.makeText(OrderActivity.this, "Name field is to short", Toast.LENGTH_SHORT).show();
            return;
        }
        if(etLastName.getText().length() <2){
            Toast.makeText(OrderActivity.this, "Last name field is to short", Toast.LENGTH_SHORT).show();
            return;
        }
        if(etCardNumber.getText().length() != 16){
            Toast.makeText(OrderActivity.this, "Card number must contains 16 digits", Toast.LENGTH_SHORT).show();
            return;
        }
        if(etMonth.getText().length() != 2 || Integer.parseInt(etMonth.getText().toString()) > 12){
            Toast.makeText(OrderActivity.this, "field 'MM' must contain 2 digits or less than 12", Toast.LENGTH_SHORT).show();
            return;
        }
        if(etYear.getText().length() !=2  || Integer.parseInt(etYear.getText().toString()) > year){
            Toast.makeText(OrderActivity.this, "field 'YY' must contain 2 digits and be less or equals current year", Toast.LENGTH_SHORT).show();
            return;
        }
        if(etCV.getText().length() !=3 ){
            Toast.makeText(OrderActivity.this, "field 'CV' must contain 3 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        //Notify user for success
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
        builder.setTitle("Payment completed").
                setMessage("Payment has been successfully completed! Check will be on your email.").
                setCancelable(false).
                setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });

        builder.create().show();
    }
}
