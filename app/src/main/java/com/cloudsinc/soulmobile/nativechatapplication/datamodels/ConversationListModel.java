package com.cloudsinc.soulmobile.nativechatapplication.datamodels;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by developers on 21/2/18.
 */

public class ConversationListModel {


    //private JSONObject lObj;
    private String uNm,uMsg,uMtime,uUrl,vType;
    private boolean isSelected = false;

    public ConversationListModel(String uNm, String uMsg, String uMtime, String uUrl, String vType) {
        this.uNm = uNm;
        this.uMsg = uMsg;
        this.uMtime = uMtime;
        this.uUrl = uUrl;
        this.vType = vType;
    }

    public String getuNm() {
        return uNm;
    }

    public String getuMsg() {
        return uMsg;
    }

    public String getuMtime() {
        return uMtime;
    }

    public String getuUrl() {
        return uUrl;
    }

    public String getvType() {
        return vType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
