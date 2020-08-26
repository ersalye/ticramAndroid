package com.turnpoint.ticram.Activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.turnpoint.ticram.Adapters.Adapter_MyCoupons;
import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.PathUrl;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.Volley.IResult;
import com.turnpoint.ticram.Volley.VolleyService;
import com.turnpoint.ticram.modules.coupon;
import com.turnpoint.ticram.modules.current_coupoun;
import com.turnpoint.ticram.modules.usual_result;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TarweejCode extends AppCompatActivity {

    IResult iresult;
    VolleyService voly_ser;
    public  ProgressDialog loading;
    String method;
    EditText ed_code;
        private Adapter_MyCoupons mAdapter;
    RecyclerView mRecyclerView;



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
        setContentView(R.layout.activity_tarweej_code);
        callBackVolly();
        ed_code=findViewById(R.id.editText);
        mRecyclerView= findViewById(R.id.recycleview);
        mAdapter = new Adapter_MyCoupons(this, new ArrayList<current_coupoun>(0),
                new Adapter_MyCoupons.PostItemListener() {
            @Override
            public void onPostClick(int order_id ,int pos ) {
             /*    Intent i= new Intent(getApplicationContext(), MyOrderDetails.class);
                i.putExtra("order_id",order_id);
                i.putExtra("pos",pos);
                startActivity(i);
                // Toast.makeText(getApplicationContext(),String.valueOf(pos),Toast.LENGTH_SHORT).show();
*/

            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);

        method="get_coupons";
        Volley_go();
    }

    public void back(View view){
        onBackPressed();
    }

    public void use_code(View view){
        if(ed_code.getText().toString().equals("")){
          Toast.makeText(getApplicationContext(),getResources().getString(R.string.enter_required_fields)
                  ,Toast.LENGTH_SHORT).show();
        }
        else if(!ed_code.getText().toString().equals("")){
            method="use_code";
            Volley_go();
        }


    }








    private void Volley_go(){
        if(method.equals("use_code")) {
            loading = ProgressDialog.show(TarweejCode.this, "",
                    getResources().getString(R.string.please_wait), false, false);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", new GetCurrentLanguagePhone().getLang());
            params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("code", ed_code.getText().toString());
            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.InsertCoupon, params);
        }


        if(method.equals("get_coupons")) {
            loading = ProgressDialog.show(TarweejCode.this, "",
                    getResources().getString(R.string.please_wait), false, false);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", new GetCurrentLanguagePhone().getLang());
            params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.UserCoupons, params);
        }
    }




    void callBackVolly(){
        iresult= new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                if(method.equals("use_code")) {
                    loading.dismiss();
                    Gson gson = new Gson();
                    usual_result res = gson.fromJson(response, usual_result.class);
                    //Toast.makeText(TarweejCode.this,response , Toast.LENGTH_LONG).show();
                    if (res.getHandle().equals("10")) {
                        Toast.makeText(TarweejCode.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),TarweejCode.class));
                        finish();
                    } else if (res.getHandle().equals("02")) {
                        Toast.makeText(TarweejCode.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(TarweejCode.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    }
                }

                if(method.equals("get_coupons")) {
                    loading.dismiss();
                    //Toast.makeText(TarweejCode.this,response , Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    coupon res = gson.fromJson(response, coupon.class);
                    List<current_coupoun> list= res.getCoupons();
                    if (res.getHandle().equals("10")) {
                       // Toast.makeText(TarweejCode.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        if (list.size()>0){
                            mAdapter.updateAnswers(list);
                        }
                    } else if (res.getHandle().equals("02")) {
                        Toast.makeText(TarweejCode.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(TarweejCode.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    }
                }

                }

            @Override
            public void notifyError(VolleyError error) {
                loading.dismiss();
              //  Toast.makeText(TarweejCode.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(TarweejCode.this,R.string.check_internet, Toast.LENGTH_LONG).show();

            }
        };
    }






}
