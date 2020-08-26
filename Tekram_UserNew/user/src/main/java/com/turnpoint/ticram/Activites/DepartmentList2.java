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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.turnpoint.ticram.Adapters.Adapter_subDepartment;
import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.Parse_departments;
import com.turnpoint.ticram.PathUrl;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.Volley.IResult;
import com.turnpoint.ticram.Volley.VolleyService;
import com.turnpoint.ticram.modules.Department;
import com.turnpoint.ticram.modules.SubDepartment;
import com.turnpoint.ticram.modules.respose_tickets;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DepartmentList2 extends AppCompatActivity {

    IResult iresult;
    VolleyService voly_ser;
    ProgressBar progressBar;
    RecyclerView mRecyclerView;
    TextView tv_title;
    Adapter_subDepartment mAdapter;
    String order_id, method ,title, selected_dept_id , from_act;
    int dep_id;
    View subView;

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
        setContentView(R.layout.activity_department_list2);
        callBackVolly();

        progressBar = findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview);
        tv_title=findViewById(R.id.textView_title);
        from_act = getIntent().getExtras().getString("from");
        title = getIntent().getExtras().getString("title");
        dep_id = getIntent().getExtras().getInt("pos");
        //Toast.makeText(getApplicationContext(),dep_id,Toast.LENGTH_SHORT).show();

        if(from_act.equals("order"))
            order_id=getIntent().getExtras().getString("order_id");
        tv_title.setText(title);
        show_list();
    }

    public void back(View view) {
        onBackPressed();
    }


    public void show_list(){
        ArrayList<SubDepartment> list_sub=new ArrayList<SubDepartment>();
        ArrayList<Department> list_main=new Parse_departments().getCatalog();
       for(int i=0 ; i <list_main.size(); i++){
            if(list_main.get(i).getId()== dep_id){
              list_sub=list_main.get(i).getSubDepartments();
                mAdapter = new Adapter_subDepartment(getApplicationContext(),
                        list_sub, new Adapter_subDepartment.PostItemListener() {
                    @Override
                    public void onPostClick(String title,String id ,int pos ) {
                        selected_dept_id=id;
                        send_ticket();
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

    public void send_ticket(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DepartmentList2.this);
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



    private void Volley_go(){
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





    void callBackVolly(){

        iresult= new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                 if(method.equals("send_ticket")) {
                     progressBar.setVisibility(View.INVISIBLE);
                     // Toast.makeText(ComplaintSupport.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                     respose_tickets res = gson.fromJson(response, respose_tickets.class);
                    if (res.getHandle().equals("02")) {  // account not found
                        Toast.makeText(DepartmentList2.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {   // account found
                        //Toast.makeText(DepartmentList2.this, res.getMsg(), Toast.LENGTH_LONG).show();
                        open_dialoge();
                       // finish();
                    }
                }


            }

            @Override
            public void notifyError(VolleyError error) {

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(DepartmentList2.this,R.string.check_internet, Toast.LENGTH_LONG).show();
                //  Toast.makeText(ComplaintSupport.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };
    }




    public void open_dialoge() {
        LayoutInflater inflater = LayoutInflater.from(DepartmentList2.this);
        subView = inflater.inflate(R.layout.dialoge_custom, null);
        final TextView tv_title = (TextView) subView.findViewById(R.id.textView_title);
        final Button btn_ok = (Button) subView.findViewById(R.id.button12);

        // subView=null;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        tv_title.setText(title);
        builder.setView(subView);
        final AlertDialog alertDialog = builder.create();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MyProfile.this, "clicked", Toast.LENGTH_LONG).show();
                builder.setCancelable(true);
                alertDialog.dismiss();
                 startActivity(new Intent(getApplicationContext(), MyTickets.class));
                 finish();
              }
        });
        builder.show();
    }




}

