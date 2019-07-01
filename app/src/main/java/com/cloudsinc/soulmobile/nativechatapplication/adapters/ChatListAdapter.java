package com.cloudsinc.soulmobile.nativechatapplication.adapters;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cloudsinc.soulmobile.nativechatapplication.App;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.datamodels.ChatListModel;
import com.cloudsinc.soulmobile.nativechatapplication.dialogs.CustomDialog;
import com.cloudsinc.soulmobile.nativechatapplication.dialogs.NestedChatListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ImageExapndViewFragment;

import com.cloudsinc.soulmobile.nativechatapplication.utils.BlurTransformation;
import com.cloudsinc.soulmobile.nativechatapplication.utils.DateClass;
import com.cloudsinc.soulmobile.nativechatapplication.utils.DownloadFile;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.models.Conversation;
import com.nc.developers.cloudscommunicator.models.Messages;
import com.nc.developers.cloudscommunicator.socket.JsonParserClass;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.VISIBLE;
import static com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment.img_right_star;
import static com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment.replyConvArrayList;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setRobotoRegularFont;

/**
 * This Fragment is used to display send and receive messages and attachment item data
 *
 * @author Prajakta Patil
 * @version 1.0
 * @since 14.11.2018
 */
public class ChatListAdapter extends RecyclerView.Adapter {

    public static int selectedCount = 0;
    public static String selectedstarmsg = "";
    private ArrayList<ChatListModel> ChatList;
    Context mContext;
    int total_types;
    public static Dialog fraDialog;
    ChatListModel chatListModel = null;
    public static String fileName = "";
    private String m = "";
    private Fragment mfragment;
    String group_json = "";
    ChatListAdapter chatListAdapter;
    private static final String TAG = ChatListAdapter.class.getSimpleName();
    public static Conversation conversation = null;
    Messages messages;
    public static ArrayList<Messages> conversationArrayList = new ArrayList<>();
    public static ArrayList<Messages> starmsgArrayList = new ArrayList<>();
    public static ArrayList<String> starmsgList = new ArrayList<>();
    public static ArrayList<String> GallaryimgList = new ArrayList<>();
    public static ArrayList<String> GallarypdfList = new ArrayList<>();
    public static ArrayList<String> unstarmsgList = new ArrayList<>();
    public static ArrayList<Messages> forwardArrayList = new ArrayList<>();
    public static String keyval = "";
    public static int count = 0, replycounterback = 0;
    public static boolean download = false;
    int pos = 0;
    String localpath1 = "";
    public static String rplyMsg = "", msg = "";
    public static int rply_pos = -1, reply_pos, image_pos;

    int row_index = -1;
    public static JSONObject rplyJsonObj, forwardJsonobj, starmsg, replynestedmsg;
    boolean selectStar = false;

    //int selectedState[];
    public ChatListAdapter(ArrayList<ChatListModel> ChatList, Context context, Fragment fragment, String group_json) {
        this.ChatList = ChatList;
        this.mContext = context;
        total_types = ChatList.size();
        this.mfragment = fragment;
        this.group_json = group_json;
    }

    public void reload(ArrayList<ChatListModel> ChatList) {
        this.ChatList = ChatList;
    }

    public void filterList(ArrayList<ChatListModel> filterdNames) {
        this.ChatList = filterdNames;
        notifyDataSetChanged();
    }


    public static class MsgInViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_chat, ll_msg, li_download, li_download_doc, li_download_zip;
        TextView txt_time;
        TextView txt_sender_name;
        EmojiconTextView txt_msg;
        ImageView img_msg_status, img_reply_msg, img_star;
        ImageView img_from_pic, img_pdf, img_zip;
        Button btn_download, btnDownload_doc, btnDownload_zip;
        RelativeLayout rel_image_in, rl_reply, rel_image_doc, rel_image_zip;
        ProgressBar progress_bar;
        TextView txt_reply;


        public MsgInViewHolder(View itemView) {
            super(itemView);

            this.ll_chat = (LinearLayout) itemView.findViewById(R.id.ll_chat);
            this.rel_image_in = (RelativeLayout) itemView.findViewById(R.id.rel_image_in);
            this.txt_msg = (EmojiconTextView) itemView.findViewById(R.id.txt_msg);
            this.ll_msg = (LinearLayout) itemView.findViewById(R.id.msg_square);
            this.txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            this.img_msg_status = (ImageView) itemView.findViewById(R.id.img_msg_status);
            this.img_from_pic = (ImageView) itemView.findViewById(R.id.img_from_pic);
            this.btn_download = (Button) itemView.findViewById(R.id.btnDownload);
            this.btnDownload_doc = (Button) itemView.findViewById(R.id.btnDownload_doc);
            this.btnDownload_zip = (Button) itemView.findViewById(R.id.btnDownload_zip);
            this.progress_bar = itemView.findViewById(R.id.progress_bar_in);
            this.txt_sender_name = (TextView) itemView.findViewById(R.id.txt_sender_name);
            this.li_download = (LinearLayout) itemView.findViewById(R.id.li_download);
            this.txt_reply = (TextView) itemView.findViewById(R.id.txt_reply_msg);
            this.img_reply_msg = (ImageView) itemView.findViewById(R.id.img_reply);
            this.rl_reply = (RelativeLayout) itemView.findViewById(R.id.rl_reply);
            this.img_pdf = (ImageView) itemView.findViewById(R.id.img_pdf);
            this.img_zip = (ImageView) itemView.findViewById(R.id.img_zip);
            this.rel_image_doc = (RelativeLayout) itemView.findViewById(R.id.rel_image_doc);
            this.rel_image_zip = (RelativeLayout) itemView.findViewById(R.id.rel_image_zip);
            this.li_download_doc = (LinearLayout) itemView.findViewById(R.id.li_download_doc);
            this.li_download_zip = (LinearLayout) itemView.findViewById(R.id.li_download_zip);
            this.img_star = (ImageView) itemView.findViewById(R.id.img_msg_star);


        }
    }

    public static class MsgOutViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_chat;
        TextView txt_time;
        EmojiconTextView txt_msg;
        ImageView img_msg_status, img_reply_msg;
        ImageView img_to_pic, img_pdf, img_zip, img_star;
        TextView txt_sender_name;
        RelativeLayout rel_image_out, rl_reply_out;
        Button btn_download;
        TextView txt_reply_out;


        public MsgOutViewHolder(View itemView) {
            super(itemView);
            this.ll_chat = (LinearLayout) itemView.findViewById(R.id.ll_chat);
            this.txt_msg = (EmojiconTextView) itemView.findViewById(R.id.txt_msg);
            this.txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            this.img_msg_status = (ImageView) itemView.findViewById(R.id.img_msg_status);
            this.img_to_pic = (ImageView) itemView.findViewById(R.id.img_to_pic);
            this.txt_sender_name = (TextView) itemView.findViewById(R.id.txt_sender_name);
            this.rel_image_out = (RelativeLayout) itemView.findViewById(R.id.rel_image_out);
            this.btn_download = (Button) itemView.findViewById(R.id.btnDownload);
            this.txt_reply_out = (TextView) itemView.findViewById(R.id.txt_reply_msg);
            this.img_reply_msg = (ImageView) itemView.findViewById(R.id.img_reply);
            this.rl_reply_out = (RelativeLayout) itemView.findViewById(R.id.rl_reply);
            this.img_pdf = (ImageView) itemView.findViewById(R.id.img_pdf);
            this.img_zip = (ImageView) itemView.findViewById(R.id.img_zip);
            this.img_star = (ImageView) itemView.findViewById(R.id.img_msg_star);


        }
    }


    public static class MsgOfflineViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_chat;
        TextView txt_time;
        EmojiconTextView txt_msg;
        ImageView img_msg_status, img_reply_msg;
        ImageView img_to_pic, img_pdf, img_zip, img_star;
        TextView txt_sender_name;
        RelativeLayout rel_image_out, rl_reply_out;
        Button btn_download;
        TextView txt_reply_out;


        public MsgOfflineViewHolder(View itemView) {
            super(itemView);
            this.ll_chat = (LinearLayout) itemView.findViewById(R.id.ll_chat);
            this.txt_msg = (EmojiconTextView) itemView.findViewById(R.id.txt_msg);
            this.txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            this.img_msg_status = (ImageView) itemView.findViewById(R.id.img_msg_status);
            this.img_to_pic = (ImageView) itemView.findViewById(R.id.img_to_pic);
            this.txt_sender_name = (TextView) itemView.findViewById(R.id.txt_sender_name);
            this.rel_image_out = (RelativeLayout) itemView.findViewById(R.id.rel_image_out);


        }
    }

    public static class MsgInfoViewHolder extends RecyclerView.ViewHolder {

        TextView txt_msg_info;

        public MsgInfoViewHolder(View itemView) {
            super(itemView);

            this.txt_msg_info = (TextView) itemView.findViewById(R.id.txt_msg_info);
        }
    }

    public static class MsgEventViewHolder extends RecyclerView.ViewHolder {

        TextView txt_msg_info, txt_date, txt_month, txt_time,txt_sender_name;

        public MsgEventViewHolder(View itemView) {
            super(itemView);

            this.txt_msg_info = (TextView) itemView.findViewById(R.id.txt_msg_info);
            this.txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            this.txt_month = (TextView) itemView.findViewById(R.id.txt_month);
            this.txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            this.txt_sender_name = (TextView) itemView.findViewById(R.id.txt_sender_name);



        }
    }


    public static class MsgEventInViewHolder extends RecyclerView.ViewHolder {

        TextView txt_msg_info,txt_date, txt_month, txt_time,txt_sender_name;

        public MsgEventInViewHolder(View itemView) {
            super(itemView);

            this.txt_msg_info = (TextView) itemView.findViewById(R.id.txt_msg_info);
            this.txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            this.txt_month = (TextView) itemView.findViewById(R.id.txt_month);
            this.txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            this.txt_sender_name = (TextView) itemView.findViewById(R.id.txt_sender_name);


        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        switch (viewType) {
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_in_row, parent, false);
                return new MsgInViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_out_row, parent, false);
                return new MsgOutViewHolder(view);
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_info_row, parent, false);
                return new MsgInfoViewHolder(view);

            case 4:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_offline_row, parent, false);
                return new MsgOfflineViewHolder(view);
            case 5:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_event_out_row, parent, false);
                return new MsgEventViewHolder(view);

            case 6:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_event_in_row, parent, false);
                return new MsgEventInViewHolder(view);

        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (ChatList.get(position).getViewType()) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            default:
                return -1;
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        if (listPosition < ChatList.size()) chatListModel = ChatList.get(listPosition);

        if (ChatList.size() == 1 || listPosition == ChatList.size() - 1) {
            if (CustomDialog.progress != null) {
                if (CustomDialog.progress.isShowing()) {
                    CustomDialog.progress.dismiss();
                }
            }
        }


        // JSONObject object = chatListModel.getJsonMessage();
        if (chatListModel != null) {
            System.out.println("******************************viewtype" + chatListModel.getViewType());

            switch (chatListModel.getViewType()) {
                case 1:
                    try {
                        ((MsgInViewHolder) holder).txt_msg.setTypeface(setRobotoRegularFont(mContext));
                        ((MsgInViewHolder) holder).txt_time.setTypeface(setRobotoRegularFont(mContext));
                        JSONObject jsonObjectMsg = chatListModel.getJsonMessage();
                        String msgCreatedOn, timeString, destitle1 = "", startmsg = "";
                        msgCreatedOn = timeString = "";
                        int rpcountin = 0;
                        if (jsonObjectMsg != null) {
                            Log.i("msgObject:", String.valueOf(jsonObjectMsg) + " ..kk");
                            msgCreatedOn = jsonObjectMsg.getString("CREATED_ON");
                            timeString = DateClass.changeDateFormatToTime(msgCreatedOn);
                            Log.i("msg_time:", timeString + " ..kk");
                            destitle1 = jsonObjectMsg.getString("CML_DESCRIPTION");
                            startmsg = jsonObjectMsg.getString("CML_STAR");

                        }


                        if (startmsg.equals("1")) {
                            ((MsgInViewHolder) holder).img_star.setVisibility(View.VISIBLE);
                        } else {
                            ((MsgInViewHolder) holder).img_star.setVisibility(View.GONE);
                        }

                        Log.d("star", startmsg);
                        if (destitle1.contains(".pdf")) {
                            Log.d("pdf", destitle1);
                            ((MsgInViewHolder) holder).img_pdf.setVisibility(View.VISIBLE);
                            ((MsgInViewHolder) holder).txt_msg.setVisibility(View.VISIBLE);
                            ((MsgInViewHolder) holder).rel_image_doc.setVisibility(View.VISIBLE);

                        } else {
                            ((MsgInViewHolder) holder).img_pdf.setVisibility(View.GONE);
                            ((MsgInViewHolder) holder).img_from_pic.setVisibility(View.VISIBLE);
                            ((MsgInViewHolder) holder).rel_image_doc.setVisibility(View.GONE);
                        }

                        if (destitle1.contains(".zip")) {

                            ((MsgInViewHolder) holder).img_zip.setVisibility(View.VISIBLE);
                            ((MsgInViewHolder) holder).txt_msg.setVisibility(View.VISIBLE);
                            ((MsgInViewHolder) holder).rel_image_zip.setVisibility(View.VISIBLE);

                        } else {
                            ((MsgInViewHolder) holder).img_zip.setVisibility(View.GONE);
                            ((MsgInViewHolder) holder).img_from_pic.setVisibility(View.VISIBLE);
                            ((MsgInViewHolder) holder).rel_image_zip.setVisibility(View.GONE);

                        }

                        ((MsgInViewHolder) holder).txt_time.setText(timeString);

                        ((MsgInViewHolder) holder).txt_msg.setText(chatListModel.getMsg());
                        ((MsgInViewHolder) holder).txt_sender_name.setText(Html.fromHtml(unescapeJava(split(chatListModel.getSender_name()))));
                        if (chatListModel.getStatus().equals("read"))
                            ((MsgInViewHolder) holder).img_msg_status.setImageResource(R.drawable.ic_done_all);
                        if (chatListModel.isSend_flag())
                            ((MsgInViewHolder) holder).img_msg_status.setImageResource(R.drawable.ic_done);
                        ((MsgInViewHolder) holder).rel_image_in.setVisibility(chatListModel.isMedia() ? VISIBLE : View.GONE);
                        ((MsgInViewHolder) holder).txt_msg.setVisibility(chatListModel.isMedia() ? View.GONE : View.VISIBLE);
                        ((MsgInViewHolder) holder).ll_chat.setBackgroundColor(chatListModel.isSelected() ? mContext.getResources().getColor(R.color.colorItemSelected) : mContext.getResources().getColor(R.color.colorTransparent));
                        ((MsgInViewHolder) holder).rl_reply.setVisibility(View.GONE);

                        //  Log.d("ismedia"+listPosition, String.valueOf(ChatList.get(listPosition).isMedia()));

                        ((MsgInViewHolder) holder).ll_chat.setTag(listPosition);


                        ((MsgInViewHolder) holder).ll_chat.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                int pos = (Integer) v.getTag();

                                selectItem(v, pos);
                               /* try {
                                    if(ChatList.get(pos).getJsonMessage().getString("CML_STAR").equals("1"))
                                    {
                                        selectStar=true;
                                    }
                                    else
                                    {
                                        selectStar=false;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
*/

                                //  JSONObject conversationJsonObj = ChatList.get(listPosition).getJsonMessage();
                                // conversationArrayList.add(JsonParserClass.parseMessagesJSONObject(conversationJsonObj));
                                return true;
                            }
                        });

                        try {
                            int rpcount = chatListModel.getJsonMessage().getInt("CML_COUNT");
                            Log.d("rpcount", String.valueOf(rpcount));

                            if (rpcount > 0) {
                                ((MsgInViewHolder) holder).rl_reply.setVisibility(VISIBLE);
                                ((MsgInViewHolder) holder).txt_reply.setText(String.valueOf(rpcount) + " Replies");

                            } else {
                                ((MsgInViewHolder) holder).rl_reply.setVisibility(View.GONE);

                            }

                        } catch (JSONException e) {
                        }


                        ((MsgInViewHolder) holder).ll_chat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int pos = (Integer) v.getTag();

                                if (rply_pos > -1) {
                                    rply_pos = -1;
                                    resetFlag();
                                    notifyDataSetChanged();
                                }

                                if (selectedCount > 0) {
                                    selectItem(v, pos);

                                } else {
                                    //Delete
                                }


/*

                                    if (selectStar) {
                                        try {
                                            if (ChatList.get(pos).getJsonMessage().getString("CML_STAR").equals("1"))
                                                selectItem(v, pos);
                                            else {
                                                Toast.makeText(mContext, "You can select Star msg", Toast.LENGTH_SHORT).show();

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        try {
                                            if (ChatList.get(pos).getJsonMessage().getString("CML_STAR").equals("0"))
                                                selectItem(v, pos);
                                            else {
                                                Toast.makeText(mContext, "You can select unStar msg", Toast.LENGTH_SHORT).show();

                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }


*/


                            }
                        });


                    } catch (Exception e) {
                        Log.e("txt_mesages", " ");
                    }

                    JSONObject jsonObjectMsg = null;
                    try {
                        String keyval = "";
                        Messages singleMsg = null;
                        Boolean iscml = false, iscml1 = false;
                        String parentid = "", parentid1 = "";
                        String localpath = "";
                        Repository repo;

                        try {
                            keyval = String.valueOf(ChatList.get(listPosition).getJsonMessage().get("KEY_VAL"));
                            repo = Repository.getRepository();

                            singleMsg = repo.getMessage(keyval);
                            localpath = singleMsg.getLocalImagePath();
                            iscml1 = singleMsg.isCmlIsReply();
                            parentid1 = singleMsg.getParentTaskId();


                        } catch (Exception e) {
                        }


                        iscml = iscml1;
                        parentid = parentid1;
                        // Log.i("iscml:", iscml + " ..kk");
                       /* try {

                            if (iscml != null) {

                                if (iscml == true) {
                                    String parent_msg = String.valueOf(ChatList.get(listPosition).getJsonMessage().get("CML_PARENT_MSG_TITLE"));
                                    String parent_created = String.valueOf(ChatList.get(listPosition).getJsonMessage().get("CML_PARENT_CREATED_BY"));
                                    ((MsgInViewHolder) holder).rl_reply.setVisibility(VISIBLE);
                                    ((MsgInViewHolder) holder).txt_reply.setText(Html.fromHtml(unescapeJava(split(parent_created) + ":" + parent_msg)));

                                } else {
                                    ((MsgInViewHolder) holder).rl_reply.setVisibility(View.GONE);
                                }
                            }
                        } catch (Exception e) {
                        }*/

                        ((MsgInViewHolder) holder).txt_reply.setTag(listPosition);
                        ((MsgInViewHolder) holder).txt_reply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                int rplypos = (int) v.getTag();
                                Observable.just("scrollposition")
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(GlobalClass.getCurrentSubcriberr().getObserver());

                                replynestedmsg = ChatList.get(listPosition).getJsonMessage();

                                Bundle bundle = new Bundle();
                                bundle.putString("json", "" + replynestedmsg);
                                bundle.putString("group_json", group_json);
                                replycounterback = replycounterback + 1;
                                ChatListFragment.rply = true;
                                reply_pos = listPosition;
                                Fragment fragment = new NestedChatListFragment();
                                fragment.setArguments(bundle);
                                ReplaceFragment.replaceFragment(mfragment, R.id.frame_frag_container, fragment, true);

                            }
                        });

                        // localpath=repo.getMessage(keyval).getLocalImagePath();

                        try {
                            if (singleMsg.getLocalImagePath() != null) {

                                Log.d("imagepath", singleMsg.getLocalImagePath());
                                File myDir = new File(singleMsg.getLocalImagePath());

                                if (myDir.exists()) {
                                    ((MsgInViewHolder) holder).btn_download.setVisibility(View.GONE);
                                    ((MsgInViewHolder) holder).li_download.setVisibility(View.GONE);

                                } else {
                                    ((MsgInViewHolder) holder).btn_download.setVisibility(View.VISIBLE);
                                    ((MsgInViewHolder) holder).li_download.setVisibility(View.VISIBLE);
                                }

                            } else {

                                ((MsgInViewHolder) holder).btn_download.setVisibility(View.VISIBLE);
                                ((MsgInViewHolder) holder).li_download.setVisibility(View.VISIBLE);


                            }
                        } catch (Exception e) {
                        }


                    } catch (Exception e) {
                    }


                    if ((!(chatListModel.getImg_url() == null))) {
                        ((MsgInViewHolder) holder).img_from_pic.setImageURI(null);


                        try {
                            try {
                                String keyval = String.valueOf(ChatList.get(listPosition).getJsonMessage().get("KEY_VAL"));

                                Repository repodoc = Repository.getRepository();
                                Log.i("message_keyVal:", keyval + " ..kk");
                                Messages singleMsgdoc = repodoc.getMessage(keyval);
                                String localpath = singleMsgdoc.getLocalImagePath();
                                Log.i("status_lcl_img_path:", localpath + " ..kk" + singleMsgdoc);

                                try {

                                    if (singleMsgdoc.getLocalImagePath() != null) {

                                        if (singleMsgdoc.getLocalImagePath().contains(".zip")) {
                                            File myDir = new File(singleMsgdoc.getLocalImagePath());
                                            if (myDir.exists()) {
                                                ((MsgInViewHolder) holder).li_download_zip.setVisibility(View.GONE);
                                            } else {
                                                ((MsgInViewHolder) holder).li_download_zip.setVisibility(View.VISIBLE);

                                            }
                                        }

                                        if (singleMsgdoc.getLocalImagePath().contains(".pdf")) {
                                            File myDir = new File(singleMsgdoc.getLocalImagePath());

                                            if (myDir.exists()) {
                                                ((MsgInViewHolder) holder).li_download_doc.setVisibility(View.GONE);
                                            } else {
                                                ((MsgInViewHolder) holder).li_download_doc.setVisibility(View.VISIBLE);

                                            }
                                        }


                                        File myDir = new File(singleMsgdoc.getLocalImagePath());
                                        Log.d("path", singleMsgdoc.getLocalImagePath());
                                        if (myDir.exists()) {

                                            Glide.with(mContext).load(chatListModel.getImg_url())
                                                    .dontAnimate()
                                                    .placeholder(R.drawable.clouds_icon)
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .into(((MsgInViewHolder) holder).img_from_pic);

                                        } else {

                                            Glide.with(mContext).load(chatListModel.getImg_url()).transform(new BlurTransformation(mContext))
                                                    .into(((MsgInViewHolder) holder).img_from_pic);

                                        }


                                    } else {


                                        Glide.with(mContext).load(chatListModel.getImg_url()).transform(new BlurTransformation(mContext))
                                                .into(((MsgInViewHolder) holder).img_from_pic);
                                    }
                                } catch (Exception e) {
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                        }


                        ((MsgInViewHolder) holder).btn_download.setClickable(true);
                        ((MsgInViewHolder) holder).btn_download.setTag(listPosition);
                        ((MsgInViewHolder) holder).btn_download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final int pos = (int) v.getTag();
                                //   JSONObject jsonObjectMsg = null;
                                try {
                                    keyval = String.valueOf(ChatList.get(pos).getJsonMessage().get("KEY_VAL"));
                                    Log.d("keyval", keyval);
                                    String pathImage = "";
                                    new DownloadFile(mContext, ChatListAdapter.this).execute(ChatList.get(pos).getImg_url());
                                    notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                   /*((MsgInViewHolder) holder).btn_download.setVisibility(View.GONE);
                                new DownloadImage(((MsgInViewHolder) holder).progress_bar, pos, pathImage, ((MsgInViewHolder) holder).img_from_pic).execute(ChatList.get(pos).getImg_url());
                                Log.e("loginImage", "" + ChatList.get(pos).getImg_url());*/
                            }
                        });


                        ((MsgInViewHolder) holder).btnDownload_doc.setClickable(true);
                        ((MsgInViewHolder) holder).btnDownload_doc.setTag(listPosition);
                        ((MsgInViewHolder) holder).btnDownload_doc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final int pos = (int) v.getTag();
                                //   JSONObject jsonObjectMsg = null;
                                try {
                                    keyval = String.valueOf(ChatList.get(pos).getJsonMessage().get("KEY_VAL"));
                                    Log.d("keyval", keyval);
                                    String pathImage = "";
                                    new DownloadFile(mContext, ChatListAdapter.this).execute(ChatList.get(pos).getImg_url());
                                    notifyDataSetChanged();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                   /*((MsgInViewHolder) holder).btn_download.setVisibility(View.GONE);
                                new DownloadImage(((MsgInViewHolder) holder).progress_bar, pos, pathImage, ((MsgInViewHolder) holder).img_from_pic).execute(ChatList.get(pos).getImg_url());
                                Log.e("loginImage", "" + ChatList.get(pos).getImg_url());*/
                            }
                        });


                        ((MsgInViewHolder) holder).btnDownload_zip.setClickable(true);
                        ((MsgInViewHolder) holder).btnDownload_zip.setTag(listPosition);
                        ((MsgInViewHolder) holder).btnDownload_zip.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final int pos = (int) v.getTag();
                                //   JSONObject jsonObjectMsg = null;
                                try {
                                    keyval = String.valueOf(ChatList.get(pos).getJsonMessage().get("KEY_VAL"));
                                    Log.d("keyval", keyval);
                                    String pathImage = "";
                                    new DownloadFile(mContext, ChatListAdapter.this).execute(ChatList.get(pos).getImg_url());
                                    notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                   /*((MsgInViewHolder) holder).btn_download.setVisibility(View.GONE);
                                new DownloadImage(((MsgInViewHolder) holder).progress_bar, pos, pathImage, ((MsgInViewHolder) holder).img_from_pic).execute(ChatList.get(pos).getImg_url());
                                Log.e("loginImage", "" + ChatList.get(pos).getImg_url());*/
                            }
                        });

                        ((MsgInViewHolder) holder).rel_image_doc.setTag(listPosition);
                        ((MsgInViewHolder) holder).rel_image_doc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int pos = (Integer) v.getTag();

                                Repository repo = Repository.getRepository();
                                Log.i("message_keyVal:", keyval + " ..kk");
                                try {
                                    Messages singleMsg = repo.getMessage(String.valueOf(ChatList.get(pos).getJsonMessage().get("KEY_VAL")));
                                    String filepath = singleMsg.getLocalImagePath();
                                    Log.i("filepath:", filepath + " ..kk");
                                    if (filepath != null) {
                                        File file = new File(filepath);

                                        if (file.exists()) //Checking if the file exists or not
                                        {
                                            Uri path = Uri.fromFile(file);
                                            Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                                            pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            pdfOpenintent.setDataAndType(path, "application/pdf");
                                            try {
                                                mContext.startActivity(pdfOpenintent);
                                            } catch (ActivityNotFoundException e) {

                                            }
                                        } else {

                                            Toast.makeText(mContext, "The file not exists! ", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    notifyDataSetChanged();

                                } catch (JSONException e) {
                                }

                            }
                        });
                        ((MsgInViewHolder) holder).rel_image_in.setTag(listPosition);
                        ((MsgInViewHolder) holder).rel_image_in.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                int pos = (Integer) v.getTag();
                                selectItem(v, pos);
                                return true;
                            }
                        });


                        ((MsgInViewHolder) holder).rel_image_in.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    int position = (int) v.getTag();
                                    if (selectedCount > 0) {
                                        selectItem(v, position);

                                    } else {
                                        String keyValue = " ";
                                        keyValue = ChatList.get(position).getJsonMessage().getString("KEY_VAL");
                                        String senderName = split(ChatList.get(position).getSender_name());
                                        String sendTime = ChatList.get(position).getJsonMessage().getString("CREATED_ON");
                                        String timeString = DateClass.changeDateFormatToTime(sendTime);
                                        String imagePathDb = "";

                                        count = count + 1;
                                        image_pos = position;
                                        String key = "";
                                        final int pos = (int) v.getTag();
                                        JSONObject jsonObjectMsg = null;
                                        try {
                                            key = String.valueOf(ChatList.get(listPosition).getJsonMessage().get("KEY_VAL"));
                                            Repository repo = Repository.getRepository();
                                            Messages singleMsg1 = repo.getMessage(key);
                                            localpath1 = singleMsg1.getLocalImagePath();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        ImageExapndViewFragment imageExapndViewFragment = new ImageExapndViewFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("Image", ChatList.get(position).getImg_url());

                                        if (localpath1 != null) {

                                            File myDir = new File(localpath1);
                                            if (myDir.exists()) {
                                                bundle.putString("download", "true");
                                                bundle.putString("key", key);
                                                bundle.putString("sender", senderName);
                                                bundle.putString("time", timeString);
                                                bundle.putString("group_json", group_json);
                                                imageExapndViewFragment.setArguments(bundle);
                                                Log.e("bundleImage", "" + bundle);

                                                ReplaceFragment.replaceFragment(mfragment, R.id.frame_frag_container, imageExapndViewFragment, true);

                                            } else {
                                                // bundle.putString("download", "false");
                                            }

                                        } else {
                                            //  bundle.putString("download", "false");
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }


                        });

                    }
                    break;
                case 2:
                    try {
                        ((MsgOutViewHolder) holder).txt_msg.setTypeface(setRobotoRegularFont(mContext));
                        ((MsgOutViewHolder) holder).txt_time.setTypeface(setRobotoRegularFont(mContext));
                        String senderName = chatListModel.getSender_name();
                        if (senderName != null) {
                            ((MsgOutViewHolder) holder).txt_sender_name.setText(Html.fromHtml(unescapeJava("You")));
                        }
                        String messageString = chatListModel.getMsg();

                        if (messageString != null) {

                            ((MsgOutViewHolder) holder).txt_msg.setText(messageString);
                        }
                        jsonObjectMsg = chatListModel.getJsonMessage();
                        String msgCreatedOn, timeString, destitle = "", startmsg = "";
                        int counter = 0;
                        msgCreatedOn = timeString = "";

                        int rpcount = 0;
                        if (jsonObjectMsg != null) {
                            Log.i("msgObjectout:", String.valueOf(jsonObjectMsg) + " ..kk");
                            msgCreatedOn = jsonObjectMsg.getString("CREATED_ON");
                            timeString = DateClass.changeDateFormatToTime(msgCreatedOn);
                            Log.i("msg_time:", timeString + " ..kk");
                            Log.i("msg_time:online", msgCreatedOn + " ..kk");

                            startmsg = jsonObjectMsg.getString("CML_STAR");
                            destitle = jsonObjectMsg.getString("CML_DESCRIPTION");

                        }

                        Log.i("status:", chatListModel.getStatus() + " ..kk");

                        ((MsgOutViewHolder) holder).img_msg_status.setImageResource(chatListModel.getStatus().equals("unread") ? R.drawable.ic_done : R.drawable.ic_done_all);
                        ((MsgOutViewHolder) holder).img_msg_status.setImageResource(chatListModel.getStatus().equals("no_status") ? R.drawable.ic_clock : R.drawable.ic_done_all);

                        ((MsgOutViewHolder) holder).rel_image_out.setVisibility(chatListModel.isMedia() ? VISIBLE : View.GONE);
                        ((MsgOutViewHolder) holder).txt_msg.setVisibility(chatListModel.isMedia() ? View.GONE : VISIBLE);
                        ((MsgOutViewHolder) holder).ll_chat.setBackgroundColor(chatListModel.isSelected() ? mContext.getResources().getColor(R.color.colorItemSelected) : mContext.getResources().getColor(R.color.colorTransparent));
                        ((MsgOutViewHolder) holder).txt_time.setText(timeString);
                        ((MsgOutViewHolder) holder).rl_reply_out.setVisibility(View.GONE);

                        if (startmsg.equals("1")) {
                            ((MsgOutViewHolder) holder).img_star.setVisibility(View.VISIBLE);
                        } else {
                            ((MsgOutViewHolder) holder).img_star.setVisibility(View.GONE);
                        }

                        if (destitle.contains(".pdf")) {
                            ((MsgOutViewHolder) holder).img_pdf.setVisibility(View.VISIBLE);
                            ((MsgOutViewHolder) holder).txt_msg.setVisibility(View.VISIBLE);
                            ((MsgOutViewHolder) holder).img_to_pic.setVisibility(View.GONE);

                        } else {
                            ((MsgOutViewHolder) holder).img_pdf.setVisibility(View.GONE);
                            ((MsgOutViewHolder) holder).img_to_pic.setVisibility(View.VISIBLE);

                        }

                        if (destitle.contains(".zip")) {

                            ((MsgOutViewHolder) holder).img_zip.setVisibility(View.VISIBLE);
                            ((MsgOutViewHolder) holder).txt_msg.setVisibility(View.VISIBLE);
                            ((MsgOutViewHolder) holder).img_to_pic.setVisibility(View.GONE);

                        } else {
                            ((MsgOutViewHolder) holder).img_zip.setVisibility(View.GONE);
                            ((MsgOutViewHolder) holder).img_to_pic.setVisibility(View.VISIBLE);

                        }

                        ((MsgOutViewHolder) holder).ll_chat.setTag(listPosition);

                        try {
                            rpcount = chatListModel.getJsonMessage().getInt("CML_COUNT");
                            Log.d("rpcount", String.valueOf(rpcount));

                            if (rpcount > 0) {
                                ((MsgOutViewHolder) holder).rl_reply_out.setVisibility(VISIBLE);
                                ((MsgOutViewHolder) holder).txt_reply_out.setText(String.valueOf(rpcount) + " Replies");

                            } else {
                                ((MsgOutViewHolder) holder).rl_reply_out.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            Log.e(TAG, "-----------" + e.getMessage());
                        }

                        try {
                            String keyval1 = String.valueOf(chatListModel.getJsonMessage().get("KEY_VAL"));

                            Repository repo = Repository.getRepository();
                            Messages singleOutMsg = repo.getMessage(keyval1);

                            Boolean iscml1 = singleOutMsg.isCmlIsReply();
                            String parentmsg1 = singleOutMsg.getParentTaskId();
                            String parent_msg1 = chatListModel.getJsonMessage().getString("CML_PARENT_MSG_TITLE");
                            String parent_created1 = String.valueOf(chatListModel.getJsonMessage().get("CML_PARENT_CREATED_BY"));

                           /* if (iscml1 != null) {
                                if (iscml1) {
                                    ((MsgOutViewHolder) holder).rl_reply_out.setVisibility(VISIBLE);
                                    ((MsgOutViewHolder) holder).txt_reply_out.setText(Html.fromHtml(unescapeJava(split(parent_created1) + ":" + parent_msg1)));

                                } else {
                                    ((MsgOutViewHolder) holder).rl_reply_out.setVisibility(View.GONE);
                                }
                            }*/


                        } catch (Exception e) {
                        }

                        ((MsgOutViewHolder) holder).txt_reply_out.setTag(listPosition);

                        ((MsgOutViewHolder) holder).txt_reply_out.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int rplypos = (int) v.getTag();
                                replynestedmsg = ChatList.get(listPosition).getJsonMessage();
                                Bundle bundle = new Bundle();
                                bundle.putString("json", "" + replynestedmsg);
                                bundle.putString("group_json", group_json);
                                replycounterback = replycounterback + 1;
                                reply_pos = rplypos;
                                Fragment fragment = new NestedChatListFragment();
                                fragment.setArguments(bundle);
                                ReplaceFragment.replaceFragment(mfragment, R.id.frame_frag_container, fragment, true);

                            }
                        });

                        ((MsgOutViewHolder) holder).img_pdf.setTag(listPosition);
                        ((MsgOutViewHolder) holder).img_pdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int pos = (Integer) v.getTag();

                                Repository repo = Repository.getRepository();
                                Log.i("message_keyVal:", keyval + " ..kk");
                                try {
                                    Messages singleMsg = repo.getMessage(String.valueOf(ChatList.get(pos).getJsonMessage().get("KEY_VAL")));
                                    String filepath = singleMsg.getLocalImagePath();

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
                                                mContext.startActivity(pdfOpenintent);
                                            } catch (ActivityNotFoundException e) {

                                            }
                                        } else {

                                            Toast.makeText(mContext, "The file not exists! ", Toast.LENGTH_SHORT).show();

                                        }

                                    } else {
                                        try {
                                            keyval = String.valueOf(ChatList.get(pos).getJsonMessage().get("KEY_VAL"));
                                            Log.d("keyval", keyval);
                                            String pathImage = "";
                                            new DownloadFiledoc(mContext, ChatListAdapter.this, pos).execute(ChatList.get(pos).getImg_url());


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }


                                } catch (JSONException e) {
                                }


                            }
                        });

                        ((MsgOutViewHolder) holder).ll_chat.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                int pos = (Integer) v.getTag();
                                selectItem(v, pos);
                                JSONObject conversationJsonObj = ChatList.get(listPosition).getJsonMessage();
                                conversationArrayList.add(JsonParserClass.parseMessagesJSONObject(conversationJsonObj));
                                return true;
                            }
                        });

                        ((MsgOutViewHolder) holder).ll_chat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int pos = (Integer) v.getTag();

                                if (rply_pos > -1) {
                                    rply_pos = -1;
                                    resetFlag();
                                    notifyDataSetChanged();
                                }


                                if (selectedCount > 0) {
                                    selectItem(v, pos);

                                }

                            }
                        });

                        System.out.println("*******image-url" + chatListModel.getImg_url() + "*****************" + chatListModel.isMedia());
                        if ((chatListModel.getImg_url() != null)) {


                            System.out.println("--------------------------" + chatListModel.getImg_url());
                            Glide.with(mContext).load(chatListModel.getImg_url())
                                    .dontAnimate()
                                    .placeholder(R.drawable.clouds_icon)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(((MsgOutViewHolder) holder).img_to_pic);

                            String data = String.valueOf(chatListModel.getJsonMessage());
                            Log.d("counter", data);

                            ((MsgOutViewHolder) holder).rel_image_out.setTag(listPosition);

                            ((MsgOutViewHolder) holder).rel_image_out.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {

                                    int pos = (Integer) v.getTag();
                                    selectItem(v, pos);
                                    JSONObject conversationJsonObj = ChatList.get(listPosition).getJsonMessage();
                                    conversationArrayList.add(JsonParserClass.parseMessagesJSONObject(conversationJsonObj));

                                    return true;
                                }
                            });

                            ((MsgOutViewHolder) holder).rel_image_out.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        int position = (int) v.getTag();
                                        if (selectedCount > 0) {
                                            selectItem(v, position);

                                        } else {
                                            String keyValue = " ";

                                            keyValue = ChatList.get(position).getJsonMessage().getString("KEY_VAL");
                                            //  String sendTime1 = ChatList.get(position).getTime();

                                            String sendTime1 = ChatList.get(position).getJsonMessage().getString("CREATED_ON");
                                            String timeString1 = DateClass.changeDateFormatToTime(sendTime1);

                                            String imagePathDb = "";
                                            JSONObject object = null;
                                            count = count + 1;
                                            image_pos = position;
                                            ImageExapndViewFragment imageExapndViewFragment = new ImageExapndViewFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("Image", ChatList.get(position).getImg_url());
                                            bundle.putString("sender", "You");
                                            bundle.putString("download", "true");
                                            bundle.putString("time", timeString1);
                                            bundle.putString("group_json", group_json);
                                            imageExapndViewFragment.setArguments(bundle);
                                            Log.e("bundleImage", "" + bundle);

                                            ReplaceFragment.replaceFragment(mfragment, R.id.frame_frag_container, imageExapndViewFragment, true);
                                        }
                                    } catch (Exception e) {
                                        Log.e("ImageUpload", "" + e.getMessage());
                                    }
                                }

                            });
                        }
                    } catch (Exception ex) {
                        Log.e("txtMsgOut", "-----" + ex.getMessage());
                    }


                    break;
                case 3:
                    try {
                        if (chatListModel.getJsonMessage().getInt("CML_CHT_MESSAGE_TYPE") == 5) {
                            //((MsgInfoViewHolder) holder).txt_msg_info.setBackground(mContext.getDrawable(R.drawable.msg_info_bg));
                            ((MsgInfoViewHolder) holder).txt_msg_info.setText(chatListModel.getJsonMessage().getString("CML_TITLE"));
                        } else {
                            ((MsgInfoViewHolder) holder).txt_msg_info.setText(chatListModel.getTime());
                        }
                    } catch (Exception ex) {
                        Log.e("InfoMessageSetError", "" + ex.getMessage());
                    }

                    break;


                case 4:
                    try {
                        ((MsgOfflineViewHolder) holder).txt_msg.setTypeface(setRobotoRegularFont(mContext));
                        ((MsgOfflineViewHolder) holder).txt_time.setTypeface(setRobotoRegularFont(mContext));
                        String senderName = chatListModel.getSender_name();
                        if (senderName != null) {
                            ((MsgOfflineViewHolder) holder).txt_sender_name.setText(Html.fromHtml(unescapeJava("You")));
                        }

                        String messageString = chatListModel.getMsg();

                        String timeString = DateClass.changeDateFormatToTimeForOffline(chatListModel.getTime());
                        if (messageString != null) {

                            ((MsgOfflineViewHolder) holder).txt_msg.setText(Html.fromHtml(unescapeJava(messageString)));
                        }
                        ((MsgOfflineViewHolder) holder).txt_time.setText(timeString);
                        Log.d("timeoff", "-----" + chatListModel.getTime());

                    } catch (Exception ex) {
                        Log.e("txtMsgOut", "-----" + ex.getMessage());
                    }

                    break;

                case 5:
                    try {

                        String timeString = chatListModel.getJsonMessage().getString("CREATED_ON");
                        ((MsgEventViewHolder) holder).txt_msg_info.setText(chatListModel.getJsonMessage().getString("CML_TITLE"));

                        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Date date = utcFormat.parse(timeString);
                        DateFormat pstFormat = new SimpleDateFormat("dd MMM yyyy");
                        pstFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

                        DateFormat pstFormat1 = new SimpleDateFormat("hh:mm aa");
                        pstFormat1.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

                        String s = pstFormat.format(date);
                        String[] data = s.split(" ");

                        ((MsgEventViewHolder) holder).txt_sender_name.setText(Html.fromHtml(unescapeJava("You")));

                        ((MsgEventViewHolder) holder).txt_month.setText(data[1]);
                        ((MsgEventViewHolder) holder).txt_date.setText(data[0]);

                        ((MsgEventViewHolder) holder).txt_time.setText(pstFormat1.format(date));
                        Log.i("calendereventtime", pstFormat1.format(date) + chatListModel.getJsonMessage().getString("CML_TITLE"));

                    } catch (Exception ex) {
                        Log.e("InfoMessageSetError", "" + ex.getMessage());
                    }

                    break;

                case 6:
                    try {



                        //((MsgInfoViewHolder) holder).txt_msg_info.setBackground(mContext.getDrawable(R.drawable.msg_info_bg));
                        ((MsgEventInViewHolder) holder).txt_msg_info.setText(chatListModel.getJsonMessage().getString("CML_TITLE"));

                        String timeString = chatListModel.getJsonMessage().getString("CREATED_ON");

                        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Date date = utcFormat.parse(timeString);
                        DateFormat pstFormat = new SimpleDateFormat("dd MMM yyyy");
                        pstFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

                        DateFormat pstFormat1 = new SimpleDateFormat("hh:mm aa");
                        pstFormat1.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

                        String s = pstFormat.format(date);
                        String[] data = s.split(" ");

                        ((MsgEventInViewHolder) holder).txt_month.setText(data[1]);
                        ((MsgEventInViewHolder) holder).txt_date.setText(data[0]);

                        ((MsgEventInViewHolder) holder).txt_time.setText(pstFormat1.format(date));

                        String senderName = split(chatListModel.getSender_name());
                        ((MsgEventInViewHolder) holder).txt_sender_name.setText(senderName);

                        Log.i("sendernameevent", senderName);


                    } catch (Exception ex) {
                        Log.e("InfoMessageSetError", "" + ex.getMessage());
                    }

                    break;
            }

            holder.setIsRecyclable(false);


        }


    }

    @Override
    public int getItemCount() {
        return ChatList.size();
    }


    public void selectReplyItem(View v) {
        // Log.d("rply_pos_pos", String.valueOf(rply_pos));
        resetFlag();
        addFlag(rply_pos);
        for (int i = 0; i < ChatList.size(); i++) {
            if (ChatList.get(i).isSelected()) {
                v.setBackgroundColor(mContext.getResources().getColor(R.color.colorItemSelected));
                //  Log.d("selected_pos" + i, String.valueOf(ChatList.get(i).isSelected()));
            } else {
                v.setBackgroundColor(mContext.getResources().getColor(R.color.colorTransparent));
            }
        }
    }


    private void selectItem(View v, int pos) {
        ArrayList<String> keyValList = new ArrayList<>();
        keyValList.clear();

        ChatList.get(pos).setSelected(!ChatList.get(pos).isSelected());

        v.setBackgroundColor(ChatList.get(pos).isSelected() ? mContext.getResources().getColor(R.color.colorItemSelected) : mContext.getResources().getColor(R.color.colorTransparent));
        //notifyDataSetChanged();

        ChatListFragment.li_reply.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ChatListFragment.li_msg_layout.setBackgroundResource(R.drawable.send_msg_bg);
            ChatListFragment.li_send_msg_back.setBackground(null);
        }
        selectedCount = 0;
        conversationArrayList.clear();
        forwardArrayList.clear();
        starmsgArrayList.clear();
        for (int i = 0; i < ChatList.size(); i++) {
            if (ChatList.get(i).isSelected()) {
                try {
                    selectedCount++;
                    keyValList.add(ChatList.get(i).getJsonMessage().getString("KEY_VAL") + "*" + ChatList.get(i).getJsonMessage().getString("CREATED_BY"));
                    //Log.d("MsgKeyVal",ChatList.get(i).getJsonMessage().getString("KEY_VAL"));
                    //  Log.d("DelMsgObj", ChatList.get(i).getJsonMessage().getString("KEY_VAL") + "*" + ChatList.get(i).getJsonMessage().getString("CREATED_BY"));

                    JSONObject conversationJsonObj = ChatList.get(i).getJsonMessage();
                    conversationArrayList.add(JsonParserClass.parseMessagesJSONObject(conversationJsonObj));
                    //  Log.d("conv", String.valueOf(conversationArrayList.size()));


                    try {
                        rplyMsg = split(ChatList.get(i).getSender_name());
                        msg = ChatList.get(i).getJsonMessage().getString("CML_DESCRIPTION");
                        selectedstarmsg = ChatList.get(i).getJsonMessage().getString("CML_STAR");

                        forwardJsonobj = ChatList.get(i).getJsonMessage();
                        starmsg = ChatList.get(i).getJsonMessage();
                        reply_pos = i;

                        rplyJsonObj = ChatList.get(i).getJsonMessage();
                        replyConvArrayList.clear();
                        replyConvArrayList.add(JsonParserClass.parseMessagesJSONObject(rplyJsonObj));
                        starmsgArrayList.add(JsonParserClass.parseMessagesJSONObject(starmsg));
                        //    Log.d("rconv", String.valueOf(replyConvArrayList.size()));

                    } catch (Exception e) {

                    }


                    for (int k = 0; k < starmsgArrayList.size(); k++) {
                        if (starmsgArrayList.get(k).getCmlStar() == 1) {
                            Drawable new_image = mContext.getResources().getDrawable(R.drawable.unstar);
                            new_image = DrawableCompat.wrap(new_image);
                            DrawableCompat.setTint(new_image, mContext.getResources().getColor(R.color.colorWhite));
                            img_right_star.setBackgroundDrawable(new_image);

                        } else {
                            Drawable new_image = mContext.getResources().getDrawable(R.drawable.star_fill);
                            new_image = DrawableCompat.wrap(new_image);
                            DrawableCompat.setTint(new_image, mContext.getResources().getColor(R.color.colorWhite));
                            img_right_star.setBackgroundDrawable(new_image);


                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }

        try {
            ChatListFragment.chatActinBarVisibility(selectedCount);
            App.setDelMsgKeyValList(keyValList);
            notifyItemChanged(pos);


        } catch (Exception ex) {
            Log.e("Exception", "" + ex.getMessage());
        }

    }


    public void resetFlag() {
        for (int i = 0; i < ChatList.size(); i++) {
            ChatList.get(i).setSelected(false);

        }

        try {
            //Log.d("counter", String.valueOf(selectedCount));
            selectedCount = 0;
            ChatListFragment.chatActinBarVisibility(selectedCount);
            notifyDataSetChanged();
        } catch (Exception ex) {
            Log.e("Exception", "" + ex.getMessage());
        }

    }

    public void addFlag(int pos) {

        ChatList.get(pos).setSelected(true);

    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    private static String unescapeJava(String escaped) {

        if (escaped.indexOf("\\u") == -1)
            return escaped;

        String processed = "";

        int position = escaped.indexOf("\\u");
        while (position != -1) {
            if (position != 0)
                processed += escaped.substring(0, position);
            String token = escaped.substring(position + 2, position + 6);
            escaped = escaped.substring(position + 6);
            processed += (char) Integer.parseInt(token, 16);
            position = escaped.indexOf("\\u");
        }
        processed += escaped;

        return processed;
    }


    public String split(String str) {

        String s = "";
        if (str.split("@")[0].contains(".")) {
            s = str.split("@")[0].split("\\.")[0] + " " + str.split("@")[0].split("\\.")[1];
            Log.e("IfPart", "");
        } else {
            s = str.split("@")[0];
            Log.e("elsePart", "");
        }
        return s;

    }


    public class DownloadFiledoc extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;
        private Context mcontext;
        private RecyclerView.Adapter adapter;
        int posdownload;

        public DownloadFiledoc(Context mcontext, RecyclerView.Adapter adapter, int posdownload) {
            this.mcontext = mcontext;
            this.adapter = adapter;
            this.posdownload = posdownload;
        }

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* this.progressDialog = new ProgressDialog(mcontext, R.style.progress_dialog_theme);
            this.progressDialog.setMessage("Downloading...........");
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();*/
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
                folder = Environment.getExternalStorageDirectory() + "/" + "ChatApp" + "/";

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

                Repository repo = Repository.getRepository();
                Log.i("status_msg_keyval:", ChatListAdapter.keyval + " ..kk");
                repo.insertImagePath(ChatListAdapter.keyval, folder + fileName);

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
//            this.progressDialog.dismiss();
            //  if (adapter != null) adapter.notifyDataSetChanged();


            Repository repo1 = new Repository();
            Messages singleMsg1 = null;
            String filepath = "";
            try {
                singleMsg1 = repo1.getMessage(String.valueOf(ChatList.get(posdownload).getJsonMessage().get("KEY_VAL")));
                filepath = singleMsg1.getLocalImagePath();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {

                if (filepath != null || filepath.equals("")) {
                    File file = new File(filepath);
                    if (file.exists()) //Checking if the file exists or not
                    {
                        Uri path = Uri.fromFile(file);
                        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pdfOpenintent.setDataAndType(path, "application/pdf");
                        try {
                            mContext.startActivity(pdfOpenintent);
                        } catch (ActivityNotFoundException e) {

                        }
                    } else {

                        Toast.makeText(mContext, "The file not exists! ", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (Exception e) {
            }

        }
    }


}
