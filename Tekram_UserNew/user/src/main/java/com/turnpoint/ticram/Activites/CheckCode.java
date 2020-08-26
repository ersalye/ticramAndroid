package com.turnpoint.ticram.Activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.PathUrl;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.Volley.IResult;
import com.turnpoint.ticram.Volley.VolleyService;
import com.turnpoint.ticram.modules.User_Information;
import com.turnpoint.ticram.modules.check_code_result;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CheckCode extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    TextView txtTittle;
    EditText txtVerificationCode1;
    EditText txtVerificationCode2;
    EditText txtVerificationCode3;
    EditText txtVerificationCode4;
    EditText txtVerificationCode5;
    Button btn_verify;

    String verificationCode = "";
    String verify_or_resend;
    int [] value = {2,4,10};
    int no;
    private int whoHasFocus;
    String check_new_user;

    IResult iresult;
    VolleyService voly_ser;
    public  ProgressDialog loading;
    LinearLayout lay_ver_code;
    Boolean setClick_textview=false;

    protected static final String TAG = "LocationOnOff";


    public static final int REQUEST_LOCATION=001;
    GoogleApiClient googleApiClient;
    LocationManager locationManager;
    LocationRequest locationRequest;
    LocationSettingsRequest.Builder locationSettingsRequest;
    PendingResult<LocationSettingsResult> pendingResult;
    String method;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Al-Jazeera-Arabic-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_code);
        check_new_user=getIntent().getExtras().getString("new");
        callBackVolly();
        verify_or_resend="verify";
        lay_ver_code= findViewById(R.id.layout_ver_code);
        txtTittle=findViewById(R.id.txtTittle);
        btn_verify= findViewById(R.id.button10);
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               send_code_stuff();
            }
        });
        txtVerificationCode1=findViewById(R.id.txtVerificationCode1);
        txtVerificationCode2=findViewById(R.id.txtVerificationCode2);
        txtVerificationCode3=findViewById(R.id.txtVerificationCode3);
        txtVerificationCode4=findViewById(R.id.txtVerificationCode4);
        txtVerificationCode5=findViewById(R.id.txtVerificationCode5);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        txtVerificationCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!txtVerificationCode1.getText().toString().equals("")){
                    txtVerificationCode2.requestFocus();
                }

            }
        });


        txtVerificationCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!txtVerificationCode2.getText().toString().equals("")){
                    txtVerificationCode3.requestFocus();
                }

            }
        });


        txtVerificationCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {

                if(!txtVerificationCode3.getText().toString().equals("")){
                    txtVerificationCode4.requestFocus();
                }

            }
        });


        txtVerificationCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!txtVerificationCode4.getText().toString().equals("")){
                    txtVerificationCode5.requestFocus();
                }

            }
        });


        txtVerificationCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!txtVerificationCode5.getText().toString().equals("")){
                    verificationCode = txtVerificationCode1.getText().toString() + txtVerificationCode2.getText().toString() + txtVerificationCode3.getText().toString() +
                            txtVerificationCode4.getText().toString() + txtVerificationCode5.getText().toString();

                        if (checkFields()) {
                        method = "send_code";
                        Volley_go();
                    } else if (!checkFields()) {
                        Toast.makeText(getApplicationContext(), R.string.fields_are_missing,
                                Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

      //  txtTittle.setText(getApplicationContext().getString(R.string.check_message_pin));
        no = 0;
        counterVerificationCode();
    }




    public void send_code_stuff(){
        if(verify_or_resend.equals("verify")) {
            //btn_verify.setBackgroundResource(R.drawable.round_grey);
            btn_verify.setEnabled(false);
            verificationCode = txtVerificationCode1.getText().toString() + txtVerificationCode2.getText().toString() + txtVerificationCode3.getText().toString() +
                    txtVerificationCode4.getText().toString() + txtVerificationCode5.getText().toString();

            if (checkFields()) {
                method = "send_code";
                Volley_go();
            } else if (!checkFields()) {
                Toast.makeText(getApplicationContext(), R.string.fields_are_missing,
                        Toast.LENGTH_SHORT).show();

            }
        }

        if(verify_or_resend.equals("resend")) {
            method="resend_code";
            Volley_go();
            counterVerificationCode();
            verify_or_resend="verify";
            btn_verify.setEnabled(true);
            btn_verify.setText(getApplicationContext().getString(R.string.verify));
            //btn_verify.setBackgroundResource(R.drawable.round_corner_black);
        }
    }

    private boolean checkFields() {
        boolean result = true;
        if (txtVerificationCode1.getText().toString().equals("") || txtVerificationCode2.getText().toString().equals("") ||
                txtVerificationCode3.getText().toString().equals("") || txtVerificationCode4.getText().toString().equals("")
                || txtVerificationCode5.getText().toString().equals("")) {
            result = false;
        }
        else if (!txtVerificationCode1.getText().toString().equals("") && !txtVerificationCode2.getText().toString().equals("") &&
                !txtVerificationCode3.getText().toString().equals("") && !txtVerificationCode4.getText().toString().equals("")
                && !txtVerificationCode5.getText().toString().equals("")) {
            result = true;
        }

        return result;
    }




    private void counterVerificationCode() {
       final String vvv = getApplicationContext().getString(R.string.resend_code);
        int time;
        new CountDownTimer(240*1000, 1000) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {
                btn_verify.setEnabled(false);
                btn_verify.setBackground(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.rounded_grey));
                btn_verify.setText(" "+vvv + " "+String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
              /*  lay_ver_code.setVisibility(View.INVISIBLE);
                txtTittle.setBackgroundResource(R.color.colorAccent);
                txtTittle.setText(getApplicationContext().getString(R.string.resend_code));
                txtTittle.setTextColor(getResources().getColor(R.color.colorPrimary));
                setClick_textview=true;*/
                verify_or_resend="resend";
                btn_verify.setEnabled(true);
                btn_verify.setText(getApplicationContext().getString(R.string.resend_code));
                btn_verify.setBackground(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.rounded_corner_green));
            }
        }.start();

    }


    public void resend_code(View v){
       resendCode();
    }


    public void resendCode(){
        if(setClick_textview){
           /* lay_ver_code.setVisibility(View.VISIBLE);
            txtTittle.setBackgroundResource(R.color.colorPrimary);
            txtTittle.setTextColor(getResources().getColor(R.color.colorAccent));
            setClick_textview=false;
            txtTittle.setText(getApplicationContext().getString(R.string.check_message_pin));*/
            counterVerificationCode();
            method="resend_code";
            Volley_go();
            txtVerificationCode1.setText("");
            txtVerificationCode2.setText("");
            txtVerificationCode3.setText("");
            txtVerificationCode4.setText("");
            txtVerificationCode5.setText("");

        }
    }


   /* public String setSubtitle(String subtitle) {
        return subtitle.replace("@@@", System.getProperty("line.separator"));
    }*/



    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.turnpoint.ticram.checkcode");
        BroadcastReceiver_update receiver = new BroadcastReceiver_update();
        registerReceiver(receiver, intentFilter);

    }


    public class BroadcastReceiver_update extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Bundle extras = intent.getExtras();
                String code = extras.getString("code");
                if (!((Activity) context).isFinishing()) {
                    txtVerificationCode1.setText(Character.toString(code.charAt(0)));
                    txtVerificationCode2.setText(Character.toString(code.charAt(1)));
                    txtVerificationCode3.setText(Character.toString(code.charAt(2)));
                    txtVerificationCode4.setText(Character.toString(code.charAt(3)));
                    txtVerificationCode5.setText(Character.toString(code.charAt(4)));
                    verificationCode = code;
                    method = "send_code";
                    Volley_go();
                }
            } catch (Exception ex){}
        }
    }



    private void Volley_go(){
        if(method.equals("send_code")) {
            try {
                loading = ProgressDialog.show(CheckCode.this, "",
                        getResources().getString(R.string.please_wait), false, false);
                Map<String, String> params = new Hashtable<String, String>();
                params.put("local", new GetCurrentLanguagePhone().getLang());
                params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                params.put("code", verificationCode);
                voly_ser = new VolleyService(iresult, getApplicationContext());
                voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url")+PathUrl.CHECK_Code, params);
            } catch (Exception ex){}
        }

        if(method.equals("resend_code")) {
            try {
                loading = ProgressDialog.show(CheckCode.this, "",
                        getResources().getString(R.string.please_wait), false, false);
                Map<String, String> params = new Hashtable<String, String>();
                params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                params.put("local", new GetCurrentLanguagePhone().getLang());
                params.put("reg_id", FirebaseInstanceId.getInstance().getToken());
                params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                params.put("type", "A");

                voly_ser = new VolleyService(iresult, getApplicationContext());
                voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                        .getStringShared("base_url")+PathUrl.ResendCode, params);
            } catch (Exception ex){}
        }
    }




    void callBackVolly(){
        iresult= new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                if (method.equals("send_code")) {
                    try {
                        loading.dismiss();
                        Gson gson = new Gson();
                        check_code_result res = gson.fromJson(response, check_code_result.class);
                        User_Information u = res.getUser();
                        // Toast.makeText(CheckCode.this,response , Toast.LENGTH_LONG).show();
                        if (res.getHandle().equals("10")) {   //رمز التفعيل المدخل صحيح
                            new MySharedPreference(getApplicationContext()).setStringShared("login_status", "login");
                            new MySharedPreference(getApplicationContext()).setStringShared("access_token", u.getAccessToken());
                            new MySharedPreference(getApplicationContext()).setStringShared("user_id", String.valueOf(u.getId()));
                            new MySharedPreference(getApplicationContext()).setStringShared("user_name", u.getName());
                            new MySharedPreference(getApplicationContext()).setStringShared("photo", u.getPhoto());
                            new MySharedPreference(getApplicationContext()).setStringShared("rate", u.getRate());
                            new MySharedPreference(getApplicationContext()).setStringShared("balance", u.getBalance());
                            String num = u.getMob();
                            String sub_num = num.substring(3);
                            //Toast.makeText(CheckCode.this,sub_num , Toast.LENGTH_LONG).show();
                            new MySharedPreference(getApplicationContext()).setStringShared("mobile_full", num);
                            new MySharedPreference(getApplicationContext()).setStringShared("mobile", sub_num);
                            new MySharedPreference(getApplicationContext()).setStringShared("email", u.getEmail());
                            new MySharedPreference(getApplicationContext()).setStringShared("gender", u.getGender());
                            new MySharedPreference(getApplicationContext()).setIntShared("time_to_cancel_in_sec",
                                    u.getTime_to_cancel_in_sec());

                            Toast.makeText(CheckCode.this, res.getMsg(), Toast.LENGTH_LONG).show();
                            if (check_new_user.equals("yes")) {  // new user
                                Intent i = new Intent(getApplicationContext(), AddNameAndPicture.class);
                                startActivity(i);
                                finish();
                            } else if (check_new_user.equals("no")) {  //open map activity  // old user
                                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                    //Toast.makeText(getApplicationContext(), "Gps is Enabled", Toast.LENGTH_SHORT).show();
                                    new MySharedPreference(getApplicationContext()).setStringShared("login_status", "login");
                                    new MySharedPreference(getApplicationContext()).setStringShared("access_token", u.getAccessToken());
                                    new MySharedPreference(getApplicationContext()).setStringShared("user_id", String.valueOf(u.getId()));
                                    new MySharedPreference(getApplicationContext()).setStringShared("user_name", u.getName());
                                    new MySharedPreference(getApplicationContext()).setStringShared("photo", u.getPhoto());
                                    new MySharedPreference(getApplicationContext()).setStringShared("rate", u.getRate());
                                    new MySharedPreference(getApplicationContext()).setStringShared("balance", u.getBalance());
                                    String num_ = u.getMob();
                                    String sub_num_ = num_.substring(3);
                                    //Toast.makeText(CheckCode.this,sub_num_ , Toast.LENGTH_LONG).show();
                                    new MySharedPreference(getApplicationContext()).setStringShared("mobile_full", num_);
                                    new MySharedPreference(getApplicationContext()).setStringShared("mobile", sub_num_);
                                    new MySharedPreference(getApplicationContext()).setStringShared("email", u.getEmail());
                                    new MySharedPreference(getApplicationContext()).setStringShared("gender", u.getGender());
                                    Intent i = new Intent(getApplicationContext(), MapActivity.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    mEnableGps();
                                }
                            }

                        } else if (res.getHandle().equals("02")) {     // لم يتم ايجاد رمز التفعيل",
                            Toast.makeText(CheckCode.this, res.getMsg(), Toast.LENGTH_LONG).show();

                            //resendCode();
                        }
                        SplashActivity.updateVersion = Double.parseDouble(res.getUser().getAndroid_version());
                    } catch (Exception ex){}
                }


                if (method.equals("resend_code")) {
                    try{
                    loading.dismiss();
                    Gson gson = new Gson();
                    check_code_result res = gson.fromJson(response, check_code_result.class);
                    User_Information u = res.getUser();
                    // Toast.makeText(CheckCode.this,response , Toast.LENGTH_LONG).show();
                    if (res.getHandle().equals("10")) {   //رمز التفعيل المدخل صحيح
                        Toast.makeText(CheckCode.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("02")) {     // لم يتم ايجاد رمز التفعيل",
                        Toast.makeText(CheckCode.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        //resendCode();
                    }
                } catch (Exception ex){}
            }

                }

            @Override
            public void notifyError(VolleyError error) {
                loading.dismiss();
                Toast.makeText(CheckCode.this,R.string.check_internet, Toast.LENGTH_LONG).show();

                //  Toast.makeText(CheckCode.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };
    }





  ///=========================================check gps ======================




    public void mEnableGps() {
        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
        mLocationSetting();
    }

    public void mLocationSetting() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1 * 1000);
        locationRequest.setFastestInterval(1 * 1000);

        locationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        mResult();

    }


    public void mResult() {
        pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest.build());
        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();


                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {

                            status.startResolutionForResult(CheckCode.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.


                        break;
                }
            }

        });
    }







    //callback method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        //Toast.makeText(getApplicationContext(), "Gps enabled", Toast.LENGTH_SHORT).show();
                        new MySharedPreference(getApplicationContext()).setStringShared("login_status", "login");
                        Intent i = new Intent(getApplicationContext(), MapActivity.class);
                                startActivity(i);
                                finish();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(getApplicationContext(), "ارجوك قم بتشغيل ال gps لاستكمال العمليه!", Toast.LENGTH_SHORT).show();
                       // Toast.makeText(getApplicationContext(), "Gps Canceled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

