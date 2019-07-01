package com.nc.developers.cloudscommunicator.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.nc.developers.cloudscommunicator.database.ConstantsClass;
import com.nc.developers.cloudscommunicator.models.Login;

@Dao
public interface LoginDao{

    @Insert
    long insertLogin(Login login);

    @Update
    int updateLogin(Login login);

    @Query("SELECT * from " + ConstantsClass.TBL_LOGIN)
    Login getLoginData();

    @Query("UPDATE "+ConstantsClass.TBL_LOGIN+" SET "+ConstantsClass.IMAGE_PATH+"=:profileImagePath"
            +" where "+ConstantsClass.ID+"=1")
    void updateProfileImagePath(String profileImagePath);
}