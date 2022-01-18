package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<Weather> arrayList;

    public CustomAdapter(Context context, ArrayList<Weather> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.listview_line,null);

        Weather weather = arrayList.get(position);

        TextView textDate = (TextView) convertView.findViewById(R.id.textDate);
        TextView textStatus = (TextView) convertView.findViewById(R.id.textStatus);
        TextView textMaxTemp = (TextView) convertView.findViewById(R.id.textMaxTemp);
        TextView textMinTemp = (TextView) convertView.findViewById(R.id.textMinTemp);
        ImageView imageStatus_weather = (ImageView) convertView.findViewById(R.id.imageStatus_weather);

        textDate.setText(weather.Date);
        textStatus.setText(weather.Status);
        textMaxTemp.setText(weather.MaxTemp+"°C");
        textMinTemp.setText(weather.MinTemp+"°C");



        if (weather.Image.equals("01d")){
            imageStatus_weather.setImageResource(R.drawable.sun);
        }
        else if (weather.Image.equals("01n")){
            imageStatus_weather.setImageResource(R.drawable.full_moon);
        }
        else if (weather.Image.equals("02d")){
            imageStatus_weather.setImageResource(R.drawable.sun_cloud);
        }
        else if (weather.Image.equals("02n")){
            imageStatus_weather.setImageResource(R.drawable.cloud_moon);
        }
        else if (weather.Image.equals("03d")||weather.Image.equals("03n")){
            imageStatus_weather.setImageResource(R.drawable.cloud);
        }
        else if (weather.Image.equals("04d")||weather.Image.equals("04n")){
            imageStatus_weather.setImageResource(R.drawable.cloud_broken);
        }
        else if (weather.Image.equals("09d")||weather.Image.equals("09n")){
            imageStatus_weather.setImageResource(R.drawable.rain);
        }
        else if (weather.Image.equals("13d") || weather.Image.equals("13n")){
            imageStatus_weather.setImageResource(R.drawable.snow);
        }
        else if (weather.Image.equals("50d") || weather.Image.equals("50n")){
            imageStatus_weather.setImageResource(R.drawable.mist);
        }
        else if (weather.Image.equals("10d")){
            imageStatus_weather.setImageResource(R.drawable.cloudy);
        }
        else if (weather.Image.equals("10n")){
            imageStatus_weather.setImageResource(R.drawable.raining);
        }
        else if (weather.Image.equals("11n") || weather.Image.equals("11d")){
            imageStatus_weather.setImageResource(R.drawable.storm);
        }
        else{
            imageStatus_weather.setImageResource(R.drawable.ic_sunny);
        }

        return convertView;
    }

}
