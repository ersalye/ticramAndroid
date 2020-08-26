package com.turnpoint.ticram.Activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.turnpoint.ticram.Adapters.Adapter_tickets;
import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.PathUrl;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.Volley.IResult;
import com.turnpoint.ticram.Volley.VolleyService;
import com.turnpoint.ticram.modules.Ticket;
import com.turnpoint.ticram.modules.respose_tickets;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyTickets extends AppCompatActivity {

    IResult iresult;
    VolleyService voly_ser;
    ProgressBar progressBar;
    RecyclerView mRecyclerView;
    Adapter_tickets mAdapter;
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
        setContentView(R.layout.activity_my_tickets);
        callBackVolly();

        progressBar = findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView)findViewById(R.id.recycleview);

    }


    @Override
    public void onResume(){
        super.onResume();
        method="view_tickets";
        Volley_go();
    }

    public void add_new_ticket(View v){
        Intent i= new Intent(getApplicationContext(), DepartmentList.class);
        i.putExtra("from", "not_order");
        startActivity(i);
    }


    public void back(View view){
        onBackPressed();
    }

    private void Volley_go(){
        if(method.equals("view_tickets")) {
            progressBar.setVisibility(View.VISIBLE);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", new GetCurrentLanguagePhone().getLang());
            params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("page", "1");
            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.Tickets, params);
        }


    }





    void callBackVolly(){

        iresult= new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                if(method.equals("view_tickets")) {
                    progressBar.setVisibility(View.INVISIBLE);
                    // Toast.makeText(ComplaintSupport.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    respose_tickets res = gson.fromJson(response, respose_tickets.class);
                    if (res.getHandle().equals("02")) {  // account not found
                        Toast.makeText(MyTickets.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {   // account found
                       // Toast.makeText(MyTickets.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        // finish();
                        final ArrayList<Ticket> list_=res.getTickets();
                        if(list_.size()>0) {
                            mAdapter = new Adapter_tickets (getApplicationContext(),
                                    list_, new Adapter_tickets.PostItemListener() {
                                @Override
                                public void onPostClick(String id ) {
                                        Intent i = new Intent(getApplicationContext(), ViewTicket.class);
                                        i.putExtra("ticket_id", id);
                                        startActivity(i);
                                    //   Toast.makeText(getApplicationContext(), order_id+" - "+String.valueOf(pos),Toast.LENGTH_SHORT).show();
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


            }

            @Override
            public void notifyError(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MyTickets.this,R.string.check_internet, Toast.LENGTH_LONG).show();
                //  Toast.makeText(ComplaintSupport.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };
    }


}
