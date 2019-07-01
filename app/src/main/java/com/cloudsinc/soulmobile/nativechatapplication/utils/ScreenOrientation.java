package com.cloudsinc.soulmobile.nativechatapplication.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Created by developers on 9/8/17.
 */

public class ScreenOrientation {

    public static boolean checkIsTablet(Activity activity) {
        Display display = activity.getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        float width = displayMetrics.widthPixels / displayMetrics.xdpi;
        float height = displayMetrics.heightPixels / displayMetrics.ydpi;

        double screenDiagonal = Math.sqrt( width * width + height * height );
        int inch = (int) (screenDiagonal + 0.5);
        ////Toast.makeText(activity, "inch : "+ inch, Toast.LENGTH_LONG).show();
        return (inch >= 7 );
    }
}