package com.cloudsinc.soulmobile.nativechatapplication.fragments.agenda;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudsinc.soulmobile.nativechatapplication.R;
import com.cloudsinc.soulmobile.nativechatapplication.adapters.EventListAdapter;
import com.cloudsinc.soulmobile.nativechatapplication.dialogs.AddEventAgentaDialog;
import com.cloudsinc.soulmobile.nativechatapplication.fragments.contacts.ContactListFragment;
import com.nc.developers.cloudscommunicator.GlobalClass;
import com.nc.developers.cloudscommunicator.Repository;
import com.nc.developers.cloudscommunicator.Subcription;
import com.nc.developers.cloudscommunicator.models.Event;
import com.nc.developers.cloudscommunicator.objects.ConstantsObjects;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class AgendaFragment extends Fragment {
    public AgendaFragment() {
    }

    static Context mcontext;
    private Observable<String> mobservable;
    private Observer<String> myObserver;
    private ArrayList<Event> eventList = new ArrayList<>();
    private RecyclerView eventlistRecyclerview;
    private EventListAdapter eventListAdapter;
    private static final String TAG=AgendaFragment.class.getSimpleName();
    @BindView(R.id.ll_no_dataevent)LinearLayout ll_no_data;
    @BindView(R.id.txt_no_dataevent)TextView txt_no_data;
    @BindView(R.id.prg_event)ProgressBar prg_contact;
    @BindView(R.id.agendaLayout)LinearLayout agendaLayout;
    @BindView(R.id.btn_addevent)Button btnadd_event;




    public static AgendaFragment newInstance() {
        return new AgendaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_agenda, container, false);
        ButterKnife.bind(this, view);
        mcontext = view.getContext();
        eventlistRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerView_eventList);
        setSubcriber();
        fetchEventFun();
        updateUi();


        btnadd_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                AddEventAgentaDialog dFragment = new AddEventAgentaDialog();
                dFragment.setCancelable(false);
                dFragment.show(fragmentManager,"Light");

            }
        });


        return view;
    }

    private void fetchEventFun() {
        Event.fetchEvents("2018-06-18T18:30:00.000Z",
                "2020-06-22T18:29:59.000Z",
                ConstantsObjects.ROLE_PERSONAL);

    }

    private void noData() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                agendaLayout.setVisibility(eventList.size()>0?View.VISIBLE:View.GONE);
                ll_no_data.setVisibility(eventList.size()>0?View.GONE:View.VISIBLE);
                txt_no_data.setVisibility(eventList.size()>0?View.GONE:View.VISIBLE);
                prg_contact.setVisibility(View.GONE);
            }
        },eventList.size()>0?0:4000);
    }
    private void updateUi(){
        try{

            eventList.clear();
            Repository repository=Repository.getRepository();
            eventList=(ArrayList<Event>)repository.getEventList();
            for(int h=0;h<eventList.size();h++){
                Event event=eventList.get(h);
                Log.i(TAG+"_eventName:",event.getCmlTitle()+" ..kk");
            }

            if(eventList!=null && eventList.size()>0){
                if (eventListAdapter==null){
                    RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getActivity());
                    eventlistRecyclerview.setLayoutManager(mLayoutManager);
                    eventlistRecyclerview.setItemAnimator(new DefaultItemAnimator());
                    eventListAdapter = new EventListAdapter(getActivity(),eventList,AgendaFragment.this);
                    eventlistRecyclerview.setAdapter(eventListAdapter);
                    float offsetPx = getResources().getDimension(R.dimen.px_1);
                    ContactListFragment.BottomOffsetDecoration bottomOffsetDecoration = new ContactListFragment.BottomOffsetDecoration((int) offsetPx);
                    eventlistRecyclerview.addItemDecoration(bottomOffsetDecoration);

                }
                else {
                    eventListAdapter.reload(eventList);
                    eventListAdapter.notifyDataSetChanged();
                }
            }
           noData();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void setSubcriber(){
        mobservable=Observable.create(
                new Observable.OnSubscribe<String>(){
                    @Override
                    public void call(Subscriber<? super String> sub){
                        sub.onNext("");
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
                if(string!=null){
                    Log.i(TAG+"_onNxt:",string+" ..kk");
                    if(string.equals("SUPERIMPOSE_EVENT_SERVER")){
                        Log.i(TAG+"onNext:",string);
                       updateUi();

                    }
                }
            }
        };
        Subcription s=new Subcription();
        s.setObservable(mobservable);
        s.setObserver(myObserver);
        GlobalClass.setCurrentSubcriberr(s);
    }

}