package com.nc.developers.cloudscommunicator.socket;

import android.util.Log;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Repository;

import org.json.JSONException;
import org.json.JSONObject;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SocketListenerSync{
    public void listenSyncSocket(final Socket socket){
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","sync socket connected...");
                GlobalClass.setAuthenticatedSyncSocket(socket);
                bindUI("socket_sync_connect");
            }
        }).on(Socket.EVENT_ERROR, new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","error occurred...");
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","login socket disconnected...");
            }
        }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener(){
            @Override
            public void call(Object... args){
                //handle here network disconnect issue
                Log.i("status:","event_connect error occurred...");
            }
        }).on("error", new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("error: ", args[0].toString());
            }
        }).on("serverToClientIDBncSync",new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("IDBncSync: ", args[0].toString());
            }
        }).on("authenticated", new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","sync socket authenticated...");
                bindUI("socket_sync_auth_success");
                Repository rr=Repository.getRepository();
                if(rr!=null){
                    rr.offlineSyncWorker();
                }
            }
        }).on("serverToClientIDBIncSync",new Emitter.Listener(){
            @Override
            public void call(Object... args){
                String responseString=(String)args[0];
                Log.i("response:","serverToClientIDBIncSync");
                Log.i("response:",responseString);
                JSONObject responseObj=null;
                try{
                    responseObj=new JSONObject(responseString);
                    HandleOnDemandResponse obj=new HandleOnDemandResponse();
                    obj.handleResponse(responseObj);
                }catch(JSONException e){
                    Log.e("expn_respns_onDemnd1:",e.toString());
                }catch(Exception e){
                    Log.e("expn_respns_onDemnd2:",e.toString());
                }
            }
        }).on("onDemand",new Emitter.Listener(){
            @Override
            public void call(Object... args){
                String responseString=(String)args[0];
                Log.i("response:","onDemand");
                Log.i("response:",responseString);
                JSONObject responseObj=null;
                try{
                    responseObj=new JSONObject(responseString);
                    HandleOnDemandResponse obj=new HandleOnDemandResponse();
                    obj.handleResponse(responseObj);
                }catch(JSONException e){
                    Log.e("expn_respns_onDemnd1:",e.toString());
                }catch(Exception e){
                    Log.e("expn_respns_onDemnd2:",e.toString());
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
        if(GlobalClass.getMainSubcriberr()!=null){
            mObservable=GlobalClass.getMainSubcriberr().getObservable();
            mObserver=GlobalClass.getMainSubcriberr().getObserver();
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
        if(GlobalClass.getLoginFragmentSubcriberr()!=null){
            String percentage="";
            if(string.equals("socket_sync_connect")){
                percentage="percentage#35";
            }
            if(string.equals("socket_sync_auth_success")){
                percentage="percentage#50";
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