package com.turnpoint.ticram.Activites;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallListener;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.Sinch.mySinchClient;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MakeCallSinch extends  AppCompatActivity {

    private Call call;
    private TextView callState, tv_reciverName;
    private SinchClient sinchClient;
    private ImageView button;
    private String callerId;
    private String recipientId;
    private String recipientName;
    String btn_clicked;
    Context ctx;
    private TextView tv_sec, tv_min , tv_hours;
    int sum_sec=0;
    int sum_min=0;
    int sum_hours=0;
    MediaPlayer mPlayer;
    AudioManager audioManager;
    TextView tvv_dots1, tvv_dots2;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Al-Jazeera-Arabic-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_make_call_sinch);

        recipientId=getIntent().getExtras().getString("call_whome");
       // Toast.makeText(getApplicationContext(),recipientId , Toast.LENGTH_SHORT).show();
        if(recipientId ==null){
            Toast.makeText(getApplicationContext(),"reciever id= null " , Toast.LENGTH_SHORT).show();
            finish();
        }
        recipientName=getIntent().getExtras().getString("reciever_name");
        callState = (TextView) findViewById(R.id.callState);
        tv_reciverName=findViewById(R.id.textView_recieverName);
        tv_reciverName.setText(recipientName);
        tv_sec = (TextView) findViewById(R.id.textView_sec);
        tv_min = (TextView) findViewById(R.id.textView_min);
        tv_hours = (TextView) findViewById(R.id.textView_hours);
        tvv_dots1=(TextView)findViewById(R.id.tv_dots1);
        tvv_dots2=(TextView)findViewById(R.id.tv_dots2);

        /*tv_sec.setText("00 :");
        tv_sec.setText("00 : ");
        tv_sec.setText("00");*/
        tv_sec.setVisibility(View.INVISIBLE);
        tv_min.setVisibility(View.INVISIBLE);
        tv_hours.setVisibility(View.INVISIBLE);
        tvv_dots1.setVisibility(View.INVISIBLE);
        tvv_dots2.setVisibility(View.INVISIBLE);
        callerId=new MySharedPreference(getApplicationContext()).getStringShared("mobile_full");
        ctx=MakeCallSinch.this;
        mySinchClient mysinch=new mySinchClient(ctx , callerId);
        sinchClient = mysinch.getSinchClient();
        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();
        btn_clicked="not_clicked";
        // sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
        button =  findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event_call();
            }

        });



       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1);

            }
            else if(checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED){
                // Permission has already been granted
                //Permission_audio_granted="yes";
                event_call();


            }
        }

       else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            event_call();
        }*/

    }




    public void event_call(){
        try {
            if (call == null) {  // theres no call i want to make a call right now
                if (recipientId != null) {
                    button.setImageResource(R.drawable.hangup);
                    playRingtone();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            call = sinchClient.getCallClient().callUser(recipientId);
                            call.addCallListener(new SinchCallListener());
                        }
                    }, 2000);

                } else if (recipientId == null) {
                    Toast.makeText(getApplicationContext(), "نعتذر .لا توجد خدمة حاليا! ",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                call.hangup();
                stopRingtone();
            }
        } catch (Exception ex){}
    }


   /* @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    event_call();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

*/

    public void playRingtone() {
        try {
            audioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_RING);

            try {
                mPlayer.setDataSource(ctx,
                        Uri.parse("android.resource://" + ctx.getPackageName() + "/" + R.raw.ringback));
                mPlayer.prepare();
            } catch (IOException e) {
                //Log.e(LOG_TAG, "Could not setup media player for ringtone");
                mPlayer = null;
                return;
            }
            mPlayer.setLooping(true);
            mPlayer.start();
        } catch (Exception ex){}
    }


    public void stopRingtone() {
        try {
            if (mPlayer != null) {
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
            }
        } catch (Exception ex){}
    }


    private class SinchCallListener implements CallListener {

        @Override
        public void onCallEnded(Call endedCall) {
            //Toast.makeText(ctx, "call ended .." , Toast.LENGTH_LONG).show();
            //  call.hangup();
            call = null;
            callState.setText("call ended");
            stopRingtone();
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);   //to enable set the vloume of the call
            finish();

        }


        @Override
        public void onCallEstablished(Call establishedCall) {
            // Toast.makeText(ctx, "connected .." , Toast.LENGTH_LONG).show();
            callState.setText("connected");
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            add_timer();
           stopRingtone();
        }


        @Override
        public void onCallProgressing(Call progressingCall) {
            //Toast.makeText(ctx, "ringing .." , Toast.LENGTH_LONG).show();
            callState.setText("ringing...");
        }


        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // call.hangup();
            //call=null;
        }
    }



    public void add_timer(){
        tvv_dots1.setVisibility(View.VISIBLE);
        tvv_dots2.setVisibility(View.VISIBLE);
        tv_sec.setVisibility(View.VISIBLE);
        tv_min.setVisibility(View.VISIBLE);
        tv_hours.setVisibility(View.VISIBLE);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        sum_sec=sum_sec+1;
                        if(sum_sec== 60){
                            sum_min=sum_min+1;
                            sum_sec=0;
                        }
                        if(sum_min== 60){
                            sum_hours=sum_hours+1;
                        }
                        //sum_sec=Integer.parseInt(timerValue.getText().toString());
                        tv_sec.setText(String.valueOf(sum_sec));
                        tv_min.setText(String.valueOf(sum_min));
                        tv_hours.setText(String.valueOf(sum_hours));

                    }
                });

            }
        }, 0, 1000);
    }



   /* private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
           // Toast.makeText(ctx, "incoming call .." , Toast.LENGTH_LONG).show();
              *//*      call = incomingCall;
                    call.answer();
                    call.addCallListener(new SinchCallListener());
                    button.setText("Hang Up");*//*

            onCommingCall call_Obj= new onCommingCall();
            call_Obj.setMyCall(incomingCall);

            Intent i= new Intent(ctx, IncommingCallActivity.class);
            startActivity(i);


        }
    }
*/



}