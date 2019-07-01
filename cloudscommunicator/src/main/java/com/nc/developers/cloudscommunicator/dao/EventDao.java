package com.nc.developers.cloudscommunicator.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.nc.developers.cloudscommunicator.database.ConstantsClass;
import com.nc.developers.cloudscommunicator.models.Event;
import java.util.List;

@Dao
public abstract class EventDao{

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    public abstract List<Long> insertAllEvents(List<Event> eventList);

    @Query("SELECT * from "+ConstantsClass.TBL_CALENDAR_EVENT
            +" where "+ConstantsClass.EVENT_STATUS
            +"=''")
    public abstract List<Event> getEventList();

    @Query("SELECT * from "+ConstantsClass.TBL_CALENDAR_EVENT
            +" where "+ConstantsClass.EVENT_STATUS
            +"='event_request'")
    public abstract List<Event> getEventRequestList();

    @Query("SELECT * from "+ConstantsClass.TBL_CALENDAR_EVENT
            +" where "+ConstantsClass.CONVERSATION_ID+"=:conversation_ownerId")
    public abstract List<Event> getEventsFromConversation(String conversation_ownerId);

    @Query("delete from "+ConstantsClass.TBL_CALENDAR_EVENT
            +" where "+ConstantsClass.OWNER_ID+"=:eventOwnerId"
            +" AND "+ConstantsClass.EVENT_STATUS
            +"='event_request'")
    public abstract int deleteEventRequest(String eventOwnerId);
}