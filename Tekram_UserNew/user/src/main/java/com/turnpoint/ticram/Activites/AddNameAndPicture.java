package com.turnpoint.ticram.Activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
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
import com.google.gson.Gson;
import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.PathUrl;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.Volley.IResult;
import com.turnpoint.ticram.Volley.VolleyService;
import com.turnpoint.ticram.modules.User_info;
import com.turnpoint.ticram.modules.complete_info_result;

import java.util.Hashtable;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddNameAndPicture extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    EditText ed_name;
    IResult iresult;
    VolleyService voly_ser;
    public  ProgressDialog loading;

    public static final int REQUEST_LOCATION=001;
    GoogleApiClient googleApiClient;
    LocationManager locationManager;
    LocationRequest locationRequest;
    LocationSettingsRequest.Builder locationSettingsRequest;
    PendingResult<LocationSettingsResult> pendingResult;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Al-Jazeera-Arabic-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_name_and_picture);
        callBackVolly();
        ed_name=findViewById(R.id.editText_username);
    }

    public void add_name_and_pic(View view){
        if(ed_name.getText().toString().length() >0 ) {
            Volley_go();
        }
        else if(ed_name.getText().toString().length() < 3 || ed_name.getText().toString().length() ==0 ){
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.user_name_confir) , Toast.LENGTH_SHORT).show();
        }
    }


    private void Volley_go(){

        loading = ProgressDialog.show(AddNameAndPicture.this,"",
                getResources().getString(R.string.please_wait),false,false);
        Map<String,String> params = new Hashtable<String, String>();
        params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
        params.put("local",new GetCurrentLanguagePhone().getLang());
        params.put("name", ed_name.getText().toString());
        params.put("user_id",new MySharedPreference(getApplicationContext()).getStringShared("user_id") );
        voly_ser = new VolleyService(iresult,getApplicationContext());
        voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                .getStringShared("base_url")+PathUrl.COMPLETE_INFO ,params );

    }




    void callBackVolly(){

        iresult= new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                loading.dismiss();
                Gson gson= new Gson();
                complete_info_result res =gson.fromJson(response, complete_info_result.class);
                User_info u= res.getUser();
               // Toast.makeText(AddNameAndPicture.this, response, Toast.LENGTH_LONG).show();
                if(res.getHandle().equals("10")){   //login completed
                     Toast.makeText(AddNameAndPicture.this,res.getMsg(), Toast.LENGTH_LONG).show();
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        //Toast.makeText(getApplicationContext(), "Gps is Enabled", Toast.LENGTH_SHORT).show();
                        new MySharedPreference(getApplicationContext()).setStringShared("login_status", "login");
                        new MySharedPreference(getApplicationContext()).setStringShared("user_name",u.getName());
                        Intent i = new Intent(getApplicationContext(), SplashActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        mEnableGps();
                    }
                }
                else if(res.getHandle().equals("01")) {  // login faild
                    Toast.makeText(AddNameAndPicture.this, res.getMsg() , Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void notifyError(VolleyError error) {
                loading.dismiss();
                Toast.makeText(AddNameAndPicture.this,R.string.check_internet, Toast.LENGTH_LONG).show();

                // Toast.makeText(AddNameAndPicture.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };
    }






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

                            status.startResolutionForResult(AddNameAndPicture.this, REQUEST_LOCATION);
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
                        // All required changes were successfully made
                        new MySharedPreference(getApplicationContext()).setStringShared("login_status", "login");
                        //Toast.makeText(getApplicationContext(), "Gps enabled", Toast.LENGTH_SHORT).show();
                        Intent i= new Intent(getApplicationContext(), MapActivity.class);
                        startActivity(i);
                        finish();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.turnGps_confir)
                                , Toast.LENGTH_SHORT).show();
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
}


