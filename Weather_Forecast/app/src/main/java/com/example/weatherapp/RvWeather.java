package com.example.weatherapp;

public class RvWeather {
    public String Date;
    public String ImageID;
    public String MaxTemp;
    public String MinTemp;

    public RvWeather(String date, String imageID, String maxTemp, String minTemp) {
        Date = date;
        ImageID = imageID;
        MaxTemp = maxTemp;
        MinTemp = minTemp;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getImageID() {
        return ImageID;
    }

    public void setImageID(String imageID) {
        ImageID = imageID;
    }

    public String getMaxTemp() {
        return MaxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        MaxTemp = maxTemp;
    }

    public String getMinTemp() {
        return MinTemp;
    }

    public void setMinTemp(String minTemp) {
        MinTemp = minTemp;
    }
}
