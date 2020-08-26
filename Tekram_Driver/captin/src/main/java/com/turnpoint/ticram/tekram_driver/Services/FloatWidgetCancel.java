package com.turnpoint.ticram.tekram_driver.Services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.turnpoint.ticram.tekram_driver.Activites.SplashActivity;
import com.turnpoint.ticram.tekram_driver.R;

/**
 * Created by marina on 15/08/2018.
 */

public class FloatWidgetCancel extends Service {
    private WindowManager mWindowManager;
    private View mFloatingView;
    WindowManager.LayoutParams params;

    public FloatWidgetCancel() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.cancel_order, null);

        // already asked in splashActivity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }


        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }


        //Specify the view position
        params.gravity = Gravity.NO_GRAVITY | Gravity.NO_GRAVITY;        //Initially view will be added to top-RIGHT corner
       /* params.x = 20;
        params.y = 100;*/

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        //The root element of the collapsed view layout
        final View collapsedView = mFloatingView.findViewById(R.id.collapse_view);

        //Set the close button
        Button closeButtonCollapsed = (Button) mFloatingView.findViewById(R.id.button10);
        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the service and remove the from from the window
                stopService(new Intent(getApplicationContext(), CancelTripListner.class)) ;
                Intent i= new Intent(FloatWidgetCancel.this, SplashActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                stopSelf();
                // collapsedView.setVisibility(View.GONE);
            }
        });

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
      //  Toast.makeText(getApplicationContext(),"onDestroyCancel", Toast.LENGTH_SHORT ).show();
        if (mFloatingView != null)
            mWindowManager.removeView(mFloatingView);
    }
}
