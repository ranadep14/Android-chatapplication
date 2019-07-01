package com.cloudsinc.soulmobile.nativechatapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.dialogs.CustomDialog;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.objects.login.JsonObjectLogin;
import com.nc.developers.cloudscommunicator.socket.SocketConnectionLogin;
import com.nc.developers.cloudscommunicator.utils.CheckInternetConnectionCommunicator;

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
public class ChangePasswordActivity extends AppCompatActivity{
    public ChangePasswordActivity() {
        // Required empty public constructor
    }
    @BindView(R.id.edit_new_pwd)EditText edit_new_pwd;
    @BindView(R.id.edit_re_enter_pwd)EditText edit_re_enter_pwd;
    @BindView(R.id.txt_reset_pwd)TextView txt_reset_pwd;
    @BindView(R.id.btn_reset)Button btn_reset;
    @BindView(R.id.Hidepassnewpwd)ImageView Hidepassnewpwd;
    @BindView(R.id.hidepassconfirm)ImageView hidepassconfirm;
    @BindView(R.id.txt_backto_login)TextView txt_backto_login;


    private  final String TAG= getClass().getSimpleName();
    private Observable<String> mObservable;
    private Observer<String> mObserver;
    private String encodedQuery,url;

    static Context mcontext;
    private boolean passwordShown = false;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        mcontext=this;
        txt_reset_pwd.setTypeface(setQuicksandRegularFont(mcontext));
        edit_new_pwd.setTypeface(setQuicksandRegularFont(mcontext));
        edit_re_enter_pwd.setTypeface(setQuicksandRegularFont(mcontext));
        btn_reset.setTypeface(setQuicksandMediumFont(mcontext));

        try{
            encodedQuery=url="";
            Intent intent=getIntent();
            Uri data=intent.getData();
            String receivedData=data.getEncodedPath();
            encodedQuery=data.getEncodedQuery();
            String encodedUserInfo=data.getEncodedUserInfo();
            String path=data.getPath();
            //String securityPassword=data.getQueryParameter("forgotPassword");
            String securityPassword="";
            url=data.getHost();
            if(receivedData!=null){
                Log.i(TAG+"_rcved_data:",receivedData+" ..kk");
            }
            if(encodedQuery!=null){
                Log.i(TAG+"_encoded_qry:",encodedQuery+" ..kk");
            }
            if(encodedUserInfo!=null){
                Log.i(TAG+"_encoded_usr_info:",encodedUserInfo+" ..kk");
            }
            if(path!=null){
                Log.i(TAG+"_path:",path+" ..kk");
            }
            if(securityPassword!=null){
                Log.i(TAG+"_sec._pswrd:",securityPassword+" ..kk");
            }
            if(url!=null){
                Log.i(TAG+"_url:",url+" ..kk");
            }
            String coockies= CookieManager.getInstance().getCookie(url);
            if(coockies!=null){
                Log.i(TAG+"_coockiesInfo:",coockies+" ..kk");
            }
        }catch(Exception e){
            Log.e(TAG+"_expn1:",e.toString());
        }

        btn_reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String password=edit_new_pwd.getText().toString().trim();
                String confirmPassword=edit_re_enter_pwd.getText().toString().trim();
                if(password!=null && confirmPassword!=null){
                    if(password.length()>0 && confirmPassword.length()>0){
                        if(password.equals(confirmPassword)){
                            if(password.length()<2){
                                String message=getString(R.string.password_short);
                                CustomDialog.dispDialogConfirmation(mcontext,ChangePasswordActivity.class,
                                        message,false);
                            }else if(password.length()>13){
                                String message=getString(R.string.password_long);
                                CustomDialog.dispDialogConfirmation(mcontext,ChangePasswordActivity.class,
                                        message,false);
                            }else{
                                setSubcriber();
                                String nowUrl="https://"+url+"/login";
                                Log.i(TAG+"_server_url:",nowUrl+" ..kk");
                                SocketConnectionLogin.makeLoginSocketConnection(nowUrl);
                            }
                        }else{
                            String message=getString(R.string.repeat_password_not_match);
                            CustomDialog.dispDialogConfirmation(mcontext,ChangePasswordActivity.class,
                                    message,false);
                        }
                    }else if(password.length()>0 && confirmPassword.length()<=0){
                        String message=getString(R.string.empty_repeat_password);
                        CustomDialog.dispDialogConfirmation(mcontext,ChangePasswordActivity.class,
                                message,false);
                    }else if(password.length()<=0 && confirmPassword.length()>0){
                        String message=getString(R.string.empty_password);
                        CustomDialog.dispDialogConfirmation(mcontext,ChangePasswordActivity.class,
                                message,false);
                    }else if(password.length()<=0 && confirmPassword.length()<=0){
                        String message=getString(R.string.empty_password_resetPassword);
                        CustomDialog.dispDialogConfirmation(mcontext,ChangePasswordActivity.class,
                                message,false);
                    }
                }else{
                    String message=getString(R.string.empty_password_resetPassword);
                    CustomDialog.dispDialogConfirmation(mcontext,ChangePasswordActivity.class,
                            message,false);
                }
            }
        });

        txt_backto_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent openLoginFragmentIntent=new Intent(ChangePasswordActivity.this,
                        MainActivity.class);
                openLoginFragmentIntent.putExtra("fragment_kk","loginFragment");
                startActivity(openLoginFragmentIntent);
            }
        });
    }
    private void setSubcriber(){
        mObservable= Observable.create(new Observable.OnSubscribe<String>(){
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
                        String password,confirmPassword;
                        password=confirmPassword="";
                        password=edit_new_pwd.getText().toString().trim();
                        confirmPassword=edit_re_enter_pwd.getText().toString().trim();
                        String encodedQueryArr[]=encodedQuery.split("&");
                        String userId=encodedQueryArr[0].substring(encodedQueryArr[0].indexOf("=")+1);
                        String pwd=encodedQueryArr[1].substring(encodedQueryArr[1].indexOf("=")+1);
                        if(userId!=null && pwd!=null && password.equals(confirmPassword)){
                            JSONObject restPasswordObj= JsonObjectLogin.getJSONObjectChangePassword(userId,confirmPassword,pwd);
                            boolean internetAvailability= CheckInternetConnectionCommunicator.isInternetAvailable();
                            Log.i(TAG+"_is internet available:",String.valueOf(internetAvailability)+" ..kk");
                            Log.i(TAG+"_emit_rst_pswrd:",String.valueOf(restPasswordObj)+" ..kk");
                            if(GlobalClass.getSocketLogin()!=null && internetAvailability){
                                Log.i(TAG+"_emit_rst_pswrd:",String.valueOf(restPasswordObj)+" ..kk");
                                GlobalClass.getSocketLogin().emit("LOGIN",restPasswordObj);
                            }
                        }else if(password.length()<=0 && confirmPassword.length()<=0){
                            String message=getString(R.string.empty_password_resetPassword);
                            CustomDialog.dispDialogConfirmation(mcontext,ChangePasswordActivity.class,
                                    message,false);
                        }else{
                            String message=getString(R.string.empty_password_resetPassword);
                            CustomDialog.dispDialogConfirmation(mcontext,ChangePasswordActivity.class,
                                    message,false);
                        }
                    }
                    if(string.equals("response_login")){
                        JSONObject loginObj=GlobalClass.getLoginObject();
                        boolean successStatus=false;
                        String msgStatus="";
                        if(loginObj!=null){
                            try{
                                if(loginObj.has("SUCCESS")){
                                    successStatus=loginObj.getBoolean("SUCCESS");
                                }
                                if(loginObj.has("MSG")){
                                    msgStatus=loginObj.getString("MSG");
                                }
                            }catch(JSONException e){
                                Log.e(TAG+"_rstPwd1:",e.toString());
                            }catch(Exception e){
                                Log.e(TAG+"_rstPwd2:",e.toString());
                            }
                        }

                        if(successStatus && msgStatus.equals("SUCCESS")){
                            edit_new_pwd.setText("");
                            edit_re_enter_pwd.setText("");
                            btn_reset.setEnabled(false);
                            String message=getString(R.string.password_reset_done);
                            CustomDialog.dispDialogConfirmation(mcontext,ChangePasswordActivity.class,
                                    message,false);
                        }
                    }
                }
            }
        };
        com.nc.developers.cloudscommunicator.Subcription subcription=new com.nc.developers.cloudscommunicator.Subcription();
        subcription.setObservable(mObservable);
        subcription.setObserver(mObserver);
        GlobalClass.setForgetPasswordSubcriberr(subcription);
    }
    @Optional
    @OnClick({R.id.Hidepassnewpwd,R.id.hidepassconfirm})
    public void onSelectDeselect(View view) {
        switch (view.getId()) {
            case R.id.Hidepassnewpwd:
                if(passwordShown) {
                    passwordShown = false;
                    Hidepassnewpwd.setBackgroundResource(R.drawable.ic_show_pwd);
                    edit_new_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    passwordShown = true;
                    Hidepassnewpwd.setBackgroundResource(R.drawable.ic_hide_pass);
                    edit_new_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;

            case R.id.hidepassconfirm:

                if(passwordShown) {
                    passwordShown = false;
                    hidepassconfirm.setBackgroundResource(R.drawable.ic_show_pwd);
                    edit_re_enter_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    passwordShown = true;
                    hidepassconfirm.setBackgroundResource(R.drawable.ic_hide_pass);
                    edit_re_enter_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

                break;
        }
    }
}