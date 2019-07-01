package com.cloudsinc.soulmobile.nativechatapplication.datamodels;

import org.json.JSONObject;

/**
 * Created by developers on 25/6/18.
 */

public class GrpMemberModel {

    //private JSONObject lObj;
    private JSONObject json_members = new JSONObject();
    private String cNm,cMail,cUrl,vType;

    public JSONObject getJson_members() {
        return json_members;
    }

    public void setJson_members(JSONObject json_members) {
        this.json_members = json_members;
    }
}

