package com.turnpoint.ticram.tekram_driver.Services;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.turnpoint.ticram.tekram_driver.Activites.IncommingCallActivity;
import com.turnpoint.ticram.tekram_driver.MySharedPreference;
import com.turnpoint.ticram.tekram_driver.Sinch.mySinchClient;
import com.turnpoint.ticram.tekram_driver.Sinch.onCommingCall;

public class StartSinch extends Service {
   // Context ctx = this;

    public StartSinch() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        //  Toast.makeText(StartSinch.this,"Onstart",Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        register_sinch();
        // Toast.makeText(StartSinch.this,"OnCreate",Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        /*Intent broadcastIntent = new Intent("uk.ac.shef.oak.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);*/
        //Toast.makeText(StartSinch.this,"onDestroy",Toast.LENGTH_SHORT).show();

    }



    public void register_sinch(){
        mySinchClient mysinch=new mySinchClient(StartSinch.this,
                new MySharedPreference(StartSinch.this).getStringShared("mobile"));
        SinchClient sinchClient = mysinch.getSinchClient();
        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        //sinchClient.setSupportManagedPush(true);
        sinchClient.start();
        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
    }


    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, com.sinch.android.rtc.calling.Call incomingCall) {
            onCommingCall call_Obj= new onCommingCall();
            call_Obj.setMyCall(incomingCall);

           // Toast.makeText(getApplicationContext(), "incomming call" , Toast.LENGTH_SHORT).show();

            KeyguardManager myKM = (KeyguardManager) StartSinch.this.getSystemService(Context.KEYGUARD_SERVICE);
            if( myKM.inKeyguardRestrictedInputMode()) {
                //it is locked
                //addNotfi();
                Intent i= new Intent(StartSinch.this, IncommingCallActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            } else {
                //it is not locked
              //  addNotfi();
                Intent i= new Intent(StartSinch.this, IncommingCallActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }


        }
    }


  /* public void addNotfi(){

       PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
       PowerManager.WakeLock wakeLock = pm.newWakeLock(
               (PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK |
                       PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
       wakeLock.acquire();

       NotificationManager mNotificationManager = (NotificationManager)
               getSystemService(Context.NOTIFICATION_SERVICE);
       Intent notificationIntent = new Intent(this, IncommingCallActivity.class);

       PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
               notificationIntent, PendingIntent.FLAG_ONE_SHOT);

       NotificationCompat.Builder mBuilder =
               new NotificationCompat.Builder(this)
                       .setSmallIcon(R.drawable.logo)
                       .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                       .setContentTitle("Incomming Call")
                       .setDefaults(Notification.DEFAULT_SOUND)
                       .setStyle(new NotificationCompat.BigTextStyle()
                               .bigText("Incomming Call"))
                       .setContentText("Incomming Call")
                       .setAutoCancel(true)
                       .setPriority(Notification.PRIORITY_MAX);

       mBuilder.setContentIntent(contentIntent);
       mNotificationManager.notify(0, mBuilder.build());
   }*/

}
