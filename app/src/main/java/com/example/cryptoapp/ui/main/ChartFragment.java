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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ChartFragment extends Fragment {

    private String api = "PUT_API_KEY_HERE"; // from https://www.coinapi.io/
    private RequestQueue queue;

    ArrayList timeArr = new ArrayList<>();
    ArrayList<Double> priceArr = new ArrayList<Double>();

    public LineChart priceChart;

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

        final ChartFragment context = this;

        priceChart = (LineChart) view.findViewById(R.id.price_chart);

        queue = Volley.newRequestQueue(context.getContext());

        queue.add(findPrice());

    }



    public StringRequest findPrice(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        final Date date = new Date();

        System.out.println("Today's date is "+sdf.format(calendar.getTime()));
        String current_time = sdf.format(calendar.getTime());
        calendar.add(Calendar.DATE, -1);

        String url_for_historical_data = "https://rest.coinapi.io/v1/ohlcv/BTC/USD/history?period_id=1HRS&time_start=";
        String full_url = url_for_historical_data + sdf.format(calendar.getTime()) + "&time_end=" + current_time + "&apikey=" + api;
        System.out.println(full_url);

        final ArrayList<Double> price_Open = new ArrayList<Double>();
        final ArrayList timeList = new ArrayList<>();

        return new StringRequest(Request.Method.GET, full_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                        for(int i = 0; i < jsonArray.length(); i++){

                            JSONObject tempJSONObject = jsonArray.getJSONObject(i);

                            Double price = tempJSONObject.getDouble("price_open");
                            String time = tempJSONObject.getString("time_period_start");

                            String parsed_time = time.substring(0, 10) + " " + time.substring(11, 19);
                            System.out.println("Parsed Time  = " + parsed_time);


                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = df.parse(parsed_time);
                            long unix_time = date.getTime();
                            long final_time = unix_time / 1000;
                            System.out.println("The Unix time :" + final_time);

                            String hour = time.substring(11, 13);
                            String min = time.substring(14, 16);
                            String actual_time = hour + "." + min;


                          //  System.out.println("New time format: " + actual_time);
                            timeList.add(actual_time);
                            price_Open.add(price);

                        }
                        setTimeArr(timeList);
                        timeArr = (ArrayList) getTimeArr();

                        setPriceArr(price_Open);
                        priceArr = getPriceArr();

                        LineDataSet lineDataSet = new LineDataSet(values(), "Bitcoin");
//                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//                        dataSets.add(lineDataSet);

                        LineData data = new LineData(lineDataSet);
                        priceChart.setData(data);
                        priceChart.invalidate();

                    }   catch (JSONException | ParseException e) {
                            e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Didn't work");
            }
        });
    }

    public ArrayList<Entry> values () {
        ArrayList<Entry> values = new ArrayList<Entry>();
        System.out.println(priceArr.size());
        for (int i = 0; i < priceArr.size(); i++){

            double price_double = priceArr.get(i);
            float price = (float) price_double;

            double time_double = Double.valueOf((String) timeArr.get(i));
            float time = (float) time_double;
           // System.out.println("Time value: " + time);

            values.add(new Entry(time, price));
            //System.out.println("Values : "+ values.get(i));
        }

        return values;
    }

    public List getTimeArr(){
        return timeArr;
    }

    public void setTimeArr(ArrayList arrayList){
          this.timeArr = arrayList;
    }

    public ArrayList<Double> getPriceArr(){
        return priceArr;
    }
    public void setPriceArr(ArrayList<Double> arrayList){
        this.priceArr = arrayList;
    }

}
