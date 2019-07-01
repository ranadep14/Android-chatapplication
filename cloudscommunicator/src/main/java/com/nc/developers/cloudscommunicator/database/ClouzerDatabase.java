package com.nc.developers.cloudscommunicator.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.nc.developers.cloudscommunicator.dao.CalendarDao;
import com.nc.developers.cloudscommunicator.dao.ContactDao;
import com.nc.developers.cloudscommunicator.dao.ConversationDao;
import com.nc.developers.cloudscommunicator.dao.EventDao;
import com.nc.developers.cloudscommunicator.dao.InviteeDao;
import com.nc.developers.cloudscommunicator.dao.LoginDao;
import com.nc.developers.cloudscommunicator.dao.MessagesDao;
import com.nc.developers.cloudscommunicator.dao.OfflineSyncDao;
import com.nc.developers.cloudscommunicator.dao.RoleTaskDao;
import com.nc.developers.cloudscommunicator.dao.UserURMDao;
import com.nc.developers.cloudscommunicator.models.Calendar;
import com.nc.developers.cloudscommunicator.models.Contact;
import com.nc.developers.cloudscommunicator.models.Conversation;
import com.nc.developers.cloudscommunicator.models.Event;
import com.nc.developers.cloudscommunicator.models.Invitee;
import com.nc.developers.cloudscommunicator.models.Login;
import com.nc.developers.cloudscommunicator.models.Messages;
import com.nc.developers.cloudscommunicator.models.OfflineSync;
import com.nc.developers.cloudscommunicator.models.RoleTask;
import com.nc.developers.cloudscommunicator.models.UserURM;

@Database(entities={RoleTask.class,UserURM.class,Login.class,Contact.class,
        Conversation.class,Messages.class,Invitee.class,OfflineSync.class,
        Event.class,Calendar.class},version=1,exportSchema=false)
public abstract class ClouzerDatabase extends RoomDatabase{

    public abstract RoleTaskDao roleTaskDao();
    public abstract UserURMDao userURMDao();
    public abstract LoginDao loginDao();
    public abstract ContactDao contactDao();
    public abstract ConversationDao conversationDao();
    public abstract MessagesDao messagesDao();
    public abstract InviteeDao inviteeDao();
    public abstract OfflineSyncDao offlineSyncDao();
    public abstract EventDao eventDao();
    public abstract CalendarDao calendarDao();

    private static volatile ClouzerDatabase INSTANCE;

    public static ClouzerDatabase getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized(ClouzerDatabase.class){
                if(INSTANCE==null){
                    INSTANCE=Room.databaseBuilder(
                            context.getApplicationContext(),
                            ClouzerDatabase.class,
                            "clouzer_chat_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}