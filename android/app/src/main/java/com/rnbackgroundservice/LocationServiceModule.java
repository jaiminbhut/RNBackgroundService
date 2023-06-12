package com.rnbackgroundservice;

import android.app.Notification;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Service;
import android.os.IBinder;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class LocationServiceModule extends ReactContextBaseJavaModule {
    public static final String REACT_CLASS = "LocationService";
    private static ReactApplicationContext reactContext;

    public LocationServiceModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void startService() {
        // Starting the heartbeat service
        reactContext.startService(new Intent(reactContext, LocationService.class));
    }
}
