package com.example.alex.largo.largo;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class LargoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
    }
}
