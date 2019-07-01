package com.cloudsinc.soulmobile.nativechatapplication.fragments.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.interfaces.IOnBackPressed;
import com.cloudsinc.soulmobile.nativechatapplication.utils.DownloadFile;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;
import com.github.chrisbanes.photoview.PhotoView;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.models.Messages;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import jp.wasabeef.blurry.Blurry;

/**
 * Created by developers on 23/6/18.
 */

public class ImageExapndViewFragment extends Fragment implements IOnBackPressed{

    View view;
    Context mContext;
    ImageView imageLeftIcon;
    public static PhotoView imageViewExpand;
    TextView txtSenderName,txtSenderTime;
    public static boolean fromImageviewfr=false;
    public static Button btnDownload;
    public static ImageView imageview;
    public static LinearLayout liDownload;
    public static ImageExapndViewFragment newInstance() {
        return new ImageExapndViewFragment();
    }
    public static String strImage="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.image_view_fragment, container, false);
        mContext=view.getContext();

        imageLeftIcon = view.findViewById(R.id.img_left_icon_img);
        imageViewExpand=view.findViewById(R.id.imageViewExpand);
        txtSenderName = view.findViewById(R.id.txt_sender_name);
        txtSenderTime = view.findViewById(R.id.txt_sender_time);
        strImage= getArguments().getString("Image");
        String strSenderName = getArguments().getString("sender");
        String strSenderTime = getArguments().getString("time");
        String downloadstatus=getArguments().getString("download");
        final String key=getArguments().getString("key");

        txtSenderName.setText(strSenderName);
        txtSenderTime.setText(strSenderTime);
        Log.d("download",downloadstatus);

        Repository repo = Repository.getRepository();
        final Messages singleMsg1 = repo.getMessage(key);



        Glide.with(getActivity())
                .load(strImage)
                .thumbnail(Glide.with(getActivity()).load(R.drawable.clouds_icon))
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewExpand);


       /* if (downloadstatus.equals("true")) {

            Glide.with(getActivity())
                    .load(strImage)
                    .thumbnail(Glide.with(getActivity()).load(R.drawable.clouds_icon))
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewExpand);

        } else {

            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Bitmap bitmap;
                InputStream inputStream = new URL(strImage).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                Blurry.with(getActivity()).from(bitmap).into(imageview);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }*/



        imageLeftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment f=getFragmentManager().findFragmentById(R.id.frame_frag_container);
                if(f instanceof ImageExapndViewFragment)
                {
                    FragmentManager manager=getActivity().getSupportFragmentManager();
                    FragmentTransaction trans=manager.beginTransaction();
                    trans.remove(new ImageExapndViewFragment());
                    trans.commit();
                    manager.popBackStack();
                    fromImageviewfr=true;
                    ChatListFragment.showingimage=true;
                    ChatListFragment.rply=false;
                    Bundle bundle = new Bundle();
                    bundle.putString("group_json", ""+getArguments().getString("group_json"));
                    Fragment fragment = new ChatListFragment();
                    fragment.setArguments(bundle);
                    ReplaceFragment.replaceFragment(ImageExapndViewFragment.this, R.id.frame_frag_container, fragment, false);
                }
              }
        });

        return view;
    }

    @Override
    public boolean onBackPressed() {

        Fragment f=getFragmentManager().findFragmentById(R.id.frame_frag_container);
        if(f instanceof ImageExapndViewFragment)
        {
            FragmentManager manager=getActivity().getSupportFragmentManager();
            FragmentTransaction trans=manager.beginTransaction();
            trans.remove(new ImageExapndViewFragment());
            trans.commit();
            manager.popBackStack();
            fromImageviewfr=true;
            ChatListFragment.showingimage=true;
            ChatListFragment.rply=false;
            Bundle bundle = new Bundle();
            bundle.putString("group_json", ""+getArguments().getString("group_json"));
            Fragment fragment = new ChatListFragment();
            fragment.setArguments(bundle);
            ReplaceFragment.replaceFragment(ImageExapndViewFragment.this, R.id.frame_frag_container, fragment, false);

        }
        return true;
    }
}