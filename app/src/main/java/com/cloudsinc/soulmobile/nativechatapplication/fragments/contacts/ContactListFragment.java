package com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudsinc.soulmobile.nativechatapplication.adapters.ConversationListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.dialogs.CustomDialog;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.utils.InternetConnectionChecker;
import com.cloudsinc.soulmobile.nativechatapplication.utils.SortingClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.Subcription;
import com.nc.developers.cloudscommunicator.models.Contact;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.ContactsListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.FragmentHomeScreen;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;
import com.cloudsinc.soulmobile.nativechatapplication.utils.common;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.models.Conversation;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

import static com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactsSearchFragment.serchcount;

/**
 * This Fragment is used to display Contact List
 * @author Prajakta Patil
 * @version 1.0
 * @since 2.11.2018
 */
public class ContactListFragment extends Fragment{
    private Observable<String> mobservable;
    private Observer<String> myObserver;
    private ArrayList<Contact> contactList=new ArrayList<>();
    private ArrayList<Contact> NeContList=new ArrayList<>();
    private View view;
    private Context context;
    static Context mcontext;
    ContactsListAdapter conAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static boolean isContactSearch = false;
    public static boolean isContacSearchVisible=false;

    @BindView(R.id.rec_contact_list)RecyclerView rec_contact_list;
    @BindView(R.id.fab_add_contact)FloatingActionButton fab_add_contact;
    @BindView(R.id.img_back)ImageView img_back;
    @BindView(R.id.img_clear_txt)ImageView img_clear_txt;
    @BindView(R.id.ll_back)LinearLayout ll_back;
    @BindView(R.id.ll_clear_txt)LinearLayout ll_clear_txt;
    @BindView(R.id.img_left_icon)ImageView img_left_icon;
    @BindView(R.id.img_right_icon1)ImageView img_right_icon1;
    @BindView(R.id.img_right_icon2)ImageView img_right_icon2;
    @BindView(R.id.li_img_delete)LinearLayout li_img_delete;
    @BindView(R.id.img_right_icon3)ImageView img_right_icon3;
    @BindView(R.id.ll_no_data)LinearLayout ll_no_data;
    @BindView(R.id.txt_no_data)TextView txt_no_data;
    @BindView(R.id.prg_contact)ProgressBar prg_contact;
    private static LinearLayout lin_search_bar;
    private static RelativeLayout lin_action_bar;
    private static TextView txt_action_title;
    public static EditText edit_search;
    public static String userName;
    public static LinearLayout ll_contact_list;
    public static String contect_user="";
    private static final String TAG=ContactListFragment.class.getSimpleName();
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        view=inflater.inflate(R.layout.fragment_contact_list, container, false);
        context=view.getContext();
        mcontext=view.getContext();
        ButterKnife.bind(this,view);
        lin_search_bar=(LinearLayout)view.findViewById(R.id.lin_search_bar);
        lin_action_bar=(RelativeLayout)view.findViewById(R.id.lin_action_bar);
        txt_action_title = (TextView) view.findViewById(R.id.txt_action_title);
        ll_contact_list=(LinearLayout)view.findViewById(R.id.ll_contact_list);

        FragmentHomeScreen.actionTabsVis();
        edit_search=(EditText) view.findViewById(R.id.edit_search);
        userName="";
        edit_search.setHint("Search Contacts..");
        edit_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        li_img_delete.setVisibility(View.VISIBLE);

        mSwipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_contact_list);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        mSwipeRefreshLayout.setRefreshing(false);
                        if(NeContList.size()>8){
                            Log.d(TAG,""+NeContList.size());

                        }
                    }
                }, 2000);
            }
        });

        setSubcriber();
        fetchallContactCall();


        edit_search.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s,int start,int count,int after){
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
            }
            @Override
            public void afterTextChanged(Editable s){
                if(contactList.size()>0){
                    filter(s.toString());
                }
            }
        });

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        Repository rr=Repository.getRepository();
        if(rr!=null){
            ArrayList<Contact> contacts=(ArrayList<Contact>)rr.getAllContacts();
            if(contacts!=null){
                if(contacts.size()>0){
                    updateUi();
                }
            }
        }
    }


    private void filter(String text){
        ArrayList<Contact> temp = new ArrayList();
        String filterString = "";
        if(contactList!=null) {
            for (Contact d : contactList) {
                filterString = d.getFirstname() + " " + d.getOfficialEmail();
                if (filterString.contains(text)) {
                    temp.add(d);
                }
            }
        }
        if(temp!=null){
            conAdapter.filterList(temp);
            if(temp.size()>0){
                ll_contact_list.setVisibility(View.VISIBLE);
                ll_no_data.setVisibility(View.GONE);
                txt_no_data.setVisibility(View.GONE);
                txt_no_data.setText("");
                prg_contact.setVisibility(View.GONE);
            }
            else {
                ll_contact_list.setVisibility(View.GONE);
                ll_no_data.setVisibility(View.VISIBLE);
                txt_no_data.setVisibility(View.VISIBLE);
                txt_no_data.setText("No Result Found");
                prg_contact.setVisibility(View.GONE);
            }
        }
    }
    /**
     * this method is used for Fetch All Contacts
     * @return void
     */
    private void fetchallContactCall(){
        try {
            Contact contact=new Contact();
            contact.getJSONObjectFetchAllContacts();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contect_user="";
    }

    @Optional
    @OnClick({R.id.fab_add_contact,R.id.ll_back,R.id.ll_clear_txt,R.id.img_left_icon,R.id.img_right_icon2})
    public void onSelectDeselect(View view){
        switch(view.getId()){
            case R.id.fab_add_contact:
                serchcount=0;
                ReplaceFragment.replaceFragment(ContactListFragment.this, R.id.frame_frag_container, ContactsSearchFragment.newInstance(), true);
                break;
            case R.id.ll_back:
                if(ContactListFragment.isContactSearch){
                    common.hideKeyboard(getActivity());
                    FragmentHomeScreen.actinBarVisibility(true);
                    ContactLandingFragment.contactTabsVisibility(true);
                    ContactListFragment.contactSerchBarVisiblity(false);
                    edit_search.setText("");
                }
                break;
            case R.id.ll_clear_txt:
                edit_search.setText("");
                break;
            case R.id.img_left_icon:
                ContactListFragment.contactActionBarVisiblity(false,0);
                FragmentHomeScreen.actinBarVisibility(true);
                ContactLandingFragment.contactTabsVisibility(true);
                conAdapter.resetFlag();
                break;
            case R.id.img_right_icon2:
                if(ContactsListAdapter.selectedcontctlist.size()==1){
                    CustomDialog.showActionDialog(mcontext,"Confirm","Do you want to  delete this contact?","contact_delete",true);
                }
                else {
                    CustomDialog.showActionDialog(mcontext,"Confirm","Do you want to  delete this contacts?","contact_delete",true);
                }
                break;
        }
    }

    /**
     * this method is used for delete selected contact
     * @return void
     */

    public static void deleteContactFunction(){
        ContactListFragment.contactActionBarVisiblity(false,0);
        FragmentHomeScreen.actinBarVisibility(true);
        ContactLandingFragment.contactTabsVisibility(true);
        CustomDialog.startProgressDialog(mcontext);
        ll_contact_list.setVisibility(View.GONE);
        Contact contact = ContactsListAdapter.selected_contact;
        Log.i(TAG + "cntct_id:", contact.getOfficialEmail() + " ..kk");
        try {
            contact.getJSONObjectDeleteContact(ContactsListAdapter.selectedcontctlist);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public static class BottomOffsetDecoration extends RecyclerView.ItemDecoration {
        private int mBottomOffset;
        public BottomOffsetDecoration(int bottomOffset) {
            mBottomOffset = bottomOffset;
        }
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int dataSize = state.getItemCount();
            int position = parent.getChildAdapterPosition(view);
            if (dataSize > 0 && position == dataSize - 1) {
                outRect.set(0, 0, 0, mBottomOffset);
            } else {
                outRect.set(0, 0, 0, 0);
            }

        }
    }



    public static void contactSerchBarVisiblity(boolean b){
        lin_search_bar.setVisibility(b?View.VISIBLE:View.GONE);
        if (b) common.showKeyboard((Activity) mcontext,edit_search);
    }
    public static void contactActionBarVisiblity(boolean b,int selectCont){
        lin_action_bar.setVisibility(b?View.VISIBLE:View.GONE);
        txt_action_title.setText(""+selectCont+" Selected");

    }
    /**
     * this method is used for render data on UI.
     * @return void
     */

    private void updateUi(){
        try{
            contactList.clear();
            Repository repo=Repository.getRepository();
            ArrayList<Contact>newcntlist= (ArrayList<Contact>)repo.getAllContacts();
            contactList= SortingClass.getSortedContactList(newcntlist);
            Log.i(TAG+"contactLst:",String.valueOf(contactList.size())+" ..kk");
            if(contactList!=null && contactList.size()>0){
                if (conAdapter==null){
                    RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getActivity());
                    rec_contact_list.setLayoutManager(mLayoutManager);
                    rec_contact_list.setItemAnimator(new DefaultItemAnimator());
                    conAdapter = new ContactsListAdapter(getActivity(),contactList,ContactListFragment.this);
                    rec_contact_list.setAdapter(conAdapter);
                    float offsetPx = getResources().getDimension(R.dimen.px_80);
                    BottomOffsetDecoration bottomOffsetDecoration = new BottomOffsetDecoration((int) offsetPx);
                    rec_contact_list.addItemDecoration(bottomOffsetDecoration);
                }
                else {
                    conAdapter.reload(contactList);
                    conAdapter.notifyDataSetChanged();
                }
            }
            noData();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method is used for MainTain Visibility
     * @return void
     */
    private void noData() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ll_contact_list.setVisibility(contactList.size()>0?View.VISIBLE:View.GONE);
                ll_no_data.setVisibility(contactList.size()>0?View.GONE:View.VISIBLE);
                txt_no_data.setVisibility(contactList.size()>0?View.GONE:View.VISIBLE);
                prg_contact.setVisibility(View.GONE);
            }
        },contactList.size()>0?0:0);
    }

    private void setSubcriber(){
        mobservable=Observable.create(
                new Observable.OnSubscribe<String>(){
                    @Override
                    public void call(Subscriber<? super String> sub){
                        sub.onNext("");
                        sub.onCompleted();
                    }
                }
        );
        myObserver = new Observer<String>(){

            @Override
            public void onCompleted(){
            }
            @Override
            public void onError(Throwable e){
            }
            @Override
            public void onNext(String string){
                if(string!=null){
                    Log.i(TAG+"_onNxt:",string+" ..kk");
                    if(string.equals("contact_added")||string.equals("no_contacts") || string.equals("addContactComplete") ||
                            string.equals("contact_request_declined") ||string.equals("contact_declined")||
                            string.equals("contact_removed") || string.equals("fetchContactComplete") ||
                            string.equals("contact_decline_done")
                            ||string.equals("CHAT_FILTER_SERVER")){
                        Log.i(TAG+"onNext:",string);
                        if(ContactsListAdapter.selectedCount<=0){
                            updateUi();
                        }
                        CustomDialog.stopProgressDialog();
                    }
                    if(string.contains("CHAT_FILTER_SERVERCCCCCC"))
                    {
                        try {
                            JSONObject jsonObject=new JSONObject(string.split("CCCCCC")[1]);
                            Log.i(TAG,"------------"+jsonObject.getString("CONTACT_USER")+"-------------------"+contect_user);
                            if(jsonObject.getString("CONTACT_USER").equals(contect_user))
                            {
                                try {
                                    Bundle bundle=new Bundle();
                                    bundle.putString("group_json",""+jsonObject);
                                    bundle.putString("call_from","ContactListFragment");
                                    Fragment fragment = new ChatListFragment();
                                    fragment.setArguments(bundle);
                                    ReplaceFragment.replaceFragment(ContactListFragment.this, R.id.frame_frag_container, fragment, true);

                                } catch (Exception e) {
                                    Log.e(TAG,"----------------"+e.getMessage());
                                }
                            }
                        }
                        catch (Exception ex)
                        {
                            Log.e(TAG,"-----------------"+ex.getMessage());
                        }

                    }

                    //created by nikhil for one to one
                    if(string.contains("create_conversation")) {
                        try {
                            Bundle bundle=new Bundle();
                            Repository repo=Repository.getRepository();
                            ArrayList<Conversation> groupNmList=(ArrayList<Conversation>)repo.getAllConversations("unarchive");
                            for (int i=0;i<groupNmList.size();i++)
                            {
                                Conversation conversation=groupNmList.get(i);
                                if(conversation.getKeyval().equals(string.split("<>")[1]))
                                {
                                    bundle.putString("group_json",conversation.getCompleteData());
                                }
                            }
                            bundle.putString("call_from","ContactListFragment");
                            Fragment fragment = new ChatListFragment();
                            fragment.setArguments(bundle);
                            ReplaceFragment.replaceFragment(ContactListFragment.this, R.id.frame_frag_container, fragment, true);

                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }  catch (Exception ex){
                            ex.printStackTrace();
                        }


                    }
                }
            }
        };
        Subcription s=new Subcription();
        s.setObservable(mobservable);
        s.setObserver(myObserver);
        GlobalClass.setCurrentSubcriberr(s);
    }
}