package com.example.cryptoapp.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cryptoapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ChartFragment extends Fragment {

    private String api = "PUT_API_KEY_HERE";
    private RequestQueue queue;
    private TextView textView;

    public ChartFragment(){

    }

    public static Fragment newInstance() {
        ChartFragment fragment = new ChartFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_chart, container, false);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        textView = view.findViewById(R.id.placeholder);
        final ChartFragment context = this;

        queue = Volley.newRequestQueue(context.getContext());


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");


        System.out.println("Today's date is "+sdf.format(calendar.getTime()));
        String current_time = sdf.format(calendar.getTime());
        calendar.add(Calendar.DATE, -1);

        System.out.println(sdf.format(calendar.getTime()));

        String url_for_historical_data = "https://rest.coinapi.io/v1/ohlcv/BTC/USD/history?period_id=1HRS&time_start=";
        String full_url = url_for_historical_data + sdf.format(calendar.getTime()) + "&time_end=" + current_time + "&apikey=" + api;
        System.out.println(full_url);

        final ArrayList<Double> price_Open = new ArrayList<Double>();

        // Request for price open for the past 24 hours from current time
        final StringRequest experimenting = new StringRequest(Request.Method.GET, full_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i < jsonArray.length(); i++){

                                JSONObject tempJSONObject = jsonArray.getJSONObject(i);
                                Double price = tempJSONObject.getDouble("price_open");
                                price_Open.add(price);
                            }
                            for(int i = 0; i < price_Open.size(); i++){
                                textView.setText(textView.getText() + "" + price_Open.get(i) + "\n");
                                System.out.println("hour = " + i + ": " + price_Open.get(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Didn't work");
            }
        });

        queue.add(experimenting);
    }


}
