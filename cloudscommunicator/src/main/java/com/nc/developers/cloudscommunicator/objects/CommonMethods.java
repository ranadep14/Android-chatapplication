package com.nc.developers.cloudscommunicator.objects;

import android.text.TextUtils;
import android.util.Log;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.models.Contact;
import com.nc.developers.cloudscommunicator.models.Login;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

import io.socket.client.Socket;

public class CommonMethods{
    private static final String TAG=CommonMethods.class.getSimpleName();
    public static String getRequestId(){
        String userId,requestId;
        userId=requestId="";
        Random random=new Random();
        userId=GlobalClass.getUserId();
        Socket skt=null;
        if(GlobalClass.getAuthenticatedSyncSocket()!=null){
            skt=GlobalClass.getAuthenticatedSyncSocket();
        }
        if(skt!=null){
            String randomNumber1=String.format("%04d",random.nextInt(10000));
            String randomNumber2=String.format("%04d",random.nextInt(10000));
            requestId="/sync#"+skt.id()+userId+"#"+System.currentTimeMillis()+"r1"+randomNumber1+"r2"+randomNumber2;
        }
        return requestId;
    }
    public static String getSocketId(){
        String socketId="";
        Socket socket=null;
        if(GlobalClass.getAuthenticatedSyncSocket()!=null){
            socket=GlobalClass.getAuthenticatedSyncSocket();
            socketId=socket.id();
        }
        return socketId;
    }
    public static JSONArray getActionArray(String actionArrayValue){
        JSONArray actionArr=new JSONArray();
        try{
            if(actionArrayValue!=null && !TextUtils.isEmpty(actionArrayValue)){
                actionArr.put(0,actionArrayValue);
            }
        }catch(JSONException e){
            Log.e("expn_action_arr1:",e.toString());
        }catch(Exception e){
            Log.e("expn_action_arr2:",e.toString());
        }
        return actionArr;
    }
    public static JSONObject getCalmailUpdateObject2(){
        JSONObject calmailObj=new JSONObject();
        String userId,currentTimeInZuluFormat;
        userId=currentTimeInZuluFormat="";
        try{
            userId=GlobalClass.getUserId();
            calmailObj.put(ConstantsObjects.LAST_MODIFIED_BY,userId);
            currentTimeInZuluFormat=CommonMethods.getCurrentTimeStampInZuluFormat();
            calmailObj.put(ConstantsObjects.LAST_MODIFIED_ON,currentTimeInZuluFormat);
            calmailObj.put(ConstantsObjects.SYNC_PENDING_STATUS,0);
        }catch(JSONException e){
            Log.e("expn_calmail_updt1:",e.toString());
        }catch(Exception e){
            Log.e("expn_calmail_updt2:",e.toString());
        }
        return calmailObj;
    }
    public static String getCurrentTimeStampInZuluFormat(){
        String str="";
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date=new Date();
        String dateFormatString=dateFormat.format(date);
        DateFormat original=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        DateFormat target=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.ENGLISH);
        Date date2=null;
        try{
            date2=original.parse(dateFormatString);
        }catch(ParseException e){
            Log.e("expn_tm_zulu1:",e.toString());
        }catch(Exception e){
            Log.e("expn_tm_zulu2:",e.toString());
        }
        str=target.format(date2);
        return str;
    }
    public static JSONArray getIdeDesignationArray(String designation){
        JSONArray array=new JSONArray();
        try{
            if(designation!=null){
                array.put(0,designation);
            }
        }catch(JSONException e){
            Log.e("expn_designation1",e.toString());
        }catch(Exception e){
            Log.e("expn_designation2",e.toString());
        }
        return array;
    }
    public static JSONObject getExtraParamObject(int hitServerFlag){
        JSONObject extraParamObject=new JSONObject();
        try{
            extraParamObject.put(ConstantsObjects.HIT_SERVER_FLAG,hitServerFlag);
            extraParamObject.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_NAME_VALUE);
            String userId=GlobalClass.getUserId();
            extraParamObject.put(ConstantsObjects.USER_ID,userId);
        }catch(JSONException e){
            Log.e("expn_extraParam1:",e.toString());
        }catch(Exception e){
            Log.e("expn_extraParam2:",e.toString());
        }
        return extraParamObject;
    }
    public static JSONArray getEssentialListArray(String conversationOwnerId){
        JSONArray essentialListArr=new JSONArray();
        try{
            if(conversationOwnerId!=null){
                JSONObject innerObj=new JSONObject();
                innerObj.put(ConstantsObjects.CREATORS_ID,conversationOwnerId);
                essentialListArr.put(0,innerObj);
            }
        }catch(JSONException e){
            Log.e("expn_essentialList2_1",e.toString());
        }catch(Exception e){
            Log.e("expn_essentialList2_2",e.toString());
        }
        return essentialListArr;
    }
    public static String getFourDigitRandomNumber(){
        String str="";
        Random random=new Random();
        str=String.format("%04d",random.nextInt(10000));
        return str;
    }
    public static String getFiveDigitRandomNumber(){
        String str="";
        Random r=new Random(System.currentTimeMillis());
        int fiveDgtNumber=((1+r.nextInt(2))*10000+r.nextInt(10000));
        str=String.valueOf(fiveDgtNumber);
        return str;
    }
    public static String getElevenDigitRandomNumber(){
        String str="";
        Random r=new Random();
        long elevenDgtNumber=r.nextInt(1_000_000_000)+(r.nextInt(90)+10)*1_000_000_000L;
        str=String.valueOf(elevenDgtNumber);
        return str;
    }
    public static JSONObject getPrimaryJsonObject(String actionArrayString,String conversationName,
                                                  String personaSelected,String calmailKeyType,
                                                  String calmailSubKeyType,String calmailTempKeyval,
                                                  String calmailCmlRefId){
        JSONObject mainObj=new JSONObject();
        String userId="";
        try{
            JSONArray dataArr=new JSONArray();
            mainObj.put(ConstantsObjects.DATA_ARRAY,dataArr);
            JSONObject dataArrInnerObj=new JSONObject();
            dataArr.put(0,dataArrInnerObj);
            JSONArray actionArr=CommonMethods.getActionArray(actionArrayString);
            dataArrInnerObj.put(ConstantsObjects.ACTION_ARRAY,actionArr);
            JSONObject calmailObj=CommonMethods.getCalmailObject(personaSelected,conversationName,
                    calmailKeyType,calmailSubKeyType,calmailTempKeyval,calmailCmlRefId);
            dataArrInnerObj.put(ConstantsObjects.CALMAIL_OBJECT,calmailObj);
            mainObj.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_NAME_VALUE);
            mainObj.put(ConstantsObjects.REQUEST_ID,CommonMethods.getRequestId());
            mainObj.put(ConstantsObjects.SOCKET_ID,CommonMethods.getSocketId());
            userId=GlobalClass.getUserId();
            mainObj.put(ConstantsObjects.USER_ID,userId);
        }catch(JSONException e){
            Log.e(TAG+"_baseObj1:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_baseObj2:",e.toString());
        }
        return mainObj;
    }
    public static JSONObject inviteeArrayInnerObject(String emailAddress,String cmlSubCategory){
        JSONObject innerObj=new JSONObject();
        String userId,tempKeyVal;
        userId=tempKeyVal="";
        try{
            userId=GlobalClass.getUserId();
            Repository rr=Repository.getRepository();
            if(rr!=null){
                Login login=rr.getLoginData();
                if(login!=null){
                    String deptId="";
                    deptId=login.getDeptId();
                    tempKeyVal=deptId+"#"+ConstantsObjects.SUB_KT_CONVERSATION_ARRAY1
                                +"#"+userId+":"+System.currentTimeMillis()
                                +"_"+CommonMethods.getElevenDigitRandomNumber()
                                +"_"+CommonMethods.getFiveDigitRandomNumber();
                }
            }
            if(emailAddress!=null){
                if(emailAddress.length()>0 && !TextUtils.isEmpty(emailAddress)){
                    innerObj.put(ConstantsObjects.ADDED_BY,userId);
                    innerObj.put(ConstantsObjects.CML_ASSIGNED,emailAddress);
                }
            }
            innerObj.put(ConstantsObjects.CML_ACCEPTED,1);
            innerObj.put(ConstantsObjects.CML_IS_ACTIVE,true);
            innerObj.put(ConstantsObjects.CML_IS_LATEST,1);
            innerObj.put(ConstantsObjects.CML_PARENT_TEMP_KEY_VAL,tempKeyVal);
            innerObj.put(ConstantsObjects.CML_PRIORITY,0);
            innerObj.put(ConstantsObjects.CML_STAR,0);
            innerObj.put(ConstantsObjects.CML_SUB_CATEGORY,cmlSubCategory+"");
            innerObj.put(ConstantsObjects.CML_UNREAD_COUNT,0);
            if(emailAddress!=null){
                if(emailAddress.length()>0 && !TextUtils.isEmpty(emailAddress)){
                    innerObj.put(ConstantsObjects.IDE_ATTENDEES_EMAIL,emailAddress);
                }else{
                    innerObj.put(ConstantsObjects.IDE_ATTENDEES_EMAIL,userId);
                }
            }
            JSONArray ideDesignationArr=null;
            if(emailAddress!=null){
                if(emailAddress.length()>0 && !TextUtils.isEmpty(emailAddress)){
                    ideDesignationArr=new JSONArray();
                }else{
                    ideDesignationArr=CommonMethods.getIdeDesignationArray(ConstantsObjects.IDE_DESIGNATION_VALUE);
                }
            }
            innerObj.put(ConstantsObjects.IDE_DESIGNATION,ideDesignationArr);
            innerObj.put(ConstantsObjects.IDE_ORIGINAL_CREATOR,userId);
            if(emailAddress!=null){
                if(emailAddress.length()>0 && !TextUtils.isEmpty(emailAddress)){
                    innerObj.put(ConstantsObjects.IDE_TYPE,ConstantsObjects.IDE_TYPE_VALUE_TO);
                    innerObj.put(ConstantsObjects.IDE_ACCEPTED,0);
                    innerObj.put(ConstantsObjects.CML_TEMP_KEY_VAL,tempKeyVal+"_IDE:TO_"+emailAddress);
                }else{
                    innerObj.put(ConstantsObjects.IDE_TYPE,ConstantsObjects.IDE_TYPE_VALUE_FROM);
                    innerObj.put(ConstantsObjects.IDE_ACCEPTED,1);
                    innerObj.put(ConstantsObjects.CML_TEMP_KEY_VAL,tempKeyVal+"_IDE:FROM_"+userId);
                }
            }
            if(rr!=null){
                Contact cntct=null;
                if(emailAddress!=null){
                    if(emailAddress.length()>0 && !TextUtils.isEmpty(emailAddress)){
                        cntct=rr.getContact(emailAddress);
                        if(cntct==null){
                            innerObj.put(ConstantsObjects.IS_CONTACT_INVITEE_UPDATE,false);
                        }else{
                            innerObj.put(ConstantsObjects.IS_CONTACT_INVITEE_UPDATE,true);
                        }
                    }else{
                        innerObj.put(ConstantsObjects.IS_CONTACT_INVITEE_UPDATE,true);
                    }
                }
            }
            innerObj.put(ConstantsObjects.KEY_TYPE_INNER,ConstantsObjects.KT_INVITEE);
            innerObj.put(ConstantsObjects.PARENT_KEY_TYPE,ConstantsObjects.KT_CONVERSATION);
            innerObj.put(ConstantsObjects.PARENT_SUB_KEY_TYPE,ConstantsObjects.SUB_KT_CONVERSATION_ARRAY1);
            innerObj.put(ConstantsObjects.SUB_KEY_TYPE_INNER,ConstantsObjects.SUB_KT_INVITEE);
        }catch(JSONException e){
            Log.e(TAG+"_inviteeInnArr1:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_inviteeInnArr2:",e.toString());
        }
        return innerObj;
    }
    private static JSONObject getCalmailObject(String personaSelected,String conversationName,String calmailKeyType,
                                               String calmailSubKeyType,String cmlTempKeyval,String cmlRefId){
        JSONObject calmailObj=new JSONObject();
        String userId,deptId,orgId,orgProjectId;
        userId=deptId=orgId=orgProjectId="";
        Login login=null;
        try{
            Repository rr=Repository.getRepository();
            if(rr!=null && personaSelected!=null){
                login=rr.getLoginData();
            }
            calmailObj.put(ConstantsObjects.CML_REF_ID,cmlRefId);
            userId=GlobalClass.getUserId();
            calmailObj.put(ConstantsObjects.CML_SUB_CATEGORY,userId+"#WKS:1");
            deptId=login.getDeptId();
            calmailObj.put(ConstantsObjects.CML_TEMP_KEY_VAL,cmlTempKeyval);
            calmailObj.put(ConstantsObjects.CML_TITLE,conversationName);
            calmailObj.put(ConstantsObjects.DEPT_ID_INNER,deptId);
            calmailObj.put(ConstantsObjects.DEPT_PROJECT_ID,deptId);
            calmailObj.put(ConstantsObjects.KEY_TYPE_INNER,calmailKeyType);
            orgId=login.getOrgId();
            calmailObj.put(ConstantsObjects.ORG_ID_INNER,orgId);
            orgProjectId=login.getProjectId();
            Log.i(TAG+"_lala_orgProjectId:",orgProjectId);
            calmailObj.put(ConstantsObjects.ORG_PROJECT_ID_INNER,orgProjectId);
            calmailObj.put(ConstantsObjects.SUB_KEY_TYPE_INNER,calmailSubKeyType);
        }catch(JSONException e){
            Log.e(TAG+"_calmailObj1:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_calmailObj2:",e.toString());
        }
        return calmailObj;
    }

    public static String getCmlTempKeyval(String subKeyType){
        Log.i(TAG+"_cmlTmpKvalGenerate:",subKeyType+" ..kk");
        String cmlTempKeyval,deptId,userId;
        cmlTempKeyval=deptId=userId="";
        Repository repository=null;
        Login login=null;
        repository=Repository.getRepository();
        if(repository!=null){
            login=repository.getLoginData();
        }
        if(login!=null){
            deptId=login.getDeptId();
        }
        userId=GlobalClass.getUserId();
        cmlTempKeyval=deptId+"#"+subKeyType+"#"+userId+":"+System.currentTimeMillis()+"_"
                            +CommonMethods.getElevenDigitRandomNumber()
                            +"_"+CommonMethods.getFiveDigitRandomNumber();
        return cmlTempKeyval;
    }

    //return false if string contains only numbers
    public static String isStringContainsOnlyNumbers(String inputString){
        boolean result=false;
        String str="no";
        Pattern pattern=Pattern.compile(".*\\D.*");//checks for non-digit character anywhere in the string.
        if(pattern!=null){
            result=pattern.matcher(inputString).matches();
        }
        if(result){

        }else{
            str="yes";
        }
        return str;
    }

    //returns true if string contains only symbols
    public static String isStringContainsOnlySymbols(String inputString){
        boolean result=false;
        String str="no";
        String specialCharacters="[" + "-/@#!*$%^&.'_+={}()"+ "]+";
        if(inputString!=null){
            result=inputString.matches(specialCharacters);
        }
        if(result){
            str="yes";
        }
        return str;
    }
    //returns yes if string contains only/any character/characters
    public static String isStringContainsCharacter(String inputString){
        String str="no";
        if(inputString!=null){
            char chars[]=inputString.toCharArray();
            if(chars!=null){
                if(chars.length>0){
                    for(int i=0;i<chars.length;i++){
                        char ch=chars[i];
                        if(Character.isLetter(ch)){
                            str="yes";
                            break;
                        }
                    }
                }
            }
        }
        return str;
    }

    //below method called when creating event either from conversation or agenda
    public static JSONObject inviteeListInnerObject(int status_i,String inviteeEmailId,String userId
            ,String cmlTempKeyval,String cmlSubCategory){
        JSONObject inviteeObj=new JSONObject();
        try{
            if(status_i!=0){
                //for invitees
                inviteeObj.put(ConstantsObjects.ADDED_BY,userId);
                inviteeObj.put(ConstantsObjects.CML_ASSIGNED,inviteeEmailId);
                inviteeObj.put(ConstantsObjects.CML_TEMP_KEY_VAL,
                        cmlTempKeyval+"_IDE:TO_"+inviteeEmailId);
                inviteeObj.put(ConstantsObjects.IDE_ACCEPTED,0);
                inviteeObj.put(ConstantsObjects.IDE_ATTENDEES_EMAIL,inviteeEmailId);
                inviteeObj.put(ConstantsObjects.IDE_TYPE,ConstantsObjects.IDE_TYPE_VALUE_TO);
            }else{
                //for logged in user who is creating calendar event
                inviteeObj.put(ConstantsObjects.CML_TEMP_KEY_VAL,
                        cmlTempKeyval+"_IDE:FROM_"+userId);
                inviteeObj.put(ConstantsObjects.IDE_ACCEPTED,1);
                inviteeObj.put(ConstantsObjects.IDE_ATTENDEES_EMAIL,userId);
                inviteeObj.put(ConstantsObjects.IDE_TYPE,ConstantsObjects.IDE_TYPE_VALUE_FROM);
            }
            inviteeObj.put(ConstantsObjects.CML_PARENT_TEMP_KEY_VAL,
                    CommonMethods.getCmlTempKeyval("TSK_EVT"));
            inviteeObj.put(ConstantsObjects.CML_PRIORITY,0);
            inviteeObj.put(ConstantsObjects.CML_SUB_CATEGORY,cmlSubCategory);
            inviteeObj.put(ConstantsObjects.IDE_ORIGINAL_CREATOR,userId);
            inviteeObj.put(ConstantsObjects.KEY_TYPE_INNER,ConstantsObjects.KT_INVITEE);
            inviteeObj.put(ConstantsObjects.PARENT_KEY_TYPE,ConstantsObjects.KT_TSK);
            inviteeObj.put(ConstantsObjects.PARENT_SUB_KEY_TYPE,"TSK_EVT");
            inviteeObj.put(ConstantsObjects.SUB_KEY_TYPE_INNER,"TSK_EVT_IDE");
        }catch(JSONException e){
            Log.e(TAG+"_inviteeLstInnerObj1:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_inviteeLstInnerObj1:",e.toString());
        }
        return inviteeObj;
    }
}