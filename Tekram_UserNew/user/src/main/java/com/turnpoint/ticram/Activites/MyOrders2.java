package com.turnpoint.ticram.Activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.turnpoint.ticram.Adapters.Adapter_ViewMyOrders;
import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.PathUrl;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.Volley.IResult;
import com.turnpoint.ticram.Volley.VolleyService;
import com.turnpoint.ticram.modules.HistoryOrders;
import com.turnpoint.ticram.modules.singleOrder;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyOrders2 extends AppCompatActivity {
    private Adapter_ViewMyOrders mAdapter;
    RecyclerView mRecyclerView;
    public ProgressDialog loading;
    IResult iresult;
    VolleyService voly_ser;
    RecyclerView.LayoutManager mLayoutManager;
    TextView tv_no_orders;

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
        setContentView(R.layout.activity_my_orders2);
        callBackVolly();
        mRecyclerView = (RecyclerView)findViewById(R.id.recycleview);
        tv_no_orders=findViewById(R.id.textView11);
        //Volley_go();

       // getTabes();

        Volley_go("CAR");
    }



    public void getTabes(){

       /* final ArrayList<tabs_catogary> array_tabs=new Parse_mainCats().getCatalog();
        final LinearLayout lay_addTextView= findViewById(R.id.layout_tabs);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = (float) 1.0;

        if(array_tabs.size() >0){
            for( int i=0; i<array_tabs.size();i++) {
                TextView tv=new TextView(MyOrders2.this);
                tv.setText(array_tabs.get(i).getText());
                tv.setTextColor(Color.parseColor("#2f5b78"));
               // tv.setPadding(5, 5, 5, 5);
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(18);
                tv.setId(i);
                tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
                lay_addTextView.addView(tv);
                  if(i==0)
                tv.setPaintFlags(tv.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

            }


            for (int ii = 0; ii < lay_addTextView.getChildCount(); ii++) {
                View child = lay_addTextView.getChildAt(ii);
                if (child instanceof TextView) {
                    final TextView textView2 = (TextView) child;
                    android.widget.LinearLayout.LayoutParams params2 = new android.widget.LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 1f);
                    textView2.setLayoutParams(params2);


                }
            }



          for (int ii = 0; ii < lay_addTextView.getChildCount(); ii++) {
                View child = lay_addTextView.getChildAt(ii);
                if (child instanceof TextView) {
                    final TextView textView = (TextView) child;

                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(getApplicationContext(), array_tabs.get(textView.getId()).getType() , Toast.LENGTH_SHORT).show();
                            Volley_go(array_tabs.get(textView.getId()).getType());

                            for (int ii = 0; ii < lay_addTextView.getChildCount(); ii++) {
                                View child = lay_addTextView.getChildAt(ii);
                                if (child instanceof TextView) {
                                    final TextView textView2 = (TextView) child;
                                    textView2.setPaintFlags(0);

                                }
                            }

                            textView.setPaintFlags(textView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                            //textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        }
                    });

                }
            }


            Volley_go("CAR");

        }*/

    }




    public void back(View view){
        onBackPressed();
    }


    public void Volley_go(String type){

        loading = ProgressDialog.show(MyOrders2.this, "",
                getResources().getString(R.string.please_wait), false, false);
        Map<String, String> params = new Hashtable<String, String>();
        params.put("access_token", new MySharedPreference(MyOrders2.this).getStringShared("access_token"));
        params.put("local", new GetCurrentLanguagePhone().getLang());
        params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
        params.put("page","1");
        params.put("type", "T");
        voly_ser = new VolleyService(iresult, MyOrders2.this);
        voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                .getStringShared("base_url")+PathUrl.MyOrders, params);

    }




    public void callBackVolly(){
        iresult= new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                Log.d("response", response);
                loading.dismiss();
                //Toast.makeText(MyOrders2.this, response, Toast.LENGTH_LONG).show();
                Gson gson = new Gson();
                HistoryOrders res = gson.fromJson(response, HistoryOrders.class);
                if (res.getHandle().equals("02")) {
                    // Toast.makeText(MyOrders.this, res.getMsg(), Toast.LENGTH_LONG).show();
                } else if (res.getHandle().equals("10")) {
                    //  Toast.makeText(MyOrders.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    List<singleOrder> list_orders=res.getOrders();
                    if(list_orders.size()>0) {

                        tv_no_orders.setVisibility(View.INVISIBLE);
                        mAdapter = new Adapter_ViewMyOrders(getApplicationContext(),
                                list_orders, new Adapter_ViewMyOrders.PostItemListener() {
                                    @Override
                                    public void onPostClick(String order_id ,int pos ) {
                                        Intent i= new Intent(getApplicationContext(), MyOrderDetails.class);
                                        i.putExtra("order_id",order_id);
                                        i.putExtra("pos",pos);
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


                    else if(list_orders.size()==0) {

                        tv_no_orders.setVisibility(View.VISIBLE);
                        mAdapter = new Adapter_ViewMyOrders(getApplicationContext(),
                                list_orders, new Adapter_ViewMyOrders.PostItemListener() {
                            @Override
                            public void onPostClick(String order_id ,int pos ) {
                                Intent i= new Intent(getApplicationContext(), MyOrderDetails.class);
                                i.putExtra("order_id",order_id);
                                i.putExtra("pos",pos);
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

            @Override
            public void notifyError(VolleyError error) {
                loading.dismiss();
                Toast.makeText(MyOrders2.this,R.string.check_internet, Toast.LENGTH_LONG).show();

                Toast.makeText(MyOrders2.this,"Volley Error"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();


            }
        };
    }


}
