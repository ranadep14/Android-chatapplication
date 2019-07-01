package com.cloudsinc.soulmobile.nativechatapplication.fragments.login;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.signup.SignupFragment;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setQuicksandMediumFont;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setQuicksandRegularFont;

/**
 * This Fragment is used to display Starting Screen
 * @author Prajakta Patil
 * @version 1.0
 * @since 2.11.2018
 */
public class WellcomeFragment extends Fragment {
    public WellcomeFragment() {
    }

    @BindView(R.id.btn_login)Button btn_login;
    @BindView(R.id.btn_signup)Button btn_signup;
    @BindView(R.id.txt_welcome)TextView txt_welcome;
    @BindView(R.id.txt_subtitile)TextView txt_subtitile;
    @BindView(R.id.txt_subtitile1)TextView txt_subtitile1;

    static Context mcontext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wellcome, container, false);

        ButterKnife.bind(this,view);
        if(getArguments()!=null)
        {
            if(getArguments().getString("call_for").equals("login"))btn_login.performClick();
        }
        mcontext=view.getContext();
        txt_welcome.setTypeface(setQuicksandRegularFont(mcontext));
        txt_subtitile.setTypeface(setQuicksandRegularFont(mcontext));
        txt_subtitile1.setTypeface(setQuicksandRegularFont(mcontext));
        btn_login.setTypeface(setQuicksandMediumFont(mcontext));
        btn_signup.setTypeface(setQuicksandRegularFont(mcontext));


        return view;
    }

    @Optional
    @OnClick({R.id.btn_login,R.id.btn_signup})
    public void onSelectDeselect(View view){

        switch (view.getId()) {
            case R.id.btn_login:
                ReplaceFragment.replaceFragment(WellcomeFragment.this,R.id.frame_frag_container, new LoginFragment(),true);
                break;
            case R.id.btn_signup:
                ReplaceFragment.replaceFragment(WellcomeFragment.this,R.id.frame_frag_container, new SignupFragment(),true);
                break;
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}