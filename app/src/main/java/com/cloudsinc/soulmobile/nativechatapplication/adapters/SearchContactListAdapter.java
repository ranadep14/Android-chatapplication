package com.cloudsinc.soulmobile.nativechatapplication.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudsinc.soulmobile.nativechatapplication.App;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.datamodels.User;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.FragmentHomeScreen;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactLandingFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.CreateConversationFragment;
import com.cloudsinc.soulmobile.nativechatapplication.utils.CircleTransform;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.models.Contact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cloudsinc.soulmobile.nativechatapplication.adapters.MailListAdapter.templst;
import static com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.CreateConversationFragment.arrayList;
import static com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.CreateConversationFragment.btn_add;
import static com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.CreateConversationFragment.dialog;
import static com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.CreateConversationFragment.img_close;
import static com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.CreateConversationFragment.selected_user_arr;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setRobotoRegularFont;

/**
 * Created by developers on 21/2/18.
 */
public class SearchContactListAdapter extends RecyclerView.Adapter {


    private ArrayList<User> contactList;
    private boolean[] check_flag, check_flag1;
    private Context context;
    private Fragment mfragment;
    ArrayList<String> searchSelectedUserArr, searchSelectedUserArr1;
    ArrayList<String> tempsearchSelectedUserArr;
    public static int templstcount=0;

    public SearchContactListAdapter(Context contex, ArrayList<User> contactList, Fragment mfragment) {
        this.contactList = contactList;
        this.context = contex;
        this.mfragment = mfragment;
        check_flag = new boolean[contactList.size()];
        check_flag1 = new boolean[contactList.size()];
        tempsearchSelectedUserArr = new ArrayList<>();
        searchSelectedUserArr1 = new ArrayList<>();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name, txt_mail, txt_status;
        ImageView img_profile;
        CheckBox rad_select;

        public MyViewHolder(View view) {
            super(view);
            img_profile = (ImageView) view.findViewById(R.id.img_profile);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_status = (TextView) view.findViewById(R.id.txt_status);
            txt_mail = (TextView) view.findViewById(R.id.txt_mail);
            rad_select = (CheckBox) view.findViewById(R.id.rad_select);
        }
    }

    public class LoadHolder extends RecyclerView.ViewHolder {
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        return new MyViewHolder(inflater.inflate(R.layout.search_contact_list_row, parent, false));

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        //final ContactsListModel contactsListModel = contactList.get(position);
        final User contactsListModel = contactList.get(position);

        ((MyViewHolder) holder).txt_name.setText(contactsListModel.getcNm());
        ((MyViewHolder) holder).txt_mail.setText(contactsListModel.getcMail());
         searchSelectedUserArr = App.getSearchSelectedUserArr();


        if (templst.contains(contactsListModel.getcMail())) {
            check_flag1[position] = true;


        }

        Log.d("array", String.valueOf(templst));
        ((MyViewHolder) holder).rad_select.setTag(position);
        ((MyViewHolder) holder).rad_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox rad_button = (CheckBox) view;
                final int pos = (int) view.getTag();
                final String mail_ids = contactList.get(pos).getcMail();

                Log.i("mail", mail_ids);
                if (rad_button.isChecked()) {
                    check_flag[pos] = true;

                    if (!searchSelectedUserArr.contains(mail_ids)) {
                        searchSelectedUserArr.add(mail_ids);

                       if(App.getSearchAddUserArr().size()>0)
                       {
                           for(int j=0;j<App.getSearchAddUserArr().size();j++)
                           {
                               searchSelectedUserArr.add(App.getSearchAddUserArr().get(j));
                           }
                       }
                        Set<String> set = new HashSet<>(searchSelectedUserArr);
                        searchSelectedUserArr.clear();
                        searchSelectedUserArr.addAll(set);

                        App.setSearchSelectedUserArr(searchSelectedUserArr);
                        tempsearchSelectedUserArr.add(mail_ids);

                    }

                    if (!selected_user_arr.contains(mail_ids)) {
                        selected_user_arr.add(mail_ids);
                        Observable.just("user_click")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(GlobalClass.getCurrentSubcriberr().getObserver());

                        templstcount=templst.size();
                      }


                    setEnable();
                } else {
                    if (searchSelectedUserArr.contains(mail_ids)) {
                        searchSelectedUserArr.remove(searchSelectedUserArr.indexOf(mail_ids));
                        App.setSearchSelectedUserArr(searchSelectedUserArr);
                        tempsearchSelectedUserArr.remove(mail_ids);
                    }
                    if (selected_user_arr.contains(mail_ids))
                        selected_user_arr.remove(selected_user_arr.indexOf(mail_ids));

                    check_flag[pos] = false;
                    setEnable();
                    Log.i("rmail", String.valueOf(searchSelectedUserArr));
                    Log.i("tempsearchSelectedUser", String.valueOf(tempsearchSelectedUserArr));
                    Log.i("rmail", String.valueOf(selected_user_arr));



                }
            }
        });

        if (check_flag1[position]) {

            ((MyViewHolder) holder).rad_select.setChecked(true);
            ((MyViewHolder) holder).rad_select.setClickable(false);

        } else {
             //((MyViewHolder) holder).rad_select.setChecked(false);
            if (check_flag[position]) {

                ((MyViewHolder) holder).rad_select.setChecked(true);

            }
            else
            {
                ((MyViewHolder) holder).rad_select.setChecked(false);
            }
        }




    }

    private void setEnable() {
        if (tempsearchSelectedUserArr.size() > 0) {
            btn_add.setAlpha(1f);
            btn_add.setEnabled(true);


        } else {
            btn_add.setAlpha(0.5f);
            btn_add.setEnabled(false);

        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }


}