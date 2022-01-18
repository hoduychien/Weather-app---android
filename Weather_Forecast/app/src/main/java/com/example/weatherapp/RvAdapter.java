package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {

    ArrayList<RvWeather> rvModels;
    Context context;

    public RvAdapter(Context context,ArrayList<RvWeather> rvModels){
        this.context = context;
        this.rvModels = rvModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recycler_line,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (rvModels.get(position).getImageID().equals("01d")){
            holder.imageWeather.setImageResource(R.drawable.sun);
        }
        else if (rvModels.get(position).getImageID().equals("01n")){
            holder.imageWeather.setImageResource(R.drawable.full_moon);
        }
        else if (rvModels.get(position).getImageID().equals("02d")){
            holder.imageWeather.setImageResource(R.drawable.sun_cloud);
        }
        else if (rvModels.get(position).getImageID().equals("02n")){
            holder.imageWeather.setImageResource(R.drawable.cloud_moon);
        }
        else if (rvModels.get(position).getImageID().equals("03d")||rvModels.get(position).getImageID().equals("03n")){
            holder.imageWeather.setImageResource(R.drawable.cloud);
        }
        else if (rvModels.get(position).getImageID().equals("04d")||rvModels.get(position).getImageID().equals("04n")){
            holder.imageWeather.setImageResource(R.drawable.cloud_broken);
        }
        else if (rvModels.get(position).getImageID().equals("09d")||rvModels.get(position).getImageID().equals("09n")){
            holder.imageWeather.setImageResource(R.drawable.rain);
        }
        else if (rvModels.get(position).getImageID().equals("13d") || rvModels.get(position).getImageID().equals("13n")){
            holder.imageWeather.setImageResource(R.drawable.snow);
        }
        else if (rvModels.get(position).getImageID().equals("50d") || rvModels.get(position).getImageID().equals("50n")){
            holder.imageWeather.setImageResource(R.drawable.mist);
        }
        else if (rvModels.get(position).getImageID().equals("10d")){
            holder.imageWeather.setImageResource(R.drawable.cloudy);
        }
        else if (rvModels.get(position).getImageID().equals("10n")){
            holder.imageWeather.setImageResource(R.drawable.raining);
        }
        else if (rvModels.get(position).getImageID().equals("11n") || rvModels.get(position).getImageID().equals("11d")){
            holder.imageWeather.setImageResource(R.drawable.storm);
        }
        else{
            holder.imageWeather.setImageResource(R.drawable.ic_sunny);
        }

        holder.textDate.setText(rvModels.get(position).getDate());
        holder.textMax.setText(rvModels.get(position).getMaxTemp()+"°");
        holder.textMin.setText(rvModels.get(position).getMinTemp());
    }

    @Override
    public int getItemCount() {
        return rvModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDate;
        TextView textMax;
        TextView textMin;
        ImageView imageWeather;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageWeather = itemView.findViewById(R.id.imageWeather);
            textDate = itemView.findViewById(R.id.textDate);
            textMax = itemView.findViewById(R.id.textMax);
            textMin = itemView.findViewById(R.id.textMin);

        }
    }
}
