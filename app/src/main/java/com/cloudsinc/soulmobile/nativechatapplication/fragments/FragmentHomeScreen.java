package com.cloudsinc.soulmobile.nativechatapplication.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.cloudsinc.soulmobile.nativechatapplication.App;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.activities.MainActivity;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.ContactsListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.ConversationListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.agenda.AgendaFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.GroupMemberListFrag;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactLandingFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.ConversationLandingFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.ConversationListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.login.LoginFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.setting.SettingFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.signup.OtpVerificationFragment;
import com.cloudsinc.soulmobile.nativechatapplication.interfaces.IOnBackPressed;
import com.cloudsinc.soulmobile.nativechatapplication.utils.CircleTransform;
import com.cloudsinc.soulmobile.nativechatapplication.utils.InternetConnectionChecker;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;
import com.cloudsinc.soulmobile.nativechatapplication.utils.common;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.Subcription;
import com.nc.developers.cloudscommunicator.models.Conversation;
import com.nc.developers.cloudscommunicator.models.Login;
import java.io.File;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.MailListAdapter.templst;
import static com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactsSearchFragment.isSearchContac;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setRobotoRegularFont;

/**
 * This Fragment is used to display and Manage drawer Layout Functionality.
 *
 * @author Prajakta Patil
 * @version 1.0
 * @since 29.10.2018
 */

public class FragmentHomeScreen extends Fragment implements IOnBackPressed {
    public static boolean isInbox = true;
    public static boolean isAppExit = true;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;
    @BindView(R.id.lin_agenda)
    LinearLayout lin_agenda;
    @BindView(R.id.lin_settings)
    LinearLayout lin_settings;
    @BindView(R.id.lin_logout)
    LinearLayout lin_logout;
    @BindView(R.id.lin_inbox)
    LinearLayout lin_inbox;
    @BindView(R.id.lin_drawer_container)
    LinearLayout lin_drawer_container;
    @BindView(R.id.img_user_profile)
    ImageView img_user_profile;
    @BindView(R.id.txt_user_name)
    TextView txt_user_name;
    @BindView(R.id.txt_user_mail_id)
    TextView txt_user_mail_id;
    @BindView(R.id.img_search)
    ImageView img_search;
    @BindView(R.id.txt_action_title)
    TextView txt_action_title;
    @BindView(R.id.option_menu_home)
    ImageView option_menu_home;
    @BindView(R.id.prg_optionmenu)
    ProgressBar prg_optionmenu;

    @BindView(R.id.img_inbox)
    ImageView img_inbox;
    @BindView(R.id.txt_inbox)
    TextView txt_inbox;
    @BindView(R.id.img_contact)
    ImageView img_contact;
    @BindView(R.id.txt_contact)
    TextView txt_contact;
    @BindView(R.id.img_agenda)
    ImageView img_agenda;
    @BindView(R.id.txt_agenda)
    TextView txt_agenda;
    @BindView(R.id.img_setting)
    ImageView img_setting;
    @BindView(R.id.txt_setting)
    TextView txt_setting;
    @BindView(R.id.txt_logout)
    TextView txt_logout;

    private Observable<String> mobservable;
    private Observer<String> myObserver;
    Uri photoURI = null;

    private static final int GALLERY_PERMISSION_REQUEST_CODE = 250;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 150;
    static RelativeLayout lin_main_actionbar;
    public static LinearLayout lin_cont;
    private Uri ImgProfileUri, ImgProfileUri1;
    String mediaPath;
    private Thread threadLogout;
    private Runnable runnableLogout;
    Repository repo = Repository.getRepository();

    String url1="";
    String TAG=this.getClass().getSimpleName();
    String url=com.nc.developers.cloudscommunicator.GlobalClass.getLoginUrl();
    String image_path = "";
    int flag=0;
    public PopupMenu popup;
    public static boolean isOptionmenudisable=false;

    public static FragmentHomeScreen newInstance() {
        FragmentHomeScreen fragment = new FragmentHomeScreen();
        return fragment;
    }

    Context mcontext;
    View v;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        v = inflater.inflate(R.layout.fragment_home_screen, container, false);
        isSearchContac = false;
        //isRequestcontact=false;
        if (getArguments() != null) {
            if (getArguments().getString("call_from").equals("search")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txt_action_title.setText(R.string.contacts_text);
                        setOptionBack();
                        img_search.setVisibility(View.VISIBLE);
                        lin_cont.setBackgroundResource(R.color.colorNavSelectedBg);
                        img_contact.setImageResource(R.drawable.ic_contacts);
                        txt_contact.setTextColor(getResources().getColor(R.color.colorNavSelectedText));
                        ConversationListFragment.isConversationSearch = false;
                        ContactListFragment.isContactSearch = true;
                        ReplaceFragment.replaceFragment(FragmentHomeScreen.this, R.id.frm_content_fram, ContactLandingFragment.newInstance(), false);
                        //actionTabsVis();
                    }
                }, 300);
            } else {
                ReplaceFragment.replaceFragment(FragmentHomeScreen.this, R.id.frm_content_fram, ConversationLandingFragment.newInstance(), true);

            }
        } else {
            if (GroupMemberListFrag.isbackpressfromgroup.equals("contact")) {

                ReplaceFragment.replaceFragment(FragmentHomeScreen.this, R.id.frm_content_fram, ContactLandingFragment.newInstance(), true);

                GroupMemberListFrag.isbackpressfromgroup = "";


            } else {


                if (ChatListFragment.fromfrgament.equals("contact")) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            txt_action_title.setText(R.string.contacts_text);
                            setOptionBack();
                            img_search.setVisibility(View.VISIBLE);
                            lin_cont.setBackgroundResource(R.color.colorNavSelectedBg);
                            img_contact.setImageResource(R.drawable.ic_contacts);
                            txt_contact.setTextColor(getResources().getColor(R.color.colorNavSelectedText));
                            ConversationListFragment.isConversationSearch = false;
                            ContactListFragment.isContactSearch = true;
                            ReplaceFragment.replaceFragment(FragmentHomeScreen.this, R.id.frm_content_fram, ContactLandingFragment.newInstance(), false);
                            //actionTabsVis();
                        }
                    }, 300);
                } else {
                    ReplaceFragment.replaceFragment(FragmentHomeScreen.this, R.id.frm_content_fram, ConversationLandingFragment.newInstance(), true);

                }
            }
        }
        mcontext = v.getContext();
        ButterKnife.bind(this, v);
        lin_inbox.setBackgroundResource(R.color.colorNavSelectedBg);
        lin_main_actionbar = (RelativeLayout) v.findViewById(R.id.lin_main_actionbar);
        lin_cont = (LinearLayout) v.findViewById(R.id.lin_contacts);
        lin_cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer_layout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);

                    }
                });

                drawer_layout.closeDrawer(lin_drawer_container);
                isOptionmenudisable=false;
                option_menu_home.setVisibility(View.VISIBLE);
                isInbox = false;
                isAppExit = false;
                txt_action_title.setText(R.string.contacts_text);
                setOptionBack();
                //lin_contacts.setBackgroundResource(R.color.colorNavSelectedBg);
                img_search.setVisibility(View.VISIBLE);
                lin_cont.setBackgroundResource(R.color.colorNavSelectedBg);
                img_contact.setImageResource(R.drawable.ic_contacts1);
                txt_contact.setTextColor(getResources().getColor(R.color.colorNavSelectedText));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        ConversationListFragment.isConversationSearch = false;
                        ContactListFragment.isContactSearch = true;
                        ReplaceFragment.replaceFragment(FragmentHomeScreen.this, R.id.frm_content_fram, ContactLandingFragment.newInstance(), false);
                    }
                }, 300);

            }
        });
        txt_inbox.setTypeface(setRobotoRegularFont(mcontext));
        txt_user_name.setTypeface(setRobotoRegularFont(mcontext));
        txt_user_mail_id.setTypeface(setRobotoRegularFont(mcontext));
        txt_inbox.setTypeface(setRobotoRegularFont(mcontext));
        txt_contact.setTypeface(setRobotoRegularFont(mcontext));
        txt_agenda.setTypeface(setRobotoRegularFont(mcontext));
        txt_setting.setTypeface(setRobotoRegularFont(mcontext));
        txt_logout.setTypeface(setRobotoRegularFont(mcontext));
        setUserProfile();

        img_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer_layout.closeDrawer(GravityCompat.START);
                isInbox = false;
                isAppExit = false;
                txt_action_title.setText(R.string.setting_text);
                setOptionBack();
                isOptionmenudisable=true;
                option_menu_home.setVisibility(View.GONE);
                img_search.setVisibility(View.GONE);
                lin_settings.setBackgroundResource(R.color.colorNavSelectedBg);
                img_setting.setImageResource(R.drawable.ic_settings1);
                txt_setting.setTextColor(getResources().getColor(R.color.colorNavSelectedText));
                ReplaceFragment.replaceFragment(FragmentHomeScreen.this, R.id.frm_content_fram, SettingFragment.newInstance(), false);

            }
        });

        drawer_layout.closeDrawer(GravityCompat.START);
        txt_action_title.setText(R.string.inbox_text);


        setSubcriber();

        return v;
    }

    public void removefile()
    {
        File dir = new File(Environment.getExternalStorageDirectory()+"/ChatApp");
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }

        getActivity().finish();
        startActivity(new Intent(mcontext, MainActivity.class));
    }



    @Override
    public void onResume() {
        super.onResume();
    }


    private void MyOptionMenu() {
        String isArc = ConversationListFragment.isArchive ? "Unarchive Chat" : "Archive Chat";

        popup = new PopupMenu(getActivity(), option_menu_home);
        popup.getMenuInflater().inflate(R.menu.main, popup.getMenu());
        popup.getMenu().findItem(R.id.item1).setTitle(Html.fromHtml("<font color='#000000'>New Group</font>"));
        //popup.getMenu().findItem(R.id.item2).setTitle(Html.fromHtml("<font color='#000000'>Archive Chat</font>"));
        popup.getMenu().findItem(R.id.item2).setTitle(Html.fromHtml("<font color='#000000'>" + isArc + "</font>"));
        popup.getMenu().findItem(R.id.item3).setTitle(Html.fromHtml("<font color='#000000'>Settings </font>"));
        popup.getMenu().findItem(R.id.item4).setTitle(Html.fromHtml("<font color='#000000'>New Messages</font>"));
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                        break;
                    case R.id.item2:
                        try {
                            boolean internetAvailability = InternetConnectionChecker.checkInternetConnection(mcontext);
                            Log.i("is internet available:", String.valueOf(internetAvailability) + " ..kk");
                            option_menu_home.setVisibility(View.GONE);
                            prg_optionmenu.setVisibility(View.VISIBLE);

                            if (ConversationListFragment.isArchive) {
                                ConversationListFragment.isArchive = false;
                                popup.getMenu().findItem(R.id.item2).setTitle(Html.fromHtml("<font color='#000000'>Unarchive Chat</font>"));
                            } else {
                                ConversationListFragment.isArchive = true;
                                popup.getMenu().findItem(R.id.item2).setTitle(Html.fromHtml("<font color='#000000'>Archive Chat</font>"));
                            }

                            Conversation conversation=new Conversation();
                            conversation.getJSONObjectFetchAllConversations("archive","");

                           /* if (GlobalClass.getAuthenticatedSyncSocket() != null && internetAvailability) {
                                JSONObject jsonobj_archive = JsonObjectConversation.getJSONObjectFetchAllConversations("archive","");
                                Log.d("emit_fetcharchiveList", String.valueOf(jsonobj_archive) + " ..");
                                GlobalClass.getAuthenticatedSyncSocket().emit("OnDemandCall", jsonobj_archive);
                            }*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    @OnClick(R.id.img_drawer_open_close)
    public void img_drawer_open_close() {
        drawer_layout.openDrawer(Gravity.LEFT);
    }

    @Optional
    @OnClick({R.id.lin_logout, R.id.lin_agenda, R.id.lin_settings/*, R.id.lin_contacts*/, R.id.lin_inbox, R.id.option_menu_home, R.id.img_search, R.id.lin_drawer_container})
    public void onClick(View view) {
        Fragment mfragment = null;
        String clickon = "";
        switch (view.getId()) {
            case R.id.lin_logout:
                clickon = "logout";
                final Dialog d = new Dialog(mcontext);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.custom_dialog);

                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.setCanceledOnTouchOutside(false);
                TextView txtmsgstr = (TextView) d.findViewById(R.id.txt_msg);
                txtmsgstr.setText("" + mcontext.getResources().getString(R.string.logout_que));
                Button btn_ok = (Button) d.findViewById(R.id.btn_ok);
                btn_ok.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        try{
                            runnableLogout=new Runnable(){
                                @Override
                                public void run(){
                                    Repository repo=Repository.getRepository();
                                    if(repo!=null){
                                        repo.clearDatabase();
                                        if(GlobalClass.getAuthenticatedSyncSocket()!=null){
                                            GlobalClass.getAuthenticatedSyncSocket().disconnect();
                                            GlobalClass.setAuthenticatedSyncSocket(null);
                                        }
                                    }
                                    OtpVerificationFragment.resetToDefault();
                                    LoginFragment.isMobileSyncCompleted=false;
                                    Log.i(TAG+"_login_ms*status:",String.valueOf(LoginFragment.isMobileSyncCompleted)+" ..kk");
                                }
                            };
                            threadLogout=new Thread(runnableLogout);
                            threadLogout.start();
                        }catch(Exception e){
                            Log.e(TAG+"_expn_logout:",e.toString());
                        }finally{
                            if(threadLogout!=null){
                                if(!threadLogout.isInterrupted()){
                                    threadLogout.interrupt();
                                }
                                threadLogout=null;
                            }
                        }

                        new Thread(new Runnable(){
                            @Override
                            public void run(){
                                Glide.get(getActivity()).clearDiskCache();
                            }
                        }).start();

                        ChatListFragment.fromChat=false;
                        ChatListFragment.fromfrgament="";
                        GroupMemberListFrag.isbackpressOntoolbar=false;
                        GroupMemberListFrag.isbackpressfromgroup="";
                        App.getSearchSelectedUserArr().clear();
                        templst.clear();
                        d.dismiss();
                        removefile();
                    }
                });
                Button btn_cancle=(Button)d.findViewById(R.id.btn_cancle);
                btn_cancle.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        d.dismiss();
                    }
                });
                d.show();
                break;

            case R.id.lin_agenda:
                isOptionmenudisable=true;
                option_menu_home.setVisibility(View.GONE);
                clickon = "agenda";
                isInbox = false;
                isAppExit = false;
                txt_action_title.setText(R.string.agenda_text);
                img_search.setVisibility(View.GONE);
                setOptionBack();
                lin_agenda.setBackgroundResource(R.color.colorNavSelectedBg);
                img_agenda.setImageResource(R.drawable.ic_agenda1);
                txt_agenda.setTextColor(getResources().getColor(R.color.colorNavSelectedText));
                ReplaceFragment.replaceFragment(FragmentHomeScreen.this, R.id.frm_content_fram, AgendaFragment.newInstance(), false);

                break;
            case R.id.lin_settings:
                clickon = "setting";
                isOptionmenudisable=true;
                option_menu_home.setVisibility(View.GONE);
                isInbox = false;
                isAppExit = false;
                txt_action_title.setText(R.string.setting_text);
                setOptionBack();
                img_search.setVisibility(View.GONE);
                lin_settings.setBackgroundResource(R.color.colorNavSelectedBg);
                img_setting.setImageResource(R.drawable.ic_settings1);
                txt_setting.setTextColor(getResources().getColor(R.color.colorNavSelectedText));
                ReplaceFragment.replaceFragment(FragmentHomeScreen.this, R.id.frm_content_fram, SettingFragment.newInstance(), false);
                break;
            case R.id.lin_inbox:
                clickon = "inbox";
                isInbox = true;
                isOptionmenudisable=false;
                option_menu_home.setVisibility(View.VISIBLE);

                isAppExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txt_action_title.setText(R.string.inbox_text);
                        setOptionBack();
                        img_search.setVisibility(View.VISIBLE);
                        lin_inbox.setBackgroundResource(R.color.colorNavSelectedBg);
                        img_inbox.setImageResource(R.drawable.ic_inbox1);
                        txt_inbox.setTextColor(getResources().getColor(R.color.colorNavSelectedText));

                        ConversationListFragment.isConversationSearch = true;
                        ContactListFragment.isContactSearch = false;
                        ReplaceFragment.replaceFragment(FragmentHomeScreen.this, R.id.frm_content_fram, ConversationLandingFragment.newInstance(), false);
                    }
                }, 300);
                break;

            case R.id.option_menu_home:
                //showMenu(view);
                if(isInbox){
                    MyOptionMenu();
                }
                break;
            case R.id.img_search:
                clickon = "img_search";
                if ((ConversationListFragment.isConversationSearch) && !(ContactListFragment.isContactSearch)) {
                    ConversationListFragment.isConversationSearchVisible = true;
                    FragmentHomeScreen.actinBarVisibility(false);
                    ConversationLandingFragment.conTabsVisibility(false);
                    ConversationListFragment.conSerchBarVisiblity(true);
                }
                if ((ContactListFragment.isContactSearch) && !(ConversationListFragment.isConversationSearch)) {
                    ContactListFragment.isContacSearchVisible = true;
                    FragmentHomeScreen.actinBarVisibility(false);
                    ContactLandingFragment.contactTabsVisibility(false);
                    ContactListFragment.contactSerchBarVisiblity(true);
                }
                break;
            case R.id.lin_drawer_container:
                Log.d("DrawerClick", "DrawerClick");
                break;
        }
        drawer_layout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                common.hideKeyboard(getActivity());
            }
        });
        drawer_layout.closeDrawer(lin_drawer_container);
    }

    private void setOptionBack() {

        lin_agenda.setBackgroundResource(R.color.colorTransparent);
        lin_cont.setBackgroundResource(R.color.colorTransparent);
        lin_settings.setBackgroundResource(R.color.colorTransparent);
        lin_logout.setBackgroundResource(R.color.colorTransparent);
        lin_inbox.setBackgroundResource(R.color.colorTransparent);

        img_inbox.setImageResource(R.drawable.ic_inbox);
        txt_inbox.setTextColor(getResources().getColor(R.color.colorBlack));
        img_contact.setImageResource(R.drawable.ic_contacts);
        ;
        txt_contact.setTextColor(getResources().getColor(R.color.colorBlack));
        img_agenda.setImageResource(R.drawable.ic_agenda);
        ;
        txt_agenda.setTextColor(getResources().getColor(R.color.colorBlack));
        img_setting.setImageResource(R.drawable.ic_settings);
        ;
        txt_setting.setTextColor(getResources().getColor(R.color.colorBlack));
    }

    private void setUserProfile() {
        String uName = "";
        String uMailId = "";
        String uProfileImg = "";

        Repository repo = Repository.getRepository();
        Login login = repo.getLoginData();
        if (login != null) {
            uName = login.getFirstname() + " " + login.getLastname();
            uMailId = login.getUserEmail();
            Log.i("user_login:", uMailId + " ..kk");
            String imagePath = login.getImagePath();
            Log.d("LoginIMagepic", ""+imagePath);
            if (imagePath != null && !TextUtils.isEmpty(imagePath)) {
                Log.i("imagePth:", imagePath + "..kk");
                uProfileImg = imagePath;
            }
        }

        txt_user_name.setText(uName);
        txt_user_mail_id.setText(uMailId);
        String strimagepath = GlobalClass.getLoginUrl() + "/" + uProfileImg;
        Log.d("strimagepathhomeIS:", strimagepath);
        Glide.with(mcontext).
                load(GlobalClass.getLoginUrl() + "/" + uProfileImg)
                .error(R.drawable.ic_user)
                .placeholder(R.drawable.ic_user)
                .transform(new CircleTransform(mcontext))
                .into(img_user_profile);
    }


    private void setSubcriber() {
        mobservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        sub.onNext("");
                        sub.onCompleted();
                    }
                }
        );
        myObserver = new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String string) {

                if (string.equals("update_user_profile")) {

                    setUserProfile();

                   // Toast.makeText(getActivity(), "Profile Update Successfully", Toast.LENGTH_SHORT).show();

                }

                if(string.equals("CHAT_FILTER_SERVER")){
                    if(isOptionmenudisable==false){
                        option_menu_home.setVisibility(View.VISIBLE);
                        prg_optionmenu.setVisibility(View.GONE);


                    }else{
                        option_menu_home.setVisibility(View.GONE);


                    }



                }
            }
        };
        Subcription s = new Subcription();
        s.setObservable(mobservable);
        s.setObserver(myObserver);
        GlobalClass.setLoginFragmentSubcriberr(s);
    }

    public static void actionTabsVis() {
        if ((ConversationListFragment.isConversationSearch) && !(ContactListFragment.isContactSearch)) {
            FragmentHomeScreen.actinBarVisibility(true);
            ConversationLandingFragment.conTabsVisibility(true);
            ConversationListFragment.conSerchBarVisiblity(false);
        }
        if ((ContactListFragment.isContactSearch) && !(ConversationListFragment.isConversationSearch)) {
            FragmentHomeScreen.actinBarVisibility(true);
            ContactLandingFragment.contactTabsVisibility(true);
            ContactListFragment.contactSerchBarVisiblity(false);
        }
    }

    public static void actinBarVisibility(boolean b) {
        lin_main_actionbar.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    private void exitApp() {
        final Dialog d = new Dialog(mcontext);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.custom_dialog);

        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.setCanceledOnTouchOutside(false);
        TextView txtmsgstr = (TextView) d.findViewById(R.id.txt_msg);
        txtmsgstr.setText("" + getResources().getString(R.string.app_exit_msg));
        Button btn_ok = (Button) d.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mcontext).finish();
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

    @Override
    public boolean onBackPressed() {
        ConversationListAdapter.selectedCount=0;
        ContactsListAdapter.selectedCount=0;
        if (ConversationListFragment.isConversationSearchVisible) {
            if ((ConversationListFragment.isConversationSearch) && !(ContactListFragment.isContactSearch)) {
                isAppExit = false;
                ConversationListFragment.isConversationSearchVisible = false;
                FragmentHomeScreen.actinBarVisibility(true);
                ConversationLandingFragment.conTabsVisibility(true);
                ConversationListFragment.conSerchBarVisiblity(false);
                Log.d("conversationback", "conversationback");
            }

        }
        if (ContactListFragment.isContacSearchVisible) {
            common.hideKeyboard(getActivity());
            FragmentHomeScreen.actinBarVisibility(true);
            ContactLandingFragment.contactTabsVisibility(true);
            ContactListFragment.contactSerchBarVisiblity(false);
            isInbox = true;
            ContactListFragment.edit_search.setText("");
            Log.d("contactback", "contactback");


        }
        if (isAppExit) {
            exitApp();
        }
        if (isInbox) {
            if (ContactListFragment.isContacSearchVisible) {
                ContactListFragment.isContacSearchVisible = false;
            } else {
                lin_inbox.performClick();
            }
        }
        return isInbox;
    }


}