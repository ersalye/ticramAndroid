package com.turnpoint.ticram.tekram_driver;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by marina on 11/06/2018.
 */

public class CustomRunnable implements Runnable {
    public int millisUntilFinished = 0;
    public TextView holder;
    Handler handler;
    LinearLayout linaer_item;
   // ImageView imageView;

    public CustomRunnable(Handler handler,TextView holder,LinearLayout lin,int millisUntilFinished ) {
        this.handler = handler;
        this.holder = holder;
        this.millisUntilFinished = millisUntilFinished;
        this.linaer_item=lin;
    }

    @Override
    public void run() {
        int seconds = millisUntilFinished ;
        String time = String.valueOf(seconds % 60) ;
        holder.setText(time);
        millisUntilFinished -= 1;

        if(millisUntilFinished==0){
            linaer_item.setVisibility(View.GONE);
        }
        handler.postDelayed(this, 1000);
    }

}
