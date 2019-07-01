package com.nc.developers.cloudscommunicator.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.nc.developers.cloudscommunicator.database.ConstantsClass;
import com.nc.developers.cloudscommunicator.models.OfflineSync;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class OfflineSyncDao{
    @Insert
    public abstract Long insertObjectToEmit(OfflineSync offlineSync);

    /*@Query("DELETE from "+ConstantsClass.TBL_OFFLINESYNC
            +" where "+ConstantsClass.CML_TEMP_KEY_VAL+"=:cmlTempKeyval")
    public abstract int deleteObjToEmit(String cmlTempKeyval);*/

    @Query("SELECT * from "+ ConstantsClass.TBL_OFFLINESYNC)
    public abstract List<OfflineSync> getAllObjectsToEmit();

    @Delete
    public abstract int deleteObjToEmit(ArrayList<OfflineSync> offlineSyncArrayList);
}