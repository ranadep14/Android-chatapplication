package com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cloudsinc.soulmobile.nativechatapplication.App;
import com.cloudsinc.soulmobile.nativechatapplication.AppConfig;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.InviteeListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.SearchContactListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.datamodels.User;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.FragmentHomeScreen;
import com.cloudsinc.soulmobile.nativechatapplication.interfaces.ApiConfig;
import com.cloudsinc.soulmobile.nativechatapplication.utils.CircleTransform;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ProfileUploads;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;
import com.google.gson.JsonElement;
import com.nc.developers.cloudscommunicator.ImageClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.Subcription;
import com.nc.developers.cloudscommunicator.models.Contact;
import com.nc.developers.cloudscommunicator.models.Conversation;
import com.nc.developers.cloudscommunicator.models.Login;
import com.nc.developers.cloudscommunicator.models.UserURM;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.MailListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.datamodels.MailListModel;
import com.cloudsinc.soulmobile.nativechatapplication.utils.InternetConnectionChecker;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.objects.login.JsonObjectLogin;
import com.nc.developers.cloudscommunicator.sharedprefrences.PrefManager;
import com.nc.developers.cloudscommunicator.socket.JsonParserClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/*import static com.cloudsinc.soulmobile.nativechatapplication.GlobalClass.getUserId;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.contactImag;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.hideKeyboard;*/
import static android.app.Activity.RESULT_OK;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.MailListAdapter.templst;
import static com.cloudsinc.soulmobile.nativechatapplication.adapters.SearchContactListAdapter.templstcount;
import static com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactsSearchFragment.hideKeyboard;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.ImageCompress.compressImage;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.isValidate;

/**
 * This Fragment is used to create conversations
 *
 * @author Prajakta Patil
 * @version 1.0
 * @since 23.10.2018
 */


public class CreateConversationFragment extends Fragment {
    private Observable<String> mobservable;
    private Observer<String> myObserver;
    public ArrayList<String> personaList = new ArrayList<>();
    public ArrayList<MailListModel> MailList = new ArrayList<MailListModel>();
    public ArrayList<MailListModel> addMemMailList = new ArrayList<MailListModel>();
    private ArrayList<Contact> ContactList = new ArrayList<>();
    private String personaSelected = "";
    int flag = 1;

    private static boolean isActiveContact = false;

    private ArrayList<String> strings = new ArrayList<>();
    private ArrayList<String> userUrmProjectIdList = new ArrayList<String>();

    Context context;
    MailListAdapter mailAdapter;
    int random;
    int max = 9000;
    int min = 1000;

    JSONObject group_json = null;
    JSONObject create_grp_json = null;
    String user_id, groupTitle;
    boolean activestatus;


    public static Button btn_add;
    ArrayList<User> searchContactList;
    SearchContactListAdapter conAdapter;
    public static ArrayList<String> selected_user_arr;
    public static Dialog dialog;
    public static ArrayList<String> arrayList;

    @BindView(R.id.spin_persona)
    Spinner spin_persona;
    @BindView(R.id.img_search_contact)
    ImageView img_search_contact;
    @BindView(R.id.edit_email)
    EditText edit_email;
    @BindView(R.id.edit_group_nm)
    EditText edit_group_nm;
    @BindView(R.id.rec_mail_list)
    RecyclerView rec_mail_list;
    @BindView(R.id.btn_create)
    Button btn_create;
    @BindView(R.id.btn_cancel)
    Button btn_cancel;
    @BindView(R.id.txt_action_title)
    TextView txt_action_title;
    @BindView(R.id.scroll_creategrp)
    ScrollView scrollViewcrategrp;
    @BindView(R.id.ll_chat_progress)
    LinearLayout ll_chat_progresscrategrp;

    @BindView(R.id.fab_edit_grp)
    FloatingActionButton fab_edit;

    @BindView(R.id.img_grp_profile)
    ImageView img_user_profile;
    @BindView(R.id.search_progress)
    ProgressBar search_progress;

    RecyclerView rec_contact;
    TextView txt_no_contact;
    ProgressBar progress;
    public static ImageView img_close;

    ProfileUploads profileUploads;
    private String TAG = this.getClass().getSimpleName();

    String email, title, group_title;
    String strEmailId;
    Boolean spSelected = false;
    int contactsize;

    Uri photoURI = null;
    private static final int GALLERY_PERMISSION_REQUEST_CODE = 250;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 150;
    private Uri ImgProfileUri, ImgProfileUri1;
    String mediaPath;
    String image_path = "";
    String uName = "", uProfileImg = "", uMailId = "";
    ImageClass imageClass= ImageClass.getImageClassInstance();
    //ArrayList<String>imagepath=imageClass.getPathList();
    ArrayList<String>imagepath=GlobalClass.getServerPathList();
    String imageNn="";



    public CreateConversationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_conversation, container, false);
        context = view.getContext();
        ButterKnife.bind(this, view);
        selected_user_arr = new ArrayList<>();
        searchContactList = new ArrayList<>();
        arrayList = new ArrayList<>();
        txt_action_title.setText(context.getResources().getString(R.string.create_conversation));

        App.setSearchSelectedUserArr(new ArrayList<String>());




        String[] persona = new String[]{
                "Persona",
                "Work",
                "Family",
                "Personal",
                "Social"
        };

         profileUploads=new ProfileUploads(getActivity());


        final List<String> personaList = new ArrayList<>(Arrays.asList(persona));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.textview_layout, personaList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.DKGRAY);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.textview_layout);
        spin_persona.setAdapter(spinnerArrayAdapter);

        spin_persona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(getResources().getColor(R.color.appTextHintColor));
                String selectedItemText = (String) parent.getItemAtPosition(position);
                personaSelected = (String) parent.getItemAtPosition(position);
                if (position > 0) {
                    ((TextView) view).setTextColor(getResources().getColor(R.color.appTextColor));
                    if (activestatus && edit_group_nm.length() > 0 && contactsize > 0) {

                        btn_create.setEnabled(true);
                        btn_create.setAlpha(1);

                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        edit_group_nm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v, getActivity());
                }
            }
        });
        edit_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v, getActivity());
                }
            }
        });
        edit_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (edit_email.getText().length() > 0) {
                    img_search_contact.setAlpha(1f);
                    img_search_contact.setEnabled(true);

                } else {
                    img_search_contact.setAlpha(0.5f);
                    img_search_contact.setEnabled(false);
                }
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rec_mail_list.setLayoutManager(mLayoutManager);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.recycler_divide));
        rec_mail_list.addItemDecoration(itemDecorator);
        mailAdapter = new MailListAdapter(getActivity(), MailList, adapterInterface);
        rec_mail_list.setAdapter(mailAdapter);

        edit_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                strEmailId = s.toString();

                if (strEmailId.contains("@")) {
                    if (!isValidEmail(strEmailId)) {

                        edit_email.setError("Please Enter correct Email Id");
                        Log.d("success", "success");

                    }

                } else {
                    strEmailId = strEmailId + "@clouzer.com";
                    Log.d("strEmailId", strEmailId);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edit_group_nm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                title = s.toString();
                if (!title.equals("")) {
                    setEnable();
                } else {
                    btn_create.setEnabled(false);
                    btn_create.setAlpha(0.3f);

                }

            }
        });

        if (ConversationListFragment.isGrpUpdate) {
            try {
                String clickedJsonString = getArguments().getString("group_json");
                try {
                    group_json = new JSONObject(clickedJsonString);
                    Log.i("grpUpdtObj: ", group_json.toString());
                } catch (JSONException e) {
                    Log.e("exception: ", e.toString());
                }
                edit_group_nm.setText("" + group_json.getString("CML_TITLE"));
                groupTitle = group_json.getString("CML_TITLE");
                btn_create.setText("UPDATE");

                Log.d("WorksSpace", group_json.getString("CML_SUB_CATEGORY"));
                if (group_json.getString("CML_SUB_CATEGORY").contains("1"))
                    spin_persona.setSelection(1);
                if (group_json.getString("CML_SUB_CATEGORY").contains("2"))
                    spin_persona.setSelection(2);
                if (group_json.getString("CML_SUB_CATEGORY").contains("3"))
                    spin_persona.setSelection(3);
                if (group_json.getString("CML_SUB_CATEGORY").contains("4"))
                    spin_persona.setSelection(4);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (InternetConnectionChecker.checkInternetConnection(context)) {
            fetchDefaultContact();
        }
        btn_create.setEnabled(false);
        btn_create.setAlpha(0.3f);
        img_search_contact.setAlpha(0.5f);
        img_search_contact.setEnabled(false);
        setSubcriber();

      // updateprofile();


        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 showPictureDialog();

            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static boolean isValidEmail(String email) {

        if (email != null) {
            Pattern p = Pattern.compile("^[A-Za-z].*?@clouzer\\.com$");
            Matcher m = p.matcher(email);
            return m.find();
        }
        return false;

    }

    MailListAdapter.InfoAdapterInterface adapterInterface = new MailListAdapter.InfoAdapterInterface() {
        @Override
        public void OnItemClicked(int size) {

            if (size <= 0) {
                contactsize = size;
                btn_create.setEnabled(false);
                btn_create.setAlpha(0.3f);
            }


        }
    };

    @Optional
    @OnClick({R.id.img_search_contact, R.id.btn_create})
    public void onSelectDeselect(View view) {
        setSubcriber();
        switch (view.getId()) {
            case R.id.img_search_contact:
                showDialog();

                break;

            case R.id.btn_create:
               // imageNn="";
                scrollViewcrategrp.setVisibility(View.GONE);
                ll_chat_progresscrategrp.setVisibility(View.VISIBLE);
                emitRequireCalls();
                break;
        }
    }

    private void showDialog() {

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_search_contact_dialog);

        btn_add = dialog.findViewById(R.id.btn_add);

        dialog.setCancelable(false);
        rec_contact = (RecyclerView) dialog.findViewById(R.id.rec_contact);
        txt_no_contact = dialog.findViewById(R.id.txt_no_contact);

        progress = dialog.findViewById(R.id.progress);

        img_close = dialog.findViewById(R.id.img_close);


        btn_add.setAlpha(0.5f);
        btn_add.setEnabled(false);
        Conversation conversation=new Conversation();
        conversation.getJsonObjectSearchAllContact(edit_email.getText().toString(),"conversation");

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (GlobalClass.getCurrentSubcriberr() != null) {
                    GlobalClass.getCurrentSubcriberr().getObservable().just("userclickedmail")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(GlobalClass.getCurrentSubcriberr().getObserver());
                }
                dialog.dismiss();
            }
        });


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (templstcount > 0) {
                } else {
                    selected_user_arr.clear();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void fetchDefaultContact() {

        try {
            JSONObject contactObj = new JSONObject();
            JSONArray dataArray = new JSONArray();
            JSONObject dataObj = new JSONObject();

            dataObj.put("actionArray", new JSONArray().put("FETCH_CONTACT"));// delete for only me
            dataObj.put("lastID", "");
            dataArray.put(dataObj);

            contactObj.put("userId", "" + GlobalClass.getUserId());
            contactObj.put("dataArray", dataArray);
            contactObj.put("socketId", "" + GlobalClass.getAuthenticatedSyncSocket().id());
            contactObj.put("requestId", "/sync#" + GlobalClass.getAuthenticatedSyncSocket().id() + "" + GlobalClass.getUserId() + "#" + System.currentTimeMillis());
            contactObj.put("moduleName", "ORG");
            if (GlobalClass.getAuthenticatedSyncSocket() != null) {
                GlobalClass.getAuthenticatedSyncSocket().emit("OnDemandCall", contactObj);
                Log.d("fetchContactObj", String.valueOf(contactObj));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateUI() {

        try {
            searchContactList.clear();
            if (!(searchContactList.size() > 0)) {
                System.out.println("fffffff" + GlobalClass.getSearchContact());
                //   JSONArray jsonArray = GlobalClass.getSearchContact();
                JSONObject jbcontact = GlobalClass.getSearchContact();

                JSONArray jarray = jbcontact.getJSONArray("dataArray");

                for (int j = 0; j < jarray.length(); j++) {
                    boolean flag = false;
                    JSONObject jsonObject = jarray.getJSONObject(j);
                    if (jsonObject.has("USM_EMAIL")) {
                        flag = jsonObject.getString("USM_EMAIL").equals(GlobalClass.getUserId());
                    }
                    if (!flag) searchContactList.add(new User(jarray.getJSONObject(j)));

                }

            }

            System.out.println("fffffff" + GlobalClass.getSearchAllContactResult());
            JSONObject jobject = GlobalClass.getSearchAllContactResult();

            JSONArray jarray1 = jobject.getJSONArray("dataArray");

            for (int j = 0; j < jarray1.length(); j++) {
                boolean flag = false;
                JSONObject jsonObject = jarray1.getJSONObject(j);
                if (jsonObject.has("CML_OFFICIAL_EMAIL")) {
                    flag = jsonObject.getString("CML_OFFICIAL_EMAIL").equals(GlobalClass.getUserId());
                }
                if (!flag) searchContactList.add(new User(jarray1.getJSONObject(j)));

            }


            Log.i("contactLst:", String.valueOf(searchContactList.size()) + " ..kk");
            if (searchContactList != null && searchContactList.size() > 0) {
                progress.setVisibility(View.GONE);
                txt_no_contact.setVisibility(View.GONE);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                rec_contact.setLayoutManager(mLayoutManager);
                rec_contact.setItemAnimator(new DefaultItemAnimator());
                conAdapter = new SearchContactListAdapter(getActivity(), searchContactList, CreateConversationFragment.this);
                rec_contact.setAdapter(conAdapter);


            } else {
                progress.setVisibility(View.GONE);
                txt_no_contact.setVisibility(View.VISIBLE);
            }
        } catch (Exception ex) {
            Log.e(TAG, "-----------" + ex.getMessage());
        }
    }

    private void checkAddButton() {
        if (selected_user_arr.size() > 0) {
            btn_add.setAlpha(1f);
            btn_add.setEnabled(true);
        } else {
            btn_add.setAlpha(0.5f);
            btn_add.setEnabled(false);
        }
    }

    private void addMail() {

        MailList.clear();
        arrayList = App.getSearchSelectedUserArr();
        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                MailList.add(new MailListModel(arrayList.get(i)));
            }
        }
        strings.add(strEmailId);// added by me
        mailAdapter.notifyDataSetChanged();
        edit_email.setText("");
        if (MailList.size() > 0) {
            activestatus = true;
        } else {
            activestatus = false;
            btn_create.setEnabled(false);
            btn_create.setAlpha(0.3f);
        }
        setEnable();


    }

    private void createGroup() {

        if (isValidate(edit_group_nm, 1, true, "", getActivity())) {
            if (spin_persona.getSelectedItemPosition() > 0) {
                if (MailList.size() > 0) {

                    ArrayList<String> lst = new ArrayList<>();
                    if (MailList.size() > 0) {
                        for (int i = 0; i < MailList.size(); i++) {
                            lst.add(MailList.get(i).geteMail());
                            Log.i("object_such_such:", lst.get(i) + " ..kk");

                        }
                        String conversationName = edit_group_nm.getText().toString().trim();
                        Log.i("persona_selected:", personaSelected + " ..kk");
                        Log.i("conve_name:", conversationName + " ..kk");
                        for (int i = 0; i < lst.size(); i++) {
                            Log.i("list_value:", lst.get(i) + " ..kk");
                        }



                        UserURM userURM=new UserURM();
                        userURM.getJSONObjectCreateConversations(conversationName,
                                personaSelected,
                                lst,"",imageNn.replaceAll("\\?op=OPEN",""));
                    }


                } else {
                    //Toast.makeText(getActivity(),"Please add email..",Toast.LENGTH_SHORT).show();
                }
            } else {
                //Toast.makeText(getActivity(),"Select Persona Type.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setEnable() {
        if (!title.equals("") && activestatus && spin_persona.getSelectedItemPosition() > 0) {
            btn_create.setEnabled(true);
            btn_create.setAlpha(1);
        }
    }


    @OnClick(R.id.img_back)
    public void img_back() {
        getActivity().onBackPressed();

    }

    @OnClick(R.id.btn_cancel)
    public void btn_cancel() {
        ReplaceFragment.replaceFragment(CreateConversationFragment.this, R.id.frame_frag_container, FragmentHomeScreen.newInstance(), false);
    }

    private void emitRequireCalls() {//ankita code
        Repository repo = Repository.getRepository();
        userUrmProjectIdList.clear();
        userUrmProjectIdList = repo.getUserUrmProjectIdList();
        Contact contact =new Contact();
        contact.getJSONObjectFetchAllContacts();
        /*JSONObject fetchAllContacts = JsonObjectContact.getJSONObjectFetchAllContacts();
        Log.i("emit_fetch_all_cntcts:", String.valueOf(fetchAllContacts) + " ..kk");
        if (GlobalClass.getAuthenticatedSyncSocket() != null) {
            GlobalClass.getAuthenticatedSyncSocket().emit("OnDemandCall", fetchAllContacts);
        }*/
    }


    public void updateprofile() {


        /*ImageClass imageClass=new ImageClass();
        ArrayList<String>imagepath=new ArrayList<>();
        imagepath =imageClass.getPathList();*/
        ImageClass imageClass= ImageClass.getImageClassInstance();
        ArrayList<String>imagepath=GlobalClass.getServerPathList();
        Log.i("imagePathSz:",String.valueOf(imagepath.size())+" ..kk");
        String imageNn=imagepath.get(0);
        Log.i("image:",imageNn+" ..kk");

        Log.d(TAG + "pathForUpload", imagepath.toString());
        Glide.with(context).
                load( imagepath)
                .error(R.drawable.group_icon)
                .placeholder(R.drawable.group_icon)
                .transform(new CircleTransform(context))
                .into(img_user_profile);

    }



    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        pictureDialog.setTitle("Select Action");


        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                setGalleryImg();
                                break;
                            case 1:
                                setCamImg();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void setCamImg() {

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
                photoURI = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 3);
            }

        }

    }

    private void setGalleryImg() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 && (
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(intent, 7);
        }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //the selected audio.
        System.out.println("****************" + requestCode + "************" + resultCode);

        //Activity result for Gallery
        if (resultCode == RESULT_OK && requestCode == 7) {

            try {
                ImgProfileUri = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(ImgProfileUri, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);
                cursor.close();

                ArrayList<String>Imgproupload=new ArrayList<>();
                Imgproupload.add(mediaPath);
              /*  ImageClass imageClass=new ImageClass();
                imageClass.pdfFileUpload1(Imgproupload);*/
                ImageClass imageClass= ImageClass.getImageClassInstance();
                imageClass.uploadFile(Imgproupload);

                Log.d(TAG +"pppppppppp",Imgproupload.toString());
                if(mediaPath!=null){
                    search_progress.setVisibility(View.VISIBLE);
                }

              //  FileUpload(mediaPath);
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
                image_path = cursor.getString(column_index_data);
                File file = new File(image_path);
                //Uri uri = Uri.fromFile(file);
                ImgProfileUri = Uri.fromFile(file);
                Log.d("ImgUri", String.valueOf(ImgProfileUri));
                ArrayList<String>Imgpro=new ArrayList<>();
                Imgpro.add(image_path);
                /*ImageClass imageClass=new ImageClass();
                imageClass.pdfFileUpload1(Imgpro);*/
                ImageClass imageClass= ImageClass.getImageClassInstance();
                imageClass.uploadFile(Imgpro);


                Log.d(TAG +"sssssssss",Imgpro.toString());
                if(image_path!=null){
                    search_progress.setVisibility(View.VISIBLE);

                }


          //    imageUpload("image");

            } catch (Exception e) {
            }

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
                Log.i(TAG, "onNext----------" + string);
                if (string != null) {


                    if (string.contains("upload_done")) {
                      //  Toast.makeText(context,"ssssssss",Toast.LENGTH_SHORT).show();
                        imagepath=GlobalClass.getServerPathList();
                          Log.i("imagePathSz:",String.valueOf(imagepath.size())+" ..kk");
                          String str=imagepath.get(0);
                        String strArr[]=null;
                          if(str!=null){
                              strArr=str.split("#");
                          }
                          if(strArr!=null){
                              if(strArr.length==2){
                                  imageNn=strArr[1];
                              }
                          }


                         Log.i("image:",imageNn+" ..kk");


                        Log.d(TAG + "pathForUpload",GlobalClass.getLoginUrl() + "/" +imageNn );
                        Glide.with(context).
                                load( GlobalClass.getLoginUrl() + "/" +imageNn)
                                .error(R.drawable.group_icon)
                                .placeholder(R.drawable.group_icon)
                                .transform(new CircleTransform(context))
                                .into(img_user_profile);
                        search_progress.setVisibility(View.GONE);


                        // Toast.makeText(getActivity(), "Profile Update Successfully", Toast.LENGTH_SHORT).show();
                    }





                    if (string.contains("userclickedmail")) {
                        addMail();
                        Log.d("array", String.valueOf(App.getSearchSelectedUserArr()));

                    }

                    if (string.equals("searchContactComplete") || string.equals("search_contact_done")) {
                        updateUI();
                    }

                    if (string.contains("user_click")) {

                        checkAddButton();

                    }
                    if (string.contains("delete")) {

                    }
                    if (string.contains("fetch_conversation") || string.contains("editGroupComplete") || string.contains("createGroupComplete") || string.contains("addMemberComplete")
                            || string.equals("create_conversation") || string.equals("CHAT_FILTER_SERVER")) {

                        Repository repo = Repository.getRepository();
                        ArrayList<Conversation> conversations = (ArrayList<Conversation>) repo.getAllConversations("unarchive");
                        if (conversations != null && conversations.size() > 0) {
                            for (int kk = 0; kk < conversations.size(); kk++) {
                                Conversation conv = conversations.get(kk);
                                Log.i("conv_name**:", conv.getCmlTitle() + " ..kk");
                            }
                        }
                        App.getSearchAddUserArr().clear();
                        //Ankita code
                        ReplaceFragment.replaceFragment(CreateConversationFragment.this, R.id.frame_frag_container, FragmentHomeScreen.newInstance(), false);

                    }
                    //nikhil code
                    if (string.equals("fetchContactComplete") || string.equals("no_contacts")) {

                        btn_create.setEnabled(false);
                        //ankita code
                        createGroup();

                        ContactList.clear();
                        Repository repo = Repository.getRepository();
                        Log.i(TAG, "contact list item" + repo.getAllContacts());
                        ArrayList<Contact> allContacts = (ArrayList<Contact>) repo.getAllContacts();
                        if (allContacts.size() > 0) {
                            for (int i = 0; i < allContacts.size(); i++) {
                                Contact cntct = allContacts.get(i);
                                Log.i("contact_name:", cntct.getFirstname() + "_" + cntct.getLastname() + " ..kk");
                                if (allContacts.get(i).getCmlAccepted() == 1) {
                                    ContactList.add(allContacts.get(i));
                                    Log.d("Contactlistis", allContacts.get(i).getOfficialEmail());
                                }
                            }
                        }
                        ArrayList<Contact> contactList = (ArrayList<Contact>) repo.getAllContacts();
                        for (int k = 0; k < contactList.size(); k++) {
                            Contact cntct = contactList.get(k);
                            Log.i("contact_Nm:", cntct.getFirstname() + "_" + cntct.getLastname() + " ..kk");
                        }
                    }

                }
            }

        };

        Subcription s = new Subcription();
        s.setObservable(mobservable);
        s.setObserver(myObserver);
        GlobalClass.setCurrentSubcriberr(s);

    }


}