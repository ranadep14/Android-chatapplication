package com.cloudsinc.soulmobile.nativechatapplication.fragments.signup;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.login.LoginFragment;
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
public class SignupFragment extends Fragment {
    public SignupFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.txt_mstorm_ac)TextView txt_mstorm_ac;
    @BindView(R.id.txt_login)TextView txt_login;
    @BindView(R.id.edit_fname)EditText edit_fname;
    @BindView(R.id.edit_lname)EditText edit_lname;
    @BindView(R.id.btn_next)Button btn_next;

    static Context mcontext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this,view);
        mcontext=view.getContext();
        txt_mstorm_ac.setTypeface(setQuicksandRegularFont(mcontext));
        txt_login.setTypeface(setQuicksandRegularFont(mcontext));
        edit_fname.setTypeface(setQuicksandRegularFont(mcontext));
        edit_lname.setTypeface(setQuicksandRegularFont(mcontext));
        btn_next.setTypeface(setQuicksandMediumFont(mcontext));

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String matchRegex = "[a-zA-z]+([a-zA-Z]+)*";

                String firstName = edit_fname.getText().toString();
                String lastName = edit_lname.getText().toString();

                if(firstName.isEmpty() && lastName.isEmpty()){
                    edit_fname.setError(getString(R.string.empty_firstname));
                    edit_lname.setError(getString(R.string.empty_lastname));
                    edit_lname.requestFocus();
                }

                if(lastName.isEmpty() && !(firstName.isEmpty()) && firstName.matches(matchRegex)){

                    edit_lname.setError(getString(R.string.empty_lastname));
                    edit_lname.requestFocus();
                }

                if(lastName.isEmpty() && !(firstName.isEmpty()) && !firstName.matches(matchRegex)){

                    edit_fname.setError(getString(R.string.invalid_firstname));
                    edit_lname.setError(getString(R.string.empty_lastname));
                    edit_fname.requestFocus();
                }

                if(firstName.isEmpty() && !lastName.isEmpty() && lastName.matches(matchRegex)){

                    edit_fname.setError(getString(R.string.empty_firstname));
                    edit_fname.requestFocus();
                }

                if(firstName.isEmpty() && !lastName.isEmpty() && !lastName.matches(matchRegex)){

                    edit_fname.setError(getString(R.string.empty_firstname));
                    edit_lname.setError(getString(R.string.invalid_lastname));
                    edit_lname.requestFocus();
                }

                if(!firstName.isEmpty() && !lastName.isEmpty()){

                    if(firstName.matches(matchRegex) && lastName.matches(matchRegex)){

                        Bundle bundle = new Bundle();
                        bundle.putString("firstname", firstName);
                        bundle.putString("lastname", lastName);
                        /*OtpVerificationFragment otpFragment = new OtpVerificationFragment();
                        otpFragment.setArguments(bundle);*/

                        Signup1Fragment signup1Fragment = new Signup1Fragment();
                        signup1Fragment.setArguments(bundle);

                        // code to go to next fragment
                        // ReplaceFragment.replaceFragment(SignupFragment.this,R.id.frame_frag_container, new Signup1Fragment(),false);
                        ReplaceFragment.replaceFragment(SignupFragment.this, R.id.frame_frag_container, signup1Fragment,true);
                    }

                    if(!(firstName.matches(matchRegex))){

                        edit_fname.setError(getString(R.string.invalid_firstname));
                        edit_fname.requestFocus();
                    }

                    if(!(lastName.matches(matchRegex))){

                        edit_lname.setError(getString(R.string.invalid_lastname));
                        edit_lname.requestFocus();
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    @Optional
    @OnClick({R.id.btn_next,R.id.txt_login})
    public void onSelectDeselect(View view){
        //System.out.println("iiiiiiiiiiiiiiii" + view.getId());

        switch (view.getId()) {

            /*case R.id.btn_next:
                ReplaceFragment.replaceFragment(SignupFragment.this,R.id.frame_frag_container, new Signup1Fragment(),false);
                break;*/
            case R.id.txt_login:
                ReplaceFragment.replaceFragment(SignupFragment.this,R.id.frame_frag_container, new LoginFragment(),true);
                //FragmentManager fragmentManager = getFragmentManager();
                //fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()-2).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }
    }
}
