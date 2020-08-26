package com.turnpoint.ticram.tekram_driver.Sinch;

import com.sinch.android.rtc.calling.Call;

/**
 * Created by Server on 9/16/2017.
 */

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
