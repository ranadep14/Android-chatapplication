package com.cloudsinc.soulmobile.nativechatapplication.fragments.login;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.dialogs.CustomDialog;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.FragmentHomeScreen;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.signup.SignupFragment;
import com.cloudsinc.soulmobile.nativechatapplication.utils.InternetConnectionChecker;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.objects.login.JsonObjectLogin;
import com.nc.developers.cloudscommunicator.sharedprefrences.PrefManager;
import com.nc.developers.cloudscommunicator.utils.EncryptionUtility;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.socket.JSonObjectClass;
import com.nc.developers.cloudscommunicator.socket.SocketConnection;
import com.nc.developers.cloudscommunicator.socket.SocketConnectionLogin;
import com.github.ybq.android.spinkit.SpinKitView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.hideKeyboard;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setQuicksandMediumFont;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setQuicksandRegularFont;

public  class LoginFragment extends Fragment{
    private String username="",password="",failureMsg="",message="",actionArrayString="",
            userId="",jwtToken="",userUID="",orgId="";
    private JSONObject syncMapObject=null;
    public static boolean isForgotPwd = false;
    private boolean passwordShown = false;
    public static long startTime=0L;
    private static boolean isLoginDone=false,isInternetNotAvailableDialogueAlreadyDisplayed=false,
            isEmptyUsernamePasswordDialogueAlreadyDisplayed=false,isPasswordShortDialogueAlreadyDisplayed=false,
            isPasswordLongDialogueAlreadyDisplayed=false,isLoginErrorDisplayed=false;
    private static final String TAG=LoginFragment.class.getSimpleName();
    private static final int KEYSIZE=128;
    private static final String PASSPHRASE="abcdef";
    private static final int ITERATION_COUNT=1000;
    private static final String SALT="577bd45a17977269694908d80905c32a";
    private static final String FOUR="9a2b73d130c8796309b776eeb09834b0";
    public static boolean isMobileSyncCompleted=false;

    public LoginFragment(){
        // Required empty public constructor
    }
    @BindView(R.id.btn_login)Button btn_login;
    @BindView(R.id.edit_uname)EditText edit_uname;
    @BindView(R.id.edit_password)EditText edit_password;
    @BindView(R.id.txt_signup)TextView txt_signup;
    @BindView(R.id.txt_forgot_pwd)TextView txt_forgot_pwd;
    @BindView(R.id.txt_login_title)TextView txt_login_title;
    @BindView(R.id.progress_login)SpinKitView progress_login;
    @BindView(R.id.iconHidePass)ImageView iconHidePass;
    @BindView(R.id.circularProgressbar)ProgressBar circularProgressbar;
    @BindView(R.id.tv)TextView tv;

    private static Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this,view);
        mContext=view.getContext();
        txt_login_title.setTypeface(setQuicksandRegularFont(mContext));
        txt_forgot_pwd.setTypeface(setQuicksandRegularFont(mContext));
        txt_signup.setTypeface(setQuicksandRegularFont(mContext));
        edit_uname.setTypeface(setQuicksandRegularFont(mContext));
        edit_password.setTypeface(setQuicksandRegularFont(mContext));
        btn_login.setTypeface(setQuicksandMediumFont(mContext));
        btn_login=view.findViewById(R.id.btn_login);
        edit_uname=view.findViewById(R.id.edit_uname);
        edit_password=view.findViewById(R.id.edit_password);

        setSubcriber();
        username=password=failureMsg=message=actionArrayString=userId=jwtToken=userUID=orgId="";

        iconHidePass.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api=Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v){
                if(passwordShown){
                    passwordShown=false;
                    iconHidePass.setBackgroundResource(R.drawable.ic_show_pwd);
                    edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    passwordShown=true;
                    iconHidePass.setBackgroundResource(R.drawable.ic_hide_pass);
                    edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        /*login operation is handled*/
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                LoginFragment.isMobileSyncCompleted=false;
                startTime=System.nanoTime();
                GlobalClass.setForgetPasswordSubcriberr(null);
                LoginFragment.isLoginDone
                        =LoginFragment.isInternetNotAvailableDialogueAlreadyDisplayed
                        =LoginFragment.isEmptyUsernamePasswordDialogueAlreadyDisplayed
                        =LoginFragment.isPasswordShortDialogueAlreadyDisplayed
                        =LoginFragment.isPasswordLongDialogueAlreadyDisplayed
                        =LoginFragment.isLoginErrorDisplayed
                        =false;
                Handler handler=new Handler();
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        Log.i(TAG+"_loginStatus:",String.valueOf(LoginFragment.isLoginDone)+" ..kk");
                        if(!LoginFragment.isLoginDone
                                && !LoginFragment.isInternetNotAvailableDialogueAlreadyDisplayed
                                && !LoginFragment.isEmptyUsernamePasswordDialogueAlreadyDisplayed
                                && !LoginFragment.isPasswordShortDialogueAlreadyDisplayed
                                && !LoginFragment.isPasswordLongDialogueAlreadyDisplayed
                                && !LoginFragment.isLoginErrorDisplayed){
                            circularProgressbar.setVisibility(View.GONE);
                            tv.setVisibility(View.GONE);
                            LoginFragment.isLoginDone=false;
                            message="";
                            message=getString(R.string.login_response_not_received);
                            Repository rr=Repository.getRepository();
                            if(rr!=null){
                                rr.clearDatabase();
                                rr.closeAllSockets();
                            }
                            if(com.nc.developers.cloudscommunicator.GlobalClass.getCurrentSubcriberr()!=null){
                                com.nc.developers.cloudscommunicator.GlobalClass.setCurrentSubcriberr(null);
                            }
                            if(com.nc.developers.cloudscommunicator.GlobalClass.getLoginFragmentSubcriberr()!=null){
                                com.nc.developers.cloudscommunicator.GlobalClass.setLoginFragmentSubcriberr(null);
                            }
                            if(com.nc.developers.cloudscommunicator.GlobalClass.getSignupSubcription()!=null){
                                com.nc.developers.cloudscommunicator.GlobalClass.setSignupSubcription(null);
                            }
                            edit_uname.setEnabled(true);
                            edit_password.setEnabled(true);
                            btn_login.setVisibility(View.VISIBLE);
                            btn_login.setEnabled(true);
                            progress_login.setVisibility(View.GONE);
                            CustomDialog.dispDialogAlert(getContext(),LoginFragment.class,message,false);
                        }
                        long endTime=System.nanoTime();
                        long loginDuration=endTime-LoginFragment.startTime;
                        double seconds=(double)loginDuration/1_000_000_000.0;
                        Log.i(TAG+"_loginFailTiming:",String.valueOf(seconds)+" ..kk");
                    }
                },80000);
                com.nc.developers.cloudscommunicator.GlobalClass.setSignupSubcription(null);
                setLoginFragmentSubcriber();
                setSubcriber();
                username=edit_uname.getText().toString();
                password=edit_password.getText().toString();
                if(username.isEmpty() && !password.isEmpty()){
                    edit_uname.setError(getString(R.string.empty_user_id));
                    edit_uname.requestFocus();
                }

                if(!username.isEmpty() && password.isEmpty()){
                    message=getString(R.string.enter_valid_password);
                    CustomDialog.dispDialogAlert(getContext(),LoginFragment.class,message,false);
                    LoginFragment.isEmptyUsernamePasswordDialogueAlreadyDisplayed=true;
                }

                if(username.isEmpty() && password.isEmpty()){
                    edit_uname.requestFocus();
                    message=getString(R.string.empty_username_password);
                    CustomDialog.dispDialogAlert(getContext(), LoginFragment.class, message, false);
                    LoginFragment.isEmptyUsernamePasswordDialogueAlreadyDisplayed=true;
                }

                if(username!=null && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)
                        &&password!=null && password.length()<2){
                    message=getString(R.string.password_short);
                    CustomDialog.dispDialogAlert(getContext(), LoginFragment.class, message, false);
                    edit_password.setError(getString(R.string.password_short));
                    edit_password.requestFocus();
                    LoginFragment.isPasswordShortDialogueAlreadyDisplayed=true;
                }

                if(username!=null && !TextUtils.isEmpty(username) && password!=null
                        && password.length()>13){
                    message = getString(R.string.password_long);
                    CustomDialog.dispDialogAlert(getContext(), LoginFragment.class, message, false);
                    edit_password.setError(getString(R.string.password_long));
                    edit_password.requestFocus();
                    LoginFragment.isPasswordLongDialogueAlreadyDisplayed=true;
                }
                if(validateUsername(username) && !password.isEmpty()){
                    if(InternetConnectionChecker.checkInternetConnection(mContext)){
                        startTime=System.nanoTime();
                        txt_signup.setVisibility(View.INVISIBLE);
                        btn_login.setVisibility(View.VISIBLE);
                        btn_login.setEnabled(false);
                        edit_uname.setEnabled(false);
                        edit_password.setEnabled(false);
                        txt_forgot_pwd.setEnabled(false);
                        hideKeyboard(getActivity());

                        if(circularProgressbar!=null){
                            circularProgressbar.setProgress(2);
                        }
                        if(tv!=null){
                            tv.setText("2%");
                        }
                        if(com.nc.developers.cloudscommunicator.GlobalClass.getSignupSubcription()!=null){
                            com.nc.developers.cloudscommunicator.GlobalClass.setSignupSubcription(null);
                        }
                        String url=com.nc.developers.cloudscommunicator.GlobalClass.getLoginUrl();
                        if(username.contains("@clouzersoftwaresolutions.clouzer.com")){
                            url="https://clouzersoftwaresolutions.clouzer.com:443";
                            GlobalClass.setLoginUrl("https://clouzersoftwaresolutions.clouzer.com:443");
                        }
                        if(url!=null){
                            if(!TextUtils.isEmpty(url)){
                                btn_login.setEnabled(false);
                                Log.i(TAG+"_urlBhai:",url+" ..kk");
                                SocketConnectionLogin.makeLoginSocketConnection(url+"/login");
                                circularProgressbar.setVisibility(View.VISIBLE);
                                tv.setVisibility(View.VISIBLE);
                            }
                        }
                    }else{
                        CustomDialog.dispDialogAlert(mContext,LoginFragment.class,
                                getString(R.string.internet_not_available),false);
                        LoginFragment.isInternetNotAvailableDialogueAlreadyDisplayed=true;
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onPause(){
        Log.i(TAG+"_fragmntState:","paused...");
        Log.i(TAG+"_mobileSyncStateOnP:",String.valueOf(LoginFragment.isMobileSyncCompleted)+" ..kk");
        if(!LoginFragment.isMobileSyncCompleted){
            SocketConnection.initialSyncCounter=0;
            if(GlobalClass.getSocketModuleMessaging()!=null){
                GlobalClass.getSocketModuleMessaging().off();
                GlobalClass.setSocketModuleMessaging(null);
            }
            if(GlobalClass.getSocketModuleConv()!=null){
                GlobalClass.getSocketModuleConv().off();
                GlobalClass.setSocketModuleConv(null);
            }
            if(GlobalClass.getSocketModuleCalendar()!=null){
                GlobalClass.getSocketModuleCalendar().off();
                GlobalClass.setSocketModuleCalendar(null);
            }
            if(GlobalClass.getAuthenticatedSyncSocket()!=null){
                GlobalClass.getAuthenticatedSyncSocket().off();
                GlobalClass.setAuthenticatedSyncSocket(null);
            }
            LoginFragment.isLoginDone=true;//to avoid dialog box
            Repository rrr=Repository.getRepository();
            if(rrr!=null){
                rrr.clearDatabase();
            }
        }
        super.onPause();
    }

    @Override
    public void onResume(){
        Log.i(TAG+"_mobileSyncState:",String.valueOf(LoginFragment.isMobileSyncCompleted)+" ..kk");
        if(LoginFragment.isMobileSyncCompleted){
            LoginFragment.isMobileSyncCompleted=false;
            circularProgressbar.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
            ReplaceFragment.replaceFragment(LoginFragment.this,R.id.frame_frag_container,new FragmentHomeScreen(),false);
        }else{
            if(circularProgressbar!=null){
                circularProgressbar.setVisibility(View.GONE);
            }
            if(tv!=null){
                tv.setVisibility(View.GONE);
            }
            if(edit_uname!=null){
                edit_uname.setEnabled(true);
                edit_uname.setText("");
                edit_uname.requestFocus();
            }
            if(edit_password!=null){
                edit_password.setEnabled(true);
                edit_password.setText("");
            }
            if(btn_login!=null){
                btn_login.setEnabled(true);
            }
        }
        super.onResume();
    }

    private void setLoginFragmentSubcriber(){
        mObservable=Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("");
                subscriber.onCompleted();
            }
        });
        mObserver=new Observer<String>(){
            @Override
            public void onCompleted(){

            }

            @Override
            public void onError(Throwable e){

            }

            @Override
            public void onNext(String string){
                Log.i(TAG+"_LoginFragment:",string + "..kk");
                if(string!=null){
                    if(string.contains("percentage")){
                        String[] arr=string.split("#");
                        if(arr!=null){
                            if(arr.length==2){
                                String percentageString=arr[1];
                                Log.i(TAG+"_calculate:",percentageString+" ..kk");
                                int percentageHowMany=Integer.parseInt(percentageString);
                                Log.i(TAG+"_percentageHowMany:",String.valueOf(percentageHowMany)+" ..kk");
                                circularProgressbar.setProgress(percentageHowMany);
                                tv.setText(percentageString+"%");
                            }
                        }
                    }
                    if(string.equals("initial_sync_done")){
                        isMobileSyncCompleted=true;
                        circularProgressbar.setProgress(100);
                        tv.setText("100%");
                        LoginFragment.isLoginDone=true;
                        long endTime=System.nanoTime();
                        long loginDuration=endTime-LoginFragment.startTime;
                        double seconds=(double)loginDuration/1_000_000_000.0;
                        Log.i(TAG+"_loginTimeInSeconds.:",String.valueOf(seconds)+" ..kk");
                        circularProgressbar.setVisibility(View.GONE);
                        tv.setVisibility(View.GONE);
                        ReplaceFragment.replaceFragment(LoginFragment.this,R.id.frame_frag_container,new FragmentHomeScreen(),false);
                    }
                }
            }
        };
        com.nc.developers.cloudscommunicator.Subcription subcription=new com.nc.developers.cloudscommunicator.Subcription();
        subcription.setObservable(mObservable);
        subcription.setObserver(mObserver);
        com.nc.developers.cloudscommunicator.GlobalClass.setLoginFragmentSubcriberr(subcription);
    }

    private Observable<String> mObservable;
    private Observer<String> mObserver;
    private void setSubcriber(){
        mObservable=Observable.create(new Observable.OnSubscribe<String>(){
            @Override
            public void call(Subscriber<? super String> subscriber){
                subscriber.onNext("");
                subscriber.onCompleted();
            }
        });

        mObserver=new Observer<String>(){
            @Override
            public void onCompleted(){

            }

            @Override
            public void onError(Throwable e){

            }

            @Override
            public void onNext(String string){
                if(string!=null){
                    Log.i(TAG+"_loginFragment:",string+" ..kk");
                    if(string.equals("socket_login_connect")){
                        EncryptionUtility utility=new EncryptionUtility(KEYSIZE,ITERATION_COUNT);
                        String enryptedPassword=utility.encrypt(SALT,FOUR,PASSPHRASE,password);
                        Log.i(TAG+"_encryptedPass:",enryptedPassword+" ..kk");
                        if(username!=null && enryptedPassword!=null
                                && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(enryptedPassword)){
                            JSONObject loginRequestObject=null;
                            Log.i(TAG+"_userName:",username+" ..kk");
                            Log.i(TAG+"_password:",password+" ..kk");
                            String userNameParts[]=username.split("@");
                            String secondPart="";
                            if(userNameParts!=null){
                                if(userNameParts.length==2){
                                    secondPart=userNameParts[1];
                                }
                            }
                            if(secondPart.equals("clouzer.com")){
                                loginRequestObject=JSonObjectClass.getJsonObjectLogin(username,enryptedPassword);
                            }
                            if(secondPart.equals("clouzersoftwaresolutions.clouzer.com")){
                                loginRequestObject= JSonObjectClass.getJsonObjectLoginOrg(username,enryptedPassword);
                            }
                            Log.i(TAG+"_loginObj:",String.valueOf(loginRequestObject));
                            if(com.nc.developers.cloudscommunicator.GlobalClass.getSocketLogin()!=null){
                                com.nc.developers.cloudscommunicator.GlobalClass.getSocketLogin()
                                        .emit("LOGIN",loginRequestObject);
                                bindUI("emit_login");
                            }
                        }
                    }
                    if(string.equals("response_login")){
                        JSONObject objectLoginResponse=GlobalClass.getLoginObject();
                        PrefManager.setValue("login_rrr",String.valueOf(objectLoginResponse)+" ..kk");
                        if(objectLoginResponse!=null){
                            if(com.nc.developers.cloudscommunicator.GlobalClass.getSocketLogin()!=null){
                                com.nc.developers.cloudscommunicator.GlobalClass.getSocketLogin().disconnect();
                                com.nc.developers.cloudscommunicator.GlobalClass.setSocketLogin(null);
                            }
                            try{
                                if(objectLoginResponse.has("ACTION_ARRAY")){
                                    JSONArray actionArray=objectLoginResponse.getJSONArray("ACTION_ARRAY");
                                    actionArrayString=actionArray.getString(0);
                                    if(actionArrayString.equals("LOGIN")){
                                        if(objectLoginResponse.has("cookie_options")){
                                            objectLoginResponse.remove("cookie_options");
                                        }
                                        if(objectLoginResponse.has("baton")){
                                            objectLoginResponse.remove("baton");
                                        }
                                        if(objectLoginResponse.has("jwtToken")){
                                            objectLoginResponse.remove("jwtToken");
                                        }
                                        if(objectLoginResponse.has("userKeyVal")){
                                            com.nc.developers.cloudscommunicator.GlobalClass.setUserKeyval(
                                                    objectLoginResponse.getString("userKeyVal"));
                                        }
                                        if(objectLoginResponse.has("syncMap")){
                                            syncMapObject=objectLoginResponse.getJSONObject("syncMap");
                                            Log.i(TAG+"_syncMapObj:",String.valueOf(syncMapObject)+" ..kk");
                                            com.nc.developers.cloudscommunicator.GlobalClass.setSyncMapJSONObject(syncMapObject);
                                            objectLoginResponse.remove("syncMap");
                                        }
                                        if(objectLoginResponse.has("Info")){
                                            JSONObject objectInfo=objectLoginResponse.getJSONObject("Info");
                                            if(objectInfo.has("serverInfo")){
                                                objectInfo.remove("serverInfo");
                                            }
                                            if(objectInfo.has("myInfo")){
                                                JSONObject objectMyInfo=objectInfo.getJSONObject("myInfo");
                                                if(objectMyInfo.has("globalVars")){
                                                    objectMyInfo.remove("globalVars");
                                                }
                                                if(objectMyInfo.has("userId")){
                                                    userId=objectMyInfo.getString("userId");
                                                    com.nc.developers.cloudscommunicator.GlobalClass.setUserId(userId);
                                                }
                                                if(objectMyInfo.has("jwttoken")){
                                                    jwtToken=objectMyInfo.getString("jwttoken");
                                                }
                                                if(objectMyInfo.has("userUID")){
                                                    userUID=objectMyInfo.getString("userUID");
                                                    com.nc.developers.cloudscommunicator.GlobalClass.setUserUID(userUID);
                                                }
                                                if(objectMyInfo.has("ORG_ID")){
                                                    orgId=objectMyInfo.getString("ORG_ID");
                                                    com.nc.developers.cloudscommunicator.GlobalClass.setOrgID(orgId);
                                                }
                                            }
                                        }

                                        // call authenticate here
                                        SocketConnection.authenticate(username,password,userId,
                                                jwtToken,
                                                userUID,
                                                orgId,
                                                syncMapObject);
                                    }
                                    if(actionArrayString.equals("ERROR")){
                                        LoginFragment.isLoginErrorDisplayed=true;
                                        if(objectLoginResponse.has("MSG")){
                                            failureMsg=objectLoginResponse.getString("MSG");
                                        }
                                        Log.i("failure_message:",failureMsg);
                                        if(failureMsg.equals("Password is not Correct")){
                                            failureMsg=getString(R.string.enter_valid_password);
                                        }
                                        circularProgressbar.setVisibility(View.GONE);
                                        tv.setVisibility(View.GONE);
                                        new Handler(Looper.getMainLooper()).post(new Runnable(){
                                            @Override
                                            public void run(){
                                                CustomDialog.dispDialogAlert(mContext,LoginFragment.class,failureMsg,false);
                                                txt_signup.setVisibility(View.VISIBLE);
                                                btn_login.setVisibility(View.VISIBLE);
                                                progress_login.setVisibility(View.GONE);
                                                btn_login.setEnabled(true);
                                                edit_uname.setEnabled(true);
                                                edit_password.setEnabled(true);
                                                txt_forgot_pwd.setEnabled(true);
                                                //CustomDialog.progress.dismiss();
                                                edit_uname.getText().clear();
                                                edit_uname.requestFocus();
                                            }
                                        });
                                    }
                                }
                            }catch(JSONException e){
                                Log.e("expn_respns_logn1:",e.toString());
                            }catch(Exception e){
                                Log.e("expn_respns_logn2:",e.toString());
                            }
                            Log.i("modified_response:",String.valueOf(objectLoginResponse));
                        }
                    }
                }
            }
        };
        com.nc.developers.cloudscommunicator.Subcription subcription=new com.nc.developers.cloudscommunicator.Subcription();
        subcription.setObservable(mObservable);
        subcription.setObserver(mObserver);
        com.nc.developers.cloudscommunicator.GlobalClass.setCurrentSubcriberr(subcription);
    }

    private void bindUI(String strKK){
        if(com.nc.developers.cloudscommunicator.GlobalClass.getLoginFragmentSubcriberr()!=null){
            String percentage="";
            if(strKK.equals("emit_login")){
                percentage="percentage#15";
            }
            mObservable= com.nc.developers.cloudscommunicator.GlobalClass.getLoginFragmentSubcriberr().getObservable();
            mObserver= com.nc.developers.cloudscommunicator.GlobalClass.getLoginFragmentSubcriberr().getObserver();
            mObservable.just(percentage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mObserver);
        }
    }

    private boolean validateUsername(String username){
        boolean isUsernameValid=false;
        if(username.contains("@")){
            if(username.contains("@clouzer.com") || username.contains("@clouzersoftwaresolutions.clouzer.com")){
                isUsernameValid=Patterns.EMAIL_ADDRESS.matcher(username).matches();
            }
        }else{
            username+="@clouzer.com";
            isUsernameValid=Patterns.EMAIL_ADDRESS.matcher(username).matches();
        }
        if(isUsernameValid){
            this.username=username;
        }
        return isUsernameValid;
    }

    @Optional
    @OnClick({R.id.btn_login,R.id.txt_signup,R.id.txt_forgot_pwd})
    public void onSelectDeselect(View view){
        //System.out.println("iiiiiiiiiiiiiiii" + view.getId());

        switch (view.getId()) {

            case R.id.btn_login:
                // ReplaceFragment.replaceFragment(LoginFragment.this,R.id.frame_frag_container, new LandingFragment(),true);
                break;
            case R.id.txt_signup:
                ReplaceFragment.replaceFragment(LoginFragment.this,R.id.frame_frag_container, new SignupFragment(),true);
                break;
            case R.id.txt_forgot_pwd:
                isForgotPwd = true;
                ReplaceFragment.replaceFragment(LoginFragment.this,R.id.frame_frag_container, new ForgotPwdFragment(),true);
                break;
        }
    }
}