package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ForecastActivity extends AppCompatActivity {

    ImageButton imageBack;
    TextView textLocation,textUpdateTime;
    ListView listView;
    ConstraintLayout layoutForecast;

    CustomAdapter customAdapter;
    ArrayList<Weather> weatherArrayList;

    String lat = "";
    String lon = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        imageBack = findViewById(R.id.imageBack);
        textLocation = findViewById(R.id.textLocation);
        listView = findViewById(R.id.listView);
        textUpdateTime =findViewById(R.id.textUpdateTime);
        layoutForecast = findViewById(R.id.layoutForecast);
        weatherArrayList = new ArrayList<Weather>();
        customAdapter = new CustomAdapter(ForecastActivity.this, weatherArrayList);
        listView.setAdapter(customAdapter);


        Intent it = getIntent();
        String city = it.getStringExtra("Current City");
        lat = it.getStringExtra("Lat");
        lon = it.getStringExtra("Lon");
        textLocation.setText(city);

        getWeekWeatherData(lat, lon);
    }
    private void getWeekWeatherData(String lats,String lons) {
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat="+lats+"&lon="+lons+"&exclude=current,hourly,minutely,alerts&units=metric&appid=f48820e368fc63731e7028f896c8374f";
        RequestQueue requestQueue = Volley.newRequestQueue(ForecastActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("daily");
                    String[] arrDay = new String[jsonArray.length()+1];
                    String[] arrDayTemp = new String[jsonArray.length()+1];
                    String[] arrMin = new String[jsonArray.length()+1];
                    String[] arrMax = new String[jsonArray.length()+1];
                    String[] arrIcon = new String[jsonArray.length()+1];
                    String[] arrStatus = new String[jsonArray.length()+1];
                    String[] arrHumi = new String[jsonArray.length()+1];
                    String[] arrWind = new String[jsonArray.length()+1];
                    String[] arrCloud = new String[jsonArray.length()+1];


                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObjectList = jsonArray.getJSONObject(i);

                        // Date
                        String day = jsonObjectList.getString("dt");
                        long l = Long.valueOf(day);
                        java.util.Date date = new Date(l*1000L);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE,DD-MM ");
                        String Day = simpleDateFormat.format(date);
                        arrDay[i] = Day;
                        textUpdateTime.setText("Update to"+Day);


                        //Temp min - max
                        JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("temp");
                        String dayTemp = jsonObjectTemp.getString("day");
                        String maxTemp = jsonObjectTemp.getString("max");
                        String minTemp = jsonObjectTemp.getString("min");
                        Double daytemp = Double.valueOf(dayTemp);
                        Double max = Double.valueOf(maxTemp);
                        Double min = Double.valueOf(minTemp);
                        String Temperature_Max = String.valueOf(max.intValue());
                        String Temperature_Min = String.valueOf(min.intValue());
                        String Temperature_Day = String.valueOf(daytemp.intValue());

                        arrMax[i] = Temperature_Max;
                        arrMin[i] = Temperature_Min;
                        arrDayTemp[i] = Temperature_Day;

                        //Image and Desc
                        JSONArray jsonArrayWeather  = jsonObjectList.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                        String Status = jsonObjectWeather.getString("description");
                        String Icon = jsonObjectWeather.getString("icon");
                        arrIcon[i] = Icon;
                        arrStatus[i] = Status;

                        String Cloud = jsonObjectList.getString("clouds");
                        arrCloud[i] = Cloud;

                        String Humidity = jsonObjectList.getString("humidity");
                        arrHumi[i] = Humidity;

                        String WindSpeed = jsonObjectList.getString("wind_speed");
                        arrWind[i] = WindSpeed;

                        weatherArrayList.add(new Weather(Day,Status,Icon,Temperature_Max,Temperature_Min));
                    }
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            final Dialog dialog = new Dialog(ForecastActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.layout_dialog_detail_item_7day);
                            Window window = dialog.getWindow();
                            if (window == null){
                                return;
                            }
                            else{
                                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                                window.setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT));

                                WindowManager.LayoutParams windowAttributes = window.getAttributes();
                                windowAttributes.gravity = Gravity.CENTER;
                                window.setAttributes(windowAttributes);

                                TextView textDate = dialog.findViewById(R.id.textDate);
                                ImageView imageIcon = dialog.findViewById(R.id.imageIcon);
                                TextView textTemp = dialog.findViewById(R.id.textTemp);
                                TextView textTempMax = dialog.findViewById(R.id.textTempMax);
                                TextView textTempMin = dialog.findViewById(R.id.textTempMin);
                                TextView textDesc = dialog.findViewById(R.id.textDesc);
                                TextView textHumi = dialog.findViewById(R.id.textHumi);
                                TextView textWindspeed = dialog.findViewById(R.id.textWindspeed);
                                TextView textCloud = dialog.findViewById(R.id.textCloud);

                                textDate.setText(arrDay[position]);
                                textTemp.setText(arrDayTemp[position]+"°");
                                textTempMax.setText("Max : "+arrMax[position]+"°C");
                                textTempMin.setText("Min : "+arrMin[position]+"°C");
                                textDesc.setText("Status : "+arrStatus[position]);
                                textHumi.setText("Humidity : "+arrHumi[position]+"%");
                                textWindspeed.setText("Wind : "+arrWind[position]+"m/s");
                                textCloud.setText("Clouds : "+arrCloud[position]+"%");


                                if (arrIcon[position].equals("01d")){
                                    imageIcon.setImageResource(R.drawable.sun);
                                }
                                else if (arrIcon[position].equals("01n")){
                                    imageIcon.setImageResource(R.drawable.full_moon);
                                }
                                else if (arrIcon[position].equals("02d")){
                                    imageIcon.setImageResource(R.drawable.sun_cloud);
                                }
                                else if (arrIcon[position].equals("02n")){
                                    imageIcon.setImageResource(R.drawable.cloud_moon);
                                }
                                else if (arrIcon[position].equals("03d")||arrIcon[position].equals("03n")){
                                    imageIcon.setImageResource(R.drawable.cloud);
                                }
                                else if (arrIcon[position].equals("04d")||arrIcon[position].equals("04n")){
                                    imageIcon.setImageResource(R.drawable.cloud_broken);
                                }
                                else if (arrIcon[position].equals("09d")||arrIcon[position].equals("09n")){
                                    imageIcon.setImageResource(R.drawable.rain);
                                }
                                else if (arrIcon[position].equals("13d") || arrIcon[position].equals("13n")){
                                    imageIcon.setImageResource(R.drawable.snow);
                                }
                                else if (arrIcon[position].equals("50d") || arrIcon[position].equals("50n")){
                                    imageIcon.setImageResource(R.drawable.mist);
                                }
                                else if (arrIcon[position].equals("10d")){
                                    imageIcon.setImageResource(R.drawable.cloudy);
                                }
                                else if (arrIcon[position].equals("10n")){
                                    imageIcon.setImageResource(R.drawable.raining);
                                }
                                else if (arrIcon[position].equals("11n") || arrIcon[position].equals("11d")){
                                    imageIcon.setImageResource(R.drawable.storm);
                                }
                                else{
                                    imageIcon.setImageResource(R.drawable.ic_sunny);
                                }


                                dialog.setCancelable(true);
                                dialog.show();
                            }
                        }
                    });

                    customAdapter.notifyDataSetChanged();

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

    public void buttonBackOnClick(View v){
        onBackPressed();
    }

    public void buttonChartOnClick(View v){
        Intent it = new Intent(this,ChartActivity.class);
        startActivity(it);
    }
    public void refrestData(View v){
        finish();
        startActivity(getIntent());
    }

}