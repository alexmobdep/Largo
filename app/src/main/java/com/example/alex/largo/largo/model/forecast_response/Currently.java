package com.example.alex.largo.largo.model.forecast_response;


import com.google.gson.annotations.SerializedName;

public class Currently {
    @SerializedName("time")
    int time;

    @SerializedName("summary")
    String summary;

    @SerializedName("icon")
    String icon;

    public int getTime() {
        return time;
    }

    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }
}
