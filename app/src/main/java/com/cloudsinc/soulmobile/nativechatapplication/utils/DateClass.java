package com.cloudsinc.soulmobile.nativechatapplication.utils;

import android.util.Log;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateClass{

    //prajkta
    private static final String TAG=DateClass.class.getSimpleName();
    public static String convertAndGetDate(String dateInMilliSeconds){
        String modifiedDate="";
        if(dateInMilliSeconds!=null){
            DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
            long milliseconds=Long.parseLong(dateInMilliSeconds);
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(milliseconds);
            modifiedDate=dateFormat.format(calendar.getTime());
        }
        return modifiedDate;
    }

    public static String changeDateFormat(String time){
        String dateToReturn,timeString;
        dateToReturn=timeString="";
        DateFormat utcFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date=null;
        try{

            date=utcFormat.parse(time);
            DateFormat localFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            localFormat.setTimeZone(TimeZone.getDefault());
            dateToReturn=localFormat.format(date);
            Log.i("dateToReturn:",dateToReturn+" ..kk");
            /*String dateStringArr[]=dateToReturn.split(" ");
            if(dateStringArr!=null){
                if(dateStringArr.length==3){
                    String innerTimeString=dateStringArr[1];
                    if(innerTimeString!=null){
                        timeString=innerTimeString.substring(0,innerTimeString.indexOf('.'));
                        timeString+=":";
                        timeString+=innerTimeString.substring(3,5);
                        if(dateToReturn.contains("PM")){
                            timeString+=" PM";
                        }else{
                            timeString+=" AM";
                        }
                    }
                }
            }*/
        }catch(ParseException e){
            Log.e(TAG+"_dateFormat2:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_dateFormat1:",e.toString());
        }
        return dateToReturn;
    }
    public static String changeDateFormatToTime(String time){
        String dateToReturn,timeString;
        dateToReturn=timeString="";
        DateFormat utcFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat utcFormat1=new SimpleDateFormat("hh:mm a");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date=null;


        try{
            date=utcFormat.parse(time);
            timeString=utcFormat1.format(date);
        }catch(ParseException e){
            Log.e(TAG+"_dateFormat2:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_dateFormat1:",e.toString());
        }
        return timeString;
    }


    public static String changeDateFormatToTimeanddate(String time){
        String dateToReturn,timeString;
        dateToReturn=timeString="";
        DateFormat utcFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat utcFormat1=new SimpleDateFormat(" yyyy/MM/dd hh:mm a");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date=null;


        try{
            date=utcFormat.parse(time);
            timeString=utcFormat1.format(date);
        }catch(ParseException e){
            Log.e(TAG+"_dateFormat2:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_dateFormat1:",e.toString());
        }
        return timeString;
    }



    public static String changeDateFormatToTimeForOffline(String time){
        String dateToReturn,timeString;
        dateToReturn=timeString="";
        DateFormat utcFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat utcFormat1=new SimpleDateFormat("hh:mm a");
        Date date=null;
        try{
            date=utcFormat.parse(time);
            timeString=utcFormat1.format(date);
        }catch(ParseException e){
            Log.e(TAG+"_dateFormat2:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_dateFormat1:",e.toString());
        }
        return timeString;
    }

    public static String getTimeInMillis(String time){
        System.out.println("*************"+time);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String timeToReturn="";
        try{
            Date date=simpleDateFormat.parse(time);
            timeToReturn=String.valueOf(date.getTime());
        }catch(ParseException e){
            Log.e(TAG+"_TimeInMillis1:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_TimeInMillis2:",e.toString());
        }
        return timeToReturn;
    }

    public static String getMonthNameDateFormat(String inputDateFormat){
        final String OLD_FORMAT="dd/MM/yyyy";
        final String NEW_FORMAT="dd MMM yyyy";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(OLD_FORMAT);
        String timeToReturn="";
        try{
            Date date=simpleDateFormat.parse(inputDateFormat);
            simpleDateFormat.applyPattern(NEW_FORMAT);
            timeToReturn=simpleDateFormat.format(date);
        }catch(ParseException e){
            Log.e(TAG+"_MonthName1:",e.toString());
        }catch(Exception e){
            Log.e(TAG+"_MonthName2:",e.toString());
        }
        return timeToReturn;
    }
    public static String getMinuteSecondAmPmString(long msgArrivalTime){
        String am_pm,timeString;
        am_pm=timeString="";
        long totalSeconds,minutes,hours;
        totalSeconds=minutes=hours=0;
        totalSeconds=msgArrivalTime/1000;
        minutes=(totalSeconds/60)%60;
        hours=(totalSeconds/3600);
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(msgArrivalTime);
        if(calendar.get(Calendar.AM_PM)==Calendar.PM){
            am_pm="PM";
        }else{
            am_pm="AM";
        }
        timeString=String.valueOf(hours)+":"+String.valueOf(minutes)+" "+am_pm;
        return timeString;
    }
}