package com.example.cryptoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardview);

        Intent intent = getIntent();

//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cardlist);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(linearLayoutManager);





        queue = Volley.newRequestQueue(this);
        String api = "PUT_YOUR_API_KEY_HERE"; // from https://www.coinapi.io/
        String url_for_price = "https://rest.coinapi.io/v1/assets";
        String url_for_image = "https://rest.coinapi.io/v1/assets/icons/32";
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

                                if(getBTC.equalsIgnoreCase("BTC")){
                                    JSONObject bitcoin = jsonArray.getJSONObject(i);

                                    double priceCoin = (Double.valueOf(bitcoin.getString("price_usd")));
                                    DecimalFormat changeDecimals = new DecimalFormat("##.00");
                                    System.out.println(priceCoin);


                                    price.setText("Price: $" + changeDecimals.format(priceCoin));
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

                                if(getBTC.equalsIgnoreCase("BTC")){
                                    int indexofBTC = i;
                                    JSONObject bitcoin = jsonArray.getJSONObject(i);
                                    String image_url = (bitcoin.getString("url"));
                                    Picasso.get()
                                            .load(image_url)
                                            .resize(32, 32)
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
}
