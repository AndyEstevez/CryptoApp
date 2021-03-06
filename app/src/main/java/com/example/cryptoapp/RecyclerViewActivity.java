package com.example.cryptoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;


public class RecyclerViewActivity extends AppCompatActivity {

    private RequestQueue queue;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String stayLoggedIn = "loggedIn";
    public static final String loggedOnce = "loggedOnce";
    public static final String USER = "username";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private double priceCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sharedPreferences.getBoolean(stayLoggedIn, true);
        sharedPreferences.getBoolean(loggedOnce, true);
        sharedPreferences.getString(USER, null);

        queue = Volley.newRequestQueue(this);


        // urls from the API to get the price and image of Bitcoin
        String url_for_price = "https://rest.coinapi.io/v1/assets";
        String url_for_image = "https://rest.coinapi.io/v1/assets/icons/128";

        final TextView name = findViewById(R.id.name_of_coin);
        final TextView price = findViewById(R.id.price_of_coin);
        final ImageView image = findViewById(R.id.image_of_coin);

        // Request for the price of coin
        final StringRequest priceRequest = new StringRequest(Request.Method.GET, url_for_price,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            // jsonArray.getJSONObject(0).getString("asset_id"); gets the first object in the json array and its asset id name
                            String getBTC = "";
                            for (int i = 0; i < jsonArray.length(); i++) {
                                getBTC = jsonArray.getJSONObject(i).getString("asset_id");

                                // find the symbol: "BTC" which matches for Bitcoin in the API response results
                                if(getBTC.equalsIgnoreCase("BTC")){
                                    JSONObject bitcoin = jsonArray.getJSONObject(i); // the specific index in API response where bitcoin is
                                    System.out.println(bitcoin.toString());
                                    priceCoin = (Double.valueOf(bitcoin.getString("price_usd"))); // get the current price
                                    DecimalFormat changeDecimals = new DecimalFormat("##.00");
                                    System.out.println(priceCoin);


                                    price.setText("$" + changeDecimals.format(priceCoin));
                                    name.setText(bitcoin.getString("asset_id"));
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                name.setText("That didn't work");
            }
        });


        // Request for the image icon of coin
        final StringRequest imageRequest = new StringRequest(Request.Method.GET, url_for_image,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            String getBTC = "";
                            for (int i = 0; i < jsonArray.length(); i++) {
                                getBTC = jsonArray.getJSONObject(i).getString("asset_id");

                                // find the index where the symbol is "BTC" to get Bitcoin
                                if(getBTC.equalsIgnoreCase("BTC")){
                                    int indexofBTC = i;
                                    JSONObject bitcoin = jsonArray.getJSONObject(i);
                                    String image_url = (bitcoin.getString("url")); // get the image url of Bitcoin
                                    // using Picasso to render and resize the image
                                    Picasso.get()
                                            .load(image_url)
                                            .resize(64, 64)
                                            .into(image);
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(priceRequest);
        queue.add(imageRequest);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_logout:
                editor.putBoolean(stayLoggedIn, false);
                editor.putBoolean(loggedOnce, false);
                editor.apply();
                editor.commit();
                Intent intent = new Intent(RecyclerViewActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // When the user clicks the recyclerview tab on info of Bitcoin and send to the charts and holdings activity
    public void sendToChart(View view) {
        Intent chart_holdings = new Intent(this, Chart_and_HoldingsActivity.class);
        chart_holdings.putExtra("value_of_bitcoin", priceCoin);
        startActivity(chart_holdings);
    }


}
