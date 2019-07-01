package com.cloudsinc.soulmobile.nativechatapplication.adapters;

/**
 * This Adpter is used to display Searched user item data
 * @author Prajakta Patil
 * @version 1.0
 * @since 5.2.2019
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.dialogs.UploadFileDialog;
import com.nc.developers.cloudscommunicator.GlobalClass;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

import static com.cloudsinc.soulmobile.nativechatapplication.dialogs.UploadFileDialog.btn_upload;
import static com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment.uploadresponse;


public class UploadFileListAdapter extends RecyclerView.Adapter<UploadFileListAdapter.ViewHolder> {

    private ArrayList<String> uploadList;
    private Context mcontext;
    private Fragment mfragment;
    private static final String TAG=UploadFileListAdapter.class.getSimpleName();
    String path="",filename="";
    private Observable<String> mobservable;
    private Observer<String> myObserver;

    String upload_url="";

    int count=-1;

    public UploadFileListAdapter(ArrayList<String> uploadList, Context mcontext, Fragment mfragment)
    {
        this.uploadList = uploadList;
        this.mcontext = mcontext;
        this.mfragment = mfragment;
        setSubcriber();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_file_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


            String response = uploadList.get(position);

             holder.textViewName.setText(response.substring(response.lastIndexOf('/') + 1));

            Log.d("response",response);
            if(position==count)
            {
                Log.d("filecount", String.valueOf(count));
                holder.progressBarupload.setVisibility(View.GONE);

                if (count == uploadresponse.size() - 1) {

                    holder.progressBarupload.setVisibility(View.GONE);
                }

            }

        if (count == uploadresponse.size() - 1) {
            btn_upload.setEnabled(true);
            btn_upload.setAlpha(1f);
            holder.progressBarupload.setVisibility(View.GONE);
        }


    }
    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageView img_profile;
        ProgressBar progressBarupload;


        ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.txt_user_name);
            img_profile = (ImageView) itemView.findViewById(R.id.img_profile);
            progressBarupload=(ProgressBar)itemView.findViewById(R.id.progbar_file);


        }
    }


    private void setSubcriber() {

        mobservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        sub.onNext("");
                        sub.onCompleted();
                    }
                }
        );
        myObserver = new Observer<String>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String string) {

                Log.i("uploaddilog", string + " ..kk");
                Log.i("uploadresponse", uploadresponse + " ..kk");

                if(string.contains("upload_done"))
                {
                    upload_url=string;
                    notifyDataSetChanged();
                    Log.i("upload_url", upload_url + " ..kk");

                }

               count=Integer.parseInt(string);

                Log.i("count", count + " ..kk");

                if(count==uploadresponse.size()-1)
                {
                    btn_upload.setEnabled(true);
                    btn_upload.setAlpha(1f);
                }
                notifyDataSetChanged();



            }
        };

        com.nc.developers.cloudscommunicator.Subcription s = new com.nc.developers.cloudscommunicator.Subcription();
        s.setObservable(mobservable);
        s.setObserver(myObserver);
        GlobalClass.setLoginFragmentSubcriberr(s);
    }

}