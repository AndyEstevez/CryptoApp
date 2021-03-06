package com.example.cryptoapp.ui.main;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChartFragment extends Fragment implements View.OnClickListener {

    private String api = "PUT-API-KEY-HERE"; // from https://www.coinapi.io/
    private RequestQueue queue;
    private Date date = new Date();
    public LineChart priceChart;

    private TextView change_in_price;

    private Button Hours;
    private Button Week;
    private Button Month;

    // Unique Array Lists & first timestamp for each time period,
    // to only have use a fixed amount of queues when switching to different charts
    ArrayList time_24H_Arr = new ArrayList();
    ArrayList<Double> price_24H_Arr = new ArrayList<Double>();
    private long first_24H_timestamp;

    ArrayList time_1W_Arr = new ArrayList();
    ArrayList<Double> price_1W_Arr = new ArrayList<Double>();
    private long first_1W_timestamp;
    
    ArrayList time_1M_Arr = new ArrayList();
    ArrayList<Double> price_1M_Arr = new ArrayList<Double>();
    private long first_1M_timestamp;


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

        change_in_price = view.findViewById(R.id.change_in_price);

        Hours = view.findViewById(R.id.Hours_Button);
        Week = view.findViewById(R.id.Week_Button);
        Month = view.findViewById(R.id.Month_Button);

        Hours.setOnClickListener(this);
        Week.setOnClickListener(this);
        Month.setOnClickListener(this);

        priceChart = (LineChart) view.findViewById(R.id.price_chart);
        priceChart.getDescription().setEnabled(false);
        priceChart.getLegend().setEnabled(false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        final ChartFragment context = this;

        queue = Volley.newRequestQueue(context.getContext());

        try {
            queue.add(find24HPrice(date));
            queue.add(find1WPrice(date));
            queue.add(find1MPrice(date));
            Hours.setEnabled(false);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Listeners for buttons to change chart based on the label
    @Override
    public void onClick(View v) {
        final ChartFragment context = this;

        queue = Volley.newRequestQueue(context.getContext());

        Hours.setEnabled(true);
        Week.setEnabled(true);
        Month.setEnabled(true);

        // plot chart based on which button clicked
        switch (v.getId()){
            case R.id.Hours_Button:
                    plotChart(first_24H_timestamp, price_24H_Arr, time_24H_Arr, priceChart);
                    setPercentColor(price_24H_Arr);
                    change_in_price.setText("24 Hour Change: " + getPercentageChange(price_24H_Arr) + "%");
                    Hours.setEnabled(false);
                    break;

            case R.id.Week_Button:
                    plotChart(first_1W_timestamp, price_1W_Arr, time_1W_Arr, priceChart);
                    setPercentColor(price_1W_Arr);
                    change_in_price.setText("1 Week Change: " + getPercentageChange(price_1W_Arr) + "%");
                    Week.setEnabled(false);
                    break;

            case R.id.Month_Button:
                    plotChart(first_1M_timestamp, price_1M_Arr, time_1M_Arr, priceChart);
                    setPercentColor(price_1M_Arr);
                    change_in_price.setText("1 Month Change: " + getPercentageChange(price_1M_Arr) + "%");
                    Month.setEnabled(false);
                    break;
        }

    }

    private void setPercentColor (ArrayList<Double> priceArr){
        Double percentageChange = getPercentageChange(priceArr);
        if(percentageChange < 0.00){
            change_in_price.setTextColor(Color.RED);
        }
        else{
            change_in_price.setTextColor(Color.GREEN);
        }
    }

    public StringRequest find1MPrice(Date date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        String current_time = sdf.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        String time_1MonthAgo = sdf.format(calendar.getTime());

        String url_for_historical_data = "https://rest.coinapi.io/v1/ohlcv/BTC/USD/history?period_id=1DAY&time_start=";
        String full_url = url_for_historical_data + time_1MonthAgo + "&apikey=" + api;

        first_1M_timestamp = get1MAgoTime(sdf, calendar);

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

                                String parsed_time = time.substring(0, 10) + " " + time.substring(11, 16);

                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                Date date = df.parse(parsed_time);
                                long unix_time = date.getTime();
                                long final_time = unix_time / 1000;
                                long timestamp = final_time - first_1M_timestamp;

                                time_1M_Arr.add(timestamp);
                                price_1M_Arr.add(price);
                            }

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error");
            }
        });
    }


    public StringRequest find1WPrice(Date date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        String current_time = sdf.format(calendar.getTime());
        calendar.add(Calendar.DATE, -7);
        String time_1WeekAgo = sdf.format(calendar.getTime());

        String url_for_historical_data = "https://rest.coinapi.io/v1/ohlcv/BTC/USD/history?period_id=6HRS&time_start=";
        String full_url = url_for_historical_data + time_1WeekAgo + "&time_end=" + current_time + "&apikey=" + api;

        first_1W_timestamp = get1WAgoTime(sdf, calendar);

        return new StringRequest(Request.Method.GET, full_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);

                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject tempJSONObject = jsonArray.getJSONObject(i);

                                Double price = tempJSONObject.getDouble("price_open");
                                String time = tempJSONObject.getString("time_period_start");

                                String parsed_time = time.substring(0, 10) + " " + time.substring(11, 16);

                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                Date date = df.parse(parsed_time);
                                long unix_time = date.getTime();
                                long final_time = unix_time / 1000;
                                long timestamp = final_time - first_1W_timestamp;

                                time_1W_Arr.add(timestamp);
                                price_1W_Arr.add(price);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error");
            }
        });
    }


    public StringRequest find24HPrice(Date date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        String current_time = sdf.format(calendar.getTime());
        calendar.add(Calendar.DATE, -1);
        String time_24hrsAgo = sdf.format(calendar.getTime());

        String url_for_historical_data = "https://rest.coinapi.io/v1/ohlcv/BTC/USD/history?period_id=1HRS&time_start=";
        String full_url = url_for_historical_data + time_24hrsAgo + "&time_end=" + current_time + "&apikey=" + api;

        // convert the time from 24 hours ago from zero minutes mark to unix time
        first_24H_timestamp = get24HoursAgoTime(sdf, calendar);

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

                            String parsed_time = time.substring(0, 10) + " " + time.substring(11, 16);

                            // change the date to unix time & subtract against the first hr in the 24hr time set
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Date date = df.parse(parsed_time);
                            long unix_time = date.getTime();
                            long final_time = unix_time / 1000;
                            long timestamp = final_time - first_24H_timestamp;

                            time_24H_Arr.add(timestamp);
                            price_24H_Arr.add(price);
                        }
                        plotChart(first_24H_timestamp, price_24H_Arr, time_24H_Arr, priceChart);
                        setPercentColor(price_24H_Arr);
                        change_in_price.setText("24 Hour Change: " + getPercentageChange(price_24H_Arr) + "%");

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

    private void plotChart(long timestamp, ArrayList<Double> priceArr, ArrayList timeArr, LineChart chart){
        LineDataSet lineDataSet = new LineDataSet(values(priceArr, timeArr), "");
        lineDataSet.setValueTextSize(12);
        lineDataSet.setLineWidth(3);
        lineDataSet.setFillColor(Color.GREEN);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawValues(false);
        lineDataSet.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return priceChart.getAxisLeft().getAxisMinimum();
            }
        });
        LineData data = new LineData(lineDataSet);

        chart.setData(data);
        chart.invalidate();

        // set the X-axis labels to time
        XAxis xAxis = priceChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(5, true);
        xAxis.setValueFormatter(new XValueFormatter(timestamp));
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.WHITE);

        YAxis yAxis = priceChart.getAxisLeft();
        yAxis.setTextSize(12f);
        yAxis.setTextColor(Color.WHITE);
    }

    private Double getPercentageChange(ArrayList<Double> priceArr){
        double previous_price = priceArr.get(0);
        double current_price = priceArr.get(priceArr.size()-1);

        double percentage = (((previous_price - current_price) / previous_price) * 100);
        double rounded_percent = Math.round(percentage * 100.0) / 100.0;
        return rounded_percent;
    }

    private long get1MAgoTime(SimpleDateFormat sdf, Calendar calendar) throws ParseException {
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        calendar.add(Calendar.DATE, 1);
        String MonthAgoTime = sdf.format(calendar.getTime());
        String time = MonthAgoTime.substring(0, 10) + " " + "00:00";
        Date d = sdf.parse(time);
        long unix_time = d.getTime();
        long final_time = unix_time / 1000;

        return final_time;
    }

    private long get1WAgoTime(SimpleDateFormat sdf, Calendar calendar) throws ParseException {
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String WeekAgoTime = sdf.format(calendar.getTime());
        int hour = Integer.parseInt(WeekAgoTime.substring(11, 13));
        String start_1W_time = change1WHour(hour) + ":00";
        String time = WeekAgoTime.substring(0, 10) + " " + start_1W_time;
        Date d = sdf.parse(time);
        long unix_time = d.getTime();
        long final_time = unix_time / 1000;

        return final_time;
    }

    private String change1WHour(int hour){
        if(hour >= 23 || hour <= 4){
            return String.valueOf(0);
        }
        else if(hour >= 5 && hour <= 10){
            return String.valueOf(6);
        }
        else if(hour >= 11 && hour <= 16){
            return String.valueOf(12);
        }
        else{
            return String.valueOf(18);
        }
    }
    // Used to get the first unix time for the 24hrs timeline, which is used to subtract the hours that proceed it
    private long get24HoursAgoTime(SimpleDateFormat sdf, Calendar calendar) throws ParseException {
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String past_24time = sdf.format(calendar.getTime());
        int yesterday_hour = Integer.parseInt(past_24time.substring(11, 13));
        String actual_24hour_num = changeHour(yesterday_hour) + ":00";
        String time = past_24time.substring(0, 10) + " " + actual_24hour_num;
        Date d = sdf.parse(time);
        long unix_time = d.getTime();
        long final_time = unix_time / 1000;

        return final_time;
    }

    // Used to change the 24 hrs ago specific hour to be increased by one to follow the same time as the api's past 24 hrs
    private String changeHour(int yesterday_hour) {
        int new_hour;

        if(yesterday_hour < 23){
            new_hour = yesterday_hour + 1;
            return String.valueOf(new_hour);
        }
        else {
            return String.valueOf(0);
        }

    }


    // Creates data entries for the data set that will be plotted
    public ArrayList<Entry> values (ArrayList<Double> priceArr, ArrayList timeArr) {
        ArrayList<Entry> values = new ArrayList<Entry>();
        for (int i = 0; i < priceArr.size(); i++){

            double price_double = priceArr.get(i);
            float price = (float) price_double;

            float time = Float.parseFloat(timeArr.get(i).toString());
            values.add(new Entry(time, price));
        }

        return values;
    }


    // Code used for this XValueFormatter class from https://github.com/PhilJay/MPAndroidChart/issues/789
    // User: @Yasir-Ghunaim
    class XValueFormatter extends ValueFormatter {
        private long x_value_timestamp;
        private Date mDate;
        private DateFormat mDataFormat;

        public XValueFormatter(long timestamp){
            this.x_value_timestamp = timestamp; // change timestamp to equal to param for the different timestamps
            this.mDate = date;
            this.mDataFormat = new SimpleDateFormat("MM-dd HH:mm");
        }

        // Change getFormattedValue() method to getAxisLabel since the old method is now deprecated
        public String getAxisLabel(float value, AxisBase axis){
            long convertedTimestamp = (long) value;
            long og_timestamp = this.x_value_timestamp + convertedTimestamp;
            return getHour(og_timestamp);
        }
        private String getHour(long timestamp){
            mDate.setTime(timestamp*1000);
            return mDataFormat.format(mDate);

        }
    }
}
