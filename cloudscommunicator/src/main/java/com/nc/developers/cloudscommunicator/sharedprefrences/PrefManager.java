package com.nc.developers.cloudscommunicator.sharedprefrences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by developers on 29/5/18.
 */

public class PrefManager {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private Context context;
    private static final String PREFNAME = "chatPref";

    public PrefManager(Context context){
        this.context=context;
        PrefManager.sharedPreferences=context.getSharedPreferences(PREFNAME,Context.MODE_PRIVATE);
        PrefManager.editor=PrefManager.sharedPreferences.edit();
    }

    public static void setValue(String value){
        if(PrefManager.editor!=null) {
            PrefManager.editor.putBoolean(value, true);
            PrefManager.editor.commit();
        }
    }

    public static void setValue(String key,String value){
        if(PrefManager.editor!=null){
            PrefManager.editor.putString(key,value);
            PrefManager.editor.commit();
        }
    }

    public static boolean getValue(String value){
        boolean status=false;
        if(PrefManager.sharedPreferences!=null) {
            if (PrefManager.sharedPreferences.contains(value)) {
                status=PrefManager.sharedPreferences.getBoolean(value, false);
            }
        }
        return status;
    }

    public static String getStringValue(String key){
        String str_hoHo="";
        if(PrefManager.sharedPreferences!=null) {
            if (PrefManager.sharedPreferences.contains(key)) {
                str_hoHo=PrefManager.sharedPreferences.getString(key,"");
            }
        }
        return str_hoHo;
    }

    public static void removeValue(String value){
        if(PrefManager.sharedPreferences!=null) {
            if (PrefManager.sharedPreferences.contains(value)) {
                PrefManager.editor.remove(value);
                PrefManager.editor.commit();
            }
        }
    }
}