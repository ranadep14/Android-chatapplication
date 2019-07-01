package com.nc.developers.cloudscommunicator.socket;

import android.text.TextUtils;
import android.util.Log;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.objects.ConstantsObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Random;
import io.socket.client.Socket;

public class JSonObjectClass{
    private static final String TAG=JSonObjectClass.class.getSimpleName();
    // authenticate json object
    public static JSONObject getAuthenticateJsonObject(String myInfoObject, String jwtToken){
        JSONObject object = new JSONObject();
        try {
            object.put("userId", myInfoObject);
            object.put("token", jwtToken);
        } catch (JSONException e){
            Log.i("excep: ", e.getMessage());
        }
        return object;
    }
    public static JSONObject getJsonSignUpObject(String firstName,String lastName,String userId,String rPassword,
                                                 String recoveryMail){
        //JSONObject mainObj=new JSONObject();
        JSONObject mainObj=null;
        try{
            String str="{\"firstName\":\"dummy\",\"lastName\":\"user4mar3\",\"username\":\"dummy.user4mar3@clouzer.com\",\"rPassword\":\"123\",\"isMobileApplicationReq\":true,\"application\":\"ios_mobile\",\"RECOVERY_MAIL\":\"dummy.user4mar3@proeasyweb.com\",\"RegisterWith\":\"mstorm\",\"orgObj\":null,\"isOrgDomainRegister\":true,\"orgId\":\"admin@clouzer.com#TSK_LST_ORG\",\"ACTION_ARRAY\":[\"REGISTER\"],\"baton\":[{\"Location\":\"loginserv_register\",\"Timestamp\":1551677147762},{\"Location\":\"etServerSocket_login : node\",\"Timestamp\":1551677147771},{\"Location\":\"etLoginActionCreator_loginInit : node\",\"Timestamp\":1551677147771},{\"Location\":\"etLoginReducer_sendDataToJava : node\",\"Timestamp\":1551677147772},{\"Location\":\"etDirectToQueue_sendToJavaProcess : node\",\"Timestamp\":1551677147772},{\"Location\":\"etKafka_producer_sendMessage:node\",\"Timestamp\":1551677147772}],\"socketId\":\"/login#JgHdxs4BqOcMi4Y9AAPy\",\"requestId\":\"/sync#/login#JgHdxs4BqOcMi4Y9AAPydummy.user4mar3@clouzer.com#1551677147771\",\"topic\":\"REGISTER\"}";
            mainObj=new JSONObject(str);
            mainObj.put("firstName",firstName);
            mainObj.put("lastName",lastName);
            mainObj.put("username",userId);
            mainObj.put("rPassword",rPassword);
            mainObj.put("RECOVERY_MAIL",recoveryMail);
            /*mainObj.put("firstName",firstName);
            mainObj.put("lastName",lastName);
            mainObj.put("username",userId);
            mainObj.put("rPassword",rPassword);
            mainObj.put("isMobileApplicationReq",true);
            mainObj.put("application","ios_mobile");
            mainObj.put("RECOVERY_MAIL",recoveryMail);
            mainObj.put("RegisterWith","mstorm");
            mainObj.put("orgObj",JSONObject.NULL);
            mainObj.put("isOrgDomainRegister",true);
            mainObj.put("orgId","admin@clouzer.com#TSK_LST_ORG");

            JSONArray actionArr=new JSONArray();
            actionArr.put(0,"REGISTER");
            mainObj.put("ACTION_ARRAY",actionArr);

            JSONArray batonArr=new JSONArray();
            JSONObject batonInnerObj=new JSONObject();
            batonInnerObj.put("Location","loginserv_register");
            batonInnerObj.put("Timestamp",System.currentTimeMillis());
            batonArr.put(0,batonInnerObj);
            mainObj.put("baton",batonArr);
            mainObj.put(ConstantsObjects.SOCKET_ID,getSocketId());
            mainObj.put(ConstantsObjects.REQUEST_ID,getRequestId());*/
        }catch(JSONException e){
            Log.e("expn_signup1:",e.toString());
        }catch(Exception e){
            Log.e("expn_signup2:",e.toString());
        }
        return mainObj;
    }
    public static JSONObject getJsonObjectLogin(String username,String password){
        JSONArray jsonArray = new JSONArray();
        JSONObject object = new JSONObject();
        try{
            if(username!=null && !TextUtils.isEmpty(username) && password!=null && !TextUtils.isEmpty(password)){
                Log.i("recvd_username:",username);
                Log.i("recvd_password:",password);
                jsonArray.put(0,"LOGIN");
                object.put("ACTION_ARRAY",jsonArray);
                //object.put("securityAnswer", "");
                //object.put("registerWith", "mstorm");
                //object.put("registerWith", "clouzer");
                if(username.contains("@clouzer")){
                    object.put("orgId","admin@clouzer.com#TSK_LST_ORG");
                }
                if(username.contains("@mstorm")){
                    object.put("orgId","admin@mstorm.com#1231231231231#ORG:1_1");
                }
                object.put("username",username);
                object.put("password",password);
                boolean isTablet=GlobalClass.getTablet();
                if(isTablet){
                    object.put("application","android_tablet");
                } else {
                    object.put("application","android_mobile");
                }
            }
        }catch(JSONException e){
            Log.e("expn_json_login1:",e.getMessage());
        }catch(Exception e){
            Log.e("expn_json_login2:",e.getMessage());
        }
        return object;
    }
    public static JSONObject getJsonObjectLoginOrg(String username,String password){
        JSONObject obb=new JSONObject();
        try{
            JSONArray actionArr=new JSONArray();
            actionArr.put(0,"LOGIN");
            obb.put("ACTION_ARRAY",actionArr);
            obb.put(ConstantsObjects.ORG_ID,"rutuja.d@clouzersoftwaresolutions.clouzer.com#PRJ_WKS_ORG_1555569515142_2056");
            obb.put("password",password);
            obb.put("username",username);
        }catch(JSONException e){
            Log.e(TAG+"_orgLgnObb1:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_orgLgnObb2:",e.toString());
        }
        return obb;
    }
    public static JSONObject getJSONObjectToEmit(){
        JSONObject object=new JSONObject();
        try{
            object.put("userId",GlobalClass.getUserId());
            String deviceId="/sync#"+GlobalClass.getAuthenticatedSyncSocket().id();
            object.put("deviceId",deviceId);
        }catch(JSONException e){
            Log.i("expn_obj1:",e.toString());
        }catch(Exception e){
            Log.i("expn_obj2:",e.toString());
        }
        return object;
    }
    private static String getRequestId(){
        String userId,requestId;
        userId=requestId="";
        Random random=new Random();
        userId=GlobalClass.getUserId();
        Socket skt=null;
        if(GlobalClass.getSocketLogin()!=null){
            skt=GlobalClass.getSocketLogin();
        }
        if(skt!=null){
            String randomNumber1=String.format("%04d",random.nextInt(10000));
            String randomNumber2=String.format("%04d",random.nextInt(10000));
            requestId="/sync#"+skt.id()+userId+"#"+System.currentTimeMillis()+"r1"+randomNumber1+"r2"+randomNumber2;
        }
        return requestId;
    }
    private static String getSocketId(){
        String socketId="";
        Socket socket=null;
        if(GlobalClass.getSocketLogin()!=null){
            socket=GlobalClass.getSocketLogin();
            socketId=socket.id();
        }
        return socketId;
    }
}