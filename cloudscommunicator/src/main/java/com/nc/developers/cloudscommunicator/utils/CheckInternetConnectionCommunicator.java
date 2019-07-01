package com.nc.developers.cloudscommunicator.utils;

import android.util.Log;

import java.net.InetAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CheckInternetConnectionCommunicator {
    private static final String TAG=CheckInternetConnectionCommunicator.class.getSimpleName();
    public static boolean isInternetAvailable(){
        boolean success=false;
        Callable<Boolean> callableInternetCheckrr=null;
        callableInternetCheckrr=new Callable<Boolean>(){
            @Override
            public Boolean call(){
                boolean bk=false;
                try{
                    InetAddress ipAddr=InetAddress.getByName("google.com"); //You can replace it with your name
                    if (ipAddr.equals("")){
                        bk=false;
                        //return false;
                    }else{
                        bk=true;
                        //return true;
                    }
                }catch(Exception e){
                    bk=false;
                    //return false;
                }
                return bk;
            }
        };
        try{
            ExecutorService service= Executors.newSingleThreadExecutor();
            Future<Boolean> future=service.submit(callableInternetCheckrr);
            success=future.get();
        }catch(InterruptedException e){
            Log.i(TAG+"_internet1:",e.toString());
        }catch(ExecutionException e){
            Log.i(TAG+"_internet2:",e.toString());
        }catch(Exception e){
            Log.i(TAG+"_internet3:",e.toString());
        }
        return success;
    }
    /*private boolean checkInternet(Context context){
        ConnectivityManager cm=(ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo=cm.getActiveNetworkInfo();
        return (netInfo!=null && netInfo.isConnected());
    }*/
}