package com.example.alex.largo.largo.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;


public class Meet implements Parcelable {

    private String description;
    private String userName;
    private String petName;
    private boolean isActual;
    private String userPhotoUrl;
    private double latitude;
    private double longitude;
    private int hour;
    private int min;

    protected Meet(Parcel in) {
        description = in.readString();
        userName = in.readString();
        petName = in.readString();
        isActual = in.readByte() != 0;
        userPhotoUrl = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        hour = in.readInt();
        min = in.readInt();
    }

    public static final Creator<Meet> CREATOR = new Creator<Meet>() {
        @Override
        public Meet createFromParcel(Parcel in) {
            return new Meet(in);
        }

        @Override
        public Meet[] newArray(int size) {
            return new Meet[size];
        }
    };


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public Meet() {
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActual() {
        return isActual;
    }

    public void setActual(boolean actual) {
        isActual = actual;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(description);
        parcel.writeString(userName);
        parcel.writeString(petName);
        parcel.writeByte((byte) (isActual ? 1 : 0));
        parcel.writeString(userPhotoUrl);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeInt(hour);
        parcel.writeInt(min);
    }
}
