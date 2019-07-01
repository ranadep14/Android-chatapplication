package com.cloudsinc.soulmobile.nativechatapplication.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.ContactsListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.ConversationListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.ConversationListFragment;

/**
 * This CLass is used to display Custom Dialogs
 * @author Prajakta Patil
 * @version 1.0
 * @since 2.2.2019
 */

public class CustomDialog {
    public static Dialog progress=null;
    private static Dialog intd=null;
    private static Exception e;
    private static  ProgressDialog pd;
    private TextView txt;
    //static Dialog d;
    public static void dispDialogConfirmation(final Context mcontext, final Class mclass, String msgstr, boolean actioVis) {

        e=new Exception(msgstr);
        final Dialog d=new Dialog(mcontext);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.custom_dialog);

        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.setCanceledOnTouchOutside(false);
        TextView txtmsgstr=(TextView)d.findViewById(R.id.txt_msg) ;
        txtmsgstr.setText(""+msgstr);
        Button btn_ok=(Button)d.findViewById(R.id.btn_ok);
        Button btn_cancle=(Button)d.findViewById(R.id.btn_cancle);
        LinearLayout ll_ok=(LinearLayout)d.findViewById(R.id.ll_ok);

        btn_cancle.setVisibility(View.VISIBLE);
        btn_cancle.setText(actioVis?"Cancel":"OK");
        btn_ok.setVisibility(actioVis?View.VISIBLE:View.GONE);
        ll_ok.setVisibility(actioVis?View.VISIBLE:View.GONE);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        d.show();
    }
    public static void dispDialogAlert(final Context mcontext, final Class mclass, String msgstr, boolean actioVis) {

        e=new Exception(msgstr);
        final Dialog d=new Dialog(mcontext);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.custom_dialog);

        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.setCanceledOnTouchOutside(false);
        TextView txtmsgstr=(TextView)d.findViewById(R.id.txt_msg) ;
        txtmsgstr.setText(""+msgstr);
        Button btn_ok=(Button)d.findViewById(R.id.btn_ok);
        Button btn_cancle=(Button)d.findViewById(R.id.btn_cancle);
        TextView txt_Confirm=(TextView)d.findViewById(R.id.txt_Confirm);
        LinearLayout ll_ok=(LinearLayout)d.findViewById(R.id.ll_ok);
        txt_Confirm.setText("Alert!");
        btn_cancle.setVisibility(View.VISIBLE);
        btn_cancle.setText(actioVis?"Cancel":"OK");
        btn_ok.setVisibility(actioVis?View.VISIBLE:View.GONE);
        ll_ok.setVisibility(actioVis?View.VISIBLE:View.GONE);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        d.show();
    }




    public static void contactdeleteDialog(final Context mcontext) {

        final Dialog d=new Dialog(mcontext, R.style.MyDialogTheme);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.contact_delete_dialog);

        ImageView imgClose=(ImageView)d.findViewById(R.id.img_close);


        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        d.setCanceledOnTouchOutside(false);
        d.setCancelable(false);


        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        d.show();




    }




    /** \
     * this method is used for Show Action Dialog On MUltiple Place
     * @return void
     */
    public static void showActionDialog(final Context mcontext,String title, String msgstr,final String action,boolean actionVis) {

        e=new Exception(msgstr);
        final Dialog d=new Dialog(mcontext);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.custom_dialog);

        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        d.setCanceledOnTouchOutside(false);
        TextView txtmsgstr=(TextView)d.findViewById(R.id.txt_msg) ;
        TextView txtitlestr=(TextView)d.findViewById(R.id.txt_title) ;

        txtmsgstr.setText(""+msgstr);
        if(txtitlestr!=null){
            txtitlestr.setText(""+title);
        }
        Button btn_ok=(Button)d.findViewById(R.id.btn_ok);
        Button btn_cancle=(Button)d.findViewById(R.id.btn_cancle);
        LinearLayout ll_ok=(LinearLayout)d.findViewById(R.id.ll_ok);
        btn_cancle.setVisibility(View.VISIBLE);
        btn_cancle.setText(actionVis?"No":"Yes");
        btn_ok.setVisibility(actionVis?View.VISIBLE:View.GONE);
        ll_ok.setVisibility(actionVis?View.VISIBLE:View.GONE);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    switch (action){
                        case "conversation_delete":
                            ConversationListFragment.deleteConvFunction();
                            ConversationListAdapter.selectedCount=0;

                            break;
                        case "contact_delete":
                            ContactListFragment.deleteContactFunction();
                            ContactsListAdapter.selectedCount=0;
                            break;
                        case "clear_chat":
                            ChatListFragment.clearChatFunction();
                            break;
                        case "archive_msg":
                            ChatListFragment.archivemsgFunction();
                            break;
                        case "":
                        default:
                            d.dismiss();
                            break;
                    }
                    d.dismiss();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        d.show();
    }

    /**
     * this method is used for start Progress Dialog
     */
    public static void startProgressDialog(final Context mcontext)
    {
        progress=new Dialog(mcontext, R.style.ProgressDialogTheme);
        progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(true);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progress.setContentView(R.layout.custom_progress_dialog);
        progress.show();

    }


    /**
     * this method is used for stop Progress Dialog
     */
    public static void stopProgressDialog()
    {
        progress.dismiss();
    }

}