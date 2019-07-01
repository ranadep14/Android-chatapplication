package com.nc.developers.cloudscommunicator.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.nc.developers.cloudscommunicator.database.ConstantsClass;
import com.nc.developers.cloudscommunicator.models.Calendar;
import java.util.List;

@Dao
public abstract class CalendarDao{

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    public abstract List<Long> insertCalendarSyncData(List<Calendar> calendarDataList);

    @Query("SELECT * from "+ ConstantsClass.TBL_CALENDAR+" where "
            +ConstantsClass.CML_SUB_CATEGORY+"=:personaProjectId")
    public abstract Calendar getCalendar(String personaProjectId);
}