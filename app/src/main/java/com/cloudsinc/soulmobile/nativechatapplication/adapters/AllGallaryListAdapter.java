package com.cloudsinc.soulmobile.nativechatapplication.adapters;

/**
 * This Adpter is used to display Searched user item data
 * @author Prajakta Patil
 * @version 1.0
 * @since 5.2.2019
 */

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;


public class AllGallaryListAdapter extends RecyclerView.Adapter<AllGallaryListAdapter.ViewHolder> {

    private ArrayList<String> GallaryList;
    private Context mcontext;
    private Fragment mfragment;
    private static final String TAG=AllGallaryListAdapter.class.getSimpleName();
    String path="",filename="";
    private Observable<String> mobservable;
    private Observer<String> myObserver;
    String url="";
    int count=-1;
    Dialog dialog;

    public AllGallaryListAdapter(ArrayList<String> GallaryList, Context mcontext)
    {
        this.GallaryList = GallaryList;
        this.mcontext = mcontext;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_gallary_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        url = GallaryList.get(position);

        try {
            Glide.with(mcontext).load(url)
                    .dontAnimate()
                    .placeholder(R.drawable.clouds_icon)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.img_gallery);

        } catch (Exception e) {
        }

        holder.img_gallery.setTag(position);

        holder.img_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int pos = (int) v.getTag();
                ImageView imageLeftIcon;
                PhotoView imageViewExpand;
                TextView txtSenderName,txtSenderTime;
                dialog = new Dialog(mcontext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.gallary_dialog);


                imageLeftIcon = dialog.findViewById(R.id.img_left_icon_img);
                imageViewExpand=dialog.findViewById(R.id.imageViewExpand);
                txtSenderName = dialog.findViewById(R.id.txt_sender_name);


                Glide.with(mcontext)
                        .load(GallaryList.get(pos))
                        .thumbnail(Glide.with(mcontext).load(R.drawable.clouds_icon))
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageViewExpand);

                imageLeftIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ChatListFragment.dFragment=null;
                        dialog.dismiss();

                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            }
        });


    }


    @Override
    public int getItemCount() {

      return GallaryList.size();


    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_gallery;


        ViewHolder(View itemView) {
            super(itemView);

            img_gallery = (ImageView) itemView.findViewById(R.id.img_gallery);


        }
    }



}