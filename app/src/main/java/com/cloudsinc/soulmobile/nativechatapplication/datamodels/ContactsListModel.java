package com.cloudsinc.soulmobile.nativechatapplication.datamodels;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by developers on 21/2/18.
 */

public class ContactsListModel {


    private JSONObject contactObj;
    private String cNm,cMail,cUrl="http://keenthemes.com/preview/metronic/theme/assets/pages/media/profile/profile_user.jpg";
    private int cAcceptedStatus,vType;
    public ContactsListModel(JSONObject contactObj, int vType) {
        try {
            this.contactObj = contactObj;
            if (contactObj.has("CML_TITLE"))
             this.cNm = contactObj.getString("CML_TITLE");
            if (contactObj.has("CML_OFFICIAL_EMAIL"))
             this.cMail = contactObj.getString("CML_OFFICIAL_EMAIL");
            if (contactObj.has("CML_IMAGE_PATH")){
                if (contactObj.getJSONArray("CML_IMAGE_PATH").length()>0){
                    this.cUrl = ""+contactObj.getJSONArray("CML_IMAGE_PATH").get(0);
                }

               // this.cUrl= "http://keenthemes.com/preview/metronic/theme/assets/pages/media/profile/profile_user.jpg";
            }

            if (contactObj.has("CML_ACCEPTED"))
                this.cAcceptedStatus = contactObj.getInt("CML_ACCEPTED");
            this.vType = vType;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject getContactObj() {
        return contactObj;
    }

    public String getcNm() {
        return cNm;
    }

    public String getcMail() {
        return cMail;
    }

    public String getcUrl() {
        return cUrl;
    }

    public int getcAcceptedStatus() {
        return cAcceptedStatus;
    }

    public int getvType() {
        return vType;
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
