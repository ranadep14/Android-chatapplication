package com.nc.developers.cloudscommunicator.socket;

import android.util.Log;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;

public class SocketConnectionSync{
    private static Socket socket;
    private static SSLContext sslContext;
    private static X509TrustManager x509TrustManager;
    private static TrustManager[] trustAllCerts;

    public static void makeSyncSocketConnection(String url){
        try {
            getX509TrustManager();
            sslContext=SSLContext.getInstance("TLS");
            sslContext.init(null,trustAllCerts,null);
        }catch(NoSuchAlgorithmException e){
            Log.i("expn_connection1:",e.toString());
        }catch(KeyManagementException e){
            Log.i("expn_connection2:",e.toString());
        }catch(Exception e){
            Log.i("expn_connection3:",e.toString());
        }

        try{
            socket=IO.socket(url,getOptions());
            socket.connect();
            SocketListenerSync socketListenerSync=new SocketListenerSync();
            socketListenerSync.listenSyncSocket(socket);
        }catch(URISyntaxException e){
            Log.i("expn_uri_syntax1:",e.toString());
        }catch(Exception e){
            Log.i("expn_uri_syntax2:",e.toString());
        }
    }

    private static IO.Options getOptions(){
        IO.Options options = new IO.Options();
        String[] strArray = {"websocket"};
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .hostnameVerifier(getHostNameVerifier())
                .sslSocketFactory(sslContext.getSocketFactory(),
                        x509TrustManager)
                .build();

        options.callFactory=okHttpClient;
        options.webSocketFactory=okHttpClient;
        options.secure=true;
        options.path="/sync";
        options.transports=strArray;
        options.forceNew=true;
        options.reconnection=true;
        options.reconnectionDelay=2000;
        options.reconnectionDelayMax=5000;
        options.timeout=50000;

        return options;
    }

    private static HostnameVerifier getHostNameVerifier(){
        HostnameVerifier hostnameVerifier=new HostnameVerifier(){
            @Override
            public boolean verify(String s, SSLSession sslSession){
                return true;
            }
        };
        return hostnameVerifier;
    }

    private static void getX509TrustManager(){
        x509TrustManager=null;

        trustAllCerts= new TrustManager[]
                {
                        x509TrustManager = new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s)
                                    throws java.security.cert.CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s)
                                    throws java.security.cert.CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };
    }
}