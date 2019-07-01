package com.cloudsinc.soulmobile.nativechatapplication.fragments.signup;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.FragmentHomeScreen;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.login.LoginFragment;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.sharedprefrences.PrefManager;
import com.nc.developers.cloudscommunicator.socket.JSonObjectClass;
import com.nc.developers.cloudscommunicator.socket.SocketConnection;
import com.nc.developers.cloudscommunicator.socket.SocketConnectionLogin;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.dialogs.CustomDialog;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setQuicksandMediumFont;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setQuicksandRegularFont;

public class OtpVerificationFragment extends Fragment{

    private EditText dgtFirst,dgtSecond,dgtThird,dgtFourth;

    @BindView(R.id.btn_verify)Button btn_verify;
    @BindView(R.id.txt_reset_pwd)TextView txt_reset_pwd;
    @BindView(R.id.txt_resen_otp)TextView txt_resen_otp;
    @BindView(R.id.txt_varification_code)TextView txt_varification_code;
    @BindView(R.id.circularProgressbar)ProgressBar circularProgressbar;
    @BindView(R.id.tv)TextView tv;
    @BindView(R.id.linearSignUpBackground)RelativeLayout signUpBackgroundLayout;
    @BindView(R.id.part1)LinearLayout part1Layout;
    @BindView(R.id.part2)LinearLayout part2Layout;

    private static Context mcontext;

    private Observable<String> mObservable;
    private Observer<String> mObserver;

    private String userId,password;
    private static final String TAG=OtpVerificationFragment.class.getSimpleName();
    public static boolean isMobileSyncCompleted=false;
    private boolean isFragmentVisible=false;
    private static String isRegisterResponseGot,isMobileSyncStarted;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_otp_verification,container,false);
        isRegisterResponseGot=isMobileSyncStarted="no";
        Log.i(TAG+"_onCratViw_rgstrRspnse:",isRegisterResponseGot+" ..kk");
        Log.i(TAG+"_onCratViw_mblSycStrtd:",isMobileSyncStarted+" ..kk");
        ButterKnife.bind(this,view);
        mcontext=view.getContext();
        GlobalClass.setCurrentSubcriberr(null);
        dgtFirst=view.findViewById(R.id.digit_first);
        dgtSecond=view.findViewById(R.id.digit_second);
        dgtThird=view.findViewById(R.id.digit_third);
        dgtFourth=view.findViewById(R.id.digit_fourth);

        txt_reset_pwd.setTypeface(setQuicksandRegularFont(mcontext));
        txt_resen_otp.setTypeface(setQuicksandRegularFont(mcontext));
        txt_varification_code.setTypeface(setQuicksandRegularFont(mcontext));
        dgtFirst.setTypeface(setQuicksandRegularFont(mcontext));
        dgtSecond.setTypeface(setQuicksandRegularFont(mcontext));
        dgtThird.setTypeface(setQuicksandRegularFont(mcontext));
        dgtFourth.setTypeface(setQuicksandRegularFont(mcontext));
        btn_verify.setTypeface(setQuicksandMediumFont(mcontext));

        if(part1Layout!=null && part2Layout!=null){
            part1Layout.setVisibility(View.VISIBLE);
            part2Layout.setVisibility(View.VISIBLE);
        }
        if(circularProgressbar!=null){
            circularProgressbar.setVisibility(View.VISIBLE);
        }

        dgtFirst.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence,int i,int i1,int i2){

            }

            @Override
            public void onTextChanged(CharSequence charSequence,int i,int i1,int i2){
                dgtSecond.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable){

            }
        });

        dgtSecond.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence,int i,int i1,int i2){

            }

            @Override
            public void onTextChanged(CharSequence charSequence,int i,int i1,int i2){
                dgtThird.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable){

            }
        });

        dgtThird.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence,int i,int i1,int i2){

            }

            @Override
            public void onTextChanged(CharSequence charSequence,int i,int i1,int i2){
                dgtFourth.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable){

            }
        });

        setSubcriber();
        btn_verify.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(view!=null && OtpVerificationFragment.mcontext!=null){
                    InputMethodManager imm=(InputMethodManager)
                                OtpVerificationFragment.mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                }
                String firstDigit=dgtFirst.getText().toString();
                String secondDigit=dgtSecond.getText().toString();
                String thirdDigit=dgtThird.getText().toString();
                String fourthDigit=dgtFourth.getText().toString();

                GlobalClass.setLoginFragmentSubcriberr(null);

                if(!firstDigit.isEmpty() && !secondDigit.isEmpty() && !thirdDigit.isEmpty() && !fourthDigit.isEmpty()){
                    if(part1Layout!=null && part2Layout!=null){
                        part1Layout.setVisibility(View.GONE);
                        part2Layout.setVisibility(View.GONE);
                    }
                    if(signUpBackgroundLayout!=null){
                        signUpBackgroundLayout.setVisibility(View.VISIBLE);
                    }
                    GlobalClass.setLoginFragmentSubcriberr(null);
                    circularProgressbar.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                    circularProgressbar.setProgress(2);
                    tv.setText("2%");
                    btn_verify.setEnabled(false);
                    txt_resen_otp.setEnabled(false);
                    String urlLogin= GlobalClass.getLoginUrl();
                    urlLogin+="/login";
                    Log.i("login_url:",urlLogin+" ..url");
                    OtpVerificationFragment.isMobileSyncCompleted=false;
                    //connect to sign up socket
                    SocketConnectionLogin.makeLoginSocketConnection(urlLogin);
                }
            }
        });
        return view;
    }

    @Override
    public void onPause(){
        boolean mobileSyncStatus=OtpVerificationFragment.isMobileSyncCompleted;
        Log.i(TAG+"_onPause_ms*Status:",String.valueOf(mobileSyncStatus)+" ..kk");
        if(!mobileSyncStatus){
            Repository rr=Repository.getRepository();
            if(rr!=null){
                rr.closeAllSockets();
                rr.clearDatabase();
            }
            OtpVerificationFragment.isMobileSyncCompleted=false;
        }
        Log.i(TAG+"_onPause:","paused...");
        isFragmentVisible=false;
        super.onPause();
    }

    @Override
    public void onResume(){
        Log.i(TAG+"_onResume:","resumed...");
        Log.i(TAG+"_onResume_rgstrRspnse:",isRegisterResponseGot+" ..kk");
        Log.i(TAG+"_onResume_mblSycStrtd:",isMobileSyncStarted+" ..kk");
        isFragmentVisible=true;
        Log.i(TAG+"_mobileSyncState:",String.valueOf(OtpVerificationFragment.isMobileSyncCompleted)+" ..kk");
        if(OtpVerificationFragment.isMobileSyncCompleted){
            OtpVerificationFragment.isMobileSyncCompleted=false;
            ReplaceFragment.replaceFragment(OtpVerificationFragment.this,R.id.frame_frag_container,new FragmentHomeScreen(),false);
            circularProgressbar.setVisibility(View.GONE);
            tv.setText("");
            tv.setVisibility(View.GONE);
        }else{
            if(circularProgressbar!=null){
                circularProgressbar.setVisibility(View.GONE);
            }
            if(btn_verify!=null){
                btn_verify.setEnabled(true);
            }
            if(tv!=null){
                tv.setText("");
                tv.setVisibility(View.GONE);
            }
            if(isRegisterResponseGot.equals("yes") && isMobileSyncStarted.equals("yes")){
                resetToDefault();
                ReplaceFragment.replaceFragment(OtpVerificationFragment.this,R.id.frame_frag_container,
                    new LoginFragment(),false);
                Repository rr=Repository.getRepository();
                if(rr!=null){
                    rr.clearDatabase();
                }
            }
        }
        super.onResume();
    }

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
                Log.i(TAG+"_onNxt:",string+" ..kk");
                if(string!=null){
                    if(string.contains("percent")){
                        String percentageString=string;
                        String arr[]=percentageString.split("#");
                        if(arr!=null){
                            if(arr.length>0){
                                final String percentString=arr[1];
                                circularProgressbar.setProgress(Integer.parseInt(percentString));
                                tv.setText(percentString+"%");
                            }
                        }
                    }
                    if(string.equals("socket_login_connect")){
                        Log.i("sign_up_socket:","..connected..");
                        Log.i("signup_socket_conncted:",String.valueOf(GlobalClass.getSocketLogin().connected() + " ..kk"));
                        userId=password="";
                        String firstName=getArguments().getString("firstname");
                        String lastName=getArguments().getString("lastname");
                        password=getArguments().getString("rPassword");
                        GlobalClass.setPassword(password);
                        String recoveryMail=getArguments().getString("recovery_mail");
                        userId=getArguments().getString("mstorm_id");
                        JSONObject signupObject=JSonObjectClass.getJsonSignUpObject(
                                firstName,
                                lastName,
                                userId,
                                password,
                                recoveryMail);
                        Log.i("emit_signup_object:",String.valueOf(signupObject));
                        GlobalClass.getSocketLogin().emit("LOGIN",signupObject);
                    }
                    if(string.equals("response_login")){
                        JSONObject object=GlobalClass.getLoginObject();
                        PrefManager.setValue("signup_shot:**",String.valueOf(object));
                        Log.i("response_sign_up:",String.valueOf(object)+" ..");
                        if(object.has("baton")){
                            object.remove("baton");
                            Log.i("modified_sign_up:",String.valueOf(object));
                        }
                        if(object.has("ACTION_ARRAY")){
                            try{
                                JSONArray arr=object.getJSONArray("ACTION_ARRAY");
                                String actionArr=arr.getString(0);
                                Log.i(TAG+"_actionArr:",actionArr+" ..kk");
                                if(actionArr.equals("REGISTER")){
                                    isRegisterResponseGot="yes";
                                    circularProgressbar.setProgress(20);
                                    tv.setText("20%");
                                    JSONObject syncMapObject=new JSONObject();
                                    String user_Id,jwtToken,userUID,orgId;
                                    user_Id=jwtToken=userUID=orgId="";
                                    if(object.has("syncMap")){
                                        syncMapObject=object.getJSONObject("syncMap");
                                        Log.i(TAG+"_syncMapObj:",String.valueOf(syncMapObject)+" ..kk");
                                        GlobalClass.setSyncMapJSONObject(syncMapObject);
                                        object.remove("syncMap");
                                    }
                                    if(object.has("userKeyVal")){
                                        com.nc.developers.cloudscommunicator.GlobalClass.setUserKeyval(
                                                object.getString("userKeyVal"));
                                    }
                                    if(object.has("myInfo")){
                                        JSONObject objectMyInfo=object.getJSONObject("myInfo");
                                        if(objectMyInfo.has("globalVars")){
                                            objectMyInfo.remove("globalVars");
                                        }
                                        if(objectMyInfo.has("userId")){
                                            user_Id=objectMyInfo.getString("userId");
                                            GlobalClass.setUserId(userId);
                                        }
                                        if(objectMyInfo.has("jwttoken")){
                                            jwtToken=objectMyInfo.getString("jwttoken");
                                        }
                                        if(objectMyInfo.has("userUID")){
                                            userUID=objectMyInfo.getString("userUID");
                                            GlobalClass.setUserUID(userUID);
                                        }
                                        if(objectMyInfo.has("ORG_ID")){
                                            orgId=objectMyInfo.getString("ORG_ID");
                                            GlobalClass.setOrgID(orgId);
                                        }
                                    }
                                    isMobileSyncStarted="yes";
                                    //call authenticate here
                                    GlobalClass.setLoginFragmentSubcriberr(null);
                                    SocketConnection.authenticate(userId,password,user_Id,
                                            jwtToken,
                                            userUID,
                                            orgId,
                                            syncMapObject);
                                }
                                if(actionArr.equals("ERROR")){
                                    isRegisterResponseGot="no";
                                    isMobileSyncStarted="no";
                                    if(object.has("MSG")){
                                        String errorMsg=object.getString("MSG");
                                        if(errorMsg!=null && !TextUtils.isEmpty(errorMsg)){
                                            //display dialog box
                                            CustomDialog.dispDialogConfirmation(getContext(),OtpVerificationFragment.class,
                                                    errorMsg,false);
                                            Log.i("signup_error:",errorMsg);
                                            ReplaceFragment.replaceFragment(OtpVerificationFragment.this,R.id.frame_frag_container,new SignupFragment(),false);
                                        }
                                    }
                                }
                            }catch(JSONException e){
                                Log.e("expn_sgnp_rspns_parse1:",e.toString());
                            }catch(Exception e){
                                Log.e("expn_sgnp_rspns_parse2:",e.toString());
                            }
                        }
                    }

                    if(string.equals("initial_sync_done")){
                        OtpVerificationFragment.isMobileSyncCompleted=true;
                        if(OtpVerificationFragment.isMobileSyncCompleted && isFragmentVisible){
                            circularProgressbar.setProgress(100);
                            tv.setText("100%");
                            ReplaceFragment.replaceFragment(OtpVerificationFragment.this,R.id.frame_frag_container,new FragmentHomeScreen(),false);
                            if(signUpBackgroundLayout!=null){
                                signUpBackgroundLayout.setVisibility(View.GONE);
                            }
                            circularProgressbar.setVisibility(View.GONE);
                            tv.setText("");
                            tv.setVisibility(View.GONE);
                        }
                    }
                }
            }
        };
        com.nc.developers.cloudscommunicator.Subcription subcription=new com.nc.developers.cloudscommunicator.Subcription();
        subcription.setObserver(mObserver);
        subcription.setObservable(mObservable);
        GlobalClass.setSignupSubcription(subcription);
    }

    public static void resetToDefault(){
        OtpVerificationFragment.isMobileSyncStarted=OtpVerificationFragment.isRegisterResponseGot="no";
        OtpVerificationFragment.isMobileSyncCompleted=false;
    }
}