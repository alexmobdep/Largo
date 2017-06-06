package com.example.alex.largo.largo.model.forecast_response;


import com.google.gson.annotations.SerializedName;

import java.util.TimeZone;

public class ForecastResponse {
    @SerializedName("latitude")
    double latitude;

    @SerializedName("longitude")
    double longitude;

    @SerializedName("timezone")
    TimeZone timeZone;

    @SerializedName("currently")
    Currently currently;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public Currently getCurrently() {
        return currently;
    }
}
