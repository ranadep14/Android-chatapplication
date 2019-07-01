package com.cloudsinc.soulmobile.nativechatapplication.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.models.Conversation;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment.selected_forward_arr;

/**
 * Created by developers on 21/2/18.
 */
public class ForwardMsgListAdapter extends RecyclerView.Adapter {

    private ArrayList<Conversation> targetConvertionList;
    public static ArrayList<Conversation> targetConvertionListselected;
    public static ArrayList<Conversation> forwardConvertionList;
    private boolean[] check_flag, check_flag1;
    private Context context;
    private Fragment mfragment;
    ArrayList<String> searchSelectedUserArr, searchSelectedUserArr1;
    ArrayList<String> tempsearchSelectedUserArr;
    public static int templstcount=0;

    public ForwardMsgListAdapter(Context contex, ArrayList<Conversation> contactList, Fragment mfragment) {
        this.targetConvertionList = contactList;
        this.context = contex;
        this.mfragment = mfragment;
        check_flag = new boolean[contactList.size()];
        check_flag1 = new boolean[contactList.size()];
        targetConvertionListselected=new ArrayList<>();
        forwardConvertionList=new ArrayList<>();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name,txt_status;
        ImageView img_profile;
        CheckBox rad_select;

        public MyViewHolder(View view) {
            super(view);
            img_profile = (ImageView) view.findViewById(R.id.img_profile);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_status = (TextView) view.findViewById(R.id.txt_status);
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

        //final ContactsListModel contactsListModel = targetConvertionList.get(position);
        final Conversation contactsListModel = targetConvertionList.get(position);

        ((MyViewHolder) holder).txt_name.setText(contactsListModel.getCmlTitle());
         /* searchSelectedUserArr = App.getSearchSelectedUserArr();


        if (templst.contains(contactsListModel.getCmlTitle())) {
            check_flag1[position] = true;

        }*/

        // Log.d("array", String.valueOf(templst));
        ((MyViewHolder) holder).rad_select.setTag(position);
        ((MyViewHolder) holder).rad_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox rad_button = (CheckBox) view;
                final int pos = (int) view.getTag();
                final String conv = targetConvertionList.get(pos).getCompleteData();
                targetConvertionListselected.add(targetConvertionList.get(pos));
                Log.i("conv", conv);
                if (rad_button.isChecked()) {
                    check_flag[pos] = true;
                    selected_forward_arr.add(conv);
                    forwardConvertionList.add(targetConvertionList.get(pos));
                    Log.i("beforeremovesize", String.valueOf(forwardConvertionList.size()));

                    Observable.just("forward")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(GlobalClass.getCurrentSubcriberr().getObserver());

                } else {
                    check_flag[pos] = false;

                    selected_forward_arr.remove(conv);
                    targetConvertionListselected.remove(targetConvertionList.get(pos));
                    forwardConvertionList.remove(targetConvertionList.get(pos));
                    Log.i("afterremovesize", String.valueOf(forwardConvertionList.size()));

                    Observable.just("remove")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(GlobalClass.getCurrentSubcriberr().getObserver());

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



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return targetConvertionList.size();
    }


}