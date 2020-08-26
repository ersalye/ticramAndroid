package com.turnpoint.ticram.Activites;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.PathUrl;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.Volley.IResult;
import com.turnpoint.ticram.Volley.VolleyService;
import com.turnpoint.ticram.modules.current_support;
import com.turnpoint.ticram.modules.support;


import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Support extends AppCompatActivity {
    IResult iresult;
    VolleyService voly_ser;
    public ProgressDialog loading;
    String cur_support;
    current_support res;
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
        setContentView(R.layout.activity_support);
        callBackVolly();

        LinearLayout lin_send_shakwa=findViewById(R.id.linearLay_send_shakwaa);
        LinearLayout lin_call=findViewById(R.id.linearLay_call);

        LinearLayout lin_policyAndRules=findViewById(R.id.linearLay_policy);
        LinearLayout lin_help=findViewById(R.id.linearLay_help);

        LinearLayout lin_ques=findViewById(R.id.linearLay_questions);
        LinearLayout lin_about=findViewById(R.id.linearLay_about_tecram);

        lin_send_shakwa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),MyTickets.class);
                //i.putExtra("from","not_order");
                startActivity(i);
            }
        });


        lin_ques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),FrequentlyQuestions.class);
                i.putExtra("to","questions");

                startActivity(i);
            }
        });
        lin_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),FrequentlyQuestions.class);
                i.putExtra("to","about_ticram");
                startActivity(i);
            }
        });
        lin_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Volley_go();

              /* Intent i=new Intent(getApplicationContext(),MakeCallSinch.class);
               if(cur_support.equals("") || cur_support==null){
                   Toast.makeText(Support.this, res.getMsg(), Toast.LENGTH_LONG).show();
                }
                else {
                    i.putExtra("call_whome", cur_support);
                    i.putExtra("reciever_name", "الدعم الفني");
                    startActivity(i);
                }*/
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(Support.this,
                                Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(Support.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    202);

                        } else if (ActivityCompat.checkSelfPermission(Support.this,
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

            }
        });
        lin_policyAndRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),TermsActivity.class));
            }
        });
        lin_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),HelpActivity.class));
            }
        });

       // Volley_go();

    }

    public void back(View view){
        onBackPressed();
    }



    private void Volley_go(){
        loading = ProgressDialog.show(Support.this, "",
                getResources().getString(R.string.please_wait), false, true);
        voly_ser = new VolleyService(iresult, getApplicationContext());
        voly_ser.getDataVolley(new MySharedPreference(getApplicationContext())
                .getStringShared("base_url")+PathUrl.GetCurrentSupport);
    }



    @Override
    protected void onResume(){
        super.onResume();
       // Volley_go();

    }



    void callBackVolly(){
        iresult= new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                loading.dismiss();
                 //Toast.makeText(Support.this, response, Toast.LENGTH_LONG).show();
                Gson gson = new Gson();
                 res = gson.fromJson(response, current_support.class);
                if (res.getHandle().equals("02")) {  // account not found
                    Toast.makeText(Support.this, res.getMsg(), Toast.LENGTH_LONG).show();
                } else if (res.getHandle().equals("10")) {   // account found
                   //  Toast.makeText(Support.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    support s=res.getSupport();
                    cur_support=s.getUsername();
                    //Toast.makeText(Support.this, cur_support, Toast.LENGTH_LONG).show();
                   // Toast.makeText(Support.this, res.getMsg(), Toast.LENGTH_LONG).show();
                   Intent i=new Intent(getApplicationContext(),MakeCallSinch.class);
                    if(cur_support.equals("") || cur_support==null){
                       // Toast.makeText(Support.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    }
                    else {
                        i.putExtra("call_whome", cur_support);
                        i.putExtra("reciever_name", "الدعم الفني");
                        startActivity(i);
                    }
                }

                else {
                   Toast.makeText(Support.this, res.getMsg(), Toast.LENGTH_LONG).show();
                }



            }

            @Override
            public void notifyError(VolleyError error) {
                loading.dismiss();
                Toast.makeText(Support.this,R.string.check_internet, Toast.LENGTH_LONG).show();

                // Toast.makeText(Support.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };
    }










    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 202: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //creatEventMap();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:0" + "788459676"));
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    startActivity(callIntent);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    ActivityCompat.requestPermissions(Support.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            202);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

}
