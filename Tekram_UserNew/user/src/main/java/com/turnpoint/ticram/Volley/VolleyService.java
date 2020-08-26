package com.turnpoint.ticram.Volley;

import android.app.Application;
import android.content.Context;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class VolleyService {

    IResult mResultCallback = null;
    Context mContext;
    private RequestQueue requestQueue;

    public VolleyService(IResult resultCallback, Context context) {
        mResultCallback = resultCallback;
        mContext = context;
    }

    //--Post-Api---
    public void postDataVolley(String url, final Map<String, String> param) {
        try {
            System.out.println("N/R User requesting: "+url + " params "+ param.toString());
            StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (mResultCallback != null)
                        mResultCallback.notifySuccessPost(response);
                    System.out.println("N/R User response of "+url+": "+response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mResultCallback != null)
                        mResultCallback.notifyError(error);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    return param;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //headers.put("Content-Type", "application/x-www-form-urlencoded");
                    String credentials = "root" + ":" + "Tr3ri@_(rfe";
                    String auths = "Basic "
                            + Base64.encodeToString(credentials.getBytes(),
                            Base64.NO_WRAP);
                    headers.put("Authorization", auths);

                    return headers;
                }
            };

            sr.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            //Application.getInstance(mContext).addToRequestQueue(sr);
            uploadData();
            requestQueue.add(sr);
        } catch (Exception e) {

        }
    }


    public void uploadData() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext);
        }
    }
    // no params
    public void postDataVolley(String url) {
        try {
            System.out.println("N/R User requesting: "+url);
            StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (mResultCallback != null)
                        mResultCallback.notifySuccessPost(response);
                    System.out.println("N/R User response of "+url+": "+response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mResultCallback != null)
                        mResultCallback.notifyError(error);
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //headers.put("Content-Type", "application/x-www-form-urlencoded");
                    String credentials = "root" + ":" + "Tr3ri@_(rfe";
                    String auths = "Basic "
                            + Base64.encodeToString(credentials.getBytes(),
                            Base64.NO_WRAP);
                    headers.put("Authorization", auths);

                    return headers;
                }
            };

            sr.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            //Application.getInstance(mContext).addToRequestQueue(sr);
            uploadData();
            requestQueue.add(sr);
        } catch (Exception e) {

        }
    }









    // get
    public void getDataVolley(String url) {
        try {
            System.out.println("N/R User requesting: "+url);
            StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (mResultCallback != null)
                        mResultCallback.notifySuccessPost(response);
                    System.out.println("N/R User response of "+url+": "+response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mResultCallback != null)
                        mResultCallback.notifyError(error);
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //headers.put("Content-Type", "application/x-www-form-urlencoded");
                    String credentials = "root" + ":" + "Tr3ri@_(rfe";
                    String auths = "Basic "
                            + Base64.encodeToString(credentials.getBytes(),
                            Base64.NO_WRAP);
                    headers.put("Authorization", auths);

                    return headers;
                }
            };

            sr.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            uploadData();
            requestQueue.add(sr);
        } catch (Exception e) {

        }
    }

}