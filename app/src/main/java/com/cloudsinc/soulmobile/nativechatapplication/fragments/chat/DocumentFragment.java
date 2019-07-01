package com.cloudsinc.soulmobile.nativechatapplication.fragments.chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.AllDocumentListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.AllGallaryListAdapter;

import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.GallaryimgList;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.GallarypdfList;

public class DocumentFragment extends Fragment {

    RecyclerView rclDocAll;
    private String mText = "";

    public static DocumentFragment createInstance(String txt)
    {
        DocumentFragment fragment = new DocumentFragment();
        fragment.mText = txt;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_all_gallary_item,container,false);

        rclDocAll = (RecyclerView) v.findViewById(R.id.rec_all_gallary);

        for(int i=0;i<GallarypdfList.size();i++)
        {
            Log.d("gallarypdf",GallarypdfList.get(i));

        }

        AllDocumentListAdapter gallaryListAdapter1 = new AllDocumentListAdapter(GallarypdfList, getActivity());
        rclDocAll.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        rclDocAll.setAdapter(gallaryListAdapter1);


        return v;

    }


}
