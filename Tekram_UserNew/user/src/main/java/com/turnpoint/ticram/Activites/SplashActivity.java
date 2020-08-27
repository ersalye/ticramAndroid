package com.turnpoint.ticram.Activites;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.turnpoint.ticram.BuildConfig;
import com.turnpoint.ticram.CheckIntenetConn;
import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.PathUrl;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.Services.SingleShotLocationProvider;
import com.turnpoint.ticram.Volley.IResult;
import com.turnpoint.ticram.Volley.VolleyService;
import com.turnpoint.ticram.modules.user_info_splash;
import com.turnpoint.ticram.modules.user_info_splash2;
import com.turnpoint.ticram.modules.user_info_splash3;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.yayandroid.locationmanager.base.LocationBaseActivity;
import com.yayandroid.locationmanager.configuration.DefaultProviderConfiguration;
import com.yayandroid.locationmanager.configuration.GooglePlayServicesConfiguration;
import com.yayandroid.locationmanager.configuration.LocationConfiguration;
import com.yayandroid.locationmanager.constants.ProviderType;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class SplashActivity extends LocationBaseActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public ProgressDialog loading;
    IResult iresult;
    VolleyService voly_ser;
    public static final int REQUEST_LOCATION = 001;
    GoogleApiClient googleApiClient;
    LocationManager locationManager;
    LocationRequest locationRequest;
    LocationSettingsRequest.Builder locationSettingsRequest;
    PendingResult<LocationSettingsResult> pendingResult;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    FirebaseRemoteConfigSettings configSettings;

    long cacheExpiration = 43200;
    String BaseUrl = "";
    boolean ckeckVersion = true;

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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkUpdate();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Al-Jazeera-Arabic-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_splash);


        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1200)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        RelativeLayout relativelayout = findViewById(R.id.splash_lay);
        relativelayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        new MySharedPreference(getApplicationContext()).setStringShared("from_w_status", "normal"); //  normal - MyOrderDetails - splash
        new MySharedPreference(getApplicationContext()).setStringShared("from_D_A_S_status", "normal");
        new MySharedPreference(getApplicationContext()).setStringShared("PaymentMethod", "CASH");
        callBackVolly();


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
                    delay();
                } else {
                    mEnableGps();
                }
            }
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                delay();
            } else {
                mEnableGps();
            }

        }


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
        builder.setMessage(R.string.check_internet)
                .setCancelable(false)
                .setPositiveButton(R.string.settings,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                startActivity(i);
                            }
                        }
                )
                .setNegativeButton(R.string.cancel,
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                       /* if (ActivityCompat.checkSelfPermission(SplashActivity.this,
                                Manifest.permission.RECORD_AUDIO)
                                != PackageManager.PERMISSION_GRANTED ) {
                            ActivityCompat.requestPermissions(SplashActivity.this,
                                    new String[]{Manifest.permission.RECORD_AUDIO},
                                    1);*/
                        delay();

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

            /*case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(SplashActivity.this,
                            Manifest.permission.RECEIVE_SMS)
                            != PackageManager.PERMISSION_GRANTED ) {

                        ActivityCompat.requestPermissions(SplashActivity.this,
                                new String[]{Manifest.permission.RECEIVE_SMS},
                                2);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    ActivityCompat.requestPermissions(SplashActivity.this,
                            new String[]{Manifest.permission.RECEIVE_SMS},
                            2);
                }
                return;
            }*/

           /* case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    delay();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    ActivityCompat.requestPermissions(SplashActivity.this,
                            new String[]{Manifest.permission.RECEIVE_SMS},
                            2);
                }
                return;
            }*/

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    public void delay() {
        fetchUrlBase(SplashActivity.this);
        getLocation();
    }


    public void Volley_go() {
        try {
            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(SplashActivity.this).getStringShared("access_token"));
            params.put("local", new GetCurrentLanguagePhone().getLang());
            params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            voly_ser = new VolleyService(iresult, SplashActivity.this);
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url") + PathUrl.USERInfo, params);

/*
          Log.d("ressssss", new MySharedPreference(SplashActivity.this).getStringShared("access_token")
                  +"  --  "+ new GetCurrentLanguagePhone().getLang() +" ---- "+
                  new MySharedPreference(getApplicationContext()).getStringShared("user_id"));*/
        } catch (Exception ex) {
        }
    }


    public void callBackVolly() {
        iresult = new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                try {
                    Log.d("responseee", response);
                    Gson gson = new Gson();
                    user_info_splash res = gson.fromJson(response, user_info_splash.class);
                    if (res.getHandle().equals("02")) {  // account not found
                        Toast.makeText(SplashActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        if (ckeckVersion) {
                            Intent intent = new Intent(SplashActivity.this, LoginPhoneNumber.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        }
                    } else if (res.getHandle().equals("10")) {
                        //Toast.makeText(SplashActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();

                        user_info_splash2 user_info1 = res.getUser();
                        // new Parse_mainCats().setCatalog(user_info1.getTypes());
                        user_info_splash3 userInfo3 = user_info1.getOrder();

                        new MySharedPreference(getApplicationContext()).setStringShared("user_name", user_info1.getName());
                        new MySharedPreference(getApplicationContext()).setStringShared("photo", user_info1.getPhoto());
                        new MySharedPreference(getApplicationContext()).setStringShared("access_token", user_info1.getAccessToken());
                        new MySharedPreference(getApplicationContext()).setStringShared("rate", user_info1.getRate());
                        new MySharedPreference(getApplicationContext()).setStringShared("balance", user_info1.getBalance());
                        String num = user_info1.getMob();
                        String sub_num = num.substring(3);
                        //Toast.makeText(CheckCode.this,sub_num , Toast.LENGTH_LONG).show();
                        new MySharedPreference(getApplicationContext()).setStringShared("mobile_full", num);
                        new MySharedPreference(getApplicationContext()).setStringShared("mobile", sub_num);
                        new MySharedPreference(getApplicationContext()).setStringShared("email", user_info1.getEmail());
                        new MySharedPreference(getApplicationContext()).setStringShared("gender", user_info1.getGender());
                        new MySharedPreference(getApplicationContext()).setIntShared("time_to_cancel_in_sec",
                                userInfo3.getTime_to_cancel_in_sec());
                        //Toast.makeText(SplashActivity.this, String.valueOf(userInfo3.getTime_to_cancel_in_sec()), Toast.LENGTH_LONG).show();

                        if (!user_info1.getOrderStatus().equals("C") &&
                                !user_info1.getOrderStatus().equals("N") &&
                                !user_info1.getOrderStatus().equals("")) {
                            //Toast.makeText(SplashActivity.this,user_info1.getOrderStatus(), Toast.LENGTH_LONG).show();

                            if (user_info1.getOrderStatus().equals("W")) {  // waiting
                                new MySharedPreference(getApplicationContext()).setStringShared("order_id", String.valueOf(user_info1.getOrderId()));
                                new MySharedPreference(getApplicationContext()).setStringShared("from_w_status", "splash");
                                user_info_splash3 user_info2 = user_info1.getOrder();
                                if (ckeckVersion) {
                                    Intent i = new Intent(getApplicationContext(), MapActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                                    if (!user_info2.getToLocation().equals("") &&
                                            user_info2.getToLocation() != null) {
                                        i.putExtra("tolocation", user_info2.getToLocation());
                                        i.putExtra("destination_exist", "yes");
                                    }
                                    if (user_info2.getToLocation().equals("") ||
                                            user_info2.getToLocation() == null) {
                                        i.putExtra("destination_exist", "no");
                                    }
                                    i.putExtra("location", user_info2.getLocation());
                                    i.putExtra("subtype", user_info2.getSubtype());
                                    i.putExtra("tmp_distance", user_info2.getDistance());
                                    i.putExtra("tmp_time", user_info2.getTime());
                                    startActivity(i);
                                    finish();
                                }
                            }


                            if (user_info1.getOrderStatus().equals("D")) {  // accepted
                                new MySharedPreference(getApplicationContext()).setStringShared("from_D_A_S_status", "splash");
                                new MySharedPreference(getApplicationContext()).setStringShared("order_id", String.valueOf(user_info1.getOrderId()));
                                new MySharedPreference(getApplicationContext()).setStringShared("order_status_splash", String.valueOf(user_info1.getOrderStatus()));
                                if (ckeckVersion) {
                                    Intent intent = new Intent(SplashActivity.this, TripDetails.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            if (user_info1.getOrderStatus().equals("A")) {   // arrived
                                new MySharedPreference(getApplicationContext()).setStringShared("from_D_A_S_status", "splash");
                                new MySharedPreference(getApplicationContext()).setStringShared("order_id", String.valueOf(user_info1.getOrderId()));
                                new MySharedPreference(getApplicationContext()).setStringShared("order_status_splash", String.valueOf(user_info1.getOrderStatus()));
                                if (ckeckVersion) {
                                    Intent intent = new Intent(SplashActivity.this, TripDetails.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            if (user_info1.getOrderStatus().equals("S")) {   // start
                                new MySharedPreference(getApplicationContext()).setStringShared("from_D_A_S_status", "splash");
                                new MySharedPreference(getApplicationContext()).setStringShared("order_id", String.valueOf(user_info1.getOrderId()));
                                new MySharedPreference(getApplicationContext()).setStringShared("order_status_splash", String.valueOf(user_info1.getOrderStatus()));
                                Intent intent = new Intent(SplashActivity.this, TripDetails.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                finish();
                            }
                            if (user_info1.getOrderStatus().equals("E") || user_info1.getOrderStatus().equals("F")) {

                                // user_info_splash3 u= user_info1.getOrder();
                                // Toast.makeText(SplashActivity.this,userInfo3.getRated(), Toast.LENGTH_LONG).show();

                                if (userInfo3.getRated().equals("0")) {  // not rated
                                    new MySharedPreference(getApplicationContext()).setStringShared("order_id",
                                            String.valueOf(user_info1.getOrderId()));
                                    if (ckeckVersion) {
                                        Intent intent = new Intent(SplashActivity.this, PaymentAndReview.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else if (userInfo3.getRated().equals("1")) {// rated
                                    if (ckeckVersion) {
                                        Intent intent = new Intent(SplashActivity.this, MapActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                        } else if (user_info1.getOrderStatus().equals("C") ||
                                user_info1.getOrderStatus().equals("N") ||
                                user_info1.getOrderStatus().equals("") ||
                                user_info1.getOrderStatus() == null) {
                            if (ckeckVersion) {
                                Intent intent = new Intent(SplashActivity.this, MapActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                    updateVersion = Double.parseDouble(res.getUser().getAndroid_version());
                } catch (Exception ex) {

                }

            }

            @Override
            public void notifyError(VolleyError error) {
                //  loading.dismiss();
                //Log.d("response_error",String.valueOf(error.getMessage().toString()) );
                Toast.makeText(SplashActivity.this, R.string.check_internet, Toast.LENGTH_LONG).show();
                // Toast.makeText(SplashActivity.this,"Volley Error"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();


            }
        };
    }


    static Double updateVersion = 0.0;


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
        super.onActivityResult(requestCode, resultCode, data);
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        delay();
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
                        //new MySharedPreference(getApplicationContext()).setStringShared("base_url", "https://new.faistec.com/");
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

    public static Location currentLocation;

    @Override
    public void onLocationChanged(Location location) {
        if (location == null || (location.getLatitude() + location.getLongitude() == 0))
            return;
        currentLocation = location;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String login_status = new MySharedPreference(getApplicationContext()).getStringShared("login_status");
                if (login_status.contains("login")) {
                    Volley_go();

                } else {
                    if (ckeckVersion) {
                        Intent intent = new Intent(SplashActivity.this, LoginPhoneNumber.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        }, 1000);

    }

    @Override
    public void onLocationFailed(int type) {

    }
    private void checkUpdate() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("version_code").child("user_code");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String code = snapshot.getValue(String.class);
                int user_code = Integer.parseInt(code);

                if (snapshot.exists()) {
                    if (BuildConfig.VERSION_CODE < user_code) {
                        ckeckVersion = false;
                        showForceUpdateDialog();
                    } else {
                        ckeckVersion = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void showForceUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.alerte_force_update_title));
        builder.setMessage(getResources().getString(R.string.alerte_force_update_message));
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.update), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }


            }
        });

        builder.show();
    }
}


