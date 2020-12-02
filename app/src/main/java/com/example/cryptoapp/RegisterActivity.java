package com.example.cryptoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText et_name, et_password;
    Button btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // creating a database
        final Database db = new Database(this);

        et_name = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_signup = findViewById(R.id.register);

        // when signup button is clicked
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel user;
                // creating user object and adding username & password
                // and checking if the username is already in the database
                user = new UserModel(et_name.getText().toString(), et_password.getText().toString());
                String name = et_name.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                boolean checkUser = db.ifUserExists(name);

                // Username is available and send back to Login Activity
                if(checkUser == false){
                    db.addUser(user);
                    Toast.makeText(getBaseContext(), "Registration successful", Toast.LENGTH_LONG).show();
                    RegisterActivity.this.finish();
                }

                // Username already exists and reset the text fields to attempt at making account again
                else{
                    et_name.setText("");
                    et_password.setText("");
                    Toast.makeText(getBaseContext(), "Registration Error", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
