package com.cloudsinc.soulmobile.nativechatapplication.fragments.login;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.dialogs.CustomDialog;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.signup.OtpVerificationFragment;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setQuicksandMediumFont;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setQuicksandRegularFont;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class ResetPasswordFragment extends Fragment {
    @BindView(R.id.btn_send_code)Button btn_send_code;
    @BindView(R.id.txt_login)TextView txt_login;
    @BindView(R.id.txt_reset_password)TextView txt_reset_password;
    @BindView(R.id.txt_rst)TextView txt_rst;

    static Context mcontext;
    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        ButterKnife.bind(this,view);
        mcontext=view.getContext();

        txt_reset_password.setTypeface(setQuicksandRegularFont(mcontext));
        txt_rst.setTypeface(setQuicksandRegularFont(mcontext));
        txt_login.setTypeface(setQuicksandRegularFont(mcontext));
        btn_send_code.setTypeface(setQuicksandMediumFont(mcontext));

        return view;
    }

    @Optional
    @OnClick({R.id.btn_send_code,R.id.txt_login})
    public void onSelectDeselect(View view){
        //System.out.println("iiiiiiiiiiiiiiii" + view.getId());
        switch (view.getId()) {
            case R.id.btn_send_code:
                CustomDialog.dispDialogConfirmation(mcontext, ResetPasswordFragment.class, getString(R.string.after_send_code_done), false);
                ReplaceFragment.replaceFragment(ResetPasswordFragment.this,R.id.frame_frag_container, new OtpVerificationFragment(),true);
                break;case R.id.txt_login:
                ReplaceFragment.replaceFragment(ResetPasswordFragment.this,R.id.frame_frag_container, new LoginFragment(),true);
                break;
        }
    }
}