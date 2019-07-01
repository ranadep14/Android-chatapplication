package com.cloudsinc.soulmobile.nativechatapplication.utils;
import android.util.Log;
import com.cloudsinc.soulmobile.nativechatapplication.datamodels.ChatGroup;
import com.nc.developers.cloudscommunicator.models.Contact;
import com.nc.developers.cloudscommunicator.models.Conversation;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
public class SortingClass {
    public static ArrayList<ChatGroup> getSortedGrpList(ArrayList<ChatGroup> groupNameList){
        try {
            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
            Collections.sort(groupNameList, new Comparator<ChatGroup>() {
                @Override
                public int compare(ChatGroup obj1, ChatGroup obj2) {
                    String timeInfo1 = "", timeInfo2 = "", timeInMillis1 = "", timeInMillis2 = "";
                    String cmlTime="";
                    String cmlTime1="";
                    if (obj1.getMsgTime().contains("+0000")){
                        //Log.d("cmlTime",obj1.getMsgTime().split("\\+")[0]);
                        Log.d("cmlTimeSSS",""+obj1.getMsgTime().toString().replace("+0000","Z"));
                        cmlTime = obj1.getMsgTime().toString().replace("+0000","Z");
                    }
                    else {
                        //Log.d("cmlTime",obj1.getMsgTime());
                        cmlTime=obj1.getMsgTime();
                    }
                    timeInfo1 = DateClass.changeDateFormat(cmlTime);

                    if (obj2.getMsgTime().contains("+0000")){
                        //Log.d("cmlTime1",obj2.getMsgTime());
                        Log.d("cmlTimeSSS1",""+obj2.getMsgTime().toString().replace("+0000","Z"));
                        cmlTime1 = obj2.getMsgTime().toString().replace("+0000","Z");
                    }
                    else {
                        //Log.d("cmlTime1",obj2.getMsgTime());
                        cmlTime1 = obj2.getMsgTime();
                    }
                    timeInfo2 = DateClass.changeDateFormat(cmlTime1);

                    if(timeInfo1!="" && !timeInfo1.isEmpty() && timeInfo2!="" && !timeInfo2.isEmpty()){
                        timeInMillis1 = DateClass.getTimeInMillis(timeInfo1);
                        timeInMillis2 = DateClass.getTimeInMillis(timeInfo2);
                    }
                    return timeInMillis1.compareTo(timeInMillis2);
                }
            });
            Collections.reverse(groupNameList);
        } catch (Exception e) {
            Log.e("SortingException: ", ""+e.toString());
        }
        return groupNameList;
    }

    public static ArrayList<Conversation> getSortedConvList(ArrayList<Conversation> conversationsList){
        try {
            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
            Collections.sort(conversationsList, new Comparator<Conversation>() {
                @Override
                public int compare(Conversation obj1, Conversation obj2) {
                      String timeInfo1 = "", timeInfo2 = "";
                      String cmlTime="";
                       String cmlTime1="";
                        int compare=0;
                        Log.d("cmlTime",obj1.getLastModifiedOn());
                        cmlTime=obj1.getLastModifiedOn();

                        Log.d("cmlTime1",obj2.getLastModifiedOn());
                        cmlTime1 = obj2.getLastModifiedOn();

                    timeInfo2 = DateClass.changeDateFormat(cmlTime1);
                    timeInfo1 = DateClass.changeDateFormat(cmlTime);

                    System.out.println("--------------------------"+timeInfo1+"-----------------------"+timeInfo2);
                   Long timeInMillis1 = Long.parseLong(DateClass.getTimeInMillis(timeInfo1));
                   Long  timeInMillis2 = Long.parseLong(DateClass.getTimeInMillis(timeInfo2));

                    try{

                        if(timeInMillis1>timeInMillis2){
                            compare=1;
                        }
                        if(timeInMillis1<timeInMillis2){
                            compare=-1;
                        }
                        if(timeInMillis1==timeInMillis2){
                            compare=0;
                        }
                    }catch(Exception e){
                        Log.e("expn_srt_logic1:",e.toString());
                    }
                    return compare;

                }
            });
            Collections.reverse(conversationsList);
        } catch (Exception e) {
            Log.e("SortingException: ", ""+e.toString());
        }
        return conversationsList;
    }
    public static ArrayList<Contact> getSortedContactList(ArrayList<Contact> contactNameList){
        try {
            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
            /*Collections.sort(contactNameList, new Comparator<Contact>() {
                @Override
                public int compare(Contact obj1, Contact obj2) {

                }
            });*/
            Collections.sort(contactNameList, new Comparator<Contact>() {
                @Override
                public int compare(Contact o1, Contact o2) {
                    return o1.getOfficialEmail().compareTo(o2.getOfficialEmail());
                }
            });
            //Collections.reverse(groupNameList);
        } catch (Exception e) {
            Log.e("SortingException: ", ""+e.toString());
        }
        return contactNameList;
    }
}