package com.nc.developers.cloudscommunicator.objects.login;

import android.util.Log;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.models.Login;
import com.nc.developers.cloudscommunicator.objects.CommonMethods;
import com.nc.developers.cloudscommunicator.objects.ConstantsObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Random;
import io.socket.client.Socket;

public class JsonObjectLogin{
    private static final String TAG=JsonObjectLogin.class.getSimpleName();
    public static JSONObject getJSONObjectForgetPassword(String recoveryName,String userName){
        JSONObject mainObj=new JSONObject();
        try{
            JSONArray actionArray=new JSONArray();
            actionArray.put(0,"FORGOT_PASSWORD");
            mainObj.put("ACTION_ARRAY",actionArray);
            if(recoveryName!=null){
                mainObj.put("RECOVERY_MAIL",recoveryName);
            }
            JSONArray batonArray=new JSONArray();
            JSONObject batonInnerObj=new JSONObject();
            batonInnerObj.put("Location","login_initalize");
            batonInnerObj.put("Timestamp",System.currentTimeMillis());
            batonArray.put(0,batonInnerObj);
            //mainObj.put("baton",batonArray);
            JSONObject serverObj=new JSONObject();
            serverObj.put("server",GlobalClass.getLoginUrl());
            serverObj.put("type","clouzer");
            mainObj.put("data",serverObj);
            if(userName!=null){
                mainObj.put("username",userName);
            }
        }catch(JSONException e){
            Log.e(TAG+"_frgt_passwd1:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_frgt_passwd2:",e.toString());
        }
        return mainObj;
    }
    public static JSONObject getJSONObjectChangePassword(String userName,String newPassword,String securityPassword){
        JSONObject mainObj=new JSONObject();
        try{
            if(userName!=null){
                mainObj.put("username",userName);
            }
            if(newPassword!=null){
                mainObj.put("password",newPassword);
            }
            if(securityPassword!=null){
                mainObj.put("SecurityPassword",securityPassword);
            }
            JSONArray actionArr=new JSONArray();
            actionArr.put(0,"CHANGE_PASSWORD");
            mainObj.put("ACTION_ARRAY",actionArr);
            //mainObj.put("isVerified",true);
            JSONArray batonArr=new JSONArray();
            JSONObject firstObj=new JSONObject();
            firstObj.put("Location","login_initalize");
            firstObj.put("Timestamp",System.currentTimeMillis());
            batonArr.put(0,firstObj);
            JSONObject secondObj=new JSONObject();
            secondObj.put("Location","etServerSocket_login : node");
            secondObj.put("Timestamp",System.currentTimeMillis());
            batonArr.put(1,secondObj);
            JSONObject thirdObj=new JSONObject();
            thirdObj.put("Location","etLoginActionCreator_loginInit : node");
            thirdObj.put("Timestamp",System.currentTimeMillis());
            batonArr.put(2,thirdObj);
            //mainObj.put("baton",batonArr);
            mainObj.put("socketId",getSocketId());
            mainObj.put("requestId",getRequestId(userName));
            mainObj.put("topic","LOGIN");
        }catch(JSONException e){
            Log.e(TAG+"_expn_rst_pswrd1:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_expn_rst_pswrd2:",e.toString());
        }
        return mainObj;
    }
    public static JSONObject getJSONObjectUpdateUser(String imagePath){
        JSONObject obj=null;
        String keyval="";
        try{
            Repository rr=Repository.getRepository();
            if(rr!=null){
                Login lgn=rr.getLoginData();
                if(lgn!=null){
                    keyval=lgn.getKeyVal();
                }
            }
            obj= CommonMethods.getPrimaryJsonObject(ConstantsObjects.UPDATE_USER,
                    "","",
                    "","","","");
            JSONObject innerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            innerObj.remove(ConstantsObjects.CALMAIL_OBJECT);
            JSONObject calmailUpdateObj=new JSONObject();
            innerObj.put(ConstantsObjects.CALMAIL_UPDATE,calmailUpdateObj);
            calmailUpdateObj.put(ConstantsObjects.LAST_MODIFIED_ON,CommonMethods.getCurrentTimeStampInZuluFormat());
            calmailUpdateObj.put(ConstantsObjects.SYNC_PENDING_STATUS,0);
            calmailUpdateObj.put("UAD_CURRENT_ADDRESS","Bhusari Colony, Kothrud");
            calmailUpdateObj.put("UAD_CURRENT_ADDRESS_CITY","Pune");
            calmailUpdateObj.put("UAD_CURRENT_ADDRESS_COUNTRY","India");
            calmailUpdateObj.put("UAD_CURRENT_ADDRESS_PIN","000000");
            calmailUpdateObj.put("UAD_CURRENT_ADDRESS_STATE","Maharashtra");
            JSONArray imagePathArray=new JSONArray();
            calmailUpdateObj.put("UAD_USER_IMAGE",imagePathArray);
            imagePathArray.put(0,imagePath);
            innerObj.put(ConstantsObjects.KEY_TYPE,ConstantsObjects.KT_TSK);
            innerObj.put(ConstantsObjects.KEY_VAL,keyval);
            innerObj.put(ConstantsObjects.SUB_KEY_TYPE,"TSK_USR");
            JSONObject extraParamObj=new JSONObject();
            obj.put(ConstantsObjects.EXTRA_PARAM,extraParamObj);
            obj.remove(ConstantsObjects.MODULE_NAME);
            obj.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_VALUE_CONTACT);
            obj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            obj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_UPDATE);
        }catch(JSONException e){
            Log.e(TAG+"_updtUsr1:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_updtUsr2:",e.toString());
        }
        return obj;
    }
    private static String getSocketId(){
        String socketId="";
        if(GlobalClass.getSocketLogin()!=null){
            Socket socket=GlobalClass.getSocketLogin();
            socketId=socket.id();
        }
        return socketId;
    }
    private static String getRequestId(String userId){
        String requestId="";
        Random random=new Random();
        Socket skt=null;
        if(GlobalClass.getSocketLogin()!=null){
            skt=GlobalClass.getSocketLogin();
        }
        if(skt!=null){
            String randomNumber1=String.format("%04d",random.nextInt(10000));
            String randomNumber2=String.format("%04d",random.nextInt(10000));
            if(skt!=null && userId!=null){
                requestId="/sync#"+skt.id()+userId+"#"+System.currentTimeMillis()+"r1"+randomNumber1+"r2"+randomNumber2;
            }
        }
        return requestId;
    }
}