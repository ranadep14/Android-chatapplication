package com.nc.developers.cloudscommunicator.objects.signup;

import android.util.Log;

import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.objects.CommonMethods;
import com.nc.developers.cloudscommunicator.objects.ConstantsObjects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import io.socket.client.Socket;

public class JsonObjectSignup{
    private static final String TAG=JsonObjectSignup.class.getSimpleName();
    public static JSONObject getJSONObjectSignup(String firstName,String lastName,String rPassword,String recoveryMail,
                                                 String userName){
        JSONObject mainObj=new JSONObject();
        try{
            if(lastName!=null){
                mainObj.put(ConstantsObjects.LAST_NAME,lastName);
            }
            JSONArray batonArr=new JSONArray();
            JSONObject innerObj=new JSONObject();
            innerObj.put("Location","KAFKA : In GeneralBroadcast : internalProducer : Producing on kafka Topic: REGISTER");
            innerObj.put("Timestamp",System.currentTimeMillis());
            batonArr.put(0,innerObj);
            mainObj.put("baton",batonArr);
            if(recoveryMail!=null){
                mainObj.put(ConstantsObjects.RECOVERY_MAIL,recoveryMail);
            }
            mainObj.put(ConstantsObjects.REGISTER_WITH,"mstorm");
            mainObj.put(ConstantsObjects.ORG_ID,"admin@clouzer.com#TSK_LST_ORG");
            if(rPassword!=null){
                mainObj.put(ConstantsObjects.RPASSWORD,rPassword);
            }
            mainObj.put(ConstantsObjects.SOCKET_ID,JsonObjectSignup.getSocketId());
            if(firstName!=null){
                mainObj.put(ConstantsObjects.FIRSTNAME,firstName);
            }
            mainObj.put(ConstantsObjects.IS_MOBILE_APPLICATION_REQ,true);
            JSONObject orgObject=new JSONObject();
            mainObj.put(ConstantsObjects.ORG_OBJ,orgObject);
            mainObj.put(ConstantsObjects.APPLICATION,"ios_mobile");
            mainObj.put(ConstantsObjects.IS_ORG_DOMAIN_REGISTER,true);
            mainObj.put(ConstantsObjects.REQUEST_ID,JsonObjectSignup.getRequestId(userName));
            mainObj.put(ConstantsObjects.TOPIC,ConstantsObjects.TOPIC_VALUE_REGISTER);
            JSONArray actionArr=CommonMethods.getActionArray(ConstantsObjects.REGISTER);
            mainObj.put(ConstantsObjects.ACTION_ARRAY_REGISTER,actionArr);
            if(userName!=null){
                mainObj.put(ConstantsObjects.USERNAME,userName);
            }
        }catch(JSONException e){
            Log.e(TAG+"_signup1:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_signup2:",e.toString());
        }
        return mainObj;
    }

    private static String getRequestId(String userName){
        String requestId="";
        Random random=new Random();
        Socket skt=null;
        if(GlobalClass.getSocketLogin()!=null){
            skt=GlobalClass.getSocketLogin();
        }
        if(skt!=null && userName!=null){
            String randomNumber1=String.format("%04d",random.nextInt(10000));
            String randomNumber2=String.format("%04d",random.nextInt(10000));
            requestId="/sync#"+skt.id()+userName+"#"+System.currentTimeMillis()+"r1"+randomNumber1+"r2"+randomNumber2;
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