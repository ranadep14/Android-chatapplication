package com.cloudsinc.soulmobile.nativechatapplication.fragments.chat;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudsinc.soulmobile.nativechatapplication.App;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.AllGallaryListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.ConversationListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.CustomAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.GallaryListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.GrpMemberAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.InviteeListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.datamodels.User;
import com.cloudsinc.soulmobile.nativechatapplication.dialogs.TabbedDialog;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.ConversationListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.CreateConversationFragment;
import com.cloudsinc.soulmobile.nativechatapplication.interfaces.IOnBackPressed;
import com.cloudsinc.soulmobile.nativechatapplication.utils.CircleTransform;
import com.cloudsinc.soulmobile.nativechatapplication.utils.InternetConnectionChecker;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.Subcription;
import com.nc.developers.cloudscommunicator.models.Contact;
import com.nc.developers.cloudscommunicator.models.Conversation;
import com.nc.developers.cloudscommunicator.models.Invitee;
import com.nc.developers.cloudscommunicator.socket.JsonParserClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.GallaryimgList;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.GallarypdfList;
import static com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment.ChatList;
import static com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactsSearchFragment.hideKeyboard;

/**
 * Created by developers on 25/6/18.
 */

public class GroupMemberListFrag extends Fragment implements IOnBackPressed {
    private Observable<String> mobservable;
    private Observer<String> myObserver;
    //private ArrayList<GrpMemberModel> grpMemList = new ArrayList<>();
    private ArrayList<Invitee> grpMemList = new ArrayList<>();
    // ArrayList<String> searchEmailAddressList=new ArrayList<>();
    Conversation conversation1;
    public static boolean isGroupMemberProfile = false;
    public static boolean isbackpressforviewprofile=false;



    public static boolean isGrpEdit = false;
    public static boolean isAddMem = false;
    RecyclerView recGrpMemList,gallrecyList;
    JSONObject group_json = null;
    Context context;
    static Context mcontext;
    GrpMemberAdapter grpMemAdapter;
    ImageView img_profile, img_edit, img_options,img_cancel;
    View view;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    private String grpKeyVal = null;
    private String grpownerId = null, createdby = null;
    private String grpMsg = null;
    private String tempkeyval = null;
    TextView txt_adduser;
    public static boolean isFetchInvitee = false;
    private Conversation selectedConversation;
    private boolean isOverflowMenuClicked = false;
    boolean current_members[] = null;
    private ArrayList<Invitee> inviteeList = new ArrayList<>();
    ArrayList<User> searchContactList = new ArrayList<>();
    ArrayList<String> inviteesToAdd = new ArrayList<String>();
    public static boolean isbackpressOntoolbar = false;
    public static String isbackpressfromgroup = "";


    private Repository repo = Repository.getRepository();

    private CoordinatorLayout coordinatorLayout;
    InviteeListAdapter inviteeListAdapter;
    RecyclerView rec_invitee_list;
    String TAG = this.getClass().getSimpleName();

    TextView txt_no_user,txt_load_more,txt_lbl_media, txt_lbl_showmedia_count;
    ProgressBar prg_searchuser;
    LinearLayout ll_search_contact_user;
    LinearLayout ll_no_userdata;
    EditText edit_serach;
    ImageView image_search;
    Button btn_ok;
    ImageView img_close;
    EditText ed_edittitle;
    Conversation conversation;
    ImageView img_save;
    ProgressBar progbar_renameloader;
    public static ProgressBar progbar_rename;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.grpmembers_fragment, container, false);
        context = view.getContext();
        mcontext = view.getContext();

        recGrpMemList = (RecyclerView) view.findViewById(R.id.collapsing_toolbar_recycler_view);
        gallrecyList = (RecyclerView) view.findViewById(R.id.recycler_media);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recGrpMemList.setLayoutManager(mLayoutManager);
        recGrpMemList.setItemAnimator(new DefaultItemAnimator());
        img_save = (ImageView) view.findViewById(R.id.img_save);
        progbar_renameloader = (ProgressBar) view.findViewById(R.id.progbar_renameloader);
        txt_adduser = (TextView) view.findViewById(R.id.txt_adduser);
        txt_adduser.setPaintFlags(txt_adduser.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        progbar_rename = (ProgressBar) view.findViewById(R.id.progbar_rename);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.CoordinatorLayout);
        ed_edittitle = (EditText) view.findViewById(R.id.edt_edit);
        txt_load_more = (TextView) view.findViewById(R.id.txt_loadmore);
        img_edit = (ImageView) view.findViewById(R.id.img_edit);
        img_cancel = (ImageView) view.findViewById(R.id.img_cancel);
        img_options = (ImageView) view.findViewById(R.id.img_options);
        txt_lbl_media=(TextView)view.findViewById(R.id.txt_lbl_media);
        txt_lbl_showmedia_count=(TextView)view.findViewById(R.id.txt_lbl_showmedia_count);

        try {
            group_json = App.getGrpObjForEdit();
            Log.i("clickedObj: ", group_json.toString());
            grpKeyVal = group_json.getString("KEY_VAL");
            grpownerId = group_json.getString("OWNER_ID");
            createdby = group_json.getString("CREATED_BY");
        } catch (JSONException e) {
            Log.e("exception: ", e.toString());
        }

        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar_layout);
        img_profile = (ImageView) view.findViewById(R.id.collapsing_toolbar_image_view);

        img_save.setEnabled(false);
        img_save.setVisibility(View.GONE);

        ed_edittitle.setCursorVisible(true);
        ed_edittitle.setSelection(ed_edittitle.getText().length());


        toolbar = (Toolbar) view.findViewById(R.id.collapsing_toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back, null));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                isbackpressOntoolbar = true;
                ChatListFragment.fromfrgament = "GroupMember";
                //bundle.putString("group_json", ""+getArguments().getString("group_json"));
                bundle.putString("group_json", group_json.toString());
                GallaryimgList.clear();
                GallarypdfList.clear();
                ChatListFragment.rply=false;
                ChatListFragment.showingimage=false;
                Fragment fragment = new ChatListFragment();
                fragment.setArguments(bundle);
                ReplaceFragment.replaceFragment(GroupMemberListFrag.this, R.id.frame_frag_container, fragment, false);
            }
        });


        if(GallaryimgList.size()>12)
        {
            txt_load_more.setVisibility(View.VISIBLE);
        }
        else
        {
            txt_load_more.setVisibility(View.GONE);
        }


        if(GallaryimgList.size()>0) {
            txt_lbl_media.setVisibility(View.VISIBLE);
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
            txt_lbl_media.setVisibility(View.GONE);
            txt_lbl_showmedia_count.setVisibility(View.GONE);

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

        txt_adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (inviteeList.size()>0){
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_invitee_custom_dailog);

                rec_invitee_list = (RecyclerView) dialog.findViewById(R.id.rec_invitee_list);
                edit_serach = (EditText) dialog.findViewById(R.id.edit_search);
                image_search = (ImageView) dialog.findViewById(R.id.imageSearch);
                btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
                img_close = (ImageView) dialog.findViewById(R.id.img_close);
                txt_no_user = (TextView) dialog.findViewById(R.id.txt_no_user);
                prg_searchuser = (ProgressBar) dialog.findViewById(R.id.prg_searchuser);
                ll_search_contact_user = (LinearLayout) dialog.findViewById(R.id.ll_search_contact_user);
                ll_no_userdata = (LinearLayout) dialog.findViewById(R.id.ll_no_userdata);


                img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        inviteesToAdd.clear();
                        ArrayList<User> addMemList = App.getAddMemList();
                        Log.d("addMemListIsss", String.valueOf(App.getAddMemList()));
                        if (addMemList.size() > 0) {
                            for (int i = 0; i < addMemList.size(); i++) {
                                inviteesToAdd.add(addMemList.get(i).getcMail());
                                Log.d("inviteesToAddlist1", inviteesToAdd.toString());

                            }
                        }

                        JSONObject grpObj = App.getGrpObjForEdit();
                        Log.d("grp0bj........",grpObj.toString());
                        if (grpObj != null) {
                            conversation = JsonParserClass.parseConversationJsonObject(grpObj);
                            Log.d("Conversationselected1", conversation.getCmlTitle());
                        }
                        Log.d("inviteesToAddlist", inviteesToAdd.toString());


                        if (inviteesToAdd.size() > 0) {
                            conversation.getJSONObjectAddMember(inviteesToAdd);

                            /*JSONObject objToAddRemoveInvitees = JsonObjectInvitee.getJSONObjectAddMember(inviteesToAdd,conversation);
                            boolean internetAvailability = InternetConnectionChecker.checkInternetConnection(getActivity());
                            Log.i("is internet available:", String.valueOf(internetAvailability) + " ..kk");
                            if (GlobalClass.getAuthenticatedSyncSocket() != null && internetAvailability) {
                                Log.i("emit_invitee_aadd*:", String.valueOf(objToAddRemoveInvitees));
                                GlobalClass.getAuthenticatedSyncSocket().emit("serverOperation", objToAddRemoveInvitees);
                            }*/

                        }
                        dialog.dismiss();

                    }


                });
                rec_invitee_list.setHasFixedSize(true);
                rec_invitee_list.setLayoutManager(new LinearLayoutManager(getActivity()));


                edit_serach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            searchUserFunction();
                            return true;
                        }
                        return false;
                    }
                });


                image_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchUserFunction();
                    }
                });

                inviteeListAdapter = new InviteeListAdapter(getActivity(), searchContactList);
                rec_invitee_list.setAdapter(inviteeListAdapter);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        });

        img_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOptionMenu();
            }
        });

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_edittitle.setEnabled(false);
                ed_edittitle.setClickable(false);
                img_edit.setVisibility(View.VISIBLE);
                img_save.setVisibility(View.GONE);
                img_cancel.setVisibility(View.GONE);
                // if(ed_edittitle.getText().length()<=0){
                try {
                    ed_edittitle.setText(group_json.getString("CML_TITLE"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //}

            }
        });


        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ed_edittitle.setFocusableInTouchMode(true);
                ed_edittitle.requestFocus();
                ed_edittitle.setSelection(ed_edittitle.length());
                ed_edittitle.setEnabled(true);
                img_save.setEnabled(true);
                img_save.setVisibility(View.VISIBLE);
                img_edit.setVisibility(View.GONE);
                img_cancel.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);


                ed_edittitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {




                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        if (s.length() <= 0) {

                            img_save.setEnabled(false);
                            img_save.setAlpha(0.2f);
                            // img_cancel.setEnabled(false);
                        } else {

                            img_save.setEnabled(true);
                            img_save.setAlpha(1.0f);
                            //  img_cancel.setEnabled(true);


                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {


                    }
                });

            }
        });


        ed_edittitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v, getActivity());
                }
            }
        });

        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Gjson", group_json.toString());
                img_save.setVisibility(View.GONE);
                img_edit.setVisibility(View.VISIBLE);
                img_cancel.setVisibility(View.GONE);
                progbar_renameloader.setVisibility(View.VISIBLE);
                ed_edittitle.setEnabled(false);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject grpObj = App.getGrpObjForEdit();
                            grpObj.put("CML_TITLE", "" + ed_edittitle.getText());
                            App.setGrpObjForEdit(grpObj);
                            Log.d("aaaaaaaaaaaaaa",grpObj.toString());
                            conversation1 = JsonParserClass.parseConversationJsonObject(grpObj);
                            Log.d("bbbbbb",conversation1.toString());


                            progbar_renameloader.setVisibility(View.GONE);

                            //    ReplaceFragment.replaceFragment(GroupMemberListFrag.this, R.id.frame_frag_container, new GroupMemberListFrag(), false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progbar_renameloader.setVisibility(View.GONE);
                        }
                        conversation1.getJsonObjectUpdateConversation("rename",ed_edittitle.getText().toString());

                    }
                }, 500);


            }
        });

        setSubcriber();
        emitRequireCalls();
        return view;
    }


    private void searchUserFunction() {
        String searchString = "";
        searchString = edit_serach.getText().toString().trim();
        Conversation conversation=new Conversation();
        conversation.getJsonObjectSearchAllContact(searchString,"conversation");

        if (edit_serach.getText().length() > 0) {

            searchContactList.clear();

            ll_search_contact_user.setVisibility(View.GONE);
            ll_no_userdata.setVisibility(View.VISIBLE);
            prg_searchuser.setVisibility(View.VISIBLE);
            txt_no_user.setVisibility(View.GONE);
            noData();


        }
    }

    private void noData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ll_search_contact_user.setVisibility(searchContactList.size() > 0 ? View.VISIBLE : View.GONE);
                ll_no_userdata.setVisibility(searchContactList.size() > 0 ? View.GONE : View.VISIBLE);
                txt_no_user.setText("No Result found ");
                txt_no_user.setVisibility(searchContactList.size() > 0 ? View.GONE : View.VISIBLE);
                prg_searchuser.setVisibility(View.GONE);
            }
        },      searchContactList.size() > 0 ? 0 : 7000);
    }


    @Override
    public void onResume() {
        if (searchContactList.size() > 0) {            //setAdapter();

            txt_no_user.setVisibility(View.GONE);
            ll_no_userdata.setVisibility(View.GONE);
            ll_search_contact_user.setVisibility(View.VISIBLE);

        }
        super.onResume();
    }

    @Override
    public boolean onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if (ChatListFragment.fromfrgament.equals("contact")) {
            if (fragmentManager.getBackStackEntryCount() > 0) {

                Bundle bundle = new Bundle();
                ChatListFragment.fromfrgament = "GroupMemberContact";
                isbackpressOntoolbar = false;
                isbackpressfromgroup = "contact";
                GallaryimgList.clear();
                GallarypdfList.clear();
                ChatListFragment.rply=false;
                ChatListFragment.showingimage=false;
                bundle.putString("group_json", group_json.toString());
                Fragment fragment = new ChatListFragment();
                fragment.setArguments(bundle);
                ReplaceFragment.replaceFragment(GroupMemberListFrag.this, R.id.frame_frag_container, fragment, false);
            }

        } else {
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
                Bundle bundle = new Bundle();
                ChatListFragment.fromfrgament = "GroupMember";
                isbackpressOntoolbar = false;
                GallaryimgList.clear();
                GallarypdfList.clear();
                ChatListFragment.rply=false;
                ChatListFragment.showingimage=false;
                bundle.putString("group_json", group_json.toString());
                Fragment fragment = new ChatListFragment();
                fragment.setArguments(bundle);
                ReplaceFragment.replaceFragment(GroupMemberListFrag.this, R.id.frame_frag_container, fragment, false);
            }
        }

        return true;

    }

    private void MyOptionMenu() {
        PopupMenu popup = new PopupMenu(getActivity(), img_options);
        popup.getMenuInflater().inflate(R.menu.main, popup.getMenu());
        popup.getMenu().findItem(R.id.item1).setTitle(Html.fromHtml("<font color='#000000'>Edit</font>"));
        popup.getMenu().findItem(R.id.item2).setTitle(Html.fromHtml("<font color='#000000'>Add Member</font>"));
        popup.getMenu().findItem(R.id.item3).setTitle(Html.fromHtml("<font color='#000000'>Important</font>"));
        popup.getMenu().findItem(R.id.item4).setTitle(Html.fromHtml("<font color='#000000'>Delete</font>"));
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                        ConversationListFragment.isGrpUpdate = true;
                        Log.d("udt_group_json", String.valueOf(group_json));
                        Bundle bundle = new Bundle();
                        bundle.putString("group_json", String.valueOf(group_json));
                        Fragment fragment = new CreateConversationFragment();
                        fragment.setArguments(bundle);
                        ReplaceFragment.replaceFragment(GroupMemberListFrag.this, R.id.frame_frag_container, fragment, true);

                        break;
                }
                return true;
            }
        });
        popup.show();
    }


    private void emitRequireCalls()
    {
        Contact contact=new Contact();
        contact.getJSONObjectFetchAllContacts();
        /*JSONObject fetchAllContacts= JsonObjectContact.getJSONObjectFetchAllContacts();
        Log.i("emit_fetch_all_cntcts:",String.valueOf(fetchAllContacts)+" ..kk");
        if(GlobalClass.getAuthenticatedSyncSocket()!=null){
            GlobalClass.getAuthenticatedSyncSocket().emit("OnDemandCall",fetchAllContacts);
        }*/
    }

    private void updateUiforSearch(String str) {

        try {
            if (str.equals("searchContactComplete")) {
                searchContactList.clear();
            }
            if (!(searchContactList.size() > 0)) {
                System.out.println("fffffff" + GlobalClass.getSearchContact());
                //   JSONArray jsonArray = GlobalClass.getSearchContact();
                JSONObject jbcontact = GlobalClass.getSearchContact();

                JSONArray jarray = jbcontact.getJSONArray("dataArray");

                for (int j = 0; j < jarray.length(); j++) {
                    boolean flag = false;
                    JSONObject jsonObject = jarray.getJSONObject(j);
                    if (jsonObject.has("USM_EMAIL")) {
                        flag = jsonObject.getString("USM_EMAIL").equals(GlobalClass.getUserId());
                    }
                    if (!flag) searchContactList.add(new User(jarray.getJSONObject(j)));





                    System.out.println("fffffff" + GlobalClass.getSearchAllContactResult());
                    JSONObject jobject = GlobalClass.getSearchAllContactResult();

                    JSONArray jarray1 = jobject.getJSONArray("dataArray");

                    for (int k = 0; k < jarray1.length(); k++) {
                        boolean flag1 = false;
                        JSONObject jsonObject1 = jarray1.getJSONObject(k);
                        if (jsonObject1.has("CML_OFFICIAL_EMAIL")) {
                            flag1 = jsonObject1.getString("CML_OFFICIAL_EMAIL").equals(GlobalClass.getUserId());
                        }
                        if (!flag1) searchContactList.add(new User(jarray1.getJSONObject(k)));





                        for (int i = 0; i < searchContactList.size(); i++) {
                            for (int J = 0; J < grpMemList.size(); J++) {
                                if (searchContactList.get(i).getcMail().equalsIgnoreCase(grpMemList.get(J).getIde_attendees_email())) {
                                    searchContactList.remove(i);
                                    break;
                                }
                            }
                        }
                    }
                }

                if (inviteeListAdapter != null) {
                    inviteeListAdapter.reloadList(searchContactList);
                    inviteeListAdapter.notifyDataSetChanged();
                } else {
                    rec_invitee_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                    for (int i = 0; inviteeList.size() > 0; i++) {
                        Log.d("aaaaaaaaaaaa", searchContactList.get(i).getcNm());
                    }

                    inviteeListAdapter = new InviteeListAdapter(getActivity(), searchContactList);
                    rec_invitee_list.setAdapter(inviteeListAdapter);
                }
            }
            noData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateUi() {
        try {

            ed_edittitle.setText(group_json.getString("CML_TITLE"));
            Log.i("grpUpdtObj: ", group_json.toString());
            boolean isimagepath=group_json.has("CML_IMAGE_PATH");
            Log.d("isImagepathcont",""+isimagepath);
            if(isimagepath==true){
                if((group_json.getString("CML_IMAGE_PATH")!=null)){
                    String str=GlobalClass.getLoginUrl()+""+group_json.getString("CML_IMAGE_PATH");
                    Log.d("StringIs",str);
                    Glide.with(mcontext).
                            load( str)
                            .error(R.drawable.group_icon)
                            .placeholder(R.drawable.group_icon)
                            .into(img_profile);
                }


            }else {
                Glide.with(mcontext).
                        load( R.drawable.group_icon)
                        .error(R.drawable.group_icon)
                        .placeholder(R.drawable.group_icon)
                        .into(img_profile);
            }


            grpMemList.clear();

            Repository repo = Repository.getRepository();
            ArrayList<Invitee> allInvitee = (ArrayList<Invitee>) repo.getAllInvitees(grpownerId);
            if (allInvitee.size() > 0) {
                for (int i = 0; i < allInvitee.size(); i++) {
                    grpMemList.add(allInvitee.get(i));


                    if (grpMemList != null) {
                        for (int j = 0; j < grpMemList.size(); j++) {
                            if (GlobalClass.getUserId().equals(grpMemList.get(i).getIde_attendees_email())) {


                                if (grpMemList.get(i).getIde_designation().equals("ADMIN")) {
                                    txt_adduser.setVisibility(View.VISIBLE);

                                }
                                else {
                                    txt_adduser.setVisibility(View.GONE);


                                }
                            }
                        }
                    }






                    Collections.sort(grpMemList, new Comparator<Invitee>() {
                        @Override
                        public int compare(Invitee item, Invitee t1) {
                            String s1 = item.getIde_attendees_email();
                            String s2 = t1.getIde_attendees_email();
                            return s1.compareToIgnoreCase(s2);
                        }

                    });
                    Log.d("Aplhabeical", grpMemList.get(i).getIde_attendees_email());
                }

            }


            if (grpMemList.size() > 0) {
                for (int i = 0; i < grpMemList.size(); i++) {
                    Invitee invitie = grpMemList.get(i);
                    Log.i("invtee_member:", grpMemList.get(i).getCompleteData());
                }
            } else {
                Log.i("status:", "no member available");
            }

            Log.d("memberlistsize", String.valueOf(grpMemList.size()));

            if (grpMemList != null && grpMemList.size() > 0) {
                if (grpMemAdapter == null) {
                    grpMemAdapter = new GrpMemberAdapter(getActivity(), grpMemList, GroupMemberListFrag.this);
                    recGrpMemList.setAdapter(grpMemAdapter);
                    Log.i("Notify", "adapter called");

                } else {
                    grpMemAdapter.reload(grpMemList);
                    grpMemAdapter.notifyDataSetChanged();
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

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
                Log.i("onNxt_string", string + " ..kk");
                if (string != null) {

                    if (string.equals("fetch_invitee") || string.equals("UPDATE_CHAT_INVITEE_SERVER")||string.equals("fetchContactComplete")
                            || string.equals("REMOVE_INVITEE_SERVER") || string.equals("ADD_INVITEE_SERVER")|| string.equals("REMOVE_ADMIN_SERVER")||string.equals("remove_invitee")
                            || string.equals("conversation_update")||(string.equals("add_invitee"))) {
                        updateUi();
                        coordinatorLayout.setVisibility(View.VISIBLE);
                        progbar_rename.setVisibility(View.GONE);
                        if(string.equals("REMOVE_INVITEE_SERVER") ||string.equals("REMOVE_ADMIN_SERVER")||string.equals("remove_invitee")){
                            GrpMemberAdapter.dialog.dismiss();

                        }


                    }
                    //ankita code
                    if (string.equals("make/remove_admin")) {
                        updateUi();
                        progbar_rename.setVisibility(View.GONE);
                        GrpMemberAdapter.dialog.dismiss();
                        if (GrpMemberAdapter.dialog != null) {
                            GrpMemberAdapter.dialog.dismiss();
                            GrpMemberAdapter.dialog.setCanceledOnTouchOutside(true);
                            GrpMemberAdapter.dialog.setCancelable(true);

                        }
                        coordinatorLayout.setVisibility(View.VISIBLE);


                    }
                    if (string.equals("searchContactComplete")||string.equals("search_contact_done")) {
                        updateUiforSearch(string);

                    }
                    if (string.equals("userselected")) {
                        btn_ok.setAlpha(1f);
                    } else {
                        btn_ok.setAlpha(0.5f);
                    }



                }
            }
        };

        Subcription s = new Subcription();
        s.setObservable(mobservable);
        s.setObserver(myObserver);
        GlobalClass.setCurrentSubcriberr(s);


    }


}