package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ImageView image;
    TextView textTemperature,textLocation,textStatus,textCloud,textHumidity,textWind,textDate,
            textSunrise,textSunset,textMax,textMin,textFeekslike,textPressure,textUpdateTime;
    ImageButton buttonAdd;
    ListView listView;
    RecyclerView recyclerView;

    CustomAdapter customAdapter;
    ArrayList<Weather> weatherArrayList;

    ArrayList<RvWeather> rvWeathers_main;
    RvAdapter rvAdapter_main;

    String city = "Quang tri";
    String lat = "107.2";
    String lon = "16.75";
    public String[] Date = {""};
    public String[] Image = {""};
    public String[] Temp = {""};
    public String[] Status = {""};
    public Integer[] temp ={};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.image);
        recyclerView = findViewById(R.id.recyclerView);
        textTemperature = findViewById(R.id.textTemperature);
        textLocation = findViewById(R.id.textLocation);
        textStatus = findViewById(R.id.textStatus);
        textCloud = findViewById(R.id.textCloud);
        textHumidity = findViewById(R.id.textHumidity);
        textWind = findViewById(R.id.textWind);
        textDate = findViewById(R.id.textDate);
        textSunrise= findViewById(R.id.textSunrise);
        textSunset= findViewById(R.id.textSunset);
        textMax = findViewById(R.id.textMax);
        textMin = findViewById(R.id.textMin);
        textFeekslike = findViewById(R.id.textFeekslike);
        textUpdateTime = findViewById(R.id.textUpdateTime);
        textPressure = findViewById(R.id.textPressure);
        buttonAdd= findViewById(R.id.buttonAdd);
        listView = findViewById(R.id.listView);


        weatherArrayList = new ArrayList<Weather>();
        customAdapter = new CustomAdapter(MainActivity.this, weatherArrayList);


        getCurrentWeatherData(city);


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAddLocationDialog(Gravity.CENTER);
            }
        });

    }
    private void OpenAddLocationDialog(int gravity){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_add_location);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        else{
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = gravity;
            window.setAttributes(windowAttributes);

            if (Gravity.CENTER == gravity){
                dialog.setCancelable(true);
            }
            else{
                dialog.setCancelable(false);
            }

            EditText editTextLocation = dialog.findViewById(R.id.editTextLocation);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
            Button buttonFind = dialog.findViewById(R.id.buttonFind);

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            buttonFind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editTextLocation.getText().toString().equals("")){
                        return;
                    }
                    else{
                        city = editTextLocation.getText().toString();
                        getCurrentWeatherData(city);
                        hidenKeyBoard();
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
        }

    }

    String currentCity = "";
    public void getCurrentWeatherData(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
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
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMMM DD   HH:mm aa");
                            String Day = simpleDateFormat.format(date);
                            textDate.setText(Day);
                            textUpdateTime.setText(Day);


                            JSONObject jsonObjectSystem = jsonObject.getJSONObject("sys");
                            String country = jsonObjectSystem.getString("country");
                            String name = jsonObject.getString("name");
                            String sunrise = jsonObjectSystem.getString("sunrise");
                            String sunset = jsonObjectSystem.getString("sunset");
                            long sr = Long.valueOf(sunrise);
                            Date sunrises = new Date(sr*1000L);
                            long ss = Long.valueOf(sunset);
                            Date sunsets = new Date(ss*1000L);
                            SimpleDateFormat srFormat = new SimpleDateFormat("HH:MM");
                            String Sunr = srFormat.format(sunrises);
                            String Suns = srFormat.format(sunsets);


                            currentCity = name;
                            textLocation.setText(name + " - " + country);
                            textSunrise.setText(Sunr);
                            textSunset.setText(Suns);


                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String status = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");
                            setImage(icon);

                            textStatus.setText(status);

                            setImage(icon);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String temperature = jsonObjectMain.getString("temp");
                            String humidity = jsonObjectMain.getString("humidity");
                            String current_Max = jsonObjectMain.getString("temp_max");
                            String current_Min = jsonObjectMain.getString("temp_min");
                            String pressure = jsonObjectMain.getString("pressure");
                            String feels_like = jsonObjectMain.getString("feels_like");


                            textMax.setText(current_Max);
                            textMin.setText(current_Min);
                            textPressure.setText(pressure+"hPa");
                            Double Feels = Double.valueOf(feels_like);
                            String Feels_Like = String.valueOf(Feels.intValue());
                            textFeekslike.setText(Feels_Like+"°");
                            Double a = Double.valueOf(temperature);
                            String Temperature = String.valueOf(a.intValue());
                            textTemperature.setText(Temperature);
                            textHumidity.setText(humidity+"%");


                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String wind = jsonObjectWind.getString("speed");
                            textWind.setText(wind+"m/s");
                            

                            JSONObject jsonObjectClouds = jsonObject.getJSONObject("clouds");
                            String cloud = jsonObjectClouds.getString("all");
                            textCloud.setText(cloud+"%");

                            JSONObject jsonObjectCoord = jsonObject.getJSONObject("coord");
                            lon = jsonObjectCoord.getString("lon");
                            lat = jsonObjectCoord.getString("lat");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getHourlyWeatherData(lat,lon);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
        });
        requestQueue.add(stringRequest);
    }


    private void getHourlyWeatherData(String lats,String lons) {
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat="+lats+"&lon="+lons+"&exclude=daily,minutely,current,alerts&units=metric&appid=f48820e368fc63731e7028f896c8374f";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
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
                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject jsonObjectList = jsonArray.getJSONObject(i);
                                String day = jsonObjectList.getString("dt");
                                long l = Long.valueOf(day);
                                java.util.Date date = new Date(l*1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE HH:MM");
                                String Day = simpleDateFormat.format(date);
                                Date[i] = Day;


                                String Temps = jsonObjectList.getString("temp");

                                Double temp = Double.valueOf(Temps);
                                String Temperature = String.valueOf(temp.intValue());

                                Temp[i] = Temperature;

                                //Image and Desc
                                JSONArray jsonArrayWeather  = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String Desc = jsonObjectWeather.getString("main");
                                String Icon = jsonObjectWeather.getString("icon");
                                Image[i] = Icon;
                                Status[i] = Desc;

                                rvWeathers_main = new ArrayList<>();

                                for (int j = 0;j < jsonArray.length();j++){
                                    RvWeather model = new RvWeather(Date[j],Image[j],Temp[j],Status[j]);
                                    rvWeathers_main.add(model);
                                }
                                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this,
                                        LinearLayoutManager.HORIZONTAL,false);
                                recyclerView.setLayoutManager(layoutManager);


                                rvAdapter_main = new RvAdapter(MainActivity.this,rvWeathers_main);
                                recyclerView.setAdapter(rvAdapter_main);
                            }

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

    public void buttonChartOnClick(View v){
        Intent it = new Intent(this,ChartActivity.class);
        it.putExtra("Current City",currentCity);
        it.putExtra("Lat",lat);
        it.putExtra("Lon",lon);
        startActivity(it);
    }

    public void buttonForecastOnClick(View v){
        Intent it = new Intent(this,ForecastActivity.class);
        it.putExtra("Current City",currentCity);
        it.putExtra("Lat",lat);
        it.putExtra("Lon",lon);
        startActivity(it);
    }

    public void setImage(String s){

        if (s.equals("01d")){
            image.setImageResource(R.drawable.sun);
        }
        else if (s.equals("01n")){
            image.setImageResource(R.drawable.full_moon);
        }
        else if (s.equals("02d")){
            image.setImageResource(R.drawable.sun_cloud);
        }
        else if (s.equals("02n")){
            image.setImageResource(R.drawable.cloud_moon);
        }
        else if (s.equals("03d")||s.equals("03n")){
            image.setImageResource(R.drawable.cloud);
        }
        else if (s.equals("04d")||s.equals("04n")){
            image.setImageResource(R.drawable.cloud_broken);
        }
        else if (s.equals("09d")||s.equals("09n")){
            image.setImageResource(R.drawable.rain);
        }
        else if (s.equals("13d") || s.equals("13n")){
            image.setImageResource(R.drawable.snow);
        }
        else if (s.equals("50d") || s.equals("50n")){
            image.setImageResource(R.drawable.mist);
        }
        else if (s.equals("10d")){
            image.setImageResource(R.drawable.cloudy);
        }
        else if (s.equals("10n")){
            image.setImageResource(R.drawable.raining);
        }
        else if (s.equals("11n") || s.equals("11d")){
            image.setImageResource(R.drawable.storm);
        }
        else{
            image.setImageResource(R.drawable.ic_sunny);
        }
    }

    public void hidenKeyBoard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),0);
        }
        catch (Exception e){

        }
    }

    public void refrestData(View v){
        finish();
        startActivity(getIntent());
    }
}