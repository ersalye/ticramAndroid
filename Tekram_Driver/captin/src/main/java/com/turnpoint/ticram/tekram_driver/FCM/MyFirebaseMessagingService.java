package com.turnpoint.ticram.tekram_driver.FCM;


import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.turnpoint.ticram.tekram_driver.Activites.MapsMain;
import com.turnpoint.ticram.tekram_driver.Activites.ShowNotficationLoggedOut;
import com.turnpoint.ticram.tekram_driver.Activites.SplashActivity;
import com.turnpoint.ticram.tekram_driver.MySharedPreference;
import com.turnpoint.ticram.tekram_driver.PathUrl;
import com.turnpoint.ticram.tekram_driver.R;
import com.turnpoint.ticram.tekram_driver.Services.FloatWidgetCancel;
import com.turnpoint.ticram.tekram_driver.Services.LocationServiceBeforeTawaklna;
import com.turnpoint.ticram.tekram_driver.Services.WidgetNewOrder;
import com.turnpoint.ticram.tekram_driver.Volley.IResult;
import com.turnpoint.ticram.tekram_driver.Volley.VolleyService;
import com.turnpoint.ticram.tekram_driver.modules.Check;
import com.turnpoint.ticram.tekram_driver.modules.check_order;
import io.paperdb.Paper;

import java.util.Hashtable;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    IResult iresult;
    VolleyService voly_ser;
    public ProgressDialog loading;
    String app_back_fore;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        new MySharedPreference(this).setStringShared("FirebaseMessagingToken", s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        System.out.println("shtayyat => onMessageReceived "+remoteMessage.getData());
        callBackVolly();
        if (remoteMessage.getData().size() > 0) {
            if (new MySharedPreference(this).getStringShared("login_status").equals("login")) {
                //Toast.makeText(this,"notf_recieved" , Toast.LENGTH_SHORT).show();
                String title = remoteMessage.getData().get("title");
                String body_ = remoteMessage.getData().get("body");
                //String order_id = remoteMessage.getData().get("id");   // order_id
                String notf_type = remoteMessage.getData().get("type");
                String order_count_down = remoteMessage.getData().get("order_count_down");
                // Log.d("nof_infoooooo", remoteMessage.getData().toString());
                // Toast.makeText(this, notf_type, Toast.LENGTH_SHORT).show();

                if (remoteMessage.getData().get("type").equals("ORDER_CANCELLED")) {  //order canceld from the user
                    addNotificationSplash(body_);
                    app_back_fore = new MySharedPreference(getApplicationContext()).getStringShared("ViewDetailsOrder_act");
                    if (app_back_fore.equals("background")) {
                        //   startService(new Intent(getApplicationContext(), FloatingViewService.class));
                        startService(new Intent(getApplicationContext(), FloatWidgetCancel.class));
                    } else if (app_back_fore.equals("foreground")) {
                        Intent intent = new Intent();
                        intent.putExtra("extra", body_);
                        intent.putExtra("what_todo", "ORDER_CANCELLED");
                        intent.setAction("com.turnpoint.ticram.tekram_driver.FCM.onMessageReceived");
                        sendBroadcast(intent);
                    }
                }


                if (remoteMessage.getData().get("type").equals("ORDER_CANCELLED_BEFORE_ACCEPT")) {  //order canceld from the user
                    Intent i = new Intent(getApplicationContext(), WidgetNewOrder.class);
                    stopService(i);
                }

                if (remoteMessage.getData().get("type").equals("PAYMENT")) {  // payment done!
                    addNotificationSplash(body_);
                }

                if (remoteMessage.getData().get("type").equals("UNAVAILABLE")) {  // did not take an order
                    new MySharedPreference(getApplicationContext()).setStringShared("available", "off");
                    addNotificationSplash(body_);
                      /*Intent intent = new Intent();
                      intent.setAction("com.turnpoint.ticram.tekram_driver.UpdateAvailable");
                      sendBroadcast(intent);*/
                }


                if (remoteMessage.getData().get("type").equals("ORDER")) {   // recieve new order
                    addNotification(body_);
               /*    app_back_fore= new MySharedPreference(getApplicationContext()).getStringShared("MapsMain_act");
                     if(app_back_fore.equals("background")) {
                         Intent serviceIntent = new Intent(getApplicationContext(), WidgetNewOrder.class);
                         serviceIntent.putExtra("order_id", remoteMessage.getData().get("order_id"));
                         serviceIntent.putExtra("time_to_user", remoteMessage.getData().get("time_to_user"));
                         serviceIntent.putExtra("distance_to_user", remoteMessage.getData().get("distance_to_user"));
                         serviceIntent.putExtra("user_name", remoteMessage.getData().get("user_name"));
                         serviceIntent.putExtra("user_photo", remoteMessage.getData().get("user_photo"));
                         serviceIntent.putExtra("user_rate", remoteMessage.getData().get("user_rate"));
                         serviceIntent.putExtra("location_text", remoteMessage.getData().get("location_text"));
                         serviceIntent.putExtra("destination_text", remoteMessage.getData().get("destination_text"));
                         startService(serviceIntent);
                     }
                     else if(app_back_fore.equals("foreground")){
                           Intent in = new Intent(this, MapsMain.class);
                          in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                          startActivity(in);
                     }*/
                    try {
                        //  loading = ProgressDialog.show(MapsMain.this, "", "الرجاء الانتظار...", false, false);
                        Map<String, String> params = new Hashtable<String, String>();
                        params.put("local", "ara");
                        params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
                        params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
                        params.put("order_id", "" + remoteMessage.getData().get("order_id"));
                        voly_ser = new VolleyService(new IResult() {
                            @Override
                            public void notifySuccessPost(String response) {
                                System.out.println(response);
                            }

                            @Override
                            public void notifyError(VolleyError error) {
                                System.out.println(error.getMessage());
                            }
                        }, getApplicationContext());
                        voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                                .getStringShared("base_url") + PathUrl.receivedNotification, params);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (isMyServiceRunning(WidgetNewOrder.class) == false  && !Paper.book().read("lastAccept", "").equalsIgnoreCase(""+ remoteMessage.getData().get("order_id"))) {
                        try {

                            Intent serviceIntent = new Intent(getApplicationContext(), WidgetNewOrder.class);
                            serviceIntent.putExtra("order_id", "" + remoteMessage.getData().get("order_id"));
                            serviceIntent.putExtra("time_to_user", remoteMessage.getData().get("time_to_user"));
                            serviceIntent.putExtra("distance_to_user", remoteMessage.getData().get("distance_to_user"));
                            serviceIntent.putExtra("user_name", remoteMessage.getData().get("user_name"));
                            serviceIntent.putExtra("user_photo", remoteMessage.getData().get("user_photo"));
                            serviceIntent.putExtra("user_rate", "" + remoteMessage.getData().get("user_rate"));
                            serviceIntent.putExtra("location_text", remoteMessage.getData().get("location_text"));
                            serviceIntent.putExtra("destination_text", remoteMessage.getData().get("destination_text"));
                            serviceIntent.putExtra("order_info", remoteMessage.getData().get("text_info"));
                            serviceIntent.putExtra("taxi", ""+ remoteMessage.getData().get("taxi"));
                            serviceIntent.putExtra("order_count_down", ""+ remoteMessage.getData().get("order_count_down"));
                            startService(serviceIntent);

                        } catch (Exception ex) {
                        }
                    }


                }

                if (remoteMessage.getData().get("type").equals("UPDATE_ORDER_LIST")) {   // recieve new order
                    //  addNotification(body_);

                }

                if (remoteMessage.getData().get("type").equals("INCOMINGCALL")) {   // INCOMINGCALL
                    // addNotificationIncommingCall(title);
                }

                if (remoteMessage.getData().get("type").equals("ADD_TO_BALANCE")) {   // add balance from admin
                    addNotification(body_);
                    Volley_go();
                }


                if (remoteMessage.getData().get("type").equals("NEWS")) {   // general notfi from admin
                    addNotificationNew(body_);
                    //new MySharedPreference(getApplicationContext()).setStringShared("text_notfi", body_);
                }

                if (remoteMessage.getData().get("type").equals("ACCOUNT_LOCKED")) {   // ?
                    addNotificationNew(body_);
                    //new MySharedPreference(getApplicationContext()).setStringShared("text_notfi", body_);
                }

                if (remoteMessage.getData().get("type").equals("RESTART_GPS")) {   // restart gps
                    //addNotificationNew("djjd");
                    //new MySharedPreference(getApplicationContext()).setStringShared("text_notfi", body_);
                    if (isMyServiceRunning(LocationServiceBeforeTawaklna.class) == false) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            getApplicationContext().startForegroundService(new Intent(getApplicationContext(),
                                    LocationServiceBeforeTawaklna.class));
                        } else {
                            startService(new Intent(getApplicationContext(),
                                    LocationServiceBeforeTawaklna.class));
                        }
                    } else if (isMyServiceRunning(LocationServiceBeforeTawaklna.class)) {
                        // Toast.makeText(getApplicationContext(),"SERVICE IS ON!!" , Toast.LENGTH_SHORT).show();
                    }

                }

            } else if (!new MySharedPreference(this).getStringShared("login_status").equals("login")) {   // user logged out
                String body_ = remoteMessage.getData().get("body");
                if (remoteMessage.getData().get("type").equals("NEWS")) {   // general notfi from admin
                    addNotificationNew(body_);
                    //   new MySharedPreference(getApplicationContext()).setStringShared("text_notfi", body_);
                }

                if (remoteMessage.getData().get("type").equals("ACCOUNT_LOCKED")) {   // ?
                    addNotificationNew(body_);
                    //    new MySharedPreference(getApplicationContext()).setStringShared("text_notfi", body_);
                }
            }

        }
    }


    private void addNotification(String title) {

        String idChannel = "channel_notification";
        NotificationChannel mChannel = null;
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationManager mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, MapsMain.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


       /* Intent notificationIntentOpen = new Intent(this, MyOrders.class);
        notificationIntentOpen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent_open = PendingIntent.getActivity(this, 0,
                notificationIntentOpen, PendingIntent.FLAG_ONE_SHOT);*/

        // Uri notf_sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Ticram")
                        .setContentText(title)
                        .setAutoCancel(true)
                        //.addAction(R.drawable.ic_open, "Open", contentIntent_open)
                        .setContentIntent(contentIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName()
                    + "/" + R.raw.in_a_hurryyy);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            mChannel = new NotificationChannel(idChannel, getApplicationContext().
                    getString(R.string.app_name), importance);
            mChannel.setSound(sound, attributes);
            mNotificationManager.createNotificationChannel(mChannel);
            //playRingtone();
        } else {
            builder.setSound(Uri.parse("android.resource://"
                    + getApplicationContext().getPackageName() + "/" + R.raw.in_a_hurryyy));

        }

        builder.setChannelId(idChannel);
        mNotificationManager.notify(0, builder.build());
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addNotificationNew(String title) {

        String idChannel = "channel_notification";
        NotificationChannel mChannel = null;
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        new MySharedPreference(getApplicationContext()).setStringShared("text_notfi", title);
        Intent notificationIntent = new Intent(this, ShowNotficationLoggedOut.class);
        notificationIntent.putExtra("text", title);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri notf_sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Ticram")
                        .setContentText(title)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName()
                    + "/" + R.raw.in_a_hurryyy);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            mChannel = new NotificationChannel(idChannel, getApplicationContext().
                    getString(R.string.app_name), importance);
            mChannel.setSound(sound, attributes);
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            builder.setSound(Uri.parse("android.resource://"
                    + getApplicationContext().getPackageName() + "/" + R.raw.in_a_hurryyy));
        }

        builder.setChannelId(idChannel);
        mNotificationManager.notify(0, builder.build());
    }


    private void addNotificationSplash(String title) {

        String idChannel = "channel_notification";
        NotificationChannel mChannel = null;
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationManager mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, SplashActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Uri notf_sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Ticram")
                        .setContentText(title)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName()
                    + "/" + R.raw.in_a_hurryyy);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            mChannel = new NotificationChannel(idChannel, getApplicationContext().
                    getString(R.string.app_name), importance);
            mChannel.setSound(sound, attributes);
            mNotificationManager.createNotificationChannel(mChannel);
            //playRingtone();
        } else {
            builder.setSound(Uri.parse("android.resource://"
                    + getApplicationContext().getPackageName() + "/" + R.raw.in_a_hurryyy));

        }

        builder.setChannelId(idChannel);
        mNotificationManager.notify(0, builder.build());
    }


    public void Volley_go() {

        // loading = ProgressDialog.show(this, "", "", false, false);
        Map<String, String> params = new Hashtable<String, String>();
        params.put("access_token", new MySharedPreference(getApplicationContext()).getStringShared("access_token"));
        params.put("local", "ara");
        params.put("driver_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
        voly_ser = new VolleyService(iresult, getApplicationContext());
        voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                .getStringShared("base_url") + PathUrl.TransportInfo, params);
    }


    void callBackVolly() {
        iresult = new IResult() {
            @Override
            public void notifySuccessPost(String response) {
                // loading.dismiss();
                // Toast.makeText(SplashActivity.this, response, Toast.LENGTH_LONG).show();
                Gson gson = new Gson();
                Check res = gson.fromJson(response, Check.class);
                if (res.getHandle().equals("02")) {  // account not found
                    Toast.makeText(getApplicationContext(), res.getMsg(), Toast.LENGTH_LONG).show();
                    // Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    //  startActivity(intent);

                } else if (res.getHandle().equals("10")) {   // account found
                    // Toast.makeText(getApplicationContext(), res.getMsg(), Toast.LENGTH_LONG).show();
                    check_order u = res.getTransport();
                    new MySharedPreference(getApplicationContext()).setStringShared("balance", u.getBalance());
                } else {
                    Toast.makeText(getApplicationContext(), res.getMsg(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void notifyError(VolleyError error) {
                // loading.dismiss();
                Toast.makeText(getApplicationContext(), " مشكلة بالاتصال بالانترنت!", Toast.LENGTH_LONG).show();
                // Toast.makeText(SplashActivity.this, "error android Volly" + error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}