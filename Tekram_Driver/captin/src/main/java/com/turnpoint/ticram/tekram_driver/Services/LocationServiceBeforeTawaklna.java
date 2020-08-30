package com.turnpoint.ticram.tekram_driver.Services;

import android.app.*;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.location.Location;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.location.LocationRequest;
import com.google.firebase.database.*;
import com.google.gson.JsonObject;
import com.turnpoint.ticram.tekram_driver.Activites.MapsMain;
import com.turnpoint.ticram.tekram_driver.Activites.ShowNotficationLoggedOut;
import com.turnpoint.ticram.tekram_driver.DBHelper2;
import com.turnpoint.ticram.tekram_driver.MySharedPreference;
import com.turnpoint.ticram.tekram_driver.PathUrl;
import com.turnpoint.ticram.tekram_driver.R;
import com.turnpoint.ticram.tekram_driver.Volley.IResult;
import com.turnpoint.ticram.tekram_driver.Volley.VolleyService;
import com.turnpoint.ticram.tekram_driver.modules.FollowCooridnates;
import com.turnpoint.ticram.tekram_driver.modules.addOrderFirebase;
import com.turnpoint.ticram.tekram_driver.modules.addcoordsfirebase;
import com.yayandroid.locationmanager.base.LocationBaseService;
import com.yayandroid.locationmanager.configuration.DefaultProviderConfiguration;
import com.yayandroid.locationmanager.configuration.GooglePlayServicesConfiguration;
import com.yayandroid.locationmanager.configuration.LocationConfiguration;
import com.yayandroid.locationmanager.constants.ProcessType;
import com.yayandroid.locationmanager.constants.ProviderType;
import io.paperdb.Paper;

import java.text.SimpleDateFormat;
import java.util.*;


public class LocationServiceBeforeTawaklna extends LocationBaseService {
    private boolean isLocationRequested = false;
    private static final String TAG = "LocServiceBeforeTawklna";
    DBHelper2 db;
    Context mContext  = MapsMain.context;

    @Override
    public void onLocationChanged(Location location) {
        if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
            long newTime = System.currentTimeMillis();
            long oldTime = Paper.book().read("lastCall", 0L);
            double difference_sec = (newTime - oldTime) / 1000;
            if (difference_sec < 2 && oldTime != 0) {
                System.out.println("shtayyat -> Too much requests ( " + difference_sec + " )" + location.getLatitude() + " - " + location.getLongitude());
                return;
            }
            Paper.book().write("lastCall", newTime);
            System.out.println("shtayyat -> new location  ( " + difference_sec + " )" + location.getLatitude() + " - " + location.getLongitude());
            firebase(location.getLatitude(), location.getLongitude(), newTime);
        }
    }

    @Override
    public void onLocationFailed(int type) {
        System.out.println("shtayyat -> onLocationFailed " + type);

    }

    @Override
    public void onProcessTypeChanged(@ProcessType int processType) {
        System.out.println("shtayyat -> processType " + processType);
    }
    Boolean dataSent = false;
    private void firebase(double lat, double lon, long newTime) {
        db = new DBHelper2(mContext);
        String[][] Arr = db.getLatLongTable();

        //Seperate Address to Lat & Long and get the Id
        for (int i = 0; i < Arr.length; i++) {
            try {
                String id = Arr[i][0];
                String title = Arr[i][1];
                String Lat = Arr[i][2];
                String Lon = Arr[i][3];

                String[] ArrLon = Lon.split(",");
                String[] ArrLat = Lat.split(",");
                //Convert Array To Double

                double[] convertedVerticesYArray = arrayConverter(ArrLon);
                double[] convertedVerticesXArray = arrayConverter(ArrLat);

                // Check if Current Location inside Geo Zone

                if (IsPointInPolygonTest(ArrLat.length, convertedVerticesXArray, convertedVerticesYArray, lat, lon)) {
                    Log.d("result", "true" + i + id);
                    addcoordsfirebase values = new addcoordsfirebase(String.valueOf(lat) + "," + String.valueOf(lon), id, title);
                    //System.out.println("shtayyat_firebase -> setting " + new MySharedPreference(getApplicationContext()).getStringShared("user_id") + " location to: "+values) ;
                    dataSent = false;
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("drivers").child(new MySharedPreference(mContext).getStringShared("user_id")).child("info").setValue(values, new DatabaseReference.CompletionListener() {
                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                            if (error == null)
                                dataSent = true;
                            System.out.println("Value was set. Error = " + error);
                        }

                    });
                    break;

                } else
                    Log.d("result", "false" + i + id);


            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        if (new MySharedPreference(mContext).getBooleanShared("startDistanceCount", false) || Paper.book().read("startDistanceCount", false)) {
            calculations(lat, lon, newTime);
        }

        Paper.book().write("drLatitude", lat);
        Paper.book().write("drLongitude", lon);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("shtayyat_firebase_dataSent: " + dataSent);
                if (dataSent == false) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            mContext.startForegroundService(new Intent(mContext,
                                    LocationServiceBeforeTawaklna.class));
                        } else {
                            startService(new Intent(mContext,
                                    LocationServiceBeforeTawaklna.class));
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        }, 7500);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public LocationConfiguration getLocationConfiguration() {
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(4 * 1000);
//        locationRequest.setFastestInterval(4 * 1000);
        LocationConfiguration awesomeConfiguration = new LocationConfiguration.Builder()
                .keepTracking(true)
//                .useGooglePlayServices(new GooglePlayServicesConfiguration.Builder()
//                        .fallbackToDefault(true)
//                        .askForGooglePlayServices(false)
//                        .askForSettingsApi(false)
//                        .failOnConnectionSuspended(false)
//                        .failOnSettingsApiSuspended(false)
//                        .ignoreLastKnowLocation(false)
//                        .setWaitPeriod(4 * 1000)
//                        .locationRequest(locationRequest)
//                        .build())
                .useDefaultProviders(new DefaultProviderConfiguration.Builder()
                        .requiredTimeInterval(4 * 1000)
                        .requiredDistanceInterval(1)
                        .acceptableAccuracy(5.0f)
                        .acceptableTimePeriod(4 * 1000)
                        .gpsMessage("Turn on GPS?")
                        .setWaitPeriod(ProviderType.GPS, 4 * 1000)
                        .setWaitPeriod(ProviderType.NETWORK, 4 * 1000)
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
        tracking_firebase();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("onCreate", "onCreateBefore");

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
            System.out.println("shtayyat1 ->Non Double.isNaN ");
            double speed = difference_sec != 0 ? ((newDistance / 1000) * 3600) / difference_sec : 0;

            System.out.println("shtayyat ->speed (newDistance): " + newDistance);
            System.out.println("shtayyat ->speed T (new_time): " + difference_sec);
            System.out.println("shtayyat ->speed:T " + speed);

            if (Double.isNaN(newDistance) || newDistance <=0 || newDistance >= 500) {  // error accured in gps -- distance is way too big
                System.out.println("shtayyat1 ->speed >= 200 ");
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
                System.out.println("shtayyat ->oldTime " + oldTime);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                String currentDateandTime = sdf.format(new Date());


                try {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    System.out.println("shtayyatfirebase ->adding to firebase LocationServiceBeforeTawaklna");
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
                System.out.println("shtayyat1 ->new  totalDistance" + (newDistance + Paper.book().read("totalDistance", Double.parseDouble("0"))));
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
        double pk = (double) (180.f / Math.PI);
        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;
        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = (double) Math.acos(t1 + t2 + t3);
        return 6366000 * tt;
    }
    DatabaseReference connectedRef;
    FirebaseDatabase database;
    private ValueEventListener mListener;
    public void tracking_firebase(){
        database = FirebaseDatabase.getInstance();
        System.out.println("shtayyatW=> onChildAdded "+new MySharedPreference(getApplicationContext()).
                getStringShared("user_id") +" - ");

        FirebaseDatabase.getInstance()
                .getReference()
                .child("orders")
                .child(new MySharedPreference(getApplicationContext()).
                        getStringShared("user_id")).
                addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        if(dataSnapshot.exists()) {
                            addOrderFirebase values = dataSnapshot.getValue(addOrderFirebase.class);
                            System.out.println("shtayyatW=> onChildAdded "+new MySharedPreference(getApplicationContext()).
                                    getStringShared("user_id") +" - "+  values.id + " - "+values.status);
                            if(values.status.equals("W")){
                                handleNewOrder(values);
                            }
                        }

                    }
                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        if(dataSnapshot.exists()) {
                            addOrderFirebase values = dataSnapshot.getValue(addOrderFirebase.class);
                            System.out.println("shtayyatW=> onChildChanged "+new MySharedPreference(getApplicationContext()).
                                    getStringShared("user_id") +" - "+  values.id + " - "+values.status);
                            if(values.status.equals("W")){
                                handleNewOrder(values);
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


    }
    private void handleNewOrder(addOrderFirebase values){
        System.out.println("shtayyatW=> handleNewOrder isMyServiceRunning" + isMyServiceRunning(WidgetNewOrder.class));
        if(isMyServiceRunning(WidgetNewOrder.class) == true && !Paper.book().read("lastAccept", "").equalsIgnoreCase(""+ values.id)) return;
        addNotification("لديك طلب جديد");
        try {
            Intent serviceIntent = new Intent(getApplicationContext(), WidgetNewOrder.class);
            serviceIntent.putExtra("order_id",""+ values.id);
            serviceIntent.putExtra("time_to_user", values.time_to_user);
            serviceIntent.putExtra("distance_to_user", values.distance_to_user);
            serviceIntent.putExtra("user_name", values.user_name);
            serviceIntent.putExtra("user_photo", "");
            serviceIntent.putExtra("user_rate",""+ values.user_rate);
            serviceIntent.putExtra("location_text", values.location_text);
            serviceIntent.putExtra( "destination_text", values.destination_text);
            serviceIntent.putExtra("order_info", values.order_info);
            serviceIntent.putExtra("taxi", ""+values.taxi);
            serviceIntent.putExtra("order_count_down", ""+values.order_count_down);
            startService(serviceIntent);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        try {
            Map<String, String> params = new Hashtable<String, String>();
            params.put("local", "ara");
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("order_id", "" + values.id);
            VolleyService voly_ser = new VolleyService(new IResult() {
                @Override
                public void notifySuccessPost(String response) {
                    System.out.println(response);
                }

                @Override
                public void notifyError(VolleyError error) {
                    System.out.println(error.getMessage());
                }
            }, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url") + PathUrl.receivedNotification, params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private void addNotification(String title) {

        String idChannel = "channel_notification";
        NotificationChannel mChannel = null;
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationManager mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, MapsMain.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


       /* Intent notificationIntentOpen = new Intent(this, MyOrders.class);
        notificationIntentOpen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent_open = PendingIntent.getActivity(this, 0,
                notificationIntentOpen, PendingIntent.FLAG_ONE_SHOT);*/

        // Uri notf_sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Ticram")
                        .setContentText(title)
                        .setAutoCancel(true)
                        //.addAction(R.drawable.ic_open, "Open", contentIntent_open)
                        .setContentIntent(contentIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName()
                    + "/" + R.raw.in_a_hurryyy);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            mChannel = new NotificationChannel(idChannel, getApplicationContext().
                    getString(R.string.app_name), importance);
            mChannel.setSound(sound, attributes);
            mNotificationManager.createNotificationChannel(mChannel);
            //playRingtone();
        } else {
            builder.setSound(Uri.parse("android.resource://"
                    + getApplicationContext().getPackageName() + "/" + R.raw.in_a_hurryyy));

        }

        builder.setChannelId(idChannel);
        mNotificationManager.notify(0, builder.build());
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addNotificationNew(String title) {

        String idChannel = "channel_notification";
        NotificationChannel mChannel = null;
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        new MySharedPreference(getApplicationContext()).setStringShared("text_notfi", title);
        Intent notificationIntent = new Intent(this, ShowNotficationLoggedOut.class);
        notificationIntent.putExtra("text", title);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri notf_sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Ticram")
                        .setContentText(title)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName()
                    + "/" + R.raw.in_a_hurryyy);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            mChannel = new NotificationChannel(idChannel, getApplicationContext().
                    getString(R.string.app_name), importance);
            mChannel.setSound(sound, attributes);
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            builder.setSound(Uri.parse("android.resource://"
                    + getApplicationContext().getPackageName() + "/" + R.raw.in_a_hurryyy));
        }

        builder.setChannelId(idChannel);
        mNotificationManager.notify(0, builder.build());
    }
    //To Converte Array of Address to Double
    public final double[] arrayConverter(String[] arrayToConvert) {
        double[] convertedArray = new double[arrayToConvert.length];
        for (int i = 0; i < arrayToConvert.length; i++) {
            convertedArray[i] = Double.parseDouble(arrayToConvert[i]);
        }

        return convertedArray;
    }
    //To check if the site is within any geoZones
    public final boolean IsPointInPolygonTest(int count, double[] verticesXArray, double[] verticesYArray, Double currentLat, Double currentLng) {
        boolean validater = false;

        int loopCounter = count - 1;

        for (int counter = 0; counter < count; counter++) {
            if (verticesYArray[counter] < currentLng && verticesYArray[loopCounter] >= currentLng || verticesYArray[loopCounter] < currentLng && verticesYArray[counter] >= currentLng) {
                if (verticesXArray[counter] + (currentLng - verticesYArray[counter]) / (verticesYArray[loopCounter] - verticesYArray[counter]) * (verticesXArray[loopCounter] - verticesXArray[counter]) < currentLat) {
                    validater = true;
                }
            }

            loopCounter = counter;
        }

        return validater;
    }

}