package com.cloudsinc.soulmobile.nativechatapplication.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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

import java.io.File;
import java.util.ArrayList;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.VISIBLE;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.selectedCount;
import static com.cloudsinc.soulmobile.nativechatapplication.dialogs.NestedChatListFragment.img_right_star_nested;
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
public class NestedChatListAdapter extends RecyclerView.Adapter {

    public static int selectedCountnested = 0;
    public static String selectedstarmsg = "";
    private ArrayList<ChatListModel> ChatList;
    Context mContext;
    int total_types;
    ChatListModel chatListModel = null;
    public static String fileName = "";
    private String m = "";
    private Fragment mfragment;
    String group_json = "";
    NestedChatListAdapter chatListAdapter;
    private static final String TAG = NestedChatListAdapter.class.getSimpleName();
    public static Conversation conversation = null;
    Messages messages;
    public static ArrayList<Messages> conversationArrayListnested = new ArrayList<>();
    public static ArrayList<Messages> starmsgArrayList = new ArrayList<>();
    public static String keyval = "";
    public static int count = 0;
    public static boolean download = false;
    int pos = 0;
    String localpath1 = "";
    public static String rplyMsg = "", msg = "";
    public static int rply_pos = -1;
    int row_index = -1;
    public static JSONObject rplyJsonObj, forwardJsonobj,starmsg;
    boolean selectStar=false;

    //int selectedState[];
    public NestedChatListAdapter(ArrayList<ChatListModel> ChatList, Context context, Fragment fragment, String group_json) {
        this.ChatList = ChatList;
        this.mContext = context;
        total_types = ChatList.size();
        this.mfragment = fragment;
        this.group_json = group_json;
    }

    public void reload(ArrayList<ChatListModel> ChatList) {
        this.ChatList = ChatList;
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
        ImageView img_to_pic, img_pdf, img_zip,img_star;
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

    public static class MsgInfoViewHolder extends RecyclerView.ViewHolder {

        TextView txt_msg_info;

        public MsgInfoViewHolder(View itemView) {
            super(itemView);

            this.txt_msg_info = (TextView) itemView.findViewById(R.id.txt_msg_info);
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

                        ((MsgInViewHolder) holder).txt_msg.setText(Html.fromHtml(unescapeJava(chatListModel.getMsg())));
                        ((MsgInViewHolder) holder).txt_sender_name.setText(Html.fromHtml(unescapeJava(split(chatListModel.getSender_name()))));
                        if (chatListModel.getStatus().equals("read"))
                            ((MsgInViewHolder) holder).img_msg_status.setImageResource(R.drawable.ic_done_all);
                        if (chatListModel.isSend_flag())
                            ((MsgInViewHolder) holder).img_msg_status.setImageResource(R.drawable.ic_done);
                        ((MsgInViewHolder) holder).rel_image_in.setVisibility(chatListModel.isMedia() ? VISIBLE : View.GONE);
                        ((MsgInViewHolder) holder).txt_msg.setVisibility(chatListModel.isMedia() ? View.GONE : View.VISIBLE);
                        ((MsgInViewHolder) holder).ll_chat.setBackgroundColor(chatListModel.isSelected() ? mContext.getResources().getColor(R.color.colorItemSelected) : mContext.getResources().getColor(R.color.colorTransparent));

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
                                }*/
                                //  JSONObject conversationJsonObj = ChatList.get(listPosition).getJsonMessage();
                                // conversationArrayList.add(JsonParserClass.parseMessagesJSONObject(conversationJsonObj));
                                return true;
                            }
                        });


                        ((MsgInViewHolder) holder).ll_chat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int pos = (Integer) v.getTag();

                                if (rply_pos > -1) {
                                    rply_pos = -1;
                                    resetFlag();
                                    notifyDataSetChanged();
                                }

                                if (selectedCountnested > 0) {
                                    selectItem(v, pos);

                                }

                               /* if (selectedCountnested > 0) {

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

                                }*/
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




                    } catch (Exception e) {
                    }


                    if ((!(chatListModel.getImg_url() == null))) {
                        ((MsgInViewHolder) holder).img_from_pic.setImageURI(null);
                        try {
                            Glide.with(mContext).load(chatListModel.getImg_url())
                                    .dontAnimate()
                                    .placeholder(R.drawable.clouds_icon)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(((MsgInViewHolder) holder).img_from_pic);



                        } catch (Exception e) {
                        }



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

                            ((MsgOutViewHolder) holder).txt_msg.setText(Html.fromHtml(unescapeJava(messageString)));
                        }
                        jsonObjectMsg = chatListModel.getJsonMessage();
                        String msgCreatedOn, timeString, destitle = "",startmsg="";
                        int counter=0;
                        msgCreatedOn = timeString = "";

                        int rpcount=0;
                        if (jsonObjectMsg != null) {
                            Log.i("msgObjectout:", String.valueOf(jsonObjectMsg) + " ..kk");
                            msgCreatedOn = jsonObjectMsg.getString("CREATED_ON");
                            timeString = DateClass.changeDateFormatToTime(msgCreatedOn);
                            Log.i("msg_time:", timeString + " ..kk");

                            startmsg = jsonObjectMsg.getString("CML_STAR");
                            destitle = jsonObjectMsg.getString("CML_DESCRIPTION");

                        }
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



                        ((MsgOutViewHolder) holder).ll_chat.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                int pos = (Integer) v.getTag();
                                selectItem(v, pos);
/*


                                try {
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


                                JSONObject conversationJsonObj = ChatList.get(listPosition).getJsonMessage();
                                conversationArrayListnested.add(JsonParserClass.parseMessagesJSONObject(conversationJsonObj));
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

                                if (selectedCountnested > 0) {
                                    selectItem(v, pos);

                                }

                               /* if (selectedCountnested > 0) {

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

                                }*/
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
            }

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
        selectedCountnested = 0;
        conversationArrayListnested.clear();
        starmsgArrayList.clear();

        for (int i = 0; i < ChatList.size(); i++) {
            if (ChatList.get(i).isSelected()) {
                try {
                    selectedCountnested++;
                    keyValList.add(ChatList.get(i).getJsonMessage().getString("KEY_VAL") + "*" + ChatList.get(i).getJsonMessage().getString("CREATED_BY"));
                    //Log.d("MsgKeyVal",ChatList.get(i).getJsonMessage().getString("KEY_VAL"));
                    //  Log.d("DelMsgObj", ChatList.get(i).getJsonMessage().getString("KEY_VAL") + "*" + ChatList.get(i).getJsonMessage().getString("CREATED_BY"));
                    selectedstarmsg = ChatList.get(i).getJsonMessage().getString("CML_STAR");

                    JSONObject conversationJsonObj = ChatList.get(i).getJsonMessage();
                    conversationArrayListnested.add(JsonParserClass.parseMessagesJSONObject(conversationJsonObj));
                    //  Log.d("conv", String.valueOf(conversationArrayList.size()));


                    try {

                        starmsg=ChatList.get(i).getJsonMessage();

                        starmsgArrayList.add(JsonParserClass.parseMessagesJSONObject(starmsg));
                        //    Log.d("rconv", String.valueOf(replyConvArrayList.size()));

                    } catch (Exception e) {

                    }


                    for(int k=0;k<starmsgArrayList.size();k++)
                    {
                        if(starmsgArrayList.get(k).getCmlStar()==1)
                        {
                            Drawable new_image = mContext.getResources().getDrawable(R.drawable.unstar);
                            new_image = DrawableCompat.wrap(new_image);
                            DrawableCompat.setTint(new_image, mContext.getResources().getColor(R.color.colorWhite));
                            img_right_star_nested.setBackgroundDrawable(new_image);

                        }

                        else
                        {
                            Drawable new_image = mContext.getResources().getDrawable(R.drawable.star_fill);
                            new_image = DrawableCompat.wrap(new_image);
                            DrawableCompat.setTint(new_image, mContext.getResources().getColor(R.color.colorWhite));
                            img_right_star_nested.setBackgroundDrawable(new_image);


                        }
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        try {
            NestedChatListFragment.chatActinBarVisibility(selectedCountnested);
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
            selectedCountnested = 0;
            NestedChatListFragment.chatActinBarVisibility(selectedCountnested);
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

}