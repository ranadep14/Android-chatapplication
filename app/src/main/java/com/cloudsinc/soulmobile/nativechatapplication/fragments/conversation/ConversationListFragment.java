package com.cloudsinc.soulmobile.nativechatapplication.fragments.conversation;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudsinc.soulmobile.nativechatapplication.App;
import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.ChatListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.ConversationListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.datamodels.ChatGroup;
import com.cloudsinc.soulmobile.nativechatapplication.dialogs.CustomDialog;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.FragmentHomeScreen;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.chat.ChatListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactListFragment;
import com.cloudsinc.soulmobile.nativechatapplication.interfaces.IOnBackPressed;
import com.cloudsinc.soulmobile.nativechatapplication.utils.InternetConnectionChecker;
import com.cloudsinc.soulmobile.nativechatapplication.utils.PaginationScrollListener;
import com.cloudsinc.soulmobile.nativechatapplication.utils.ReplaceFragment;
import com.cloudsinc.soulmobile.nativechatapplication.utils.SortingClass;
import com.cloudsinc.soulmobile.nativechatapplication.utils.common;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.Subcription;
import com.nc.developers.cloudscommunicator.models.Conversation;
import com.nc.developers.cloudscommunicator.models.UserURM;
import com.nc.developers.cloudscommunicator.socket.JsonParserClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

import static com.cloudsinc.soulmobile.nativechatapplication.adapters.MailListAdapter.templst;

/**
 * This Fragment is used to display Conversation List.
 * @author Prajakta Patil
 * @version 1.0
 * @since 5.11.2018
 */
public class ConversationListFragment extends Fragment {
    private Observable<String> mobservable=null;
    private Observer<String> myObserver=null;

    public static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;

    private View view;
    private Context context;
    ConversationListAdapter conAdapter;
    public static boolean isGrpUpdate = false;
    public static int unreadCount=0;
    private ArrayList<Conversation> temGrpList = new ArrayList<Conversation>();
    private ArrayList<Conversation> groupNmList = new ArrayList<Conversation>();
    private ArrayList<ChatGroup> groupNameList = new ArrayList<ChatGroup>();
    private ArrayList<ChatGroup> visibleGrpList = new ArrayList<ChatGroup>();
    private ArrayList<ChatGroup> updatedGrpItem = new ArrayList<ChatGroup>();
    public static boolean isConversationSearch = false;
    public static boolean isConversationSearchVisible = false;
    private static int counterDelete=0,counterInvitee=0;
    public static boolean isArchive=false;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.btn_retry) TextView btnRetry;
    @BindView(R.id.rec_coversation_list)RecyclerView rec_coversation_list;
    @BindView(R.id.fab_add_contact)FloatingActionButton fab_add_contact;
    @BindView(R.id.ll_no_data)LinearLayout ll_no_data;
    @BindView(R.id.prg_conversation)ProgressBar prg_conversation;
    @BindView(R.id.img_back)ImageView img_back;
    @BindView(R.id.ll_back)LinearLayout ll_back;
    @BindView(R.id.ll_clear_txt)LinearLayout ll_clear_txt;
    @BindView(R.id.img_clear_txt)ImageView img_clear_txt;
    @BindView(R.id.img_left_icon)ImageView img_left_icon;
    @BindView(R.id.img_right_icon1)ImageView img_right_icon1;
    @BindView(R.id.img_right_icon2)ImageView img_right_icon2;
    @BindView(R.id.li_img_delete)LinearLayout li_img_delete;
    @BindView(R.id.img_right_icon3)ImageView img_right_icon3;
    @BindView(R.id.txt_no_data)TextView txt_no_data;
    private static LinearLayout lin_search_bar;
    private static RelativeLayout lin_action_bar;
    private static EditText edit_search;
    private static TextView txt_action_title;
    static Context mcontext;
    private static LinearLayout ll_group_list;
    private int flag=1;
    private static final String TAG=ConversationListFragment.class.getSimpleName();
    public static ConversationListFragment newInstance() {
        return new ConversationListFragment();
    }
    @Override
    public void onResume(){
        super.onResume();
        conAdapter = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_conversation_list, container, false);
        context=view.getContext();
        ButterKnife.bind(this,view);
        mcontext=view.getContext();
        GlobalClass.setCurrentSubcriberr(null);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_conversation);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        li_img_delete.setVisibility(View.VISIBLE);

        prg_conversation.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(mcontext, R.color.colorPrimary), PorterDuff.Mode.SRC_IN );

        groupNameList = App.getConversationList();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                itemCount = 0;
                currentPage = PAGE_START;
                isLastPage = false;
                conAdapter.clear();
                loadMorConversations();

            }
        });

        img_right_icon1.setVisibility(View.GONE);
        img_right_icon1.setImageResource(R.drawable.ic_edit);
        lin_search_bar = (LinearLayout)view.findViewById(R.id.lin_search_bar);
        ll_group_list = (LinearLayout)view.findViewById(R.id.ll_group_list);
        lin_action_bar = (RelativeLayout) view.findViewById(R.id.lin_action_bar);
        txt_action_title = (TextView) view.findViewById(R.id.txt_action_title);
        FragmentHomeScreen.actionTabsVis();
        edit_search = (EditText) view.findViewById(R.id.edit_search);
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        setSubcriber();
        featchallConversationCall();


        //ankita code
        Repository repo= Repository.getRepository();
        ArrayList<Conversation>newgrplist= (ArrayList<Conversation>)repo.getAllConversations("unarchive");

        Log.i(TAG,"ssssssssssss"+newgrplist.size());
        /*if(newgrplist.size()==0) {
            new Handler().postDelayed(new Runnable() {
                                          @Override
                                          public void run() {
                                              updateUi("CHAT_FILTER_SERVER");
                                              flag = 1;
                                          }
                                      },
                    3000);
        }
        else
        {
            updateUi("CHAT_FILTER_SERVER");
        }*/
        if(newgrplist.size()==0){
            noData();
        }

        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    /**
     * this method is used for Display All available Conversations.
     * @return void
     */

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isArchive=false;
    }

    private void featchallConversationCall(){
        Conversation conversation=new Conversation();
        conversation.getJSONObjectFetchAllConversations("unarchive","");
    }

    /**
     * this method is used for render data on UI.
     * @return String
     */
    //**
    private void updateUi(String key){
        try{

            String isArc = isArchive?"archive":"unarchive";
            groupNmList.clear();
            Repository repo= Repository.getRepository();
            ArrayList<Conversation>newgrplist= (ArrayList<Conversation>)repo.getAllConversations(isArc);
            // groupNmList=(ArrayList<Conversation>)repo.getAllConversations();
            groupNmList= SortingClass.getSortedConvList(newgrplist);
            Log.d("newgrplistsize", String.valueOf(newgrplist.size()));

            if (groupNmList.size()>0){
                itemCount = groupNmList.size()>10?10:groupNmList.size();
                temGrpList.clear();


                for (int i=0;i<(groupNmList.size()>10?10:groupNmList.size());i++)
                {  temGrpList.add(groupNmList.get(i));}

                Log.d("tempGrpSize", temGrpList.size()+" "+groupNmList.size());
                if (conAdapter==null){
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    rec_coversation_list.setLayoutManager(mLayoutManager);
                    rec_coversation_list.setItemAnimator(new DefaultItemAnimator());
                    rec_coversation_list.setNestedScrollingEnabled(false);
                    float offsetPx = getResources().getDimension(R.dimen.px_80);
                    ContactListFragment.BottomOffsetDecoration bottomOffsetDecoration = new ContactListFragment.BottomOffsetDecoration((int) offsetPx);
                    rec_coversation_list.addItemDecoration(bottomOffsetDecoration);
                    conAdapter = new ConversationListAdapter(temGrpList,getActivity(),ConversationListFragment.this);
                    rec_coversation_list.setAdapter(conAdapter);
                    setMsgCount();

                    itemCount = 0;
                    currentPage = PAGE_START;
                    isLastPage = false;
                    conAdapter.clear();
                    loadMorConversations();


                    //rec_coversation_list.addOnScrollListener(null);
                    rec_coversation_list.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
                        @Override
                        protected void loadMoreItems() {
                            //itemCount++;
                            Log.d("Scrolling", "Scrolling"+itemCount);
                            isLoading = true;
                            currentPage++;
                            loadMorConversations();

                        }

                        @Override
                        public boolean isLastPage() {
                            return isLastPage;
                        }

                        @Override
                        public boolean isLoading() {
                            return isLoading;
                        }
                    });
                }else {
                    String keyVal = null;
                    String action = key;
                    Log.d(TAG +"grpAdapterStatus","Notify called");
                    //conAdapter.reload(groupNmList);
                    conAdapter.reload(temGrpList);
                    conAdapter.notifyDataChanged();
                    if (keyVal!=null && action!=null){
                        updatedGrpItem.clear();
                        updatedGrpItem = App.getConversation(keyVal);
                        switch (action) {
                            case "CHAT_FILTER_SERVER":
                                conAdapter.reload(temGrpList);
                                conAdapter.notifyItemInserted(0);
                                break;
                        }

                    }
                    setMsgCount();
                }

            }
            noData();
        }catch (Exception e){
            Log.e(TAG +"exception: ", e.toString()+"");
        }
    }

    private void noData() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ll_group_list.setVisibility(groupNmList.size() > 0 ? View.VISIBLE : View.GONE);
                ll_no_data.setVisibility(groupNmList.size() > 0 ? View.GONE : View.VISIBLE);
                txt_no_data.setVisibility(groupNmList.size() > 0 ? View.GONE : View.VISIBLE);
                txt_no_data.setText("No Conversation Available");
                prg_conversation.setVisibility(View.GONE);
                btnRetry.setVisibility(View.GONE);
            }
        },groupNmList.size()>0?0:0);
    }

    /**
     * this method is used for set unread msg Count.
     * @return void
     */
    private void setMsgCount(){
        unreadCount = 0;
        for(int i=0;i<groupNameList.size();i++){
            unreadCount += groupNameList.get(i).getUnreadCount();}
        Log.d(TAG +"unreadCnt", ""+String.valueOf(unreadCount));
        ConversationLandingFragment.setMsgCount(unreadCount);
    }
    @Optional
    @OnClick({R.id.fab_add_contact, R.id.ll_back, R.id.ll_clear_txt, R.id.img_left_icon, R.id.img_right_icon1, R.id.img_right_icon2, R.id.img_right_icon3})
    public void onSelectDeselect(View view) {
        switch (view.getId()) {
            case R.id.fab_add_contact:
                isGrpUpdate = false;
                App.getSearchSelectedUserArr().clear();
                //selected_user_arr.clear();
                templst.clear();
                ChatListFragment.fromfrgament="";
                ReplaceFragment.replaceFragment(ConversationListFragment.this, R.id.frame_frag_container, new CreateConversationFragment(), true);


                break;
            case R.id.ll_back:
                if (ConversationListFragment.isConversationSearch) {
                    isConversationSearchVisible = false;
                    common.hideKeyboard(getActivity());
                    FragmentHomeScreen.actinBarVisibility(true);
                    ConversationLandingFragment.conTabsVisibility(true);
                    ConversationListFragment.conSerchBarVisiblity(false);
                    edit_search.setText("");
                }
                break;
            case R.id.img_left_icon:
                ConversationListFragment.actionBarVisiblity(false, 0);
                FragmentHomeScreen.actinBarVisibility(true);
                ConversationLandingFragment.conTabsVisibility(true);
                conAdapter.resetFlag();
                break;
            case R.id.img_right_icon2:
                CustomDialog.showActionDialog(mcontext,"Confirm","Do you want to  delete this conversation?","conversation_delete",true);
                //deleteConversationDialog();
                break;
            case R.id.ll_clear_txt:
                edit_search.setText("");
                break;
            case R.id.img_right_icon1:
                isGrpUpdate = true;
                Log.d(TAG +"group_json",String.valueOf(GlobalClass.getGroupJson()));
                Bundle bundle = new Bundle();
                bundle.putString("group_json", String.valueOf(GlobalClass.getGroupJson()));
                Fragment fragment = new CreateConversationFragment();
                fragment.setArguments(bundle);
                ReplaceFragment.replaceFragment(ConversationListFragment.this, R.id.frame_frag_container, fragment, true);
                break;
            case R.id.img_right_icon3:
                MyOptionMenu();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void MyOptionMenu(){
        final PopupMenu popup = new PopupMenu(getActivity(), img_right_icon3);
        popup.getMenuInflater().inflate(R.menu.main, popup.getMenu());
        popup.getMenu().findItem(R.id.item1).setTitle(Html.fromHtml("<font color='#000000'>View Contact</font>"));
        popup.getMenu().findItem(R.id.item2).setTitle(Html.fromHtml("<font color='#000000'>Archive Chat</font>"));
        popup.getMenu().findItem(R.id.item3).setTitle(Html.fromHtml("<font color='#000000'>settings </font>"));
        popup.getMenu().findItem(R.id.item4).setTitle(Html.fromHtml("<font color='#000000'>Mark as unread</font>"));
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                        break;
                    case R.id.item2:
                        Toast.makeText(mcontext,"ssssssssssss",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        popup.show();
    }
    private void filter(String text) {

        if (groupNmList.size()>0){
            ArrayList<Conversation> temp = new ArrayList();
            for(Conversation d: groupNmList){
                try {
                    JSONObject grpObj = new JSONObject(d.getCompleteData());
                    if((grpObj.getString("CML_TITLE").toLowerCase()).contains(text.toLowerCase())){
                        temp.add(d);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            conAdapter.filterList(temp);

            if(temp.size()>0){
                prg_conversation.setVisibility(View.GONE);
                ll_group_list.setVisibility(View.VISIBLE);
                ll_no_data.setVisibility(View.GONE);
                txt_no_data.setVisibility(View.GONE);
                txt_no_data.setText("");

            }
            else {
                prg_conversation.setVisibility(View.GONE);
                ll_group_list.setVisibility(View.GONE);
                ll_no_data.setVisibility(View.VISIBLE);
                txt_no_data.setVisibility(View.VISIBLE);
                txt_no_data.setText("No Result Found");
            }

        }
    }

    /**
     * this method is used for delete Conversation.
     * @return void
     */
    public static void deleteConvFunction(){
        CustomDialog.startProgressDialog(mcontext);
        ll_group_list.setVisibility(View.GONE);
        ConversationListFragment.actionBarVisiblity(false,0);
        FragmentHomeScreen.actinBarVisibility(true);
        ConversationLandingFragment.conTabsVisibility(true);
        // conAdapter.resetFlag();
        Log.i(TAG+"conversation_to_dlt:", ConversationListAdapter.selected_conversations.getCmlTitle() + " ..kk");

        try {

            if (!ConversationListAdapter.isOnetoOne){
                try {

                    Conversation conversation=new Conversation();
                    conversation.getJSONObjectDeleteConversations(ConversationListAdapter.selectConvlist);


                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }else {
                Log.i(TAG +"contactName:", ConversationListAdapter.selectConvlist.get(0).getCmlTitle() + " ..kk");
                deleteOneToOneFun();
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * this method is used for delete one To One Conversation.
     * @return void
     */
    private static void deleteOneToOneFun(){
        Conversation conversation=new Conversation();
        conversation.getJsonObjectDeleteOneToOneConversation(ConversationListAdapter.selectConvlist);

    }

    private void setSubcriber(){
        mobservable = Observable.create(
                new Observable.OnSubscribe<String>(){
                    @Override
                    public void call(Subscriber<? super String> sub){
                        sub.onNext("" );
                        sub.onCompleted();
                    }
                }
        );
        myObserver = new Observer<String>(){
            @Override
            public void onCompleted(){

            }

            @Override
            public void onError(Throwable e){

            }

            @Override
            public void onNext(String string){
                Log.i(TAG +"onNext_string:",string+"");
                if(string!=null){
                    if(string.equals("CHAT_FILTER_SERVER") || string.equals("no_conversation") || string.equals("delete_conversation")
                            || string.equals("create_conversation")||string.equals("broadMsgStatus")
                            ||(string.equals("conversation_update"))){
                        Log.d(TAG +"chatString",string);

                         if(ConversationListAdapter.selectedCount<=0){
                             updateUi(string);

                         }
                        CustomDialog.stopProgressDialog();
                    }

                }
            }
        };
        Subcription s=new Subcription();
        s.setObservable(mobservable);
        s.setObserver(myObserver);
        GlobalClass.setCurrentSubcriberr(s);
    }

    public static void conSerchBarVisiblity(boolean b){
        lin_search_bar.setVisibility(b?View.VISIBLE:View.GONE);
        if (b) common.showKeyboard((Activity) mcontext,edit_search);
    }
    public static void actionBarVisiblity(boolean b,int selCount){

        lin_action_bar.setVisibility(b?View.VISIBLE:View.GONE);
        txt_action_title.setText(""+selCount+" Selected");
    }


    private void loadMorConversations() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                int endSize = itemCount+10>groupNmList.size()?groupNmList.size():itemCount+10;
                int startSize = itemCount;
                /*if (endSize%50==0){
                    Conversation conversation=new Conversation();
                    conversation.getJSONObjectFetchAllConversations("unarchive","next");

                }*/

                    for (int i = startSize; i < endSize; i++) {
                        itemCount++;
                        Log.d(TAG, "ItemCnt: " + itemCount);
                        temGrpList.add(groupNmList.get(i));

                    }
                    if (conAdapter != null) {
                        if (currentPage != PAGE_START) conAdapter.removeLoading();
                        conAdapter.addAll(temGrpList);
                        conAdapter.notifyDataSetChanged();
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                    //}
                    if (currentPage < totalPage) {
                        conAdapter.addLoading();
                    } else isLastPage = true;
                    isLoading = false;


            }
        }, 1500);

    }

}