package com.turnpoint.ticram.tekram_driver.Activites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.turnpoint.ticram.tekram_driver.*;
import com.turnpoint.ticram.tekram_driver.Services.*;
import com.turnpoint.ticram.tekram_driver.Volley.IResult;
import com.turnpoint.ticram.tekram_driver.Volley.VolleyService;
import com.turnpoint.ticram.tekram_driver.modules.Current;
import com.turnpoint.ticram.tekram_driver.modules.Order;
import com.turnpoint.ticram.tekram_driver.modules.User;
import com.turnpoint.ticram.tekram_driver.modules.addcoordsfirebase;
import com.turnpoint.ticram.tekram_driver.modules.availabilityFirebase;
import com.turnpoint.ticram.tekram_driver.modules.myorders;
import com.turnpoint.ticram.tekram_driver.modules.savedOrder;
import com.turnpoint.ticram.tekram_driver.modules.usual_result;
import com.turnpoint.ticram.tekram_driver.retrofitServices.ApiUtils;
import com.turnpoint.ticram.tekram_driver.retrofitServices.SendLocationService;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MapsMain extends FragmentActivity implements OnMapReadyCallback {

    //private MyFirebaseMessagingService mReceiver;
    private GoogleMap mMap;
    double currentLatitude = 0.0, currentLongitude = 0.0;
    public static Context context;
    IResult iresult;
    VolleyService voly_ser;
    String method;
    CoordinatorLayout lay_main;
    BitmapDescriptor icon_driver;
    SendLocationService sendLoc;

    RelativeLayout content_drawer;
    DrawerLayout drawerLayout;
    TextView tv_drawer_driverName, tv_drawer_rate, tv_drawer_money, tv_locationText, tv_availability;
    ImageView img_driver;
    LinearLayout lin_my_orders, lin_logout;

    String onoff;
    ToggleButton toggleButton;
    private LocationManager mLocationManager = null;
    private static final String TAG = "LocServiceAfterTawklna";
    String provider;
    Criteria criteria;
    Location location;
    LocationListenerMAPSMain mLocationListener;
    RatingBar rb_menu;
    DatabaseReference mDatabase;
    ProgressBar progressBar;
    TextView tv_driver_name, tv_rate, tv_time, tv_distance, tv_payment_method, tv_timeTOuser, tv_timer, tv_acceptRatio;
    LinearLayout linear_cash, linear_dist_time, linear_all;
    ImageView icon_user;
    Button btn_tawklna;
    RatingBar rb;
    CountDownTimer myCountDownTimer;
    int ii;
    DatabaseReference connectedRef;
    private ValueEventListener mListener;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = MapsMain.this;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Al-Jazeera-Arabic-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.drawer_layout);

        Log.d("SEND_LOCATIONUrl", "SEND_LOCATION: " + new MySharedPreference(getApplicationContext())
                .getStringShared("base_url") + PathUrl.SEND_LOCATION);
        callBackVolly();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //startService(new Intent(getApplicationContext(),StartSinch.class));
        triger_alarm_sendLocBeforeTawklna();
        /*Intent intent = new Intent();
        intent.setAction("com.tecram.FirebaseBroadcastReceiver");
        sendBroadcast(intent);*/

        new MySharedPreference(getApplicationContext()).setIntShared("time_cancel_trip_saved", 0);


        //for order //
        linear_cash = findViewById(R.id.lin_cash);
        linear_dist_time = findViewById(R.id.lin_dis_time);
        tv_driver_name = findViewById(R.id.txtNameDriver);
        tv_rate = findViewById(R.id.txtRate);
        tv_time = findViewById(R.id.txtTime);
        tv_distance = findViewById(R.id.txtKm);
        tv_payment_method = findViewById(R.id.txtCash);
        tv_timeTOuser = findViewById(R.id.txtBaqya);
        tv_timer = findViewById(R.id.txt_timer);
        btn_tawklna = findViewById(R.id.button_tawklnaa);
        rb = findViewById(R.id.ratingBar);
        icon_user = findViewById(R.id.imgDriver);
        linear_all = findViewById(R.id.rtMain);
        /////////////


        sendLoc = ApiUtils.sendLoc();
        content_drawer = findViewById(R.id.left_drawer);
        drawerLayout = findViewById(R.id.drawer_layout);
        img_driver = findViewById(R.id.drawer_imgdriver);
        tv_drawer_driverName = findViewById(R.id.driver_name);
        tv_drawer_rate = findViewById(R.id.driver_rate);
        tv_drawer_money = findViewById(R.id.driver_money);
        tv_acceptRatio = findViewById(R.id.driver_ratioAccept);
        tv_locationText = findViewById(R.id.tv_locationtxt);
        tv_availability = findViewById(R.id.textView_availability);
        rb_menu = findViewById(R.id.ratingBar_me);
        lin_my_orders = findViewById(R.id.linlay_profile_myorders);

        lin_logout = findViewById(R.id.linlay_profile_logout);
        LinearLayout lin_shareApp = findViewById(R.id.linlay_profile_invite);
        LinearLayout linearlay_ordersDensity = findViewById(R.id.linear_ordersDensity);
        toggleButton = findViewById(R.id.toggleAvilable);
        progressBar = findViewById(R.id.progressBar2);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Change_Availability();
            }
        });

        String versionName = BuildConfig.VERSION_NAME;
        TextView tv_buildV = findViewById(R.id.tv_build_v_num);
        tv_buildV.setText(" نسخة رقم : " + versionName);

        linearlay_ordersDensity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(content_drawer);
                startActivity(new Intent(getApplicationContext(), OrdersDensity.class));

            }
        });

        lin_shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(content_drawer);
                try {
                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Ticram");
                    String sAux = "\nLet me recommend you this application\n\n";
                    // = sAux + "https://play.google.com/store/apps/details?id=" +appPackageName;
                    sAux = sAux + "http://www.ticram.com/invite.php";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });
        LinearLayout lin_support = findViewById(R.id.linlay_support);
        lin_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(content_drawer);
                startActivity(new Intent(getApplicationContext(), Support.class));
            }
        });
        LinearLayout lin_invitCaptain = findViewById(R.id.linlay_profile_invite);
        lin_invitCaptain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(content_drawer);
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Ticram");
                    String sAux = "http://new.ticram.com/reg-captain";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        lin_my_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MapsMain.this, "clicked", Toast.LENGTH_LONG).show();
                drawerLayout.closeDrawer(content_drawer);
                startActivity(new Intent(getApplicationContext(), MyOrders.class));

            }
        });

        lin_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapsMain.this);
                alertDialogBuilder.setMessage("هل انت متأكد؟");
                alertDialogBuilder.setPositiveButton("نعم",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // stopService(new Intent(getApplicationContext(),StartSinch.class));
                                method = "logout";
                                Volley_go();

                            }
                        });
                alertDialogBuilder.setNegativeButton("لا",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        img_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(MapsMain.this, "clicked", Toast.LENGTH_LONG).show();
                drawerLayout.closeDrawer(content_drawer);
                startActivity(new Intent(getApplicationContext(), EditProfile.class));
            }
        });
        Glide.with(getApplicationContext()).load(new MySharedPreference
                (getApplicationContext()).getStringShared("photo")).into(img_driver);
        tv_drawer_driverName.setText(new MySharedPreference(getApplicationContext()).getStringShared("user_name"));
        tv_drawer_money.setText(new MySharedPreference(getApplicationContext()).getStringShared("balance") + " د.أ ");
        tv_drawer_rate.setText(new MySharedPreference(getApplicationContext()).getStringShared("rate"));
        rb_menu.setRating(Float.parseFloat(new MySharedPreference(getApplicationContext()).getStringShared("rate")));

        new MySharedPreference(getApplicationContext()).setStringShared("order_selected", "no");
        new MySharedPreference(getApplicationContext()).setStringShared("finish_trip", "no");

        icon_driver = BitmapDescriptorFactory.fromResource(R.drawable.markermdpi);

        lay_main = findViewById(R.id.relative);


        //================drawer ===============================================
        ImageView img_drawer = findViewById(R.id.imageView5);
        img_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    drawerLayout.openDrawer(content_drawer);

                } catch (Exception ex) {
                }

            }
        });


        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {
            }

            @Override
            public void onDrawerOpened(View view) {
                try {
                    tv_drawer_driverName.setText(new MySharedPreference(getApplicationContext()).getStringShared("user_name"));
                    tv_drawer_money.setText(new MySharedPreference(getApplicationContext()).getStringShared("balance") + " د.أ ");
                    tv_drawer_rate.setText(new MySharedPreference(getApplicationContext()).getStringShared("rate"));
                    rb_menu.setRating(Float.parseFloat(new MySharedPreference(getApplicationContext()).getStringShared("rate")));
                } catch (Exception ex) {
                }

            }

            @Override
            public void onDrawerClosed(View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ask_forPermission_floatIcon();

        //addNotification("hi");
        //Log.d("ssssssssssss", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));

        Intent ll24 = new Intent(MapsMain.this, AlarmReceiverLifeLog.class);
        PendingIntent recurringLl24 = PendingIntent.getBroadcast(MapsMain.this, 1220, ll24, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarms.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, recurringLl24);
        rangeCount = 0;
        enableThread = true;
        statusHandler = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                if (!enableThread) return;
                if (rangeCount < 60) rangeCount += 5;
                method = "get_current_orders";
                Volley_go();
                statusHandler.postDelayed(this, rangeCount * 1000);
            }
        };
        statusHandler.post(run);

    }

    Integer rangeCount = 0;
    Boolean enableThread = false;
    Handler statusHandler;
    BroadcastReceiver receiver;

    public void ask_forPermission_floatIcon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 987);

            } else if (Settings.canDrawOverlays(getApplicationContext())) {

            }

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 987) {
            if (resultCode == RESULT_OK) {
                // ask_forPermission_floatIcon();
               /* Intent intent = new Intent(MapsMain.this, MapsMain.class);
                startActivity(intent);*/

            } else {
                //ask_forPermission_floatIcon();
               /* Intent intent = new Intent(MapsMain.this, MapsMain.class);
                startActivity(intent);*/
            }
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void Change_Availability() {
        if (!new MySharedPreference(getApplicationContext()).getStringShared("available").equals("")) {
            if (new MySharedPreference(getApplicationContext()).getStringShared("available").equals("on")) {
                onoff = "0";
                method = "availability_update";
                Volley_go();

            } else if (new MySharedPreference(getApplicationContext()).getStringShared("available").equals("off")) {
                method = "availability_update";
                onoff = "1";
                Volley_go();


            }
        } else if (new MySharedPreference(getApplicationContext()).getStringShared("available").equals("")) {
            method = "availability_update";
            onoff = "0";
            Volley_go();

        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            if (mLocationManager != null) {
                mLocationManager.removeUpdates(mLocationListener);
            }
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    public void triger_alarm_sendLocBeforeTawklna() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(new Intent(getApplicationContext(),
                    LocationServiceBeforeTawaklna.class));
        } else {
            startService(new Intent(getApplicationContext(),
                    LocationServiceBeforeTawaklna.class));
        }
    }


    public void stopAlarmManager() {
        stopService(new Intent(getApplicationContext(), LocationServiceBeforeTawaklna.class));
        stopService(new Intent(getApplicationContext(), MyLocationServiceAfter.class));
        // stopService(new Intent(getApplicationContext(), CancelTripListner.class));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //permission not granted
        if (ActivityCompat.checkSelfPermission(MapsMain.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsMain.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsMain.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    200);

        } else {
            creatEventMap();
            initializeLocationManager();
        }

    }


    public void creatEventMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                mMap.clear();
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
                try {

                    LatLng mylatlng = new LatLng(currentLatitude, currentLongitude);
                    //salam
                    icon_driver = BitmapDescriptorFactory.fromResource(R.drawable.markermdpi);
                    MarkerOptions markerOptionsss = new MarkerOptions().position(mylatlng).icon(icon_driver);
                    mMap.addMarker(markerOptionsss);

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylatlng, 14.0f));
                    /*Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);
                        if (addresses != null && addresses.size() > 0) {
                            String address = addresses.get(0).getAddressLine(0);
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();
                            tv_locationText.setText(address);
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }*/
                } catch (Exception ex) {
                }
            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 200: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    creatEventMap();
                    initializeLocationManager();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    ActivityCompat.requestPermissions(MapsMain.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            200);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        new MySharedPreference(getApplicationContext()).setStringShared("MapsMain_act", "foreground");
        //Toast.makeText(getApplicationContext(),new MySharedPreference(getApplicationContext()).getStringShared("available"), Toast.LENGTH_LONG ).show();
        //============on off available==========================================

       /* IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.turnpoint.ticram.tekram_driver.UpdateAvailable");
        BroadcastReceiver_UpdateAvailable receiver = new BroadcastReceiver_UpdateAvailable();
        registerReceiver(receiver, intentFilter);*/
        Availability_firebase();

        if (!new MySharedPreference(getApplicationContext()).getStringShared("available").equals("")) {
            if (new MySharedPreference(getApplicationContext()).getStringShared("available").equals("on")) {
                Drawable image = getResources().getDrawable(R.drawable.avilable_icon);
                toggleButton.setBackground(image);

            } else if (new MySharedPreference(getApplicationContext()).getStringShared("available").equals("off")) {
                Drawable image = getResources().getDrawable(R.drawable.not_avilable_icon);
                toggleButton.setBackground(image);
                tv_availability.setVisibility(View.VISIBLE);
            }
        } else if (new MySharedPreference(getApplicationContext()).getStringShared("available").equals("")) {
            Drawable image = getResources().getDrawable(R.drawable.avilable_icon);
            toggleButton.setBackground(image);
        }
        //============================================================


    }


    ProgressDialog loading = null;

    @SuppressLint("LongLogTag")
    public void Volley_go() {
        if (new MySharedPreference(getApplicationContext()).getStringShared("login_status").contains("logout")) {
            return;
        }
        if (method.equals("get_current_orders")) {
            try {
                progressBar.setVisibility(View.VISIBLE);
                //  loading = ProgressDialog.show(MapsMain.this, "", "الرجاء الانتظار...", false, false);
                Map<String, String> params = new Hashtable<String, String>();
                params.put("local", "ara");
                params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                params.put("location", currentLatitude + "," + currentLongitude); //  change_me
                // params.put("location", "32.563850" + "," + "35.860933");
                voly_ser = new VolleyService(new IResult() {
                    @Override
                    public void notifySuccessPost(String response) {
                        if (loading != null) {
                            loading.dismiss();
                            loading = null;
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        if (new MySharedPreference(getApplicationContext()).getStringShared("login_status").contains("logout")) {
                            return;
                        }
                        Log.d("get_current_orders", response);
                        try {
                            Gson gson = new Gson();
                            myorders res = gson.fromJson(response, myorders.class);
                            if (res.getHandle().equals("02")) {
                                Toast.makeText(MapsMain.this, res.getMsg(), Toast.LENGTH_LONG).show();
                            } else if (res.getHandle().equals("10")) {

                                User user = res.getTransport();
                                if (user.getAvailable().equals("-1")) {
                                    new MySharedPreference(getApplicationContext()).setStringShared("available", "off");
                                    tv_availability.setVisibility(View.VISIBLE);
                                    Drawable image = getResources().getDrawable(R.drawable.not_avilable_icon);
                                    toggleButton.setBackground(image);

                                } else if (!user.getAvailable().equals("-1")) {
                                    new MySharedPreference(getApplicationContext()).setStringShared("available", "on");
                                    tv_availability.setVisibility(View.INVISIBLE);
                                    Drawable image = getResources().getDrawable(R.drawable.avilable_icon);
                                    toggleButton.setBackground(image);
                                }
                                new MySharedPreference(getApplicationContext()).
                                        setStringShared("balance", user.getBalance());
                                tv_acceptRatio.setText(user.getAcceptance_ratio());
                                Order order = res.getOrder();
                                if (order.getUserName() != null && order.getUserName() != "") { //theres order
                                    new savedOrder().saveorder(order);

                                    if (order.getStatus().equals("E")) {
                                        if (!FinishTrip.isVisible) {
                                            new MySharedPreference(getApplicationContext()).setStringShared("cur_order_id",
                                                    String.valueOf(order.getId()));
                                            Paper.book().write("startDistanceCount", false);
                                            new MySharedPreference(getApplicationContext()).setBooleanShared("startDistanceCount", false);
                                            Intent i = new Intent(getApplicationContext(), FinishTrip.class);
                                            i.putExtra("from", "splash");
                                            startActivity(i);
                                            finish();
                                        }
                                    } else {
                                        new MySharedPreference(getApplicationContext()).setStringShared("cur_order_id",
                                                String.valueOf(order.getId()));
                                        if (!ViewDetailsOrder.isVisible) {
                                            Intent i = new Intent(getApplicationContext(), ViewDetailsOrder.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            i.putExtra("from_act", "splash");
                                            startActivity(i);
                                            finish();
                                        }
                                    }

                                } else {
                                    Paper.book().write("startDistanceCount", false);
                                    new MySharedPreference(getApplicationContext()).setBooleanShared("startDistanceCount", false);
                                }

                                //////..... check if theres an incoming order
                                /*else if (order.getUserName() == null || order.getUserName() == "") {
                                    List<Current> list_orders = res.getCurrent();
                                    final Current item = list_orders.get(0);
                                    if (item.getOrderId() != null) {
                                        try {
                                            Intent serviceIntent = new Intent(getApplicationContext(), WidgetNewOrder.class);
                                            serviceIntent.putExtra("order_id", ""+item.getOrderId());
                                            serviceIntent.putExtra("time_to_user", item.getTimeToUser());
                                            serviceIntent.putExtra("distance_to_user", item.getDistance());
                                            serviceIntent.putExtra("user_name", item.getUserName());
                                            serviceIntent.putExtra("user_photo", item.getUserPhoto());
                                            serviceIntent.putExtra("user_rate", item.getUserRate());
                                            serviceIntent.putExtra("location_text", item.getOrigin_address());
                                            serviceIntent.putExtra("destination_text", item.getDestination_address());
                                            startService(serviceIntent);
                                        } catch (Exception ex) {
                                        }
                                    }
                                }*/
                            } else {
                                Toast.makeText(MapsMain.this, res.getMsg(), Toast.LENGTH_LONG).show();
                            }


                            Double updateVersion = Double.parseDouble(res.getTransport().getAndroid_version());
                            Double currentVersion = Double.parseDouble(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);

                            if (updateVersion > currentVersion && updateAlert == null) {
                                updateDialogBuilder = new AlertDialog.Builder(MapsMain.this);
                                updateDialogBuilder.setMessage("يرجى تحديث تطبيق تكرم الى احدث اصدار لتتمكن من الاستفادة من كل المميزات الجديدة")
                                        .setTitle("هناك اصدار جديد " + updateVersion)
                                        .setCancelable(false)
                                        .setPositiveButton("تحديث",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        updateAlert.dismiss();
                                                        final String appPackageName = getPackageName();
                                                        try {
                                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                        } catch (android.content.ActivityNotFoundException anfe) {
                                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                        }
                                                    }
                                                }
                                        )
                                        .setNegativeButton("تجاهل",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        updateAlert.dismiss();
                                                    }
                                                }
                                        );
                                updateAlert = updateDialogBuilder.create();
                                updateAlert.show();
                            }
                        } catch (Exception ex) {
                        }

                    }

                    @Override
                    public void notifyError(VolleyError error) {
                        //loading.dismiss();
                        progressBar.setVisibility(View.INVISIBLE);
                        if (loading != null) {
                            loading.dismiss();
                            loading = null;
                        }
                        Toast.makeText(MapsMain.this, " مشكلة بالاتصال بالانترنت! ", Toast.LENGTH_LONG).show();

                        // Toast.makeText(MapsMain.this, "Volley Error" + error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }, getApplicationContext());
                voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url") + PathUrl.TransportInfo, params);
            } catch (Exception ex) {
            }
        }


        if (method.equals("availability_update")) {
            loading = ProgressDialog.show(MapsMain.this, "", "الرجاء الانتظار...", false, false);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("local", "ara");
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("val", onoff);
            voly_ser = new VolleyService(new IResult() {
                @Override
                public void notifySuccessPost(String response) {
                    if (loading != null) {
                        loading.dismiss();
                        loading = null;
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    if (new MySharedPreference(getApplicationContext()).getStringShared("login_status").contains("logout")) {
                        return;
                    }


                    //  loading.dismiss();
                    Gson gson = new Gson();
                    usual_result res = gson.fromJson(response, usual_result.class);
                    if (res.getHandle().equals("02")) {
                        Toast.makeText(MapsMain.this, "Error 02 :" + res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {
                        Toast.makeText(MapsMain.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MapsMain.this, "Not Success :" + res.getMsg(), Toast.LENGTH_LONG).show();
                    }
                    method = "get_current_orders";
                    Volley_go();

                }

                @Override
                public void notifyError(VolleyError error) {
                    //loading.dismiss();
                    progressBar.setVisibility(View.INVISIBLE);
                    if (loading != null) {
                        loading.dismiss();
                        loading = null;
                    }
                    Toast.makeText(MapsMain.this, " مشكلة بالاتصال بالانترنت! ", Toast.LENGTH_LONG).show();

                    // Toast.makeText(MapsMain.this, "Volley Error" + error.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url") + PathUrl.Availability, params);
            Log.d("testttttting", new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url") + PathUrl.Availability);
        }

        if (method.equals("logout")) {
            //  loading = ProgressDialog.show(MapsMain.this, "","الرجاء الانتظار...", false, false);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("local", "ara");
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            voly_ser = new VolleyService(new IResult() {
                @Override
                public void notifySuccessPost(String response) {
                    if (loading != null) {
                        loading.dismiss();
                        loading = null;
                    }
                    if (progressBar != null) progressBar.setVisibility(View.INVISIBLE);
                    stopAlarmManager();
                    new MySharedPreference(getApplicationContext()).setStringShared("login_status", "logout");


                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                    Gson gson = new Gson();
                    usual_result res = gson.fromJson(response, usual_result.class);
                    if (res.getHandle().equals("02")) {
                    } else if (res.getHandle().equals("10")) {
                    } else {
                        Toast.makeText(MapsMain.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void notifyError(VolleyError error) {
                    //loading.dismiss();
                    progressBar.setVisibility(View.INVISIBLE);
                    if (loading != null) {
                        loading.dismiss();
                        loading = null;
                    }
                    Toast.makeText(MapsMain.this, " مشكلة بالاتصال بالانترنت! ", Toast.LENGTH_LONG).show();

                    // Toast.makeText(MapsMain.this, "Volley Error" + error.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url") + PathUrl.LOGOUT, params);
        }


    }


    public void callBackVolly() {

        iresult = new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                if (loading != null) {
                    loading.dismiss();
                    loading = null;
                }
                progressBar.setVisibility(View.INVISIBLE);
                if (new MySharedPreference(getApplicationContext()).getStringShared("login_status").contains("logout")) {
                    return;
                }
                if (method.equals("get_current_orders")) {
                    Log.d("get_current_orders", response);
                    try {
                        Gson gson = new Gson();
                        myorders res = gson.fromJson(response, myorders.class);
                        if (res.getHandle().equals("02")) {
                            Toast.makeText(MapsMain.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        } else if (res.getHandle().equals("10")) {

                            User user = res.getTransport();
                            if (user.getAvailable().equals("-1")) {
                                new MySharedPreference(getApplicationContext()).setStringShared("available", "off");
                                tv_availability.setVisibility(View.VISIBLE);

                                Drawable image = getResources().getDrawable(R.drawable.not_avilable_icon);
                                toggleButton.setBackground(image);

                            } else if (!user.getAvailable().equals("-1")) {
                                new MySharedPreference(getApplicationContext()).setStringShared("available", "on");
                                tv_availability.setVisibility(View.INVISIBLE);
                                Drawable image = getResources().getDrawable(R.drawable.avilable_icon);
                                toggleButton.setBackground(image);
                            }
                            new MySharedPreference(getApplicationContext()).
                                    setStringShared("balance", user.getBalance());
                            tv_acceptRatio.setText(user.getAcceptance_ratio());
                            Order order = res.getOrder();
                            if (order.getUserName() != null && order.getUserName() != "") { //theres order
                                new savedOrder().saveorder(order);

                                if (order.getStatus().equals("E")) {
                                    Paper.book().write("startDistanceCount", false);
                                    new MySharedPreference(getApplicationContext()).setBooleanShared("startDistanceCount", false);
                                    if (!FinishTrip.isVisible) {
                                        Paper.book().write("startDistanceCount", false);
                                        new MySharedPreference(getApplicationContext()).setBooleanShared("startDistanceCount", false);
                                        new MySharedPreference(getApplicationContext()).setStringShared("cur_order_id",
                                                String.valueOf(order.getId()));
                                        Intent i = new Intent(getApplicationContext(), FinishTrip.class);
                                        i.putExtra("from", "splash");
                                        startActivity(i);
                                        finish();
                                    }
                                } else {
                                    new MySharedPreference(getApplicationContext()).setStringShared("cur_order_id",
                                            String.valueOf(order.getId()));
                                    if (!ViewDetailsOrder.isVisible) {
                                        Intent i = new Intent(getApplicationContext(), ViewDetailsOrder.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        i.putExtra("from_act", "splash");
                                        startActivity(i);
                                        finish();
                                    }
                                }

                            }

                            //////..... check if theres an incoming order
                            else if (order.getUserName() == null || order.getUserName() == "") {
                                List<Current> list_orders = res.getCurrent();
                                final Current item = list_orders.get(0);
                                if (item.getOrderId() != null) {

                                    try {
                                        Intent serviceIntent = new Intent(getApplicationContext(), WidgetNewOrder.class);
                                        serviceIntent.putExtra("order_id", item.getOrderId());
                                        serviceIntent.putExtra("time_to_user", item.getTimeToUser());
                                        serviceIntent.putExtra("distance_to_user", item.getDistance());
                                        serviceIntent.putExtra("user_name", item.getUserName());
                                        serviceIntent.putExtra("user_photo", item.getUserPhoto());
                                        serviceIntent.putExtra("user_rate", item.getUserRate());
                                        serviceIntent.putExtra("location_text", item.getOrigin_address());
                                        serviceIntent.putExtra("destination_text", item.getDestination_address());
                                        startService(serviceIntent);
                                    } catch (Exception ex) {
                                    }


                                }
                            } else {
                                Paper.book().write("startDistanceCount", false);
                                new MySharedPreference(getApplicationContext()).setBooleanShared("startDistanceCount", false);
                            }
                        } else {
                            Toast.makeText(MapsMain.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ex) {
                    }

                }


                if (method.equals("availability_update")) {
                    //  loading.dismiss();
                    Gson gson = new Gson();
                    usual_result res = gson.fromJson(response, usual_result.class);
                    if (res.getHandle().equals("02")) {
                        Toast.makeText(MapsMain.this, "Error 02 :" + res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {
                        Toast.makeText(MapsMain.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MapsMain.this, "Not Success :" + res.getMsg(), Toast.LENGTH_LONG).show();
                    }
                    method = "get_current_orders";
                    Volley_go();
                }

                if (method.equals("logout")) {
                    //  loading.dismiss();
                    stopAlarmManager();
                    new MySharedPreference(getApplicationContext()).setStringShared("login_status", "logout");
                    startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                    finish();
                    Gson gson = new Gson();
                    usual_result res = gson.fromJson(response, usual_result.class);
                    if (res.getHandle().equals("02")) {
                    } else if (res.getHandle().equals("10")) {
                    } else {
                        Toast.makeText(MapsMain.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    }
                }


            }

            @Override
            public void notifyError(VolleyError error) {
                //loading.dismiss();
                progressBar.setVisibility(View.INVISIBLE);
                if (loading != null) {
                    loading.dismiss();
                    loading = null;
                }
                Toast.makeText(MapsMain.this, " مشكلة بالاتصال بالانترنت! ", Toast.LENGTH_LONG).show();

                // Toast.makeText(MapsMain.this, "Volley Error" + error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };
    }

    static AlertDialog.Builder updateDialogBuilder;
    static AlertDialog updateAlert;

    public void timer_order(final int num) {
        myCountDownTimer = new CountDownTimer(num * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                tv_timer.setText(String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                // Toast.makeText(getApplicationContext(), "mmmm", Toast.LENGTH_SHORT).show();
            }

            public void onFinish() {
                linear_all.setVisibility(View.INVISIBLE);
            }
        }.start();

    }


    @Override
    protected void onPause() {
        super.onPause();
        new MySharedPreference(getApplicationContext()).setStringShared("MapsMain_act", "background");

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        enableThread = false;
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
        try {
            if (receiver != null)
                this.unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleNewLocation() {

        //Toast.makeText(getApplicationContext(), "hhhhhh", Toast.LENGTH_LONG).show();
        try {
            mMap.clear();
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            LatLng mylocation = new LatLng(currentLatitude, currentLongitude);
            //salam
            icon_driver = BitmapDescriptorFactory.fromResource(R.drawable.markermdpi);
            MarkerOptions markerOptionsss = new MarkerOptions().position(mylocation).icon(icon_driver);
            mMap.addMarker(markerOptionsss);

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 14.0f));
            /*Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    tv_locationText.setText(address);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }*/
            firebase_send_currentLocation(currentLatitude, currentLongitude);
            method = "get_current_orders";
            Volley_go();

         /*  if (new MySharedPreference(getApplicationContext()).getStringShared("first_login").equals("yes")) {
                firebase_send_currentLocation(currentLatitude, currentLongitude);
               // firebase(32.563850, 35.860933);
                new MySharedPreference(getApplicationContext()).setStringShared("first_login", "no");
            } else if (new MySharedPreference(getApplicationContext()).
                    getStringShared("first_login").equals("no")) {
                firebase_send_currentLocation(currentLatitude, currentLongitude);
                method = "get_current_orders";
                Volley_go();
            }*/

        } catch (Exception ex) {
        }
    }


    private void firebase_send_currentLocation(double lat, double lon) {
        //Seperate Address to Lat & Long and get the Id
            try {
                // Check if Current Location inside Geo Zone
                mDatabase = FirebaseDatabase.getInstance().getReference();
                addcoordsfirebase values = new addcoordsfirebase(String.valueOf(lat) + "," + String.valueOf(lon));
                mDatabase.child("drivers").child(new MySharedPreference(getApplicationContext()).getStringShared("user_id")).child("info").setValue(values);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
    }


    private class LocationListenerMAPSMain implements LocationListener {

        public LocationListenerMAPSMain(String provider) {

        }

        @Override
        public void onLocationChanged(Location location_) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getApplicationContext(),
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

            if (location_ != null) {
                location = mLocationManager.getLastKnownLocation(provider);
                //currentLatitude = location.getLatitude();
                // currentLongitude = location.getLongitude();
                //handleNewLocation();
            } else if (location_ == null) {
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                //currentLatitude = location.getLatitude();
                // currentLongitude = location.getLongitude();
                //handleNewLocation();

            }
            //Toast.makeText(getApplicationContext(),String.valueOf(currentLatitude)+","+String.valueOf(currentLongitude),Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
            // Toast.makeText(getApplicationContext(),"onProviderDisabled",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
            //  Toast.makeText(getApplicationContext(),"onProviderEnabled",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
            //  Toast.makeText(getApplicationContext(),"onStatusChanged",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter("tecram.action.refresh");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getStringExtra("order") != null) {
                    if (intent.getStringExtra("order").equalsIgnoreCase("finish")) {
                        method = "get_current_orders";
                        Volley_go();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                method = "get_current_orders";
                                Volley_go();
                            }
                        }, 5000);
                    }
                }
            }
        };
        this.registerReceiver(receiver, intentFilter);
    }

    private void initializeLocationManager() {

        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setPowerRequirement(Criteria.POWER_HIGH);
            provider = mLocationManager.getBestProvider(criteria, true);
            try {
                mLocationListener = new LocationListenerMAPSMain(provider);
                mLocationManager.requestLocationUpdates(
                        provider,
                        1 * 1000,
                        1,
                        mLocationListener);

            } catch (SecurityException ex) {
                // Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                //Log.d(TAG, "network provider does not exist, " + ex.getMessage());
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            location = mLocationManager.getLastKnownLocation(provider);
            if (location != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if(! location.isFromMockProvider())
                        handleNewLocation();
                }
            }

            //  Toast.makeText(getApplicationContext(), String.valueOf(location.getLatitude())+ String.valueOf(location.getLongitude()) , Toast.LENGTH_LONG).show();
            else if (location == null) {
                mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                boolean network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (network_enabled) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        // Toast.makeText(getApplicationContext(), String.valueOf(location.getLatitude())+ String.valueOf(location.getLongitude()) , Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            if(! location.isFromMockProvider())
                                handleNewLocation();
                        }
                    }
                }
            }
        }
    }

         /* private void addNotification(String title) {
              String idChannel = "channel_notification";
              NotificationChannel mChannel = null;
              int importance = NotificationManager.IMPORTANCE_HIGH;
              NotificationManager mNotificationManager = (NotificationManager)
                      getSystemService(Context.NOTIFICATION_SERVICE);
              Intent notificationIntent = new Intent(this, MapsMain.class);
              PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                      notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
              Intent notificationIntentOpen = new Intent(this, MyOrders.class);
              PendingIntent contentIntent_open = PendingIntent.getActivity(this, 0,
                      notificationIntentOpen, PendingIntent.FLAG_UPDATE_CURRENT);
              // Uri notf_sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
              NotificationCompat.Builder builder =
                      new NotificationCompat.Builder(this)
                              .setSmallIcon(R.drawable.logo)
                              .setContentTitle("Ticram")
                              .setContentText(title)
                              .setAutoCancel(true)
                              .addAction(R.drawable.ic_open, "Open", contentIntent_open)
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
          }*/


/*
public class BroadcastReceiver_UpdateAvailable extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
         if(!((Activity) context).isFinishing()) {
             new MySharedPreference(getApplicationContext()).setStringShared("available", "off");
             tv_availability.setVisibility(View.VISIBLE);
              Drawable image = getResources().getDrawable(R.drawable.off);
                img_onoff.setBackground(image);
            }
    }
}*/


    public void Availability_firebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        connectedRef = database.getReference("tikram-192101");
        mListener = connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("availability")
                        .child(new MySharedPreference(getApplicationContext()).getStringShared("user_id"))
                        .addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.exists()) {
                                    //addcoordsfirebase values = dataSnapshot.getValue(addcoordsfirebase.class);
                                    availabilityFirebase values = dataSnapshot.getValue(availabilityFirebase.class);

                                    try {
                                        String avail = values.available;
                                        if (avail.equals("1")) {
                                            method = "get_current_orders";
                                            Volley_go();
                                        } else if (avail.equals("-1")) {
                                            method = "get_current_orders";
                                            Volley_go();
                                        }
                                        // Toast.makeText(getApplicationContext(), values.coords, Toast.LENGTH_LONG).show();

                                    } catch (Exception ex) {
                                    }
                                }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.exists()) {
                                    availabilityFirebase values = dataSnapshot.getValue(availabilityFirebase.class);

                                    try {
                                        String avail = values.available;
                                        if (avail.equals("1")) {
                                            method = "get_current_orders";
                                            Volley_go();
                                        } else if (avail.equals("-1")) {
                                            method = "get_current_orders";
                                            Volley_go();
                                        }
                                        // Toast.makeText(getApplicationContext(), values.coords, Toast.LENGTH_LONG).show();

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

    private void checkAndupdateVersion() {
        try {
            if (PathUrl.VERSION_NUMBER.equals(new MySharedPreference(getApplicationContext()).getStringShared("VERSION_NUMBER")))
                return;
            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", "ara");
            params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("version", PathUrl.VERSION_NUMBER);
            voly_ser = new VolleyService(new IResult() {
                @Override
                public void notifySuccessPost(String response) {
                    new MySharedPreference(getApplicationContext()).setStringShared("VERSION_NUMBER", PathUrl.VERSION_NUMBER);
                }

                @Override
                public void notifyError(VolleyError error) {
                }
            }, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url") + PathUrl.updateVersion, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
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