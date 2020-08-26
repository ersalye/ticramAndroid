package com.turnpoint.ticram.tekram_driver.Activites;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.turnpoint.ticram.tekram_driver.Adapters.Adapter_ViewMyOrders;
import com.turnpoint.ticram.tekram_driver.MySharedPreference;
import com.turnpoint.ticram.tekram_driver.PathUrl;
import com.turnpoint.ticram.tekram_driver.R;
import com.turnpoint.ticram.tekram_driver.Volley.IResult;
import com.turnpoint.ticram.tekram_driver.Volley.VolleyService;
import com.turnpoint.ticram.tekram_driver.modules.HistoryOrders;
import com.turnpoint.ticram.tekram_driver.modules.singleOrder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyOrders extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

public static final String TAG = MapsMain.class.getSimpleName();
private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
private GoogleApiClient mGoogleApiClient;
private LocationRequest mLocationRequest;
double currentLatitude=0.0, currentLongitude=0.0;

        IResult iresult;
        VolleyService voly_ser;
        public ProgressDialog loading;
        private Adapter_ViewMyOrders mAdapter;
        RecyclerView mRecyclerView;

        TextView tv_from, tv_to, tv_numOfRides, tv_mymoney,tv_earnedMoney;
        ImageView img_back, img_next;
        String currentDate, date_BeforeOneweek;
        String first_time;
        String next_back;
        String first_next;
        TextView tv_label_no_orders;
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
        setContentView(R.layout.activity_my_orders);
        callBackVolly();

         first_time="yes";
         next_back="back";
         mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();

        mLocationRequest = LocationRequest.create()
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setInterval(10 * 1000)        // 10 seconds, in milliseconds
        .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        mRecyclerView= findViewById(R.id.recycle_myorders);
       mAdapter = new Adapter_ViewMyOrders(this, new ArrayList<singleOrder>(0),
               new Adapter_ViewMyOrders.PostItemListener() {
            @Override
            public void onPostClick(int order_id ,int pos ) {
                Intent i= new Intent(getApplicationContext(), MyOrderDetails.class);
                i.putExtra("order_id",order_id);
                i.putExtra("pos",pos);
                startActivity(i);
               // Toast.makeText(getApplicationContext(),String.valueOf(pos),Toast.LENGTH_SHORT).show();


            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);


        tv_label_no_orders=findViewById(R.id.textView_no_orderLabel);
        tv_from=findViewById(R.id.tv_date_from);
        tv_to=findViewById(R.id.tv_date_to);

        tv_numOfRides=findViewById(R.id.textView_numOfRides);
        tv_earnedMoney=findViewById(R.id.textView_earned_money);
        tv_mymoney=findViewById(R.id.textView_mymoney);

        img_back=findViewById(R.id.imageView_back);
        img_next=findViewById(R.id.imageView_next);

        img_next.setVisibility(View.INVISIBLE);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //img_next.setVisibility(View.VISIBLE);
                next_back="back";
                currentDate=date_BeforeOneweek;
                updateDate();
              // Toast.makeText(getApplicationContext(), currentDate +"  "+ date_BeforeOneweek, Toast.LENGTH_SHORT).show();
            }
        });

        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date1 = null;
                Date date2 =null;
                Date date3 =null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd" , Locale.ENGLISH);
                String date_now = sdf.format(new Date());
                try {
                     date1 = sdf.parse(currentDate);
                     date2 = sdf.parse(date_BeforeOneweek);
                     date3 = sdf.parse(date_now);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date date4=getDate_minus_seven();
                if(date3.equals(date1) ||date3.equals(date2) || date3.equals(date4)){
                 // img_next.setVisibility(View.INVISIBLE);
                }
                else {
                    next_back = "next";
                    updateDateplus();
                }

            }
        });
    }



    public Date getDate_minus_seven(){

        Date datedate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd" ,Locale.ENGLISH);
        String date_now = sdf.format(new Date());
        String dt = date_now;  // Start date
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, -7);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        String date = sdf.format(c.getTime());
        try {
            datedate = sdf.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return datedate;
    }







    public void updateDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd" , Locale.ENGLISH);
        String dt = currentDate;  // Start date
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, -7);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        date_BeforeOneweek= sdf.format(c.getTime());

      //  Toast.makeText(getApplicationContext(), currentDate +"  "+ date_BeforeOneweek, Toast.LENGTH_SHORT).show();
        Volley_go();
    }



    public void updateDateplus(){

        date_BeforeOneweek=currentDate;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd" , Locale.ENGLISH);
        String dt = currentDate;  // Start date
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 7);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        currentDate = sdf.format(c.getTime());

        //Toast.makeText(getApplicationContext(), "from = "+currentDate + " - to = " + date_BeforeOneweek, Toast.LENGTH_LONG).show();
        Volley_go();
    }


    public void save_changes(View view){
    }

    public void back(View view){
        onBackPressed();
    }



    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        Volley_go();

    }




    public void Volley_go(){


        if(first_time.equals("yes")) {
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd" , Locale.ENGLISH);
           // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd" );
            currentDate = sdf.format(new Date());
            String dt = currentDate;  // Start date
            Calendar c = Calendar.getInstance();
            try {
                c.setTime(sdf.parse(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.add(Calendar.DATE, -7);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            date_BeforeOneweek = sdf.format(c.getTime());

            Log.d("from" ,date_BeforeOneweek +  "  -  " + currentDate );

            loading = ProgressDialog.show(MyOrders.this, "",
                    "الرجاء الانتظار...", false, true);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", "ara");
            params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("location", currentLatitude + "," + currentLongitude);
            params.put("from", date_BeforeOneweek);
            params.put("to", currentDate);
            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.ViewMyOrders, params);
            first_time = "no";
        }







        else if (first_time.equals("no")) {
            if (next_back.equals("next")) {
                loading = ProgressDialog.show(MyOrders.this, "",
                        "الرجاء الانتظار...", false, true);
                Map<String, String> params = new Hashtable<String, String>();
                params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                params.put("local", "ara");
                params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                params.put("location", currentLatitude + "," + currentLongitude);
                params.put("from", date_BeforeOneweek);
                params.put("to", currentDate);
                voly_ser = new VolleyService(iresult, getApplicationContext());
                voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url")+PathUrl.ViewMyOrders, params);
            }


            if (next_back.equals("back")) {
                loading = ProgressDialog.show(MyOrders.this, "",
                        "الرجاء الانتظار...", false, true);
                Map<String, String> params = new Hashtable<String, String>();
                params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                params.put("local", "ara");
                params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                params.put("location", currentLatitude + "," + currentLongitude);
                params.put("from", date_BeforeOneweek);
                params.put("to", currentDate);
                voly_ser = new VolleyService(iresult, getApplicationContext());
                voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url")+PathUrl.ViewMyOrders, params);
            }

        }
    }




    public void callBackVolly(){
        iresult= new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                    loading.dismiss();
                    //Log.d("ssssssss" , response);
                   // Toast.makeText(MyOrders.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    HistoryOrders res = gson.fromJson(response, HistoryOrders.class);
                    if (res.getHandle().equals("02")) {
                      // Toast.makeText(MyOrders.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {
                      //  Toast.makeText(MyOrders.this, res.getMsg(), Toast.LENGTH_LONG).show();
                         if(!res.getStatus().equals("") && res.getStatus()!= null){
                             if(res.getStatus().equals("E")){
                                 img_next.setVisibility(View.INVISIBLE);
                             }
                             else if(res.getStatus().equals("S")){
                                img_back.setVisibility(View.INVISIBLE);
                             }
                         }
                         else if(res.getStatus().equals("") || res.getStatus()== null){
                             img_next.setVisibility(View.VISIBLE);
                             img_back.setVisibility(View.VISIBLE);
                         }


                        List<singleOrder> list_orders=res.getOrders();
                            tv_earnedMoney.setText(res.getPayments().toString());
                            tv_mymoney.setText(res.getBalance().toString());
                            tv_numOfRides.setText(res.getTransports().toString());
                            mAdapter.updateAnswers(list_orders);
                            tv_from.setText(currentDate);
                            tv_to.setText(date_BeforeOneweek);


                            if(list_orders.size()==0){
                                tv_label_no_orders.setVisibility(View.VISIBLE);
                            }
                        else if(list_orders.size()>0){
                            tv_label_no_orders.setVisibility(View.GONE);
                        }




                    }

            }

            @Override
            public void notifyError(VolleyError error) {
                    loading.dismiss();
                Toast.makeText(MyOrders.this, " مشكلة بالاتصال بالانترنت!", Toast.LENGTH_LONG).show();

                //Toast.makeText(MyOrders.this,"Volley Error"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();


            }
        };
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
    public void onLocationChanged(Location location) {handleNewLocation(location);
    }

}
