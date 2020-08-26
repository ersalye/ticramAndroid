package com.turnpoint.ticram.Activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.turnpoint.ticram.DataParser;
import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.PathUrl;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.Volley.IResult;
import com.turnpoint.ticram.Volley.VolleyService;
import com.turnpoint.ticram.modules.viewDetails;
import com.turnpoint.ticram.modules.viewDetails2;
import com.turnpoint.ticram.modules.viewDetailsOrder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyOrderDetails extends AppCompatActivity {

    IResult iresult;
    VolleyService voly_ser;
    public ProgressDialog loading;
    public TextView tv_start_loc , tv_end_loc , tv_date;
    private  TextView tv_captainname,tv_car_type,tv_car_num , tv_transport_type,
            tv_fee, tv_payMethod,tv_orderID , tv_time;
    //List<singleOrder> wsListCopy;
    int pos_list;
    GoogleMap mMap;
    String order_id;
    BitmapDescriptor icon_user_start,icon_user_destination;
    SupportMapFragment mapFragment;
    String method;
    String userID;
    viewDetails v;
    viewDetails2 v2;
    String destination_exist="no";
    ImageView img_mapp;
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
        setContentView(R.layout.activity_my_order_details);
        callBackVolly();



        pos_list=getIntent().getExtras().getInt("pos");
        order_id=getIntent().getExtras().getString("order_id");

        //wsListCopy=new Adapter_ViewMyOrders().getmItems();

        tv_start_loc =findViewById(R.id.tv_from);
        tv_end_loc =findViewById(R.id.tv_to);
        tv_date =findViewById(R.id.tv_date);

        tv_captainname =findViewById(R.id.tv_captainname);
        tv_car_type=findViewById(R.id.tv_car_type);
        tv_car_num =findViewById(R.id.tv_car_num);
        tv_transport_type =findViewById(R.id.tv_transport_type);

        tv_fee =findViewById(R.id.tv_OrderFee);
        tv_time =findViewById(R.id.tv_transportTime);
        tv_payMethod =findViewById(R.id.tv_pay_type);
        tv_orderID =findViewById(R.id.tv_OrderNum);
        img_mapp =findViewById(R.id.img_map);
        /* tv_date.setText(wsListCopy.get(pos_list).getDateTrip());
         tv_start_loc.setText(wsListCopy.get(pos_list).getLocationTxt());
         tv_end_loc.setText(wsListCopy.get(pos_list).getToLocationTxt());
         tv_fee.setText(wsListCopy.get(pos_list).getFee());
         tv_payMethod_.setText(wsListCopy.get(pos_list).getPaymentMethod());*/

        method="order_details";
        Volley_go();

    }

    public void back(View view){
        onBackPressed();
    }

    public void report_trip(View View){
        /*Intent i= new Intent(getApplicationContext(), ComplaintSupport.class);
        i.putExtra("from","myorderDetails");
        i.putExtra("order_id", order_id);
        i.putExtra("user_id",userID);
        startActivity(i);*/
        Intent i= new Intent(getApplicationContext(), DepartmentList.class);
        i.putExtra("from", "order");
        i.putExtra("order_id", order_id);
        startActivity(i);
    }


    public void reorder(View View){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyOrderDetails.this);
        alertDialogBuilder.setMessage(getResources().getString(R.string.are_you_sure));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent i= new Intent(getApplicationContext(), MapActivity.class);
                        new MySharedPreference(getApplicationContext()).setStringShared("from_w_status", "myorderdetails");
                        //i.putExtra("from","myorderDetails");
                        if(destination_exist.equals("yes")) {
                            i.putExtra("tolocation", v.getToLocation());
                            i.putExtra("destination_exist", "yes");
                        }
                        if(destination_exist.equals("no")) {
                            i.putExtra("destination_exist", "no");
                        }
                        i.putExtra("location" , v.getLocation());
                        i.putExtra("subtype" , v.getSubtype());
                        i.putExtra("tmp_distance",v.getDistance());
                        i.putExtra("tmp_time",v.getTime());
                        i.putExtra("main_type",v.getType());
                        //Toast.makeText(getApplicationContext(), v.getType(), Toast.LENGTH_SHORT).show();
                        startActivity(i);
                        finish();
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




    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        method="order_details";
        Volley_go();

        int mapWidth =  mapFragment.getView().getMeasuredWidth();
        int mapHeight =  mapFragment.getView().getMinimumHeight();
       *//* String loc_from=wsListCopy.get(pos_list).getLocation();
        String loc_to=wsListCopy.get(pos_list).getToLocation();
        LatLng user_loc_final = null, user_loc_start;
        //add final location user marker IF exsist
        if(!loc_to.equals("") && loc_to != null){
            String string_final_loc = loc_to;
            String[] separated_final = string_final_loc.split(",");
            user_loc_final = new LatLng(Double.parseDouble(separated_final[0]), Double.parseDouble(separated_final[1]));
            MarkerOptions markerOptionsss = new MarkerOptions().position(user_loc_final).icon(icon_user_destination);
            mMap.addMarker(markerOptionsss);
        }
        // add start location user marker
        String string_start_loc = loc_from;
        String[] separated = string_start_loc.split(",");
        user_loc_start = new LatLng(Double.parseDouble(separated[0]), Double.parseDouble(separated[1]));
        MarkerOptions markerOptionsss = new MarkerOptions().position(user_loc_start).icon(icon_user_start);
        mMap.addMarker(markerOptionsss);


        if(!loc_to.equals("") && loc_to != null && !loc_to.equals("0.0")) {

            String url = getUrl(user_loc_start, user_loc_final);
            FetchUrl FetchUrl = new FetchUrl();
            FetchUrl.execute(url);
*//**//*
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(user_loc_final);
            builder.include(user_loc_start);
            LatLngBounds bounds = builder.build();
            int padding = 10; // offset from edges of the marker_center_map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,mapWidth, mapHeight, padding);
            mMap.moveCamera(cu);
            mMap.animateCamera(cu);*//**//*
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user_loc_start, 12.0f));

        }

        else if(loc_to.equals("") || loc_to == null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user_loc_start, 12.0f));

        }

        method="order_details";
        Volley_go();
*//*



    }*/
    public String getTime(int timer) {

        int minutes = (int) ((timer / (1)) % 60);
        int hours = (int) ((timer / (1 * 60)) % 24);
        String finalTime = "";
        if (hours == 0)
            finalTime = minutes + " min";
        else
            finalTime = hours + getResources().getString(R.string.hour)+","+ minutes + " " + getResources().getString(R.string.min);

        return finalTime;
    }

    public void Volley_go(){
        if(method.equals("order_details")) {
            loading = ProgressDialog.show(MyOrderDetails.this, "",
                    getResources().getString(R.string.please_wait), false, true);
            Map<String, String> params = new Hashtable<String, String>();
            params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
            params.put("local", new GetCurrentLanguagePhone().getLang());
            params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
            params.put("order_id", String.valueOf(order_id));
            voly_ser = new VolleyService(iresult, getApplicationContext());
            voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+PathUrl.ViewDetailsOrder, params);
        }



    }




    public void callBackVolly(){
        iresult= new IResult() {
            @Override
            public void notifySuccessPost(String response) {

                if(method.equals("order_details")) {
                    loading.dismiss();
                    // Toast.makeText(MyOrderDetails.this, response, Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    viewDetailsOrder res = gson.fromJson(response, viewDetailsOrder.class);
                    if (res.getHandle().equals("02")) {
                        Toast.makeText(MyOrderDetails.this, res.getMsg(), Toast.LENGTH_LONG).show();
                    } else if (res.getHandle().equals("10")) {
                       // Toast.makeText(MyOrderDetails.this, res.getMsg(), Toast.LENGTH_LONG).show();
                         v=res.getOrder();
                         v2=v.getOrderInfo();

                        tv_date.setText(v.getDateTime());
                        tv_start_loc.setText(v.getLocationTxt());
                        tv_end_loc.setText(v.getToLocationTxt());

                        tv_payMethod.setText(v.getPaymentType());
                        String timeTrip = v.getTime().replaceAll("\\D+","");
                        tv_time.setText(v.getTime());
                        tv_orderID.setText(v.getOrderNo());
                        tv_fee.setText(v.getFee());

                        tv_captainname.setText(v.getTransportName());
                        tv_car_num.setText(v.getPlateNo());
                        tv_car_type.setText(v.getTransportCar());
                        tv_transport_type.setText(v.getSubtype_txt());

                        userID=v.getTransportId();

                        Glide.with(getApplicationContext())
                                .load(v.getStaticmap())
                                .apply(new RequestOptions()
                                        .centerCrop()
                                        .dontAnimate()
                                        .dontTransform())
                                .into(img_mapp);

                       /* String loc_from=v.getLocation();
                        String loc_to=v.getToLocation();
                        LatLng user_loc_final = null, user_loc_start;
                        //add final location user marker IF exsist
                        if(!loc_to.equals("") && loc_to != null){
                            destination_exist="yes";
                            String string_final_loc = loc_to;
                            String[] separated_final = string_final_loc.split(",");
                            user_loc_final = new LatLng(Double.parseDouble(separated_final[0]), Double.parseDouble(separated_final[1]));
                            MarkerOptions markerOptionsss = new MarkerOptions().position(user_loc_final).icon(icon_user_destination);
                            mMap.addMarker(markerOptionsss);
                        }
                        // add start location user marker
                        String string_start_loc = loc_from;
                        String[] separated = string_start_loc.split(",");
                        user_loc_start = new LatLng(Double.parseDouble(separated[0]), Double.parseDouble(separated[1]));
                        MarkerOptions markerOptionsss = new MarkerOptions().position(user_loc_start).icon(icon_user_start);
                        mMap.addMarker(markerOptionsss);*/


                    /*    if(!loc_to.equals("") && loc_to != null && !loc_to.equals("0.0")) {

                            String url = getUrl(user_loc_start, user_loc_final);
                            FetchUrl FetchUrl = new FetchUrl();
                            FetchUrl.execute(url);

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user_loc_start, 12.0f));

                        }

                        else if(loc_to.equals("") || loc_to == null) {
                            destination_exist="no";
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user_loc_start, 12.0f));

                        }*/



                    }
                }




            }

            @Override
            public void notifyError(VolleyError error) {
                loading.dismiss();
                Toast.makeText(MyOrderDetails.this,R.string.check_internet, Toast.LENGTH_LONG).show();

                // Toast.makeText(MyOrderDetails.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();


            }
        };
    }








    //----------------------------------- drawing rout stuff --------------------------------------------------------//


    private String getUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLACK);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }

}

