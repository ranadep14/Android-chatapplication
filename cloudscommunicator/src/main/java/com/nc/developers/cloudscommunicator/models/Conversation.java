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
import com.nc.developers.cloudscommunicator.utils.CheckInternetConnectionCommunicator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

@Entity(tableName=ConstantsClass.TBL_CONVERSATION)
public class Conversation{

    @ColumnInfo(name=ConstantsClass.COMMON_ACTIVE_STATUS)
    private int activeStatus;

    @ColumnInfo(name=ConstantsClass.CML_TITLE)
    private String cmlTitle;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name=ConstantsClass.COMMON_KEY_VAL)
    private String keyval;

    @ColumnInfo(name=ConstantsClass.CML_ACCEPTED)
    public int cmlAccepted;

    @ColumnInfo(name=ConstantsClass.LAST_MODIFIED_ON)
    private String lastModifiedOn;

    @ColumnInfo(name=ConstantsClass.COMMON_CML_REF_ID)
    private String cmlRefId;

    @ColumnInfo(name=ConstantsClass.CML_UNREAD_COUNT)
    private int unreadCount;

    @ColumnInfo(name=ConstantsClass.CREATEDON)
    private String createdOn;

    @ColumnInfo(name=ConstantsClass.LATEST_MSG_OBJ)
    private String latestMessage;

    @ColumnInfo(name=ConstantsClass.OWNER_ID)
    private String ownerId;

    @ColumnInfo(name=ConstantsClass.COMMON_KEY_TYPE)
    private String keyType;

    @ColumnInfo(name=ConstantsClass.COMMON_SUB_KEY_TYPE)
    private String subKeyType;

    @ColumnInfo(name=ConstantsClass.CREATED_BY)
    private String createdBy;

    @Ignore
    @ColumnInfo(name=ConstantsClass.CONVERSATION_SELECTED)
    private boolean conversationSelected=false;

    @ColumnInfo(name=ConstantsClass.LINKUP_ID)
    private String linkupId;

    @ColumnInfo(name=ConstantsClass.IMAGE_PATH)
    private String cmlImagePath;

    @ColumnInfo(name=ConstantsClass.RESTDATA)
    private String completeData;

    public int getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(int activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getCmlTitle() {
        return cmlTitle;
    }

    public void setCmlTitle(String cmlTitle) {
        this.cmlTitle = cmlTitle;
    }

    @NonNull
    public String getKeyval(){
        return keyval;
    }

    public void setKeyval(@NonNull String keyval){
        this.keyval = keyval;
    }

    public int getCmlAccepted() {
        return cmlAccepted;
    }

    public void setCmlAccepted(int cmlAccepted) {
        this.cmlAccepted = cmlAccepted;
    }

    public String getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(String lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public String getCmlRefId() {
        return cmlRefId;
    }

    public void setCmlRefId(String cmlRefId) {
        this.cmlRefId = cmlRefId;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getSubKeyType() {
        return subKeyType;
    }

    public void setSubKeyType(String subKeyType) {
        this.subKeyType = subKeyType;
    }

    public String getCompleteData() {
        return completeData;
    }

    public void setCompleteData(String completeData) {
        this.completeData = completeData;
    }

    public String getCreatedBy(){
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isConversationSelected(){
        return conversationSelected;
    }

    public void setConversationSelected(boolean conversationSelected){
        this.conversationSelected=conversationSelected;
    }

    public String getLinkupId(){
        return linkupId;
    }

    public void setLinkupId(String linkupId){
        this.linkupId=linkupId;
    }

    public String getCmlImagePath(){
        return cmlImagePath;
    }

    public void setCmlImagePath(String cmlImagePath){
        this.cmlImagePath=cmlImagePath;
    }

    /*======================================================================================================================
            model class changes==>>
        =======================================================================================================================*/
    public void getJSONObjectMakeAdminOrRemoveAdmin(Invitee invitee,String string){
        JSONObject obj=null;
        String actionArrayString="";
        JSONObject inviteeObj=null;
        if(string.equals("make admin")){
            actionArrayString=ConstantsObjects.MAKE_ADMIN;
        }
        if(string.equals("remove admin")){
            actionArrayString=ConstantsObjects.REMOVE_ADMIN;
        }
        try{
            if(invitee!=null){
                String completeString=invitee.getCompleteData();
                inviteeObj=new JSONObject(completeString);
                Log.i("Mdl_C_inviteeObj:",String.valueOf(inviteeObj)+" ..kk");
                inviteeObj.remove(ConstantsObjects.IDE_DESIGNATION);
                if(string.equals("make admin")){
                    JSONArray ideDesignationArr=new JSONArray();
                    ideDesignationArr.put(0,ConstantsObjects.IDE_DESIGNATION_VALUE);
                    inviteeObj.put(ConstantsObjects.IDE_DESIGNATION,ideDesignationArr);
                }
                if(string.equals("remove admin")){
                    JSONArray ideDesignationArr=new JSONArray();
                    inviteeObj.put(ConstantsObjects.IDE_DESIGNATION,ideDesignationArr);
                }
            }
            obj=CommonMethods.getPrimaryJsonObject(actionArrayString,
                    "","",
                    "","","","");
            JSONObject innerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            innerObj.remove(ConstantsObjects.CALMAIL_OBJECT);
            JSONObject calmailUpdateObj=new JSONObject();
            calmailUpdateObj.put(ConstantsObjects.SYNC_PENDING_STATUS,0);
            innerObj.put(ConstantsObjects.CALMAIL_UPDATE,calmailUpdateObj);
            JSONArray essentialLstArr=CommonMethods.getEssentialListArray(this.ownerId);
            innerObj.put(ConstantsObjects.ESSENTIAL_LIST_ARRAY,essentialLstArr);
            JSONArray invitationUpdateArr=new JSONArray();
            innerObj.put(ConstantsObjects.INVITATION_UPDATE,invitationUpdateArr);
            invitationUpdateArr.put(0,inviteeObj);
            innerObj.put(ConstantsObjects.KEY_TYPE,this.keyType);
            innerObj.put(ConstantsObjects.KEY_VAL,this.keyval);
            innerObj.put(ConstantsObjects.SUB_KEY_TYPE,this.subKeyType);
            JSONObject extraParam=CommonMethods.getExtraParamObject(0);
            obj.put(ConstantsObjects.EXTRA_PARAM,extraParam);
            obj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            obj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_UPDATE);
        }catch(JSONException e){
            Log.e("Mdl_C_mk/rm_admn1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_mk/rm_admn2:",e.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_C_mk/rm_admn:",String.valueOf(obj)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(obj,ConstantsObjects.SERVER_OPERATION);
        }
    }
    public void getJSONObjectDeleteConversations(ArrayList<Conversation> conversationList){
        JSONObject mainObj=new JSONObject();
        String ownerId,keyval,requestId,socketId,userId;
        ownerId=keyval=requestId=socketId=userId="";
        try{
            mainObj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            mainObj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_DELETE);
            JSONArray dataArr=new JSONArray();
            mainObj.put(ConstantsObjects.DATA_ARRAY,dataArr);
            if(conversationList!=null){
                if(conversationList.size()>0){
                    for(int u=0;u<conversationList.size();u++){
                        com.nc.developers.cloudscommunicator.models.Conversation singleConversation=conversationList.get(u);
                        if(singleConversation!=null){
                            ownerId=keyval="";
                            ownerId=singleConversation.getOwnerId();
                            keyval=singleConversation.getKeyval();
                            JSONObject innerObj=new JSONObject();
                            JSONArray actionArr= CommonMethods.getActionArray(ConstantsObjects.SNIP_CONVERSATION);
                            innerObj.put(ConstantsObjects.ACTION_ARRAY,actionArr);
                            innerObj.put(ConstantsObjects.ACTIVE_STATUS_DATA_ARRAY_INNER,5);
                            JSONArray essentialLstArr=CommonMethods.getEssentialListArray(ownerId);
                            innerObj.put(ConstantsObjects.ESSENTIAL_LIST_ARRAY,essentialLstArr);
                            innerObj.put(ConstantsObjects.KEY_TYPE,ConstantsObjects.KT_CONVERSATION);
                            innerObj.put(ConstantsObjects.KEY_VAL,keyval);
                            innerObj.put(ConstantsObjects.SUB_KEY_TYPE,ConstantsObjects.SUB_KT_CONVERSATION_ARRAY1);
                            dataArr.put(u,innerObj);
                        }
                    }
                }
            }
            mainObj.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_NAME_VALUE);
            requestId=CommonMethods.getRequestId();
            mainObj.put(ConstantsObjects.REQUEST_ID,requestId);
            socketId=CommonMethods.getSocketId();
            mainObj.put(ConstantsObjects.SOCKET_ID,socketId);
            userId= GlobalClass.getUserId();
            mainObj.put(ConstantsObjects.USER_ID,userId);
        }catch(JSONException e){
            Log.e("Mdl_C_dltConversation1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_dltConversation2:",e.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_C_dltConversation:",String.valueOf(mainObj)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(mainObj,ConstantsObjects.SERVER_OPERATION);
        }
    }
    public void getJSONObjectConversationResetUNReadCount(){
        JSONObject obj=null;
        try{
            obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.UPDATE_CONVERSATION,
                    "","",
                    "","","","");
            JSONObject innerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            innerObj.remove(ConstantsObjects.CALMAIL_OBJECT);
            JSONObject calmailUpdateObj=new JSONObject();
            innerObj.put(ConstantsObjects.CALMAIL_UPDATE,calmailUpdateObj);
            calmailUpdateObj.put(ConstantsObjects.CML_UNREAD_COUNT,0);
            calmailUpdateObj.put(ConstantsObjects.SYNC_PENDING_STATUS,0);
            JSONArray essentialLstArr=CommonMethods.getEssentialListArray(this.ownerId);
            innerObj.put(ConstantsObjects.ESSENTIAL_LIST_ARRAY,essentialLstArr);
            innerObj.put(ConstantsObjects.KEY_TYPE,this.keyType);
            innerObj.put(ConstantsObjects.KEY_VAL,this.keyval);
            innerObj.put(ConstantsObjects.SUB_KEY_TYPE,this.subKeyType);
            JSONObject extraParamObj=CommonMethods.getExtraParamObject(0);
            obj.put(ConstantsObjects.EXTRA_PARAM,extraParamObj);
            obj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            obj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_UPDATE);
        }catch(JSONException e){
            Log.e("Mdl_C_rstCnt1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_rstCnt2:",e.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_C_rstCnt:",String.valueOf(obj)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(obj,ConstantsObjects.SERVER_OPERATION);
        }
    }
    public void getJsonObjectClearChat(){
        JSONObject obj=null;
        try{
            obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.ERASE_CONVERSATION,
                    "","",
                    "","","","");
            JSONObject innerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            innerObj.remove(ConstantsObjects.CALMAIL_OBJECT);
            JSONObject calmailUpdateObj=CommonMethods.getCalmailUpdateObject2();
            innerObj.put(ConstantsObjects.CALMAIL_UPDATE,calmailUpdateObj);
            JSONArray essentialLstArr=new JSONArray();
            innerObj.put(ConstantsObjects.ESSENTIAL_LIST_ARRAY,essentialLstArr);
            JSONObject essentialInnerObj=new JSONObject();
            essentialLstArr.put(0,essentialInnerObj);
            essentialInnerObj.put(ConstantsObjects.CREATORS_ID,this.ownerId);
            essentialInnerObj.put(ConstantsObjects.LINKUP_ID,this.linkupId);
            innerObj.put(ConstantsObjects.KEY_TYPE,this.keyType);
            innerObj.put(ConstantsObjects.KEY_VAL,this.keyval);
            innerObj.put(ConstantsObjects.SUB_KEY_TYPE,this.subKeyType);
            JSONObject extraParamObj=CommonMethods.getExtraParamObject(1);
            obj.put(ConstantsObjects.EXTRA_PARAM,extraParamObj);
            obj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            obj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_UPDATE);
        }catch(JSONException e1){
            Log.e("Mdl_C_clearChat1:",e1.toString());
        }catch(Exception e2){
            Log.e("Mdl_C_clearChat2:",e2.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_C_clearChat:",String.valueOf(obj)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(obj,ConstantsObjects.SERVER_OPERATION);
        }
    }
    public void getJsonObjectDeleteOneToOneConversation(ArrayList<Conversation> conversations){
        JSONObject mainObj=new JSONObject();
        try{
            String userId,ownerId,linkupId,keytype,keyval,subkeytype;
            userId="";
            userId=GlobalClass.getUserId();
            mainObj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            mainObj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_UPDATE);
            JSONArray dataArr=new JSONArray();
            mainObj.put(ConstantsObjects.DATA_ARRAY,dataArr);
            if(conversations!=null){
                if(conversations.size()>0){
                    for(int i=0;i<conversations.size();i++){
                        Conversation singleConversation=conversations.get(i);
                        if(singleConversation!=null){
                            ownerId=linkupId=keytype=keyval=subkeytype="";
                            keytype=singleConversation.getKeyType();
                            keyval=singleConversation.getKeyval();
                            subkeytype=singleConversation.getSubKeyType();
                            ownerId=singleConversation.getOwnerId();
                            linkupId=singleConversation.getLinkupId();
                            JSONObject innerObj=new JSONObject();
                            dataArr.put(i,innerObj);
                            JSONArray actionArr=CommonMethods.getActionArray(ConstantsObjects.ERASE_CONVERSATION);
                            innerObj.put(ConstantsObjects.ACTION_ARRAY,actionArr);
                            JSONObject calmailUpdateObj=new JSONObject();
                            innerObj.put(ConstantsObjects.CALMAIL_UPDATE,calmailUpdateObj);
                            calmailUpdateObj.put(ConstantsObjects.CML_ACCEPTED,4);
                            calmailUpdateObj.put(ConstantsObjects.LAST_MODIFIED_BY,userId);
                            calmailUpdateObj.put(ConstantsObjects.LAST_MODIFIED_ON,CommonMethods.getCurrentTimeStampInZuluFormat());
                            calmailUpdateObj.put(ConstantsObjects.SYNC_PENDING_STATUS,0);
                            JSONArray essentialLstArr=new JSONArray();
                            JSONObject essentialLstArrInnerObj=new JSONObject();
                            essentialLstArr.put(0,essentialLstArrInnerObj);
                            essentialLstArrInnerObj.put(ConstantsObjects.CREATORS_ID,ownerId);
                            essentialLstArrInnerObj.put(ConstantsObjects.LINKUP_ID,linkupId);
                            innerObj.put(ConstantsObjects.ESSENTIAL_LIST_ARRAY,essentialLstArr);
                            innerObj.put(ConstantsObjects.KEY_TYPE,keytype);
                            innerObj.put(ConstantsObjects.KEY_VAL,keyval);
                            innerObj.put(ConstantsObjects.SUB_KEY_TYPE,subkeytype);
                        }
                    }
                }
            }
            JSONObject extraParamObj=CommonMethods.getExtraParamObject(1);
            mainObj.put(ConstantsObjects.EXTRA_PARAM,extraParamObj);
            mainObj.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_NAME_VALUE);
            mainObj.put(ConstantsObjects.REQUEST_ID,CommonMethods.getRequestId());
            mainObj.put(ConstantsObjects.SOCKET_ID,CommonMethods.getSocketId());
            mainObj.put(ConstantsObjects.USER_ID,userId);
        }catch(JSONException e){
            Log.e("Mdl_C_dltOneToOne1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_dltOneToOne2:",e.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_C_dltOneToOne:",String.valueOf(mainObj)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(mainObj,ConstantsObjects.SERVER_OPERATION);
        }
    }
    public void getJSONObjectFetchAllConversations(String type,String next){
        JSONObject obj=null;
        try{
            obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.FETCH_CONVERSATION_FILTER,
                    "","",
                    "","","","");
            JSONObject filterObj=new JSONObject();
            filterObj.put(ConstantsObjects.CML_IS_ACTIVE,false);
            if(type!=null){
                if(type.equals("archive")){
                    filterObj.put("isArchive",true);
                }
                if(type.equals("unarchive")){
                    filterObj.put("isArchive",false);
                }
            }
            JSONObject lastIdObj=new JSONObject();
            if(next!=null){
                if(next.length()>0 && next.equals("next")){
                    JSONObject obb=GlobalClass.getConversationLastIdObject();
                    if(obb!=null){
                        String key=obb.keys().next();
                        int value=obb.getInt(key);
                        Log.i("Mdl_C_nxtKey:",key+" ..kk");
                        Log.i("Mdl_C_nxtValue:",String.valueOf(value)+" ..kk");
                        lastIdObj.put(key,value);
                    }
                }
            }
            filterObj.put(ConstantsObjects.LAST_ID_OBJECT,lastIdObj);
            filterObj.put(ConstantsObjects.SORT_FIELD,ConstantsObjects.SORT_FIELD_VALUE);
            JSONArray sktArr=new JSONArray();
            sktArr.put(0,ConstantsObjects.SUB_KT_CONVERSATION_ARRAY1);
            sktArr.put(1,"TSK_SCONV_LST");
            filterObj.put(ConstantsObjects.SUB_KEY_TYPE_ARRAY,sktArr);
            JSONObject dataArrayInnerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            if(dataArrayInnerObj.has(ConstantsObjects.CALMAIL_OBJECT)){
                dataArrayInnerObj.remove(ConstantsObjects.CALMAIL_OBJECT);
            }
            dataArrayInnerObj.put(ConstantsObjects.FILTER_OBJECT,filterObj);
            dataArrayInnerObj.put(ConstantsObjects.KEY_TYPE,ConstantsObjects.KT_CONVERSATION);
            JSONArray sectionIdsArr=new JSONArray();
            Repository rr=Repository.getRepository();
            if(rr!=null){
                ArrayList<String> strings=rr.getUserUrmProjectIdList();
                if(strings!=null){
                    if(strings.size()>0){
                        for(int i=0;i<strings.size();i++){
                            String str=strings.get(i);
                            str=str+"#SEC_REPL_WIZ_0025";
                            if(str!=null){
                                sectionIdsArr.put(i,str);
                            }
                        }
                    }
                }
            }
            dataArrayInnerObj.put(ConstantsObjects.SECTION_IDS,sectionIdsArr);
            dataArrayInnerObj.put(ConstantsObjects.SUB_KEY_TYPE,ConstantsObjects.SUB_KT_CONVERSATION);
        }catch(JSONException e){
            Log.e("Mdl_C_ftch_all_conv1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_ftch_all_conv2:",e.toString());
        }
        Log.i("Mdl_C_ftch_all_conv:",String.valueOf(obj)+" ..kk");
        if(GlobalClass.getAuthenticatedSyncSocket()!=null){
            GlobalClass.getAuthenticatedSyncSocket().emit(ConstantsObjects.ON_DEMAND_CALL,obj);
        }
    }
    public void getJsonObjectSearchAllContact(String searchString,String searchWhat){
        String projectId,userId;
        projectId=userId="";
        JSONObject obj=new JSONObject();
        try{
            JSONArray dataArr=new JSONArray();
            obj.put(ConstantsObjects.DATA_ARRAY,dataArr);
            if(searchWhat!=null){
                if(searchWhat.equals("contact")){
                    JSONObject contactObj=getJsonObjectFetchContactUsers(searchString);
                    dataArr.put(0,contactObj);
                }
                if(searchWhat.equals("conversation")){
                    JSONObject contactObj=getJsonObjectFetchContactUsers(searchString);
                    JSONObject conversationObj=getJsonObjectSearchAllContact(searchString);
                    dataArr.put(0,contactObj);
                    dataArr.put(1,conversationObj);
                }
            }
            obj.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_VALUE_CONTACT);
            obj.put(ConstantsObjects.REQUEST_ID,CommonMethods.getRequestId());
            obj.put(ConstantsObjects.SOCKET_ID,CommonMethods.getSocketId());
            userId=GlobalClass.getUserId();
            obj.put(ConstantsObjects.USER_ID,userId);
        }catch(JSONException e){
            Log.e("Mdl_C_SrchAllUsrs1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_SrchAllUsrs2:",e.toString());
        }
        Log.i("Mdl_C_searchUsrsObj:",String.valueOf(obj)+" ..kk");
        boolean internetConnectionStatus=CheckInternetConnectionCommunicator.isInternetAvailable();
        Log.i("Mdl_C_searchUsrs:",String.valueOf(internetConnectionStatus)+" ..kk");
        if(GlobalClass.getAuthenticatedSyncSocket()!=null && internetConnectionStatus){
            GlobalClass.getAuthenticatedSyncSocket().emit(ConstantsObjects.ON_DEMAND_CALL,obj);
        }
    }
    private JSONObject getJsonObjectFetchContactUsers(String searchString){
        JSONObject obb=new JSONObject();
        String projectId="";
        try{
            Repository rr=Repository.getRepository();
            if(rr!=null){
                Login login=rr.getLoginData();
                if(login!=null){
                    projectId=login.getProjectId();
                }
            }
            obb.put(ConstantsObjects.ORG_PROJECT_ID_INNER,projectId);
            JSONArray actionArr=CommonMethods.getActionArray(ConstantsObjects.FETCH_CONTACT_USERS);
            obb.put(ConstantsObjects.ACTION_ARRAY,actionArr);
            obb.put(ConstantsObjects.CONTACT_ID_INNER,searchString);
            obb.put(ConstantsObjects.KEY_TYPE,"TSK");
            obb.put(ConstantsObjects.LAST_ID_D_CAP,"");
            obb.put(ConstantsObjects.OFFSET,0);
            obb.put(ConstantsObjects.SUB_KEY_TYPE,"TSK_USR");
        }catch(JSONException e){
            Log.e("Mdl_C_fetchCntctUsrs1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_fetchCntctUsrs2:",e.toString());
        }
        return obb;
    }
    private JSONObject getJsonObjectSearchAllContact(String searchString){
        JSONObject obb=new JSONObject();
        try{
            JSONArray actionArr=CommonMethods.getActionArray(ConstantsObjects.SEARCH_ALL_CONTACT);
            obb.put(ConstantsObjects.ACTION_ARRAY,actionArr);
            JSONObject filterObj=new JSONObject();
            obb.put(ConstantsObjects.FILTER_OBJECT,filterObj);
            filterObj.put(ConstantsObjects.CML_TITLE,searchString);
            JSONObject fetchKeysObject=new JSONObject();
            filterObj.put(ConstantsObjects.FETCH_KEYS,fetchKeysObject);
            Repository rr=Repository.getRepository();
            if(rr!=null){
                ArrayList<String> strings=rr.getUserUrmProjectIdList();
                if(strings!=null){
                    if(strings.size()>0){
                        for(int i=0;i<strings.size();i++){
                            fetchKeysObject.put(strings.get(i)+"#SEC_REPL_WIZ_0029#TSK_CDE_GRP","");
                        }
                    }
                }
            }
            obb.put(ConstantsObjects.KEY_TYPE,ConstantsObjects.KT_TSK);
            obb.put(ConstantsObjects.SUB_KEY_TYPE,ConstantsObjects.SUB_KT_CONTACT);
        }catch(JSONException e){
            Log.e("Mdl_C_SrchAllCntct1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_SrchAllCntct2:",e.toString());
        }
        return obb;
    }
    public void getJsonObjectArchiveUnarchiveConversation(String operation){
        JSONObject obj=null;
        String actionArrayString="";
        try{
            if(operation!=null){
                if(operation.equals("archive")){
                    actionArrayString=ConstantsObjects.ARCHIVE_CONVERSATION;
                }
                if(operation.equals("unarchive")){
                    actionArrayString=ConstantsObjects.UNARCHIVE_CONVERSATION;
                }
            }
            obj=CommonMethods.getPrimaryJsonObject(actionArrayString,
                    "","",
                    "","","","");
            JSONObject innerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            innerObj.remove(ConstantsObjects.CALMAIL_OBJECT);
            JSONObject calmailUpdateObj=new JSONObject();
            if(operation!=null){
                if(operation.equals("archive")){
                    calmailUpdateObj.put(ConstantsObjects.ACTIVE_STATUS,9);
                }
                if(operation.equals("unarchive")){
                    calmailUpdateObj.put(ConstantsObjects.ACTIVE_STATUS,1);
                }
            }
            calmailUpdateObj.put(ConstantsObjects.SYNC_PENDING_STATUS,0);
            innerObj.put(ConstantsObjects.CALMAIL_UPDATE,calmailUpdateObj);
            JSONArray essentialListArr=CommonMethods.getEssentialListArray(this.ownerId);
            innerObj.put(ConstantsObjects.ESSENTIAL_LIST_ARRAY,essentialListArr);
            innerObj.put(ConstantsObjects.KEY_TYPE,this.keyType);
            innerObj.put(ConstantsObjects.KEY_VAL,this.keyval);
            innerObj.put(ConstantsObjects.SUB_KEY_TYPE,this.subKeyType);
            JSONObject extraParamObj=CommonMethods.getExtraParamObject(1);
            obj.put(ConstantsObjects.EXTRA_PARAM,extraParamObj);
            obj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            obj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_UPDATE);
        }catch(JSONException e){
            Log.e("Mdl_C_archiveByAdmin1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_archiveByAdmin2:",e.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_C_archiveByAdmin:",String.valueOf(obj)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(obj,ConstantsObjects.SERVER_OPERATION);
        }
    }
    public void getJsonObjectUpdateConversation(String action,String newTitle){
        JSONObject obj=null;
        try{
            obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.UPDATE_CONVERSATION,
                    "","",
                    "","","","");
            obj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            obj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_UPDATE);
            JSONObject dataArrInnerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            dataArrInnerObj.remove(ConstantsObjects.CALMAIL_OBJECT);
            JSONObject calmailUpdateObj=new JSONObject();
            calmailUpdateObj.put(ConstantsObjects.SYNC_PENDING_STATUS,0);
            if(action!=null){
                if(action.equals("rename")){
                    if(newTitle!=null){
                        calmailUpdateObj.put(ConstantsObjects.CML_TITLE,newTitle);
                    }
                }
                if(action.equals("important")){
                    calmailUpdateObj.put(ConstantsObjects.CML_STAR,1);
                }
                if(action.equals("unimportant")){
                    calmailUpdateObj.put(ConstantsObjects.CML_STAR,0);
                }
            }
            dataArrInnerObj.put(ConstantsObjects.CALMAIL_UPDATE,calmailUpdateObj);
            JSONArray essentialLstArr=CommonMethods.getEssentialListArray(this.ownerId);
            dataArrInnerObj.put(ConstantsObjects.ESSENTIAL_LIST_ARRAY,essentialLstArr);
            dataArrInnerObj.put(ConstantsObjects.KEY_TYPE,this.keyType);
            dataArrInnerObj.put(ConstantsObjects.KEY_VAL,this.keyval);
            dataArrInnerObj.put(ConstantsObjects.SUB_KEY_TYPE,this.subKeyType);
            JSONObject extraParamObj=null;
            if(action!=null){
                if(action.equals("rename")){
                    extraParamObj=CommonMethods.getExtraParamObject(1);
                }
                if(action.equals("important") || action.equals("unimportant")){
                    extraParamObj=CommonMethods.getExtraParamObject(0);
                }
            }
            obj.put(ConstantsObjects.EXTRA_PARAM,extraParamObj);
        }catch(JSONException e){
            Log.e("Mdl_C_updateCnvrstn1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_updateCnvrstn2:",e.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_C_updateCnvrstn:",String.valueOf(obj)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(obj,ConstantsObjects.SERVER_OPERATION);
        }
    }
    public void getJSONObjectFetchMessages(String lastMessageKeyval){
        JSONObject obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.FETCH_MESSAGE_FILTER,
                "","",
                "","","","");
        try{
            if(obj.has(ConstantsObjects.FROM)){
                obj.remove(ConstantsObjects.FROM);
            }
            if(obj.has(ConstantsObjects.ACTION)){
                obj.remove(ConstantsObjects.ACTION);
            }
            JSONObject dataArrInnerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            if(dataArrInnerObj.has(ConstantsObjects.CALMAIL_OBJECT)){
                dataArrInnerObj.remove(ConstantsObjects.CALMAIL_OBJECT);
            }
            JSONObject filterObj=new JSONObject();
            filterObj.put(ConstantsObjects.IS_ATTACHMENT,false);
            dataArrInnerObj.put(ConstantsObjects.FILTER_OBJECT,filterObj);
            dataArrInnerObj.put(ConstantsObjects.KEY_TYPE,ConstantsObjects.KT_TSK);
            //dataArrInnerObj.put(ConstantsObjects.LAST_ELEMENT_ID,"");
            dataArrInnerObj.put(ConstantsObjects.LAST_ELEMENT_ID,lastMessageKeyval);
            dataArrInnerObj.put(ConstantsObjects.SECTION_ID,this.linkupId);
            dataArrInnerObj.put(ConstantsObjects.SUB_KEY_TYPE,ConstantsObjects.SUB_KT_MESSAGE);
        }catch(JSONException e){
            Log.e("Mdl_C_fetchMsg1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_fetchMsg2:",e.toString());
        }
        Log.i("Mdl_C_fetchMsg:",String.valueOf(obj)+" ..kk");
        if(GlobalClass.getAuthenticatedSyncSocket()!=null){
            GlobalClass.getAuthenticatedSyncSocket().emit(ConstantsObjects.ON_DEMAND_CALL,obj);
        }
    }
    //below function for send message and reply message
    public void getJSONObjectSendMessages(String messageString,String str,
                                                       Messages singleMessage){
        String cmlTempKeyval,userId;
        cmlTempKeyval=userId="";
        userId=GlobalClass.getUserId();
        JSONObject obj=null;
        try{
            cmlTempKeyval=ConstantsObjects.SUB_KT_MESSAGE+"#"+userId+System.currentTimeMillis()+"_"
                    +CommonMethods.getFourDigitRandomNumber();
            Log.i("Mdl_C_linkupId:",this.linkupId+" ..kk");
            obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.ADD_MESSAGE,
                    messageString,
                    messageString,
                    ConstantsObjects.KT_TSK,ConstantsObjects.SUB_KT_MESSAGE,cmlTempKeyval,this.linkupId);
            obj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            obj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_INSERT);
            JSONObject innerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            JSONObject calmailObj=null;
            if(innerObj.has(ConstantsObjects.CALMAIL_OBJECT)){
                calmailObj=innerObj.getJSONObject(ConstantsObjects.CALMAIL_OBJECT);
                if(messageString!=null){
                    calmailObj.put(ConstantsObjects.CML_DESCRIPTION,messageString);
                }
                calmailObj.put(ConstantsObjects.CML_MESSAGE_INDEX,System.currentTimeMillis());
                if(this.subKeyType.equals(ConstantsObjects.SUB_KT_CONVERSATION_ARRAY1)){
                    calmailObj.put(ConstantsObjects.CML_PARENT_SUB_KEY_TYPE,"SEC_CONV");
                }
                if(this.subKeyType.equals(ConstantsObjects.SUB_KT_CONVERSATION_ONE_TO_ONE)){
                    calmailObj.put(ConstantsObjects.CML_PARENT_SUB_KEY_TYPE,"SEC_SCONV");
                }
                calmailObj.put(ConstantsObjects.CREATED_BY,userId);
                calmailObj.put(ConstantsObjects.CREATED_ON,CommonMethods.getCurrentTimeStampInZuluFormat());
                calmailObj.put(ConstantsObjects.GROUP_ID,this.ownerId);
                calmailObj.remove(ConstantsObjects.CML_SUB_CATEGORY);
                calmailObj.remove(ConstantsObjects.DEPT_ID_INNER);
                calmailObj.remove(ConstantsObjects.DEPT_PROJECT_ID);
                calmailObj.remove(ConstantsObjects.ORG_ID_INNER);
                if(str!=null){
                    if(str.equals("reply")){
                        if(singleMessage!=null){
                            String createdBy,message,keyval;
                            createdBy=message=keyval="";
                            calmailObj.put(ConstantsObjects.CML_ISREPLY,true);
                            createdBy=singleMessage.getCreatedBy();
                            calmailObj.put(ConstantsObjects.CML_PARENT_CREATED_BY,createdBy);
                            message=singleMessage.getCmlTitle();
                            calmailObj.put(ConstantsObjects.CML_PARENT_MSG_TITLE,message);
                            keyval=singleMessage.getKeyval();
                            calmailObj.put(ConstantsObjects.CML_PARENT_TASK_ID,keyval);
                        }
                    }
                }
            }
        }catch(JSONException e){
            Log.e("Mdl_C_sendMsg1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_sendMsg2:",e.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_C_sendMsg:",String.valueOf(obj)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(obj,ConstantsObjects.SERVER_OPERATION);
        }
    }
    // Todo:use this function instead, this function is working correctly
    public void getJSONObjectSendMessageAttchmnt(ArrayList<String> cmlImagePathList){
        JSONObject mainObj=new JSONObject();
        String projectId,userId,tempkeyval,deptId,orgId;
        projectId=userId=tempkeyval=deptId=orgId="";
        try{
            Repository rr=Repository.getRepository();
            Login login=null;
            if(rr!=null){
                login=rr.getLoginData();
                if(login!=null){
                    projectId=login.getProjectId();
                    deptId=login.getDeptId();
                    orgId=login.getOrgId();
                }
            }
            userId=GlobalClass.getUserId();
            mainObj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            mainObj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_INSERT);
            JSONArray dataArr=new JSONArray();
            mainObj.put(ConstantsObjects.DATA_ARRAY,dataArr);
            if(cmlImagePathList!=null && cmlImagePathList!=null
                    && cmlImagePathList!=null){
                for(int i=0;i<cmlImagePathList.size();i++){
                    String imagePath=cmlImagePathList.get(i);
                    if(imagePath!=null){
                        String fileNameWithExtention=imagePath.substring(imagePath.lastIndexOf("/")+1);
                        String attachmentType="";
                        if(fileNameWithExtention!=null){
                            if(fileNameWithExtention.contains("jpg") || fileNameWithExtention.contains("jpeg")
                                    || fileNameWithExtention.contains("png")){
                                attachmentType="image";
                            }
                            if(fileNameWithExtention.contains("pdf")){
                                attachmentType="pdf";
                            }
                            if(fileNameWithExtention.contains("zip")){
                                attachmentType="zip";
                            }
                        }
                        JSONObject innerObj=new JSONObject();
                        dataArr.put(i,innerObj);

                        JSONArray actionArr=CommonMethods.getActionArray(ConstantsObjects.ADD_MESSAGE);
                        innerObj.put(ConstantsObjects.ACTION_ARRAY,actionArr);

                        JSONObject calmailObj=new JSONObject();
                        innerObj.put(ConstantsObjects.CALMAIL_OBJECT,calmailObj);
                        calmailObj.put(ConstantsObjects.CML_DESCRIPTION,fileNameWithExtention);
                        calmailObj.put(ConstantsObjects.CML_IMAGE_PATH,imagePath);
                        calmailObj.put(ConstantsObjects.CML_MESSAGE_INDEX,System.currentTimeMillis());
                        if(subKeyType.equals(ConstantsObjects.SUB_KT_CONVERSATION_ARRAY1)){
                            calmailObj.put(ConstantsObjects.CML_PARENT_SUB_KEY_TYPE,"SEC_CONV");
                        }
                        if(subKeyType.equals(ConstantsObjects.SUB_KT_CONVERSATION_ONE_TO_ONE)){
                            calmailObj.put(ConstantsObjects.CML_PARENT_SUB_KEY_TYPE,"SEC_SCONV");
                        }
                        calmailObj.put(ConstantsObjects.CML_REF_ID,this.linkupId);
                        tempkeyval=ConstantsObjects.SUB_KT_SEND_MESSAGE_ATTACHMENT
                                +"#"+userId
                                +System.currentTimeMillis()+"_"+CommonMethods.getFourDigitRandomNumber();
                        calmailObj.put(ConstantsObjects.CML_TEMP_KEY_VAL,tempkeyval);
                        calmailObj.put(ConstantsObjects.CML_TITLE,fileNameWithExtention);
                        if(attachmentType.equals("image")){
                            if(fileNameWithExtention.contains("jpg") ||
                                    fileNameWithExtention.contains("jpeg")){
                                calmailObj.put(ConstantsObjects.CML_TYPE,"image/jpeg");
                            }
                            if(fileNameWithExtention.contains("png")){
                                calmailObj.put(ConstantsObjects.CML_TYPE,"image/png");
                            }
                        }
                        if(attachmentType.equals("pdf")){
                            calmailObj.put(ConstantsObjects.CML_TYPE,"application/pdf");
                        }
                        if(attachmentType.equals("zip")){
                            calmailObj.put(ConstantsObjects.CML_TYPE,"application/zip");
                        }
                        calmailObj.put(ConstantsObjects.CREATED_BY,userId);
                        calmailObj.put(ConstantsObjects.CREATED_ON,CommonMethods.getCurrentTimeStampInZuluFormat());
                        calmailObj.put(ConstantsObjects.GROUP_ID,this.ownerId);
                        calmailObj.put(ConstantsObjects.KEY_TYPE_INNER,ConstantsObjects.KT_TSK);
                        calmailObj.put(ConstantsObjects.ORG_PROJECT_ID_INNER,projectId);
                        calmailObj.put(ConstantsObjects.SUB_KEY_TYPE_INNER,ConstantsObjects.SUB_KT_SEND_MESSAGE_ATTACHMENT);
                        Log.i("Mdl_C_deptId:",deptId+" ..kk");
                        Log.i("Mdl_C_orgId:",orgId+" ..kk");
                        calmailObj.put(ConstantsObjects.DEPT_ID_INNER,deptId);
                        calmailObj.put(ConstantsObjects.ORG_ID_INNER,orgId);
                    }
                }
                mainObj.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_NAME_VALUE);
                mainObj.put(ConstantsObjects.REQUEST_ID,CommonMethods.getRequestId());
                mainObj.put(ConstantsObjects.SOCKET_ID,CommonMethods.getSocketId());
                mainObj.put(ConstantsObjects.USER_ID,userId);
            }
        }catch(JSONException e){
            Log.e("Mdl_C_msgAttchmnt1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_msgAttchmnt2:",e.toString());
        }
        Repository rr=Repository.getRepository();
        if(rr!=null){
            rr.emitRequestCall(mainObj,ConstantsObjects.SERVER_OPERATION);
        }
    }

    /*public void getJSONObjectSendMessageAttchmnt(ArrayList<String> cmlImagePathList){
        JSONObject mainObj=null;
        if(cmlImagePathList!=null){
            if(cmlImagePathList.size()>0){
                String tempkeyval,userId,deptId,
                        orgId,orgProjectId;
                tempkeyval=userId=deptId=orgId=orgProjectId="";
                userId=GlobalClass.getUserId();
                Login login=null;
                Repository repository=Repository.getRepository();
                if(repository!=null){
                    login=repository.getLoginData();
                }
                if(login!=null){
                    deptId=login.getDeptId();
                    orgId=login.getOrgId();
                    orgProjectId=login.getProjectId();
                }
                for(int i=0;i<cmlImagePathList.size();i++){
                    mainObj=new JSONObject();
                    String filePathLocal=cmlImagePathList.get(i);
                    if(filePathLocal!=null){
                        String fileNameWithExtention=filePathLocal.substring(filePathLocal.lastIndexOf("/")+1);
                        String attachmentType="";
                        if(fileNameWithExtention!=null){
                            if(fileNameWithExtention.contains("jpg") || fileNameWithExtention.contains("jpeg")
                                    || fileNameWithExtention.contains("png")){
                                attachmentType="image";
                            }
                            if(fileNameWithExtention.contains("pdf")){
                                attachmentType="pdf";
                            }
                            if(fileNameWithExtention.contains("zip")){
                                attachmentType="zip";
                            }
                        }
                        try{
                            mainObj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
                            mainObj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_INSERT);
                            JSONArray dataArr=new JSONArray();
                            mainObj.put(ConstantsObjects.DATA_ARRAY,dataArr);
                            JSONObject innerObb=new JSONObject();
                            dataArr.put(0,innerObb);
                            JSONArray actionArr=CommonMethods.getActionArray(ConstantsObjects.ADD_MESSAGE);
                            innerObb.put(ConstantsObjects.ACTION_ARRAY,actionArr);
                            JSONObject calmailObb=new JSONObject();
                            innerObb.put(ConstantsObjects.CALMAIL_OBJECT,calmailObb);
                            calmailObb.put(ConstantsObjects.CML_DESCRIPTION,fileNameWithExtention);
                            calmailObb.put(ConstantsObjects.CML_IMAGE_PATH,filePathLocal);
                            calmailObb.put(ConstantsObjects.CML_MESSAGE_INDEX,System.currentTimeMillis());
                            if(subKeyType.equals(ConstantsObjects.SUB_KT_CONVERSATION_ARRAY1)){
                                calmailObb.put(ConstantsObjects.CML_PARENT_SUB_KEY_TYPE,"SEC_CONV");
                            }
                            if(subKeyType.equals(ConstantsObjects.SUB_KT_CONVERSATION_ONE_TO_ONE)){
                                calmailObb.put(ConstantsObjects.CML_PARENT_SUB_KEY_TYPE,"SEC_SCONV");
                            }
                            calmailObb.put(ConstantsObjects.CML_REF_ID,this.linkupId);
                            tempkeyval=ConstantsObjects.SUB_KT_SEND_MESSAGE_ATTACHMENT
                                    +"#"+userId
                                    +System.currentTimeMillis()+"_"+CommonMethods.getFourDigitRandomNumber();
                            calmailObb.put(ConstantsObjects.CML_TEMP_KEY_VAL,tempkeyval);
                            calmailObb.put(ConstantsObjects.CML_TITLE,fileNameWithExtention);
                            if(attachmentType.equals("image")){
                                if(fileNameWithExtention.contains("jpg") ||
                                        fileNameWithExtention.contains("jpeg")){
                                    calmailObb.put(ConstantsObjects.CML_TYPE,"image/jpeg");
                                }
                                if(fileNameWithExtention.contains("png")){
                                    calmailObb.put(ConstantsObjects.CML_TYPE,"image/png");
                                }
                            }
                            if(attachmentType.equals("pdf")){
                                calmailObb.put(ConstantsObjects.CML_TYPE,"application/pdf");
                            }
                            if(attachmentType.equals("zip")){
                                calmailObb.put(ConstantsObjects.CML_TYPE,"application/zip");
                            }
                            calmailObb.put(ConstantsObjects.CREATED_BY,userId);
                            calmailObb.put(ConstantsObjects.CREATED_ON,CommonMethods.getCurrentTimeStampInZuluFormat());
                            calmailObb.put(ConstantsObjects.DEPT_ID_INNER,deptId);
                            calmailObb.put(ConstantsObjects.GROUP_ID,this.ownerId);
                            calmailObb.put(ConstantsObjects.KEY_TYPE_INNER,ConstantsObjects.KT_TSK);
                            calmailObb.put(ConstantsObjects.ORG_ID_INNER,orgId);
                            calmailObb.put(ConstantsObjects.ORG_PROJECT_ID_INNER,orgProjectId);
                            calmailObb.put(ConstantsObjects.SUB_KEY_TYPE_INNER,ConstantsObjects.SUB_KT_SEND_MESSAGE_ATTACHMENT);
                            mainObj.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_NAME_VALUE);
                            mainObj.put(ConstantsObjects.REQUEST_ID,CommonMethods.getRequestId());
                            mainObj.put(ConstantsObjects.SOCKET_ID,CommonMethods.getSocketId());
                            mainObj.put(ConstantsObjects.USER_ID,userId);
                        }catch(JSONException e){
                            Log.e("Mdl_C_msgAttchmnt1:",e.toString());
                        }catch(Exception e){
                            Log.e("Mdl_C_msgAttchmnt2:",e.toString());
                        }
                    }
                    Log.i("Mdl_C_SendAttchmntObb:",String.valueOf(mainObj)+ " ..kk");
                    repository=Repository.getRepository();
                    if(repository!=null){
                        repository.emitRequestCall(mainObj,ConstantsObjects.SERVER_OPERATION);
                    }
                    *//*try{
                        Thread.sleep(3000);
                    }catch(InterruptedException e){
                        Log.e("Mdl_C_msgAttchmntSlp1:",e.toString());
                    }catch(Exception e){
                        Log.e("Mdl_C_msgAttchmntSlp2:",e.toString());
                    }*//*
                }
            }
        }
    }*/

    public void getJSONObjectDeleteMessagesForMe(ArrayList<Messages> msgList){
        JSONObject mainObj=new JSONObject();
        String kt,keyval,skt,requestId,socketId,userId;
        kt=keyval=skt=requestId=socketId=userId="";
        try{
            mainObj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            mainObj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_DELETE);
            JSONArray dataArr=new JSONArray();
            mainObj.put(ConstantsObjects.DATA_ARRAY,dataArr);
            if(msgList!=null){
                if(msgList.size()>0){
                    for(int i=0;i<msgList.size();i++){
                        Messages singleMsg=msgList.get(i);
                        if(singleMsg!=null){
                            String completeString=singleMsg.getCompleteData();
                            if(completeString!=null){
                                JSONObject completeData=new JSONObject(completeString);
                                if(completeData!=null){
                                    kt=completeData.getString(ConstantsObjects.KEY_TYPE_INNER);
                                    keyval=completeData.getString(ConstantsObjects.KEY_VAL_INNER);
                                    skt=completeData.getString(ConstantsObjects.SUB_KEY_TYPE_INNER);
                                }
                            }
                            JSONObject dataArrInnerObj=new JSONObject();
                            dataArr.put(i,dataArrInnerObj);
                            JSONArray actionArr=CommonMethods.getActionArray(ConstantsObjects.SNIP_MESSAGE);
                            dataArrInnerObj.put(ConstantsObjects.ACTION_ARRAY,actionArr);
                            dataArrInnerObj.put(ConstantsObjects.ACTIVE_STATUS_DATA_ARRAY_INNER,5);
                            JSONArray essentialLstArr=CommonMethods.getEssentialListArray(this.ownerId);
                            dataArrInnerObj.put(ConstantsObjects.ESSENTIAL_LIST_ARRAY,essentialLstArr);
                            dataArrInnerObj.put(ConstantsObjects.KEY_TYPE,kt);
                            dataArrInnerObj.put(ConstantsObjects.KEY_VAL,keyval);
                            dataArrInnerObj.put(ConstantsObjects.SUB_KEY_TYPE,skt);
                        }
                    }
                }
            }
            JSONObject extraParamObj=new JSONObject();
            mainObj.put(ConstantsObjects.EXTRA_PARAM,extraParamObj);
            mainObj.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_NAME_VALUE);
            requestId=CommonMethods.getRequestId();
            mainObj.put(ConstantsObjects.REQUEST_ID,requestId);
            socketId=CommonMethods.getSocketId();
            mainObj.put(ConstantsObjects.SOCKET_ID,socketId);
            userId=GlobalClass.getUserId();
            mainObj.put(ConstantsObjects.USER_ID,userId);
        }catch(JSONException e){
            Log.e("Mdl_C_dltMsgForMe1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_dltMsgForMe2:",e.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_C_dltMsgForMe:",String.valueOf(mainObj)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(mainObj,ConstantsObjects.SERVER_OPERATION);
        }
    }
    public void getJSONObjectReplyMessageOneToOne(Messages originalMessage,
                                                  String replyString){
        JSONObject obj=null;
        String original_msg_keyval,userId,tempKeyval;
        original_msg_keyval=userId=tempKeyval="";
        try{
            if(originalMessage!=null){
                original_msg_keyval=originalMessage.getKeyval();
            }
            userId=GlobalClass.getUserId();
            tempKeyval=ConstantsObjects.SUB_KT_MESSAGE+"#"+userId+System.currentTimeMillis()+"_"
                    +CommonMethods.getFourDigitRandomNumber();
            obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.NESTED_MESSAGE,
                    replyString,"",
                    ConstantsObjects.KT_TSK,ConstantsObjects.SUB_KT_MESSAGE,tempKeyval,original_msg_keyval);
            JSONObject innerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            JSONObject calmailObj=innerObj.getJSONObject(ConstantsObjects.CALMAIL_OBJECT);
            calmailObj.put(ConstantsObjects.CML_DESCRIPTION,replyString);
            calmailObj.put(ConstantsObjects.CML_MESSAGE_INDEX,System.currentTimeMillis());
            calmailObj.put(ConstantsObjects.CREATED_BY,userId);
            calmailObj.put(ConstantsObjects.CREATED_ON,CommonMethods.getCurrentTimeStampInZuluFormat());
            calmailObj.put(ConstantsObjects.GROUP_ID,this.ownerId);
            calmailObj.remove(ConstantsObjects.CML_SUB_CATEGORY);
            calmailObj.remove(ConstantsObjects.DEPT_ID_INNER);
            calmailObj.remove(ConstantsObjects.DEPT_PROJECT_ID);
            calmailObj.remove(ConstantsObjects.ORG_ID_INNER);
            obj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            obj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_INSERT);
        }catch(JSONException e1){
            Log.e("Mdl_C_rply_oto1:",e1.toString());
        }catch(Exception e2){
            Log.e("Mdl_C_rply_oto2:",e2.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_C_rply_oto:",String.valueOf(obj)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(obj,ConstantsObjects.SERVER_OPERATION);
        }
    }
    public void getJSONObjectForwardMessage(ArrayList<Messages> selectedMessageList,
                                            ArrayList<Conversation> targetConversations){
        JSONObject obj=null;
        String keyval,msgString,kt,skt,projectId,cmlType,filePath;
        keyval=msgString=kt=skt=projectId=cmlType=filePath="";
        try{
            obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.FORWARD_MESSAGE,
                    "","",
                    "","","","");
            JSONObject innerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            innerObj.remove(ConstantsObjects.CALMAIL_OBJECT);
            JSONObject calmailUpdateObj=CommonMethods.getCalmailUpdateObject2();
            innerObj.put(ConstantsObjects.CALMAIL_UPDATE,calmailUpdateObj);
            JSONArray essentialLstArr=new JSONArray();
            innerObj.put(ConstantsObjects.ESSENTIAL_LIST_ARRAY,essentialLstArr);
            JSONObject essentialInnerObj=new JSONObject();
            essentialLstArr.put(0,essentialInnerObj);
            JSONArray grpIdsArr=new JSONArray();
            essentialInnerObj.put(ConstantsObjects.GROUP_IDS,grpIdsArr);
            if(targetConversations!=null){
                if(targetConversations.size()>0){
                    for(int i=0;i<targetConversations.size();i++){
                        Conversation cc=targetConversations.get(i);
                        if(cc!=null){
                            keyval=cc.getKeyval();
                            grpIdsArr.put(i,keyval);
                        }
                    }
                }
            }
            JSONArray msgArr=new JSONArray();
            essentialInnerObj.put(ConstantsObjects.MESSAGE_ARRAY,msgArr);
            if(selectedMessageList!=null){
                Repository rr=Repository.getRepository();
                Login lgn=null;
                if(rr!=null){
                    lgn=rr.getLoginData();
                }
                if(lgn!=null){
                    projectId=lgn.getProjectId();
                }
                Log.i("Mdl_C_projectId:",projectId+" ..kk");
                for(int i=0;i<selectedMessageList.size();i++){
                    Messages singleMessage=selectedMessageList.get(i);
                    cmlType=msgString=kt=skt=filePath="";
                    if(singleMessage!=null){
                        msgString=singleMessage.getCmlTitle();
                        String completeString=singleMessage.getCompleteData();
                        JSONObject completeObj=new JSONObject(completeString);
                        if(completeObj!=null){
                            kt=completeObj.getString(ConstantsObjects.KEY_TYPE_INNER);
                            skt=completeObj.getString(ConstantsObjects.SUB_KEY_TYPE_INNER);
                        }
                    }
                    cmlType=singleMessage.getCmlType();
                    filePath=singleMessage.getImagePath();

                    JSONObject msgArrInnerObj=new JSONObject();
                    msgArr.put(i,msgArrInnerObj);
                    msgArrInnerObj.put(ConstantsObjects.CML_DESCRIPTION,msgString);
                    msgArrInnerObj.put(ConstantsObjects.CML_MESSAGE_INDEX,System.currentTimeMillis());
                    msgArrInnerObj.put(ConstantsObjects.CML_PARENT_SUB_KEY_TYPE,"SEC_CONV");
                    msgArrInnerObj.put(ConstantsObjects.CML_TITLE,msgString);
                    msgArrInnerObj.put(ConstantsObjects.KEY_TYPE_INNER,kt);
                    msgArrInnerObj.put(ConstantsObjects.ORG_PROJECT_ID_INNER,projectId);
                    msgArrInnerObj.put(ConstantsObjects.SUB_KEY_TYPE_INNER,skt);

                    if(cmlType!=null && filePath!=null){
                        msgArrInnerObj.put(ConstantsObjects.CML_IMAGE_PATH,filePath);
                        msgArrInnerObj.put(ConstantsObjects.CML_TYPE,cmlType);
                    }
                }
            }
            innerObj.put(ConstantsObjects.KEY_TYPE,this.keyType);
            innerObj.put(ConstantsObjects.KEY_VAL,this.keyval);
            innerObj.put(ConstantsObjects.SUB_KEY_TYPE,this.subKeyType);
            JSONObject extraParamObj=CommonMethods.getExtraParamObject(1);
            obj.put(ConstantsObjects.EXTRA_PARAM,extraParamObj);
            obj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            obj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_UPDATE);
        }catch(JSONException e){
            Log.e("Mdl_C_expn_frwrd_msg1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_expn_frwrd_msg2:",e.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_C_expn_frwrd_msg:",String.valueOf(obj)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(obj,ConstantsObjects.SERVER_OPERATION);
        }
    }
    public void getJSONObjectSearchMessage(String searchString,
                                           boolean isSearchingStarMessage,
                                           String lastElementId){
        JSONObject obj=null;
        boolean starMessageStatus=false;
        try{
            obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.SEARCH_MESSAGE,
                    "","",
                    "","","","");
            JSONObject innerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            innerObj.remove(ConstantsObjects.CALMAIL_OBJECT);
            JSONObject filterObj=new JSONObject();
            innerObj.put(ConstantsObjects.FILTER_OBJECT,filterObj);
            if(isSearchingStarMessage){
                starMessageStatus=true;
            }
            filterObj.put(ConstantsObjects.CML_STAR,starMessageStatus);
            filterObj.put(ConstantsObjects.CML_TITLE,searchString);
            innerObj.put(ConstantsObjects.KEY_TYPE,ConstantsObjects.KT_TSK);
            innerObj.put(ConstantsObjects.LAST_ELEMENT_ID,lastElementId);
            innerObj.put(ConstantsObjects.SECTION_ID,this.linkupId);
            innerObj.put(ConstantsObjects.SUB_KEY_TYPE,ConstantsObjects.SUB_KT_MESSAGE);
        }catch(JSONException e){
            Log.i("Mdl_C_searchMsg1:",e.toString());
        }catch(Exception e){
            Log.i("Mdl_C_searchMsg2:",e.toString());
        }
        Log.i("Mdl_C_searchMsg:",String.valueOf(obj)+" ..kk");
        GlobalClass.getAuthenticatedSyncSocket().emit(ConstantsObjects.ON_DEMAND_CALL,obj);
    }
    public void getJSONObjectFetchInvitees(){
        JSONObject obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.FETCH_INVITEE,
                "","",
                "","","","");
        try{
            if(obj.has(ConstantsObjects.FROM)){
                obj.remove(ConstantsObjects.FROM);
            }
            if(obj.has(ConstantsObjects.ACTION)){
                obj.remove(ConstantsObjects.ACTION);
            }
            JSONObject dataArrInnerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            if(dataArrInnerObj.has(ConstantsObjects.CALMAIL_OBJECT)){
                dataArrInnerObj.remove(ConstantsObjects.CALMAIL_OBJECT);
            }
            JSONObject filterObj=new JSONObject();
            dataArrInnerObj.put(ConstantsObjects.FILTER_OBJECT,filterObj);
            dataArrInnerObj.put(ConstantsObjects.KEY_TYPE,ConstantsObjects.KT_INVITEE);
            dataArrInnerObj.put(ConstantsObjects.LAST_ELEMENT_ID,"");
            dataArrInnerObj.put(ConstantsObjects.PROJECT_ID,this.ownerId);
            dataArrInnerObj.put(ConstantsObjects.SUB_KEY_TYPE,ConstantsObjects.SUB_KT_CONVERSATION_ARRAY1);
        }catch(JSONException e){
            Log.e("Mdl_C_ftchInvitee1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_ftchInvitee2:",e.toString());
        }
        Log.i("Mdl_C_ftchInvitee:",String.valueOf(obj)+" ..kk");
        if(GlobalClass.getAuthenticatedSyncSocket()!=null){
            GlobalClass.getAuthenticatedSyncSocket().emit(ConstantsObjects.ON_DEMAND_CALL,obj);
        }
    }
    public void getJSONObjectAddMember(ArrayList<String> emailAddressList){
        JSONObject mainObj=null;
        String userId="";
        try{
            mainObj=CommonMethods
                    .getPrimaryJsonObject(ConstantsObjects.ADD_MEMBER,"","",
                            "","","","");
            JSONObject innerObj=mainObj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            innerObj.remove(ConstantsObjects.CALMAIL_OBJECT);
            JSONObject calmailUpdate=CommonMethods.getCalmailUpdateObject2();
            innerObj.put(ConstantsObjects.CALMAIL_UPDATE,calmailUpdate);
            JSONArray essentialLstArr=CommonMethods.getEssentialListArray(this.ownerId);
            innerObj.put(ConstantsObjects.ESSENTIAL_LIST_ARRAY,essentialLstArr);
            JSONArray invitationInsertArr=new JSONArray();
            innerObj.put(ConstantsObjects.INVITATION_INSERT,invitationInsertArr);
            if(emailAddressList!=null){
                if(emailAddressList.size()>0){
                    for(int indexEmailAddress=0;indexEmailAddress<emailAddressList.size();indexEmailAddress++){
                        String emailAddress=emailAddressList.get(indexEmailAddress);
                        JSONObject invitationInnerObj=new JSONObject();
                        invitationInsertArr.put(indexEmailAddress,invitationInnerObj);
                        userId=GlobalClass.getUserId();
                        invitationInnerObj.put(ConstantsObjects.ADDED_BY,userId);
                        invitationInnerObj.put(ConstantsObjects.CML_ACCEPTED,1);
                        invitationInnerObj.put(ConstantsObjects.CML_ASSIGNED,emailAddress);
                        invitationInnerObj.put(ConstantsObjects.CML_IS_ACTIVE,true);
                        invitationInnerObj.put(ConstantsObjects.CML_IS_LATEST,1);
                        invitationInnerObj.put(ConstantsObjects.CML_PARENT_TEMP_KEY_VAL,this.keyval);
                        invitationInnerObj.put(ConstantsObjects.CML_PRIORITY,0);
                        invitationInnerObj.put(ConstantsObjects.CML_STAR,0);
                        invitationInnerObj.put(ConstantsObjects.CML_SUB_CATEGORY,emailAddress+"#WKS:6");
                        invitationInnerObj.put(ConstantsObjects.CML_TEMP_KEY_VAL,this.keyval+"_IDE:TO_"+emailAddress);
                        invitationInnerObj.put(ConstantsObjects.CML_UNREAD_COUNT,0);
                        invitationInnerObj.put(ConstantsObjects.IDE_ACCEPTED,1);
                        invitationInnerObj.put(ConstantsObjects.IDE_ATTENDEES_EMAIL,emailAddress);
                        JSONArray arr=new JSONArray();
                        invitationInnerObj.put(ConstantsObjects.IDE_DESIGNATION,arr);
                        invitationInnerObj.put(ConstantsObjects.IDE_ORIGINAL_CREATOR,userId);
                        invitationInnerObj.put(ConstantsObjects.IDE_TYPE,ConstantsObjects.IDE_TYPE_VALUE_TO);
                        invitationInnerObj.put(ConstantsObjects.KEY_TYPE_INNER,ConstantsObjects.KT_INVITEE);
                        invitationInnerObj.put(ConstantsObjects.PARENT_KEY_TYPE,this.keyType);
                        invitationInnerObj.put(ConstantsObjects.PARENT_SUB_KEY_TYPE,this.subKeyType);
                        invitationInnerObj.put(ConstantsObjects.SUB_KEY_TYPE_INNER,ConstantsObjects.SUB_KT_INVITEE);
                        Repository rr=Repository.getRepository();
                        if(rr!=null){
                            Contact cntct=rr.getContact(emailAddress);
                            if(cntct!=null){
                                invitationInnerObj.put(ConstantsObjects.IS_CONTACT_INVITEE_UPDATE,true);
                            }else{
                                invitationInnerObj.put(ConstantsObjects.IS_CONTACT_INVITEE_UPDATE,false);
                            }
                        }
                    }
                }
            }
            innerObj.put(ConstantsObjects.KEY_TYPE,this.keyType);
            innerObj.put(ConstantsObjects.KEY_VAL,this.keyval);
            innerObj.put(ConstantsObjects.SUB_KEY_TYPE,this.subKeyType);
            JSONObject extraParamObj=CommonMethods.getExtraParamObject(1);
            mainObj.put(ConstantsObjects.EXTRA_PARAM,extraParamObj);
            mainObj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            mainObj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_UPDATE);
        }catch(JSONException e1){
            Log.e("Mdl_C_addMmbr1:",e1.toString());
        }catch(Exception e2){
            Log.e("Mdl_C_addMmbr2:",e2.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_C_addMmbr:",String.valueOf(mainObj)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(mainObj,ConstantsObjects.SERVER_OPERATION);
        }
    }
    public void getJSONObjectRemoveMember(ArrayList<Invitee> invitees){
        JSONObject obj=null;
        String completeData="";
        try{
            obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.REMOVE_MEMBER,
                    "","",
                    "","","","");
            JSONObject innerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            innerObj.remove(ConstantsObjects.CALMAIL_OBJECT);
            JSONObject calmailUpdateObj=new JSONObject();
            calmailUpdateObj.put(ConstantsObjects.SYNC_PENDING_STATUS,0);
            innerObj.put(ConstantsObjects.CALMAIL_UPDATE,calmailUpdateObj);
            JSONArray essentialListArr=CommonMethods.getEssentialListArray(ownerId);
            innerObj.put(ConstantsObjects.ESSENTIAL_LIST_ARRAY,essentialListArr);
            JSONArray invitationDeleteArr=new JSONArray();
            innerObj.put(ConstantsObjects.INVITATION_DELETE,invitationDeleteArr);
            if(invitees!=null){
                if(invitees.size()>0){
                    for(int p=0;p<invitees.size();p++){
                        Invitee invitee=invitees.get(p);
                        if(invitee!=null){
                            completeData=invitee.getCompleteData();
                            JSONObject completeObj=new JSONObject(completeData);
                            invitationDeleteArr.put(p,completeObj);
                        }
                    }
                }
            }
            innerObj.put(ConstantsObjects.KEY_TYPE,this.keyType);
            innerObj.put(ConstantsObjects.KEY_VAL,this.keyval);
            innerObj.put(ConstantsObjects.SUB_KEY_TYPE,this.subKeyType);
            JSONObject extraParamObj=CommonMethods.getExtraParamObject(1);
            obj.put(ConstantsObjects.EXTRA_PARAM,extraParamObj);
            obj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            obj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_UPDATE);
        }catch(JSONException e1){
            Log.e("Mdl_C_rmveMmbr1:",e1.toString());
        }catch(Exception e2){
            Log.e("Mdl_C_rmveMmbr2:",e2.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_C_rmveMmbr:",String.valueOf(obj)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(obj,ConstantsObjects.SERVER_OPERATION);
        }
    }
    //JsonObjectContact.java
    public void getJSONObjectAddMessagingContact(){
        JSONObject obj=null;
        String personalProjectId,cmlRefId,projectId,firstName,lastName,cmlTempKeyval,userId;
        personalProjectId=cmlRefId=projectId=firstName=lastName=cmlTempKeyval=userId="";
        Login lgn=null;
        try{
            Repository rr=Repository.getRepository();
            if(rr!=null){
                lgn=rr.getLoginData();
                personalProjectId=rr.getPersonaWiseProjectId(ConstantsObjects.ROLE_PERSONAL);
                cmlRefId=personalProjectId+"#SEC_REPL_WIZ_0029#TSK_CDE_GRP";
            }
            if(lgn!=null){
                projectId=lgn.getProjectId();
            }
            userId=GlobalClass.getUserId();
            cmlTempKeyval="#"+ConstantsObjects.SUB_KT_CONTACT+"#"+userId+":"+System.currentTimeMillis()
                    +"_"+CommonMethods.getElevenDigitRandomNumber()+"_"+CommonMethods.getFiveDigitRandomNumber();
            obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.ADD_MESSAGING_CONTACT,
                    this.cmlTitle,"",
                    ConstantsObjects.KT_TSK,ConstantsObjects.SUB_KT_CONTACT,cmlTempKeyval,cmlRefId);
            JSONObject innerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            JSONObject calmailObject=innerObj.getJSONObject(ConstantsObjects.CALMAIL_OBJECT);
            calmailObject.remove(ConstantsObjects.CML_SUB_CATEGORY);
            calmailObject.put(ConstantsObjects.CML_SUB_CATEGORY,personalProjectId);
            calmailObject.remove(ConstantsObjects.DEPT_ID_INNER);
            calmailObject.put(ConstantsObjects.DEPT_ID_INNER,"");
            calmailObject.remove(ConstantsObjects.DEPT_PROJECT_ID);
            calmailObject.put(ConstantsObjects.DEPT_PROJECT_ID,projectId);
            calmailObject.remove(ConstantsObjects.ORG_ID_INNER);
            calmailObject.put(ConstantsObjects.ORG_ID_INNER,"");
            calmailObject.put(ConstantsObjects.ACTIVE_STATUS,1);
            calmailObject.put(ConstantsObjects.CML_ACCEPTED,1);
            calmailObject.put(ConstantsObjects.CML_DESCRIPTION,"");
            String name[]=this.cmlTitle.split(" ");
            if(name!=null){
                if(name.length>0){
                    firstName=name[0];
                    lastName=name[1];
                }
            }
            Log.i("Mdl_C_+cntct_fn:",firstName+" ..kk");
            Log.i("Mdl_C_+cntct_ln:",lastName+" ..kk");
            calmailObject.put(ConstantsObjects.CML_FIRST_NAME,firstName);
            calmailObject.put(ConstantsObjects.CML_IMAGE_PATH,"");
            calmailObject.put(ConstantsObjects.CML_LAST_NAME,lastName);
            calmailObject.put(ConstantsObjects.CML_NICK_NAME,firstName);
            calmailObject.put(ConstantsObjects.CML_OFFICIAL_EMAIL,this.createdBy);
            calmailObject.put(ConstantsObjects.CML_PERSONAL_EMAIL,this.createdBy);
            calmailObject.put(ConstantsObjects.SYNC_PENDING_STATUS,0);
            calmailObject.put(ConstantsObjects.USER_LAST_MODIFIED_ON,CommonMethods.getCurrentTimeStampInZuluFormat());
            JSONArray essentialLstArr=new JSONArray();
            innerObj.put(ConstantsObjects.ESSENTIAL_LIST_ARRAY,essentialLstArr);
            essentialLstArr.put(0,this.keyval);
            obj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            obj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_INSERT);
            obj.remove(ConstantsObjects.MODULE_NAME);
            obj.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_VALUE_CONTACT);
        }catch(JSONException e){
            Log.e("Mdl_C_add_msgng_cntct1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_add_msgng_cntct2:",e.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_C_add_msgng_cntct:",String.valueOf(obj)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(obj,ConstantsObjects.SERVER_OPERATION);
        }
    }
    public void getJSONObjectAddBlockContact(){
        //unassigned projectId for cmlRefId
        //unassigned projectId for cmlSubCategory
        JSONObject obj=null;
        String cmlRefId,unassignedProjectId,cmlTempKeyval,userId,
                projectId,fn,ln;
        cmlRefId=unassignedProjectId=cmlTempKeyval=userId
                =projectId=fn=ln="";
        Login lgn=null;
        try{
            Repository rr=Repository.getRepository();
            if(rr!=null){
                unassignedProjectId=rr.getPersonaWiseProjectId(ConstantsObjects.ROLE_UNASSIGNED);
                Log.i("Mdl_C_unasgndProjectId:",unassignedProjectId+" ..kk");
                cmlRefId=unassignedProjectId+"#SEC_REPL_WIZ_0029#TSK_CDE_GRP";
                lgn=rr.getLoginData();
            }
            if(lgn!=null){
                projectId=lgn.getProjectId();
            }
            userId=GlobalClass.getUserId();
            cmlTempKeyval="#"+ConstantsObjects.SUB_KT_BLOCK_MESSAGING_CONTACT+"#"
                    +userId+":"+System.currentTimeMillis()+"_"
                    +CommonMethods.getElevenDigitRandomNumber()
                    +"_"+CommonMethods.getFiveDigitRandomNumber();
            obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.ADD_BLOCK_CONTACT,
                    this.cmlTitle,"",
                    ConstantsObjects.KT_TSK,ConstantsObjects.SUB_KT_BLOCK_MESSAGING_CONTACT,cmlTempKeyval,cmlRefId);
            JSONObject innerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            JSONObject calmailObject=innerObj.getJSONObject(ConstantsObjects.CALMAIL_OBJECT);
            calmailObject.remove(ConstantsObjects.CML_SUB_CATEGORY);
            calmailObject.put(ConstantsObjects.CML_SUB_CATEGORY,unassignedProjectId);
            calmailObject.remove(ConstantsObjects.DEPT_ID_INNER);
            calmailObject.put(ConstantsObjects.DEPT_ID_INNER,"");
            calmailObject.remove(ConstantsObjects.DEPT_PROJECT_ID);
            calmailObject.put(ConstantsObjects.DEPT_PROJECT_ID,projectId);
            calmailObject.remove(ConstantsObjects.ORG_ID_INNER);
            calmailObject.put(ConstantsObjects.ORG_ID_INNER,"");
            calmailObject.put(ConstantsObjects.ACTIVE_STATUS,1);
            calmailObject.put(ConstantsObjects.CML_ACCEPTED,5);
            calmailObject.put(ConstantsObjects.CML_DESCRIPTION,"");
            String name[]=this.cmlTitle.split(" ");
            if(name!=null){
                if(name.length>0){
                    fn=name[0];
                    ln=name[1];
                }
            }
            calmailObject.put(ConstantsObjects.CML_FIRST_NAME,fn);
            calmailObject.put(ConstantsObjects.CML_IMAGE_PATH,"");
            calmailObject.put(ConstantsObjects.CML_LAST_NAME,ln);
            calmailObject.put(ConstantsObjects.CML_NICK_NAME,fn);
            calmailObject.put(ConstantsObjects.CML_OFFICIAL_EMAIL,this.createdBy);
            calmailObject.put(ConstantsObjects.CML_PERSONAL_EMAIL,this.createdBy);
            calmailObject.put(ConstantsObjects.SYNC_PENDING_STATUS,0);
            calmailObject.put(ConstantsObjects.USER_LAST_MODIFIED_ON,CommonMethods.getCurrentTimeStampInZuluFormat());
            obj.remove(ConstantsObjects.MODULE_NAME);
            obj.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_VALUE_CONTACT);
            obj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            obj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_INSERT);
        }catch(JSONException e){
            Log.e("Mdl_C_blck_cntct1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_blck_cntct2:",e.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_C_blck_cntct:",String.valueOf(obj)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(obj,ConstantsObjects.SERVER_OPERATION);
        }
    }
    /*===================================================================================================================*/
    public void createCalendarEventFromConversation(String eventTitle,String personaSelected,
                                           ArrayList<String> invitees,JSONObject locationObject){
        Log.i("Mdl_C_locationObb:",String.valueOf(locationObject)+" ..kk");
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
            calmailObj.put(ConstantsObjects.CML_MAIN_PARENT,"");
            calmailObj.put(ConstantsObjects.CML_REF_ID,"");
            calmailObj.put(ConstantsObjects.PARENT_CONVERSATION_ID,this.ownerId);
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
                    JSONObject inviteeArrInnerObj= CommonMethods
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
            Log.e("Mdl_C_creatEvnts1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_C_creatEvnts2:",e.toString());
        }
        Log.i("Mdl_C_createEventObb:",String.valueOf(mainObj)+" ..kk");
        boolean internetStatus=CheckInternetConnectionCommunicator.isInternetAvailable();
        Log.i("Mdl_C_crtEvtIntntStats:",String.valueOf(internetStatus)+" ..kk");
        if(GlobalClass.getAuthenticatedSyncSocket()!=null && internetStatus){
            GlobalClass.getAuthenticatedSyncSocket().emit(ConstantsObjects.SERVER_OPERATION,mainObj);
        }
    }
}