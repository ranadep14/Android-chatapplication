package com.cloudsinc.soulmobile.nativechatapplication.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by developers on 10/5/18.
 */

public class JsonObject extends JSONObject {

    JSONObject jsonObject;
    public JsonObject(JSONObject jsonObject)
    {
        this.jsonObject=jsonObject;
    }
    @Override
    public String getString(String name) throws JSONException {
        String string="";
        if(jsonObject.has(name))string=jsonObject.getString(name);
        return string;
    }

    @Override
    public int getInt(String name) throws JSONException {
        int temp_int=0;
        if(jsonObject.has(name))temp_int=jsonObject.getInt(name);
        return temp_int;
    }

}
