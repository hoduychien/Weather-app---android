package com.example.weatherapp;

public class Weather {
    public String Date;
    public String Status;
    public String Image;
    public String MaxTemp;
    public String MinTemp;

    public Weather(String date, String status, String image, String maxTemp, String minTemp) {
        Date = date;
        Status = status;
        Image = image;
        MaxTemp = maxTemp;
        MinTemp = minTemp;
    }
}
