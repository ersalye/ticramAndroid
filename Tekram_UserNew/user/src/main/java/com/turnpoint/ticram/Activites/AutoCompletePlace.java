package com.turnpoint.ticram.Activites;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.R;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AutoCompletePlace  extends FragmentActivity implements OnMapReadyCallback,  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


private GoogleMap mMap;
        BitmapDescriptor icon_user_start;
        // MarkerOptions markerOptions_currentloc;
        double selected_lat;
        double selected_lon;
        String selected_place;

public static final String TAG = MapActivity.class.getSimpleName();
private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
private GoogleApiClient mGoogleApiClient;
private LocationRequest mLocationRequest;
        double currentLatitude, currentLongitude;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Al-Jazeera-Arabic-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.content_auto_complete_place);
// Initialize the AutocompleteSupportFragment.
        Places.initialize(getApplicationContext()," AIzaSyCrBUhvuIULUX7buroHkeVDx5ZMeP1gKsU");
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());

                try {
                    place.getLatLng();
                    Log.i(TAG, "Place: " + place.getName());
                    selected_place = place.getName().toString();
                    LatLng selected_lat_lon = place.getLatLng();
                    selected_lat = selected_lat_lon.latitude;
                    selected_lon = selected_lat_lon.longitude;
                    LatLng latLng = new LatLng(selected_lat, selected_lon);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(selected_lat, selected_lon), 16.0f));
                } catch (Exception ex){
                    ex.printStackTrace();
                }

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        autocompleteFragment.getView().findViewById(R.id.places_autocomplete_clear_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // example : way to access view from PlaceAutoCompleteFragment
                        // ((EditText) autocompleteFragment.getView()
                        // .findViewById(R.id.place_autocomplete_search_input)).setText("");
                        if (lastCurrentLocation != null) {
                            autocompleteFragment.setText("");
                            view.setVisibility(View.GONE);
                            handleNewLocation(lastCurrentLocation);
                        }

                    }
                });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        mGoogleApiClient.connect();

        try {
            icon_user_start = BitmapDescriptorFactory.fromResource(R.drawable.start_bullet);
        } catch (Exception ex){}
        mapFragment.getMapAsync(this);

    }

       public void done_current_loc(View v ){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.dialoge_select_destination);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        new MySharedPreference(getApplicationContext()).setStringShared("destination_trip", selected_place);
                        new MySharedPreference(getApplicationContext()).setStringShared("destination_lat", String.valueOf(selected_lat));
                        new MySharedPreference(getApplicationContext()).setStringShared("destination_lon", String.valueOf(selected_lon));
                        new MySharedPreference(getApplicationContext()).setStringShared("destination_selected", "yes");
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", "OK");
                        setResult(Activity.RESULT_OK, returnIntent);

                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new MySharedPreference(getApplicationContext()).setStringShared("destination_selected", "no");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", "NO");
                setResult(Activity.RESULT_CANCELED, returnIntent);

                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                try {
                    LatLng midLatLng = mMap.getCameraPosition().target;
                    selected_lat = midLatLng.latitude;
                    selected_lon = midLatLng.longitude;
                    address_text();
                } catch (Exception ex){}
            }
        });
    }


    public void address_text(){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(selected_lat, selected_lon, 1);
            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                selected_place=address;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(getApplicationContext(), "OnResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // new MySharedPreference(getApplicationContext()).setStringShared("destination_selected", "no");
    }



    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new MySharedPreference(getApplicationContext()).setStringShared("destination_selected", "no");
        finish();
    }





    private void handleNewLocation(Location location) {
        try {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 16.0f));
        } catch (Exception ex){}
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
        if (location==null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            onLocationChanged(location);
        }
    }



    @Override
    public void onConnectionSuspended(int i) {}
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());}
    }

    Location lastCurrentLocation = null;
    @Override
    public void onLocationChanged(Location location)
    {
        lastCurrentLocation = location;
        handleNewLocation(location);
    }




}



