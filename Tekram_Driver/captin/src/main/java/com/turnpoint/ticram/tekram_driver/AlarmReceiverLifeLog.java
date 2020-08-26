package com.turnpoint.ticram.tekram_driver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;
import com.turnpoint.ticram.tekram_driver.Services.LocationServiceBeforeTawaklna;

public class AlarmReceiverLifeLog extends BroadcastReceiver {




    @Override
    public void onReceive(Context context, Intent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            context.startForegroundService(new Intent(context,
                    LocationServiceBeforeTawaklna.class));
            Toast.makeText(context, "RUNNING", Toast.LENGTH_SHORT).show();
        }
        else {
            context.startService(new Intent(context,
                    LocationServiceBeforeTawaklna.class));
            Toast.makeText(context, "RUNNING", Toast.LENGTH_SHORT).show();
        }
    }
    }
