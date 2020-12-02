package com.example.cryptoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String stayLoggedIn = "loggedIn";
    public static final String loggedOnce = "loggedOnce";
    public static final String USER = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // create a listener for the image button to go to Login Activity if logged out
        // If logged in send to price of coin

        ImageButton mainButton = findViewById(R.id.main_button);

        // checking the preferences for if the user decided to stay logged in
        final SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        final boolean checkLogin = preferences.getBoolean(stayLoggedIn, false);
        final boolean check1Login = preferences.getBoolean(loggedOnce, false);
        String username = preferences.getString(USER, null);

        // this is for if user is logged in already when they open the app again,
        // send to the price of Bitcoin
        if(checkLogin == true || check1Login == true){
            Intent gotoPortfolio = new Intent(this, RecyclerViewActivity.class);
            startActivity(gotoPortfolio);
        }


//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putBoolean("checkLogin", false);
//        editor.apply();

        mainButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // send to login activity if they are NOT signed in
                if (check1Login == false) {
                    Intent goLogin = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(goLogin);
                }

                // send to the price of bitcoin if they are signed in
                else if(checkLogin == true){
                    Intent gotoPortfolio = new Intent(MainActivity.this, RecyclerViewActivity.class);
                    startActivity(gotoPortfolio);
                }

                // send to the price of bitcoin if they are signed in
                else if(check1Login == true){
                    Intent gotoPortfolio = new Intent(MainActivity.this, RecyclerViewActivity.class);
                    startActivity(gotoPortfolio);
                }
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
