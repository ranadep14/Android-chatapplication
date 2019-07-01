package com.nc.developers.cloudscommunicator.socket;

import android.text.TextUtils;
import android.util.Log;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.database.ConstantsClass;
import com.nc.developers.cloudscommunicator.models.Calendar;
import com.nc.developers.cloudscommunicator.models.Contact;
import com.nc.developers.cloudscommunicator.models.Conversation;
import com.nc.developers.cloudscommunicator.models.Event;
import com.nc.developers.cloudscommunicator.models.Invitee;
import com.nc.developers.cloudscommunicator.models.Login;
import com.nc.developers.cloudscommunicator.models.Messages;
import com.nc.developers.cloudscommunicator.models.RoleTask;
import com.nc.developers.cloudscommunicator.models.UserURM;
import com.nc.developers.cloudscommunicator.objects.ConstantsObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParserClass {

    private static final String defaultImagePath=
            "http://keenthemes.com/preview/metronic/theme/assets/pages/media/profile/profile_user.jpg";
    private static final String TAG=JsonParserClass.class.getSimpleName();

    public static Calendar parseCalendarSyncResponse(JSONObject object){
        Calendar calendar=null;
        if(object!=null){
            calendar=new Calendar();
            try{
                if(object.has("CML_REF_ID")){
                    calendar.setCmlRefId(object.getString("CML_REF_ID"));
                }
                if(object.has("CML_SUB_CATEGORY")){
                    calendar.setCmlSubCategory(object.getString("CML_SUB_CATEGORY"));
                }
                if(object.has("CML_TITLE")){
                    calendar.setCmlTitle(object.getString("CML_TITLE"));
                }
                if(object.has("KEY_TYPE")){
                    calendar.setKeyType(object.getString("KEY_TYPE"));
                }
                if(object.has("SUB_KEY_TYPE")){
                    calendar.setSubKeyType(object.getString("SUB_KEY_TYPE"));
                }
                if(object.has("KEY_VAL")){
                    calendar.setKeyVal(object.getString("KEY_VAL"));
                }
                if(object.has("PROJECT_ID")){
                    calendar.setProjectId(object.getString("PROJECT_ID"));
                }
                calendar.setCompleteData(String.valueOf(object));
            }catch(JSONException e){
                Log.e(TAG+"_expn_parse_calSync1:",e.toString());
            }catch(Exception e){
                Log.e(TAG+"_expn_parse_calSync2:",e.toString());
            }
        }
        return calendar;
    }

    public static RoleTask parseRoleTaskJSONObject(JSONObject object){
        RoleTask roleTask=null;
        if(object!=null){
            roleTask=new RoleTask();
            try{
                if(object.has("CML_TITLE")){
                    roleTask.setCmlTitle(object.getString("CML_TITLE"));
                }
                if(object.has("KEY_VAL")){
                    roleTask.setKeyVal(object.getString("KEY_VAL"));
                }
                roleTask.setCompleteData(String.valueOf(object));
            }catch(JSONException e){
                Log.e(TAG+"_expn_role_task_obj1:",e.toString());
            }catch(Exception e){
                Log.e(TAG+"_expn_role_task_obj2:",e.toString());
            }
        }
        return roleTask;
    }

    public static UserURM parseUserURMJSONObject(JSONObject object){
        UserURM userURM=null;
        if(object!=null){
            userURM=new UserURM();
            try{
                if(object.has("KEY_VAL")){
                    userURM.setKeyVal(object.getString("KEY_VAL"));
                }
                if(object.has("CML_ROLE_ID")){
                    userURM.setCmlRoleId(object.getString("CML_ROLE_ID"));
                }
                if(object.has("KEY_TYPE")){
                    userURM.setKeyType(object.getString("KEY_TYPE"));
                }
                if(object.has("SUB_KEY_TYPE")){
                    userURM.setSubKeyType(object.getString("SUB_KEY_TYPE"));
                }
                if(object.has("PROJECT_ID")){
                    userURM.setProjectId(object.getString("PROJECT_ID"));
                }
                userURM.setCompleteData(String.valueOf(object));
            }catch(JSONException e){
                Log.e(TAG+"_expn_parse_urm_obj1:",e.toString());
            }catch(Exception e){
                Log.e(TAG+"_expn_parse_urm_obj1:",e.toString());
            }
        }
        return userURM;
    }
    public static Login parseLoginJSONObject(JSONObject object){
        Login login=null;
        if(object!=null){
            login=new Login();
            try{
                login.setRestData(String.valueOf(object));
                login.setInputUserId(GlobalClass.getUsername());
                login.setPassword(GlobalClass.getPassword());
                if(object.has("userKeyVal")){
                    login.setKeyVal(object.getString("userKeyVal"));
                }
                if(object.has("syncMap")){
                    JSONObject syncMapObj=object.getJSONObject("syncMap");
                    GlobalClass.setSyncMapJSONObject(syncMapObj);
                }
                if(object.has("userKeyVal")){
                    GlobalClass.setUserKeyval(object.getString("userKeyVal"));
                }
                if(object.has("USM_EMAIL")){
                    login.setUserEmail(object.getString("USM_EMAIL"));
                }else{
                    JSONObject myInfoObj=object.getJSONObject("myInfo");
                    if(myInfoObj!=null){
                        if(myInfoObj.has("userEmail")){
                            login.setUserEmail(myInfoObj.getString("userEmail"));
                        }
                    }
                }
                if(object.has("orgObject")){
                    JSONObject orgObject=object.getJSONObject("orgObject");
                    if(orgObject!=null){
                        if(orgObject.has("DEPT_ID")){
                            login.setDeptId(orgObject.getString("DEPT_ID"));
                        }
                        if(orgObject.has("ORG_ID")){
                            login.setOrgId(orgObject.getString("ORG_ID"));
                        }
                        if(orgObject.has("ORG_PROJECT_ID")){
                            login.setOrgProjectId(orgObject.getString("ORG_PROJECT_ID"));
                        }
                        if(orgObject.has("PROJECT_ID")){
                            login.setProjectId(orgObject.getString("PROJECT_ID"));
                        }
                        Log.i("orgObject_:",String.valueOf(orgObject)+" ..kk");
                        login.setOrgObject(String.valueOf(orgObject));
                    }
                }
                if((object.has("Info")) || ((!object.has("Info")) && object.has("myInfo"))){
                    JSONObject innerObj=null;
                    if(object.has("Info")){
                        JSONObject infoObject=object.getJSONObject("Info");
                        innerObj=infoObject.getJSONObject("myInfo");
                    }else{
                        if(object.has("myInfo")){
                            innerObj=object.getJSONObject("myInfo");
                        }
                    }
                    if(innerObj.has("userId")){
                        login.setUserId(innerObj.getString("userId"));
                    }
                    String firstname="",lastname="";
                    if(innerObj.has("firstName")){
                        firstname=innerObj.getString("firstName");
                        login.setFirstname(firstname);
                    }
                    if(innerObj.has("lastName")){
                        lastname=innerObj.getString("lastName");
                        login.setLastname(lastname);
                    }
                    if(firstname!=null && lastname!=null && !TextUtils.isEmpty(firstname)
                            && !TextUtils.isEmpty(lastname)){
                        login.setImageTitle(firstname + "_" + lastname);
                        login.setUsername(firstname + "_" + lastname);
                    }
                    if(innerObj.has("userUID")){
                        login.setUserUID(innerObj.getString("userUID"));
                    }
                    if(innerObj.has("ORG_ID")){
                        login.setOrgId(innerObj.getString("ORG_ID"));
                    }
                    if(innerObj.has("jwttoken")){
                        login.setJwtToken(innerObj.getString("jwttoken"));
                    }
                    if(innerObj.has("profile_data")){
                        JSONObject proDataObj=innerObj.getJSONObject("profile_data");
                        if(proDataObj.has("keyType")){
                            login.setKeyType(proDataObj.getString("keyType"));
                        }
                        if(proDataObj.has("imgPath")){
                            login.setImagePath(proDataObj.getString("imgPath"));
                        }
                    }
                }
            }catch(JSONException e){
                Log.i("expn_parse_login1:",e.toString());
            }catch(Exception e){
                Log.i("expn_parse_login2:",e.toString());
            }
        }
        return login;
    }

    public static Contact parseContactJSONObject(JSONObject object){
        Contact contact=null;
        if(object!=null){
            contact=new Contact();
            try{
                if(object.has("CML_TITLE")){
                    contact.setTitle(object.getString("CML_TITLE"));
                }
                if(object.has("CML_NICK_NAME")){
                    contact.setNickname(object.getString("CML_NICK_NAME"));
                }
                if(object.has("KEY_VAL")){
                    contact.setKeyval(object.getString("KEY_VAL"));
                }
                if(object.has("CML_FIRST_NAME")){
                    contact.setFirstname(object.getString("CML_FIRST_NAME"));
                }
                if(object.has("CML_LAST_NAME")){
                    contact.setLastname(object.getString("CML_LAST_NAME"));
                }
                if(object.has("CML_REF_ID")){
                    contact.setCmlRefId(object.getString("CML_REF_ID"));
                }
                if(object.has("CREATED_ON")){
                    contact.setCreatedOn(object.getString("CREATED_ON"));
                }
                if(object.has("CML_PERSONAL_EMAIL")){
                    contact.setPersonalEmail(object.getString("CML_PERSONAL_EMAIL"));
                }
                if(object.has("CML_IMAGE_PATH")){
                    JSONArray imagePathArr=object.getJSONArray("CML_IMAGE_PATH");
                    if(imagePathArr!=null){
                        if(imagePathArr.length()>0){
                            contact.setImagePath(imagePathArr.getString(0));
                        }else{
                            contact.setImagePath(JsonParserClass.defaultImagePath);
                        }
                    }
                }else{
                    contact.setImagePath(JsonParserClass.defaultImagePath);
                }
                if(object.has("CML_OFFICIAL_EMAIL")){
                    contact.setOfficialEmail(object.getString("CML_OFFICIAL_EMAIL"));
                }
                if(object.has("KEY_TYPE")){
                    contact.setKeytype(object.getString("KEY_TYPE"));
                }
                if(object.has("SUB_KEY_TYPE")){
                    contact.setSubkeytype(object.getString("SUB_KEY_TYPE"));
                }
                if(object.has("CML_ACCEPTED")){
                    contact.setCmlAccepted(object.getInt("CML_ACCEPTED"));
                }
                contact.setCompleteData(String.valueOf(object));
            }catch(JSONException e){
                Log.e("expn_parse_contact1:",e.toString());
            }catch(Exception e){
                Log.e("expn_parse_contact2:",e.toString());
            }
        }
        return contact;
    }

    public static Messages parseMessagesJSONObject(JSONObject object){
        Messages messages=new Messages();
        try{
            if(object.has("CML_TITLE")){
                messages.setCmlTitle(object.getString("CML_TITLE"));
            }
            if(object.has("CREATED_BY")){
                messages.setCreatedBy(object.getString("CREATED_BY"));
            }
            if(object.has("KEY_VAL")){
                messages.setKeyval(object.getString("KEY_VAL"));
            }
            if(object.has("KEY_TYPE")){
                messages.setKeytype(object.getString("KEY_TYPE"));
            }
            if(object.has("SUB_KEY_TYPE")){
                messages.setSubKeytype(object.getString("SUB_KEY_TYPE"));
            }
            if(object.has("CML_CHT_MESSAGE_TYPE")){
                messages.setMesssageType(object.getInt("CML_CHT_MESSAGE_TYPE"));
            }
            if(object.has("CML_STAR")){
                messages.setCmlStar(object.getInt("CML_STAR"));
            }
            if(object.has("CML_MESSAGE_INDEX")){
                messages.setCmlMessageIndex(object.getLong("CML_MESSAGE_INDEX"));
            }
            if(object.has("CML_REF_ID")){
                messages.setCmlRefId(object.getString("CML_REF_ID"));
            }
            if(object.has("CML_IMAGE_PATH")){
                messages.setImagePath(object.getString("CML_IMAGE_PATH"));
            }
            if(object.has("CML_TYPE")){
                messages.setCmlType(object.getString("CML_TYPE"));
            }
            if(object.has("GROUP_ID")){
                messages.setGroupId(object.getString("GROUP_ID"));
            }
            if(object.has("CML_ISREPLY")){
                messages.setCmlIsReply(object.getBoolean("CML_ISREPLY"));
            }
            if(object.has("CML_PARENT_TASK_ID")){
                messages.setParentTaskId(object.getString("CML_PARENT_TASK_ID"));
            }
            if(object.has("CML_COMMENT_TYPE")){
                String commentType=object.getString("CML_COMMENT_TYPE");
                if(commentType!=null){
                    if(commentType.equals("EVENT")){
                        messages.setIsThisEventMessage("yes");
                    }else{
                        messages.setIsThisEventMessage("no");
                    }
                }else{
                    messages.setIsThisEventMessage("no");
                }
            }else{
                messages.setIsThisEventMessage("no");
            }
            messages.setCompleteData(String.valueOf(object));
        }catch(JSONException e){
            Log.i("expn_parse_messages1:",e.toString());
        }catch(Exception e){
            Log.i("expn_parse_messages2:",e.toString());
        }
        return messages;
    }

    public static Invitee parseInviteeObject(JSONObject object){
        Invitee invitee=new Invitee();
        try{
            if(object.has("IDE_ATTENDEES_EMAIL")){
                invitee.setIde_attendees_email(object.getString("IDE_ATTENDEES_EMAIL"));
            }
            if(object.has("KEY_VAL")){
                invitee.setKeyval(object.getString("KEY_VAL"));
            }
            if(object.has("CML_UNREAD_COUNT")){
                invitee.setCmlUnreadCount(object.getString("CML_UNREAD_COUNT"));
            }
            if(object.has("IDE_CML_ID")){
                invitee.setIdeCmlId(object.getString("IDE_CML_ID"));
            }
            if(object.has("IDE_DESIGNATION")){
                JSONArray arr=object.getJSONArray("IDE_DESIGNATION");
                if(arr!=null && arr.length()>0){
                    invitee.setIde_designation(arr.getString(0));
                }else{
                    invitee.setIde_designation("");
                }
            }
            if(object.has("URM_PROJECT_ID")){
                invitee.setUrm_project_id(object.getString("URM_PROJECT_ID"));
            }
            if(object.has("CREATED_BY")){
                invitee.setCreatedBy(object.getString("CREATED_BY"));
            }
            if(object.has("CREATED_ON")){
                invitee.setCreatedOn(object.getString("CREATED_ON"));
            }
            if(object.has("LAST_MODIFIED_BY")){
                invitee.setLastModifiedBy(object.getString("LAST_MODIFIED_BY"));
            }
            if(object.has("LAST_MODIFIED_ON")){
                invitee.setLastModifiedOn(object.getString("LAST_MODIFIED_ON"));
            }
            if(object.has("ADDED_BY")){
                invitee.setAddedBy(object.getString("ADDED_BY"));
            }
            if(object.has("IDE_ORIGINAL_CREATOR")){
                invitee.setIdeOriginalCreator(object.getString("IDE_ORIGINAL_CREATOR"));
            }
            if(object.has("PARENT_KEY_TYPE")){
                invitee.setParentKeyType(object.getString("PARENT_KEY_TYPE"));
            }
            if(object.has("PARENT_SUB_KEY_TYPE")){
                invitee.setParentSubKeyType(object.getString("PARENT_SUB_KEY_TYPE"));
            }
            if(object.has("SUB_KEY_TYPE")){
                invitee.setSubKeyType(object.getString("SUB_KEY_TYPE"));
            }
            if(object.has("SYNC_PENDING_STATUS")){
                invitee.setSyncPendingStatus(object.getString("SYNC_PENDING_STATUS"));
            }
            if(object.has("CML_IS_LATEST")){
                invitee.setCmlIsLatest(String.valueOf(object.getInt("CML_IS_LATEST")));
            }
            if(object.has("CML_PRIORITY")){
                invitee.setCmlPriority(String.valueOf(object.getInt("CML_PRIORITY")));
            }
            if(object.has("CML_STAR")){
                invitee.setCmlStar(String.valueOf(object.getInt("CML_STAR")));
            }
            if(object.has("IDE_ACCEPTED")){
                invitee.setIdeAccepted(String.valueOf(object.getInt("IDE_ACCEPTED")));
            }
            if(object.has("IDE_TYPE")){
                invitee.setIdeType(object.getString("IDE_TYPE"));
            }
            if(object.has("KEY_TYPE")){
                invitee.setKeyType(object.getString("KEY_TYPE"));
            }
            if(object.has("ACTIVE_STATUS")){
                invitee.setActiveStatus(String.valueOf(object.getInt("ACTIVE_STATUS")));
            }
            if(object.has("CML_ACCEPTED")){
                invitee.setCmlAccepted(String.valueOf(object.getInt("CML_ACCEPTED")));
            }
            if(object.has("CML_IS_ACTIVE")){
                invitee.setCmlIsActive(object.getString("CML_IS_ACTIVE"));
            }
            if(object.has("CML_ASSIGNED")){
                invitee.setCmlAssigned(object.getString("CML_ASSIGNED"));
            }
            if(object.has("CML_SUB_CATEGORY")){
                invitee.setCmlSubCategory(object.getString("CML_SUB_CATEGORY"));
            }
            invitee.setCompleteData(String.valueOf(object));
        }catch(JSONException e){
            Log.e("expn_ftch_invitee1:",e.toString());
        }catch(Exception e){
            Log.e("expn_ftch_invitee2:",e.toString());
        }
        return invitee;
    }

    public static Conversation parseConversationJsonObject(JSONObject object){
        Conversation conv=null;
        if(object!=null){
            conv=new Conversation();
            try{
                if(object.has("ACTIVE_STATUS")){
                    conv.setActiveStatus(object.getInt("ACTIVE_STATUS"));
                }
                if(object.has("CML_ACCEPTED")){
                    conv.setCmlAccepted(object.getInt("CML_ACCEPTED"));
                }
                if(object.has("CML_TITLE")){
                    conv.setCmlTitle(object.getString("CML_TITLE"));
                }
                if(object.has("KEY_VAL")){
                    conv.setKeyval(object.getString("KEY_VAL"));
                }
                if(object.has("USER_LAST_MODIFIED_ON")){
                    conv.setLastModifiedOn(object.getString("USER_LAST_MODIFIED_ON"));
                }
                if(object.has("OWNER_ID")){
                    conv.setOwnerId(object.getString("OWNER_ID"));
                }
                if(object.has("CML_REF_ID")){
                    conv.setCmlRefId(object.getString("CML_REF_ID"));
                }
                if(object.has("CML_UNREAD_COUNT")){
                    conv.setUnreadCount(object.getInt("CML_UNREAD_COUNT"));
                }
                if(object.has("CREATED_ON")){
                    conv.setCreatedOn(object.getString("CREATED_ON"));
                }
                if(object.has("LATEST_MESSAGE")){
                    conv.setLatestMessage(object.getString("LATEST_MESSAGE"));
                }
                if(object.has("CREATED_BY")){
                    conv.setCreatedBy(object.getString("CREATED_BY"));
                }
                if(object.has("SUB_KEY_TYPE")){
                    conv.setSubKeyType(object.getString("SUB_KEY_TYPE"));
                }
                if(object.has("KEY_TYPE")){
                    conv.setKeyType(object.getString("KEY_TYPE"));
                }
                if(object.has("LINKUP_ID")){
                    conv.setLinkupId(object.getString("LINKUP_ID"));
                }
                if(object.has("CML_IMAGE_PATH")){
                    conv.setCmlImagePath(object.getString("CML_IMAGE_PATH"));
                }else{
                    conv.setCmlImagePath("");
                }
                conv.setCompleteData(String.valueOf(object));
            }catch(JSONException e){
                Log.e("expn_parse_conv1:",e.toString());
            }catch(Exception e){
                Log.e("expn_parse_conv2:",e.toString());
            }
        }
        return conv;
    }
    public static Event parseEventJsonObject(JSONObject object){
        Event event=null;
        if(object!=null){
            event=new Event();
            try{
                if(object.has(ConstantsObjects.CML_TITLE)){
                    event.setCmlTitle(object.getString(ConstantsObjects.CML_TITLE));
                }
                if(object.has(ConstantsObjects.CML_DESCRIPTION)){
                    event.setCmlDescription(object.getString(ConstantsObjects.CML_DESCRIPTION));
                }
                if(object.has(ConstantsObjects.KEY_VAL_INNER)){
                    event.setKeyVal(object.getString(ConstantsObjects.KEY_VAL_INNER));
                }
                if(object.has(ConstantsObjects.KEY_TYPE_INNER)){
                    event.setKeyType(object.getString(ConstantsObjects.KEY_TYPE_INNER));
                }
                if(object.has(ConstantsObjects.SUB_KEY_TYPE_INNER)){
                    event.setSubKeyType(object.getString(ConstantsObjects.SUB_KEY_TYPE_INNER));
                }
                if(object.has(ConstantsObjects.ACTIVE_STATUS)){
                    event.setActiveStatus(object.getInt(ConstantsObjects.ACTIVE_STATUS));
                }
                if(object.has(ConstantsObjects.CML_LOCATION)){
                    event.setEventLocation(object.getString(ConstantsObjects.CML_LOCATION));
                }
                if(object.has(ConstantsObjects.CML_FROM_DATETIME)){
                    event.setEventFromDate(object.getString(ConstantsObjects.CML_FROM_DATETIME));
                }
                if(object.has(ConstantsObjects.CML_TO_DATETIME)){
                    event.setEventToDate(object.getString(ConstantsObjects.CML_TO_DATETIME));
                }
                if(object.has("PARENT_CONVERSATION_ID")){
                    event.setEventConversationId(object.getString("PARENT_CONVERSATION_ID"));
                }
                if(object.has(ConstantsObjects.OWNER_ID)){
                    event.setOwnerId(object.getString(ConstantsObjects.OWNER_ID));
                }
                event.setCompleteData(String.valueOf(object));
            }catch(JSONException e){
                Log.e("expn_parse_event1:",e.toString());
            }catch(Exception e){
                Log.e("expn_parse_event2:",e.toString());
            }
        }
        return event;
    }
}