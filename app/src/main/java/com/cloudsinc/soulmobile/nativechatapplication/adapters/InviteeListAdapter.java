package com.cloudsinc.soulmobile.nativechatapplication.adapters;

/**
 * Created by developers on 5/5/18.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudsinc.soulmobile.nativechatapplication.App;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.datamodels.User;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactProfileFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactsSearchFragment;
import com.cloudsinc.soulmobile.nativechatapplication.utils.CircleTransform;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.models.Invitee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Belal on 6/6/2017.
 */

public class InviteeListAdapter extends RecyclerView.Adapter<InviteeListAdapter.ViewHolder> {

    //private ArrayList<Invitee> inviteeList;
    private ArrayList<User> searchContactList;
    private Context mcontext;
    ArrayList<String> inviteesToAdd;
    Integer pos;
    ArrayList<User> addMemList = new ArrayList<>();
    // public  boolean pressedFlag[];


    public InviteeListAdapter(Context mcontext, ArrayList<User> searchContactList) {
        this.mcontext = mcontext;
        this.searchContactList = searchContactList;
       /* pressedFlag = new boolean[searchContactList.size()];
        for (int i=0;i<pressedFlag.length;i++){
            pressedFlag[i]=false;
        }*/

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_invitee_list_row, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try {
            final User user = searchContactList.get(position);
            //holder.textViewName.setText(invitee.getcNm());
            holder.checkbox_add_invitee.setChecked(false);
            holder.txt_usernm.setText(user.getcNm());
            holder.txt_mailid.setText(user.getcMail());

            // holder.checkbox_add_invitee.setText("Checkbox " + position);
            //holder.checkbox_add_invitee.setChecked(searchContactList.get(position).isIscheckSelected());
            //  holder.checkbox_add_invitee.setOnCheckedChangeListener(null);
            //   holder.checkbox_add_invitee.setChecked(pressedFlag[position]);

            holder.checkbox_add_invitee.setTag(position);
            holder.checkbox_add_invitee.setTag(position);
            holder.checkbox_add_invitee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = (Integer) holder.checkbox_add_invitee.getTag();

                    CheckBox rad_button = (CheckBox) v;
                    final int pos = (int) v.getTag();
                    if (rad_button.isChecked()) {
                        Log.d("select", "select");
                        searchContactList.get(pos).setIscheckSelected(true);
                        addMemList();
                        //pressedFlag[pos] = true;

                        if (GlobalClass.getCurrentSubcriberr() != null) {
                            GlobalClass.getCurrentSubcriberr().getObservable().just("userselected")
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(GlobalClass.getCurrentSubcriberr().getObserver());
                        }


                        // pressedFlag[pos] = false;
                    } else {

                        Log.d("unselect", "unselect");
                        searchContactList.get(pos).setIscheckSelected(false);
                        if (addMemList != null && addMemList.size() > 0) {

                            addMemList.remove(searchContactList.get(pos));

                            if (addMemList.size() < 1) {
                                if (GlobalClass.getCurrentSubcriberr() != null) {
                                    GlobalClass.getCurrentSubcriberr().getObservable().just("unselected")
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(GlobalClass.getCurrentSubcriberr().getObserver());
                                }
                            }
                        }

                    }


                }
            });

            if (searchContactList.get(position).isIscheckSelected()) {
                holder.checkbox_add_invitee.setChecked(true);
            } else {
                holder.checkbox_add_invitee.setChecked(false);
            }


        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return searchContactList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkbox_add_invitee;
        TextView txt_usernm, txt_mailid;

        ViewHolder(View itemView) {
            super(itemView);

            checkbox_add_invitee = (CheckBox) itemView.findViewById(R.id.checkbox_add_invitee);
            txt_usernm = (TextView) itemView.findViewById(R.id.txt_usernm);
            txt_mailid = (TextView) itemView.findViewById(R.id.txt_mailid);

        }
    }

    public void reloadList(ArrayList<User> list) {
        this.searchContactList = list;
    }

    public void addMemList() {
        addMemList = new ArrayList<>();
        addMemList.clear();
        for (int i = 0; i < searchContactList.size(); i++) {
            if (searchContactList.get(i).isIscheckSelected()) {
                addMemList.add(searchContactList.get(i));
                Log.d("addMemListinadpter", String.valueOf(addMemList));
            }
        }
        if (addMemList.size() > 0) {
            App.setAddMemList(addMemList);
            Log.d("setMemListinadpter", String.valueOf(App.getAddMemList()));

        }

    }


}