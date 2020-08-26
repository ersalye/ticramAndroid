package com.turnpoint.ticram.Activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowNotificationContent extends AppCompatActivity {

    String body_msg;

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
        setContentView(R.layout.activity_show_notification_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView tv_text=findViewById(R.id.textView8);


        if (getIntent().getExtras() != null) {
            body_msg=getIntent().getExtras().getString("text");
            tv_text.setText(body_msg);
        }

        else if (getIntent().getExtras() == null) {
            body_msg=new MySharedPreference(getApplicationContext()).getStringShared("text_notfi");
            tv_text.setText(body_msg);
        }
    }


    public void close_act(View v){
        if(new MySharedPreference(this).getStringShared("login_status").equals("login")) {
                startActivity(new Intent(getApplicationContext(),MapActivity.class));
            finish();
        }

        else if (!new MySharedPreference(this).getStringShared("login_status").equals("login")) {   // user logged out
            startActivity(new Intent(getApplicationContext(),LoginPhoneNumber.class));
            finish();
        }

    }

}