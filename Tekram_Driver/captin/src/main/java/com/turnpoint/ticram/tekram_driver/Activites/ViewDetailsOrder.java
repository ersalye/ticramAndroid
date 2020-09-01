package com.turnpoint.ticram.tekram_driver.Activites;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.turnpoint.ticram.tekram_driver.DBHelper2;
import com.turnpoint.ticram.tekram_driver.DataParser;
import com.turnpoint.ticram.tekram_driver.MySharedPreference;
import com.turnpoint.ticram.tekram_driver.PathUrl;
import com.turnpoint.ticram.tekram_driver.R;
import com.turnpoint.ticram.tekram_driver.Services.CancelTripListner;
import com.turnpoint.ticram.tekram_driver.Services.MyLocationServiceAfter;
import com.turnpoint.ticram.tekram_driver.Volley.IResult;
import com.turnpoint.ticram.tekram_driver.Volley.VolleyService;
import com.turnpoint.ticram.tekram_driver.modules.Check;
import com.turnpoint.ticram.tekram_driver.modules.FollowCooridnates;
import com.turnpoint.ticram.tekram_driver.modules.Order;
import com.turnpoint.ticram.tekram_driver.modules.OrderDetails;
import com.turnpoint.ticram.tekram_driver.modules.addOrderFirebase;
import com.turnpoint.ticram.tekram_driver.modules.check_order;
import com.turnpoint.ticram.tekram_driver.modules.current_support;
import com.turnpoint.ticram.tekram_driver.modules.end_trip;
import com.turnpoint.ticram.tekram_driver.modules.savedOrder;
import com.turnpoint.ticram.tekram_driver.modules.support;
import com.turnpoint.ticram.tekram_driver.modules.usual_result;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;
import com.txusballesteros.bubbles.OnInitializedCallback;
import com.yayandroid.locationmanager.base.LocationBaseActivity;
import com.yayandroid.locationmanager.configuration.DefaultProviderConfiguration;
import com.yayandroid.locationmanager.configuration.LocationConfiguration;
import com.yayandroid.locationmanager.constants.ProviderType;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ViewDetailsOrder extends LocationBaseActivity implements OnMapReadyCallback {
    private BubblesManager bubblesManager;

    private GoogleMap mMap;
    // int  pos;
    String order_id;
    public TextView tv_driver_name, tv_rate, tv_time, tv_distance, tv_payment_method, tv_timeTOuser,
            tv_locationText, tv_HowFar, txt_info;
    public ImageView icon_user, icon_call;
    String method;
    //public ProgressDialog loading;
    ProgressBar progressBar;

    IResult iresult;
    IResult iresultOrderDetails;
    IResult iresultUpdateStatusTrip;
    IResult iresultFinishTrip;
    IResult iresultFinishUpdateInfo;
    VolleyService voly_ser;
    String btn_type = "tawaklna";
    BitmapDescriptor icon_driver;
    BitmapDescriptor icon_user_start;
    BitmapDescriptor icon_user_destination;

    Marker marker_user_start, marker_user_destination, marker_driver_location;
    LatLng driver_loc;
    LatLng user_loc_final = null, user_loc_start = null;
    String trip_status = "";

    public static final String TAG = MapsMain.class.getSimpleName();
    double driver_cur_lat;
    double driver_cur_lng;
    String onbackPress_status = "can_go_back";
    String can_cancel_trip;
    String from_what_Act;
    ImageView img_nav;
    Button btn_tawklna, btn_cancel;

    TimerTask timerTask;
    final Handler handler = new Handler();
    String done_loading_details = "no";
    //String order_status_splash;
    String userName = "", userRate = "", userNumber = "", userPhoro = "", timeToUser = "", userid = "";
    RatingBar rb;
    String final_dest_exists = "no";
    LinearLayout linear_cash;
    int count = 0;
    int num = 0;
    int num_sec_saved;
    TextView tv_title_DistTime;

    int ss, mm, hh;
    String mDefaultFormat = "%02d:%02d:%02d";
    int total_min, total_sec, total_hours;
    Timer timer;
    String check_onresume = "yes";
    private LocationManager mLocationManager = null;
    // private static final String TAG = "LocServiceAfterTawklna";
    String provider;
    Criteria criteria;
    Location location;
    LocationListenerMAPSMain mLocationListener;
    //TextView tv_test;
    Marker marker;
    TextView tv_dash_;
    Location Captain_current_loc;
    public static Boolean isVisible = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public LocationConfiguration getLocationConfiguration() {

        LocationConfiguration awesomeConfiguration = new LocationConfiguration.Builder()
                .keepTracking(true)

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Al-Jazeera-Arabic-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_view_details_order);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        callBackVolly();
        hh = new MySharedPreference(getApplicationContext()).getIntShared("counter_tripTime_hour");
        mm = new MySharedPreference(getApplicationContext()).getIntShared("counter_tripTime_min");
        ss = new MySharedPreference(getApplicationContext()).getIntShared("counter_tripTime_sec");
        icon_driver = BitmapDescriptorFactory.fromResource(R.drawable.markermdpi);
        icon_user_start = BitmapDescriptorFactory.fromResource(R.drawable.marker_start_locationmdpi);
        icon_user_destination = BitmapDescriptorFactory.fromResource(R.drawable.marker_end_locationmdpi);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.`
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btn_tawklna = findViewById(R.id.btn_tawklna_arrived_start_finish);
        btn_cancel = findViewById(R.id.btn_cancel_trip);
        icon_call = findViewById(R.id.img_call);
        linear_cash = findViewById(R.id.lin_cash);
        tv_title_DistTime = findViewById(R.id.tv_title_time_dis);

        tv_locationText = findViewById(R.id.tv_locationtxt);
        tv_driver_name = findViewById(R.id.txtNameDriver);
        tv_rate = findViewById(R.id.txtRate);
        tv_distance = findViewById(R.id.txt_distance);
        tv_time = findViewById(R.id.txt_time);
        tv_payment_method = findViewById(R.id.txt_Cash);
        tv_timeTOuser = findViewById(R.id.txtBaqya);
        icon_user = findViewById(R.id.imgDriver);
        tv_HowFar = findViewById(R.id.txt_yb3d);
        txt_info = findViewById(R.id.txt_info);
        tv_dash_ = findViewById(R.id.tv_dash);
        rb = findViewById(R.id.ratingBar);
        img_nav = findViewById(R.id.img_nav_googlemap);
        progressBar = findViewById(R.id.progressBar2);


        if (getIntent().getExtras() != null) {
            // Toast.makeText(getApplicationContext(), "MMMMM",Toast.LENGTH_LONG).show();
            from_what_Act = getIntent().getExtras().getString("from_act");
            //  Toast.makeText(getApplicationContext(), from_what_Act,Toast.LENGTH_LONG).show();
            // from_what_Act = "adapter";
            if (from_what_Act.equals("activity") || from_what_Act.equals("adapter")) {
                //order_id = getIntent().getExtras().getInt("order_id");
                order_id = getIntent().getExtras().getString("order_id");

            } else if (from_what_Act.equals("splash")) {
                //order_id = Integer.parseInt(new MySharedPreference(getApplicationContext()).getStringShared("cur_order_id"));
                order_id = new MySharedPreference(getApplicationContext()).getStringShared("cur_order_id");

            }
            if (!getIntent().getExtras().getString("initialBtnType", "").isEmpty()) {
                btn_type = getIntent().getExtras().getString("initialBtnType", "");
            }

        }


        icon_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialoge_call();
            }
        });
        img_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (trip_status.equals("start")) {
                        if (final_dest_exists.equals("yes")) {
                            //ask_forPermission_floatIcon();
                            String uri = String.format(Locale.ENGLISH,
                                    "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)",
                                    driver_cur_lat, driver_cur_lng, "current location",
                                    user_loc_final.latitude, user_loc_final.longitude, "final location");
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            intent.setPackage("com.google.android.apps.maps");
                            startActivity(intent);
                            addNewBubble();
                        } else if (final_dest_exists.equals("no")) {
                            //do nothing
                            Toast.makeText(getApplicationContext(), "لم يتم تحديد وجهة الراكب!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else if (!trip_status.equals("start")) {
                        //ask_forPermission_floatIcon();
                        String uri = String.format(Locale.ENGLISH,
                                "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)",
                                driver_cur_lat, driver_cur_lng, "current location",
                                user_loc_start.latitude, user_loc_start.longitude, "final location");
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setPackage("com.google.android.apps.maps");
                        startActivity(intent);
                        addNewBubble();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });


        btn_tawklna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_type.equals("tawaklna")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewDetailsOrder.this);
                    alertDialogBuilder.setMessage("هل انت متأكد؟");
                    alertDialogBuilder.setPositiveButton("نعم",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    new MySharedPreference(getApplicationContext()).setFloatShared("total_dis_before", (float) 0.0);
                                    new MySharedPreference(getApplicationContext()).setIntShared("total_time_normal_before", 0);
                                    new MySharedPreference(getApplicationContext()).setIntShared("total_time_slow_before", 0);

                                    Paper.book().write("totalDistance", Double.parseDouble("0"));
                                    Paper.book().write("totalTimeNormal", Double.parseDouble("0"));
                                    Paper.book().write("totalTimeSlow", Double.parseDouble("0"));


                                    DBHelper2 db = new DBHelper2(getApplicationContext());
                                    db.deleteCoordsTable();
                                    db.deleteTimesTable();
                                    db.deleteSecTable();

                                    trip_status = "tawaklna";
                                    if (!from_what_Act.equals("splash")) {
                                        method = "update_status_trip";
                                        Volley_go();
                                    }
                                    if (from_what_Act.equals("splash")) {
                                        tawaklna_event();
                                    }
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
                } else if (btn_type.equals("wosoul")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewDetailsOrder.this);
                    alertDialogBuilder.setMessage("هل انت متأكد؟");
                    alertDialogBuilder.setPositiveButton("نعم",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    trip_status = "wosoul";
                                    if (!from_what_Act.equals("splash")) {
                                        method = "update_status_trip";
                                        Volley_go();
                                    }
                                    if (from_what_Act.equals("splash")) {
                                        wosoul_event();
                                    }
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
                } else if (btn_type.equals("start")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewDetailsOrder.this);
                    alertDialogBuilder.setMessage("هل انت متأكد؟");
                    alertDialogBuilder.setPositiveButton("نعم",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    trip_status = "start";
                                    if (!from_what_Act.equals("splash")) {
                                        method = "update_status_trip";
                                        Volley_go();
                                    }
                                    if (from_what_Act.equals("splash")) {
                                        start_event();
                                    }
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
                } else if (btn_type.equals("finish_trip")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewDetailsOrder.this);
                    alertDialogBuilder.setMessage("هل انت متأكد؟");
                    alertDialogBuilder.setPositiveButton("نعم",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    finish_event();
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

            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (can_cancel_trip.equals("yes")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewDetailsOrder.this);
                    alertDialogBuilder.setMessage("هل انت متأكد؟");
                    alertDialogBuilder.setPositiveButton("نعم",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    clearValues();
                                    method = "cancel_order";
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

                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewDetailsOrder.this);
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

            }
        });

        getLocation();
    }

    public void show_dialoge_call() {
        // final CharSequence[] items ={"الدعم الفني", userName , userNumber};
        final CharSequence[] items = {"الدعم الفني", "الاتصال بالراكب هاتفيا"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ViewDetailsOrder.this);
        builder.setTitle("");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("الدعم الفني")) {

                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ActivityCompat.checkSelfPermission(ViewDetailsOrder.this,
                                    Manifest.permission.CALL_PHONE)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(ViewDetailsOrder.this,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        202);

                            } else if (ActivityCompat.checkSelfPermission(ViewDetailsOrder.this,
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
                    } catch (Exception ex) {
                    }

                } else if (items[item].equals("الاتصال بالراكب هاتفيا")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(ViewDetailsOrder.this,
                                Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(ViewDetailsOrder.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    202);

                        } else if (ActivityCompat.checkSelfPermission(ViewDetailsOrder.this,
                                Manifest.permission.CALL_PHONE)
                                == PackageManager.PERMISSION_GRANTED) {

                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:00" + userNumber));
                            startActivity(callIntent);
                            Log.d("usernumber", userNumber);

                        }
                    } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                        Log.d("usernumber", userNumber);
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:00" + userNumber));
                        startActivity(callIntent);

                    }
                }
            }
        });
        builder.show();

    }

    private void initializeBubblesManager() {
        bubblesManager = new BubblesManager.Builder(this)
                .setTrashLayout(R.layout.bubble_trash_layout)
                .setInitializationCallback(new OnInitializedCallback() {
                    @Override
                    public void onInitialized() {
                        //addNewBubble();
                    }
                })
                .build();
        bubblesManager.initialize();
    }


    int current_time = 0;


    public void add_timer() {
        current_time = new MySharedPreference(getApplicationContext()).getIntShared("counter_tripTime_sec");
        // tv_title_DistTime.setText("المدة والمسافة حتى الان");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        current_time++;
//                        ss = new MySharedPreference(getApplicationContext()).getIntShared("counter_tripTime_sec") + 1;
                        new MySharedPreference(getApplicationContext()).setIntShared("counter_tripTime_sec", current_time);
//                        total_sec = new MySharedPreference(getApplicationContext()).getIntShared("counter_tripTime_sec");
//                        if (total_sec == 60) {
//                            mm = new MySharedPreference(getApplicationContext()).getIntShared("counter_tripTime_min") + 1;
//                            new MySharedPreference(getApplicationContext()).setIntShared("counter_tripTime_min", mm);
//                            total_min = new MySharedPreference(getApplicationContext()).getIntShared("counter_tripTime_min");
//                            ss = 0;
//                            new MySharedPreference(getApplicationContext()).setIntShared("counter_tripTime_sec", 0);
//                        }
//                        if (total_min == 60) {
//                            hh = new MySharedPreference(getApplicationContext()).getIntShared("counter_tripTime_hour") + 1;
//                            new MySharedPreference(getApplicationContext()).setIntShared("counter_tripTime_hour", hh);
//                            mm = 0;
//                        }


//                        String text = String.format(Locale.US, mDefaultFormat, hh, mm, ss);
                        tv_time.setText(getTime(new MySharedPreference(getApplicationContext()).getIntShared("counter_tripTime_sec")));
                        Double tot_dis = Paper.book().read("totalDistance", Double.parseDouble("0")) / 1000;
                        if (Double.isNaN(tot_dis)) return;
                      /*  float tot_dis=(new
                                MySharedPreference(getApplicationContext()).getFloatShared("total_dis_before")+
                                new MySharedPreference(getApplicationContext()).getFloatShared("total_dis_after")) /1000;*/
                        String tot_dis_fix = new DecimalFormat("##.##").format(tot_dis);
                        tv_distance.setText(String.valueOf(tot_dis_fix) + " km ");

                    }
                });

            }
        }, 1, 1000);
    }

    public String getTime(int timer) {
        int seconds = (int) (timer / 1) % 60;
        int minutes = (int) ((timer / (1 * 60)) % 60);
        int hours = (int) ((timer / (1 * 60 * 60)) % 24);
        String finalTime = "";
        if (hours == 0)
            if (minutes == 0) {
                if (seconds < 10)
                    finalTime = "00:0" + seconds;
                else {
                    finalTime = "00:" + seconds;
                }
            } else {
                if (seconds < 10 && minutes < 10)
                    finalTime = "0" + minutes + ":0" + seconds;
                else
                    finalTime = minutes + ":" + seconds;
            }
        else
            finalTime = hours + ":" + minutes + ":" + seconds;

        return finalTime;
    }

    public void tawaklna_event() {

        try {

            from_what_Act = "activity";
            //startCancelService();
            check_onresume = "yes";
            new MySharedPreference(getApplicationContext()).setStringShared("trip_finished", "no");
            if (timer == null) {
                // add_timer();
            }
            mMap.clear();
            new MySharedPreference(getApplicationContext()).setStringShared("cur_order_id", String.valueOf(order_id));
            onbackPress_status = "cant_go_back";
            btn_type = "wosoul";
            trip_status = "tawaklna";
            btn_tawklna.setText(R.string.btn_arrive_user);
            icon_call.setVisibility(View.VISIBLE);
            tv_title_DistTime.setText("التكلفة المتوقعة");


            if (final_dest_exists.equals("no")) {
                //tv_distance.setVisibility(View.INVISIBLE);
                tv_distance.setText("");
            }


            //if (Captain_current_loc != null) {
            //add marker driver location
            driver_loc = new LatLng(driver_cur_lat, driver_cur_lng);

            //marker = mMap.addMarker(new MarkerOptions()
            //       .position(new LatLng(driver_cur_lat, driver_cur_lng))
            //      .icon(BitmapDescriptorFactory.fromResource(R.drawable.markermdpi)));

            // marker on current location driver
            //MarkerOptions markerOptions_driver_curLoc = new MarkerOptions().position(driver_loc).icon(icon_user_start);
            //mMap.addMarker(markerOptions_driver_curLoc);

            // marker on start location user marker
            MarkerOptions markerOptionss_user_start_loc = new MarkerOptions().position(user_loc_start).icon(icon_user_destination);
            mMap.addMarker(markerOptionss_user_start_loc);


            String url = getUrl(driver_loc, user_loc_start);
            FetchUrl FetchUrl = new FetchUrl();
            FetchUrl.execute(url);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(user_loc_start);
            builder.include(driver_loc);
            LatLngBounds bounds = builder.build();
            int padding = 50;
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.moveCamera(cu);
            mMap.animateCamera(cu);

            //}
        } catch (Exception ex) {
        }
    }


    public void wosoul_event() {
        try {
            from_what_Act = "activity";
            check_onresume = "no";
            tv_title_DistTime.setText("التكلفة المتوقعة");
            if (timer == null) {
                //add_timer();
            }
            can_cancel_trip = "no";
            int num_sec = Integer.parseInt(new MySharedPreference(getApplicationContext()).getStringShared("time_to_cancel_trip_in_sec"));
            num_sec_saved = new MySharedPreference(getApplicationContext()).getIntShared("time_cancel_trip_saved");
            if (num_sec >= num_sec_saved) {
                num = num_sec - num_sec_saved;
            } else if (num_sec < num_sec_saved) {
                num = num_sec_saved - num_sec;
            }
            new CountDownTimer(num * 1000, 1000) { // adjust the milli seconds here
                public void onTick(long millisUntilFinished) {
                    //Toast.makeText(getApplicationContext(),"tick!", Toast.LENGTH_SHORT).show();
                    btn_cancel.setText(" الغاء " + String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    num_sec_saved = num_sec_saved + 1;
                    new MySharedPreference(getApplicationContext()).setIntShared("time_cancel_trip_saved", num_sec_saved);
                }

                public void onFinish() {
                    can_cancel_trip = "yes";
                    btn_cancel.setText("الغاء");
                    btn_cancel.setBackgroundColor(getResources().getColor(R.color.red));
                }
            }.start();

            btn_type = "start";
            trip_status = "wosoul";
            tv_timeTOuser.setVisibility(View.INVISIBLE);
            // tv_timeTOuser.setText("-");


            btn_tawklna.setText(R.string.btn_start_trip);
            btn_cancel.setVisibility(View.VISIBLE);
            img_nav.setVisibility(View.INVISIBLE);
            tv_HowFar.setVisibility(View.INVISIBLE);
            mMap.clear();

            if (Captain_current_loc != null) {

                //add marker driver location
                driver_loc = new LatLng(driver_cur_lat, driver_cur_lng);

                // marker = mMap.addMarker(new MarkerOptions()
                //        .position(new LatLng(driver_cur_lat, driver_cur_lng))
                //       .icon(BitmapDescriptorFactory.fromResource(R.drawable.markermdpi)));

                if (user_loc_final != null) {
                    MarkerOptions markerOptionsss = new MarkerOptions().position(user_loc_final).icon(icon_user_destination);
                    marker_user_destination = mMap.addMarker(markerOptionsss);
                    String url = getUrl(driver_loc, user_loc_final);

                    FetchUrl FetchUrl = new FetchUrl();
                    FetchUrl.execute(url);

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    // builder.include(user_loc_start);
                    builder.include(driver_loc);
                    builder.include(user_loc_final);
                    LatLngBounds bounds = builder.build();
                    int padding = 50;
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mMap.moveCamera(cu);
                    mMap.animateCamera(cu);
                    //mMap.animateCamera(CameraUpdateFactory.zoomTo( 13.0f ) );
                    //tv_HowFar.setText("نقطه الوصول");

                } else if (user_loc_final == null) {
                    tv_distance.setText("");
                    //tv_distance.setVisibility(View.INVISIBLE);
                    // linear_cash.setVisibility(View.INVISIBLE);

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    //builder.include(user_loc_start);
                    builder.include(driver_loc);
                    LatLngBounds bounds = builder.build();
                    int padding = 50;
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mMap.moveCamera(cu);
                    mMap.animateCamera(cu);
                    //mMap.animateCamera(CameraUpdateFactory.zoomTo( 13.0f ) );

                }
            }

        } catch (Exception ex) {

        }

    }


    public void start_event() {
        try {
            Paper.book().write("startDistanceCount", true);
            new MySharedPreference(getApplicationContext()).setBooleanShared("startDistanceCount", true);
            triger_alarmManager_sendLocation_After();
            //stopCancelService();
            btn_type = "finish_trip";
            trip_status = "start";
            from_what_Act = "activity";
            check_onresume = "no";
            tv_title_DistTime.setText("المدة والمسافة حتى الان");
            tv_distance.setVisibility(View.VISIBLE);
            tv_dash_.setVisibility(View.VISIBLE);

            btn_tawklna.setText(R.string.btn_done_transport_user);
            btn_cancel.setVisibility(View.GONE);
            icon_call.setVisibility(View.VISIBLE);
            tv_timeTOuser.setVisibility(View.INVISIBLE);

            if (timer == null) {
                add_timer();
            }


            if (user_loc_final != null) {
                img_nav.setVisibility(View.VISIBLE);
                tv_HowFar.setVisibility(View.VISIBLE);
                tv_HowFar.setText("نقطة الوصول ");
                //tv_timeTOuser.setVisibility(View.INVISIBLE);
                MarkerOptions markerOptionsss = new MarkerOptions().position
                        (user_loc_final).icon(icon_user_destination);
                marker_user_destination = mMap.addMarker(markerOptionsss);

                String url = getUrl(driver_loc, user_loc_final);
                FetchUrl FetchUrl = new FetchUrl();
                FetchUrl.execute(url);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(user_loc_start);
                builder.include(driver_loc);
                builder.include(user_loc_final);
                LatLngBounds bounds = builder.build();
                int padding = 50;
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.moveCamera(cu);
                mMap.animateCamera(cu);

            } else if (user_loc_final == null) {

                tv_distance.setText("");
                img_nav.setVisibility(View.INVISIBLE);
                tv_HowFar.setVisibility(View.INVISIBLE);
                tv_timeTOuser.setVisibility(View.INVISIBLE);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(user_loc_start);
                builder.include(driver_loc);
                LatLngBounds bounds = builder.build();
                int padding = 50;
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.moveCamera(cu);
                mMap.animateCamera(cu);

            }

        } catch (Exception ex) {
        }


    }


    public void finish_event() {

        try {
            check_onresume = "no";
            new MySharedPreference(getApplicationContext()).setStringShared("trip_finished", "yes");
            SharedPreferences sp = getApplicationContext().getSharedPreferences("myshared", Context.MODE_PRIVATE);
            Long new_time = System.currentTimeMillis();
            long difference = new_time - sp.getLong("old_time_after", System.currentTimeMillis());
            double difference_sec = difference / 1000;
            Paper.book().write("totalTimeSlow", (int) difference_sec + +Paper.book().read("totalTimeSlow", Double.parseDouble("0")));
            Log.d("time_finish", String.valueOf((int) difference_sec + +Paper.book().read("totalTimeSlow", Double.parseDouble("0"))));

            trip_status = "finish_trip";
            method = "finish_trip";
            Volley_go();
        } catch (Exception ex) {
        }

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    /*    if (onbackPress_status.equals("can_go_back")) {
            startActivity(new Intent(getApplicationContext(), MapsMain.class));
            finish();
        }
        if (onbackPress_status.equals("cant_go_back")) {
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
        }*/
    }

/*

    public void ask_forPermission_floatIcon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 987);

            } else if (Settings.canDrawOverlays(getApplicationContext())) {
                startService(new Intent(getApplicationContext(), FloatingViewService.class));
            }

        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startService(new Intent(getApplicationContext(), FloatingViewService.class));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 987) {
            if (resultCode == RESULT_OK) {
               // ask_forPermission_floatIcon();
                 startService(new Intent(getApplicationContext(), FloatingViewService.class));

            } else {
                //ask_forPermission_floatIcon();

            }
        }

    }
*/


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //permission not granted
        if (ActivityCompat.checkSelfPermission(ViewDetailsOrder.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ViewDetailsOrder.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ViewDetailsOrder.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    100);

        } else {
            // Permission has already been granted

            initializeLocationManager();
            // creatEventMap();

        }

    }


    AlertDialog alertDialogError;

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initializeLocationManager();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    ActivityCompat.requestPermissions(ViewDetailsOrder.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            100);
                }
                return;
            }


            case 202: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //creatEventMap();
                    Log.d("usernumber", userNumber);
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:00" + userNumber));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    startActivity(callIntent);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    ActivityCompat.requestPermissions(ViewDetailsOrder.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            202);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    public void Volley_go() {
        if (progressBar.getVisibility() == View.VISIBLE) return;

      /*  if(!((Activity) ViewDetailsOrder.this).isFinishing()) {
            loading = ProgressDialog.show(ViewDetailsOrder.this, "", "الرجاء الانتظار...", false, true);
        }*/

        progressBar.setVisibility(View.VISIBLE);

        if (method.equals("get_order_details")) {
            Map<String, String> params = new Hashtable<String, String>();
            params.put("local", "ara");
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("order_id", order_id);
            // params.put("order_id", "893");
            voly_ser = new VolleyService(iresultOrderDetails, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url") + PathUrl.ViewTransportOrder_Details, params);
            //Toast.makeText(getApplicationContext(), String.valueOf(order_id), Toast.LENGTH_SHORT).show();

        }

        if (method.equals("update_status_trip")) {
            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", "ara");
            params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("order_id", String.valueOf(order_id));
            voly_ser = new VolleyService(iresultUpdateStatusTrip, getApplicationContext());
            if (trip_status.equals("tawaklna")) {
                voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url") + PathUrl.AcceptOrder, params);
            } else if (trip_status.equals("wosoul")) {
                voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url") + PathUrl.CapinArrived, params);

            } else if (trip_status.equals("start")) {
                params.put("driver_coords", lastLocation != null ? lastLocation.getLatitude() + "," + lastLocation.getLongitude() : String.valueOf(driver_cur_lat) + "," + String.valueOf(driver_cur_lng));
                Paper.book().write("followCoordinates", new ArrayList<FollowCooridnates>());
                Paper.book().write("totalDistance", Double.parseDouble("0"));
                Paper.book().write("totalTimeNormal", Double.parseDouble("0"));
                Paper.book().write("totalTimeSlow", Double.parseDouble("0"));
                Paper.book().write("inaccurate_locations_count", 0);

                Paper.book().write("lastCall", System.currentTimeMillis());
                Paper.book().write("oldLatitude", Paper.book().read("drLatitude"));
                Paper.book().write("oldLongitude", Paper.book().read("drLongitude"));
                Paper.book().write("oldTime", Long.parseLong("" + System.currentTimeMillis()));

                voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url") + PathUrl.StartTrip, params);
                // Toast.makeText(getApplicationContext(), "update_ status_start "+String.valueOf(order_id), Toast.LENGTH_SHORT).show();

            }
        }


        if (method.equals("finish_trip")) {

            try {
                if (lastLocation == null && (Captain_current_loc == null || ((Captain_current_loc.getLongitude() + Captain_current_loc.getLatitude()) == 0))) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("الرجاء تفعيل خدمة المقوع الحالي GPS! ثم اعادة المحاولة");
                    alertDialogBuilder.setPositiveButton("اغلاق",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    alertDialogError.dismiss();
                                }
                            });
                    alertDialogError = alertDialogBuilder.create();
                    alertDialogError.show();
                    return;
                }
                //loading = ProgressDialog.show(ViewDetailsOrder.this, "","الرجاء الانتظار...", false, false);
                Map<String, String> params = new Hashtable<String, String>();
                params.put("local", "ara");
                params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                params.put("order_id", String.valueOf(order_id));
                params.put("driver_coords", lastLocation != null ? lastLocation.getLatitude() + "," + lastLocation.getLongitude() : Captain_current_loc.getLatitude() + "," + Captain_current_loc.getLongitude());

                params.put("trip_values",
                        "0" + "," +
                                "0" + "," +
                                "0" + "," +
                                Paper.book().read("totalTimeSlow") + "," +
                                Paper.book().read("totalTimeNormal") + "," +
                                Paper.book().read("totalDistance"));

                ArrayList<FollowCooridnates> followCoordinates = Paper.book().read("followCoordinates", new ArrayList<>());
                StringBuilder sb_coords = new StringBuilder();
                StringBuilder sb_times = new StringBuilder();
                StringBuilder sb_sec = new StringBuilder();
                for (int i = 0; i < followCoordinates.size(); i++) {
                    FollowCooridnates coordinates = followCoordinates.get(i);
                    sb_coords.append((i == 0 ? "" : "|") + coordinates.getLatitude() + "," + coordinates.getLongitude());
                    sb_times.append((i == 0 ? "" : "|") + coordinates.getDateTime());
                    sb_sec.append((i == 0 ? "" : "|") + coordinates.getSecondsDiff());

                }
                params.put("coords_c", sb_coords.toString());
                params.put("array_time", sb_times.toString());
                params.put("array_sec", sb_sec.toString());
                params.put("enable_firebase", "1");
                Paper.book().write("inaccurate_locations_count", 0);

                voly_ser = new VolleyService(iresultFinishTrip, getApplicationContext());
                voly_ser.postDataVolleyClearCashe(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url") + PathUrl.EndTripNEW, params);
                Paper.book().write("startDistanceCount", false);
                new MySharedPreference(getApplicationContext()).setBooleanShared("startDistanceCount", false);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }


        if (method.equals("cancel_order")) {
            Map<String, String> params = new Hashtable<String, String>();
            params.put("local", "ara");
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("order_id", String.valueOf(order_id));
            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url") + PathUrl.CancelOrder, params);
        }


        if (method.equals("get_cur_support")) {
            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.getDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url") + PathUrl.GetCurrentSupport);
        }


        if (method.equals("update_info")) {
            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", "ara");
            params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            voly_ser = new VolleyService(iresultFinishUpdateInfo, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url") + PathUrl.TransportInfo, params);
        }


    }


    public void callBackVolly() {

        iresultOrderDetails = new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                progressBar.setVisibility(View.INVISIBLE);

                //loading.dismiss();
                // Toast.makeText(ViewDetailsOrder.this, response, Toast.LENGTH_LONG).show();
                Gson gson = new Gson();
                OrderDetails res = gson.fromJson(response, OrderDetails.class);
                System.out.println("WS? " + response);
                if (res.getHandle().equals("02")) {
                    Toast.makeText(ViewDetailsOrder.this, res.getMsg(), Toast.LENGTH_LONG).show();
                } else if (res.getHandle().equals("10")) {
                    try {
                        Order selected_order = res.getOrder();
                        System.out.println("AWSORDER" + response);
//                        System.out.println("AWSORDER"+res.getOrder().getDistance());
                        if (selected_order.getStatus().equalsIgnoreCase("F") || selected_order.getStatus().equalsIgnoreCase("C")) {
                            clearBubble();
                            Paper.book().write("startDistanceCount", false);
                            new MySharedPreference(getApplicationContext()).setBooleanShared("startDistanceCount", false);
                            stopAlarmManager_After();
                            startActivity(new Intent(getApplicationContext(), MapsMain.class));
                            finish();
                        }
                        //add final location user marker IF exsist
                        if (!selected_order.getToLocation().equals("") && selected_order.getToLocation() != null) {
                            String string_final_loc = selected_order.getToLocation();
                            String[] separated_final = string_final_loc.split(",");
                            user_loc_final = new LatLng(Double.parseDouble(separated_final[0]),
                                    Double.parseDouble(separated_final[1]));
                            MarkerOptions markerOptionsss = new MarkerOptions().position(user_loc_final).
                                    icon(icon_user_destination);
                            if (selected_order.getStatus().equals("S") || selected_order.getStatus().equals("A")) {
                                marker_user_destination = mMap.addMarker(markerOptionsss);
                            }
                            final_dest_exists = "yes";
                            tv_time.setText(selected_order.getExpectFee() + " د.أ ");
                            tv_distance.setText("  " + selected_order.getDistance());
                            tv_title_DistTime.setText("المدة والمسافة");
                            tv_distance.setVisibility(View.VISIBLE);
                            tv_dash_.setVisibility(View.VISIBLE);
                        }
                        // add start location user marker
                        String string_start_loc = selected_order.getLocation();
                        String[] separated = string_start_loc.split(",");
                        user_loc_start = new LatLng(Double.parseDouble(separated[0]), Double.parseDouble(separated[1]));
                        MarkerOptions markerOptionsss = new MarkerOptions().position(user_loc_start).icon(icon_user_start);
                        if (selected_order.getStatus().equals("D")) {
                            marker_user_start = mMap.addMarker(markerOptionsss);
                        }
                        System.out.println("WS? selected_order.getStatus: " + selected_order.getStatus() + " - " + selected_order.getStatus().equalsIgnoreCase("S"));
                        System.out.println("WS? trip_status.equalsIgnoreCase: " + trip_status + " - " + trip_status.isEmpty());

                        if (selected_order.getStatus().equalsIgnoreCase("S") && trip_status.isEmpty()) {
                            trip_status = "start";
                            start_event();
                        }

                        //add marker driver location
                        driver_loc = new LatLng(driver_cur_lat, driver_cur_lng);

/*
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(driver_cur_lat, driver_cur_lng))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.markermdpi)));
*/

                        // there is final dest
                        if (!selected_order.getToLocation().equals("") &&
                                selected_order.getToLocation() != null &&
                                !selected_order.getToLocation().equals("0.0")) {
                            if (from_what_Act.equals("adapter") || from_what_Act.equals("splash")) {
                            } else {
                                mMap.setPadding(30, 30, 30, 30);
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                builder.include(user_loc_final);
                                builder.include(user_loc_start);
                                builder.include(driver_loc);
                                String url = getUrl(user_loc_start, user_loc_final);
                                FetchUrl FetchUrl = new FetchUrl();
                                FetchUrl.execute(url);
                                LatLngBounds bounds = builder.build();
                                int padding = 0; // offset from edges of the map in pixels
                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                                mMap.moveCamera(cu);
                                mMap.animateCamera(cu);

                            }
                        }

                        //theres no destination
                        else if (selected_order.getToLocation().equals("") ||
                                selected_order.getToLocation() == null) {
                            if (from_what_Act.equals("adapter") || from_what_Act.equals("splash")) {
                            } else {
                                mMap.setPadding(30, 30, 30, 30);
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                builder.include(user_loc_start);
                                builder.include(driver_loc);
                                LatLngBounds bounds = builder.build();
                                int padding = 0;
                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                                mMap.moveCamera(cu);
                                mMap.animateCamera(cu);

                                // mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
                                String url = getUrl(user_loc_start, driver_loc);
                                FetchUrl FetchUrl = new FetchUrl();
                                FetchUrl.execute(url);
                            }
                        }

                        tv_driver_name.setText(selected_order.getUserName());
                        tv_payment_method.setText(selected_order.getPaymentType());
                        Glide.with(getApplicationContext())
                                .load(selected_order.getUserPhoto())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.user_imagehdpi)
                                        .centerCrop()
                                        .dontAnimate()
                                        .dontTransform())
                                .into(icon_user);

                        Log.d("icoooonnnn", selected_order.getUserPhoto());
                        //Toast.makeText(getApplicationContext(), selected_order.getUserPhoto(), Toast.LENGTH_SHORT ).show();
                        tv_rate.setText(selected_order.getUserRate());
                        tv_timeTOuser.setText(selected_order.getTimeToUser());
//
                        if (selected_order.getDistance().equals("0 كم")) {
                            // tv_distance.setText("");
                            //  tv_time.setText("");

                        } else if (!selected_order.getDistance().equals("0 كم")) {
                            // tv_distance.setText(selected_order.getDistance());
                            //  tv_time.setText(selected_order.getTime());
                        }

                        rb.setRating(Float.parseFloat(selected_order.getUserRate()));
                        userRate = selected_order.getUserRate();
                        userName = selected_order.getUserName();
                        userNumber = selected_order.getUserMob();
                        userPhoro = selected_order.getUserPhoto();
                        userid = String.valueOf(selected_order.getuserId());
                        // Toast.makeText(getApplicationContext(), String.valueOf(selected_order.getId()), Toast.LENGTH_SHORT).show();

                        done_loading_details = "yes";
                        if (from_what_Act.equals("adapter")) {
                            trip_status = "tawaklna";
                            method = "update_status_trip";
                            Volley_go();
                        }

                        if (!selected_order.getOrder_info().isEmpty() && !selected_order.getOrder_info().equalsIgnoreCase("0")) {
                            txt_info.setText(selected_order.getOrder_info());
                            txt_info.setVisibility(View.VISIBLE);
                        }
                        if (selected_order.getStatus().equalsIgnoreCase("E")) {
                            clearBubble();
                            Intent i = new Intent(getApplicationContext(), FinishTrip.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra("respone_end_trip", response);
                            i.putExtra("order_id", String.valueOf(order_id));
                            i.putExtra("user_name", userName);
                            i.putExtra("user_photo", userPhoro);
                            i.putExtra("user_rate", userRate);
                            i.putExtra("user_rate", userRate);
                            i.putExtra("user_id", userid);
                            i.putExtra("from", "activity");
                            startActivity(i);
                            finish();
                        }
                    } catch (Exception ex) {
                    }

                } else {
                    Toast.makeText(ViewDetailsOrder.this, res.getMsg(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void notifyError(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                // loading.dismiss();
                Toast.makeText(ViewDetailsOrder.this, " مشكلة بالاتصال بالانترنت!", Toast.LENGTH_LONG).show();
                // Toast.makeText(ViewDetailsOrder.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };

        iresultUpdateStatusTrip = new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                progressBar.setVisibility(View.INVISIBLE);

                //  loading.dismiss();
                // Toast.makeText(ViewDetailsOrder.this, response, Toast.LENGTH_LONG).show();
                Gson gson = new Gson();
                usual_result res = gson.fromJson(response, usual_result.class);
                if (res.getHandle().equals("02")) {
                    Toast.makeText(ViewDetailsOrder.this, res.getMsg(), Toast.LENGTH_LONG).show();
                } else if (res.getHandle().equals("10")) {
                    //Toast.makeText(ViewDetailsOrder.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    if (trip_status.equals("tawaklna")) {
                        new MySharedPreference(getApplicationContext()).setStringShared("cur_order_id", String.valueOf(order_id));
                        tawaklna_event();
                    }
                    if (trip_status.equals("wosoul")) {
                        wosoul_event();
                    }
                    if (trip_status.equals("start")) {
                        start_event();
                    }
                } else if (res.getHandle().equals("03")) { // لم يتم وجود الطلب --
                    Toast.makeText(ViewDetailsOrder.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                    finish();
                } else {
                    Toast.makeText(ViewDetailsOrder.this, res.getMsg(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void notifyError(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                // loading.dismiss();
                Toast.makeText(ViewDetailsOrder.this, " مشكلة بالاتصال بالانترنت!", Toast.LENGTH_LONG).show();
                // Toast.makeText(ViewDetailsOrder.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };
        iresultFinishTrip = new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                progressBar.setVisibility(View.INVISIBLE);

                try {
                    stopAlarmManager_After();
                    //stop_sendLocAfterTawklna();
                    //loading.dismiss();
                    Gson gson = new Gson();
                    end_trip res = gson.fromJson(response, end_trip.class);
                    if (res.getHandle().equals("02")) {
                        Toast.makeText(ViewDetailsOrder.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        finish();
                    } else if (res.getHandle().equals("10")) {
                        // Toast.makeText(ViewDetailsOrder.this, response, Toast.LENGTH_LONG).show();
                        clearBubble();
                        Intent i = new Intent(getApplicationContext(), FinishTrip.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("respone_end_trip", response);
                        i.putExtra("order_id", String.valueOf(order_id));
                        i.putExtra("user_name", userName);
                        i.putExtra("user_photo", userPhoro);
                        i.putExtra("user_rate", userRate);
                        i.putExtra("user_rate", userRate);
                        i.putExtra("user_id", userid);
                        i.putExtra("from", "activity");
                        startActivity(i);
                        finish();
                    } else if (res.getHandle().equals("03")) {  // لم يتم وجود الطلب -- درايفر تاني اخدو
                        Toast.makeText(ViewDetailsOrder.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        clearBubble();
                        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                        finish();
                    } else {
                        Toast.makeText(ViewDetailsOrder.this, res.getMsg(), Toast.LENGTH_LONG).show();

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void notifyError(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                // loading.dismiss();
                Toast.makeText(ViewDetailsOrder.this, " مشكلة بالاتصال بالانترنت!", Toast.LENGTH_LONG).show();
                // Toast.makeText(ViewDetailsOrder.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };
        iresultFinishUpdateInfo = new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                progressBar.setVisibility(View.INVISIBLE);
                //loading.dismiss();
                // Toast.makeText(SplashActivity.this, response, Toast.LENGTH_LONG).show();
                Gson gson = new Gson();
                Check res = gson.fromJson(response, Check.class);
                if (res.getHandle().equals("02")) {  // account not found
                    //Toast.makeText(SplashActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ViewDetailsOrder.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                } else if (res.getHandle().equals("10")) {   // account found
                    check_order u = res.getTransport();

                    new MySharedPreference(getApplicationContext()).setStringShared("rate", u.getRate());
                    new MySharedPreference(getApplicationContext()).setStringShared("balance", u.getBalance());
                    clearBubble();
                    startActivity(new Intent(getApplicationContext(), MapsMain.class));
                    finish();

                } else {
                    Toast.makeText(ViewDetailsOrder.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    clearBubble();
                    Intent intent = new Intent(ViewDetailsOrder.this, MapsMain.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                // loading.dismiss();
                Toast.makeText(ViewDetailsOrder.this, " مشكلة بالاتصال بالانترنت!", Toast.LENGTH_LONG).show();
                // Toast.makeText(ViewDetailsOrder.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };
        iresult = new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                progressBar.setVisibility(View.INVISIBLE);

                if (method.equals("cancel_order")) {
                    // loading.dismiss();
                    //stop_sendLocAfterTawklna();
                    new MySharedPreference(getApplicationContext()).setIntShared("time_cancel_trip_saved", 0);
                    DBHelper2 db = new DBHelper2(getApplicationContext());
                    db.deleteCoordsTable();
                    db.deleteTimesTable();
                    db.deleteSecTable();
                    Gson gson = new Gson();
                    usual_result res = gson.fromJson(response, usual_result.class);
                    if (res.getHandle().equals("02")) {
                        Toast.makeText(ViewDetailsOrder.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {
                        Toast.makeText(ViewDetailsOrder.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                method = "update_info";
                                Volley_go();
                            }
                        }, 2000);

                    } else if (res.getHandle().equals("03")) { // لم يتم وجود الطلب -- درايفر تاني اخدو
                        Toast.makeText(ViewDetailsOrder.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        clearBubble();
                        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                        finish();
                    }
                }

                if (method.equals("get_cur_support")) {
                    //loading.dismiss();
                    // Toast.makeText(Support.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    current_support res = gson.fromJson(response, current_support.class);
                    if (res.getHandle().equals("02")) {  // account not found
                        Toast.makeText(ViewDetailsOrder.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {   // account found
                        //Toast.makeText(ViewDetailsOrder.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        support s = res.getSupport();
                        String cur_support = s.getUsername();
                        Intent i = new Intent(getApplicationContext(), MakeCallSinch.class);
                        i.putExtra("call_whome", cur_support);
                        i.putExtra("reciever_name", "الدعم الفني");
                        startActivity(i);
                    } else {
                        Toast.makeText(ViewDetailsOrder.this, res.getMsg(), Toast.LENGTH_LONG).show();

                    }
                }

            }

            @Override
            public void notifyError(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                // loading.dismiss();
                Toast.makeText(ViewDetailsOrder.this, " مشكلة بالاتصال بالانترنت!", Toast.LENGTH_LONG).show();
                // Toast.makeText(ViewDetailsOrder.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
        //  finish();

        clearBubble();

    }

    private void clearBubble() {
        try {
            if (bubblesManager != null)
                bubblesManager.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    BubbleLayout bubbleView;

    private void addNewBubble() {
        System.out.println("shtaay => addNewBubble");
        clearBubble();
        initializeBubblesManager();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bubbleView = (BubbleLayout) LayoutInflater.from(ViewDetailsOrder.this).inflate(R.layout.bubble_layout, null);
                bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
                    @Override
                    public void onBubbleRemoved(BubbleLayout bubble) {
                    }
                });
                bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {

                    @Override
                    public void onBubbleClick(BubbleLayout bubble) {
                        try {
                            bubblesManager.removeBubble(bubbleView);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent i = new Intent(getApplicationContext(), ViewDetailsOrder.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("from_act", "splash");
                        startActivity(i);
                        finish();
                    }
                });
                bubbleView.setShouldStickToWall(true);

                bubblesManager.addBubble(bubbleView, 120, 420);
            }
        }, 500);


        System.out.println("shtaay => addNewBubble 60, 20");
    }


    @Override
    public void onPause() {
        super.onPause();
        isVisible = false;
        new MySharedPreference(getApplicationContext()).setStringShared("ViewDetailsOrder_act", "background");
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
        new MySharedPreference(getApplicationContext()).setStringShared("ViewDetailsOrder_act", "foreground");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.turnpoint.ticram.tekram_driver.FCM.onMessageReceived");
        BroadcastReceiver_UpdateUI receiver = new BroadcastReceiver_UpdateUI();
        registerReceiver(receiver, intentFilter);
        method = "get_order_details";
        Volley_go();
    }

    Location lastLocation;

    @Override
    public void onLocationChanged(Location location) {
        if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
            lastLocation = location;
            System.out.println("onLocationChanged =>" + (location.getLatitude() + "," + location.getLongitude()));
        }
    }

    @Override
    public void onLocationFailed(int type) {

        System.out.println("onLocationChanged onLocationFailed =>" + type);
    }


    public class BroadcastReceiver_UpdateUI extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            String state = extras.getString("extra");
            String what_todo = extras.getString("what_todo");

            if (what_todo.equals("ORDER_CANCELLED")) {
                if (!((Activity) context).isFinishing()) {
                    clearValues();
                    show_dialoge_cancelorder();
                }
            }

        }
    }


    public void clearValues() {

        try {
            new MySharedPreference(getApplicationContext()).setFloatShared("total_dis_before", (float) 0.0);
            new MySharedPreference(getApplicationContext()).setIntShared("total_time_normal_before", 0);
            new MySharedPreference(getApplicationContext()).setIntShared("total_time_slow_before", 0);

            Paper.book().write("totalDistance", Double.parseDouble("0"));
            Paper.book().write("totalTimeNormal", Double.parseDouble("0"));
            Paper.book().write("totalTimeSlow", Double.parseDouble("0"));

            new MySharedPreference(getApplicationContext()).setIntShared("counter_tripTime_sec", 0);
            new MySharedPreference(getApplicationContext()).setIntShared("counter_tripTime_min", 0);
            new MySharedPreference(getApplicationContext()).setIntShared("counter_tripTime_hour", 0);

       /* new MySharedPreference(getApplicationContext()).setStringShared("coords_c","");
        new MySharedPreference(getApplicationContext()).setStringShared("array_time", "");
        new MySharedPreference(getApplicationContext()).setStringShared("array_sec","");*/

            DBHelper2 db = new DBHelper2(getApplicationContext());
            db.deleteCoordsTable();
            db.deleteTimesTable();
            db.deleteSecTable();
        } catch (Exception ex) {
        }
    }

    public void show_dialoge_cancelorder() {

        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewDetailsOrder.this);
            alertDialogBuilder.setMessage("لقد تم الغاء الرحلة! ");
            alertDialogBuilder.setPositiveButton("الرجوع الى الشاشة الرئيسية.",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    method = "update_info";
                                    Volley_go();
                                }
                            }, 2000);
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception ex) {
        }

    }


    private void handleNewLocation(Location location) {
        // mMap.clear();
        try {
            Captain_current_loc = location;
            driver_cur_lat = location.getLatitude();
            driver_cur_lng = location.getLongitude();

            marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(driver_cur_lat, driver_cur_lng))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.markermdpi)));

            /*Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(driver_cur_lat, driver_cur_lng, 1);
                if (addresses != null && addresses.size() > 0) {
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    tv_locationText.setText(address + " " + city + " ");
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/

            creatEventMap();

            if (from_what_Act.equals("splash")) {
                // order_id = Integer.parseInt(new MySharedPreference(getApplicationContext()).getStringShared("cur_order_id"));
                order_id = new MySharedPreference(getApplicationContext()).getStringShared("cur_order_id");
                getorderDetailsSplash();

            } else if (!from_what_Act.equals("splash")) {
                method = "get_order_details";
                Volley_go();
            }
        } catch (Exception ex) {
        }

    }


    MarkerOptions markerDestinationOptions;

    public void getorderDetailsSplash() {

        try {
            Order selected_order = new savedOrder().getorder();
            //add final location user marker IF exsist
            if (selected_order.getToLocation() != null && selected_order.getToLocation() != "") {
                String string_final_loc = selected_order.getToLocation();
                String[] separated_final = string_final_loc.split(",");
                user_loc_final = new LatLng(Double.parseDouble(separated_final[0]), Double.parseDouble(separated_final[1]));
                markerDestinationOptions = new MarkerOptions().position(user_loc_final).icon(icon_user_destination);

                final_dest_exists = "yes";
                tv_time.setText(selected_order.getExpectFee() + " د.أ ");
                tv_distance.setVisibility(View.GONE);
                tv_dash_.setVisibility(View.GONE);
            }
            // add start location user marker
            String string_start_loc = selected_order.getLocation();
            String[] separated = string_start_loc.split(",");
            user_loc_start = new LatLng(Double.parseDouble(separated[0]), Double.parseDouble(separated[1]));
            MarkerOptions markerOptionsss = new MarkerOptions().position(user_loc_start).icon(icon_user_start);
            //marker_user_start = mMap.addMarker(markerOptionsss);


            //add marker driver location
          /*  driver_loc = new LatLng(driver_cur_lat, driver_cur_lng);

            marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(driver_cur_lat, driver_cur_lng))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.markermdpi)));*/


            tv_driver_name.setText(selected_order.getUserName());
            tv_payment_method.setText(selected_order.getPaymentType());
            Glide.with(getApplicationContext()).load(selected_order.getUserPhoto()).into(icon_user);
            tv_rate.setText(selected_order.getUserRate());
//            tv_timeTOuser.setText(new MySharedPreference(getApplicationContext()).getIntShared("distance_user"));
//            Toast.makeText(ViewDetailsOrder.this,new MySharedPreference(getApplicationContext()).getIntShared("distance_user") , Toast.LENGTH_SHORT).show();
            tv_timeTOuser.setText(selected_order.getTimeToUser());
//            tv_timeTOuser.setText(new MySharedPreference(getApplicationContext()).getIntShared("distance_user"));

            rb.setRating(Float.parseFloat(selected_order.getUserRate()));
            userRate = selected_order.getUserRate();
            userName = selected_order.getUserName();
            userNumber = selected_order.getUserMob();
            userPhoro = selected_order.getUserPhoto();
            userid = String.valueOf(selected_order.getuserId());

            done_loading_details = "yes";
            onbackPress_status = "cant_go_back";

            if (selected_order.getStatus().equals("D")) {
                //Toast.makeText(getApplicationContext(), "tawaklna_event", Toast.LENGTH_SHORT).show();
                tawaklna_event();
                marker_user_start = mMap.addMarker(markerOptionsss);

            }
            if (selected_order.getStatus().equals("A")) {
                // Toast.makeText(getApplicationContext(), "wosoul_event", Toast.LENGTH_SHORT).show();
                wosoul_event();
                if (markerDestinationOptions != null) {
                    marker_user_destination = mMap.addMarker(markerDestinationOptions);
                }
            }
            if (selected_order.getStatus().equals("S")) {
                //  Toast.makeText(getApplicationContext(), "start_event", Toast.LENGTH_SHORT).show();
                start_event();
                if (markerDestinationOptions != null) {
                    marker_user_destination = mMap.addMarker(markerDestinationOptions);
                }

            }
            if (selected_order.getStatus().equals("E")) {
                //Toast.makeText(getApplicationContext(), "finish_event", Toast.LENGTH_SHORT).show();
                finish_event();
            }


        } catch (Exception ex) {
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
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                try {
                    LatLng mylatlng = new LatLng(lat, lon);
                    marker.setPosition(mylatlng);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylatlng, 16.0f));
                } catch (Exception ex) {
                }


            }
        });


    }


    private void Add_order_firebase() {
        addOrderFirebase values = new addOrderFirebase(Long.valueOf(order_id), "D");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("orders").child(new MySharedPreference(getApplicationContext()).
                getStringShared("user_id")).child("order").setValue(values);

    }

    public void startCancelService() {
        Add_order_firebase();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                Intent i = new Intent(getApplicationContext(), CancelTripListner.class);
                startForegroundService(i);
            } catch (Exception ex) {
            }

        } else {
            Intent i = new Intent(getApplicationContext(), CancelTripListner.class);
            startService(i);
        }
    }


    public void stopCancelService() {
        stopService(new Intent(getApplicationContext(), CancelTripListner.class));
    }


    // start alarm After trip
    public void triger_alarmManager_sendLocation_After() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                getApplicationContext().startForegroundService(new Intent(getApplicationContext(),
                        MyLocationServiceAfter.class));
            } catch (Exception ex) {
            }

        } else {
            startService(new Intent(getApplicationContext(), MyLocationServiceAfter.class));
        }
    }


    // Stop/Cancel alarm manager After trip
    public void stopAlarmManager_After() {
        stopService(new Intent(this, MyLocationServiceAfter.class));

    }


    private class LocationListenerMAPSMain implements android.location.LocationListener {

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
                driver_cur_lat = location.getLatitude();
                driver_cur_lng = location.getLongitude();

            } else if (location_ == null) {
                boolean network_enabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (network_enabled) {
                    location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    driver_cur_lat = location.getLatitude();
                    driver_cur_lng = location.getLongitude();
                }
            }
            // Toast.makeText(ctx,"first "+String.valueOf(newDistance),Toast.LENGTH_SHORT).show();


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
                        1,
                        1,
                        mLocationListener);

            } catch (java.lang.SecurityException ex) {
                // Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                //Log.d(TAG, "network provider does not exist, " + ex.getMessage());
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
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
            location = mLocationManager.getLastKnownLocation(provider);
            if (location != null) {
                driver_cur_lat = location.getLongitude();
                driver_cur_lng = location.getLatitude();
                // Toast.makeText(getApplicationContext(), String.valueOf(location.getLatitude())+ String.valueOf(location.getLongitude()) , Toast.LENGTH_LONG).show();
                handleNewLocation(location);
            } else if (location == null) {
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
                        driver_cur_lat = location.getLongitude();
                        driver_cur_lng = location.getLatitude();
                        // Toast.makeText(getApplicationContext(), String.valueOf(location.getLatitude())+ String.valueOf(location.getLongitude()) , Toast.LENGTH_LONG).show();
                        handleNewLocation(location);

                    }


                }
            }
        }
        creatEventMap();

        if (from_what_Act.equals("splash")) {
            // order_id = Integer.parseInt(new MySharedPreference(getApplicationContext()).getStringShared("cur_order_id"));
            order_id = new MySharedPreference(getApplicationContext()).getStringShared("cur_order_id");
            getorderDetailsSplash();

        } else if (!from_what_Act.equals("splash")) {
            method = "get_order_details";
            Volley_go();
        }

    }


//----------------------------------- drawing rout stuff --------------------------------------------------------//


    private String getUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            if (result != null) {
                parserTask.execute(result);
            }

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            if (result != null)
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(3);
                    lineOptions.color(Color.BLACK);

                    Log.d("onPostExecute", "onPostExecute lineoptions decoded");

                }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

}
