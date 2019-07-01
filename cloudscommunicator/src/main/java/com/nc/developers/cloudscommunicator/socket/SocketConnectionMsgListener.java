package com.nc.developers.cloudscommunicator.socket;

import android.util.Log;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.objects.ConstantsObjects;
import com.nc.developers.cloudscommunicator.sharedprefrences.PrefManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SocketConnectionMsgListener{
    private int counter=0;
    public void listenMsgSocket(final Socket socket){
        socket.on(Socket.EVENT_CONNECT,new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","msg socket connected...");
                GlobalClass.setSocketModuleMessaging(socket);
                bindUI("socket_msg_connect");
            }
        }).on(Socket.EVENT_ERROR,new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","error occurred...");
            }
        }).on(Socket.EVENT_DISCONNECT,new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","login socket disconnected...");
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
        }).on("serverToClientIDBncSync",new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("IDBncSync: ", args[0].toString());
            }
        }).on("authenticated",new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","msg socket authenticated...");
                bindUI("socket_msg_auth_success");
            }
        }).on("serverToClientIDBIncSync",new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("sToClientIDBIncSync ", args[0].toString());
            }
        }).on("messagingSync",new Emitter.Listener(){
            @Override
            public void call(Object... args){
                String stringResponse=(String)args[0];
                PrefManager.setValue("msgSync:",stringResponse+" ..kk");
                JSONObject msgSyncObj=null;
                try{
                    msgSyncObj=new JSONObject(stringResponse);
                    Log.i("msgSync:",stringResponse);
                    if(msgSyncObj!=null){
                        if(msgSyncObj.has("key")){
                            String key=msgSyncObj.getString("key");
                            if(key!=null){
                                if(key.equals("CDE")){
                                    if(msgSyncObj.has(ConstantsObjects.DATA_ARRAY)){
                                        JSONArray dataArr=msgSyncObj.getJSONArray(ConstantsObjects.DATA_ARRAY);
                                        if(dataArr!=null){
                                            JSONObject innerObj=dataArr.getJSONObject(0);
                                            if(innerObj!=null){
                                                if(innerObj.has(ConstantsObjects.KEY_VAL_INNER)){
                                                    GlobalClass.setTSK_USR_keyval(innerObj.getString(ConstantsObjects.KEY_VAL_INNER));
                                                }
                                                if(innerObj.has("UAD_USER_IMAGE")){
                                                    JSONArray uadUserImageArr=innerObj.getJSONArray("UAD_USER_IMAGE");
                                                    if(uadUserImageArr!=null){
                                                        String imagePath=uadUserImageArr.getString(0);
                                                        Log.i("msgSyncccImgPth:",imagePath+" ..kk");
                                                        GlobalClass.setTSK_USR_imagePath(imagePath);
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
                    Log.e("msgSync_1",e.toString());
                }catch(Exception e){
                    Log.e("msgSync_2",e.toString());
                }
            }
        }).on("messagingSyncCallback",new Emitter.Listener(){
            @Override
            public void call(Object... args){
                counter+=1;
                if(GlobalClass.getMessagingArray()!=null){
                    if(counter==GlobalClass.getMessagingArray().length()){
                        counter=0;
                        bindUI("msg_sync_done");
                        if(GlobalClass.getSocketModuleMessaging()!=null){
                            GlobalClass.getSocketModuleMessaging().disconnect();
                            GlobalClass.setSocketModuleMessaging(null);
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
            if(string.equals("msg_sync_done")){
                percentage="percentage#70";
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