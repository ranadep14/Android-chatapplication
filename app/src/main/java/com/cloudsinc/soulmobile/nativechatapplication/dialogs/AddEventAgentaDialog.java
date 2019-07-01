package com.cloudsinc.soulmobile.nativechatapplication.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cloudsinc.soulmobile.nativechatapplication.App;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.InviteeEventListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.UploadFileListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.datamodels.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Subcription;
import com.nc.developers.cloudscommunicator.models.Contact;
import com.nc.developers.cloudscommunicator.models.Event;
import com.nc.developers.cloudscommunicator.models.Invitee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

import static com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment.selected_Conversation;
import static com.cloudsinc.soulmobile.nativechatapplication.utils.common.isValidate;

/*
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;*/

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

public class AddEventAgentaDialog extends DialogFragment {

    private Observable<String> mobservable;
    private Observer<String> myObserver;
    boolean isUsersMsg;
    View view;
    Context context;
    static Context mcontext;
    UploadFileListAdapter uploadAdapter;
    AutoCompleteTextView autoCompleteTextView;
    public ArrayList<String> selectedInvitee=new ArrayList<>();

    private ArrayList<Invitee> inviteeList = new ArrayList<>();
    ArrayList<User> searchContactList = new ArrayList<>();

    private String personaSelected = "";

    public AddEventAgentaDialog() {
    }


    TextView txt_no_user;
    ProgressBar prg_searchuser;
    LinearLayout ll_search_contact_user;
    LinearLayout ll_no_userdata;
    EditText edit_serach;
    ImageView image_search;
    Button btn_ok;

    RecyclerView rec_invitee_list;
    InviteeEventListAdapter inviteeListAdapter;

    @BindView(R.id.img_close)
    ImageView img_close;

    @BindView(R.id.btn_event)
    Button btn_event;

    @BindView(R.id.spin_persona)
    Spinner spin_persona;

    @BindView(R.id.edit_start_date)
    TextView edt_start_date;

    @BindView(R.id.edit_end_date)
    TextView edt_end_date;

    @BindView(R.id.edit_end_time)
    TextView edt_end_time;

    @BindView(R.id.edit_start_time)
    TextView edt_start_time;

    @BindView(R.id.edit_envitee)
    TextView edit_envitee;

    @BindView(R.id.edit_group_nm)
    EditText edit_group_nm;

    private int mYear, mMonth, mDay, mHour, mMinute,second;
    private ArrayList<Invitee> grpMemList = new ArrayList<>();


   // com.google.api.services.calendar.Calendar mService;
    public static String time;

   /* GoogleAccountCredential credential;

    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
  */  private static final String DATE_FORMAT = "dd-M-yyyy hh:mm:ss a";


    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String PREF_ACCOUNT_NAME = "accountName";
  //  private static final String[] SCOPES = { CalendarScopes.CALENDAR };
    public String nama, tanggal, mulai, beres;
    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    private boolean shouldShow = false;

    String startdate_time="",enddate_time="";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_add_event, container, false);

        context = view.getContext();
        mcontext = view.getContext();



        String[] persona = new String[]{
                "Persona",
                "Work",
                "Family",
                "Personal",
                "Social"
        };


        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final LinearLayout root = new LinearLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ButterKnife.bind(this, view);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


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


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


      /*  SharedPreferences settings =getActivity().getPreferences(Context.MODE_PRIVATE);
               credential = GoogleAccountCredential.usingOAuth2(
                getActivity(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));

        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Calendar API Android Quickstart")
                .build();
*/

        edit_envitee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getAddMemList().clear();

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_invitee_custom_event_dailog);

                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                rec_invitee_list = (RecyclerView) dialog.findViewById(R.id.rec_invitee_list);
                edit_serach = (EditText) dialog.findViewById(R.id.edit_search);
                image_search = (ImageView) dialog.findViewById(R.id.imageSearch);
                btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
                img_close = (ImageView) dialog.findViewById(R.id.img_close);
                txt_no_user = (TextView) dialog.findViewById(R.id.txt_no_user);
                prg_searchuser = (ProgressBar) dialog.findViewById(R.id.prg_searchuser);
                ll_search_contact_user = (LinearLayout) dialog.findViewById(R.id.ll_search_contact_user);
                ll_no_userdata = (LinearLayout) dialog.findViewById(R.id.ll_no_userdata);

                btn_ok.setText("DONE");


                img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                      if(App.getAddMemList().size()>0)
                         {
                            Log.d("setMemListinadpter", String.valueOf(App.getAddMemList()));

                             edit_envitee.setText(App.getAddMemList().get(0).getcNm()+ "+"+(App.getAddMemList().size()-1)+"more");

                             dialog.dismiss();
                         }

                    }


                });
                rec_invitee_list.setHasFixedSize(true);
                rec_invitee_list.setLayoutManager(new LinearLayoutManager(getActivity()));


                edit_serach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                            searchUserFunction();
                               return true;
                        }
                        return false;
                    }


                });


                image_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchUserFunction();
                      }
                });

                inviteeListAdapter = new InviteeEventListAdapter(getActivity(), searchContactList);
                rec_invitee_list.setAdapter(inviteeListAdapter);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


            }
        });





      //  String apiKey = "AIzaSyDwsl59cd_g_5M1vMMVjuvKy0vrgBLQ4Nc";
       /* if(apiKey.isEmpty()){
            responseView.setText(getString(R.string.error));
           return;
        }*/

      /*  // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(getActivity(), apiKey);
        }

        placesClient = Places.createClient(getActivity());


        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
               getActivity().getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.NAME));

*/
      /*  // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
               // responseView.setText(place.getName()+","+place.getAddress());
                Log.i("Place: " , place.getName() + ", " + place.getAddress());
            }

            @Override
            public void onError(@NonNull Status status) {

            }




        });
*/


        btn_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           //    refreshResults();

                SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(PREF_ACCOUNT_NAME, "");
                editor.apply();
               // credential.setSelectedAccountName("");

               createEvent();

                  //JsonObjectEvent.createCalendarEvent("test");


            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // ChatListFragment.dFragment=null;

                 dismiss();

            }
        });


        edt_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog =new DatePickerDialog(getActivity(), R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        edt_start_date.setText(dayOfMonth + "-" + (month + 1) + "-" + year);

                    }

                }, mYear, mMonth, mDay);
                datePickerDialog.show();
               }
        });


        edt_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                               final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog =new DatePickerDialog(getActivity(), R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        edt_end_date.setText(dayOfMonth + "-" + (month + 1) + "-" + year);

                    }

                }, mYear, mMonth, mDay);

                datePickerDialog.show();

             }
        });

        edt_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.datepicker,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        second=c.get(Calendar.SECOND);

                        startdate_time=selectedHour + ":" + selectedMinute +":"+second;
                        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
                        Date date = null;
                        try {
                            date = fmt.parse(startdate_time);
                        } catch (ParseException e) {

                            e.printStackTrace();
                        }

                        SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm:ss aa");

                        String formattedTime=fmtOut.format(date);

                        edt_start_time.setText(formattedTime);



                    }
                },  mHour, mMinute, false);//Yes 24 hour time
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                                 }
                });

                timePickerDialog.show();

            }
        });


        edt_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.datepicker,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        second=c.get(Calendar.SECOND);

                         enddate_time=selectedHour + ":" + selectedMinute +":"+second;
                        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
                        Date date = null;
                        try {
                            date = fmt.parse(enddate_time);
                        } catch (ParseException e) {

                            e.printStackTrace();
                        }

                        SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm:ss aa");

                        String formattedTime=fmtOut.format(date);

                        edt_end_time.setText(formattedTime);



                    }
                },  mHour, mMinute, false);//Yes 24 hour time
                timePickerDialog.setTitle("Select Time");

                timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                    }
                });

                timePickerDialog.show();

            }
        });

        setSubcriber();

        return view;

    }


    private void createEvent() {

        if (spin_persona.getSelectedItemPosition() == 0|| (edt_start_time.length() == 0)
                || (edt_start_date.length() == 0) || (edt_end_time.length() == 0) || (edt_end_date.length() == 0) || (edit_envitee.length() == 0)) {
            if (isValidate(edit_group_nm, 1, true, "", getActivity())) {
            }

            if (spin_persona.getSelectedItemPosition() > 0) {
            } else {
                Toast.makeText(getActivity(), "Select Persona Type.", Toast.LENGTH_SHORT).show();
            }

            if (edt_start_date.length() == 0) {
                Toast.makeText(getActivity(), "Select start date.", Toast.LENGTH_SHORT).show();

            }
            if (edt_start_time.length() == 0) {

                Toast.makeText(getActivity(), "Select start time.", Toast.LENGTH_SHORT).show();
            }
            if (edt_end_date.length() == 0) {
                Toast.makeText(getActivity(), "Select end date.", Toast.LENGTH_SHORT).show();
            }

            if (edt_end_time.length() == 0) {
                Toast.makeText(getActivity(), "Select end time.", Toast.LENGTH_SHORT).show();
            }

            if (edit_envitee.length() == 0) {
                Toast.makeText(getActivity(), "select envitee", Toast.LENGTH_SHORT).show();
            }

        }
        else {


            boolean check_date = false;
            SimpleDateFormat objSDF = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date dt_1 = null;
            try {

                String start_date_time = edt_start_date.getText().toString() + " " + startdate_time + " ";
                String end_date_time = edt_end_date.getText().toString() + " " + enddate_time + " ";
                dt_1 = objSDF.parse(start_date_time);
                Date dt_2 = objSDF.parse(end_date_time);
                System.out.println("Date1 : " + objSDF.format(dt_1));
                System.out.println("Date2 : " + objSDF.format(dt_2));

                if (dt_1.compareTo(dt_2) > 0) {
                    Log.i("app", "Date1 is after Date2");
                    check_date = true;
                } else if (dt_1.compareTo(dt_2) < 0) {
                    Log.i("app", "Date1 is before Date2");
                    check_date = false;
                } else if (dt_1.compareTo(dt_2) == 0) {
                    Log.i("app", "Date1 is equal to Date2");
                    check_date = false;
                } else {
                    check_date = false;
                    System.out.println("You seem to be a time traveller !!");
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (!check_date) {
                for (int i = 0; i < App.getAddMemList().size(); i++) {
                    selectedInvitee.add(App.getAddMemList().get(i).getcMail());
                }

                String start_date_time = edt_start_date.getText().toString() + " " + startdate_time + " ";
                String end_date_time = edt_end_date.getText().toString() + " " + enddate_time + " ";

                String startDatetime11 = start_date_time;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ");
                System.out.println(startDatetime11);

                Date convertedDate11 = new Date();
                try {
                    convertedDate11 = dateFormat.parse(startDatetime11);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                System.out.println(convertedDate11);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));

                String strUTCDatestart = dateFormatter.format(convertedDate11);

                System.out.println("starttime" + strUTCDatestart);


                String startDatetime12 = end_date_time;
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ");

                System.out.println(startDatetime12);

                Date convertedDate12 = new Date();
                try {
                    convertedDate12 = dateFormat1.parse(startDatetime12);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                System.out.println(convertedDate12);
                SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                dateFormatter1.setTimeZone(TimeZone.getTimeZone("UTC"));

                String strUTCDateend = dateFormatter1.format(convertedDate12);

                System.out.println("endtime" + strUTCDateend);

                JSONObject jobCreateevent = new JSONObject();
                try {
                    jobCreateevent.put("cml_continent", "North America");
                    jobCreateevent.put("cml_country_code", "US");
                    jobCreateevent.put("cml_from_date_time", strUTCDatestart);
                    jobCreateevent.put("cml_latitude", 37.7875445);
                    jobCreateevent.put("cml_location", "California Street, San Francisco, CA, USA");
                    jobCreateevent.put("cml_longitude", -122.44492709999997);
                    jobCreateevent.put("cml_pincode", "41522");
                    jobCreateevent.put("cml_timezone", 25200000);
                    jobCreateevent.put("cml_to_date_time", strUTCDateend);


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                Event e=new Event();
                e.createCalendarEventFromAgenda(edit_group_nm.getText().toString(), personaSelected, selectedInvitee, jobCreateevent);

            }
            else
            {
                Toast.makeText(getActivity(), "Please select correct time", Toast.LENGTH_SHORT).show();

            }
        }

    }

    private void searchUserFunction() {
        String searchString = "";
        searchString = edit_serach.getText().toString().trim();
        Contact c=new Contact();
        c.searchUsersWhenCreatingEvent(searchString);

        if (edit_serach.getText().length() > 0) {

            searchContactList.clear();

            ll_search_contact_user.setVisibility(View.GONE);
            ll_no_userdata.setVisibility(View.VISIBLE);
            prg_searchuser.setVisibility(View.VISIBLE);
            txt_no_user.setVisibility(View.GONE);


        }
    }
    private void updateUiforSearch(String str) {

        try {
            if (str.equals("searchContactComplete")) {
                searchContactList.clear();
            }
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


                        for (int i = 0; i < searchContactList.size(); i++) {
                            for (int J = 0; J < grpMemList.size(); J++) {
                                if (searchContactList.get(i).getcMail().equalsIgnoreCase(grpMemList.get(J).getIde_attendees_email())) {
                                    searchContactList.remove(i);
                                    break;
                                }
                            }
                        }

                }

                if (inviteeListAdapter != null) {
                    inviteeListAdapter.reloadList(searchContactList);
                    inviteeListAdapter.notifyDataSetChanged();
                } else {
                    rec_invitee_list.setLayoutManager(new LinearLayoutManager(getActivity()));
                    for (int i = 0; inviteeList.size() > 0; i++) {
                        Log.d("aaaaaaaaaaaa", searchContactList.get(i).getcNm());
                    }

                    inviteeListAdapter = new InviteeEventListAdapter(getActivity(), searchContactList);
                    rec_invitee_list.setAdapter(inviteeListAdapter);
                }
            }
            noData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





    private void noData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ll_search_contact_user.setVisibility(searchContactList.size() > 0 ? View.VISIBLE : View.GONE);
                ll_no_userdata.setVisibility(searchContactList.size() > 0 ? View.GONE : View.VISIBLE);
                txt_no_user.setText("No Result found ");
                txt_no_user.setVisibility(searchContactList.size() > 0 ? View.GONE : View.VISIBLE);
                prg_searchuser.setVisibility(View.GONE);
            }
        },      searchContactList.size() > 0 ? 0 : 7000);
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
                Log.i("onNxt_string", string + " ..kk");
                if (string != null) {

                    if (string.equals("searchContactComplete")||string.equals("search_contact_done")) {
                        updateUiforSearch(string);

                    }

                    if(string.equals("SUPERIMPOSE_EVENT_SERVER"))
                    {
                      dismiss();

                    }

                    if (string.equals("userselected")) {
                        btn_ok.setAlpha(1f);
                    } else {
                        btn_ok.setAlpha(0.5f);
                    }


                }
            }
        };

        Subcription s = new Subcription();
        s.setObservable(mobservable);
        s.setObserver(myObserver);
        GlobalClass.setSignupSubcription(s);


    }
/*

    public class CreateApiAsyncTask extends AsyncTask<Void, Void, Void> {


        CreateApiAsyncTask() {

        }


        @Override
        protected Void doInBackground(Void... params) {
            try {

                getDataFromApi();
                createEvent();

            } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
                showGooglePlayServicesAvailabilityErrorDialog(
                        availabilityException.getConnectionStatusCode());

            } catch (UserRecoverableAuthIOException userRecoverableException) {
                startActivityForResult(
                        userRecoverableException.getIntent(),
                        REQUEST_AUTHORIZATION);

            } catch (IOException e) {


            }
            return null;
        }


        private String createEvent() throws IOException {


            String start_date_time=edt_start_date.getText().toString()+" "+startdate_time+" ";
            String end_date_time=edt_end_date.getText().toString()+" "+enddate_time+" ";

            String startDatetime11 =start_date_time;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ");
            System.out.println(startDatetime11);

            Date convertedDate11 = new Date();
            try {
                convertedDate11 = dateFormat.parse(startDatetime11);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            System.out.println(convertedDate11);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));

            String strUTCDatestart = dateFormatter.format(convertedDate11);

            System.out.println("starttime"+strUTCDatestart);


            String startDatetime12 =end_date_time;
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ");

            System.out.println(startDatetime12);

            Date convertedDate12 = new Date();
            try {
                convertedDate12 = dateFormat1.parse(startDatetime12);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            System.out.println(convertedDate12);
            SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            dateFormatter1.setTimeZone(TimeZone.getTimeZone("UTC"));

            String strUTCDateend = dateFormatter1.format(convertedDate12);

            System.out.println("endtime"+strUTCDateend);
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            com.google.api.services.calendar.Calendar service = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("R_D_Location Callendar")
                    .build();

            Event event = new Event()
                    .setSummary(edit_group_nm.getText().toString())
                    .setLocation("800 Howard St., San Francisco, CA 94103")
                    .setDescription("A chance to hear more about Google's developer products.");

            DateTime startDateTime = new DateTime(strUTCDatestart);
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime).setTimeZone(TimeZone.getTimeZone("" +
                            "").getID());
            event.setStart(start);

            DateTime endDateTime = new DateTime(strUTCDateend);
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime).setTimeZone(TimeZone.getDefault().getID());
            event.setEnd(end);

       */
/*     String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=1"};
            event.setRecurrence(Arrays.asList(recurrence));
*//*

            String calendarId = "primary";
            try {
                event = service.events().insert(calendarId, event).execute();
            } catch (Exception e) {
                Log.e("MainActivity","-----------------------"+e.getMessage());
            }

            System.out.printf("Event created: %s\n", event.getHtmlLink());
            Log.e("Event", "created : " + event.getHtmlLink());
            String eventStrings = "created : " + event.getHtmlLink();
            return eventStrings;
        }

    }


    */
/**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     *//*

    @Override
    public void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == RESULT_OK) {
                    refreshResults();
                } else {
                    isGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        credential.setSelectedAccountName(accountName);
                        SharedPreferences settings =
                              getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.commit();
                        refreshResults();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    //mStatusText.setText("Account unspecified.");
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    refreshResults();
                } else {
                    chooseAccount();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    */
/**
     * Attempt to get a set of data from the Google Calendar API to display. If the
     * email address isn't known yet, then call chooseAccount() method so the
     * user can pick an account.
     *//*

    private void refreshResults() {
        if (credential.getSelectedAccountName() == null) {
            chooseAccount();
        } else {
            if (isDeviceOnline()) {
               // new ApiAsyncTask(this).execute();


                new CreateApiAsyncTask().execute();

            } else {
              //  mStatusText.setText("No network connection available.");
            }
        }
    }


    private List<String> getDataFromApi() throws IOException {
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        List<String> eventStrings = new ArrayList<String>();
        Events events = mService.events().list("primary")
                .execute();
        List<Event> items = events.getItems();

        for (Event event : items) {
            DateTime start = event.getStart().getDateTime();
            if (start == null) {
                // All-day events don't have start times, so just use
                // the start date.
                start = event.getStart().getDate();
            }
            eventStrings.add(
                    String.format("%s (%s)", event.getSummary(), start));
        }

        return eventStrings;

    }
*/




    /**
     * Clear any existing Google Calendar API data from the TextView and update
     * the header message; called from background threads and async tasks
     * that need to update the UI (in the UI thread).
     */


    /**
     * Fill the data TextView with the given List of Strings; called from
     * background threads and async tasks that need to update the UI (in the
     * UI thread).
     * @param dataStrings a List of Strings to populate the main TextView with.
     */

    /**
     * Show a status message in the list header TextView; called from background
     * threads and async tasks that need to update the UI (in the UI thread).
     * @param message a String to display in the UI header TextView.
     */


    /**
     * Starts an activity in Google Play Services so the user can pick an
     * account.
     */
   /* private void chooseAccount() {
        startActivityForResult(
                credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }*/

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
   /* private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }*/

    /**
     * Check that Google Play services APK is installed and up to date. Will
     * launch an error dialog for the user to update Google Play Services if
     * possible.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS ) {
            return false;
        }
        return true;
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode,
                        getActivity(),
                        REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }


}