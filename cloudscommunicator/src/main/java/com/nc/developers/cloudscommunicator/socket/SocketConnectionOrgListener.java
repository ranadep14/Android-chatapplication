package com.nc.developers.cloudscommunicator.socket;

import android.util.Log;
import com.nc.developers.cloudscommunicator.GlobalClass;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SocketConnectionOrgListener{
    private int counter=0;
    public void listenOrgSocket(final Socket socket){
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","org socket connected...");
                GlobalClass.setSocketModuleOrg(socket);
                bindUI("socket_org_connect");
            }
        }).on(Socket.EVENT_ERROR, new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","error occurred...");
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","org socket disconnected...");
            }
        }).on("error", new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("error: ", args[0].toString());
            }
        }).on("serverToClientIDBncSync", new Emitter.Listener() {
            @Override
            public void call(Object... args){
                Log.i("IDBncSync: ", args[0].toString());
            }
        }).on("authenticated", new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","org socket authenticated...");
                bindUI("socket_org_auth_success");
            }
        }).on("serverToClientIDBIncSync", new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("sToClientIDBIncSync ", args[0].toString());
            }
        }).on("orgSync", new Emitter.Listener(){
            @Override
            public void call(Object... args){
                String stringResponse=(String)args[0];
                Log.i("orgSync:",stringResponse);

                try{
                    JSONObject obj=new JSONObject(stringResponse);
                    if(obj!=null && obj.has("dataArray")){
                        JSONArray dataArr=obj.getJSONArray("dataArray");
                        if(dataArr!=null && dataArr.length()>0){
                            for(int k=0;k<dataArr.length();k++){
                                JSONObject innerObj=dataArr.getJSONObject(k);
                                if(innerObj.has("KEY_TYPE") && innerObj.has("SUB_KEY_TYPE")){
                                    String keytype=innerObj.getString("KEY_TYPE");
                                    String subkeytype=innerObj.getString("SUB_KEY_TYPE");
                                    if(keytype.equals("TSK") && subkeytype.equals("MSG")){
                                        Log.i("msg_obj123_org:",String.valueOf(innerObj));
                                    }
                                }
                                if(innerObj.has("KEY_TYPE") && innerObj.has("SUB_KEY_TYPE")){
                                    String keytype=innerObj.getString("KEY_TYPE");
                                    String subkeytype=innerObj.getString("SUB_KEY_TYPE");
                                    if(keytype.equals("IDE") && subkeytype.equals("TSK_CGR_IDE")){
                                        Log.i("invitee_obj123_org:",String.valueOf(innerObj));
                                    }
                                }
                                if(innerObj.has("KEY_TYPE") && innerObj.has("SUB_KEY_TYPE")){
                                    String keytype=innerObj.getString("KEY_TYPE");
                                    String subkeytype=innerObj.getString("SUB_KEY_TYPE");
                                    if(keytype.equals("TSK") && subkeytype.equals("TSK_URM_LST")){
                                        Log.i("user_urm_object_msg:",String.valueOf(innerObj));
                                    }
                                }
                                if(innerObj.has("KEY_TYPE") && innerObj.has("SUB_KEY_TYPE")){
                                    String keytype=innerObj.getString("KEY_TYPE");
                                    String subkeytype=innerObj.getString("SUB_KEY_TYPE");
                                    if(keytype.equals("TSK") && subkeytype.equals("TSK_CGR")){
                                        Log.i("convtn_object_cal:",String.valueOf(innerObj));
                                    }
                                }
                            }
                        }
                    }
                }catch(JSONException e){
                    Log.e("expn_org_sync1:",e.toString());
                }catch(Exception e){
                    Log.e("expn_org_sync2:",e.toString());
                }
            }
        }).on("orgSyncCallback", new Emitter.Listener(){
            @Override
            public void call(Object... args){
                counter+=1;
                Log.i("org_sync_callback:",String.valueOf(counter));
                if(GlobalClass.getOrgArray()!=null){
                    if(counter==GlobalClass.getOrgArray().length()){
                        counter=0;
                        bindUI("org_sync_done");
                        if(GlobalClass.getSocketModuleOrg()!=null){
                            GlobalClass.getSocketModuleOrg().disconnect();
                            GlobalClass.setSocketModuleOrg(null);
                        }
                    }
                }
                Log.i("listenerCall:","org");
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
    }
}