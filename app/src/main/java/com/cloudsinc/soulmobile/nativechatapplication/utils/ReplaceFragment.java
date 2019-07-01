package com.cloudsinc.soulmobile.nativechatapplication.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * Created by developers on 20/5/17.
 */
public class ReplaceFragment {
    public static void replaceFragment(Fragment mcontext, int frm_id, Fragment fragment, boolean isBackStack)
    {
        //hideKeyboard(mcontext.getActivity());
        FragmentTransaction transaction = mcontext.getFragmentManager().beginTransaction();
        transaction.replace(frm_id, fragment);
        if (isBackStack)
            transaction.addToBackStack(fragment.getClass().getName());
        transaction.addToBackStack(null);
        transaction.commit();
        /*try {

        }catch (Exception e){
            Log.e("Exception",""+e);
        }*/

    }
    public static void replaceFragmentWithRotation(Fragment mcontext, Fragment fragment)
    {
       /* FragmentTransaction transaction = mcontext.getFragmentManager().beginTransaction();
        transaction.replace(R.id.conta, fragment);
        transaction.commit();*/
      /*  hideKeyboard(mcontext.getActivity());
        if(App.isOrientationFlag())
        {
            FragmentTransaction transaction = mcontext.getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_right, fragment);
            transaction.commit();
        }
        else
        {
            FragmentTransaction transaction = mcontext.getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_middle, fragment);
            transaction.commit();
        }*/


    }
}
