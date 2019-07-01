package com.nc.developers.cloudscommunicator.socket;

import android.util.Log;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.models.Calendar;
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

public class SocketConnectionCalListener{
    private int counter=0;
    private static final String TAG=SocketConnectionCalListener.class.getSimpleName();
    public void listenCalendarSocket(final Socket socket){
        socket.on(Socket.EVENT_CONNECT,new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","calendar socket connected...");
                GlobalClass.setSocketModuleCalendar(socket);
                bindUI("socket_cal_connect");
            }
        }).on(Socket.EVENT_ERROR,new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","error occurred...");
            }
        }).on(Socket.EVENT_DISCONNECT,new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("status:","calendar socket disconnected...");
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
                Log.i("status:","calendar socket authenticated...");
                bindUI("socket_cal_auth_success");
            }
        }).on("serverToClientIDBIncSync",new Emitter.Listener(){
            @Override
            public void call(Object... args){
                Log.i("sToClientIDBIncSync ",args[0].toString());
            }
        }).on("calendarSync",new Emitter.Listener(){
            @Override
            public void call(Object... args){
                String stringResponse=(String)args[0];
                PrefManager.setValue("calSync:",stringResponse+" ..kk");
                Log.i("calSync:",stringResponse);
                JSONObject calSyncObject=null;
                try{
                    calSyncObject=new JSONObject(stringResponse);
                    if(calSyncObject!=null){
                        if(calSyncObject.has("key")){
                            String keyString=calSyncObject.getString("key");
                            if(keyString!=null){
                                if(keyString.equals("CAL")){
                                    if(calSyncObject.has(ConstantsObjects.DATA_ARRAY)){
                                        JSONArray dataArr=calSyncObject.getJSONArray(ConstantsObjects.DATA_ARRAY);
                                        if(dataArr!=null){
                                            if(dataArr.length()>0){
                                                ArrayList<Calendar> calendarList=new ArrayList<>();
                                                for(int i=0;i<dataArr.length();i++){
                                                    JSONObject innerObj=dataArr.getJSONObject(i);
                                                    Calendar calendar=JsonParserClass.parseCalendarSyncResponse(innerObj);
                                                    calendarList.add(calendar);
                                                }
                                                Repository repository=Repository.getRepository();
                                                if(repository!=null){
                                                    boolean bbc=repository.insertCalendarData(calendarList);
                                                    if(bbc){
                                                        Log.i(TAG+"_calSyncStatus:","insert success");
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
                    Log.e(TAG+"_calSync1:",e.toString());
                }catch(Exception e){
                    Log.e(TAG+"_calSync2:",e.toString());
                }
            }
        }).on("calendarSyncCallback",new Emitter.Listener(){
            @Override
            public void call(Object... args){
                counter+=1;
                if(GlobalClass.getCalendarArray()!=null){
                    if(counter==GlobalClass.getCalendarArray().length()){
                        counter=0;
                        bindUI("cal_sync_done");
                        if(GlobalClass.getSocketModuleCalendar()!=null){
                            GlobalClass.getSocketModuleCalendar().disconnect();
                            GlobalClass.setSocketModuleCalendar(null);
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
            if(string.equals("cal_sync_done")){
                percentage="percentage#60";
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