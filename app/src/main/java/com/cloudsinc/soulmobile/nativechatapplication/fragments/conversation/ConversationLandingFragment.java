package com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.agenda.AgendaFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setRobotoRegularFont;

/**
 * This Fragment is used to display and Manage Conversation Tabs.
 * @author Prajakta Patil
 * @version 1.0
 * @since 2.11.2018
 */

public class ConversationLandingFragment extends Fragment implements ViewPager.OnPageChangeListener {

    @BindView(R.id.ll_msgs)LinearLayout ll_msgs;
    @BindView(R.id.ll_msg_slider)LinearLayout ll_msg_slider;
    @BindView(R.id.ll_requsts)LinearLayout ll_requsts;
    @BindView(R.id.ll_req_slider)LinearLayout ll_req_slider;
    @BindView(R.id.txt_msg_title)TextView txt_msg_title;
    @BindView(R.id.txt_req_title)TextView txt_req_title;

    private static LinearLayout ll_conversation_tabs;
    private static TextView txt_msg_count;
    private static TextView txt_req_count;
    static Context mcontext;
    public static ConversationLandingFragment newInstance() {
        return new ConversationLandingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation_landing, container, false);
        ButterKnife.bind(this,view);
        mcontext=view.getContext();
        ll_conversation_tabs = (LinearLayout)view.findViewById(R.id.ll_conversation_tabs);
        txt_msg_count = (TextView) view.findViewById(R.id.txt_msg_count);
        txt_req_count = (TextView) view.findViewById(R.id.txt_req_count);
        txt_msg_title.setTypeface(setRobotoRegularFont(mcontext));
        txt_msg_count.setTypeface(setRobotoRegularFont(mcontext));
        txt_req_title.setTypeface(setRobotoRegularFont(mcontext));
        txt_req_count.setTypeface(setRobotoRegularFont(mcontext));
        ll_msgs.performClick();
        return view;
    }
    @Optional
    @OnClick({R.id.ll_msgs,R.id.ll_requsts})
    public void onSelectDeselect(View view){
        switch (view.getId()) {
            case R.id.ll_msgs:
                ConversationListFragment.isConversationSearch = true;
                ContactListFragment.isContactSearch = false;
                setViewPos(0);
                ReplaceFragment.replaceFragment(ConversationLandingFragment.this, R.id.frm_con_container, ConversationListFragment.newInstance(), false);
                break;
            case R.id.ll_requsts:
                ConversationListFragment.isConversationSearch = false;
                setViewPos(1);
                ReplaceFragment.replaceFragment(ConversationLandingFragment.this, R.id.frm_con_container, GroupTabFragment.newInstance(), false);
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private void setViewPos(int pos){
        ll_msg_slider.setVisibility(pos==0?View.VISIBLE:View.INVISIBLE);
        ll_req_slider.setVisibility(pos==1?View.VISIBLE:View.INVISIBLE);
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        setViewPos(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public static void setMsgCount(int cnt){
        txt_msg_count.setText(""+cnt);
        txt_msg_count.setVisibility(cnt>0?View.VISIBLE:View.INVISIBLE);
    }
    public static void conTabsVisibility(boolean b){
        ll_conversation_tabs.setVisibility(b?View.VISIBLE:View.GONE);
    }
}