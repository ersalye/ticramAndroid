package com.turnpoint.ticram.tekram_driver.Activites;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.turnpoint.ticram.tekram_driver.Adapters.Adapter_ViewMyNotification;
import com.turnpoint.ticram.tekram_driver.MySharedPreference;
import com.turnpoint.ticram.tekram_driver.PathUrl;
import com.turnpoint.ticram.tekram_driver.R;
import com.turnpoint.ticram.tekram_driver.Volley.IResult;
import com.turnpoint.ticram.tekram_driver.Volley.VolleyService;
import com.turnpoint.ticram.tekram_driver.modules.HistoryNotifications;
import com.turnpoint.ticram.tekram_driver.modules.SingleNotification;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyNotification extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String TAG = MapsMain.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    double currentLatitude = 0.0, currentLongitude = 0.0;

    IResult iresult;
    VolleyService voly_ser;
    public ProgressDialog loading;
    private Adapter_ViewMyNotification mAdapter;
    RecyclerView mRecyclerView;

    ImageView imageViewBack;
    TextView textView_no_notificationLabel;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Al-Jazeera-Arabic-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_notification);
        callBackVolly();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        mRecyclerView = findViewById(R.id.recycle_notification);
        mAdapter = new Adapter_ViewMyNotification(this, new ArrayList<SingleNotification>(0));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);

        textView_no_notificationLabel = findViewById(R.id.textView_no_notificationLabel);
        imageViewBack = findViewById(R.id.imageViewBack);

    }


    public void save_changes(View view) {
    }

    public void back(View view) {
        onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        Volley_go();
    }


    public void Volley_go() {
        loading = ProgressDialog.show(MyNotification.this, "", "الرجاء الانتظار...", false, true);
        Map<String, String> params = new Hashtable<String, String>();
        String id = new MySharedPreference(getApplicationContext()).getStringShared("user_id");
        params.put("transport_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
        voly_ser = new VolleyService(iresult, getApplicationContext());
        voly_ser.postDataVolley(new MySharedPreference(getApplicationContext()).getStringShared("base_url") + PathUrl.ViewMyNotification, params);
    }


    public void callBackVolly() {
        iresult = new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                loading.dismiss();
                //Log.d("ssssssss" , response);
                // Toast.makeText(MyOrders.this, response, Toast.LENGTH_LONG).show();
                Gson gson = new Gson();
                HistoryNotifications res = gson.fromJson(response, HistoryNotifications.class);

                List<SingleNotification> list_notifications = res.getNotifications();
                mAdapter.updateAnswers(list_notifications);
                if (list_notifications == null || list_notifications.size() == 0) {
                    textView_no_notificationLabel.setVisibility(View.VISIBLE);
                } else if (list_notifications.size() > 0) {
                    textView_no_notificationLabel.setVisibility(View.GONE);
                }


            }

            @Override
            public void notifyError(VolleyError error) {
                loading.dismiss();
                Toast.makeText(MyNotification.this, " مشكلة بالاتصال بالانترنت!", Toast.LENGTH_LONG).show();

                //Toast.makeText(MyOrders.this,"Volley Error"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();


            }
        };
    }


    @Override
    protected void onPause() {
        super.onPause();
//        if (mGoogleApiClient.isConnected()) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//            mGoogleApiClient.disconnect();
//        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationManager lm = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location == null) {
            onLocationChanged(location);
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            onLocationChanged(location);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {// Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {

            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }


    private void handleNewLocation(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

    }

    @Override
    public void onLocationChanged(Location location) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (!location.isFromMockProvider())
                handleNewLocation(location);
            else {
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("FakeGPS")
                        .child(new MySharedPreference(getApplicationContext()).getStringShared("user_id"))
                        .setValue("2");
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
