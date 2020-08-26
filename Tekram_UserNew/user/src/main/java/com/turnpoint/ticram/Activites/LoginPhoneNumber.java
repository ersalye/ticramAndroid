package com.turnpoint.ticram.Activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.PathUrl;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.Volley.IResult;
import com.turnpoint.ticram.Volley.VolleyService;
import com.turnpoint.ticram.modules.User;
import com.turnpoint.ticram.modules.register_user;

import java.util.Hashtable;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginPhoneNumber extends AppCompatActivity {
    EditText ed_mobile;
    IResult iresult;
    VolleyService voly_ser;
    public  ProgressDialog loading;
    String firebase_Token;
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
        setContentView(R.layout.activity_login_phone_number);
        callBackVolly();
        ed_mobile=findViewById(R.id.txtMobile);
       // register_Service= ApiUtils.getregisterService();

        firebase_Token=new MySharedPreference(this).getStringShared("FirebaseMessagingToken");
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token

                        firebase_Token = task.getResult().getToken();
                        new MySharedPreference(getApplicationContext()).setStringShared("FirebaseMessagingToken", firebase_Token);
                    }
                });


    }

    public void register(View v){
        String num_pattern="[0-9]{9}";
        String num_pattern2="[0-9]{10}";

        if(ed_mobile.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please Enter Phone Number!", Toast.LENGTH_SHORT).show();
        }
        else {
            if(ed_mobile.getText().toString().matches(num_pattern) || ed_mobile.getText().toString().matches(num_pattern2)  ){
               // Toast.makeText(getApplicationContext(),"Okaaay!", Toast.LENGTH_SHORT).show();
              // startActivity(new Intent(getApplicationContext(), CheckCode.class));
              // finish();
                //reg_user_("ara",ed_mobile.getText().toString(),"123" , "A");
                Volley_go();
            }
            else{
                Toast.makeText(getApplicationContext(),"Please Enter a Valid Phone Number!", Toast.LENGTH_SHORT).show();
            }
        }
    }






    private void Volley_go(){
        try {
            firebase_Token=new MySharedPreference(this).getStringShared("FirebaseMessagingToken");
            loading = ProgressDialog.show(LoginPhoneNumber.this, "",
                    getResources().getString(R.string.please_wait), false, false);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("local", new GetCurrentLanguagePhone().getLang());
            params.put("mob", ed_mobile.getText().toString());
            params.put("reg_id", firebase_Token);
            params.put("type", "A");
            params.put("version", PathUrl.VERSION_NUMBER);
            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.LOGIN, params);

            new MySharedPreference(getApplicationContext()).setStringShared("VERSION_NUMBER", PathUrl.VERSION_NUMBER);
        } catch (Exception ex){}
    }




    void callBackVolly(){

        iresult= new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                try {
                    Log.d("response", response);
                    loading.dismiss();
                    Gson gson = new Gson();
                    register_user res = gson.fromJson(response, register_user.class);
                    User u = res.user;
                    //Toast.makeText(LoginPhoneNumber.this,response , Toast.LENGTH_LONG).show();
                    new MySharedPreference(getApplicationContext()).setStringShared("user_id", u.id);
                    //Toast.makeText(LoginPhoneNumber.this,response , Toast.LENGTH_LONG).show();
                    if (u._new.equals("0")) {   //not new
                        Intent i = new Intent(getApplicationContext(), CheckCode.class);
                        i.putExtra("new", "no");
                        startActivity(i);
                        finish();
                    } else if (u._new.equals("1")) {     // new User
                        Intent i = new Intent(getApplicationContext(), CheckCode.class);
                        i.putExtra("new", "yes");
                        startActivity(i);
                        finish();
                    }
                } catch (Exception ex){}
            }

            @Override
            public void notifyError(VolleyError error) {
                loading.dismiss();
                Toast.makeText(LoginPhoneNumber.this,R.string.check_internet, Toast.LENGTH_LONG).show();

                //Toast.makeText(LoginPhoneNumber.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };
    }


   /* private void Volley_go(){
        final ProgressDialog loading = ProgressDialog.show(LoginPhoneNumber.this,"","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://test.ticram.com/api/api/send_code",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        loading.dismiss();
                          Gson gson= new Gson();
                         register_user res =gson.fromJson(s, register_user.class);
                        User u= res.user;

                        new MySharedPreference(getApplicationContext()).setStringShared("user_id" ,u.id);

                       Toast.makeText(LoginPhoneNumber.this, u._new , Toast.LENGTH_LONG).show();
                        if(u._new.equals("0")){   //not new
                            Intent i= new Intent(getApplicationContext(), CheckCode.class);
                            i.putExtra("new","no");
                            startActivity(i);
                            finish();

                         }
                        else if(u._new.equals("1")){     // new User
                            Intent i= new Intent(getApplicationContext(), CheckCode.class);
                            i.putExtra("new","yes");
                            startActivity(i);
                            finish();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        Toast.makeText(LoginPhoneNumber.this,"error android Volly"+
                                volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                String credentials = "root" + ":" + "Tr3ri@_(rfe";
                String auths = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auths);

                return headers;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<String, String>();
                params.put("local", "ara");
                params.put("mob", ed_mobile.getText().toString());
                params.put("reg_id","123" );
                params.put("type", "A");


                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
*/









   /*

    public void reg_user_(String local, String mob, String reg_id, String type) {
       // Call<register_user> res= register_Service.register_user("","","","");
        //tv.setText(res.b);

        register_Service.reg_user(local,mob,reg_id,type).enqueue(new Callback<register_user>() {
                    @Override
                    public void onResponse(Call<register_user> call, Response<register_user> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"is sucss :" +
                                    response.body().toString(),Toast.LENGTH_LONG).show();
                            //tv.setText(response.body().toString());
                           */
/* String res=new Gson().toJson(response);
                            Toast.makeText(getApplicationContext(),"is sucss :" +
                                    response.body().toString(),Toast.LENGTH_LONG).show();
*//*

                        }
                    }

                    @Override
                    public void onFailure(Call<register_user> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"faild :" +t.getMessage(),Toast.LENGTH_LONG).show();
                       // tv.setText(t.getMessage());
                    }


                });
    }

*/


}
