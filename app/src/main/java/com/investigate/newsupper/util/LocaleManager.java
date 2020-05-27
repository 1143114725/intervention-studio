package com.investigate.newsupper.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

/**
 * https://proandroiddev.com/change-language-programmatically-at-runtime-on-android-5e6bc15c758
 */
public class LocaleManager {


    public static Context setLocale(Context c) {
        return updateResources(c, Locale.getDefault().getLanguage());
    }



    @SuppressLint("NewApi")
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        //做版本兼容性判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            //点进去看方法详情
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

}