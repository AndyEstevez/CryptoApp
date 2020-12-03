package com.example.cryptoapp;

import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;


import java.util.Calendar;

import mehdi.sakout.fancybuttons.FancyButton;


public class Buy_SellActivity extends AppCompatActivity {

    DatePickerDialog datePickerDialog;
    FancyButton btn_buy, btn_sell, btn_save;
    EditText et_quantity, et_price_per_coin, et_date;
    Intent goBack;
    String date, quantity, price_per_coin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy__sell);
        View.inflate(getApplicationContext(), R.layout.activity_buy__sell, null);

        btn_buy = findViewById(R.id.btn_buy);
        btn_sell = findViewById(R.id.btn_sell);
        btn_save = findViewById(R.id.btn_save);

        et_quantity = findViewById(R.id.et_quantity);
        et_price_per_coin = findViewById(R.id.et_price_per_coin);
        et_date = findViewById(R.id.et_date);

        // when the user clicks on saving a transaction they created
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // buy transaction
                // send back to the holdings fragment with new transaction
                if(btn_buy.isEnabled() == false) {
                    goBack = new Intent();

                    date = et_date.getText().toString();
                    quantity = et_quantity.getText().toString();
                    price_per_coin = "Paid $" + et_price_per_coin.getText().toString() + " USD per coin";

                    goBack.putExtra("image", R.drawable.resize_green);
                    goBack.putExtra("type", "Bought");
                    goBack.putExtra("date", date);
                    goBack.putExtra("quantity", quantity);
                    goBack.putExtra("price_per_coin", price_per_coin);

                    setResult(-1, goBack);
                    finish();
                }

                // sell transaction
                // send back to holdings fragment with new transaction
                else if (btn_sell.isEnabled() == false){
                    goBack = new Intent();

                    date = et_date.getText().toString();
                    quantity = et_quantity.getText().toString();
                    price_per_coin = "Received $" + et_price_per_coin.getText().toString() + " USD per coin";

                    goBack.putExtra("image", R.drawable.new_red);
                    goBack.putExtra("type", "Sold");
                    goBack.putExtra("date", date);
                    goBack.putExtra("quantity", quantity);
                    goBack.putExtra("price_per_coin", price_per_coin);

                    setResult(-1, goBack);
                    finish();
                }
            }
        });


    }

    // show the user which option they pick for the type of transaction they made
    // buy or sell option
    public void buyOption(View view) {
        btn_buy.setEnabled(false);
        btn_sell.setEnabled(true);
    }

    public void sellOption(View view) {
        btn_buy.setEnabled(true);
        btn_sell.setEnabled(false);
    }

    // used for picking the date for when the transaction was made
    public void datePicker(View view) {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(Buy_SellActivity.this,
                new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        et_date.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }



}
