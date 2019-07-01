package com.cloudsinc.soulmobile.nativechatapplication.datamodels;

import android.net.Uri;
import org.json.JSONObject;
/**
 * Created by developers on 21/2/18.
 */

public class ChatListModel {

    private JSONObject json_messages = new JSONObject();

    public JSONObject getJsonMessage(){
        return this.json_messages;
    }
     //private JSONObject lObj;
    int viewType;
    private String msg;
    private String time;
    private String status;

    public String getSender_name() {
        return sender_name;
    }

    private String sender_name;
    private boolean isSelected = false;

    public boolean isSend_flag() {
        return send_flag;
    }

    public void setSend_flag(boolean send_flag) {
        this.send_flag = send_flag;
    }

    private boolean send_flag = false;
    private String img_url;
    private boolean isMedia;
    private boolean isImageUrl;

    public boolean isImageUrl() {
        return isImageUrl;
    }

    public void setImageUrl(boolean imageUrl) {
        isImageUrl = imageUrl;
    }

    public ChatListModel() {
    }

    public ChatListModel(int viewType, String msg, String time, String status, String img_url, boolean isMedia, JSONObject json_messages, boolean send_flag,String sender_name) {
        this.viewType = viewType;
        this.msg = msg;
        this.time = time;
        this.status = status;
        this.json_messages = json_messages;
        this.img_url = img_url;
        this.isMedia = isMedia;
        this.send_flag = send_flag;
        this.sender_name = sender_name;
    }

    public int getViewType() {
        return viewType;
    }

    public String getMsg() {
        return msg;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public String getImg_url() {
        return img_url;
    }

    public boolean isMedia() {
        return isMedia;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
