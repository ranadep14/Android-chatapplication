package com.cloudsinc.soulmobile.nativechatapplication.adapters;

/**
 * This Adpter is used to display Searched user item data
 * @author Prajakta Patil
 * @version 1.0
 * @since 5.2.2019
 */

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.util.ArrayList;

import rx.Observable;
import rx.Observer;


public class AllDocumentListAdapter extends RecyclerView.Adapter<AllDocumentListAdapter.ViewHolder> {

    private ArrayList<String> GallaryList;
    private Context mcontext;
    private Fragment mfragment;
    private static final String TAG=AllDocumentListAdapter.class.getSimpleName();
    String path="",filename="";
    private Observable<String> mobservable;
    private Observer<String> myObserver;
    String url="";
    int count=-1;
    Dialog dialog;

    public AllDocumentListAdapter(ArrayList<String> GallaryList, Context mcontext)
    {
        this.GallaryList = GallaryList;
        this.mcontext = mcontext;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_document_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        url = GallaryList.get(position);



        String lastWord = url.substring(url.lastIndexOf("_")+1);

        Log.d("lastword",lastWord);

        holder.txt_document.setText(lastWord);

        holder.img_pdf.setTag(position);


        holder.img_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int pos = (int) v.getTag();
                String filepath=GallaryList.get(pos);

                if (filepath != null)

                {
                    File file = new File(filepath);

                    if (file.exists()) //Checking if the file exists or not
                    {
                        Uri path = Uri.fromFile(file);
                        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pdfOpenintent.setDataAndType(path, "application/pdf");
                        try {
                            mcontext.startActivity(pdfOpenintent);
                        } catch (ActivityNotFoundException e) {

                        }
                    } else {

                        Toast.makeText(mcontext, "The file not exists! ", Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });


    }


    @Override
    public int getItemCount() {

      return GallaryList.size();


    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_pdf;
        TextView txt_document;


        ViewHolder(View itemView) {
            super(itemView);

            img_pdf = (ImageView) itemView.findViewById(R.id.img_pdf);
            txt_document=(TextView)itemView.findViewById(R.id.txt_pdf_name);


        }
    }



}