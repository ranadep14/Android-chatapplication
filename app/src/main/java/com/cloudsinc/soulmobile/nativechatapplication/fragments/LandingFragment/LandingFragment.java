package com.cloudsinc.soulmobile.nativechatapplication.fragments.LandingFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.FragmentHomeScreen;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.ConversationLandingFragment;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class LandingFragment extends Fragment {
    public LandingFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment LandingFragment.
     */
    // TODO: Rename and change types and number of parameters
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_landing, container, false);
        ReplaceFragment.replaceFragment(LandingFragment.this,R.id.frame_frag_container, new FragmentHomeScreen(),false);
        //ReplaceFragment.replaceFragment(LandingFragment.this,R.id.frame_frag_container, new ConversationLandingFragment(),false);
       //ReplaceFragment.replaceFragment(LandingFragment.this,R.id.frame_frag_container, new CreateConversationFragment(),false);

        return view;
    }

}