package com.cloudsinc.soulmobile.nativechatapplication.dialogs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.cloudsinc.soulmobile.nativechatapplication.App;
import com.cloudsinc.soulmobile.nativechatapplication.AppConfig;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.ContactsListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.ConversationListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.ForwardMsgListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.NestedChatListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.datamodels.ChatListModel;
import com.cloudsinc.soulmobile.nativechatapplication.dialogs.CustomDialog;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.FragmentHomeScreen;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ImageExapndViewFragment;
import com.cloudsinc.soulmobile.nativechatapplication.interfaces.ApiConfig;
import com.cloudsinc.soulmobile.nativechatapplication.interfaces.IOnBackPressed;
import com.cloudsinc.soulmobile.nativechatapplication.utils.CustomLinearLayoutManager;
import com.cloudsinc.soulmobile.nativechatapplication.utils.DateClass;
import com.cloudsinc.soulmobile.nativechatapplication.utils.GalleryRealPath;
import com.cloudsinc.soulmobile.nativechatapplication.utils.GpsTracker;
import com.cloudsinc.soulmobile.nativechatapplication.utils.InternetConnectionChecker;
import com.cloudsinc.soulmobile.nativechatapplication.utils.JsonObject;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;
import com.cloudsinc.soulmobile.nativechatapplication.utils.common;
import com.google.gson.JsonElement;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.models.Conversation;
import com.nc.developers.cloudscommunicator.models.Login;
import com.nc.developers.cloudscommunicator.models.Messages;

import com.nc.developers.cloudscommunicator.sharedprefrences.PrefManager;
import com.nc.developers.cloudscommunicator.socket.JsonParserClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.models.sort.SortingTypes;
import droidninja.filepicker.utils.Orientation;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.WIFI_SERVICE;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.conversationArrayList;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.forwardJsonobj;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.msg;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.replynestedmsg;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.rplyJsonObj;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.rplyMsg;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.rply_pos;

import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.starmsg;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ForwardMsgListAdapter.targetConvertionListselected;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.NestedChatListAdapter.conversationArrayListnested;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.NestedChatListAdapter.selectedstarmsg;
import static com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment.fromfrgament;
import static com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment.nestedjsondata;
import static com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment.selectedConversationrply;
import static com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.ConversationListFragment.isArchive;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.ImageCompress.compressImage;

//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;

/**
 * This Fragment is used to display send and receive messages and attachment
 *
 * @author Prajakta Patil
 * @version 1.0
 * @since 2.11.2018
 */


public class NestedChatListFragment extends Fragment implements IOnBackPressed{

    private Observable<String> mobservable;
    private Observer<String> myObserver;
    boolean isUsersMsg;
    private static ArrayList<ChatListModel> ChatList = new ArrayList<>();
    private static ArrayList<Messages> msgList = new ArrayList<>();
    private ArrayList<Conversation> groupNmList = new ArrayList<Conversation>();
    private ArrayList<Conversation> groupNmforwardList = new ArrayList<Conversation>();

    public static ArrayList<Messages> replyConvArrayList = new ArrayList<>();
    View view;
    Context context;
    static Context mcontext;
    NestedChatListAdapter chatAdapter;

    private Messages selectedMsg;
    private static final String TAG = NestedChatListFragment.class.getSimpleName();
    ContactsListAdapter contactsListAdapter;
    public static boolean isUser = true;
    public static boolean isAttachView = false;
    public static boolean isAttachBack = false;
    public static boolean isFetchInvitee = false;
    private LinearLayout ll_no_messages;
    private TextView txt_no_messages;
    String user_id="";
    RecyclerView rec_contact;
    TextView txt_no_contact;
    ProgressBar progress, progress1;
    Button btn_add;
    String messagereply="" ,createdByreply="",arrivalTimereply="", img_pathreply="",msg_typereply = "";

    EmojIconActions emojIcon;
    private String date = "", message = "", arrivalTime = "", createdBy = "", time = "",
            rplyimg_path="", currentDay = "", currentMonth = "", currentYear = "", to_email = "", msg_type = "", img_path = "";
    private boolean status = false, messageSendStatus = false, todayFound = false, yesterdayFound = false,
            sendFalg = false;

    String keyVal="";
    private ArrayList<JSONObject> fetchedObjects = new ArrayList<>();
    private ArrayList<JSONObject> sortedArrayMessagesObjectList = new ArrayList<JSONObject>();


    private ArrayList<ChatListModel> todayList = new ArrayList<>();
    private ArrayList<ChatListModel> yesterdayList = new ArrayList<>();
    private ArrayList<ChatListModel> specificDayList = new ArrayList<>();
    private ArrayList<ChatListModel> specificParentList = new ArrayList<>();

    public NestedChatListFragment() {
    }


    @BindView(R.id.rec_chat_list)
    RecyclerView rec_chat_list;

    @BindView(R.id.lin_chat_root)
    RelativeLayout lin_chat_root;


    @BindView(R.id.img_imoji)
    ImageView img_imoji;
    @BindView(R.id.img_send)
    ImageView img_send;
    @BindView(R.id.edit_msg)
    EmojiconEditText edit_msg;

    @BindView(R.id.img_close)
    ImageView img_close;
    @BindView(R.id.progress_chat)
    LinearLayout progressBar;
    @BindView(R.id.ll_img_send)
    LinearLayout ll_img_send;
    String response="";

    public static TextView txt_lbl;

    public static ImageView img_right_star_nested,img_right_delete_nested;

    public static boolean nestedmsg=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nested_chat_list, container, false);

        context = view.getContext();
        mcontext = view.getContext();

       /* getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
*/



        img_right_star_nested=view.findViewById(R.id.img_right_star_nested);

        img_right_delete_nested=view.findViewById(R.id.img_right_delete_nested);

        txt_lbl=view.findViewById(R.id.lbl);


        final LinearLayout root = new LinearLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ButterKnife.bind(this, view);
        user_id = GlobalClass.getUserId();

        //   progressBar.setVisibility(View.VISIBLE);

        System.out.println("*****************" + user_id);

        Log.d("json", String.valueOf(replynestedmsg));

        edit_msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    Pattern pattern = Pattern.compile("^\\s*$");
                    Matcher matcher = pattern.matcher(s);
                    boolean blankspace = matcher.find();

                    Log.d("blankspace", String.valueOf(blankspace));
                    if (!blankspace) {
                        ll_img_send.setEnabled(true);
                        ll_img_send.setAlpha(1f);
                    } else {
                        ll_img_send.setEnabled(false);
                        ll_img_send.setAlpha(0.4f);
                    }
                } else {
                    ll_img_send.setEnabled(false);
                    ll_img_send.setAlpha(0.4f);

                }

            }
        });
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        emojIcon = new EmojIconActions(context, lin_chat_root, edit_msg, img_imoji);
        emojIcon.ShowEmojIcon();
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {


            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });




        try {
            keyVal=replynestedmsg.getString("KEY_VAL");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();
            }
        });

        response= String.valueOf(replynestedmsg);

        JSONObject replyjson = replynestedmsg;
        int messageType1 = 0;


        edit_msg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    if (!(chatAdapter.getItemCount() == 0)) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rec_chat_list.scrollToPosition(chatAdapter.getItemCount() - 1);
                            }
                        }, 300);


                    }
                } catch (Exception ex) {
                    Log.e("scrollTolastPositon", "");
                }
                return false;
            }
        });



        if (replyjson != null) {
            messagereply = createdByreply = arrivalTimereply = msg_typereply = rplyimg_path="";
            if (replyjson.has("CML_TITLE")) {
                try {
                    messagereply = replyjson.getString("CML_TITLE");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (replyjson.has("CML_TYPE")) {
                try {
                    msg_typereply = replyjson.getString("CML_TYPE");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("msg_type", msg_type);
            }

            if (replyjson.has("CREATED_BY")) {
                try {
                    createdByreply = replyjson.getString("CREATED_BY");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (replyjson.has("CML_CHT_MESSAGE_TYPE")) {
                try {
                    messageType1 = replyjson.getInt("CML_CHT_MESSAGE_TYPE");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (replyjson.has("CML_MESSAGE_INDEX")) {
                try {
                    arrivalTimereply = String.valueOf(replyjson.getLong("CML_MESSAGE_INDEX"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (replyjson.has("CML_IMAGE_PATH")) {
                try {
                    rplyimg_path = GlobalClass.getLoginUrl() + replyjson.getString("CML_IMAGE_PATH").replace("?op=OPEN", "").replaceAll(" ", "%20");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        String senderName = createdByreply.equals(user_id) ? "" : createdByreply;

        Date arrivalDate = new Date(Long.parseLong(arrivalTimereply));
        SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        String arrivalDateStringreply = df1.format(arrivalDate);

        if (createdByreply.equals(user_id)) {
            ChatListModel model = new ChatListModel((messageType1 == 5 ? 3 : 2), messagereply,
                    arrivalDateStringreply,
                    "read", rplyimg_path, msg_typereply.contains("image"),
                    replyjson,
                    sendFalg,
                    senderName);
            specificParentList.add(model);
        }else
        {
            ChatListModel model = new ChatListModel((messageType1 == 5 ? 3 : 1), messagereply,
                    arrivalDateStringreply,
                    "read", rplyimg_path, msg_typereply.contains("image"),
                    replyjson,
                    sendFalg,
                    senderName);
            specificParentList.add(model);
        }

        Log.d("sender",senderName);

        fetchnestedmsg();
        setSubcriber();


        img_right_star_nested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedstarmsg.equals("1")) {
                    UnStarMsg();

                } else {
                    StarMsg();
                }

            }
        });


        img_right_delete_nested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMsg();
            }
        });

        return view;

    }


    private void deleteMsg() {

        try {
            ArrayList<String> delMsgList = App.getDelMsgKeyValList();
            Log.e("delMessageLengthBefore", " " + delMsgList.size());
            isUsersMsg = false;
            for (int i = 0; i < delMsgList.size(); i++) {
                if (!GlobalClass.getUserId().equals(delMsgList.get(i).split("\\*")[1])) {
                    isUsersMsg = true;
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
            builder.setTitle("Delete message");
            builder.setCancelable(false);
            ArrayList<String> items = new ArrayList<String>();
            items.add("Delete for me");
            //  if (!isUsersMsg)
            //   items.add("Delete for everyone");
            items.add("Cancel");
            String[] ch = items.toArray(new String[0]);
            //String[] ch = {"Camera","Gallery","Cancel"};
            builder.setItems(ch, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    boolean internetAvailability = InternetConnectionChecker.checkInternetConnection(getActivity());
                    switch (which) {
                        case 0:
                            //delMessage("REMOVE_MESSAGE");
                            int counter = 0;
                            for (int i = 0 ; i < conversationArrayListnested.size(); i++) {
                                for (int j = 1+i;  j < conversationArrayListnested.size() ; j++)
                                    //don't start on the same word or you'll eliminate it.
                                    if (conversationArrayListnested.get(j).getKeyval().equals( conversationArrayListnested.get(i).getKeyval() )  ) {
                                        conversationArrayListnested.remove(conversationArrayListnested.get(j));
                                        counter++;

                                    }
                            }

                            if (conversationArrayListnested.size() > 0) {
                                Log.d("conversationlistsize", String.valueOf(conversationArrayListnested.size()));
                                selectedMsg=JsonParserClass.parseMessagesJSONObject(replynestedmsg);
                                selectedMsg.deleteNestedMessage(conversationArrayListnested);                                 }

                                dialog.dismiss();
                            break;


                        case 2:

                            chatActinBarVisibility(0);
                            dialog.dismiss();
                            chatAdapter.resetFlag();

                        case 1:

                            chatAdapter.resetFlag();
                            chatAdapter.notifyDataSetChanged();
                            Log.i("click", "click");
                            dialog.dismiss();
                            break;
                        default:

                            break;

                    }
                }
            });

            builder.show();
        } catch (IndexOutOfBoundsException ex) {
            Log.e("IndexOutOfBoundEx", "" + ex.getMessage());
        }

    }


    public static void chatActinBarVisibility(int cnt) {
        img_right_star_nested.setVisibility(cnt > 0  ? View.VISIBLE :View.GONE);
        img_right_delete_nested.setVisibility(cnt > 0  ? View.VISIBLE :View.GONE);

        if(cnt>0)
        {
            txt_lbl.setText("");
        }

    }

    private void StarMsg() {
        try {
            selectedMsg=JsonParserClass.parseMessagesJSONObject(replynestedmsg);
            selectedMsg.getJSONObjectStarUnstarMessage(conversationArrayListnested, "star");

        } catch (Exception e) {
        }

    }

    private void UnStarMsg() {
        try {
            selectedMsg=JsonParserClass.parseMessagesJSONObject(replynestedmsg);
            selectedMsg.getJSONObjectStarUnstarMessage(conversationArrayListnested, "unstar");

        } catch (Exception e) {
        }
    }

    private void showData() {
        if (!(ChatList.size() == 0)) {

        }

        Repository repo = Repository.getRepository();

        if (repo != null ) {

            if (repo != null) {
                ArrayList<Messages> msgs = (ArrayList<Messages>) repo.getAllNestedMessages(keyVal);
                if (msgs != null) {

                    if (msgs.size() == 0) {
                        ChatList.clear();
                        rec_chat_list.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);


                    } else {
                        rec_chat_list.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);


                    }

                    if (msgs.size() > 0) {
                        fetchedObjects.clear();

                        List<JSONObject> msgList = new ArrayList<JSONObject>();
                        try {
                            //get all json objects
                            for (int k = 0; k < msgs.size(); k++) {
                                Messages msg = msgs.get(k);
                                if (msg != null) {
                                    String jsonObjString = msg.getCompleteData();
                                    if (jsonObjString != null) {
                                        msgList.add(new JSONObject(jsonObjString));
                                    }
                                }
                            }


                            //sort json object according to cml_message_index
                            Collections.sort(msgList, new Comparator<JSONObject>() {
                                @Override
                                public int compare(JSONObject jsonObjectA, JSONObject jsonObjectB) {
                                    int compare = 0;
                                    try {
                                        long index1 = jsonObjectA.getLong("CML_MESSAGE_INDEX");
                                        long index2 = jsonObjectB.getLong("CML_MESSAGE_INDEX");
                                        if (index1 > index2) {
                                            compare = 1;
                                        }
                                        if (index1 < index2) {
                                            compare = -1;
                                        }
                                        if (index1 == index2) {
                                            compare = 0;
                                        }
                                    } catch (JSONException e) {
                                        Log.e("expn_srt_logic1:", e.toString());
                                    } catch (Exception e) {
                                        Log.e("expn_srt_logic2:", e.toString());
                                    }
                                    return compare;
                                }
                            });

                            sortedArrayMessagesObjectList.clear();
                            if (msgList != null) {
                                if (msgList.size() > 0) {
                                    for (int k = 0; k < msgList.size(); k++) {
                                        sortedArrayMessagesObjectList.add(msgList.get(k));
                                    }
                                }
                            }
                            if (sortedArrayMessagesObjectList.size() > 0) {
                                int messageType = 0;
                                String userId = "";
                                userId = GlobalClass.getUserId();
                                todayList.clear();
                                yesterdayList.clear();
                                specificDayList.clear();

                                String specificDayString = "";
                                long diff1 = 0,diffDays=0;


                                for (int i = 0; i < sortedArrayMessagesObjectList.size(); i++) {
                                    JSONObject singleMsgObj = sortedArrayMessagesObjectList.get(i);

                                    Log.i("msg", String.valueOf(singleMsgObj));

                                    if (singleMsgObj != null) {
                                        message = createdBy = arrivalTime = img_path = msg_type = "";
                                        if (singleMsgObj.has("CML_TITLE")) {
                                            message = singleMsgObj.getString("CML_TITLE");
                                        }
                                        if (singleMsgObj.has("CML_TYPE")) {
                                            msg_type = singleMsgObj.getString("CML_TYPE");
                                            Log.i("msg_type", msg_type);
                                        }
                                        /*if(singleMsgObj.has("imgPath")){
                                            img_path=com.nc.developers.cloudscommunicator.GlobalClass.getLoginUrl()+singleMsgObj.getString("imgPath").replace("?op=OPEN","").replaceAll(" ","%20");
                                        }*/
                                        if (singleMsgObj.has("CML_IMAGE_PATH")) {
                                            img_path = GlobalClass.getLoginUrl() + singleMsgObj.getString("CML_IMAGE_PATH").replace("?op=OPEN", "").replaceAll(" ", "%20");
                                        }
                                        if (singleMsgObj.has("CREATED_BY")) {
                                            createdBy = singleMsgObj.getString("CREATED_BY");
                                        }
                                        if (singleMsgObj.has("CML_CHT_MESSAGE_TYPE")) {
                                            messageType = singleMsgObj.getInt("CML_CHT_MESSAGE_TYPE");
                                        }
                                        if (singleMsgObj.has("CML_MESSAGE_INDEX")) {
                                            arrivalTime = String.valueOf(singleMsgObj.getLong("CML_MESSAGE_INDEX"));
                                        }
                                        if (singleMsgObj.has("CML_MESSAGE_INDEX")) {
                                            arrivalTime = String.valueOf(singleMsgObj.getLong("CML_MESSAGE_INDEX"));
                                        }
                                        Log.i(TAG + "_rawArrivalTime*:", arrivalTime + " ..kk");
                                        Date arrivalDate = new Date(Long.parseLong(arrivalTime));
                                        SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
                                        String arrivalDateString = df1.format(arrivalDate);
                                        Log.i(TAG + "_arrivalDate*:", arrivalDateString + " .kk");

                                        Date currentDate = new Date();
                                        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
                                        String currentDateString = df2.format(currentDate);
                                        Log.i(TAG + "_currentDate*:", currentDateString + " .kk");

                                        String senderName = "";
                                        senderName = createdBy.equals(userId) ? "" : createdBy;

                                       /* diff = currentDate.getTime() - arrivalDate.getTime();
                                        diffInDays = java.util.concurrent.TimeUnit.DAYS.convert(diff, java.util.concurrent.TimeUnit.MILLISECONDS);
                                        Log.i("daysDiff:", String.valueOf(diffInDays) + " ..kk");
*/


                                        String dateStart = currentDateString;
                                        String dateStop = arrivalDateString;

                                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

                                        Date d1 = null;
                                        Date d2 = null;

                                        d1 = format.parse(dateStop);
                                        d2 = format.parse(dateStart);
                                        diff1 = d2.getTime() - d1.getTime();
                                        diffDays = diff1 / (24 * 60 * 60 * 1000);

                                        Log.i("diff1:", String.valueOf(diffDays) + " ..kk");

                                        Log.i(TAG, "image_path" + img_path);
                                        if (diffDays == 0) {
                                            if (createdBy.equals(userId)) {
                                                ChatListModel model = new ChatListModel((messageType == 5 ? 3 : 2), message,
                                                        arrivalDateString,
                                                        "read", img_path, msg_type.contains("image"),
                                                        singleMsgObj,
                                                        sendFalg,
                                                        senderName);
                                                todayList.add(model);
                                            } else {
                                                ChatListModel model = new ChatListModel((messageType == 5 ? 3 : 1), message,
                                                        arrivalDateString,
                                                        "no_status", img_path, msg_type.contains("image"),
                                                        singleMsgObj,
                                                        sendFalg,
                                                        senderName);
                                                todayList.add(model);
                                            }
                                        }
                                        //if(result<0){
                                        if (diffDays > 0) {
                                            diff1 = d2.getTime() - d1.getTime();
                                            diffDays = diff1 / (24 * 60 * 60 * 1000);
                                            Log.i("daysDiff:", String.valueOf(diffDays) + " ..kk");
                                            //  diffDays += 1;
                                            if (diffDays == 1) {
                                                if (createdBy.equals(userId)) {
                                                    ChatListModel model = new ChatListModel((messageType == 5 ? 3 : 2), message,
                                                            arrivalDateString,
                                                            "read", img_path, msg_type.contains("image"),
                                                            singleMsgObj,
                                                            sendFalg,
                                                            senderName);
                                                    yesterdayList.add(model);
                                                } else {
                                                    ChatListModel model = new ChatListModel((messageType == 5 ? 3 : 1), message,
                                                            arrivalDateString,
                                                            "no_status", img_path, msg_type.contains("image"),
                                                            singleMsgObj,
                                                            sendFalg,
                                                            senderName);
                                                    yesterdayList.add(model);
                                                }
                                            } else {
                                                if (!specificDayString.equals(arrivalDateString)) {
                                                    specificDayString = arrivalDateString;
                                                    ChatListModel model = new ChatListModel(3, "", arrivalDateString,
                                                            "",
                                                            null,
                                                            false,
                                                            new JSONObject().put("CML_CHT_MESSAGE_TYPE", 0),
                                                            sendFalg,
                                                            senderName);
                                                    specificDayList.add(model);
                                                }
                                                if (createdBy.equals(userId)) {
                                                    ChatListModel model = new ChatListModel((messageType == 5 ? 3 : 2), message, arrivalDateString,
                                                            "read", img_path, msg_type.contains("image"),
                                                            singleMsgObj,
                                                            sendFalg,
                                                            senderName);
                                                    specificDayList.add(model);
                                                } else {
                                                    ChatListModel model = new ChatListModel((messageType == 5 ? 3 : 1), message, arrivalDateString,
                                                            "no_status", img_path, msg_type.contains("image"),
                                                            singleMsgObj,
                                                            sendFalg,
                                                            senderName);
                                                    specificDayList.add(model);
                                                }
                                            }
                                        }
                                    }
                                }
                                ChatList.clear();
                                boolean isYesterdayStringAdded, isTodayStringAdded;
                                isYesterdayStringAdded = isTodayStringAdded = false;
                                //  specificParentList.add()

                                ChatList.addAll(specificParentList);
                                ChatList.addAll(specificDayList);
                                if (yesterdayList.size() > 0) {
                                    if (!isYesterdayStringAdded) {
                                        ChatListModel model = new ChatListModel(3, "", "YESTERDAY",
                                                "", null, false,
                                                new JSONObject().put("CML_CHT_MESSAGE_TYPE", 0),
                                                sendFalg,
                                                "");
                                        yesterdayList.add(0, model);
                                        isYesterdayStringAdded = true;
                                    }
                                    ChatList.addAll(yesterdayList);
                                }
                                if (todayList.size() > 0 && diffDays == 0) {
                                    if (!isTodayStringAdded) {
                                        ChatListModel model = new ChatListModel(3, "", "TODAY",
                                                "", null, false,
                                                new JSONObject().put("CML_CHT_MESSAGE_TYPE", 0),
                                                sendFalg,
                                                "");
                                        todayList.add(0, model);
                                        isTodayStringAdded = true;
                                    }
                                    ChatList.addAll(todayList);
                                }
                                if (ChatList.size() > 0) {
                                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                    //  mLayoutManager.setStackFromEnd(true);
                                    //rec_chat_list.setLayoutManager(mLayoutManager);
                                    CustomLinearLayoutManager customLinearLayoutManager =
                                            new CustomLinearLayoutManager(context);
                                    rec_chat_list.setLayoutManager(customLinearLayoutManager);
                                    rec_chat_list.setItemAnimator(null);
                                    if (chatAdapter == null) {

                                        progressBar.setVisibility(View.GONE);
                                        txt_lbl.setVisibility(View.VISIBLE);
                                        rec_chat_list.getRecycledViewPool().clear();
                                        chatAdapter = new NestedChatListAdapter(ChatList, getActivity(),
                                                NestedChatListFragment.this,response);
                                        rec_chat_list.setAdapter(chatAdapter);
                                    } else {

                                        progressBar.setVisibility(View.GONE);
                                        txt_lbl.setVisibility(View.VISIBLE);

                                        chatAdapter.reload(ChatList);
                                        rec_chat_list.getRecycledViewPool().clear();
                                        chatAdapter.notifyDataSetChanged();
                                    }
                                    rec_chat_list.scrollToPosition(chatAdapter.getItemCount() - 1);
                                    System.out.println("S*****************" + user_id);
                                    Log.i("after msgs objects: ", "");
                                    Log.i("what: ", "end happens here");
                                }
                            }
                        } catch (JSONException e) {
                            Log.e("expn_srt_msg1:", e.toString());
                        } catch (Exception e) {
                            Log.e("expn_srt_msg2:", e.toString());
                        }
                    }
                } else {

                    rec_chat_list.setVisibility(View.GONE);
                    txt_lbl.setVisibility(View.GONE);


                }
            }
        }
    }



    @Optional
    @OnClick({R.id.ll_img_send})
    public void onSelectDeselect(View view) {
        //System.out.println("iiiiiiiiiiiiiiii" + view.getId());
        switch (view.getId()) {
            case R.id.ll_img_send:
                sendreplyMsg();
                ll_img_send.setEnabled(false);
                ll_img_send.setAlpha(0.4f);

        }
    }

    private void sendreplyMsg() {
        String messageString = "";
        messageString = edit_msg.getText().toString().trim();
        Messages messages = JsonParserClass.parseMessagesJSONObject(replynestedmsg);

        if ( messageString != null) {
            selectedConversationrply.getJSONObjectReplyMessageOneToOne(messages,
                    messageString);
            ll_img_send.setEnabled(true);
            ll_img_send.setAlpha(1f);
            edit_msg.setText("");
        }
    }

    private void fetchnestedmsg() {
        try {
            selectedMsg=JsonParserClass.parseMessagesJSONObject(replynestedmsg);
            selectedMsg.getJSONObjectFetchNestedMessages("");

        } catch (Exception e) {
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
                Log.i("nestedchatlist", string + " ..kk");

                if (string.equals("fetch_msg_complete")) {

                    progressBar.setVisibility(View.GONE);

                    if(NestedChatListAdapter.selectedCountnested<=0)
                    {
                        showData();

                    }
                }

                if (string.equals("broadMsgStatus"))
                {
                    showData();
                }

                if (string.equals("msg_edit_done")||string.equals("delete_msg")) {
                    showData();
                    selectedstarmsg = "";
                    try {
                        if (chatAdapter != null) {
                            chatAdapter.resetFlag();
                            chatAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                    }

                }

            }
        };
        com.nc.developers.cloudscommunicator.Subcription s = new com.nc.developers.cloudscommunicator.Subcription();
        s.setObservable(mobservable);
        s.setObserver(myObserver);
        GlobalClass.setLoginFragmentSubcriberr(s);
    }


    @Override
    public boolean onBackPressed() {

        Fragment f=getFragmentManager().findFragmentById(R.id.frame_frag_container);
        if(f instanceof NestedChatListFragment)
        {
            common.hideKeyboard(getActivity());
            FragmentManager manager=getActivity().getSupportFragmentManager();
            FragmentTransaction trans=manager.beginTransaction();
            trans.remove(new NestedChatListFragment());
            trans.commit();
            manager.popBackStack();
            nestedmsg=true;
            ChatListFragment.showingimage=false;
            ChatListFragment.rply=true;
            Bundle bundle = new Bundle();
            bundle.putString("group_json", ""+getArguments().getString("group_json"));
            Fragment fragment = new ChatListFragment();
            fragment.setArguments(bundle);
            ReplaceFragment.replaceFragment(NestedChatListFragment.this, R.id.frame_frag_container, fragment, false);

        }   return true;
    }

}