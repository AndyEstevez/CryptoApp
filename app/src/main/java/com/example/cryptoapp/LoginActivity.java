package com.example.cryptoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    CheckBox cb_login;
    EditText et_username, et_password;

    // used for shared preferences when the user has logged in
    // and if they decided to stay logged in through the checkbox
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String stayLoggedIn = "loggedIn";
    public static final String loggedOnce = "loggedOnce";
    public static final String USER = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Database db = new Database(this);

        btn_login = findViewById(R.id.btn_login);
        cb_login = findViewById(R.id.cb_login);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking if the login is successful or failed
                String username = et_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                boolean auth = db.checkUserCredentials(username, password);


                if(auth == true){
                    Toast.makeText(getBaseContext(), "Login Successful", Toast.LENGTH_LONG).show();
                    // when the login is successful, check if the user decided to stay logged in when they reopen the app
                    if(cb_login.isChecked() == true){
                        editor.putBoolean(stayLoggedIn, cb_login.isChecked());
                    }
                    // add to shared preferences and send back to main activity to see bitcoin pricing
                    editor.putBoolean(loggedOnce, true);
                    editor.putString(USER, username);
                    System.out.println(USER);
                    editor.apply();
//                    editor.commit();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                else{
                    // the login was a failure
                    et_username.setText("");
                    et_password.setText("");
                    Toast.makeText(getBaseContext(), "Username or password incorrect", Toast.LENGTH_LONG).show();
                }

            }

        });
    }


    // send to Register activity to create an account
    public void sendToRegister(View view) {
        Intent register = new Intent(this, RegisterActivity.class);
        startActivity(register);
    }
}
