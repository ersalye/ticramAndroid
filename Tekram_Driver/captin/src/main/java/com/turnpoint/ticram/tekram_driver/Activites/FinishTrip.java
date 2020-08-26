package com.turnpoint.ticram.tekram_driver.Activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.turnpoint.ticram.tekram_driver.DBHelper2;
import com.turnpoint.ticram.tekram_driver.MySharedPreference;
import com.turnpoint.ticram.tekram_driver.PathUrl;
import com.turnpoint.ticram.tekram_driver.R;
import com.turnpoint.ticram.tekram_driver.Services.MyLocationServiceAfter;
import com.turnpoint.ticram.tekram_driver.Volley.IResult;
import com.turnpoint.ticram.tekram_driver.Volley.VolleyService;
import com.turnpoint.ticram.tekram_driver.modules.Order;
import com.turnpoint.ticram.tekram_driver.modules.OrderDetails;
import com.turnpoint.ticram.tekram_driver.modules.Payment_end_trip;
import com.turnpoint.ticram.tekram_driver.modules.end_trip;
import com.turnpoint.ticram.tekram_driver.modules.usual_result;

import java.util.Hashtable;
import java.util.Map;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FinishTrip extends AppCompatActivity {

    EditText ed_pay_amount;
    TextView tv_base_tax, tv_pay_amount , tv_discount_ , tv_final_fee, tv_username;
    ImageView imgView_user;
    RatingBar ratingBar;
    Button btn_NotPaying;

    IResult iresult;
    VolleyService voly_ser;

    public ProgressDialog loading;
        ProgressBar progressBar;
    String method;
    String order_id;
    String base_fee,discountt, trip_fee , final_fee;
    String user_id;

    String user_name;
    String user_photo ;
    String user_rate ;
    public static Boolean isVisible = false;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestInProgress = false;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Al-Jazeera-Arabic-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_finish_trip);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        callBackVolly();

        //stop_sendLocAfterTawklna();
        stopAlarmManager_After();
        new MySharedPreference(getApplicationContext()).setIntShared("time_cancel_trip_saved",0);

        ed_pay_amount=findViewById(R.id.editText_pay_amount);
        tv_base_tax=findViewById(R.id.tv_base_price);
        tv_discount_=findViewById(R.id.tv_discount);
        tv_pay_amount=findViewById(R.id.tv_price_trip);
        tv_username=findViewById(R.id.tv_username);
        imgView_user=findViewById(R.id.img_user);
        ratingBar=findViewById(R.id.rb_user);
        tv_final_fee=findViewById(R.id.tv_price2);
        btn_NotPaying=findViewById(R.id.button4);
        progressBar=findViewById(R.id.progressBar2);


        try {
            if (getIntent().getExtras().getString("from").equals("splash")) {
                order_id = new MySharedPreference(getApplicationContext())
                        .getStringShared("cur_order_id");

               /* trip_fee = getIntent().getExtras().getString("trip_fee");
                base_fee = getIntent().getExtras().getString("base_fee");
                discountt = getIntent().getExtras().getString("discountt");
                final_fee = getIntent().getExtras().getString("final_fee");*/
                method = "get_trip_finish_details";
                Volley_go();


            } else if (getIntent().getExtras().getString("from").equals("activity")) {
                order_id = getIntent().getExtras().getString("order_id");
                String json_end_trip = getIntent().getExtras().getString("respone_end_trip");
                Gson gson = new Gson();
                end_trip res = gson.fromJson(json_end_trip, end_trip.class);
                Payment_end_trip pay_info = res.getOrder();
                trip_fee = pay_info.getFee();
                base_fee = pay_info.getBaseFare();
                discountt = pay_info.getDiscount();
                final_fee = pay_info.getFinalFee();

                user_name = getIntent().getExtras().getString("user_name");
                user_photo = getIntent().getExtras().getString("user_photo");
                user_rate = getIntent().getExtras().getString("user_rate");
                user_id = getIntent().getExtras().getString("user_id");

                setValues();
            }



        }

        catch (Exception ex){}

        Paper.book().write("startDistanceCount", false);
        new MySharedPreference(getApplicationContext()).setBooleanShared("startDistanceCount", false);
    }



    @Override
    public void onBackPressed() {

    }


    public void setValues(){
        tv_pay_amount.setText(trip_fee);
        tv_base_tax.setText(base_fee);
        tv_discount_.setText(discountt);
        tv_final_fee.setText(String.valueOf(final_fee));
        tv_username.setText(user_name);
        Glide.with(getApplicationContext()).load(user_photo).into(imgView_user);
        ratingBar.setRating(Float.parseFloat(user_rate));
        ed_pay_amount.setText(String.valueOf(final_fee));
        if(final_fee.equals("0") || final_fee.equals("0.0") ){
            btn_NotPaying.setEnabled(false);
            btn_NotPaying.setBackgroundColor(getResources().getColor(R.color.gray));
        }

    }

    public void rate_captain(View v){
        method="rate_captain";
        Volley_go();
    }


    public void send_payment(View view){
        float limit_value=Float.parseFloat(tv_final_fee.getText().toString())+ 15;
        float fee= Float.parseFloat(ed_pay_amount.getText().toString());
        if(fee <= limit_value){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FinishTrip.this);
            alertDialogBuilder.setMessage("هل انت متأكد؟");
            alertDialogBuilder.setPositiveButton("نعم",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            method="done_payment";
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
        else
            Toast.makeText(getApplicationContext(),"القيمه المدخله غير صحيحه ", Toast.LENGTH_SHORT).show();

   /*    float f= Float.parseFloat(ed_pay_amount.getText().toString());
        if(ed_pay_amount.getText().toString().trim().length() >0  && f <=15.0 ) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FinishTrip.this);
            alertDialogBuilder.setMessage("هل انت متأكد؟");
            alertDialogBuilder.setPositiveButton("نعم",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            method="done_payment";
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

        else{
            Toast.makeText(getApplicationContext(),"قم بادخال قيمه بين 1.20 -15 ", Toast.LENGTH_SHORT).show();
        }*/
    }



    public void did_not_pay(View view){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FinishTrip.this);
            alertDialogBuilder.setMessage("هل انت متأكد؟");
            alertDialogBuilder.setPositiveButton("نعم",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            method = "did_not_pay";
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


    public void change_pay_method(View view){

    }








    private static Boolean requestInProgress = false;
    public void Volley_go() {
        if (requestInProgress) return;
        requestInProgress = true;
        progressBar.setVisibility(View.VISIBLE);
        new MySharedPreference(getApplicationContext()).setStringShared("tawklna","no");
        if(method.equals("done_payment")) {
            loading = ProgressDialog.show(FinishTrip.this, "","الرجاء الانتظار...", false, false);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("local", "ara");
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("order_id", order_id);
            params.put("payment", ed_pay_amount.getText().toString());
            params.put("paymentmethod",  "CASH");
            params.put("paid", "1");
            //params.put("txn_id", "");
           // params.put("card_id", "");

            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.Pay, params);
        }else if(method.equals("did_not_pay")) {
            loading = ProgressDialog.show(FinishTrip.this, "","الرجاء الانتظار...", false, false);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("local", "ara");
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("order_id", order_id);
            params.put("payment", ed_pay_amount.getText().toString());
            params.put("paymentmethod",  "CASH");
            params.put("paid", "-1");
            //params.put("txn_id", "");
            // params.put("card_id", "");

            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.Pay, params);
        } else if(method.equals("rate_captain")) {
           loading = ProgressDialog.show(FinishTrip.this, "","الرجاء الانتظار...", false, false);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", "ara");
            params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("user_id", user_id);
            params.put("val", String.valueOf(ratingBar.getRating()));
            params.put("order_id", order_id);
            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.RateUser, params);
        } else if (method.equals("get_trip_finish_details")) {
            //loading = ProgressDialog.show(SplashActivity.this, "","", false, true);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("local", "ara");
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("order_id", String.valueOf(order_id));
            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.ViewTransportOrder_Details, params);
        } else {
            requestInProgress = false;
        }
    }




    public void stopAlarmManager_After() {
        stopService(new Intent(this, MyLocationServiceAfter.class));
    }


    public void callBackVolly(){
        iresult= new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                requestInProgress = false;
                progressBar.setVisibility(View.INVISIBLE);

                if(method.equals("done_payment")) {
                    // Toast.makeText(FinishTrip.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    usual_result res = gson.fromJson(response, usual_result.class);
                    if (res.getHandle().equals("02")) {
                        Toast.makeText(FinishTrip.this, "Error : 02 : "+res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {
                        new MySharedPreference(getApplicationContext()).setFloatShared("total_dis_before",(float) 0.0);
                        new MySharedPreference(getApplicationContext()).setIntShared("total_time_normal_before",0);
                        new MySharedPreference(getApplicationContext()).setIntShared("total_time_slow_before",0);

                        Paper.book().write("totalDistance",Double.parseDouble("0"));
                        Paper.book().write("totalTimeNormal",Double.parseDouble("0"));
                        Paper.book().write("totalTimeSlow", Double.parseDouble("0"));


                        new MySharedPreference(getApplicationContext()).setIntShared("counter_tripTime_sec" , 0);
                        new MySharedPreference(getApplicationContext()).setIntShared("counter_tripTime_min" , 0);
                        new MySharedPreference(getApplicationContext()).setIntShared("counter_tripTime_hour" , 0);

                        DBHelper2 db= new DBHelper2(getApplicationContext());
                        db.deleteCoordsTable();
                        db.deleteTimesTable();
                        db.deleteSecTable();

                        if (loading != null ) loading.dismiss();
                        Toast.makeText(FinishTrip.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MapsMain.class));
                        finish();
                    }
                    else{
                        Toast.makeText(FinishTrip.this, "Error : "+res.getMsg(), Toast.LENGTH_LONG).show();
                    }
                }

                if(method.equals("did_not_pay")) {
                    // Toast.makeText(FinishTrip.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    usual_result res = gson.fromJson(response, usual_result.class);
                    if (res.getHandle().equals("02")) {
                        Toast.makeText(FinishTrip.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {
                        new MySharedPreference(getApplicationContext()).setFloatShared("total_dis_before",(float) 0.0);
                        new MySharedPreference(getApplicationContext()).setIntShared("total_time_normal_before",0);
                        new MySharedPreference(getApplicationContext()).setIntShared("total_time_slow_before",0);

                        Paper.book().write("totalDistance",Double.parseDouble("0"));
                        Paper.book().write("totalTimeNormal",Double.parseDouble("0"));
                        Paper.book().write("totalTimeSlow", Double.parseDouble("0"));

                        new MySharedPreference(getApplicationContext()).setIntShared("counter_tripTime_sec" , 0);
                        new MySharedPreference(getApplicationContext()).setIntShared("counter_tripTime_min" , 0);
                        new MySharedPreference(getApplicationContext()).setIntShared("counter_tripTime_hour" , 0);

                        /*new MySharedPreference(getApplicationContext()).setStringShared("coords_c","");
                        new MySharedPreference(getApplicationContext()).setStringShared("array_time", "");
                        new MySharedPreference(getApplicationContext()).setStringShared("array_sec","");*/

                        if (loading != null ) loading.dismiss();
                        DBHelper2 db= new DBHelper2(getApplicationContext());
                        db.deleteCoordsTable();
                        db.deleteTimesTable();
                        db.deleteSecTable();

                        Toast.makeText(FinishTrip.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MapsMain.class));
                        finish();
                    }
                    else{
                        if (loading != null ) loading.dismiss();
                        Toast.makeText(FinishTrip.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    }
                }




                if(method.equals("rate_captain")) {
                  //  loading.dismiss();
                   // Toast.makeText(FinishTrip.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    usual_result res = gson.fromJson(response, usual_result.class);
                    if (res.getHandle().equals("10")) {
                        Toast.makeText(FinishTrip.this,res.getMsg(), Toast.LENGTH_LONG).show();
                        /*startActivity(new Intent(getApplicationContext(), MapsMain.class));
                        finish();*/
                    }
                    if (res.getHandle().equals("02")) {
                        Toast.makeText(FinishTrip.this,
                                res.getMsg(), Toast.LENGTH_LONG).show();
                    }
                }

                if (method.equals("get_trip_finish_details")) {
                    if (loading != null ) loading.dismiss();
                    // Toast.makeText(SplashActivity.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    OrderDetails res = gson.fromJson(response, OrderDetails.class);
                    if (res.getHandle().equals("02")) {
                        Toast.makeText(FinishTrip.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {
                        Order selected_order = res.getOrder();
                        // Toast.makeText(SplashActivity.this,response, Toast.LENGTH_LONG).show();
                        trip_fee = selected_order.getFee();
                        base_fee = selected_order.getBaseFare();
                        discountt = selected_order.getDiscount();
                        final_fee = selected_order.getFinalFee();

                        user_name = selected_order.getUserName();
                        user_photo = selected_order.getUserPhoto();
                        user_rate = selected_order.getUserRate();
                        user_id = String.valueOf(selected_order.getuserId());
                        // Toast.makeText(SplashActivity.this,String.valueOf(cur_order_id), Toast.LENGTH_LONG).show();
                        //Toast.makeText(SplashActivity.this,user_id, Toast.LENGTH_LONG).show();
                        setValues();
                    }
                    else {
                        Toast.makeText(FinishTrip.this, res.getMsg(), Toast.LENGTH_LONG).show();

                    }
                }

                if (loading != null ) loading.dismiss();
            }

            @Override
            public void notifyError(VolleyError error) {
                requestInProgress = false;
                progressBar.setVisibility(View.INVISIBLE);
                if (loading != null ) loading.dismiss();
                    Toast.makeText(FinishTrip.this, " مشكلة بالاتصال بالانترنت!", Toast.LENGTH_LONG).show();

                //Toast.makeText(FinishTrip.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();

            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        isVisible = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
    }
}
