package com.cloudsinc.soulmobile.nativechatapplication.datamodels;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by developers on 9/5/18.
 */

public class ChatGroup {

    private JSONObject group_json;
    JSONObject latestMessage;

    String grpKeyVal="";
    String grpTitle="No Title";
    String lstMsg="";
    String msgTime="2018-01-01T04:30:05.903Z";
    int unreadCount =0;
    int viewType;
    public ChatGroup(JSONObject group_json, JSONObject latestMessage,int viewType) {
        this.group_json = group_json;
        this.latestMessage = latestMessage;
        this.viewType=viewType;

        try {
            if (group_json.has("KEY_VAL"))
                this.grpKeyVal = group_json.getString("KEY_VAL");
            if (group_json.has("CML_TITLE"))
                this.grpTitle = group_json.getString("CML_TITLE");
            //else this.grpTitle = "No Title";
            if (group_json.has("CML_UNREAD_COUNT"))
                this.unreadCount = group_json.getInt("CML_UNREAD_COUNT");
            //else this.grpTitle = "No Title";

            if (latestMessage.has("CML_TITLE"))
                this.lstMsg = latestMessage.getString("CML_TITLE");
            //else this.lstMsg = "";
            if (group_json.has("LAST_MODIFIED_ON"))
              this.msgTime = group_json.getString("LAST_MODIFIED_ON");
            //else this.msgTime="2018-01-01T04:30:05.903Z";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getGrpKeyVal() {
        return grpKeyVal;
    }

    public String getGrpTitle() {
        return grpTitle;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public String getLstMsg() {
        return lstMsg;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public JSONObject getGroup_json() {
        return group_json;
    }

    public JSONObject getLatestMessage() {
        return latestMessage;
    }

    public int getViewType() {
        return viewType;
    }

    /*************  Flag for select Recyceler Item ********************/
    private boolean isSelected = false;
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
