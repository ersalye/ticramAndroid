package com.turnpoint.ticram.Sinch;

import com.sinch.android.rtc.calling.Call;


public class onCommingCall {


     static Call incommingCall;

     public onCommingCall(){
     }


     public void setMyCall(Call call){
         this.incommingCall=call;
     }

     public Call getMyCall(){
         return incommingCall;
     }

}
