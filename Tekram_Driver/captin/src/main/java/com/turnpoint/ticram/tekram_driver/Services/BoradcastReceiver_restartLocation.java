package com.turnpoint.ticram.tekram_driver.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class BoradcastReceiver_restartLocation extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, MyLocationServiceAfter.class));
        }
        else {
            context.startService(new Intent(context, MyLocationServiceAfter.class));
        }    }
}
