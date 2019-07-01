package com.nc.developers.cloudscommunicator.socket;

import android.app.Application;
import android.util.Log;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.sharedprefrences.PrefManager;

import org.json.JSONObject;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SocketListenerLogin{

    public void listenLoginSocket(final Socket socket){
        socket.on(Socket.EVENT_CONNECT,new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","login socket connected...");
                GlobalClass.setSocketLogin(socket);
                bindUI("socket_login_connect");
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
        }).on("LoginResponce",new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","login response received...");
                JSONObject object=(JSONObject)args[0];
                if(object!=null){
                    Log.i("response_login:",String.valueOf(object));
                    GlobalClass.setLoginObject(object);
                    bindUI("response_login");
                }
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
        }).on("authenticated", new Emitter.Listener(){
            @Override
            public void call(Object... args){
            }
        }).on("serverToClientIDBIncSync", new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("sToClientIDBIncSync ", args[0].toString());
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
        if(GlobalClass.getSignupSubcription()!=null){
            mObservable=GlobalClass.getSignupSubcription().getObservable();
            mObserver=GlobalClass.getSignupSubcription().getObserver();
            mObservable.just(string)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mObserver);
        }
        if(GlobalClass.getForgetPasswordSubcriberr()!=null){
            mObservable=GlobalClass.getForgetPasswordSubcriberr().getObservable();
            mObserver=GlobalClass.getForgetPasswordSubcriberr().getObserver();
            mObservable.just(string)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mObserver);
        }
        //today added
        if(GlobalClass.getLoginFragmentSubcriberr()!=null){
            String percentage="";
            if(string.equals("socket_login_connect")){
                percentage="percentage#10";
            }
            if(string.equals("response_login")){
                percentage="percentage#20";
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