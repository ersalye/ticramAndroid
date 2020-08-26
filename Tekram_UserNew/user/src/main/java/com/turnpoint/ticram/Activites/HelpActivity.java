package com.turnpoint.ticram.Activites;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.webkit.WebView;

import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.R;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        WebView view = new WebView(this);
        view.getSettings().setJavaScriptEnabled(true);
       // view.loadUrl("http://test.ticram.com/page/api/help-mob?local="+ new GetCurrentLanguagePhone().getLang());
        view.loadUrl(new MySharedPreference(getApplicationContext())
                .getStringShared("base_url") +"page/api/help-mob?local="+ new GetCurrentLanguagePhone().getLang());

        setContentView(view);
    }

}
