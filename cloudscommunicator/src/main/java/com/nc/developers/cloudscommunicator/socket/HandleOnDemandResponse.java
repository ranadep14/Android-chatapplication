package com.nc.developers.cloudscommunicator.socket;

import android.app.Application;
import android.util.Log;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.dao.ConversationDao;
import com.nc.developers.cloudscommunicator.database.ClouzerDatabase;
import com.nc.developers.cloudscommunicator.models.Conversation;
import com.nc.developers.cloudscommunicator.models.Event;
import com.nc.developers.cloudscommunicator.models.Invitee;
import com.nc.developers.cloudscommunicator.models.Messages;
import com.nc.developers.cloudscommunicator.models.OfflineSync;
import com.nc.developers.cloudscommunicator.models.RoleTask;
import com.nc.developers.cloudscommunicator.objects.ConstantsObjects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HandleOnDemandResponse{

    private static final String TAG=HandleOnDemandResponse.class.getSimpleName();

    private static final String ADD_CONTACT_SERVER="ADD_CONTACT_SERVER";
    private static final String DELETE_CONTACT_SERVER="DELETE_CONTACT_SERVER";
    private static final String FETCH_CONTACT_USERS_SERVER="FETCH_CONTACT_USERS_SERVER";
    private static final String FETCH_CONTACT_SERVER="FETCH_CONTACT_SERVER";
    private static final String FETCH_USER_URM_SERVER="FETCH_USER_URM_SERVER";
    private static final String FETCH_INVITEE_SERVER="FETCH_INVITEE_SERVER";
    private static final String GET_ROLE_TASK_SERVER="GET_ROLE_TASK_SERVER";
    private static final String UPDATE_CHAT_INVITEE_SERVER="UPDATE_CHAT_INVITEE_SERVER";
    private static final String FETCH_ALL_CONTACT_SERVER="FETCH_ALL_CONTACT_SERVER";
    private static final String ADD_MESSAGING_CONTACT_SERVER="ADD_MESSAGING_CONTACT_SERVER";
    private static final String SPECIFIC_INVITEE_CHAT_SERVER="SPECIFIC_INVITEE_CHAT_SERVER";
    private static final String FETCH_CONVERSATION_FILTER_SERVER="FETCH_CONVERSATION_FILTER_SERVER";
    private static final String CAST_CONVERSATION_SERVER="CAST_CONVERSATION_SERVER";
    private static final String ADD_COMMENT_SERVER="ADD_COMMENT_SERVER";
    private static final String FETCH_MESSAGE_FILTER_SERVER="FETCH_MESSAGE_FILTER_SERVER";
    private static final String ADD_MESSAGE_SERVER="ADD_MESSAGE_SERVER";
    private static final String MAKE_ADMIN_SERVER="MAKE_ADMIN_SERVER";
    private static final String REMOVE_ADMIN_SERVER="REMOVE_ADMIN_SERVER";
    private static final String UPDATE_CONVERSATION_SERVER="UPDATE_CONVERSATION_SERVER";
    private static final String SNIP_CONVERSATION_SERVER="SNIP_CONVERSATION_SERVER";
    private static final String SEARCH_ALL_CONTACT_SERVER="SEARCH_ALL_CONTACT_SERVER";
    private static final String SNIP_MESSAGE_SERVER="SNIP_MESSAGE_SERVER";
    private static final String ADD_MEMBER_SERVER="ADD_MEMBER_SERVER";
    private static final String REMOVE_MEMBER_SERVER="REMOVE_MEMBER_SERVER";
    private static final String FETCH_SPECIFIC_GROUP_SERVER="FETCH_SPECIFIC_GROUP_SERVER";
    private static final String ERASE_CONVERSATION_SERVER="ERASE_CONVERSATION_SERVER";
    private static final String ARCHIVE_CONVERSATION_SERVER="ARCHIVE_CONVERSATION_SERVER";
    private static final String UNARCHIVE_CONVERSATION_SERVER="UNARCHIVE_CONVERSATION_SERVER";
    private static final String NESTED_MESSAGE_SERVER="NESTED_MESSAGE_SERVER";
    private static final String EDIT_MESSAGE_SERVER="EDIT_MESSAGE_SERVER";
    private static final String FETCH_NESTED_MESSAGE_SERVER="FETCH_NESTED_MESSAGE_SERVER";
    private static final String ADD_BLOCK_CONTACT_SERVER="ADD_BLOCK_CONTACT_SERVER";
    private static final String BLOCK_CONVERSATION_SERVER="BLOCK_CONVERSATION_SERVER";
    private static final String UPDATE_USER_SERVER="UPDATE_USER_SERVER";
    private static final String SEARCH_MESSAGE_SERVER="SEARCH_MESSAGE_SERVER";
    private static final String SUPERIMPOSE_EVENT_SERVER="SUPERIMPOSE_EVENT_SERVER";
    private static final String CAST_EVENT_SERVER="CAST_EVENT_SERVER";
    private static final String FETCH_INBOX_OBJECTS_SERVER="FETCH_INBOX_OBJECTS_SERVER";
    private static final String MODIFY_EVENT_SERVER="MODIFY_EVENT_SERVER";

    private String bind_ui_string="";
    private static int offlineCounter=0;

    public void handleResponse(JSONObject object){
        if(object!=null){
            if(object.has("serverAction") && object.has(ConstantsObjects.DATA_ARRAY)){
                String serverAction="";
                JSONArray dataArr=null;
                try{
                    serverAction=object.getString("serverAction");
                    Log.i("$erverAction:",serverAction);
                    dataArr=object.getJSONArray(ConstantsObjects.DATA_ARRAY);
                    JSONObject innerObj=null;
                    if(dataArr!=null && serverAction!=null){
                        switch(serverAction){
                            case MODIFY_EVENT_SERVER:
                                if(dataArr.length()>0){
                                    Repository repository=Repository.getRepository();
                                    for(int i=0;i<dataArr.length();i++){
                                        JSONObject eventObb=dataArr.getJSONObject(i);
                                        if(eventObb!=null){
                                            String kt,skt;
                                            kt=skt="";
                                            if(eventObb.has(ConstantsObjects.KEY_TYPE_INNER)
                                                    && eventObb.has(ConstantsObjects.SUB_KEY_TYPE_INNER)){
                                                kt=eventObb.getString(ConstantsObjects.KEY_TYPE_INNER);
                                                skt=eventObb.getString(ConstantsObjects.SUB_KEY_TYPE_INNER);
                                                if(kt.equals("TSK") && skt.equals("TSK_EVT")){
                                                    Event event=JsonParserClass.parseEventJsonObject(eventObb);
                                                    if(event!=null && repository!=null){
                                                        boolean eventRequestStatus=repository.deleteEventRequest(event);
                                                        Log.i(TAG+"_evntRqstStatus_Mdfy:",
                                                                String.valueOf(eventRequestStatus)+" ..kk");
                                                        if(eventRequestStatus){
                                                            bindUI(serverAction);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case FETCH_INBOX_OBJECTS_SERVER:
                                if(dataArr.length()>0){
                                    ArrayList<Event> eventRequestList=new ArrayList<>();
                                    for(int i=0;i<dataArr.length();i++){
                                        JSONObject eventRequestObb=dataArr.getJSONObject(i);
                                        if(eventRequestObb!=null){
                                            Event eventRequest=JsonParserClass.parseEventJsonObject(eventRequestObb);
                                            eventRequest.setEventStatus("event_request");
                                            eventRequestList.add(eventRequest);
                                        }
                                    }
                                    Repository repository=Repository.getRepository();
                                    if(repository!=null){
                                        boolean eventRequest=repository.insertEventList(eventRequestList);
                                        Log.i(TAG+"_evntRqst:",String.valueOf(eventRequest)+" ..kk");
                                        if(eventRequest){
                                            bindUI(serverAction);
                                        }
                                    }
                                }
                                break;
                            case CAST_EVENT_SERVER:
                            case SUPERIMPOSE_EVENT_SERVER:
                                if(dataArr.length()>0){
                                    if(serverAction.equals("CAST_EVENT_SERVER")){
                                        boolean isEventRequestDeleted=false;
                                        Repository repository=Repository.getRepository();
                                        if(repository!=null){
                                            JSONObject eventObb=dataArr.getJSONObject(0);
                                            Event event=JsonParserClass.parseEventJsonObject(eventObb);
                                            isEventRequestDeleted=repository.deleteEventRequest(event);
                                            Log.i(TAG+"_eventRqstDltHoho:",String.valueOf(isEventRequestDeleted)+" ..kk");
                                        }
                                    }
                                    ArrayList<Event> eventList=new ArrayList<>();
                                    for(int i=0;i<dataArr.length();i++){
                                        JSONObject eventObj=dataArr.getJSONObject(i);
                                        Event event=JsonParserClass.parseEventJsonObject(eventObj);
                                        event.setEventStatus("");
                                        eventList.add(event);
                                    }
                                    Repository rr=Repository.getRepository();
                                    if(rr!=null){
                                        boolean eventStatus=rr.insertEventList(eventList);
                                        Log.i(TAG+"_eventStatus:",String.valueOf(eventStatus)+" ..kk");
                                        if(eventStatus){
                                            bindUI(SUPERIMPOSE_EVENT_SERVER);
                                        }
                                    }
                                }
                                break;
                            case SEARCH_MESSAGE_SERVER:
                                ArrayList<Messages> searchMsgLst=new ArrayList<>();
                                for(int i=0;i<dataArr.length();i++){
                                    innerObj=dataArr.getJSONObject(i);
                                    Messages singleMessage=JsonParserClass.parseMessagesJSONObject(innerObj);
                                    searchMsgLst.add(singleMessage);
                                }
                                Log.i(TAG+"_dataArrSz:",String.valueOf(dataArr.length())+" ..kk");
                                Log.i(TAG+"_searchMsgLstSz:",String.valueOf(searchMsgLst.size())+" ..kk");
                                GlobalClass.setSearchMessageList(searchMsgLst);
                                bindUI("search_msg_done");
                                break;
                            case UPDATE_USER_SERVER:
                                boolean updateProfileImgStatus=false;
                                Repository rr=Repository.getRepository();
                                if(rr!=null){
                                    updateProfileImgStatus=rr.updateUserProfileImage(object);
                                    Log.i(TAG+"_prflImgStats:",String.valueOf(updateProfileImgStatus)+" ..kk");
                                    if(updateProfileImgStatus){
                                        bindUI("update_user_profile");
                                    }
                                }
                                break;
                            case BLOCK_CONVERSATION_SERVER:
                                boolean blockConversationStatus=false;
                                if(dataArr.length()>0){
                                    JSONObject conversationObj=dataArr.getJSONObject(0);
                                    if(conversationObj!=null){
                                        Conversation conversation=JsonParserClass
                                                .parseConversationJsonObject(conversationObj);
                                        if(conversation!=null){
                                            ArrayList<Conversation> conversationList=
                                                    new ArrayList<Conversation>();
                                            conversationList.add(conversation);
                                            rr=Repository.getRepository();
                                            blockConversationStatus=
                                                    rr.insertConversationData(conversationList,"update");
                                            Log.i(TAG+"_blockConvStatus:",String.valueOf(blockConversationStatus)+" ..kk");
                                            if(blockConversationStatus){
                                                bindUI("block_conversation");
                                            }
                                        }
                                    }
                                }
                                break;
                            case EDIT_MESSAGE_SERVER:
                                if(dataArr.length()>0){
                                    JSONObject innerMsgObj=dataArr.getJSONObject(0);
                                    if(innerMsgObj!=null){
                                        String kt,skt;
                                        kt=skt="";
                                        boolean editMsgStatus=false;
                                        if(innerMsgObj.has(ConstantsObjects.KEY_TYPE_INNER)
                                                && innerMsgObj.has(ConstantsObjects.SUB_KEY_TYPE_INNER)){
                                            kt=innerMsgObj.getString(ConstantsObjects.KEY_TYPE_INNER);
                                            skt=innerMsgObj.getString(ConstantsObjects.SUB_KEY_TYPE_INNER);
                                            if(kt.equals(ConstantsObjects.KT_TSK)
                                                    && (skt.equals(ConstantsObjects.SUB_KT_MESSAGE)
                                                        || skt.equals(ConstantsObjects.SUB_KT_SEND_MESSAGE_ATTACHMENT))){
                                                Messages msg=JsonParserClass
                                                .parseMessagesJSONObject(innerMsgObj);
                                                ArrayList<Messages> msgList=new ArrayList<Messages>();
                                                msgList.add(msg);
                                                rr=Repository.getRepository();
                                                if(rr!=null){
                                                    editMsgStatus=rr.updateMessageList(msgList);
                                                    Log.i(TAG+"_editMsgStats:",String.valueOf(editMsgStatus)+" ..kk");
                                                    if(editMsgStatus){
                                                        bindUI("msg_edit_done");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case UNARCHIVE_CONVERSATION_SERVER:
                                bind_ui_string=UNARCHIVE_CONVERSATION_SERVER;
                            case ARCHIVE_CONVERSATION_SERVER:
                                bind_ui_string=ARCHIVE_CONVERSATION_SERVER;
                                boolean archiveConversationStatus=false;
                                if(dataArr.length()>0){
                                    JSONObject archiveConversationObj=dataArr.getJSONObject(0);
                                    if(archiveConversationObj!=null){
                                        Conversation conversation=JsonParserClass
                                                .parseConversationJsonObject(archiveConversationObj);
                                        if(conversation!=null){
                                            ArrayList<Conversation> conversationArrayList=new ArrayList<Conversation>();
                                            conversationArrayList.add(conversation);
                                            rr=Repository.getRepository();
                                            if(rr!=null){
                                                archiveConversationStatus=rr.insertConversationData(conversationArrayList,
                                                        "update");
                                                Log.i(TAG+"_ar_un_chiveConvStatus:",
                                                        String.valueOf(archiveConversationStatus)+" ..kk");
                                                if(archiveConversationStatus){
                                                    bindUI(bind_ui_string);
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case REMOVE_MEMBER_SERVER:
                                if(dataArr.length()>0){
                                    String kt,skt;
                                    ArrayList<Conversation> cList=new ArrayList<Conversation>();
                                    ArrayList<Invitee> iList=new ArrayList<Invitee>();

                                    boolean conversationStatus,removeInviteeStatus;
                                    conversationStatus=removeInviteeStatus=false;

                                    for(int p=0;p<dataArr.length();p++){
                                        JSONObject rmvInviteeObj=dataArr.getJSONObject(p);
                                        if(rmvInviteeObj!=null){
                                            if(rmvInviteeObj.has(ConstantsObjects.KEY_TYPE_INNER)
                                                    && rmvInviteeObj.has(ConstantsObjects.SUB_KEY_TYPE_INNER)){
                                                kt=skt="";
                                                kt=rmvInviteeObj.getString(ConstantsObjects.KEY_TYPE_INNER);
                                                skt=rmvInviteeObj.getString(ConstantsObjects.SUB_KEY_TYPE_INNER);
                                                if(kt.equals(ConstantsObjects.KT_CONVERSATION)
                                                        && skt.equals(ConstantsObjects.SUB_KT_CONVERSATION_ARRAY1)){
                                                    Conversation conversation=JsonParserClass
                                                            .parseConversationJsonObject(rmvInviteeObj);
                                                    if(conversation!=null){
                                                        cList.add(conversation);
                                                    }
                                                }

                                                if(kt.equals(ConstantsObjects.KT_INVITEE)
                                                        && skt.equals(ConstantsObjects.SUB_KT_INVITEE)){
                                                    Invitee invitee=JsonParserClass.parseInviteeObject(rmvInviteeObj);
                                                    if(invitee!=null){
                                                        iList.add(invitee);
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    rr=Repository.getRepository();
                                    if(rr!=null){
                                        conversationStatus=rr.insertConversationData(cList,"update");
                                        Log.i(TAG+"_conUpdateStatus:",
                                                String.valueOf(conversationStatus)+" ..kk");

                                        removeInviteeStatus=rr.deleteInvitee(iList);
                                        Log.i(TAG+"_removeInviteeStatus:",
                                                String.valueOf(removeInviteeStatus)+" ..kk");
                                        if(removeInviteeStatus){
                                            bindUI("remove_invitee");
                                        }
                                    }
                                }
                                break;
                            case ADD_MEMBER_SERVER:
                                if(dataArr.length()>0){
                                    for(int b=0;b<dataArr.length();b++){
                                        JSONObject innrObj=dataArr.getJSONObject(b);
                                        if(innrObj!=null){
                                            if(innrObj.has(ConstantsObjects.KEY_TYPE_INNER)
                                                    && innrObj.has(ConstantsObjects.SUB_KEY_TYPE_INNER)){
                                                String kt,skt;
                                                kt=skt="";
                                                kt=innrObj.getString(ConstantsObjects.KEY_TYPE_INNER);
                                                skt=innrObj.getString(ConstantsObjects.SUB_KEY_TYPE_INNER);
                                                if(kt.equals(ConstantsObjects.KT_CONVERSATION)
                                                        && skt.equals(ConstantsObjects.SUB_KT_CONVERSATION_ARRAY1)){
                                                    Conversation conversation=JsonParserClass
                                                            .parseConversationJsonObject(innrObj);
                                                    if(conversation!=null){
                                                        boolean conversationStatus=false;
                                                        ArrayList<Conversation> cList=new ArrayList<Conversation>();
                                                        cList.add(conversation);
                                                        rr=Repository.getRepository();
                                                        if(rr!=null){
                                                            conversationStatus=rr.insertConversationData(cList,"update");
                                                            Log.i(TAG+"_conUpdateStatus:",
                                                                    String.valueOf(conversationStatus)+" ..kk");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    boolean inviteeStatus=false;
                                    rr=Repository.getRepository();
                                    if(rr!=null){
                                        inviteeStatus=rr.updateOrInsertInvitee(object);
                                        Log.i(TAG+"_inviteeInsrtStatus:",
                                                String.valueOf(inviteeStatus)+" ..kk");
                                        if(inviteeStatus){
                                            bindUI("add_invitee");
                                        }
                                    }
                                }
                                break;
                            case SNIP_MESSAGE_SERVER:
                                if(dataArr.length()>0){
                                    JSONObject msgDltInnerObj=dataArr.getJSONObject(0);
                                    if(msgDltInnerObj!=null){
                                        if(msgDltInnerObj.has(ConstantsObjects.KEY_VAL_INNER)){
                                            String keyval=msgDltInnerObj.getString(ConstantsObjects.KEY_VAL_INNER);
                                            rr=Repository.getRepository();
                                            if(rr!=null){
                                                boolean dltMsgStatus=false;
                                                dltMsgStatus=rr.deleteMessage(keyval);
                                                Log.i(TAG+"_dltMsgStatus:",String.valueOf(dltMsgStatus)+" ..kk");
                                                if(dltMsgStatus){
                                                    bindUI("delete_msg");
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case SEARCH_ALL_CONTACT_SERVER:
                                GlobalClass.setSearchAllContactResult(object);
                                bindUI("search_contact_done");
                                break;
                            case FETCH_CONTACT_USERS_SERVER://this response is coming now now
                                GlobalClass.setSearchContact(object);
                                bindUI("searchContactComplete");
                                break;
                            case ERASE_CONVERSATION_SERVER:
                                if(dataArr.length()>0){
                                    boolean msgDltStatus=false;
                                    JSONObject conversationObj=dataArr.getJSONObject(0);
                                    if(conversationObj!=null){
                                        if(conversationObj.has(ConstantsObjects.LINKUP_ID)){
                                            String linkupId=conversationObj.getString(ConstantsObjects.LINKUP_ID);
                                            Log.i(TAG+"_linkupId:",linkupId+" ..kk");
                                            rr=Repository.getRepository();
                                            if(rr!=null && linkupId!=null){
                                                ArrayList<Messages> msgList=(ArrayList<Messages>)rr.getAllMessages(linkupId);
                                                msgDltStatus=rr.deleteMessageList(msgList);
                                                Log.i(TAG+"_clearChtStatus:",String.valueOf(msgDltStatus)+" ..kk");
                                                if(msgDltStatus){
                                                    bindUI("clear_chat");
                                                }
                                            }
                                        }
                                    }
                                }
                            case SNIP_CONVERSATION_SERVER:
                                boolean conversationDltStatus=false;
                                if(dataArr.length()>0){
                                    JSONObject conversationDltInnerObj=dataArr.getJSONObject(0);
                                    if(conversationDltInnerObj!=null){
                                        if(conversationDltInnerObj.has(ConstantsObjects.KEY_VAL_INNER)){
                                            String keyval=conversationDltInnerObj.getString(ConstantsObjects.KEY_VAL_INNER);
                                            String ownerId="";
                                            if(conversationDltInnerObj.has("OWNER_ID")){
                                                ownerId=conversationDltInnerObj.getString("OWNER_ID");
                                            }
                                            rr=Repository.getRepository();
                                            if(keyval!=null && rr!=null){
                                                conversationDltStatus=rr.deleteConversation(keyval);
                                                Log.i("dltConversation:",String.valueOf(conversationDltStatus)+" ..kk");
                                                if(conversationDltStatus){
                                                    //invitee delete
                                                    ArrayList<Invitee> invitees=new ArrayList<Invitee>();
                                                    invitees=(ArrayList<Invitee>)rr.getAllInvitees(ownerId);
                                                    Log.i(TAG+"_inviteeSz:",String.valueOf(invitees.size())+" ..kk");
                                                    boolean inviteeDeleteStatus=false;
                                                    if(invitees.size()>0){
                                                        inviteeDeleteStatus=rr.deleteInvitee(invitees);
                                                    }
                                                    Log.i(TAG+"_DltInvitee:",String.valueOf(inviteeDeleteStatus)+" ..kk");
                                                    //message delete
                                                    ArrayList<Messages> msgList=new ArrayList<Messages>();
                                                    msgList=(ArrayList<Messages>)rr.getAllMessagesByGroupId(keyval);
                                                    Log.i(TAG+"_messageSz:",String.valueOf(msgList.size())+" ..kk");
                                                    boolean msgDeleteStatus=false;
                                                    if(msgList.size()>0){
                                                        msgDeleteStatus=rr.deleteMessageList(msgList);
                                                    }
                                                    Log.i(TAG+"_DltMessages:",String.valueOf(msgDeleteStatus)+" ..kk");
                                                    bindUI("delete_conversation");
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case UPDATE_CONVERSATION_SERVER:
                                boolean conversationUpdateStatus=false;
                                if(dataArr.length()>0){
                                    String kt,skt;
                                    for(int i=0;i<dataArr.length();i++){
                                        JSONObject conversationObj=dataArr.getJSONObject(i);
                                        if(conversationObj!=null){
                                            if(conversationObj.has(ConstantsObjects.KEY_TYPE_INNER)
                                                    && conversationObj.has(ConstantsObjects.SUB_KEY_TYPE_INNER)){
                                                kt=skt="";
                                                kt=conversationObj.getString(ConstantsObjects.KEY_TYPE_INNER);
                                                skt=conversationObj.getString(ConstantsObjects.SUB_KEY_TYPE_INNER);
                                                if(kt.equals(ConstantsObjects.KT_TSK)
                                                        && (skt.equals(ConstantsObjects.SUB_KT_CONVERSATION_ONE_TO_ONE)
                                                            || skt.equals(ConstantsObjects.SUB_KT_CONVERSATION_ARRAY1))){
                                                    Conversation conversation=JsonParserClass
                                                            .parseConversationJsonObject(conversationObj);
                                                    rr=Repository.getRepository();
                                                    if(conversation!=null && rr!=null){
                                                        ArrayList<Conversation> cList=new ArrayList<>();
                                                        cList.add(conversation);
                                                        if(cList.size()>0){
                                                            conversationUpdateStatus=rr.insertConversationData(cList,"update");
                                                            Log.i(TAG+"_conversationUpdate:",
                                                                    String.valueOf(conversationUpdateStatus)+" ..kk");
                                                            if(conversationUpdateStatus){
                                                                bindUI("conversation_update");
                                                            }
                                                        }
                                                    }
                                                }
                                                if(kt.equals(ConstantsObjects.KT_TSK)
                                                        && skt.equals(ConstantsObjects.SUB_KT_INVITEE)){
                                                    Invitee invitee=JsonParserClass.parseInviteeObject(conversationObj);
                                                    rr=Repository.getRepository();
                                                    if(invitee!=null && rr!=null){
                                                        boolean inviteeStatus=rr.updateInvitee(invitee);
                                                        if(inviteeStatus){
                                                            Log.i(TAG+"_inviteeStatus:","update_success");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case REMOVE_ADMIN_SERVER:
                            case MAKE_ADMIN_SERVER:
                                boolean inviteeUpdateStatus;
                                conversationUpdateStatus=inviteeUpdateStatus=false;
                                String kt,skt;
                                rr=Repository.getRepository();
                                for(int o=0;o<dataArr.length();o++){
                                    JSONObject innerObjkt=dataArr.getJSONObject(o);
                                    if(innerObjkt!=null){
                                        if(innerObjkt.has(ConstantsObjects.KEY_TYPE_INNER)
                                                && innerObjkt.has(ConstantsObjects.SUB_KEY_TYPE_INNER)){
                                            kt=skt="";
                                            kt=innerObjkt.getString(ConstantsObjects.KEY_TYPE_INNER);
                                            skt=innerObjkt.getString(ConstantsObjects.SUB_KEY_TYPE_INNER);
                                            if(kt.equals(ConstantsObjects.KT_TSK)
                                                    && skt.equals(ConstantsObjects.SUB_KT_CONVERSATION_ARRAY1)){
                                                //this is conversation object
                                                Conversation conversation=JsonParserClass
                                                        .parseConversationJsonObject(innerObjkt);
                                                ArrayList<Conversation> cList=new ArrayList<Conversation>();
                                                cList.add(conversation);
                                                conversationUpdateStatus=rr
                                                        .insertConversationData(cList,"update");
                                            }
                                            if(kt.equals(ConstantsObjects.KT_INVITEE)
                                                    && skt.equals(ConstantsObjects.SUB_KT_INVITEE)){
                                                //this is invitee object
                                                Invitee invitee=JsonParserClass
                                                        .parseInviteeObject(innerObjkt);
                                                inviteeUpdateStatus=rr.updateInvitee(invitee);
                                            }
                                        }
                                    }
                                }
                                if(conversationUpdateStatus && inviteeUpdateStatus){
                                    Log.i(TAG+"_make/removeAdmin:",String.valueOf(true)+" ..kk");
                                    bindUI("make/remove_admin");
                                }else{
                                    Log.i(TAG+"_make/removeAdmin:",String.valueOf(false)+" ..kk");
                                }
                                break;
                            case NESTED_MESSAGE_SERVER:
                            case ADD_MESSAGE_SERVER:
                                boolean msgSendStatus=false;
                                rr=Repository.getRepository();
                                if(rr!=null && object!=null){
                                    boolean deleteOfflineMsgStatus=rr.deleteOfflineMessage(object);
                                    if(deleteOfflineMsgStatus){
                                        offlineCounter=offlineCounter+1;
                                        if(GlobalClass.getOfflineObjectCounter()==offlineCounter){
                                            offlineCounter=0;
                                            rr=Repository.getRepository();
                                            if(rr!=null){
                                                ArrayList<OfflineSync> offlineSyncArrayList=
                                                        (ArrayList<OfflineSync>)rr.getOfflineSyncDataList();
                                                rr.handleOfflineData(offlineSyncArrayList);
                                            }
                                        }
                                    }
                                    Log.i(TAG+"_dltOfflnStatus:",String.valueOf(deleteOfflineMsgStatus)+" ..kk");
                                    msgSendStatus=rr.updateOrInsertMessage(object);
                                    if(dataArr.length()>0){
                                        JSONObject msgInnerObj=dataArr.getJSONObject(0);
                                        if(msgInnerObj!=null){
                                            if(msgInnerObj.has(ConstantsObjects.CML_REF_ID)){
                                                String cmlRefId=msgInnerObj.getString(ConstantsObjects.CML_REF_ID);
                                                String cmlTitle=msgInnerObj.getString(ConstantsObjects.CML_TITLE);
                                                Application application=GlobalClass.getApplication();
                                                if(application!=null){
                                                    ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                                                    if(db!=null){
                                                        ConversationDao conversationDao=db.conversationDao();
                                                        if(conversationDao!=null && cmlRefId!=null && cmlTitle!=null){
                                                            conversationDao
                                                                    .updateConversationLatestMessage(cmlRefId,cmlTitle);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Log.i(TAG+"_msgSendStatus:",String.valueOf(msgSendStatus)+" ..kk");
                                    if(msgSendStatus){
                                        bindUI("broadMsgStatus");
                                    }
                                }
                                break;
                            case ADD_COMMENT_SERVER:
                                if(dataArr.length()>0){
                                    kt=skt="";
                                    boolean conversationLatestMsgStatus=false;
                                    JSONObject conversationCommentObj=dataArr.getJSONObject(0);
                                    if(conversationCommentObj!=null){
                                        if(conversationCommentObj.has(ConstantsObjects.KEY_TYPE_INNER)
                                                && conversationCommentObj.has(ConstantsObjects.SUB_KEY_TYPE_INNER)){
                                            kt=conversationCommentObj.getString(ConstantsObjects.KEY_TYPE_INNER);
                                            skt=conversationCommentObj.getString(ConstantsObjects.SUB_KEY_TYPE_INNER);
                                            if(kt!=null && skt!=null){
                                                if(kt.equals(ConstantsObjects.KT_TSK)
                                                        && skt.equals(ConstantsObjects.SUB_KT_MESSAGE)){
                                                    //here updating conversation latest message
                                                    String cmlRefId=conversationCommentObj.getString(ConstantsObjects.CML_REF_ID);
                                                    String latestMsg=conversationCommentObj.getString(ConstantsObjects.CML_TITLE);
                                                    rr=Repository.getRepository();
                                                    if(rr!=null){
                                                        conversationLatestMsgStatus=
                                                                rr.updateConversationLatestMessage(cmlRefId,latestMsg);
                                                        Log.i(TAG+"_commentServr:",String.valueOf(conversationLatestMsgStatus)+" ..kk");
                                                        if(conversationLatestMsgStatus){
                                                            bindUI("conversation_latest_msg");
                                                        }
                                                    }
                                                    //here storing message object in message table
                                                    if(rr!=null){
                                                        boolean msgStatus=false;
                                                        msgStatus=rr.updateOrInsertMessage(object);
                                                        Log.i(TAG+"_cmnt_msg_status:",String.valueOf(msgStatus)+" ..kk");
                                                        if(msgStatus){
                                                            bindUI("broadMsgStatus");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case FETCH_SPECIFIC_GROUP_SERVER:
                            case CAST_CONVERSATION_SERVER:
                            case FETCH_CONVERSATION_FILTER_SERVER:
                                    ArrayList<Conversation> conversationList=new ArrayList<>();
                                    JSONObject oneToOneJSon=null;
                                    for(int i=0;i<dataArr.length();i++){
                                        JSONObject convObj=dataArr.getJSONObject(i);
                                        oneToOneJSon=convObj;
                                        if(convObj.has(ConstantsObjects.KEY_TYPE_INNER)
                                                && convObj.has(ConstantsObjects.SUB_KEY_TYPE_INNER)
                                                && convObj.has(ConstantsObjects.CML_ACCEPTED)){
                                            kt=skt="";
                                            kt=convObj.getString(ConstantsObjects.KEY_TYPE_INNER);
                                            skt=convObj.getString(ConstantsObjects.SUB_KEY_TYPE_INNER);
                                            int cmlAcceptedValue=convObj.getInt(ConstantsObjects.CML_ACCEPTED);
                                            if(kt!=null && skt!=null){
                                                if(kt.equals(ConstantsObjects.KT_CONVERSATION)
                                                        && (skt.equals(ConstantsObjects.SUB_KT_CONVERSATION_ARRAY1)
                                                        || skt.equals(ConstantsObjects.SUB_KT_CONVERSATION_ONE_TO_ONE))
                                                        && cmlAcceptedValue!=9){
                                                    if(convObj.has(ConstantsObjects.LAST_ID_OBJECT)){
                                                        JSONObject lastIdObjectObb=convObj.getJSONObject(
                                                                ConstantsObjects.LAST_ID_OBJECT);
                                                        Log.i(TAG+"_convName:",convObj.getString(ConstantsObjects.CML_TITLE)+" ..kk");
                                                        Log.i(TAG+"_conversationLstIdObj:",String.valueOf(lastIdObjectObb)+" ..kk");
                                                        GlobalClass
                                                                .setConversationLastIdObject(lastIdObjectObb);
                                                    }
                                                    Conversation conv1=JsonParserClass.parseConversationJsonObject(convObj);
                                                    conversationList.add(conv1);
                                                }
                                            }
                                        }
                                    }
                                    boolean conversationStatus=false;
                                    rr=Repository.getRepository();
                                    if(rr!=null && conversationList.size()>0){
                                        conversationStatus=rr.insertConversationData(conversationList,"insert");
                                        Log.i("status_conv_lst:",String.valueOf(conversationStatus)+" ..kk");
                                        if(conversationStatus){
                                            bindUI("CHAT_FILTER_SERVER");
                                            bindUI("CHAT_FILTER_SERVERCCCCCC"+oneToOneJSon);
                                        }else{
                                            bindUI("no_conversation");
                                        }
                                    }
                                    /*boolean offlineStatus=rr.handleOfflineData(object);
                                    Log.i(TAG+"_offlineStatus:",String.valueOf(offlineStatus)+" ..kk");*/
                                break;
                            case SPECIFIC_INVITEE_CHAT_SERVER:
                                if(dataArr.length()>0){
                                    JSONObject oneToOneObj=dataArr.getJSONObject(0);
                                    if(oneToOneObj!=null){
                                        boolean isOneToOneConversationDeleted=false;
                                        if(oneToOneObj.has(ConstantsObjects.KEY_TYPE_INNER) && oneToOneObj.has(ConstantsObjects.SUB_KEY_TYPE_INNER)
                                                && oneToOneObj.has(ConstantsObjects.IS_SINGLE_DELETE)){
                                            kt=skt="";
                                            boolean isSingleDelete=false;
                                            kt=oneToOneObj.getString(ConstantsObjects.KEY_TYPE_INNER);
                                            skt=oneToOneObj.getString(ConstantsObjects.SUB_KEY_TYPE_INNER);
                                            isSingleDelete=oneToOneObj.getBoolean(ConstantsObjects.IS_SINGLE_DELETE);
                                            if(kt.equals(ConstantsObjects.KT_CONVERSATION)
                                                    && skt.equals(ConstantsObjects.SUB_KT_CONVERSATION_ONE_TO_ONE)
                                                    && isSingleDelete){
                                                Conversation conversation=JsonParserClass.parseConversationJsonObject(oneToOneObj);
                                                rr=Repository.getRepository();
                                                if(conversation!=null && rr!=null){
                                                    isOneToOneConversationDeleted=rr.deleteConversation(conversation);
                                                    Log.i(TAG+"_1T1DltStatus:",String.valueOf(isOneToOneConversationDeleted)+" ..kk");
                                                    if(isOneToOneConversationDeleted){
                                                        bindUI("delete_conversation");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case UPDATE_CHAT_INVITEE_SERVER:
                                boolean status=false;
                                JSONObject inviteeObj=dataArr.getJSONObject(1);
                                if(inviteeObj.has("KEY_VAL") && inviteeObj.has("KEY_TYPE")
                                        && inviteeObj.has("SUB_KEY_TYPE")){
                                    String keyType,subKeyType;
                                    keyType=subKeyType="";
                                    keyType=inviteeObj.getString("KEY_TYPE");
                                    subKeyType=inviteeObj.getString("SUB_KEY_TYPE");
                                    if(keyType.equals("IDE") && subKeyType.equals("TSK_CGR_IDE")){
                                        Repository repo=Repository.getRepository();
                                        Invitee invitee=JsonParserClass.parseInviteeObject(inviteeObj);
                                        if(repo!=null && invitee!=null){
                                            status=repo.updateInvitee(invitee);
                                            if(status){
                                                bindUI(UPDATE_CHAT_INVITEE_SERVER);
                                            }
                                        }
                                    }
                                }
                                break;
                            case GET_ROLE_TASK_SERVER:
                                boolean isRoleTaskInserted=false;
                                if(dataArr.length()>0){
                                    ArrayList<RoleTask> roleTaskList=new ArrayList<RoleTask>();
                                    for(int i=0;i<dataArr.length();i++){
                                        JSONObject roleTaskObject=dataArr.getJSONObject(i);
                                        Log.i(TAG+"_roleTaskInnerObj:",String.valueOf(roleTaskObject)+" ..kk");
                                        if(roleTaskObject!=null){
                                            RoleTask roleTask=JsonParserClass.parseRoleTaskJSONObject(roleTaskObject);
                                            roleTaskList.add(roleTask);
                                        }
                                    }
                                    rr=Repository.getRepository();
                                    if(rr!=null){
                                        isRoleTaskInserted=rr.insertRoleTaskData(roleTaskList);
                                    }
                                }
                                if(isRoleTaskInserted){
                                    bindUI(GET_ROLE_TASK_SERVER);
                                }
                                break;
                            case FETCH_INVITEE_SERVER:
                                status=false;
                                Repository repo=Repository.getRepository();
                                if(repo!=null){
                                    status=repo.updateOrInsertInvitee(object);
                                    Log.i(TAG+"_invt_stats:",String.valueOf(status)+" ..kk");
                                    if(status){
                                        bindUI("fetch_invitee");
                                    }
                                }
                                break;
                            case FETCH_NESTED_MESSAGE_SERVER:
                            case FETCH_MESSAGE_FILTER_SERVER:
                                boolean fetchMsgsStatus=false;
                                repo=Repository.getRepository();
                                if(repo!=null){
                                    fetchMsgsStatus=repo.updateOrInsertMessage(object);
                                    Log.i(TAG+"_fetch_msg_status:",String.valueOf(fetchMsgsStatus));
                                    if(fetchMsgsStatus){
                                        bindUI("fetch_msg_complete");
                                    }
                                }
                                break;
                            case FETCH_USER_URM_SERVER:
                                if(dataArr.length()>0){
                                    for(int k=0;k<dataArr.length();k++){
                                        innerObj=dataArr.getJSONObject(k);
                                        Log.i("userUrmObj:",String.valueOf(innerObj)+" ..kk");
                                    }
                                    repo=Repository.getRepository();
                                    if(repo!=null){
                                        GlobalClass.setUserURMObject(object);
                                        bindUI(FETCH_USER_URM_SERVER);
                                    }
                                }
                                break;
                            case ADD_CONTACT_SERVER://when we create conversation by including member who is not in the contact list
                            case FETCH_ALL_CONTACT_SERVER://when we fetch contact list
                            case ADD_MESSAGING_CONTACT_SERVER://one-to-one scenario
                            case ADD_BLOCK_CONTACT_SERVER://one-to-one scenario block contact
                                boolean statusContact=false;
                                repo=Repository.getRepository();
                                if(repo!=null){
                                    statusContact=repo.insertContacts(object);
                                    Log.i(TAG+"_status_cntct:",String.valueOf(statusContact)+" ..kk");
                                    if(statusContact){
                                        bindUI("fetchContactComplete");
                                    }else{
                                        bindUI("no_contacts");
                                    }
                                }
                                break;
                            case FETCH_CONTACT_SERVER://fetching blocked contact scenario
                                boolean blockedContactStatus=false;
                                repo=Repository.getRepository();
                                if(repo!=null){
                                    blockedContactStatus=repo.insertContacts(object);
                                    Log.i(TAG+"_status_cntct:",String.valueOf(blockedContactStatus)+" ..kk");
                                    if(blockedContactStatus){
                                        bindUI("FETCH_CONTACT_SERVER");
                                    }
                                }
                                break;
                            case DELETE_CONTACT_SERVER:
                                status=false;
                                repo=Repository.getRepository();
                                if(repo!=null){
                                    status=repo.removeContact(object);
                                    if(status){
                                        bindUI("contact_removed");
                                    }
                                }
                                break;
                        }
                    }
                }catch(JSONException e){
                    Log.e("expn_handle_respnse1:",e.toString());
                }catch(Exception e){
                    Log.e("expn_handle_respnse2:",e.toString());
                }
            }
        }
    }

    private Observable<String> mObservable;
    private Observer<String> mObserver;
    private void bindUI(String string){
        if(GlobalClass.getCurrentSubcriberr()!=null){
            mObservable=GlobalClass.getCurrentSubcriberr().getObservable();
            mObserver=GlobalClass.getCurrentSubcriberr().getObserver();
            mObservable.just(string)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mObserver);
        }
        if(GlobalClass.getLoginFragmentSubcriberr()!=null){
            mObservable=GlobalClass.getLoginFragmentSubcriberr().getObservable();
            mObserver=GlobalClass.getLoginFragmentSubcriberr().getObserver();
            mObservable.just(string)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mObserver);
        }
        if(GlobalClass.getSignupSubcription()!=null){
            mObservable=GlobalClass.getSignupSubcription().getObservable();
            mObserver=GlobalClass.getSignupSubcription().getObserver();
            mObservable.just(string)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mObserver);
        }
    }
}