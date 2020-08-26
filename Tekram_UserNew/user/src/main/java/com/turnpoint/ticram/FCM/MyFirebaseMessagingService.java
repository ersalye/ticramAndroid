package com.turnpoint.ticram.FCM;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.turnpoint.ticram.Activites.PaymentAndReview;
import com.turnpoint.ticram.Activites.ShowNotificationContent;
import com.turnpoint.ticram.Activites.SplashActivity;
import com.turnpoint.ticram.Activites.TripDetails;
import com.turnpoint.ticram.Activites.ViewTicket;
import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.PathUrl;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.Volley.IResult;
import com.turnpoint.ticram.Volley.VolleyService;
import com.turnpoint.ticram.modules.user_info_splash;
import com.turnpoint.ticram.modules.user_info_splash2;

import java.util.Hashtable;
import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    IResult iresult;
    VolleyService voly_ser;
    public  ProgressDialog loading;
     @SuppressWarnings("unused")
    public MyFirebaseMessagingService() {}

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        callBackVolly();
        if (remoteMessage.getData().size() > 0) {
              if(new MySharedPreference(this).getStringShared("login_status").equals("login")) {
                  //Toast.makeText(this,"notf_recieved" , Toast.LENGTH_SHORT).show();
                  String title = remoteMessage.getData().get("title");
                  String body_ = remoteMessage.getData().get("body");
                  String id = remoteMessage.getData().get("id");   // order_id
                  String notf_type = remoteMessage.getData().get("type");

                  if (remoteMessage.getData().get("type").equals("ACCEPT_ORDER")) {  //when driver accept the order
                      addNotification(body_);
                      //finish mapsActivity
                      Intent intent = new Intent();
                      intent.putExtra("extra", body_);
                      intent.setAction("com.turnpoint.ticram.FCM.fiinshMapActivity");
                      sendBroadcast(intent);

                      // in case app is not on screen --when app resumed i want to update ui --MapActivity onresume
                   //   new MySharedPreference(getApplicationContext()).setStringShared("order_accepted_onresume" , "yes");

                      //open TripDetails Act
                      Intent in = new Intent(this, TripDetails.class);
                      in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                      startActivity(in);

                  }


                  if (remoteMessage.getData().get("type").equals("CAPTAIN_ARRIVED")) {  // captain arrived

                      new MySharedPreference(getApplicationContext()).setStringShared("status_onresume" , "CAPTAIN_ARRIVED");
                      addNotification(body_);
                      Intent intent = new Intent();
                      intent.putExtra("extra", "وصل الكابتن");
                      intent.putExtra("what_todo", "driver_arrived");
                      intent.setAction("com.turnpoint.ticram.FCM.onMessageReceived");
                      sendBroadcast(intent);
                  }
                  if (remoteMessage.getData().get("type").equals("START_TRIP")) { // here we update the UI

                      new MySharedPreference(getApplicationContext()).setStringShared("status_onresume" , "START_TRIP");

                      //update the view
                      addNotification(body_);
                      Intent intent = new Intent();
                      intent.putExtra("extra", body_);
                      intent.putExtra("what_todo", "start_trip");
                      intent.setAction("com.turnpoint.ticram.FCM.onMessageReceived");
                      sendBroadcast(intent);
                  }

                  if (remoteMessage.getData().get("type").equals("END_TRIP")) {

                      new MySharedPreference(getApplicationContext()).setStringShared("status_onresume" , "END_TRIP");

                      //finish mapsActivity
                      Intent intent = new Intent();
                      intent.putExtra("extra", body_);
                      intent.putExtra("what_todo", "finish");
                      intent.setAction("com.turnpoint.ticram.FCM.onMessageReceived");
                      sendBroadcast(intent);

                      //open paymentAndreview activity
                      addNotificationEndTrip(body_);
                      Intent in = new Intent(this, PaymentAndReview.class);
                      in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                      startActivity(in);


                  }
                  if (remoteMessage.getData().get("type").equals("PAYMENT")) {
                      addNotificationEndTrip(body_);
                  }


                  if (remoteMessage.getData().get("type").equals("ORDER_CANCELLED")) {
                      addNotification(body_);
                      Intent intent = new Intent();
                      intent.putExtra("extra", body_);
                      intent.putExtra("what_todo", "ORDER_CANCELLED");
                      intent.setAction("com.turnpoint.ticram.FCM.onMessageReceived");
                      sendBroadcast(intent);

                  }

                  if (remoteMessage.getData().get("type").equals("INCOMINGCALL")) {   // incomming call
                      addNotificationIncommingcall(body_);
                  }

                  if (remoteMessage.getData().get("type").equals("ADD_TO_BALANCE")) {   // add balance from admin
                      addNotification(body_);
                      Volley_go();
                  }


                  if (remoteMessage.getData().get("type").equals("ACCOUNT_LOCKED")) {   // ?
                      addNotificationNew(body_);
                      new MySharedPreference(getApplicationContext()).setStringShared("text_notfi", body_);
                  }

                  if (remoteMessage.getData().get("type").equals("NEWS")) {   // ?
                      addNotificationNew(body_);
                      new MySharedPreference(getApplicationContext()).setStringShared("text_notfi", body_);

                  }


                  /////////////// for tickets////////////////
                  if (remoteMessage.getData().get("type").equals("TICKET_REPLY")) {
                      addNotificationTicket(title ,body_, id);
                  }

                  if (remoteMessage.getData().get("type").equals("TICKET_CLOSED")) {
                      addNotificationTicket(title ,body_, id);
                  }
                  ////////////////// //////////////////////// ////////////////////
              }


              else if (!new MySharedPreference(this).getStringShared("login_status").equals("login")){   // user logged out
                  String body_ = remoteMessage.getData().get("body");
                  if (remoteMessage.getData().get("type").equals("ACCOUNT_LOCKED")) {   // ?
                      addNotificationNew(body_);
                      new MySharedPreference(getApplicationContext()).setStringShared("text_notfi", body_);
                  }

                  if (remoteMessage.getData().get("type").equals("NEWS")) {   // ?
                      addNotificationNew(body_);
                      new MySharedPreference(getApplicationContext()).setStringShared("text_notfi", body_);
                  }
              }




        }
    }





    private void addNotificationTicket(String title , String body, String id) {
        String idChannel = "my_channel_01";
        NotificationChannel mChannel = null;
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, ViewTicket.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.putExtra("ticket_id", id);
        PendingIntent contentIntent =
                PendingIntent.getActivity(this, 0,
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri notf_sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Ticram")
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(notf_sound)
                        .setContentIntent(contentIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(idChannel, getApplicationContext().getString(R.string.app_name), importance);
            mNotificationManager.createNotificationChannel(mChannel);
        } else {

        }

        builder.setChannelId(idChannel);
        mNotificationManager.notify(0, builder.build());
    }




    private void addNotification(String title) {
        String idChannel = "my_channel_01";
        NotificationChannel mChannel = null;
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, SplashActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri notf_sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Ticram")
                        .setContentText(title)
                        .setAutoCancel(true)
                        .setSound(notf_sound)
                        .setContentIntent(contentIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(idChannel, getApplicationContext().getString(R.string.app_name), importance);
            mNotificationManager.createNotificationChannel(mChannel);
        } else {

        }

        builder.setChannelId(idChannel);
        mNotificationManager.notify(0, builder.build());
    }






    private void addNotificationNew(String title) {
        String idChannel = "my_channel_01";
        NotificationChannel mChannel = null;
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this, ShowNotificationContent.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri notf_sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Ticram")
                        .setContentText(title)
                        .setAutoCancel(true)
                        .setSound(notf_sound)
                        .setContentIntent(contentIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(idChannel, getApplicationContext().getString(R.string.app_name), importance);
            mNotificationManager.createNotificationChannel(mChannel);
        } else {

        }

        builder.setChannelId(idChannel);
        mNotificationManager.notify(0, builder.build());
    }










    private void addNotificationEndTrip(String title) {
      String idChannel = "my_channel_01";
        NotificationChannel mChannel = null;
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this, PaymentAndReview.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri notf_sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Ticram")
                        .setContentText(title)
                        .setAutoCancel(true)
                        .setSound(notf_sound)
                        .setContentIntent(contentIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(idChannel, getApplicationContext().getString(R.string.app_name), importance);
            mNotificationManager.createNotificationChannel(mChannel);
        } else {

        }

        builder.setChannelId(idChannel);
        mNotificationManager.notify(0, builder.build());


    }









    private void addNotificationIncommingcall(String title) {
        String idChannel = "my_channel_01";
        NotificationChannel mChannel = null;
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this, SplashActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri notf_sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Ticram")
                        .setContentText(title)
                        .setAutoCancel(true)
                        .setSound(notf_sound)
                        .setContentIntent(contentIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(idChannel, getApplicationContext().getString(R.string.app_name), importance);
            mNotificationManager.createNotificationChannel(mChannel);
        } else {

        }

        builder.setChannelId(idChannel);
        mNotificationManager.notify(0, builder.build());
    }







    private void Volley_go(){

       // loading = ProgressDialog.show(this, "", getResources().getString(R.string.please_wait), false, false);
        Map<String, String> params = new Hashtable<String, String>();
        params.put("access_token", new MySharedPreference(this).getStringShared("access_token"));
        params.put("local", new GetCurrentLanguagePhone().getLang());
        params.put("user_id", new MySharedPreference(getApplicationContext()).getStringShared("user_id"));
        voly_ser = new VolleyService(iresult, this);
        voly_ser.postDataVolley(new MySharedPreference(getApplicationContext())
                .getStringShared("base_url")+PathUrl.USERInfo, params);
    }




    void callBackVolly(){

        iresult= new IResult() {
            @Override
            public void notifySuccessPost(String response) {
             //   loading.dismiss();
                Gson gson= new Gson();
                user_info_splash res = gson.fromJson(response, user_info_splash.class);
                // Toast.makeText(AddNameAndPicture.this, response, Toast.LENGTH_LONG).show();
                if(res.getHandle().equals("10")){   //login completed
                   // Toast.makeText(getApplicationContext(),res.getMsg(), Toast.LENGTH_LONG).show();
                    user_info_splash2 user_info1=res.getUser();
                    new MySharedPreference(getApplicationContext()).setStringShared("balance",user_info1.getBalance());

                }
                else if(res.getHandle().equals("01")) {  // login faild
                    Toast.makeText(getApplicationContext(), res.getMsg() , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(),R.string.check_internet, Toast.LENGTH_LONG).show();

                // Toast.makeText(AddNameAndPicture.this,"error android Volly"+ error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        };
    }




    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Refreshed token", "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }
    public void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        // Toast.makeText(getApplicationContext(),token, Toast.LENGTH_LONG).show();

    }
}