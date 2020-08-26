package com.turnpoint.ticram;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by marina on 27/03/2018.
 */

public class MySharedPreference {

   public SharedPreferences sharedPref ;
   public SharedPreferences.Editor ed;

   public MySharedPreference(Context ctx){
       sharedPref = ctx.getSharedPreferences("tikram",Context.MODE_PRIVATE);
       ed=sharedPref.edit();
   }
    public String getStringShared(String key) {
        return sharedPref.getString(key, "");
    }

    public  void setStringShared(String key, String value) {
        ed.putString(key, value);
        ed.commit();
    }

    public  int getIntShared(String key) {
        return sharedPref.getInt(key,0);
    }

    public  void setIntShared(String key, int value) {
        ed.putInt(key, value);
        ed.commit();
    }

    public  float getDoubleShared(String key) {
        return sharedPref.getFloat(key,0);
    }

    public  void setDoubleShared(String key, float value) {
        ed.putFloat(key, value);
        ed.commit();
    }


    public boolean getBooleanShared(String key) {
        return sharedPref.getBoolean(key,true);

    }

    public void setBooleanShared(String key, boolean value) {
        ed.putBoolean(key, value);
        ed.commit();
    }


}
