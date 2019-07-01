package com.nc.developers.cloudscommunicator.socket;

import android.text.TextUtils;
import android.util.Log;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.Subcription;
import com.nc.developers.cloudscommunicator.models.Login;
import com.nc.developers.cloudscommunicator.models.UserURM;
import com.nc.developers.cloudscommunicator.objects.user_urm.JsonObjectUserUrm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SocketConnection{
    private static String userId1="",jwtToken1="";
    private static int counter=0,/*initialSyncCounter=0,*/msgArrLength=0,calArrLength=0,convArrLength=0,orgArrLength=0,modelCounter=0;
    private static boolean isTablet=false;
    private static final String TAG=SocketConnection.class.getSimpleName();
    public static int initialSyncCounter=0;
    // method 1:authenticate
    public static void authenticate(String username,String password,String userId,String jwtToken,
                                    String userUId,String orgId,JSONObject syncMapObject){
        setSubcriber();
        SocketConnection.isTablet=GlobalClass.getTablet();
        GlobalClass.setUserId(userId);
        Log.i("userId_Set:",GlobalClass.getUserId());
        if(syncMapObject!=null){
            SocketConnection.setupSyncMapObject(syncMapObject);
        }
        GlobalClass.setUsername(username);
        GlobalClass.setPassword(password);
        GlobalClass.setAuthenticatedCallHistory(true);
        GlobalClass.setInfoId(userId);
        GlobalClass.setUserUID(userUId);
        GlobalClass.setOrgID(orgId);
        SocketConnection.userId1=userId;
        SocketConnection.jwtToken1=jwtToken;
        String url=GlobalClass.getLoginUrl();
        if(url!=null && !TextUtils.isEmpty(url)){
            SocketConnectionSync.makeSyncSocketConnection(url + "/sync");
        }
    }
    /*======================================================================================================*/
    private static Observable<String> mObservable;
    private static Observer<String> mObserver;
    private static void setSubcriber(){
        mObservable=Observable.create(new Observable.OnSubscribe<String>(){
            @Override
            public void call(Subscriber<? super String> subscriber){
                subscriber.onNext("");
                subscriber.onCompleted();
            }
        });
        mObserver=new Observer<String>(){
            @Override
            public void onCompleted(){

            }

            @Override
            public void onError(Throwable e){

            }

            @Override
            public void onNext(String string){
                if(string!=null){
                    Log.i("SocketConnection_k:",string);
                    if(string.equals("socket_sync_connect")){
                        SocketConnection.sendPercentageForSignup("percent#35");
                        if(userId1!=null && jwtToken1!=null && !TextUtils.isEmpty(userId1) && !TextUtils.isEmpty(jwtToken1)){
                            JSONObject authObj=JSonObjectClass.getAuthenticateJsonObject(userId1,jwtToken1);
                            if(authObj!=null && GlobalClass.getAuthenticatedSyncSocket()!=null){
                                GlobalClass.getAuthenticatedSyncSocket().emit("authenticate",authObj);
                            }
                        }
                    }
                    if(string.equals("socket_sync_auth_success")){
                        SocketConnection.sendPercentageForSignup("percent#50");
                        //connect to messaging socket
                        String url=GlobalClass.getLoginUrl();
                        SocketConnection.counter=0;
                        if(url!=null && !TextUtils.isEmpty(url)){
                            if(msgArrLength>0){
                                SocketConnectionMessaging.makeSocketConnection(url+"/messagingSync");
                            }
                            if(calArrLength>0){
                                SocketConnectionCalendar.makeSocketConnection(url+"/calendarSync");
                            }
                            if(convArrLength>0){
                                SocketConnectionConv.makeSocketConnection(url+"/conversationSync");
                            }
                        }
                    }
                    if(string.equals("socket_msg_connect")){
                        //emit authenticate on messaging socket
                        JSONObject authObj=JSonObjectClass.getAuthenticateJsonObject(userId1,jwtToken1);
                        if(authObj!=null && GlobalClass.getSocketModuleMessaging()!=null){
                            GlobalClass.getSocketModuleMessaging().emit("authenticate",authObj);
                        }
                    }
                    if(string.equals("socket_msg_auth_success")){
                        SocketConnection.sendPercentageForSignup("percent#80");
                        SocketConnection.counter+=1;
                        checkCondition();
                        JSONObject obj=JSonObjectClass.getJSONObjectToEmit();
                        if(obj!=null && GlobalClass.getSocketModuleMessaging()!=null){
                            GlobalClass.getSocketModuleMessaging().emit("messagingSyncForIndexDb",obj);
                        }
                    }
                    if(string.equals("socket_cal_connect")){
                        //emit authenticate on calendar socket
                        JSONObject authObj=JSonObjectClass.getAuthenticateJsonObject(userId1,jwtToken1);
                        if(authObj!=null && GlobalClass.getSocketModuleCalendar()!=null){
                            GlobalClass.getSocketModuleCalendar().emit("authenticate",authObj);
                        }
                    }
                    if(string.equals("socket_cal_auth_success")){
                        SocketConnection.sendPercentageForSignup("percent#60");
                        SocketConnection.counter+=1;
                        checkCondition();
                        JSONObject obj=JSonObjectClass.getJSONObjectToEmit();
                        if(obj!=null && GlobalClass.getSocketModuleCalendar()!=null){
                            GlobalClass.getSocketModuleCalendar().emit("calendarSyncForIndexDb",obj);
                        }
                    }
                    if(string.equals("socket_conv_connect")){
                        //emit authenticate on conversation socket
                        JSONObject authObj=JSonObjectClass.getAuthenticateJsonObject(userId1,jwtToken1);
                        if(authObj!=null && GlobalClass.getSocketModuleConv()!=null){
                            GlobalClass.getSocketModuleConv().emit("authenticate",authObj);
                        }
                    }
                    if(string.equals("socket_conv_auth_success")){
                        SocketConnection.sendPercentageForSignup("percent#70");
                        SocketConnection.counter+=1;
                        checkCondition();
                        JSONObject obj=JSonObjectClass.getJSONObjectToEmit();
                        if(obj!=null && GlobalClass.getSocketModuleConv()!=null){
                            GlobalClass.getSocketModuleConv().emit("conversationSyncForIdb",obj);
                        }
                    }
                    if(string.equals("cal_sync_done") || string.equals("conv_sync_done") || string.equals("msg_sync_done")){
                        initialSyncCounter+=1;
                        Log.i("initialSyncCounter_**:",String.valueOf(initialSyncCounter)+" ..kk");
                        if(initialSyncCounter==3){
                            initialSyncCounter=0;
                            saveLoginDataIntoDb();
                        }
                    }
                    if(string.equals("GET_ROLE_TASK_SERVER")){
                        JSONObject fetchUserUrmObj=JsonObjectUserUrm.getUserURMJsonObject();
                        Log.i(TAG+"_fetch_user_urm:",String.valueOf(fetchUserUrmObj)+" ..kk");
                        if(GlobalClass.getAuthenticatedSyncSocket()!=null){
                            GlobalClass.getAuthenticatedSyncSocket().emit("OnDemandCall",fetchUserUrmObj);
                        }
                    }
                    if(string.equals("FETCH_USER_URM_SERVER")){
                        Log.i(TAG+"_statua1**:",String.valueOf(GlobalClass.getCurrentSubcriberr())+" ..kk");
                        Log.i(TAG+"_statua2**:",String.valueOf(GlobalClass.getSignupSubcription())+" ..kk");
                        JSONObject userUrmObj=GlobalClass.getUserUrmObject();
                        if(userUrmObj!=null){
                            try{
                                if(userUrmObj.has("dataArray")){
                                    JSONArray dataArr=userUrmObj.getJSONArray("dataArray");
                                    if(dataArr!=null){
                                        if(dataArr.length()>0){
                                            ArrayList<UserURM> userURMS=new ArrayList<UserURM>();
                                            for(int i=0;i<dataArr.length();i++){
                                                JSONObject innerObj=dataArr.getJSONObject(i);
                                                UserURM userURM=JsonParserClass.parseUserURMJSONObject(innerObj);
                                                userURMS.add(userURM);
                                            }
                                            Repository rr=Repository.getRepository();
                                            if(rr!=null){
                                                boolean finalStatus=rr.insertUserURMData(userURMS);
                                                if(finalStatus){
                                                    if(GlobalClass.getLoginFragmentSubcriberr()!=null){
                                                        Observable<String> observable=GlobalClass.getLoginFragmentSubcriberr().getObservable();
                                                        Observer<String> observer=GlobalClass.getLoginFragmentSubcriberr().getObserver();
                                                        observable.just("initial_sync_done")
                                                                .subscribeOn(Schedulers.io())
                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                .subscribe(observer);
                                                    }
                                                    if(GlobalClass.getSignupSubcription()!=null){
                                                        Observable<String> observable=GlobalClass.getSignupSubcription().getObservable();
                                                        Observer<String> observer=GlobalClass.getSignupSubcription().getObserver();
                                                        observable.just("initial_sync_done")
                                                                .subscribeOn(Schedulers.io())
                                                                .observeOn(AndroidSchedulers.mainThread())
                                                                .subscribe(observer);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }catch(JSONException e){
                                Log.e(TAG+"_expn_urm_rspns1:",e.toString());
                            }catch(Exception e){
                                Log.e(TAG+"_expn_urm_rspns1:",e.toString());
                            }
                        }
                    }
                }
            }
        };

        Subcription subcription=new Subcription();
        subcription.setObservable(mObservable);
        subcription.setObserver(mObserver);
        GlobalClass.setCurrentSubcriberr(subcription);
    }
    private static void sendPercentageForSignup(String string){
        if(GlobalClass.getSignupSubcription()!=null){
            mObservable=GlobalClass.getSignupSubcription().getObservable();
            mObserver=GlobalClass.getSignupSubcription().getObserver();
            mObservable.just(string)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mObserver);
        }
    }
    /*======================================================================================================*/
    private static boolean saveLoginDataIntoDb(){
        boolean statusInsertLogin=false;
        JSONObject loginObject=GlobalClass.getLoginObject();
        Login login=JsonParserClass.parseLoginJSONObject(loginObject);
        if(login!=null){
            String finalImagePath=GlobalClass.getTSK_USR_imagePath();
            Log.i(TAG+"_finalImagePath:",finalImagePath+" ..kk");
            String keyval=GlobalClass.getTSK_USR_keyval();
            login.setImagePath(finalImagePath);
            GlobalClass.setTSK_USR_imagePath("");
            login.setKeyVal(keyval);
            GlobalClass.setTSK_USR_keyval("");
        }
        Repository repository=Repository.getRepository();
        if(repository!=null){
            statusInsertLogin=repository.insertLoginData(login);
            if(statusInsertLogin){
                if(GlobalClass.getAuthenticatedSyncSocket()!=null){
                    JSONObject fetchAllRoles=JsonObjectUserUrm.fetchAllRoles();
                    Log.i(TAG+"_emit_fetch_all_roles:",String.valueOf(fetchAllRoles)+" ..kk");
                    GlobalClass.getAuthenticatedSyncSocket().emit("OnDemandCall",fetchAllRoles);
                }
            }
        }
        return statusInsertLogin;
    }
    /*======================================================================================================*/
    private static void checkCondition(){
        Log.i("counter_value:",String.valueOf(SocketConnection.counter));
        Log.i("modelCounter_value:",String.valueOf(SocketConnection.modelCounter));
        if(SocketConnection.counter==SocketConnection.modelCounter){
            Log.i("status_khokho:","condition_match");
            SocketConnection.counter=SocketConnection.modelCounter=0;
            JSONObject object=new JSONObject();
            try{
                object.put("userId",GlobalClass.getInfoId());
                object.put("action","START_SYNC");
                if(SocketConnection.isTablet){
                    object.put("application","android_tablet");
                }else{
                    object.put("application","android_mobile");
                }
                object.put("deviceId","/sync#"+GlobalClass.getAuthenticatedSyncSocket().id());
                object.put("orgId",GlobalClass.getOrgID());
                object.put("userUID",GlobalClass.getUserUID());
                object.put("syncTime",System.currentTimeMillis());
                object.put("syncMap",GlobalClass.getSyncMapJSONObject());
                object.put("userKeyVal",GlobalClass.getUserKeyval());
            }catch(JSONException e){
                Log.e("expn_SAM1:",e.toString());
            }catch(Exception e){
                Log.e("expn_SAM2:",e.toString());
            }
            if(GlobalClass.getAuthenticatedSyncSocket()!=null){
                Log.i("object_SAM:",String.valueOf(object)+" ..kk");
                GlobalClass.getAuthenticatedSyncSocket().emit("SyncAllModules",object);
            }
        }else{
            Log.i("status_khokho:","condition_doesn't nmatch");
        }
    }
    /*========================================================================================================*/
    private static void setupSyncMapObject(JSONObject syncMapObject){
        try{
            SocketConnection.calArrLength
                    =SocketConnection.convArrLength
                    =SocketConnection.msgArrLength
                    =SocketConnection.modelCounter
                    =0;
            if(syncMapObject.has("calendar")){
                JSONArray arr_Calendar=syncMapObject.getJSONArray("calendar");
                if(arr_Calendar!=null){
                    if(arr_Calendar.length()>0){
                        GlobalClass.setCalendarArray(arr_Calendar);
                        calArrLength=arr_Calendar.length();
                        SocketConnection.modelCounter+=1;
                    }else{
                        calArrLength=0;
                    }
                }else{
                    calArrLength=0;
                }
            }
            if(syncMapObject.has("conversations")){
                JSONArray arr_Conv=syncMapObject.getJSONArray("conversations");
                if(arr_Conv!=null){
                    if(arr_Conv.length()>0){
                        GlobalClass.setConversationArray(arr_Conv);
                        convArrLength=arr_Conv.length();
                        SocketConnection.modelCounter+=1;
                    }else{
                        convArrLength=0;
                    }
                }else {
                    convArrLength=0;
                }
            }
            if(syncMapObject.has("messaging")){
                JSONArray arr_Msg=syncMapObject.getJSONArray("messaging");
                if(arr_Msg!=null){
                    if(arr_Msg.length()>0){
                        GlobalClass.setMessagingArray(arr_Msg);
                        msgArrLength=arr_Msg.length();
                        SocketConnection.modelCounter+=1;
                    }else{
                        msgArrLength=0;
                    }
                }else{
                    msgArrLength=0;
                }
            }
        }catch(JSONException e){
            Log.i("expn_sync_map_obj1:",e.toString());
        }catch(Exception e){
            Log.i("expn_sync_map_obj2:",e.toString());
        }
    }
}