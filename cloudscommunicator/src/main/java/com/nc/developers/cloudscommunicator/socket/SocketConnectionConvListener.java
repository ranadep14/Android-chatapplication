package com.nc.developers.cloudscommunicator.socket;

import android.util.Log;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.models.Conversation;
import com.nc.developers.cloudscommunicator.models.Invitee;
import com.nc.developers.cloudscommunicator.models.Messages;
import com.nc.developers.cloudscommunicator.objects.ConstantsObjects;
import com.nc.developers.cloudscommunicator.sharedprefrences.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SocketConnectionConvListener{
    private int counter=0;
    private static int convSyncCounter=0;
    public void listenConversationSocket(final Socket socket){
        socket.on(Socket.EVENT_CONNECT,new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","conv. socket connected...");
                GlobalClass.setSocketModuleConv(socket);
                bindUI("socket_conv_connect");
            }
        }).on(Socket.EVENT_ERROR,new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","error occurred...");
            }
        }).on(Socket.EVENT_DISCONNECT,new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","conv. socket disconnected...");
            }
        }).on(Socket.EVENT_CONNECT_ERROR,new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","event_connect error occurred...");
            }
        }).on("error",new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("error: ", args[0].toString());
            }
        }).on("serverToClientIDBncSync",new Emitter.Listener() {
            @Override
            public void call(Object... args){
                Log.i("IDBncSync: ", args[0].toString());
            }
        }).on("authenticated",new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","conv. socket authenticated...");
                bindUI("socket_conv_auth_success");
            }
        }).on("serverToClientIDBIncSync",new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("sToClientIDBIncSync ", args[0].toString());
            }
        }).on("conversationSync",new Emitter.Listener(){
            @Override
            public void call(Object... args){
                String stringResponse=(String)args[0];
                Log.i("convSync:",stringResponse);
                PrefManager.setValue("convSync_"+(convSyncCounter),stringResponse+" ..kk");
                convSyncCounter=convSyncCounter+1;
                try{
                    JSONObject obj=new JSONObject(stringResponse);
                    if(obj!=null){
                        if(obj.has("key")){
                            String key=obj.getString("key");
                            if(key.equals("MOB_CON")){
                                if(obj.has(ConstantsObjects.DATA_ARRAY)){
                                    JSONArray dataArr=obj.getJSONArray(ConstantsObjects.DATA_ARRAY);
                                    if(dataArr!=null){
                                        if(dataArr.length()>0){
                                            JSONArray innerArr=dataArr.getJSONArray(0);
                                            if(innerArr!=null){
                                                if(innerArr.length()>0){
                                                    ArrayList<Conversation> conversations=new ArrayList<>();
                                                    for(int i=0;i<innerArr.length();i++){
                                                        JSONObject myObj=innerArr.getJSONObject(i);
                                                        JSONArray inviteeArr,messageArr;
                                                        inviteeArr=messageArr=null;
                                                        if(myObj.has("INVITEES")){
                                                            inviteeArr=myObj.getJSONArray("INVITEES");
                                                        }
                                                        if(myObj.has("MESSAGES")){
                                                            messageArr=myObj.getJSONArray("MESSAGES");
                                                        }

                                                        //process invitee data
                                                        Repository repository=Repository.getRepository();
                                                        if(inviteeArr!=null){
                                                            if(inviteeArr.length()>0){
                                                                ArrayList<Invitee> invitees=new ArrayList<>();
                                                                for(int a=0;a<inviteeArr.length();a++){
                                                                    JSONObject inviteeObj=inviteeArr.getJSONObject(a);
                                                                    Invitee invitee=JsonParserClass.parseInviteeObject(inviteeObj);
                                                                    if(invitee!=null){
                                                                        invitees.add(invitee);
                                                                    }
                                                                }
                                                                if(repository!=null){
                                                                    repository.insertInvitees(invitees);
                                                                }
                                                            }
                                                        }

                                                        //process message data
                                                        if(messageArr!=null){
                                                            if(messageArr.length()>0){
                                                                ArrayList<Messages> msgList=new ArrayList<>();
                                                                for(int b=0;b<messageArr.length();b++){
                                                                    JSONObject msgObj=messageArr.getJSONObject(b);
                                                                    Messages messages=JsonParserClass.parseMessagesJSONObject(msgObj);
                                                                    if(messages!=null){
                                                                        msgList.add(messages);
                                                                    }
                                                                }
                                                                if(repository!=null){
                                                                    repository.insertMessages(msgList);
                                                                }
                                                            }
                                                        }

                                                        //process conversation data
                                                        if(myObj.has("INVITEES")){
                                                            myObj.remove("INVITEES");
                                                        }
                                                        if(myObj.has("MESSAGES")){
                                                            myObj.remove("MESSAGES");
                                                        }
                                                        Conversation conversation=JsonParserClass.parseConversationJsonObject(myObj);
                                                        if(conversation!=null){
                                                            conversations.add(conversation);
                                                        }
                                                    }
                                                    Repository repository=Repository.getRepository();
                                                    if(repository!=null){
                                                        repository.insertConversationData(conversations,"insert");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }catch(JSONException e){
                    Log.e("expn_convSync1:",e.toString());
                }catch(Exception e){
                    Log.e("expn_convSync2:",e.toString());
                }
            }
        }).on("conversationSyncCallback",new Emitter.Listener(){
            @Override
            public void call(Object... args){
                counter+=1;
                if(GlobalClass.getConversationArray()!=null){
                    if(counter==GlobalClass.getConversationArray().length()){
                        counter=0;
                        bindUI("conv_sync_done");
                        if(GlobalClass.getSocketModuleConv()!=null){
                            GlobalClass.getSocketModuleConv().disconnect();
                            GlobalClass.setSocketModuleConv(null);
                        }
                    }
                }
            }
        });
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
            String percentage="";
            if(string.equals("conv_sync_done")){
                percentage="percentage#80";
            }
            mObservable=GlobalClass.getLoginFragmentSubcriberr().getObservable();
            mObserver=GlobalClass.getLoginFragmentSubcriberr().getObserver();
            mObservable.just(percentage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mObserver);
        }
    }
}