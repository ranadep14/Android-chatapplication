package com.cloudsinc.soulmobile.nativechatapplication.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.cloudsinc.soulmobile.nativechatapplication.App;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.FragmentHomeScreen;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.GroupMemberListFrag;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.login.LoginFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.login.WellcomeFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.signup.OtpVerificationFragment;
import com.cloudsinc.soulmobile.nativechatapplication.interfaces.IOnBackPressed;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ScreenOrientation;
import com.google.android.gms.analytics.Tracker;
import com.nc.developers.cloudscommunicator.utils.CheckInternetConnectionCommunicator;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.Subcription;
import com.nc.developers.cloudscommunicator.models.Login;
import com.nc.developers.cloudscommunicator.sharedprefrences.PrefManager;
import com.nc.developers.cloudscommunicator.socket.JSonObjectClass;
import com.nc.developers.cloudscommunicator.socket.SocketConnectionSync;
import org.json.JSONObject;
import java.io.File;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity{
    //taken from new cvs
    private static final int PERMISSION_REQUEST_CODE = 200;
    private String firstTimeOrNot="",userId="";
    private Observable<String> mObservable;
    private Observer<String> mObserver;
    private Tracker mTracker;
    private static final String TAG=MainActivity.class.getSimpleName();
    private static boolean mainOnCreateStatus=false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        MainActivity.mainOnCreateStatus=true;
        setSubcriber();
        setContentView(R.layout.activity_main);
        com.nc.developers.cloudscommunicator.GlobalClass.setApplication(App.getApplication());
        //setting url here
        //com.nc.developers.cloudscommunicator.GlobalClass.setLoginUrl("https://10.14.15.234:8036");
        //com.nc.developers.cloudscommunicator.GlobalClass.setLoginUrl("https://dev.clouzer.com");
        //com.nc.developers.cloudscommunicator.GlobalClass.setLoginUrl("https://predev.clouzer.com");
        //com.nc.developers.cloudscommuni0cator.GlobalClass.setLoginUrl("https://temp.clouzer.com");
        //com.nc.developers.cloudscommunicator.GlobalClass.setLoginUrl("https://www.clouzer.com");
        com.nc.developers.cloudscommunicator.GlobalClass.setLoginUrl("https://test.clouzer.com");
        //com.nc.developers.cloudscommunicator.GlobalClass.setLoginUrl("https://10.14.16.141:8036");
        //com.nc.developers.cloudscommunicator.GlobalClass.setLoginUrl("https://10.13.15.136:8037");
        //com.nc.developers.cloudscommunicator.GlobalClass.setLoginUrl("https://10.13.15.136:8036");
        //com.nc.developers.cloudscommunicator.GlobalClass.setLoginUrl("https://10.13.15.157:8036"); //bhawana machine
        //com.nc.developers.cloudscommunicator.GlobalClass.setLoginUrl("https://10.13.15.65:8036");

        // here checking device type
        boolean isTablet=ScreenOrientation.checkIsTablet(this);
        com.nc.developers.cloudscommunicator.GlobalClass.setTablet(isTablet);

        //==================================================================================================//
        new PrefManager(this);
        //==================================================================================================//
        requestPermission();
        createDirectory();
        ButterKnife.bind(this);

        App app=App.getApplication();
        mTracker=app.getDefaultTracker();

        firstTimeOrNot=userId="";
        Repository repo=Repository.getRepository();
        if(repo!=null){
            Login login=repo.getLoginData();
            if(login!=null){
                firstTimeOrNot=login.getFirstTimeLoginDoneOrNot();
                userId=login.getUserId();
                Log.i("login_status:",firstTimeOrNot+" ..kk");
                Log.i("userId:",userId+" ..kk");
            }
        }

        if(getIntent().hasExtra("fragment_kk")){
            Log.i(TAG+"_kukuaaStatus:","oho");
            Bundle bundle=new Bundle();
            bundle.putString("call_for","login");
            Fragment fragment=new WellcomeFragment();
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_frag_container, fragment, "fragment");
            fragmentTransaction.commitAllowingStateLoss();
        }
        Log.i(TAG+"_firstTimeOrNot:",firstTimeOrNot+" ..kk");
        if(firstTimeOrNot.equals("yes")){  // yes means first_time login done
            com.nc.developers.cloudscommunicator.GlobalClass.setUserId(userId);
            Log.i("userId_Set:",com.nc.developers.cloudscommunicator.GlobalClass.getUserId()+" ..kk");
            if(CheckInternetConnectionCommunicator.isInternetAvailable()){
                SocketConnectionSync.makeSyncSocketConnection(com.nc.developers.cloudscommunicator.GlobalClass.getLoginUrl()
                        +"/sync");
            }else{
                //below code is necessary to open an app in offline mode
                //app is not getting internet connection then open below screen instead of showing blank screen
                firstTimeOrNot="";
                MainActivity.mainOnCreateStatus=false;
                FragmentManager fragmentManager=getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_frag_container, new FragmentHomeScreen(), "fragment");
                fragmentTransaction.commitAllowingStateLoss();
            }
        }else{
            MainActivity.mainOnCreateStatus=false;
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_frag_container, new WellcomeFragment(), "fragment");
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    private void setSubcriber(){
        GlobalClass.setLoginFragmentSubcriberr(null);
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
                    Log.i("onNxt_MainAct_",string);
                    if(string.equals("socket_sync_connect")){
                        if(com.nc.developers.cloudscommunicator.GlobalClass.getAuthenticatedSyncSocket()!=null){
                            Repository repo=Repository.getRepository();
                            if(repo!=null){
                                Login login=repo.getLoginData();
                                if(login!=null){
                                    String userId=login.getUserId();
                                    String jwtToken=login.getJwtToken();
                                    if(userId!=null && !TextUtils.isEmpty(userId)
                                            && jwtToken!=null && !TextUtils.isEmpty(jwtToken)){
                                        JSONObject authObj= JSonObjectClass.getAuthenticateJsonObject(userId,jwtToken);
                                        com.nc.developers.cloudscommunicator.GlobalClass.getAuthenticatedSyncSocket().emit("authenticate",authObj);
                                    }
                                }
                            }
                        }
                    }
                    if(string.equals("socket_sync_auth_success") && MainActivity.mainOnCreateStatus
                            && firstTimeOrNot.equals("yes")){
                        firstTimeOrNot="";
                        MainActivity.mainOnCreateStatus=false;
                        FragmentManager fragmentManager=getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_frag_container, new FragmentHomeScreen(), "fragment");
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                }
            }
        };
        Subcription subcription=new Subcription();
        subcription.setObservable(mObservable);
        subcription.setObserver(mObserver);
        com.nc.developers.cloudscommunicator.GlobalClass.setMainSubcriberr(subcription);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i("JDDDDDDDDDD","where im.....onresume");
        //mTracker.send(new HitBuilders.ScreenViewBuilder().build()); //temp comment
    }

    @Override
    public void onBackPressed(){
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_frag_container);
        /*if (f instanceof FragmentHomeScreen) {//the fragment on which you want to handle your back press
            Log.i("BACK PRESSED", "BACK PRESSED");
            exitApp();
        }*/
        if ((!(f instanceof IOnBackPressed) || !((IOnBackPressed) f).onBackPressed())
                && !(f instanceof LoginFragment) && !(f instanceof OtpVerificationFragment)){
            Log.i("IonFrag", "BACK PRESSED");
            super.onBackPressed();

            if(ChatListFragment.fromfrgament.equals("contact"))
            {

                Log.d("fromfragment",ChatListFragment.fromfrgament);
                if (getFragmentManager().getBackStackEntryCount() < 0) {

                } else {
                    try {

                        FragmentHomeScreen.lin_cont.performClick();
                        ChatListFragment.fromfrgament="conv";

                    } catch (Exception e) {


                    }
                }


            }
            if (GroupMemberListFrag.isbackpressfromgroup.equals("contact")) {

                if (getFragmentManager().getBackStackEntryCount() < 0) {
                } else {
                    try {
                        FragmentHomeScreen.lin_cont.performClick();
                    } catch (Exception e) {
                    }

                }

            }
        }
        /*else {
            //Log.i("IonFrag", "BACK PRESSED");
            super.onBackPressed();
        }*/
    }

    private void exitApp() {
        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.custom_dialog);

        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.setCanceledOnTouchOutside(false);
        TextView txtmsgstr = (TextView) d.findViewById(R.id.txt_msg);
        txtmsgstr.setText("" + getResources().getString(R.string.app_exit_msg));
        //txtmsgstr.setTypeface(setFontForDialog(mcontext));
        Button btn_ok = (Button) d.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //finishAffinity();
                d.dismiss();
            }
        });
        Button btn_cancle = (Button) d.findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    private void createDirectory() {

        File direct = new File(Environment.getExternalStorageDirectory(), "/internal/CloudsChat/Media/images/");
        if (!direct.exists()) {
            direct.mkdirs();
            Log.e("MakeDirectory", "" + direct.getAbsolutePath());
        }
    }


    public void requestPermission() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 && (
                ContextCompat.checkSelfPermission(

                        this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(

                                this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(

                                this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))

        {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else {
                    //Toast.makeText(this, "Camera Permission NOT Granted!", Toast.LENGTH_SHORT).show();
                }
        }
    }
}