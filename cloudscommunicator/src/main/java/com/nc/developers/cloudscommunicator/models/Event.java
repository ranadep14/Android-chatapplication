package com.nc.developers.cloudscommunicator.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.database.ConstantsClass;
import com.nc.developers.cloudscommunicator.objects.CommonMethods;
import com.nc.developers.cloudscommunicator.objects.ConstantsObjects;
import com.nc.developers.cloudscommunicator.utils.CheckInternetConnectionCommunicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@Entity(tableName=ConstantsClass.TBL_CALENDAR_EVENT)
public class Event{
    @ColumnInfo(name= ConstantsClass.CML_TITLE)
    private String cmlTitle;

    @ColumnInfo(name=ConstantsClass.CML_DESCRIPTION)
    private String cmlDescription;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name=ConstantsClass.COMMON_KEY_VAL)
    private String keyVal;

    @ColumnInfo(name=ConstantsClass.COMMON_KEY_TYPE)
    private String keyType;

    @ColumnInfo(name=ConstantsClass.COMMON_SUB_KEY_TYPE)
    private String subKeyType;

    @ColumnInfo(name=ConstantsClass.COMMON_ACTIVE_STATUS)
    private int activeStatus;

    @ColumnInfo(name=ConstantsClass.EVENT_LOCATION)
    private String eventLocation;

    @ColumnInfo(name=ConstantsClass.EVENT_FROM_DATE)
    private String eventFromDate;

    @ColumnInfo(name=ConstantsClass.EVENT_TO_DATE)
    private String eventToDate;

    @ColumnInfo(name=ConstantsClass.CONVERSATION_ID)
    private String eventConversationId;

    @ColumnInfo(name=ConstantsClass.OWNER_ID)
    private String ownerId;

    @ColumnInfo(name=ConstantsClass.EVENT_STATUS)
    private String eventStatus;

    @ColumnInfo(name=ConstantsClass.RESTDATA)
    private String completeData;

    public String getCmlTitle(){
        return cmlTitle;
    }

    public void setCmlTitle(String cmlTitle){
        this.cmlTitle=cmlTitle;
    }

    public String getCmlDescription(){
        return cmlDescription;
    }

    public void setCmlDescription(String cmlDescription){
        this.cmlDescription=cmlDescription;
    }

    @NonNull
    public String getKeyVal(){
        return keyVal;
    }

    public void setKeyVal(@NonNull String keyVal){
        this.keyVal=keyVal;
    }

    public String getKeyType(){
        return keyType;
    }

    public void setKeyType(String keyType){
        this.keyType=keyType;
    }

    public String getSubKeyType(){
        return subKeyType;
    }

    public void setSubKeyType(String subKeyType){
        this.subKeyType=subKeyType;
    }

    public int getActiveStatus(){
        return activeStatus;
    }

    public void setActiveStatus(int activeStatus){
        this.activeStatus=activeStatus;
    }

    public String getCompleteData(){
        return completeData;
    }

    public void setCompleteData(String completeData){
        this.completeData=completeData;
    }

    public String getEventLocation(){
        return eventLocation;
    }

    public void setEventLocation(String eventLocation){
        this.eventLocation=eventLocation;
    }

    public String getEventFromDate(){
        return eventFromDate;
    }

    public void setEventFromDate(String eventFromDate){
        this.eventFromDate=eventFromDate;
    }

    public String getEventToDate(){
        return eventToDate;
    }

    public void setEventToDate(String eventToDate){
        this.eventToDate=eventToDate;
    }

    public String getEventConversationId(){
        return eventConversationId;
    }

    public void setEventConversationId(String eventConversationId){
        this.eventConversationId=eventConversationId;
    }

    public String getOwnerId(){
        return ownerId;
    }

    public void setOwnerId(String ownerId){
        this.ownerId=ownerId;
    }

    public String getEventStatus(){
        return eventStatus;
    }

    public void setEventStatus(String eventStatus){
        this.eventStatus=eventStatus;
    }

    //object structure begins here...
    public void acceptEventRequest(String personaSelected){
        JSONObject mainObb=new JSONObject();
        String userId,personaProjectId;
        userId=personaProjectId="";
        try{
            Repository repository=Repository.getRepository();
            if(repository!=null){
                if(personaSelected!=null){
                    ArrayList<String> personaLst=(ArrayList<String>)repository.getPersonaList();
                    if(personaLst!=null){
                        if(personaLst.contains(personaSelected)){
                            personaProjectId=repository.getPersonaWiseProjectId(personaSelected);
                        }else{
                            personaProjectId=repository.getPersonaWiseProjectId(ConstantsObjects.ROLE_PERSONAL);
                        }
                    }
                }
            }
            userId=GlobalClass.getUserId();
            mainObb.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            mainObb.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_UPDATE);
            JSONArray dataArr=new JSONArray();
            mainObb.put(ConstantsObjects.DATA_ARRAY,dataArr);
            JSONObject innerObb=new JSONObject();
            dataArr.put(0,innerObb);
            JSONArray actionArr= CommonMethods.getActionArray("CONFIRM_EVENT");
            innerObb.put(ConstantsObjects.ACTION_ARRAY,actionArr);
            JSONObject calmailUpdateObb=CommonMethods.getCalmailUpdateObject2();
            calmailUpdateObb.put(ConstantsObjects.CML_ACCEPTED,1);
            innerObb.put(ConstantsObjects.CALMAIL_UPDATE,calmailUpdateObb);
            JSONArray essentialLstArr=new JSONArray();
            innerObb.put(ConstantsObjects.ESSENTIAL_LIST_ARRAY,essentialLstArr);
            JSONObject essentialLstArrInnerObb=new JSONObject();
            essentialLstArr.put(0,essentialLstArrInnerObb);
            essentialLstArrInnerObb.put("urmId",personaProjectId);
            innerObb.put(ConstantsObjects.KEY_TYPE,ConstantsObjects.KT_TSK);
            innerObb.put(ConstantsObjects.KEY_VAL,this.keyVal);
            innerObb.put(ConstantsObjects.SUB_KEY_TYPE,"TSK_EVT");
            JSONObject extraParamObb=new JSONObject();
            mainObb.put(ConstantsObjects.EXTRA_PARAM,extraParamObb);
            JSONObject fromEndObb=new JSONObject();
            extraParamObb.put("frontEndReq",fromEndObb);
            fromEndObb.put("operateCurrentState",true);
            extraParamObb.put(ConstantsObjects.HIT_SERVER_FLAG,"0");
            extraParamObb.put(ConstantsObjects.MODULE_NAME,"EVT");
            mainObb.put(ConstantsObjects.MODULE_NAME,"EVT");
            mainObb.put(ConstantsObjects.REQUEST_ID,CommonMethods.getRequestId());
            mainObb.put(ConstantsObjects.SOCKET_ID,CommonMethods.getSocketId());
            mainObb.put(ConstantsObjects.USER_ID,userId);
        }catch(JSONException e){
            Log.e("Mdl_Evnt_accptEvnt1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_Evnt_accptEvnt2:",e.toString());
        }
        boolean internetConnectionStatus= CheckInternetConnectionCommunicator.isInternetAvailable();
        Log.i("Mdl_E_itrntCnnctnStats:",String.valueOf(internetConnectionStatus)+" ..kk");
        if(GlobalClass.getAuthenticatedSyncSocket()!=null && internetConnectionStatus){
            Log.i("Mdl_Evnt_accptEvntCall:",String.valueOf(mainObb)+" ..kk");
            GlobalClass.getAuthenticatedSyncSocket().emit(ConstantsObjects.SERVER_OPERATION,mainObb);
        }
    }

    public void rejectEventRequest(String declineComment){
        JSONObject mainObb=new JSONObject();
        String userId="";
        try{
            userId=GlobalClass.getUserId();
            mainObb.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            mainObb.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_UPDATE);
            JSONArray dataArr=new JSONArray();
            mainObb.put(ConstantsObjects.DATA_ARRAY,dataArr);
            JSONObject innerObb=new JSONObject();
            dataArr.put(0,innerObb);
            JSONArray actionArr=CommonMethods.getActionArray("REJECT_EVENT");
            innerObb.put(ConstantsObjects.ACTION_ARRAY,actionArr);
            JSONObject calmailObb=CommonMethods.getCalmailUpdateObject2();
            innerObb.put(ConstantsObjects.CALMAIL_UPDATE,calmailObb);
            calmailObb.put(ConstantsObjects.CML_ACCEPTED,7);
            JSONArray essentialLstArr=new JSONArray();
            innerObb.put(ConstantsObjects.ESSENTIAL_LIST_ARRAY,essentialLstArr);
            JSONObject essentialLstArrInnerObb=new JSONObject();
            essentialLstArr.put(0,essentialLstArrInnerObb);
            essentialLstArrInnerObb.put("DECLINE_COMMENT",declineComment);
            innerObb.put(ConstantsObjects.KEY_TYPE,ConstantsObjects.KT_TSK);
            innerObb.put(ConstantsObjects.KEY_VAL,this.keyVal);
            innerObb.put(ConstantsObjects.SUB_KEY_TYPE,"TSK_EVT");
            JSONObject extraParamObb=new JSONObject();
            mainObb.put(ConstantsObjects.EXTRA_PARAM,extraParamObb);
            extraParamObb.put(ConstantsObjects.HIT_SERVER_FLAG,"0");
            extraParamObb.put(ConstantsObjects.MODULE_NAME,"EVT");
            extraParamObb.put(ConstantsObjects.USER_ID,userId);
            mainObb.put(ConstantsObjects.MODULE_NAME,"EVT");
            mainObb.put(ConstantsObjects.REQUEST_ID,CommonMethods.getRequestId());
            mainObb.put(ConstantsObjects.SOCKET_ID,CommonMethods.getSocketId());
            mainObb.put(ConstantsObjects.USER_ID,userId);
        }catch(JSONException e){
            Log.e("Mdl_Evnt_rjctEvnt1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_Evnt_rjctEvnt2:",e.toString());
        }
        boolean internetConnectionStatus=CheckInternetConnectionCommunicator.isInternetAvailable();
        Log.i("Mdl_E_itrntCnnctnStats:",String.valueOf(internetConnectionStatus)+" ..kk");
        if(GlobalClass.getAuthenticatedSyncSocket()!=null && internetConnectionStatus){
            Log.i("Mdl_Evnt_rjctEvntCall:",String.valueOf(mainObb)+" ..kk");
            GlobalClass.getAuthenticatedSyncSocket().emit(ConstantsObjects.SERVER_OPERATION,mainObb);
        }
    }

    public static void fetchEvents(String fromTimeInZuluFormat,String toTimeInZuluFormat,
                                   String personaSelected){
        Log.i("Mdl_Et_frmTmInZuluFrmt:",fromTimeInZuluFormat+" ..kk");
        Log.i("Mdl_Et_toTmInZuluFrmt:",toTimeInZuluFormat+" ..kk");
        Log.i("Mdl_Et_personaSelected:",personaSelected+" ..kk");
        JSONObject obb=CommonMethods.getPrimaryJsonObject(ConstantsObjects.SUPERIMPOSE_EVENT,
                "","","","",
                "","");
        try{
            JSONArray dataArr=obb.getJSONArray(ConstantsObjects.DATA_ARRAY);
            JSONObject innerObj=dataArr.getJSONObject(0);
            innerObj.remove(ConstantsObjects.CALMAIL_OBJECT);
            JSONObject filterObb=new JSONObject();
            innerObj.put(ConstantsObjects.FILTER_OBJECT,filterObb);
            filterObb.put(ConstantsObjects.CML_FROM_TIME,fromTimeInZuluFormat);
            filterObb.put(ConstantsObjects.CML_TO_TIME,toTimeInZuluFormat);
            filterObb.put(ConstantsObjects.ACCEPTED_BY_ME,false);
            filterObb.put(ConstantsObjects.ARCHIVE_EVENT,false);
            filterObb.put(ConstantsObjects.CREATED_BY_ME,false);
            filterObb.put(ConstantsObjects.CREATED_FOR_ME,false);
            filterObb.put(ConstantsObjects.MAY_BE_FILTER,false);
            JSONArray projectIdsArray=new JSONArray();
            filterObb.put(ConstantsObjects.PROJECT_IDS,projectIdsArray);
            String personaProjectId="";
            Repository rr=Repository.getRepository();
            if(rr!=null){
                personaProjectId=rr.getPersonaWiseProjectId(personaSelected);
                Log.i("Mdl_Et_prsnaPrjctId:",personaProjectId+" ..kk");
            }
            projectIdsArray.put(0,personaProjectId);
            JSONObject sectionIdObb=new JSONObject();
            filterObb.put(ConstantsObjects.SECTION_ID,sectionIdObb);
            innerObj.put(ConstantsObjects.KEY_TYPE,ConstantsObjects.KT_TSK);
            JSONArray sectionIdsArr=new JSONArray();
            innerObj.put(ConstantsObjects.SECTION_IDS,sectionIdsArr);
            innerObj.put(ConstantsObjects.SUB_KEY_TYPE,ConstantsObjects.SUB_KT_CALENDAR_EVENT);
            obb.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_VALUE_CALENDAR_EVENT);
        }catch(JSONException e){
            Log.e("Mdl_Et_fetchEvnts1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_Et_fetchEvnts2:",e.toString());
        }
        Log.i("Mdl_Et_fetchEventObb:",String.valueOf(obb)+" ..kk");
        boolean internetStatus= CheckInternetConnectionCommunicator.isInternetAvailable();
        Log.i("Mdl_Et_crtEvtIntrntSts:",String.valueOf(internetStatus)+" ..kk");
        if(GlobalClass.getAuthenticatedSyncSocket()!=null && internetStatus){
            GlobalClass.getAuthenticatedSyncSocket().emit(ConstantsObjects.ON_DEMAND_CALL,obb);
        }
    }

    public static void fetchEventRequest(){
        JSONObject mainObb=new JSONObject();
        String deptId,orgId,projectId,userId;
        deptId=orgId=projectId=userId="";
        Repository repository=Repository.getRepository();
        try{
            userId=GlobalClass.getUserId();
            Login login=null;
            if(repository!=null){
                login=repository.getLoginData();
            }
            if(login!=null){
                deptId=login.getDeptId();
                orgId=login.getOrgId();
                projectId=login.getProjectId();
            }
            JSONArray dataArr=new JSONArray();
            mainObb.put(ConstantsObjects.DATA_ARRAY,dataArr);
            JSONObject innerObb=new JSONObject();
            dataArr.put(0,innerObb);
            JSONArray actionArr=CommonMethods.getActionArray("FETCH_INBOX_OBJECTS");
            innerObb.put(ConstantsObjects.ACTION_ARRAY,actionArr);
            innerObb.put(ConstantsObjects.DEPT_ID,deptId);
            innerObb.put(ConstantsObjects.KEY_TYPE,"INB");
            innerObb.put(ConstantsObjects.LAST_ID,"");
            innerObb.put(ConstantsObjects.OFFSET,0);
            innerObb.put(ConstantsObjects.ORG_ID,orgId);
            innerObb.put(ConstantsObjects.ORG_PROJECT_ID,projectId);
            innerObb.put(ConstantsObjects.PROJECT_ID,userId+"#PRJ:INB_PRJ:INB_EVT_REQ");
            innerObb.put(ConstantsObjects.SUB_KEY_TYPE,"INB_EVT_REQ");
            JSONArray subKeyTypeArr=new JSONArray();
            subKeyTypeArr.put(0,"TSK_PRE_EVT");
            subKeyTypeArr.put(1,"TSK_SHELL_EVT_ORG");
            subKeyTypeArr.put(2,"TSK_EVT_ORG");
            subKeyTypeArr.put(3,"TSK_RPE_EVT_ORG");
            subKeyTypeArr.put(4,"TSK_POST_EVT");
            subKeyTypeArr.put(5,"TSK_EVT");
            innerObb.put("subKeyTypeArr",subKeyTypeArr);
            innerObb.put("type","ORG");
            mainObb.put(ConstantsObjects.MODULE_NAME,"INB");
            mainObb.put(ConstantsObjects.REQUEST_ID,CommonMethods.getRequestId());
            mainObb.put(ConstantsObjects.SOCKET_ID,CommonMethods.getSocketId());
            mainObb.put(ConstantsObjects.USER_ID,userId);
        }catch(JSONException e){
            Log.e("Mdl_Evnt_ftchEvntRqst1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_Evnt_ftchEvntRqst2:",e.toString());
        }
        boolean internetConnectionStatus=CheckInternetConnectionCommunicator.isInternetAvailable();
        Log.i("Mdl_E_itrntCnnctnStats:",String.valueOf(internetConnectionStatus)+" ..kk");
        if(GlobalClass.getAuthenticatedSyncSocket()!=null && internetConnectionStatus){
            Log.i("Mdl_Evt_ftEvntRqstCall:",String.valueOf(mainObb)+" ..kk");
            GlobalClass.getAuthenticatedSyncSocket().emit(ConstantsObjects.ON_DEMAND_CALL,mainObb);
        }
    }

    public void createCalendarEventFromAgenda(String eventTitle,String personaSelected,
                                           ArrayList<String> invitees,JSONObject locationObject){
        Log.i("Mdl_Evnt_locationObb:",String.valueOf(locationObject)+" ..kk");
        JSONObject mainObj=null;
        String userId,cmlTempKeyval,personaProjectId,caledaySyncCmlRefId,
                calendarSyncProjectId,calendarSyncCmlSubCategory,projectId;
        userId=cmlTempKeyval=personaProjectId=caledaySyncCmlRefId
                =calendarSyncProjectId=calendarSyncCmlSubCategory=projectId="";
        Calendar calendar=null;
        Login login=null;
        try{
            String continent,countryCode,fromDateTime,location,pincode,toDateTime;
            continent=countryCode=fromDateTime=location=pincode=toDateTime="";
            double latitude,longitude;
            latitude=longitude=0.0;
            int timezone=0;
            if(locationObject!=null){
                if(locationObject.has("cml_continent")){
                    continent=locationObject.getString("cml_continent");
                }
                if(locationObject.has("cml_country_code")){
                    countryCode=locationObject.getString("cml_country_code");
                }
                if(locationObject.has("cml_from_date_time")){
                    fromDateTime=locationObject.getString("cml_from_date_time");
                }
                if(locationObject.has("cml_latitude")){
                    latitude=locationObject.getDouble("cml_latitude");
                }
                if(locationObject.has("cml_location")){
                    location=locationObject.getString("cml_location");
                }
                if(locationObject.has("cml_longitude")){
                    longitude=locationObject.getDouble("cml_longitude");
                }
                if(locationObject.has("cml_pincode")){
                    pincode=locationObject.getString("cml_pincode");
                }
                if(locationObject.has("cml_timezone")){
                    timezone=locationObject.getInt("cml_timezone");
                }
                if(locationObject.has("cml_to_date_time")){
                    toDateTime=locationObject.getString("cml_to_date_time");
                }
            }
            Repository repository=Repository.getRepository();
            if(repository!=null){
                personaProjectId=repository.getPersonaWiseProjectId(personaSelected);
                calendar=repository.getCalendar(personaProjectId);
                if(calendar!=null){
                    caledaySyncCmlRefId=calendar.getCmlRefId();
                    calendarSyncProjectId=calendar.getProjectId();
                    calendarSyncCmlSubCategory=calendar.getCmlSubCategory();
                }
                login=repository.getLoginData();
                if(login!=null){
                    projectId=login.getProjectId();
                }
            }
            userId=GlobalClass.getUserId();
            cmlTempKeyval=CommonMethods.getCmlTempKeyval("TSK_EVT");
            mainObj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.CAST_EVENT,eventTitle,
                    "",ConstantsObjects.KT_TSK,"TSK_EVT",
                    cmlTempKeyval,calendarSyncProjectId);
            JSONArray dataArr=mainObj.getJSONArray(ConstantsObjects.DATA_ARRAY);
            JSONObject innerObj=dataArr.getJSONObject(0);
            JSONObject calmailObj=innerObj.getJSONObject(ConstantsObjects.CALMAIL_OBJECT);
            calmailObj.put(ConstantsObjects.ACTIVE_STATUS,1);
            calmailObj.put(ConstantsObjects.CML_ASSIGNED_TO,"");
            calmailObj.put(ConstantsObjects.CML_CITY,"");
            calmailObj.put(ConstantsObjects.CML_CONTINENT,continent);
            calmailObj.put(ConstantsObjects.CML_COUNTRY,"");
            calmailObj.put(ConstantsObjects.CML_COUNTRY_CODE,countryCode);
            calmailObj.put(ConstantsObjects.CML_DESCRIPTION,"");
            calmailObj.put(ConstantsObjects.CML_FROM_DATETIME,fromDateTime);
            calmailObj.put(ConstantsObjects.CML_LATITUDE,latitude);
            calmailObj.put(ConstantsObjects.CML_LOCATION,location);
            calmailObj.put(ConstantsObjects.CML_LONGITUDE,longitude);
            calmailObj.put(ConstantsObjects.CML_MAIN_PARENT,caledaySyncCmlRefId);
            calmailObj.put(ConstantsObjects.CML_ORG_WEB_SITE,ConstantsObjects.DOMAIN_VALUE.substring(1));
            calmailObj.put(ConstantsObjects.CML_PINCODE,pincode);
            calmailObj.put(ConstantsObjects.CML_PRIORITY,0);
            calmailObj.put(ConstantsObjects.CML_STATE,"");
            calmailObj.put(ConstantsObjects.CML_SUB_CATEGORY,calendarSyncCmlSubCategory);
            calmailObj.put(ConstantsObjects.CML_TIMEZONE,timezone);
            calmailObj.put(ConstantsObjects.CML_TO_DATETIME,toDateTime);
            calmailObj.put(ConstantsObjects.CML_ZIPCODE,"");
            calmailObj.put(ConstantsObjects.PARENT_WIZARD_ID,caledaySyncCmlRefId);
            calmailObj.put(ConstantsObjects.SYNC_PENDING_STATUS,0);
            calmailObj.put(ConstantsObjects.USER_LAST_MODIFIED_ON,CommonMethods.getCurrentTimeStampInZuluFormat());
            calmailObj.put(ConstantsObjects.DEPT_PROJECT_ID,projectId);
            calmailObj.put(ConstantsObjects.TYPE,3);
            if(invitees!=null){
                if(invitees.size()>0){
                    JSONArray inviteeArr=new JSONArray();
                    innerObj.put(ConstantsObjects.INVITEE_LIST,inviteeArr);
                    JSONObject inviteeArrInnerObj=CommonMethods
                            .inviteeListInnerObject(0,null,userId,cmlTempKeyval,calendarSyncCmlSubCategory);
                    inviteeArr.put(0,inviteeArrInnerObj);
                    for(int i=0;i<invitees.size();i++){
                        String singleInvitee=invitees.get(i);
                        inviteeArrInnerObj=CommonMethods
                                .inviteeListInnerObject((i+1),singleInvitee,userId,cmlTempKeyval,calendarSyncCmlSubCategory);
                        inviteeArr.put((i+1),inviteeArrInnerObj);
                    }
                }
            }
            mainObj.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_VALUE_CALENDAR_EVENT);
            mainObj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            mainObj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_INSERT);
        }catch(JSONException e){
            Log.e("Mdl_Evnt_creatEvnts1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_Evnt_creatEvnts2:",e.toString());
        }
        Log.i("Mdl_Evnt_crtEventObb:",String.valueOf(mainObj)+" ..kk");
        boolean internetStatus= CheckInternetConnectionCommunicator.isInternetAvailable();
        Log.i("Mdl_crtEvntIntrntStats:",String.valueOf(internetStatus)+" ..kk");
        if(GlobalClass.getAuthenticatedSyncSocket()!=null && internetStatus){
            GlobalClass.getAuthenticatedSyncSocket().emit(ConstantsObjects.SERVER_OPERATION,mainObj);
        }
    }
}