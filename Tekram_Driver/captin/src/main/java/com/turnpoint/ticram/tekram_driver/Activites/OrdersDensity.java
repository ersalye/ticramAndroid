package com.turnpoint.ticram.tekram_driver.Activites;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.turnpoint.ticram.tekram_driver.MySharedPreference;
import com.turnpoint.ticram.tekram_driver.PathUrl;
import com.turnpoint.ticram.tekram_driver.R;
import com.turnpoint.ticram.tekram_driver.Volley.IResult;
import com.turnpoint.ticram.tekram_driver.Volley.VolleyService;
import com.turnpoint.ticram.tekram_driver.modules.LocationDensity;
import com.turnpoint.ticram.tekram_driver.modules.Locations;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OrdersDensity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    public static final String TAG = MapsMain.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    double currentLatitude = 0.0, currentLongitude = 0.0;

    IResult iresult;
    VolleyService voly_ser;
    public ProgressDialog loading;
    String method;
    BitmapDescriptor icon_driver, icon_red, icon_green, icon_yellow;

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
        setContentView(R.layout.activity_orders_density);
        callBackVolly();

        icon_driver = BitmapDescriptorFactory.fromResource(R.drawable.markermdpi);
        icon_red = BitmapDescriptorFactory.fromResource(R.drawable.red_marker);
        icon_green = BitmapDescriptorFactory.fromResource(R.drawable.green_marker);
        icon_yellow = BitmapDescriptorFactory.fromResource(R.drawable.yellow_marker);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        //Toast.makeText(getApplicationContext(),"لا يوجد طلبات حاليا!", Toast.LENGTH_SHORT).show();
    }


    public void back(View view) {
        onBackPressed();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //permission not granted
        if (ActivityCompat.checkSelfPermission(OrdersDensity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(OrdersDensity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(OrdersDensity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    200);

        } else {
            // Permission has already been granted
          /*  LatLng mylocation = new LatLng(currentLatitude, currentLongitude);
            MarkerOptions markerOptionsss = new MarkerOptions().position(mylocation).icon(icon_driver);
            mMap.addMarker(markerOptionsss);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 14.0f));*/

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 200: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        LatLng mylocation = new LatLng(currentLatitude, currentLongitude);
                        mMap.addMarker(new MarkerOptions().position(mylocation).title("Me"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new
                                LatLng(currentLatitude, currentLongitude), 14.0f));
                    } catch (Exception ex) {
                    }


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void zoom_into_my_loc(View v) {
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude),
                    14.0f));
        } catch (Exception ex) {
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();

    }


    @Override
    protected void onPause() {
        super.onPause();
//        Toast.makeText(getApplicationContext(),"onPause", Toast.LENGTH_SHORT).show();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
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
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
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
        try {
            mMap.clear();
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            //onMapReady(mMap);
            LatLng mylocation = new LatLng(currentLatitude, currentLongitude);
            MarkerOptions markerOptionsss = new MarkerOptions().position(mylocation).icon(icon_driver);
            mMap.addMarker(markerOptionsss);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 14.0f));
            Volley_go();
        } catch (Exception ex) {
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if(! location.isFromMockProvider())
                handleNewLocation(location);
            else {
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("FakeGPS")
                        .child(new MySharedPreference( getApplicationContext()).getStringShared("user_id"))
                        .setValue("2");
            }
        }
    }


    private void Volley_go() {
        try {
            loading = ProgressDialog.show(OrdersDensity.this, "",
                    "الرجاء الانتظار...", false, false);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("local", "ara");
            params.put("radius", "10");
            params.put("location", currentLatitude + "," + currentLongitude);
            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url") + PathUrl.OrderDensity, params);
        } catch (Exception ex) {
        }

    }


    void callBackVolly() {
        iresult = new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                try {
                    loading.dismiss();
                    Gson gson = new Gson();
                    LocationDensity res = gson.fromJson(response, LocationDensity.class);
                    if (res.getHandle().equals("02")) {  // account not found
                    } else if (res.getHandle().equals("10")) {   // account found
                        List<Locations> loc = res.getOrders();
                        for (int i = 0; i < loc.size(); i++) {
                            String string_loc = loc.get(i).getLocation();
                            String[] separated = string_loc.split(",");
                            LatLng locs = new LatLng(Double.parseDouble(separated[0]), Double.parseDouble(separated[1]));
                            if (loc.get(i).getColor().equals("#ff0000")) {
                                MarkerOptions markerOptions_red = new MarkerOptions().position(locs).icon(icon_red);
                                mMap.addMarker(markerOptions_red);
                            }
                            if (loc.get(i).getColor().equals("#ff5b00")) {
                                MarkerOptions markerOptions_yellow = new MarkerOptions().position(locs).icon(icon_yellow);
                                mMap.addMarker(markerOptions_yellow);
                            }
                            if (loc.get(i).getColor().equals("#008000")) {
                                MarkerOptions markerOptions_green = new MarkerOptions().position(locs).icon(icon_green);
                                mMap.addMarker(markerOptions_green);
                            }
                        }
                    }
                } catch (Exception ex) {
                }


            }

            @Override
            public void notifyError(VolleyError error) {
                loading.dismiss();
                Toast.makeText(OrdersDensity.this, " مشكلة بالاتصال بالانترنت!", Toast.LENGTH_LONG).show();

                //Toast.makeText(OrdersDensity.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };
    }


}



