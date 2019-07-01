package com.cloudsinc.soulmobile.nativechatapplication.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.FragmentHomeScreen;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.ConversationLandingFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.ConversationListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.utils.CircleTransform;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;
import com.cloudsinc.soulmobile.nativechatapplication.utils.common;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.models.Conversation;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.GallaryimgList;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.GallarypdfList;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setRobotoRegularFont;

/**
 * This Adpter is used to display Conversation List item data
 * @author Prajakta Patil
 * @version 1.0
 * @since 2.11.2018
 */
public class ConversationListAdapter extends RecyclerView.Adapter{
    public final int TYPE_CONVERSATION = 0;
    public final int TYPE_LOAD = 1;
    private boolean isLoaderVisible = false;
    private Context context;
    private Fragment mfragment;
    Thread delThread  = null;
    public static int selectedCount = 0;
    private int pos = 0;
    private ArrayList<Conversation> groupNameList = new ArrayList<>();
    private String cmlTitle = "";
    private int cmlUnreadcount = 0;
    String imagepathq="";
    String TAG=this.getClass().getSimpleName();
    public static boolean isOnetoOne=false;
    public boolean issetprofile;


    public static Conversation conversations;
    public static Conversation selected_conversations;
    public static ArrayList<Conversation> selectConvlist = new ArrayList<>();
  //  public static String[] prof_image_path ;

    public ConversationListAdapter(ArrayList<Conversation> groupNameList, Context contex,
                                   Fragment mfragment) {
        this.groupNameList = groupNameList;
        this.context = contex;
        this.mfragment = mfragment;
        /*prof_image_path= new String[groupNameList.size()];
        for(int i=0;i<groupNameList.size();i++)
        {
            prof_image_path[i]="";
        }*/

    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_conversation_row,lin_convdata_row;
        TextView txt_user_img,txt_name,txt_time,txt_msg,txt_msg_count;
        ImageView img_important;
        ImageView img_grp_profile;
        public MyViewHolder(View view) {
            super(view);
            ll_conversation_row = (LinearLayout)view.findViewById(R.id.ll_conversation_row);
            lin_convdata_row = (LinearLayout)view.findViewById(R.id.lin_convdata_row);
            txt_user_img=(TextView) view.findViewById(R.id.txt_user_img);
            txt_name=(TextView) view.findViewById(R.id.txt_name);
            txt_time=(TextView) view.findViewById(R.id.txt_time);
            txt_msg=(TextView) view.findViewById(R.id.txt_msg);
            txt_msg_count=(TextView) view.findViewById(R.id.txt_msg_count);
            img_important=(ImageView) view.findViewById(R.id.img_important);
            img_grp_profile=(ImageView)view.findViewById(R.id.img_grp_profile);
        }
    }

    public class LoadHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.progressBar)
        ProgressBar mProgressBar;


        public LoadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_CONVERSATION){
            return new MyViewHolder(inflater.inflate(R.layout.conversation_list_row,parent,false));
        }else{
            return new LoadHolder(inflater.inflate(R.layout.loadin_row,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position)  {
        conversations=groupNameList.get(position);
        Log.d(TAG+"position", String.valueOf(position));
        Log.d(TAG+"LISTsize",""+groupNameList.size());


        switch (getItemViewType(position)){
            case TYPE_CONVERSATION:
                try {
                    JSONObject lstMsg = null;
                    JSONObject completObj = new JSONObject(conversations.getCompleteData());
                    ((MyViewHolder) holder).txt_user_img.setTypeface(setRobotoRegularFont(context));
                    ((MyViewHolder) holder).txt_name.setTypeface(setRobotoRegularFont(context), Typeface.BOLD);
                    ((MyViewHolder) holder).txt_msg.setTypeface(setRobotoRegularFont(context));
                    ((MyViewHolder) holder).txt_time.setTypeface(setRobotoRegularFont(context));
                    ((MyViewHolder) holder).txt_msg_count.setTypeface(setRobotoRegularFont(context));

                    ((MyViewHolder)holder).img_important.setVisibility(completObj.getInt("CML_STAR")==0?View.GONE:View.VISIBLE);
                    cmlTitle = conversations.getCmlTitle();
                    imagepathq=conversations.getCmlImagePath();
                   // prof_image_path[position]=imagepathq;
                    cmlUnreadcount=conversations.getUnreadCount();
                    Log.d(TAG+"unreadcount", String.valueOf(cmlUnreadcount)+"------------"+cmlTitle);
                    ((MyViewHolder) holder).txt_name.setText(""+cmlTitle);



                    if(imagepathq.equals("") && imagepathq==null){
                        ((MyViewHolder) holder).txt_user_img.setVisibility(View.VISIBLE);
                        ((MyViewHolder) holder).img_grp_profile.setVisibility(View.GONE);
                        if (cmlTitle!=null) {
                            if(cmlTitle.length()>0) {
                                ((MyViewHolder) holder).txt_user_img.setText(cmlTitle.substring(0, 1).toUpperCase());
                            }
                        }

                    }else {
                        ((MyViewHolder) holder).txt_user_img.setVisibility(View.GONE);
                        ((MyViewHolder) holder).img_grp_profile.setVisibility(View.VISIBLE);
                        String group_object=groupNameList.get(position).getCompleteData();
                        JSONObject  clickedJsonObject = new JSONObject(group_object);


                        Log.d(TAG+"CCCCCCCCCCCCCC",clickedJsonObject.toString());
                        Log.d(TAG +"chchhhhhhhhhc",conversations.getCmlTitle()+"::" +conversations.getCmlImagePath().replaceAll("//?op=OPEN",""));
                        if (clickedJsonObject.getString("SUB_KEY_TYPE").equals("TSK_SCONV_LST")){
                            Glide.with(context).
                                    load( GlobalClass.getLoginUrl()+""+conversations.getCmlImagePath().replaceAll("//?op=OPEN",""))
                                    .error(R.drawable.profile_user_in)
                                    .placeholder(R.drawable.profile_user_in)
                                    .transform(new CircleTransform(context))
                                    .into(((MyViewHolder) holder).img_grp_profile);
                        }
                        if (clickedJsonObject.getString("SUB_KEY_TYPE").equals("TSK_CONV_LST")){
                            Glide.with(context).
                                    load( GlobalClass.getLoginUrl()+""+conversations.getCmlImagePath().replaceAll("//?op=OPEN",""))
                                    .error(R.drawable.group_icon)
                                    .placeholder(R.drawable.group_icon)
                                    .transform(new CircleTransform(context))
                                    .into(((MyViewHolder) holder).img_grp_profile);

                        }




                        //img_grp_profile.setImageURI(Uri.parse( conversations.getCmlImagePath()));

                    }






                    ((MyViewHolder) holder).txt_msg_count.setText(""+ cmlUnreadcount);
                    ((MyViewHolder) holder).txt_msg_count.setVisibility(cmlUnreadcount>0?View.VISIBLE:View.INVISIBLE);
                    ((MyViewHolder) holder).txt_time.setTextColor(cmlUnreadcount>0?context.getResources().getColor(R.color.colorLinkTxt):context.getResources().getColor(R.color.colorBlack));
                    ((MyViewHolder) holder).txt_time.setAlpha(cmlUnreadcount>0?.99f:.52f);
                    try {
                        lstMsg = conversations.getLatestMessage() == null || conversations.getLatestMessage().equals("") ? null : new JSONObject(conversations.getLatestMessage());
                    }
                    catch (Exception ex){lstMsg=null;}
                    ((MyViewHolder) holder).txt_msg.setText(Html.fromHtml(common.unescapeJava(lstMsg==null?"No Message":lstMsg.has("CML_TITLE")?lstMsg.getString("CML_TITLE"):"No Message")));
                    ((MyViewHolder) holder).txt_time.setText(convertAndGetTime1(conversations.getLastModifiedOn()));
                    Log.d(TAG+"isSelectedFlag", String.valueOf(conversations.isConversationSelected()));
                    ((MyViewHolder) holder).ll_conversation_row.setBackgroundColor(conversations.isConversationSelected() ? context.getResources().getColor(R.color.colorItemSelected) :context.getResources().getColor(R.color.colorTransparent));
                    ((MyViewHolder) holder).ll_conversation_row.setTag(position);


                    ((MyViewHolder) holder).ll_conversation_row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                int pos = (Integer) v.getTag();
                                Log.d(TAG+"position", "" + pos + "-----------" + groupNameList.get(pos).getUnreadCount());
                                if (selectedCount > 0) {
                                    selectItem(v, pos);
                                }
                                else {
                                    resetMsgCountcall(pos);
                                }

                            }catch (Exception ex){
                                ex.getMessage();
                            }
                        }
                    });

                    ((MyViewHolder) holder).img_grp_profile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_viewcontactprofile);


                            TextView txt_titlenm=(TextView)dialog.findViewById(R.id.txt_titlenm);
                            ImageView img_profile_expand=(ImageView)dialog.findViewById(R.id.img_profile_expand);
                            String group_object=groupNameList.get(position).getCompleteData();
                            conversations=groupNameList.get(position);
                            txt_titlenm.setText(conversations.getCmlTitle());
                            JSONObject  clickedJsonObject = null;
                            try {
                                clickedJsonObject = new JSONObject(group_object);
                                Log.d(TAG +"ffffffffff",conversations.getCmlTitle()+"::" +conversations.getCmlImagePath().replaceAll("//?op=OPEN",""));

                                if (clickedJsonObject.getString("SUB_KEY_TYPE").equals("TSK_SCONV_LST")){
                                    Glide.with(context).
                                            load( GlobalClass.getLoginUrl()+""+conversations.getCmlImagePath().replaceAll("//?op=OPEN",""))
                                            .error(R.drawable.profile_user_in)
                                            .placeholder(R.drawable.profile_user_in)
                                            .transform(new CircleTransform(context))
                                            .into(img_profile_expand);
                                }
                                if (clickedJsonObject.getString("SUB_KEY_TYPE").equals("TSK_CONV_LST")){
                                    Glide.with(context).
                                            load( GlobalClass.getLoginUrl()+""+conversations.getCmlImagePath().replaceAll("//?op=OPEN",""))
                                            .error(R.drawable.group_icon)
                                            .placeholder(R.drawable.group_icon)
                                            .transform(new CircleTransform(context))
                                            .into(img_profile_expand);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }




                            dialog.show();
                        }
                    });

                    ((MyViewHolder) holder).txt_user_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });


                    ((MyViewHolder) holder).ll_conversation_row.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            int pos=(int)v.getTag();
                            selectItem(v,pos);

                            return true;
                        }
                    });
                    break;
                } catch (Exception e) {
                    Log.e(TAG,"*************"+e.getMessage());
                }
        }
    }

    /**
     * this method is used for reset message count
     * @return void
     * @param position
     */
    private void resetMsgCountcall(int position){
        try {
            selected_conversations=groupNameList.get(position);
            ChatListFragment.fromfrgament="";
            ChatListFragment.forwardsingle="";

            ChatListFragment.showingimage=false;
            ChatListFragment.rply=false;
            ChatListAdapter.selectedCount=0;
            GallaryimgList.clear();
            GallarypdfList.clear();

            String group_object=groupNameList.get(position).getCompleteData();
            selected_conversations.getJSONObjectConversationResetUNReadCount();

            Bundle bundle = new Bundle();
            bundle.putString("group_json", "" +group_object);
            Fragment fragment = new ChatListFragment();
            fragment.setArguments(bundle);
            ReplaceFragment.replaceFragment(mfragment, R.id.frame_frag_container, fragment, true);
        }
        catch (Exception ex)
        {
            Log.i(TAG,"-------------"+ex.getMessage());
        }


    }


    /**
     * this method is used for Conversion of Time
     * @return Time
     */
    private String convertAndGetTime1(String msgTime){

        String time;
        if (msgTime.contains("+0000")){
            time = msgTime.replace("+0000","Z");
        }
        else {
            time = msgTime;
        }
        String month_name="";
        String time_original = "";
        String time_system = "";
        Calendar expdate = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat utcFormat1 = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat utcFormat_syetm = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat utcFormat_original = new SimpleDateFormat("yyyy-MM-dd");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {

            time_system=utcFormat_syetm.format(new Date());
            time_original=utcFormat_original.format(utcFormat_original.parse(time));
            if(!time_system.equals(time_original))
            {
                time=time_original;
            }
            else
            {
                time=utcFormat1.format(utcFormat.parse(time));
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG,"--------"+ex.getMessage());
        }
        return time;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {

        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemViewType(int position) {
    if (isLoaderVisible) {
        return position == groupNameList.size() - 1 ? TYPE_LOAD : TYPE_CONVERSATION;
    } else {
        return TYPE_CONVERSATION;
    }
    }

    @Override
    public int getItemCount() {
        return groupNameList==null?0:groupNameList.size();
    }


    public void notifyDataChanged(){
        notifyDataSetChanged();
    }

    public void resetFlag(){
        for (int i=0;i<groupNameList.size();i++){
            groupNameList.get(i).setConversationSelected(false);
        }
        selectedCount = 0;
        notifyDataSetChanged();
    }
    public void reload(ArrayList<Conversation> newGrpList){
        this.groupNameList = newGrpList;
    }

    public void filterList(ArrayList<Conversation> filterdNames) {
        this.groupNameList = filterdNames;
        notifyDataSetChanged();
    }

    /**
     * this method is used for select conversation for delete
     * @return void
     */

    private void selectItem(View v,int pos) {

        try {
            selected_conversations=groupNameList.get(pos);
            groupNameList.get(pos).setConversationSelected(!groupNameList.get(pos).isConversationSelected());
            v.setBackgroundColor(groupNameList.get(pos).isConversationSelected() ? context.getResources().getColor(R.color.colorItemSelected) :context.getResources().getColor(R.color.colorTransparent));


            ArrayList<JSONObject> grpDelList= new ArrayList<>();
            // grpDelList.clear();
            selectConvlist.clear();
            selectedCount=0;
            for (int i=0;i<groupNameList.size();i++){
                if (groupNameList.get(i).isConversationSelected()){
                    selectConvlist.add(groupNameList.get(i));
                    //selectConvlist.add(new JSONObject(groupNameList.get(i).getCompleteData()));
                    selectedCount++;
                }
            }
            GlobalClass.setGrpDelList(grpDelList);
            if (selectedCount>0){
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ConversationListFragment.actionBarVisiblity(true,selectedCount);
                        FragmentHomeScreen.actinBarVisibility(false);
                        ConversationLandingFragment.conTabsVisibility(false);
                    }
                }, 100);

            }
            else {
                ConversationListFragment.actionBarVisiblity(false,0);
                FragmentHomeScreen.actinBarVisibility(true);
                ConversationLandingFragment.conTabsVisibility(true);
            }
            notifyItemChanged(pos);
        }catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }catch (IndexOutOfBoundsException e) {
            e.printStackTrace();

        }catch (Exception e) {
            e.printStackTrace();
        }

    }




    public void add(Conversation response) {
        groupNameList.add(response);
        notifyItemInserted(groupNameList.size() - 1);
    }

    public void addAll(ArrayList<Conversation> postItems) {
        /*for (Conversation response : postItems) {
            add(response);
        }*/
        groupNameList = postItems;
    }


    private void remove(Conversation postItems) {
        int position = groupNameList.indexOf(postItems);
        if (position > -1) {
            groupNameList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoading() {
        if(groupNameList.size()>9){
            isLoaderVisible = true;
        }
        //add(new Conversation());
        notifyDataSetChanged();
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = groupNameList.size() - 1;
        Conversation item = getItem(position);
        if (item != null) {
            groupNameList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    Conversation getItem(int position) {
        return groupNameList.get(position);
    }


}