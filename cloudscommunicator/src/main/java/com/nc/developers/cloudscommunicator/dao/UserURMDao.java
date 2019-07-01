package com.nc.developers.cloudscommunicator.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.nc.developers.cloudscommunicator.database.ConstantsClass;
import com.nc.developers.cloudscommunicator.models.UserURM;
import java.util.List;

@Dao
public abstract class UserURMDao{
    @Insert(onConflict=OnConflictStrategy.REPLACE)
    public abstract List<Long> insertAllUserURMS(List<UserURM> userURMS);

    @Query("SELECT * from "+ConstantsClass.TBL_USER_URM)
    public abstract List<UserURM> getUserURMList();

    @Query("SELECT "+ConstantsClass.URM_PROJECT_ID+" from "+ConstantsClass.TBL_USER_URM
            +" where "+ConstantsClass.CML_ROLE_ID+"=:keyVal")
    public abstract String getPersonaProjectId(String keyVal);
}