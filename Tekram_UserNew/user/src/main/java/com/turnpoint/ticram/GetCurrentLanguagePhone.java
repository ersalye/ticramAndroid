package com.turnpoint.ticram;

import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

/**
 * Created by marina on 10/06/2018.
 */

public class GetCurrentLanguagePhone {

    public String lang;

    public GetCurrentLanguagePhone() {

    }

    public String getLang() {

        if (Locale.getDefault().getLanguage().equals("ar")) {
           lang="ara";
        } else if (!Locale.getDefault().getLanguage().equals("ar")) {
            lang= "eng";

        }

        return lang;
    }
}