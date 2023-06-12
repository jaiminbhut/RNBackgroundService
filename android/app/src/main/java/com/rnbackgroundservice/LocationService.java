package com.rnbackgroundservice;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.app.Service;
import android.location.LocationRequest;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class LocationService extends Service implements LocationListener {
    private LocationManager locationManager;
    private boolean moving = false;
    private String speed = "0 KM/H";
    NotificationChannel chan = null;
    NotificationCompat.Builder notificationBuilder = null;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "LocationServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the location manager and listener
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return START_STICKY;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);

        // Start the service in the foreground
        createNotificationChannel();

        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            chan = new NotificationChannel(CHANNEL_ID, "Driving " + (moving ? "ON" : "OFF"), NotificationManager.IMPORTANCE_HIGH);
        }

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(chan);
        }

        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        Notification notification = notificationBuilder
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Driving " + (moving ? "ON" : "OFF"))
                .setContentText("Speed " + speed)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setChannelId(CHANNEL_ID).build();

        startForeground(1, notification);
    }


    private void updateNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(chan);
        }

        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID).setOngoing(true).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Driving " + (moving ? "ON" : "OFF")).setContentText("Speed " + speed).setPriority(NotificationManager.IMPORTANCE_HIGH).setCategory(Notification.CATEGORY_SERVICE);

        manager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop location updates
        locationManager.removeUpdates(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        moving = location.hasSpeed() && location.getSpeed() > 0;
        speed = location.hasSpeed() && location.getSpeed() > 0 ? Integer.parseInt(String.valueOf(location.getSpeed() * 3.6)) + " KM/H" : "0 KM/H";

        updateNotification();

        // For example, you can send a broadcast to update UI components or save the values to a database
//        Intent intent = new Intent("LOCATION_UPDATE");
//        intent.putExtra("latitude", latitude);
//        intent.putExtra("longitude", longitude);
//        intent.putExtra("isMoving", isMoving);
//        moving = isMoving;
//        sendBroadcast(intent);
    }
}
