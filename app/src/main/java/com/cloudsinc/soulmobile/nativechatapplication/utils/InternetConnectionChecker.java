package com.cloudsinc.soulmobile.nativechatapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetConnectionChecker{
    private static boolean status=false;
    public static boolean checkInternetConnection(Context context){
        ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        if(networkInfo==null){
            InternetConnectionChecker.status=false;
        } else {
            InternetConnectionChecker.status=true;
        }
        return InternetConnectionChecker.status;
    }
}