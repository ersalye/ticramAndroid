package com.turnpoint.ticram.Activites;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.turnpoint.ticram.Adapters.Adapter_cancel2;
import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.PathUrl;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.Volley.IResult;
import com.turnpoint.ticram.Volley.VolleyService;
import com.turnpoint.ticram.modules.Cancel_list_response;
import com.turnpoint.ticram.modules.Cancels;
import com.turnpoint.ticram.modules.OrderWaiting;
import com.turnpoint.ticram.modules.ResponseWaiting;
import com.turnpoint.ticram.modules.addOrderFirebase;
import com.turnpoint.ticram.modules.addcoordsfirebase;
import com.turnpoint.ticram.modules.cancelbyUser;
import com.turnpoint.ticram.modules.current_support;
import com.turnpoint.ticram.modules.firebase_values;
import com.turnpoint.ticram.modules.support;
import com.turnpoint.ticram.modules.user_info_splash;
import com.turnpoint.ticram.modules.user_info_splash2;
import com.turnpoint.ticram.modules.user_info_splash3;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TripDetails extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private GoogleMap mMap;
    public static final String TAG = "saaaaaaaaaaas";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    double currentLatitude, currentLongitude;
    public ProgressDialog loading;
    IResult iresult;
    IResult iresultOrderInfo;
    IResult iresultOrderStateChange;
    VolleyService voly_ser;
    Marker marker_user_start, marker_user_destination, marker_driver;
    BitmapDescriptor icon_user_start, icon_user_destination, icon_driver;
    TextView tv_des_text, tv_currentLocation;

    TextView tv_driver_name, tv_rate, tv_TimeToUSER , tv_car_plateNum , tv_car_model, order_info;
    ImageView icon_user, icon_call;
    RatingBar rb;
    String order_id;
    String driver_id;
    LinearLayout lin_captinDetails, lin_cancelTrip, trip_details_ll;
    LatLng final_dest_user, UserLocationStart;
    String final_des_found;
    CountDownTimer myCountDownTimer;
    Button btn_cancel;
    String method;
    String captin_arrived = "no";
    String captainName, captainNumber;
    ImageView img_drawer;

    Marker marker_driverrr;

    Timer timer;
    TimerTask timerTask;
    private Handler handler;

    int num_sec_saved=0;
    int num=0;
    int num_sec_saved_wosoul=0;
    int num_wousoul=0;
    String status_cancel;
    Adapter_cancel2 adapter;
    String seleted_id_reasonCancel;
    DatabaseReference connectedRef;
    private ValueEventListener mListener;



    private static final long DELAY = 4500;
    private static final long ANIMATION_TIME_PER_ROUTE = 3000;
    String polyLine = "q`epCakwfP_@EMvBEv@iSmBq@GeGg@}C]mBS{@KTiDRyCiBS";
    private PolylineOptions polylineOptions;
    private Polyline greyPolyLine;
    private SupportMapFragment mapFragment;
    private Handler handler_tracking;
    private Marker carMarker;
    private int index;
    private int next;
    private LatLng startPosition;
    private LatLng endPosition;
    private float v;
    Button button2;
    List<LatLng> polyLineList;
    private double lat, lng;
    // banani
    double latitude = 23.7877649;
    double longitude = 90.4007049;

    // Give your Server URL here >> where you get car location update
    public static final String URL_DRIVER_LOCATION_ON_RIDE = "*******";
    private boolean isFirstPosition = true;
    private Double startLatitude;
    private Double startLongitude;

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
        setContentView(R.layout.activity_trip_details);
        callBackVolly();
        handler_tracking = new Handler();
        handler = new Handler();

        img_drawer = findViewById(R.id.imageView5);
        img_drawer.setVisibility(View.INVISIBLE);
        //Toast.makeText(getApplicationContext(), new MySharedPreference(getApplicationContext()).getIntShared("time_cancel_trip_saved")+"  -  "+ new MySharedPreference(getApplicationContext()).getIntShared("time_to_cancel_in_sec"), Toast.LENGTH_LONG).show();
        status_cancel="no";
        try {
            icon_user_start = BitmapDescriptorFactory.fromResource(R.drawable.start_bullet);
            icon_user_destination = BitmapDescriptorFactory.fromResource(R.drawable.end_bullet);
           // icon_driver = BitmapDescriptorFactory.fromResource(R.drawable.markermdpi);
        } catch (Exception ex){}
        lin_captinDetails = findViewById(R.id.linearLay_transportDetails);
        lin_cancelTrip = findViewById(R.id.linearLay_CancelTrip);
        trip_details_ll = findViewById(R.id.trip_details_ll);

        tv_currentLocation = findViewById(R.id.tv_currentLoc_txt);
        tv_des_text = findViewById(R.id.tv_des_text);
        tv_TimeToUSER = findViewById(R.id.tv_time_to_user);

        tv_car_model = findViewById(R.id.textview_car_model);
        order_info = findViewById(R.id.order_info);
        tv_car_plateNum = findViewById(R.id.textView_plateNum);

        tv_TimeToUSER = findViewById(R.id.tv_time_to_user);


        tv_driver_name = findViewById(R.id.txtNameDriver);
        tv_rate = findViewById(R.id.txtRate);
        icon_user = findViewById(R.id.imgDriver);
        icon_call = findViewById(R.id.img_call);
        btn_cancel = findViewById(R.id.button_cancel_order);
        rb = findViewById(R.id.ratingBar);

        LinearLayout lay_des=findViewById(R.id.lay_select_des);
        /*if(new MySharedPreference(getApplicationContext()).getStringShared("destination_selected").equals("no") ||
                new MySharedPreference(getApplicationContext()).getStringShared("destination_selected").equals("not_yet")){
            lay_des.setVisibility(View.INVISIBLE);
        }*/

            btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                method = "get_cancel_reasons";
                Volley_go();


            }
        });

        icon_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialoge_call_2();

            }
        });

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
        mGoogleApiClient.connect();


        order_id = new MySharedPreference(getApplicationContext()).getStringShared("order_id");
       String from_ = new MySharedPreference(getApplicationContext()).getStringShared("from_D_A_S_status");
        new MySharedPreference(getApplicationContext()).setStringShared("from_w_status","normal");
        /*if(from_.equals("normal")){
            method = "get_myorder_details";
            Volley_go();
            //cancel_order_counter();
        }
       else if(from_.equals("splash")){

        }*/



    }







    public void cancel_order_counter() {
        num_sec_saved=new MySharedPreference(getApplicationContext()).getIntShared("time_cancel_trip_saved");
        int num_sec=new MySharedPreference(getApplicationContext()).getIntShared("time_to_cancel_in_sec");
        if(num_sec>=num_sec_saved) {
            num = num_sec - num_sec_saved;
        }
        else if(num_sec<num_sec_saved) {
            num = num_sec_saved - num_sec;
        }
        final String vvv = getResources().getString(R.string.free_cancelation);
        myCountDownTimer = new CountDownTimer(num* 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                btn_cancel.setText(" " + vvv + " " + String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                num_sec_saved=num_sec_saved+1;
                new MySharedPreference(getApplicationContext()).setIntShared("time_cancel_trip_saved",
                        num_sec_saved);

            }

            public void onFinish() {
                btn_cancel.setText(getResources().getString(R.string.cancel_pay));
            }
        }.start();

    }


    public void choose_current_loc(View v){ }


    public void cancel_order_counter2(int remaining_sec) {

        final String vvv = getResources().getString(R.string.free_cancelation);
        myCountDownTimer = new CountDownTimer(remaining_sec* 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                btn_cancel.setText(" " + vvv + " " + String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

            }

            public void onFinish() {
                btn_cancel.setText(getResources().getString(R.string.cancel_pay));
            }
        }.start();

    }


    /*public void show_dialoge_call() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(TripDetails.this,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(TripDetails.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            202);

                } else if (ActivityCompat.checkSelfPermission(TripDetails.this,
                        Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:0" + captainNumber));
                    startActivity(callIntent);
                    Log.d("captainNumber", captainNumber);
                }
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                Log.d("captainNumber", captainNumber);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:0" + captainNumber));
                startActivity(callIntent);
            }
        } catch (Exception ex){}
    }*/






    public void show_dialoge_call_2() {
        // final CharSequence[] items ={"الدعم الفني", userName , userNumber};
        //final CharSequence[] items = {"الدعم الفني", "الاتصال بالراكب هاتفيا"};
        final CharSequence[] items = {getResources().getString(R.string.call_support),
                getResources().getString(R.string.call_captain)};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TripDetails.this);
        builder.setTitle("");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getResources().getString(R.string.call_support))) {

                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ActivityCompat.checkSelfPermission(TripDetails.this,
                                    Manifest.permission.CALL_PHONE)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(TripDetails.this,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        202);

                            } else if (ActivityCompat.checkSelfPermission(TripDetails.this,
                                    Manifest.permission.CALL_PHONE)
                                    == PackageManager.PERMISSION_GRANTED) {

                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:0" + "788459676"));
                                startActivity(callIntent);
                            }
                        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:0" + "788459676"));
                            startActivity(callIntent);
                        }
                    } catch (Exception ex){}

                }  else if (items[item].equals(getResources().getString(R.string.call_captain))) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(TripDetails.this,
                                Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(TripDetails.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    202);

                        } else if (ActivityCompat.checkSelfPermission(TripDetails.this,
                                Manifest.permission.CALL_PHONE)
                                == PackageManager.PERMISSION_GRANTED) {

                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:0" + captainNumber));
                            startActivity(callIntent);
                            Log.d("usernumber" , captainNumber);

                        }
                    } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                        Log.d("usernumber" , captainNumber);
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:0" + captainNumber));
                        startActivity(callIntent);

                    }
                }
            }
        });
        builder.show();

    }

    public void zoom_into_my_loc(View v) {
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 14.0f));
        } catch (Exception ex){}
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        //stopAlarmManager();
        stoptimertask();

    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            method = "get_myorder_details";
            Volley_go();

            method = "check_statusTrip_chnaged";
            Volley_go();

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.turnpoint.ticram.FCM.onMessageReceived");
            BroadcastReceiver_UpdateUI receiver = new BroadcastReceiver_UpdateUI();
            registerReceiver(receiver, intentFilter);



            if(driver_id!=null && driver_id!="") {
                tracking_firebase();
            }

            if (!status_cancel.equals("yes")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        method = "check_statusTrip_chnaged";
                        Volley_go();
                    }
                }, 11*1000);
            }


        } catch (Exception ex){}
    }



    @Override
    public void onDestroy(){
        super.onDestroy();
       connectedRef.removeEventListener(mListener);
    }


   /* private void firebase_cancel(){
            addOrderFirebase values =new addOrderFirebase(String.valueOf(order_id),"C") ;
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("orders").child(order_id).child("info").setValue(values);

    }*/

    private void update_order_firebase(){
        addOrderFirebase values =new addOrderFirebase(Long.valueOf(order_id),"C") ;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("orders").child(driver_id).child("order").setValue(values);

    }



    public void tracking_firebase(){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
             connectedRef = database.getReference("tikram-192101");
             mListener= connectedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("drivers")
                            .child(driver_id).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if(dataSnapshot.exists()) {
                                //addcoordsfirebase values = dataSnapshot.getValue(addcoordsfirebase.class);
                                firebase_values values = dataSnapshot.getValue(firebase_values.class);

                                try {
                                    String driver_pos = values.coords;
                                    String[] separated_driver_loc = driver_pos.split(",");
                                    LatLng driverLocation = new LatLng(Double.parseDouble(separated_driver_loc[0]),
                                            Double.parseDouble(separated_driver_loc[1]));
                                   // double marker_lat = marker.getPosition().latitude;
                                   // double marker_lon = marker.getPosition().longitude;
                                   // startPosition=driverLocation;
                                   // animateMarker(new LatLng(marker_lat, marker_lon), driverLocation, false);
                                    startLatitude=driverLocation.latitude;
                                    startLongitude=driverLocation.longitude;
                                    getDriverLocationUpdate();
                                   // Toast.makeText(getApplicationContext(), values.coords, Toast.LENGTH_LONG).show();

                                } catch (Exception ex) {
                                }
                            }
                        }
                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            if(dataSnapshot.exists()) {
                                addcoordsfirebase values = dataSnapshot.getValue(addcoordsfirebase.class);
                                try {
                                    String driver_pos = values.coords;
                                    String[] separated_driver_loc = driver_pos.split(",");
                                    LatLng driverLocation = new LatLng(Double.parseDouble(separated_driver_loc[0]),
                                            Double.parseDouble(separated_driver_loc[1]));
                                    //double marker_lat = marker.getPosition().latitude;
                                    //double marker_lon = marker.getPosition().longitude;
                                   // animateMarker(new LatLng(marker_lat, marker_lon), driverLocation, false);
                                    startLatitude=driverLocation.latitude;
                                    startLongitude=driverLocation.longitude;
                                    getDriverLocationUpdate();
                                  //  Toast.makeText(getApplicationContext(), values.coords, Toast.LENGTH_LONG).show();
                                } catch (Exception ex) {
                                }
                             //   Toast.makeText(getApplicationContext(), values.coords, Toast.LENGTH_LONG).show();
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

                @Override
                public void onCancelled(DatabaseError error) {
                    //System.err.println("Listener was cancelled at .info/connected");
                }
            });

        }






    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 5000, 5* 1000);

    }

    public void stoptimertask() {

        if (timer != null) {
            timer.cancel();
            timer = null;

        }

    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        method = "get_curloc_driver";
                        Volley_go();
                    }
                });
            }
        };
    }

    /* public void triger_alarmManager_sendLocation(){
     *//* *//**//* String time_to_reload=new MySharedPreference(getApplicationContext()).getStringShared("time_to_reload_current_orders_in_sec");
        int time_to_reload_cur=Integer.parseInt(time_to_reload);*//**//*
        Intent alarmIntent = new Intent(TripDetails.this, BroadcastReceiver_LocUpdate.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(TripDetails.this,
                123456789, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager
        manager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 1000 * 5 , pendingIntent);
        Toast.makeText(this, "alarm started!", Toast.LENGTH_SHORT).show();*//*
    }


    // Stop/Cancel alarm manager
    public void stopAlarmManager() {
       *//* Intent alarmIntent = new Intent(TripDetails.this, BroadcastReceiver_LocUpdate.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(TripDetails.this,
                123456789, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "alarm canceled!", Toast.LENGTH_SHORT).show();*//*

      stoptimertask();
    }
*/




    public class BroadcastReceiver_UpdateUI extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            String state = extras.getString("extra");
            String what_todo = extras.getString("what_todo");

            if (what_todo.equals("finish")) {
                if(!((Activity) context).isFinishing()) {
                    finish();
                }
               // Toast.makeText(getApplicationContext(),what_todo, Toast.LENGTH_SHORT).show();
            }
            if (what_todo.equals("driver_arrived")) {
                //lin_captinDetails.setVisibility(View.GONE);
                //stopAlarmManager();
                Captain_Arrived(state);
            }

            if (what_todo.equals("start_trip")) {
                Captain_Start_trip(state);
            }

            if(what_todo.equals("ORDER_CANCELLED")){
                if(!((Activity) context).isFinishing()) {
                    status_cancel="yes";
                    show_Dialoge_Cancel_order();
            }
        }
    }


    public void show_Dialoge_Cancel_order(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TripDetails.this);
                alertDialogBuilder.setMessage(getResources().getString(R.string.trip_canceled_dialoge));
                alertDialogBuilder.setPositiveButton(getResources().getString(R.string.back_toMain),
                        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        method = "update_user_info";
                        Volley_go();
                    }
                }, 2000);

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
        }
    }


    public void Captain_Arrived(String state_){
        try {
            myCountDownTimer.cancel();
            if (myOrder !=null && myOrder.getTaxi() == 1)
                lin_cancelTrip.setVisibility(View.GONE);
            if (new GetCurrentLanguagePhone().getLang().equals("ara")) {
                tv_TimeToUSER.setText("وصل الكابتن :)");
            } else if (!new GetCurrentLanguagePhone().getLang().equals("ara")) {
                tv_TimeToUSER.setText("Captain Arrived");
            }
            tv_TimeToUSER.setBackgroundResource(R.drawable.rounded_corner_green);
            captin_arrived = "yes";
        } catch (Exception ex){}
    }

    public void Captain_Start_trip(String state_){
      //  Toast.makeText(getApplicationContext(), state_, Toast.LENGTH_SHORT).show();
        if (myOrder !=null && myOrder.getTaxi() == 1)
            lin_cancelTrip.setVisibility(View.GONE);
        update_map();
        tv_TimeToUSER.setVisibility(View.INVISIBLE);
        stoptimertask();

       /* if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                try {
                    //mMap.clear();
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    LatLng mylatlng = new LatLng(lat, lon);
                    double marker_lat = marker.getPosition().latitude;
                    double marker_lon = marker.getPosition().longitude;
                    animateMarker(new LatLng(marker_lat, marker_lon), mylatlng, false);
                } catch (Exception ex){}

            }
        });*/
    }


/*
    public class BroadcastReceiver_LocUpdate extends BroadcastReceiver {

        BroadcastReceiver_LocUpdate(){}
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent i = new Intent(context, Service_getDriverLoc.class);
            context.startService(i);
        }
    }*/


   /* public class Service_getDriverLoc extends Service {

        public Service_getDriverLoc() {
        }

        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            super.onStartCommand(intent, flags, startId);

            return START_STICKY;
        }

        @Override
        public void onCreate() {
            Log.e(TAG, "onCreate");
           // Toast.makeText(getApplicationContext(),"OnCreate",Toast.LENGTH_SHORT).show();
            method="get_curloc_driver";
            Volley_go();

        }

    }
*/

    public void update_map(){
        try {
            if (final_des_found != null && final_des_found.equals("yes")) {
                //mMap.clear();

                /*MarkerOptions markerOptionss_driver = new MarkerOptions().
                        position(UserLocationStart).icon(icon_driver);
                mMap.addMarker(markerOptionss_driver);*/

                MarkerOptions markerOptionsss = new MarkerOptions().
                        position(UserLocationStart).icon(icon_user_start);
                mMap.addMarker(markerOptionsss);

                MarkerOptions markerOptionsss2 = new MarkerOptions().
                        position(final_dest_user).icon(icon_user_destination);
                mMap.addMarker(markerOptionsss2);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(final_dest_user);
                builder.include(UserLocationStart);
                LatLngBounds bounds = builder.build();
                int padding = 70;
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.moveCamera(cu);
                mMap.animateCamera(cu);

               /* String url = getUrl(final_dest_user, UserLocationStart);
                FetchUrl FetchUrl = new FetchUrl();
                FetchUrl.execute(url);*/


            }
        }  catch (Exception ex){
            ex.printStackTrace();
        }
      }




    private void handleNewLocation(Location location) {
        try {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
        } catch (Exception ex){}
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            mMap.setPadding(10, 10, 10, 10);
            if (ActivityCompat.checkSelfPermission(TripDetails.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TripDetails.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(TripDetails.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        200);

            } else {

            }
        } catch (Exception ex){}
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 200: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    ActivityCompat.requestPermissions(TripDetails.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            200);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


            case 202: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //creatEventMap();
                    Log.d("captainNumber" , captainNumber);
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:0" + captainNumber));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    startActivity(callIntent);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    ActivityCompat.requestPermissions(TripDetails.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            202);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
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
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());}
    }
    @Override
    public void onLocationChanged(Location location)
    {
        handleNewLocation(location);
    }





    private void Volley_go(){

            if (method.equals("get_myorder_details")) {
                try {
                     Map<String, String> params = new Hashtable<String, String>();
                    params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                    params.put("local", new GetCurrentLanguagePhone().getLang());
                    params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                    params.put("order_id", order_id);
                    voly_ser = new VolleyService(iresultOrderInfo, getApplicationContext());
                    voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                            .getStringShared("base_url")+PathUrl.View_Transport_Order, params);
                } catch (Exception ex){}

            }

        if (method.equals("cancel")) {
                try {
                    loading = ProgressDialog.show(TripDetails.this, "",
                            getResources().getString(R.string.please_wait),
                            false, false);
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                    params.put("local", new GetCurrentLanguagePhone().getLang());
                    params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                    params.put("order_id", order_id);
                    params.put("cancel_id", seleted_id_reasonCancel);
                    voly_ser = new VolleyService(iresult, getApplicationContext());
                    voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                            .getStringShared("base_url")+PathUrl.Cancel_Order, params);
                } catch (Exception ex){}

        }



       /* if (method.equals("get_curloc_driver")) {
                try {
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                    params.put("local", new GetCurrentLanguagePhone().getLang());
                    params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                    params.put("driver_id", driver_id);
                    voly_ser = new VolleyService(iresult, getApplicationContext());
                    voly_ser.postDataVolley(PathUrl.TransportLocation, params);
                } catch (Exception ex){}
        }*/

        if(method.equals("get_cur_support")) {
                try {
                    loading = ProgressDialog.show(TripDetails.this, "",
                            getResources().getString(R.string.please_wait), false, false);
                    voly_ser = new VolleyService(iresult, getApplicationContext());
                    voly_ser.getDataVolley(new MySharedPreference(getApplicationContext())
                            .getStringShared("base_url")+PathUrl.GetCurrentSupport);
                } catch (Exception ex){}
        }


        if(method.equals("check_statusTrip_chnaged")) {
                try {
                     Map<String, String> params = new Hashtable<String, String>();
                    params.put("access_token", new MySharedPreference(TripDetails.this).getStringShared("access_token"));
                    params.put("local", new GetCurrentLanguagePhone().getLang());
                    params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                    voly_ser = new VolleyService(iresultOrderStateChange, TripDetails.this);
                    voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                            .getStringShared("base_url")+PathUrl.USERInfo, params);
                } catch (Exception ex){}
        }



        if(method.equals("update_user_info")) {
                try {
                    loading = ProgressDialog.show(TripDetails.this, "",
                            getResources().getString(R.string.please_wait), false, false);
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put("access_token", new MySharedPreference(TripDetails.this).getStringShared("access_token"));
                    params.put("local", new GetCurrentLanguagePhone().getLang());
                    params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                    voly_ser = new VolleyService(iresult, TripDetails.this);
                    voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                            .getStringShared("base_url")+PathUrl.USERInfo, params);
                } catch (Exception ex){}
        }

        if(method.equals("get_cancel_reasons")) {
                try {
                    loading = ProgressDialog.show(TripDetails.this, "",
                            getResources().getString(R.string.please_wait), false, false);
                    voly_ser = new VolleyService(iresult, TripDetails.this);
                    voly_ser.getDataVolley(new MySharedPreference(getApplicationContext())
                            .getStringShared("base_url") + PathUrl.GETCANCELReasons);
                }
                catch (Exception ex){}
        }

    }




    private void Volley_go(String des_lat, String des_lon){
        String URL_DISTANCE="https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +
                currentLatitude +","+currentLongitude+ "&destinations=" + des_lat+","+des_lon + "&mode=driving&language=fr-FR&avoid=tolls";
        loading = ProgressDialog.show(TripDetails.this,"",
                getResources().getString(R.string.please_wait),false,false);
        voly_ser = new VolleyService(iresult,getApplicationContext());
        voly_ser.postDataVolley(URL_DISTANCE);


    }

    OrderWaiting myOrder;

    void callBackVolly(){
        iresult= new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                if (method.equals("get_myorder_details")) {
                    try {
                        if (loading != null) loading.dismiss();
                        Gson gson = new Gson();
                        ResponseWaiting res = gson.fromJson(response, ResponseWaiting.class);
                        if (res.getHandle().equals("10")) {
                            myOrder = res.getOrder();
                            driver_id = myOrder.getTransportId();
                            captainName = myOrder.getTransportName();
                            captainNumber = myOrder.getTransportPhone();
                            tracking_firebase();
                            if (!myOrder.getToLocationTxt().equals("") && myOrder.getToLocationTxt() != null) {
                                String final_destination_user = myOrder.getToLocation();
                                String[] separated_user_loc_final = final_destination_user.split(",");
                                final_dest_user = new LatLng(Double.parseDouble(separated_user_loc_final[0]),
                                        Double.parseDouble(separated_user_loc_final[1]));
                                final_des_found = "yes";
                            }
                            if (myOrder.getToLocationTxt().equals("") || myOrder.getToLocationTxt() == null) {
                                final_des_found = "no";
                            }


                            if (myOrder.getTime_to_cancel() == 0) {
                                btn_cancel.setText(getResources().getString(R.string.cancel_pay));
                            } else if (myOrder.getTime_to_cancel() > 0) {
                                cancel_order_counter2(myOrder.getTime_to_cancel());
                            }

                            tv_currentLocation.setText(myOrder.getLocationTxt());
                            tv_des_text.setText(myOrder.getToLocationTxt());
                            //if (myOrder)
                            // Toast.makeText(getApplicationContext(),myOrder.getTimetoUser(), Toast.LENGTH_SHORT).show();

                            int timetouser = Integer.parseInt(myOrder.getTimetoUser()) * 60;
                            // int timetouser = 1;

                            num_sec_saved_wosoul = new MySharedPreference(getApplicationContext()).getIntShared("timer_wousol_captain");
                            if (timetouser >= num_sec_saved_wosoul) {
                                num_wousoul = timetouser - num_sec_saved_wosoul;
                            } else if (timetouser < num_sec_saved_wosoul) {
                                num_wousoul = num_sec_saved_wosoul - timetouser;
                            }
                            final String vvv = getResources().getString(R.string.afew_min_left);
                            myCountDownTimer = new CountDownTimer(num_wousoul * 1000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                    tv_TimeToUSER.setText(" " + vvv + " " + String.format("%02d:%02d",
                                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                                    num_sec_saved_wosoul = num_sec_saved_wosoul + 1;
                                    new MySharedPreference(getApplicationContext()).setIntShared("timer_wousol_captain",
                                            num_sec_saved_wosoul);

                                }

                                public void onFinish() {
                                    tv_TimeToUSER.setText(getResources().getString(R.string.captain_is_close));
                                }
                            }.start();

                            tv_driver_name.setText(myOrder.getTransportName());
                            tv_car_model.setText(myOrder.getTransportCar());
                            // Toast.makeText(getApplicationContext(),myOrder.getTransportCar(), Toast.LENGTH_SHORT).show();

                            Glide.with(getApplicationContext())
                                    .load(myOrder.getTransportPhoto())
                                    .apply(new RequestOptions()
                                            .placeholder(R.drawable.user_imagehdpi)
                                            .centerCrop()
                                            .dontAnimate()
                                            .dontTransform())
                                    .into(icon_user);
                            Log.d("icoooon", myOrder.getTransportPhoto());


                            tv_car_plateNum.setText(myOrder.getPlateNo());
                            tv_rate.setText(myOrder.getTransportRate());
                            rb.setRating(Float.parseFloat(myOrder.getTransportRate()));
                            String driver_loc = myOrder.getTransportCoords();
                            String userLoc_START = myOrder.getLocation();
                            String[] separated_driver_loc = driver_loc.split(",");
                            LatLng driverLocation = new LatLng(Double.parseDouble(separated_driver_loc[0]),
                                    Double.parseDouble(separated_driver_loc[1]));

                       /* MarkerOptions markerOptions_driver = new MarkerOptions().
                                position(driverLocation).icon(icon_driver);
                        mMap.addMarker(markerOptions_driver);*/

                           /* marker = mMap.addMarker(new MarkerOptions()
                                    .position(driverLocation)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.markermdpi)));
*/

                            String[] separated_user_loc = userLoc_START.split(",");
                            UserLocationStart = new LatLng(Double.parseDouble(separated_user_loc[0]), Double.parseDouble(separated_user_loc[1]));
                            MarkerOptions markerOptionsss = new MarkerOptions().
                                    position(UserLocationStart).icon(icon_user_start);
                            mMap.addMarker(markerOptionsss);

                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            builder.include(driverLocation);
                            builder.include(UserLocationStart);
                            LatLngBounds bounds = builder.build();
                            int padding = 50;
                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                            mMap.moveCamera(cu);
                            mMap.animateCamera(cu);
                            /*String url = getUrl(driverLocation, UserLocationStart);
                            FetchUrl FetchUrl = new FetchUrl();
                            FetchUrl.execute(url);*/


                            if (!myOrder.getOrder_info().isEmpty()) {
                                trip_details_ll.setVisibility(View.GONE);
                                order_info.setVisibility(View.VISIBLE);
                                order_info.setText(myOrder.getOrder_info());
                            }
                            if (myOrder.getTaxi() != 1) {
                                isTaxi = false;

                                lin_cancelTrip.setVisibility(View.VISIBLE);
                                btn_cancel.setText(getResources().getString(R.string.cancel_pay));

                            }
                        }

                        String from_ = new MySharedPreference(getApplicationContext()).getStringShared("from_D_A_S_status");
                        if (from_.equals("normal")) {

                        } else if (from_.equals("splash")) {
                            if (new MySharedPreference(getApplicationContext()).getStringShared("order_status_splash").equals("D")) {
                                //cancel_order_counter();
                            } else if (new MySharedPreference(getApplicationContext()).getStringShared("order_status_splash").equals("A")) {
                                Captain_Arrived("وصل الكابتن");
                            } else if (new MySharedPreference(getApplicationContext()).getStringShared("order_status_splash").equals("S")) {
                                Captain_Start_trip("بدء الكابتن الرحلة");
                            }
                        }
                    } catch (Exception ex){
                        ex.printStackTrace();
                    }


                    tracking_firebase();
                }


                if (method.equals("cancel")) {
                    try {
                        if (loading != null) loading.dismiss();
                        new MySharedPreference(getApplicationContext()).setStringShared("from_w_status", "normal");
                        stoptimertask();
                        Gson gson = new Gson();
                        cancelbyUser res = gson.fromJson(response, cancelbyUser.class);
                           if (res.getHandle().equals("10")) {
                               new MySharedPreference(getApplicationContext()).
                                       setStringShared("balance", res.getBalance());
                               update_order_firebase();
                               startActivity(new Intent(getApplicationContext(), MapActivity.class));
                               finish();
                        }

                    } catch (Exception ex){}

                    /*          new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                method = "update_user_info";
                                Volley_go();
                            }
                        }, 2000);*/

                       /* method = "update_user_info";
                        Volley_go();*/
                }




          /*      if (method.equals("get_curloc_driver")) {
                    try {
                        Gson gson = new Gson();
                        LocationDriver res = gson.fromJson(response, LocationDriver.class);
                        if (res.getHandle().equals("10")) {
                            if (res.getLocation() != null) {
                                String driver_pos = res.getLocation();
                                String[] separated_driver_loc = driver_pos.split(",");
                                LatLng driverLocation = new LatLng(Double.parseDouble(separated_driver_loc[0]), Double.parseDouble(separated_driver_loc[1]));
                                double marker_lat = marker.getPosition().latitude;
                                double marker_lon = marker.getPosition().longitude;
                                animateMarker(new LatLng(marker_lat, marker_lon), driverLocation, false);
                            }
                        }
                    } catch (Exception ex){}
                    }*/


                if(method.equals("get_cur_support")) {
                    try {
                        if (loading != null) loading.dismiss();
                        //  Toast.makeText(TripDetails.this, response, Toast.LENGTH_LONG).show();
                        Gson gson = new Gson();
                        current_support res = gson.fromJson(response, current_support.class);
                        if (res.getHandle().equals("02")) {  // account not found
                            Toast.makeText(TripDetails.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        } else if (res.getHandle().equals("10")) {   // account found
                            support s = res.getSupport();
                            // String cur_support = s.getUsername();
                            // Toast.makeText(TripDetails.this, s.getUsername(), Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(), MakeCallSinch.class);
                            if (s.getUsername() != null) {
                                i.putExtra("call_whome", s.getUsername());
                                i.putExtra("reciever_name", "الدعم الفني");
                                startActivity(i);
                            } else if (s.getUsername() == null) {
                                Toast.makeText(TripDetails.this, getResources().getString(R.string.tech_support_not_available), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(TripDetails.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ex){}
                }



                if(method.equals("check_statusTrip_chnaged")){
                    try {
                        if (loading != null) loading.dismiss();
                        //Toast.makeText(SplashActivity.this, response, Toast.LENGTH_LONG).show();
                        Gson gson = new Gson();
                        user_info_splash res = gson.fromJson(response, user_info_splash.class);
                        if (res.getHandle().equals("02")) {  // account not found
                            Toast.makeText(TripDetails.this, res.getMsg(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(TripDetails.this, LoginPhoneNumber.class);
                            startActivity(intent);
                            finish();
                        } else if (res.getHandle().equals("10")) {
                            //Toast.makeText(SplashActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                            user_info_splash2 user_info1 = res.getUser();
                            if (user_info1.getOrderStatus() == null) {
                            } else {
                                if (!user_info1.getOrderStatus().equals("C") &&
                                        !user_info1.getOrderStatus().equals("N") &&
                                        !user_info1.getOrderStatus().equals("")) {

                                    if (user_info1.getOrderStatus().equals("W")) {  // waiting
                                        new MySharedPreference(getApplicationContext()).setStringShared("order_id", String.valueOf(user_info1.getOrderId()));
                                        new MySharedPreference(getApplicationContext()).setStringShared("from_w_status", "splash");
                                        user_info_splash3 user_info2 = user_info1.getOrder();
                                        Intent i = new Intent(getApplicationContext(), MapActivity.class);
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


                                    if (user_info1.getOrderStatus().equals("D")) {  // accepted
                              /*  new MySharedPreference(getApplicationContext()).setStringShared("from_D_A_S_status", "splash");
                                new MySharedPreference(getApplicationContext()).setStringShared("order_id", String.valueOf(user_info1.getOrderId()));
                                new MySharedPreference(getApplicationContext()).setStringShared("order_status_splash", String.valueOf(user_info1.getOrderStatus()));
                                Intent intent = new Intent(TripDetails.this, TripDetails.class);
                                startActivity(intent);
                                finish();*/
                                    }
                                    if (user_info1.getOrderStatus().equals("A")) {   // arrived
                                        Captain_Arrived(getResources().getString(R.string.captain_Arrived));
                                    }
                                    if (user_info1.getOrderStatus().equals("S")) {   // start
                                        Captain_Start_trip(getResources().getString(R.string.trip_started));

                                    }
                                    // payment and review.
                                    if (user_info1.getOrderStatus().equals("E") || user_info1.getOrderStatus().equals("F")) {
                                        user_info_splash3 u= user_info1.getOrder();
                                        if(u.getRated().equals("0")) {  // not rated
                                            new MySharedPreference(getApplicationContext()).setStringShared("order_id",
                                                    String.valueOf(user_info1.getOrderId()));
                                            Intent intent = new Intent(TripDetails.this, PaymentAndReview.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else if (u.getRated().equals("1")){  // rated
                                            // stay here
                                        }
                                    }
                                } else if (user_info1.getOrderStatus().equals("C") ||
                                        user_info1.getOrderStatus().equals("N") ||
                                        user_info1.getOrderStatus().equals("") ||
                                        user_info1.getOrderStatus() == null) {
                                    Intent intent = new Intent(TripDetails.this, MapActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    } catch (Exception ex){}
                }




                if(method.equals("update_user_info")) {
                    try {
                        if (loading != null) loading.dismiss();
                        Gson gson = new Gson();
                        user_info_splash res = gson.fromJson(response, user_info_splash.class);
                        if (res.getHandle().equals("02")) {  // account not found
                            Toast.makeText(TripDetails.this, res.getMsg(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(TripDetails.this, LoginPhoneNumber.class);
                            startActivity(intent);
                            finish();
                        } else if (res.getHandle().equals("10")) {
                            user_info_splash2 user_info1 = res.getUser();
                            new MySharedPreference(getApplicationContext()).setStringShared("rate", user_info1.getRate());
                            new MySharedPreference(getApplicationContext()).setStringShared("balance", user_info1.getBalance());
                            stoptimertask();
                            startActivity(new Intent(getApplicationContext(), MapActivity.class));
                            finish();
                        } else if (res.getHandle().equals("02")) {
                            Toast.makeText(TripDetails.this,
                                    res.getMsg(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(TripDetails.this, res.getMsg(),
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ex){}
                }


                if(method.equals("get_cancel_reasons")) {
                    try {
                        if (loading != null) loading.dismiss();
                        Gson gson = new Gson();
                        Cancel_list_response res = gson.fromJson(response, Cancel_list_response.class);
                        if (res.getHandle().equals("02")) {  // account not found
                            Toast.makeText(TripDetails.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        } else if (res.getHandle().equals("10")) {
                            Cancels res2 = gson.fromJson(response, Cancels.class);

                            if (res.getCancels().size() > 0) {
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(TripDetails.this);
                                LayoutInflater inflater = getLayoutInflater();
                                final View convertView = (View) inflater.inflate(R.layout.dialoge_cancel_reasons, null);
                                ListView lv = (ListView) convertView.findViewById(R.id.List);
                                adapter = new Adapter_cancel2(res.getCancels(), getApplicationContext());
                                lv.setAdapter(adapter);
                                alertDialog.setView(convertView);
                                alertDialog.setTitle(getResources().getString(R.string.choose_reason));
                                final AlertDialog ad = alertDialog.show();
                                ad.show();

                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        try {
                                            Cancels selectedObject = adapter.getSelected(position);
                                            seleted_id_reasonCancel = String.valueOf(selectedObject.getId());
                                            ad.dismiss();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    method = "cancel";
                                                    Volley_go();
                                                }
                                            }, 2000);

                                        }catch (Exception ex){}
                                    }

                                });

                            }
                        } else if (res.getHandle().equals("02")) {
                            Toast.makeText(TripDetails.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(TripDetails.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ex){}
                }





                }

            @Override
            public void notifyError(VolleyError error) {
                if (loading != null) loading.dismiss();
                Toast.makeText(TripDetails.this,R.string.check_internet, Toast.LENGTH_LONG).show();

            }
        };
        iresultOrderStateChange= new IResult() {
            @Override
            public void notifySuccessPost(String response) {


                    try {
                        if (loading != null) loading.dismiss();
                        //Toast.makeText(SplashActivity.this, response, Toast.LENGTH_LONG).show();
                        Gson gson = new Gson();
                        user_info_splash res = gson.fromJson(response, user_info_splash.class);
                        if (res.getHandle().equals("02")) {  // account not found
                            Toast.makeText(TripDetails.this, res.getMsg(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(TripDetails.this, LoginPhoneNumber.class);
                            startActivity(intent);
                            finish();
                        } else if (res.getHandle().equals("10")) {
                            //Toast.makeText(SplashActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                            user_info_splash2 user_info1 = res.getUser();
                            if (user_info1.getOrderStatus() == null) {
                            } else {
                                if (!user_info1.getOrderStatus().equals("C") &&
                                        !user_info1.getOrderStatus().equals("N") &&
                                        !user_info1.getOrderStatus().equals("")) {

                                    if (user_info1.getOrderStatus().equals("W")) {  // waiting
                                        new MySharedPreference(getApplicationContext()).setStringShared("order_id", String.valueOf(user_info1.getOrderId()));
                                        new MySharedPreference(getApplicationContext()).setStringShared("from_w_status", "splash");
                                        user_info_splash3 user_info2 = user_info1.getOrder();
                                        Intent i = new Intent(getApplicationContext(), MapActivity.class);
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


                                    if (user_info1.getOrderStatus().equals("D")) {  // accepted
                              /*  new MySharedPreference(getApplicationContext()).setStringShared("from_D_A_S_status", "splash");
                                new MySharedPreference(getApplicationContext()).setStringShared("order_id", String.valueOf(user_info1.getOrderId()));
                                new MySharedPreference(getApplicationContext()).setStringShared("order_status_splash", String.valueOf(user_info1.getOrderStatus()));
                                Intent intent = new Intent(TripDetails.this, TripDetails.class);
                                startActivity(intent);
                                finish();*/
                                    }
                                    if (user_info1.getOrderStatus().equals("A")) {   // arrived
                                        Captain_Arrived(getResources().getString(R.string.captain_Arrived));
                                    }
                                    if (user_info1.getOrderStatus().equals("S")) {   // start
                                        Captain_Start_trip(getResources().getString(R.string.trip_started));

                                    }
                                    // payment and review.
                                    if (user_info1.getOrderStatus().equals("E") || user_info1.getOrderStatus().equals("F")) {
                                        user_info_splash3 u= user_info1.getOrder();
                                        if(u.getRated().equals("0")) {  // not rated
                                            new MySharedPreference(getApplicationContext()).setStringShared("order_id",
                                                    String.valueOf(user_info1.getOrderId()));
                                            Intent intent = new Intent(TripDetails.this, PaymentAndReview.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else if (u.getRated().equals("1")){  // rated
                                            // stay here
                                        }
                                    }
                                } else if (user_info1.getOrderStatus().equals("C") ||
                                        user_info1.getOrderStatus().equals("N") ||
                                        user_info1.getOrderStatus().equals("") ||
                                        user_info1.getOrderStatus() == null) {
                                    Intent intent = new Intent(TripDetails.this, MapActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    } catch (Exception ex){}












                }

            @Override
            public void notifyError(VolleyError error) {
                if (loading != null) loading.dismiss();
                Toast.makeText(TripDetails.this,R.string.check_internet, Toast.LENGTH_LONG).show();

            }
        };
        iresultOrderInfo= new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                     try {
                        if (loading != null) loading.dismiss();
                        Gson gson = new Gson();
                        ResponseWaiting res = gson.fromJson(response, ResponseWaiting.class);
                        if (res.getHandle().equals("10")) {
                            myOrder = res.getOrder();
                            driver_id = myOrder.getTransportId();
                            captainName = myOrder.getTransportName();
                            captainNumber = myOrder.getTransportPhone();
                            tracking_firebase();
                            if (!myOrder.getToLocationTxt().equals("") && myOrder.getToLocationTxt() != null) {
                                String final_destination_user = myOrder.getToLocation();
                                String[] separated_user_loc_final = final_destination_user.split(",");
                                final_dest_user = new LatLng(Double.parseDouble(separated_user_loc_final[0]),
                                        Double.parseDouble(separated_user_loc_final[1]));
                                final_des_found = "yes";
                            }
                            if (myOrder.getToLocationTxt().equals("") || myOrder.getToLocationTxt() == null) {
                                final_des_found = "no";
                            }


                            if (myOrder.getTime_to_cancel() == 0) {
                                btn_cancel.setText(getResources().getString(R.string.cancel_pay));
                            } else if (myOrder.getTime_to_cancel() > 0) {
                                cancel_order_counter2(myOrder.getTime_to_cancel());
                            }

                            tv_currentLocation.setText(myOrder.getLocationTxt());
                            tv_des_text.setText(myOrder.getToLocationTxt());
                            //if (myOrder)
                            // Toast.makeText(getApplicationContext(),myOrder.getTimetoUser(), Toast.LENGTH_SHORT).show();

                            int timetouser = Integer.parseInt(myOrder.getTimetoUser()) * 60;
                            // int timetouser = 1;

                            num_sec_saved_wosoul = new MySharedPreference(getApplicationContext()).getIntShared("timer_wousol_captain");
                            if (timetouser >= num_sec_saved_wosoul) {
                                num_wousoul = timetouser - num_sec_saved_wosoul;
                            } else if (timetouser < num_sec_saved_wosoul) {
                                num_wousoul = num_sec_saved_wosoul - timetouser;
                            }
                            final String vvv = getResources().getString(R.string.afew_min_left);
                            myCountDownTimer = new CountDownTimer(num_wousoul * 1000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                    tv_TimeToUSER.setText(" " + vvv + " " + String.format("%02d:%02d",
                                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                                    num_sec_saved_wosoul = num_sec_saved_wosoul + 1;
                                    new MySharedPreference(getApplicationContext()).setIntShared("timer_wousol_captain",
                                            num_sec_saved_wosoul);

                                }

                                public void onFinish() {
                                    tv_TimeToUSER.setText(getResources().getString(R.string.captain_is_close));
                                }
                            }.start();

                            tv_driver_name.setText(myOrder.getTransportName());
                            tv_car_model.setText(myOrder.getTransportCar());
                            // Toast.makeText(getApplicationContext(),myOrder.getTransportCar(), Toast.LENGTH_SHORT).show();

                            Glide.with(getApplicationContext())
                                    .load(myOrder.getTransportPhoto())
                                    .apply(new RequestOptions()
                                            .placeholder(R.drawable.user_imagehdpi)
                                            .centerCrop()
                                            .dontAnimate()
                                            .dontTransform())
                                    .into(icon_user);
                            Log.d("icoooon", myOrder.getTransportPhoto());


                            tv_car_plateNum.setText(myOrder.getPlateNo());
                            tv_rate.setText(myOrder.getTransportRate());
                            rb.setRating(Float.parseFloat(myOrder.getTransportRate()));
                            String driver_loc = myOrder.getTransportCoords();
                            String userLoc_START = myOrder.getLocation();
                            String[] separated_driver_loc = driver_loc.split(",");
                            LatLng driverLocation = new LatLng(Double.parseDouble(separated_driver_loc[0]),
                                    Double.parseDouble(separated_driver_loc[1]));

                       /* MarkerOptions markerOptions_driver = new MarkerOptions().
                                position(driverLocation).icon(icon_driver);
                        mMap.addMarker(markerOptions_driver);*/

                           /* marker = mMap.addMarker(new MarkerOptions()
                                    .position(driverLocation)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.markermdpi)));
*/

                            String[] separated_user_loc = userLoc_START.split(",");
                            UserLocationStart = new LatLng(Double.parseDouble(separated_user_loc[0]), Double.parseDouble(separated_user_loc[1]));
                            MarkerOptions markerOptionsss = new MarkerOptions().
                                    position(UserLocationStart).icon(icon_user_start);
                            mMap.addMarker(markerOptionsss);

                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            builder.include(driverLocation);
                            builder.include(UserLocationStart);
                            LatLngBounds bounds = builder.build();
                            int padding = 50;
                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                            mMap.moveCamera(cu);
                            mMap.animateCamera(cu);
                            /*String url = getUrl(driverLocation, UserLocationStart);
                            FetchUrl FetchUrl = new FetchUrl();
                            FetchUrl.execute(url);*/


                            if (!myOrder.getOrder_info().isEmpty()) {
                                trip_details_ll.setVisibility(View.GONE);
                                order_info.setVisibility(View.VISIBLE);
                                order_info.setText(myOrder.getOrder_info());
                            }
                            if (myOrder.getTaxi() != 1) {
                                isTaxi = false;

                                lin_cancelTrip.setVisibility(View.VISIBLE);
                                btn_cancel.setText(getResources().getString(R.string.cancel_pay));
                            }
                        }

                        String from_ = new MySharedPreference(getApplicationContext()).getStringShared("from_D_A_S_status");
                        if (from_.equals("normal")) {

                        } else if (from_.equals("splash")) {
                            if (new MySharedPreference(getApplicationContext()).getStringShared("order_status_splash").equals("D")) {
                                //cancel_order_counter();
                            } else if (new MySharedPreference(getApplicationContext()).getStringShared("order_status_splash").equals("A")) {
                                Captain_Arrived("وصل الكابتن");
                            } else if (new MySharedPreference(getApplicationContext()).getStringShared("order_status_splash").equals("S")) {
                                Captain_Start_trip("بدء الكابتن الرحلة");
                            }
                        }
                    } catch (Exception ex){
                        ex.printStackTrace();
                    }


                    tracking_firebase();




                }

            @Override
            public void notifyError(VolleyError error) {
                if (loading != null) loading.dismiss();
                Toast.makeText(TripDetails.this,R.string.check_internet, Toast.LENGTH_LONG).show();

            }
        };
    }


//This methos is used to move the marker of each car smoothly when there are any updates of their position
   /* public void animateMarker(final LatLng startPosition, final LatLng toPosition,
                              final boolean hideMarker) {

        try {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final long duration = 1 * 1000;
            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed
                            / duration);
                    double lng = t * toPosition.longitude + (1 - t)
                            * startPosition.longitude;
                    double lat = t * toPosition.latitude + (1 - t)
                            * startPosition.latitude;

                    marker.setPosition(new LatLng(lat, lng));

                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }
                }
            });
        } catch (Exception ex){}
    }
*/

   /* public  void onBackPressed(){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("لا تستطيع اتمام العملية!");
            alertDialogBuilder.setPositiveButton("اغلاق",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

    }
*/
















    private Boolean isTaxi = true;


    private void getDriverLocationUpdate() {

        if (isFirstPosition) {
           // Toast.makeText(getApplicationContext(), "isFirstPosition", Toast.LENGTH_LONG).show();
            startPosition = new LatLng(startLatitude, startLongitude);
            carMarker = mMap.addMarker(new MarkerOptions().position(startPosition).
                    flat(true).icon(BitmapDescriptorFactory.fromResource(isTaxi ? R.drawable.car:R.drawable.truck)));
            carMarker.setAnchor(0.5f, 0.5f);

            mMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition
                            (new CameraPosition.Builder()
                                    .target(startPosition)
                                    .zoom(15.5f)
                                    .build()));

            isFirstPosition = false;

        } else {
           // Toast.makeText(getApplicationContext(), "endPosition", Toast.LENGTH_LONG).show();

            endPosition = new LatLng(startLatitude, startLongitude);
            Log.d(TAG, startPosition.latitude + "--" + endPosition.latitude + "--Check --" + startPosition.longitude + "--" + endPosition.longitude);
            if ((startPosition.latitude != endPosition.latitude) || (startPosition.longitude != endPosition.longitude)) {
                Log.e(TAG, "NOT SAME");
                startBikeAnimation(startPosition, endPosition);

            } else {

                Log.e(TAG, "SAMME");
            }
        }
    }

    private void startBikeAnimation(final LatLng start, final LatLng end) {

        Log.i(TAG, "startBikeAnimation called...");

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(ANIMATION_TIME_PER_ROUTE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                //LogMe.i(TAG, "Car Animation Started...");
                v = valueAnimator.getAnimatedFraction();
                lng = v * end.longitude + (1 - v)
                        * start.longitude;
                lat = v * end.latitude + (1 - v)
                        * start.latitude;

                LatLng newPos = new LatLng(lat, lng);
                carMarker.setPosition(newPos);
                carMarker.setAnchor(0.5f, 0.5f);
                carMarker.setRotation(getBearing(start, end));

                // todo : Shihab > i can delay here
                mMap.moveCamera(CameraUpdateFactory
                        .newCameraPosition
                                (new CameraPosition.Builder()
                                        .target(newPos)
                                        .zoom(15.5f)
                                        .build()));

                startPosition = carMarker.getPosition();

            }

        });
        valueAnimator.start();
    }



    /*Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {

                getDriverLocationUpdate();


            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            handler.postDelayed(mStatusChecker, DELAY);

        }
    };

    void startGettingOnlineDataFromCar() {
        handler.post(mStatusChecker);
    }
*/





    public static float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }
}
