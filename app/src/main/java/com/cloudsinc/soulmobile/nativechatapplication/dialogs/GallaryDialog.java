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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.UploadFileListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ImageExapndViewFragment;
import com.github.chrisbanes.photoview.PhotoView;
import com.nc.developers.cloudscommunicator.GlobalClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

public class GallaryDialog extends DialogFragment {

    View view;
    Context mContext;
    ImageView imageLeftIcon;
    public static PhotoView imageViewExpand;
    TextView txtSenderName,txtSenderTime;
    public static String strImage="";

    public GallaryDialog() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.gallary_dialog, container, false);


        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        imageLeftIcon = view.findViewById(R.id.img_left_icon_img);
        imageViewExpand=view.findViewById(R.id.imageViewExpand);
        txtSenderName = view.findViewById(R.id.txt_sender_name);

        final LinearLayout root = new LinearLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ButterKnife.bind(this, view);

        strImage= getArguments().getString("Image");


        Glide.with(getActivity())
                .load(strImage)
                .thumbnail(Glide.with(getActivity()).load(R.drawable.clouds_icon))
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewExpand);

        imageLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // ChatListFragment.dFragment=null;
                dismiss();

            }
        });


        return view;

    }




}