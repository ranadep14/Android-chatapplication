package com.cloudsinc.soulmobile.nativechatapplication.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by developers on 12/4/18.
 */

public class App extends Application {

    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        //TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/fonts/Quicksand-Regular.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
    }

    public static Context getContext() {
        return mContext;
    }
}
