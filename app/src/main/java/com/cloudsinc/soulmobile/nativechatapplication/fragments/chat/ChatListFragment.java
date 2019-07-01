package com.cloudsinc.soulmobile.nativechatapplication.fragments.chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
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
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.ConversationListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.ForwardMsgListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.dialogs.AddEventDialog;
import com.cloudsinc.soulmobile.nativechatapplication.dialogs.NestedChatListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.dialogs.UploadFileDialog;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactProfileFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactsSearchFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation.ConversationListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.utils.DateClass;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.models.Contact;
import com.nc.developers.cloudscommunicator.models.Conversation;
import com.nc.developers.cloudscommunicator.models.Login;
import com.nc.developers.cloudscommunicator.models.Messages;

import com.nc.developers.cloudscommunicator.sharedprefrences.PrefManager;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.cloudsinc.soulmobile.nativechatapplication.App;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.datamodels.ChatListModel;
import com.cloudsinc.soulmobile.nativechatapplication.dialogs.CustomDialog;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.FragmentHomeScreen;
import com.cloudsinc.soulmobile.nativechatapplication.interfaces.IOnBackPressed;
import com.cloudsinc.soulmobile.nativechatapplication.utils.CustomLinearLayoutManager;
import com.cloudsinc.soulmobile.nativechatapplication.utils.GalleryRealPath;
import com.cloudsinc.soulmobile.nativechatapplication.utils.GpsTracker;
import com.cloudsinc.soulmobile.nativechatapplication.utils.InternetConnectionChecker;
import com.cloudsinc.soulmobile.nativechatapplication.utils.JsonObject;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;
import com.cloudsinc.soulmobile.nativechatapplication.utils.common;
import com.nc.developers.cloudscommunicator.socket.JsonParserClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.WIFI_SERVICE;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.GallaryimgList;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.GallarypdfList;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.conversationArrayList;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.image_pos;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.msg;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.reply_pos;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.rplyJsonObj;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.rplyMsg;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter.selectedstarmsg;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.ForwardMsgListAdapter.forwardConvertionList;
import static com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactProfileFragment.backcounterContact;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.ImageCompress.compressImage;

/**
 * This Fragment is used to display send and receive messages and attachment
 *
 * @author Prajakta Patil
 * @version 1.0
 * @since 2.11.2018
 */

public class ChatListFragment extends Fragment implements View.OnClickListener, IOnBackPressed {

    private Observable<String> mobservable;
    private Observer<String> myObserver;
    private boolean isUsersMsg;
    public static ArrayList<ChatListModel> ChatList = new ArrayList<>();
    private static ArrayList<Messages> msgList = new ArrayList<>();
    private ArrayList<Conversation> groupNmList = new ArrayList<Conversation>();
    private ArrayList<Conversation> groupNmforwardList = new ArrayList<Conversation>();
    public static ArrayList<Messages> replyConvArrayList = new ArrayList<>();
    private View view;
    private Context context;
    private ChatListAdapter chatAdapter;
    public static Dialog dialog;
    public static boolean isUser = true;
    public static boolean isAttachView = false;
    public static boolean isAttachBack = false;
    public static boolean isFetchInvitee = false;
    private LinearLayout ll_no_messages;
    private TextView txt_no_messages;
    private RecyclerView rec_contact;
    private TextView txt_no_contact;
    private ProgressBar progress1;
    private Button btn_add;
    public static String groupTitle;
    private String grpKeyVal = null;
    private String grpownerId = null;
    public static String forwardmsg="";
    private JSONObject clickedJsonObject = null;
    private JSONObject conversationObj = null;
    private static final String TAG = ChatListFragment.class.getSimpleName();
    private Bitmap bitmap;
    private ImageView imageToSend;
    private int position;
    public Calendar cc;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:MM");
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 150;
    private static final int GALLERY_PERMISSION_REQUEST_CODE = 250;
    private Uri ImgProfileUri;
    private String mediaPath;
    public static ArrayList<String> selected_forward_arr;
    private GpsTracker gpsTracker;
    private TextView text_link;
    private ArrayList<ChatListModel> todayList = new ArrayList<>();
    private ArrayList<ChatListModel> yesterdayList = new ArrayList<>();
    private ArrayList<ChatListModel> specificDayList = new ArrayList<>();
    private ArrayList<ChatListModel> tempList = new ArrayList<>();
    private int PICK_PDF_REQUEST = 10;
    private ArrayList<String> docPaths = new ArrayList<>();
    private ArrayList<String> docfile = new ArrayList<>();
    private ArrayList<String> photoPaths = new ArrayList<>();
    private ArrayList<String> Gallarypath = new ArrayList<>();
    private ArrayList<String> filename = new ArrayList<>();
    private ArrayList<String> fileExtension = new ArrayList<>();
    private ArrayList<String> fileUrl = new ArrayList<>();
    public static ArrayList<String> uploadresponse=new ArrayList<>();
    public static ArrayList<String> uploadfileresponse=new ArrayList<>();

    public ChatListFragment() {
    }

    private Conversation selectedConversation;
    public static Conversation selectedConversationrply,selected_Conversation;
    public static String nestedjsondata="";

    @SuppressLint("ValidFragment")
    public ChatListFragment(Bitmap bitmap, int position, ImageView imageToSend) {
        this.bitmap = bitmap;
        this.imageToSend = imageToSend;
        this.position = position;
    }

    @BindView(R.id.rec_chat_list)
    RecyclerView rec_chat_list;
    @BindView(R.id.img_attachment)
    ImageView img_attachment;
    @BindView(R.id.img_imoji)
    ImageView img_imoji;
    @BindView(R.id.img_send)
    ImageView img_send;
    @BindView(R.id.edit_msg)
    EmojiconEditText edit_msg;
    @BindView(R.id.ll_attachment)
    LinearLayout ll_attachment;
    @BindView(R.id.img_gallery)
    ImageView img_gallery;
    @BindView(R.id.img_cam)
    ImageView img_cam;
    @BindView(R.id.img_audio)
    ImageView img_audio;
    @BindView(R.id.lin_chat_root)
    LinearLayout lin_chat_root;
    @BindView(R.id.ll_chat_progress)
    LinearLayout ll_chat_progress;
    @BindView(R.id.img_loc)
    ImageView img_loc;
    @BindView(R.id.ll_img_attachment)
    LinearLayout ll_img_attachment;
    @BindView(R.id.ll_img_send)
    LinearLayout ll_img_send;
    @BindView(R.id.progress_chat)
    LinearLayout progressBar;
    @BindView(R.id.msgLayout)
    LinearLayout msgLayout;
    @BindView(R.id.txt_removed)
    TextView txt_removed;

    private JSONObject calmailObj = null;
    private EmojIconActions emojIcon;
    private RelativeLayout rel_upload_progress;
    private LinearLayout ll_chat_progress_audio;
    public static LinearLayout li_send_msg_back, li_reply, li_msg_layout;
    private ImageView img_left_icon;
    private LinearLayout ll_img_left_icon;
    private ImageView img_search;
    private LinearLayout ll_search;
    private static ImageView img_right_icon1;
    private static ImageView img_right_icon2;
    private static ImageView img_right_icon3;
    private static ImageView img_right_forward;
    public static ImageView img_right_star;
    private static TextView txt_action_title;
    private static RelativeLayout lin_group_member_list;
    private static RelativeLayout lin_action_bar;
    public static LinearLayout li_img_star,li_img_forward,li_img_delete,li_img_reply;
    private static LinearLayout lin_search_bar;
    public static EditText edit_search;
    public String strgson;
    private JSONObject group_json = null;
    private String user_id, response_attachemnt_url = "", response_attachemnt_name = "";
    private int max = 9000;
    private int min = 1000;
    private Repository repo = Repository.getRepository();
    private Login login = repo.getLoginData();
    public static UploadFileDialog dFragment=null;
    private String jwtToken = login.getJwtToken();
    private String tp, jwt_token = jwtToken;
    private int random;
    private Dialog d;
    private Button btn_send, btn_audio_send, btn_cancel, btn_loc_send, btn_loc_cancel;
    private ImageView audioCaptionDialog, img_close;
    private String date = "", message = "", arrivalTime = "", createdBy = "", time = "",
            onlinetime="",msg_type = "", img_path = "";
    private boolean sendFalg = false;
    private String designation = "";
    private Uri photoURI = null;
    private ArrayList<JSONObject> fetchedObjects = new ArrayList<>();
    private ArrayList<JSONObject> sortedArrayMessagesObjectList = new ArrayList<JSONObject>();
    private ArrayList<ChatListModel> temp ;
    private String attach_image_path = "";
    private String attach_audio_path = "";
    private Thread msgThread;
    public static boolean fromChat = false;
    public static String fromfrgament = "",forwardsingle="";
    boolean replyConv = false;
    private boolean one_to_one_flag = false;
    public static boolean ismakeImportant = false;
    public static boolean rply=false;
    public static boolean showingimage=false;
    private Menu menu;
    public static  PopupMenu popup;
    private static boolean flag_Archive=false;
    private static boolean isStarMessages=false;
    @BindView(R.id.lin_add_block)
    LinearLayout lin_add_block;
    @BindView(R.id.lin_Un_block)
    LinearLayout lin_Un_block;
    @BindView(R.id.btn_addcontact)
    Button btn_addcontact;
    @BindView(R.id.btn_blockcontact)
    Button btn_blockcontact;
    @BindView(R.id.btn_unblockcontact)
    Button btn_unblockcontact;
    @BindView(R.id.txt_sender_name)
    TextView txt_sender_name;
    @BindView(R.id.txt_msg)
    TextView txt_msg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        context = view.getContext();

        selected_forward_arr = new ArrayList<>();
        temp=new ArrayList<>();

        ButterKnife.bind(this, view);
        //prajakta26

        isStarMessages=false;
        progressBar.setVisibility(View.VISIBLE);



        Repository repo = Repository.getRepository();
        if (repo != null && selectedConversation != null) {
            ArrayList<Messages> msgs = (ArrayList<Messages>) repo.getAllMessages(selectedConversation.getKeyval());
            if (msgs.size() > 0) {
                showData();
            }
        }

        String clickedJsonString=getArguments().getString("group_json");

        nestedjsondata=clickedJsonString;

        try {
            clickedJsonObject = new JSONObject(clickedJsonString);
            Log.i("clickedObj: ", clickedJsonObject.toString());
            grpKeyVal = clickedJsonObject.getString("KEY_VAL");
            grpownerId = clickedJsonObject.getString("OWNER_ID");
            flag_Archive= (clickedJsonObject.getInt("ACTIVE_STATUS")==9)?true:false;
            Log.d("keyvalis", grpKeyVal);
        } catch (JSONException e) {
            Log.e("exception: ", "" + e.toString());
        }


        random = new Random().nextInt(max - min + 1) + min;
        tp = String.valueOf(System.currentTimeMillis());
        img_left_icon = (ImageView) view.findViewById(R.id.img_left_icon);
        ll_img_left_icon = (LinearLayout) view.findViewById(R.id.ll_img_left_icon);

        //prajakta2
        ll_search = (LinearLayout) view.findViewById(R.id.ll_search);
        img_search = (ImageView) view.findViewById(R.id.img_search);



        img_right_icon1 = (ImageView) view.findViewById(R.id.img_right_icon1);
        img_right_icon2 = (ImageView) view.findViewById(R.id.img_right_icon2);
        img_right_icon3 = (ImageView) view.findViewById(R.id.img_right_icon3);
        img_right_forward = (ImageView) view.findViewById(R.id.img_right_forward);
        img_right_star=(ImageView)view.findViewById(R.id.img_right_star);
        txt_action_title = (TextView) view.findViewById(R.id.txt_action_title);
        lin_group_member_list = (RelativeLayout) view.findViewById(R.id.lin_group_member_list);
        lin_action_bar = (RelativeLayout) view.findViewById(R.id.lin_action_bar);
        ll_no_messages = (LinearLayout) view.findViewById(R.id.ll_no_messages);
        txt_no_messages = (TextView) view.findViewById(R.id.txt_no_messages);
        li_send_msg_back = (LinearLayout) view.findViewById(R.id.li_send_bkround);
        li_reply = (LinearLayout) view.findViewById(R.id.li_reply);
        li_msg_layout = (LinearLayout) view.findViewById(R.id.li_msg);
        img_close = (ImageView) view.findViewById(R.id.img_close);

        //prajakta2
        lin_search_bar=(LinearLayout) view.findViewById(R.id.lin_search_bar);
        edit_search=(EditText) view.findViewById(R.id.edit_search);
        edit_search.setHint("Search Message");
        edit_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);


        li_img_star=(LinearLayout) view.findViewById(R.id.li_img_star);
        li_img_forward=(LinearLayout) view.findViewById(R.id.li_img_forward);
        li_img_delete=(LinearLayout) view.findViewById(R.id.li_img_delete);
        li_img_reply=(LinearLayout) view.findViewById(R.id.li_img_reply);

        ll_no_messages.setVisibility(View.VISIBLE);
        img_right_icon3.setOnClickListener(this);
        lin_action_bar.setVisibility(View.VISIBLE);
        ChatListFragment.chatActinBarVisibility(0);

        try {
            if(!clickedJsonObject.getBoolean("CML_IS_ACTIVE"))
            {
                txt_removed.setVisibility(View.VISIBLE);
                li_msg_layout.setVisibility(View.GONE);
            }
            else
            {
                txt_removed.setVisibility(View.GONE);
                li_msg_layout.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //prajakta2
        edit_search.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s,int start,int count,int after){
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
            }
            @Override
            public void afterTextChanged(Editable s){
                if(ChatList.size()>0){
                    filter(s.toString());

                }else {
                    // isMsgInfoshow=true;

                }
            }
        });


        try {
            if(clickedJsonObject.getString("SUB_KEY_TYPE").equalsIgnoreCase("TSK_SCONV_LST")&&
                    clickedJsonObject.getBoolean("IS_CONTACT")==false ){
                Log.i("subkeyType",clickedJsonObject.getString("SUB_KEY_TYPE"));
                Log.i("isCOntact", String.valueOf(clickedJsonObject.getBoolean("IS_CONTACT")));
                lin_add_block.setVisibility(View.VISIBLE);
            }

        } catch (Exception ex) {
            Log.e(TAG, "--------" + ex.getMessage());
        }

        ll_img_send.setEnabled(false);
        ll_img_send.setAlpha(0.4f);

        //prajakta2
        ll_search.setVisibility(View.VISIBLE);
        ll_search.setOnClickListener(this);
        img_left_icon.setOnClickListener(this);
        img_right_icon2.setOnClickListener(this);
        try {
            String grpJsonString = getArguments().getString("group_json");
            group_json = new JSONObject(grpJsonString);
            Log.d("grpJsonIs", "aaaaaaaaaaaaaaaaaaaa" + group_json);
            if (!(App.getChatText() == null)) {
                if (App.getChatText().length() > 0 && group_json.getString("KEY_VAL").equals(App.getChatkeyval())) {
                    edit_msg.setText(App.getChatText());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String conversation_completeData = "";
        Bundle bundle = getArguments();
        conversation_completeData = bundle.getString("group_json");
        try {
            conversationObj = new JSONObject(conversation_completeData);
            Log.d("conve_completeData",conversation_completeData);

            Log.d("conversationObj", conversationObj.toString());
            selectedConversation = JsonParserClass.parseConversationJsonObject(conversationObj);
            selectedConversationrply=JsonParserClass.parseConversationJsonObject(conversationObj);
            selected_Conversation=JsonParserClass.parseConversationJsonObject(conversationObj);

            ismakeImportant = conversationObj.getInt("CML_STAR") == 0 ? false : true;
            setIsmakeImportant();
            Log.d("importantStatus", String.valueOf(ismakeImportant));

            Log.d("status", String.valueOf(conversationObj.getBoolean("CML_IS_ACTIVE")));
            if(!conversationObj.getBoolean("CML_IS_ACTIVE"))
            {
                txt_removed.setVisibility(View.VISIBLE);
                li_msg_layout.setVisibility(View.GONE);
            }
            else
            {
                txt_removed.setVisibility(View.GONE);
                li_msg_layout.setVisibility(View.VISIBLE);
            }

            if(conversationObj.getInt("BLOCK_STATUS")==5){
                lin_add_block.setVisibility(View.GONE);
                txt_removed.setText("You have Blocked "+conversationObj.getString("CML_TITLE"));
                txt_removed.setVisibility(View.VISIBLE);
                li_msg_layout.setVisibility(View.GONE);
                lin_Un_block.setVisibility(View.VISIBLE);
            }

            if(conversationObj.getInt("BLOCK_STATUS")==6){
                lin_add_block.setVisibility(View.GONE);
                txt_removed.setText(conversationObj.getString("CML_TITLE")+" has Blocked you");
                txt_removed.setVisibility(View.VISIBLE);
                li_msg_layout.setVisibility(View.GONE);
                lin_Un_block.setVisibility(View.GONE);

            }

        } catch (JSONException e) {
            Log.e("getting_conv1:", e.toString());
        } catch (Exception e) {
            Log.e("getting_conv2:", e.toString());
        }


        img_right_icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                li_send_msg_back.setBackgroundResource(R.drawable.send_msg_bg);
                li_reply.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    li_msg_layout.setBackground(null);
                }

                replyConv = true;
                txt_sender_name.setText(rplyMsg);
                txt_msg.setText(msg);

                if (rplyMsg.equals("") || rplyMsg.equals(null)) {
                    if (!isUsersMsg) {
                        txt_sender_name.setText("You");
                    }
                }

            }
        });

        img_right_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedstarmsg.equals("1")) {
                    UnStarMsg();

                } else {
                    StarMsg();
                }

            }
        });

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

                        ll_attachment.setVisibility(View.GONE);
                    }
                } catch (Exception ex) {
                    Log.e("scrollTolastPositon", "");
                }
                return false;
            }
        });


        try {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


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

        user_id = GlobalClass.getUserId();
        System.out.println("*****************" + user_id);
        System.out.println("singleMsgObj" + getArguments());
        try {
            group_json = new JsonObject(new JSONObject(getArguments().getString("group_json")));
            strgson=getArguments().getString("group_json");
            Log.d("GrpTitleString",strgson);
            Log.i("GrpTitle", group_json.getString("CML_TITLE"));
            txt_action_title.setText(group_json.getString("CML_TITLE"));
            groupTitle = group_json.getString("CML_TITLE");
            if (group_json.getString("CREATED_BY").equals(GlobalClass.getUserId()))
                designation = "ADMIN";
            else designation = "GROUP MEMBER";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        resetMsgCountcall();
        setSubcriber();

        emojIcon = new EmojIconActions(context, lin_chat_root, edit_msg, img_imoji);

        img_imoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emojIcon.ShowEmojIcon();
            }
        });


        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                ll_attachment.setVisibility(View.GONE);
                isAttachView = false;
                isAttachBack = true;
                Log.e("Keyboard", "open");

            }

            @Override
            public void onKeyboardClose() {

                Log.e("Keyboard", "close");

            }
        });


        emojIcon.setUseSystemEmoji(true);
        edit_msg.setUseSystemDefault(true);

        img_right_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();
            }
        });


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatListFragment.li_reply.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ChatListFragment.li_msg_layout.setBackgroundResource(R.drawable.send_msg_bg);
                    ChatListFragment.li_send_msg_back.setBackground(null);
                }
                replyConv = false;
                if (chatAdapter != null) {
                    chatAdapter.resetFlag();
                    chatAdapter.notifyDataSetChanged();
                }

            }
        });

        lin_group_member_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupMemberListFrag.isGroupMemberProfile = false;
                fromChat = true;
                Log.d("Gjson", clickedJsonObject.toString());
                common.hideKeyboard(getActivity());
                if (clickedJsonObject != null) {
                    App.setGrpObjForEdit(clickedJsonObject);
                    if (fromfrgament.equals("contact")) {

                        fromfrgament = "contact";
                    } else {
                        fromfrgament = "";
                    }
                    try {
                        if (clickedJsonObject.getString("SUB_KEY_TYPE").equals("TSK_SCONV_LST")){
                            //  Toast.makeText(mcontext,"new Toast",Toast.LENGTH_LONG).show();
                            ContactsSearchFragment.isSearchContac=false;
                            Bundle bundle = new Bundle();
                            backcounterContact=0;
                            bundle.putString("contact_json", ""+strgson);
                            Log.d("group_jsonfortest",""+strgson);
                            // bundle.putString("selected_contact",String.valueOf(groupObject));// see to it
                            Fragment fragment = new ContactProfileFragment();
                            fragment.setArguments(bundle);
                            ReplaceFragment.replaceFragment(ChatListFragment.this, R.id.frame_frag_container, fragment, true);

                        }

                        if (!clickedJsonObject.getString("SUB_KEY_TYPE").equals("TSK_SCONV_LST"))
                            ReplaceFragment.replaceFragment(ChatListFragment.this, R.id.frame_frag_container, new GroupMemberListFrag(), true);

                    }

                    catch (Exception ex) {
                        Log.e(TAG, "------------" + ex.getMessage());
                    }

                }
            }
        });


        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.delete);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        System.out.println("jjjjjjjjjjjjjj" + jwt_token);

        try {
            one_to_one_flag = clickedJsonObject.getString("SUB_KEY_TYPE").equals("TSK_SCONV_LST");
        } catch (Exception ex) {
            Log.e(TAG, "------" + ex.getMessage());
        }

        Log.d("one_flag", String.valueOf(one_to_one_flag));
        if (!one_to_one_flag) {
            fetcInvitee();
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            fetcMessages();
        }

        /// ankita chnages
        if (ChatList.size() == 0) {
            progressBar.setVisibility(View.GONE);

            if (ChatList.size() > 0) {
                showData();
            } else {
                ll_no_messages.setVisibility(View.VISIBLE);
            }

        } else {
            showData();
        }

        this.menu = menu;

        return view;

    }

    //prajakta2
    private void filter(String text){
        String filterString = "";
        if(ChatList!=null) {
            for (ChatListModel d : ChatList) {
                try {
                    if(!(d.getViewType()==3) && (!d.isMedia())){
                        if(!(d.getJsonMessage().has("CML_TYPE"))){
                            filterString = d.getJsonMessage().getString("CML_TITLE").toString();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (filterString.contains(text)) {
                    if(edit_search.getText().length()>0 && (!(d.getViewType()==3))){
                        if(!(d.getJsonMessage().has("CML_TYPE"))){
                            temp.add(d);
                        }


                    }
                    else {
                        temp.add(d);
                    }
                }
            }
        }
        if(temp!=null){
            chatAdapter.filterList(temp);
            if(temp.size()>0){
                rec_chat_list.setVisibility(View.VISIBLE);
                ll_no_messages.setVisibility(View.GONE);
                txt_no_messages.setVisibility(View.GONE);
                txt_no_messages.setText("");
            }
            else {
                rec_chat_list.setVisibility(View.GONE);
                ll_no_messages.setVisibility(View.VISIBLE);
                txt_no_messages.setVisibility(View.VISIBLE);
                txt_no_messages.setText("No Result Found");

            }
        }

    }


    public String getLocation() {
        gpsTracker = new GpsTracker(getActivity());
        String url = null;
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            Log.d("latlongis", "" + latitude);
            url = "http://maps.google.com/maps?daddr=" + latitude + "/" + longitude;
        } else {
            gpsTracker.showSettingsAlert();
        }

        return url;
    }

    private void showDialog() {

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setContentView(R.layout.custom_forward_msg_dialog);
        btn_add = dialog.findViewById(R.id.btn_forward);

        dialog.setCancelable(false);
        rec_contact = (RecyclerView) dialog.findViewById(R.id.rec_contact);
        txt_no_contact = dialog.findViewById(R.id.txt_no_contact);
        progress1 = dialog.findViewById(R.id.progress);
        ImageView img_close = dialog.findViewById(R.id.img_close);

        selected_forward_arr.clear();

        if (selected_forward_arr.size() > 0) {
            btn_add.setAlpha(1f);
            btn_add.setEnabled(true);
        } else {
            btn_add.setAlpha(0.5f);
            btn_add.setEnabled(false);

        }

        featchallConversationCall();


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendForwardMsg();
                forwardmsg="forward";
                btn_add.setAlpha(0.5f);
                btn_add.setEnabled(false);

            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chatAdapter != null) {
                    chatAdapter.resetFlag();
                    chatAdapter.notifyDataSetChanged();
                }

                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    private void featchallConversationCall() {
        Conversation conversation=new Conversation();
        conversation.getJSONObjectFetchAllConversations("unarchive","");
    }




    //prajakta26
    private void showData() {
        if (!(ChatList.size() == 0)) {
            if (ll_no_messages != null) {
                ll_no_messages.setVisibility(View.GONE);

            }
        }
        Repository repo = Repository.getRepository();
        if (repo != null && selectedConversation != null) {
            String conversationKeyval = selectedConversation.getKeyval();
            String linkupId = selectedConversation.getLinkupId();
            if (linkupId != null) {
                ArrayList<Messages> msgs;
                if (isStarMessages){
                    msgs = (ArrayList<Messages>)repo.getAllStarMessages(linkupId);

                }else {
                    msgs = (ArrayList<Messages>) repo.getAllMessages(linkupId);

                }
                if (msgs != null) {

                    if (msgs.size() == 0) {
                        ChatList.clear();
                        rec_chat_list.setVisibility(View.GONE);
                        ll_no_messages.setVisibility(View.VISIBLE);
                    } else {
                        rec_chat_list.setVisibility(View.VISIBLE);
                        ll_no_messages.setVisibility(View.GONE);
                    }

                    if (msgs.size() > 0) {
                        fetchedObjects.clear();

                        final List<JSONObject> msgList = new ArrayList<JSONObject>();
                        try {
                            //get all json objects
                            for (int k = 0; k < msgs.size(); k++) {
                                Messages msg = msgs.get(k);
                                if (msg != null) {
                                    String jsonObjString = msg.getCompleteData();
                                    if (jsonObjString != null) {
                                        msgList.add(new JSONObject(jsonObjString));
                                        Log.d("dataoffline", String.valueOf(msgList.get(k)));
                                    }
                                }
                            }
                            //sort json object according to cml_message_index

                            Collections.sort(msgList, new Comparator<JSONObject>() {
                                @Override
                                public int compare(JSONObject jsonObjectA, JSONObject jsonObjectB) {
                                    int compare = 0;
                                    try {
                                        long index1=0,index2=0;
                                        //  long index1 = jsonObjectA.getLong("CML_MESSAGE_INDEX");

                                        if (jsonObjectA.has("CML_MESSAGE_INDEX")) {
                                            index1 = jsonObjectA.getLong("CML_MESSAGE_INDEX");
                                        }
                                        else
                                        {
                                            JSONObject jb=new JSONObject(String.valueOf(jsonObjectA));
                                            JSONObject jb1=jb.getJSONObject("calmailObject");
                                            index1=jb1.getLong("CML_MESSAGE_INDEX");
                                            Log.i("arrivalTimeoofline", arrivalTime);

                                        }


                                        if (jsonObjectB.has("CML_MESSAGE_INDEX")) {
                                            index2 = jsonObjectB.getLong("CML_MESSAGE_INDEX");
                                        }
                                        else
                                        {
                                            JSONObject jb=new JSONObject(String.valueOf(jsonObjectB));
                                            JSONObject jb1=jb.getJSONObject("calmailObject");
                                            index2=jb1.getLong("CML_MESSAGE_INDEX");
                                            Log.i("arrivalTimeoofline", arrivalTime);
                                        }

                                        if (index1 > index2) {
                                            compare = 1;

                                        }
                                        if (index1 < index2) {
                                            compare = -1;
                                        }
                                        if (index1 == index2) {
                                            compare = 0;
                                        }

                                        Log.i("cmlindex", String.valueOf(index1));
                                        Log.i("cmlindex", String.valueOf(index2));

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
                                        message = createdBy = arrivalTime = img_path = msg_type = onlinetime="";
                                        if (singleMsgObj.has("CML_TITLE")) {
                                            message = singleMsgObj.getString("CML_TITLE");
                                        }
                                        else
                                        {
                                            JSONObject jb=new JSONObject(String.valueOf(singleMsgObj));
                                            JSONObject jb1=jb.getJSONObject("calmailObject");
                                            message=jb1.getString("CML_TITLE");
                                            Log.i("arrivalTimeoofline", arrivalTime);

                                        }
                                        if (singleMsgObj.has("CML_TYPE")) {
                                            msg_type = singleMsgObj.getString("CML_TYPE");
                                            Log.i("msg_type", msg_type);
                                        }
                                        else {
                                            msg_type="";
                                        }
                                     /*if(singleMsgObj.has("imgPath")){
                                            img_path=com.nc.developers.cloudscommunicator.GlobalClass.getLoginUrl()+singleMsgObj.getString("imgPath").replace("?op=OPEN","").replaceAll(" ","%20");
                                        }*/
                                        if (singleMsgObj.has("CML_IMAGE_PATH")) {
                                            img_path = GlobalClass.getLoginUrl() + singleMsgObj.getString("CML_IMAGE_PATH").replace("?op=OPEN", "").replaceAll(" ", "%20");
                                        }


                                        if (singleMsgObj.has("CREATED_ON")) {
                                            onlinetime = singleMsgObj.getString("CREATED_ON");
                                        }
                                        else
                                        {
                                            JSONObject jb = new JSONObject(String.valueOf(singleMsgObj));
                                            JSONObject jb1 = jb.getJSONObject("calmailObject");
                                            onlinetime = jb1.getString("CREATED_ON");

                                        }

                                        if (singleMsgObj.has("CREATED_BY")) {
                                            createdBy = singleMsgObj.getString("CREATED_BY");
                                        }
                                        else
                                        {
                                            JSONObject jb=new JSONObject(String.valueOf(singleMsgObj));
                                            JSONObject jb1=jb.getJSONObject("calmailObject");
                                            createdBy=jb1.getString("CREATED_BY");

                                        }
                                        if (singleMsgObj.has("CML_CHT_MESSAGE_TYPE")) {
                                            if(singleMsgObj.has("CML_COMMENT_TYPE")) messageType =singleMsgObj.getString("CML_COMMENT_TYPE").equals("EVENT") ?6:singleMsgObj.getInt("CML_CHT_MESSAGE_TYPE");
                                            else messageType=singleMsgObj.getInt("CML_CHT_MESSAGE_TYPE");
                                        }
                                        else
                                        {
                                            messageType=4;
                                        }
                                        if (singleMsgObj.has("CML_MESSAGE_INDEX")) {
                                            arrivalTime = String.valueOf(singleMsgObj.getLong("CML_MESSAGE_INDEX"));

                                            Log.i("arrivalTime", arrivalTime);
                                        }
                                        else
                                        {
                                            JSONObject jb=new JSONObject(String.valueOf(singleMsgObj));
                                            JSONObject jb1=jb.getJSONObject("calmailObject");
                                            arrivalTime= String.valueOf(jb1.getLong("CML_MESSAGE_INDEX"));
                                            Log.i("arrivalTimeoofline", arrivalTime);

                                        }
                                        if (singleMsgObj.has("CML_MESSAGE_INDEX")) {
                                            arrivalTime = String.valueOf(singleMsgObj.getLong("CML_MESSAGE_INDEX"));
                                            Log.i("arrivalTime", arrivalTime);
                                        }
                                        else
                                        {
                                            JSONObject jb=new JSONObject(String.valueOf(singleMsgObj));
                                            JSONObject jb1=jb.getJSONObject("calmailObject");
                                            arrivalTime= String.valueOf(jb1.getLong("CML_MESSAGE_INDEX"));
                                            Log.i("arrivalTimeoofline", arrivalTime);

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

                                      /*  diff = currentDate.getTime() - arrivalDate.getTime();
                                        diffInDays = java.util.concurrent.TimeUnit.DAYS.convert(diff, java.util.concurrent.TimeUnit.MILLISECONDS);
                                        Log.i("daysDiff:", String.valueOf(diffInDays) + " ..kk");
*/

                                        Log.i(TAG, "image_path" + img_path);

                                        if (diffDays == 0) {
                                            if (createdBy.equals(userId)) {
                                                if (messageType == 4) {

                                                    ChatListModel model = new ChatListModel(4, message,
                                                            onlinetime,
                                                            "no status", img_path, false,
                                                            singleMsgObj,
                                                            sendFalg,
                                                            senderName);
                                                    todayList.add(model);
                                                } else {
                                                    Log.i("arrivalTimeonline", arrivalDateString);

                                                    ChatListModel model = new ChatListModel(messageType==6?5:(messageType == 5 ? 3 : 2), message,
                                                            arrivalDateString,
                                                            "read", img_path, msg_type.contains("image"),
                                                            singleMsgObj,
                                                            sendFalg,
                                                            senderName);
                                                    todayList.add(model);
                                                    Log.d("todayListmsgadd_user", message);
                                                }
                                            }

                                            else {
                                                Log.i("arrivalTimeonline", arrivalDateString);

                                                ChatListModel model = new ChatListModel(messageType==6?6:(messageType == 5 ? 3 : 1), message,
                                                        arrivalDateString,
                                                        "no_status", img_path, msg_type.contains("image"),
                                                        singleMsgObj,
                                                        sendFalg,
                                                        senderName);
                                                todayList.add(model);
                                                Log.d("todayListmsgadd_nouser",message);
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
                                                    ChatListModel model = new ChatListModel(messageType==6?5:(messageType == 5 ? 3 : 2), message,
                                                            arrivalDateString,
                                                            "read", img_path, msg_type.contains("image"),
                                                            singleMsgObj,
                                                            sendFalg,
                                                            senderName);
                                                    yesterdayList.add(model);
                                                    Log.d("yesterdaymsgadd",message);
                                                } else {
                                                    ChatListModel model = new ChatListModel(messageType==6?6:(messageType == 5 ? 3 : 1), message,
                                                            arrivalDateString,
                                                            "no_status", img_path, msg_type.contains("image"),
                                                            singleMsgObj,
                                                            sendFalg,
                                                            senderName);
                                                    yesterdayList.add(model);
                                                    Log.d("notyesterdaymsgadd",message);
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
                                                    Log.d("specificmsgadd",message);
                                                }
                                                if (createdBy.equals(userId)) {
                                                    ChatListModel model = new ChatListModel(messageType==6?5:(messageType == 5 ? 3 : 2), message, arrivalDateString,
                                                            "read", img_path, msg_type.contains("image"),
                                                            singleMsgObj,
                                                            sendFalg,
                                                            senderName);
                                                    specificDayList.add(model);
                                                    Log.d("specificmsgaddnotmsgadd",message);
                                                } else {
                                                    ChatListModel model = new ChatListModel(messageType==6?6:(messageType == 5 ? 3 : 1), message, arrivalDateString,
                                                            "no_status", img_path, msg_type.contains("image"),
                                                            singleMsgObj,
                                                            sendFalg,
                                                            senderName);
                                                    specificDayList.add(model);
                                                    Log.d("specificmsgaddno",message);
                                                }
                                            }
                                        }
                                    }
                                }

                                ChatList.clear();
                                boolean isYesterdayStringAdded, isTodayStringAdded;
                                isYesterdayStringAdded = isTodayStringAdded = false;
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
                                        Log.d("yesterdayaddedmsgadd",message);
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
                                        Log.d("todaystringaddedmsgadd",message);
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
                                        ll_no_messages.setVisibility(View.GONE);
                                        rec_chat_list.getRecycledViewPool().clear();
                                        chatAdapter = new ChatListAdapter(ChatList, getActivity(),
                                                ChatListFragment.this,
                                                selectedConversation.getCompleteData());
                                        rec_chat_list.setAdapter(chatAdapter);
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        chatAdapter.reload(ChatList);

                                        rec_chat_list.getRecycledViewPool().clear();
                                        chatAdapter.notifyDataSetChanged();
                                    }

                                    if (rply) {
                                        rec_chat_list.scrollToPosition(reply_pos);

                                    } else {

                                        if (showingimage) {
                                            rec_chat_list.scrollToPosition(image_pos);


                                        } else {
                                            rec_chat_list.scrollToPosition(chatAdapter.getItemCount() - 1);
                                        }


                                    }
                                    GallaryimgList.clear();
                                    GallarypdfList.clear();

                                    for (int i = 0; i < ChatList.size(); i++) {
                                        System.out.println("url" + ChatList.get(i).getImg_url());
                                        try {
                                            if (ChatList.get(i).getImg_url().contains("https")) {

                                                if (ChatList.get(i).getImg_url().contains(".zip")) {

                                                } else if (ChatList.get(i).getImg_url().contains(".pdf")) {


                                                    if (ChatList.get(i).getViewType() == 1) {
                                                        try {
                                                            Repository repo1 = Repository.getRepository();
                                                            Messages singleMsg = repo1.getMessage(String.valueOf(ChatList.get(i).getJsonMessage().get("KEY_VAL")));
                                                            String localpath = singleMsg.getLocalImagePath();
                                                            if (localpath != null) {
                                                                GallarypdfList.add(localpath);

                                                            }
                                                        } catch (Exception e) {
                                                        }

                                                    } else {

                                                        try {
                                                            Repository repo1 = Repository.getRepository();
                                                            Messages singleMsg = repo1.getMessage(String.valueOf(ChatList.get(i).getJsonMessage().get("KEY_VAL")));
                                                            String localpath = singleMsg.getLocalImagePath();
                                                            if (localpath != null) {
                                                                GallarypdfList.add(localpath);

                                                            }
                                                        } catch (Exception e) {
                                                        }

                                                    }


                                                } else {

                                                    if (ChatList.get(i).getViewType() == 1) {
                                                        try {
                                                            Repository repo1 = Repository.getRepository();
                                                            Messages singleMsg = repo1.getMessage(String.valueOf(ChatList.get(i).getJsonMessage().get("KEY_VAL")));
                                                            String localpath = singleMsg.getLocalImagePath();
                                                            if (localpath != null) {
                                                                GallaryimgList.add(ChatList.get(i).getImg_url());

                                                            }
                                                        } catch (Exception e) {
                                                        }

                                                    } else {
                                                        GallaryimgList.add(ChatList.get(i).getImg_url());

                                                    }


                                                }

                                            } else {

                                            }
                                        } catch (Exception e) {
                                        }

                                    }
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
                    ll_no_messages.setVisibility(View.VISIBLE);
                }
            }
        }
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (msgThread != null) {
            if (msgThread.isAlive()) msgThread.interrupt();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_left_icon:

                if (fromfrgament.equals("GroupMemberContact")) {
                    getActivity().onBackPressed();
                } else if (fromfrgament.equals("contact")) {
                    common.hideKeyboard(getActivity());
                    Bundle bundle = new Bundle();
                    bundle.putString("call_from", "search");
                    Fragment fragment = new FragmentHomeScreen();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_frag_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else {
                    fromfrgament = "conv";
                    GroupMemberListFrag.isbackpressOntoolbar = false;
                    common.hideKeyboard(getActivity());
                    ReplaceFragment.replaceFragment(ChatListFragment.this, R.id.frame_frag_container, FragmentHomeScreen.newInstance(), false);
                }
                break;
            case R.id.img_right_icon2:
                deleteMsg();
                break;
            case R.id.img_right_icon3:
                MyOptionMenu();
                break;

            //prajakta2
            case R.id.ll_search:
                lin_search_bar.setVisibility(View.VISIBLE);
                lin_action_bar.setVisibility(View.GONE);
                //prajakta8
                li_msg_layout.setVisibility(View.GONE);
                break;
        }
    }
    //prajakta26
    private void MyOptionMenu() {
        String isArc = flag_Archive ? "Unarchive" : "Archive";
        popup = new PopupMenu(getActivity(), img_right_icon3);
        popup.getMenuInflater().inflate(R.menu.main, popup.getMenu());
        popup.getMenu().findItem(R.id.item1).setTitle(Html.fromHtml("<font color='#000000'>Clear Chat</font>"));
        popup.getMenu().findItem(R.id.item2).setTitle(!ismakeImportant ? Html.fromHtml("<font color='#000000'>Important</font>") : Html.fromHtml("<font color='#000000'>Unimportant</font>"));
        popup.getMenu().findItem(R.id.item3).setTitle(Html.fromHtml("<font color='#000000'>" + isArc + "</font>"));
        popup.getMenu().findItem(R.id.item4).setTitle(!isStarMessages ?Html.fromHtml("<font color='#000000'>Star Messages</font>"):Html.fromHtml("<font color='#000000'>All Messages</font>"));
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                        CustomDialog.showActionDialog(context, "Confirm", "Are you sure you want to clear messages in this chat?", "clear_chat", true);
                        break;
                    case R.id.item2:
                        impUnimpFun(ismakeImportant);
                        break;
                    case R.id.item3:
                        String isArc = flag_Archive ? "Are you sure you want to unarchive conversation?" : "Are you sure you want to archive conversation?";
                        CustomDialog.showActionDialog(context, "Alert", isArc,
                                "archive_msg", true);
                        break;
                    case R.id.item4:
                        if (isStarMessages){
                            isStarMessages = false;
                        }
                        else {
                            isStarMessages = true;
                        }
                        showData();
                        try {
                            Repository repo = Repository.getRepository();
                            if (repo != null && selectedConversation != null) {
                                String linkupId = selectedConversation.getLinkupId();
                                if (linkupId != null) {
                                    ArrayList<Messages> msgs = (ArrayList<Messages>) repo.getAllStarMessages(linkupId);
                                    if (msgs != null&&msgs.size()>0) {
                                        for(int i=0;msgs.size()>0;i++){
                                            Log.d("ListOfStarMsgs",msgs.get(i).getCmlTitle());
                                        }
                                    }
                                }
                            }

                        }catch (Exception ex){
                            ex.getMessage();
                        }
                        // Toast.makeText(context, "******", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    //created by prajakta
    public static void clearChatFunction() {
        try {
            ConversationListAdapter.selected_conversations.getJsonObjectClearChat();
            /*boolean internetAvailability = InternetConnectionChecker.checkInternetConnection(mcontext);
            Log.i("is internet available:", String.valueOf(internetAvailability) + " ..kk");
            if (GlobalClass.getAuthenticatedSyncSocket() != null && internetAvailability) {
                Log.d("zzzzzzzzzzz", ConversationListAdapter.selected_conversations.getCmlTitle());
                JSONObject jsonobj_clearchat = JsonObjectConversation.getJsonObjectClearChat(ConversationListAdapter.selected_conversations);
                Log.d("emit_clearchat", String.valueOf(jsonobj_clearchat) + " ..");
                GlobalClass.getAuthenticatedSyncSocket().emit("serverOperation", jsonobj_clearchat);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //created by prajakta
    public static void archivemsgFunction() {
        String isArc = flag_Archive ? "unarchive" : "archive";
        ConversationListAdapter.selected_conversations.getJsonObjectArchiveUnarchiveConversation( isArc);
       /* try {
            boolean internetAvailability = InternetConnectionChecker.checkInternetConnection(mcontext);
            Log.i("is internet available:", String.valueOf(internetAvailability) + " ..kk");
            if (GlobalClass.getAuthenticatedSyncSocket() != null && internetAvailability) {
                JSONObject jsonobj_archive = JsonObjectConversationArchive.getJsonObjectArchiveUnarchiveConversation(ConversationListAdapter.selected_conversations, isArc);
                Log.d("emit_archivechat", String.valueOf(jsonobj_archive) + " ..");
                GlobalClass.getAuthenticatedSyncSocket().emit("serverOperation", jsonobj_archive);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

    private void impUnimpFun(boolean makeimp) {
        selectedConversation.getJsonObjectUpdateConversation(!makeimp ? "important" : "unimportant", "");
       /* try {
            boolean internetAvailability = InternetConnectionChecker.checkInternetConnection(getActivity());
            Log.i("is internet available:", String.valueOf(internetAvailability) + " ..kk");
            if (GlobalClass.getAuthenticatedSyncSocket() != null && internetAvailability) {
                JSONObject jsonobj_imp = JsonObjectConversationKebabMenu.getJsonObjectUpdateConversation(selectedConversation, !makeimp ? "important" : "unimportant", "");
                Log.d("emit_important", String.valueOf(jsonobj_imp) + " ..");
                GlobalClass.getAuthenticatedSyncSocket().emit("serverOperation", jsonobj_imp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void fetcInvitee() {
        try {
            selectedConversation.getJSONObjectFetchInvitees();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetcMessages() {
        try {
            Log.i(TAG+"_title:",selectedConversation.getCmlTitle()+" ..kk");
            selectedConversation.getJSONObjectFetchMessages("");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                            if (conversationArrayList.size() > 0) {
                                selectedConversation.getJSONObjectDeleteMessagesForMe(conversationArrayList);
                            }

                            ChatListFragment.chatActinBarVisibility(0);

                            dialog.dismiss();
                            break;

                        case 2:

                            /*if(GlobalClass.getAuthenticatedSyncSocket()!=null && internetAvailability && ChatListAdapter.conversationArrayList.size()>0){
                                JSONObject dltMsgForMeObj=JsonObjectMessage.deleteMessageForEveryone(grpownerId,ChatListAdapter.conversationArrayList);
                                Log.i("emit_send_msg:",String.valueOf(dltMsgForMeObj)+" ..kk");
                                GlobalClass.getAuthenticatedSyncSocket().emit("serverOperation",dltMsgForMeObj);
                            }*/
                            ChatListFragment.chatActinBarVisibility(0);
                            dialog.dismiss();
                            chatAdapter.resetFlag();

                        case 1:

                            chatAdapter.resetFlag();
                            chatAdapter.notifyDataSetChanged();
                            Log.i("click", "click");
                            dialog.dismiss();
                            break;
                        default:



                    }
                }
            });

            builder.show();
        } catch (IndexOutOfBoundsException ex) {
            Log.e("IndexOutOfBoundEx", "" + ex.getMessage());
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Optional
    @OnClick({R.id.ll_img_send, R.id.ll_img_attachment, R.id.img_gallery, R.id.img_cam, R.id.edit_msg, R.id.img_imoji, R.id.img_audio, R.id.img_loc, R.id.img_doc,R.id.img_hold_talk})
    public void onSelectDeselect(View view) {
        //System.out.println("iiiiiiiiiiiiiiii" + view.getId());
        switch (view.getId()) {
            case R.id.ll_img_send:
                // sendMessage();
                if (replyConv) {
                    sendreplyMsg();
                    rply=true;
                    ChatListFragment.li_reply.setVisibility(View.GONE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ChatListFragment.li_msg_layout.setBackgroundResource(R.drawable.send_msg_bg);
                        ChatListFragment.li_send_msg_back.setBackground(null);
                    }

                    replyConv = false;
                } else {
                    sendMsg();
                    rply=false;
                    showingimage=false;
                }
                ll_img_send.setEnabled(false);
                ll_img_send.setAlpha(0.4f);
                if (!one_to_one_flag) {
                    try {
                        if (chatAdapter != null) {
                            chatAdapter.resetFlag();
                        }
                    } catch (Exception e) {
                    }

                }
                edit_msg.setText("");

                break;
            case R.id.ll_img_attachment:
                setAttachView();
                break;

            case R.id.img_doc:
                setDoctFile();
                break;


            case R.id.img_hold_talk:
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                AddEventDialog dFragment = new AddEventDialog();
                dFragment.show(fragmentManager,"Light");
                break;

            case R.id.img_gallery:
                setGalleryImg();
                break;
            case R.id.img_cam:
                setCamImg();
                break;
            case R.id.img_audio:
                setAudio();
                break;
            case R.id.edit_msg:
                ll_attachment.setVisibility(View.GONE);
                break;
            case R.id.img_imoji:
                if (isAttachView)
                    setAttachView();
                ll_attachment.setVisibility(View.GONE);
                break;

            case R.id.img_loc:
                showCaptionLocation();
                break;
        }
    }


    private void setAttachView() {
        if (isAttachView) {
            isAttachView = false;
            isAttachBack = true;
            ll_attachment.setVisibility(View.GONE);
        } else {
            isAttachView = true;
            isAttachBack = false;
            common.hideKeyboard(getActivity());
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    ll_attachment.setVisibility(View.VISIBLE);
                }
            }, 100);
        }
    }


    private void StarMsg() {
        try {
            Messages msg=new Messages();
            msg.getJSONObjectStarUnstarMessage(conversationArrayList, "star");

        } catch (Exception e) {
        }

    }

    private void UnStarMsg() {
        try {
            Messages msg=new Messages();
            msg.getJSONObjectStarUnstarMessage(conversationArrayList, "unstar");

        } catch (Exception e) {
        }
    }


    private void sendMsg() {
        boolean internetAvailability = InternetConnectionChecker.checkInternetConnection(getActivity());
        rec_chat_list.setVisibility(View.VISIBLE);
        ll_no_messages.setVisibility(View.GONE);
        tempList.clear();
        String messageString = "";
        messageString = edit_msg.getText().toString();


        Log.d("editMsg",messageString);
        if (messageString != null) {
            String title = selectedConversation.getCmlTitle();
            Log.i(TAG + "_title:", title + " ..kk");
            selectedConversation.getJSONObjectSendMessages(edit_msg.getText().toString(),
                    "", null);

            ll_img_send.setEnabled(true);
            ll_img_send.setAlpha(1f);

            if (!internetAvailability) {
                showData();
            }

        }
    }

    private void sendForwardMsg() {
        int counter = 0;
        for (int i = 0 ; i < conversationArrayList.size(); i++) {
            for (int j = 1+i;  j < conversationArrayList.size() ; j++)
                //don't start on the same word or you'll eliminate it.
                if (conversationArrayList.get(j).getKeyval().equals( conversationArrayList.get(i).getKeyval() )  ) {
                    conversationArrayList.remove(conversationArrayList.get(j));
                    counter++;

                }
        }
        try {
            String title = selectedConversation.getCompleteData();
            Log.i(TAG + "_title:", title + " ..kk");
            selectedConversation.getJSONObjectForwardMessage(conversationArrayList, forwardConvertionList);
            if (chatAdapter != null) {
                chatAdapter.resetFlag();
                chatAdapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
        }

    }

    private void sendreplyMsg() {
        String messageString = "";
        messageString = edit_msg.getText().toString().trim();
        Messages messages = JsonParserClass.parseMessagesJSONObject(rplyJsonObj);

        if (messageString != null) {
            selectedConversation.getJSONObjectReplyMessageOneToOne(messages,messageString);
            ll_img_send.setEnabled(true);
            ll_img_send.setAlpha(1f);
        }
        try {
            if (chatAdapter != null) {
                chatAdapter.resetFlag();
                chatAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
        }

    }


/*

    private void sendImageAttachment() {

        boolean internetAvailability = InternetConnectionChecker.checkInternetConnection(getActivity());
        Log.i("is internet available:", String.valueOf(internetAvailability) + " ..kk");
        Log.i("group_owner_Id:", grpownerId);
        if (GlobalClass.getAuthenticatedSyncSocket() != null && internetAvailability) {
            WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            */
    /*one_to_one_flag?JsonObjectMessage.getJSONObjectSendOneToOneAttachment(selectedConversation,response_attachemnt_name,response_attachemnt_url):getJSONObjectSendOneToOneMessage(selectedConversation,edit_msg.getText().toString());
     *//*

            JSONObject sendMsgObj = JsonObjectMessage.getJSONObjectSendMessageAttchmnt(selectedConversation, "image", response_attachemnt_name, response_attachemnt_url);// for plain send message
            Log.i("send_attach_emit:", String.valueOf(sendMsgObj) + " ..kk");
            GlobalClass.getAuthenticatedSyncSocket().emit("serverOperation", sendMsgObj);
        }
    }
*/

   /* private void sendpdfAttachment() {

        boolean internetAvailability = InternetConnectionChecker.checkInternetConnection(getActivity());
        Log.i("is internet available:", String.valueOf(internetAvailability) + " ..kk");
        Log.i("group_owner_Id:", grpownerId);
        if (GlobalClass.getAuthenticatedSyncSocket() != null && internetAvailability) {
            WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            JSONObject sendMsgObj = JsonObjectMessage.getJSONObjectSendMessageAttchmnt(selectedConversation, "pdf", response_attachemnt_name, response_attachemnt_url);// for plain send message
            Log.i("send_attach_emit:", String.valueOf(sendMsgObj) + " ..kk");
            GlobalClass.getAuthenticatedSyncSocket().emit("serverOperation", sendMsgObj);
        }
    }*/

   /* private void sendZipAttachment() {

        boolean internetAvailability = InternetConnectionChecker.checkInternetConnection(getActivity());
        Log.i("is internet available:", String.valueOf(internetAvailability) + " ..kk");
        Log.i("group_owner_Id:", grpownerId);
        if (GlobalClass.getAuthenticatedSyncSocket() != null && internetAvailability) {
            WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            *//*one_to_one_flag?JsonObjectMessage.getJSONObjectSendOneToOneAttachment(selectedConversation,response_attachemnt_name,response_attachemnt_url):getJSONObjectSendOneToOneMessage(selectedConversation,edit_msg.getText().toString());
     *//*
            JSONObject sendMsgObj = JsonObjectMessage.getJSONObjectSendMessageAttchmnt(selectedConversation, "zip", response_attachemnt_name, response_attachemnt_url);// for plain send message
            Log.i("send_attach_emit:", String.valueOf(sendMsgObj) + " ..kk");
            GlobalClass.getAuthenticatedSyncSocket().emit("serverOperation", sendMsgObj);
        }
    }*/

    private void sendMessage() {
        if (edit_msg.getText().length() > 0) {
            ll_no_messages.setVisibility(View.GONE);

            String recentCmlTempKeyVal = "";
            JSONObject addMessage = new JSONObject();

            try {

                String to_email = "";
                JSONArray inviteeArray = new JSONArray();
                JSONArray inviteeToArray = new JSONArray();
                JSONArray jsonArray = null;
                if (GlobalClass.getInviteeJson() != null) {
                    jsonArray = GlobalClass.getInviteeJson().getJSONArray("dataArray");
                }

                JSONObject dataObj = new JSONObject();
                calmailObj = new JSONObject();
                JSONArray dataArray = new JSONArray();
                JSONObject inviteFromObj = new JSONObject();


                inviteFromObj.put("CML_PARENT_TEMP_KEY_VAL", "admin@mstorm.com#" + tp + "#ORG1_1#MSG#" + user_id + "" + tp + "_" + random);
                inviteFromObj.put("CML_PRIORITY", 1);
                inviteFromObj.put("CML_SUB_CATEGORY", "");
                inviteFromObj.put("KEY_TYPE", "IDE");
                inviteFromObj.put("PARENT_KEY_TYPE", "TSK");
                inviteFromObj.put("PARENT_SUB_KEY_TYPE", "MSG");
                inviteFromObj.put("SUB_KEY_TYPE", "MSG_IDE");
                inviteFromObj.put("IDE_ATTENDEES_EMAIL", "" + user_id);
                inviteFromObj.put("IDE_TYPE", "FROM");
                inviteFromObj.put("CML_TEMP_KEY_VAL", "admin@mstorm.com#PRJ:CGR_" + tp + "_" + random + "_TSK:MSG_" + tp + "_" + random + "_IDE:FROM_" + "" + user_id);
                inviteeArray.put(inviteFromObj);


                calmailObj.put("ACTIVE_STATUS", 1);
                calmailObj.put("CML_COMPLETION_PERCENTAGE", 0);
                calmailObj.put("CML_HOURS", 0);
                calmailObj.put("CML_PRIORITY", 1);
                calmailObj.put("CML_REF_ID", "" + group_json.getString("PROJECT_ID"));
                calmailObj.put("DEPT_ID", "" + group_json.getString("DEPT_ID"));
                calmailObj.put("CML_SUB_CATEGORY", "");
                calmailObj.put("CREATED_ON", "");
                calmailObj.put("SYNC_PENDING_STATUS", 0);
                calmailObj.put("ORG_ID", "admin@mstorm.com#1231231231231#ORG:1_1");
                calmailObj.put("CML_TAG", new JSONArray());
                calmailObj.put("CML_TASK_LIST_ID", "" + group_json.getString("KEY_VAL"));
                calmailObj.put("CML_DESCRIPTION", "");
                calmailObj.put("CML_TITLE", "" + edit_msg.getText().toString());
                calmailObj.put("CML_PARENT_SUB_KEY_TYPE", "CGR");
                random = new Random().nextInt(max - min + 1) + min;
                recentCmlTempKeyVal = "admin@mstorm.com#" + tp + "#ORG:1_1#MSG#" + user_id + "" + tp + "_" + random;
                // calmailObj.put("CML_TEMP_KEY_VAL", "admin@mstorm.com#" + tp + "#ORG:1_1#MSG#" + user_id + "" + tp + "_" + random);
                calmailObj.put("CML_TEMP_KEY_VAL", recentCmlTempKeyVal);
                calmailObj.put("CML_PARENT_ID", "" + group_json.getString("PROJECT_ID"));
                calmailObj.put("KEY_TYPE", "TSK");
                calmailObj.put("SUB_KEY_TYPE", "MSG");
                calmailObj.put("DESIGNATION", "" + designation);
                calmailObj.put("SYNC_PENDING_STATUS", 0);


                calmailObj.put("CREATED_BY", user_id);
                calmailObj.put("CML_TYPE", "1");
                calmailObj.put("CML_MESSAGE_INDEX", System.currentTimeMillis());
                calmailObj.put("send_flag", false);
                calmailObj.put("LAST_MODIFIED_ON", "");

                dataObj.put("inviteeList", inviteeArray);
                dataObj.put("actionArray", new JSONArray().put("ADD_CHAT_MESSAGE"));
                dataObj.put("calmailObject", calmailObj);

                dataArray.put(dataObj);

                addMessage.put("dataArray", dataArray);
                addMessage.put("socketId", GlobalClass.getAuthenticatedSyncSocket().id());
                addMessage.put("requestId", "/sync#" + GlobalClass.getAuthenticatedSyncSocket().id() + "" + user_id + "#" + tp);
                addMessage.put("moduleName", "CGR");
                addMessage.put("action", "INSERT");
                addMessage.put("userId", user_id);
                addMessage.put("FROM", "senddatatoserver");

            } catch (JSONException e) {

                Log.e(TAG, "------" + e.getMessage());
            }
            if (addMessage.keys().hasNext()) {

                try {
                    ChatList.add(ChatList.size(), new ChatListModel(2, edit_msg.getText().toString(), time, "no_status", null, false, new JSONObject(), false, ""));
                    rec_chat_list.getRecycledViewPool().clear();
                    chatAdapter.notifyDataSetChanged();
                    edit_msg.setText("");
                    rec_chat_list.scrollToPosition(chatAdapter.getItemCount() - 1);
                    //SocketListenerClass.prepareInsert(TBL_MESSAGES, calmailObj);
                } catch (Exception ex) {
                    Log.e("Error", "" + ex.getMessage());
                }
            }

            if (GlobalClass.getAuthenticatedSyncSocket() != null) {
                if (InternetConnectionChecker.checkInternetConnection(context)) {
                    GlobalClass.getAuthenticatedSyncSocket().emit("serverOperation", addMessage);
                    Log.d("AddChatObj", String.valueOf(addMessage));
                } else {
                    Log.i("AddChatObj", String.valueOf(addMessage));
                }
            } else {
                CustomDialog.dispDialogAlert(getActivity(), ChatListFragment.class, getString(R.string.unableToConnect), false);
            }


        } else {

            if (edit_msg.getText().length() == 0) {
                ll_img_send.setEnabled(false);
                ll_img_send.setAlpha(0.4f);

            }

        }
    }

    private void setAudio() {
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload, 5);

    }

    private void setCamImg() {

        ll_no_messages.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 && (
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //Intent takePictureIntent = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

                String fileName = "/" + System.currentTimeMillis() + "_chat.png";

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, fileName);
                photoURI = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 3);
            }

        }
        ll_attachment.setVisibility(View.GONE);
    }

    @SuppressLint("InlinedApi")
    private void setGalleryImg() {

        ll_no_messages.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 && (
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
        }
        else
        {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, 7);
            //onPickPhoto();
        }
        ll_attachment.setVisibility(View.GONE);
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private void setDoctFile() {

        ll_no_messages.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 && (
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
        } else {
            /*Intent intent = new Intent();
            intent.setType("application/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
   */
            onPickDoc();

        }

        ll_attachment.setVisibility(View.GONE);
    }


    public void onPickDoc() {
        String[] zips = {".zip", ".rar"};
        String[] pdfs = {".pdf"};
        int maxCount = 5;
        docPaths.clear();
        FilePickerBuilder.getInstance()
                .setMaxCount(maxCount)
                .setSelectedFiles(docPaths)
                .setActivityTheme(R.style.FilePickerTheme)
                .setActivityTitle("Please select doc")
                .addFileSupport("ZIP", zips)
                .addFileSupport("PDF", pdfs)
                .enableDocSupport(false)
                .enableSelectAll(true)
                .sortDocumentsBy(SortingTypes.name)
                .withOrientation(Orientation.UNSPECIFIED)
                .pickFile(ChatListFragment.this, 666);

    }

    public void removeItem(int position) {
        ChatList.remove(position);

        chatAdapter.notifyItemRemoved(position);
    }

    // Overriden onActivityResult function for set profile photo
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //the selected audio.
        System.out.println("****************" + requestCode + "************" + resultCode);
        if (requestCode == 666) {
            try {
                docfile.clear();
                if (resultCode == Activity.RESULT_OK && data != null) {


                    docPaths = new ArrayList<>();
                    docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    docfile = docPaths;
                    System.out.println("******************" + docPaths.get(0));
                    System.out.println("********docfile**********" + docfile.size());

                }
            } catch (Exception e) {

            }

            uploadresponse.clear();
            if(docfile.size()>0) {
                uploadresponse.addAll(docfile);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                dFragment = new UploadFileDialog();
                dFragment.setCancelable(false);

                dFragment.show(fragmentManager, "upload");
            }


        }
        if (requestCode == 230) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                photoPaths = new ArrayList<>();
                photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
            }
        }
        if (requestCode == 5) {
            if (resultCode == Activity.RESULT_OK) {

                try {
                    Uri uri = data.getData();
                    attach_audio_path = GalleryRealPath.getRealPathFromURI(context, uri);
                    Log.i("audio_path", "" + attach_audio_path);
                    audioUpload(attach_audio_path);
                    showCaptionAudio();
                } catch (Exception e) {
                }

            }

        }


        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                ImgProfileUri = data.getData();
                Uri selectedImage = data.getData();
                mediaPath = getPath(getActivity(), selectedImage);
                //pdfFileUpload(mediaPath);
            } catch (Exception e) {
            }

        }

        if (resultCode == RESULT_OK && requestCode == 3) {

            try {
                String[] projection = {MediaStore.Images.Media.DATA};
                @SuppressWarnings("deprecation")
                Cursor cursor = getActivity().managedQuery(photoURI, projection,
                        null, null, null);
                int column_index_data = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                attach_image_path = cursor.getString(column_index_data);
                Log.d("image_path", "----" + attach_image_path);
                File file = new File(attach_image_path);
                //Uri uri = Uri.fromFile(file);
                ImgProfileUri = Uri.fromFile(file);
                Log.d("ImgUri", String.valueOf(ImgProfileUri));
                //imgChatFragAdd.performClick();

                imageUpload("image");
                showCaptionDialog();
            } catch (Exception e) {
            }

        }

        else if (resultCode == RESULT_OK && requestCode == 7 ) {

            try {
                Uri imageUri=null;
                Gallarypath.clear();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if(data.getClipData() != null) {

                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            mediaPath  = cursor.getString(columnIndex);
                            Log.i("mediaPath", String.valueOf(mediaPath));

                            Gallarypath.add(mediaPath);
                            cursor.close();

                        }
                        Log.i("Gallarypath", String.valueOf(Gallarypath.size()));

                    }
                    else if(data.getData() != null) {

                        ImgProfileUri = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver().query(ImgProfileUri, filePathColumn, null, null, null);
                        assert cursor != null;
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        mediaPath  = cursor.getString(columnIndex);
                        Log.i("mediaPath", String.valueOf(mediaPath));

                        Gallarypath.add(mediaPath);
                        cursor.close();

                    }
                }

                uploadresponse.clear();
                if(Gallarypath.size()>0) {
                    uploadresponse.addAll(Gallarypath);
                    if(group_json != null) {
                        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        dFragment = new UploadFileDialog();
                        dFragment.setCancelable(false);
                        dFragment.show(fragmentManager, "upload");
                    }
                    //  pdfFileUpload1(Gallarypath);
                }


            } catch (Exception e) {

                Log.d("singleexception",e.getMessage());
            }

        }
    }

    @OnClick(R.id.btn_addcontact)
    public void btn_addcontact() {
        selectedConversation.getJSONObjectAddMessagingContact();
        /*boolean internetAvailability = InternetConnectionChecker.checkInternetConnection(getActivity());
        Log.i("is internet available:", String.valueOf(internetAvailability) + " ..kk");
        if (GlobalClass.getAuthenticatedSyncSocket() != null && internetAvailability) {
            JSONObject addMsgObj = JsonObjectContact.getJSONObjectAddMessagingContact(selectedConversation);
            Log.d("AddContact", String.valueOf(addMsgObj));
            GlobalClass.getAuthenticatedSyncSocket().emit("serverOperation", addMsgObj);

        }*/
    }


    @OnClick(R.id.btn_blockcontact)
    public void btn_blockcontact() {
        selectedConversation.getJSONObjectAddMessagingContact();

        //btn_blockcontact.setTextColor(Color.WHITE);
       /* boolean internetAvailability = InternetConnectionChecker.checkInternetConnection(getActivity());
        Log.i("is internet available:", String.valueOf(internetAvailability) + " ..kk");
        if (GlobalClass.getAuthenticatedSyncSocket() != null && internetAvailability) {
            Log.d("ssssssssss",selectedConversation.getCmlTitle());
            JSONObject blockMsgObj = JsonObjectContact.getJSONObjectAddBlockContact(selectedConversation);
            Log.d("BlockContact", String.valueOf(blockMsgObj));
            GlobalClass.getAuthenticatedSyncSocket().emit("serverOperation", blockMsgObj);

        }
*/

        //  btn_blockcontact.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.reject_req), PorterDuff.Mode.MULTIPLY);
    }

    @OnClick(R.id.btn_unblockcontact)
    public void btn_unblockContact() {
        Contact contact=new Contact();
        contact.getJSONObjectFetchBlockedContacts();
        /*boolean internetAvailability = InternetConnectionChecker.checkInternetConnection(getActivity());
        Log.i("is internet available:", String.valueOf(internetAvailability) + " ..kk");
        if (GlobalClass.getAuthenticatedSyncSocket() != null && internetAvailability) {

            JSONObject blockcontactObj = JsonObjectContact.getJSONObjectFetchBlockedContacts();
            Log.d("BlockContacttounblock", String.valueOf(blockcontactObj));
            GlobalClass.getAuthenticatedSyncSocket().emit("OnDemandCall", blockcontactObj);

        }*/

    }


    //prajakta2
    @OnClick(R.id.ll_clear_txt)
    public void ll_clear_txt () {
        edit_search.setText("");

    }
    //prajakta2
    @OnClick(R.id.ll_back)
    public void ll_back () {
        lin_search_bar.setVisibility(View.GONE);
        lin_action_bar.setVisibility(View.VISIBLE);
        rec_chat_list.scrollToPosition(chatAdapter.getItemCount() - 1);
        //prajakta8
        li_msg_layout.setVisibility(View.VISIBLE);
        edit_search.setText("");

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        Uri photoURI = null;
                        //File photoFile = createImageFileWith();
                        File photoFile = new File(Environment.getExternalStorageDirectory(), "temp.png");
                        //path = photoFile.getAbsolutePath();
                        photoURI = FileProvider.getUriForFile(getActivity(), getString(R.string.file_provider_authority), photoFile);

                        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, 3);
                    }
                } else {
                    //Toast.makeText(getActivity(), "Camera Permission NOT Granted!", Toast.LENGTH_SHORT).show();
                }
                return;

            case GALLERY_PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Intent pickPhoto = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //startActivityForResult(pickPhoto, 7);
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 7);
                } else {
                    //Toast.makeText(getActivity(), "Gallery Permission NOT Granted!", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }


    public void showCaptionLocation() {
        d = new Dialog(getActivity());
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.custom_dialog_location);
        text_link = (TextView) d.findViewById(R.id.text_link);
        btn_loc_cancel = (Button) d.findViewById(R.id.btn_cancelloc);
        btn_loc_send = (Button) d.findViewById(R.id.btn_send_loc);
        text_link.setText(getLocation());
        text_link.setTextColor(Color.WHITE);
        btn_loc_send.setClickable(true);
        btn_loc_send.setEnabled(true);

        btn_loc_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_msg.setText(text_link.getText().toString());
                sendMessage();
                d.dismiss();
            }
        });
        btn_loc_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();

    }

    public void showCaptionAudio() {
        //final Dialog d = new Dialog(getActivity(),R.style.dialog_theme);
        d = new Dialog(getActivity());
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.custom_dialog_audio);
        d.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        audioCaptionDialog = (ImageView) d.findViewById(R.id.img_audio);
        ll_chat_progress_audio = (LinearLayout) d.findViewById(R.id.ll_chat_progress_audio);
        btn_cancel = (Button) d.findViewById(R.id.btn_cancelaudio);
        btn_audio_send = (Button) d.findViewById(R.id.btn_send_audio);
        btn_audio_send.setAlpha(0.5f);
        btn_audio_send.setClickable(false);
        btn_audio_send.setEnabled(false);
        // audioCaptionDialog.setVisibility(View.VISIBLE);
        //  ll_chat_progress_audio.setVisibility(View.GONE);

        // audioCaptionDialog.setImageURI();
        btn_audio_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!response_attachemnt_url.equals("")) {
                    try {
                        sendAttchment(response_attachemnt_url);
                        //    ChatList.add(new ChatListModel(isUser ? 2 : 1, edit_msg.getText().toString(), sdf.format(cc.getInstance().getTime()), isUser ? "unread" : "read", response_attachemnt_url, true, null, sendFalg, ""));
                        isUser = isUser ? false : true;
                        // chatAdapter.outAudio();
                        Log.e("audiocccName", " " + response_attachemnt_url);
                        edit_msg.setText("");
                        rec_chat_list.getRecycledViewPool().clear();
                        chatAdapter.notifyDataSetChanged();
                    } catch (Exception ex) {
                        Log.e("Exception", "" + ex.getMessage());
                    }
                }
                d.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    public void showCaptionDialog() {
        d = new Dialog(getActivity());
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.custom_dialog_for_img_captiont);
        d.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        final ImageView imgCaptionDialog = (ImageView) d.findViewById(R.id.imgCaptionDialog);
        rel_upload_progress = (RelativeLayout) d.findViewById(R.id.rel_upload_progress);
        final Button btn_cancel = (Button) d.findViewById(R.id.btn_cancel);
        btn_send = (Button) d.findViewById(R.id.btn_send);
        btn_send.setAlpha(0.5f);
        btn_send.setClickable(false);
        btn_send.setEnabled(false);
        imgCaptionDialog.setImageURI(ImgProfileUri);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!response_attachemnt_url.equals("")) {
                    try {
                        boolean internetAvailability = InternetConnectionChecker.checkInternetConnection(getActivity());
                        Log.i("is internet available:", String.valueOf(internetAvailability) + " ..kk");
                        Log.i("group_owner_Id:", grpownerId);
                        fileUrl.clear();
                        fileUrl.add(attach_image_path);
                        selectedConversation.getJSONObjectSendMessageAttchmnt(fileUrl);// for plain send message
                        ChatList.add(new ChatListModel(isUser ? 2 : 1, edit_msg.getText().toString(), sdf.format(cc.getInstance().getTime()), isUser ? "unread" : "read", response_attachemnt_url, true, null, sendFalg, ""));
                        rec_chat_list.getRecycledViewPool().clear();
                        chatAdapter.notifyDataSetChanged();
                        isUser = isUser ? false : true;
                        edit_msg.setText("");

                    } catch (Exception ex) {
                        Log.e("Exception", "" + ex.getMessage());
                    }
                }
                d.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    private void audioUpload(String attach_audio_path) {

        SimpleMultiPartRequest request = new SimpleMultiPartRequest(GlobalClass.getLoginUrl() + "/upload", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG + "_RespponseAudio", response);

                try {
                    JSONObject json = new JSONObject(response).getJSONArray("uploadFiles").getJSONObject(0);
                    response_attachemnt_url = json.getString("path").replaceAll(" ", "%20");
                    ll_chat_progress_audio.setVisibility(View.GONE);
                    btn_audio_send.setAlpha(1f);
                    btn_audio_send.setClickable(true);
                    btn_audio_send.setEnabled(true);
                    Log.d("_RespponseAudiopath", "pdyj is" + response_attachemnt_url);

                } catch (Exception ex) {
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                PrefManager smt = new PrefManager(context);
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "" + jwt_token);
                Log.i(TAG + "_token", jwt_token);
                return params;
            }
        };
        request.addFile("stream", attach_audio_path);
        request.addStringParam("name", attach_audio_path.substring(attach_audio_path.lastIndexOf("/") + 1));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
        Log.d("sssssssssss", "" + attach_audio_path);

    }

    private void imageUpload(String file_type) {
        fileExtension.clear();
        fileUrl.clear();
        filename.clear();
        File file = new File(attach_image_path, attach_image_path.substring(attach_image_path.lastIndexOf("/") + 1));
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        int index = file.getName().lastIndexOf('.') + 1;
        String ext = file.getName().substring(index).toLowerCase();
        final String type = mime.getMimeTypeFromExtension(ext);
        Log.i(TAG, "mime type......" + type);
        Log.i(TAG, "url" + GlobalClass.getLoginUrl());
        SimpleMultiPartRequest request = new SimpleMultiPartRequest(GlobalClass.getLoginUrl() + "/upload", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG + "_Respponse", response);
                try {
                    JSONObject json = new JSONObject(response).getJSONArray("uploadFiles").getJSONObject(0);
                    response_attachemnt_url = json.getString("path").replaceAll(" ", "%20");
                    response_attachemnt_name = json.getString("fileName");
                    rel_upload_progress.setVisibility(View.GONE);
                    fileExtension.add("image");
                    filename.add(response_attachemnt_name);
                    fileUrl.add(response_attachemnt_url);
                    btn_send.setAlpha(1f);
                    btn_send.setClickable(true);
                    btn_send.setEnabled(true);

                } catch (Exception ex) {
                    Log.i(TAG, "*****************" + ex.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                PrefManager smt = new PrefManager(context);
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "" + jwt_token);
                Log.i(TAG + "_token", jwt_token);
                return params;
            }
        };
        request.addFile("stream", file_type.equals("image") ? compressImage(attach_image_path) : attach_image_path);
        request.addStringParam("name", attach_image_path.substring(attach_image_path.lastIndexOf("/") + 1));
        request.addStringParam("windowId", "" + new Random().nextInt(1000));
        request.addStringParam("id", "" + user_id);
        request.addStringParam("size", "" + file.length());
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public void sendAttchment(String img_path) {
        ll_no_messages.setVisibility(View.GONE);
        JSONObject addMessage = new JSONObject();
        try {

            String to_email = "";
            JSONArray inviteeArray = new JSONArray();
            WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
            String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            Log.d("IPis", ip);

            JSONObject dataObj = new JSONObject();
            calmailObj = new JSONObject();
            JSONArray dataArray = new JSONArray();
            JSONObject inviteFromObj = new JSONObject();

            inviteFromObj.put("CML_PARENT_TEMP_KEY_VAL", "admin@mstorm.com#" + tp + "#ORG1_1#MSG#" + user_id + "" + tp + "_" + random);
            inviteFromObj.put("CML_PRIORITY", 1);
            inviteFromObj.put("CML_SUB_CATEGORY", "");
            inviteFromObj.put("KEY_TYPE", "IDE");
            inviteFromObj.put("PARENT_KEY_TYPE", "TSK");
            inviteFromObj.put("PARENT_SUB_KEY_TYPE", "MSG");
            inviteFromObj.put("SUB_KEY_TYPE", "MSG_IDE");
            inviteFromObj.put("IDE_ATTENDEES_EMAIL", "" + user_id);
            inviteFromObj.put("IDE_TYPE", "FROM");
            inviteFromObj.put("CML_TEMP_KEY_VAL", "admin@mstorm.com#PRJ:CGR_" + tp + "_" + random + "_TSK:MSG_" + tp + "_" + random + "_IDE:FROM_" + "" + user_id);
            inviteeArray.put(inviteFromObj);


            calmailObj.put("ACTIVE_STATUS", 1);
            calmailObj.put("CML_COMPLETION_PERCENTAGE", 0);
            calmailObj.put("CML_DESCRIPTION", "");
            calmailObj.put("CML_HOURS", 0);
            calmailObj.put("CML_IMAGE_PATH", new JSONArray().put("" + img_path));
            calmailObj.put("CML_IMAGE_PATH", "");
            calmailObj.put("CML_MESSAGE_INDEX", System.currentTimeMillis());
            calmailObj.put("CML_PARENT_ID", "" + group_json.getString("PROJECT_ID"));
            calmailObj.put("CML_PARENT_SUB_KEY_TYPE", "TSK_CGR");
            calmailObj.put("CML_PRIORITY", 1);
            calmailObj.put("CML_REF_ID", "" + group_json.getString("PROJECT_ID"));
            calmailObj.put("CML_SUB_CATEGORY", "");
            calmailObj.put("CML_TAG", new JSONArray());
            calmailObj.put("CML_TEMP_KEY_VAL", "admin@mstorm.com#" + tp + "#ORG:1_1#MSG#" + user_id + "" + tp + "_" + random);
            calmailObj.put("CML_TITLE", "" + img_path.split("/")[img_path.split("/").length - 1]);
            calmailObj.put("CML_TYPE", "image/png");
            calmailObj.put("CREATED_BY", "" + user_id);
            calmailObj.put("CREATED_ON", "");
            calmailObj.put("CREATE_IP", "");
            calmailObj.put("DEPT_ID", "" + group_json.getString("DEPT_ID"));
            calmailObj.put("DEPT_PROJECT_ID", "");
            calmailObj.put("DESIGNATION", "" + designation);
            calmailObj.put("KEY_TYPE", "TSK");
            calmailObj.put("ORG_ID", "admin@mstorm.com#1231231231231#ORG:1_1");
            calmailObj.put("SYNC_PENDING_STATUS", 0);
            calmailObj.put("SUB_KEY_TYPE", "CGR_ATH");
            calmailObj.put("SYNC_PENDING_STATUS", 0);
            calmailObj.put("imgPath", "" + GlobalClass.getLoginUrl() + "" + img_path);

            calmailObj.put("CREATED_BY", user_id);
            calmailObj.put("CML_MESSAGE_INDEX", System.currentTimeMillis());
            calmailObj.put("send_flag", false);
            calmailObj.put("LAST_MODIFIED_ON", "");

            dataObj.put("inviteeList", inviteeArray);
            dataObj.put("actionArray", new JSONArray().put("ADD_CHAT_MESSAGE"));
            dataObj.put("calmailObject", calmailObj);

            dataArray.put(dataObj);

            addMessage.put("dataArray", dataArray);
            addMessage.put("socketId", GlobalClass.getAuthenticatedSyncSocket().id());
            addMessage.put("requestId", "/sync#" + GlobalClass.getAuthenticatedSyncSocket().id() + "" + user_id + "#" + tp);
            addMessage.put("moduleName", "CGR");
            addMessage.put("action", "INSERT");
            addMessage.put("userId", user_id);
            addMessage.put("FROM", "senddatatoserver");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (addMessage.keys().hasNext()) {
            try {
                ChatList.add(ChatList.size(), new ChatListModel(2, edit_msg.getText().toString(), time, "no_status", calmailObj.getString("imgPath"), true, new JSONObject(), false, ""));
                rec_chat_list.getRecycledViewPool().clear();
                chatAdapter.notifyDataSetChanged();
                //SocketListenerClass.prepareInsert(TBL_MESSAGES, calmailObj);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (GlobalClass.getAuthenticatedSyncSocket() != null) {
            GlobalClass.getAuthenticatedSyncSocket().emit("serverOperation", addMessage);
            Log.d("ChatMsgObj", String.valueOf(addMessage));
        }
    }


    private void resetMsgCountcall(){
        try {
            selectedConversation.getJSONObjectConversationResetUNReadCount();
        }
        catch (Exception ex)
        {
            Log.i(TAG,"-------------"+ex.getMessage());
        }
    }

    public static void chatActinBarVisibility(int cnt) {
        img_right_icon1.setVisibility(cnt == 1 ? View.VISIBLE : View.GONE);
        img_right_icon2.setVisibility(cnt > 0 ? View.VISIBLE : View.GONE);
        img_right_forward.setVisibility(cnt > 0 ? View.VISIBLE : View.GONE);
        img_right_star.setVisibility(cnt > 0  ? View.VISIBLE :View.GONE);
        txt_action_title.setText(cnt > 0 ? cnt + " " : groupTitle);
        lin_group_member_list.setEnabled(cnt > 0 ? false : true);
        li_img_star.setVisibility(cnt > 0  ? View.VISIBLE :View.GONE);
        li_img_delete.setVisibility(cnt > 0 ? View.VISIBLE : View.GONE);
        li_img_forward.setVisibility(cnt > 0 ? View.VISIBLE : View.GONE);
        li_img_reply.setVisibility(cnt == 1 ? View.VISIBLE : View.GONE);

    }


    @Override
    public boolean onBackPressed() {

        if (isAttachView) {
            setAttachView();
            return isAttachBack;
        }

        try {
            if (edit_msg.getText().length() > 0) {
                App.setChatText(edit_msg.getText().toString());
                App.setChatkeyval(group_json.getString("KEY_VAL"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (fromfrgament.equals("GroupMember")) {

            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove(new ChatListFragment());
            trans.commit();
            manager.popBackStack();
            fromfrgament = "";
        }

        if(backcounterContact>0)
        {
            for (int i = 0; i < backcounterContact; i++) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.remove(new ChatListFragment());
                trans.commit();
                manager.popBackStack();
            }
        }

        if (ImageExapndViewFragment.fromImageviewfr) {
            if (ChatListAdapter.count > 0) {
                for (int i = 0; i < ChatListAdapter.count; i++) {
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction trans = manager.beginTransaction();
                    trans.remove(new ChatListFragment());
                    trans.commit();
                    manager.popBackStack();
                }
            }

            ImageExapndViewFragment.fromImageviewfr = false;
            ChatListFragment.fromfrgament = "conv";
            ChatListAdapter.count = 0;
        }


        if (NestedChatListFragment.nestedmsg) {
            if (ChatListAdapter.replycounterback > 0) {
                for (int i = 0; i < ChatListAdapter.replycounterback; i++) {
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction trans = manager.beginTransaction();
                    trans.remove(new ChatListFragment());
                    trans.commit();
                    manager.popBackStack();
                }
            }

            NestedChatListFragment.nestedmsg = false;
            ChatListFragment.fromfrgament = "conv";
            ChatListAdapter.replycounterback = 0;
        }

        if (fromfrgament.equals("GroupMemberContact")) {

            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove(new ChatListFragment());
            trans.commit();
            manager.popBackStack();
            fromfrgament = "contact";
            FragmentManager manager1 = getActivity().getSupportFragmentManager();
            Fragment mFragment = new FragmentHomeScreen();
            FragmentTransaction transaction = manager1.beginTransaction();
            transaction.replace(R.id.frame_frag_container, mFragment);
            transaction.commit();
            GroupMemberListFrag.isbackpressfromgroup = "contact";

        }
/*
        if(GroupMemberListFrag.isbackpressforviewprofile==true){
            ReplaceFragment.replaceFragment(ChatListFragment.this, R.id.frame_frag_container, FragmentHomeScreen.newInstance(), false);

        }
*/

        if (GroupMemberListFrag.isbackpressOntoolbar) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove(new ChatListFragment());
            trans.commit();
            manager.popBackStack();
            fromfrgament = "conv";
            GroupMemberListFrag.isbackpressOntoolbar = false;
        }

        return false;

    }

    private void updateUi() {
        try {
            groupNmforwardList.clear();
            Repository repo = Repository.getRepository();

            ArrayList<Conversation> newgrplist = (ArrayList<Conversation>) repo.getAllConversations("unarchive");
            // groupNmList=(ArrayList<Conversation>)repo.getAllConversations();

            Log.d("beforer", String.valueOf(newgrplist.size()));

            if (newgrplist.size() > 0) {

                for (int i = 0; i < newgrplist.size(); i++) {
                    if (newgrplist.get(i).getKeyval().equals(selectedConversation.getKeyval())) {
                        newgrplist.remove(newgrplist.get(i));
                    }
                }
            }

            Log.d("after", String.valueOf(newgrplist.size()));


            groupNmforwardList.addAll(newgrplist);

            if (groupNmforwardList.size() > 0 && groupNmforwardList != null) {
                progress1.setVisibility(View.GONE);
                txt_no_contact.setVisibility(View.GONE);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                rec_contact.setLayoutManager(mLayoutManager);
                rec_contact.setItemAnimator(new DefaultItemAnimator());
                ForwardMsgListAdapter conAdapter = new ForwardMsgListAdapter(getActivity(), groupNmforwardList, ChatListFragment.this);
                rec_contact.setAdapter(conAdapter);

            } else {
                progress1.setVisibility(View.GONE);
                txt_no_contact.setVisibility(View.VISIBLE);
            }





        } catch (Exception e) {
        }
    }


    public static ArrayList<Conversation> getSortedConvList(ArrayList<Conversation> conversationsList) {
        try {
            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
            Collections.sort(conversationsList, new Comparator<Conversation>() {
                @Override
                public int compare(Conversation obj1, Conversation obj2) {
                    String timeInfo1 = "", timeInfo2 = "";
                    String cmlTime = "";
                    String cmlTime1 = "";
                    int compare = 0;
                    Log.d("cmlTime", obj1.getLastModifiedOn());
                    cmlTime = obj1.getLastModifiedOn();

                    Log.d("cmlTime1", obj2.getLastModifiedOn());
                    cmlTime1 = obj2.getLastModifiedOn();

                    timeInfo2 = DateClass.changeDateFormat(cmlTime1);
                    timeInfo1 = DateClass.changeDateFormat(cmlTime);

                    System.out.println("--------------------------" + timeInfo1 + "-----------------------" + timeInfo2);
                    Long timeInMillis1 = Long.parseLong(DateClass.getTimeInMillis(timeInfo1));
                    Long timeInMillis2 = Long.parseLong(DateClass.getTimeInMillis(timeInfo2));

                    try {

                        if (timeInMillis1 > timeInMillis2) {
                            compare = 1;
                        }
                        if (timeInMillis1 < timeInMillis2) {
                            compare = -1;
                        }
                        if (timeInMillis1 == timeInMillis2) {
                            compare = 0;
                        }
                    } catch (Exception e) {
                        Log.e("expn_srt_logic1:", e.toString());
                    }
                    return compare;

                }
            });
            Collections.reverse(conversationsList);
        } catch (Exception e) {
            Log.e("SortingException: ", "" + e.toString());
        }
        return conversationsList;
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
                Log.i(TAG, string + " ..kk");
                if (string != null) {

                    if (string.equals("fetch_invitee")) {
                        isFetchInvitee = true;
                        fetcMessages();
                    }

                    if (string.equals("msg_edit_done")) {
                        tempList.clear();
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

                    if (string.equals("CHAT_FILTER_SERVER")) {

                        updateUi();
                      /*  if (flag_Archive) {
                            flag_Archive = false;
                            popup.getMenu().findItem(R.id.item3).setTitle(Html.fromHtml("<font color='#000000'>Unarchive</font>"));
                        } else {
                            flag_Archive = true;
                            popup.getMenu().findItem(R.id.item3).setTitle(Html.fromHtml("<font color='#000000'>Archive</font>"));
                        }*/


                    }

                    if (string.equals("ARCHIVE_CONVERSATION_SERVER")) {
                        flag_Archive = true;
                        popup.getMenu().findItem(R.id.item3).setTitle(Html.fromHtml("<font color='#000000'>Unarchive</font>"));
                        updateUi();
                    }

                    if (string.equals("UNARCHIVE_CONVERSATION_SERVER")){
                        flag_Archive = false;
                        popup.getMenu().findItem(R.id.item3).setTitle(Html.fromHtml("<font color='#000000'>Archive</font>"));
                        updateUi();
                    }


                    if(string.equals("SUPERIMPOSE_EVENT_SERVER"))
                    {

                        if (isAttachView) {
                            isAttachView = false;
                            isAttachBack = true;
                            ll_attachment.setVisibility(View.GONE);
                        }

                        showData();

                    }

                    if (string.equals("scrollposition")) {
                        rec_chat_list.scrollToPosition(reply_pos);

                    }


                    if((string.equals("fetchContactComplete"))){
                        lin_add_block.setVisibility(View.GONE);
                        lin_Un_block.setVisibility(View.GONE);
                        showData();
                    }



                    if(string.equals("FETCH_CONTACT_SERVER")){
                        boolean internetAvailability = InternetConnectionChecker.checkInternetConnection(getActivity());
                        Log.i("is internet available:", String.valueOf(internetAvailability) + " ..kk");
                        if (GlobalClass.getAuthenticatedSyncSocket() != null && internetAvailability) {
                           /*JSONObject unblockcontactObj = JsonObjectContact
                                    .getJSONObjectUnblockContact(ContactsListAdapter.contactsListModel);
                            Log.d("UNBLOCKContact", String.valueOf(unblockcontactObj));
                            GlobalClass.getAuthenticatedSyncSocket().emit("serverOperation", unblockcontactObj);*/
                            try{
                                Repository rr=Repository.getRepository();
                                String completeString=selectedConversation.getCompleteData();
                                JSONObject completeObj=new JSONObject(completeString);
                                String contactUserString="";
                                if(completeObj.has("CONTACT_USER")){
                                    contactUserString=completeObj.getString("CONTACT_USER");
                                }
                                Log.i("cntctUsrString:",contactUserString);
                                Contact cntct=rr.getContact(contactUserString);
                                Log.i("officialEmailId:",cntct.getOfficialEmail()+" ..kk");
                                cntct.getJSONObjectUnblockContact();
                                /*JSONObject unblckObj=JsonObjectContact.getJSONObjectUnblockContact(cntct);
                                Log.i(TAG+"_unblockCntct:",String.valueOf(unblckObj)+" ..kk");
                                String kt=cntct.getKeytype();
                                String skt=cntct.getSubkeytype();
                                String keyval=cntct.getKeyval();
                                Log.i("cntct_kt:",kt+" ..kk");
                                Log.i("cntct_skt:",skt+" ..kk");
                                Log.i("cntct_keyval:",keyval+" ..kk");
                                GlobalClass.getAuthenticatedSyncSocket().emit("serverOperation",unblckObj);
                           */ }catch(JSONException e){
                                Log.e("expn1:",e.toString());
                            }catch(Exception e){
                                Log.e("expn2:",e.toString());
                            }

                        }
                        showData();


                    }



                    if(string.equals("fetch_msg_complete"))
                    {
                        progressBar.setVisibility(View.GONE);
                        Log.d("response:", string);
                        //edit_msg.setText("");
                        tempList.clear();

                        if(ChatListAdapter.selectedCount<=0)
                        {
                            showData();
                            /*if(chatAdapter!=null)
                            {
                                chatAdapter.resetFlag();
                            }*/
                        }
                    }

                    if (string.equals("broadMsgStatus")){
                        resetMsgCountcall();
                    }

                    if (string.equals("broadMsgStatus") ||
                            string.equals("delete_msg")||string.equals("contact_removed")) {
                        //ll_chat_progress.setVisibility(View.INVISIBLE);
                        //ll_chat_progress.setVisibility(View.GONE); // temp comment

                        if(string.equals("contact_removed")){
                            lin_Un_block.setVisibility(View.GONE);
                            txt_removed.setVisibility(View.GONE);
                            li_msg_layout.setVisibility(View.VISIBLE);
                        }
                        progressBar.setVisibility(View.GONE);
                        Log.d("response:", string);
                        //edit_msg.setText("");
                        tempList.clear();

                        showData();

                      /*  if(chatAdapter!=null)
                        {
                            chatAdapter.resetFlag();
                        }*/
                        if (forwardmsg.equals("forward")) {
                            if (dialog != null) {
                                dialog.dismiss();
                                forwardmsg = "";
                                if (selected_forward_arr.size() > 1) {
                                    fromfrgament = "conv";
                                    GroupMemberListFrag.isbackpressOntoolbar = false;
                                    common.hideKeyboard(getActivity());
                                    ConversationListFragment.isArchive = false;
                                    ReplaceFragment.replaceFragment(ChatListFragment.this, R.id.frame_frag_container, FragmentHomeScreen.newInstance(), false);
                                } else {

                                    fromfrgament = "";
                                    forwardsingle="msg";

                                    Bundle bundle = new Bundle();
                                    ChatListFragment.fromfrgament = "GroupMember";
                                    //bundle.putString("group_json", ""+getArguments().getString("group_json"));
                                    bundle.putString("group_json", selected_forward_arr.get(0));
                                    Fragment fragment = new ChatListFragment();
                                    fragment.setArguments(bundle);
                                    ReplaceFragment.replaceFragment(ChatListFragment.this, R.id.frame_frag_container, fragment, false);
                                }
                            }
                        }
                    }

                    if(string.equals("block_conversation")){

                        try {
                            lin_add_block.setVisibility(View.GONE);
                            txt_removed.setText("You have Blocked "+conversationObj.getString("CML_TITLE"));
                            txt_removed.setVisibility(View.VISIBLE);
                            li_msg_layout.setVisibility(View.GONE);
                            lin_Un_block.setVisibility(View.VISIBLE);
                            showData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    if (string.equals("clear_chat")) {
                        ChatList.clear();
                        // showData();

                        if (ChatList.size() == 0) {
                            rec_chat_list.setVisibility(View.GONE);
                            ll_no_messages.setVisibility(View.VISIBLE);
                            txt_no_messages.setText("No message listed");
                        }

                    }

                    if (string.equals("forward")) {
                        Log.i("fsize", String.valueOf(selected_forward_arr.size()));
                        if (selected_forward_arr.size() > 0) {
                            btn_add.setAlpha(1f);
                            btn_add.setEnabled(true);

                        } else {
                            btn_add.setAlpha(0.5f);
                            btn_add.setEnabled(false);

                        }

                    }

                    if (string.equals("remove")) {
                        Log.i("fsize", String.valueOf(selected_forward_arr.size()));
                        if (selected_forward_arr.size() > 0) {
                            btn_add.setAlpha(1f);
                            btn_add.setEnabled(true);
                        } else {
                            btn_add.setAlpha(0.5f);
                            btn_add.setEnabled(false);

                        }
                    }

                    if(string.equals("upload_done"))
                    {
                        filename.clear();
                        docfile.clear();
                        fileExtension.clear();
                        fileUrl.clear();
                       /* btn_upload.setEnabled(true);
                        btn_upload.setAlpha(1f);*/

                    }

                    if (string.equals("conversation_update")) {
                        groupNmList.clear();
                        Repository repo = Repository.getRepository();

                        String isArc = flag_Archive ? "archive" : "unarchive";
                        groupNmList = (ArrayList<Conversation>) repo.getAllConversations(isArc);
                        for (int i = 0; i < groupNmList.size(); i++) {
                            Log.d("grpNMlistttttttt", groupNmList.get(i).getCmlTitle());
                        }
                        try {
                            if (groupNmList.size() > 0) {
                                for (int i = 0; i < groupNmList.size(); i++) {
                                    Log.d("grpListkeyval", groupNmList.get(i).getCmlTitle());
                                    Log.d("conversationObj2", conversationObj.toString());
                                    if (groupNmList.get(i).getKeyval().equals(conversationObj.getString("KEY_VAL"))) {
                                        Log.d("conversation_keyval", conversationObj.getString("KEY_VAL"));
                                        conversationObj = new JSONObject(groupNmList.get(i).getCompleteData());
                                        ismakeImportant = conversationObj.getInt("CML_STAR") == 0 ? false : true;
                                        selectedConversation = JsonParserClass.parseConversationJsonObject(conversationObj);
                                        break;

                                    }
                                }
                            }
                            setIsmakeImportant();
                        } catch (Exception ex) {
                            Log.e(TAG, "------------" + ex.getMessage());
                        }
                    }
                }
            }
        };
        com.nc.developers.cloudscommunicator.Subcription s = new com.nc.developers.cloudscommunicator.Subcription();
        s.setObservable(mobservable);
        s.setObserver(myObserver);
        GlobalClass.setCurrentSubcriberr(s);
    }


    private void setIsmakeImportant()
    {
        if (ismakeImportant) {
            popup.getMenu().findItem(R.id.item2).setTitle(Html.fromHtml("<font color='#000000'>Unimportant</font>"));
        } else {
            popup.getMenu().findItem(R.id.item2).setTitle(Html.fromHtml("<font color='#000000'>Important</font>"));
        }
    }

}