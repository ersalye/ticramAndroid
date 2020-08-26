package com.turnpoint.ticram.tekram_driver.FCM;

import android.content.Context;
import android.content.Intent;
import androidx.legacy.content.WakefulBroadcastReceiver;
import android.widget.Toast;

public class FirebaseBroadcastReceiver  extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
         if(intent.getExtras() != null){
             String order_id= intent.getExtras().getString("order_id");
             Toast.makeText(context, "Order ID : "+order_id, Toast.LENGTH_LONG).show();
         }
    }
}
