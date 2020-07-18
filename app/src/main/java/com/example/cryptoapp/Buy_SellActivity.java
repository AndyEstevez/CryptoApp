package com.example.cryptoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.sql.Time;
import java.util.Calendar;


public class Buy_SellActivity extends AppCompatActivity {

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy__sell);
        View.inflate(getApplicationContext(), R.layout.activity_buy__sell, null);


        final EditText et_date = findViewById(R.id.et_date);

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });


    }
}
