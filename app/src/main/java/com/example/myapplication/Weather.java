package com.example.myapplication;

public class Weather {
    public final String date;
    public final String minTemp;
    public final String maxTemp;
    public final String humidity;
    public final String description;
    public final String icon;

    public Weather(String date, double minTempC, double maxTempC,
                   double humidityFraction, String description, String icon) {

        this.date = date;
        this.minTemp = String.format("%.0f°C", minTempC);
        this.maxTemp = String.format("%.0f°C", maxTempC);
        this.humidity = (int) (humidityFraction * 100) + "%";
        this.description = description;
        this.icon = icon;
    }
}
