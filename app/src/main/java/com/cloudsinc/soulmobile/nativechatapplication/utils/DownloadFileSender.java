package com.cloudsinc.soulmobile.nativechatapplication.utils;
/**
 * Created by developers on 19/2/19.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter;
import com.nc.developers.cloudscommunicator.Repository;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Async Task to download file from URL
 */
public class DownloadFileSender extends AsyncTask<String, String, String> {

    private ProgressDialog progressDialog;
    private String fileName;
    private String folder;
    private boolean isDownloaded;
    private Context mcontext;
    private RecyclerView.Adapter adapter;
    String keyval;

    public DownloadFileSender(Context mcontext,String keyval)
    {
        this.mcontext=mcontext;
        this.keyval=keyval;

    }

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progressDialog = new ProgressDialog(mcontext, R.style.progress_dialog_theme);
        this.progressDialog.setMessage("Downloading...........");
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection connection = url.openConnection();
            connection.connect();
            // getting file length
            int lengthOfFile = connection.getContentLength();


            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

            //Extract file name from URL
            fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

            //Append timestamp to file name
            fileName = timestamp + "_" + fileName;

            //External directory path to save file
            folder = Environment.getExternalStorageDirectory() + "/"+ "ChatApp"+"/";

            //Create androiddeft folder if it does not exist
            File directory = new File(folder);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Output stream to write file
            OutputStream output = new FileOutputStream(folder + fileName);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                // publishProgress("" + (int) ((total * 100) / lengthOfFile));
                //Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

            Repository repo=Repository.getRepository();
            Log.i("status_msg_keyval:",keyval+" ..kk");
            repo.insertImagePath(keyval,folder + fileName);

            return "Downloaded at: " + folder + fileName;

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return "Something went wrong";
    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        progressDialog.setProgress(Integer.parseInt(progress[0]));
    }


    @Override
    protected void onPostExecute(String message) {
        // dismiss the dialog after the file was downloaded
        this.progressDialog.dismiss();
       if(adapter!=null) adapter.notifyDataSetChanged();

    /*    if(ImageExapndViewFragment.strImage!=null ||!ImageExapndViewFragment.strImage.equals("")||!ImageExapndViewFragment.strImage.isEmpty()) {
            if (message.contains("Downloaded at:")) {
                btnDownload.setVisibility(View.GONE);
                liDownload.setVisibility(View.GONE);
                imageview.setVisibility(View.GONE);
                imageViewExpand.setVisibility(View.VISIBLE);
                Glide.with(mcontext)
                        .load(strImage)
                        .thumbnail(Glide.with(mcontext).load(R.drawable.loading_icon))
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageViewExpand);
            }
        }
        // Display File path after downloading

        ImageExapndViewFragment.strImage="";*/
     /*   Toast.makeText(mcontext,
               "Download Successfully", Toast.LENGTH_LONG).show();*/
    }
}