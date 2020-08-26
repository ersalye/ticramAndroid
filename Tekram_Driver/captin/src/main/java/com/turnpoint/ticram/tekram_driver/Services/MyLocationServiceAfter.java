package com.turnpoint.ticram.tekram_driver.Services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.location.LocationRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.turnpoint.ticram.tekram_driver.MySharedPreference;
import com.turnpoint.ticram.tekram_driver.R;
import com.turnpoint.ticram.tekram_driver.modules.FollowCooridnates;
import com.turnpoint.ticram.tekram_driver.modules.addcoordsfirebase;
import com.yayandroid.locationmanager.base.LocationBaseService;
import com.yayandroid.locationmanager.configuration.DefaultProviderConfiguration;
import com.yayandroid.locationmanager.configuration.GooglePlayServicesConfiguration;
import com.yayandroid.locationmanager.configuration.LocationConfiguration;
import com.yayandroid.locationmanager.constants.ProcessType;
import com.yayandroid.locationmanager.constants.ProviderType;
import io.paperdb.Paper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
public class MyLocationServiceAfter extends LocationBaseService {
    private boolean isLocationRequested = false;
    private static final String TAG = "LocServiceBeforeTawklna";

    @Override
    public void onLocationChanged(Location location) {
        if (!(new MySharedPreference(getApplicationContext()).getBooleanShared("startDistanceCount", false) || Paper.book().read("startDistanceCount", false))) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startMyOwnForeground();
                    stopForeground(true);
                    stopSelf();
                }
            } catch (Exception ex) {
            }
            return;
        }
        if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
            long newTime = System.currentTimeMillis();
            long oldTime = Paper.book().read("lastCall", 0L);
            long difference_sec = (newTime - oldTime) / 1000;
            if (difference_sec < 2 && oldTime != 0) {
                return;
            }
            Paper.book().write("lastCall", newTime);
            calculations(location.getLatitude(), location.getLongitude(), newTime);
        }
    }

    @Override
    public void onLocationFailed(int type) {
    }

    @Override
    public void onProcessTypeChanged(@ProcessType int processType) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public LocationConfiguration getLocationConfiguration() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1 * 1000);
        locationRequest.setFastestInterval(1 * 1000);
        LocationConfiguration awesomeConfiguration = new LocationConfiguration.Builder()
                .keepTracking(true)
                .useGooglePlayServices(new GooglePlayServicesConfiguration.Builder()
                        .fallbackToDefault(true)
                        .askForGooglePlayServices(false)
                        .askForSettingsApi(false)
                        .failOnConnectionSuspended(false)
                        .failOnSettingsApiSuspended(false)
                        .ignoreLastKnowLocation(false)
                        .setWaitPeriod(1 * 1000)
                        .locationRequest(locationRequest)
                        .build())
                .useDefaultProviders(new DefaultProviderConfiguration.Builder()
                        .requiredTimeInterval(1 * 1000)
                        .requiredDistanceInterval(1)
                        .acceptableAccuracy(5.0f)
                        .acceptableTimePeriod(1 * 1000)
                        .gpsMessage("Turn on GPS?")
                        .setWaitPeriod(ProviderType.GPS, 1 * 1000)
                        .setWaitPeriod(ProviderType.NETWORK, 1 * 1000)
                        .build())
                .build();

        return awesomeConfiguration;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (!isLocationRequested) {
            isLocationRequested = true;
            getLocation();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("onCreate", "onCreateBefore");
        Paper.book().write("startDistanceCount", true);
        new MySharedPreference(getApplicationContext()).setBooleanShared("startDistanceCount", true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        }
    }

    private void startMyOwnForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String NOTIFICATION_CHANNEL_ID = "channel_id_11";
            String channelName = "My Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setSound(null, null);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,
                    NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("App is running in background")
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(11, notification);
        }
    }

    @Override
    public void onDestroy() {
        Log.e("onDestroy", "onDestroyBeforeTawklna");
        super.onDestroy();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startMyOwnForeground();
                stopForeground(true);
                stopSelf();
            }
        } catch (Exception ex) {
        }
    }

    public void calculations(double new_latitude, double new_longitude, long newTime) {
        try {
            long oldTime = Paper.book().read("oldTime", 0L);
            double difference_sec = (newTime - oldTime) / 1000;
            Double oldLatitude = Paper.book().read("oldLatitude", new_latitude);
            Double oldLongitude = Paper.book().read("oldLongitude", new_longitude);

            double newDistance = meterDistanceBetweenPoints(oldLatitude, oldLongitude, new_latitude, new_longitude);
            /*if (Double.isNaN(newDistance) || newDistance <=0) {
                System.out.println("shtayyat ->Double.isNaN " + oldTime);
                Paper.book().write("oldTime", newTime);
                return;
            }*/
            double speed = difference_sec != 0 ? ((newDistance / 1000) * 3600) / difference_sec : 0;

            if (Double.isNaN(newDistance) || newDistance <=0 || newDistance >= 500) {  // error accured in gps -- distance is way too big
                Paper.book().write("totalTimeNormal", difference_sec + Paper.book().read("totalTimeNormal", Double.parseDouble("0")));
                Integer currentFails = Paper.book().read("inaccurate_locations_count", 0);
                if (currentFails > 10) {
                    Paper.book().write("oldLatitude", new_latitude);
                    Paper.book().write("oldLongitude", new_longitude);
                    Paper.book().write("inaccurate_locations_count", 0);
                } else {
                    Paper.book().write("inaccurate_locations_count", ++currentFails);
                }
            } else {
                Paper.book().write("inaccurate_locations_count", 0);
                Paper.book().write("oldLatitude", new_latitude);
                Paper.book().write("oldLongitude", new_longitude);
                Paper.book().write("oldTime", newTime);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                String currentDateandTime = sdf.format(new Date());

                try {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference coords_c = mDatabase.child("order_info").child(new MySharedPreference(getApplicationContext()).
                            getStringShared("cur_order_id")).child("coords_c").push();
                    coords_c.setValue(new_latitude+","+new_longitude);

                    DatabaseReference array_time = mDatabase.child("order_info").child(new MySharedPreference(getApplicationContext()).
                            getStringShared("cur_order_id")).child("array_time").push();
                    array_time.setValue(currentDateandTime);

                    DatabaseReference array_sec = mDatabase.child("order_info").child(new MySharedPreference(getApplicationContext()).
                            getStringShared("cur_order_id")).child("array_sec").push();
                    array_sec.setValue(difference_sec);
                } catch ( Exception er) {
                    er.printStackTrace();
                }

                ArrayList<FollowCooridnates> followCoordinates = Paper.book().read("followCoordinates", new ArrayList<>());

                followCoordinates.add(new FollowCooridnates(
                        new_latitude,
                        new_longitude,
                        currentDateandTime,
                        difference_sec
                ));
                Paper.book().write("followCoordinates", followCoordinates);
                Paper.book().write("totalDistance", newDistance + Paper.book().read("totalDistance", Double.parseDouble("0")));
                if (newDistance / difference_sec >= 5.0)
                    Paper.book().write("totalTimeNormal", difference_sec + Paper.book().read("totalTimeNormal", Double.parseDouble("0")));
                else
                    Paper.book().write("totalTimeSlow", difference_sec + Paper.book().read("totalTimeSlow", Double.parseDouble("0")));
              }
        } catch (Exception ex) {
            ex.printStackTrace();
            Crashlytics.logException(ex);
        }
    }


    private double meterDistanceBetweenPoints(double lat_a, double lng_a, double lat_b, double lng_b) {
        float pk = (float) (180.f / Math.PI);
        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;
        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        float tt = (float) Math.acos(t1 + t2 + t3);
        return 6366000 * tt;
    }

}
