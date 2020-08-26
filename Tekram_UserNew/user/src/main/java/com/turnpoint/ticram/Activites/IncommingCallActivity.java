package com.turnpoint.ticram.Activites;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallListener;
import com.turnpoint.ticram.R;
import com.turnpoint.ticram.Sinch.onCommingCall;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class IncommingCallActivity extends AppCompatActivity {

    Call call;

    Context ctx;
    TextView callState;
    ImageView btn_answer;
    //MediaPlayer ring=null;
    private TextView tv_sec, tv_min , tv_hours;
    int sum_sec=0;
    int sum_min=0;
    int sum_hours=0;
    MediaPlayer mPlayer;
    AudioManager audioManager;
    TextView tvv_dots1, tvv_dots2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomming_call);

        //stopRingtone();
        tv_sec = (TextView) findViewById(R.id.textView_sec);
        tv_min = (TextView) findViewById(R.id.textView_min);
        tv_hours = (TextView) findViewById(R.id.textView_hours);
        tvv_dots1=(TextView)findViewById(R.id.tv_dots1);
        tvv_dots2=(TextView)findViewById(R.id.tv_dots2);

        btn_answer=findViewById(R.id.button2);
        callState = (TextView) findViewById(R.id.callState);
        ctx=getApplicationContext();

        tv_sec.setVisibility(View.INVISIBLE);
        tv_min.setVisibility(View.INVISIBLE);
        tv_hours.setVisibility(View.INVISIBLE);
        tvv_dots1.setVisibility(View.INVISIBLE);
        tvv_dots2.setVisibility(View.INVISIBLE);
        audioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);


       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1);

            }
            else if(checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED){
                try {
                    onCommingCall i = new onCommingCall();
                    call = i.getMyCall();
                    call.addCallListener(new SinchCallListener());
                    playRingtone();
                } catch (Exception ex){}

            }
        }

        else  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                onCommingCall i = new onCommingCall();
                call = i.getMyCall();
                call.addCallListener(new SinchCallListener());
                playRingtone();
            } catch (Exception ex){}
        }
*/


    }



    /*@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    try {
                        onCommingCall i = new onCommingCall();
                        call = i.getMyCall();
                        call.addCallListener(new SinchCallListener());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playRingtone();
                            }
                        }, 2000);

                    } catch (Exception ex){}

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    requestPermissions(
                            new String[]{Manifest.permission.RECORD_AUDIO }, 1);
                }
                return;
            }


        }
    }

*/

    public void check_manager_audio_per(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS)
                    != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS}, 2);
            }
        }
        else {
            // Permission has already been granted
            //Permission_audio_granted="yes";
            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    playRingtone();
                }
            }, 2000);*/


        }



    }

    public void answer_call(View v){
        try {
            if (call != null) {
                call.answer();
                btn_answer.setVisibility(View.GONE);
                stopRingtone();
                // add_timer();
            } else if (call == null) {
                onCommingCall i = new onCommingCall();
                call = i.getMyCall();
                call.addCallListener(new SinchCallListener());
                if (call != null) {
                    call.answer();
                    btn_answer.setVisibility(View.GONE);
                    stopRingtone();
                    // add_timer();
                } else if (call == null) {
                    Toast.makeText(getApplicationContext(), "call = null", Toast.LENGTH_SHORT).show();

                }
            }
        } catch (Exception ex){}
    }

    public void hangup_call(View v){
        try {
            if (call != null) {
                call.hangup();
                stopRingtone();
                finish();
            } else if (call == null) {
                onCommingCall i = new onCommingCall();
                call = i.getMyCall();
                call.addCallListener(new SinchCallListener());
                if (call != null) {
                    call.hangup();
                    stopRingtone();
                    finish();
                } else if (call == null) {
                    Toast.makeText(getApplicationContext(), "call = null", Toast.LENGTH_SHORT).show();

                }
            }

        } catch (Exception ex){}

    }


    public void finish_act(){
        finish();

    }




    public void playRingtone() {
        try {
            // Toast.makeText(getApplicationContext(),"Ring", Toast.LENGTH_SHORT).show();
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_RING);
            audioManager.setSpeakerphoneOn(true);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            audioManager.setSpeakerphoneOn(true);

            try {
                mPlayer.setDataSource(ctx,
                        Uri.parse("android.resource://" + ctx.getPackageName() + "/" + R.raw.incoming));
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
                audioManager.setSpeakerphoneOn(false);
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
            }
        } catch (Exception ex){}
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
                        tv_min.setText(String.valueOf(sum_min ));
                        tv_hours.setText(String.valueOf(sum_hours));

                    }
                });

            }
        }, 0, 1000);
    }




    private class SinchCallListener implements CallListener {

        @Override
        public void onCallEnded(Call endedCall) {
            //call=endedCall;
            Toast.makeText(ctx, "call ended .." , Toast.LENGTH_LONG).show();
            stopRingtone();
            //call.hangup();
            call = null;
            callState.setText("call ended");
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);   //to enable set the vloume of the call
            finish();
        }


        @Override
        public void onCallEstablished(Call establishedCall) {
            //Toast.makeText(ctx, "connected .." , Toast.LENGTH_LONG).show();
            callState.setText("connected");
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            audioManager.setSpeakerphoneOn(false);
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            add_timer();
            stopRingtone();
        }


        @Override
        public void onCallProgressing(Call progressingCall) {
            //Toast.makeText(ctx, "ringing .." , Toast.LENGTH_LONG).show();
            //mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
           audioManager.setMode(AudioManager.MODE_IN_CALL);
            audioManager.setSpeakerphoneOn(true);
            callState.setText("ringing");
        }


        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
           // Toast.makeText(ctx, "onShouldSendPushNotification .." , Toast.LENGTH_LONG).show();
            // call.hangup();
            // call=null;
        }
    }



}