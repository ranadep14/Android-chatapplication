package com.cloudsinc.soulmobile.nativechatapplication.fragments.login;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.dialogs.CustomDialog;
import com.cloudsinc.soulmobile.nativechatapplication.utils.InternetConnectionChecker;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;
import com.github.ybq.android.spinkit.SpinKitView;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.objects.login.JsonObjectLogin;
import com.nc.developers.cloudscommunicator.socket.SocketConnectionLogin;
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
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setQuicksandMediumFont;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setQuicksandRegularFont;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class ForgotPwdFragment extends Fragment {

    private static final String TAG=ForgotPwdFragment.class.getSimpleName();
    private Observable<String> mObservable;
    private Observer<String> mObserver;
    private static final String EMAIL_ID_REGEX="^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private static final String CLOUZER_ID_REGEX="^[A-Za-z@_.][A-Za-z0-9@_.]*$";
    private String userId,recoveryMail;

    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.edit_txt_uname)
    EditText edit_txt_uname;
    @BindView(R.id.edit_p_emailid)
    EditText edit_p_emailid;
    @BindView(R.id.txt_login)
    TextView txt_login;
    @BindView(R.id.txt_recover_pwd)
    TextView txt_recover_pwd;
    @BindView(R.id.progress_login)
    SpinKitView progress_login;
    static Context mcontext;

    public ForgotPwdFragment(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_forgot_pwd, container, false);
        ButterKnife.bind(this, view);
        mcontext = view.getContext();

        txt_recover_pwd.setTypeface(setQuicksandRegularFont(mcontext));
        edit_p_emailid.setTypeface(setQuicksandRegularFont(mcontext));
        edit_txt_uname.setTypeface(setQuicksandRegularFont(mcontext));
        txt_login.setTypeface(setQuicksandRegularFont(mcontext));
        btn_submit.setTypeface(setQuicksandMediumFont(mcontext));

        return view;
    }

    @Optional
    @OnClick({R.id.txt_login,R.id.btn_submit})
    public void onSelectDeselect(View view){
        switch(view.getId()){
            case R.id.txt_login:
                ReplaceFragment.replaceFragment(ForgotPwdFragment.this, R.id.frame_frag_container, new LoginFragment(), true);
                break;
            case R.id.btn_submit:
                GlobalClass.setLoginFragmentSubcriberr(null);
                GlobalClass.setCurrentSubcriberr(null);
                userId=recoveryMail="";
                boolean kk=validateDetails();
                Log.i("kk_variable:",String.valueOf(kk)+" ..kk");
                Log.i(TAG+"_userId:",userId+" ..kk");
                Log.i(TAG+"_recvryMail:",recoveryMail+" ..kk");
                if(kk){
                    setSubcriber();
                    GlobalClass.setLoginFragmentSubcriberr(null);
                    GlobalClass.setCurrentSubcriberr(null);
                    GlobalClass.setSignupSubcription(null);
                    String url=GlobalClass.getLoginUrl();
                    Log.i(TAG+"_urlBhai:",url+" ..kk");
                    if(url!=null){
                        SocketConnectionLogin.makeLoginSocketConnection(url+"/login");
                    }
                }
                break;
        }
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
                if(string!=null){
                    Log.i(TAG+"_onNxt:",string);
                    if(string.equals("socket_login_connect")){
                        Log.i(TAG+"_userId:",userId+" ..kk");
                        JSONObject objectForgetPassword=JsonObjectLogin.getJSONObjectForgetPassword(recoveryMail,userId);
                        Log.i("emit_frgt_pswrd:",String.valueOf(objectForgetPassword)+" ..kk");
                        boolean internetAvailability= InternetConnectionChecker.checkInternetConnection(getContext());
                        Log.i("is internet available:",String.valueOf(internetAvailability)+" ..kk");
                        if(GlobalClass.getSocketLogin()!=null && internetAvailability){
                            Log.i(TAG+"_status:","inside_emit_condition");
                            GlobalClass.getSocketLogin().emit("LOGIN",objectForgetPassword);
                        }else{
                            Log.i(TAG+"_status:","outside_emit_condition");
                        }
                    }
                    if(string.equals("response_login")){
                        JSONObject object=GlobalClass.getLoginObject();
                        if(object!=null){
                            try{
                                if(object.has("ACTION_ARRAY")){
                                    JSONArray actionArr=object.getJSONArray("ACTION_ARRAY");
                                    if(actionArr!=null){
                                        String actionArrString=actionArr.getString(0);
                                        Log.i(TAG+"_actionString:",actionArrString+" ..kk");
                                        if(actionArrString!=null){
                                            if(actionArrString.equals("FORGOT_PASSWORD")){
                                                if(object.has("SUCCESS")){
                                                    boolean successBoolean=object.getBoolean("SUCCESS");
                                                    Log.i(TAG+"_successStatus:",String.valueOf(successBoolean)+" ..kk");
                                                    if(successBoolean){
                                                        CustomDialog.dispDialogConfirmation(mcontext,LoginFragment.class,
                                                                getString(R.string.forget_password),false);
                                                        if(edit_p_emailid!=null){
                                                            edit_p_emailid.setText("");
                                                        }
                                                        if(edit_txt_uname!=null){
                                                            edit_txt_uname.setText("");
                                                            edit_txt_uname.requestFocus();
                                                        }
                                                        ReplaceFragment.replaceFragment(ForgotPwdFragment.this,R.id.frame_frag_container, new LoginFragment(),true);
                                                    }else{
                                                        String falseMessage="";
                                                        if(object.has("MSG")){
                                                            falseMessage=object.getString("MSG");
                                                            Log.i(TAG+"_msg:",falseMessage+" ..kk");
                                                            CustomDialog.dispDialogAlert(mcontext,
                                                                    LoginFragment.class,
                                                                    getString(R.string.forgot_password_wrong_username),
                                                                    false);
                                                            if(edit_p_emailid!=null){
                                                                edit_p_emailid.setText("");
                                                            }
                                                            if(edit_txt_uname!=null){
                                                                edit_txt_uname.setText("");
                                                                edit_txt_uname.requestFocus();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            if(actionArrString.equals("ERROR")){
                                                String message="";
                                                if(object.has("MSG")){
                                                    message=object.getString("MSG");
                                                }
                                                CustomDialog.dispDialogConfirmation(mcontext,LoginFragment.class,
                                                        message,false);
                                                edit_txt_uname.setText("");
                                                edit_p_emailid.setText("");
                                            }
                                        }
                                    }
                                }
                            }catch(JSONException e){
                                Log.e(TAG+"_frgt_pwd_rspns1:",e.toString());
                            }catch(Exception e){
                                Log.e(TAG+"_frgt_pwd_rspns2:",e.toString());
                            }
                        }
                    }
                }
            }
        };
        com.nc.developers.cloudscommunicator.Subcription subcription=new com.nc.developers.cloudscommunicator.Subcription();
        subcription.setObservable(mObservable);
        subcription.setObserver(mObserver);
        com.nc.developers.cloudscommunicator.GlobalClass.setForgetPasswordSubcriberr(subcription);
    }

    private boolean validateDetails(){
        boolean returnValue=false;
        recoveryMail=userId="";
        boolean statusUserName=false,statusPersonalEmailId=false;
        recoveryMail=edit_p_emailid.getText().toString().trim();
        userId=edit_txt_uname.getText().toString().trim();
        String msg="";
        Log.i("userId:",userId+" ..kk");
        Log.i("recoveryMail:",recoveryMail+" ..kk");
        if(!userId.contains("@") && userId.length()>0){
            userId=userId+"@clouzer.com";
        }

        if(userId.length()<=0 && recoveryMail.length()<=0){
            statusPersonalEmailId=false;
            statusUserName=false;
            msg=getString(R.string.empty_username_emailId);
            CustomDialog.dispDialogAlert(mcontext,ForgotPwdFragment.class,
                    msg,false);
        }else if(userId.length()>0 && recoveryMail.length()<=0){
            statusPersonalEmailId=false;
            msg=getString(R.string.empty_personal_mail_id);
            CustomDialog.dispDialogAlert(mcontext,ForgotPwdFragment.class,
                    msg,false);
        }else if(userId.length()<=0 && recoveryMail.length()>0){
            statusUserName=false;
            msg=getString(R.string.empty_mstorm_id);
            CustomDialog.dispDialogAlert(mcontext,ForgotPwdFragment.class,
                    msg,false);
        }else if(userId.length()>0 && recoveryMail.length()>0){
            statusUserName=statusPersonalEmailId=true;
            if(statusUserName && statusPersonalEmailId){
                if(!userId.matches(ForgotPwdFragment.CLOUZER_ID_REGEX)
                        && !recoveryMail.matches(ForgotPwdFragment.EMAIL_ID_REGEX)){
                    statusPersonalEmailId=statusUserName=false;
                    msg=getString(R.string.invalid_username_emailId);
                    CustomDialog.dispDialogAlert(mcontext,ForgotPwdFragment.class,
                            msg,false);
                    edit_txt_uname.requestFocus();
                }else if(!userId.matches(ForgotPwdFragment.CLOUZER_ID_REGEX)
                        && recoveryMail.matches(ForgotPwdFragment.EMAIL_ID_REGEX)){
                    statusUserName=false;
                    msg=getString(R.string.invalid_mstorm_id);
                    CustomDialog.dispDialogAlert(mcontext,ForgotPwdFragment.class,
                            msg,false);
                    edit_txt_uname.requestFocus();
                }else if(!recoveryMail.matches(ForgotPwdFragment.EMAIL_ID_REGEX)
                        && userId.matches(ForgotPwdFragment.CLOUZER_ID_REGEX)){
                    statusPersonalEmailId=false;
                    msg=getString(R.string.invalid_personal_mail_id);
                    CustomDialog.dispDialogAlert(mcontext,ForgotPwdFragment.class,
                            msg,false);
                    edit_p_emailid.requestFocus();
                }else if(userId.matches(ForgotPwdFragment.CLOUZER_ID_REGEX) &&
                        recoveryMail.matches(ForgotPwdFragment.EMAIL_ID_REGEX)){
                    statusUserName=statusPersonalEmailId=true;
                }
            }
        }
        if(statusPersonalEmailId && statusUserName){
            returnValue=true;
        }
        return returnValue;
    }
}