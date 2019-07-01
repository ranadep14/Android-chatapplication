package com.cloudsinc.soulmobile.nativechatapplication.datamodels;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by developers on 21/2/18.
 */

public class RequestListModel {


    private JSONObject lObj;
    private String uNm,uMsg,uTime,uUrl="http://keenthemes.com/preview/metronic/theme/assets/pages/media/profile/profile_user.jpg";
    int vType;
    private boolean isSelected = false;
    public RequestListModel(JSONObject lObj, int vType) {

        try {
            this.lObj=lObj;
            this.uNm = lObj.has("CML_TITLE")?lObj.getString("CML_TITLE"):"";
            this.uMsg = "Wants to connect with you.";

            if (lObj.has("CML_IMAGE_PATH")){
                if (lObj.getJSONArray("CML_IMAGE_PATH").length()>0){
                    this.uUrl = ""+lObj.getJSONArray("CML_IMAGE_PATH").get(0);
                }

            }

           // this.uUrl = "http://cdn2.stylecraze.com/wp-content/uploads/2014/12/184-15-bollywood-stars-without-makeup.jpg";
            this.uTime = lObj.has("LAST_MODIFIED_ON")?lObj.getString("LAST_MODIFIED_ON"):"";
            this.vType = vType;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject getlObj() {
        return lObj;
    }

    public String getuNm() {
        return uNm;
    }

    public String getuMsg() {
        return uMsg;
    }

    public String getuUrl() {
        return uUrl;
    }

    public String getuTime() {
        return uTime;
    }

    public int getvType() {
        return vType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
