package com.turnpoint.ticram.tekram_driver.Services;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.turnpoint.ticram.tekram_driver.Activites.SplashActivity;
import com.turnpoint.ticram.tekram_driver.Activites.ViewDetailsOrder;
import com.turnpoint.ticram.tekram_driver.DBHelper2;
import com.turnpoint.ticram.tekram_driver.MySharedPreference;
import com.turnpoint.ticram.tekram_driver.PathUrl;
import com.turnpoint.ticram.tekram_driver.R;
import com.turnpoint.ticram.tekram_driver.Volley.IResult;
import com.turnpoint.ticram.tekram_driver.Volley.VolleyService;
import com.turnpoint.ticram.tekram_driver.modules.usual_result;
import io.paperdb.Paper;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WidgetNewOrder extends Service {
    private WindowManager mWindowManager;
    private View mFloatingView;
    WindowManager.LayoutParams params;
    String order_id, time_, distance_, user_name;
    TextView tv_userName, tv_distance, tv_time, tv_timer, tv_rate, tv_locCurrent, tv_locDesti, textView_info;
    ImageView img_user;
    RatingBar rb;
    ProgressBar progressBar;
    Integer countDown = 20;

    public WidgetNewOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.new_order, null);

        // already asked in splashActivity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }


        //Specify the view position
        params.gravity = Gravity.NO_GRAVITY | Gravity.NO_GRAVITY;        //Initially view will be added to top-RIGHT corner
       /* params.x = 20;
        params.y = 100;*/

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        try {
            mWindowManager.addView(mFloatingView, params);
        } catch ( Exception e) {
            e.printStackTrace();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        mWindowManager.addView(mFloatingView, params);
                    } catch ( Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 1000);

        }


        tv_userName = (TextView) mFloatingView.findViewById(R.id.textView_name);
        tv_distance = (TextView) mFloatingView.findViewById(R.id.textView_distance);
        //tv_time = (TextView) mFloatingView.findViewById(R.id.textView_time);
        tv_timer = (TextView) mFloatingView.findViewById(R.id.textView_timer);
        textView_info = (TextView) mFloatingView.findViewById(R.id.textView_info);

        tv_rate = (TextView) mFloatingView.findViewById(R.id.textView_rate);
        tv_locCurrent = (TextView) mFloatingView.findViewById(R.id.textView_curLoc);
        tv_locDesti = (TextView) mFloatingView.findViewById(R.id.textView_desLoc);
        rb = (RatingBar) mFloatingView.findViewById(R.id.ratingBar2);
        img_user = mFloatingView.findViewById(R.id.imgUser);
        progressBar = mFloatingView.findViewById(R.id.progressBar2);
        Button acceptButtonCollapsed = (Button) mFloatingView.findViewById(R.id.button11);
        acceptButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the service and remove the from from the window
                if(progressBar.getVisibility() ==  View.VISIBLE) return;
                try {
                    new MySharedPreference(WidgetNewOrder.this).setFloatShared("total_dis_before", (float) 0.0);
                    new MySharedPreference(WidgetNewOrder.this).setIntShared("total_time_normal_before", 0);
                    new MySharedPreference(WidgetNewOrder.this).setIntShared("total_time_slow_before", 0);

                    new MySharedPreference(WidgetNewOrder.this).setFloatShared("total_dis_after", (float) 0.0);
                    new MySharedPreference(WidgetNewOrder.this).setIntShared("total_time_normal_after", 0);
                    new MySharedPreference(WidgetNewOrder.this).setIntShared("total_time_slow_after", 0);

                    new MySharedPreference(WidgetNewOrder.this).setIntShared("counter_tripTime_sec", 0);
                    new MySharedPreference(WidgetNewOrder.this).setIntShared("counter_tripTime_min", 0);
                    new MySharedPreference(WidgetNewOrder.this).setIntShared("counter_tripTime_hour", 0);

                    DBHelper2 db = new DBHelper2(WidgetNewOrder.this);
                    db.deleteCoordsTable();
                    db.deleteTimesTable();
                    db.deleteSecTable();

                    Paper.book().write("lastAccept", order_id);
                } catch (Exception ex) {
                }
                Map<String, String> params = new Hashtable<String, String>();
                params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                params.put("local", "ara");
                params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                params.put("order_id", String.valueOf(order_id));

                VolleyService voly_ser = new VolleyService(new IResult() {
                    @Override
                    public void notifySuccessPost(String response) {
                        progressBar.setVisibility(View.VISIBLE);
                        Gson gson = new Gson();
                        usual_result res = gson.fromJson(response, usual_result.class);
                        if (res.getHandle().equals("02")) {
                            Toast.makeText(getApplicationContext(), res.getMsg(), Toast.LENGTH_LONG).show();
                        } else if (res.getHandle().equals("10")) {
                            //Toast.makeText(ViewDetailsOrder.this, res.getMsg(), Toast.LENGTH_LONG).show();
                            new MySharedPreference(getApplicationContext()).setStringShared("cur_order_id", String.valueOf(order_id));
                            Intent i = new Intent(WidgetNewOrder.this, ViewDetailsOrder.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra("order_id", String.valueOf(order_id));
                            i.putExtra("from_act", "activity");
                            i.putExtra("initialBtnType", "wosoul");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                        else if (res.getHandle().equals("03")) { // لم يتم وجود الطلب --
                            Toast.makeText(getApplicationContext(), res.getMsg(), Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), res.getMsg(), Toast.LENGTH_LONG).show();
                        }
                        try {
                            if (!res.getHandle().equals("10")) {
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                JsonObject errorDetails = new JsonObject();
                                errorDetails.addProperty("request", params.toString());
                                errorDetails.addProperty("response", response);
                                errorDetails.addProperty("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                                errorDetails.addProperty("local", "ara");
                                errorDetails.addProperty("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                                errorDetails.addProperty("order_id", String.valueOf(order_id));
                                try {
                                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                    errorDetails.addProperty("app_version_number", pInfo.versionName);
                                    errorDetails.addProperty("app_version_code", pInfo.versionCode);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                DatabaseReference newVal = mDatabase.child("errors").child(new MySharedPreference(getApplicationContext()).
                                        getStringShared("user_id")).child("errors").push();
                                newVal.setValue(errorDetails.toString());
                            }
                        } catch ( Exception ex ) {
                            ex.printStackTrace();
                        }
                        stopSelf();
                    }

                    @Override
                    public void notifyError(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), " مشكلة بالاتصال بالانترنت!", Toast.LENGTH_LONG).show();
                        System.out.println(error.getMessage());
                        try {
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            JsonObject errorDetails = new JsonObject();
                            errorDetails.addProperty("request", params.toString());
                            errorDetails.addProperty("response", error.getMessage());
                            errorDetails.addProperty("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                            errorDetails.addProperty("local", "ara");
                            errorDetails.addProperty("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                            errorDetails.addProperty("order_id", String.valueOf(order_id));
                            try {
                                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                errorDetails.addProperty("app_version_number", pInfo.versionName);
                                errorDetails.addProperty("app_version_code", pInfo.versionCode);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            DatabaseReference newVal = mDatabase.child("errors").child(new MySharedPreference(getApplicationContext()).
                                    getStringShared("user_id")).child("errors").push();
                            newVal.setValue(errorDetails);
                        } catch ( Exception er) {
                            er.printStackTrace();
                        }
                    }
                }, getApplicationContext());
                progressBar.setVisibility(View.VISIBLE);
                voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url") + PathUrl.AcceptOrder, params);
            }
        });


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        try {
            try {
                order_id = intent.getStringExtra("order_id");
            } catch ( Exception e) {
                e.printStackTrace();
                try {
                    order_id = ""+ intent.getIntExtra("order_id", 0);
                } catch ( Exception ex) {
                    ex.printStackTrace();
                    try {
                        order_id = ""+ intent.getFloatExtra("order_id", 0);
                    } catch ( Exception exe) {
                        exe.printStackTrace();
                    }
                }

            }
            System.out.println("onStartCommand order_id" + order_id);
            time_ = intent.getStringExtra("time_to_user");
            distance_ = intent.getStringExtra("distance_to_user");
            // user_name = intent.getStringExtra("user_name");
            // Toast.makeText(this,"time : " +time_   +" dis : " + distance_ +" name : "+user_name,Toast.LENGTH_LONG).show();

            //tv_userName.setText(intent.getStringExtra("user_name"));
            tv_distance.setText(" يبعد عنك " + distance_ + "  -  " + time_);
            new MySharedPreference(getApplicationContext()).setIntShared("distance_user", tv_distance.getText().toString() );

            System.out.println("AWSDistance"+tv_distance.getText().toString());
            //tv_time.setText(intent.getStringExtra("time_to_user"));

            tv_rate.setText(intent.getStringExtra("user_rate"));
            tv_locCurrent.setText(intent.getStringExtra("location_text"));
            tv_locDesti.setText(intent.getStringExtra("destination_text"));
            rb.setRating(Float.parseFloat(intent.getStringExtra("user_rate")));
            /*Glide.with(getApplicationContext())
                    .load(intent.getStringExtra("user_photo"))
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.user_imagehdpi)
                            .centerCrop()
                            .dontAnimate()
                            .dontTransform())
                    .into(img_user);*/

            if (intent.getStringExtra("destination_text").equals(""))
                tv_locDesti.setText("غير محدد");

            if (!intent.getStringExtra("order_info").isEmpty() && !intent.getStringExtra("order_info").equalsIgnoreCase("0")) {
                textView_info.setText(intent.getStringExtra("order_info"));
                textView_info.setVisibility(View.VISIBLE);
            }

            countDown = Integer.parseInt(intent.getStringExtra("order_count_down"));
            new CountDownTimer(countDown * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    tv_timer.setText(String.format("%02d",
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    // Toast.makeText(getApplicationContext(), "mmmm", Toast.LENGTH_SHORT).show();
                }

                public void onFinish() {
                    Intent i = new Intent("tecram.action.refresh");
                    i.putExtra("order", "finish");
                    sendBroadcast(i);
                    stopSelf();
                }
            }.start();

        } catch (Exception ex) {
            System.out.println("onStartCommand order_id Exception" + order_id);
            ex.printStackTrace();
        }
        if (order_id == null || order_id.equalsIgnoreCase("0")) {
            stopSelf();
        }
        return START_STICKY;
    }


    /*public void timer_order(final int num){
        new CountDownTimer(num* 1000, 1000) {
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

    }*/


    @Override
    public void onDestroy() {
        super.onDestroy();
        //  Toast.makeText(getApplicationContext(),"onDestroyCancel", Toast.LENGTH_SHORT ).show();
        if (mFloatingView != null)
            mWindowManager.removeView(mFloatingView);
    }
}
