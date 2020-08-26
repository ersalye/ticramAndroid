package com.turnpoint.ticram.tekram_driver.Activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.turnpoint.ticram.tekram_driver.MySharedPreference;
import com.turnpoint.ticram.tekram_driver.PathUrl;
import com.turnpoint.ticram.tekram_driver.R;
import com.turnpoint.ticram.tekram_driver.Volley.IResult;
import com.turnpoint.ticram.tekram_driver.Volley.VolleyService;
import com.turnpoint.ticram.tekram_driver.modules.Report;
import com.turnpoint.ticram.tekram_driver.modules.getComplainsTypes;
import com.turnpoint.ticram.tekram_driver.modules.usual_result;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ComplaintSupport extends AppCompatActivity {

    IResult iresult;
    VolleyService voly_ser;

    public ProgressDialog loading;
    String method;
    List<String> fields;
    List<String> ids;

    String selected_field;
    String selected_id;
    EditText ed_message;
    String type_report;
    String user_id,order_id;
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
         setContentView(R.layout.activity_complaint_support);
        callBackVolly();

         String from_what_act=getIntent().getExtras().getString("from");
         if(from_what_act.equals("support")){
             type_report="support";
         }
        else if(from_what_act.equals("myorderDetails")){
             type_report="another";
             order_id=getIntent().getExtras().getString("order_id");
             user_id=getIntent().getExtras().getString("user_id");
            // Toast.makeText(getApplicationContext(),user_id, Toast.LENGTH_SHORT).show();

         }
        ed_message=findViewById(R.id.ed_msg);


        // select fields
        Spinner spinner_spacialty = (Spinner) findViewById(R.id.spinner);
        fields = new ArrayList<String>();
        ids = new ArrayList<String>();
        fields.add("--اختر نوع التبليغ--");
        ids.add("0");

        ArrayAdapter<String> dataAdapter_spaclites = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, fields);
        dataAdapter_spaclites.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_spacialty.setAdapter(dataAdapter_spaclites);

        spinner_spacialty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 selected_field = parent.getItemAtPosition(position).toString();
                 selected_id=ids.get(position).toString();
                 //Toast.makeText(ComplaintSupport.this, selected_id, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

            method="get_types";
            Volley_go();
    }

    public void back(View view){
        onBackPressed();
    }

    public void send_complain(View view){
        if(!selected_field.equals("--اختر نوع التبليغ--") && !ed_message.getText().toString().equals("")) {
            if(type_report.equals("support")) {
                method = "send_complain";
                Volley_go();
            }
            else if(type_report.equals("another")) {
                method = "report_user";
                Volley_go();
            }
        }
        else{
           Toast.makeText(ComplaintSupport.this, "قم بادخال المعلومات المطلوبة اولا!", Toast.LENGTH_LONG).show();

        }

    }



    private void Volley_go(){
        if(method.equals("get_types")) {
            loading = ProgressDialog.show(ComplaintSupport.this, "",
                    "الرجاء الانتظار...", false, false);
            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.getDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.ReportComplain);
        }

       else if(method.equals("send_complain")) {
            loading = ProgressDialog.show(ComplaintSupport.this, "",
                    "الرجاء الانتظار...", false, false);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", "ara");
            params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("report_id", selected_id);
            params.put("notes", ed_message.getText().toString() );
            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.SendReport, params);
        }

        else if(method.equals("report_user")) {
            loading = ProgressDialog.show(ComplaintSupport.this, "",
                    "الرجاء الانتظار...", false, false);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", "ara");
            params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("report_id", selected_id);
            params.put("user_id", user_id);
            params.put("order_id", order_id);
            params.put("notes", ed_message.getText().toString() );
            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.ReportUser, params);
        }
    }





    void callBackVolly(){

        iresult= new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                if(method.equals("get_types")) {

                    loading.dismiss();
                    // Toast.makeText(ComplaintSupport.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    getComplainsTypes res = gson.fromJson(response, getComplainsTypes.class);
                    if (res.getHandle().equals("02")) {  // account not found
                        Toast.makeText(ComplaintSupport.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {   // account found
                        // Toast.makeText(ComplaintSupport.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        List<Report> reports_list = res.getReports();
                        if (reports_list.size() == 0) {

                        } else {
                            for (int i = 0; i < reports_list.size(); i++) {
                                fields.add(reports_list.get(i).getTitle());
                                ids.add(reports_list.get(i).getId().toString());
                            }

                        }
                    }
                }
                else if(method.equals("send_complain")) {
                    loading.dismiss();
                    // Toast.makeText(ComplaintSupport.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    usual_result res = gson.fromJson(response, usual_result.class);
                    if (res.getHandle().equals("02")) {  // account not found
                        Toast.makeText(ComplaintSupport.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {   // account found
                         Toast.makeText(ComplaintSupport.this, res.getMsg(), Toast.LENGTH_LONG).show();
                         finish();
                    }
                }

                else if(method.equals("report_user")) {
                    loading.dismiss();
                    // Toast.makeText(ComplaintSupport.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    usual_result res = gson.fromJson(response, usual_result.class);
                    if (res.getHandle().equals("02")) {  // account not found
                        Toast.makeText(ComplaintSupport.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {   // account found
                        Toast.makeText(ComplaintSupport.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                }

            @Override
            public void notifyError(VolleyError error) {
                loading.dismiss();
                Toast.makeText(ComplaintSupport.this, " مشكلة بالاتصال بالانترنت!", Toast.LENGTH_LONG).show();

                //  Toast.makeText(ComplaintSupport.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };
    }


}
