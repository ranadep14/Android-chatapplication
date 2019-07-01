package com.cloudsinc.soulmobile.nativechatapplication.fragments.chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.AllGallaryListAdapter;

import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.GallaryimgList;

public class GallaryFragment extends Fragment {

    RecyclerView gallAllMedia;
     private String mText = "";
    public static GallaryFragment createInstance(String txt)
    {
        GallaryFragment fragment = new GallaryFragment();
        fragment.mText = txt;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_all_gallary_item,container,false);

         gallAllMedia = (RecyclerView) v.findViewById(R.id.rec_all_gallary);

        AllGallaryListAdapter gallaryListAdapter1 = new AllGallaryListAdapter(GallaryimgList, getActivity());
        gallAllMedia.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        gallAllMedia.setAdapter(gallaryListAdapter1);

        return v;

    }


}
