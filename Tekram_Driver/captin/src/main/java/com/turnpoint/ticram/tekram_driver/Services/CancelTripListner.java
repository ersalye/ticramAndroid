package com.turnpoint.ticram.tekram_driver.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.turnpoint.ticram.tekram_driver.MySharedPreference;
import com.turnpoint.ticram.tekram_driver.R;
import com.turnpoint.ticram.tekram_driver.modules.addOrderFirebase;

public class CancelTripListner extends Service {

    DatabaseReference connectedRef;
    FirebaseDatabase database;
    private ValueEventListener mListener;


    public CancelTripListner() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        //Toast.makeText(this,"onStartCancelListner",Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        }

        tracking_firebase();

        return START_STICKY;
    }



    public void tracking_firebase(){
         database = FirebaseDatabase.getInstance();
         connectedRef = database.getReference("tikram-192101");
         mListener= connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("orders")
                        .child(new MySharedPreference(getApplicationContext()).
                                getStringShared("user_id")).
                        addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Log.d("sosoo",  dataSnapshot.toString());
                                if(dataSnapshot.exists()) {
                                    addOrderFirebase values = dataSnapshot.getValue(addOrderFirebase.class);
                                    if(values.status.equals("C")){
                                      //  Toast.makeText(getApplicationContext(), values.status, Toast.LENGTH_LONG).show();
                                        //startService(new Intent(getApplicationContext(), FloatWidgetCancel.class));
                                    }
                                }

                            }
                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                if(dataSnapshot.exists()) {
                                    addOrderFirebase values = dataSnapshot.getValue(addOrderFirebase.class);
                                    if(values.status.equals("C")){
                                        //Toast.makeText(getApplicationContext(), values.status, Toast.LENGTH_LONG).show();
                                        startService(new Intent(getApplicationContext(), FloatWidgetCancel.class));
                                        addOrderFirebase order_ =new addOrderFirebase
                                                (Long.valueOf(new MySharedPreference(getApplicationContext()).
                                                getStringShared("cur_order_id")),"F") ;
                                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                        mDatabase.child("orders").child(new MySharedPreference(getApplicationContext()).
                                                getStringShared("user_id")).child("order").setValue(order_);
                                    }
                                }
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //System.err.println("Listener was cancelled at .info/connected");
            }
        });

    }







    @Override
    public void onCreate() {
        Log.e("onCreate", "onCreateAfterCancel");
        super.onCreate();
      //  Toast.makeText(this,"onCreateCancelListner",Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        }

        tracking_firebase();
    }


    private void startMyOwnForeground()
    {

      /*  Notification notification = new Notification();
        startForeground(1, notification);*/

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            String NOTIFICATION_CHANNEL_ID = "channel_id_22";
            String channelName = "My Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("App is running in background Cancel")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(22, notification);
        }
    }



    @Override
    public void onDestroy() {
        Log.e("onDestroy", "onDestroyCancel");
       // Toast.makeText(getApplicationContext(),"onDestroyCancel", Toast.LENGTH_SHORT ).show();
        super.onDestroy();
        //connectedRef.removeEventListener(mListener);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startMyOwnForeground();
                stopForeground(true); //true will remove notification
                stopSelf();
            }

        } catch (Exception ex) {}
    }

}
