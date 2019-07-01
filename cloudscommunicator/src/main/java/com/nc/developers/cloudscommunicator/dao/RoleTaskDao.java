package com.nc.developers.cloudscommunicator.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.nc.developers.cloudscommunicator.database.ConstantsClass;
import com.nc.developers.cloudscommunicator.models.RoleTask;

import java.util.List;

@Dao
public abstract class RoleTaskDao{
    @Insert(onConflict= OnConflictStrategy.REPLACE)
    public abstract List<Long> insertAllRoleTask(List<RoleTask> roleTasks);

    @Query("SELECT "+ ConstantsClass.COMMON_KEY_VAL+" from "+ConstantsClass.TBL_ROLE_TASK
            +" where "+ConstantsClass.CML_TITLE+"=:personaSelected")
    public abstract String getRoleTaskKeyval(String personaSelected);

    @Query("SELECT "+ConstantsClass.COMMON_KEY_VAL+" from "+ConstantsClass.TBL_ROLE_TASK+" where "
            +ConstantsClass.CML_TITLE+"<>'Unassigned'"
            +" AND "+ConstantsClass.CML_TITLE+"<>'Creator'"
            +" AND "+ConstantsClass.CML_TITLE+"<>'Admin'"
            +" AND "+ConstantsClass.CML_TITLE+"<>'Clouzer Admin'")
    public abstract List<String> getPersonaKeyVal();

    @Query("SELECT "+ConstantsClass.CML_TITLE+" from "+ConstantsClass.TBL_ROLE_TASK+" where "
            +ConstantsClass.CML_TITLE+"<>'Unassigned'"
            +" AND "+ConstantsClass.CML_TITLE+"<>'Creator'"
            +" AND "+ConstantsClass.CML_TITLE+"<>'Admin'"
            +" AND "+ConstantsClass.CML_TITLE+"<>'Clouzer Admin'")
    public abstract List<String> getPersonaList();
}