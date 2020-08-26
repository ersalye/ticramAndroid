package com.turnpoint.ticram.tekram_driver.Volley;

import android.content.Context;
import android.os.Build;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.io.File;
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
            System.out.println("N/R Cap requesting: "+url +" ruequest: "+param);
            StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (mResultCallback != null)
                        mResultCallback.notifySuccessPost(response);
                    System.out.println("N/R Cap response: "+response);
                    //getJSONListener.onRemoteCallComplete(response.toString());
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
                    30*1000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            //Application.getInstance(mContext).addToRequestQueue(sr);
           // RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            uploadData();
            requestQueue.add(sr);
        } catch (Exception e) {

        }
    }


    // post --no params
    public void postDataVolley(String url) {
        try {
            System.out.println("N/R Cap requesting: "+url);
            StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (mResultCallback != null)
                        mResultCallback.notifySuccessPost(response);
                    System.out.println("N/R Cap response: "+response);
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
                    30*1000,
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
            System.out.println("N/R Cap requesting: "+url);
            StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (mResultCallback != null)
                        mResultCallback.notifySuccessPost(response);
                    System.out.println("N/R Cap response: "+response);
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
                    30*1000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

           // RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            uploadData();
            requestQueue.add(sr);
        } catch (Exception e) {

        }
    }

    public void uploadData() {
        try {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(mContext);
            }
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }






    //-- Post Api .. for clear cashe .. when trip finished
    public void postDataVolleyClearCashe(String url, final Map<String, String> param) {
        try {

            System.out.println("N/R Cap requesting: "+url +" ruequest: "+param);
            // Instantiate the cache
            Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap
            Network network = new BasicNetwork(new HurlStack());
            requestQueue = new RequestQueue(cache, network);
            requestQueue.start();

            StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (mResultCallback != null)
                        mResultCallback.notifySuccessPost(response);
                    deleteCache(mContext);
                    System.out.println("N/R Cap response: "+response);
                    //getJSONListener.onRemoteCallComplete(response.toString());
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
                    30*1000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            //Application.getInstance(mContext).addToRequestQueue(sr);
            // RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            uploadData();
            requestQueue.add(sr);
        } catch (Exception e) {

        }
    }






    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        else if(dir!= null && dir.isFile())
            return dir.delete();
        else {
            return false;
        }
    }

}