package com.nc.developers.cloudscommunicator;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.nc.developers.cloudscommunicator.dao.CalendarDao;
import com.nc.developers.cloudscommunicator.dao.ContactDao;
import com.nc.developers.cloudscommunicator.dao.ConversationDao;
import com.nc.developers.cloudscommunicator.dao.EventDao;
import com.nc.developers.cloudscommunicator.dao.InviteeDao;
import com.nc.developers.cloudscommunicator.dao.LoginDao;
import com.nc.developers.cloudscommunicator.dao.MessagesDao;
import com.nc.developers.cloudscommunicator.dao.OfflineSyncDao;
import com.nc.developers.cloudscommunicator.dao.RoleTaskDao;
import com.nc.developers.cloudscommunicator.dao.UserURMDao;
import com.nc.developers.cloudscommunicator.database.ClouzerDatabase;
import com.nc.developers.cloudscommunicator.models.Calendar;
import com.nc.developers.cloudscommunicator.models.Contact;
import com.nc.developers.cloudscommunicator.models.Conversation;
import com.nc.developers.cloudscommunicator.models.Event;
import com.nc.developers.cloudscommunicator.models.Invitee;
import com.nc.developers.cloudscommunicator.models.Login;
import com.nc.developers.cloudscommunicator.models.Messages;
import com.nc.developers.cloudscommunicator.models.OfflineSync;
import com.nc.developers.cloudscommunicator.models.RoleTask;
import com.nc.developers.cloudscommunicator.models.UserURM;
import com.nc.developers.cloudscommunicator.objects.ConstantsObjects;
import com.nc.developers.cloudscommunicator.socket.JsonParserClass;
import com.nc.developers.cloudscommunicator.utils.CheckInternetConnectionCommunicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

public class Repository{
    private static volatile Repository INSTANCE;
    private boolean status=false;
    private ClouzerDatabase db=null;

    private static String TAG=Repository.class.getSimpleName();

    public static Repository getRepository(){
        if(INSTANCE==null){
            synchronized(Repository.class){
                if(INSTANCE==null){
                    INSTANCE=new Repository();
                }
            }
        }
        return INSTANCE;
    }

    public boolean updateConversationLatestMessage(String conversation_cmlRefId,String msg){
        boolean conversationLatestMsgStatus=false;
        Application application=GlobalClass.getApplication();
        if(application!=null){
            ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
            if(db!=null){
                ConversationDao conversationDao=db.conversationDao();
                if(conversationDao!=null && conversation_cmlRefId!=null && msg!=null){
                    conversationDao.updateConversationLatestMessage(msg,conversation_cmlRefId);
                    conversationLatestMsgStatus=true;
                }
            }
        }
        return conversationLatestMsgStatus;
    }
    public boolean insertInvitees(final ArrayList<Invitee> invitees){
        boolean inviteeStatus=false;
        Callable<Boolean> callableInvitee=null;
        callableInvitee=new Callable<Boolean>(){
            @Override
            public Boolean call(){
                boolean bk=false;
                try{
                    Application application=GlobalClass.getApplication();
                    ClouzerDatabase dbK=ClouzerDatabase.getDatabase(application);
                    if(dbK!=null){
                        InviteeDao inviteeDao=dbK.inviteeDao();
                        if(inviteeDao!=null){
                            ArrayList<Long> longs=(ArrayList<Long>)inviteeDao.insertInvitees(invitees);
                            if(longs!=null){
                                if(!longs.contains(-1)){
                                    bk=true;
                                }
                            }
                        }
                    }
                }catch(Exception e){
                    Log.i("expn_sync_invitee1:",e.toString());
                }
                return bk;
            }
        };
        try{
            ExecutorService service= Executors.newSingleThreadExecutor();
            Future<Boolean> future=service.submit(callableInvitee);
            status=future.get();
        }catch(InterruptedException e){
            Log.i("expn_sync_invitee2:",e.toString());
        }catch(ExecutionException e){
            Log.i("expn_sync_invitee3:",e.toString());
        }catch(Exception e){
            Log.i("expn_sync_invitee4:",e.toString());
        }
        return inviteeStatus;
    }
    public boolean insertCalendarData(final ArrayList<Calendar> calendars){
        boolean calendarStatus=false;
        Callable<Boolean> callableCalendar=null;
        callableCalendar=new Callable<Boolean>(){
            @Override
            public Boolean call(){
                boolean bk=false;
                try{
                    Application application=GlobalClass.getApplication();
                    ClouzerDatabase dbK=ClouzerDatabase.getDatabase(application);
                    if(dbK!=null){
                        CalendarDao calendarDao=dbK.calendarDao();
                        if(calendarDao!=null){
                            ArrayList<Long> longs=(ArrayList<Long>)calendarDao.insertCalendarSyncData(calendars);
                            if(longs!=null){
                                if(!longs.contains(-1)){
                                    bk=true;
                                }
                            }
                        }
                    }
                }catch(Exception e){
                    Log.i("expn_calSyncInsert1:",e.toString());
                }
                return bk;
            }
        };
        try{
            ExecutorService service= Executors.newSingleThreadExecutor();
            Future<Boolean> future=service.submit(callableCalendar);
            calendarStatus=future.get();
        }catch(InterruptedException e){
            Log.i("expn_calSyncInsert2:",e.toString());
        }catch(ExecutionException e){
            Log.i("expn_calSyncInsert3:",e.toString());
        }catch(Exception e){
            Log.i("expn_calSyncInsert4:",e.toString());
        }
        return calendarStatus;
    }
    public boolean updateOrInsertInvitee(final JSONObject object){
        boolean status=false;
        Callable<Boolean> callableInvitee=null;
        callableInvitee=new Callable<Boolean>(){
            @Override
            public Boolean call(){
                boolean bk=false;
                try{
                    Application application=GlobalClass.getApplication();
                    if(application!=null){
                        ClouzerDatabase dbK=ClouzerDatabase.getDatabase(application);
                        if(dbK!=null){
                            InviteeDao inviteeDao=dbK.inviteeDao();
                            if(inviteeDao!=null){
                                String keytype="",subkeytype="";
                                JSONArray dataArr=object.getJSONArray(ConstantsObjects.DATA_ARRAY);
                                if(dataArr!=null){
                                    if(dataArr.length()>0){
                                        ArrayList<Invitee> inviteeLst=new ArrayList<Invitee>();
                                        for(int i=0;i<dataArr.length();i++){
                                            JSONObject innerObj=dataArr.getJSONObject(i);
                                            if(innerObj!=null){
                                                if(innerObj.has(ConstantsObjects.KEY_TYPE_INNER)
                                                        && innerObj.has(ConstantsObjects.SUB_KEY_TYPE_INNER)){
                                                    keytype=innerObj.getString(ConstantsObjects.KEY_TYPE_INNER);
                                                    subkeytype=innerObj.getString(ConstantsObjects.SUB_KEY_TYPE_INNER);
                                                    if(keytype.equals(ConstantsObjects.KT_INVITEE)
                                                            && subkeytype.equals(ConstantsObjects.SUB_KT_INVITEE)){
                                                        Invitee invitee=JsonParserClass.parseInviteeObject(innerObj);
                                                        inviteeLst.add(invitee);
                                                    }
                                                }
                                            }
                                        }
                                        bk=inviteeDao.insertAllInvitees(inviteeLst);
                                    }
                                }
                            }
                        }
                    }
                }catch(JSONException e){
                    Log.i("expn_insrt_invitee1:",e.toString());
                }catch(Exception e){
                    Log.i("expn_insrt_invitee2:",e.toString());
                }
                return bk;
            }
        };
        try{
            ExecutorService service= Executors.newSingleThreadExecutor();
            Future<Boolean> future=service.submit(callableInvitee);
            status=future.get();
        }catch(InterruptedException e){
            Log.i("expn_insrt_invitee3:",e.toString());
        }catch(ExecutionException e){
            Log.i("expn_insrt_invitee4:",e.toString());
        }catch(Exception e){
            Log.i("expn_insrt_invitee5:",e.toString());
        }
        return status;
    }
    public boolean updateInvitee(final Invitee invitee){
        status=false;
        Callable<Boolean> callableInvitee=null;
        callableInvitee=new Callable<Boolean>(){
            @Override
            public Boolean call(){
                boolean bk=false;
                try{
                    if(GlobalClass.getApplication()!=null){
                        db=ClouzerDatabase.getDatabase(GlobalClass.getApplication());
                        if(db!=null){
                            InviteeDao inviteeDao=db.inviteeDao();
                            if(inviteeDao!=null){
                                int counterUpdate=inviteeDao.updateInvitee(invitee);
                                if(counterUpdate==1){
                                    bk=true;
                                }
                            }
                        }
                    }
                }catch(Exception e){
                    Log.i("expn_update_invitee1:",e.toString());
                }
                return bk;
            }
        };
        try{
            ExecutorService service= Executors.newSingleThreadExecutor();
            Future<Boolean> future=service.submit(callableInvitee);
            status=future.get();
        }catch(InterruptedException e){
            Log.i("expn_update_invitee2:",e.toString());
        }catch(ExecutionException e){
            Log.i("expn_update_invitee3:",e.toString());
        }catch(Exception e){
            Log.i("expn_update_invitee4:",e.toString());
        }
        return status;
    }
    public boolean insertMessages(final ArrayList<Messages> msgList){
        boolean msgStatus=false;
        Callable<Boolean> callableMessages=null;
        callableMessages=new Callable<Boolean>(){
            @Override
            public Boolean call(){
                boolean bk=false;
                Application application=GlobalClass.getApplication();
                ClouzerDatabase dbK=ClouzerDatabase.getDatabase(application);
                try{
                    if(dbK!=null){
                        MessagesDao messagesDao=dbK.messagesDao();
                        if(messagesDao!=null){
                            ArrayList<Long> longs=(ArrayList<Long>)messagesDao.insertMessages(msgList);
                            if(longs!=null){
                                if(!longs.contains(-1)){
                                    bk=true;
                                }
                            }
                        }
                    }
                }catch(Exception e){
                    Log.i("expn_sync_msgs1:",e.toString());
                }
                return bk;
            }
        };
        try{
            ExecutorService service= Executors.newSingleThreadExecutor();
            Future<Boolean> future=service.submit(callableMessages);
            msgStatus=future.get();
        }catch(InterruptedException e){
            Log.i("expn_sync_msgs2:",e.toString());
        }catch(ExecutionException e){
            Log.i("expn_sync_msgs3:",e.toString());
        }catch(Exception e){
            Log.i("expn_sync_msgs4:",e.toString());
        }
        return msgStatus;
    }
    public boolean updateOrInsertMessage(final JSONObject object){
        boolean msgsStatus=false;
        Callable<Boolean> callableMessages=null;
        callableMessages=new Callable<Boolean>(){
            @Override
            public Boolean call(){
                boolean bk=false;
                Application application=GlobalClass.getApplication();
                try{
                    if(application!=null){
                        ClouzerDatabase dbK=ClouzerDatabase.getDatabase(application);
                        if(dbK!=null){
                            MessagesDao messagesDao=dbK.messagesDao();
                            if(messagesDao!=null){
                                JSONArray dataArr=object.getJSONArray(ConstantsObjects.DATA_ARRAY);
                                String keytype,subkeytype,userId;
                                keytype=subkeytype=userId="";
                                userId=GlobalClass.getUserId();
                                ArrayList<Messages> messageList=new ArrayList<Messages>();
                                if(dataArr.length()>0){
                                    for(int i=0;i<dataArr.length();i++){
                                        keytype=subkeytype="";
                                        JSONObject innerObj=dataArr.getJSONObject(i);
                                        if(innerObj.has(ConstantsObjects.KEY_TYPE_INNER) &&
                                                innerObj.has(ConstantsObjects.SUB_KEY_TYPE_INNER)){
                                            keytype=innerObj.getString(ConstantsObjects.KEY_TYPE_INNER);
                                            subkeytype=innerObj.getString(ConstantsObjects.SUB_KEY_TYPE_INNER);
                                            if(keytype.equals(ConstantsObjects.KT_TSK)
                                                    && (subkeytype.equals(ConstantsObjects.SUB_KT_MESSAGE)
                                                    || subkeytype.equals("TSK_CONV_ATH"))){
                                                Messages messages=JsonParserClass.parseMessagesJSONObject(innerObj);
                                                messages.setPendingStatus(1);
                                                String messageSender=messages.getCreatedBy();
                                                if(userId!=null && !TextUtils.isEmpty(userId) && messageSender!=null
                                                        && !TextUtils.isEmpty(messageSender)){
                                                    if(messageSender.equals(userId) && (messages.getMesssageType()==1)){
                                                        messages.setViewType(2);
                                                    }
                                                    if(!messageSender.equals(userId) && (messages.getMesssageType()==1)){
                                                        messages.setViewType(1);
                                                    }
                                                    if(messages.getMesssageType()==5){
                                                        messages.setViewType(3);
                                                    }
                                                    messageList.add(messages);
                                                }
                                            }
                                        }
                                    }
                                    ArrayList<Long> longs=(ArrayList<Long>)messagesDao.insertMessages(messageList);
                                    if(longs!=null){
                                        if(longs.size()>0 && !longs.contains(-1)){
                                            bk=true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }catch(JSONException e){
                    Log.i("insrt_msg1:",e.toString());
                }catch(Exception e){
                    Log.i("insrt_msg2:",e.toString());
                }
                return bk;
            }
        };
        try{
            ExecutorService service= Executors.newSingleThreadExecutor();
            Future<Boolean> future=service.submit(callableMessages);
            msgsStatus=future.get();
        }catch(InterruptedException e){
            Log.i("insrt_msg3:",e.toString());
        }catch(ExecutionException e){
            Log.i("insrt_msg4:",e.toString());
        }catch(Exception e){
            Log.i("insrt_msg5:",e.toString());
        }
        return msgsStatus;
    }
    public void insertImagePath(final String keyVal, final String imagePath){
        Callable<Boolean> callableImagePath=null;
        callableImagePath=new Callable<Boolean>(){
            @Override
            public Boolean call(){
                if(GlobalClass.getApplication()!=null){
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(GlobalClass.getApplication());
                    if(db!=null){
                        MessagesDao messagesDao=db.messagesDao();
                        if(messagesDao!=null){
                            messagesDao.insertLocalImagePath(keyVal,imagePath);
                        }
                    }
                }
                return true;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<Boolean> future=service.submit(callableImagePath);
            future.get();
        }catch(InterruptedException e){
            Log.i("insrt_imgPath1:",e.toString());
        }catch(ExecutionException e){
            Log.i("insrt_imgPath2:",e.toString());
        }catch(Exception e){
            Log.i("insrt_imgPath3:",e.toString());
        }
    }
    public boolean insertConversationData(final ArrayList<Conversation> conversations, final String operationMode){
        status=false;
        Callable<Boolean> callableConversation=null;
        callableConversation=new Callable<Boolean>(){
            @Override
            public Boolean call(){
                boolean bk=false;
                if(GlobalClass.getApplication()!=null){
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(GlobalClass.getApplication());
                    if(db!=null){
                        ConversationDao conversationDao=db.conversationDao();
                        if(conversationDao!=null && operationMode!=null){
                            if(operationMode.equals("insert")){
                                ArrayList<Long> longs=(ArrayList<Long>)conversationDao.insertConversations(conversations);
                                if(longs!=null){
                                    if(!longs.contains(-1)){
                                        bk=true;
                                    }
                                }
                            }
                            if(operationMode.equals("update")){
                                Conversation singleConversation=conversations.get(0);
                                if(singleConversation!=null){
                                    int statusUpdate=conversationDao.updateConversation(singleConversation);
                                    if(statusUpdate==1){
                                        bk=true;
                                    }
                                }
                            }
                        }
                    }
                }
                return bk;
            }
        };
        try{
            ExecutorService service= Executors.newSingleThreadExecutor();
            Future<Boolean> future=service.submit(callableConversation);
            status=future.get();
        }catch(InterruptedException e){
            Log.i("insrt_conversation1:",e.toString());
        }catch(ExecutionException e){
            Log.i("insrt_conversation2:",e.toString());
        }catch(Exception e){
            Log.i("insrt_conversation3:",e.toString());
        }
        return status;
    }
    public boolean insertEventList(final ArrayList<Event> events){
        status=false;
        Callable<Boolean> callableEvents=null;
        callableEvents=new Callable<Boolean>(){
            @Override
            public Boolean call(){
                boolean bk=false;
                Application application=GlobalClass.getApplication();
                ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                if(db!=null){
                    EventDao eventDao=db.eventDao();
                    if(eventDao!=null){
                        ArrayList<Long> longs=(ArrayList<Long>)eventDao.insertAllEvents(events);
                        if(longs!=null){
                            if(!longs.contains(-1)){
                                bk=true;
                            }
                        }
                    }
                }
                return bk;
            }
        };
        try{
            ExecutorService service= Executors.newSingleThreadExecutor();
            Future<Boolean> future=service.submit(callableEvents);
            status=future.get();
        }catch(InterruptedException e){
            Log.i("insrt_events1:",e.toString());
        }catch(ExecutionException e){
            Log.i("insrt_events2:",e.toString());
        }catch(Exception e){
            Log.i("insrt_events3:",e.toString());
        }
        return status;
    }
    public boolean insertRoleTaskData(final ArrayList<RoleTask> roleTaskArrayList){
        boolean status=false;
        Callable<Boolean> callableRoleTask=null;
        callableRoleTask=new Callable<Boolean>(){
            @Override
            public Boolean call(){
                boolean bk=false;
                if(GlobalClass.getApplication()!=null){
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(GlobalClass.getApplication());
                    if(db!=null){
                        RoleTaskDao roleTaskDao=db.roleTaskDao();
                        if(roleTaskDao!=null){
                            ArrayList<Long> longArrayList=(ArrayList<Long>)roleTaskDao.insertAllRoleTask(roleTaskArrayList);
                            if(longArrayList!=null){
                                if(longArrayList.size()>0 && !longArrayList.contains(-1)){
                                    bk=true;
                                }
                            }
                        }
                    }
                }
                return bk;
            }
        };
        try{
            ExecutorService service= Executors.newSingleThreadExecutor();
            Future<Boolean> future=service.submit(callableRoleTask);
            status=future.get();
        }catch(InterruptedException e){
            Log.i("insrt_role_task1:",e.toString());
        }catch(ExecutionException e){
            Log.i("insrt_role_task2:",e.toString());
        }catch(Exception e){
            Log.i("insrt_role_task3:",e.toString());
        }
        return status;
    }
    public boolean insertUserURMData(final ArrayList<UserURM> userURMS){
        boolean status=false;
        Callable<Boolean> callableLogin=null;
        callableLogin=new Callable<Boolean>(){
            @Override
            public Boolean call(){
                boolean bk=false;
                if(GlobalClass.getApplication()!=null){
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(GlobalClass.getApplication());
                    if(db!=null){
                        UserURMDao userURMDao=db.userURMDao();
                        if(userURMDao!=null){
                            ArrayList<Long> longArrayList=(ArrayList<Long>)userURMDao.insertAllUserURMS(userURMS);
                            if(longArrayList!=null){
                                if(longArrayList.size()>0 && !longArrayList.contains(-1)){
                                    bk=true;
                                }
                            }
                        }
                    }
                }
                return bk;
            }
        };
        try{
            ExecutorService service= Executors.newSingleThreadExecutor();
            Future<Boolean> future=service.submit(callableLogin);
            status=future.get();
        }catch(InterruptedException e){
            Log.i("insrt_urm1:",e.toString());
        }catch(ExecutionException e){
            Log.i("insrt_urm2:",e.toString());
        }catch(Exception e){
            Log.i("insrt_urm3:",e.toString());
        }
        return status;
    }
    public boolean insertLoginData(final Login login){
        status=false;
        Callable<Boolean> callableLogin=null;
        callableLogin=new Callable<Boolean>(){
            @Override
            public Boolean call(){
                boolean bk=false;
                if(GlobalClass.getApplication()!=null){
                    ClouzerDatabase db= ClouzerDatabase.getDatabase(GlobalClass.getApplication());
                    LoginDao loginDao=db.loginDao();
                    int loginUpdate=loginDao.updateLogin(login);
                    if(loginUpdate!=1){
                        long resultInsert=loginDao.insertLogin(login);
                        if(resultInsert!=-1){
                            bk=true;
                        }
                    }else{
                        if(loginUpdate==1){
                            bk=true;
                        }
                    }
                }
                return bk;
            }
        };
        try{
            ExecutorService service= Executors.newSingleThreadExecutor();
            Future<Boolean> future=service.submit(callableLogin);
            status=future.get();
        }catch(InterruptedException e){
            Log.i("insrt_login1:",e.toString());
        }catch(ExecutionException e){
            Log.i("insrt_login2:",e.toString());
        }catch(Exception e){
            Log.i("insrt_login3:",e.toString());
        }
        return status;
    }
    public boolean updateUserProfileImage(final JSONObject updateUserObj){
        status=false;
        Callable<Boolean> callableProfileUpdate=null;
        callableProfileUpdate=new Callable<Boolean>(){
            @Override
            public Boolean call(){
                boolean bk=false;
                String path="";
                if(GlobalClass.getApplication()!=null){
                    ClouzerDatabase db= ClouzerDatabase.getDatabase(GlobalClass.getApplication());
                    if(db!=null){
                        LoginDao loginDao=db.loginDao();
                        if(loginDao!=null){
                            if(updateUserObj!=null){
                                if(updateUserObj.has(ConstantsObjects.DATA_ARRAY)){
                                    try{
                                        JSONArray dataArr=updateUserObj.getJSONArray(ConstantsObjects.DATA_ARRAY);
                                        if(dataArr!=null){
                                            if(dataArr.length()>0){
                                                JSONObject innerObj=dataArr.getJSONObject(0);
                                                if(innerObj!=null){
                                                    if(innerObj.has("UAD_USER_IMAGE")){
                                                        JSONArray imgArr=innerObj.getJSONArray("UAD_USER_IMAGE");
                                                        if(imgArr!=null){
                                                            path=imgArr.getString(0);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }catch(JSONException e){
                                        Log.e("updt_profileImg1:",e.toString());
                                    }catch(Exception e){
                                        Log.e("updt_profileImg2:",e.toString());
                                    }
                                }
                            }
                            loginDao.updateProfileImagePath(path);
                            bk=true;
                        }
                    }
                }
                return bk;
            }
        };
        try{
            ExecutorService service= Executors.newSingleThreadExecutor();
            Future<Boolean> future=service.submit(callableProfileUpdate);
            status=future.get();
        }catch(InterruptedException e){
            Log.i("updt_profileImg3:",e.toString());
        }catch(ExecutionException e){
            Log.i("updt_profileImg4:",e.toString());
        }catch(Exception e){
            Log.i("updt_profileImg5:",e.toString());
        }
        return status;
    }
    public boolean insertContacts(final JSONObject object){
        status=false;
        Callable<Boolean> callableContacts=null;
        callableContacts=new Callable<Boolean>(){
            @Override
            public Boolean call(){
                boolean kk=false;
                JSONArray dataArr=null;
                try{
                    dataArr=object.getJSONArray(ConstantsObjects.DATA_ARRAY);
                    Application application=GlobalClass.getApplication();
                    if(dataArr!=null && application!=null){
                        if(dataArr.length()>0){
                            ClouzerDatabase db=ClouzerDatabase.getDatabase(GlobalClass.getApplication());
                            if(db!=null){
                                ContactDao contactDao=db.contactDao();
                                if(contactDao!=null){
                                    ArrayList<Contact> contactList=new ArrayList<>();
                                    for(int k=0;k<dataArr.length();k++){
                                        JSONObject innerObj=dataArr.getJSONObject(k);
                                        if(innerObj!=null){
                                            Contact contact=JsonParserClass.parseContactJSONObject(innerObj);
                                            contactList.add(contact);
                                        }
                                    }
                                    ArrayList<Long> longs=(ArrayList<Long>)contactDao.insertContact(contactList);
                                    if(longs!=null){
                                        if(longs.size()>0 && !longs.contains(-1)){
                                            kk=true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }catch(JSONException e){
                    Log.e("expn_inrt_contacts1:",e.toString());
                }catch(Exception e){
                    Log.e("expn_inrt_contacts2:",e.toString());
                }
                return kk;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<Boolean> future=service.submit(callableContacts);
            status=future.get();
        }catch(InterruptedException e){
            Log.e("expn_insrt_contct3:",e.toString());
        }catch(ExecutionException e){
            Log.e("expn_insrt_contct4:",e.toString());
        }catch(Exception e){
            Log.e("expn_insrt_contct5:",e.toString());
        }
        return status;
    }
    public boolean removeContact(final JSONObject object){
        boolean status=false;
        Callable<Boolean> callableBoolean=null;
        callableBoolean=new Callable<Boolean>(){
            @Override
            public Boolean call(){
                boolean kk=false;
                if(object!=null && object.has("dataArray")){
                    try{
                        JSONArray dataArr=object.getJSONArray("dataArray");
                        if(dataArr!=null && dataArr.length()>0 && GlobalClass.getApplication()!=null){
                            Application application=GlobalClass.getApplication();
                            ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                            ContactDao contactDao=db.contactDao();
                            int statusDelete=0;
                            for(int t=0;t<dataArr.length();t++){
                                JSONObject innerObj=dataArr.getJSONObject(t);
                                if(innerObj.has("ACTION_ARRAY")){
                                    JSONArray actionArr=innerObj.getJSONArray("ACTION_ARRAY");
                                    if(actionArr.getString(0).equals("DELETE_CONTACT")){
                                        Contact cntct=JsonParserClass.parseContactJSONObject(innerObj);
                                        statusDelete=contactDao.deleteContact(cntct);
                                        Log.i("status_cntct_delete:",String.valueOf(statusDelete));
                                        if(statusDelete==1){
                                            kk=true;
                                        }
                                    }
                                }
                            }
                        }
                    }catch(JSONException e){
                        Log.e("expn_rm_cntct1:",e.toString());
                    }catch(Exception e){
                        Log.e("expn_rm_cntct2:",e.toString());
                    }
                }
                return kk;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<Boolean> future=service.submit(callableBoolean);
            status=future.get();
        }catch(InterruptedException e){
            Log.e("expn_rm_cntct3:",e.toString());
        }catch(ExecutionException e){
            Log.e("expn_rm_cntct4:",e.toString());
        }catch(Exception e){
            Log.e("expn_rm_cntct5:",e.toString());
        }
        return status;
    }

    public Login getLoginData(){
        Login login=null;
        Callable<Login> callableLogin=null;
        callableLogin=new Callable<Login>(){
            @Override
            public Login call(){
                Login lgn=null;
                if(GlobalClass.getApplication()!=null){
                    Application application=GlobalClass.getApplication();
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                    if(db!=null){
                        LoginDao loginDao=db.loginDao();
                        if(loginDao!=null){
                            lgn=loginDao.getLoginData();
                        }
                    }
                }
                return lgn;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<Login> future=service.submit(callableLogin);
            login=future.get();
        }catch(InterruptedException e){
            Log.e("expn_get_login_data1:",e.toString());
        }catch(ExecutionException e){
            Log.e("expn_get_login_data2:",e.toString());
        }catch(Exception e){
            Log.e("expn_get_login_data3:",e.toString());
        }
        return login;
    }
    public List<Conversation> getAllConversations(final String type){
        List<Conversation> conversations=null;
        Callable<List<Conversation>> callableConversations=null;
        callableConversations=new Callable<List<Conversation>>(){
            @Override
            public List<Conversation> call(){
                List<Conversation> convsns=null;
                if(GlobalClass.getApplication()!=null){
                    Application application=GlobalClass.getApplication();
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                    if(db!=null){
                        ConversationDao conversationDao=db.conversationDao();
                        if(conversationDao!=null){
                            if(type!=null){
                                if(type.equals("archive")){
                                    convsns=conversationDao.getAllArchiveConversations();
                                }
                                if(type.equals("unarchive")){
                                    convsns=conversationDao.getAllConversations();
                                }
                            }
                        }
                    }
                }
                return convsns;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<List<Conversation>> future=service.submit(callableConversations);
            conversations=future.get();
        }catch(InterruptedException e){
            Log.e("expn_get_convsn_data1:",e.toString());
        }catch(ExecutionException e){
            Log.e("expn_get_convsn_data2:",e.toString());
        }catch(Exception e){
            Log.e("expn_get_convsn_data3:",e.toString());
        }
        return conversations;
    }

    public List<Event> getEventList(){
        List<Event> eventList=null;
        Callable<List<Event>> callableEvents=null;
        callableEvents=new Callable<List<Event>>(){
            @Override
            public List<Event> call(){
                List<Event> events=null;
                Application application=GlobalClass.getApplication();
                ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                if(db!=null){
                    EventDao eventDao=db.eventDao();
                    if(eventDao!=null){
                        events=eventDao.getEventList();
                    }
                }
                return events;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<List<Event>> future=service.submit(callableEvents);
            eventList=future.get();
        }catch(InterruptedException e){
            Log.e(TAG+"_event_data1:",e.toString());
        }catch(ExecutionException e){
            Log.e(TAG+"_event_data2:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_event_data3:",e.toString());
        }
        return eventList;
    }

    public List<Event> getEventRequestList(){
        List<Event> eventRequestList=null;
        Callable<List<Event>> callableEventRequest=null;
        callableEventRequest=new Callable<List<Event>>(){
            @Override
            public List<Event> call(){
                List<Event> eventRequestLst=null;
                Application application=GlobalClass.getApplication();
                ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                if(db!=null){
                    EventDao eventDao=db.eventDao();
                    if(eventDao!=null){
                        eventRequestLst=eventDao.getEventRequestList();
                    }
                }
                return eventRequestLst;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<List<Event>> future=service.submit(callableEventRequest);
            eventRequestList=future.get();
        }catch(InterruptedException e){
            Log.e(TAG+"_event_rqst1:",e.toString());
        }catch(ExecutionException e){
            Log.e(TAG+"_event_rqst2:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_event_rqst3:",e.toString());
        }
        return eventRequestList;
    }

    public boolean deleteEventRequest(final Event event){
        boolean isEventRequestDeleted=false;
        Callable<Boolean> callableEventRequestStatus=null;
        callableEventRequestStatus=new Callable<Boolean>(){
            @Override
            public Boolean call(){
                boolean eventRequestDeleteStatus=false;
                Application application=GlobalClass.getApplication();
                ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                if(db!=null){
                    EventDao eventDao=db.eventDao();
                    if(eventDao!=null && event!=null){
                        String eventOwnerId="";
                        eventOwnerId=event.getOwnerId();
                        int deleteCount=eventDao.deleteEventRequest(eventOwnerId);
                        if(deleteCount==1){
                            eventRequestDeleteStatus=true;
                        }
                    }
                }
                return eventRequestDeleteStatus;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<Boolean> future=service.submit(callableEventRequestStatus);
            isEventRequestDeleted=future.get();
        }catch(InterruptedException e){
            Log.e(TAG+"_evtRqstDlt1::",e.toString());
        }catch(ExecutionException e){
            Log.e(TAG+"_evtRqstDlt2::",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_evtRqstDlt3::",e.toString());
        }
        return isEventRequestDeleted;
    }

    private String conversation_ownerId="";
    public List<Event> getConversationEvents(Conversation conversation){
        List<Event> eventList=null;
        conversation_ownerId="";
        if(conversation!=null){
            conversation_ownerId=conversation.getOwnerId();
        }
        Callable<List<Event>> callableEvents=null;
        callableEvents=new Callable<List<Event>>(){
            @Override
            public List<Event> call(){
                List<Event> events=null;
                Application application=GlobalClass.getApplication();
                ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                if(db!=null){
                    EventDao eventDao=db.eventDao();
                    if(eventDao!=null){
                        events=eventDao.getEventsFromConversation(conversation_ownerId);
                    }
                }
                return events;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<List<Event>> future=service.submit(callableEvents);
            eventList=future.get();
        }catch(InterruptedException e){
            Log.e(TAG+"_convEvent_data1:",e.toString());
        }catch(ExecutionException e){
            Log.e(TAG+"_convEvent_data2:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_convEvent_data3:",e.toString());
        }
        return eventList;
    }

    public Calendar getCalendar(final String personaProjectId){
        Calendar calendar=null;
        Callable<Calendar> callableCalendar=null;
        callableCalendar=new Callable<Calendar>(){
            @Override
            public Calendar call(){
                Calendar calendarObj=null;
                Application application=GlobalClass.getApplication();
                ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                if(db!=null){
                    CalendarDao calendarDao=db.calendarDao();
                    if(calendarDao!=null){
                        calendarObj=calendarDao.getCalendar(personaProjectId);
                    }
                }
                return calendarObj;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<Calendar> future=service.submit(callableCalendar);
            calendar=future.get();
        }catch(InterruptedException e){
            Log.e(TAG+"_expn_get_calendar1:",e.toString());
        }catch(ExecutionException e){
            Log.e(TAG+"_expn_get_calendar2:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_expn_get_calendar3:",e.toString());
        }
        return calendar;
    }

    public List<Contact> getAllContacts(){
        List<Contact> contactList=null;
        Callable<List<Contact>> callableContactList=null;
        callableContactList=new Callable<List<Contact>>(){
            @Override
            public List<Contact> call(){
                List<Contact> cntctLst=null;
                if(GlobalClass.getApplication()!=null){
                    Application application=GlobalClass.getApplication();
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                    if(db!=null){
                        ContactDao contactDao=db.contactDao();
                        if(contactDao!=null){
                            cntctLst=contactDao.getAllContacts();
                        }
                    }
                }
                return cntctLst;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<List<Contact>> future=service.submit(callableContactList);
            contactList=future.get();
        }catch(InterruptedException e){
            Log.e("expn_get_cntctLst1:",e.toString());
        }catch(ExecutionException e){
            Log.e("expn_get_cntctLst2:",e.toString());
        }catch(Exception e){
            Log.e("expn_get_cntctLst3:",e.toString());
        }
        return contactList;
    }
    public List<Contact> getBlockedContacts(){
        List<Contact> contactList=null;
        Callable<List<Contact>> callableContactList=null;
        callableContactList=new Callable<List<Contact>>(){
            @Override
            public List<Contact> call(){
                List<Contact> cntctLst=null;
                Application application=GlobalClass.getApplication();
                if(application!=null){
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                    if(db!=null){
                        ContactDao contactDao=db.contactDao();
                        if(contactDao!=null){
                            cntctLst=contactDao.getBlockedContactList();
                        }
                    }
                }
                return cntctLst;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<List<Contact>> future=service.submit(callableContactList);
            contactList=future.get();
        }catch(InterruptedException e){
            Log.e("expn_get_blk_cntctLst1:",e.toString());
        }catch(ExecutionException e){
            Log.e("expn_get_blk_cntctLst2:",e.toString());
        }catch(Exception e){
            Log.e("expn_get_blk_cntctLst3:",e.toString());
        }
        return contactList;
    }
    /*==============================================================================================*/
    public ArrayList<String> getUserUrmProjectIdList(){
        ArrayList<String> userUrmList=null;
        Callable<List<String>> callableUserUrm=null;
        callableUserUrm=new Callable<List<String>>(){
            @Override
            public List<String> call(){
                ArrayList<String> userUrmLst=new ArrayList<String>();
                if(GlobalClass.getApplication()!=null){
                    Application application=GlobalClass.getApplication();
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                    if(db!=null){
                        RoleTaskDao roleTaskDao=db.roleTaskDao();
                        if(roleTaskDao!=null){
                            ArrayList<String> strings=(ArrayList<String>)roleTaskDao.getPersonaKeyVal();
                            if(strings!=null){
                                if(strings.size()>0){
                                    UserURMDao userURMDao=db.userURMDao();
                                    if(userURMDao!=null){
                                        for(int i=0;i<strings.size();i++){
                                            String projectId=userURMDao.getPersonaProjectId(strings.get(i));
                                            userUrmLst.add(projectId);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return userUrmLst;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<List<String>> future=service.submit(callableUserUrm);
            userUrmList=(ArrayList<String>)future.get();
        }catch(InterruptedException e){
            Log.e("expn_get_urm1:",e.toString());
        }catch(ExecutionException e){
            Log.e("expn_get_urm2:",e.toString());
        }catch(Exception e){
            Log.e("expn_get_urm3:",e.toString());
        }
        return userUrmList;
    }

    public String getPersonaWiseProjectId(final String personaString){
        String personaWiseProjectId=null;
        Callable<String> callablePersonaWiseProjectId=null;
        callablePersonaWiseProjectId=new Callable<String>(){
            @Override
            public String call(){
                String personalUserUrmProjectId=null;
                if(GlobalClass.getApplication()!=null){
                    Application application=GlobalClass.getApplication();
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                    if(db!=null){
                        RoleTaskDao roleTaskDao=db.roleTaskDao();
                        if(roleTaskDao!=null){
                            String personaKeyVal=roleTaskDao.getRoleTaskKeyval(personaString);
                            UserURMDao userURMDao=db.userURMDao();
                            if(personaKeyVal!=null && userURMDao!=null){
                                personalUserUrmProjectId=userURMDao.getPersonaProjectId(personaKeyVal);
                            }
                        }
                    }
                }
                return personalUserUrmProjectId;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<String> future=service.submit(callablePersonaWiseProjectId);
            personaWiseProjectId=future.get();
        }catch(InterruptedException e){
            Log.e("expn_get_ppId1:",e.toString());
        }catch(ExecutionException e){
            Log.e("expn_get_ppId2:",e.toString());
        }catch(Exception e){
            Log.e("expn_get_ppId3:",e.toString());
        }
        return personaWiseProjectId;
    }

    public List<String> getPersonaList(){
        List<String> personaList=null;
        Callable<List<String>> callablePersonaList=null;
        callablePersonaList=new Callable<List<String>>(){
            @Override
            public List<String> call(){
                List<String> personaLst=null;
                if(GlobalClass.getApplication()!=null){
                    Application application=GlobalClass.getApplication();
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                    if(db!=null){
                        RoleTaskDao roleTaskDao=db.roleTaskDao();
                        if(roleTaskDao!=null){
                            personaLst=roleTaskDao.getPersonaList();
                        }
                    }
                }
                return personaLst;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<List<String>> future=service.submit(callablePersonaList);
            personaList=future.get();
        }catch(InterruptedException e){
            Log.e(TAG+"_expn_get_prsnLst1:",e.toString());
        }catch(ExecutionException e){
            Log.e(TAG+"_expn_get_prsnLst2:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_expn_get_prsnLst3:",e.toString());
        }
        return personaList;
    }

    public List<Messages> getAllMessages(final String conversation_linkupId){
        List<Messages> messageList=null;
        Callable<List<Messages>> callableMessagesList=null;
        callableMessagesList=new Callable<List<Messages>>(){
            @Override
            public List<Messages> call(){
                List<Messages> msgLst=null;
                if(GlobalClass.getApplication()!=null){
                    Application application=GlobalClass.getApplication();
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                    if(db!=null){
                        MessagesDao messagesDao=db.messagesDao();
                        if(messagesDao!=null){
                            msgLst=messagesDao.getAllMessages(conversation_linkupId);
                        }
                    }
                }
                return msgLst;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<List<Messages>> future=service.submit(callableMessagesList);
            messageList=future.get();
        }catch(InterruptedException e){
            Log.e("expn_msgLst1:",e.toString());
        }catch(ExecutionException e){
            Log.e("expn_msgLst2:",e.toString());
        }catch(Exception e){
            Log.e("expn_msgLst3:",e.toString());
        }
        return messageList;
    }

    public List<Messages> getAllStarMessages(final String conversation_linkupId){
        List<Messages> messageList=null;
        Callable<List<Messages>> callableMessagesList=null;
        callableMessagesList=new Callable<List<Messages>>(){
            @Override
            public List<Messages> call(){
                List<Messages> msgLst=null;
                if(GlobalClass.getApplication()!=null){
                    Application application=GlobalClass.getApplication();
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                    if(db!=null){
                        MessagesDao messagesDao=db.messagesDao();
                        if(messagesDao!=null){
                            msgLst=messagesDao.getAllStarMessages(conversation_linkupId);
                        }
                    }
                }
                return msgLst;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<List<Messages>> future=service.submit(callableMessagesList);
            messageList=future.get();
        }catch(InterruptedException e){
            Log.e("expn_*msgLst1:",e.toString());
        }catch(ExecutionException e){
            Log.e("expn_*msgLst2:",e.toString());
        }catch(Exception e){
            Log.e("expn_*msgLst3:",e.toString());
        }
        return messageList;
    }
    public List<Messages> getAllNestedMessages(final String message_keyval){
        List<Messages> messageList=null;
        Callable<List<Messages>> callableMessagesList=null;
        callableMessagesList=new Callable<List<Messages>>(){
            @Override
            public List<Messages> call(){
                List<Messages> msgLst=null;
                Application application=GlobalClass.getApplication();
                if(application!=null){
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                    if(db!=null){
                        MessagesDao messagesDao=db.messagesDao();
                        if(messagesDao!=null){
                            msgLst=messagesDao.getNestedMessages(message_keyval);
                        }
                    }
                }
                return msgLst;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<List<Messages>> future=service.submit(callableMessagesList);
            messageList=future.get();
        }catch(InterruptedException e){
            Log.e("expn_nstdMsgLst1:",e.toString());
        }catch(ExecutionException e){
            Log.e("expn_nstdMsgLst2:",e.toString());
        }catch(Exception e){
            Log.e("expn_nstdMsgLst3:",e.toString());
        }
        return messageList;
    }
    public boolean isThisParentMessage(Messages singleMessages){
        boolean parentMsgStatus=false;
        if(singleMessages!=null){
            String completeData=singleMessages.getCompleteData();
            try{
                JSONObject completeObj=new JSONObject(completeData);
                if(completeObj!=null){
                    if(completeObj.has("CML_IS_PARENT")){
                        int kk=completeObj.getInt("CML_IS_PARENT");
                        if(kk==1){
                            parentMsgStatus=true;
                        }
                    }
                }
            }catch(JSONException e){
                Log.e("parentMsgSts1:",e.toString());
            }catch(Exception e){
                Log.e("parentMsgSts2:",e.toString());
            }
        }
        return parentMsgStatus;
    }
    public List<Messages> getAllMessagesByGroupId(final String conversation_keyval){
        List<Messages> messageList=null;
        Callable<List<Messages>> callableMessagesList=null;
        callableMessagesList=new Callable<List<Messages>>(){
            @Override
            public List<Messages> call(){
                List<Messages> msgLst=null;
                if(GlobalClass.getApplication()!=null){
                    Application application=GlobalClass.getApplication();
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                    if(db!=null){
                        MessagesDao messagesDao=db.messagesDao();
                        if(messagesDao!=null){
                            msgLst=messagesDao.getAllMessagesByKeyval(conversation_keyval);
                        }
                    }
                }
                return msgLst;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<List<Messages>> future=service.submit(callableMessagesList);
            messageList=future.get();
        }catch(InterruptedException e){
            Log.e("expn_msgLst11:",e.toString());
        }catch(ExecutionException e){
            Log.e("expn_msgLst22:",e.toString());
        }catch(Exception e){
            Log.e("expn_msgLst33:",e.toString());
        }
        return messageList;
    }
    public Messages getMessage(final String message_keyval){
        Messages singleMessage=null;
        Callable<Messages> callableMessage=null;
        callableMessage=new Callable<Messages>(){
            @Override
            public Messages call(){
                Messages singleMessage=null;
                if(GlobalClass.getApplication()!=null){
                    Application application=GlobalClass.getApplication();
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                    if(db!=null){
                        MessagesDao messagesDao=db.messagesDao();
                        if(messagesDao!=null){
                            singleMessage=messagesDao.getSingleMessage(message_keyval);
                        }
                    }
                }
                return singleMessage;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<Messages> future=service.submit(callableMessage);
            singleMessage=future.get();
        }catch(InterruptedException e){
            Log.e("expn_msgLst1:",e.toString());
        }catch(ExecutionException e){
            Log.e("expn_msgLst2:",e.toString());
        }catch(Exception e){
            Log.e("expn_msgLst3:",e.toString());
        }
        return singleMessage;
    }
    public List<Invitee> getAllInvitees(final String conversation_ownerId){
        List<Invitee> inviteeList=null;
        Callable<List<Invitee>> callableInviteeList=null;
        callableInviteeList=new Callable<List<Invitee>>(){
            @Override
            public List<Invitee> call(){
                List<Invitee> inviteeLst=null;
                if(GlobalClass.getApplication()!=null){
                    Application application=GlobalClass.getApplication();
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                    if(db!=null){
                        InviteeDao inviteeDao=db.inviteeDao();
                        if(inviteeDao!=null){
                            inviteeLst=inviteeDao.getAllInvitees(conversation_ownerId);
                        }
                    }
                }
                return inviteeLst;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<List<Invitee>> future=service.submit(callableInviteeList);
            inviteeList=future.get();
        }catch(InterruptedException e){
            Log.e("expn_invitee1:",e.toString());
        }catch(ExecutionException e){
            Log.e("expn_invitee2:",e.toString());
        }catch(Exception e){
            Log.e("expn_invitee3:",e.toString());
        }
        return inviteeList;
    }
    public Invitee getConversationInvitee(final String conversation_ownerId,
                                                            final String inviteeEmailAddress){
        Invitee invitee=null;
        Callable<Invitee> callableInvitee=null;
        callableInvitee=new Callable<Invitee>(){
            @Override
            public Invitee call(){
                Invitee invitee=null;
                if(GlobalClass.getApplication()!=null){
                    Application application=GlobalClass.getApplication();
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                    if(db!=null){
                        InviteeDao inviteeDao=db.inviteeDao();
                        if(inviteeDao!=null && conversation_ownerId!=null && inviteeEmailAddress!=null){
                            invitee=inviteeDao.getInviteeWhoIsGoingToDeleteConversation
                                    (conversation_ownerId,inviteeEmailAddress);
                        }
                    }
                }
                return invitee;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<Invitee> future=service.submit(callableInvitee);
            invitee=future.get();
        }catch(InterruptedException e){
            Log.e("expn_invitee4:",e.toString());
        }catch(ExecutionException e){
            Log.e("expn_invitee5:",e.toString());
        }catch(Exception e){
            Log.e("expn_invitee6:",e.toString());
        }
        return invitee;
    }
    public Contact getContact(final String string){
        Contact contact=null;
        Callable<Contact> callableContact=null;
        callableContact=new Callable<Contact>(){
            @Override
            public Contact call(){
                Contact cntct=null;
                if(GlobalClass.getApplication()!=null){
                    Application application=GlobalClass.getApplication();
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                    if(db!=null){
                        ContactDao contactDao=db.contactDao();
                        if(contactDao!=null){
                            cntct=contactDao.getContact(string);
                        }
                    }
                }
                return cntct;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<Contact> future=service.submit(callableContact);
            contact=future.get();
        }catch(InterruptedException e){
            Log.e("expn_cntct1:",e.toString());
        }catch(ExecutionException e){
            Log.e("expn_cntct2:",e.toString());
        }catch(Exception e){
            Log.e("expn_cntct3:",e.toString());
        }
        return contact;
    }
    public boolean updateMessageList(ArrayList<Messages> msgList){
        boolean editMessageStatus=false;
        try{
            Application application=GlobalClass.getApplication();
            if(msgList!=null && application!=null){
                ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                if(db!=null){
                    MessagesDao messagesDao=db.messagesDao();
                    if(messagesDao!=null){
                        Messages singleMessage=msgList.get(0);
                        String keyval,completeData;
                        keyval=completeData="";
                        int cmlStar=-1;
                        if(singleMessage!=null){
                            keyval=singleMessage.getKeyval();
                            cmlStar=singleMessage.getCmlStar();
                            completeData=singleMessage.getCompleteData();
                            Log.i(TAG+"_msgKeyval:",keyval+" ..kk");
                            Log.i(TAG+"_msgCmlStar:",String.valueOf(cmlStar)+" ..kk");
                        }
                        messagesDao.updateMessage(keyval,cmlStar,completeData);
                        editMessageStatus=true;
                    }
                }
            }
        }catch(Exception e){
            Log.e("expn_edt_msgs:",e.toString());
        }
        return editMessageStatus;
    }
    public boolean deleteMessageList(ArrayList<Messages> messages){
        boolean deleteMessageStatus=false;
        try{
            Application application=GlobalClass.getApplication();
            if(messages!=null && messages.size()>0 && application!=null){
                ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                int deleteMessageCounter=0;
                if(db!=null){
                    MessagesDao messagesDao=db.messagesDao();
                    if(messagesDao!=null){
                        deleteMessageCounter=messagesDao.deleteMessages(messages);
                        if(deleteMessageCounter==messages.size()){
                            deleteMessageStatus=true;
                        }
                    }
                }
            }
        }catch(Exception e){
            Log.e("expn_delete_msgs:",e.toString());
        }
        return deleteMessageStatus;
    }
    public boolean deleteMessage(String keyval){
        boolean msgDltStatus=false;
        try{
            Application application=GlobalClass.getApplication();
            if(application!=null){
                ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                if(db!=null){
                    MessagesDao msgDao=db.messagesDao();
                    if(msgDao!=null && keyval!=null){
                        msgDao.deleteMessage(keyval);
                        msgDltStatus=true;
                    }
                }
            }
        }catch(Exception e){
            Log.e("expn_delete_msgs:",e.toString());
        }
        return msgDltStatus;
    }
    public boolean deleteConversation(Conversation conversation){
        boolean status=false;
        try{
            Application application=GlobalClass.getApplication();
            if(conversation!=null && application!=null){
                ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                int statusDeleteConversation=0;
                if(db!=null){
                    ConversationDao conversationDao=db.conversationDao();
                    if(conversationDao!=null){
                        statusDeleteConversation=conversationDao.deleteConversation(conversation);
                    }
                    if(statusDeleteConversation==1){
                        status=true;
                    }
                }
            }
        }catch(Exception e){
            Log.e("expn_dlt_conversation:",e.toString());
        }
        return status;
    }
    public boolean deleteConversation(String keyval){
        boolean status=false;
        try{
            Application application=GlobalClass.getApplication();
            if(application!=null){
                ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                if(db!=null){
                    ConversationDao conversationDao=db.conversationDao();
                    if(conversationDao!=null && keyval!=null){
                        conversationDao.deleteConversation(keyval);
                        status=true;
                    }
                }
            }
        }catch(Exception e){
            Log.e("expn_dlt_conversation:",e.toString());
        }
        return status;
    }
    public boolean deleteInvitee(ArrayList<Invitee> inviteeLst){
        boolean status=false;
        try{
            Application application=GlobalClass.getApplication();
            if(inviteeLst!=null && application!=null){
                if(inviteeLst.size()>0){
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                    int statusDeleteInvitee=0;
                    if(db!=null){
                        InviteeDao inviteeDao=db.inviteeDao();
                        if(inviteeDao!=null){
                            statusDeleteInvitee=inviteeDao.deleteInvitee(inviteeLst);
                        }
                        if(statusDeleteInvitee==inviteeLst.size()){
                            status=true;
                        }
                    }
                }
            }
        }catch(Exception e){
            Log.e("expn_delete_invitee:",e.toString());
        }
        return status;
    }
    public boolean deleteSpecificConversationInvitees(String conversationOwnerId){
        boolean status=false;
        try{
            Application application=GlobalClass.getApplication();
            if(conversationOwnerId!=null && application!=null){
                ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                int statusDeleteInvitee=0;
                if(db!=null){
                    InviteeDao inviteeDao=db.inviteeDao();
                    if(inviteeDao!=null){
                        statusDeleteInvitee=inviteeDao
                                .deleteSpecificConversationInvitees(conversationOwnerId);
                    }
                    if(statusDeleteInvitee>0){
                        status=true;
                    }
                }
            }
        }catch(Exception e){
            Log.e("expn_dlt_conv_invitee:",e.toString());
        }
        return status;
    }

    public boolean emitRequestCall(final JSONObject object,final String emitAction){
        boolean statuskk=false;
        boolean internetStatus= CheckInternetConnectionCommunicator.isInternetAvailable();
        Log.i(TAG+"_internetStatus:",String.valueOf(internetStatus)+" ..kk");
        boolean attchmntMsg=isAttachmentMessage(object);
        String attchmnt="";
        if(attchmntMsg){
            attchmnt="a_yes";
        }else{
            attchmnt="a_no";
        }
        Log.i(TAG+"_attchmntMsgStatus:",String.valueOf(attchmntMsg)+" ..kk");
        Log.i(TAG+"_attchmnt_string**:",attchmnt+" ..kk");
        if(internetStatus){
            if(attchmntMsg){
                GlobalClass.setConversationSubcriberr(null);
                setFileUploadSubcriber(object);
                ArrayList<String> filePathLst=getFilePathList(object);
                if(filePathLst!=null){
                    if(filePathLst.size()>0){
                        ImageClass imageClass=ImageClass.getImageClassInstance();
                        if(imageClass!=null){
                            imageClass.uploadFile(filePathLst);
                        }
                    }
                }
            }
            if(GlobalClass.getAuthenticatedSyncSocket()!=null && attchmnt.equals("a_no")){
                GlobalClass.getAuthenticatedSyncSocket()
                        .emit(emitAction,object);
            }
        }else{
            Callable<Boolean> callableOfflineSync=null;
            callableOfflineSync=new Callable<Boolean>(){
                @Override
                public Boolean call(){
                    boolean bk=false;
                    try{
                        Application application=GlobalClass.getApplication();
                        ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                        if(db!=null){
                            OfflineSyncDao offlineSyncDao=db.offlineSyncDao();
                            if(offlineSyncDao!=null){
                                JSONArray dataArr=object.getJSONArray(ConstantsObjects.DATA_ARRAY);
                                JSONObject innerObj=dataArr.getJSONObject(0);
                                if(innerObj!=null){
                                    if(innerObj.has(ConstantsObjects.CALMAIL_OBJECT)){
                                        JSONObject calmailObj=innerObj.getJSONObject(ConstantsObjects.CALMAIL_OBJECT);
                                        if(calmailObj.has(ConstantsObjects.KEY_TYPE_INNER) &&
                                                calmailObj.has(ConstantsObjects.SUB_KEY_TYPE_INNER)){
                                            String kt=calmailObj.getString(ConstantsObjects.KEY_TYPE_INNER);
                                            String skt=calmailObj.getString(ConstantsObjects.SUB_KEY_TYPE_INNER);
                                            if(kt.equals(ConstantsObjects.KT_TSK) &&
                                                    (skt.equals(ConstantsObjects.SUB_KT_MESSAGE)
                                                            || skt.equals(ConstantsObjects.SUB_KT_SEND_MESSAGE_ATTACHMENT))){
                                                boolean var=storeOfflineMessage(object);
                                                Log.i(TAG+"_storeOfflnMsg:",String.valueOf(var)+" ..kk");
                                            }
                                        }
                                    }
                                }
                                OfflineSync offlineSync=new OfflineSync();
                                offlineSync.setEmitString(emitAction);
                                offlineSync.setObjectString(String.valueOf(object));
                                offlineSync.setCompleteObject(String.valueOf(innerObj));
                                Long aLong=offlineSyncDao.insertObjectToEmit(offlineSync);
                                if (aLong!=-1){
                                    bk=true;
                                }
                            }
                        }
                    }catch(Exception e){
                        Log.i("expn_innr_offlineSync1:",e.toString());
                    }
                    return bk;
                }
            };
            try{
                ExecutorService service=Executors.newSingleThreadExecutor();
                Future<Boolean> future=service.submit(callableOfflineSync);
                statuskk=future.get();
            }catch(InterruptedException e){
                Log.i("expn_offlineSync1:",e.toString());
            }catch(ExecutionException e){
                Log.i("expn_offlineSync2:",e.toString());
            }catch(Exception e){
                Log.i("expn_offlineSync3:",e.toString());
            }
        }
        return statuskk;
    }

    private boolean isAttachmentMessage(JSONObject obb){
        boolean bb=false;
        try{
            if(obb!=null){
                if(obb.has(ConstantsObjects.DATA_ARRAY)){
                    JSONArray dataArr=obb.getJSONArray(ConstantsObjects.DATA_ARRAY);
                    JSONObject innerObj=dataArr.getJSONObject(0);
                    JSONObject calmailObj=innerObj.getJSONObject(ConstantsObjects.CALMAIL_OBJECT);
                    String skt=calmailObj.getString(ConstantsObjects.SUB_KEY_TYPE_INNER);
                    if(skt.equals(ConstantsObjects.SUB_KT_SEND_MESSAGE_ATTACHMENT)){
                        bb=true;
                    }
                }
            }
        }catch(JSONException e){
            Log.e(TAG+"_attchmtId1:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_attchmtId2:",e.toString());
        }
        return bb;
    }

    private ArrayList<String> getFilePathList(JSONObject object){
        ArrayList<String> pathList=new ArrayList<>();
        try{
            JSONArray dataArr=object.getJSONArray(ConstantsObjects.DATA_ARRAY);
            for(int i=0;i<dataArr.length();i++){
                JSONObject innerObj=dataArr.getJSONObject(i);
                JSONObject calmailObj=innerObj.getJSONObject(ConstantsObjects.CALMAIL_OBJECT);
                String imagePath=calmailObj.getString(ConstantsObjects.CML_IMAGE_PATH);
                pathList.add(imagePath);
            }
        }catch(JSONException e){
            Log.e(TAG+"_filePathLst1:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_filePathLst2:",e.toString());
        }
        return pathList;
    }
    private void setFileUploadSubcriber(final JSONObject object){
        Observable<String> mObservable=null;
        Observer<String> mObserver=null;
        mObservable=Observable.create(new Observable.OnSubscribe<String>(){
            @Override
            public void call(Subscriber<? super String> subscriber){
                subscriber.onNext("");
                subscriber.onCompleted();
            }
        });
        mObserver=new Observer<String>(){
            @Override
            public void onCompleted(){
                Log.i(TAG+"_onCompl:","onComplete..thanx:)");
            }

            @Override
            public void onError(Throwable e){

            }

            @Override
            public void onNext(String string){
                Log.i(TAG+"_onNxt:",string+" ..kk");
                if(string!=null){
                    if(string.contains("upload_done")){
                        ArrayList<String> serverPathList=null;
                        serverPathList=GlobalClass.getServerPathList();
                        for(int t=0;t<serverPathList.size();t++){
                            String serverPathhh=serverPathList.get(t);
                            Log.i(TAG+"_serverPathList:",serverPathhh+" ..kk");
                        }
                        JSONObject obbBobo=null;
                        try{
                            obbBobo=object;
                            Log.i(TAG+"_aaaaaaaeeeee:",String.valueOf(obbBobo)+" ..kk");
                            JSONArray dataArr=obbBobo.getJSONArray(ConstantsObjects.DATA_ARRAY);
                            if(serverPathList!=null){
                                if(dataArr.length()==serverPathList.size()){
                                    for(int i=0;i<dataArr.length();i++){
                                        JSONObject innerObb=dataArr.getJSONObject(i);
                                        JSONObject calmailObj=innerObb.getJSONObject(ConstantsObjects.CALMAIL_OBJECT);
                                        String cmlTitle=calmailObj.getString(ConstantsObjects.CML_TITLE);
                                        String fileName,filePath;
                                        fileName=filePath="";
                                        for(int j=0;j<serverPathList.size();j++){
                                            fileName=filePath="";
                                            String str=serverPathList.get(j);
                                            if(str!=null){
                                                String strArr[]=str.split("#");
                                                if(strArr!=null){
                                                    if(strArr.length==2){
                                                        fileName=strArr[0];
                                                        filePath=strArr[1];
                                                        if(cmlTitle.equals(fileName)){
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        calmailObj.put(ConstantsObjects.CML_IMAGE_PATH,filePath);
                                    }
                                }
                            }
                            boolean internetStatus= CheckInternetConnectionCommunicator.isInternetAvailable();
                            if(internetStatus && GlobalClass.getAuthenticatedSyncSocket()!=null){
                                Log.i(TAG+"_obbBobo_**:",String.valueOf(obbBobo)+" ..kk");
                                GlobalClass.getAuthenticatedSyncSocket().emit(ConstantsObjects.SERVER_OPERATION,object);
                            }else{
                                boolean var=storeOfflineMessage(obbBobo);
                                Log.i(TAG+"_fileUpldSbcrbr**:",String.valueOf(var)+" ..kk");
                            }
                        }catch(JSONException e){
                            Log.e(TAG+"_fuS1:",e.toString());
                        }catch(Exception e){
                            Log.e(TAG+"_fuS2:",e.toString());
                        }
                    }
                }
            }
        };
        Subcription subcription=new Subcription();
        subcription.setObservable(mObservable);
        subcription.setObserver(mObserver);
        GlobalClass.setFileUploadSubcriberr(subcription);
    }

    private boolean storeOfflineMessage(final JSONObject object){
        boolean offlineMessageStore=false;
        Callable<Boolean> callableOfflineMessageStore=null;
        callableOfflineMessageStore=new Callable<Boolean>(){
            @Override
            public Boolean call(){
                boolean bk=false;
                try{
                    if(object!=null){
                        JSONArray dataArr=object.getJSONArray(ConstantsObjects.DATA_ARRAY);
                        if(dataArr!=null){
                            JSONObject innerObj=dataArr.getJSONObject(0);
                            JSONObject calmailObj=null;
                            if(innerObj!=null){
                                calmailObj=innerObj.getJSONObject(ConstantsObjects.CALMAIL_OBJECT);
                            }
                            if(calmailObj!=null){
                                Messages msg=new Messages();
                                if(calmailObj.has(ConstantsObjects.CML_TITLE)){
                                    msg.setCmlTitle(calmailObj.getString(ConstantsObjects.CML_TITLE));
                                }
                                if(calmailObj.has(ConstantsObjects.CML_MESSAGE_INDEX)){
                                    msg.setCmlMessageIndex(calmailObj.getLong(ConstantsObjects.CML_MESSAGE_INDEX));
                                }
                                if(calmailObj.has(ConstantsObjects.CML_REF_ID)){
                                    msg.setCmlRefId(calmailObj.getString(ConstantsObjects.CML_REF_ID));
                                }
                                if(calmailObj.has(ConstantsObjects.CREATED_BY)){
                                    msg.setCreatedBy(calmailObj.getString(ConstantsObjects.CREATED_BY));
                                }
                                if(calmailObj.has(ConstantsObjects.GROUP_ID)){
                                    msg.setGroupId(calmailObj.getString(ConstantsObjects.GROUP_ID));
                                }
                                if(calmailObj.has(ConstantsObjects.CML_TEMP_KEY_VAL)){
                                    msg.setKeyval(calmailObj.getString(ConstantsObjects.CML_TEMP_KEY_VAL));
                                }
                                msg.setCompleteData(String.valueOf(innerObj));
                                ArrayList<Messages> msgList=new ArrayList<>();
                                msgList.add(msg);
                                Application application=GlobalClass.getApplication();
                                ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                                if(db!=null){
                                    MessagesDao msgDao=db.messagesDao();
                                    if(msgDao!=null){
                                        ArrayList<Long> longs=(ArrayList<Long>)msgDao.insertMessages(msgList);
                                        if(longs!=null){
                                            if(!longs.contains(-1)){
                                                bk=true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }catch(JSONException e){
                    Log.e("expn_offlineMsgStr4:",e.toString());
                }catch(Exception e){
                    Log.e("expn_offlineMsgStr5:",e.toString());
                }
                return bk;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<Boolean> future=service.submit(callableOfflineMessageStore);
            offlineMessageStore=future.get();
        }catch(InterruptedException e){
            Log.e("expn_offlineMsgStr1:",e.toString());
        }catch(ExecutionException e){
            Log.e("expn_offlineMsgStr2:",e.toString());
        }catch(Exception e){
            Log.e("expn_offlineMsgStr3:",e.toString());
        }
        return offlineMessageStore;
    }

    public boolean deleteOfflineMessage(final JSONObject object){
        boolean deleteOfflineMsg=false;
        Callable<Boolean> callableDeleteOfflineMessage=null;
        callableDeleteOfflineMessage=new Callable<Boolean>(){
            @Override
            public Boolean call(){
                boolean bk=false;
                try{
                    if(object!=null){
                        JSONArray dataArr=object.getJSONArray(ConstantsObjects.DATA_ARRAY);
                        if(dataArr!=null){
                            JSONObject innerObj=dataArr.getJSONObject(0);
                            if(innerObj!=null){
                                String cmlTempKeyval="";
                                if(innerObj.has(ConstantsObjects.CML_TEMP_KEY_VAL)){
                                    cmlTempKeyval=innerObj.getString(ConstantsObjects.CML_TEMP_KEY_VAL);
                                }
                                Application application=GlobalClass.getApplication();
                                ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                                if(db!=null){
                                    MessagesDao messagesDao=db.messagesDao();
                                    if(messagesDao!=null){
                                        int deleteStatus=messagesDao.deleteOfflineMessage(cmlTempKeyval);
                                        if(deleteStatus==1){
                                            bk=true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }catch(JSONException e){
                    Log.e("expn_offlineDltMsg4:",e.toString());
                }catch(Exception e){
                    Log.e("expn_offlineDltMsg5:",e.toString());
                }
                return bk;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<Boolean> future=service.submit(callableDeleteOfflineMessage);
            deleteOfflineMsg=future.get();
        }catch(InterruptedException e){
            Log.e("expn_offlineDltMsg1:",e.toString());
        }catch(ExecutionException e){
            Log.e("expn_offlineDltMsg2:",e.toString());
        }catch(Exception e){
            Log.e("expn_offlineDltMsg3:",e.toString());
        }
        return deleteOfflineMsg;
    }

    public List<OfflineSync> getOfflineSyncDataList(){
        List<OfflineSync> offlineSyncList=null;
        Callable<List<OfflineSync>> callableOfflineSyncList=null;
        callableOfflineSyncList=new Callable<List<OfflineSync>>(){
            @Override
            public List<OfflineSync> call(){
                List<OfflineSync> offlineSyncs=null;
                Application application=GlobalClass.getApplication();
                ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                if(db!=null){
                    OfflineSyncDao offlineSyncDao=db.offlineSyncDao();
                    if(offlineSyncDao!=null){
                        offlineSyncs=offlineSyncDao.getAllObjectsToEmit();
                    }
                }
                return offlineSyncs;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<List<OfflineSync>> future=service.submit(callableOfflineSyncList);
            offlineSyncList=future.get();
        }catch(InterruptedException e){
            Log.e("expn_offlineSyncLst1:",e.toString());
        }catch(ExecutionException e){
            Log.e("expn_offlineSyncLst2:",e.toString());
        }catch(Exception e){
            Log.e("expn_offlineSyncLst3:",e.toString());
        }
        return offlineSyncList;
    }
    public void offlineSyncWorker(){
        Repository rr=Repository.getRepository();
        GlobalClass.setOfflineObjectCounter(0);
        ArrayList<OfflineSync> list=(ArrayList<OfflineSync>)rr.getOfflineSyncDataList();
        if(list!=null){
            GlobalClass.setOfflineObjectCounter(list.size());
            Log.i(TAG+"_offlineList:",String.valueOf(list.size())+" ..kk");
        }
        boolean isAttchmntMsg=false;
        for(int i=0;i<list.size();i++){
            OfflineSync offlineSync=list.get(i);
            if(offlineSync!=null){
                String action=offlineSync.getEmitString();
                String object=offlineSync.getObjectString();
                Log.i(TAG+"_actionString:",action+" ..kk");
                Log.i(TAG+"_objectString:",object+" ..kk");
                isAttchmntMsg=false;
                try{
                    JSONObject obb=new JSONObject(object);
                    Log.i(TAG+"_objectToEmit:",String.valueOf(obb)+" ..kk");
                    isAttchmntMsg=isAttachmentMessage(obb);
                    Log.i(TAG+"_isAttchmntMsg:",String.valueOf(isAttchmntMsg)+" ..kk");
                    emitRequestCall(obb,action);
                }catch(JSONException e){
                    Log.e(TAG+"_offWrkr3:",e.toString());
                }catch(Exception e){
                    Log.e(TAG+"_offWrkr4:",e.toString());
                }
            }
            if(isAttchmntMsg){
                try{
                    Thread.sleep(60000);
                }catch(InterruptedException e){
                    Log.e(TAG+"_offWrkr1:",e.toString());
                }catch(Exception e){
                    Log.e(TAG+"_offWrkr2:",e.toString());
                }
            }
        }
    }
    /*================================================================================================*/
    public void clearDatabase(){
        Runnable clearDbRunnable=null;
        Thread clearDbThread=null;
        clearDbRunnable=new Runnable(){
            @Override
            public void run(){
                if(GlobalClass.getApplication()!=null){
                    ClouzerDatabase db=ClouzerDatabase.getDatabase(GlobalClass.getApplication());
                    if(db!=null){
                        db.clearAllTables();
                    }
                }
            }
        };
        clearDbThread=new Thread(clearDbRunnable);
        clearDbThread.start();
    }
    public void closeDatabase(){
        if(GlobalClass.getApplication()!=null){
            ClouzerDatabase db=ClouzerDatabase.getDatabase(GlobalClass.getApplication());
            if(db!=null){
                db.close();
                db=null;
            }
        }
    }
    /*=================================================================================================*/
    public void closeAllSockets(){
        if(com.nc.developers.cloudscommunicator.GlobalClass.getSocketLogin()!=null){
            GlobalClass.getSocketLogin().off();
            GlobalClass.setSocketLogin(null);
        }
        if(com.nc.developers.cloudscommunicator.GlobalClass.getAuthenticatedSyncSocket()!=null){
            GlobalClass.getAuthenticatedSyncSocket().off();
            GlobalClass.setAuthenticatedSyncSocket(null);
        }
        if(com.nc.developers.cloudscommunicator.GlobalClass.getSocketModuleMessaging()!=null){
            GlobalClass.getSocketModuleMessaging().off();
            GlobalClass.setSocketModuleMessaging(null);
        }
        if(com.nc.developers.cloudscommunicator.GlobalClass.getSocketModuleCalendar()!=null){
            GlobalClass.getSocketModuleCalendar().off();
            GlobalClass.setSocketModuleCalendar(null);
        }
        if(com.nc.developers.cloudscommunicator.GlobalClass.getSocketModuleConv()!=null){
            GlobalClass.getSocketModuleConv().off();
            GlobalClass.setSocketModuleConv(null);
        }
    }
    /*==================================================================================================*/
    public boolean handleOfflineData(final ArrayList<OfflineSync> offlineSyncArrayList){
        boolean offlineStatus=false;
        Callable<Boolean> callableOfflineSyncList=null;
        callableOfflineSyncList=new Callable<Boolean>(){
            @Override
            public Boolean call(){
                boolean bk=false;
                Application application=GlobalClass.getApplication();
                ClouzerDatabase db=ClouzerDatabase.getDatabase(application);
                if(db!=null){
                    OfflineSyncDao offlineSyncDao=db.offlineSyncDao();
                    if(offlineSyncDao!=null){
                        int deleteStatus=offlineSyncDao.deleteObjToEmit(offlineSyncArrayList);
                        if(deleteStatus==1){
                            bk=true;
                        }
                    }
                }
                return bk;
            }
        };
        try{
            ExecutorService service=Executors.newSingleThreadExecutor();
            Future<Boolean> future=service.submit(callableOfflineSyncList);
            offlineStatus=future.get();
        }catch(InterruptedException e){
            Log.e(TAG+"_handleOffline1:",e.toString());
        }catch(ExecutionException e){
            Log.e(TAG+"_handleOffline2:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_handleOffline3:",e.toString());
        }
        return offlineStatus;
    }
}