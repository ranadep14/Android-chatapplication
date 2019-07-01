package com.cloudsinc.soulmobile.nativechatapplication.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.UploadFileListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.models.Conversation;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment.nestedjsondata;
import static com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment.uploadfileresponse;
import static com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment.uploadresponse;

//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;

/**
 * This Fragment is used to display send and receive messages and attachment
 *
 * @author Prajakta Patil
 * @version 1.0
 * @since 2.11.2018
 */

public class UploadFileDialog extends DialogFragment {

    private Observable<String> mobservable;
    private Observer<String> myObserver;
    boolean isUsersMsg;
    View view;
    Context context;
    static Context mcontext;
    String TAG=this.getClass().getSimpleName();
    UploadFileListAdapter uploadAdapter;
    ArrayList<String> imglist=new ArrayList<>();
    ArrayList<String> extensionlst=new ArrayList<>();
    ArrayList<String> urlLst=new ArrayList<>();


    public UploadFileDialog() {
    }


    @BindView(R.id.rec_upload_file)
    RecyclerView rec_upload_file;


    @BindView(R.id.img_close)
    ImageView img_close;

    ProgressBar progressBarupload;

   public static Button btn_upload;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_upload_list, container, false);

        context = view.getContext();
        mcontext = view.getContext();

        btn_upload=(Button)view.findViewById(R.id.btn_upload);
        progressBarupload=(ProgressBar)view.findViewById(R.id.progbar_file);


        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setSubcriber();

        final LinearLayout root = new LinearLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ButterKnife.bind(this, view);

     /*   btn_upload.setEnabled(false);
        btn_upload.setAlpha(0.5f);

     */
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rec_upload_file.setLayoutManager(mLayoutManager);
        rec_upload_file.setItemAnimator(new DefaultItemAnimator());
        uploadAdapter = new UploadFileListAdapter(uploadresponse, getActivity(),
                UploadFileDialog.this);
        rec_upload_file.setAdapter(uploadAdapter);


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // ChatListFragment.dFragment=null;

                uploadresponse.clear();
                uploadfileresponse.clear();
                ChatListFragment.rply=false;
                ChatListFragment.showingimage=false;
                dismiss();

            }
        });

        Log.d("uploadresponse", String.valueOf(uploadresponse));


        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_upload.setVisibility(View.GONE);
                progressBarupload.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(nestedjsondata);

                    Conversation conversation = new Conversation();
                    conversation.setLinkupId("" + jsonObject.getString("LINKUP_ID"));
                    conversation.setOwnerId("" + jsonObject.getString("OWNER_ID"));
                    conversation.setSubKeyType("" + jsonObject.getString("SUB_KEY_TYPE"));

                    Log.d("nestedjsondata", String.valueOf(nestedjsondata));

                    conversation.getJSONObjectSendMessageAttchmnt(uploadresponse);
                }
                catch (Exception ex)
                {
                    Log.e(TAG,"-----------"+ex.getMessage());
                }


            }
        });

        return view;

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

                Log.i(TAG+"_onNxt:",string+" ..kk");


                if(string.contains("upload_done"))
                {
                    ChatListFragment.rply=false;
                    ChatListFragment.showingimage=false;
                    progressBarupload.setVisibility(View.GONE);

                 }

                if(string.equals("broadMsgStatus"))
                {
                    uploadresponse.clear();
                    dismiss();
                }

            }
        };

        com.nc.developers.cloudscommunicator.Subcription s = new com.nc.developers.cloudscommunicator.Subcription();
        s.setObservable(mobservable);
        s.setObserver(myObserver);
        GlobalClass.setSignupSubcription(s);
    }


}