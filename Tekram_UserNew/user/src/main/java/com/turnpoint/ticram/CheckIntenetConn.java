package com.turnpoint.ticram;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by marina on 02/04/2018.
 */

public class CheckIntenetConn {

    Context ctx;
    public CheckIntenetConn(Context context){
     this.ctx=context;
    }

    public boolean isNetworkAvailable() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
