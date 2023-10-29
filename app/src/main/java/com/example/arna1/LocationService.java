package com.example.arna1;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LocationService extends Service {
    private static final String CHANNEL_ID = "LocationServiceChannel";
    private LocationManager locationManager;
    private LocationListener locationListener;

    DatabaseReference usersReference;


    @Override
    public void onCreate() {
       usersReference = FirebaseDatabase.getInstance().getReference("UserDetails");
        super.onCreate();
        startForeground();
        startLocationUpdates();
    }

    private void startForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Location Service", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Location Service")
                    .setContentText("Updating user location")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)

                    .build();

            startForeground(1, notification);
        }
    }

    private void startLocationUpdates() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocation(location);
            }


        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
        }
    }

    private void updateLocation(Location location) {

        double latitude = location.getLatitude();
           double longitude = location.getLongitude();

            String userId = getCurrentUserId();


            if (userId != null) {
                DatabaseReference userRef = usersReference.child(userId);
                userRef.child("latitude").setValue(latitude);
                userRef.child("longitude").setValue(longitude);
            }
        }
        private String getCurrentUserId() {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                return currentUser.getUid();
            }
            return null;
        }





    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void stopLocationUpdates() {
        locationManager.removeUpdates(locationListener);
    }
}
