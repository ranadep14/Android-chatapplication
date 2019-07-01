package com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudsinc.soulmobile.nativechatapplication.adapters.AllGallaryListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.GallaryListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.dialogs.TabbedDialog;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.GroupMemberListFrag;
import com.cloudsinc.soulmobile.nativechatapplication.interfaces.IOnBackPressed;
import com.cloudsinc.soulmobile.nativechatapplication.utils.InternetConnectionChecker;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;
import com.nc.developers.cloudscommunicator.Subcription;
import com.bumptech.glide.Glide;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.FragmentHomeScreen;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.ConversationLandingFragment;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.models.Contact;
import com.nc.developers.cloudscommunicator.objects.ConstantsObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.GallaryimgList;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setQuicksandMediumFont;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setQuicksandRegularFont;

/**
 * This Fragment is used to display Contact Profile
 * @author Prajakta Patil
 * @version 1.0
 * @since 10.11.2018
 */
public class ContactProfileFragment extends Fragment implements IOnBackPressed {

    private Observable<String> mobservable;
    private Observer<String> myObserver;
    private View view;
    private Context context;
    private JSONObject contactJson = null;
    private String uProfileImg = "";
    private static final String TAG=ContactProfileFragment.class.getSimpleName();
    public static boolean isbackpressOntoolbar = false;
    public static String isbackpressfromgroup = "";
    public static int backcounterContact=0;


    @BindView(R.id.txt_user_name)TextView txt_user_name;
    @BindView(R.id.txt_user_email)TextView txt_user_email;
    @BindView(R.id.txt_user_contact)TextView txt_user_contact;
    @BindView(R.id.txt_more_details)TextView txt_more_details;
    @BindView(R.id.txt_add)TextView txt_add;
    @BindView(R.id.txt_decline)TextView txt_decline;
    @BindView(R.id.txt_action_title)TextView txt_action_title;
    @BindView(R.id.img_back)ImageView img_back;
    @BindView(R.id.profile_option_menu)ImageView profile_option_menu;
    @BindView(R.id.userprofileimg)ImageView userprofileimg;
    TextView txt_load_more,txt_lbl_media,txt_lbl_showmedia_count;
    RecyclerView gallrecyList;


    public static ConversationLandingFragment newInstance() {
        return new ConversationLandingFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_contact, container, false);
        context=view.getContext();
        ButterKnife.bind(this,view);
        gallrecyList = (RecyclerView) view.findViewById(R.id.recycler_media);
        txt_lbl_media=(TextView)view.findViewById(R.id.txt_lbl_media);
        txt_load_more = (TextView) view.findViewById(R.id.txt_loadmore);
        txt_lbl_showmedia_count=(TextView)view.findViewById(R.id.txt_lbl_showmedia_count);

        txt_user_name.setTypeface(setQuicksandMediumFont(context));
        txt_user_email.setTypeface(setQuicksandMediumFont(context));
        txt_user_contact.setTypeface(setQuicksandMediumFont(context));
        txt_more_details.setTypeface(setQuicksandRegularFont(context));
        txt_add.setTypeface(setQuicksandMediumFont(context));
        txt_decline.setTypeface(setQuicksandMediumFont(context));
        txt_action_title.setTypeface(setQuicksandMediumFont(context));

        try {
            if (GroupMemberListFrag.isGroupMemberProfile){
                String inviteeMail = getArguments().getString("invitee_mail");
                txt_user_name.setText(inviteeMail.split("\\.")[0]+" "+inviteeMail.split("@")[0].split("\\.")[1]);
                txt_user_email.setText(inviteeMail);

            }else {
                String contactJsonString = getArguments().getString("contact_json");
                contactJson = new JSONObject(contactJsonString);
                Log.i(TAG+"cntUpdtObj: ", contactJson.toString());
                setUi(contactJson);
            }


        }catch (JSONException e){
            Log.e(TAG,"-----"+e.getMessage());
        }

        if(GallaryimgList.size()>12)
        {
            txt_load_more.setVisibility(View.VISIBLE);
        }
        else
        {
            txt_load_more.setVisibility(View.GONE);
        }

        if(GallaryimgList.size()>0) {
            txt_lbl_showmedia_count.setVisibility(View.VISIBLE);
            Set<String> set = new HashSet<>(GallaryimgList);
            GallaryimgList.clear();
            GallaryimgList.addAll(set);

            GallaryListAdapter gallaryListAdapter = new GallaryListAdapter(GallaryimgList, getActivity());
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            gallrecyList.setLayoutManager(horizontalLayoutManager);
            gallrecyList.setAdapter(gallaryListAdapter);
            Log.d("GallaryimgList_size", String.valueOf(GallaryimgList.size()));
        }
        else
        {
            txt_lbl_showmedia_count.setVisibility(View.GONE);
            txt_lbl_media.setVisibility(View.GONE);

        }

        txt_lbl_showmedia_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                TabbedDialog dFragment = new TabbedDialog();
                dFragment.show(fragmentManager,"Light");

            }
        });


        txt_load_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.layout_all_gallary_item);
                RecyclerView gallAllMedia = (RecyclerView) dialog.findViewById(R.id.rec_all_gallary);

                AllGallaryListAdapter gallaryListAdapter1 = new AllGallaryListAdapter(GallaryimgList, getActivity());
                gallAllMedia.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                gallAllMedia.setAdapter(gallaryListAdapter1);

                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            }
        });


        setSubcriber();
        return view;
    }

    @OnClick({R.id.img_back, R.id.fab_chat, R.id.txt_add, R.id.profile_option_menu})
    public void onSelectDeselect(View view){
        switch (view.getId()) {
            case R.id.img_back:
                if(ContactsSearchFragment.isSearchContac) {
                    ReplaceFragment.replaceFragment(ContactProfileFragment.this, R.id.frame_frag_container, new ContactsSearchFragment(), false);

                }else if(GroupMemberListFrag.isGroupMemberProfile){
                    ReplaceFragment.replaceFragment(ContactProfileFragment.this, R.id.frame_frag_container, new GroupMemberListFrag(), false);

                }
                else {
                    // getActivity().onBackPressed();
                    Bundle bundle = new Bundle();
                    bundle.putString("group_json", ""+contactJson);
                    backcounterContact=backcounterContact+1;
                    ChatListFragment.fromfrgament = "GroupMember";
                    isbackpressOntoolbar = false;
                    Fragment fragment = new ChatListFragment();
                    fragment.setArguments(bundle);
                    GallaryimgList.clear();
                    Log.d("ContactJsonIS",""+contactJson);
                    ReplaceFragment.replaceFragment(ContactProfileFragment.this, R.id.frame_frag_container, fragment, false);

                /*FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_frag_container, fragment);
                transaction.commit();*/
                }

                break;
            case R.id.txt_add:
                addcontactCall();
                break;
            case R.id.profile_option_menu:
                showMenu(view);
                break;
        }
    }

    /**
     * this is used for add Contacts
     * @return void
     */

    private void addcontactCall(){
        try {
            if (ContactsSearchFragment.isSearchContac){
                boolean internetAvailability= InternetConnectionChecker.checkInternetConnection(context);
                Log.i(TAG+"is internet available:",String.valueOf(internetAvailability)+"");
                Contact contact=new Contact();
                String personal= ConstantsObjects.ROLE_PERSONAL;
                contact.getJSONObjectAddContact(contactJson,personal);
                /*if(GlobalClass.getAuthenticatedSyncSocket()!=null && internetAvailability){
                    JSONObject object= JsonObjectContact.getJSONObjectAddContact(contactJson);
                    Log.i(TAG+"emit_add_cntct:",String.valueOf(object)+" ..kk");
                    GlobalClass.getAuthenticatedSyncSocket().emit("serverOperation",object);
                }*/
                Bundle bundle=new Bundle();
                bundle.putString("call_from","search");
                Fragment fragment= new FragmentHomeScreen();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_frag_container,fragment);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private void showMenu(View v)
    {
        PopupMenu popup = new PopupMenu(getActivity(),v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.userprofile_menu, popup.getMenu());
        popup.show();
    }
    private void setUi(JSONObject cntObj){
        try {
            if (ContactsSearchFragment.isSearchContac){
                Log.d("AllContactObj",""+cntObj);
                txt_user_name.setText(cntObj.getString("CML_FIRST_NAME")+" "+cntObj.getString("CML_LAST_NAME"));
                txt_user_email.setText(cntObj.getString("CML_OFFICIAL_EMAIL"));
                txt_user_contact.setText(cntObj.getString("USM_CONTACT"));
                if (cntObj.getJSONObject("USM_DEFAULT_WORKSPACE").has("imagePath"))  {
                    uProfileImg=cntObj.getJSONObject("USM_DEFAULT_WORKSPACE").getString("imagePath");
                    Glide.with(getActivity()).
                            load(GlobalClass.getLoginUrl() + "/" + uProfileImg)
                            .into((ImageView)userprofileimg);
                }
                else {

                    Glide.with(getActivity()).
                            load(GlobalClass.getLoginUrl() + "/" + uProfileImg)
                            .placeholder(R.drawable.user_pic)
                            .error(R.drawable.user_pic)
                            .into((ImageView)userprofileimg);

                }
                txt_add.setText("ADD");
                txt_add.setVisibility(View.VISIBLE);
                txt_decline.setVisibility(View.GONE);
            }
            else {
                Log.d("dddddddddddddd",""+contactJson);
                txt_user_name.setText(contactJson.getString("CML_TITLE"));
                txt_user_email.setText(contactJson.getString("CONTACT_USER"));
            }

        } catch (JSONException e) {
            Log.e(TAG,"-----"+e.getMessage());
        }


    }


    private void setSubcriber() {
        mobservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        sub.onNext("" );
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

                Log.d(TAG+"onNext",string);
                if (string.equals("fetchRequestComplete")){
                    getActivity().onBackPressed();
                }
            }
        };

        Subcription s=new Subcription();
        s.setObservable(mobservable);
        s.setObserver(myObserver);
        GlobalClass.setCurrentSubcriberr(s);
    }

    @Override
    public boolean onBackPressed() {
        if(ContactsSearchFragment.isSearchContac){
            ReplaceFragment.replaceFragment(ContactProfileFragment.this, R.id.frame_frag_container, new ContactsSearchFragment(), false);


        }

        else if(GroupMemberListFrag.isGroupMemberProfile){
            ReplaceFragment.replaceFragment(ContactProfileFragment.this, R.id.frame_frag_container, new GroupMemberListFrag(), false);

        }

        else {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
                Bundle bundle = new Bundle();
                ChatListFragment.fromfrgament = "GroupMember";
                isbackpressOntoolbar = false;
                GallaryimgList.clear();
                bundle.putString("group_json", ""+contactJson);
                Fragment fragment = new ChatListFragment();
                fragment.setArguments(bundle);
                ReplaceFragment.replaceFragment(ContactProfileFragment.this, R.id.frame_frag_container, fragment, false);
            }
        }


        return true;
    }

}
