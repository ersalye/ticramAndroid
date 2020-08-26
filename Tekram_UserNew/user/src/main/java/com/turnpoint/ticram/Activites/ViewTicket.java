package com.turnpoint.ticram.Activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.turnpoint.ticram.Adapters.Adapter_conversation;
import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.PathUrl;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.Volley.IResult;
import com.turnpoint.ticram.Volley.VolleyService;
import com.turnpoint.ticram.modules.Conversation_ticket;
import com.turnpoint.ticram.modules.Ticket_view;
import com.turnpoint.ticram.modules.response_reply;
import com.turnpoint.ticram.modules.response_view_ticket;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ViewTicket extends AppCompatActivity {

    IResult iresult;
    VolleyService voly_ser;
    ProgressBar progressBar;
    RecyclerView mRecyclerView;
    Adapter_conversation mAdapter;
    String method, ticket_id;
    public TextView tv_title, tv_ref_num, tv_date , tv_status , tv_ticket_num_title;
    Button btn_reply;
    String msg_content;
    String language;
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
        language= new GetCurrentLanguagePhone().getLang();
        if(language.equals("eng"))
            setContentView(R.layout.activity_view_ticket);
        else if(language.equals("ara"))
            setContentView(R.layout.activity_view_ticket_ara);

        callBackVolly();

        progressBar = findViewById(R.id.progressBar);
        mRecyclerView = findViewById(R.id.recycleview);
        tv_title = findViewById(R.id.textView15);
        tv_ref_num= findViewById(R.id.textView17);
        tv_date = findViewById(R.id.textView18);
        tv_status = findViewById(R.id.texttvieww);
        btn_reply=findViewById(R.id.button13);
        tv_ticket_num_title= findViewById(R.id.textview_ticketNum);

        ticket_id = getIntent().getExtras().getString("ticket_id");
        method="view";
        Volley_go();
      //  Toast.makeText(getApplicationContext(), new MySharedPreference(getApplicationContext()).getStringShared("user_id"), Toast.LENGTH_LONG).show();
    }



    @Override
    public void onResume(){
        super.onResume();

    }
    public void reply(View v){
         open_dialoge_reply();
    }

    public void back(View view){
        onBackPressed();
    }


    private void Volley_go(){
        if(method.equals("view")) {
            progressBar.setVisibility(View.VISIBLE);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", new GetCurrentLanguagePhone().getLang());
            params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("id", ticket_id);
            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.ViewTicket, params);
        }


        if(method.equals("send_reply")) {
            progressBar.setVisibility(View.VISIBLE);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", new GetCurrentLanguagePhone().getLang());
            params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("id", ticket_id);
            params.put("text", msg_content);
            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.ReplyTicket, params);
        }


    }





    void callBackVolly(){
        iresult= new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                if(method.equals("view")) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.d("sssssssss", response);
                    // Toast.makeText(ComplaintSupport.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    response_view_ticket res = gson.fromJson(response, response_view_ticket.class);
                    if (res.getHandle().equals("02")) {  // account not found
                        Toast.makeText(ViewTicket.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {   // account found
                       // Toast.makeText(ViewTicket.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        Ticket_view ticket=res.getTicket();
                         tv_title.setText(ticket.getDept());
                        tv_date.setText(ticket.getDate());
                        tv_status.setText(ticket.getStatusText());
                        tv_ref_num.setText(ticket.getRefNo());
                        tv_ticket_num_title.setText(ticket.getRefNo());

                        Log.d("statusss" , ticket.getStatus());
                        if(ticket.getStatus().equals("NEW") || ticket.getStatus().equals("OPEN")){
                            tv_status.setBackground(ContextCompat.
                                    getDrawable(getApplicationContext(), R.drawable.round_orange));

                        }else if(ticket.getStatus().equals("REPLY")){
                            tv_status.setBackground(ContextCompat.getDrawable(getApplicationContext(),
                                    R.drawable.round_blue));

                        } else if(ticket.getStatus().equals("CLOSED")){
                            tv_status.setBackground(ContextCompat.getDrawable(getApplicationContext(),
                                    R.drawable.round_green));
                            btn_reply.setVisibility(View.INVISIBLE);
                        }

                        final ArrayList<Conversation_ticket> list_=ticket.getConversation();
                        mAdapter = new Adapter_conversation(getApplicationContext(),
                                list_, new Adapter_conversation.PostItemListener() {
                            @Override
                            public void onPostClick(String id  ) {

                            }
                        });

                        if(list_.size()>0) {
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            mRecyclerView.setLayoutManager(layoutManager);
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.setHasFixedSize(true);
                            mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
                        }

                    }
                }


                if(method.equals("send_reply")) {
                    progressBar.setVisibility(View.INVISIBLE);
                    // Toast.makeText(ComplaintSupport.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    response_reply res = gson.fromJson(response, response_reply.class);
                    if (res.getHandle().equals("02")) {  // account not found
                        Toast.makeText(ViewTicket.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {   // account found
                        Toast.makeText(ViewTicket.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        //finish();
                       /* SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        String currentDateandTime = sdf.format(new Date());
                        mAdapter.addItem(new Conversation_ticket(currentDateandTime,
                                new MySharedPreference(getApplicationContext()).getStringShared("user_name") ,
                                msg_content));*/
                        Intent i = new Intent(getApplicationContext(), ViewTicket.class);
                        i.putExtra("ticket_id", ticket_id);
                        startActivity(i);
                        finish();
                    }
                }


            }

            @Override
            public void notifyError(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ViewTicket.this,R.string.check_internet, Toast.LENGTH_LONG).show();
                //  Toast.makeText(ComplaintSupport.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };
    }










    public void open_dialoge_reply() {
        LayoutInflater inflater = LayoutInflater.from(ViewTicket.this);
        View subView = inflater.inflate(R.layout.dialoge_reply, null);
        final EditText ed_msg = (EditText) subView.findViewById(R.id.editText2);
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
                alertDialog.cancel();
                msg_content=ed_msg.getText().toString();
                method = "send_reply";
                Volley_go();


            }
        });
        builder.show();



    }


}
