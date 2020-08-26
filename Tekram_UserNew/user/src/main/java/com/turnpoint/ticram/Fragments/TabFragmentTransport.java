package com.turnpoint.ticram.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.Hashtable;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class TabFragmentTransport extends Fragment {

    private Adapter_ViewMyOrders mAdapter;
    RecyclerView mRecyclerView;
    public  ProgressDialog loading;
    IResult iresult;
    VolleyService voly_ser;
    RecyclerView.LayoutManager mLayoutManager;
    Context ctx;
    public TabFragmentTransport(){
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Al-Jazeera-Arabic-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        ctx=activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.tab_fragment_transport, container, false);
       // ctx=container.getContext();
      /*  mRecyclerView = (RecyclerView)view.findViewById(R.id.recycleview);
        mAdapter = new Adapter_ViewMyOrders(getActivity(),
                new ArrayList<singleOrder>(0),
                new Adapter_ViewMyOrders.PostItemListener() {
                    @Override
                    public void onPostClick(String order_id ,int pos ) {
                       *//* Intent i= new Intent(getApplicationContext(), MyOrderDetails.class);
                        i.putExtra("order_id",order_id);
                        i.putExtra("pos",pos);
                        startActivity(i);

                        Toast.makeText(getActivity(),"cliecked",Toast.LENGTH_SHORT).show();
*//*

                    }
                });

        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);*/

        return view ;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {

      /*  callBackVolly();
        Volley_go();*/

       // mLayoutManager = new LinearLayoutManager(getActivity());
       // mRecyclerView.setLayoutManager(mLayoutManager);

      /*  LinearLayoutManager llm;
        llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);*/
    }




    public void Volley_go(){

        loading = ProgressDialog.show(getActivity(), "",
                getResources().getString(R.string.please_wait), false, false);
        Map<String, String> params = new Hashtable<String, String>();
        params.put("access_token", new MySharedPreference(getActivity()).getStringShared("access_token"));
        params.put("local", new GetCurrentLanguagePhone().getLang());
        params.put("user_id", new MySharedPreference(getActivity()).getStringShared("user_id"));
        params.put("page","1");
        params.put("type", "T");
        voly_ser = new VolleyService(iresult, getActivity());
        voly_ser.postDataVolley(new MySharedPreference(getActivity())
                .getStringShared("base_url")+PathUrl.MyOrders, params);

    }




    public void callBackVolly(){
        iresult= new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                loading.dismiss();
                Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                Gson gson = new Gson();
                HistoryOrders res = gson.fromJson(response, HistoryOrders.class);
                if (res.getHandle().equals("02")) {
                    // Toast.makeText(MyOrders.this, res.getMsg(), Toast.LENGTH_LONG).show();
                } else if (res.getHandle().equals("10")) {
                    //  Toast.makeText(MyOrders.this, res.getMsg(), Toast.LENGTH_LONG).show();
                   /* List<singleOrder> list_orders=res.getOrders();
                    if(list_orders.size()>0) {
                        mAdapter.updateAnswers(list_orders);
                    }*/
                }

            }

            @Override
            public void notifyError(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getActivity(),"Volley Error"+
                        error.getMessage().toString(), Toast.LENGTH_LONG).show();


            }
        };
    }


}