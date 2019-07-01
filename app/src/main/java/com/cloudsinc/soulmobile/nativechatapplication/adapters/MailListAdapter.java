package com.cloudsinc.soulmobile.nativechatapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudsinc.soulmobile.nativechatapplication.App;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.datamodels.MailListModel;
import com.nc.developers.cloudscommunicator.GlobalClass;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.CreateConversationFragment.dialog;
import static com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.CreateConversationFragment.img_close;

/**
 * Created by developers on 21/2/18.
 */

public class MailListAdapter extends RecyclerView.Adapter<MailListAdapter.MyViewHolder>{

    private ArrayList<MailListModel> MailList;
    private Context context;
    public  static ArrayList<String>templst=new ArrayList<>();

    private InfoAdapterInterface adapterInterface;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_mail;
        ImageView img_delete;
        public MyViewHolder(View view) {
            super(view);
            img_delete = (ImageView) view.findViewById(R.id.img_delete);
            txt_mail = (TextView) view.findViewById(R.id.txt_mail);
        }
    }


    public MailListAdapter(Context contex, ArrayList<MailListModel> MailList,InfoAdapterInterface adapterInterface) {
        this.MailList = MailList;
        this.context = contex;
        this.adapterInterface = adapterInterface;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mail_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MailListModel mailListModel = MailList.get(position);
        holder.txt_mail.setText(mailListModel.geteMail());


     //ankita code

        templst.clear();
        for(int i=0;i<MailList.size();i++)
        { templst.add(MailList.get(i).geteMail()); }

        Log.d("temsize", String.valueOf(MailList.size()));
        App.setSearchAddUserArr(templst);


        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MailList.remove(position);
                //notifyDataSetChanged();
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,MailList.size());
                adapterInterface.OnItemClicked(MailList.size());

                //ankita code
                templst.clear();
                for(int i=0;i<MailList.size();i++)
                { templst.add(MailList.get(i).geteMail()); }

                App.getSearchSelectedUserArr().clear();

                Log.d("temsize", String.valueOf(templst));

                App.setSearchAddUserArr(templst);
                Observable.just("delete")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(GlobalClass.getCurrentSubcriberr().getObserver());


            }
        });

    }



    @Override
    public int getItemCount() {
        return MailList.size();
    }


    public interface InfoAdapterInterface{
        void OnItemClicked(int item_id);
    }

}
