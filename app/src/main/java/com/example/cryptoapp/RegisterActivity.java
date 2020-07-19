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

        final Database db = new Database(this);

        et_name = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_signup = findViewById(R.id.register);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel user;
                user = new UserModel(et_name.getText().toString(), et_password.getText().toString());
                String name = et_name.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                boolean checkUser = db.ifUserExists(name);

                if(checkUser == false){
                    db.addUser(user);
                    Toast.makeText(getBaseContext(), "Registration successful", Toast.LENGTH_LONG).show();
                    RegisterActivity.this.finish();
                }
                else{
                    et_name.setText("");
                    et_password.setText("");
                    Toast.makeText(getBaseContext(), "Registration Error", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
