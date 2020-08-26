package com.turnpoint.ticram.tekram_driver;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by marina on 27/03/2018.
 */

public class MySharedPreference {

   public SharedPreferences sharedPref ;
   public SharedPreferences.Editor ed;

   public MySharedPreference(Context ctx){
       sharedPref = ctx.getSharedPreferences("tikram_driver",Context.MODE_PRIVATE);
       ed=sharedPref.edit();
   }


    public void MySharedPreferenceClear(){
       ed.clear().commit();
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

    }  public  void setIntShared(String key, String string) {
        ed.putString(key, string);
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

    public boolean getBooleanShared(String key, Boolean defaultValue) {
        return sharedPref.getBoolean(key,defaultValue);

    }

    public void setBooleanShared(String key, boolean value) {
        ed.putBoolean(key, value);
        ed.commit();
    }


    public float getFloatShared(String key) {
        return sharedPref.getFloat(key,0);

    }

    public void setFloatShared(String key, Float value) {
            ed.putFloat(key, value);
            ed.commit();
    }

    public Long getLongShared(String key) {
        return sharedPref.getLong(key,0);
    }



    public void setLongShared(String key, Long value) {
        ed.putFloat(key, value);
        ed.commit();
    }



}
