package com.turnpoint.ticram.Activites;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.turnpoint.ticram.Adapters.Adapter_cancel2;
import com.turnpoint.ticram.BuildConfig;
import com.turnpoint.ticram.DataParser;
import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.PathUrl;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.Volley.IResult;
import com.turnpoint.ticram.Volley.VolleyService;
import com.turnpoint.ticram.modules.Cancel_list_response;
import com.turnpoint.ticram.modules.Cancels;
import com.turnpoint.ticram.modules.FirebaseDoctorInfo;
import com.turnpoint.ticram.modules.NearestCar;
import com.turnpoint.ticram.modules.NearestCars;
import com.turnpoint.ticram.modules.OrderWaiting;
import com.turnpoint.ticram.modules.ResponseWaiting;
import com.turnpoint.ticram.modules.count_cars;
import com.turnpoint.ticram.modules.expectedFee;
import com.turnpoint.ticram.modules.myorder;
import com.turnpoint.ticram.modules.response_order;
import com.turnpoint.ticram.modules.result_transport_cars;
import com.turnpoint.ticram.modules.user_info_splash;
import com.turnpoint.ticram.modules.user_info_splash2;
import com.turnpoint.ticram.modules.user_info_splash3;
import com.turnpoint.ticram.modules.usual_result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    boolean checkCancel = true;
    private GoogleMap mMap;
    public static final String TAG = MapActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    double currentLatitude, currentLongitude;
    PopupWindow pw_waiting;
    CoordinatorLayout lay;
    public ProgressDialog loading;
    IResult iresult;
    VolleyService voly_ser;
    View layout_sub_menu_waiting;
    String final_des = "not_selected";
    String volly_ = "0";
    String des_selected = "not_yet";
    int min = 0;
    int dist = 0;
    String des_lat;
    String des_lon;
    Marker marker_user_start, marker_user_destination;
    BitmapDescriptor icon_user_start = null;
    BitmapDescriptor icon_user_destination = null;

    String order_id;
    String btnStatusTawaklnaClicked = "no";

    RelativeLayout content_drawer;
    DrawerLayout drawerLayout;
    String popupWindow_waittig_show = "noShow", menu_or_clear_loc;
    LinearLayout layout_des, lin_dis_time, lay_select_des_sep;

    ImageView img_drawer, img_marker_current, img_myprofile;
    TextView tv_driverName, tv_taxi_distanceFromMe, tv_cash, tv_driverRate, tv_driverMoney,
            tv_ExpectedFee, tv_ExpectedFee_text, tv_time_, tv_distance_, tv_des_text, tv_currentLocation, tv_no_cars_found;
    RatingBar rb;
    Location MyCurrentLocation, Current_loc;
    MarkerOptions markerOptionss_source;
    String selected_mainCat_type;
    String selected_subcat_car, seleted_subcat_count = "0";
    ArrayList<String> arry_count;
    count_cars selected_type;
    RecyclerView recyclerView;
    Button btn_tawklnaa;
    Geocoder geocoder;
    List<Address> addresses;
    public static String check_order = " ";

    boolean checkCancel_user = true;
    Adapter_cancel2 adapter;
    String seleted_id_reasonCancel;
    private Integer placesResult = 1220;
    private Integer placesResultCurrent = 1222;

    Handler movingCarsHandler = new Handler();

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
        setContentView(R.layout.drawer_layout);
        callBackVolly();
        Current_loc = MyCurrentLocation = SplashActivity.currentLocation;
        // startService(new Intent(getApplicationContext(), StartSinch.class));
        new MySharedPreference(getApplicationContext()).setIntShared("time_cancel_trip_saved", 0);
        new MySharedPreference(getApplicationContext()).setIntShared("timer_wousol_captain", 0);
        new MySharedPreference(getApplicationContext()).setStringShared("waiting_showing", "no");
        new MySharedPreference(getApplicationContext()).setStringShared("from_D_A_S_status", "normal");

        new MySharedPreference(getApplicationContext()).setStringShared("destination_selected", "no");
        new MySharedPreference(getApplicationContext()).setStringShared("destination_trip", "not_selected");
        new MySharedPreference(getApplicationContext()).setStringShared("startloc_selected", "no");
        new MySharedPreference(getApplicationContext()).setStringShared("startloc_trip", "not_selected");

        menu_or_clear_loc = "menu";
        selected_mainCat_type = "CAR";

        arry_count = new ArrayList<String>();
        for (int i = 1; i <= 100; i++)
            arry_count.add(String.valueOf(i));

        content_drawer = findViewById(R.id.left_drawer);
        drawerLayout = findViewById(R.id.drawer_layout);
        tv_currentLocation = findViewById(R.id.tv_currentLoc_txt);

        recyclerView = findViewById(R.id.recycleview);
        btn_tawklnaa = findViewById(R.id.btn_tawaklna);
        tv_cash = findViewById(R.id.tv_cash);
        try {
            icon_user_start = BitmapDescriptorFactory.fromResource(R.drawable.start_bullet);
            icon_user_destination = BitmapDescriptorFactory.fromResource(R.drawable.end_bullet);
        } catch (Exception ex) {
        }

        LayoutInflater inflater = (LayoutInflater) MapActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // layout_sub_menu_taxi = inflater.inflate(R.layout.subcatogaray_layout, null);

        layout_sub_menu_waiting = inflater.inflate(R.layout.waiting_for_captain, null);

        tv_time_ = findViewById(R.id.tv_time);
        tv_distance_ = findViewById(R.id.tv_distance);
        tv_taxi_distanceFromMe = findViewById(R.id.tv_time_to_user);
        img_marker_current = findViewById(R.id.test);
        lay = findViewById(R.id.relative);
        lay.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        img_drawer = findViewById(R.id.imageView5);

        // ===== hearder لوين العزم =============
        layout_des = findViewById(R.id.lay_select_des);
        lay_select_des_sep = findViewById(R.id.lay_select_des_sep);
        //relative_des.setVisibility(View.INVISIBLE);
        tv_des_text = findViewById(R.id.tv_des_text);
        layout_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnStatusTawaklnaClicked.equals("no")) {  // beacause after clicking on tawaklna we cant edit destination
                    startActivityForResult(new Intent(getApplicationContext(), AutoCompletePlace.class), placesResult);
                }
            }
        });
        //=========================


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


        //============== initilaize popup windows ================

        pw_waiting = new PopupWindow(
                layout_sub_menu_waiting,
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);


        lin_dis_time = findViewById(R.id.lay_dis_time);
        tv_ExpectedFee = findViewById(R.id.tv_expected_fee);
        tv_ExpectedFee_text = findViewById(R.id.textview_expectedFee_text);

        lin_dis_time.setVisibility(View.INVISIBLE);
        img_marker_current.setVisibility(View.VISIBLE);
        if (pw_waiting.isShowing()) {

            pw_waiting.dismiss();
        }


        //==============drawer =========================
        img_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu_or_clear_loc.equals("menu")) {
                    drawerLayout.openDrawer(content_drawer);
                } else if (menu_or_clear_loc.equals("clear")) {
                    Clear_locations();
                }

            }
        });

        tv_no_cars_found = findViewById(R.id.textView_no_gps_selected);

        tv_driverName = findViewById(R.id.driver_name);
        tv_driverRate = findViewById(R.id.driver_rate);
        tv_driverMoney = findViewById(R.id.driver_money);
        rb = findViewById(R.id.ratingBar);
        rb.setRating(Float.parseFloat(new MySharedPreference(getApplicationContext()).getStringShared("rate")));
        tv_driverName.setText(new MySharedPreference(getApplicationContext()).getStringShared("user_name"));
        tv_driverMoney.setText(new MySharedPreference(getApplicationContext()).getStringShared("balance") + " د.أ ");
        tv_driverRate.setText(new MySharedPreference(getApplicationContext()).getStringShared("rate"));

        img_myprofile = findViewById(R.id.drawer_imgdriver);

        try {
            Glide.with(getApplicationContext())
                    .load(new MySharedPreference
                            (getApplicationContext()).getStringShared("photo"))
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.user_imagehdpi)
                            .centerCrop()
                            .dontAnimate()
                            .dontTransform())
                    .into(img_myprofile);
        } catch (Exception ex) {
        }
        img_myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(content_drawer);
                startActivity(new Intent(getApplicationContext(), MyProfile.class));

            }
        });
        String versionName = BuildConfig.VERSION_NAME;
        TextView tv_buildV = findViewById(R.id.tv_build_v_num);
        tv_buildV.setText(versionName);

        LinearLayout lin_logout = findViewById(R.id.linlay_profile_logout);
        lin_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivity.this);
                alertDialogBuilder.setMessage(getResources().getString(R.string.are_you_sure));
                alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // stopService(new Intent(getApplicationContext(),StartSinch.class));
                                new MySharedPreference(getApplicationContext()).setStringShared("login_status", "logout");
                                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                                finish();
                            }
                        });
                alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });

        LinearLayout lin_shareApp = findViewById(R.id.linlay_profile_invite);
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
                    //sAux = sAux + "https://play.google.com/store/apps/details?id=" +appPackageName;
                    sAux = sAux + "http://www.ticram.com/share.php";

                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        LinearLayout lin_joinCaptain = findViewById(R.id.linlay_JoinCaptain);
        lin_joinCaptain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(content_drawer);
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Ticram");
                    String sAux = "https://new.faistec.com/reg-captain";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });
        LinearLayout lin_tarwejcode = findViewById(R.id.linlay_profile_code);
        lin_tarwejcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(content_drawer);
                startActivity(new Intent(getApplicationContext(), TarweejCode.class));

            }
        });

        LinearLayout lin_myorders = findViewById(R.id.linlay_profile_myorders);
        lin_myorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(content_drawer);
                startActivity(new Intent(getApplicationContext(), MyOrders2.class));

            }
        });

        LinearLayout lin_support = findViewById(R.id.linear_Support);
        lin_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(content_drawer);
                startActivity(new Intent(getApplicationContext(), Support.class));
            }
        });
        initVoleyOrderResult();

        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {
            }

            @Override
            public void onDrawerOpened(View view) {
                try {
                    rb.setRating(Float.parseFloat(new MySharedPreference(getApplicationContext()).getStringShared("rate")));
                    tv_driverName.setText(new MySharedPreference(getApplicationContext()).getStringShared("user_name"));
                    tv_driverMoney.setText(new MySharedPreference(getApplicationContext()).getStringShared("balance") + " د.أ ");
                    tv_driverRate.setText(new MySharedPreference(getApplicationContext()).getStringShared("rate"));
                    Glide.with(getApplicationContext()).load(new
                            MySharedPreference(getApplicationContext()).getStringShared("photo")).into(img_myprofile);

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


        // ===================delay to show popup window == check if reorder  == splash=======

        if (new MySharedPreference(getApplicationContext()).getStringShared("from_w_status").equals("myorderdetails")) {

            Volley_reorder();

        } else if (new MySharedPreference(getApplicationContext()).getStringShared("from_w_status").equals("splash")) {
            Volley_splash();
        } else if (new MySharedPreference(getApplicationContext()).getStringShared("from_w_status").equals("normal")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    show_pop_up_window();

                }
            }, 2000);

        }
        try {
            Double currentVersion = Double.parseDouble(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);

            if (SplashActivity.updateVersion > currentVersion && updateAlert == null) {
                updateDialogBuilder = new AlertDialog.Builder(MapActivity.this);
                updateDialogBuilder.setMessage("يرجى تحديث تطبيق تكرم الى احدث اصدار لتتمكن من الاستفادة من كل المميزات الجديدة")
                        .setTitle("هناك اصدار جديد " + SplashActivity.updateVersion)
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        movingCarsHandler.postDelayed(movingCarsRunnable, 1000);

        checkAndupdateVersion();
    }

    Integer movingCarsCounter = 0;
    Runnable movingCarsRunnable = new Runnable() {
        @Override
        public void run() {
            if (movingCarsCounter % 60 == 0) {
                getNearestCars("" + currentLatitude + "," + currentLongitude, false);
            } else {
                if (movingCarsCounter % 2 == 0) {
                    addNearestCars();
                }
            }
            movingCarsCounter++;
            movingCarsHandler.postDelayed(movingCarsRunnable, 1000);
        }
    };
    static AlertDialog.Builder updateDialogBuilder;
    static AlertDialog updateAlert;

    private IResult voleyOrderResult;

    private void initVoleyOrderResult() {
        voleyOrderResult = new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                try {
                    if (loading != null && loading.isShowing()) loading.dismiss();
                    Log.d("response", response);
                    final Button btn_cancel = layout_sub_menu_waiting.findViewById(R.id.button_cancel_order);

                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivity.this);
                            alertDialogBuilder.setMessage(getResources().getString(R.string.are_you_sure));
                            alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            volly_ = "get_cancel_reasons";
                                            Volley_go();

                                        }
                                    });
                            alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        }
                    });


                    btn_tawklnaa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (seleted_subcat_count.equals("0")) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.pick_up_first),
                                        Toast.LENGTH_SHORT).show();

                            } else if (!seleted_subcat_count.equals("no")) {
                                if (!selected_type.getTaxi().equalsIgnoreCase("1")) {
                                    showUnitPriceDialog();
                                    return;
                                }
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivity.this);
                                alertDialogBuilder.setMessage(getResources().getString(R.string.are_you_sure));
                                alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                volly_ = "2";
                                                Volley_go();
                                            }
                                        });
                                alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                Log.d("selected_values", selected_subcat_car + " -- " + seleted_subcat_count);
                                            }
                                        });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }


                        }
                    });


                    tv_cash.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Toast.makeText(MapActivity.this, "Cash Clicked", Toast.LENGTH_LONG).show();
                        }
                    });
                    try {
                        if (loading != null && loading.isShowing()) loading.dismiss();
                        Gson gson = new Gson();
                        response_order res = gson.fromJson(response, response_order.class);
                        if (res.getHandle().equals("10")) {
                            des_selected = new MySharedPreference(getApplicationContext()).getStringShared("destination_selected");
                            if (des_selected.equals("no")) {
                                // relative_des.setVisibility(View.INVISIBLE);
                            }
                            myorder myOrder = res.getOrder();
                            order_id = myOrder.getId().toString();
                            new MySharedPreference(getApplicationContext()).setStringShared("order_id", order_id);
                            pw_waiting.showAtLocation(lay, Gravity.BOTTOM, 0, 0);
                            popupWindow_waittig_show = "isShow";
                            Log.d("DialogAws", "aws");
                            btnStatusTawaklnaClicked = "yes";
                            img_drawer.setVisibility(View.INVISIBLE);
                            getCheckOrderResult();
                            if (popupWindow_waittig_show.equals("isShow")) {
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(check_order == "0"){
                                            showDialogElse();
                                        }
                                        pw_waiting.dismiss();

                                    }
                                }, 120 * 1000);
                            } else {
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {

                                        volly_ = "3";
                                        Volley_go();
                                    }
                                }, 1000 * 2);
                            }

                        } else {
                            Toast.makeText(MapActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception ex) {
                    }

                } catch (Exception ex) {
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                if (loading != null && loading.isShowing()) loading.dismiss();
                //Log.d("response_error", error.getMessage().toString());
                Toast.makeText(MapActivity.this, R.string.check_internet, Toast.LENGTH_LONG).show();
                //Toast.makeText(MapActivity.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();

            }
        };
    }

    public void choose_current_loc(View v) {
        if (btnStatusTawaklnaClicked.equals("no")) {  // beacause after clicking on tawaklna we cant edit destination
            startActivityForResult(new Intent(getApplicationContext(), AutoCompletePlaceCurrent.class), placesResultCurrent);
        }
    }


    public void zoom_into_my_loc(View v) {
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Current_loc.getLatitude(),
                            Current_loc.getLongitude()), 16.0f));

        } catch (Exception ex) {
        }
        //  Clear_locations();
    }

    private Integer numberOfUnits = 1;
    TextView dialogPriceTV;

    private void showUnitPriceDialog() {
        final Dialog priceDialog = new Dialog(MapActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        priceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        priceDialog.setCancelable(false);
        priceDialog.setContentView(R.layout.dialog_unit_price);

        units = new ArrayList<>();
        for (int i = 1; i < 11; i++) units.add(new GridObject("" + i, i == 1 ? 1 : 0));
        GridView gridView = priceDialog.findViewById(R.id.gridview);
        UnitsAdapter unitsAdapter = new UnitsAdapter(MapActivity.this);
        gridView.setAdapter(unitsAdapter);
        numberOfUnits = 1;
        dialogPriceTV = priceDialog.findViewById(R.id.text_dialog_price);
        TextView toLoc = priceDialog.findViewById(R.id.dialog_unit_to);
        toLoc.setText(new MySharedPreference(getApplicationContext()).getStringShared("startloc_trip"));
        TextView title = priceDialog.findViewById(R.id.dialog_unit_title);
        title.setText(selected_type.getChoose_text());

        try {
            dialogPriceTV.setText("" + (numberOfUnits * Double.parseDouble(selected_type.getFee())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                if (units.get(position).getState() == 1) return;


                try {
                    numberOfUnits = Integer.parseInt(units.get(position).getName());
                    dialogPriceTV.setText("" + (numberOfUnits * Double.parseDouble(selected_type.getFee())));
                    for (int i = 0; i < units.size(); i++) units.get(i).setState(0);
                    units.get(position).setState(1);
                    unitsAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        Button dialogButton1 = (Button) priceDialog.findViewById(R.id.button_yes);
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceDialog.dismiss();
                volly_ = "2";
                Volley_go();
            }
        });

        Button dialogButton2 = (Button) priceDialog.findViewById(R.id.button_no);
        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priceDialog.dismiss();
            }
        });

        priceDialog.show();

    }

    public void Clear_locations() {
        try {
            img_marker_current.setVisibility(View.VISIBLE);
            tv_currentLocation.setText("");
            tv_time_.setText("");
            tv_distance_.setText("");
            tv_des_text.setText(getResources().getString(R.string.chose_place));
            tv_ExpectedFee.setText("");
            tv_taxi_distanceFromMe.setText("--");
            new MySharedPreference(getApplicationContext()).setStringShared("destination_selected", "no");
            new MySharedPreference(getApplicationContext()).setStringShared("startloc_selected", "no");
            menu_or_clear_loc = "menu";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                img_drawer.setImageDrawable(getResources().getDrawable(R.drawable.menu, getApplicationContext().getTheme()));
            } else {
                img_drawer.setImageDrawable(getResources().getDrawable(R.drawable.menu));
            }

            // MAP stuff
            mMap.clear();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getNearestCars(currentLatitude + "," + currentLongitude, true);
                }
            }, 2000);
            markerOptionss_source = new MarkerOptions().
                    position(new LatLng(currentLatitude, currentLongitude)).icon(icon_user_start);
            //mMap.addMarker(markerOptionss_source);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 16.0f));
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
                    tv_currentLocation.setText(address);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            zoom_into_my_loc(null);

        } catch (Exception ex) {
        }
    }


    public void show_pop_up_window() {
        try {
            tv_ExpectedFee_text.setText(getResources().getString(R.string.expected_fee));
            new MySharedPreference(getApplicationContext()).setStringShared("fee_or_expected", "expected");
            getSubCars();

        } catch (Exception ex) {
        }
    }


    @Override
    protected void onResume() {
        super.onResume();


        movingCarsHandler.removeCallbacks(movingCarsRunnable);
        movingCarsHandler.postDelayed(movingCarsRunnable, 1000);

        tv_driverName.setText(new MySharedPreference(getApplicationContext()).getStringShared("user_name"));

        if (new MySharedPreference(getApplicationContext()).getStringShared("waiting_showing").equals("no")) {
            // show_choosen_startLoc();
            calculate_ditance_duration();
        } else if (new MySharedPreference(getApplicationContext()).getStringShared("waiting_showing").equals("yes")) {

            volly_ = "6";
            Volley_go();
        }


        // Toast.makeText(getApplicationContext(),"onresume", Toast.LENGTH_SHORT).show();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.turnpoint.ticram.FCM.fiinshMapActivity");
        BroadcastReceiver_UpdateUI_MapsActcvity receiver = new BroadcastReceiver_UpdateUI_MapsActcvity();
        registerReceiver(receiver, intentFilter);


    }


    public void getSubCars() {
        try {
            String startLoc_selected = new MySharedPreference(getApplicationContext()).
                    getStringShared("startloc_selected");
            if (startLoc_selected.equals("no")) {
                mMap.clear();


                //img_marker_current.setVisibility(View.INVISIBLE);
                markerOptionss_source = new MarkerOptions().
                        position(new LatLng(currentLatitude, currentLongitude)).icon(icon_user_start);
                //mMap.addMarker(markerOptionss_source);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 16.0f));

            } else if (startLoc_selected.equals("yes")) {
                tv_currentLocation.setText(new MySharedPreference(getApplicationContext()).getStringShared("startloc_trip"));
                String start_lat = new MySharedPreference(getApplicationContext()).getStringShared("startloc_lat");
                String start_lon = new MySharedPreference(getApplicationContext()).getStringShared("startloc_lon");
                try {
                    //img_marker_current.setVisibility(View.INVISIBLE);
                    markerOptionss_source = new MarkerOptions().
                            position(new LatLng(Double.parseDouble(start_lat), Double.parseDouble(start_lon))).icon(icon_user_start);
                    //mMap.addMarker(markerOptionss_source);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(start_lat), Double.parseDouble(start_lon)), 16.0f));
                } catch (Exception ex) {
                }
            }


            volly_ = "0";
            Volley_go();
        } catch (Exception ex) {
        }

    }

    public void show_choosen_startLoc() {

        String startLoc_selected = new MySharedPreference(getApplicationContext()).getStringShared("startloc_selected");

        if (startLoc_selected.equals("no")) {
            try {
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
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {
                        try {
                            //mMap.clear()
                            Current_loc = location;
                            markerOptionss_source = new MarkerOptions().
                                    position(new LatLng(Current_loc.getLatitude(),
                                            Current_loc.getLongitude())).
                                    icon(icon_user_start);
                            //mMap.addMarker(markerOptionss_source);
                            /*mMap.animateCamera(CameraUpdateFactory.
                                    newLatLngZoom(new LatLng(Current_loc.getLatitude(),
                                            Current_loc.getLongitude()), 16.0f));*/

                        } catch (Exception ex) {
                        }

                    }
                });


            } catch (Exception ex) {
            }

        }
        if (startLoc_selected.equals("yes")) {
            try {
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
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {
                        try {
                            Current_loc = location;
                        } catch (Exception ex) {
                        }

                    }
                });


                menu_or_clear_loc = "clear";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    img_drawer.setImageDrawable(getResources().getDrawable(R.drawable.reset_locations, getApplicationContext().getTheme()));
                } else {
                    img_drawer.setImageDrawable(getResources().getDrawable(R.drawable.reset_locations));
                }

                tv_currentLocation.setText(new MySharedPreference(getApplicationContext()).
                        getStringShared("startloc_trip"));
                getNearestCars(currentLatitude + "," + currentLongitude, true);
                mMap.clear();
                //img_marker_current.setVisibility(View.INVISIBLE);
                String start_lat = new MySharedPreference(getApplicationContext()).getStringShared("startloc_lat");
                String start_lon = new MySharedPreference(getApplicationContext()).getStringShared("startloc_lon");
                markerOptionss_source = new MarkerOptions().
                        position(new LatLng(Double.parseDouble(start_lat), Double.parseDouble(start_lon))).icon(icon_user_start);
                //mMap.addMarker(markerOptionss_source);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(start_lat), Double.parseDouble(start_lon)), 16.0f));
                volly_ = "0";
                Volley_go();


            } catch (Exception ex) {
            }
        }
    }


    public void calculate_ditance_duration() {
        String des_selected_ = new MySharedPreference(getApplicationContext()).
                getStringShared("destination_selected");
        String startLoc_selected = new MySharedPreference(getApplicationContext()).getStringShared("startloc_selected");
        if (des_selected_.equals("no")) {
            lin_dis_time.setVisibility(View.INVISIBLE);
            if (startLoc_selected.equals("no"))
                img_marker_current.setVisibility(View.VISIBLE);
            show_choosen_startLoc();
        } else if (des_selected_.equals("yes")) {
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
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    try {
                        Current_loc = location;
                    } catch (Exception ex) {
                    }

                }
            });

            try {
                menu_or_clear_loc = "clear";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    img_drawer.setImageDrawable(getResources().getDrawable(R.drawable.reset_locations, getApplicationContext().getTheme()));
                } else {
                    img_drawer.setImageDrawable(getResources().getDrawable(R.drawable.reset_locations));
                }

                final_des = new MySharedPreference(getApplicationContext()).getStringShared("destination_trip");
                lin_dis_time.setVisibility(View.VISIBLE);
                //img_marker_current.setVisibility(View.INVISIBLE);
                tv_des_text.setText(final_des);
                des_lat = new MySharedPreference(getApplicationContext()).getStringShared("destination_lat");
                des_lon = new MySharedPreference(getApplicationContext()).getStringShared("destination_lon");

                if (startLoc_selected.equals("yes")) {
                    try {
                        volly_ = "0";
                        Volley_go();

                        mMap.clear();
                        mMap.setPadding(100, 500, 100, 700);
                        String start_lat = new MySharedPreference(getApplicationContext()).getStringShared("startloc_lat");
                        String start_lon = new MySharedPreference(getApplicationContext()).getStringShared("startloc_lon");


                        MarkerOptions markerOptionsss = new MarkerOptions().
                                position(new LatLng(Double.parseDouble(des_lat), Double.parseDouble(des_lon))).icon(icon_user_destination);
                        mMap.addMarker(markerOptionsss);

                        //img_marker_current.setVisibility(View.INVISIBLE);
                        markerOptionss_source = new MarkerOptions().
                                position(new LatLng(Double.parseDouble(start_lat), Double.parseDouble(start_lon))).icon(icon_user_start);
                        mMap.addMarker(markerOptionss_source);

                   /* String url = getUrl(new LatLng(Double.valueOf(start_lat), Double.valueOf(start_lon)),
                            new LatLng(Double.parseDouble(des_lat), Double.parseDouble(des_lon)));
                    FetchUrl FetchUrl = new FetchUrl();
                    FetchUrl.execute(url);*/

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(new LatLng(Double.parseDouble(des_lat), Double.parseDouble(des_lon)));
                        builder.include(new LatLng(Double.parseDouble(start_lat), Double.parseDouble(start_lon)));
                        LatLngBounds bounds = builder.build();
                        int padding = 0;
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        mMap.moveCamera(cu);
                        mMap.animateCamera(cu);
                        img_marker_current.setVisibility(View.INVISIBLE);
                        // Volley_go(des_lat, des_lon);  // calculate distnace and time
                    } catch (Exception ex) {
                    }
                }

                if (startLoc_selected.equals("no")) {
                    try {

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
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                            @Override
                            public void onMyLocationChange(Location location) {
                                try {
                                    Current_loc = location;
                                } catch (Exception ex) {
                                }

                            }
                        });
                        mMap.clear();
                        mMap.setPadding(100, 450, 100, 600);
                        MarkerOptions markerOptionsss = new MarkerOptions().
                                position(new LatLng(Double.parseDouble(des_lat), Double.parseDouble(des_lon))).icon(icon_user_destination);
                        mMap.addMarker(markerOptionsss);

                        //img_marker_current.setVisibility(View.INVISIBLE);
                        markerOptionss_source = new MarkerOptions().
                                position(new LatLng(Current_loc.getLatitude(), Current_loc.getLongitude())).icon(icon_user_start);
                        //mMap.addMarker(markerOptionss_source);


                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(new LatLng(Double.parseDouble(des_lat), Double.parseDouble(des_lon)));
                        builder.include(new LatLng(currentLatitude, currentLongitude));
                        LatLngBounds bounds = builder.build();
                        int padding = 0;
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        mMap.moveCamera(cu);
                        mMap.animateCamera(cu);

                        //  Volley_go(des_lat, des_lon);  // calculate distnace and time
                    } catch (Exception ex) {
                    }
                }

            } catch (Exception ex) {
            }
        }


    }


    public void setBoundsMap(LatLng des) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(des);
        builder.include(new LatLng(currentLatitude, currentLongitude));
        LatLngBounds bounds = builder.build();
        int padding = 100;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);
        mMap.animateCamera(cu);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }


    private void handleNewLocation(Location location) {
        try {
            if (location == null || (location.getLatitude() + location.getLongitude() == 0) || (location.getLatitude() == currentLatitude && location.getLongitude() == currentLongitude))
                return;
            //mMap.clear();
            MyCurrentLocation = location;
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            if (Current_loc == null) {
                Current_loc = location;
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 16.0f));
        } catch (Exception ex) {
        }


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
                tv_currentLocation.setText(address);

                getNearestCars(currentLatitude + "," + currentLongitude, false);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Toast.makeText(getApplicationContext(), "onMapReady" , Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    200);
        } else {
            // Permission has already been granted
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

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                try {
                    //mMap.clear();
                    Current_loc = location;
                    // double lat = location.getLatitude();
                    // double lon = location.getLongitude();
                    markerOptionss_source = new MarkerOptions().
                            position(new LatLng(Current_loc.getLatitude(), Current_loc.getLongitude())).
                            icon(icon_user_start);
                    // mMap.addMarker(markerOptionss_source);
                   /*
                    mMap.animateCamera(CameraUpdateFactory.
                            newLatLngZoom(new LatLng(Current_loc.getLatitude(), Current_loc.getLongitude()), 16.0f));
*/
                } catch (Exception ex) {
                }

            }
        });
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                try {
                    if (new MySharedPreference(getApplicationContext()).getStringShared("destination_selected").equals("yes")) {
                        return;
                    }
                    LatLng midLatLng = mMap.getCameraPosition().target;
                    double selected_lat = midLatLng.latitude;
                    double selected_lon = midLatLng.longitude;
                    if (!oneTimeDisableClearPoint) {
                        img_marker_current.setVisibility(View.VISIBLE);
                        //mMap.clear();
                        des_lat = new MySharedPreference(getApplicationContext()).getStringShared("destination_lat");
                        des_lon = new MySharedPreference(getApplicationContext()).getStringShared("destination_lon");
                        if (new MySharedPreference(getApplicationContext()).getStringShared("destination_selected").equals("yes") && !(des_lat + des_lon).isEmpty()) {
                            MarkerOptions markerOptionsss = new MarkerOptions().
                                    position(new LatLng(Double.parseDouble(des_lat), Double.parseDouble(des_lon))).icon(icon_user_destination);
                            mMap.addMarker(markerOptionsss);
                        }
                    }
                    oneTimeDisableClearPoint = false;
                    new MySharedPreference(getApplicationContext()).setStringShared("startloc_lat", String.valueOf(selected_lat));
                    new MySharedPreference(getApplicationContext()).setStringShared("startloc_lon", String.valueOf(selected_lon));
                    new MySharedPreference(getApplicationContext()).setStringShared("startloc_selected", "yes");
                    currentLatitude = selected_lat;
                    currentLongitude = selected_lon;
                    String addressText = address_text();
                    if (addressText != null && !addressText.isEmpty()) {
                        new MySharedPreference(getApplicationContext()).setStringShared("startloc_trip", addressText);
                        tv_currentLocation.setText(addressText);
                        getNearestCars(currentLatitude + "," + currentLongitude, false);
                    }
                    volly_ = "0";
                    Volley_go();
                    volly_ = "5";
                    Volley_go();
                } catch (Exception ex) {
                }
            }
        });

        handleNewLocation(MyCurrentLocation);
    }

    Boolean oneTimeDisableClearPoint = true;

    public String address_text() {
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(currentLatitude, currentLongitude, 1);
            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                tv_currentLocation.setText(address);
                getNearestCars(currentLatitude + "," + currentLongitude, true);
                return address;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 200: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        MarkerOptions markerOptionsss = new MarkerOptions().
                                position(new LatLng(currentLatitude, currentLongitude)).icon(icon_user_start);
                        marker_user_start = mMap.addMarker(markerOptionsss);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 14.0f));
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
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }


    private void Volley_splash() {
        try {
            popupWindow_waittig_show = "isShow";
            btnStatusTawaklnaClicked = "yes";
            img_drawer.setVisibility(View.INVISIBLE);

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    pw_waiting.showAtLocation(lay, Gravity.BOTTOM, 0, 0);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pw_waiting.dismiss();
//                                        popupWindow_waittig_show = "noShow";
                            showDialogElse();
                        }
                    }, 60*1000);
                }
            }, 1000 * 2);

            volly_ = "3";
            if (loading != null && loading.isShowing()) loading.dismiss();
            loading = ProgressDialog.show(MapActivity.this, "",
                    getResources().getString(R.string.please_wait), false, false);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", new GetCurrentLanguagePhone().getLang());
            params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("order_id", new MySharedPreference(getApplicationContext()).getStringShared("order_id"));
            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url") + PathUrl.View_Transport_Order, params);

            Log.d("ressssss", new MySharedPreference(MapActivity.this).getStringShared("access_token")
                    + "  --  " + new GetCurrentLanguagePhone().getLang() + " ---- " +
                    new MySharedPreference(getApplicationContext()).getStringShared("user_id") + " -- " +
                    new MySharedPreference(getApplicationContext()).getStringShared("order_id"));
            //Toast.makeText(getApplicationContext(), new MySharedPreference(getApplicationContext()).getStringShared("order_id"),Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
        }
    }


    private void Volley_reorder() {
        try {
            popupWindow_waittig_show = "isShow";
            volly_ = "2";


            if (loading != null && loading.isShowing()) loading.dismiss();
            loading = ProgressDialog.show(MapActivity.this, "",
                    getResources().getString(R.string.please_wait), false, false);

            String start_loc = getIntent().getExtras().getString("location");
            String subtype = getIntent().getExtras().getString("subtype");
            String tmp_dist = getIntent().getExtras().getString("tmp_distance");
            String tmp_time = getIntent().getExtras().getString("tmp_time");
            String main_type = getIntent().getExtras().getString("main_type");

            //Toast.makeText(getApplicationContext(), start_loc, Toast.LENGTH_SHORT).show();

            if (getIntent().getExtras().getString("destination_exist").equals("yes")) {
                String toLocation = getIntent().getExtras().getString("tolocation");

                Map<String, String> params = new Hashtable<String, String>();
                params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                params.put("local", new GetCurrentLanguagePhone().getLang());
                params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                params.put("location", start_loc);
                params.put("radius", "10");
                params.put("to_location", toLocation);
                params.put("type", main_type);
                params.put("subtype", subtype);
                params.put("payment_type", new MySharedPreference(getApplicationContext()).getStringShared("PaymentMethod"));
                params.put("tmp_distance", tmp_dist);
                params.put("tmp_time", tmp_time);
                params.put("taxi", selected_type.getTaxi());
                params.put("notes", selected_type.getTaxi().equalsIgnoreCase("1") ? "" : "" + numberOfUnits);

                //params.put("taxi", "1");
                voly_ser = new VolleyService(voleyOrderResult, getApplicationContext());
                voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url") + PathUrl.Send_Order_Transport, params);
            } else if (getIntent().getExtras().getString("destination_exist").equals("no")) {

                Map<String, String> params = new Hashtable<String, String>();
                params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                params.put("local", new GetCurrentLanguagePhone().getLang());
                params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                params.put("location", start_loc);
                params.put("radius", "10");
                // params.put("to_location",toLocation);
                params.put("type", main_type);
                params.put("subtype", subtype);
                params.put("payment_type", new MySharedPreference(getApplicationContext()).getStringShared("PaymentMethod"));
                params.put("tmp_distance", tmp_dist);
                params.put("tmp_time", tmp_time);
                params.put("taxi", selected_type.getTaxi());
                params.put("notes", selected_type.getTaxi().equalsIgnoreCase("1") ? "" : "" + numberOfUnits);

                voly_ser = new VolleyService(voleyOrderResult, getApplicationContext());
                voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url") + PathUrl.Send_Order_Transport, params);
            }


        } catch (Exception ex) {
        }
    }

    long LastTimeCall = 0l;

    private void showDialogElse() {

        if (checkCancel) {

            final android.app.AlertDialog.Builder alertDialoge = new android.app.AlertDialog.Builder(MapActivity.this);
            alertDialoge.setTitle(R.string.delete_oreder);
            alertDialoge.setMessage(R.string.no_driver);
            alertDialoge.setCancelable(false);

            alertDialoge.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    checkCancel_user = false;
                    volly_ = "get_cancel_reasons";
                    Volley_go();
                    dialogInterface.dismiss();
                }
            });
            alertDialoge.show();
        } else {
            checkCancel = true;
        }

    }

    private void Volley_go() {
        if (volly_.equals("0")) {    // get available cars

            if (LastTimeCall != 0l) {
                long finish = System.nanoTime();
                long timeElapsed = (finish - LastTimeCall) / 1000000;
                if (timeElapsed < 5000) {
                    return;
                }
                LastTimeCall = finish;
            } else {
                LastTimeCall = System.nanoTime();
            }

            tv_no_cars_found.setVisibility(View.GONE);
            String startLoc_selected = new MySharedPreference(getApplicationContext()).getStringShared("startloc_selected");
            if (startLoc_selected.equals("yes")) {
                try {
                    String start_lat = new MySharedPreference(getApplicationContext()).getStringShared("startloc_lat");
                    String start_lon = new MySharedPreference(getApplicationContext()).getStringShared("startloc_lon");
                    if (start_lat.equalsIgnoreCase("0.0") || start_lon.equalsIgnoreCase("0.0")) {
                        tv_no_cars_found.setVisibility(View.VISIBLE);
                        return;

                    }
                    //loading = ProgressDialog.show(MapActivity.this, "", getResources().getString(R.string.please_wait), false, false);
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                    params.put("local", new GetCurrentLanguagePhone().getLang());
                    params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                    params.put("location", start_lat + "," + start_lon);
                    params.put("type", selected_mainCat_type);
                    System.out.println("transports_around start_lat " + currentLatitude + "," + currentLongitude);
                    getNearestCars(start_lat + "," + start_lon, true);
                    //params.put("taxi", "1");
                    voly_ser = new VolleyService(new IResult() {
                        @Override
                        public void notifySuccessPost(String response) {
                            try {
                                if (loading != null && loading.isShowing()) loading.dismiss();
                                Log.d("response", response);
                                final Button btn_cancel = layout_sub_menu_waiting.findViewById(R.id.button_cancel_order);

                                btn_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivity.this);
                                        alertDialogBuilder.setMessage(getResources().getString(R.string.are_you_sure));
                                        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                        volly_ = "get_cancel_reasons";
                                                        Volley_go();

                                                    }
                                                });
                                        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no),
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                    }
                                                });
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();

                                    }
                                });


                                btn_tawklnaa.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (seleted_subcat_count.equals("0")) {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.pick_up_first),
                                                    Toast.LENGTH_SHORT).show();

                                        } else if (!seleted_subcat_count.equals("no")) {
                                            if (!selected_type.getTaxi().equalsIgnoreCase("1")) {
                                                showUnitPriceDialog();
                                                return;
                                            }
                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivity.this);
                                            alertDialogBuilder.setMessage(getResources().getString(R.string.are_you_sure));
                                            alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface arg0, int arg1) {


                                                            volly_ = "2";
                                                            Volley_go();


                                                        }
                                                    });
                                            alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no),
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface arg0, int arg1) {
                                                            Log.d("selected_values", selected_subcat_car + " -- " + seleted_subcat_count);
                                                        }
                                                    });
                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            alertDialog.show();


                                        }
                                    }
                                });


                                tv_cash.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Toast.makeText(MapActivity.this, "Cash Clicked", Toast.LENGTH_LONG).show();
                                    }
                                });


                                try {
                                    //Toast.makeText(getApplicationContext(), response , Toast.LENGTH_SHORT).show();
                                    Gson gson = new Gson();
                                    result_transport_cars res = gson.fromJson(response, result_transport_cars.class);
                                    if (res.getHandle().equals("05")) {  // no cars found :(
                                        tv_no_cars_found.setVisibility(View.VISIBLE);
                                        Toast.makeText(MapActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                                        selected_type = null;
                                        recyclerView.setHasFixedSize(true);
                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                        recyclerView.setLayoutManager(linearLayoutManager);
                                        Adapter_subCatogary2 mAdapter = new Adapter_subCatogary2(getApplicationContext(),
                                                new ArrayList<>());
                                        recyclerView.setAdapter(mAdapter);

                                    }
                                    if (res.getHandle().equals("10")) {  // found cars :)
                                        // mPopupWindow.dismiss();
                                        if (pw_waiting.isShowing()) {
                                            pw_waiting.dismiss();
                                        }

                                        // Toast.makeText(MapActivity.this,res.getMsg(), Toast.LENGTH_LONG).show();
                                        List<count_cars> returened_cars = res.getCount();
                                        if (returened_cars.size() > 0) {
                                            for (int i = 0; i < returened_cars.size(); i++) {
                                                count_cars item = returened_cars.get(i);
                                                if (selected_type == null && (item.getSubtypeTxt().equalsIgnoreCase("special") || item.getSubtypeTxt().equalsIgnoreCase("خصوصي"))) {
                                                    selected_type = item;
                                                }
                                            }
                                            recyclerView.setHasFixedSize(true);
                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                            recyclerView.setLayoutManager(linearLayoutManager);
                                            Adapter_subCatogary2 mAdapter = new Adapter_subCatogary2(getApplicationContext(),
                                                    returened_cars);
                                            recyclerView.setAdapter(mAdapter);


                                        }

                                    } else {
                                        Toast.makeText(MapActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }


                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void notifyError(VolleyError error) {
                            //Log.d("response_error", error.getMessage().toString());
                            Toast.makeText(MapActivity.this, R.string.check_internet, Toast.LENGTH_LONG).show();
                            //Toast.makeText(MapActivity.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    }, getApplicationContext());
                    voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                            .getStringShared("base_url") + PathUrl.TRANSPORT_INFO, params);

                    Log.d("ressssss", new MySharedPreference(MapActivity.this).getStringShared("access_token")
                            + "  --  " + new GetCurrentLanguagePhone().getLang() + " -- " +
                            new MySharedPreference(getApplicationContext()).getStringShared("user_id") +
                            " -- " + start_lat + "," + start_lon + " -- " + selected_mainCat_type);
                } catch (Exception ex) {
                }
            } else if (startLoc_selected.equals("no")) {
                try {
                    if (loading != null && loading.isShowing()) loading.dismiss();
                    loading = ProgressDialog.show(MapActivity.this, "",
                            getResources().getString(R.string.please_wait), false, false);
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                    params.put("local", new GetCurrentLanguagePhone().getLang());
                    params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                    params.put("location", currentLatitude + "," + currentLongitude);
                    // params.put("location", "32.015695, 35.867955");
                    params.put("type", selected_mainCat_type);
                    //- params.put("taxi", "1");
                    System.out.println("transports_around currentLatitude " + currentLatitude + "," + currentLongitude);
                    getNearestCars(currentLatitude + "," + currentLongitude, true);
                    voly_ser = new VolleyService(new IResult() {
                        @Override
                        public void notifySuccessPost(String response) {
                            try {
                                if (loading != null && loading.isShowing()) loading.dismiss();
                                Log.d("response", response);
                                final Button btn_cancel = layout_sub_menu_waiting.findViewById(R.id.button_cancel_order);

                                btn_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivity.this);
                                        alertDialogBuilder.setMessage(getResources().getString(R.string.are_you_sure));
                                        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                        volly_ = "get_cancel_reasons";
                                                        Volley_go();

                                                    }
                                                });
                                        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no),
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {
                                                    }
                                                });
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();

                                    }
                                });


                                btn_tawklnaa.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (seleted_subcat_count.equals("0")) {
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.pick_up_first),
                                                    Toast.LENGTH_SHORT).show();

                                        } else if (!seleted_subcat_count.equals("no")) {
                                            if (!selected_type.getTaxi().equalsIgnoreCase("1")) {
                                                showUnitPriceDialog();
                                                return;
                                            }
                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivity.this);
                                            alertDialogBuilder.setMessage(getResources().getString(R.string.are_you_sure));
                                            alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface arg0, int arg1) {


                                                            volly_ = "2";
                                                            Volley_go();


                                                        }
                                                    });
                                            alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no),
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface arg0, int arg1) {
                                                            Log.d("selected_values", selected_subcat_car + " -- " + seleted_subcat_count);
                                                        }
                                                    });
                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            alertDialog.show();


                                        }
                                    }
                                });


                                tv_cash.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Toast.makeText(MapActivity.this, "Cash Clicked", Toast.LENGTH_LONG).show();
                                    }
                                });


                                try {
                                    //Toast.makeText(getApplicationContext(), response , Toast.LENGTH_SHORT).show();
                                    if (loading != null && loading.isShowing()) loading.dismiss();
                                    Gson gson = new Gson();
                                    result_transport_cars res = gson.fromJson(response, result_transport_cars.class);
                                    if (res.getHandle().equals("05")) {  // no cars found :(
                                        tv_no_cars_found.setVisibility(View.VISIBLE);
                                        Toast.makeText(MapActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                                    }
                                    if (res.getHandle().equals("10")) {  // found cars :)
                                        // mPopupWindow.dismiss();
                                        if (pw_waiting.isShowing()) {
                                            pw_waiting.dismiss();
                                        }

                                        // Toast.makeText(MapActivity.this,res.getMsg(), Toast.LENGTH_LONG).show();
                                        List<count_cars> returened_cars = res.getCount();
                                        if (returened_cars.size() > 0) {
                                            for (int i = 0; i < returened_cars.size(); i++) {
                                                count_cars item = returened_cars.get(i);
                                                if (selected_type == null && (item.getSubtypeTxt().equalsIgnoreCase("special") || item.getSubtypeTxt().equalsIgnoreCase("خصوصي"))) {
                                                    selected_type = item;
                                                }
                                            }
                                            recyclerView.setHasFixedSize(true);
                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                            recyclerView.setLayoutManager(linearLayoutManager);
                                            Adapter_subCatogary2 mAdapter = new Adapter_subCatogary2(getApplicationContext(),
                                                    returened_cars);
                                            recyclerView.setAdapter(mAdapter);


                                        }

                                    } else {
                                        Toast.makeText(MapActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception ex) {
                                }


                            } catch (Exception ex) {
                            }
                        }

                        @Override
                        public void notifyError(VolleyError error) {
                            if (loading != null && loading.isShowing()) loading.dismiss();
                            //Log.d("response_error", error.getMessage().toString());
                            Toast.makeText(MapActivity.this, R.string.check_internet, Toast.LENGTH_LONG).show();
                            //Toast.makeText(MapActivity.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    }, getApplicationContext());
                    voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                            .getStringShared("base_url") + PathUrl.TRANSPORT_INFO, params);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }


        if (volly_.equals("2")) {   // tawklna

            //Toast.makeText(getApplicationContext(), selected_count_spinner, Toast.LENGTH_SHORT).show();

            try {
                if (loading != null && loading.isShowing()) loading.dismiss();
                loading = ProgressDialog.show(MapActivity.this, "",
                        getResources().getString(R.string.please_wait), false, false);

                if (new MySharedPreference(getApplicationContext()).getStringShared("destination_selected").equals("yes")) {
                    des_lat = new MySharedPreference(getApplicationContext()).getStringShared("destination_lat");
                    des_lon = new MySharedPreference(getApplicationContext()).getStringShared("destination_lon");
                    String startLoc_selected = new MySharedPreference(getApplicationContext()).getStringShared("startloc_selected");
                    if (startLoc_selected.equals("yes")) {
                        String start_lat = new MySharedPreference(getApplicationContext()).getStringShared("startloc_lat");
                        String start_lon = new MySharedPreference(getApplicationContext()).getStringShared("startloc_lon");
                        Map<String, String> params = new Hashtable<String, String>();
                        params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                        params.put("local", new GetCurrentLanguagePhone().getLang());
                        params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                        params.put("location", start_lat + "," + start_lon);
                        params.put("radius", "10");
                        params.put("to_location", des_lat + "," + des_lon);
                        params.put("type", selected_mainCat_type);
                        params.put("subtype", selected_subcat_car);
                        params.put("payment_type", new MySharedPreference(getApplicationContext()).getStringShared("PaymentMethod"));
                        params.put("tmp_distance", String.valueOf(dist));
                        params.put("tmp_time", String.valueOf(min));
                        params.put("taxi", selected_type.getTaxi());
                        params.put("notes", selected_type.getTaxi().equalsIgnoreCase("1") ? "" : "" + numberOfUnits);
                        //params.put("taxi", "1");
                        voly_ser = new VolleyService(voleyOrderResult, getApplicationContext());
                        voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                                .getStringShared("base_url") + PathUrl.Send_Order_Transport, params);
                        //  Toast.makeText(getApplicationContext(), start_lat+","+ start_lon , Toast.LENGTH_SHORT).show();
                    } else if (startLoc_selected.equals("no")) {
                        Map<String, String> params = new Hashtable<String, String>();
                        params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                        params.put("local", new GetCurrentLanguagePhone().getLang());
                        params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                        params.put("location", String.valueOf(currentLatitude) + "," + String.valueOf(currentLongitude));
                        params.put("radius", "10");
                        params.put("to_location", des_lat + "," + des_lon);
                        //params.put("to_location", des_lat + "," + des_lon);
                        params.put("type", "CAR");
                        params.put("subtype", selected_subcat_car);
                        params.put("payment_type", new MySharedPreference(getApplicationContext()).getStringShared("PaymentMethod"));
                        params.put("tmp_distance", String.valueOf(dist));
                        params.put("tmp_time", String.valueOf(min));
                        params.put("taxi", selected_type.getTaxi());
                        params.put("notes", selected_type.getTaxi().equalsIgnoreCase("1") ? "" : "" + numberOfUnits);
                        voly_ser = new VolleyService(voleyOrderResult, getApplicationContext());
                        voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                                .getStringShared("base_url") + PathUrl.Send_Order_Transport, params);
                    }

                    //Toast.makeText(getApplicationContext(), String.valueOf(des_lat)+","+ String.valueOf(des_lon), Toast.LENGTH_SHORT).show();
                } else if (new MySharedPreference(getApplicationContext()).getStringShared("destination_selected").equals("no")) {

                    String startLoc_selected = new MySharedPreference(getApplicationContext()).getStringShared("startloc_selected");
                    if (startLoc_selected.equals("yes")) {
                        String start_lat = new MySharedPreference(getApplicationContext()).getStringShared("startloc_lat");
                        String start_lon = new MySharedPreference(getApplicationContext()).getStringShared("startloc_lon");
                        Map<String, String> params = new Hashtable<String, String>();
                        params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                        params.put("local", new GetCurrentLanguagePhone().getLang());
                        params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                        params.put("location", start_lat + "," + start_lon);
                        params.put("radius", "10");
                        //params.put("to_location","";
                        params.put("type", "CAR");
                        params.put("subtype", selected_subcat_car);
                        params.put("payment_type", new MySharedPreference(getApplicationContext()).getStringShared("PaymentMethod"));
                        params.put("tmp_distance", "0");
                        params.put("tmp_time", "0");
                        params.put("taxi", selected_type.getTaxi());
                        params.put("notes", selected_type.getTaxi().equalsIgnoreCase("1") ? "" : "" + numberOfUnits);
                        //params.put("taxi", "1");
                        voly_ser = new VolleyService(voleyOrderResult, getApplicationContext());
                        voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                                .getStringShared("base_url") + PathUrl.Send_Order_Transport, params);
                        // Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
                    } else if (startLoc_selected.equals("no")) {
                        Map<String, String> params = new Hashtable<String, String>();
                        params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                        params.put("local", new GetCurrentLanguagePhone().getLang());
                        params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                        params.put("location", String.valueOf(currentLatitude) + "," + String.valueOf(currentLongitude));
                        params.put("radius", "10");
                        //params.put("to_location","";
                        params.put("type", "CAR");
                        params.put("subtype", selected_subcat_car);
                        params.put("payment_type", new MySharedPreference(getApplicationContext()).getStringShared("PaymentMethod"));
                        params.put("tmp_distance", "0");
                        params.put("tmp_time", "0");
                        params.put("taxi", selected_type.getTaxi());
                        params.put("notes", selected_type.getTaxi().equalsIgnoreCase("1") ? "" : "" + numberOfUnits);

                        //params.put("taxi", "1");
                        voly_ser = new VolleyService(voleyOrderResult, getApplicationContext());
                        voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                                .getStringShared("base_url") + PathUrl.Send_Order_Transport, params);
                        // Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
                    }

                }

            } catch (Exception ex) {
            }

        }


        if (volly_.equals("3")) {    // get order details
            try {

                if (loading != null && loading.isShowing()) loading.dismiss();
                loading = ProgressDialog.show(MapActivity.this, "",
                        getResources().getString(R.string.please_wait), false, false);
                Map<String, String> params = new Hashtable<String, String>();
                params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                params.put("local", new GetCurrentLanguagePhone().getLang());
                params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                params.put("order_id", order_id);
                voly_ser = new VolleyService(iresult, getApplicationContext());
                voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url") + PathUrl.View_Transport_Order, params);


            } catch (Exception ex) {
            }
        }

        if (volly_.equals("4")) {    // cancel order
            try {
                if (loading != null && loading.isShowing()) loading.dismiss();
                loading = ProgressDialog.show(MapActivity.this, "",
                        getResources().getString(R.string.please_wait),
                        false, false);
                Map<String, String> params = new Hashtable<String, String>();
                params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                params.put("local", new GetCurrentLanguagePhone().getLang());
                params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                params.put("order_id", new MySharedPreference(getApplicationContext()).getStringShared("order_id"));
                params.put("cancel_id", seleted_id_reasonCancel);
                voly_ser = new VolleyService(iresult, getApplicationContext());
                voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url") + PathUrl.Cancel_Order, params);
            } catch (Exception ex) {
            }
        }


        if (volly_.equals("5") && selected_subcat_car != null && new MySharedPreference(getApplicationContext()).getStringShared("destination_selected").equals("yes")) {    // expected fee
            des_lat = new MySharedPreference(getApplicationContext()).getStringShared("destination_lat");
            des_lon = new MySharedPreference(getApplicationContext()).getStringShared("destination_lon");
            String startLoc_selected = new MySharedPreference(getApplicationContext()).getStringShared("startloc_selected");

            if (startLoc_selected.equals("yes")) {
                try {
                    String start_lat = new MySharedPreference(getApplicationContext()).getStringShared("startloc_lat");
                    String start_lon = new MySharedPreference(getApplicationContext()).getStringShared("startloc_lon");
                    if (loading != null && loading.isShowing()) loading.dismiss();
                    //loading = ProgressDialog.show(MapActivity.this, "",
                    //      getResources().getString(R.string.please_wait), false, false);
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put("local", "ara");
                    params.put("type", selected_mainCat_type);
                    params.put("subtype", selected_subcat_car);
                    params.put("location", start_lat + "," + start_lon);
                    params.put("to_location", des_lat + "," + des_lon);
                    voly_ser = new VolleyService(new IResult() {
                        @Override
                        public void notifySuccessPost(String response) {
                            try {
                                if (loading != null && loading.isShowing()) loading.dismiss();
                                Log.d("response", response);
                                // get expected fee
                                try {
                                    if (loading != null && loading.isShowing())
                                        loading.dismiss();
                                    Gson gson = new Gson();
                                    expectedFee res = gson.fromJson(response, expectedFee.class);
                                    if (res.getHandle().equals("10")) {
                                        if (selected_type.getTaxi().equalsIgnoreCase("1"))
                                            tv_ExpectedFee.setText(res.getExpectfee() + "د.أ");

                                        tv_time_.setText(res.getTime());
                                        tv_distance_.setText(res.getDistance());
                                    }
                                } catch (Exception ex) {
                                }


                            } catch (Exception ex) {
                            }
                        }

                        @Override
                        public void notifyError(VolleyError error) {
                            if (loading != null && loading.isShowing()) loading.dismiss();
                            //Log.d("response_error", error.getMessage().toString());
                            Toast.makeText(MapActivity.this, R.string.check_internet, Toast.LENGTH_LONG).show();
                            //Toast.makeText(MapActivity.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();

                        }
                    }, getApplicationContext());
                    voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                            .getStringShared("base_url") + PathUrl.ExpectedFee, params);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (startLoc_selected.equals("no")) {
                try {
                    if (loading != null && loading.isShowing()) loading.dismiss();
                    //loading = ProgressDialog.show(MapActivity.this, "",
                    //      getResources().getString(R.string.please_wait), false, false);
                    Map<String, String> params = new Hashtable<String, String>();
                    params.put("local", new GetCurrentLanguagePhone().getLang());
                    params.put("type", selected_mainCat_type);
                    params.put("subtype", selected_subcat_car);
                    params.put("location", String.valueOf(currentLatitude) + "," + String.valueOf(currentLongitude));
                    params.put("to_location", des_lat + "," + des_lon);
                    voly_ser = new VolleyService(new IResult() {
                        @Override
                        public void notifySuccessPost(String response) {
                            try {
                                if (loading != null && loading.isShowing()) loading.dismiss();
                                Log.d("response", response);
                                // get expected fee
                                try {
                                    if (loading != null && loading.isShowing())
                                        loading.dismiss();
                                    Gson gson = new Gson();
                                    expectedFee res = gson.fromJson(response, expectedFee.class);
                                    if (res.getHandle().equals("10")) {
                                        if (selected_type.getTaxi().equalsIgnoreCase("1"))
                                            tv_ExpectedFee.setText(res.getExpectfee() + "د.أ");

                                        tv_time_.setText(res.getTime());
                                        tv_distance_.setText(res.getDistance());
                                    }
                                } catch (Exception ex) {
                                }


                            } catch (Exception ex) {
                            }
                        }

                        @Override
                        public void notifyError(VolleyError error) {
                            if (loading != null && loading.isShowing()) loading.dismiss();
                            //Log.d("response_error", error.getMessage().toString());
                            Toast.makeText(MapActivity.this, R.string.check_internet, Toast.LENGTH_LONG).show();
                            //Toast.makeText(MapActivity.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();

                        }
                    }, getApplicationContext());
                    voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                            .getStringShared("base_url") + PathUrl.ExpectedFee, params);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }


        if (volly_.equals("6")) {
            try {
                if (loading != null && loading.isShowing()) loading.dismiss();
                loading = ProgressDialog.show(MapActivity.this, "",
                        getResources().getString(R.string.please_wait), false, false);
                Map<String, String> params = new Hashtable<String, String>();
                params.put("access_token", new MySharedPreference(MapActivity.this).getStringShared("access_token"));
                params.put("local", new GetCurrentLanguagePhone().getLang());
                params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                voly_ser = new VolleyService(iresult, MapActivity.this);
                voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url") + PathUrl.USERInfo, params);
            } catch (Exception ex) {
            }
        }


        if (volly_.equals("get_cancel_reasons")) {
            try {
                checkCancel = false;
                if (loading != null && loading.isShowing()) loading.dismiss();
                loading = ProgressDialog.show(MapActivity.this, "",
                        getResources().getString(R.string.please_wait), false, false);
                voly_ser = new VolleyService(iresult, MapActivity.this);
                voly_ser.getDataVolley(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url") + PathUrl.GETCANCELReasons);
            } catch (Exception ex) {
            }
        }


    }


    // calculate distancce and time
    private void Volley_go(String des_lat, String des_lon) {
        volly_ = "1";
        String startLoc_selected = new MySharedPreference(getApplicationContext()).getStringShared("startloc_selected");
        if (startLoc_selected.equals("yes")) {
            try {
                String start_lat = new MySharedPreference(getApplicationContext()).getStringShared("startloc_lat");
                String start_lon = new MySharedPreference(getApplicationContext()).getStringShared("startloc_lon");
                String URL_DISTANCE = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +
                        Double.parseDouble(start_lat) + "," + Double.parseDouble(start_lon) + "&destinations=" + des_lat + "," + des_lon + "&mode=driving&language=fr-FR&avoid=tolls";
                if (loading != null && loading.isShowing()) loading.dismiss();
                loading = ProgressDialog.show(MapActivity.this, "",
                        getResources().getString(R.string.please_wait), false, false);
                voly_ser = new VolleyService(iresult, getApplicationContext());
                voly_ser.postDataVolley(URL_DISTANCE);
            } catch (Exception ex) {
            }
        } else if (startLoc_selected.equals("no")) {
            try {
                //Toast.makeText(getApplicationContext(), "calculate distancce and time 2", Toast.LENGTH_SHORT).show();
                String URL_DISTANCE = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +
                        currentLatitude + "," + currentLongitude + "&destinations=" + des_lat + "," + des_lon + "&mode=driving&language=fr-FR&avoid=tolls";
                if (loading != null && loading.isShowing()) loading.dismiss();
                loading = ProgressDialog.show(MapActivity.this, "",
                        getResources().getString(R.string.please_wait), false, false);
                voly_ser = new VolleyService(iresult, getApplicationContext());
                voly_ser.postDataVolley(URL_DISTANCE);
            } catch (Exception ex) {
            }
        }


    }


    void callBackVolly() {
        iresult = new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                try {
                    if (loading != null && loading.isShowing()) loading.dismiss();
                    Log.d("response", response);
                    final Button btn_cancel = layout_sub_menu_waiting.findViewById(R.id.button_cancel_order);

                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivity.this);
                            alertDialogBuilder.setMessage(getResources().getString(R.string.are_you_sure));
                            alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            volly_ = "get_cancel_reasons";
                                            Volley_go();

                                        }
                                    });
                            alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        }
                    });


                    btn_tawklnaa.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (seleted_subcat_count.equals("0")) {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.pick_up_first),
                                        Toast.LENGTH_SHORT).show();

                            } else if (!seleted_subcat_count.equals("no")) {
                                if (!selected_type.getTaxi().equalsIgnoreCase("1")) {
                                    showUnitPriceDialog();
                                    return;
                                }
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapActivity.this);
                                alertDialogBuilder.setMessage(getResources().getString(R.string.are_you_sure));
                                alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {


                                                volly_ = "2";
                                                Volley_go();


                                            }
                                        });
                                alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                Log.d("selected_values", selected_subcat_car + " -- " + seleted_subcat_count);
                                            }
                                        });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();


                            }
                        }
                    });


                    tv_cash.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Toast.makeText(MapActivity.this, "Cash Clicked", Toast.LENGTH_LONG).show();
                        }
                    });


                    if (volly_.equals("0")) {   // get available cars
                        try {
                            //Toast.makeText(getApplicationContext(), response , Toast.LENGTH_SHORT).show();
                            if (loading != null && loading.isShowing()) loading.dismiss();
                            Gson gson = new Gson();
                            result_transport_cars res = gson.fromJson(response, result_transport_cars.class);
                            if (res.getHandle().equals("05")) {  // no cars found :(
                                tv_no_cars_found.setVisibility(View.VISIBLE);
                                Toast.makeText(MapActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                            }
                            if (res.getHandle().equals("10")) {  // found cars :)
                                // mPopupWindow.dismiss();
                                if (pw_waiting.isShowing()) {
                                    pw_waiting.dismiss();
                                }

                                // Toast.makeText(MapActivity.this,res.getMsg(), Toast.LENGTH_LONG).show();
                                List<count_cars> returened_cars = res.getCount();
                                if (returened_cars.size() > 0) {

                                    for (int i = 0; i < returened_cars.size(); i++) {
                                        count_cars item = returened_cars.get(i);
                                        if (selected_type == null && (item.getSubtypeTxt().equalsIgnoreCase("special") || item.getSubtypeTxt().equalsIgnoreCase("خصوصي"))) {
                                            selected_type = item;
                                        }
                                    }

                                    recyclerView.setHasFixedSize(true);
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    Adapter_subCatogary2 mAdapter = new Adapter_subCatogary2(getApplicationContext(),
                                            returened_cars);
                                    recyclerView.setAdapter(mAdapter);


                                }

                            } else {
                                Toast.makeText(MapActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception ex) {
                        }
                    }


                    if (volly_.equals("1")) {  // expected time /destance
                        // distance
                        if (loading != null && loading.isShowing()) loading.dismiss();
                        try {
                            //Toast.makeText(getApplicationContext(), "calculate distancce and time", Toast.LENGTH_SHORT).show();
                            JSONObject root = new JSONObject(response);
                            JSONArray array_rows = root.getJSONArray("rows");
                            JSONObject object_rows = array_rows.getJSONObject(0);
                            JSONArray array_elements = object_rows.getJSONArray("elements");
                            JSONObject object_elements = array_elements.getJSONObject(0);
                            JSONObject object_duration = object_elements.getJSONObject("duration");
                            JSONObject object_distance = object_elements.getJSONObject("distance");
                            min = Integer.parseInt(object_duration.getString("value")) / 60;
                            dist = Integer.parseInt(object_distance.getString("value")) / 1000;
                            //Toast.makeText(MapActivity.this, String.valueOf(min), Toast.LENGTH_LONG).show();
                            // Toast.makeText(MapActivity.this, String.valueOf(dist), Toast.LENGTH_LONG).show();
                            tv_time_.setText(String.valueOf(min) + " min ");
                            tv_distance_.setText(String.valueOf(dist) + " km ");

                        } catch (JSONException e) {
                            Log.d("error", "error3");
                        }
                    }


                    if (volly_.equals("2")) {  // tawaklna
                        try {
                            if (loading != null && loading.isShowing()) loading.dismiss();
                            Gson gson = new Gson();
                            response_order res = gson.fromJson(response, response_order.class);
                            if (res.getHandle().equals("10")) {
                                des_selected = new MySharedPreference(getApplicationContext()).getStringShared("destination_selected");
                                if (des_selected.equals("no")) {
                                    // relative_des.setVisibility(View.INVISIBLE);
                                }
                                myorder myOrder = res.getOrder();
                                order_id = myOrder.getId().toString();
                                new MySharedPreference(getApplicationContext()).setStringShared("order_id", order_id);
                                pw_waiting.showAtLocation(lay, Gravity.BOTTOM, 0, 0);
                                popupWindow_waittig_show = "isShow";
                                btnStatusTawaklnaClicked = "yes";
                                img_drawer.setVisibility(View.INVISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {

                                        volly_ = "3";
                                        Volley_go();

                                    }
                                }, 1000 * 2);


                            } else {
                                Toast.makeText(MapActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception ex) {
                        }
                    }


                    if (volly_.equals("3")) {   // show waiting popwindow
                        try {
                            if (loading != null && loading.isShowing()) loading.dismiss();
                            // Toast.makeText(MapActivity.this, response, Toast.LENGTH_LONG).show();
                            new MySharedPreference(getApplicationContext()).setStringShared("waiting_showing", "yes");
                            Gson gson = new Gson();
                            ResponseWaiting res = gson.fromJson(response, ResponseWaiting.class);

                            if (res.getHandle().equals("10")) {
                                //   Toast.makeText(MapActivity.this, res.getHandle(), Toast.LENGTH_LONG).show();
                                OrderWaiting order_ = res.getOrder();

                            }
                        } catch (Exception ex) {
                        }
                    }

                    if (volly_.equals("4")) {    // cancel order
                        try {
                            if (loading != null && loading.isShowing()) loading.dismiss();
                            new MySharedPreference(getApplicationContext()).setStringShared("from_w_status", "normal");
                            Gson gson = new Gson();
                            usual_result res = gson.fromJson(response, usual_result.class);
                            if (res.getHandle().equals("10")) {
                                Toast.makeText(MapActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), MapActivity.class));
                                finish();
                            } else {
                                Toast.makeText(MapActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception ex) {
                        }
                    }

                    if (volly_.equals("5")) {    // get expected fee
                        try {
                            if (loading != null && loading.isShowing())
                                loading.dismiss();
                            Gson gson = new Gson();
                            expectedFee res = gson.fromJson(response, expectedFee.class);
                            if (res.getHandle().equals("10")) {
                                if (selected_type.getTaxi().equalsIgnoreCase("1"))
                                    tv_ExpectedFee.setText(res.getExpectfee() + "د.أ");

                                tv_time_.setText(res.getTime());
                                tv_distance_.setText(res.getDistance());
                            }
                        } catch (Exception ex) {
                        }
                    }


                    if (volly_.equals("6")) {
                        try {
                            if (loading != null && loading.isShowing()) loading.dismiss();
                            //Toast.makeText(SplashActivity.this, response, Toast.LENGTH_LONG).show();
                            Gson gson = new Gson();
                            user_info_splash res = gson.fromJson(response, user_info_splash.class);
                            if (res.getHandle().equals("02")) {  // account not found
                                Toast.makeText(MapActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MapActivity.this, LoginPhoneNumber.class);
                                startActivity(intent);
                                finish();
                            } else if (res.getHandle().equals("10")) {
                                user_info_splash2 user_info1 = res.getUser();
                                if (user_info1.getOrderStatus() == null) {
                                } else {

                                    if (!user_info1.getOrderStatus().equals("C") &&
                                            !user_info1.getOrderStatus().equals("N") &&
                                            !user_info1.getOrderStatus().equals("")) {

                                        if (user_info1.getOrderStatus().equals("W")) {  // waiting

                                        }


                                        if (user_info1.getOrderStatus().equals("D")) {  // accepted
                                            new MySharedPreference(getApplicationContext()).setStringShared("from_D_A_S_status", "splash");
                                            new MySharedPreference(getApplicationContext()).setStringShared("order_id", String.valueOf(user_info1.getOrderId()));
                                            new MySharedPreference(getApplicationContext()).setStringShared("order_status_splash", String.valueOf(user_info1.getOrderStatus()));
                                            Intent intent = new Intent(MapActivity.this, TripDetails.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        if (user_info1.getOrderStatus().equals("A")) {   // arrived
                                            new MySharedPreference(getApplicationContext()).setStringShared("from_D_A_S_status", "splash");
                                            new MySharedPreference(getApplicationContext()).setStringShared("order_id", String.valueOf(user_info1.getOrderId()));
                                            new MySharedPreference(getApplicationContext()).setStringShared("order_status_splash", String.valueOf(user_info1.getOrderStatus()));

                                            Intent intent = new Intent(MapActivity.this, TripDetails.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        if (user_info1.getOrderStatus().equals("S")) {   // start
                                            new MySharedPreference(getApplicationContext()).setStringShared("from_D_A_S_status", "splash");
                                            new MySharedPreference(getApplicationContext()).setStringShared("order_id", String.valueOf(user_info1.getOrderId()));
                                            new MySharedPreference(getApplicationContext()).setStringShared("order_status_splash", String.valueOf(user_info1.getOrderStatus()));
                                            Intent intent = new Intent(MapActivity.this, TripDetails.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        // payment and review.
                                        if (user_info1.getOrderStatus().equals("E") || user_info1.getOrderStatus().equals("F")) {

                                            user_info_splash3 u = user_info1.getOrder();
                                            // Toast.makeText(MapActivity.this, u.getRated(), Toast.LENGTH_LONG).show();

                                            if (u.getRated().equals("0")) {  // not rated
                                                new MySharedPreference(getApplicationContext()).setStringShared("order_id",
                                                        String.valueOf(user_info1.getOrderId()));
                                                Intent intent = new Intent(MapActivity.this, PaymentAndReview.class);
                                                startActivity(intent);
                                                finish();
                                            } else if (u.getRated().equals("1")) {  // rated
                                                // stay here
                                            }
                                        }
                                    } else if (
                                            user_info1.getOrderStatus().equals("C") ||
                                                    user_info1.getOrderStatus().equals("N") ||
                                                    user_info1.getOrderStatus().equals("") ||
                                                    user_info1.getOrderStatus() == null) {
                               /* Intent intent = new Intent(MapActivity.this, MapActivity.class);
                                startActivity(intent);
                                finish();*/
                                    }
                                }
                            }
                        } catch (Exception ex) {
                        }
                    }


                    if (volly_.equals("get_cancel_reasons")) {
                        try {
                            if (loading != null && loading.isShowing()) loading.dismiss();
                            Gson gson = new Gson();
                            Cancel_list_response res = gson.fromJson(response, Cancel_list_response.class);
                            if (res.getHandle().equals("02")) {  // account not found
                                Toast.makeText(MapActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                            } else if (res.getHandle().equals("10")) {
                                Cancels res2 = gson.fromJson(response, Cancels.class);

                                if (res.getCancels().size() > 0) {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapActivity.this);
                                    LayoutInflater inflater = getLayoutInflater();
                                    final View convertView = (View) inflater.inflate(R.layout.dialoge_cancel_reasons, null);
                                    ListView lv = (ListView) convertView.findViewById(R.id.List);
                                    adapter = new Adapter_cancel2(res.getCancels(), getApplicationContext());
                                    lv.setAdapter(adapter);
                                    if (checkCancel_user == false) {
                                        try {
                                            Cancels selectedObject = adapter.getSelected(4);
                                            seleted_id_reasonCancel = String.valueOf(selectedObject.getId());
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    volly_ = "4";
                                                    Volley_go();
                                                }
                                            }, 2000);

                                        } catch (Exception ex) {
                                        }
                                    } else {
                                        alertDialog.setView(convertView);
                                        alertDialog.setCancelable(false);
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
                                                            volly_ = "4";
                                                            Volley_go();
                                                        }
                                                    }, 2000);

                                                } catch (Exception ex) {
                                                }
                                            }

                                        });
                                    }
                                }
                            } else if (res.getHandle().equals("02")) {
                                Toast.makeText(MapActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MapActivity.this, res.getMsg(), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception ex) {
                        }
                    }


                } catch (Exception ex) {
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                if (loading != null && loading.isShowing()) loading.dismiss();
                //Log.d("response_error", error.getMessage().toString());
                Toast.makeText(MapActivity.this, R.string.check_internet, Toast.LENGTH_LONG).show();
                //Toast.makeText(MapActivity.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();

            }
        };
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (pw_waiting.isShowing()) {
            pw_waiting.dismiss();
        }
        finish();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // stopService(new Intent(getApplicationContext(),StartSinch.class));
        if (pw_waiting.isShowing()) {
            pw_waiting.dismiss();
        }
        movingCarsHandler.removeCallbacks(movingCarsRunnable);
        if (popupWindow_waittig_show.equals("isShow")) {
            SharedPreferences settings = getApplicationContext().getSharedPreferences("isDialogshow", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("isShow", "isSho");
            editor.commit();
        } else {
            SharedPreferences settings = getApplicationContext().getSharedPreferences("isDialogshow", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("isShow", "no");
            editor.commit();
        }

    }


    @Override
    public void onStop() {
        super.onStop();
    }


    public class BroadcastReceiver_UpdateUI_MapsActcvity extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            String state = extras.getString("extra");
            //Toast.makeText(getApplicationContext(),state, Toast.LENGTH_SHORT).show();
            if (!((Activity) context).isFinishing()) {
                updateUI();
            }
        }
    }


    public void updateUI() {
        finish();
    }


//=============================================draw polyline ==================================================


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
            //  Toast.makeText(getApplicationContext(),String.valueOf(currentLatitude)+" - "+ String.valueOf(currentLongitude), Toast.LENGTH_SHORT).show();

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
            parserTask.execute(result);

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
                lineOptions.width(5);
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


    class Adapter_subCatogary2 extends RecyclerView.Adapter<Adapter_subCatogary2.ViewHolder> {

        public List<count_cars> mItems;
        private Context mContext;
        int pos;
        List<LinearLayout> itemViewList = new ArrayList<>();
        List<ImageView> itemImages = new ArrayList<>();


        public Adapter_subCatogary2() {
        }


        public List<count_cars> getmItems() {
            return mItems;
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            public TextView tv_name, tv_num;
            LinearLayout layout_all;
            ImageView img_icon;

            public ViewHolder(View itemView) {
                super(itemView);
                //    Toast.makeText(mContext, "ViewHolder" , Toast.LENGTH_SHORT).show();

                tv_num = (TextView) itemView.findViewById(R.id.tv_count_yellow);
                tv_name = (TextView) itemView.findViewById(R.id.tv_yellow_taxi);
                img_icon = (ImageView) itemView.findViewById(R.id.icon_yellow_taxi);
                layout_all = (LinearLayout) itemView.findViewById(R.id.lin_tax_yellow);
            }


        }


        public Adapter_subCatogary2(Context context, List<count_cars> posts) {
            mItems = posts;
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // Toast.makeText(mContext, "nCreateViewHolder" , Toast.LENGTH_SHORT).show();
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View postView = inflater.inflate(R.layout.single_row_subcatogry, parent, false);

            ViewHolder viewHolder = new ViewHolder(postView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // Toast.makeText(mContext, "onBindViewHolder" , Toast.LENGTH_SHORT).show();
            this.pos = position;
            final count_cars item = mItems.get(position);
            TextView TV_name = holder.tv_name;
            TextView TV_num = holder.tv_num;
            ImageView img = holder.img_icon;
            final LinearLayout linearLayout = holder.layout_all;
            itemViewList.add(linearLayout);
            //itemImages.add(img);

            TV_name.setText(item.getSubtypeTxt());
            TV_num.setText(item.getCount());

            Glide.with(getApplicationContext())
                    .load(item.getIcon())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .centerCrop()
                            .dontAnimate()
                            .dontTransform()
                            .error(R.drawable.img_taxi)
                            .override(100, 100))
                    .into(img);


            View.OnClickListener click = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(mContext, "hoooo",Toast.LENGTH_LONG).show();

                    for (LinearLayout lin : itemViewList) {
                        lin.setBackgroundResource(R.drawable.circle_blankhdpi);
                    }
                    linearLayout.setBackgroundResource(R.drawable.circle_sellected);

                    // new MySharedPreference(mContext).setStringShared("selected_car_txt", item.getSubtype());
                    // new MySharedPreference(mContext).setStringShared("selected_car_count", item.getCount());
                    seleted_subcat_count = item.getCount();
                    selected_subcat_car = item.getSubtype();
                    selected_type = item;
                    tv_taxi_distanceFromMe.setText(item.getCaptain_arrival_time());
                    //Toast.makeText(getApplicationContext(), seleted_subcat_count,Toast.LENGTH_LONG).show();
                    if (!item.getTaxi().equalsIgnoreCase("1")) {
                        layout_des.setVisibility(View.GONE);
                        lay_select_des_sep.setVisibility(View.GONE);
                        tv_ExpectedFee.setText(item.getFee() + " " + item.getFee_text());
                        btn_tawklnaa.setText(getString(R.string.order_non_taxi));
                    } else {
                        tv_ExpectedFee.setText("");
                        btn_tawklnaa.setText(getString(R.string.tawklna));
                        layout_des.setVisibility(View.VISIBLE);
                        lay_select_des_sep.setVisibility(View.VISIBLE);
                    }


                    des_selected = new MySharedPreference(getApplicationContext()).getStringShared("destination_selected");
                    if (des_selected.equals("yes")) {
                        volly_ = "5";
                        Volley_go();
                    }


                }
            };

            linearLayout.setOnClickListener(click);


            if (item.getCount().equals("0")) {
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                img.setColorFilter(filter);
                TV_num.setVisibility(View.INVISIBLE);
                linearLayout.setClickable(false);

            } else if (selected_type.getSubtype().equalsIgnoreCase(item.getSubtype())) {
                click.onClick(null);
            }


            // Toast.makeText(getApplicationContext(), "dadada", Toast.LENGTH_LONG).show();


        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public void updateAnswers(List<count_cars> items) {
            mItems = items;
            notifyDataSetChanged();
        }

        private count_cars getItem(int adapterPosition) {
            return mItems.get(adapterPosition);
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == placesResult) {
            if (resultCode == Activity.RESULT_OK) {
                oneTimeDisableClearPoint = true;
                img_marker_current.setVisibility(View.INVISIBLE);
                volly_ = "5";
                Volley_go();
            }
        } else if (requestCode == placesResultCurrent) {
            if (resultCode == Activity.RESULT_OK) {
                oneTimeDisableClearPoint = true;
                img_marker_current.setVisibility(View.VISIBLE);
                volly_ = "5";
                Volley_go();
            }
        }
    }

    static class ViewHolder {
        TextView text;
        RelativeLayout backGround;
    }

    ArrayList<GridObject> units;

    private class UnitsAdapter extends BaseAdapter {

        private final Context mContext;

        private LayoutInflater mInflater;


        // 1
        public UnitsAdapter(Context context) {
            this.mContext = context;
            mInflater = LayoutInflater.from(context);

        }

        // 2
        @Override
        public int getCount() {
            return units.size();
        }

        // 3
        @Override
        public long getItemId(int position) {
            return 0;
        }

        // 4
        @Override
        public Object getItem(int position) {
            return null;
        }

        // 5
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            GridObject object = units.get(position);
            ViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.dialog_item, null);
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.textView);
                holder.backGround = (RelativeLayout) convertView.findViewById(R.id.textView_bg);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text.setText(object.getName());

            if (object.getState() == 1) {
                holder.backGround.setBackground(mContext.getResources().getDrawable(R.drawable.circle_green));
            } else {
                holder.backGround.setBackground(mContext.getResources().getDrawable(R.drawable.circle_gray));
            }


            /*TextView dummyTextView = new TextView(mContext);
            dummyTextView.setText(String.valueOf(position));

            return dummyTextView;*/

            /*View v = convertView;
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.dialog_item, null);
            TextView textView = (TextView) v.findViewById(R.id.textView);
            textView.setText(units.get(position).getName());*/
            /*textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textView.getBackground() == getResources().getDrawable(R.drawable.circle_gray)) {
                        textView.setBackground();
                    } else {

                    }
                }
            });*/

            return convertView;


        }

    }

    private class GridObject {

        private String name;
        private int state = 0;

        public GridObject(String name, int state) {
            super();
            this.name = name;
            this.state = state;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }


    private void checkAndupdateVersion() {
        try {
            if (PathUrl.VERSION_NUMBER.equals(new MySharedPreference(getApplicationContext()).getStringShared("VERSION_NUMBER")))
                return;
            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", new GetCurrentLanguagePhone().getLang());
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

    long LastTimeGetCar = 0l;

    void getNearestCars(String location, Boolean forceCheck) {
        try {
            if (!forceCheck && LastTimeGetCar != 0l) {
                long finish = System.nanoTime();
                long timeElapsed = (finish - LastTimeGetCar) / 1000000;
                if (timeElapsed < 10000) {
                    return;
                }
                LastTimeGetCar = finish;
            } else {
                LastTimeGetCar = System.nanoTime();
            }

            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", new GetCurrentLanguagePhone().getLang());
            params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("version", PathUrl.VERSION_NUMBER);
            params.put("location", location);
            params.put("type", "CAR");
            voly_ser = new VolleyService(new IResult() {
                @Override
                public void notifySuccessPost(String response) {
                    try {
                        Gson gson = new Gson();
                        NearestCars nearestCars = gson.fromJson(response, NearestCars.class);
                        if (nearestCars.getTransports() != null && nearestCars.getTransports().size() > 0) {

                            Iterator it = previousNearestListenerHashMap.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry item = (Map.Entry) it.next();
                                String id = item.getKey().toString();
                                Boolean found = false;
                                for (int i = 0; i < nearestCars.getTransports().size(); i++) {
                                    if (nearestCars.getTransports().get(i).getId().equalsIgnoreCase(id)) {
                                        found = true;
                                    }
                                }
                                if (!found) {
                                    removeCar(id);
                                }
                            }
                            it = nearestListenerHashMap.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry item = (Map.Entry) it.next();
                                String id = item.getKey().toString();
                                Boolean found = false;
                                for (int i = 0; i < nearestCars.getTransports().size(); i++) {
                                    if (nearestCars.getTransports().get(i).getId().equalsIgnoreCase(id)) {
                                        found = true;
                                    }
                                }
                                if (!found) {
                                    removeCar(id);
                                }
                            }

                            for (int i = 0; i < nearestCars.getTransports().size(); i++) {
                                nearestListenerHashMap.put(nearestCars.getTransports().get(i).getId(), nearestCars.getTransports().get(i).getLat() + "," + nearestCars.getTransports().get(i).getLng());
                                previousNearestListenerHashMap.put(nearestCars.getTransports().get(i).getId(), nearestCars.getTransports().get(i).getLat() + "," + nearestCars.getTransports().get(i).getLng());
                            }
                            showNearestCars(nearestCars.getTransports());
                        } else {
                            showNearestCars(new ArrayList<>());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void notifyError(VolleyError error) {
                    System.out.println("eeror");
                }
            }, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url") + PathUrl.transport_around, params);

            volly_ = "0";
            Volley_go();
            volly_ = "5";
            Volley_go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ChildEventListener nearestListener;
    ConcurrentHashMap<String, String> nearestListenerHashMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, String> previousNearestListenerHashMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, String> nearestStartLocationListenerHashMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, String> previousValues = new ConcurrentHashMap<>();

    void showNearestCars(ArrayList<NearestCar> transports) {
        try {
            nearestListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.exists()) {
                        FirebaseDoctorInfo values = dataSnapshot.getValue(FirebaseDoctorInfo.class);
                        if (values == null || values.getCoords() == null) return;
                        String coordinates = values.getCoords();
                        String id = dataSnapshot.getRef().getParent().getKey();
                        if (!nearestListenerHashMap.containsKey(id)) {
                            removeCar(id);
                            return;
                        }
                        if (nearestListenerHashMap.containsKey(id) && nearestListenerHashMap.get(id).equalsIgnoreCase(coordinates)) {
                            return;
                        }
                        if (previousValues.containsKey(id) && previousValues.get(id).contains(coordinates)) {
                            return;
                        }
                        String newVal = previousValues.get(id) + "-" + coordinates;
                        try {
                            if (newVal.length() > 600) {
                                final int mid = newVal.length() / 2;
                                String[] parts = {newVal.substring(0, mid), newVal.substring(mid)};
                                newVal = parts[1];
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        previousValues.put(id, newVal);
                        System.out.println("shtayyatW=> onChildAdded (" + id + ")");

                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.exists()) {
                        FirebaseDoctorInfo values = dataSnapshot.getValue(FirebaseDoctorInfo.class);
                        if (values == null || values.getCoords() == null) return;
                        String coordinates = values.getCoords();
                        String id = dataSnapshot.getRef().getParent().getKey();
                        if (!nearestListenerHashMap.containsKey(id)) {
                            removeCar(id);
                            return;
                        }
                        if (nearestListenerHashMap.containsKey(id) && nearestListenerHashMap.get(id).equalsIgnoreCase(coordinates)) {
                            return;
                        }
                        if (previousValues.containsKey(id) && previousValues.get(id).contains(coordinates)) {
                            return;
                        }
                        String newVal = previousValues.get(id) + "-" + coordinates;
                        try {
                            if (newVal.length() > 600) {
                                final int mid = newVal.length() / 2;
                                String[] parts = {newVal.substring(0, mid), newVal.substring(mid)};
                                newVal = parts[1];
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        previousValues.put(id, newVal);
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
            };

            for (NearestCar each : transports) {
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("drivers")
                        .child(each.getId())
                        .addChildEventListener(nearestListener);
            }


            if (transports.size() > 0) {
                Iterator it = previousNearestListenerHashMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry item = (Map.Entry) it.next();
                    String id = item.getKey().toString();
                    if (!nearestListenerHashMap.containsKey(id)) {
                        removeCar(id);
                    }
                }
            } else {
                Iterator it = previousNearestListenerHashMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry item = (Map.Entry) it.next();
                    String id = item.getKey().toString();
                    removeCar(id);
                }
                it = nearestListenerHashMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry item = (Map.Entry) it.next();
                    String id = item.getKey().toString();
                    removeCar(id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeCar(String id) {
        if (!nearestMarkerHashMap.containsKey(id)) {
            if (nearestListenerHashMap.containsKey(id))
                nearestListenerHashMap.remove(id);
            if (previousNearestListenerHashMap.containsKey(id))
                previousNearestListenerHashMap.remove(id);
            if (previousValues.containsKey(id))
                previousValues.remove(id);
            return;
        }

        try {
            Animator animator = ObjectAnimator.ofFloat(nearestMarkerHashMap.get(id), "alpha", 1f, 0f);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    try {
                        System.out.println("shtayyatWcarMarker=>Animation onAnimationEnd");
                        if (nearestMarkerHashMap.containsKey(id)) {
                            nearestMarkerHashMap.get(id).remove();
                            nearestMarkerHashMap.remove(id);
                            nearestListenerHashMap.remove(id);
                            previousNearestListenerHashMap.remove(id);
                            previousValues.remove(id);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onAnimationStart(Animator animator) {
                    System.out.println("shtayyatWcarMarker=>Animation onAnimationStart");
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    System.out.println("shtayyatWcarMarker=>Animation onAnimationCancel");
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                    System.out.println("shtayyatWcarMarker=>Animation onAnimationRepeat");
                }
            });
            System.out.println("shtayyatWcarMarker=>Animation setDuration");
            animator.setDuration(500).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ConcurrentHashMap<String, Marker> nearestMarkerHashMap = new ConcurrentHashMap<>();
    long LastTimeCar = 0l;

    private void addNearestCars() {
        if (selected_type == null) return;
        if (LastTimeCar != 0l) {
            long finish = System.nanoTime();
            long timeElapsed = (finish - LastTimeCar) / 1000000;
            if (timeElapsed < 3000) {
                return;
            }
            LastTimeCar = finish;
        } else {
            LastTimeCar = System.nanoTime();
        }
        try {
            for (Map.Entry each : nearestListenerHashMap.entrySet()) {
                String id = each.getKey().toString();
                if (nearestMarkerHashMap.containsKey(id) && nearestStartLocationListenerHashMap.containsKey(id)) {

                    String[] start = nearestStartLocationListenerHashMap.get(id).split(",");
                    String[] end = nearestListenerHashMap.get(id).split(",");
                    LatLng startPosition = new LatLng(Double.parseDouble(start[0]), Double.parseDouble(start[1]));
                    LatLng endPosition = new LatLng(Double.parseDouble(end[0]), Double.parseDouble(end[1]));
                    double distance = meterDistanceBetweenPoints(startPosition.latitude, startPosition.longitude, endPosition.latitude, endPosition.longitude);
                    System.out.println("shttt distance " + distance);
                    if (distance < 5) {
                        return;
                    }
                    //Log.d(TAG, startPosition.latitude + "--" + endPosition.latitude + "--Check --" + startPosition.longitude + "--" + endPosition.longitude);
                    System.out.println("shttt startBikeAnimation ID: " + id + " - " + ((endPosition.longitude + endPosition.latitude) != (startPosition.longitude + startPosition.latitude)));
                    //if ((endPosition.longitude + endPosition.latitude) != (startPosition.longitude + startPosition.latitude)) {
                    Log.e(TAG, "NOT SAME");
                    System.out.println("shttt startBikeAnimation");
                    startBikeAnimation(id, startPosition, endPosition);
                    //} else {
                    // System.out.println("shttt SAMME");
                    //}
                } else {
                    String[] end = nearestListenerHashMap.get(id).split(",");
                    LatLng startPosition = new LatLng(Double.parseDouble(end[0]), Double.parseDouble(end[1]));
                    Marker carMarker = mMap.addMarker(new MarkerOptions().position(startPosition).
                            flat(true).icon(BitmapDescriptorFactory.fromResource(selected_type.getTaxi().equalsIgnoreCase("1") ? R.drawable.car : R.drawable.truck)));
                    carMarker.setAnchor(0.5f, 0.5f);
                    ObjectAnimator.ofFloat(carMarker, "alpha", 0f, 1f).setDuration(500).start();


/*
                    mMap.moveCamera(CameraUpdateFactory
                            .newCameraPosition
                                    (new CameraPosition.Builder()
                                            .target(startPosition)
                                            .zoom(15.5f)
                                            .build()));
*/
                    System.out.println("shtayyatWcarMarker => add carMarker" + id);
                    nearestMarkerHashMap.put(id, carMarker);
                    nearestStartLocationListenerHashMap.put(id, nearestListenerHashMap.get(id));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final long ANIMATION_TIME_PER_ROUTE = 3000;

    private void startBikeAnimation(String id, final LatLng start, final LatLng end) {

        System.out.println("shttt startBikeAnimation called");
        Marker carMarker = nearestMarkerHashMap.get(id);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(ANIMATION_TIME_PER_ROUTE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                //LogMe.i(TAG, "Car Animation Started...");
                float v = valueAnimator.getAnimatedFraction();
                double lng = v * end.longitude + (1 - v)
                        * start.longitude;
                double lat = v * end.latitude + (1 - v)
                        * start.latitude;

                LatLng newPos = new LatLng(lat, lng);
                carMarker.setPosition(newPos);
                carMarker.setAnchor(0.5f, 0.5f);
                carMarker.setRotation(getBearing(end, start));

                // todo : Shihab > i can delay here
/*
                mMap.moveCamera(CameraUpdateFactory
                        .newCameraPosition
                                (new CameraPosition.Builder()
                                        .target(newPos)
                                        .zoom(15.5f)
                                        .build()));
*/

                nearestStartLocationListenerHashMap.put(id, carMarker.getPosition().latitude + "," + carMarker.getPosition().longitude);

            }

        });
        valueAnimator.start();
    }

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

    private double meterDistanceBetweenPoints(double lat_a, double lng_a, double lat_b, double lng_b) {
        double pk = (double) (180.f / Math.PI);
        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;
        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = (double) Math.acos(t1 + t2 + t3);
        return 6366000 * tt;
    }

    Boolean isMarkerRotating = false;

    private void rotateMarker(final Marker marker, final float toRotation) {
        if (!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }

    private double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }


    public void getCheckOrderResult(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =(new MySharedPreference(getApplicationContext())
                .getStringShared("base_url")+PathUrl.check_order+new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        check_order = response;
                        Log.d(" Request  xxx" ,check_order);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Request ",error.getMessage());

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}


