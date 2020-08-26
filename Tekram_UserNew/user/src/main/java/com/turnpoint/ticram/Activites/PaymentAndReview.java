package com.turnpoint.ticram.Activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.PathUrl;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.Volley.IResult;
import com.turnpoint.ticram.Volley.VolleyService;
import com.turnpoint.ticram.modules.OrderWaiting;
import com.turnpoint.ticram.modules.ResponseWaiting;
import com.turnpoint.ticram.modules.user_info_splash;
import com.turnpoint.ticram.modules.user_info_splash2;
import com.turnpoint.ticram.modules.usual_result;

import java.util.Hashtable;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PaymentAndReview extends AppCompatActivity {

    public ProgressDialog loading;
    IResult iresult;
    VolleyService voly_ser;
    String order_id;
    TextView tv_base_tax, tv_pay_amount , tv_discount_ , tv_username , tv_prevous_price , tv_FinalFee;
    TextView tv_rateText,tv_carType;
    ImageView imgView_user;
    RatingBar ratingBar ,ratingBarSmall;
    String method;
    String driver_id;
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
        setContentView(R.layout.activity_payment_and_review);
        callBackVolly();
        new MySharedPreference(getApplicationContext()).setIntShared("time_cancel_trip_saved",0);
        new MySharedPreference(getApplicationContext()).setIntShared("timer_wousol_captain",0);
        order_id=new MySharedPreference(getApplicationContext()).getStringShared("order_id");
        new MySharedPreference(getApplicationContext()).setStringShared("from_w_status","normal");

        new MySharedPreference(getApplicationContext()).setStringShared("destination_selected", "no");
        new MySharedPreference(getApplicationContext()).setStringShared("startloc_selected", "no");

        tv_base_tax=findViewById(R.id.tv_base_price);
        tv_discount_=findViewById(R.id.tv_discount);
        tv_pay_amount=findViewById(R.id.tv_price_trip);
        tv_prevous_price=findViewById(R.id.tv_price_privous);
        tv_FinalFee=findViewById(R.id.tv_final_fee);
        imgView_user=findViewById(R.id.img_user);
        tv_username=findViewById(R.id.tv_username);
        tv_rateText=findViewById(R.id.tv_rate_text);
        tv_carType=findViewById(R.id.tv_car_type);

        ratingBar=findViewById(R.id.rb_user);
        ratingBarSmall=findViewById(R.id.ratingBar_small);
        //Toast.makeText(getApplicationContext(),order_id, Toast.LENGTH_SHORT).show();
        method="get_trip_info";
        Volley_go();
    }

    public void rate_trasnport(View v){
        float raat=ratingBar.getRating();
        if(raat>0) {
            method = "rate_transport";
            Volley_go();
        }

       else  if(raat==0) {
            method = "update_user_info";
            Volley_go();
           /* startActivity(new Intent(getApplicationContext(), MapActivity.class));
            finish();*/
        }
    }



    private void Volley_go(){
        if(method.equals("get_trip_info")) {
            try {
                loading = ProgressDialog.show(PaymentAndReview.this, "",
                        getResources().getString(R.string.please_wait), false, false);
                Map<String, String> params = new Hashtable<String, String>();
                params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                params.put("local", new GetCurrentLanguagePhone().getLang());
                params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                params.put("order_id", order_id);
                voly_ser = new VolleyService(iresult, getApplicationContext());
                voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url")+PathUrl.View_Transport_Order, params);
            } catch (Exception ex){}
        }

        if(method.equals("rate_transport")) {
            try {
                loading = ProgressDialog.show(PaymentAndReview.this, "",
                        getResources().getString(R.string.please_wait), false, false);
                Map<String, String> params = new Hashtable<String, String>();
                params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                params.put("local", new GetCurrentLanguagePhone().getLang());
                params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                params.put("driver_id", driver_id);
                params.put("val", String.valueOf(ratingBar.getRating()));
                params.put("order_id", order_id);
                voly_ser = new VolleyService(iresult, getApplicationContext());
                voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url")+PathUrl.RateCaptain, params);
            } catch (Exception ex){}
        }

        if(method.equals("update_user_info")) {
            try {
                loading = ProgressDialog.show(PaymentAndReview.this, "",
                        getResources().getString(R.string.please_wait), false, false);
                Map<String, String> params = new Hashtable<String, String>();
                params.put("access_token", new MySharedPreference(PaymentAndReview.this).getStringShared("access_token"));
                params.put("local", new GetCurrentLanguagePhone().getLang());
                params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                voly_ser = new VolleyService(iresult, PaymentAndReview.this);
                voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url")+PathUrl.USERInfo, params);
            } catch (Exception ex){}
        }
    }




    void callBackVolly(){
        iresult= new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                if(method.equals("get_trip_info")) {
                    try {
                        loading.dismiss();
                        //Toast.makeText(PaymentAndReview.this, response, Toast.LENGTH_LONG).show();
                        Gson gson = new Gson();
                        ResponseWaiting res = gson.fromJson(response, ResponseWaiting.class);
                        if (res.getHandle().equals("10")) {
                            //Toast.makeText(PaymentAndReview.this, response, Toast.LENGTH_LONG).show();
                            OrderWaiting myOrder = res.getOrder();
                            tv_base_tax.setText(myOrder.getBaseFair());
                            tv_pay_amount.setText(myOrder.getMainFee());
                            tv_discount_.setText(myOrder.getDiscount() + "%");
                            tv_prevous_price.setText(myOrder.getOldPayment());
                            tv_FinalFee.setText(myOrder.getFinalFee());
                            tv_username.setText(myOrder.getTransportName());
                            Glide.with(getApplicationContext())
                                    .load(myOrder.getTransportPhoto())
                                    .apply(new RequestOptions()
                                            .placeholder(R.drawable.placeholder)
                                            .centerCrop()
                                            .dontAnimate()
                                            .dontTransform())
                                    .into(imgView_user);
                            ratingBarSmall.setRating(Float.parseFloat(myOrder.getTransportRate()));
                            tv_rateText.setText(myOrder.getTransportRate());
                            tv_carType.setText(myOrder.getTransportCar());
                            driver_id = myOrder.getTransportId();
                        }
                    } catch (Exception ex){}
                }

                if(method.equals("rate_transport")) {
                    try {
                        loading.dismiss();
                        Gson gson = new Gson();
                        usual_result res = gson.fromJson(response, usual_result.class);
                        if (res.getHandle().equals("10")) {
                            Toast.makeText(PaymentAndReview.this, res.getMsg(), Toast.LENGTH_LONG).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    method = "update_user_info";
                                    Volley_go();
                                }
                            }, 2000);

                        } else if (res.getHandle().equals("02")) {
                            Toast.makeText(PaymentAndReview.this,
                                    res.getMsg(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(PaymentAndReview.this, res.getMsg(),
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ex){}
                }


                if(method.equals("update_user_info")) {
                    try {
                        loading.dismiss();
                        // Toast.makeText(PaymentAndReview.this, response, Toast.LENGTH_LONG).show();
                        Gson gson = new Gson();
                        user_info_splash res = gson.fromJson(response, user_info_splash.class);
                        if (res.getHandle().equals("02")) {  // account not found
                            Toast.makeText(PaymentAndReview.this, res.getMsg(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(PaymentAndReview.this, LoginPhoneNumber.class);
                            startActivity(intent);
                            finish();
                        } else if (res.getHandle().equals("10")) {
                            user_info_splash2 user_info1 = res.getUser();
                            //  Toast.makeText(PaymentAndReview.this, user_info1.getRate()+" -- " +user_info1.getBalance(), Toast.LENGTH_LONG).show();
                            new MySharedPreference(getApplicationContext()).setStringShared("rate", user_info1.getRate());
                            new MySharedPreference(getApplicationContext()).setStringShared("balance", user_info1.getBalance());
                            startActivity(new Intent(getApplicationContext(), MapActivity.class));
                            finish();
                        } else if (res.getHandle().equals("02")) {
                            Toast.makeText(PaymentAndReview.this,
                                    res.getMsg(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(PaymentAndReview.this, res.getMsg(),
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ex){}
                }


                }

            @Override
            public void notifyError(VolleyError error) {
                loading.dismiss();
                Toast.makeText(PaymentAndReview.this,R.string.check_internet, Toast.LENGTH_LONG).show();

                //  Toast.makeText(PaymentAndReview.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();

            }
        };
    }




}
