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

@Entity(tableName=ConstantsClass.TBL_MESSAGES)
public class Messages{

    @ColumnInfo(name=ConstantsClass.CML_TITLE)
    private String cmlTitle;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name=ConstantsClass.COMMON_KEY_VAL)
    private String keyval;

    @ColumnInfo(name=ConstantsClass.COMMON_KEY_TYPE)
    private String keytype;

    @ColumnInfo(name=ConstantsClass.COMMON_SUB_KEY_TYPE)
    private String subKeytype;

    @ColumnInfo(name=ConstantsClass.CML_CHT_TYPE)
    private int messsageType;

    @ColumnInfo(name=ConstantsClass.CML_STAR)
    private int cmlStar;

    @ColumnInfo(name=ConstantsClass.CML_MESSAGE_INDEX)
    private long cmlMessageIndex;

    @ColumnInfo(name=ConstantsClass.COMMON_CML_REF_ID)
    private String cmlRefId;

    @Ignore
    @ColumnInfo(name=ConstantsClass.VIEW_TYPE)
    private int viewType=0;

    @ColumnInfo(name=ConstantsClass.IMAGE_PATH)
    private String imagePath;

    @ColumnInfo(name=ConstantsClass.IS_MEDIA)
    private boolean isMedia=false;

    @ColumnInfo(name=ConstantsClass.CREATED_BY)
    private String createdBy;

    @Ignore
    @ColumnInfo(name=ConstantsClass.IS_MESSAGE_SELECTED)
    private boolean isMessageSelected;

    @ColumnInfo(name=ConstantsClass.MESSAGE_TYPE)
    private String cmlType;

    @ColumnInfo(name=ConstantsClass.RESTDATA)
    private String completeData;

    @ColumnInfo(name=ConstantsClass.IMAGE_PATH_LOCAL)
    private String localImagePath;

    @ColumnInfo(name=ConstantsClass.GROUP_ID)
    private String groupId;

    @ColumnInfo(name=ConstantsClass.CML_ISREPLY)
    private boolean cmlIsReply;

    @ColumnInfo(name=ConstantsClass.CML_PARENT_TASK_ID)
    private String parentTaskId;

    @ColumnInfo(name=ConstantsClass.SYNC_PENDING_STATUS)
    private int pendingStatus=0;

    @ColumnInfo(name=ConstantsClass.IS_THIS_EVENT_MESSAGE)
    private String isThisEventMessage;

    public String getGroupId(){
        return groupId;
    }

    public void setGroupId(String groupId){
        this.groupId=groupId;
    }

    public String getLocalImagePath(){
        return localImagePath;
    }

    public void setLocalImagePath(String localImagePath){
        this.localImagePath=localImagePath;
    }

    public String getCmlTitle(){
        return cmlTitle;
    }

    public void setCmlTitle(String cmlTitle){
        this.cmlTitle=cmlTitle;
    }

    @NonNull
    public String getKeyval(){
        return keyval;
    }

    public void setKeyval(@NonNull String keyval){
        this.keyval=keyval;
    }

    public int getMesssageType(){
        return messsageType;
    }

    public void setMesssageType(int messsageType){
        this.messsageType=messsageType;
    }

    public int getCmlStar(){
        return cmlStar;
    }

    public void setCmlStar(int cmlStar){
        this.cmlStar=cmlStar;
    }

    public long getCmlMessageIndex(){
        return cmlMessageIndex;
    }

    public void setCmlMessageIndex(long cmlMessageIndex){
        this.cmlMessageIndex=cmlMessageIndex;
    }

    public String getCmlRefId(){
        return cmlRefId;
    }

    public void setCmlRefId(String cmlRefId){
        this.cmlRefId=cmlRefId;
    }

    public int getViewType(){
        return viewType;
    }

    public void setViewType(int viewType){
        this.viewType=viewType;
    }

    public String getImagePath(){
        return imagePath;
    }

    public void setImagePath(String imagePath){
        this.imagePath=imagePath;
    }

    public boolean isMedia(){
        return isMedia;
    }

    public void setMedia(boolean media){
        isMedia=media;
    }

    public boolean isMessageSelected(){
        return isMessageSelected;
    }

    public void setMessageSelected(boolean messageSelected){
        isMessageSelected=messageSelected;
    }

    public String getCmlType(){
        return cmlType;
    }

    public void setCmlType(String cmlType){
        this.cmlType=cmlType;
    }

    public String getCompleteData(){
        return completeData;
    }

    public void setCompleteData(String completeData){
        this.completeData=completeData;
    }

    public String getCreatedBy(){
        return createdBy;
    }

    public void setCreatedBy(String createdBy){
        this.createdBy=createdBy;
    }

    public boolean isCmlIsReply(){
        return cmlIsReply;
    }

    public void setCmlIsReply(boolean cmlIsReply){
        this.cmlIsReply=cmlIsReply;
    }

    public String getParentTaskId(){
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId){
        this.parentTaskId=parentTaskId;
    }

    public int getPendingStatus(){
        return pendingStatus;
    }

    public void setPendingStatus(int pendingStatus){
        this.pendingStatus=pendingStatus;
    }

    public String getKeytype(){
        return keytype;
    }

    public void setKeytype(String keytype){
        this.keytype=keytype;
    }

    public String getSubKeytype(){
        return subKeytype;
    }

    public void setSubKeytype(String subKeytype){
        this.subKeytype=subKeytype;
    }

    public String getIsThisEventMessage(){
        return isThisEventMessage;
    }

    public void setIsThisEventMessage(String isThisEventMessage){
        this.isThisEventMessage=isThisEventMessage;
    }

    /*======================================================================================================================
                    model class changes==>>
                =======================================================================================================================*/
    public void getJSONObjectFetchNestedMessages(String lastMsgKeyval){
        JSONObject obj=null;
        try{
            obj=CommonMethods.getPrimaryJsonObject(ConstantsObjects.FETCH_NESTED_MESSAGE,
                    "","",
                    "","","","");
            JSONObject innerObj=obj.getJSONArray(ConstantsObjects.DATA_ARRAY).getJSONObject(0);
            innerObj.remove(ConstantsObjects.CALMAIL_OBJECT);
            JSONObject filterObj=new JSONObject();
            innerObj.put(ConstantsObjects.FILTER_OBJECT,filterObj);
            filterObj.put(ConstantsObjects.IS_ATTACHMENT,false);
            filterObj.put(ConstantsObjects.IS_NESTED,true);
            innerObj.put(ConstantsObjects.KEY_TYPE,this.keytype);
            innerObj.put(ConstantsObjects.LAST_ELEMENT_ID,lastMsgKeyval);
            innerObj.put(ConstantsObjects.SECTION_ID,keyval);
            innerObj.put(ConstantsObjects.SUB_KEY_TYPE,this.subKeytype);
        }catch(JSONException e){
            Log.e("Mdl_M_ftchNstdMsg1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_M_ftchNstdMsg2:",e.toString());
        }
        Log.i("Mdl_M_ftchNstdMsg:",String.valueOf(obj)+" ..kk");
        if(GlobalClass.getAuthenticatedSyncSocket()!=null){
            GlobalClass.getAuthenticatedSyncSocket().emit(ConstantsObjects.ON_DEMAND_CALL,obj);
        }
    }
    public void getJSONObjectStarUnstarMessage(ArrayList<Messages> msgList,String operation){
        JSONObject mainObj=new JSONObject();
        String ownerId,kt,keyval,skt,requestId,socketId,userId;
        int starStatus=-1;
        try{
            requestId=socketId=userId="";
            mainObj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            mainObj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_UPDATE);
            JSONArray dataArr=new JSONArray();
            mainObj.put(ConstantsObjects.DATA_ARRAY,dataArr);
            if(msgList!=null){
                if(msgList.size()>0){
                    if(operation!=null){
                        if(operation.equals("star")){
                            starStatus=1;
                        }
                        if(operation.equals("unstar")){
                            starStatus=0;
                        }
                    }
                    for(int u=0;u<msgList.size();u++){
                        Messages singleMessage=msgList.get(u);
                        ownerId=kt=keyval=skt="";
                        if(singleMessage!=null){
                            String completeData=singleMessage.getCompleteData();
                            JSONObject completeObj=new JSONObject(completeData);
                            if(completeObj!=null){
                                if(completeObj.has("OWNER_ID")){
                                    ownerId=completeObj.getString("OWNER_ID");
                                }
                                if(completeObj.has(ConstantsObjects.KEY_TYPE_INNER)){
                                    kt=completeObj.getString(ConstantsObjects.KEY_TYPE_INNER);
                                }
                                if(completeObj.has(ConstantsObjects.SUB_KEY_TYPE_INNER)){
                                    skt=completeObj.getString(ConstantsObjects.SUB_KEY_TYPE_INNER);
                                }
                            }
                            keyval=singleMessage.getKeyval();
                        }
                        JSONObject innerObj=new JSONObject();
                        dataArr.put(u,innerObj);
                        JSONArray actionArr=CommonMethods.getActionArray(ConstantsObjects.EDIT_MESSAGE);
                        innerObj.put(ConstantsObjects.ACTION_ARRAY,actionArr);
                        JSONObject calmailUpdateObj=CommonMethods.getCalmailUpdateObject2();
                        calmailUpdateObj.put(ConstantsObjects.CML_STAR,starStatus);
                        innerObj.put(ConstantsObjects.CALMAIL_UPDATE,calmailUpdateObj);
                        JSONArray essentialLstArr=CommonMethods.getEssentialListArray(ownerId);
                        innerObj.put(ConstantsObjects.ESSENTIAL_LIST_ARRAY,essentialLstArr);
                        innerObj.put(ConstantsObjects.KEY_TYPE,kt);
                        innerObj.put(ConstantsObjects.KEY_VAL,keyval);
                        innerObj.put(ConstantsObjects.SUB_KEY_TYPE,skt);
                        JSONObject extraParamObj=CommonMethods.getExtraParamObject(0);
                        mainObj.put(ConstantsObjects.EXTRA_PARAM,extraParamObj);
                        mainObj.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_NAME_VALUE);
                        requestId=CommonMethods.getRequestId();
                        mainObj.put(ConstantsObjects.REQUEST_ID,requestId);
                        socketId=CommonMethods.getSocketId();
                        mainObj.put(ConstantsObjects.SOCKET_ID,socketId);
                        userId= GlobalClass.getUserId();
                        mainObj.put(ConstantsObjects.USER_ID,userId);
                    }
                }
            }
        }catch(JSONException e){
            Log.e("Mdl_M_starUnstarMsg1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_M_starUnstarMsg2:",e.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_M_starUnstarMsg:",String.valueOf(mainObj)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(mainObj,ConstantsObjects.SERVER_OPERATION);
        }
    }
    public void deleteNestedMessage(ArrayList<Messages> nestedMessageList){
        JSONObject mainObj=new JSONObject();
        try{
            mainObj.put(ConstantsObjects.FROM,ConstantsObjects.FROM_VALUE);
            mainObj.put(ConstantsObjects.ACTION,ConstantsObjects.ACTION_DELETE);
            JSONArray dataArr=new JSONArray();
            mainObj.put(ConstantsObjects.DATA_ARRAY,dataArr);
            if(nestedMessageList!=null){
                if(nestedMessageList.size()>0){
                    for(int i=0;i<nestedMessageList.size();i++){
                        Messages nestedMsg=nestedMessageList.get(i);
                        String kt,keyval,skt,ownerId;
                        kt=keyval=skt=ownerId="";
                        if(nestedMsg!=null){
                            String completeData=nestedMsg.getCompleteData();
                            JSONObject completeObb=new JSONObject(completeData);
                            if(completeObb!=null){
                                if(completeObb.has(ConstantsObjects.OWNER_ID)){
                                    ownerId=completeObb.getString(ConstantsObjects.OWNER_ID);
                                }
                            }
                            kt=nestedMsg.getKeytype();
                            keyval=nestedMsg.getKeyval();
                            skt=nestedMsg.getSubKeytype();
                            JSONObject obb=new JSONObject();
                            dataArr.put(i,obb);
                            JSONArray actionArr=CommonMethods.getActionArray(ConstantsObjects.SNIP_MESSAGE);
                            obb.put(ConstantsObjects.ACTION_ARRAY,actionArr);
                            obb.put(ConstantsObjects.ACTIVE_STATUS_DATA_ARRAY_INNER,5);
                            JSONArray essentialLstArr=new JSONArray();
                            obb.put(ConstantsObjects.ESSENTIAL_LIST_ARRAY,essentialLstArr);
                            JSONObject essObb=new JSONObject();
                            essentialLstArr.put(0,essObb);
                            essObb.put(ConstantsObjects.CREATORS_ID,ownerId);
                            essObb.put(ConstantsObjects.IS_NESTED,true);
                            obb.put(ConstantsObjects.KEY_TYPE,kt);
                            obb.put(ConstantsObjects.KEY_VAL,keyval);
                            obb.put(ConstantsObjects.SUB_KEY_TYPE,skt);
                        }
                    }
                }
            }
            JSONObject extraParamObb=new JSONObject();
            mainObj.put(ConstantsObjects.EXTRA_PARAM,extraParamObb);
            mainObj.put(ConstantsObjects.MODULE_NAME,ConstantsObjects.MODULE_NAME_VALUE);
            mainObj.put(ConstantsObjects.REQUEST_ID,CommonMethods.getRequestId());
            mainObj.put(ConstantsObjects.SOCKET_ID,CommonMethods.getSocketId());
            mainObj.put(ConstantsObjects.USER_ID,GlobalClass.getUserId());
        }catch(JSONException e){
            Log.e("Mdl_M_dltNstdMsg1:",e.toString());
        }catch(Exception e){
            Log.e("Mdl_M_dltNstdMsg2:",e.toString());
        }
        Repository rr=Repository.getRepository();
        Log.i("Mdl_M_dltNstdMsg:",String.valueOf(mainObj)+" ..kk");
        if(rr!=null){
            rr.emitRequestCall(mainObj,ConstantsObjects.SERVER_OPERATION);
        }
    }
}