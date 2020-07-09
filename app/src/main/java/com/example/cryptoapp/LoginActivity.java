package com.example.cryptoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        System.out.println("In Login Activity");

    }

    public void sendToRegister(View view) {
        Intent register = new Intent(this, RegisterActivity.class);
        startActivity(register);
    }
}
