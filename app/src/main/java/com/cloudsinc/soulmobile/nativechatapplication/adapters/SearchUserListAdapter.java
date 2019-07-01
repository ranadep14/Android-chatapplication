package com.cloudsinc.soulmobile.nativechatapplication.adapters;

/**
 * This Adpter is used to display Searched user item data
 * @author Prajakta Patil
 * @version 1.0
 * @since 5.2.2019
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.GroupMemberListFrag;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.bumptech.glide.Glide;
import com.cloudsinc.soulmobile.nativechatapplication.App;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.datamodels.User;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactProfileFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactsSearchFragment;
import com.cloudsinc.soulmobile.nativechatapplication.utils.CircleTransform;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;
import com.nc.developers.cloudscommunicator.models.Contact;
import com.nc.developers.cloudscommunicator.objects.ConstantsObjects;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class SearchUserListAdapter extends RecyclerView.Adapter<SearchUserListAdapter.ViewHolder> {

    private ArrayList<User> searchContactList;
    private Context mcontext;
    private Fragment mfragment;
    private static final String TAG=SearchUserListAdapter.class.getSimpleName();
    private String uProfileImg = "www/public/msStream/images/defaultEventImage2.jpg";
    public SearchUserListAdapter(ArrayList<User> searchContactList, Context mcontext, Fragment mfragment)
    {
        this.searchContactList = searchContactList;
        this.mcontext = mcontext;
        this.mfragment = mfragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            final User user = searchContactList.get(position);
            holder.textViewName.setText(user.getcNm());
            holder.txt_mail.setText(user.getcMail());
            holder.txt_action.setText("Add");
            holder.txt_action.setAlpha(user.getCmlAcceptedStatus()==1?0.5f:1f);
            holder.txt_action.setEnabled(user.getCmlAcceptedStatus()==1?false:true);

            if(user.getcMail().equals(GlobalClass.getUserId())){
                holder.txt_action.setVisibility(View.GONE);
            }
            if(user.getContactObj().has("USM_DEFAULT_WORKSPACE")
                    && user.getContactObj().getJSONObject("USM_DEFAULT_WORKSPACE")!=null) {
                if (user.getContactObj().getJSONObject("USM_DEFAULT_WORKSPACE").has("imagePath")){
                    uProfileImg = user.getContactObj().getJSONObject("USM_DEFAULT_WORKSPACE").getString("imagePath");
                    Glide.with(mcontext).
                            load(GlobalClass.getLoginUrl() + "/" + uProfileImg)
                            .error(R.drawable.clouds_icon)
                            .placeholder(R.drawable.clouds_icon)
                            .transform(new CircleTransform(mcontext))
                            .into((ImageView)holder.img_profile);
                }
            }
            else{
                Glide.with(mcontext).
                        load(GlobalClass.getLoginUrl() + "/" + uProfileImg)
                        .error(R.drawable.clouds_icon)
                        .placeholder(R.drawable.clouds_icon)
                        .transform(new CircleTransform(mcontext))
                        .into((ImageView)holder.img_profile);
            }
            holder.txt_action.setTag(position);
            holder.txt_action.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                    int pos = (int) v.getTag();
                    App.setSearchSelectedUser(searchContactList.get(pos).getcMail());
                    JSONObject obj=user.getContactObj();
                    String personal= ConstantsObjects.ROLE_PERSONAL;
                    Log.d("personalSelect",personal);
                    Contact contact =new Contact();
                    contact.getJSONObjectAddContact(obj,personal);
                    searchContactList.get(pos).setCmlAcceptedStatus(3);
                    reloadList(searchContactList);
                    notifyDataSetChanged();


                }
            });
            holder.ll_search_row.setTag(position);
            holder.ll_search_row.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    ContactsSearchFragment.isSearchContac = true;
                    GroupMemberListFrag.isGroupMemberProfile = false;
                    int pos = (int) v.getTag();
                    JSONObject groupObject = searchContactList.get(pos).getContactObj();
                    Bundle bundle = new Bundle();
                    bundle.putString("contact_json", ""+groupObject);
                    Fragment fragment = new ContactProfileFragment();
                    fragment.setArguments(bundle);
                    ReplaceFragment.replaceFragment(mfragment, R.id.frame_frag_container, fragment, true);

                }
            });
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return searchContactList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView txt_mail;
        TextView txt_action;
        ImageView img_profile;
        LinearLayout ll_search_row;
        ProgressBar prog_addcontact;

        ViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.txt_user_name);
            txt_mail = (TextView) itemView.findViewById(R.id.txt_mail);
            txt_action = (TextView) itemView.findViewById(R.id.txt_action);
            img_profile = (ImageView) itemView.findViewById(R.id.img_profile);
            ll_search_row = (LinearLayout) itemView.findViewById(R.id.ll_search_row);
            prog_addcontact = (ProgressBar) itemView.findViewById(R.id.prog_addcontact);

        }
    }
    public void filterList(ArrayList<User> filterdNames) {
        this.searchContactList = filterdNames;
        notifyDataSetChanged();
    }

    public void reloadList(ArrayList<User> list){
        this.searchContactList = list;
    }

}