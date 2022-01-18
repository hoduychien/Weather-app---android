package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ChartActivity extends AppCompatActivity {

    TextView textLocation,textSunrise,textSunset,textWind,
            textHumidity,textSealv,textFeekslike,textClouds,textPressure,textUpdateTime;
    RecyclerView recyclerView;
    ArrayList<RvWeather> rvWeathers;
    RvAdapter rvAdapter;
    String lat = "";
    String lon = "";
    public String[] Date = {""};
    public String[] Hour = {""};
    public Integer[] HourlyTemp = {};
    public String[] Image = {""};
    public String[] Temp = {""};
    public String[] Status = {""};
    public Integer[] temp ={};
    BarChart barChart;

    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        textLocation = findViewById(R.id.textLocation);
        recyclerView = findViewById(R.id.recyclerView);
        textSunrise = findViewById(R.id.textSunrise);
        textSunset = findViewById(R.id.textSunset);
        textWind = findViewById(R.id.textWind);
        textHumidity = findViewById(R.id.textHumidity);
        textSealv = findViewById(R.id.textSealv);
        textFeekslike = findViewById(R.id.textFeekslike);
        textClouds = findViewById(R.id.textClouds);
        textPressure = findViewById(R.id.textPressure);
        textUpdateTime = findViewById(R.id.textUpdateTime);
        barChart = findViewById(R.id.barChart);
        lineChart = findViewById(R.id.lineChart);

        Intent it = getIntent();
        String city = it.getStringExtra("Current City");
        lat = it.getStringExtra("Lat");
        lon = it.getStringExtra("Lon");
        textLocation.setText(city);
        getHourlyWeatherData(lat,lon);
        getCurrentWeatherData(city);
        getTempWeatherData(lat,lon);



    }

    private void getHourlyWeatherData(String lats,String lons) {
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat="+lats+"&lon="+lons+"&exclude=daily,minutely,current,alerts&units=metric&appid=f48820e368fc63731e7028f896c8374f";
        RequestQueue requestQueue = Volley.newRequestQueue(ChartActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("hourly");
                            Date = new String[jsonArray.length()+1];
                            Image = new String[jsonArray.length()+1];
                            Temp = new String[jsonArray.length()+1];
                            Status= new String[jsonArray.length()+1];
                            Hour = new String[jsonArray.length()+1];
                            HourlyTemp = new Integer[jsonArray.length()+1];
                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject jsonObjectList = jsonArray.getJSONObject(i);
                                String day = jsonObjectList.getString("dt");
                                long l = Long.valueOf(day);
                                java.util.Date date = new Date(l*1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM,DD HH:MM");
                                SimpleDateFormat hourFormat = new SimpleDateFormat("HH:MM aa");
                                String Day = simpleDateFormat.format(date);
                                String hour = hourFormat.format(date);
                                Date[i] = Day;
                                Hour[i] = hour;

                                String Temps = jsonObjectList.getString("temp");
                                Double temp = Double.valueOf(Temps);
                                String Temperature = String.valueOf(temp.intValue());
                                Temp[i] = Temperature;
                                HourlyTemp[i] = Integer.parseInt(Temperature);

                                JSONArray jsonArrayWeather  = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String Desc = jsonObjectWeather.getString("main");
                                String Icon = jsonObjectWeather.getString("icon");
                                Image[i] = Icon;
                                Status[i] = Desc;

                                // Recycle view
                                rvWeathers = new ArrayList<>();
                                for (int j = 0;j < jsonArray.length();j++){
                                    RvWeather model = new RvWeather(Date[j],Image[j],Temp[j],Status[j]);
                                    rvWeathers.add(model);
                                }
                                LinearLayoutManager layoutManager = new LinearLayoutManager(ChartActivity.this,
                                        LinearLayoutManager.HORIZONTAL,false);
                                recyclerView.setLayoutManager(layoutManager);
                                rvAdapter = new RvAdapter(ChartActivity.this,rvWeathers);
                                recyclerView.setAdapter(rvAdapter);

                                // End Recycle view
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayList<Entry> dataVals = new ArrayList<Entry>();

                        for (int i = 0; i < 24; i++) {
                            dataVals.add(new Entry(i, HourlyTemp[i]));
                        }

                        LineDataSet lineDataSet = new LineDataSet(dataVals,"Data");

                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(lineDataSet);

                        LineData lineData = new LineData(dataSets);
                        lineData.setDrawValues(false);
                        lineDataSet.setDrawCircles(false);

                        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

                        lineChart.setData(lineData);

                        lineChart.getDescription().setText("");
                        lineChart.getXAxis().setTextSize(12);
                        lineChart.getXAxis().setTextColor(Color.WHITE);
                        lineChart.animateX(3500);
                        lineChart.getData().setHighlightEnabled(false);
                        lineChart.getAxisRight().setDrawLabels(false);
                        lineChart.getAxisLeft().setTextSize(12);
                        lineChart.getAxisLeft().setTextColor(Color.WHITE);
                        lineChart.getLegend().setEnabled(false);
                        lineChart.setExtraTopOffset(10);
                        lineChart.setExtraRightOffset(10);

                        XAxis xAxis = lineChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(Hour));

                        lineChart.invalidate();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }

    public void getCurrentWeatherData(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(ChartActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=f48820e368fc63731e7028f896c8374f";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String day = jsonObject.getString("dt");
                            long l = Long.valueOf(day);
                            Date date = new Date(l*1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD/MM  HH:mm aa");
                            String Day = simpleDateFormat.format(date);
                            textUpdateTime.setText("Updated "+Day);

                            JSONObject jsonObjectSystem = jsonObject.getJSONObject("sys");
                            String sunrise = jsonObjectSystem.getString("sunrise");
                            String sunset = jsonObjectSystem.getString("sunset");
                            long sr = Long.valueOf(sunrise);
                            Date sunrises = new Date(sr*1000L);
                            long ss = Long.valueOf(sunset);
                            Date sunsets = new Date(ss*1000L);
                            SimpleDateFormat srFormat = new SimpleDateFormat("HH:MM");
                            String Sunr = srFormat.format(sunrises);
                            String Suns = srFormat.format(sunsets);

                            textSunrise.setText(Sunr);
                            textSunset.setText(Suns);

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String wind = jsonObjectWind.getString("speed");
                            textWind.setText(wind+"m/s");

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String sea_level = jsonObjectMain.getString("sea_level");
                            String humidity = jsonObjectMain.getString("humidity");
                            String pressure = jsonObjectMain.getString("pressure");
                            String feels_like = jsonObjectMain.getString("feels_like");

                            textHumidity.setText(humidity+"%");
                            textSealv.setText(sea_level);
                            textPressure.setText(pressure+"hPa");


                            Double Feels = Double.valueOf(feels_like);
                            String Feels_Like = String.valueOf(Feels.intValue());
                            textFeekslike.setText(Feels_Like+"°");



                            JSONObject jsonObjectClouds = jsonObject.getJSONObject("clouds");
                            String cloud = jsonObjectClouds.getString("all");
                            textClouds.setText(cloud+"%");


                            JSONObject jsonObjectCoord = jsonObject.getJSONObject("coord");
                            lon = jsonObjectCoord.getString("lon");
                            lat = jsonObjectCoord.getString("lat");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }

    private void getTempWeatherData(String lats,String lons) {
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat="+lats+"&lon="+lons+"&exclude=current,hourly,minutely,alerts&units=metric&appid=f48820e368fc63731e7028f896c8374f";
        RequestQueue requestQueue = Volley.newRequestQueue(ChartActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArray = jsonObject.getJSONArray("daily");

                            temp = new Integer[jsonArray.length()+1];

                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject jsonObjectList = jsonArray.getJSONObject(i);

                                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("temp");
                                String dayTemp = jsonObjectTemp.getString("day");
                                Double daytemp = Double.valueOf(dayTemp);
                                String Temperature_Day = String.valueOf(daytemp.intValue());

                                temp[i] = Integer.parseInt(Temperature_Day);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ArrayList<BarEntry> visitors = new ArrayList<>();
                        visitors.add(new BarEntry(1,temp[0]));
                        visitors.add(new BarEntry(2,temp[1]));
                        visitors.add(new BarEntry(3,temp[2]));
                        visitors.add(new BarEntry(4,temp[3]));
                        visitors.add(new BarEntry(5,temp[4]));
                        visitors.add(new BarEntry(6,temp[5]));
                        visitors.add(new BarEntry(7,temp[6]));

                        BarDataSet barDataSet = new BarDataSet(visitors,"Visitor");
                        BarData data = new BarData(barDataSet);
                        data.setValueFormatter(new MyValueFormatter());
                        barChart.setData(data);
                        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                        barDataSet.setValueTextColor(Color.WHITE);
                        barDataSet.setValueTextSize(16f);

                        barChart.setFitBars(true);
                        barChart.getDescription().setText("");
                        barChart.getXAxis().setTextSize(18);
                        barChart.getXAxis().setTextColor(Color.WHITE);
                        barChart.animateY(2000);

                        barChart.getAxisLeft().setDrawLabels(false);
                        barChart.getAxisRight().setDrawLabels(false);
                        barChart.getLegend().setEnabled(false);
                        barChart.setExtraTopOffset(10);
                        barChart.setExtraRightOffset(10);


                        String[] days = new String[]{"","Mo","Tu",
                                "We","Th","Fr","Sa","Su"};
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));

                        barChart.invalidate();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }

    private class MyValueFormatter extends ValueFormatter implements IValueFormatter {

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return value + " °";
        }
    }

    public void buttonBackOnClick(View v){
        onBackPressed();
    }

    public void refrestData(View v){
        finish();
        startActivity(getIntent());
    }

    public void openWeather(View v){
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setData(Uri.parse("https://openweathermap.org/"));
        startActivity(it);
    }

}