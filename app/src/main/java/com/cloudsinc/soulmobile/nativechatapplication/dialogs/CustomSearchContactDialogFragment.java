package com.cloudsinc.soulmobile.nativechatapplication.dialogs;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudsinc.soulmobile.nativechatapplication.App;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.ContactsListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.SearchContactListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.datamodels.User;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.utils.JsonObject;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.models.Conversation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
/**
 * Created by developers on 13/2/19.
 */
public class CustomSearchContactDialogFragment extends DialogFragment {

    View rootView;
    String TAG=this.getClass().getSimpleName();
    @BindView(R.id.rec_contact)RecyclerView rec_contact;
    @BindView(R.id.txt_no_contact)TextView txt_no_contact;
    @BindView(R.id.progress)ProgressBar progress;
    @BindView(R.id.img_close)ImageView img_close;

    public static Button btn_add;
    private Observable<String> mobservable;
    private Observer<String> myObserver;
    ArrayList<User> searchContactList;
    SearchContactListAdapter conAdapter;

    public static ArrayList<String> selected_user_arr;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.custom_search_contact_dialog, container,
                false);
        btn_add=rootView.findViewById(R.id.btn_add);
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ButterKnife.bind(this,rootView);
        selected_user_arr=new ArrayList<>();

        btn_add.setAlpha(0.5f);
        btn_add.setEnabled(false);
        Conversation conversation=new Conversation();
        conversation.getJsonObjectSearchAllContact(getArguments().getString("search_string"),"contact");


        setSubcriber();

        return rootView;
    }

    @OnClick(R.id.img_close)
    public void img_close()
    {
        dismiss();
    }

    @OnClick(R.id.btn_add)
    public void btn_add()
    {
        if(GlobalClass.getCurrentSubcriberr()!=null){
            GlobalClass.getCurrentSubcriberr().getObservable().just("userclickedmail")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(GlobalClass.getCurrentSubcriberr().getObserver());
        }
        dismiss();
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

                Log.d("error",e.getMessage());
            }

            @Override
            public void onNext(String string) {
                Log.i(TAG +"StringIs", string + " ..kk");
                if(string.equals("searchContactComplete") || string.equals("fetchContactComplete")
                        ||string.equals("search_contact_done")){
                    Log.i("onNext:", string);
                    updateUI();
                }
                if(string.contains("user_click"))
                {
                    checkAddButton();
                }

            }
        };
        com.nc.developers.cloudscommunicator.Subcription s = new com.nc.developers.cloudscommunicator.Subcription();
        s.setObservable(mobservable);
        s.setObserver(myObserver);
        GlobalClass.setLoginFragmentSubcriberr(s);
    }

    private void checkAddButton()
    {
        if(selected_user_arr.size()>0)
        {
            btn_add.setAlpha(1f);
            btn_add.setEnabled(true);
        }
        else {
            btn_add.setAlpha(0.5f);
            btn_add.setEnabled(false);
        }
    }

    private void updateUI()
    {
        /*searchContactList=new ArrayList<>();
        try {
            searchContactList.clear();
            if (!(searchContactList.size() > 0)) {
                JSONArray jsonArray = GlobalClass.getSearchContactList();
                if (jsonArray != null) {
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            boolean flag=false;
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            if (jsonObject.has("USM_EMAIL"))
                            {
                                flag=jsonObject.getString("USM_EMAIL").equals(GlobalClass.getUserId());
                            }
                            if(!flag)searchContactList.add(new User(jsonArray.getJSONObject(i)));
                        }
                    }
                }
            }
            Log.i("contactLst:", String.valueOf(searchContactList.size()) + " ..kk");
            if (searchContactList != null && searchContactList.size() > 0) {
                progress.setVisibility(View.GONE);
                txt_no_contact.setVisibility(View.GONE);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    rec_contact.setLayoutManager(mLayoutManager);
                    rec_contact.setItemAnimator(new DefaultItemAnimator());
                    conAdapter = new SearchContactListAdapter(getActivity(), searchContactList, CustomSearchContactDialogFragment.this);
                    rec_contact.setAdapter(conAdapter);


            } else {
                progress.setVisibility(View.GONE);
                txt_no_contact.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG,"-----------"+ex.getMessage());
        }
    */}

}



