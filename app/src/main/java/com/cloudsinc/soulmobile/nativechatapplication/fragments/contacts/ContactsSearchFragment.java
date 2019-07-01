package com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.interfaces.IOnBackPressed;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.Subcription;
import com.nc.developers.cloudscommunicator.models.Contact;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.SearchUserListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.datamodels.User;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.FragmentHomeScreen;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.models.Conversation;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

/**
 * This Fragment is used to display and search Contact
 * @author Prajakta Patil
 * @version 1.0
 * @since 28.10.2018
 */
public class ContactsSearchFragment extends Fragment implements IOnBackPressed {
    private Observable<String> mobservable;
    private Observer<String> myObserver;
    public static boolean isSearchContac = false;
    private Context mcontext;
    private SearchUserListAdapter adapter;
    private View v;
    private ArrayList<User> searchContactList=new ArrayList<>();
    private  ArrayList<Contact> ContactList = new ArrayList<>();
    private static final String TAG=ContactsSearchFragment.class.getSimpleName();
    public static String strsearch="";
    public static int serchcount=-1;

    public static ContactsSearchFragment newInstance() {
        ContactsSearchFragment fragment = new ContactsSearchFragment();
        return fragment;
    }

    @BindView(R.id.rec_user_list)RecyclerView rec_user_list;
    @BindView(R.id.edit_uname)EditText edit_uname;
    @BindView(R.id.txt_result)TextView txt_result;
    @BindView(R.id.txt_no_contact)TextView txt_no_contact;
    @BindView(R.id.prg_searchlist)ProgressBar prg_searchlist;
    @BindView(R.id.ll_search_contact_data)LinearLayout ll_search_contact_data;
    @BindView(R.id.ll_no_data)LinearLayout ll_no_data;
    @BindView(R.id.img_back)ImageView img_back;
    @BindView(R.id.imageVSearch)ImageView imageVSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_search_contact, container, false);
        mcontext=v.getContext();
        ButterKnife.bind(this,v);
        edit_uname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchContactFun();
                    return true;
                }
                return false;
            }
        });

        edit_uname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard(v,getActivity());
                }
            }
        });

        edit_uname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                try{
                    strsearch=s.toString();
                    serchcount=s.length();

                    if (s.length()>0){
                        if(searchContactList!=null && (!searchContactList.equals(""))){
                            if (searchContactList.size()>0)
                                filter(s.toString());
                        }
                    }
                    else {
                        searchContactList.clear();
                        adapter = null;
                        ll_search_contact_data.setVisibility(View.GONE);
                        ll_no_data.setVisibility(View.VISIBLE);
                        txt_no_contact.setVisibility(View.VISIBLE);
                        txt_no_contact.setText("Please search contact");
                        prg_searchlist.setVisibility(View.GONE);
                    }
                }catch (Exception ex){
                    ex.getMessage();
                }
            }
        });
        setSubcriber();
        return v;
    }
    @Optional
    @OnClick({R.id.imageVSearch})
    public void onSelectDeselect(View view) {
        switch (view.getId()) {
            case R.id.imageVSearch:
                if (edit_uname.getText().length()>0){
                    searchContactList.clear();
                    searchContactFun();
                }
                break;
        }
    }

    /**
     * this method is used for Search Contacts
     * @return void
     */

    private void searchContactFun(){
        try {
            String searchString = "";
            searchString = edit_uname.getText().toString().trim();
            Conversation conversation=new Conversation();
            conversation.getJsonObjectSearchAllContact(searchString,"contact");

            if (edit_uname.getText().length()>0) {
                searchContactList.clear();
                ll_search_contact_data.setVisibility(View.GONE);
                ll_no_data.setVisibility(View.VISIBLE);
                prg_searchlist.setVisibility(View.VISIBLE);
                txt_no_contact.setVisibility(View.GONE);
                noData();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    private void filter(String text) {

        ArrayList<User> temp = new ArrayList();
        for(User d: searchContactList){
            try {
                if(d.getcNm().contains(text)){
                    temp.add(d);
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
        adapter.filterList(temp);


    }


    @OnClick(R.id.img_back)
    public void img_back() {
        searchContactList.clear();
        edit_uname.setText("");
        backtoFragment();
    }

    @Override
    public boolean onBackPressed() {
        searchContactList.clear();
        edit_uname.setText("");
        backtoFragment();
        return true;
    }
    @Override
    public void onResume(){
        //ankita code
        try {
            if(strsearch.length()>0){
                edit_uname.setText(strsearch);
                searchContactFun();
            }
        }catch(Exception ex){

        }
        if(searchContactList.size()>0) {
            txt_no_contact.setVisibility(View.GONE);
            ll_no_data.setVisibility(View.GONE);
            ll_search_contact_data.setVisibility(View.VISIBLE);
            txt_result.setVisibility(View.VISIBLE);

        }
        super.onResume();
    }
    private void setAdapter()
    {
        rec_user_list.setLayoutManager(new LinearLayoutManager(mcontext));
        adapter = new SearchUserListAdapter(searchContactList,mcontext,ContactsSearchFragment.this);
        rec_user_list.setAdapter(adapter);
    }


    private void backtoFragment(){
        GlobalClass.setSearchAllContactResult(null);
        ChatListFragment.fromfrgament="contact";
        Fragment fragment= new FragmentHomeScreen();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_frag_container,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * this method is used for render data to UI
     * @return String
     */
    private void updateUi(String str){
        try{
            if (str.equals("searchContactComplete")){
                searchContactList.clear();
            }
            if (!(searchContactList.size() > 0)) {
                System.out.println("ffff" + GlobalClass.getSearchContact());
                JSONObject jbcontact = GlobalClass.getSearchContact();
                JSONArray jarray = jbcontact.getJSONArray("dataArray");
                for (int j = 0; j < jarray.length(); j++) {
                    boolean flag = false;
                    JSONObject jsonObject = jarray.getJSONObject(j);
                    if (jsonObject.has("USM_EMAIL")) {
                        flag = jsonObject.getString("USM_EMAIL").equals(GlobalClass.getUserId());
                    }
                    if (!flag) searchContactList.add(new User(jarray.getJSONObject(j)));
                }
            }

            Repository repo=Repository.getRepository();
            ContactList=(ArrayList<Contact>) repo.getAllContacts();
            if(searchContactList.size()>0){
                prg_searchlist.setVisibility(View.GONE);
                for (int i=0;i<searchContactList.size();i++){
                    for (int j=0;j<ContactList.size();j++){
                        if (ContactList.get(j).getOfficialEmail().equalsIgnoreCase(searchContactList.get(i).getcMail())){
                            searchContactList.get(i).setCmlAcceptedStatus(ContactList.get(j).getCmlAccepted());
                            break;
                        }
                    }
                }
                if (adapter!=null){
                    adapter.reloadList(searchContactList);
                    adapter.notifyDataSetChanged();
                }else {
                    if(serchcount>0)
                    {
                      setAdapter();
                    }
                }
            }
            noData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void noData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(serchcount==0){
                    searchContactList.clear();
                }
                if(serchcount>0){
                    txt_no_contact.setText("No Result found ");
                    ll_search_contact_data.setVisibility(searchContactList.size()>0?View.VISIBLE:View.GONE);
                    txt_result.setVisibility(searchContactList.size()>0?View.VISIBLE:View.GONE);
                    ll_no_data.setVisibility(searchContactList.size()>0?View.GONE:View.VISIBLE);
                    txt_no_contact.setVisibility(searchContactList.size()>0?View.GONE:View.VISIBLE);
                    prg_searchlist.setVisibility(View.GONE);
                }
            }
        },searchContactList.size()>0?0:7000);
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
        myObserver = new Observer<String>(){
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onNext(String string){
                Log.i(TAG+"string_next:",string);
                if(string!=null){
                    if(string.equals("searchContactComplete")||(string.equals("search_contact_done")
                            ||string.equals("fetchContactComplete")
                            ||(string.equals("no_contacts")))){
                        updateUi(string);
                    }
                }
            }
        };
        Subcription s=new Subcription();
        s.setObservable(mobservable);
        s.setObserver(myObserver);
        GlobalClass.setCurrentSubcriberr(s);
    }
    /**
     * this method is used for maintain visibility of keyboard
     */
    public static void hideKeyboard(View view,Activity mactivity) {
        hideSystemUI(mactivity);
        InputMethodManager inputMethodManager =(InputMethodManager)mactivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static  void hideSystemUI(Activity mactivity) {
        View decorView = mactivity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}