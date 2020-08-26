package com.turnpoint.ticram.Sinch;

import android.content.Context;

import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;

/**
 * Created by Server on 9/16/2017.
 */

public class mySinchClient {
    static Context ctx;
    static String callerid;

   static SinchClient sinchClient;

    mySinchClient(){}
    public mySinchClient(Context ctx, String callerid) {
       this.ctx= ctx;
        this.callerid= callerid;
        sinchClient = Sinch.getSinchClientBuilder()
                .context(ctx)
                .applicationKey("f08639b7-f025-4c5d-8518-543ffe034365")
                .applicationSecret("x4Cpv1KFfkmjKJSN+T8zHQ==")
               //.environmentHost("sandbox.sinch.com")
                .environmentHost("clientapi.sinch.com")
                .userId(callerid)
                .build();

    }


    public SinchClient getSinchClient(){

        return sinchClient;
    }




}
