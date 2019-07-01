package com.cloudsinc.soulmobile.nativechatapplication.datamodels;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by developers on 7/5/18.
 */

public class User {
    private JSONObject contactObj;
    private String cNm,cMail,cUrl;
    private int cmlAcceptedStatus=0;
    private boolean ischeckSelected;

    private static String searchUser = "";


    public User(JSONObject contactObj) {
        try {
            this.contactObj = contactObj;
            if (contactObj.has("USM_FIRST_NAME") && contactObj.has("USM_LAST_NAME"))
                this.cNm = contactObj.getString("USM_FIRST_NAME")+" "+contactObj.getString("USM_LAST_NAME");
            if (contactObj.has("USM_EMAIL"))
                this.cMail = contactObj.getString("USM_EMAIL");
            this.cUrl = "http://keenthemes.com/preview/metronic/theme/assets/pages/media/profile/profile_user.jpg";

            if (contactObj.has("CML_TITLE"))
                this.cNm = contactObj.getString("CML_TITLE");

            if (contactObj.has("CML_OFFICIAL_EMAIL"))
                this.cMail = contactObj.getString("CML_OFFICIAL_EMAIL");
            this.cUrl = "http://keenthemes.com/preview/metronic/theme/assets/pages/media/profile/profile_user.jpg";

         }

        catch (JSONException e) {
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

    public int getCmlAcceptedStatus() {
        return cmlAcceptedStatus;
    }

    public void setCmlAcceptedStatus(int cmlAcceptedStatus) {
        this.cmlAcceptedStatus = cmlAcceptedStatus;
    }


    public static String getSearchUser() {
        return searchUser;
    }

    public static void setSearchUser(String searchUser) {
        User.searchUser = searchUser;
    }

    /*************  Flag for select Recyceler Item ********************/
    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isIscheckSelected() {
        return ischeckSelected;
    }

    public void setIscheckSelected(boolean ischeckSelected) {
        this.ischeckSelected = ischeckSelected;
    }
}