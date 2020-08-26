package com.turnpoint.ticram.Activites;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.turnpoint.ticram.Adapters.Adapter_departments;
import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.Parse_departments;
import com.turnpoint.ticram.PathUrl;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.Volley.IResult;
import com.turnpoint.ticram.Volley.VolleyService;
import com.turnpoint.ticram.modules.Department;
import com.turnpoint.ticram.modules.DepartmentResponse;
import com.turnpoint.ticram.modules.respose_tickets;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DepartmentList extends AppCompatActivity {

    IResult iresult;
    VolleyService voly_ser;
    ProgressBar progressBar;
    RecyclerView mRecyclerView;
    Adapter_departments mAdapter;
    String order_id , from_act, method, selected_dept_id;

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
        setContentView(R.layout.activity_department_list);
        callBackVolly();
        progressBar = findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycleview);

        from_act=getIntent().getExtras().getString("from");
        if(from_act.equals("order"))
        order_id=getIntent().getExtras().getString("order_id");
       // Toast.makeText(getApplicationContext(), new GetCurrentLanguagePhone().getLang(), Toast.LENGTH_LONG).show();

        method="get_list_dep";
        Volley_go();
    }

    public void back(View view){
        onBackPressed();
    }



    public void Volley_go(){
        progressBar.setVisibility(View.VISIBLE);
        if(method.equals("get_list_dep")) {
            voly_ser = new VolleyService(iresult, DepartmentList.this);
            voly_ser.getDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.DepartmentList);
        }

        if(method.equals("send_ticket")) {
            progressBar.setVisibility(View.VISIBLE);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", new GetCurrentLanguagePhone().getLang());
            params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("dept_id", selected_dept_id);
            if(from_act.equals("order"))
                params.put("order_id", order_id);
            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.SendNewTicket, params);
        }
    }




    public void callBackVolly(){
        iresult= new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                if(method.equals("get_list_dep")) {
                    Log.d("response", response);
                    progressBar.setVisibility(View.INVISIBLE);
                    Gson gson = new Gson();
                    DepartmentResponse res = gson.fromJson(response, DepartmentResponse.class);
                    if (res.getHandle().equals("02")) {
                        // Toast.makeText(MyOrders.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {
                        final ArrayList<Department> list_ = res.getDepartments();
                        new Parse_departments().setCatalog(list_);
                        if (list_.size() > 0) {

                            mAdapter = new Adapter_departments(getApplicationContext(),
                                    list_, new Adapter_departments.PostItemListener() {
                                @Override
                                public void onPostClick(String title, int id, int pos) {
                                    selected_dept_id=String.valueOf(id);
                                   // Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();

                                    if (list_.get(pos).getSubDepartments() != null) {
                                        Intent i = new Intent(getApplicationContext(), DepartmentList2.class);
                                        i.putExtra("order_id", order_id);
                                        i.putExtra("pos", id);
                                        i.putExtra("title", title);
                                        i.putExtra("from", from_act);
                                        startActivity(i);
                                    } else {
                                       // Toast.makeText(getApplicationContext(), "no sub", Toast.LENGTH_SHORT).show();
                                        //   Toast.makeText(getApplicationContext(), order_id+" - "+String.valueOf(pos),Toast.LENGTH_SHORT).show();
                                        send_Ticket();

                                    }
                                }
                            });

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            mRecyclerView.setLayoutManager(layoutManager);
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.setHasFixedSize(true);
                            mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
                        }

                    }
                }


                if(method.equals("send_ticket")) {
                    progressBar.setVisibility(View.INVISIBLE);
                    // Toast.makeText(ComplaintSupport.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    respose_tickets res = gson.fromJson(response, respose_tickets.class);
                    if (res.getHandle().equals("02")) {  // account not found
                        Toast.makeText(DepartmentList.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {   // account found
                        Toast.makeText(DepartmentList.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        open_dialoge();
                        // finish();
                    }
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(DepartmentList.this,R.string.check_internet, Toast.LENGTH_LONG).show();
                // Toast.makeText(MyOrders2.this,"Volley Error"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();


            }
        };
    }







    public void send_Ticket(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DepartmentList.this);
        alertDialogBuilder.setMessage(getResources().getString(R.string.are_you_sure));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        method = "send_ticket";
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



    public void open_dialoge() {
        LayoutInflater inflater = LayoutInflater.from(DepartmentList.this);
        View subView = inflater.inflate(R.layout.dialoge_custom, null);
        final TextView tv_title = (TextView) subView.findViewById(R.id.textView_title);
        final Button btn_ok = (Button) subView.findViewById(R.id.button12);

        // subView=null;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(subView);
        final AlertDialog alertDialog = builder.create();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MyProfile.this, "clicked", Toast.LENGTH_LONG).show();
                builder.setCancelable(true);
                alertDialog.dismiss();



            }
        });
        builder.show();
    }
}
