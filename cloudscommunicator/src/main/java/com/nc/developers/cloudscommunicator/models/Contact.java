package com.nc.developers.cloudscommunicator.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.database.ConstantsClass;
import com.nc.developers.cloudscommunicator.objects.CommonMethods;
import com.nc.developers.cloudscommunicator.objects.ConstantsObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@Entity(tableName= ConstantsClass.TBL_CONTACT)
public class Contact{

    @ColumnInfo(name=ConstantsClass.CONTACT_TITLE)
    private String title;

    @ColumnInfo(name=ConstantsClass.NICKNAME)
    private String nickname;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name=ConstantsClass.COMMON_KEY_VAL)
    private String keyval;

    @ColumnInfo(name=ConstantsClass.FIRSTNAME)
    private String firstname;

    @ColumnInfo(name=ConstantsClass.LASTNAME)
    private String lastname;

    @ColumnInfo(name=ConstantsClass.COMMON_CML_REF_ID)
    private String cmlRefId;

    @ColumnInfo(name=ConstantsClass.CREATEDON)
    private String createdOn;

    @ColumnInfo(name=ConstantsClass.CML_PERSONAL_EMAIL)
    private String personalEmail;

    @ColumnInfo(name=ConstantsClass.IMAGE_PATH)
    private String imagePath;

    @ColumnInfo(name=ConstantsClass.OFFICIAL_EMAIL)
    private String officialEmail;

    @ColumnInfo(name=ConstantsClass.COMMON_KEY_TYPE)
    private String keytype;

    @ColumnInfo(name=ConstantsClass.COMMON_SUB_KEY_TYPE)
    private String subkeytype;

    @ColumnInfo(name=ConstantsClass.CML_ACCEPTED)
    private int cmlAccepted;

    @Ignore
    @ColumnInfo(name=ConstantsClass.VIEW_TYPE)
    private int viewType=0;

    @Ignore
    @ColumnInfo(name=ConstantsClass.CONTACT_SELECTED)
    private boolean contactSelected=false;

    @ColumnInfo(name=ConstantsClass.RESTDATA)
    private String completeData;

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public String getNickname(){
        return nickname;
    }

    public void setNickname(String nickname){
        this.nickname=nickname;
    }

    @NonNull
    public String getKeyval(){
        return keyval;
    }

    public void setKeyval(@NonNull String keyval){
        this.keyval=keyval;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCmlRefId() {
        return cmlRefId;
    }

    public void setCmlRefId(String cmlRefId) {
        this.cmlRefId = cmlRefId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getOfficialEmail() {
        return officialEmail;
    }

    public void setOfficialEmail(String officialEmail) {
        this.officialEmail = officialEmail;
    }

    public String getKeytype() {
        return keytype;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    public String getSubkeytype(){
        return subkeytype;
    }

    public void setSubkeytype(String subkeytype){
        this.subkeytype = subkeytype;
    }

    public int getCmlAccepted(){
        return cmlAccepted;
    }

    public void setCmlAccepted(int cmlAccepted){
        this.cmlAccepted = cmlAccepted;
    }

    public int getViewType(){
        return viewType;
    }

    public void setViewType(int viewType){
        this.viewType=viewType;
    }

    public boolean isContactSelected(){
        return contactSelected;
    }

    public void setContactSelected(boolean contactSelected){
        this.contactSelected=contactSelected;
    }

    public String getCompleteData(){
        return completeData;
    }

    public void setCompleteData(String completeData){
        this.completeData=completeData;
    }
    /*======================================================================================================================
        model class changes==>>
    =======================================================================================================================*/
    public void getJsonObjectCreateOneToOneConversation(){
        JSONObject obj=null;
        String deptId,orgId,projectId,cmlSubCategory;
        deptId=orgId=projectId=cmlSubCategory="";
        try{
            String completeDataString=this.getCompleteData();
            if(completeDataString!=null){
                JSONObject completeDataObj=new JSONObject(completeDataString);
                if(completeDataObj.has("CML_SUB_CATEGORY")){
                    cmlSubCategory=completeDataObj.getString("CML_SUB_CATEGORY");
                }
            }
            obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.FETCH_SPECIFIC_GROUP,"","",
                    "","","","");
            JSONObject dataArrInnerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            dataArrInnerObj.remove(ConstantsObjects.CALMAIL_OBJECT);
            dataArrInnerObj.put(ConstantsObjects.CONTACT_USER,this.officialEmail);
            Repository rr=Repository.getRepository();
            if(rr!=null){
                Login login=rr.getLoginData();
                if(login!=null){
                    deptId=login.getDeptId();
                    orgId=login.getOrgId();
                    projectId=login.getProjectId();
                }
            }
            dataArrInnerObj.put(ConstantsObjects.DEPT_ID_INNER,deptId);
            dataArrInnerObj.put(ConstantsObjects.ORG_ID_INNER,orgId);
            dataArrInnerObj.put(ConstantsObjects.ORG_PROJECT_ID_INNER,projectId);
            dataArrInnerObj.put(ConstantsObjects.KEY_TYPE,ConstantsObjects.KT_CONVERSATION);
            dataArrInnerObj.put(ConstantsObjects.SECTION_ID,cmlSubCategory+"#SEC_REPL_WIZ_0025");
            dataArrInnerObj.put(ConstantsObjects.SUB_KEY_TYPE,ConstantsObjects.SUB_KT_CONVERSATION_ONE_TO_ONE);
        }catch(JSONException e1){
            Log.e("Mdl_C_oto_crt_group1:",e1.toString());
        }catch(Exception e2){
            Log.e("Mdl_C_oto_crt_group2:",e2.toString());
        }
        Log.i("Mdl_C_oto_crt_group:",String.valueOf(obj)+" ..kk");
        if(GlobalClass.getAuthenticatedSyncSocket()!=null){
            GlobalClass.getAuthenticatedSyncSocket().emit(ConstantsObjects.ON_DEMAND_CALL,obj);
        }
    }
    public void getJSONObjectFetchBlockedContacts(){
        JSONObject obj=null;
        String unassignedProjectId="";
        try{
            Repository rr=Repository.getRepository();
            unassignedProjectId=rr.getPersonaWiseProjectId(ConstantsObjects.ROLE_UNASSIGNED);
            obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.FETCH_CONTACT,
                    "","",
                    "","","","");
            JSONObject innerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            innerObj.remove(ConstantsObjects.CALMAIL_OBJECT);
            innerObj.put(ConstantsObjects.LAST_ID_D_CAP,"");
            innerObj.put(ConstantsObjects.PARENT_ID,unassignedProjectId+"#SEC_REPL_WIZ_0029#TSK_CDE_GRP");
            innerObj.put(ConstantsObjects.KEY_TYPE,ConstantsObjects.KT_TSK);
            innerObj.put(ConstantsObjects.SUB_KEY_TYPE,ConstantsObjects.SUB_KT_BLOCKED_CONTACT);
            obj.remove(ConstantsObjects.MODULE_NAME);
            obj.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_NAME_VALUE_CRM);
        }catch(JSONException e){
            Log.e("Mdl_C_ftchBlckdCntcts1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_ftchBlckdCntcts2:",e.toString());
        }
        Log.i("Mdl_C_ftchBlckdCntcts:",String.valueOf(obj)+" ..kk");
        if(GlobalClass.getAuthenticatedSyncSocket()!=null){
            GlobalClass.getAuthenticatedSyncSocket().emit(ConstantsObjects.ON_DEMAND_CALL,obj);
        }
    }
    public void getJSONObjectUnblockContact(){
        JSONObject obj=null;
        try{
            obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.DELETE_CONTACT,
                    "","",
                    "","","","");
            JSONObject innerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            innerObj.remove(ConstantsObjects.CALMAIL_OBJECT);
            innerObj.put(ConstantsObjects.KEY_TYPE,this.keytype);
            //innerObj.put(ConstantsObjects.SUB_KEY_TYPE,ConstantsObjects.SUB_KT_BLOCKED_CONTACT);
            innerObj.put(ConstantsObjects.SUB_KEY_TYPE,this.subkeytype);
            innerObj.put(ConstantsObjects.KEY_VAL,this.keyval);
            innerObj.put(ConstantsObjects.ACTIVE_STATUS_DATA_ARRAY_INNER,5);
            /*obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.UNBLOCK_CONTACT,
                    "","",
                    "","","","");
            JSONObject innerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            innerObj.remove(ConstantsObjects.CALMAIL_OBJECT);
            JSONObject calmailUpdateObj=CommonMethods.getCalmailUpdateObject2();
            calmailUpdateObj.put(ConstantsObjects.CML_ACCEPTED,1);
            innerObj.put(ConstantsObjects.CALMAIL_UPDATE,calmailUpdateObj);
            innerObj.put(ConstantsObjects.KEY_TYPE,kt);
            innerObj.put(ConstantsObjects.KEY_VAL,keyval);
            innerObj.put(ConstantsObjects.SUB_KEY_TYPE,skt);
            JSONObject extraParamObj=CommonMethods.getExtraParamObject(1);
            extraParamObj.remove(ConstantsObjects.MODULE_NAME);
            extraParamObj.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_NAME_VALUE_CRM);
            obj.put(ConstantsObjects.EXTRA_PARAM,extraParamObj);*/
            obj.remove(ConstantsObjects.MODULE_NAME);
            obj.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_NAME_VALUE_CRM);
            obj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_DELETE);
        }catch(JSONException e){
            Log.e("Mdl_C_unblckCntct1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_unblckCntct2:",e.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_C_unblckCntct:",String.valueOf(obj)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(obj,ConstantsObjects.SERVER_OPERATION);
        }
    }
    //JsonObjectContact.java
    public void getJSONObjectFetchAllContacts(){
        JSONObject mainObj=new JSONObject();
        String userId,socketId,requestId;
        userId=socketId=requestId="";
        try{
            JSONArray dataArr=new JSONArray();
            mainObj.put(ConstantsObjects.DATA_ARRAY,dataArr);
            JSONObject dataArrInnerObj=new JSONObject();
            dataArr.put(0,dataArrInnerObj);
            JSONArray actionArr=CommonMethods.getActionArray(ConstantsObjects.FETCH_ALL_CONTACT);
            dataArrInnerObj.put(ConstantsObjects.ACTION_ARRAY,actionArr);
            JSONObject filterObj=new JSONObject();
            dataArrInnerObj.put(ConstantsObjects.FILTER_OBJECT,filterObj);
            JSONObject fetchKeysObj=new JSONObject();
            filterObj.put(ConstantsObjects.FETCH_KEYS,fetchKeysObj);
            Repository rr=Repository.getRepository();
            if(rr!=null){
                ArrayList<String> userUrmLst=rr.getUserUrmProjectIdList();
                if(userUrmLst!=null){
                    if(userUrmLst.size()>0){
                        for(int i=0;i<userUrmLst.size();i++){
                            fetchKeysObj.put(userUrmLst.get(i)+"#SEC_REPL_WIZ_0029#TSK_CDE_GRP","");
                        }
                    }
                }
            }
            dataArrInnerObj.put(ConstantsObjects.KEY_TYPE,ConstantsObjects.KT_FETCH_ALL_CONTACTS);
            dataArrInnerObj.put(ConstantsObjects.SUB_KEY_TYPE,ConstantsObjects.SUB_KT_FETCH_CONTACT);
            mainObj.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_VALUE_CONTACT);
            requestId=CommonMethods.getRequestId();
            mainObj.put(ConstantsObjects.REQUEST_ID,requestId);
            userId=GlobalClass.getUserId();
            mainObj.put(ConstantsObjects.USER_ID,userId);
            socketId=CommonMethods.getSocketId();
            Log.i("Mdl_C_socketId:",socketId+" ..kk");
            mainObj.put(ConstantsObjects.SOCKET_ID,socketId);
        }catch(JSONException e){
            Log.e("Mdl_C_ftch_all_cntct1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_ftch_all_cntct2:",e.toString());
        }
        Log.i("Mdl_C_ftch_all_cntct:",String.valueOf(mainObj)+" ..kk");
        if(GlobalClass.getAuthenticatedSyncSocket()!=null){
            GlobalClass.getAuthenticatedSyncSocket().emit(ConstantsObjects.ON_DEMAND_CALL,mainObj);
        }
    }
    public void getJSONObjectAddContact(JSONObject innerObj,String personaSelected){
        JSONObject obj=null;
        String firstName,lastName,officialEmail,personalEmail,
                personaProjectId,cmlRefId,cmlTempKeyVal,userId,projectId;
        firstName=lastName=officialEmail=personalEmail=personaProjectId
                =cmlRefId=cmlTempKeyVal=userId=projectId="";
        userId=GlobalClass.getUserId();
        cmlTempKeyVal="#"+ConstantsObjects.SUB_KT_CONTACT+"#"
                        +userId+":"+System.currentTimeMillis()
                        +"_"+System.currentTimeMillis()
                        +"_"+CommonMethods.getFiveDigitRandomNumber();
        Log.i("Mdl_C_tempKeyVal:",cmlTempKeyVal+" ..kk");
        if(innerObj!=null){
            try{
                Repository rr=Repository.getRepository();
                if(rr!=null){
                    Log.i("Mdl_C_personaInput:",personaSelected+" ..kk");
                    personaProjectId=rr.getPersonaWiseProjectId(personaSelected);
                    cmlRefId=personaProjectId+"#SEC_REPL_WIZ_0029#TSK_CDE_GRP";
                    Login login=rr.getLoginData();
                    if(login!=null){
                        projectId=login.getProjectId();
                    }
                }
                if(innerObj.has("CML_FIRST_NAME")){
                    firstName=innerObj.getString("CML_FIRST_NAME");
                }
                if(innerObj.has("CML_LAST_NAME")){
                    lastName=innerObj.getString("CML_LAST_NAME");
                }
                obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.ADD_CONTACT,
                        firstName+" "+lastName,
                        "",
                        ConstantsObjects.KT_TSK,ConstantsObjects.SUB_KT_CONTACT,cmlTempKeyVal,cmlRefId);
                JSONArray dataArr=obj.getJSONArray(ConstantsObjects.DATA_ARRAY);
                JSONObject kkObj=dataArr.getJSONObject(0);
                JSONObject calmailObj=kkObj.getJSONObject(ConstantsObjects.CALMAIL_OBJECT);
                calmailObj.put(ConstantsObjects.ACTIVE_STATUS,1);
                calmailObj.put(ConstantsObjects.CML_ACCEPTED,1);
                calmailObj.put(ConstantsObjects.CML_DESCRIPTION,"");
                calmailObj.put(ConstantsObjects.CML_FIRST_NAME,firstName);
                calmailObj.put(ConstantsObjects.CML_IMAGE_PATH,new JSONArray());
                calmailObj.put(ConstantsObjects.CML_LAST_NAME,lastName);
                calmailObj.put(ConstantsObjects.CML_NICK_NAME,"");
                if(innerObj.has(ConstantsObjects.CML_OFFICIAL_EMAIL)){
                    officialEmail=innerObj.getString(ConstantsObjects.CML_OFFICIAL_EMAIL);
                }
                if(innerObj.has(ConstantsObjects.CML_PERSONAL_EMAIL)){
                    personalEmail=innerObj.getString(ConstantsObjects.CML_PERSONAL_EMAIL);
                }
                calmailObj.put(ConstantsObjects.CML_OFFICIAL_EMAIL,officialEmail);
                calmailObj.put(ConstantsObjects.CML_PERSONAL_EMAIL,personalEmail);
                calmailObj.remove(ConstantsObjects.CML_SUB_CATEGORY);
                calmailObj.put(ConstantsObjects.CML_SUB_CATEGORY,personaProjectId);
                calmailObj.remove(ConstantsObjects.DEPT_ID_INNER);
                calmailObj.put(ConstantsObjects.DEPT_ID_INNER,"");
                calmailObj.remove(ConstantsObjects.DEPT_PROJECT_ID);
                calmailObj.put(ConstantsObjects.DEPT_PROJECT_ID,projectId);
                calmailObj.put(ConstantsObjects.IS_PRIMARY,true);
                calmailObj.remove(ConstantsObjects.ORG_ID_INNER);
                calmailObj.put(ConstantsObjects.ORG_ID_INNER,"");
                calmailObj.put(ConstantsObjects.SYNC_PENDING_STATUS,0);
                calmailObj.put(ConstantsObjects.TN,"CAL_MAIL");
                calmailObj.put(ConstantsObjects.USER_LAST_MODIFIED_ON,CommonMethods.getCurrentTimeStampInZuluFormat());
                obj.remove(ConstantsObjects.MODULE_NAME);
                obj.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_VALUE_CONTACT);
                obj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
                obj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_INSERT);
            }catch(JSONException e){
                Log.e("Mdl_C_AddCntct1:",e.toString());
            }catch(Exception e){
                Log.e("Mdl_C_AddCntct2:",e.toString());
            }
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_C_add_cntct:",String.valueOf(obj)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(obj,ConstantsObjects.SERVER_OPERATION);
        }
    }
    public void getJSONObjectDeleteContact(ArrayList<Contact> contactArrayList){
        JSONObject mainObj=new JSONObject();
        String officialEmailId,kt,keyval,skt,requestId,socketId,userId;
        requestId=socketId=userId="";
        try{
            mainObj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            mainObj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_DELETE);
            JSONArray dataArr=new JSONArray();
            mainObj.put(ConstantsObjects.DATA_ARRAY,dataArr);
            if(contactArrayList!=null){
                if(contactArrayList.size()>0){
                    for(int i=0;i<contactArrayList.size();i++){
                        Contact cntct=contactArrayList.get(i);
                        if(cntct!=null){
                            officialEmailId=kt=keyval=skt="";
                            officialEmailId=cntct.getOfficialEmail();
                            kt=cntct.getKeytype();
                            keyval=cntct.getKeyval();
                            skt=cntct.getSubkeytype();
                            JSONObject innerObj=new JSONObject();
                            dataArr.put(i,innerObj);
                            JSONArray actionArr=CommonMethods.getActionArray(ConstantsObjects.DELETE_CONTACT);
                            innerObj.put(ConstantsObjects.ACTION_ARRAY,actionArr);
                            innerObj.put(ConstantsObjects.ACTIVE_STATUS_DATA_ARRAY_INNER,5);
                            JSONArray essentialLst=new JSONArray();
                            essentialLst.put(0,officialEmailId);
                            innerObj.put(ConstantsObjects.ESSENTIAL_LIST_ARRAY,essentialLst);
                            innerObj.put(ConstantsObjects.KEY_TYPE,kt);
                            innerObj.put(ConstantsObjects.KEY_VAL,keyval);
                            innerObj.put(ConstantsObjects.SUB_KEY_TYPE,skt);
                        }
                    }
                }
            }
            JSONObject extraParamObj=new JSONObject();
            mainObj.put(ConstantsObjects.EXTRA_PARAM,extraParamObj);
            mainObj.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_VALUE_CONTACT);
            requestId=CommonMethods.getRequestId();
            mainObj.put(ConstantsObjects.REQUEST_ID,requestId);
            socketId=CommonMethods.getSocketId();
            mainObj.put(ConstantsObjects.SOCKET_ID,socketId);
            userId=GlobalClass.getUserId();
            mainObj.put(ConstantsObjects.USER_ID,userId);
        }catch(JSONException e){
            Log.i("Mdl_C_dlt_cntctLst1:",e.toString());
        }catch(Exception e){
            Log.i("Mdl_C_dlt_cntctLst2:",e.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_C_dlt_cntctLst:",String.valueOf(mainObj)+" ..kk");
        Log.i("Mdl_C_dlt_cntctLst:",String.valueOf(mainObj)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(mainObj,ConstantsObjects.SERVER_OPERATION);
        }
    }
    public void searchUsersWhenCreatingEvent(String searchString){
        JSONObject obb=new JSONObject();
        String projectId,userId;
        projectId=userId="";
        try{
            userId=GlobalClass.getUserId();
            Repository rr=Repository.getRepository();
            if(rr!=null){
                Login login=rr.getLoginData();
                if(login!=null){
                    projectId=login.getProjectId();
                }
            }
            JSONArray dataArr=new JSONArray();
            obb.put(ConstantsObjects.DATA_ARRAY,dataArr);
            JSONObject innerObb=new JSONObject();
            dataArr.put(0,innerObb);
            innerObb.put(ConstantsObjects.ORG_PROJECT_ID_INNER,projectId);
            JSONArray actionArr=CommonMethods.getActionArray(ConstantsObjects.FETCH_CONTACT_USERS);
            innerObb.put(ConstantsObjects.ACTION_ARRAY,actionArr);
            innerObb.put(ConstantsObjects.CONTACT_ID_INNER,searchString);
            innerObb.put(ConstantsObjects.KEY_TYPE,"TSK");
            innerObb.put(ConstantsObjects.LAST_ID_D_CAP,"");
            innerObb.put(ConstantsObjects.OFFSET,0);
            innerObb.put(ConstantsObjects.SUB_KEY_TYPE,"TSK_USR");
            obb.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_VALUE_CONTACT);
            obb.put(ConstantsObjects.REQUEST_ID,CommonMethods.getRequestId());
            obb.put(ConstantsObjects.SOCKET_ID,CommonMethods.getSocketId());
            obb.put(ConstantsObjects.USER_ID,userId);
        }catch(JSONException e){
            Log.i("Mdl_C_searchUsrsEvnt1:",String.valueOf(obb)+" ..kk");
        }catch(Exception e){
            Log.i("Mdl_C_searchUsrsEvnt2:",String.valueOf(obb)+" ..kk");
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_C_searchUsrsEvnt:",String.valueOf(obb)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(obb,ConstantsObjects.ON_DEMAND_CALL);
        }
    }
}