package com.turnpoint.ticram.tekram_driver.Activites;

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
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.turnpoint.ticram.tekram_driver.CheckIntenetConn;
import com.turnpoint.ticram.tekram_driver.MySharedPreference;
import com.turnpoint.ticram.tekram_driver.PathUrl;
import com.turnpoint.ticram.tekram_driver.R;
import com.turnpoint.ticram.tekram_driver.Volley.IResult;
import com.turnpoint.ticram.tekram_driver.Volley.VolleyService;
import com.turnpoint.ticram.tekram_driver.modules.User;
import com.turnpoint.ticram.tekram_driver.modules.register_user;
import com.turnpoint.ticram.tekram_driver.modules.usual_result;
import com.turnpoint.ticram.tekram_driver.retrofitServices.SendLocationService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    EditText ed_mobile, ed_pass;
    IResult iresult;
    VolleyService voly_ser;
    public ProgressDialog loading;
    String method;
    String dialoge_phone;
    SendLocationService sendLoc;
    Location location;
  //  String user_id;
    User u;
    String firebase_Token;
    public static final int REQUEST_LOCATION = 001;
    GoogleApiClient googleApiClient;
    LocationManager locationManager;
    LocationRequest locationRequest;
    LocationSettingsRequest.Builder locationSettingsRequest;
    PendingResult<LocationSettingsResult> pendingResult;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Al-Jazeera-Arabic-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_login);
        callBackVolly();
        /*LinearLayout main_lay= findViewById(R.id.login_layout);
        main_lay.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);*/
       // sendLoc= ApiUtils.sendLoc();


        ed_mobile=findViewById(R.id.ed_phone);
        ed_pass=findViewById(R.id.ed_password);
        //triger_alarmManager_sendLocation();

        firebase_Token=new MySharedPreference(this).getStringShared("FirebaseMessagingToken");
        if (firebase_Token == null || firebase_Token.isEmpty()) {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w("TAG", "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token

                            firebase_Token = task.getResult().getToken();
                            new MySharedPreference(getApplicationContext()).setStringShared("FirebaseMessagingToken", firebase_Token);
                        }
                    });

        }
        //Toast.makeText(getApplicationContext(),firebase_Token,Toast.LENGTH_SHORT).show();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            initializeLocationManager();
        } else {
            mEnableGps();
        }
    }



    public void btn_reset_pass(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(LoginActivity.this);
        alert.setMessage(R.string.please_add_number);
        alert.setTitle(R.string.reset_passord);
        alert.setView(edittext);

        final boolean check_network = new CheckIntenetConn(getApplicationContext()).isNetworkAvailable();

        alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialoge_phone= edittext.getText().toString();
                method="reset_pass";
                if(check_network){
                Volley_go(method);}
                else{
                    Toast.makeText(getApplicationContext(),R.string.check_conn,Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();

    }

    public void btn_login(View v){
        try {
            final boolean check_network = new CheckIntenetConn(getApplicationContext()).isNetworkAvailable();
            if (ed_mobile.getText().toString().length() > 0 && ed_pass.getText().toString().length() > 0) {
                if (check_network) {
                    method = "login";
                    Volley_go(method);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.check_conn, Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), R.string.blank_fields, Toast.LENGTH_SHORT).show();

            }
        }
        catch (Exception ex){

        }

    }

    /*private void getgeozone() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest request= new JsonArrayRequest("https://new.ticram.com/service/geozones.php", new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("geozone", response.toString());


                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject obj = response.getJSONObject(i);
                        PolygonOptions rectOptions = new PolygonOptions()
                                .add(new LatLng(37.35, -122.0),
                                        new LatLng(37.45, -122.0),
                                        new LatLng(37.45, -122.2),
                                        new LatLng(37.35, -122.2),
                                        new LatLng(37.35, -122.0));

// Get back the mutble Polygon
                        if  (  pointInPolygon(  new LatLng(37.45, -122.0),rectOptions ))  Toast.makeText(getApplicationContext(),"ff",Toast.LENGTH_LONG).show();



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                // notifying list adapter about data changes
                // so that it renders the list view with updated data

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("geozone erro", "Error: " + error.getMessage());
                // hidePDialog();

            }
        });
        // Adding request to request queue
        requestQueue.add(request);
    }*/

    public boolean pointInPolygon(LatLng point, PolygonOptions polygon) {
        // ray casting alogrithm http://rosettacode.org/wiki/Ray-casting_algorithm
        int crossings = 0;
        List<LatLng> path = polygon.getPoints();
        path.remove(path.size()-1); //remove the last point that is added automatically by getPoints()

        // for each edge
        for (int i=0; i < path.size(); i++) {
            LatLng a = path.get(i);
            int j = i + 1;
            //to close the last edge, you have to take the first point of your polygon
            if (j >= path.size()) {
                j = 0;
            }
            LatLng b = path.get(j);
            if (rayCrossesSegment(point, a, b)) {
                crossings++;
            }
        }

        // odd number of crossings?
        return (crossings % 2 == 1);
    }

    public boolean rayCrossesSegment(LatLng point, LatLng a, LatLng b) {
        // Ray Casting algorithm checks, for each segment, if the point is 1) to the left of the segment and 2) not above nor below the segment. If these two conditions are met, it returns true
        double px = point.longitude,
                py = point.latitude,
                ax = a.longitude,
                ay = a.latitude,
                bx = b.longitude,
                by = b.latitude;
        if (ay > by) {
            ax = b.longitude;
            ay = b.latitude;
            bx = a.longitude;
            by = a.latitude;
        }
        // alter longitude to cater for 180 degree crossings
        if (px < 0 || ax <0 || bx <0) { px += 360; ax+=360; bx+=360; }
        // if the point has the same latitude as a or b, increase slightly py
        if (py == ay || py == by) py += 0.00000001;


        // if the point is above, below or to the right of the segment, it returns false
        if ((py > by || py < ay) || (px > Math.max(ax, bx))){
            return false;
        }
        // if the point is not above, below or to the right and is to the left, return true
        else if (px < Math.min(ax, bx)){
            return true;
        }
        // if the two above conditions are not met, you have to compare the slope of segment [a,b] (the red one here) and segment [a,p] (the blue one here) to see if your point is to the left of segment [a,b] or not
        else {
            double red = (ax != bx) ? ((by - ay) / (bx - ax)) : Double.POSITIVE_INFINITY;
            double blue = (ax != px) ? ((py - ay) / (px - ax)) : Double.POSITIVE_INFINITY;
            return (blue >= red);
        }

    }

    private void Volley_go(String method){

        if(method.equals("login")) {
            try {
                firebase_Token=new MySharedPreference(this).getStringShared("FirebaseMessagingToken");
                //Toast.makeText(getApplicationContext(), new MySharedPreference(getApplicationContext()).getStringShared("base_url")+PathUrl.LOGIN, Toast.LENGTH_LONG).show();
                Log.d("jjjjjjjj",new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url")+PathUrl.LOGIN );
                loading = ProgressDialog.show(LoginActivity.this, "",
                        "الرجاء الانتظار...", false, false);
                Map<String, String> params = new Hashtable<String, String>();
                params.put("local", "ara");
                params.put("mob", ed_mobile.getText().toString());
                params.put("reg_id", firebase_Token);
                params.put("password", ed_pass.getText().toString());
                params.put("version", "4");
                voly_ser = new VolleyService(iresult, getApplicationContext());
                voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url")+PathUrl.LOGIN, params);
                new MySharedPreference(getApplicationContext()).setStringShared("VERSION_NUMBER", PathUrl.VERSION_NUMBER);
            }
           catch (Exception e) {
                e.printStackTrace();
            }

        }

        if(method.equals("reset_pass")) {
            try {


                loading = ProgressDialog.show(LoginActivity.this, "",
                        "الرجاء الانتظار...", false, false);
                Map<String, String> params = new Hashtable<String, String>();
                params.put("local", "ara");
                params.put("mob", dialoge_phone);
                voly_ser = new VolleyService(iresult, getApplicationContext());
                voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url")+PathUrl.ResetPassowrd, params);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (method.equals("send_myLocation")) {
            loading = ProgressDialog.show(LoginActivity.this, "",
                    "الرجاء الانتظار...", false, true);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("local", "ara");
            params.put("access_token", new MySharedPreference(this).getStringShared("access_token"));
            params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("lat", String.valueOf(location.getLatitude()));
            params.put("lng", String.valueOf(location.getLongitude()));
            params.put("type", "T");
            voly_ser = new VolleyService(iresult, this);
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.SEND_LOCATION, params);

          //  Toast.makeText(getApplicationContext(), "Login : " + String.valueOf(location.getLatitude()) + String.valueOf(location.getLongitude()), Toast.LENGTH_SHORT).show();
        }
    }




    void callBackVolly(){

        iresult= new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                if(method.equals("login")) {
                    loading.dismiss();
                    //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    register_user res = gson.fromJson(response, register_user.class);
                    if (res.getHandle().equals("02")) {  // account not found
                        Toast.makeText(LoginActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {   // account found
                         Toast.makeText(LoginActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        // register_sinch();
                         u = res.getTransport();
                        // user_id=String.valueOf(u.getId());
                      //   Toast.makeText(LoginActivity.this,"login", Toast.LENGTH_LONG).show();
                       // new MySharedPreference(getApplicationContext()).setStringShared("first_login","yes");

                        new MySharedPreference(getApplicationContext()).setStringShared("theres_order","no");
                        new MySharedPreference(getApplicationContext()).setStringShared("login_status", "login");
                        new MySharedPreference(getApplicationContext()).setStringShared("user_id", String.valueOf(u.getId()));
                        new MySharedPreference(getApplicationContext()).setStringShared("user_name", u.getName());
                        new MySharedPreference(getApplicationContext()).setStringShared("account_type", u.getAccountType());
                        new MySharedPreference(getApplicationContext()).setStringShared("photo", u.getPhoto());
                        new MySharedPreference(getApplicationContext()).setStringShared("access_token", u.getAccessToken());
                        new MySharedPreference(getApplicationContext()).setStringShared("rate", u.getRate());
                        new MySharedPreference(getApplicationContext()).setStringShared("balance", u.getBalance());
                        new MySharedPreference(getApplicationContext()).setStringShared("mobile",u.getMob());
                        new MySharedPreference(getApplicationContext()).setStringShared("email",u.getEmail());
                        new MySharedPreference(getApplicationContext()).setStringShared("gender",u.getGender());
                        new MySharedPreference(getApplicationContext()).setStringShared("time_to_cancel_trip_in_sec",u.getTimeToCancelTripInSec());
                        new MySharedPreference(getApplicationContext()).setStringShared("time_to_reload_current_orders_in_sec",u.getTimeToReloadCurrentOrdersInSec());
                        new MySharedPreference(getApplicationContext()).setStringShared("mob",u.getMob());

                        Intent i = new Intent(getApplicationContext(), MapsMain.class);
                        startActivity(i);
                        finish();




                    }
                    else {
                        Toast.makeText(LoginActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();

                    }
                }

                if(method.equals("reset_pass")) {
                    loading.dismiss();
                    //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    usual_result res = gson.fromJson(response, usual_result.class);
                    if (res.getHandle().equals("02")) {  // account not found
                        Toast.makeText(LoginActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {   // account found
                        Toast.makeText(LoginActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();

                    }
                }



                if (method.equals("send_myLocation")) {
                    loading.dismiss();
                    // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    usual_result res = gson.fromJson(response, usual_result.class);
                    Log.d("send_myLocation res", "notifySuccessPost: "+res.toString());
                    if (res.getHandle().equals("02")) {
                        Toast.makeText(getApplicationContext(), res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {
                        Intent i = new Intent(getApplicationContext(), MapsMain.class);
                        startActivity(i);
                        finish();
                    }

                }
                }

            @Override
            public void notifyError(VolleyError error) {
                loading.dismiss();
                Toast.makeText(LoginActivity.this, " مشكلة بالاتصال بالانترنت!", Toast.LENGTH_LONG).show();
                //Toast.makeText(LoginActivity.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };
    }






    public void checkGps_permission(){

        if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    200);

        } else {
            // Permission has already been granted
                       // triger_alarmManager_sendLocation();
                        Intent i = new Intent(getApplicationContext(), MapsMain.class);
                        startActivity(i);
                        finish();

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 200: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                       // triger_alarmManager_sendLocation();
                        Intent i = new Intent(getApplicationContext(), MapsMain.class);
                        startActivity(i);
                        finish();


                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    ActivityCompat.requestPermissions(LoginActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            200);

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }







    private void initializeLocationManager() {
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (network_enabled) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(LoginActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(LoginActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(LoginActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            200);

                } else  if (ActivityCompat.checkSelfPermission(LoginActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(LoginActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if(location!=null){
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        // Toast.makeText(getApplicationContext(), String.valueOf(location.getLatitude())+ String.valueOf(location.getLongitude()) , Toast.LENGTH_LONG).show();
                    }
                }
            }

        }
    }



    /*

    public void Post_Location() {
         Call<usual_result> res= sendLoc.send_location(new MySharedPreference(this).getStringShared("access_token"),
                 "ara",
                 new MySharedPreference(this).getStringShared("user_id"),
                 "0.0", "0.0", "T");
        Toast.makeText(getApplicationContext(),"Post_Location",Toast.LENGTH_LONG).show();
       // Toast.makeText(getApplicationContext(),,Toast.LENGTH_LONG).show();

        sendLoc.send_location(new MySharedPreference(this).getStringShared("access_token"),
                "ara",
                new MySharedPreference(this).getStringShared("user_id"),
                "0.0", "0.0", "T")
                .enqueue(new Callback<usual_result>() {
                    @Override
                    public void onResponse(Call<usual_result> call, Response<usual_result> response) {

                        if(response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"is sucss :" +
                                    response.body().toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<usual_result> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"faild :" +t.getMessage(),Toast.LENGTH_LONG).show();
                    }


                });
    }
*/












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
        pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient,
                locationSettingsRequest.build());
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

                            status.startResolutionForResult(LoginActivity.this, REQUEST_LOCATION);
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
                        initializeLocationManager();
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



}


