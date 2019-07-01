package com.nc.developers.cloudscommunicator;

import android.util.Log;
import com.nc.developers.cloudscommunicator.models.Login;
import com.nc.developers.cloudscommunicator.network.NetworkRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ImageClass{

    private static volatile ImageClass INSTANCE;
    private static NetworkRequest networkRequest=null;

    private ImageClass(){
        Log.i(TAG+"_private Constructor:","status_ok#first_class");
        if(networkRequest==null){
            Retrofit.Builder builder=new Retrofit.Builder()
                    .baseUrl(GlobalClass.getLoginUrl())
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit=null;
            if(builder!=null){
                retrofit=builder.build();
            }
            if(retrofit!=null){
                networkRequest=retrofit.create(NetworkRequest.class);
            }
        }
    }

    public static ImageClass getImageClassInstance(){
        if(INSTANCE==null){
            synchronized(ImageClass.class){
                if(INSTANCE==null){
                    INSTANCE=new ImageClass();
                }
            }
        }
        return INSTANCE;
    }

    private final String TAG=ImageClass.class.getSimpleName();
    private Call<ResponseBody> call=null;
    private ArrayList<String> serverPathList;

    public void uploadFile(final ArrayList<String> localFilePathList){
        if(localFilePathList!=null){
            if(localFilePathList.size()>0){
                serverPathList=new ArrayList<String>();
                GlobalClass.setServerPathList(null);
                for(int i=0;i<localFilePathList.size();i++){
                    String localFilePath=localFilePathList.get(i);
                    if(localFilePath!=null){
                        File file=new File(localFilePath);
                        String fileName="";
                        if(file!=null){
                            fileName=file.getName();
                        }
                        Log.i(TAG+"_fileName:",fileName+" ..kk");
                        RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"),
                                file);
                        MultipartBody.Part filePart=MultipartBody.Part.createFormData("stream",
                                fileName,requestBody);
                        RequestBody namePart=RequestBody.create(MediaType.parse("text/plain"),fileName);
                        String jwtToken="";
                        Repository repository=Repository.getRepository();
                        Login login=null;
                        if(repository!=null){
                            login=repository.getLoginData();
                            if(login!=null){
                                jwtToken=login.getJwtToken();
                            }
                        }
                        if(networkRequest!=null){
                            call=networkRequest.uploadFile(filePart,namePart,jwtToken);
                            if(call!=null){
                                call.enqueue(new Callback<ResponseBody>(){
                                    @Override
                                    public void onResponse(Call<ResponseBody> call,
                                                           retrofit2.Response<ResponseBody> response){
                                        Log.i(TAG+"_inside:","onResponse");
                                        String serverPath,serverFileName;
                                        serverPath=serverFileName="";
                                        if(response!=null){
                                            try{
                                                String responseString=response.body().string();
                                                Log.i(TAG+"_response:",responseString+" ..kk");
                                                JSONObject responseObj=new JSONObject(responseString);
                                                if(responseObj!=null){
                                                    if(responseObj.has("uploadFiles")){
                                                        JSONArray fileUploadArr=responseObj.getJSONArray("uploadFiles");
                                                        if(fileUploadArr!=null){
                                                            if(fileUploadArr.length()>0){
                                                                JSONObject fileUploadObj=fileUploadArr.getJSONObject(0);
                                                                if(fileUploadObj!=null){
                                                                    if(fileUploadObj.has("path")){
                                                                        serverPath=fileUploadObj.getString("path");
                                                                    }
                                                                    if(fileUploadObj.has("fileName")){
                                                                        serverFileName=fileUploadObj.getString("fileName");
                                                                    }
                                                                    Log.i(TAG+"_server_path:",serverPath);
                                                                    Log.i(TAG+"_server_fileName:",serverFileName);
                                                                    String outputString=serverFileName+"#"+serverPath;
                                                                    if(outputString!=null){
                                                                        serverPathList.add(outputString);
                                                                    }
                                                                    if(localFilePathList.size()==serverPathList.size()){
                                                                        GlobalClass.setServerPathList(serverPathList);
                                                                        bindUI("upload_done"+"####"+serverPath+"####"+serverFileName);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }catch(IOException e){
                                                Log.e(TAG+"_expn1:",e.toString());
                                            }catch(Exception e){
                                                Log.e(TAG+"_expn2:",e.toString());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call,
                                                          Throwable t){
                                        Log.i(TAG+"_inside:","onFailure");
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }
    }

    private void bindUI(String strr){
        Observable<String> fileUploadObservable=null;
        Observer<String> fileUploadObserver=null;
        if(GlobalClass.getFileUploadSubcriberr()!=null){
            fileUploadObservable=GlobalClass.getFileUploadSubcriberr().getObservable();
            fileUploadObserver=GlobalClass.getFileUploadSubcriberr().getObserver();
            fileUploadObservable.just(strr)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(fileUploadObserver);
        }
        if(GlobalClass.getConversationSubcriberr()!=null){
            fileUploadObservable=GlobalClass.getConversationSubcriberr().getObservable();
            fileUploadObserver=GlobalClass.getConversationSubcriberr().getObserver();
            fileUploadObservable.just(strr)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(fileUploadObserver);
        }
        if(GlobalClass.getCurrentSubcriberr()!=null){
            fileUploadObservable=GlobalClass.getCurrentSubcriberr().getObservable();
            fileUploadObserver=GlobalClass.getCurrentSubcriberr().getObserver();
            fileUploadObservable.just(strr)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(fileUploadObserver);
        }
        if(GlobalClass.getSignupSubcription()!=null){
            fileUploadObservable=GlobalClass.getSignupSubcription().getObservable();
            fileUploadObserver=GlobalClass.getSignupSubcription().getObserver();
            fileUploadObservable.just(strr)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(fileUploadObserver);
        }
    }
}