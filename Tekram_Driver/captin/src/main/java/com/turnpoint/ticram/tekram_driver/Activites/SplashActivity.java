package com.turnpoint.ticram.tekram_driver.Activites;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.turnpoint.ticram.tekram_driver.BuildConfig;
import com.turnpoint.ticram.tekram_driver.CheckIntenetConn;
import com.turnpoint.ticram.tekram_driver.DBHelper2;
import com.turnpoint.ticram.tekram_driver.MySharedPreference;
import com.turnpoint.ticram.tekram_driver.R;
import com.turnpoint.ticram.tekram_driver.Volley.IResult;
import com.turnpoint.ticram.tekram_driver.Volley.VolleyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    IResult iresult;
    VolleyService voly_ser;
    public ProgressDialog loading;
    public static Context context;
    // ticram
    public static final int REQUEST_LOCATION = 001;
    GoogleApiClient googleApiClient;
    LocationManager locationManager;
    LocationRequest locationRequest;
    LocationSettingsRequest.Builder locationSettingsRequest;
    PendingResult<LocationSettingsResult> pendingResult;
    AlertDialog.Builder builder;

    FirebaseRemoteConfig mFirebaseRemoteConfig;
    FirebaseRemoteConfigSettings configSettings;
    long cacheExpiration = 43200;
    String BaseUrl = "";
    boolean checkVersion = true;
    boolean checkMockGPS = true;
    DBHelper2 db = new DBHelper2(this);

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = SplashActivity.this;
        InsertTOLocalDB();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Al-Jazeera-Arabic-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        setContentView(R.layout.activity_splash);
        checkUpdate();
        checkMockGPS();

//        checkData();//check update for clear cach
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1200)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(SplashActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(SplashActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        200);

            } else if (ActivityCompat.checkSelfPermission(SplashActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(SplashActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    delay_splash();
                } else {
                    mEnableGps();
                }
            }
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                delay_splash();
            } else {
                mEnableGps();
            }

        }


       /* if(getIntent().getExtras() !=null){
            String order_id=getIntent().getExtras().getString("id");
            Toast.makeText(getApplicationContext(), order_id, Toast.LENGTH_SHORT).show();
        }

        if(getIntent().getExtras() ==null){
            Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
        }*/

        new MySharedPreference(getApplicationContext()).setStringShared("base_url", "http://new.faistec.com/");
    }

//    private void checkData() {
//        String driver_id = new MySharedPreference(getApplicationContext()).getStringShared("user_id");
//        if (driver_id != null) {
//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("update").child(driver_id);
//            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    String status = snapshot.child("status").getValue(String.class);
//                    if (status != null) {
//                        if (status.equalsIgnoreCase("on")) {
//                            checkVersion = false;
//                            showClearDataDialog(driver_id);
//                        }
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//        }
//
//    }

    @Override
    public void onStop() {
        super.onStop();
        if (builder != null) {
            builder.create().dismiss();
            builder = null;
        }
    }

    private void checkUpdate() {
        int version_code = BuildConfig.VERSION_CODE;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("version_code").child("driver_code");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int code = Integer.parseInt(snapshot.getValue(String.class));
                Log.d("user_code_rahaf =", code + "");
                Log.d("version_code_rahaf", BuildConfig.VERSION_CODE + "");
                if (snapshot.exists()) {
                    if (code > version_code) {
                        checkVersion = false;
                        showForceUpdateDialog();
                    } else {
                        checkVersion = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
//    private void showClearDataDialog(String id) {
//        builder = new AlertDialog.Builder(this);
//        builder.create().dismiss();
//
//        builder.setMessage(getResources().getString(R.string.clear_data));
//        builder.setCancelable(false);
//        builder.setPositiveButton(getResources().getString(R.string.done), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                FirebaseDatabase.getInstance().getReference("update").child(id).child("status").setValue("off").addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
//                            ((ActivityManager) context.getSystemService(ACTIVITY_SERVICE))
//                                    .clearApplicationUserData();
//                        }
//                    }
//                });
//            }
//        });
//        builder.create().show();
//    }

    private void showForceUpdateDialog() {
        builder = new AlertDialog.Builder(this);
        builder.create().dismiss();
        builder.setTitle(getResources().getString(R.string.update_new));
        builder.setMessage(getResources().getString(R.string.update_message));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    finishAffinity();

                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    finishAffinity();
                }
            }
        });
        builder.create().show();
    }


    private void checkMockGPS() {
        List<String> appFakeFPS = getListOfFakeLocationApps(context);
        if(appFakeFPS != null){
            if (appFakeFPS.size() > 0){
                checkMockGPS = false;
                String packageNameApp = " ";
                for (String nameApp : appFakeFPS){
                    packageNameApp = packageNameApp + nameApp + "   ";
                }
                showMockGPSDialog(packageNameApp);
                String id = new MySharedPreference(getApplicationContext()).getStringShared("user_id");
                if(! id.equals("")){
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("FakeGPS")
                            .child(new MySharedPreference( getApplicationContext()).getStringShared("user_id"))
                            .setValue("1");
                }else {
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("FakeGPS")
                            .child(getRandomDoubleBetweenRange(1000000, 2000000) + "")
                            .setValue("0");
                }
            }
        }
    }

    public static int getRandomDoubleBetweenRange(int min, int max){
        int x = (int) ((Math.random()*((max-min)+1))+min);
        return x;
    }

    public static List<String> getListOfFakeLocationApps(Context context) {
        List<String> apps = getApps(context);
        List<String> fakeApps = new ArrayList<>();
        for (String app : apps) {
            if(!isSystemPackage(context, app) && hasAppPermission(context, app, "android.permission.ACCESS_MOCK_LOCATION")){
                fakeApps.add(getApplicationName(context, app));
            }
        }
        return fakeApps;
    }

    public static List<String> getApps(Context context) {
        List<String> apps = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo pack : packages){
            apps.add(pack.packageName);
        }
        return apps;
    }

    public static boolean isSystemPackage(Context context, String app){
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo pkgInfo = packageManager.getPackageInfo(app, 0);
            return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasAppPermission(Context context, String app, String permission){
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(app, PackageManager.GET_PERMISSIONS);
            if(packageInfo.requestedPermissions!= null){
                for (String requestedPermission : packageInfo.requestedPermissions) {
                    if (requestedPermission.equals(permission)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getApplicationName(Context context, String packageName) {
        String appName = packageName;
        PackageManager packageManager = context.getPackageManager();
        try {
            appName = packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appName;
    }

    private void showMockGPSDialog(String packageNameApp) {
        builder = new AlertDialog.Builder(this);
        builder.create().dismiss();
        builder.setTitle(getResources().getString(R.string.fake_gps));
        builder.setMessage(getResources().getString(R.string.fake_gps_message) + packageNameApp);
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        });
        builder.create().show();
    }

    public void InsertTOLocalDB() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest("http://new.faistec.com/api/api/get_geozones", new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                Log.d("TAGw", response.toString());


                // 1- Delete Content Of Table
                db.deleteLatlongTable();

                // 2- Fill The Table
                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Integer id_Zones = obj.getInt("id");
                        String title_geoZone = obj.getString("title");
                        String Lat = obj.getString("vertices_x");
                        String Lon = obj.getString("vertices_y");

                        db.insertToLatLongTable(id_Zones, title_geoZone, Lat, Lon);


                    } catch (JSONException e) {
                        Toast.makeText(SplashActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("Insert Error", "Error: " + error.getMessage());
                // hidePDialog();

            }
        });
        // Adding request to request queue
        requestQueue.add(request);
    }


    @Override
    public void onResume() {
        super.onResume();
        boolean check_internet = new CheckIntenetConn(getApplicationContext()).isNetworkAvailable();
        if (check_internet) {
        } else if (check_internet == false) {
            createNetErrorDialog();
        }

    }

    protected void createNetErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("يتطلب الدحول للتطبيق وجود انترنت .قم بتفعيل الانترنت.")
                .setTitle("مشكلة بالاتصال بالانترنت!")
                .setCancelable(false)
                .setPositiveButton("الاعدادات",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                if (checkVersion && checkMockGPS)
                                    startActivity(i);
                            }
                        }
                )
                .setNegativeButton("الغاء",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        }
                );
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        delay_splash();
                    } else {
                        mEnableGps();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    ActivityCompat.requestPermissions(SplashActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            200);

                }
                return;
            }


        }
    }


    public void delay_splash() {
        fetchUrlBase(SplashActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String login_status = new MySharedPreference(getApplicationContext()).getStringShared("login_status");
                if (login_status.contains("login")) {
                    Intent intent = new Intent(SplashActivity.this, MapsMain.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    if (checkVersion && checkMockGPS) {
                        startActivity(intent);
                        finish();
                    }

                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (checkVersion && checkMockGPS) {
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }, 1000);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    //===================================enable gps =================================================


    public void mEnableGps() {
        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
        mLocationSetting();
    }


    public void mLocationSetting() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1 * 1000);
        locationRequest.setFastestInterval(1 * 1000);

        locationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        mResult();

    }


    public void mResult() {
        pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest.build());
        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();


                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {

                            status.startResolutionForResult(SplashActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.


                        break;
                }
            }

        });
    }


    //callback method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);


        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        delay_splash();
                        break;
                    case Activity.RESULT_CANCELED:
                        mEnableGps();
                        break;
                    default:
                        break;
                }

                break;

        }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public String fetchUrlBase(Context context) {
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        BaseUrl = mFirebaseRemoteConfig.getString("base_url");
                        new MySharedPreference(getApplicationContext()).setStringShared("base_url", BaseUrl);
                        //new MySharedPreference(getApplicationContext()).setStringShared("base_url", "https://new2.faistec.com/");
                    }
                });
        return BaseUrl;
    }

    public long getCacheExpiration() {
// If is developer mode, cache expiration set to 0, in order to test
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 60;
        }
        return cacheExpiration;
    }


/*
    public class FirebaseConfig {
        FirebaseRemoteConfig mFirebaseRemoteConfig;
        FirebaseRemoteConfigSettings configSettings;
        long cacheExpiration = 43200;
        String imgUrl = "";
        public FirebaseConfig() {
            mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
                    .build();
            mFirebaseRemoteConfig.setConfigSettings(configSettings);
            mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        }

        public String fetchNewImage(Context context){
            mFirebaseRemoteConfig.fetch(getCacheExpiration())
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        // If is successful, activated fetched
                            if (task.isSuccessful()) {
                                mFirebaseRemoteConfig.activateFetched();
                            } else {
                                Log.d("","");
                            }
                            imgUrl = mFirebaseRemoteConfig.getString("base_url");
                        }
                    });
            return imgUrl;
        }

        public long getCacheExpiration() {
// If is developer mode, cache expiration set to 0, in order to test
            if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
                cacheExpiration = 0;
            }
            return cacheExpiration;
        }

    }*/


}
