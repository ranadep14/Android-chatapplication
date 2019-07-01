package com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.agenda.AgendaFragment;

import butterknife.ButterKnife;

/**
 * Created by developers on 16/1/19.
 */

public class GroupTabFragment extends Fragment {
    public GroupTabFragment() {
        // Required empty public constructor
    }

    static Context mcontext;
    public static GroupTabFragment newInstance() {
        return new GroupTabFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_grouptab, container, false);
        ButterKnife.bind(this,view);
        mcontext=view.getContext();
        return view;
    }
}
