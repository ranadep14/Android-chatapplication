package com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.ConversationListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.GroupTabFragment;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setRobotoRegularFont;

/**
 * This Fragment is used to display and manage contact tabs
 * @author Prajakta Patil
 * @version 1.0
 * @since 2.11.2018
 */
public class ContactLandingFragment extends Fragment implements ViewPager.OnPageChangeListener {
    @BindView(R.id.ll_contact)LinearLayout ll_contact;
    @BindView(R.id.ll_contact_slider)LinearLayout ll_contact_slider;
    @BindView(R.id.ll_groups)LinearLayout ll_groups;
    @BindView(R.id.ll_grp_slider)LinearLayout ll_grp_slider;
    @BindView(R.id.txt_contact_title)TextView txt_contact_title;
    @BindView(R.id.txt_grp_title)TextView txt_grp_title;

    static Context mcontext;
    private static LinearLayout ll_contact_tabs;
    public static ContactLandingFragment newInstance() {
        return new ContactLandingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_landing, container, false);
        ButterKnife.bind(this,view);
        mcontext=view.getContext();
        ll_contact_tabs = (LinearLayout)view.findViewById(R.id.ll_contact_tabs);
        txt_contact_title.setTypeface(setRobotoRegularFont(mcontext));
        txt_grp_title.setTypeface(setRobotoRegularFont(mcontext));
        ll_contact.performClick();
        return view;
    }
    @Optional
    @OnClick({R.id.ll_contact, R.id.ll_groups})
    public void onSelectDeselect(View view){
        switch (view.getId()) {
            case R.id.ll_contact:
                ConversationListFragment.isConversationSearch = false;
                ContactListFragment.isContactSearch = true;
                setViewPos(0);
                ReplaceFragment.replaceFragment(ContactLandingFragment.this, R.id.frm_contact_container, new ContactListFragment(), false);
                break;
            case R.id.ll_groups:
                ContactListFragment.isContactSearch = true;
                setViewPos(1);
                ReplaceFragment.replaceFragment(ContactLandingFragment.this, R.id.frm_contact_container, GroupTabFragment.newInstance(), false);
                break;
        }
    }

    private void setViewPos(int pos){
        ll_contact_slider.setVisibility(pos==0?View.VISIBLE:View.INVISIBLE);
        ll_grp_slider.setVisibility(pos==1?View.VISIBLE:View.INVISIBLE);
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

    public static void contactTabsVisibility(boolean b){
        ll_contact_tabs.setVisibility(b?View.VISIBLE:View.GONE);
    }


}