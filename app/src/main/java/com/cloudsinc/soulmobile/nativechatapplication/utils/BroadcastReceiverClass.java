package com.cloudsinc.soulmobile.nativechatapplication.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.nc.developers.cloudscommunicator.utils.CheckInternetConnectionCommunicator;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.models.OfflineSync;
import com.nc.developers.cloudscommunicator.socket.SocketConnectionSync;
import java.util.ArrayList;

public class BroadcastReceiverClass extends BroadcastReceiver{
    private static final String TAG=BroadcastReceiverClass.class.getSimpleName();
    @Override
    public void onReceive(Context context,Intent intent){
        boolean internetStatus=CheckInternetConnectionCommunicator.isInternetAvailable();
        Log.i(TAG+"_internetStatus:",String.valueOf(internetStatus)+" ..kk");
        if(internetStatus){
            Log.i(TAG+"_status:","internet available");
            Repository rr=Repository.getRepository();
            ArrayList<OfflineSync> list=new ArrayList<>();
            if(rr!=null){
                list=(ArrayList<OfflineSync>)rr.getOfflineSyncDataList();
            }
            Log.i(TAG+"_listSz:",String.valueOf(list.size())+" ..kk");
            if(list.size()>0){
                GlobalClass.setMainSubcriberr(null);
                SocketConnectionSync.makeSyncSocketConnection(com.nc.developers.cloudscommunicator.GlobalClass.getLoginUrl() + "/sync");
            }
        }
    }
}