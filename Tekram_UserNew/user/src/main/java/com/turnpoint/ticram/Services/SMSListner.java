package com.turnpoint.ticram.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSListner extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        // Toast.makeText(context, msgBody,Toast.LENGTH_LONG).show();

                        if (msgBody.contains("تكرم") || msgBody.contains("ticram") || msgBody.contains("Ticram")) {
                            String[] splited_text = msgBody.split(":");
                            String mycode = splited_text[1];
                            //Toast.makeText(context, mycode,Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent();
                            myIntent.putExtra("code", mycode.trim());
                            myIntent.setAction("com.turnpoint.ticram.checkcode");
                            context.sendBroadcast(myIntent);

                    }

                    }
                }catch(Exception e){
                          // Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }




}
