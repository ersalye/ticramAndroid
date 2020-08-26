package com.turnpoint.ticram.tekram_driver;

import android.app.Application;
import io.paperdb.Paper;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(getApplicationContext());
    }
}
