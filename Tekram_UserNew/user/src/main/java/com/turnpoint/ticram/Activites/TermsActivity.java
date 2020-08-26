package com.turnpoint.ticram.Activites;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.webkit.WebView;

import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.R;

public class TermsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        WebView view = new WebView(this);
        view.getSettings().setJavaScriptEnabled(true);
        //view.loadUrl("http://test.ticram.com/page/api/terms?local="+ new GetCurrentLanguagePhone().getLang());
        view.loadUrl(new MySharedPreference(getApplicationContext())
                .getStringShared("base_url")+"page/api/terms?local="+ new GetCurrentLanguagePhone().getLang());


        setContentView(view);
    }

}
