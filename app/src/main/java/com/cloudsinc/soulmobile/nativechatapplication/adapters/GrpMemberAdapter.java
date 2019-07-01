package com.cloudsinc.soulmobile.nativechatapplication.adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudsinc.soulmobile.nativechatapplication.App;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.GroupMemberListFrag;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactProfileFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactsSearchFragment;
import com.cloudsinc.soulmobile.nativechatapplication.utils.CircleTransform;
import com.cloudsinc.soulmobile.nativechatapplication.utils.InternetConnectionChecker;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.models.Contact;
import com.nc.developers.cloudscommunicator.models.Conversation;
import com.nc.developers.cloudscommunicator.models.Invitee;
import com.nc.developers.cloudscommunicator.socket.JsonParserClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.setRobotoRegularFont;

/**
 * This Adpter is used to display Group member List item data
 *
 * @author Prajakta Patil
 * @version 1.0
 * @since 2.2.2019
 */

public class GrpMemberAdapter extends RecyclerView.Adapter {

    private ArrayList<Invitee> inviteeList = new ArrayList<>();
    private OnLoadMoreListener loadMoreListener;
    private boolean isLoading = false, isMoreDataAvailable = true;
    private ArrayList<Invitee> grpMemberList;
    private Context context;
    private Fragment mfragment;
    private Invitee selectedInvitee = null;
    public final int TYPE_CONTACT = 0;
    public final int TYPE_LOAD = 1;
    public String memberDesignation = "";
    public static Dialog dialog = null;
    private static int selectedCount = 0;
    private static final String TAG = GrpMemberAdapter.class.getSimpleName();
    private boolean ismakeadmin = true;
    String admin = "";
    String ismakeadmincheck="";
     Invitee grpMemberModel;

    public static ImageView imgClose;
    public GrpMemberAdapter(Context contex, ArrayList<Invitee> grpMemberList, Fragment mfragment) {
        this.grpMemberList = grpMemberList;
        this.context = contex;
        this.mfragment = mfragment;


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_contact_row;
        TextView txt_name, txt_mail, txt_designation;
        ImageView img_profile;

        public MyViewHolder(View view) {
            super(view);
            ll_contact_row = (LinearLayout) view.findViewById(R.id.ll_contact_row);
            img_profile = (ImageView) view.findViewById(R.id.img_profile);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_mail = (TextView) view.findViewById(R.id.txt_mail);
            txt_designation = (TextView) view.findViewById(R.id.txt_designation);
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
            return new MyViewHolder(inflater.inflate(R.layout.grp_mem_list_row, parent, false));
        } else {
            return new LoadHolder(inflater.inflate(R.layout.loadin_row, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Invitee grpMemberModel = grpMemberList.get(position);
        Log.i("invitee_status:", grpMemberModel.getIde_attendees_email() + " ..kk");
        if (position >= getItemCount() - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }
        switch (getItemViewType(position)) {
            case TYPE_CONTACT:
                ((MyViewHolder) holder).txt_name.setTypeface(setRobotoRegularFont(context));
                ((MyViewHolder) holder).txt_mail.setTypeface(setRobotoRegularFont(context));
                try {
                    if (GlobalClass.getUserId().equals(grpMemberModel.getIde_attendees_email())) {
                        ((MyViewHolder) holder).txt_name.setText("You");
                        ((MyViewHolder) holder).txt_mail.setText(grpMemberModel.getIde_attendees_email());

                    } else {
                        ((MyViewHolder) holder).txt_name.setText(grpMemberModel.getIde_attendees_email().split("@")[0]);
                        ((MyViewHolder) holder).txt_mail.setText(grpMemberModel.getIde_attendees_email());

                    }
                    ((MyViewHolder) holder).txt_designation.setText(grpMemberModel.getIde_designation());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Glide.with(context).
                        load(R.drawable.userprofileimg)
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
                        }
                        inviteeList.clear();

                        selectedInvitee = grpMemberList.get(pos);
                        inviteeList.add(selectedInvitee);
                        Log.i(TAG + "_inviteeAddress:", selectedInvitee.getIde_attendees_email() + " ..kk");
                        Log.i(TAG + "_inviteeDesignation:", selectedInvitee.getIde_designation() + " ..kk");
                        Log.i(TAG + "_inviteePosition:", position + " ..kk");
                        if (selectedInvitee != null) {
                            memberDesignation = selectedInvitee.getIde_designation();
                            if (GlobalClass.getUserId().equals(grpMemberModel.getIde_attendees_email())) {
                                Log.d("owneris", grpMemberModel.getIde_attendees_email());
                                if (grpMemberModel.getIde_designation().equals("ADMIN")) {
                                    admin = grpMemberModel.getIde_attendees_email();
                                    Log.d("admin", "yes");
                                }
                            } else {
                                //ankita code
                                checkadmin(position,(MyViewHolder) holder);
                            }
                        }
                    }
                });
                ((MyViewHolder) holder).ll_contact_row.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = (int) v.getTag();
                        return false;
                    }
                });
                break;
        }
    }
    private void checkadmin(int position, MyViewHolder holder)
    {
        grpMemberModel = grpMemberList.get(position);
        try {
            if (grpMemberList != null) {
                for (int i = 0; i < grpMemberList.size(); i++) {
                    if (GlobalClass.getUserId().equals(grpMemberList.get(i).getIde_attendees_email())) {

                        if (grpMemberList.get(i).getIde_designation().equals("ADMIN")) {
                            admin = grpMemberModel.getIde_attendees_email();
                        }
                    }
                }
            }
            if (admin.isEmpty() || admin.equals("")) {

            } else {
                showCustomDialog(context,holder);
                Log.d("admin", "yesdialog");
            }

        } catch (Exception e) { }

    }

    private void showCustomDialog(final Context context, final MyViewHolder holder) {
        dialog = new Dialog(context, R.style.MyDialogTheme);
        dialog.setContentView(R.layout.grpmemberdetail_dialog);
        ImageView img_makeAdmin = (ImageView) dialog.findViewById(R.id.img_makeAdmin);
        ImageView img_removeMember = (ImageView) dialog.findViewById(R.id.img_removeMember);
        final LinearLayout ll_makeadmin = (LinearLayout) dialog.findViewById(R.id.ll_makeadmin);
        final LinearLayout ll_removemember = (LinearLayout) dialog.findViewById(R.id.ll_removemember);
        final LinearLayout ll_removeadmin = (LinearLayout) dialog.findViewById(R.id.ll_removeadmin);
        final LinearLayout ll_viewUser = (LinearLayout) dialog.findViewById(R.id.ll_viewUser);
        final TextView txt_viewUser = (TextView) dialog.findViewById(R.id.txt_viewUser);
        final LinearLayout ll_messageUser = (LinearLayout) dialog.findViewById(R.id.ll_messageUser);
        final TextView txt_messageUser = (TextView) dialog.findViewById(R.id.txt_messageUser);



        final ProgressBar prgradmin=(ProgressBar) dialog.findViewById(R.id.progbar_makeadmin);
        imgClose=(ImageView)dialog.findViewById(R.id.img_close);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Log.d("ddddgggggggggg",grpMemberModel.getIde_attendees_email());
        String namestr=grpMemberModel.getIde_attendees_email().split("@")[0];
        txt_viewUser.setText("View "+""+namestr.split("\\.")[0]);
        txt_messageUser.setText("Message "+""+namestr.split("\\.")[0]);


        ll_viewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContactsSearchFragment.isSearchContac = false;
                GroupMemberListFrag.isGroupMemberProfile = true;
                GroupMemberListFrag.isbackpressforviewprofile=true;
                Bundle bundle = new Bundle();
                Log.d("Inviteeeeeeeee",grpMemberModel.getIde_attendees_email());
                bundle.putString("invitee_mail",  grpMemberModel.getIde_attendees_email());
                Fragment fragment = new ContactProfileFragment();
                fragment.setArguments(bundle);
                ReplaceFragment.replaceFragment(mfragment, R.id.frame_frag_container, fragment, true);
                dialog.dismiss();

            }
        });


        ll_messageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ll_removeadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeremoveadminFun(false,v);
                prgradmin.setVisibility(View.VISIBLE);
                ll_makeadmin.setVisibility(View.GONE);
                ll_removeadmin.setVisibility(View.GONE);
                ll_removemember.setVisibility(View.GONE);
                imgClose.setVisibility(View.GONE);
                ll_viewUser.setVisibility(View.GONE);
                ll_messageUser.setVisibility(View.GONE);


            }
        });


        ll_makeadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeremoveadminFun(true,v);
                prgradmin.setVisibility(View.VISIBLE);
                ll_makeadmin.setVisibility(View.GONE);
                ll_removeadmin.setVisibility(View.GONE);
                ll_removemember.setVisibility(View.GONE);
                imgClose.setVisibility(View.GONE);
                ll_viewUser.setVisibility(View.GONE);
                ll_messageUser.setVisibility(View.GONE);

            }
        });

        ll_removemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                removeMemberFunction(view);
                prgradmin.setVisibility(View.VISIBLE);
                ll_makeadmin.setVisibility(View.GONE);
                ll_removeadmin.setVisibility(View.GONE);
                ll_removemember.setVisibility(View.GONE);
                imgClose.setVisibility(View.GONE);
                ll_viewUser.setVisibility(View.GONE);
                ll_messageUser.setVisibility(View.GONE);



            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ll_makeadmin.setVisibility(memberDesignation.equals("ADMIN") ? View.GONE : View.VISIBLE);
        ll_removeadmin.setVisibility(memberDesignation.equals("ADMIN") ? View.VISIBLE : View.GONE);


        dialog.show();
    }


    /**
     * this method is used for Make Admin to Other Member
     *
     * @return void
     */
    private void makeremoveadminFun(boolean ismakeadmin, View v) {
        try {
            JSONObject grpObj = App.getGrpObjForEdit();
            Log.i("conv_title_**********:", grpObj.getString("CML_TITLE") + " ..kk");
            Conversation conversation = JsonParserClass.parseConversationJsonObject(grpObj);
            Log.i("conv_Name:", conversation.getCmlTitle() + " ..kk");
            Log.i("invitee_Name:", selectedInvitee.getIde_attendees_email() + " ..kk");
            GroupMemberListFrag.progbar_rename.setVisibility(View.VISIBLE);
            if (ismakeadmin == true) {
                conversation.getJSONObjectMakeAdminOrRemoveAdmin(selectedInvitee, "make admin");
            } else {
                conversation.getJSONObjectMakeAdminOrRemoveAdmin(selectedInvitee, "remove admin");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method is used for Remove Member
     *
     * @return void
     */
    private void removeMemberFunction(View v) {
        try {
            JSONObject grpObj = App.getGrpObjForEdit();
            if (grpObj != null) {
                Conversation conversation = JsonParserClass.parseConversationJsonObject(grpObj);
                if (conversation != null) {
                    conversation.getJSONObjectRemoveMember(inviteeList);

                    /*boolean internetAvailability = InternetConnectionChecker.checkInternetConnection(context);
                    Log.i("is internet available:", String.valueOf(internetAvailability) + " ..kk");
                    if (GlobalClass.getAuthenticatedSyncSocket() != null && internetAvailability) {
                        JSONObject rmvInviteeObj = JsonObjectInvitee.getJSONObjectRemoveMember(conversation, inviteeList);
                        Log.i("emit_rmv_invitee:", String.valueOf(rmvInviteeObj) + " ..kk");
                        GlobalClass.getAuthenticatedSyncSocket().emit("serverOperation", rmvInviteeObj);

                    }*/
                }
            }
        } catch (Exception e) {
            Log.i(TAG + "_rmv_invt_call:", e.toString());
        } finally {
            inviteeList.clear();
        }

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_CONTACT;

    }

    @Override
    public int getItemCount() {
        return grpMemberList.size();
    }


    public void reload(ArrayList<Invitee> grpMemList) {
        this.grpMemberList = grpMemList;
    }


    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void filterList(ArrayList<Invitee> filterdNames) {
        this.grpMemberList = filterdNames;
        notifyDataSetChanged();
    }




}