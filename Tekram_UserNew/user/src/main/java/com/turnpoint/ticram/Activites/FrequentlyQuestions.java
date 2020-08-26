package com.turnpoint.ticram.Activites;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.webkit.WebView;

import com.turnpoint.ticram.GetCurrentLanguagePhone;
import com.turnpoint.ticram.MySharedPreference;
import com.turnpoint.ticram.R;

public class FrequentlyQuestions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequently_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        WebView view = new WebView(this);

       String from_what_act=getIntent().getExtras().getString("to");
        if(from_what_act.equals("questions")) {
            view.getSettings().setJavaScriptEnabled(true);
            //view.loadUrl("https://test.ticram.com/page/api/faq?lang=" + new GetCurrentLanguagePhone().getLang());
            view.loadUrl(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+"page/api/faq?lang=" + new GetCurrentLanguagePhone().getLang());

            setContentView(view);
        }

        else if(from_what_act.equals("about_ticram")){
            view.getSettings().setJavaScriptEnabled(true);
           // view.loadUrl("https://test.ticram.com/page/api/about?lang=" + new GetCurrentLanguagePhone().getLang());
            view.loadUrl(new MySharedPreference(getApplicationContext())
                    .getStringShared("base_url")+"page/api/about?lang=" + new GetCurrentLanguagePhone().getLang());
            setContentView(view);
        }

    }

}
