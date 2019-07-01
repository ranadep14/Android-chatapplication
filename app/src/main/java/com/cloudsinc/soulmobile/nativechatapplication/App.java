package com.cloudsinc.soulmobile.nativechatapplication;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.cloudsinc.soulmobile.nativechatapplication.datamodels.User;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nc.developers.cloudscommunicator.utils.EMailSender;
import com.cloudsinc.soulmobile.nativechatapplication.datamodels.ChatGroup;
import com.cloudsinc.soulmobile.nativechatapplication.utils.SortingClass;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.database.ConstantsClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class App extends Application {

    private static App instance;
    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    public App(){
        instance=this;
        GlobalClass.setContext(instance);
    }

    public static Context getContext(){
        return instance;
    }

    public static App getApplication(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sAnalytics = GoogleAnalytics.getInstance(this);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
            @Override
            public void uncaughtException(Thread thread, Throwable ex){
                handleUncaughtException(thread, ex);
            }
        });
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }

        return sTracker;
    }

    public void handleUncaughtException (Thread thread, Throwable e){
        String stackTrace = Log.getStackTraceString(e);
        String message = e.getMessage();

        EMailSender eMailSender=null;
        try{
            eMailSender=new EMailSender("smtp.gmail.com","qa.mstorm@gmail.com",
                    "Nciportal1","abhijeet.shedsale@clouzersolutions.com,"
                    + "nikhil.vharamble@nciportal.com,"
                    + "ankita.aitawade@clouzersolutions.com,"
                    + "prajakta.patil@clouzersolutions.com",
                    "chat application error details",stackTrace);
        }catch(Exception e2){
            Log.e("expn_mail_send:",e2.toString());
        }
    }

    public static ArrayList<ChatGroup> getConversation(String keyval){
        ArrayList<ChatGroup> grpItem=new ArrayList<>();
        ChatGroup chatGroup=null;
        JSONObject object=null;
        if(object.has("infoArray")){
            try {
                JSONArray array = object.getJSONArray("infoArray");
                if(array.length()>0){
                    JSONObject singleObj=array.getJSONObject(0);
                    if(singleObj.has(ConstantsClass.RESTDATA)
                            && singleObj.has(ConstantsClass.LATEST_MSG_OBJ)){
                        String restString=singleObj.getString(ConstantsClass.RESTDATA);
                        String latestMsgString=singleObj.getString(ConstantsClass.LATEST_MSG_OBJ);
                        JSONObject restObj=new JSONObject(restString);
                        JSONObject latestMsgObj=new JSONObject(latestMsgString);
                        chatGroup=new ChatGroup(restObj,latestMsgObj,0);
                        grpItem.add(chatGroup);
                    }
                }
            }catch (JSONException e){
                Log.e("expn_getConv:",e.toString());
            }
        }
        Log.i("chatGrpObj:",object.toString());
        return grpItem;
    }


    private static JSONObject grpObjForEdit;
    public static JSONObject getGrpObjForEdit() {
        return App.grpObjForEdit;
    }

    public static void setGrpObjForEdit(JSONObject grpObjForEdit) {
        App.grpObjForEdit = grpObjForEdit;
    }


    public static ArrayList<ChatGroup> groupList = new ArrayList<ChatGroup>();

    private  static String chatText ,chatkeyval;

    public static String getChatText() {
        return chatText;
    }

    public static void setChatText(String chatText) {
        App.chatText = chatText;
    }

    public static String getChatkeyval() {
        return chatkeyval;
    }

    public static void setChatkeyval(String chatkeyval) {
        App.chatkeyval = chatkeyval;
    }

    public static ArrayList<ChatGroup> getGroupList() {
        return groupList;
    }
    public static void setGroupList(ArrayList<ChatGroup> groupList) {
        App.groupList = groupList;
    }
    /*==================DeleteMsg KeyVal=====================*/

    public static ArrayList<String> getDelMsgKeyValList() {
        return delMsgKeyValList;
    }

    public static void setDelMsgKeyValList(ArrayList<String> delMsgKeyValList) {
        App.delMsgKeyValList = delMsgKeyValList;
    }

    private static ArrayList<String> delMsgKeyValList = new ArrayList<>();


    private static String searchSelectedUser = "";

    public static ArrayList<String> getSearchSelectedUserArr() {
        return searchSelectedUserArr;
    }

    public static void setSearchSelectedUserArr(ArrayList<String> searchSelectedUserArr) {
        App.searchSelectedUserArr = searchSelectedUserArr;
    }

    private static ArrayList<String> searchSelectedUserArr = new ArrayList<>();

    public static ArrayList<String> getSearchAddUserArr() {
        return searchAddUserArr;
    }

    public static void setSearchAddUserArr(ArrayList<String> searchAddUserArr) {
        App.searchAddUserArr = searchAddUserArr;
    }

    private static ArrayList<String> searchAddUserArr = new ArrayList<>();

    public static String getSearchSelectedUser() {
        return searchSelectedUser;
    }

    public static void setSearchSelectedUser(String searchSelectedUser) {
        App.searchSelectedUser = searchSelectedUser;
    }

    public static ArrayList<User> addMemList = new ArrayList<>();

    public static ArrayList<User> getAddMemList() {
        return addMemList;
    }

    public static void setAddMemList(ArrayList<User> addMemList) {
        App.addMemList = addMemList;
    }


    /*=============conversation related ======================================================================================*/
    public static ArrayList<ChatGroup> getConversationList(){
        ArrayList<ChatGroup> chatGroupList=new ArrayList<>();
        ArrayList<ChatGroup> sortedGroupList=new ArrayList<>();
        try{
            ArrayList<ArrayList<JSONObject>> convDetailsList=new ArrayList<>();
            //DBManager dbManager=DBManager.getInstance();
            //convDetailsList=dbManager.fetchConversationDetails();
            ArrayList<JSONObject> convObjList=convDetailsList.get(0);
            ArrayList<JSONObject> latestMsgObjList=convDetailsList.get(1);
            if(convObjList.size()==latestMsgObjList.size()){
                for(int i=0;i<convObjList.size();i++){
                    JSONObject groupObj=convObjList.get(i);
                    JSONObject latestMsgObj=latestMsgObjList.get(i);
                    ChatGroup chatGroup=new ChatGroup(groupObj,latestMsgObj,0);
                    chatGroupList.add(chatGroup);
                }
                sortedGroupList = SortingClass.getSortedGrpList(chatGroupList);
            }
        } catch (Exception e) {
            Log.e("exception: ", ""+e.toString());
        }
        return sortedGroupList;
    }

}