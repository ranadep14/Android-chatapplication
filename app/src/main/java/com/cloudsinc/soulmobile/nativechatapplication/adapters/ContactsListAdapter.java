package com.cloudsinc.soulmobile.nativechatapplication.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.FragmentHomeScreen;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactLandingFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactListFragment;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.models.Contact;
import com.bumptech.glide.Glide;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.utils.CircleTransform;
import org.json.JSONObject;
import java.util.ArrayList;

import static com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactListFragment.contect_user;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setRobotoRegularFont;

/**
 * This Adpter is used to display Contact List item data
 * @author Prajakta Patil
 * @version 1.0
 * @since 2.11.2018
 */
public class ContactsListAdapter extends RecyclerView.Adapter {

    public final int TYPE_CONTACT = 0;
    public final int TYPE_LOAD = 1;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    private ArrayList<Contact> contactList;
    private Context context;
    private Fragment mfragment;
    public static int selectedCount = 0;
    public static Contact selected_contact;
    private static final String TAG=ContactsListAdapter.class.getSimpleName();
    public static boolean isOneToOneConversationClicked=false;
    public static Contact contactsListModel;
    public static ArrayList<Contact> selectedcontctlist = new ArrayList<>();



    public ContactsListAdapter(Context contex, ArrayList<Contact> contactList, Fragment mfragment) {
        this.contactList = contactList;
        this.context = contex;
        this.mfragment = mfragment;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_contact_row;
        TextView txt_name, txt_mail;
        ImageView img_profile, img_sendmsg;
        ProgressBar prgbar;

        public MyViewHolder(View view) {
            super(view);
            ll_contact_row = (LinearLayout) view.findViewById(R.id.ll_contact_row);
            img_profile = (ImageView) view.findViewById(R.id.img_profile);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            img_sendmsg = (ImageView) view.findViewById(R.id.img_msg);
            txt_mail = (TextView) view.findViewById(R.id.txt_mail);
             prgbar=(ProgressBar)view.findViewById(R.id.progbar_contactchat);

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
        if (viewType == TYPE_CONTACT) {
            return new MyViewHolder(inflater.inflate(R.layout.contact_list_row, parent, false));
        } else {
            return new LoadHolder(inflater.inflate(R.layout.loadin_row, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        contactsListModel = contactList.get(position);
        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }
        switch (getItemViewType(position)) {
            case TYPE_CONTACT:
                ((MyViewHolder) holder).txt_name.setTypeface(setRobotoRegularFont(context), Typeface.BOLD);
                ((MyViewHolder) holder).txt_mail.setTypeface(setRobotoRegularFont(context));
                ((MyViewHolder) holder).txt_name.setText(contactsListModel.getFirstname() + " " + contactsListModel.getLastname());
                ((MyViewHolder) holder).txt_mail.setText(contactsListModel.getOfficialEmail());
                Log.d(TAG, "" + contactsListModel.getCmlAccepted());
                ((MyViewHolder) holder).ll_contact_row.setEnabled(contactsListModel.getCmlAccepted() == 3 ? false : true);
                ((MyViewHolder) holder).ll_contact_row.setClickable(contactsListModel.getCmlAccepted() == 3 ? false : true);
                ((MyViewHolder) holder).ll_contact_row.setBackgroundColor(contactsListModel.getCmlAccepted() == 3 ? context.getResources().getColor(R.color.colorDisable) : context.getResources().getColor(R.color.colorTransparent));

                if (contactsListModel.getCmlAccepted() != 3) {
                    ((MyViewHolder) holder).ll_contact_row.setBackgroundColor(contactsListModel.getCmlAccepted() != 3 && contactsListModel.isContactSelected() ? context.getResources().getColor(R.color.colorItemSelected) : context.getResources().getColor(R.color.colorTransparent));
                }

                ((MyViewHolder) holder).img_sendmsg.setTag(position);
                ((MyViewHolder) holder).img_sendmsg.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        try {

                            int position=(int)v.getTag();
                            Contact contact=contactList.get(position);
                            ContactsListAdapter.isOneToOneConversationClicked=true;
                            String officialEmailId="";
                            officialEmailId=contact.getOfficialEmail();
                            contect_user=officialEmailId;
                            ContactListFragment.userName=contact.getOfficialEmail();
                            if(officialEmailId!=null && contact!=null){
                                ChatListFragment.fromfrgament="cnt";
                                ((MyViewHolder) holder).prgbar.setVisibility(View.VISIBLE);
                                Log.d(TAG+ "ssssssssssssss",contact.getOfficialEmail());
                                 contact.getJsonObjectCreateOneToOneConversation();
                            }


                        }catch (Exception ex){
                            ex.printStackTrace();
                        }





                        //createOneToOneConv(((MyViewHolder) holder));
                    }
                });

                String strimagepath = GlobalClass.getLoginUrl() + "/" + contactsListModel.getImagePath();
                Log.d(TAG, strimagepath);
                Glide.with(context).
                        load(GlobalClass.getLoginUrl() + "/" + contactsListModel.getImagePath())
                        .placeholder(R.drawable.userprofileimg)
                        .error(R.drawable.userprofileimg)
                        .transform(new CircleTransform(context))
                        .into(((MyViewHolder) holder).img_profile);

                ((MyViewHolder) holder).ll_contact_row.setTag(position);

                ((MyViewHolder) holder).ll_contact_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = (int) v.getTag();
                        if (selectedCount > 0) {
                            selectItem(v, pos);
                        } else {

                        }
                    }
                });

                ((MyViewHolder) holder).ll_contact_row.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = (int) v.getTag();
                        selectItem(v, pos);
                        return true;
                    }
                });

                ((MyViewHolder) holder).img_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_viewcontactprofile);
                        contactsListModel = contactList.get(position);


                        TextView txt_titlenm=(TextView)dialog.findViewById(R.id.txt_titlenm);
                        ImageView img_profile_expand=(ImageView)dialog.findViewById(R.id.img_profile_expand);

                        txt_titlenm.setText(contactsListModel.getFirstname() + " " + contactsListModel.getLastname());
                        String strimagepath = GlobalClass.getLoginUrl() + "/" + contactsListModel.getImagePath();
                        Log.d(TAG, strimagepath);
                        /*Glide.with(context).
                                load(GlobalClass.getLoginUrl() + "/" + contactsListModel.getImagePath())
                                .placeholder(R.drawable.userprofileimg)
                                .error(R.drawable.userprofileimg)
                                .transform(new CircleTransform(context))
                                .into(((MyViewHolder) holder).img_profile);

*/


                        dialog.show();

                    }
                });

                break;
        }

    }

    /**
     * this method is used for create One to One Conversation
     * @return void
     * @param holder
     */
    private void createOneToOneConv(MyViewHolder holder){
        try {
            ContactsListAdapter.isOneToOneConversationClicked=true;
            String officialEmailId="";
            officialEmailId=contactsListModel.getOfficialEmail();
            ContactListFragment.userName=contactsListModel.getOfficialEmail();
            if(officialEmailId!=null && contactsListModel!=null){
                ChatListFragment.fromfrgament="contact";
                holder.prgbar.setVisibility(View.VISIBLE);
               // contact.getJsonObjectCreateOneToOneConversation();


               /* JSONObject oneToOneConversationObj=JsonObjectConversation.getJsonObjectCreateOneToOneConversation(officialEmailId,contactsListModel);
                boolean internetAvailability=InternetConnectionChecker.checkInternetConnection(context);
                Log.i(TAG+"_oneToOneConversation:",String.valueOf(oneToOneConversationObj)+" ..kk");
                Log.i(TAG+"_is internet available:",String.valueOf(internetAvailability)+" ..kk");
                if(internetAvailability && GlobalClass.getAuthenticatedSyncSocket()!=null){
                    GlobalClass.getAuthenticatedSyncSocket().emit("OnDemandCall",oneToOneConversationObj);
                }*/
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemViewType(int position) {
        if (contactList.get(position).getViewType() == 0) {
            return TYPE_CONTACT;
        } else {
            return TYPE_LOAD;
        }

    }

    public void resetFlag() {
        for (int i = 0; i < contactList.size(); i++) {
            contactList.get(i).setContactSelected(false);
        }
        selectedCount = 0;
        notifyDataSetChanged();
    }


    public void reload(ArrayList<Contact> list) {
        contactList = list;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void filterList(ArrayList<Contact> filterdNames) {
        this.contactList = filterdNames;
        notifyDataSetChanged();
    }

    /**
     * this method is used to select item for delete
     * @return view and item position
     */
    private void selectItem(View v, int pos) {
        try {
            selected_contact = contactList.get(pos);
            contactList.get(pos).setContactSelected(!contactList.get(pos).isContactSelected());
            v.setBackgroundColor(contactList.get(pos).isContactSelected() ? context.getResources().getColor(R.color.colorItemSelected) : context.getResources().getColor(R.color.colorTransparent));

            ArrayList<JSONObject> contactdellist= new ArrayList<>();

            selectedcontctlist.clear();
            selectedCount = 0;
            for (int i = 0; i < contactList.size(); i++) {
                if (contactList.get(i).isContactSelected()) {
                    selectedcontctlist.add(contactList.get(i));
                    selectedCount++;
                }
            }
            GlobalClass.setGrpDelList(contactdellist);
            if (selectedCount > 0) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ContactListFragment.contactActionBarVisiblity(true,selectedCount);
                        FragmentHomeScreen.actinBarVisibility(false);
                        ContactLandingFragment.contactTabsVisibility(false);
                    }
                }, 100);

            } else {
                ContactListFragment.contactActionBarVisiblity(false,0);
                FragmentHomeScreen.actinBarVisibility(true);
                ContactLandingFragment.contactTabsVisibility(true);
            }
            notifyItemChanged(pos);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}